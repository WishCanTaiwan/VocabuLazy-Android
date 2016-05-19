package com.wishcan.www.vocabulazy.storage.databaseObjects;

public class OptionSettings {
    public static final String TAG = OptionSettings.class.getSimpleName();

    public static final int MODE_DEFAULT = 0;
    public static final int MODE_SLEEPING = 1;
    public static final int MODE_COMMUTING = 2;

    private int mode;

    private boolean random;
    private int listLoop;
    private boolean sentence;

    private int stopPeriod;
    private int itemLoop;
    private int speed;
    private int playTime;

    public OptionSettings(int mode, boolean isRandom, int listLoop, boolean sentence, int stopPeriod, int itemLoop, int speed, int playTime) {
        setMode(mode);
        setRandom(isRandom);
        setListLoop(listLoop);
        setSentence(sentence);
        setStopPeriod(stopPeriod);
        setItemLoop(itemLoop);
        setSpeed(speed);
        setPlayTime(playTime);
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

    public int getListLoop() {
        return listLoop;
    }

    public void setListLoop(int listLoop) {
        this.listLoop = listLoop;
    }

    public boolean isSentence() {
        return sentence;
    }

    public void setSentence(boolean sentence) {
        this.sentence = sentence;
    }

    public int getStopPeriod() {
        return stopPeriod;
    }

    public void setStopPeriod(int stopPeriod) {
        this.stopPeriod = stopPeriod;
    }

    public int getItemLoop() {
        return itemLoop;
    }

    public void setItemLoop(int itemLoop) {
        this.itemLoop = itemLoop;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }
}
