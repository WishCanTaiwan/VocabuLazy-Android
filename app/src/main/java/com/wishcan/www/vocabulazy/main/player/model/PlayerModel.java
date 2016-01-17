package com.wishcan.www.vocabulazy.main.player.model;

import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Vocabulary;
import com.wishcan.www.vocabulazy.main.player.view.PlayerMainView;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;


public class PlayerModel {
	
	public PlayerModel() {

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

}