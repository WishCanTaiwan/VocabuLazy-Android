package com.wishcan.www.vocabulazy.storage;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by allencheng07 on 2015/10/12.
 */
public class DatabaseService extends IntentService {

    private static final String TAG = DatabaseService.class.getSimpleName();

    private ArrayList<Book> mBooks;
    private ArrayList<Lesson> mLessons;
    private ArrayList<Lesson> mNotes;
    private ArrayList<Vocabulary> mVocabularies;
    private ArrayList<Option> mOptions;

    public static final String DATABASE_ACTION_GET_BOOKS = "getBooks";

    public static final String DATABASE_ACTION_GET_BOOK_ID = "getBookID";

    public static final String DATABASE_ACTION_GET_LESSON_ID = "getLessonID";

    public static final String DATABASE_ACTION_GET_NUM_OF_LESSON_IN_BOOK = "getNumOfLessonInBook";

    public static final String DATABASE_ACTION_GET_CONTENT_ID = "getContentID";

    public static final String DATABASE_ACTION_GET_LESSON_NAME = "getLessonName";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DatabaseService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        // create database, read from file

        loadDatabaseFiles();

    }

    private void loadDatabaseFiles() {
        loadVocabularies();
        loadBooks();
        loadLessons();
        loadNotes();
        loadOptions();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        // store database, write to file
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // all kinds of actions

        return START_NOT_STICKY;
    }

    private void loadVocabularies() {

        int sizeOfFile;
        byte[] bytes = new byte[0];
        String jsonString;
        JSONObject jsonObject;

        try {
            FileInputStream fis = openFileInput("vocabulary.json");
            Log.d(TAG, "vocabulary file found");
            sizeOfFile = fis.available();
            bytes = new byte[sizeOfFile];
            fis.read(bytes);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "vocabulary file not found in internal storage");
            Log.d(TAG, "using raw file");
            InputStream is = getResources().openRawResource(R.raw.vocabulary);
            try {
                sizeOfFile = is.available();
                bytes = new byte[sizeOfFile];

                is.read(bytes);
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mVocabularies = new ArrayList<>();

        try {
            jsonString = new String(bytes, "UTF-8");
            jsonObject = new JSONObject(jsonString);

            JSONArray jsonArray = jsonObject.getJSONArray("vocabulary");
            for (int index = 0; index < jsonArray.length(); index++) {

                JSONObject vocabularyObject = jsonArray.getJSONObject(index);

                int id = vocabularyObject.getInt("id");
                String spell = vocabularyObject.getString("spell");
                String category = vocabularyObject.getString("category");
                String translate = vocabularyObject.getString("translate");
                String audio = vocabularyObject.getString("audio");
                String en_sentence = vocabularyObject.getString("en_sentence");
                String cn_sentence = vocabularyObject.getString("cn_sentence");
                String sentence_audio = vocabularyObject.getString("sentence_audio");

                Vocabulary vocabulary
                        = new Vocabulary(id, spell, category, translate, audio, en_sentence, cn_sentence, sentence_audio);

                mVocabularies.add(vocabulary);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadBooks() {

        int sizeOfFile;
        byte[] bytes = new byte[0];
        String jsonString;
        JSONObject jsonObject;

        try {
            FileInputStream fis = openFileInput("book.json");
            Log.d(TAG, "book file found");
            sizeOfFile = fis.available();
            bytes = new byte[sizeOfFile];
            fis.read(bytes);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "book file not found in internal storage");
            Log.d(TAG, "using raw file");
            InputStream is = getResources().openRawResource(R.raw.book);
            try {
                sizeOfFile = is.available();
                bytes = new byte[sizeOfFile];

                is.read(bytes);
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mBooks = new ArrayList<>();

        try {
            jsonString = new String(bytes, "UTF-8");
            jsonObject = new JSONObject(jsonString);

            JSONArray jsonArray = jsonObject.getJSONArray("book");
            for (int index = 0; index < jsonArray.length(); index++) {

                JSONObject bookObject = jsonArray.getJSONObject(index);

                int id = bookObject.getInt("id");
                String name = bookObject.getString("name");

                ArrayList<Integer> content = new ArrayList<>();
                JSONArray bookcontentArray = bookObject.getJSONArray("content");
                for (int index2 = 0; index2 < bookcontentArray.length(); index2++) {
                    content.add(bookcontentArray.getInt(index2));
                }

                mBooks.add(new Book(id, name, content));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadLessons() {

        int sizeOfFile;
        byte[] bytes = new byte[0];
        String jsonString;
        JSONObject jsonObject;

        try {
            FileInputStream fis = openFileInput("lesson.json");
            Log.d(TAG, "lesson file found");
            sizeOfFile = fis.available();
            bytes = new byte[sizeOfFile];
            fis.read(bytes);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "lesson file not found in internal storage");
            Log.d(TAG, "using raw file");
            InputStream is = getResources().openRawResource(R.raw.lesson);
            try {
                sizeOfFile = is.available();
                bytes = new byte[sizeOfFile];

                is.read(bytes);
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mLessons = new ArrayList<>();

        try {
            jsonString = new String(bytes, "UTF-8");
            jsonObject = new JSONObject(jsonString);

            JSONArray jsonArray = jsonObject.getJSONArray("lesson");
            for (int index = 0; index < jsonArray.length(); index++) {

                JSONObject lessonObject = jsonArray.getJSONObject(index);

                int id = lessonObject.getInt("id");
                String name = lessonObject.getString("name");

                ArrayList<Integer> content = new ArrayList<>();
                JSONArray lessoncontentArray = lessonObject.getJSONArray("content");
                for (int index2 = 0; index2 < lessoncontentArray.length(); index2++) {
                    content.add(lessoncontentArray.getInt(index2));
                }

                mLessons.add(new Lesson(id, name, content));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadNotes() {

        int sizeOfFile;
        byte[] bytes = new byte[0];
        String jsonString;
        JSONObject jsonObject;

        try {
            FileInputStream fis = openFileInput("note.json");
            Log.d(TAG, "note file found");
            sizeOfFile = fis.available();
            bytes = new byte[sizeOfFile];
            fis.read(bytes);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "note file not found in internal storage");
            Log.d(TAG, "using raw file");
            InputStream is = getResources().openRawResource(R.raw.note);
            try {
                sizeOfFile = is.available();
                bytes = new byte[sizeOfFile];

                is.read(bytes);
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mNotes = new ArrayList<>();

        try {
            jsonString = new String(bytes, "UTF-8");
            jsonObject = new JSONObject(jsonString);

            JSONArray jsonArray = jsonObject.getJSONArray("note");
            for (int index = 0; index < jsonArray.length(); index++) {

                JSONObject noteObject = jsonArray.getJSONObject(index);

                int id = noteObject.getInt("id");
                String name = noteObject.getString("name");

                ArrayList<Integer> content = new ArrayList<>();
                JSONArray notecontentArray = noteObject.getJSONArray("content");
                for (int index2 = 0; index2 < notecontentArray.length(); index2++) {
                    content.add(notecontentArray.getInt(index2));
                }

                mNotes.add(new Lesson(id, name, content));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadOptions() {

        int sizeOfFile;
        byte[] bytes = new byte[0];
        String jsonString;
        JSONObject jsonObject;

        try {
            FileInputStream fis = openFileInput("option.json");
            Log.d(TAG, "option file found");
            sizeOfFile = fis.available();
            bytes = new byte[sizeOfFile];
            fis.read(bytes);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "option file not found in internal storage");
            Log.d(TAG, "using raw file");
            InputStream is = getResources().openRawResource(R.raw.option);
            try {
                sizeOfFile = is.available();
                bytes = new byte[sizeOfFile];

                is.read(bytes);
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mOptions = new ArrayList<>();

        try {
            jsonString = new String(bytes, "UTF-8");
            jsonObject = new JSONObject(jsonString);

            JSONArray jsonArray = jsonObject.getJSONArray("option");
            for (int index = 0; index < jsonArray.length(); index++) {

                JSONObject optionObject = jsonArray.getJSONObject(index);

                int mode = optionObject.getInt("mode");
                boolean isRandom = optionObject.getBoolean("random");
                int listloop = optionObject.getInt("listloop");
                boolean sentence = optionObject.getBoolean("sentence");
                int stopperiod = optionObject.getInt("stopperiod");
                int itemloop = optionObject.getInt("itemloop");
                int speed = optionObject.getInt("speed");
                int playtime = optionObject.getInt("playtime");

                mOptions.add(new Option(mode, isRandom, listloop, sentence, stopperiod, itemloop, speed, playtime));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
