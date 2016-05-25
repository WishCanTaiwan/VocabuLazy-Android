package com.wishcan.www.vocabulazy.storage.databaseObjects;

import java.util.ArrayList;
import java.util.Arrays;

public class Vocabulary {

    public static final String TAG = Vocabulary.class.getSimpleName();

    private int id;
    private String spell;
    private String kk;
    private String category;
    private String translation;

    private ArrayList<String> enSentences;
    private ArrayList<String> cnSentences;

    public Vocabulary(int id, String spell, String kk,
                      String category,
                      String translation,
                      ArrayList<String> enSentences,
                      ArrayList<String> cnSentences) {
        setId(id);
        setSpell(spell);
        setKk(kk);
        setCategory(category);
        setTranslation(translation);
        setEnSentences(enSentences);
        setCnSentences(cnSentences);
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public ArrayList<String> getEnSentences() {
        return enSentences;
    }

    public void setEnSentences(ArrayList<String> enSentences) {
        this.enSentences = enSentences;
    }

    public ArrayList<String> getCnSentences() {
        return cnSentences;
    }

    public void setCnSentences(ArrayList<String> cnSentences) {
        this.cnSentences = cnSentences;
    }

    public int getSentenceAmount() {
        return enSentences.size();
    }
}
