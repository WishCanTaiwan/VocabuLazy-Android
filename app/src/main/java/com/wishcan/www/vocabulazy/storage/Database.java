package com.wishcan.www.vocabulazy.storage;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

//        Log.d(TAG, "context " + mContext);

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

//    public void setContext(Context context) {
//        Log.d(TAG, "setContext");
//        mContext = context;
//    }

    private void loadDatabaseFiles() {
        loadVocabularies();
        loadBooks();
        loadLessons();
        loadNotes();
        loadOptions();
    }

    public void writeToFile(Context context) {
        mContext = context;
//        writeVocabulary();
//        writeBook();
//        writeLesson();
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

        for (int index = 0; index < ids.size(); index++) {
            for (int index2 = 0; index2 < mVocabularies.size(); index2++) {
                Vocabulary vocabulary = mVocabularies.get(index2);
                if (ids.get(index) == vocabulary.getID()) {
                    vocabularies.add(vocabulary);
                }
            }
        }

        return vocabularies;
    }

    public ArrayList<ArrayList<String>> getAudioNames(ArrayList<Integer> ids) {
        ArrayList<ArrayList<String>> audionames = new ArrayList<>();

        ArrayList<String> audio = new ArrayList<>();
        ArrayList<String> sentence_audio = new ArrayList<>();

        for (int index = 0; index < ids.size(); index++) {
            for (int index2 = 0; index2 < mVocabularies.size(); index2++) {
                Vocabulary vocabulary = mVocabularies.get(index2);
                if (ids.get(index) == vocabulary.getID()) {
                    audio.add(vocabulary.getAudio());
                    sentence_audio.add(vocabulary.getSentence_audio());
                }
            }
        }

        audionames.add(audio);
        audionames.add(sentence_audio);

        return audionames;
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
        Log.d(TAG, "create " + name + " at " + index);
        mNotes.add(index, new Lesson(index+1, name, new ArrayList<Integer>()));
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

    private void loadVocabularies() {

        int sizeOfFile;
        byte[] bytes = new byte[0];
        String jsonString;
        JSONObject jsonObject;

        try {
            FileInputStream fis = mContext.openFileInput("vocabulary.json");
            Log.d(TAG, "vocabulary file found");
            sizeOfFile = fis.available();
            bytes = new byte[sizeOfFile];
            fis.read(bytes);
            fis.close();
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            Log.d(TAG, "vocabulary file not found in internal storage");
            Log.d(TAG, "using raw file");
            InputStream is = mContext.getResources().openRawResource(R.raw.vocabulary);
            try {
                sizeOfFile = is.available();
                bytes = new byte[sizeOfFile];

                is.read(bytes);
                is.close();
            } catch (IOException e1) {
//                e1.printStackTrace();
            }
        } catch (IOException e) {
//            e.printStackTrace();
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
//            e.printStackTrace();
        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }

    private void loadBooks() {

        int sizeOfFile;
        byte[] bytes = new byte[0];
        String jsonString;
        JSONObject jsonObject;

        try {
            FileInputStream fis = mContext.openFileInput("book.json");
            Log.d(TAG, "book file found");
            sizeOfFile = fis.available();
            bytes = new byte[sizeOfFile];
            fis.read(bytes);
            fis.close();
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            Log.d(TAG, "book file not found in internal storage");
            Log.d(TAG, "using raw file");
            InputStream is = mContext.getResources().openRawResource(R.raw.book);
            try {
                sizeOfFile = is.available();
                bytes = new byte[sizeOfFile];

                is.read(bytes);
                is.close();
            } catch (IOException e1) {
//                e1.printStackTrace();
            }
        } catch (IOException e) {
//            e.printStackTrace();
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
//            e.printStackTrace();
        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }

    private void loadLessons() {

        int sizeOfFile;
        byte[] bytes = new byte[0];
        String jsonString;
        JSONObject jsonObject;

        try {
            FileInputStream fis = mContext.openFileInput("lesson.json");
            Log.d(TAG, "lesson file found");
            sizeOfFile = fis.available();
            bytes = new byte[sizeOfFile];
            fis.read(bytes);
            fis.close();
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            Log.d(TAG, "lesson file not found in internal storage");
            Log.d(TAG, "using raw file");
            InputStream is = mContext.getResources().openRawResource(R.raw.lesson);
            try {
                sizeOfFile = is.available();
                bytes = new byte[sizeOfFile];

                is.read(bytes);
                is.close();
            } catch (IOException e1) {
//                e1.printStackTrace();
            }
        } catch (IOException e) {
//            e.printStackTrace();
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
//            e.printStackTrace();
        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }

    private void loadNotes() {

        int sizeOfFile;
        byte[] bytes = new byte[0];
        String jsonString;
        JSONObject jsonObject;

        try {
            FileInputStream fis = mContext.openFileInput("note.json");
            Log.d(TAG, "note file found");
            sizeOfFile = fis.available();
            bytes = new byte[sizeOfFile];
            fis.read(bytes);
            fis.close();
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            Log.d(TAG, "note file not found in internal storage");
            Log.d(TAG, "using raw file");
            InputStream is = mContext.getResources().openRawResource(R.raw.note);
            try {
                sizeOfFile = is.available();
                bytes = new byte[sizeOfFile];

                is.read(bytes);
                is.close();
            } catch (IOException e1) {
//                e1.printStackTrace();
            }
        } catch (IOException e) {
//            e.printStackTrace();
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
//            e.printStackTrace();
        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }

    private void loadOptions() {

        int sizeOfFile;
        byte[] bytes = new byte[0];
        String jsonString;
        JSONObject jsonObject;

        try {
            FileInputStream fis = mContext.openFileInput("option.json");
            Log.d(TAG, "option file found");
            sizeOfFile = fis.available();
            bytes = new byte[sizeOfFile];
            fis.read(bytes);
            fis.close();
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            Log.d(TAG, "option file not found in internal storage");
            Log.d(TAG, "using raw file");
            InputStream is = mContext.getResources().openRawResource(R.raw.option);
            try {
                sizeOfFile = is.available();
                bytes = new byte[sizeOfFile];

                is.read(bytes);
                is.close();
            } catch (IOException e1) {
//                e1.printStackTrace();
            }
        } catch (IOException e) {
//            e.printStackTrace();
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
//            e.printStackTrace();
        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }

    private void writeVocabulary() {
        try {

            JSONArray jsonArray = new JSONArray();

            for (int index = 0; index < mVocabularies.size(); index++) {
                Vocabulary vocabulary = mVocabularies.get(index);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", vocabulary.getID());
                jsonObject.put("spell", vocabulary.getSpell());
                jsonObject.put("category", vocabulary.getCategory());
                jsonObject.put("translate", vocabulary.getTranslate());
                jsonObject.put("audio", vocabulary.getAudio());
                jsonObject.put("en_sentence", vocabulary.getEn_sentence());
                jsonObject.put("cn_sentence", vocabulary.getCn_sentence());
                jsonObject.put("sentence_audio", vocabulary.getSentence_audio());

                jsonArray.put(jsonObject);
            }

            JSONObject vocabularyObject = new JSONObject();
            vocabularyObject.put("vocabulary", jsonArray);

                FileOutputStream fos = mContext.openFileOutput("vocabulary.json", Context.MODE_PRIVATE);
            Log.d(TAG, "open file to write");
            fos.write(vocabularyObject.toString().getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "file not found while writing");
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }

    private void writeBook() {
        try {

            JSONArray jsonArray = new JSONArray();

            for (int index = 0; index < mBooks.size(); index++) {
                Book book = mBooks.get(index);

                ArrayList<Integer> content = book.getContent();
                JSONArray contentArray = new JSONArray();
                for (int index2 = 0; index2 < content.size(); index2++) {
                    contentArray.put(content.get(index2));
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", book.getID());
                jsonObject.put("name", book.getName());
                jsonObject.put("content", contentArray);

                jsonArray.put(jsonObject);
            }

            JSONObject bookObject = new JSONObject();
            bookObject.put("book", jsonArray);

            FileOutputStream fos = mContext.openFileOutput("book.json", Context.MODE_PRIVATE);
            Log.d(TAG, "open file to write");
            fos.write(bookObject.toString().getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "file not found while writing");
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }

    private void writeLesson() {
        try {

            JSONArray jsonArray = new JSONArray();

            for (int index = 0; index < mLessons.size(); index++) {
                Lesson lesson = mLessons.get(index);

                ArrayList<Integer> content = lesson.getContent();
                JSONArray contentArray = new JSONArray();
                for (int index2 = 0; index2 < content.size(); index2++) {
                    contentArray.put(content.get(index2));
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", lesson.getID());
                jsonObject.put("name", lesson.getName());
                jsonObject.put("content", contentArray);

                jsonArray.put(jsonObject);
            }

            JSONObject lessonObject = new JSONObject();
            lessonObject.put("lesson", jsonArray);

            FileOutputStream fos = mContext.openFileOutput("lesson.json", Context.MODE_PRIVATE);
            Log.d(TAG, "open file to write");
            fos.write(lessonObject.toString().getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "file not found while writing");
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }

    private void writeNote() {
        try {

            JSONArray jsonArray = new JSONArray();

            for (int index = 0; index < mNotes.size(); index++) {
                Lesson note = mNotes.get(index);

                ArrayList<Integer> content = note.getContent();
                JSONArray contentArray = new JSONArray();
                for (int index2 = 0; index2 < content.size(); index2++) {
                    contentArray.put(content.get(index2));
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", note.getID());
                jsonObject.put("name", note.getName());
                jsonObject.put("content", contentArray);

                jsonArray.put(jsonObject);
            }

            JSONObject noteObject = new JSONObject();
            noteObject.put("note", jsonArray);

            FileOutputStream fos = mContext.openFileOutput("note.json", Context.MODE_PRIVATE);
            Log.d(TAG, "open file to write");
            fos.write(noteObject.toString().getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "file not found while writing");
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }

    private void writeOption() {
        try {

            JSONArray jsonArray = new JSONArray();

            for (int index = 0; index < mOptions.size(); index++) {
                Option option = mOptions.get(index);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("mode", option.getMode());
                jsonObject.put("random", option.isIsRandom());
                jsonObject.put("listloop", option.getListLoop());
                jsonObject.put("sentence", option.isSentence());
                jsonObject.put("stopperiod", option.getStopPeriod());
                jsonObject.put("itemloop", option.getItemLoop());
                jsonObject.put("speed", option.getSpeed());
                jsonObject.put("playtime", option.getPlayTime());

                jsonArray.put(jsonObject);
            }

            JSONObject optionObject = new JSONObject();
            optionObject.put("option", jsonArray);

            FileOutputStream fos = mContext.openFileOutput("option.json", Context.MODE_PRIVATE);
            Log.d(TAG, "open file to write");
            fos.write(optionObject.toString().getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "file not found while writing");
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }
}
