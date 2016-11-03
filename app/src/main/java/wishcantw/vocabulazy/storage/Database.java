package wishcantw.vocabulazy.storage;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.application.GlobalVariable;
import wishcantw.vocabulazy.storage.databaseObjects.Note;
import wishcantw.vocabulazy.storage.databaseObjects.OptionSettings;
import wishcantw.vocabulazy.storage.databaseObjects.Textbook;
import wishcantw.vocabulazy.storage.databaseObjects.Vocabulary;
import wishcantw.vocabulazy.utility.Logger;

public class Database {
    public static final String TAG = Database.class.getSimpleName();

    public static final String FILENAME_NOTE = "note";
    public static final String FILENAME_OPTION = "optionSetting";

    private GlobalVariable mGlobalVariable;

    private static Database database = new Database();

    private ArrayList<Vocabulary> mVocabularies = new ArrayList<>();
    private ArrayList<Textbook> mTextbooks = new ArrayList<>();
    private ArrayList<Note> mNotes = new ArrayList<>();

    private static final int MAXIMUM_LIST_SIZE = 50;

    public static Database getInstance() {
        return database;
    }

    public void init(Context context) {
        loadFiles(context);
    }

    public void loadFiles(Context context) {
        mGlobalVariable = ((GlobalVariable) context);
        try {
            mVocabularies = load(Vocabulary[].class, context.getResources().openRawResource(R.raw.vocabulary));
            mTextbooks = load(Textbook[].class, context.getResources().openRawResource(R.raw.textbook));
            mNotes = load(Note[].class, context.openFileInput(FILENAME_NOTE));
            mGlobalVariable.optionSettings = load(OptionSettings[].class, context.openFileInput(FILENAME_OPTION));
        } catch (FileNotFoundException fnfe) {
            mNotes = load(Note[].class, context.getResources().openRawResource(R.raw.note));
            mGlobalVariable.optionSettings = load(OptionSettings[].class, context.getResources().openRawResource(R.raw.option));
        }
    }

    public synchronized void writeToFile(Context context) {
        Logger.d(TAG, "Write database");
        write(context, FILENAME_NOTE, mNotes.toArray());
        write(context, FILENAME_OPTION, mGlobalVariable.optionSettings.toArray());
    }

    public ArrayList<Textbook> getTextbooks() {
        return mTextbooks;
    }

    public ArrayList<Note> getNotes() {
        return mNotes;
    }

    public String getTextbookTitle(int bookIndex) {
        return mTextbooks.get(bookIndex).getTextbookTitle();
    }

    public String getLessonTitle(int bookIndex, int lessonIndex) {
        if (bookIndex < 0) return getNoteTitle(lessonIndex);

        if (mTextbooks == null ||
                bookIndex >= mTextbooks.size() ||
                mTextbooks.get(bookIndex).getTextbookContent() == null ||
                lessonIndex >= mTextbooks.get(bookIndex).getTextbookContent().size()) {
            return "";
        }
        return mTextbooks.get(bookIndex).getTextbookContent().get(lessonIndex).getLessonTitle();
    }

    public String getNoteTitle(int noteIndex) {
        return (mNotes == null || noteIndex >= mNotes.size())
                ? ""
                : mNotes.get(noteIndex).getNoteTitle();
    }

    public String getTextbookType(int bookIndex) {
        if (mTextbooks == null || mTextbooks.isEmpty() || bookIndex < 0 || bookIndex >= mTextbooks.size()) {
            return "Textbook";
        }
        return mTextbooks.get(bookIndex).getTextbookType();
    }

    public int getNumOfLesson(int bookIndex) {
        if (bookIndex < 0 && mNotes != null) return mNotes.size();
        if (mTextbooks == null || mTextbooks.isEmpty() || bookIndex >= mTextbooks.size()
                || mTextbooks.get(bookIndex) == null || mTextbooks.get(bookIndex).getTextbookContent() == null) {
            return 0;
        }
        return mTextbooks.get(bookIndex).getTextbookContent().size();
    }

    public ArrayList<Integer> getContentIds(int bookIndex, int lessonIndex) {
        if (bookIndex < 0) return getNoteContent(lessonIndex);

        if (mTextbooks == null ||
                bookIndex >= mTextbooks.size() ||
                mTextbooks.get(bookIndex).getTextbookContent() == null ||
                lessonIndex >= mTextbooks.get(bookIndex).getTextbookContent().size()) {
            return new ArrayList<>();
        }
        return mTextbooks.get(bookIndex).getTextbookContent().get(lessonIndex).getLessonContent();
    }

    public ArrayList<Integer> getNoteContent(int noteIndex) {
        return (mNotes == null || noteIndex >= mNotes.size())
                ? new ArrayList<Integer>()
                : mNotes.get(noteIndex).getNoteContent();
    }

    public ArrayList<String> getNoteNames() {
        ArrayList<String> noteNames = new ArrayList<>();
        if (mNotes != null) {
            for (Note note : mNotes) {
                noteNames.add(note.getNoteTitle());
            }
        }
        return noteNames;
    }

    public ArrayList<Vocabulary> getVocabulariesByIDs(@NonNull final ArrayList<Integer> vocIDs) {
        ArrayList<Vocabulary> vocabularies = new ArrayList<>();
        if (mVocabularies != null) {
            for (int index = 0; index < vocIDs.size(); index++) {
                for (int index2 = 0; index2 < mVocabularies.size(); index2++) {
                    Vocabulary vocabulary = mVocabularies.get(index2);
                    if (vocIDs.get(index).equals(vocabulary.getId())) {
                        vocabularies.add(vocabulary);
                    }
                }
            }
        }
        return vocabularies;
    }

    public ArrayList<OptionSettings> getOptionSettings() {
        return mGlobalVariable.optionSettings;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /* Search operation */
    public ArrayList<Vocabulary> readSuggestVocabularyBySpell(String queryString) {
        ArrayList<Vocabulary> resultVocabularies = new ArrayList<>();
        if (mVocabularies != null) {
            for (int index = 0; index < mVocabularies.size(); index++) {
                Vocabulary vocabulary = mVocabularies.get(index);
                String spell = vocabulary.getSpell();
                int queryStringLength = queryString.length();
                if (spell.length() < queryStringLength) {
                    continue;
                }
                if (spell.substring(0, queryStringLength).equals(queryString)) {
                    resultVocabularies.add(vocabulary);
                    if (resultVocabularies.size() > MAXIMUM_LIST_SIZE) {
                        break;
                    }
                }
            }
        }
        return resultVocabularies;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /* Note operations */
    public void createNewNote(String name) {
        if (mNotes == null) mNotes = new ArrayList<>();
        int index = mNotes.size();
        mNotes.add(index, new Note(index, name, new ArrayList<Integer>()));
    }

    /**
     * Add vocabulary to a note.
     *
     * @param vocId the id of the vocabulary that will be added.
     * @param noteIndex the index of the note
     */
    public void addVocToNote(int vocId, int noteIndex) {
        // check noteIndex validity
        if (noteIndex >= mNotes.size() || noteIndex == -1) {
            return;
        }

        // if the selected note doesn't have the vocabulary, then add the vocabulary to note
        ArrayList<Integer> content = mNotes.get(noteIndex).getNoteContent();
        if (!content.contains(vocId)) {
            content.add(vocId);
        }
    }

    public void renameNoteAt(int noteIndex, String name) {
        if (mNotes == null || noteIndex == -1
                || noteIndex >= mNotes.size() || mNotes.get(noteIndex) == null) {
            return;
        }
        int id = mNotes.get(noteIndex).getNoteId();
        renameNote(mNotes, id, name);
    }

    private void renameNote(@NonNull ArrayList<Note> notes, int noteId, String newName) {
        for (int index = 0; index < notes.size(); index++) {
            Note note = notes.get(index);
            if (noteId == note.getNoteId()) {
                note.setNoteTitle(newName);
                return;
            }
        }
    }

    public void deleteNoteAt(int noteIndex) {
        if (mNotes == null|| noteIndex == -1
                || noteIndex >= mNotes.size() || mNotes.get(noteIndex) == null) {
            return;
        }
        int id = mNotes.get(noteIndex).getNoteId();
        deleteNote(mNotes, id);
    }

    private void deleteNote(@NonNull ArrayList<Note> notes, int noteId) {
        Note noteToBeDelete = null;
        for (int index = 0; index < notes.size(); index++) {
            Note note = notes.get(index);
            if (noteId == note.getNoteId()) {
                noteToBeDelete = note;
            }
        }

        if (noteToBeDelete == null)
            return;

        notes.remove(noteToBeDelete);

        // refresh IDs
        for (int index = 0; index < notes.size(); index++) {
            Note note = notes.get(index);
            note.setNoteId(index);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /* IO operations */
    private <T> ArrayList<T> load(Class<T[]> classOfT, InputStream inputStream) {

        StringBuilder builder = new StringBuilder();

        try {
            String readLine;
            BufferedReader bfdReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            while ((readLine = bfdReader.readLine()) != null) {
                builder.append(readLine);
            }

            bfdReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        T[] tArray = new Gson().fromJson(builder.toString(), classOfT);
        return new ArrayList<>(Arrays.asList(tArray));
    }

    private <T> void write(Context context, String filename, T[] array) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(new Gson().toJson(array).getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
