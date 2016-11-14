package wishcantw.vocabulazy.search.model;

import android.content.Context;
import android.support.annotation.NonNull;

import wishcantw.vocabulazy.database.DatabaseUtils;
import wishcantw.vocabulazy.search.view.SearchDetailView;
import wishcantw.vocabulazy.search.view.SearchListView;
import wishcantw.vocabulazy.database.Database;
import wishcantw.vocabulazy.database.object.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class SearchModel {

    // singleton
    private static SearchModel searchModel = new SearchModel();

    // private constructor
    private SearchModel() {}

    // getter of the singleton
    public static SearchModel getInstance() {
        return searchModel;
    }

    private Database database;
    private DatabaseUtils databaseUtils;

    public void init() {
        if (database == null) {
            database = Database.getInstance();
        }

        if (databaseUtils == null) {
            databaseUtils = DatabaseUtils.getInstance();
        }
    }

    public void addVocToNote(int id, int index) {
        databaseUtils.addVocToNote(database.getNotes(), id, index);
    }

    public void addNewNote(String newNote) {
        databaseUtils.createNewNote(database.getNotes(), newNote);
    }

    public LinkedList<HashMap> createSearchResultMap(String searchStr) {

        ArrayList<Vocabulary> vocabularies = databaseUtils.readSuggestVocabularyBySpell(database.getVocabularies(), searchStr);

        if (vocabularies == null)
            return null;

        LinkedList<HashMap> dataMap = new LinkedList<>();
        for(Vocabulary voc : vocabularies){
            HashMap<String, Object> hm = new HashMap<>();
            String from[] = SearchListView.LIST_ITEM_CONTENT_FROM;

            hm.put("vocId", voc.getId());
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

    public LinkedList<String> getNoteNameList() {
        ArrayList<String> noteNameArrayList = databaseUtils.getNoteNames(database.getNotes());
        LinkedList<String> linkedList = new LinkedList<>();
        for (String string : noteNameArrayList) {
            linkedList.add(string);
        }
        return linkedList;
    }

    public void storeData(@NonNull Context context) {
        database.storeData(context);
    }
}
