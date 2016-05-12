package com.wishcan.www.vocabulazy.storage;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.LogWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericSignatureFormatError;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.service.AudioService;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Book;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Lesson;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Option;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;

/**
 * Created by allencheng07 on 2015/9/1.
 */
public class Database {

    public static final String TAG = Database.class.getSimpleName();

    public static final String FILENAME_NOTE = "note.json";
    public static final String FILENAME_OPTION = "option.json";

    private Context mContext;

    private ArrayList<Vocabulary> wVocabularies;
    private ArrayList<Book> wBooks;
    private ArrayList<Lesson> wLessons;
    private ArrayList<Lesson> wNotes;
    private ArrayList<Option> wOptionSettings;

    private static final int MAXIMUM_LIST_SIZE = 50;

    private int mCurrentOptionMode;

    public Database(Context context) {
        mContext = context;
        mCurrentOptionMode = 0;
        loadFiles();
    }

    public void loadFiles() {
        try {
            wVocabularies = load(Vocabulary[].class, mContext.getResources().openRawResource(R.raw.vocabulary));
            wBooks = load(Book[].class, mContext.getResources().openRawResource(R.raw.book));
            wLessons = load(Lesson[].class, mContext.getResources().openRawResource(R.raw.lesson));
            wNotes = load(Lesson[].class, mContext.openFileInput(FILENAME_NOTE));
            wOptionSettings = load(Option[].class, mContext.openFileInput(FILENAME_OPTION));

            Log.d(TAG, "not the first time entering the app");
        } catch (FileNotFoundException fnfe) {
            Log.d(TAG, "first time enter app");
            wNotes = load(Lesson[].class, mContext.getResources().openRawResource(R.raw.note));
            wOptionSettings = load(Option[].class, mContext.getResources().openRawResource(R.raw.option));
        }
    }

    public <T> ArrayList<T> load(Class<T[]> classOfT, InputStream inputStream) {

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

    public void writeToFile() {
        write(FILENAME_NOTE, wNotes.toArray());
        write(FILENAME_OPTION, wOptionSettings.toArray());
    }

    private <T> void write(String filename, T[] array) {
        FileOutputStream fos;
        try {
            fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(new Gson().toJson(array).getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (bookIndex >= 0) {
            int lessonid = getLessonID(bookIndex, lessonIndex);
            for (int index = 0; index < wLessons.size(); index++) {
                Lesson lesson = wLessons.get(index);
                if (lessonid == lesson.getId()) {
                    return lesson.getContent();
                }
            }
            return null;
        } else {
            return wNotes.get(lessonIndex).getContent();
        }
    }

    public ArrayList<Lesson> getLessonsByBook(int bookIndex) {
        if (bookIndex >= 0) {
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
        } else {
            return wNotes;
        }
    }

    public ArrayList<Vocabulary> getVocabulariesByIDs(ArrayList<Integer> vocIDs) {
        ArrayList<Vocabulary> vocabularies = new ArrayList<>();

        Log.d(TAG, wVocabularies.size() + " vocabularies in total");
        Log.d(TAG, vocIDs.size() + " vocabularies to be matched");

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

    public ArrayList<Option> getOptions() {
        return wOptionSettings;
    }

    public void setCurrentOptions(ArrayList<Option> options) {
        wOptionSettings = options;
    }

    public Option getCurrentOption() {
        return wOptionSettings.get(mCurrentOptionMode);
    }

    public void setCurrentOptionMode(int mode) {
        mCurrentOptionMode = mode;
    }

    public void createNewNote(String name) {
        int index = wNotes.size();
        wNotes.add(index, new Lesson(index, name, new ArrayList<Integer>(), null));
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
        for (int index = 0; index < wNotes.size(); index++) {
            Lesson note = wNotes.get(index);
            if (id == note.getId()) {
                wNotes.remove(note);
                break;
            }
        }
        refreshNoteIDs();
    }

    private void refreshNoteIDs() {
        for (int index = 0; index < wNotes.size(); index++) {
            Lesson note = wNotes.get(index);
            note.setId(index);
        }
    }
}
