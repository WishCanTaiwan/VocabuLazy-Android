package com.wishcan.www.vocabulazy.storage;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by allencheng07 on 2015/9/1.
 */
public class Database implements Parcelable {

    private static final String TAG = Database.class.getSimpleName();

    private Context mContext;

    private ArrayList<Book> mBooks;
    private ArrayList<Lesson> mLessons;
    private ArrayList<Lesson> mNotes;
    private ArrayList<Vocabulary> mVocabularies;
    private ArrayList<Option> mOptions;

    private static final int MAXIMUM_LIST_SIZE = 50;

    private ArrayList<Integer> mCurrentContentInPlayer;
    private int mCurrentOptionMode;

    private int mCurrentPlayingBook;
    private int mCurrentPlayingList;
    private int mCurrentPlayingItem;

    public Database(Context context) {
        mContext = context;

        mCurrentContentInPlayer = new ArrayList<>();
        mCurrentOptionMode = -1;
        mCurrentPlayingBook = -1;
        mCurrentPlayingList = -1;

        loadDatabaseFiles();
    }

    protected Database(Parcel in) {
        mBooks = in.readArrayList(Book.class.getClassLoader());
        mLessons = in.readArrayList(Lesson.class.getClassLoader());
        mNotes = in.readArrayList(Lesson.class.getClassLoader());
        mVocabularies = in.readArrayList(Vocabulary.class.getClassLoader());
        mOptions = in.readArrayList(Option.class.getClassLoader());
        mCurrentContentInPlayer = in.readArrayList(Integer.class.getClassLoader());
        mCurrentOptionMode = in.readInt();
        mCurrentPlayingBook = in.readInt();
        mCurrentPlayingList = in.readInt();
    }

    public static final Creator<Database> CREATOR = new Creator<Database>() {
        @Override
        public Database createFromParcel(Parcel in) {
            return new Database(in);
        }

        @Override
        public Database[] newArray(int size) {
            return new Database[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(mBooks);
        dest.writeList(mLessons);
        dest.writeList(mNotes);
        dest.writeList(mVocabularies);
        dest.writeList(mOptions);
        dest.writeList(mCurrentContentInPlayer);
        dest.writeInt(mCurrentOptionMode);
        dest.writeInt(mCurrentPlayingBook);
        dest.writeInt(mCurrentPlayingList);
    }

    public void loadDatabaseFiles() {
        loadVocabularies();
        loadBooks();
        loadLessons();
        loadNotes();
        loadOptions();
    }

    public void writeToFile(Context context) {
        mContext = context;
        writeNote();
        writeOption();
    }

    public int getCurrentPlayingBook() {
        return mCurrentPlayingBook;
    }

    public void setCurrentPlayingBook(int mCurrentPlayingBook) {
        this.mCurrentPlayingBook = mCurrentPlayingBook;
    }

    public int getCurrentPlayingItem() {
        return mCurrentPlayingItem;
    }

    public void setCurrentPlayingItem(int mCurrentPlayingItem) {
        this.mCurrentPlayingItem = mCurrentPlayingItem;
    }

    public int getCurrentPlayingList() {
        return mCurrentPlayingList;
    }

    public void setCurrentPlayingList(int mCurrentPlayingList) {
        this.mCurrentPlayingList = mCurrentPlayingList;
    }

    public ArrayList<Book> getBooks() {
        return mBooks;
    }

    public int getBookID(int bookIndex) {
        if (bookIndex >= 0)
            return mBooks.get(bookIndex).getID();
        else
            return -1;
    }

    public int getLessonID(int bookIndex, int lessonIndex) {
        if (bookIndex >= 0)
            return mBooks.get(bookIndex).getContent().get(lessonIndex);
        else
            return mNotes.get(lessonIndex).getID();
    }

    public int getNumOfLesson(int bookIndex) {
        if (bookIndex >= 0)
            return mBooks.get(bookIndex).getContent().size();
        else
            return mNotes.size();
    }

    public ArrayList<Integer> getContentIDs(int bookIndex, int lessonIndex) {
        ArrayList<Integer> content = new ArrayList<>();

        if (bookIndex >= 0) {
            int lessonid = getLessonID(bookIndex, lessonIndex);
            for (int index = 0; index < mLessons.size(); index++) {
                Lesson lesson = mLessons.get(index);
                if (lessonid == lesson.getID()) {
                    content = lesson.getContent();
                }
            }
        } else {
            content = mNotes.get(lessonIndex).getContent();
        }

        return content;
    }

    public JSONObject getReadingContent(int lessonID) {

        JSONObject readingObject = new JSONObject();

        for (int index = 0; index < mLessons.size(); index++) {
            Lesson lesson = mLessons.get(index);
            if (lessonID == lesson.getID()) {
                readingObject = lesson.mReading;
            }
        }

        return readingObject;
    }

    public String getLessonName(int bookIndex, int lessonIndex) {
        if (bookIndex >= 0) {
            int lessonID = mBooks.get(bookIndex).getContent().get(lessonIndex);
            String lessonName = null;
            for (int index = 0; index < mLessons.size(); index++) {
                Lesson lesson = mLessons.get(index);
                if (lessonID == lesson.getID()) {
                    lessonName = lesson.getName();
                    break;
                }
            }
            return lessonName;
        } else {
            return mNotes.get(lessonIndex).getName();
        }
    }

    public ArrayList<Lesson> getLessonsByBook(int bookIndex) {

        if (bookIndex >= 0) {
            ArrayList<Lesson> lessons = new ArrayList<>();

            ArrayList<Integer> content = mBooks.get(bookIndex).getContent();

            for (int index = 0; index < content.size(); index++) {
                for (int index2 = 0; index2 < mLessons.size(); index2++) {
                    Lesson lesson = mLessons.get(index2);
                    if (content.get(index) == lesson.getID()) {
                        lessons.add(lesson);
                        break;
                    }
                }
            }

            return lessons;
        } else {
            return mNotes;
        }
    }

    public ArrayList<Vocabulary> getVocabularies() {
        return mVocabularies;
    }

    public ArrayList<Vocabulary> getVocabulariesByIDs(ArrayList<Integer> ids) {
        ArrayList<Vocabulary> vocabularies = new ArrayList<>();

//        for (int index = 0; index < ids.size(); index++) {
//            Log.d(TAG, index + ": " + ids.get(index));
//        }

//        Log.d(TAG, "size: " + mVocabularies.size());

        for (int index = 0; index < ids.size(); index++) {
            for (int index2 = 0; index2 < mVocabularies.size(); index2++) {
                Vocabulary vocabulary = mVocabularies.get(index2);
//                Log.d(TAG, "voc id: " + vocabulary.getID());
                if (ids.get(index).equals(vocabulary.getID())) {
//                    Log.d(TAG, "id: " + vocabulary.getID());
                    vocabularies.add(vocabulary);
                    Log.d(TAG, "getVocabulariesByIDs - " + vocabulary.getSpell());
                }
            }
        }

        return vocabularies;
    }

    public ArrayList<String> getSpellAudios(ArrayList<Integer> ids) {
        ArrayList<String> spellaudios = new ArrayList<>();

        for (int index = 0; index < ids.size(); index++) {
            for (int index2 = 0; index2 < mVocabularies.size(); index2++) {
                Vocabulary vocabulary = mVocabularies.get(index2);
                if (ids.get(index) == vocabulary.getID()) {
                    spellaudios.add(vocabulary.getAudio());
                    break;
                }
            }
        }

        return spellaudios;
    }

    public ArrayList<ArrayList<String>> getSentenceAudios(ArrayList<Integer> ids) {
        ArrayList<ArrayList<String>> sentenceaudios = new ArrayList<>();

        for (int index = 0; index < ids.size(); index++) {
            for (int index2 = 0; index2 < mVocabularies.size(); index2++) {
                Vocabulary vocabulary = mVocabularies.get(index2);
                if (ids.get(index) == vocabulary.getID()) {
                    sentenceaudios.add(vocabulary.getSentence_Audio());
                    break;
                }
            }
        }

        return sentenceaudios;
    }

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
                continue;
            }
        }

        return resultVocabularies;
    }

    public ArrayList<Integer> getNoteContents(int noteID) {
        return mNotes.get(noteID).getContent();
    }

    public Lesson getNoteByID(int id) {
        Lesson note = null;

        for (int index = 0; index < mNotes.size(); index++) {
            note = mNotes.get(index);
            if (id == note.getID()) {
                break;
            }
        }

        return note;
    }

    public Vocabulary getVocByID(int id) {
        Vocabulary vocabulary = null;

        for (int index = 0; index < mVocabularies.size(); index++) {
            vocabulary = mVocabularies.get(index);
            if (id == vocabulary.getID()) {
                break;
            }
        }

        return vocabulary;
    }

    public int getNumOfNotes() {
        return mNotes.size();
    }

    public int getNumOfBooks() {
        return mBooks.size();
    }

    public ArrayList<Option> getOptions() {
        return mOptions;
    }

    public ArrayList<Integer> getCurrentContentInPlayer() {
        return mCurrentContentInPlayer;
    }

    public void setCurrentContentInPlayer(ArrayList<Integer> content) {
        mCurrentContentInPlayer = content;
    }

    public void setCurrentOptions(ArrayList<Option> options) {
        mOptions = options;
    }

    public int getCurrentOptionMode() {
        return mCurrentOptionMode;
    }

    public void setCurrentOptionMode(int mode) {
        mCurrentOptionMode = mode;
    }

    public void addVocToNote(int vocID, int noteID) {

        Log.d(TAG, "addVocToNote: " + vocID + ", " + noteID);

        Lesson note = getNoteByID(noteID);

        Log.d(TAG, "note name: " + note.getName());

        ArrayList<Integer> content = note.getContent();
        for (int index = 0; index < content.size(); index++) {
            int id = content.get(index);
            if (id == vocID) {
                break;
            }
        }
        content.add(vocID);

    }

    public void createNewNote(String name) {
        int index = mNotes.size();
//        Log.d(TAG, "create " + name + " at " + index);
        mNotes.add(index, new Lesson(index + 1, name, new ArrayList<Integer>(), null));
    }

    public void renameNoteAt(int position, String name) {
        int id = mNotes.get(position).getID();
        renameNote(id, name);
    }

    public void renameNote(int id, String name) {
        for (int index = 0; index < mNotes.size(); index++) {
            Lesson note = mNotes.get(index);
            if (id == note.getID()) {
                note.rename(name);
                return;
            }
        }
    }

    public void deleteNote(int id) {
        for (int index = 0; index < mNotes.size(); index++) {
            Lesson note = mNotes.get(index);
            if (id == note.getID()) {
                Log.d(TAG, "delete: " + note.getName());
                mNotes.remove(note);
                break;
            }
        }
        refreshNoteIDs();

    }

    private void refreshNoteIDs() {
        for (int index = 0; index < mNotes.size(); index++) {
            Lesson note = mNotes.get(index);
            note.setID(index+1);
        }
    }

    public void deleteNoteAt(int position) {
        int id = mNotes.get(position).getID();
        deleteNote(id);
    }

    private void loadVocabularies() {

        InputStream is;
//        try {
//            is = mContext.openFileInput("vocabulary.json");
//        } catch (FileNotFoundException e) {
            is = mContext.getResources().openRawResource(R.raw.vocabulary);
//        }

        JSONArray jsonArray = readJSONArray(is);

        mVocabularies = new ArrayList<>();

        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject object;

            try {
                object = jsonArray.getJSONObject(index);

                int id = object.getInt("id");

                String spell = object.getString("spell");
                String kk = object.getString("kk");
                String category = object.getString("category");
//                String translation = object.getString("translation");
//                ArrayList<String> category = toArrayList(object.getJSONArray("category"), new ArrayList<String>());
                ArrayList<String> translation = toArrayList(object.getJSONArray("translation"), new ArrayList<String>());
                String audio = object.getString("spell_audio");

                ArrayList<String> en_sentence_array = toArrayList(object.getJSONArray("en_sentence"), new ArrayList<String>());
                ArrayList<String> cn_sentence_array = toArrayList(object.getJSONArray("cn_sentence"), new ArrayList<String>());
                ArrayList<String> sentence_audio_array = toArrayList(object.getJSONArray("sentence_audio"), new ArrayList<String>());

                Vocabulary vocabulary = new Vocabulary(id, spell, kk, category, translation, audio, en_sentence_array, cn_sentence_array, sentence_audio_array);
                mVocabularies.add(vocabulary);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void loadBooks() {

        InputStream is;
//        try {
//            is = mContext.openFileInput("book.json");
//        } catch (FileNotFoundException e) {
            is = mContext.getResources().openRawResource(R.raw.book);
//        }

        JSONArray jsonArray = readJSONArray(is);

        mBooks = new ArrayList<>();

        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject object;

            try {
                object = jsonArray.getJSONObject(index);

                int id = object.getInt("id");
                String name = object.getString("name");
                ArrayList<Integer> content = convertJSONArrayToIntegerArrayList(object.getJSONArray("content_of_the_book"));

                mBooks.add(new Book(id, name, content));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void loadLessons() {

        InputStream is;
//        try {
//            is = mContext.openFileInput("lesson.json");
//        } catch (FileNotFoundException e) {
            is = mContext.getResources().openRawResource(R.raw.lesson);
//        }

        JSONArray jsonArray = readJSONArray(is);

        mLessons = new ArrayList<>();

        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject object;

            try {
                object = jsonArray.getJSONObject(index);

                int id = object.getInt("id");
                String name = object.getString("name");
                ArrayList<Integer> content = convertJSONArrayToIntegerArrayList(object.getJSONArray("content_of_the_lesson"));
                JSONObject readingObject = object.getJSONObject("reading");

                mLessons.add(new Lesson(id, name, content, readingObject));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void loadNotes() {

        InputStream is;
        try {
            is = mContext.openFileInput("note.json");
        } catch (FileNotFoundException e) {
            is = mContext.getResources().openRawResource(R.raw.note);
        }

        JSONArray jsonArray = readJSONArray(is);

        mNotes = new ArrayList<>();

        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject object;

            try {
                object = jsonArray.getJSONObject(index);

                int id = object.getInt("id");
                String name = object.getString("name");
                ArrayList<Integer> content = convertJSONArrayToIntegerArrayList(object.getJSONArray("content_of_the_note"));

                mNotes.add(new Lesson(id, name, content, null));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void loadOptions() {

        InputStream is;
        try {
            is = mContext.openFileInput("option.json");
        } catch (FileNotFoundException e) {
            is = mContext.getResources().openRawResource(R.raw.option);
        }

        JSONArray jsonArray = readJSONArray(is);

        mOptions = new ArrayList<>();

        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject object;

            try {
                object = jsonArray.getJSONObject(index);

                int mode = object.getInt("mode");
                boolean isRandom = object.getBoolean("random");
                int listloop = object.getInt("listloop");
                boolean sentence = object.getBoolean("sentence");
                int stopperiod = object.getInt("stopperiod");
                int itemloop = object.getInt("itemloop");
                int speed = object.getInt("speed");
                int playtime = object.getInt("playtime");

                mOptions.add(new Option(mode, isRandom, listloop, sentence, stopperiod, itemloop, speed, playtime));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void writeNote() {

        Log.d(TAG, "writeNote");

        JSONArray jsonArray = new JSONArray();

        for (int index = 0; index < mNotes.size(); index++) {
            Lesson note = mNotes.get(index);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", note.getID());
                jsonObject.put("name", note.getName());
                jsonObject.put("content_of_the_note", convertIntegerArrayListToJSONArray(note.getContent()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonArray.put(jsonObject);
        }

        FileOutputStream fos;
        try {
            fos = mContext.openFileOutput("note.json", Context.MODE_PRIVATE);
            fos.write(jsonArray.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeOption() {

        Log.d(TAG, "writeOption");

        JSONArray jsonArray = new JSONArray();

        for (int index = 0; index < mOptions.size(); index++) {
            Option option = mOptions.get(index);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("mode", option.getMode());
                jsonObject.put("random", option.isIsRandom());
                jsonObject.put("listloop", option.getListLoop());
                jsonObject.put("sentence", option.isSentence());
                jsonObject.put("stopperiod", option.getStopPeriod());
                jsonObject.put("itemloop", option.getItemLoop());
                jsonObject.put("speed", option.getSpeed());
                jsonObject.put("playtime", option.getPlayTime());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonArray.put(jsonObject);
        }

        FileOutputStream fos;
        try {
            fos = mContext.openFileOutput("option.json", Context.MODE_PRIVATE);
            fos.write(jsonArray.toString().getBytes());
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONArray readJSONArray(InputStream is) {
        JSONArray jsonArray = new JSONArray();

        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(is, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        BufferedReader bfdReader = new BufferedReader(isr);
        String readline;
        StringBuilder jsonStringBuilder = new StringBuilder();

        try {
            while ((readline = bfdReader.readLine()) != null) {
                jsonStringBuilder.append(readline);
            }

            bfdReader.close();
            isr.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonString = jsonStringBuilder.toString();

        try {
            jsonArray = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    private ArrayList toArrayList(JSONArray jsonArray, ArrayList arrayList) {
        for (int index = 0; index < jsonArray.length(); index++) {
            try {
                arrayList.add(jsonArray.getString(index));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    private ArrayList<Integer> convertJSONArrayToIntegerArrayList(JSONArray jsonArray) {
        ArrayList<Integer> arrayList = new ArrayList<>();

        for (int index = 0; index < jsonArray.length(); index++) {

            try {
                arrayList.add(jsonArray.getInt(index));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return arrayList;
    }

    private JSONArray convertIntegerArrayListToJSONArray(ArrayList<Integer> arrayList) {
        JSONArray jsonArray = new JSONArray();
        for (int index = 0; index < arrayList.size(); index++) {
            jsonArray.put(arrayList.get(index));
        }
        return jsonArray;
    }
}
