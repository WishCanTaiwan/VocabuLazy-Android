package wishcantw.vocabulazy.database;

import wishcantw.vocabulazy.audio.AudioPlayerUtils;

public class AppPreference {

    private static AppPreference appPreference = new AppPreference();

    private AppPreference() {}

    public static AppPreference getInstance() {
        return appPreference;
    }

    private boolean isAudioFocused;

    private int playerBookIndex;
    private int playerLessonIndex;
    private int playerItemIndex;
    private int playerOptionMode = 0;
    private AudioPlayerUtils.PlayerField playerField = AudioPlayerUtils.PlayerField.SPELL;
    private AudioPlayerUtils.PlayerState playerState = AudioPlayerUtils.PlayerState.IDLE;

    public int getPlayerBookIndex() {
        return playerBookIndex;
    }

    public void setPlayerBookIndex(int playerBookIndex) {
        this.playerBookIndex = playerBookIndex;
    }

    public int getPlayerLessonIndex() {
        return playerLessonIndex;
    }

    public void setPlayerLessonIndex(int playerLessonIndex) {
        this.playerLessonIndex = playerLessonIndex;
    }

    public int getPlayerItemIndex() {
        return playerItemIndex;
    }

    public void setPlayerItemIndex(int playerItemIndex) {
        this.playerItemIndex = playerItemIndex;
    }

    public int getPlayerOptionMode() {
        return playerOptionMode;
    }

    public void setPlayerOptionMode(int playerOptionMode) {
        this.playerOptionMode = playerOptionMode;
    }

    public AudioPlayerUtils.PlayerField getPlayerField() {
        return playerField;
    }

    public void setPlayerField(AudioPlayerUtils.PlayerField playerField) {
        this.playerField = playerField;
    }

    public AudioPlayerUtils.PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(AudioPlayerUtils.PlayerState playerState) {
        this.playerState = playerState;
    }

    public boolean isAudioFocused() {
        return isAudioFocused;
    }

    public void setAudioFocused(boolean audioFocused) {
        isAudioFocused = audioFocused;
    }
}
