package wishcantw.vocabulazy.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;

import io.fabric.sdk.android.Fabric;
import wishcantw.vocabulazy.R;
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

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        // init firebase analytics service
        FirebaseManager.getInstance().init(GlobalVariable.this);

        // init flurry
        new FlurryAgent.Builder()
                .withLogEnabled(false)
                .build(GlobalVariable.this, getString(R.string.flurry_api_key));
    }
}
