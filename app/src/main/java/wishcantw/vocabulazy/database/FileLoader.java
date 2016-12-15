package wishcantw.vocabulazy.database;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.database.object.Note;
import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.database.object.Textbook;
import wishcantw.vocabulazy.database.object.Vocabulary;

public class FileLoader {

    // the singleton instance
    private static FileLoader fileLoader = new FileLoader();

    // make constructor private to prevent instantiating
    private FileLoader() {}

    // the getter of singleton
    public static synchronized FileLoader getInstance() {
        return fileLoader;
    }

    /**
     * Load vocabularies from file
     *
     * @param context the context
     *
     * @return the array list of vocabularies
     */
    public ArrayList<Vocabulary> loadVocabularies(@NonNull Context context) {
        ArrayList<Vocabulary> vocabularies = load(Vocabulary[].class, context.getResources().openRawResource(R.raw.vocabulary));
        return (vocabularies == null)
                ? new ArrayList<Vocabulary>()
                : vocabularies;
    }

    /**
     * Load textbook from file
     *
     * @param context the context
     *
     * @return the array list of textbooks
     */
    public ArrayList<Textbook> loadTextbook(@NonNull Context context) {
        ArrayList<Textbook> textbooks = load(Textbook[].class, context.getResources().openRawResource(R.raw.textbook));
        return (textbooks == null)
                ? new ArrayList<Textbook>()
                : textbooks;
    }

    /**
     * Load note from file
     *
     * @param context the context
     *
     * @return the array list of notes
     */
    public ArrayList<Note> loadNote(@NonNull Context context) {
        ArrayList<Note> notes;
        try {
            notes = load(Note[].class, context.openFileInput("note"));
        } catch (FileNotFoundException e) {
            notes = new ArrayList<>();
        }
        return (notes == null)
                ? new ArrayList<Note>()
                : notes;
    }

    /**
     * Load option settings from file
     *
     * @param context the context
     *
     * @return the array list of option settings
     */
    public ArrayList<OptionSettings> loadOptionSettings(@NonNull Context context,
                                                        boolean shouldLoadNewOption) {
        ArrayList<OptionSettings> optionSettings;
        try {
            optionSettings = load(OptionSettings[].class, (shouldLoadNewOption)
                    ? context.getResources().openRawResource(R.raw.option)
                    : context.openFileInput("optionSetting"));
        } catch (FileNotFoundException e) {
            optionSettings = load(OptionSettings[].class, context.getResources().openRawResource(R.raw.option));
        }
        return (optionSettings == null)
                ? new ArrayList<OptionSettings>()
                : optionSettings;
    }

    private <T> ArrayList<T> load(@NonNull Class<T[]> classOfT,
                                 @NonNull InputStream inputStream) {

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
        return (tArray.length == 0)
                ? new ArrayList<T>()
                : new ArrayList<>(Arrays.asList(tArray));
    }
}
