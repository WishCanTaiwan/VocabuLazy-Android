package wishcantw.vocabulazy.audio;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import wishcantw.vocabulazy.application.GlobalVariable;

class AudioServiceBroadcaster {

    private String broadcastIntent = GlobalVariable.PLAYER_BROADCAST_INTENT;
    private String broadcastAction = GlobalVariable.PLAYER_BROADCAST_ACTION;

    private Context context;

    AudioServiceBroadcaster(@NonNull Context context) {
        this.context = context;
    }

    void onItemComplete() {
        Intent intent = new Intent(broadcastIntent)
                .putExtra(broadcastAction, AudioService.ITEM_COMPLETE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    void onListComplete() {
        Intent intent = new Intent(broadcastIntent)
                .putExtra(broadcastAction, AudioService.LIST_COMPLETE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    void toItem(int itemIndex) {
        Intent intent = new Intent(broadcastIntent)
                .putExtra(broadcastAction, AudioService.TO_ITEM)
                .putExtra(AudioService.ITEM_INDEX, itemIndex);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    void toNextList() {
        Intent intent = new Intent(broadcastIntent)
                .putExtra(broadcastAction, AudioService.TO_NEXT_LIST);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    void onPlayerStateChanged() {
        Intent intent = new Intent(broadcastIntent)
                .putExtra(broadcastAction, AudioService.PLAYER_STATE_CHANGED);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
