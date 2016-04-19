package com.wishcan.www.vocabulazy.storage.databaseObjects;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Vocabulary {

    public static final String TAG = Vocabulary.class.getSimpleName();

    private int id;
    private String spell;
    private String kk;

    private ArrayList<String> category;
    private ArrayList<String> translation;
    private String spell_audio;

    private ArrayList<String> en_sentence;
    private ArrayList<String> cn_sentence;
    private ArrayList<String> sentence_audio;

    public Vocabulary(int id, String spell, String kk,
                      ArrayList<String> category,
                      ArrayList<String> translation,
                      String spell_audio,
                      ArrayList<String> en_sentence,
                      ArrayList<String> cn_sentence,
                      ArrayList<String> sentence_audio) {
        setId(id);
        setSpell(spell);
        setKk(kk);
        setCategory(category);
        setTranslation(translation);
        setSpell_audio(spell_audio);
        setEn_sentence(en_sentence);
        setCn_sentence(cn_sentence);
        setSentence_audio(sentence_audio);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public String getKk() {
        return kk;
    }

    public void setKk(String kk) {
        this.kk = kk;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public ArrayList<String> getTranslation() {
        return translation;
    }

    public void setTranslation(ArrayList<String> translation) {
        this.translation = translation;
    }

    public String getSpell_audio() {
        return spell_audio;
    }

    public void setSpell_audio(String spell_audio) {
        this.spell_audio = spell_audio;
    }

    public ArrayList<String> getEn_sentence() {
        return en_sentence;
    }

    public void setEn_sentence(ArrayList<String> en_sentence) {
        this.en_sentence = en_sentence;
    }

    public ArrayList<String> getCn_sentence() {
        return cn_sentence;
    }

    public void setCn_sentence(ArrayList<String> cn_sentence) {
        this.cn_sentence = cn_sentence;
    }

    public ArrayList<String> getSentence_audio() {
        return sentence_audio;
    }

    public void setSentence_audio(ArrayList<String> sentence_audio) {
        this.sentence_audio = sentence_audio;
    }

    public String getTranslationInOneString() {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < translation.size(); index++) {
            builder.append(translation.get(index));
            if (index != translation.size() - 1) {
                builder.append("；");
            }
        }
        return builder.toString();
    }

    private <T> ArrayList<T> toArrayList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }
}
