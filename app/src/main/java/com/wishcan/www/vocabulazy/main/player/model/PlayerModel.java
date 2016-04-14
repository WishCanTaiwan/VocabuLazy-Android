package com.wishcan.www.vocabulazy.main.player.model;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.service.AudioService;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Option;
import com.wishcan.www.vocabulazy.storage.Preferences;
import com.wishcan.www.vocabulazy.storage.Vocabulary;
import com.wishcan.www.vocabulazy.main.player.view.PlayerMainView;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerModel {

    /**
     * The TAG string for debugging.
     */
    public static final String TAG = PlayerModel.class.getSimpleName();

    private static final String KEY_CONTENT_BUNDLE = "content-bundle";
    private static final String KEY_DATABASE_BOOK_INDEX = "book-index";
    private static final String KEY_DATABASE_LESSON_INDEX = "lesson-index";

    private Database wDatabase;
    private Preferences wPreferences;
    private ArrayList<Vocabulary> mVocabularies;
    private PlayerModelDataProcessListener wDataProcessListener;

	public PlayerModel(VLApplication application) {
        wDatabase = application.getDatabase();
        wPreferences = application.getPreferences();
	}

    public void createPlayerContent(ArrayList<Vocabulary> vocabularyArrayList) {
        new PlayerModelAsyncTask().execute(vocabularyArrayList);
    }

    public void createPlayerDetailContent(Vocabulary vocabulary) {
        new PlayerModelAsyncTask().execute(vocabulary);
    }

    public void getVocabulariesIn(int bookIndex, int lessonIndex) {
        new PlayerModelAsyncTask().execute(bookIndex, lessonIndex);
    }

    public int getNumOfLessons(int bookIndex) {
        return wDatabase.getNumOfLesson(bookIndex);
    }

    public Option getCurrentOption() {
        return wDatabase.getCurrentOption();
    }

    public ArrayList<Option> getDefaultOptions() {
        return wDatabase.getOptions();
    }

    public void setOptionAndMode(ArrayList<Option> optionArrayList, int optionMode) {
        wDatabase.setCurrentOptions(optionArrayList);
        wDatabase.setCurrentOptionMode(optionMode);
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
        wPreferences.wBookIndex = indices[0];
        wPreferences.wLessonIndex = indices[1];
        wPreferences.wItemIndex = indices[2];
        wPreferences.wSentenceIndex = indices[3];
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
        indices[0] = wPreferences.wBookIndex;
        indices[1] = wPreferences.wLessonIndex;
        indices[2] = wPreferences.wItemIndex;
        indices[3] = wPreferences.wSentenceIndex;
        return indices;
    }

    /**
     * Method for updating the status of player.
     * @param status the string representing the player's status.
     */
    public void updatePlayerStatus(String status) {
        wPreferences.wPlayerStatus = status;
    }

    /**
     * Call this method to check whether the player is playing.
     * @return the bool value of whether player status equals to STATUS_PLAYING
     */
    public boolean isPlayerPlaying() {
        String playerStatus = wPreferences.wPlayerStatus;
        return (playerStatus.equals(AudioService.STATUS_PLAYING));
    }

    private void setContentToPlayer(Activity activity, ArrayList<Vocabulary> vocabularies) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_CONTENT_BUNDLE, vocabularies);

        Intent intent = new Intent(activity, AudioService.class);
        intent.setAction(AudioService.ACTION_SET_CONTENT);
        intent.putExtra(KEY_CONTENT_BUNDLE, bundle);
        activity.startService(intent);
    }

    public void setDataProcessListener(PlayerModelDataProcessListener listener) {
        wDataProcessListener = listener;
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
                    ArrayList<String> transList = voc.getTranslate();
                    ArrayList<String> cateList = voc.getCategory();
                    for(int i = 0; i < transList.size() && i < cateList.size() && i < 2; i++) {
                        String newStr = "(" +cateList.get(i)+ ")" + transList.get(i);
                        hm.put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_CONTENT_FROM[1+i], newStr);
                    }
//            hm.put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_CONTENT_FROM[1], voc.getTranslationInOneString());

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
                                    voc.getKK());
                    playerDetailDataContent
                            .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[3],
                                    voc.getEn_Sentence());
                    playerDetailDataContent
                            .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[4],
                                    voc.getCn_Sentence());
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
//                Log.d(TAG, "player content created");
                LinkedList<HashMap> playerDataContent = (LinkedList<HashMap>) result;
                wDataProcessListener.onPlayerContentCreated(playerDataContent);
                return;
            }

            if (result instanceof HashMap) {
//                Log.d(TAG, "detail content created");
                HashMap<String, Object> playerDetailDataContent = (HashMap<String, Object>) result;
                wDataProcessListener.onDetailPlayerContentCreated(playerDetailDataContent);
                return;
            }

            if (result instanceof ArrayList) {
//                Log.d(TAG, "vocabularies loaded");
                ArrayList<Vocabulary> vocabularies = (ArrayList<Vocabulary>) result;
                wDataProcessListener.onVocabulariesGet(vocabularies);
                return;
            }

        }
    }

    public interface PlayerModelDataProcessListener {
        void onPlayerContentCreated(LinkedList<HashMap> playerDataContent);
        void onDetailPlayerContentCreated(HashMap<String, Object> playerDetailDataContent);
        void onVocabulariesGet(ArrayList<Vocabulary> vocabularies);
    }

}