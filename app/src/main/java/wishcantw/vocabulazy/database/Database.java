package wishcantw.vocabulazy.database;

import android.content.Context;
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
import wishcantw.vocabulazy.database.object.Note;
import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.database.object.Textbook;
import wishcantw.vocabulazy.database.object.Vocabulary;
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
