package wishcantw.vocabulazy.application;

import android.app.Application;

import wishcantw.vocabulazy.BuildConfig;
import wishcantw.vocabulazy.ga.manager.GAManager;
import wishcantw.vocabulazy.storage.databaseObjects.OptionSettings;
import wishcantw.vocabulazy.storage.databaseObjects.Vocabulary;
//import wishcantw.vocabulazy.storage.Preferences;

import java.util.ArrayList;

/**
 * This class is created for giving a global overview to our application.
 */
public class GlobalVariable extends Application {

    // TAG for debugging
    public static final String TAG = "GlobalVariable";

    public static final String PLAYER_BROADCAST_INTENT = "player-broadcast-intent";
    public static final String PLAYER_BROADCAST_ACTION = "player-broadcast-action";

    public int playerTextbookIndex = 1359;
    public int playerLessonIndex = 1359;

    public ArrayList<Vocabulary> playerContent;
    public ArrayList<OptionSettings> optionSettings;
    public int optionMode = 0;
    public int playerItemIndex = -1;
    public int playerSentenceIndex = -1;
    public String playerState;
    public String playingField;

    public int playerItemLoop;
    public int playerListLoop;
    public int playerPlayTime;

    @Override
    public void onCreate() {
        super.onCreate();

        // init google analytics services
        GAManager.getInstance().init(getApplicationContext());

        // if it is DEBUG mode, disable the GA temporary
        if (BuildConfig.DEBUG) {
            GAManager.getInstance().disableGATemporary(true);
        }
    }
}
