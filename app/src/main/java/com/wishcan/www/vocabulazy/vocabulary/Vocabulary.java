package com.wishcan.www.vocabulazy.vocabulary;


import android.content.res.Resources;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by vc-swallow on 2015/6/11.
 */
public class Vocabulary {

    private static final String TAG = Vocabulary.class.getSimpleName();
//
//    private String mFileString;
//    private InputStream mDictionaryInputStream;
//    private JsonReader dictionaryJReader;
//    private Resources mResources;
//
//    private JSONObject mJSONObj;
//    private int previousIndex;
//

//
//    public Vocabulary(Resources res, String fileStr) {
//        mFileString = fileStr;
//        mResources = res;
//        try {
//            parseJSONData();
//
//            dictionaryJReader = new JsonReader(new InputStreamReader(mDictionaryInputStream));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e){
//            e.printStackTrace();
//        }
//    }
//
//    private void parseJSONData() throws IOException, JSONException {
//
//        mDictionaryInputStream = mResources.getAssets().open(mFileString);
//
//        int sizeOfJSONFile = mDictionaryInputStream.available();
//        Log.d(TAG, "Finish get file" + sizeOfJSONFile);
//        byte[] bytes = new byte[sizeOfJSONFile];
//        mDictionaryInputStream.read(bytes);
//        mDictionaryInputStream.close();
//
//        String JSONString = new String(bytes, "UTF-8");
//        mJSONObj = new JSONObject(JSONString);
//
//    }
//
//    public LinkedList<WordObject> readVocabularyByLesson(int inputLesson){
//
//        LinkedList<WordObject> outputWordLL = new LinkedList<WordObject>();
//
//        try{
//            JSONArray jArray = mJSONObj.getJSONArray("voc_database");
//
//            int i = 0;                  // initial index
//            int j = jArray.length();    // end index
//            int m = (i+j)/2;            // middle index, will be updated in every loop
//
//            if(inputLesson < i || inputLesson > j) {
//                System.out.println("out of lessons' range bound");
//                return null;
//            }
//
//            previousIndex = 0;
//
//            for (int ii = previousIndex ; ii < j; ii++){
//                JSONObject jObj = jArray.getJSONObject(ii);
//                int voc_lesson = Integer.parseInt(jObj.get("voc_lesson").toString());
//                previousIndex = ii;
//                if(voc_lesson < inputLesson)
//                    continue;
//                else if(voc_lesson > inputLesson)
//                    break;
//
//
//                WordObject wObj = createWordObject(jObj);
//                outputWordLL.add(wObj);
//                continue;
//            }
//        } catch(JSONException e){
//            e.printStackTrace();
//        }
//
//        Iterator<WordObject> ii = outputWordLL.iterator();
//        while(ii.hasNext()){
//            WordObject wObj = ii.next();
//            Log.d(TAG, wObj.getSpellStr());
//        }
//
//        return outputWordLL;
//    }
//
//    public WordObject readVocabularyBySpell(String inputStr){
//
//        WordObject outputWord = null;
//
//        System.out.println("Check " +inputStr);
//
//        try {
//            JSONArray jArray = mJSONObj.getJSONArray("voc_database");
//
//            for ( int m=0; m < jArray.length(); m++) {
//
//                JSONObject jObj = jArray.getJSONObject(m);
//                String voc_spell = jObj.get("voc_spell").toString();
//                if (voc_spell.equals(inputStr)) {
//                    System.out.println("Found");
//                    outputWord = createWordObject(jObj);
//                    break;
//                }
//
//            }
//
//        } catch(JSONException e){
//            e.printStackTrace();
//        }
//
//        if (outputWord == null)
//            System.out.println("Search failed");
//
//        return outputWord;
//    }
////
//    public LinkedList<WordObject> readSuggestVocabularyBySpell(String inputStr){
//        LinkedList<WordObject> outputWordLL = new LinkedList<WordObject>();
//
//        try{
//            JSONArray jArray = mJSONObj.getJSONArray("voc_database");
//            for ( int m=0; m < jArray.length(); m++) {
//
//                JSONObject jObj = jArray.getJSONObject(m);
//                String voc_spell = jObj.get("voc_spell").toString();
//
//                int inputStrLength = inputStr.length();
//                if(voc_spell.length() < inputStrLength)
//                    continue;
//                if(voc_spell.substring(0,inputStrLength).equals(inputStr)){
//                    outputWordLL.add(createWordObject(jObj));
//                    if(outputWordLL.size() > MAXIMUM_LIST_SIZE)
//                        break;
//                    continue;
//                }
//
//            }
//        } catch(Exception e){
//            e.printStackTrace();
//        }
//
//        return outputWordLL;
//    }
//
//    public int getLessonsCount(){
//        return 6;
//    }
//
//    private WordObject createWordObject(JSONObject jObj) throws JSONException{
//
//        String voc_spell = jObj.get("voc_spell").toString();
//        String voc_translation = jObj.get("voc_translation").toString();
//        String voc_sentence = jObj.get("voc_sentence").toString();
//        int voc_lesson = Integer.parseInt(jObj.get("voc_lesson").toString());
//        String voc_radio = jObj.get("voc_translation").toString();
//
//        return new WordObject(voc_spell, voc_translation, voc_sentence, voc_lesson, voc_radio);
//
//    }


}
