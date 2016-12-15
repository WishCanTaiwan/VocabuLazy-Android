package wishcantw.vocabulazy.activities.player.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import wishcantw.vocabulazy.database.AppPreference;
import wishcantw.vocabulazy.database.DatabaseUtils;
import wishcantw.vocabulazy.activities.player.view.PlayerOptionView;
import wishcantw.vocabulazy.database.Database;
import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.database.object.Vocabulary;
import wishcantw.vocabulazy.audio.AudioPlayerUtils;

import java.util.LinkedList;
import java.util.ArrayList;

public class PlayerModel {

    // tag for debugging
    public static final String TAG = PlayerModel.class.getSimpleName();

    // singleton
    private static PlayerModel playerModel = new PlayerModel();

    // private constructor
    private PlayerModel() {}

    // singleton getter
    public static PlayerModel getInstance() {
        return playerModel;
    }

    // data instances
    private Database mDatabase;
    private DatabaseUtils mDatabaseUtils;
    private AppPreference appPreference;
    private PlayerModelDataProcessListener wDataProcessListener;

    // --------------------------------------------------------------- Player //

    /**
     * Initialize the data instances
     */
    public void init() {

        if (mDatabase == null) {
            mDatabase = Database.getInstance();
        }

        if (mDatabaseUtils == null) {
            mDatabaseUtils = DatabaseUtils.getInstance();
        }

        if (appPreference == null) {
            appPreference = AppPreference.getInstance();
        }
    }

    /**
     * Set data process listener
     *
     * @param listener the instance of data process listener
     */
    public void setDataProcessListener(@NonNull PlayerModelDataProcessListener listener) {
        wDataProcessListener = listener;
    }

    /**
     * Check whether the player is playing
     *
     * @return is player playing
     */
    public boolean isPlaying() {
        return (appPreference.getPlayerState().equals(AudioPlayerUtils.PlayerState.PLAYING));
    }

    /**
     * Store data
     *
     * @param context
     */
    public void storeData(@NonNull Context context) {
        mDatabase.storeData(context, false);
    }

    // --------------------------------------------------------------- Options //

    /**
     * Update the option settings triggered from view
     *
     * @param optionItemId the id of the option item
     * @param mode the mode of the option
     * @param v the view that has been changed
     * @param value the value that option has been shown
     */
    public void updateOptionSettings(int optionItemId,
                                     int mode,
                                     @NonNull View v,
                                     int value) {
        // set option mode
        appPreference.setPlayerOptionMode(mode);

        // get the player option settings
        OptionSettings optionSettings = mDatabase.getPlayerOptionSettings();

        switch (optionItemId) {

            case PlayerOptionView.IDX_OPTION_MODE:
                appPreference.setPlayerOptionMode(mode);
                break;

            case PlayerOptionView.IDX_OPTION_RANDOM:
                optionSettings.setRandom(!optionSettings.isRandom());
                break;

            case PlayerOptionView.IDX_OPTION_SENTENCE:
                optionSettings.setSentence(!optionSettings.isSentence());
                break;

            case PlayerOptionView.IDX_OPTION_REPEAT:
                optionSettings.setListLoop(value);
                break;

            case PlayerOptionView.IDX_OPTION_SECOND:
                optionSettings.setStopPeriod(value);
                break;

            case PlayerOptionView.IDX_OPTION_FREQUENCY:
                optionSettings.setItemLoop(value+1); // de-standardize value
                break;

            case PlayerOptionView.IDX_OPTION_SPEED:
                optionSettings.setSpeed(value);
                break;

            case PlayerOptionView.IDX_OPTION_PLAY_TIME:
                optionSettings.setPlayTime(value+10); // de-standardize value
                break;

            case PlayerOptionView.IDX_OPTION_VOICE:
                appPreference.setPlayerVolume((float) value);
                break;

            default:
                break;
        }

        // set the option settings
        mDatabase.setPlayerOptionSettings(optionSettings);
    }

    // --------------------------------------------------------------- Note //

    /**
     * Add a vocabulary to a note
     *
     * @param vocId the id of the vocabulary
     * @param noteIndex the index of the note
     */
    public void addVocToNote(int vocId,
                             int noteIndex) {
        mDatabaseUtils.addVocToNote(mDatabase.getNotes(), vocId, noteIndex);
    }

    /**
     * Create a new note
     *
     * @param newNote the name of the new note
     */
    public void addNewNote(@NonNull String newNote) {
        mDatabaseUtils.createNewNote(mDatabase.getNotes(), newNote);
    }

    /**
     * Get the linked list of the notes' name
     *
     * @return the linked list of note names
     */
    public LinkedList<String> getNoteNameList() {

        ArrayList<String> nameArrayList = mDatabaseUtils.getNoteNames(mDatabase.getNotes());
        LinkedList<String> nameLinkedList = new LinkedList<>();

        for (String name : nameArrayList) {
            nameLinkedList.add(name);
        }

        return nameLinkedList;
    }

    // --------------------------------------------------------------- Data Process //

    /**
     * Create player content
     *
     * @param vocabularyArrayList the array list of vocabularies
     */
    public void createPlayerContent(ArrayList<Vocabulary> vocabularyArrayList) {
        new PlayerModelAsyncTask(wDataProcessListener).execute(vocabularyArrayList);
    }

    /**
     * Create the detail content of a vocabulary
     *
     * @param vocabulary the vocabulary
     */
    public void createPlayerDetailContent(Vocabulary vocabulary) {
        new PlayerModelAsyncTask(wDataProcessListener).execute(vocabulary);
    }

    /**
     * Get the vocabularies of a certain lesson/note
     *
     * @param bookIndex the index of the book
     * @param lessonIndex the index of the lesson/note
     */
    public void getVocabulariesIn(int bookIndex, int lessonIndex) {
        new PlayerModelAsyncTask(wDataProcessListener).execute(bookIndex, lessonIndex);
    }

    // --------------------------------------------------------------- Getter and Setter //

    public String getTitle(@NonNull Context context,
                           int bookIndex,
                           int lessonIndex) {
        return (bookIndex == -1)
                ? mDatabaseUtils.getNoteTitle(context, mDatabase.getNotes(), lessonIndex)
                : mDatabaseUtils.getLessonTitle(context, mDatabase.getTextbooks(), bookIndex, lessonIndex);
    }

    public int getBookIndex() {
        return appPreference.getPlayerBookIndex();
    }

    public void setBookIndex(int bookIndex) {
        appPreference.setPlayerBookIndex(bookIndex);
    }

    public int getLessonIndex() {
        return appPreference.getPlayerLessonIndex();
    }

    public void setLessonIndex(int lessonIndex) {
        appPreference.setPlayerLessonIndex(lessonIndex);
    }

    public int getItemIndex() {
        return appPreference.getPlayerItemIndex();
    }

    public void setItemIndex(int itemIndex) {
        appPreference.setPlayerItemIndex(itemIndex);
    }

    @SuppressWarnings("unused")
    @NonNull
    public AudioPlayerUtils.PlayerField getPlayerField() {
        return appPreference.getPlayerField();
    }

    public void setPlayerField(@NonNull AudioPlayerUtils.PlayerField playerField) {
        appPreference.setPlayerField(playerField);
    }

    public int getNumOfLessons(int bookIndex) {
        return (bookIndex == -1)
                ? mDatabaseUtils.getNoteAmount(mDatabase.getNotes())
                : mDatabaseUtils.getLessonAmount(mDatabase.getTextbooks(), bookIndex);
    }

    @NonNull
    public ArrayList<Vocabulary> getPlayerContent() {
        return mDatabase.getPlayerContent();
    }

    public void setPlayerContent(@NonNull ArrayList<Vocabulary> vocabularies) {
        mDatabase.setPlayerContent(vocabularies);
    }

    public int getContentAmount(int bookIndex,
                                int lessonIndex) {
        return (bookIndex == -1)
                ? mDatabaseUtils.getNoteContent(mDatabase.getNotes(), lessonIndex).size()
                : mDatabaseUtils.getLessonContent(mDatabase.getTextbooks(), bookIndex, lessonIndex).size();
    }

    @NonNull
    public ArrayList<OptionSettings> getOptionSettings() {
        return mDatabase.getOptionSettings();
    }

    public OptionSettings getPlayerOptionSettings() {
        return mDatabase.getPlayerOptionSettings();
    }
}