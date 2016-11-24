package wishcantw.vocabulazy.database;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import wishcantw.vocabulazy.database.object.Note;
import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.database.object.Textbook;
import wishcantw.vocabulazy.database.object.Vocabulary;

public class Database {

    // tag for debugging
    public static final String TAG = Database.class.getSimpleName();

    // singleton
    private static Database database = new Database();

    // private constructor
    private Database() {}

    // getter of singleton
    public static Database getInstance() {
        return database;
    }

    // datas
    private ArrayList<Vocabulary> mVocabularies = new ArrayList<>();
    private ArrayList<Textbook> mTextbooks = new ArrayList<>();
    private ArrayList<Note> mNotes = new ArrayList<>();
    private ArrayList<OptionSettings> mOptionSettings = new ArrayList<>();
    private ArrayList<Vocabulary> mPlayerContent = new ArrayList<>();

    public void init(@NonNull final Context context, final DatabaseCallback callback) {
        final FileLoader fileLoader = FileLoader.getInstance();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mVocabularies = fileLoader.loadVocabularies(context);
                mTextbooks = fileLoader.loadTextbook(context);
                mNotes = fileLoader.loadNote(context);
                mOptionSettings = fileLoader.loadOptionSettings(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (mVocabularies.isEmpty() || mTextbooks.isEmpty() || mOptionSettings.isEmpty()) {
                    callback.failed();
                } else {
                    callback.succeed();
                }
            }
        }.execute();

    }

    public void storeData(@NonNull final Context context,
                          @NonNull final boolean isFromMainMenu) {
        final FileWriter fileWriter = FileWriter.getInstance();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (!isFromMainMenu) {
//                    ((ParentActivity) context).showProgressDialog(context, "Storing data", "Please be patient");
                }
                fileWriter.writeNote(context, mNotes);
                fileWriter.writeOptionSettings(context, mOptionSettings);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isFromMainMenu) {
//                    ((ParentActivity) context).dismissDialog();
//                    ((ParentActivity) context).onBackPressed();
                }
            }
        }.execute();
    }

    /**
     * Get the vocabularies
     *
     * @return the array list of vocabularies
     */
    public ArrayList<Vocabulary> getVocabularies() {
        return (mVocabularies == null)
                ? new ArrayList<Vocabulary>()
                : mVocabularies;
    }

    /**
     * Get the textbooks
     *
     * @return the array list of textbooks
     */
    public ArrayList<Textbook> getTextbooks() {
        return (mTextbooks == null)
                ? new ArrayList<Textbook>()
                : mTextbooks;
    }

    /**
     * Get the notes
     *
     * @return the array list of notes
     */
    public ArrayList<Note> getNotes() {
        return (mNotes == null)
                ? new ArrayList<Note>()
                : mNotes;
    }

    /**
     * Get the option settings
     *
     * @return the array list of option settings
     */
    public ArrayList<OptionSettings> getOptionSettings() {
        return (mOptionSettings == null)
                ? new ArrayList<OptionSettings>()
                : mOptionSettings;
    }

    public ArrayList<Vocabulary> getPlayerContent() {
        return (mPlayerContent == null)
                ? new ArrayList<Vocabulary>()
                : mPlayerContent;
    }

    public void setPlayerContent(@NonNull ArrayList<Vocabulary> playerContent) {
        this.mPlayerContent = playerContent;
    }

    public OptionSettings getPlayerOptionSettings() {
        int mode = AppPreference.getInstance().getPlayerOptionMode();
        return mOptionSettings.get(mode);
    }

    public void setPlayerOptionSettings(OptionSettings optionSettings) {
        int mode = AppPreference.getInstance().getPlayerOptionMode();
        mOptionSettings.set(mode, optionSettings);
    }
}
