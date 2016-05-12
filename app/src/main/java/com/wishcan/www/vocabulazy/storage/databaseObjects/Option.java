package com.wishcan.www.vocabulazy.storage.databaseObjects;

import android.os.Parcel;
import android.os.Parcelable;

public class Option {
    private static final String TAG = Option.class.getSimpleName();

    private int mode;

    private boolean random;
    private int listloop;
    private boolean sentence;

    private int stopperiod;
    private int itemloop;
    private int speed;
    private int playtime;

    private boolean[] bools = new boolean[2];

    public Option(int mode, boolean isRandom, int listLoop, boolean sentence, int stopPeriod, int itemLoop, int speed, int playTime) {
        setMode(mode);
        setRandom(isRandom);
        setListloop(listLoop);
        setSentence(sentence);
        setStopperiod(stopPeriod);
        setItemloop(itemLoop);
        setSpeed(speed);
        setPlaytime(playTime);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public int getListloop() {
        return listloop;
    }

    public void setListloop(int listloop) {
        this.listloop = listloop;
    }

    public boolean isSentence() {
        return sentence;
    }

    public void setSentence(boolean sentence) {
        this.sentence = sentence;
    }

    public int getStopperiod() {
        return stopperiod;
    }

    public void setStopperiod(int stopperiod) {
        this.stopperiod = stopperiod;
    }

    public int getItemloop() {
        return itemloop;
    }

    public void setItemloop(int itemloop) {
        this.itemloop = itemloop;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getPlaytime() {
        return playtime;
    }

    public void setPlaytime(int playtime) {
        this.playtime = playtime;
    }

//    protected Option(Parcel in) {
//        mode = in.readInt();
//        in.readBooleanArray(bools);
//        random = bools[0];
//        sentence = bools[1];
//        listloop = in.readInt();
//        stopperiod = in.readInt();
//        itemloop = in.readInt();
//        speed = in.readInt();
//        playtime = in.readInt();
//    }
//
//    public static final Creator<Option> CREATOR = new Creator<Option>() {
//        @Override
//        public Option createFromParcel(Parcel in) {
//            return new Option(in);
//        }
//
//        @Override
//        public Option[] newArray(int size) {
//            return new Option[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(mode);
//        bools[0] = random;
//        bools[1] = sentence;
//        dest.writeBooleanArray(bools);
//        dest.writeInt(listloop);
//        dest.writeInt(stopperiod);
//        dest.writeInt(itemloop);
//        dest.writeInt(speed);
//        dest.writeInt(playtime);
//    }
}
