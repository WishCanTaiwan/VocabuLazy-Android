package com.wishcan.www.vocabulazy.search.model;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.search.view.SearchView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by swallow on 2016/1/18.
 */
public class SearchModel {

    private Database mDatabase;

    public SearchModel(VLApplication vlApplication) {
        mDatabase = vlApplication.getDatabase();
    }

    public void addVocToNote(int id, int index) {
        mDatabase.addVocToNote(id, index);
    }

    public LinkedList<HashMap> createSearchResultMap(ArrayList<Vocabulary> vocabularies) {
        if(vocabularies == null)
            return null;

        LinkedList<HashMap> dataMap = new LinkedList<>();
        for(Vocabulary voc : vocabularies){
            HashMap<String, Object> hm = new HashMap<>();
            String from[] = SearchView.SearchListView.LIST_ITEM_CONTENT_FROM;

            hm.put(from[SearchView.SearchListView.LIST_ITEM_CONTENT_ID_s.VOC_SPELL.getValue()], voc.getSpell());
            hm.put(from[SearchView.SearchListView.LIST_ITEM_CONTENT_ID_s.VOC_TRANSLATION.getValue()], voc.getTranslation());
            hm.put(from[SearchView.SearchListView.LIST_ITEM_CONTENT_ID_s.VOC_CATEGORY.getValue()], voc.getCategory());
            dataMap.add(hm);
        }
        return dataMap;
    }

    public HashMap<String, Object> createSearchResultDetailMap(Vocabulary voc) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(SearchView.LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_SPELL_DETAIL.getResFrom(), voc.getSpell());
        hm.put(SearchView.LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_KK_DETAIL.getResFrom(), voc.getKk());
        hm.put(SearchView.LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_TRANSLATION_DETAIL.getResFrom(), voc.getTranslation());
        hm.put(SearchView.LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_SENTENCE_DETAIL.getResFrom(), voc.getEnSentences());
        hm.put(SearchView.LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_SENTENCE_TRANSLATION_DETAIL.getResFrom(), voc.getCnSentences());
        return hm;
    }
}
