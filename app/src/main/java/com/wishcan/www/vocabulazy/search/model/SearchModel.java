package com.wishcan.www.vocabulazy.search.model;

import com.wishcan.www.vocabulazy.search.view.SearchView;
import com.wishcan.www.vocabulazy.storage.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by swallow on 2016/1/18.
 */
public class SearchModel {

    public SearchModel() {

    }

    public LinkedList<HashMap> createSearchResultMap(ArrayList<Vocabulary> vocabularies) {
        if(vocabularies == null)
            return null;

        LinkedList<HashMap> dataMap = new LinkedList<>();
        for(Vocabulary voc : vocabularies){
            HashMap<String, Object> hm = new HashMap<>();
            String from[] = SearchView.SearchListView.LIST_ITEM_CONTENT_FROM;

            hm.put(from[SearchView.SearchListView.LIST_ITEM_CONTENT_ID_s.VOC_SPELL.getValue()], voc.getSpell());
            hm.put(from[SearchView.SearchListView.LIST_ITEM_CONTENT_ID_s.VOC_TRANSLATION.getValue()], voc.getTranslationInOneString());
            hm.put(from[SearchView.SearchListView.LIST_ITEM_CONTENT_ID_s.VOC_CATEGORY.getValue()], voc.getCategory());
            dataMap.add(hm);
        }
        return dataMap;
    }
}
