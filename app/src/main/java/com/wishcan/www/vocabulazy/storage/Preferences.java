package com.wishcan.www.vocabulazy.storage;

import android.app.VoiceInteractor;

import com.wishcan.www.vocabulazy.main.player.view.PlayerOptionContentView;
import com.wishcan.www.vocabulazy.service.AudioPlayer;
import com.wishcan.www.vocabulazy.storage.databaseObjects.OptionSettings;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;

import java.util.ArrayList;

/**
 * Created by allencheng07 on 2016/4/14.
 *
 * The object is for storing the volatile arguments and parameters, which
 * should be separated from the @Database object.
 */
public class Preferences {
    
    public static final String VL_BROADCAST_INTENT = "vl-broadcast-intent";
    public static final String VL_BROADCAST_ACTION = "vl-broadcast-action";

    private int bookIndex = 1359;
    private int lessonIndex = 1359;
    private int itemIndex = 0;
    private int sentenceIndex = -1;

    private int itemLoop;
    private int listLoop;
    private int playTime;

    private ArrayList<Vocabulary> mVocabulariesInPlayer;
    private ArrayList<OptionSettings> mOptionSettings;
    private int mOptionMode;
    private String playerState = AudioPlayer.IDLE;
    private String playingField;

    public int getBookIndex() {
        return bookIndex;
    }

    public void setBookIndex(int bookIndex) {
        this.bookIndex = bookIndex;
    }

    public int getLessonIndex() {
        return lessonIndex;
    }

    public void setLessonIndex(int lessonIndex) {
        this.lessonIndex = lessonIndex;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }

    public int getSentenceIndex() {
        return sentenceIndex;
    }

    public void setSentenceIndex(int sentenceIndex) {
        this.sentenceIndex = sentenceIndex;
    }

    public void updateIndices(int bookIndex, int lessonIndex, int itemIndex, int sentenceIndex) {
        setBookIndex(bookIndex);
        setLessonIndex(lessonIndex);
        setItemIndex(itemIndex);
        setSentenceIndex(sentenceIndex);
    }

    public ArrayList<Vocabulary> getCurrentContent() {
        return mVocabulariesInPlayer;
    }

    public void setCurrentContent(ArrayList<Vocabulary> vocabularies) {
        mVocabulariesInPlayer = vocabularies;
    }

    public OptionSettings getCurrentOptionSettings() {
        return mOptionSettings.get(mOptionMode);
    }

    public ArrayList<OptionSettings> getOptionSettings() {
        return mOptionSettings;
    }

    public void setOptionSettings(ArrayList<OptionSettings> optionSettings) {
        mOptionSettings = optionSettings;
    }

    public void setOptionMode(int optionMode) {
        mOptionMode = optionMode;
    }

    public String getPlayerState() {
        return playerState;
    }

    public void setPlayerState(String playerState) {
        this.playerState = playerState;
    }

    public String getPlayingField() {
        return playingField;
    }

    public void setPlayingField(String playingField) {
        this.playingField = playingField;
    }

    public int getItemLoop() {
        return itemLoop;
    }

    public void setItemLoop(int itemLoop) {
        this.itemLoop = itemLoop;
    }

    public int getListLoop() {
        return listLoop;
    }

    public void setListLoop(int listLoop) {
        this.listLoop = listLoop;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }
}
