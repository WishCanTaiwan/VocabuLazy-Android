package com.wishcan.www.vocabulazy.vocabulary;

import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by swallow on 2015/6/29.
 */
public class VocabularyList {

    private static final String TAG = VocabularyList.class.getSimpleName();

//    private LinkedList<WordObject> mWordObjList;
//    /**
//     * The Map Used for rapidly find out whether the word is recorded or not
//     */
//    private HashMap<String, Boolean> mWordObjStrHashMap;
//    private OnVocListUpdateListener mOnVocListUpdateListener;
//
//    private String mType;
//
//    public VocabularyList(String type) {
//        mType = type;
//        mWordObjList = new LinkedList<>();
//        mWordObjStrHashMap = new HashMap<>();
//    }
//
//    public void addToVocList(WordObject wordObj) {
//
//        if(mWordObjStrHashMap.containsKey(wordObj.getSpellStr()))
//            return;
//        mWordObjStrHashMap.put(wordObj.getSpellStr(), true);
//        mWordObjList.add(wordObj);
//        if (mType.equals("PLAYING"))
//            mOnVocListUpdateListener.onUpdate(mWordObjList);
//
//    }
//
//    public void deleteFromVocList(int index){
//        Log.d(TAG, " "+index);
//        WordObject wObj = mWordObjList.get(index);
//        String spellStr = wObj.getSpellStr();
//        mWordObjStrHashMap.remove(spellStr);
//        mWordObjList.remove(index);
//        if (mType.equals("PLAYING"))
//            mOnVocListUpdateListener.onUpdate(mWordObjList);
//    }
//
//    public LinkedList<WordObject> getVocList(){
//        return mWordObjList;
//    }
//
//    public void setOnVocListUpdateListener(OnVocListUpdateListener listener) {
//        mOnVocListUpdateListener = listener;
//    }
//
//    public interface OnVocListUpdateListener {
//
//        void onUpdate(LinkedList<WordObject> mVocList);
//    }
}
