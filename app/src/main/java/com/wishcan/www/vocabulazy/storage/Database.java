package com.wishcan.www.vocabulazy.storage;

import android.app.VoiceInteractor;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.application.GlobalVariable;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Lesson;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Note;
import com.wishcan.www.vocabulazy.storage.databaseObjects.OptionSettings;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Textbook;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;

public class Database {
    public static final String TAG = Database.class.getSimpleName();

    public static final String FILENAME_NOTE = "note.json";
    public static final String FILENAME_OPTION = "option.json";

    private GlobalVariable mGlobalVariable;

    private static Database database = new Database();

//    private Preferences mPreferences;

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

    public void writeToFile(Context context) {
        write(context, FILENAME_NOTE, mNotes.toArray());
        write(context, FILENAME_OPTION, mGlobalVariable.optionSettings.toArray());
    }

    public ArrayList<Textbook> getTextbooks() {
        return mTextbooks;
    }

    public String getTextbookTitle(int bookIndex) {
        return mTextbooks.get(bookIndex).getTextbookTitle();
    }

    public String getLessonTitle(int bookIndex, int lessonIndex) {
        return mTextbooks.get(bookIndex).getTextbookContent().get(lessonIndex).getLessonTitle();
    }

    public int getNumOfLesson(int bookIndex) {
        return mTextbooks.get(bookIndex).getTextbookContent().size();
    }

    public int getNoteSize(int noteIndex) {
        return mNotes.get(noteIndex).getNoteContent().size();
    }

    public ArrayList<Integer> getContentIds(int bookIndex, int lessonIndex) {
        return mTextbooks.get(bookIndex).getTextbookContent().get(lessonIndex).getLessonContent();
    }

    public ArrayList<Lesson> getLessonsByBook(int bookIndex) {
        return mTextbooks.get(bookIndex).getTextbookContent();
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
