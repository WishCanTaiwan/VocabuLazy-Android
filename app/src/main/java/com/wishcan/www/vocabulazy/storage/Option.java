package com.wishcan.www.vocabulazy.storage;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by allencheng07 on 2015/9/30.
 */
public class Option implements Parcelable {
    private static final String TAG = Option.class.getSimpleName();

    public int mMode;

    public boolean mIsRandom;
    public int mListLoop;
    public boolean mSentence;

    public int mStopPeriod;
    public int mItemLoop;
    public int mSpeed;
    public int mPlayTime;

    public Option(int mode, boolean isRandom, int listLoop, boolean sentence, int stopPeriod, int itemLoop, int speed, int playTime) {
        mMode = mode;
        mIsRandom = isRandom;
        mListLoop = listLoop;
        mSentence = sentence;
        mStopPeriod = stopPeriod;
        mItemLoop = itemLoop;
        mSpeed = speed;
        mPlayTime = playTime;
    }

    protected Option(Parcel in) {
        mMode = in.readInt();
        mListLoop = in.readInt();
        mStopPeriod = in.readInt();
        mItemLoop = in.readInt();
        mSpeed = in.readInt();
        mPlayTime = in.readInt();
    }

    public static final Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };

    public int getMode() {
        return mMode;
    }

    public boolean isIsRandom() {
        return mIsRandom;
    }

    public boolean isSentence() {
        return mSentence;
    }

    public int getListLoop() {
        return mListLoop;
    }

    public int getStopPeriod() {
        return mStopPeriod;
    }

    public int getItemLoop() {
        return mItemLoop;
    }

    public int getSpeed() {
        return mSpeed;
    }

    public int getPlayTime() {
        return mPlayTime;
    }

    public void setMode(int mode) {
        mMode = mode;
    }

    public void setIsRandom(boolean isRandom) {
        mIsRandom = isRandom;
    }

    public void setListLoop(int listLoop) {
        mListLoop = listLoop;
    }

    public void setSentence(boolean sentence) {
        mSentence = sentence;
    }

    public void setStopPeriod(int stopPeriod) {
        mStopPeriod = stopPeriod;
    }

    public void setItemLoop(int itemLoop) {
        mItemLoop = itemLoop;
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    public void setPlayTime(int playTime) {
        mPlayTime = playTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMode);
        dest.writeInt(mListLoop);
        dest.writeInt(mStopPeriod);
        dest.writeInt(mItemLoop);
        dest.writeInt(mSpeed);
        dest.writeInt(mPlayTime);
    }
}
