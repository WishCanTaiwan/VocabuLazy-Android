package com.wishcan.www.vocabulazy.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by allencheng07 on 2016/2/1.
 */
public class ServiceBroadcaster {

    /**
     * tag for debugging.
     */
    public static final String TAG = ServiceBroadcaster.class.getSimpleName();

    /**
     * intent name tag for identifying broadcast
     */
    public static final String BROADCAST_INTENT = "broadcast-intent";

    /**
     * key used in bundle for identifying the action of the broadcast.
     */
    public static final String KEY_ACTION = "key-action";
    public static final String KEY_NEXT_ITEM_INDEX = "key-next-item-index";

    /**
     * strings for identifying the action.
     */
    public static final String ACTION_ITEM_COMPLETE = "action-item-complete";

    private Context wContext;
    private LocalBroadcastManager wBroadcastManager;

    public ServiceBroadcaster(Context context) {
        wContext = context;
        wBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public void onItemComplete(int nextItemIndex) {
        Intent intent = new Intent(BROADCAST_INTENT);
        intent.putExtra(KEY_ACTION, ACTION_ITEM_COMPLETE);
        intent.putExtra(KEY_NEXT_ITEM_INDEX, nextItemIndex);
        wBroadcastManager.sendBroadcast(intent);
    }
}
