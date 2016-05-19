package com.wishcan.www.vocabulazy.main.player.model;

import android.os.AsyncTask;
import android.util.Log;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.service.AudioPlayer;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.OptionSettings;
import com.wishcan.www.vocabulazy.storage.Preferences;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;
import com.wishcan.www.vocabulazy.main.player.view.PlayerMainView;

import java.lang.reflect.Array;
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
        wDatabase = application.getDatabase();
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
                    ArrayList<String> transList = voc.getTranslation();
                    ArrayList<String> cateList = voc.getCategory();
                    for(int i = 0; i < transList.size() && i < cateList.size() && i < 2; i++) {
                        String newStr = "(" +cateList.get(i)+ ")" + transList.get(i);
                        hm.put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_CONTENT_FROM[1+i], newStr);
                    }
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
                                    voc.getTranslationInOneString());
                    playerDetailDataContent
                            .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[2],
                                    voc.getKk());
                    playerDetailDataContent
                            .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[3],
                                    voc.getEn_sentence());
                    playerDetailDataContent
                            .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[4],
                                    voc.getCn_sentence());
                }
                return playerDetailDataContent;
            }

            if (params[0] instanceof Integer) {
                int bookIndex = (Integer) params[0];
                int lessonIndex = (Integer) params[1];
                ArrayList<Integer> contentIDs = wDatabase.getContentIDs(bookIndex, lessonIndex);
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