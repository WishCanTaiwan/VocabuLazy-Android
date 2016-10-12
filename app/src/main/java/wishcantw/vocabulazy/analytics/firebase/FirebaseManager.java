package wishcantw.vocabulazy.analytics.firebase;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.Set;

import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.utility.Logger;

public class FirebaseManager {

    public static final String TAG = "FirebaseManager";

    /** Singleton pattern **/

    private static final FirebaseManager firebaseManager = new FirebaseManager();

    private FirebaseManager() {

    }

    public static FirebaseManager getInstance() {
        return firebaseManager;
    }

    /****/

    private FirebaseAnalytics mFirebaseAnalytics;

    public void init(Context context) {
        Logger.d(FirebaseManager.TAG, "init firebase services");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void sendEvent(String event, HashMap<String, String> paramMap) {

        Bundle bundle = new Bundle();
        Set<String> keySet = paramMap.keySet();
        for (String param : keySet) {
            bundle.putString(param, paramMap.get(param));
        }
        mFirebaseAnalytics.logEvent(event, bundle);
    }

    public void sendScreenEvent(@NonNull String screenName) {
        HashMap<String, String> map = new HashMap<>();
        map.put(FirebaseAnalytics.Param.ITEM_CATEGORY, Analytics.Category.SCREEN);
        map.put(FirebaseAnalytics.Param.ITEM_NAME, screenName);
        sendEvent(FirebaseAnalytics.Event.VIEW_ITEM, map);
    }

    public void sendException() {

    }
}
