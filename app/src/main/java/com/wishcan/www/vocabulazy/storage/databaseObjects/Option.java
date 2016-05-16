package com.wishcan.www.vocabulazy.storage.databaseObjects;

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

}
