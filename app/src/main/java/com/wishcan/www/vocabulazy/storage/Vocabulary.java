package com.wishcan.www.vocabulazy.storage;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by allencheng07 on 2015/9/21.
 */
public class Vocabulary implements Parcelable {
    private static final String TAG = Vocabulary.class.getSimpleName();

    private int mID;
    private String mSpell;
    private String mCategory;
    private String mTranslate;
    private String mAudio;
    private String mEn_sentence;
    private String mCn_sentence;
    private String mSentence_audio;

    public Vocabulary(int id, String spell, String category, String translate, String audio,
                      String en_sentence, String cn_sentence, String sentence_audio) {
        mID = id;
        mSpell = spell;
        mCategory = category;
        mTranslate = translate;
        mAudio = audio;
        mEn_sentence = en_sentence;
        mCn_sentence = cn_sentence;
        mSentence_audio = sentence_audio;
    }

    protected Vocabulary(Parcel in) {
        mID = in.readInt();
        mSpell = in.readString();
        mCategory = in.readString();
        mTranslate = in.readString();
        mAudio = in.readString();
        mEn_sentence = in.readString();
        mCn_sentence = in.readString();
        mSentence_audio = in.readString();
    }

    public static final Creator<Vocabulary> CREATOR = new Creator<Vocabulary>() {
        @Override
        public Vocabulary createFromParcel(Parcel in) {
            return new Vocabulary(in);
        }

        @Override
        public Vocabulary[] newArray(int size) {
            return new Vocabulary[size];
        }
    };

    public String getAudio() {
        return mAudio;
    }

    public void setAudio(String mAudio) {
        this.mAudio = mAudio;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getCn_sentence() {
        return mCn_sentence;
    }

    public void setCn_sentence(String mCn_sentence) {
        this.mCn_sentence = mCn_sentence;
    }

    public String getEn_sentence() {
        return mEn_sentence;
    }

    public void setEn_sentence(String mEn_sentence) {
        this.mEn_sentence = mEn_sentence;
    }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public String getSentence_audio() {
        return mSentence_audio;
    }

    public void setSentence_audio(String mSentence_audio) {
        this.mSentence_audio = mSentence_audio;
    }

    public String getSpell() {
        return mSpell;
    }

    public void setSpell(String mSpell) {
        this.mSpell = mSpell;
    }

    public String getTranslate() {
        return mTranslate;
    }

    public void setTranslate(String mTranslate) {
        this.mTranslate = mTranslate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mID);
        dest.writeString(mSpell);
        dest.writeString(mCategory);
        dest.writeString(mTranslate);
        dest.writeString(mAudio);
        dest.writeString(mEn_sentence);
        dest.writeString(mCn_sentence);
        dest.writeString(mSentence_audio);
    }
}
