package wishcantw.vocabulazy.audio;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

import wishcantw.vocabulazy.database.AppPreference;
import wishcantw.vocabulazy.database.Database;
import wishcantw.vocabulazy.database.DatabaseUtils;
import wishcantw.vocabulazy.database.object.Vocabulary;

public class AudioPlayerUtils {

    // singleton
    private static AudioPlayerUtils audioPlayerUtils = new AudioPlayerUtils();

    // private constructor
    private AudioPlayerUtils() {}

    // singleton getter
    public static AudioPlayerUtils getInstance() {
        return audioPlayerUtils;
    }

    public enum PlayerState {
        IDLE,
        PLAYING,
        STOPPED,
        STOP_BY_SCROLLING,
        STOPPED_BY_FOCUS_CHANGE,
        SCROLLING_WHILE_PLAYING,
        SCROLLING_WHILE_STOPPED
    }

    public enum PlayerField {
        SPELL,
        TRANSLATE,
        EnSENTENCE,
        CnSENTENCE
    }

    /**
     * Make the player stop for a while
     *
     * @param milliseconds the number of milliseconds to be stopped
     */
    public void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load new content from database and callback to audio player
     *
     * @param database database instance
     * @param databaseUtils database utilities instance
     */
    public void loadNewContent(@NonNull Database database,
                               @NonNull DatabaseUtils databaseUtils) {
        // get instances
        AppPreference appPreference = AppPreference.getInstance();

        int currentBookIndex = appPreference.getPlayerBookIndex();
        int currentLessonIndex = appPreference.getPlayerLessonIndex();

        // get lesson/note amount
        int lessonAmount = (currentBookIndex == -1)
                ? databaseUtils.getNoteAmount(database.getNotes())
                : databaseUtils.getLessonAmount(database.getTextbooks(), currentBookIndex);

        // random an index of new lesson/note
        int newLessonIndex;
        Random random = new Random(System.currentTimeMillis());
        do {
            newLessonIndex = random.nextInt(lessonAmount);
        } while (currentLessonIndex == newLessonIndex);
        appPreference.setPlayerLessonIndex(newLessonIndex);

        // content ids of new lesson/note
        ArrayList<Integer> contentIds = (currentBookIndex == -1)
                ? databaseUtils.getNoteContent(database.getNotes(), newLessonIndex)
                : databaseUtils.getLessonContent(database.getTextbooks(), currentBookIndex, newLessonIndex);

        // the player content of new lesson/note
        ArrayList<Vocabulary> playerContent = databaseUtils.getVocabulariesByIDs(database.getVocabularies(), contentIds);

        // set new content to database
        database.setPlayerContent(playerContent);
    }

    /**
     * Pick up the next item to be played
     *
     * @param isRandom whether picking item randomly
     * @param contentAmount the amount of player content
     *
     * @return the index of the next item to be played
     */
    public int pickNextItem(boolean isRandom,
                            int contentAmount) {
        int currentItemIndex = AppPreference.getInstance().getPlayerItemIndex();
        int newItemIndex;

        if (isRandom) {
            Random random = new Random(System.currentTimeMillis());
            do {
                newItemIndex = random.nextInt(contentAmount);
            } while (currentItemIndex == newItemIndex);

        } else {
            newItemIndex = (currentItemIndex == contentAmount - 1)
                    ? -1
                    : currentItemIndex + 1;
        }

        return newItemIndex;
    }

    /**
     * Decide how long is the stopping period
     *
     * @param playerField the player field
     * @param stopPeriod the length of stop period in option settings
     *
     * @return the length of stopping period
     */
    public int decidePeriodLength(@NonNull AudioPlayerUtils.PlayerField playerField,
                                  int stopPeriod) {
        int defaultPeriodLength = 400;
        return (playerField.equals(PlayerField.TRANSLATE))
                ? defaultPeriodLength
                : defaultPeriodLength + stopPeriod * 1000;
    }
}
