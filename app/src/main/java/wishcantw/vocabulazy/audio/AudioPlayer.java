package wishcantw.vocabulazy.audio;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Locale;

import wishcantw.vocabulazy.database.AppPreference;
import wishcantw.vocabulazy.database.Database;
import wishcantw.vocabulazy.database.DatabaseUtils;
import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.database.object.Vocabulary;
import wishcantw.vocabulazy.utility.Logger;

public class AudioPlayer {

    // singleton
    private static AudioPlayer audioPlayer = new AudioPlayer();

    // private constructor
    private AudioPlayer() {}

    // singleton getter
    public static AudioPlayer getInstance() {
        return audioPlayer;
    }

    private Database database;
    private DatabaseUtils databaseUtils;

    private VLTextToSpeech vlTextToSpeech;
    private AudioPlayerUtils audioPlayerUtils;
    private AudioServiceBroadcaster audioServiceBroadcaster;
    private Timer timer;

    private int itemLoop;
    private int listLoop;
    private int itemLoopCountDown;
    private int listLoopCountDown;
    private int spellLoopCountDown;

    private boolean isRandom;
    private int speed;
    private int stopPeriod;
    private int playTime;
    private boolean isTimeUp = false;

    private boolean isFromExam;

    public void init(@NonNull Context context,
                     @NonNull AudioServiceBroadcaster audioServiceBroadcaster) {
        if (database == null) {
            database = Database.getInstance();
        }

        if (databaseUtils == null) {
            databaseUtils = DatabaseUtils.getInstance();
        }

        if (vlTextToSpeech == null) {
            vlTextToSpeech = VLTextToSpeech.getInstance();
            vlTextToSpeech.init(context);
            vlTextToSpeech.setOnUtteranceFinishListener(new VLTextToSpeech.OnUtteranceFinishListener() {
                @Override
                public void onUtteranceFinished(String utterance) {
                    utteranceHasFinished();
                }
            });
        }

        if (audioPlayerUtils == null) {
            audioPlayerUtils = AudioPlayerUtils.getInstance();
        }

        if (timer == null) {
            timer = Timer.getInstance();
            timer.init(new Handler());
        }

        this.audioServiceBroadcaster = audioServiceBroadcaster;
        updateOptionSettings(database.getPlayerOptionSettings());
    }

    public void updateOptionSettings(OptionSettings optionSettings) {
        itemLoop = optionSettings.getItemLoop();
        listLoop = optionSettings.getListLoop();
        resetItemLoopCountDown();
        resetListLoopCountDown();

        isRandom = optionSettings.isRandom();
        speed = optionSettings.getSpeed();
        stopPeriod = optionSettings.getStopPeriod();
        playTime = optionSettings.getPlayTime();
    }

    public void resetItemLoopCountDown() {
        itemLoopCountDown = itemLoop;
    }

    public void resetListLoopCountDown() {
        listLoopCountDown = listLoop;
    }

    public void resetSpellLoopCountDown() {
        spellLoopCountDown = 3;
    }

    private void utteranceHasFinished() {
        if (isFromExam) {
            isFromExam = false;
            return;
        }

        AppPreference appPreference = AppPreference.getInstance();;

        int newItemIndex = appPreference.getPlayerItemIndex();

        AudioPlayerUtils.PlayerField playingField = appPreference.getPlayerField();
        AudioPlayerUtils.PlayerState playerState = appPreference.getPlayerState();

        if (playerState.equals(AudioPlayerUtils.PlayerState.SCROLLING_WHILE_PLAYING)
                || playerState.equals(AudioPlayerUtils.PlayerState.SCROLLING_WHILE_STOPPED)
                || playerState.equals(AudioPlayerUtils.PlayerState.STOPPED)) {
            return;
        }

        if (audioServiceBroadcaster == null) {
            return;
        }

        switch (playingField) {
            case SPELL:
                spellLoopCountDown--;
                // if SPELL has been played for three times, next will play TRANSLATION
                playingField = (spellLoopCountDown > 0)
                        ? AudioPlayerUtils.PlayerField.SPELL
                        : AudioPlayerUtils.PlayerField.TRANSLATE;
                break;

            case TRANSLATE:
                resetSpellLoopCountDown();

                itemLoopCountDown--;
                if (itemLoopCountDown > 0) {
                    playingField = AudioPlayerUtils.PlayerField.SPELL;
                    break;
                }

                audioServiceBroadcaster.onItemComplete();
                resetItemLoopCountDown();

                // pick up next item index
                newItemIndex = audioPlayerUtils.pickNextItem(isRandom, database.getPlayerContent().size());

                // if item index is -1,
                if (newItemIndex == -1) {
                    newItemIndex = 0;
                    listLoopCountDown--;
                    if (listLoopCountDown == 0) {
                        audioServiceBroadcaster.onListComplete();
                        resetListLoopCountDown();
                        audioPlayerUtils.loadNewContent(database, databaseUtils);
                        audioServiceBroadcaster.toNextList();
                        AudioPlayerUtils.getInstance().sleep(1500);
                    }
                }

                playingField = AudioPlayerUtils.PlayerField.SPELL;
                audioServiceBroadcaster.toItem(newItemIndex);
                break;

            default:
                break;
        }

        play(newItemIndex, playingField);
    }

    public void play(String utterance) {
        if (vlTextToSpeech == null) {
            return;
        }
        isFromExam = true;
        vlTextToSpeech.stop();
        vlTextToSpeech.setLanguage(Locale.ENGLISH);
        vlTextToSpeech.speak(utterance, 1 /* default speed */);
    }

    public void play(int itemIndex, AudioPlayerUtils.PlayerField playerField) {

        if (vlTextToSpeech == null || isTimeUp) {
            return;
        }

        // get player content
        ArrayList<Vocabulary> content = database.getPlayerContent();

        String string = "";
        switch (playerField) {
            case SPELL:
                string = content.get(itemIndex).getSpell();
                vlTextToSpeech.setLanguage(Locale.ENGLISH);
                break;

            case TRANSLATE:
                string = content.get(itemIndex).getTranslation();
                vlTextToSpeech.setLanguage(Locale.TAIWAN);
                break;

            default:
                Logger.d("AudioPlayer", "unexpected case in startPlayingItemAt: " + itemIndex + ", " +
                        "playing " + playerField);
                break;
        }

        AppPreference.getInstance().setPlayerItemIndex(itemIndex);
        AppPreference.getInstance().setPlayerField(playerField);
        AppPreference.getInstance().setPlayerState(AudioPlayerUtils.PlayerState.PLAYING);
        audioServiceBroadcaster.onPlayerStateChanged();

        vlTextToSpeech.speak(string, speed);
        vlTextToSpeech.speakSilence(audioPlayerUtils.decidePeriodLength(playerField, stopPeriod));

        // set up timer
        if (isTimeUp) {
            isTimeUp = false;
            timer.startTimer(playTime, new Timer.Callback() {
                @Override
                public void timeUp() {
                    super.timeUp();
                    isTimeUp = true;
                    vlTextToSpeech.stop();
                    AppPreference.getInstance().setPlayerState(AudioPlayerUtils.PlayerState.STOPPED);
                    audioServiceBroadcaster.onPlayerStateChanged();
                }
            });
        }

    }

    public void stop() {
        if (vlTextToSpeech == null) {
            return;
        }

        vlTextToSpeech.stop();
    }
}
