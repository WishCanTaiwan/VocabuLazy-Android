package com.wishcan.www.vocabulazy.storage;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by allencheng07 on 2015/9/21.
 */
public class Vocabulary implements Parcelable {
    private static final String TAG = Vocabulary.class.getSimpleName();

    public int mID;

    public String mSpell;
    public String mKK;
//    public String mCategory;
    public ArrayList<String> mCategory;
    public ArrayList<String> mTranslate;
    public String mAudio;

    public ArrayList<String> mEn_Sentence;
    public ArrayList<String> mCn_Sentence;
    public ArrayList<String> mSentence_Audio;

    public Vocabulary(int id, String spell, String kk,
                      /*String category*/ ArrayList<String> category, ArrayList<String> translate, String audio,
                      ArrayList<String> en_sentence, ArrayList<String> cn_sentence,
                      ArrayList<String> sentence_audio) {
        mID = id;

        mSpell = spell;
        mKK = kk;
        mCategory = category;
        mTranslate = translate;
        mAudio = audio;

        mEn_Sentence = en_sentence;
        mCn_Sentence = cn_sentence;
        mSentence_Audio = sentence_audio;
    }

    protected Vocabulary(Parcel in) {
        mID = in.readInt();

        mSpell = in.readString();
        mKK = in.readString();
//        mCategory = in.readString();
        mCategory = in.readArrayList(String.class.getClassLoader());
        mTranslate = in.readArrayList(String.class.getClassLoader());
        mAudio = in.readString();

        mEn_Sentence = in.readArrayList(String.class.getClassLoader());
        mCn_Sentence = in.readArrayList(String.class.getClassLoader());
        mSentence_Audio = in.readArrayList(String.class.getClassLoader());
    }

    public String toVocabularyString() {
        return "spell: " + mSpell +
                ", translation: " + mTranslate +
                ", EnSentence: [" + mEn_Sentence.get(0) +
                "], CnSentence: [" + mCn_Sentence + "]";
    }

    public int getID() {
        return mID;
    }

    public String getSpell() {
        return mSpell;
    }

    public String getKK() {
        return mKK;
    }

    public /*String*/ ArrayList<String> getCategory() {
        return mCategory;
    }

    public ArrayList<String>  getTranslate() {
        return mTranslate;
    }

    public String getTranslationInOneString() {
        StringBuilder builder = new StringBuilder();
//        Log.d(TAG, mTranslate.toString());
        for (int index = 0; index < mTranslate.size(); index++) {
//            Log.d(TAG, mTranslate.get(index).toString());
            builder.append(mTranslate.get(index).toString());
            if (index != mTranslate.size() - 1) {
                builder.append("；");
            }
        }

        return builder.toString();
    }

    public String getAudio() {
        return mAudio;
    }

    public ArrayList<String> getCn_Sentence() {
        return mCn_Sentence;
    }

    public ArrayList<String> getEn_Sentence() {
        return mEn_Sentence;
    }

    public ArrayList<String> getSentence_Audio() {
        return mSentence_Audio;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mID);

        dest.writeString(mSpell);
        dest.writeString(mKK);
//        dest.writeString(mCategory);
        dest.writeList(mCategory);
        dest.writeList(mTranslate);
        dest.writeString(mAudio);

        dest.writeList(mEn_Sentence);
        dest.writeList(mCn_Sentence);
        dest.writeList(mSentence_Audio);
    }
}
