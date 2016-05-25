package com.wishcan.www.vocabulazy.storage;

import android.content.Context;
import android.preference.Preference;

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
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Book;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Lesson;
import com.wishcan.www.vocabulazy.storage.databaseObjects.OptionSettings;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;

/**
 * Created by allencheng07 on 2015/9/1.
 */
public class Database {

    public static final String TAG = Database.class.getSimpleName();

    public static final String FILENAME_NOTE = "vl_database_note.json";
    public static final String FILENAME_OPTION = "option.json";

    private Context wContext;
    private Preferences mPreferences;

    private ArrayList<Vocabulary> wVocabularies;
    private ArrayList<Book> wBooks;
    private ArrayList<Lesson> wLessons;
    private ArrayList<Lesson> wNotes;
    private ArrayList<OptionSettings> wOptionSettings;

    private static final int MAXIMUM_LIST_SIZE = 50;

    public Database(VLApplication application) {
        wContext = application.getApplicationContext();
        mPreferences = application.getPreferences();
    }

    public void loadFiles() {
        try {
            wVocabularies = load(Vocabulary[].class, wContext.getResources().openRawResource(R.raw.vl_database_vocabulary));
            wBooks = load(Book[].class, wContext.getResources().openRawResource(R.raw.vl_database_book));
            wLessons = load(Lesson[].class, wContext.getResources().openRawResource(R.raw.vl_database_lesson));
            wNotes = load(Lesson[].class, wContext.openFileInput(FILENAME_NOTE));
            wOptionSettings = load(OptionSettings[].class, wContext.openFileInput(FILENAME_OPTION));
        } catch (FileNotFoundException fnfe) {
            wNotes = load(Lesson[].class, wContext.getResources().openRawResource(R.raw.note));
            wOptionSettings = load(OptionSettings[].class, wContext.getResources().openRawResource(R.raw.option));
        }
    }

    public void writeToFile() {
        write(FILENAME_NOTE, wNotes.toArray());
        write(FILENAME_OPTION, mPreferences.getOptionSettings().toArray());
    }

    public void initSettings() {
        mPreferences.setOptionSettings(wOptionSettings);
        mPreferences.setOptionMode(OptionSettings.MODE_DEFAULT);
    }

    public ArrayList<Book> getBooks() {
        return wBooks;
    }

    public int getLessonID(int bookIndex, int lessonIndex) {
        if (bookIndex >= 0)
            return wBooks.get(bookIndex).getContent().get(lessonIndex);
        else
            return wNotes.get(lessonIndex).getId();
    }

    public int getNumOfLesson(int bookIndex) {
        if (bookIndex >= 0)
            return wBooks.get(bookIndex).getContent().size();
        else
            return wNotes.size();
    }

    public int getNoteSize(int noteIndex) {
        return wNotes.get(noteIndex).getContent().size();
    }

    public ArrayList<Integer> getContentIDs(int bookIndex, int lessonIndex) {
        if (bookIndex < 0) {
            return wNotes.get(lessonIndex).getContent();
        }

        int lessonId = getLessonID(bookIndex, lessonIndex);
        ArrayList<Integer> content = new ArrayList<>();
        for (int index = 0; index < wLessons.size(); index++) {
            Lesson lesson = wLessons.get(index);
            if (lessonId == lesson.getId()) {
                content = lesson.getContent();
            }
        }
        return content;
    }

    public ArrayList<Lesson> getLessonsByBook(int bookIndex) {
        if (bookIndex < 0)
            return wNotes;

        ArrayList<Lesson> lessons = new ArrayList<>();
        ArrayList<Integer> content = wBooks.get(bookIndex).getContent();
        for (int index = 0; index < content.size(); index++) {
            for (int index2 = 0; index2 < wLessons.size(); index2++) {
                Lesson lesson = wLessons.get(index2);
                if (content.get(index) == lesson.getId()) {
                    lessons.add(lesson);
                    break;
                }
            }
        }
        return lessons;
    }

    public ArrayList<Vocabulary> getVocabulariesByIDs(ArrayList<Integer> vocIDs) {
        ArrayList<Vocabulary> vocabularies = new ArrayList<>();
        for (int index = 0; index < vocIDs.size(); index++) {
            for (int index2 = 0; index2 < wVocabularies.size(); index2++) {
                Vocabulary vocabulary = wVocabularies.get(index2);
                if (vocIDs.get(index).equals(vocabulary.getId())) {
                    vocabularies.add(vocabulary);
                }
            }
        }
        return vocabularies;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /* Search operation */
    public ArrayList<Vocabulary> readSuggestVocabularyBySpell(String queryString) {
        ArrayList<Vocabulary> resultVocabularies = new ArrayList<>();
        for (int index = 0; index < wVocabularies.size(); index++) {
            Vocabulary vocabulary = wVocabularies.get(index);
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
        int index = wNotes.size();
        wNotes.add(index, new Lesson(index, name, new ArrayList<Integer>()));
    }

    public void addVocToNote(int vocID, int noteIndex) {
        ArrayList<Integer> content = wNotes.get(noteIndex).getContent();
        for (int index = 0; index < content.size(); index++) {
            int id = content.get(index);
            if (id == vocID) {
                break;
            }
        }
        content.add(vocID);
    }

    public void renameNoteAt(int noteIndex, String name) {
        int id = wNotes.get(noteIndex).getId();
        renameNote(id, name);
    }

    public void renameNote(int id, String name) {
        for (int index = 0; index < wNotes.size(); index++) {
            Lesson note = wNotes.get(index);
            if (id == note.getId()) {
                note.rename(name);
                return;
            }
        }
    }

    public void deleteNoteAt(int noteIndex) {
        int id = wNotes.get(noteIndex).getId();
        deleteNote(id);
    }

    public void deleteNote(int id) {
        Lesson noteToBeDelete = null;
        for (int index = 0; index < wNotes.size(); index++) {
            Lesson note = wNotes.get(index);
            if (id == note.getId()) {
                noteToBeDelete = note;
            }
        }

        if (noteToBeDelete == null)
            return;

        wNotes.remove(noteToBeDelete);

        // refresh IDs
        for (int index = 0; index < wNotes.size(); index++) {
            Lesson note = wNotes.get(index);
            note.setId(index);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /* IO operations */
    private <T> ArrayList<T> load(Class<T[]> classOfT, InputStream inputStream) {

        BufferedReader bfdReader = null;
        try {
            bfdReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (bfdReader == null) throw new NullPointerException();

        String readline;
        StringBuilder builder = new StringBuilder();
        try {
            while ((readline = bfdReader.readLine()) != null) {
                builder.append(readline);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bfdReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        T[] tArray = new Gson().fromJson(builder.toString(), classOfT);
        return new ArrayList<T>(Arrays.asList(tArray));
    }

    private <T> void write(String filename, T[] array) {
        FileOutputStream fos;
        try {
            fos = wContext.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(new Gson().toJson(array).getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
