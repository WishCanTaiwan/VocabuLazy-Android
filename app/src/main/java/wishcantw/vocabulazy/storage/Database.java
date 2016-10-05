package wishcantw.vocabulazy.storage;

import android.content.Context;

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

    private ArrayList<Vocabulary> mVocabularies;
    private ArrayList<Textbook> mTextbooks;
    private ArrayList<Note> mNotes;

    private static final int MAXIMUM_LIST_SIZE = 50;

    public static Database getInstance() {
        return database;
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
        if (bookIndex < 0) return mNotes.get(lessonIndex).getNoteTitle();
        return mTextbooks.get(bookIndex).getTextbookContent().get(lessonIndex).getLessonTitle();
    }

    public int getNumOfLesson(int bookIndex) {
        if (bookIndex < 0) return mNotes.size();
        return mTextbooks.get(bookIndex).getTextbookContent().size();
    }

    public ArrayList<Integer> getContentIds(int bookIndex, int lessonIndex) {
        if (bookIndex < 0) return mNotes.get(lessonIndex).getNoteContent();
        return mTextbooks.get(bookIndex).getTextbookContent().get(lessonIndex).getLessonContent();
    }

    public ArrayList<String> getNoteNames() {
        ArrayList<String> noteNames = new ArrayList<>();
        for (Note note : mNotes) {
            noteNames.add(note.getNoteTitle());
        }
        return noteNames;
    }

    public ArrayList<Vocabulary> getVocabulariesByIDs(ArrayList<Integer> vocIDs) {
        ArrayList<Vocabulary> vocabularies = new ArrayList<>();
        for (int index = 0; index < vocIDs.size(); index++) {
            for (int index2 = 0; index2 < mVocabularies.size(); index2++) {
                Vocabulary vocabulary = mVocabularies.get(index2);
                if (vocIDs.get(index).equals(vocabulary.getId())) {
                    vocabularies.add(vocabulary);
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
        return resultVocabularies;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /* Note operations */
    public void createNewNote(String name) {
        int index = mNotes.size();
        mNotes.add(index, new Note(index, name, new ArrayList<Integer>()));
    }

    public void addVocToNote(int vocID, int noteIndex) {
        ArrayList<Integer> content = mNotes.get(noteIndex).getNoteContent();
        for (int index = 0; index < content.size(); index++) {
            int id = content.get(index);
            if (id == vocID) {
                break;
            }
        }
        content.add(vocID);
    }

    public void renameNoteAt(int noteIndex, String name) {
        int id = mNotes.get(noteIndex).getNoteId();
        renameNote(id, name);
    }

    public void renameNote(int id, String name) {
        for (int index = 0; index < mNotes.size(); index++) {
            Note note = mNotes.get(index);
            if (id == note.getNoteId()) {
                note.setNoteTitle(name);
                return;
            }
        }
    }

    public void deleteNoteAt(int noteIndex) {
        int id = mNotes.get(noteIndex).getNoteId();
        deleteNote(id);
    }

    public void deleteNote(int id) {
        Note noteToBeDelete = null;
        for (int index = 0; index < mNotes.size(); index++) {
            Note note = mNotes.get(index);
            if (id == note.getNoteId()) {
                noteToBeDelete = note;
            }
        }

        if (noteToBeDelete == null)
            return;

        mNotes.remove(noteToBeDelete);

        // refresh IDs
        for (int index = 0; index < mNotes.size(); index++) {
            Note note = mNotes.get(index);
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
