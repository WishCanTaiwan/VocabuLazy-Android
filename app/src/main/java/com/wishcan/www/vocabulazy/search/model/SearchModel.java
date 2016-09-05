package com.wishcan.www.vocabulazy.search.model;

import com.wishcan.www.vocabulazy.search.view.SearchDetailView;
import com.wishcan.www.vocabulazy.search.view.SearchListView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class SearchModel {

    private Database mDatabase;

    public SearchModel() {
        mDatabase = Database.getInstance();
    }

    public void addVocToNote(int id, int index) {
        mDatabase.addVocToNote(id, index);
    }

    public LinkedList<HashMap> createSearchResultMap(ArrayList<Vocabulary> vocabularies) {
        if (vocabularies == null)
            return null;

        LinkedList<HashMap> dataMap = new LinkedList<>();
        for(Vocabulary voc : vocabularies){
            HashMap<String, Object> hm = new HashMap<>();
            String from[] = SearchListView.LIST_ITEM_CONTENT_FROM;

            hm.put(from[SearchListView.IDX_VOC_SPELL], voc.getSpell());
            hm.put(from[SearchListView.IDX_VOC_TRANSLATION], voc.getTranslation());
            hm.put(from[SearchListView.IDX_VOC_CATEGORY], voc.getPartOfSpeech());
            dataMap.add(hm);
        }
        return dataMap;
    }

    public HashMap<String, Object> createSearchResultDetailMap(Vocabulary voc) {
        HashMap<String, Object> hm = new HashMap<>();
        String from[] = SearchDetailView.DETAIL_ITEM_CONTENT_FROM;
        hm.put(from[SearchDetailView.IDX_VOC_SPELL], voc.getSpell());
        hm.put(from[SearchDetailView.IDX_VOC_KK], voc.getPhonetic());
        hm.put(from[SearchDetailView.IDX_VOC_TRANSLATION], voc.getTranslation());
        hm.put(from[SearchDetailView.IDX_VOC_SENTENCE], voc.getEnSentence());
        hm.put(from[SearchDetailView.IDX_VOC_SENTENCE_TRANSLATION], voc.getCnSentence());
        return hm;
    }
}
