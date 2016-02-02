package com.wishcan.www.vocabulazy.main.player.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.player.AudioService;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Option;
import com.wishcan.www.vocabulazy.storage.Vocabulary;
import com.wishcan.www.vocabulazy.main.player.view.PlayerMainView;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;


public class PlayerModel {

    private static final String TAG = PlayerModel.class.getSimpleName();

    private static final String KEY_CONTENT_BUNDLE = "content-bundle";

    private MainActivity mMainActivity;
    private Database mDatabase;
    private AudioService mAudioService;
    private ArrayList<Vocabulary> mVocabularies;

	public PlayerModel(MainActivity mainActivity) {
        mMainActivity = mainActivity;
        mDatabase = mainActivity.getDatabase();
	}

	public LinkedList<HashMap> createPlayerContent(ArrayList<Vocabulary> vocArrayList) {
		LinkedList<HashMap> playerDataContent = new LinkedList<>();
		for(Vocabulary voc : vocArrayList) {
            HashMap<String, String> hm = new HashMap<>();
            hm.put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_CONTENT_FROM[0], voc.getSpell());
            hm.put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_CONTENT_FROM[1], voc.getTranslationInOneString());
            playerDataContent.add(hm);
        }
        return playerDataContent;
	}

	public HashMap<String, Object> createPlayerDetailContent(Vocabulary voc) {
		HashMap<String, Object> playerDetailDataContent = new HashMap<>();
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
        return playerDetailDataContent;
	}

    public ArrayList<Vocabulary> getVocabulariesIn(int bookIndex, int lessonIndex) {
        ArrayList<Integer> contentIDs = mDatabase.getContentIDs(bookIndex, lessonIndex);
        mVocabularies = mDatabase.getVocabulariesByIDs(contentIDs);
        return mVocabularies;
    }

    public Option getCurrentOption() {
        return mDatabase.getCurrentOption();
    }

    public ArrayList<Option> getDefaultOptions() {
        return mDatabase.getOptions();
    }

    public void setOptionAndMode(ArrayList<Option> optionArrayList, int optionMode) {
        mDatabase.setCurrentOptions(optionArrayList);
        mDatabase.setCurrentOptionMode(optionMode);
    }

    private void setContentToPlayer(Activity activity, ArrayList<Vocabulary> vocabularies) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_CONTENT_BUNDLE, vocabularies);

        Intent intent = new Intent(activity, AudioService.class);
        intent.setAction(AudioService.ACTION_SET_CONTENT);
        intent.putExtra(KEY_CONTENT_BUNDLE, bundle);
        activity.startService(intent);
    }
}