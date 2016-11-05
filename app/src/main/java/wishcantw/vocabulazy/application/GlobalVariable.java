package wishcantw.vocabulazy.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import wishcantw.vocabulazy.analytics.firebase.FirebaseManager;
import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.database.object.Vocabulary;

import java.util.ArrayList;

/**
 * This class is created for giving a global overview to our application.
 */
public class GlobalVariable extends Application {

    // TAG for debugging
    public static final String TAG = "GlobalVariable";

    public static final String PLAYER_BROADCAST_INTENT = "player-broadcast-intent";
    public static final String PLAYER_BROADCAST_ACTION = "player-broadcast-action";

    public static int playerTextbookIndex = 1359;
    public static int playerLessonIndex = 1359;

    public static ArrayList<Vocabulary> playerContent;
    public static ArrayList<OptionSettings> optionSettings;
    public static int optionMode = 0;
    public static int playerItemIndex = -1;
    public static int playerSentenceIndex = -1;
    public static String playerState;
    public static String playingField;

    public static int playerItemLoop;
    public static int playerListLoop;
    public static int playerPlayTime;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        // init firebase analytics service
        FirebaseManager.getInstance().init(GlobalVariable.this);
    }
}
