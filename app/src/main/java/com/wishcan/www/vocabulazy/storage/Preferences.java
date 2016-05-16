package com.wishcan.www.vocabulazy.storage;

import com.wishcan.www.vocabulazy.service.AudioPlayer;
import com.wishcan.www.vocabulazy.service.AudioService;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Option;
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

    private ArrayList<Vocabulary> mVocabularies;
    private Option mCurrentOptionSettings;
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
        return mVocabularies;
    }

    public void setCurrentContent(ArrayList<Vocabulary> vocabularies) {
        mVocabularies = vocabularies;
    }

    public Option getCurrentOptionSettings() {
        return mCurrentOptionSettings;
    }

    public void setCurrentOptionSettings(Option optionSettings) {
        mCurrentOptionSettings = optionSettings;
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
}
