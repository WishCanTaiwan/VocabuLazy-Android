package wishcantw.vocabulazy.player.model;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.view.View;

import wishcantw.vocabulazy.application.GlobalVariable;
import wishcantw.vocabulazy.database.AppPreference;
import wishcantw.vocabulazy.database.DatabaseUtils;
import wishcantw.vocabulazy.player.view.PlayerMainView;
import wishcantw.vocabulazy.player.view.PlayerOptionContentView;
import wishcantw.vocabulazy.player.view.PlayerOptionView;
import wishcantw.vocabulazy.service.AudioPlayer;
import wishcantw.vocabulazy.database.Database;
import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.database.object.Vocabulary;
import wishcantw.vocabulazy.service.AudioPlayerUtils;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerModel {

    public interface PlayerModelDataProcessListener {
        void onPlayerContentCreated(LinkedList<HashMap> playerDataContent);
        void onDetailPlayerContentCreated(HashMap<String, Object> playerDetailDataContent);
        void onVocabulariesGet(ArrayList<Vocabulary> vocabularies);
    }

    // tag for debugging
    public static final String TAG = PlayerModel.class.getSimpleName();

    // singleton
    private static PlayerModel playerModel = new PlayerModel();

    // private constructor
    private PlayerModel() {}

    public static PlayerModel getInstance() {
        return playerModel;
    }

    private GlobalVariable mGlobalVariable;
    private Database mDatabase;
    private DatabaseUtils mDatabaseUtils;
    private AppPreference appPreference;
    private PlayerModelDataProcessListener wDataProcessListener;


    public void init(Context context) {
        mGlobalVariable = (GlobalVariable) context;

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

    public void setDataProcessListener(PlayerModelDataProcessListener listener) {
        wDataProcessListener = listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

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

    public AudioPlayerUtils.PlayerField getPlayerField() {
        return appPreference.getPlayerField();
    }

    public void setPlayerField(AudioPlayerUtils.PlayerField playerField) {
        appPreference.setPlayerField(playerField);
    }

    public int getNumOfLessons(int bookIndex) {
        return (bookIndex == -1)
                ? mDatabaseUtils.getNoteAmount(mDatabase.getNotes())
                : mDatabaseUtils.getLessonAmount(mDatabase.getTextbooks(), bookIndex);
    }

    public ArrayList<Vocabulary> getPlayerContent() {
        return mDatabase.getPlayerContent();
    }

    public void setPlayerContent(@NonNull ArrayList<Vocabulary> vocabularies) {
        mDatabase.setPlayerContent(vocabularies);
    }

    public int getContentAmount(int bookIndex, int lessonIndex) {
        return (bookIndex == -1)
                ? mDatabaseUtils.getNoteContent(mDatabase.getNotes(), lessonIndex).size()
                : mDatabaseUtils.getLessonContent(mDatabase.getTextbooks(), bookIndex, lessonIndex).size();
    }

    public ArrayList<OptionSettings> getOptionSettings() {
        return mDatabase.getOptionSettings();
    }

    public void updateOptionSettings(int optionItemId, int mode, View v, int leftOrRight) {

        OptionSettings optionSettings = mDatabase.getPlayerOptionSettings();
        switch (optionItemId) {
            case PlayerOptionContentView.IDX_OPTION_RANDOM:
                boolean oldRandom = optionSettings.isRandom();
                boolean newRandom = !oldRandom;
                optionSettings.setRandom(newRandom);
                break;
            case PlayerOptionContentView.IDX_OPTION_REPEAT:
                int oldRepeatVal = optionSettings.getListLoop();
                int newRepeatVal = (oldRepeatVal+1) % 5;
                optionSettings.setListLoop(newRepeatVal);
                break;
            case PlayerOptionContentView.IDX_OPTION_SENTENCE:
                boolean oldSentence = optionSettings.isSentence();
                boolean newSentence = !oldSentence;
                optionSettings.setSentence(newSentence);
                break;
            case PlayerOptionContentView.IDX_OPTION_SECOND:
                int oldSecond = optionSettings.getStopPeriod();
                int newSecond = (oldSecond+leftOrRight) % 10;
                optionSettings.setStopPeriod(newSecond);
                break;
            case PlayerOptionContentView.IDX_OPTION_FREQUENCY:
                int oldFrequency = optionSettings.getItemLoop();
                int newFrequency = (oldFrequency-1+leftOrRight) % 5 + 1; // frequency ranging from 1~5, thus "-1" for standardization and then "+1" for increasement.
                optionSettings.setItemLoop(newFrequency);
                break;
            case PlayerOptionContentView.IDX_OPTION_SPEED:
                int oldSpeed = optionSettings.getSpeed();
                int newSpeed = (oldSpeed-1+leftOrRight) % 2 + 1;
                optionSettings.setSpeed(newSpeed);
                break;
            case PlayerOptionContentView.IDX_OPTION_PLAY_TIME:
                int oldPlayTime = optionSettings.getPlayTime();
                int newPlayTime = (oldPlayTime-10+leftOrRight) % 30 + 10;
                optionSettings.setPlayTime(newPlayTime);
                break;
            case PlayerOptionView.PlayerOptionTabView.IDX_OPTION_TAB_0:
            case PlayerOptionView.PlayerOptionTabView.IDX_OPTION_TAB_1:
            case PlayerOptionView.PlayerOptionTabView.IDX_OPTION_TAB_2:
                break;
            default:
                break;
        }
        appPreference.setPlayerOptionMode(mode);
    }

    public boolean isPlaying() {
        return (appPreference.getPlayerState().equals(AudioPlayerUtils.PlayerState.PLAYING));
    }

    public void addVocToNote(int vocId, int noteIndex) {
        mDatabaseUtils.addVocToNote(mDatabase.getNotes(), vocId, noteIndex);
    }

    public void addNewNote(String newNote) {
        mDatabaseUtils.createNewNote(mDatabase.getNotes(), newNote);
    }

    public LinkedList<String> getNoteNameList() {
        return toLinkedList(mDatabaseUtils.getNoteNames(mDatabase.getNotes()));
    }

    private <T> LinkedList<T> toLinkedList(ArrayList<T> arrayList) {
        LinkedList<T> linkedList = new LinkedList<>();
        for (T t : arrayList) {
            linkedList.add(t);
        }
        return linkedList;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param vocabularyArrayList
     */
    public void createPlayerContent(ArrayList<Vocabulary> vocabularyArrayList) {
        new PlayerModelAsyncTask().execute(vocabularyArrayList);
    }

    /**
     *
     * @param vocabulary
     */
    public void createPlayerDetailContent(Vocabulary vocabulary) {
        new PlayerModelAsyncTask().execute(vocabulary);
    }

    /**
     *
     * @param bookIndex
     * @param lessonIndex
     */
    public void getVocabulariesIn(int bookIndex, int lessonIndex) {
        new PlayerModelAsyncTask().execute(bookIndex, lessonIndex);
    }

    /**
     *
     */
    private class PlayerModelAsyncTask extends AsyncTask<Object, Void, Object> {
        @SuppressWarnings("unchecked")
        @Override
        protected Object doInBackground(Object... params) {

            if (params[0] instanceof ArrayList) {
                ArrayList<Vocabulary> vocArrayList = (ArrayList<Vocabulary>) params[0];
                LinkedList<HashMap> playerDataContent = new LinkedList<>();
                for(Vocabulary voc : vocArrayList) {
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_CONTENT_FROM[0], voc.getSpell());
                    hm.put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_CONTENT_FROM[1], "[" + voc.getPartOfSpeech() + "] " + voc.getTranslation());
                    hm.put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_CONTENT_FROM[2], voc.getPhonetic());
                    playerDataContent.add(hm);
                }
                return playerDataContent;
            }

            if (params[0] instanceof Vocabulary) {
                Vocabulary voc = (Vocabulary) params[0];
                HashMap<String, Object> playerDetailDataContent = new HashMap<>();
                if (voc != null) {
                    playerDetailDataContent
                            .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[0],
                                    voc.getSpell());
                    playerDetailDataContent
                            .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[1],
                                    voc.getTranslation());
                    playerDetailDataContent
                            .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[2],
                                    voc.getPhonetic());
                    playerDetailDataContent
                            .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[3],
                                    voc.getEnSentence());
                    playerDetailDataContent
                            .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[4],
                                    voc.getCnSentence());
                }
                return playerDetailDataContent;
            }

            if (params[0] instanceof Integer) {
                int bookIndex = (Integer) params[0];
                int lessonIndex = (Integer) params[1];
                ArrayList<Integer> contentIDs = (bookIndex == -1)
                        ? mDatabaseUtils.getNoteContent(mDatabase.getNotes(), lessonIndex)
                        : mDatabaseUtils.getLessonContent(mDatabase.getTextbooks(), bookIndex, lessonIndex);
                return mDatabaseUtils.getVocabulariesByIDs(mDatabase.getVocabularies(), contentIDs);
            }

            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (result instanceof LinkedList) {
                LinkedList<HashMap> playerDataContent = (LinkedList<HashMap>) result;
                wDataProcessListener.onPlayerContentCreated(playerDataContent);
                return;
            }

            if (result instanceof HashMap) {
                HashMap<String, Object> playerDetailDataContent = (HashMap<String, Object>) result;
                wDataProcessListener.onDetailPlayerContentCreated(playerDetailDataContent);
                return;
            }

            if (result instanceof ArrayList) {
                ArrayList<Vocabulary> vocabularies = (ArrayList<Vocabulary>) result;
                wDataProcessListener.onVocabulariesGet(vocabularies);
            }
        }
    }
}