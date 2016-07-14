package com.wishcan.www.vocabulazy.storage.databaseObjects;

/**
 * Using this class to represent OptionSetting objects which are read from the database.\n
 *
 * @author Allen Cheng Yu-Lun
 * @version 1.0
 * @since 1.0
 */
public class OptionSettings {

    /**
     * String for Debugging.
     */
    public static final String TAG = OptionSettings.class.getSimpleName();

    /**
     * Value for {@link #mode}. MODE_DEFAULT is the default option settings.
     */
    public static final int MODE_DEFAULT = 0;

    /**
     * Representing the mode of the OptionSettings. MODE_SLEEPING is the mode used while user is sleeping.
     */
    public static final int MODE_SLEEPING = 1;

    /**
     * Representing the mode of the OptionSettings. MODE_COMMUTING is the mode used while user is commuting.
     */
    public static final int MODE_COMMUTING = 2;

    /**
     * Representing the mode of the OptionSettings.
     */
    private int mode;

    /**
     * This variable
     */
    private boolean random;

    /**
     *
     */
    private int listLoop;

    /**
     *
     */
    private boolean sentence;

    /**
     *
     */
    private int stopPeriod;

    /**
     *
     */
    private int itemLoop;

    /**
     *
     */
    private int speed;

    /**
     *
     */
    private int playTime;

    /**
     * @param mode
     * @param isRandom
     * @param listLoop
     * @param sentence
     * @param stopPeriod
     * @param itemLoop
     * @param speed
     * @param playTime
     */
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
