package com.wishcan.www.vocabulazy.player.model;

import android.os.AsyncTask;
import android.view.View;

import com.wishcan.www.vocabulazy.application.VLApplication;
import com.wishcan.www.vocabulazy.player.view.PlayerMainView;
import com.wishcan.www.vocabulazy.player.view.PlayerOptionContentView;
import com.wishcan.www.vocabulazy.service.AudioPlayer;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.OptionSettings;
import com.wishcan.www.vocabulazy.storage.Preferences;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerModel {

    /**
     * The TAG string for debugging.
     */
    public static final String TAG = PlayerModel.class.getSimpleName();

    private Database wDatabase;
    private Preferences wPreferences;
    private ArrayList<Vocabulary> mVocabularies;
    private PlayerModelDataProcessListener wDataProcessListener;

	public PlayerModel(VLApplication application) {
        wDatabase = Database.getInstance();
        wPreferences = application.getPreferences();
	}

    public void setDataProcessListener(PlayerModelDataProcessListener listener) {
        wDataProcessListener = listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public int getBookIndex() {
        return wPreferences.getBookIndex();
    }

    public int getLessonIndex() {
        return wPreferences.getLessonIndex();
    }

    public int getNumOfLessons(int bookIndex) {
        return wDatabase.getNumOfLesson(bookIndex);
    }

    public String getBookTitle(int bookIndex) {
        return wDatabase.getTextbookTitle(bookIndex);
    }

    public String getLessonTitle(int bookIndex, int lessonIndex) {
        return wDatabase.getLessonTitle(bookIndex, lessonIndex);
    }

    public ArrayList<Vocabulary> getCurrentContent() {
        return wPreferences.getCurrentContent();
    }

    public void setCurrentContent(ArrayList<Vocabulary> vocabularies) {
        wPreferences.setCurrentContent(vocabularies);
    }

    public OptionSettings getCurrentOptionSettings() {
        return wPreferences.getCurrentOptionSettings();
    }

    public ArrayList<OptionSettings> getOptionSettings() {
        return wPreferences.getOptionSettings();
    }

    public void setOptionSettingsAndMode(ArrayList<OptionSettings> optionSettings, int optionMode) {
        wPreferences.setOptionSettings(optionSettings);
        wPreferences.setOptionMode(optionMode);
    }

    public void updateOptionSettings(int optionItemId, int mode, View v) {
//        wPreferences.updateOptionSetting(optionItemId, mode);
        OptionSettings optionSettings = wPreferences.getOptionSettings().get(mode);
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
                int newSecond = (oldSecond+1) % 10;
                optionSettings.setStopPeriod(newSecond);
                break;
            case PlayerOptionContentView.IDX_OPTION_FREQUENCY:
                int oldFrequency = optionSettings.getItemLoop();
                int newFrequency = (oldFrequency-1+1) % 5 + 1; // frequency ranging from 1~5, thus "-1" for standardization and then "+1" for increasement.
                optionSettings.setItemLoop(newFrequency);
                break;
            case PlayerOptionContentView.IDX_OPTION_SPEED:
                int oldSpeed = optionSettings.getSpeed();
                int newSpeed = (oldSpeed-1+1) % 2 + 1;
                optionSettings.setSpeed(newSpeed);
                break;
            case PlayerOptionContentView.IDX_OPTION_PLAY_TIME:
                int oldPlayTime = optionSettings.getPlayTime();
                int newPlayTime = (oldPlayTime-10+1) % 30 + 10;
                optionSettings.setPlayTime(newPlayTime);
                break;
            default:
                break;
        }
        wPreferences.setOptionMode(mode);
    }

    public void updateIndices(int bookIndex, int lessonIndex, int itemIndex, int sentenceIndex) {
        wPreferences.setBookIndex(bookIndex);
        wPreferences.setLessonIndex(lessonIndex);
        wPreferences.setItemIndex(itemIndex);
        wPreferences.setSentenceIndex(sentenceIndex);
    }



    public boolean isPlaying() {
        return wPreferences.getPlayerState().equals(AudioPlayer.PLAYING);
    }

    /**
     * Call saveIndicesPreferences(int[]) to store/update the indices of player.
     * int[0] is book index.
     * int[1] is lesson index.
     * int[2] is item index.
     * int[3] is sentence index.
     * @param indices an array of int consists of four indices {book, lesson, item, sentence}
     */
    public void saveIndicesPreferences(int[] indices) {
        wPreferences.setBookIndex(indices[0]);
        wPreferences.setLessonIndex(indices[1]);
        wPreferences.setItemIndex(indices[2]);
        wPreferences.setSentenceIndex(indices[3]);
    }

    /**
     * Call loadIndicesPreferences to retrieve an array containing four indices of the player.
     * int[0] is book index.
     * int[1] is lesson index.
     * int[2] is item index.
     * int[3] is sentence index.
     * @return indices
     */
    public int[] loadIndicesPreferences() {
        int[] indices = new int[4];
        indices[0] = wPreferences.getBookIndex();
        indices[1] = wPreferences.getLessonIndex();
        indices[2] = wPreferences.getItemIndex();
        indices[3] = wPreferences.getSentenceIndex();
        return indices;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /* AsyncTasks */
    public void createPlayerContent(ArrayList<Vocabulary> vocabularyArrayList) {
        new PlayerModelAsyncTask().execute(vocabularyArrayList);
    }

    public void createPlayerDetailContent(Vocabulary vocabulary) {
        new PlayerModelAsyncTask().execute(vocabulary);
    }

    public void getVocabulariesIn(int bookIndex, int lessonIndex) {
        new PlayerModelAsyncTask().execute(bookIndex, lessonIndex);
    }

    private class PlayerModelAsyncTask extends AsyncTask<Object, Void, Object> {
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
                ArrayList<Integer> contentIDs = wDatabase.getContentIds(bookIndex, lessonIndex);
                mVocabularies = wDatabase.getVocabulariesByIDs(contentIDs);
                return mVocabularies;
            }

            return null;
        }

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

    public interface PlayerModelDataProcessListener {
        void onPlayerContentCreated(LinkedList<HashMap> playerDataContent);
        void onDetailPlayerContentCreated(HashMap<String, Object> playerDetailDataContent);
        void onVocabulariesGet(ArrayList<Vocabulary> vocabularies);
    }

}