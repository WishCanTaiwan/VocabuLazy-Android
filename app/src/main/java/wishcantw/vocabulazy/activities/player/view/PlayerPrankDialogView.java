package wishcantw.vocabulazy.activities.player.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by SwallowChen on 12/4/16.
 */

public class PlayerPrankDialogView extends DialogViewNew {

    private static final int VIEW_YES_RES_ID = R.id.player_prank_dialog_yes;
    private static final int VIEW_TEXT_VIEW_TITLE_RES_ID = R.id.player_prank_title;

    private static final int STR_PRANK_MSG_DEFAULT = R.string.player_dialog_prank_title;
    private static final int STR_PRANK_MSG_1 = R.string.player_dialog_prank_msg_1;
    private static final int STR_PRANK_MSG_2 = R.string.player_dialog_prank_msg_2;
    private static final int STR_PRANK_MSG_3 = R.string.player_dialog_prank_msg_3;
    private static final int STR_PRANK_MSG_4 = R.string.player_dialog_prank_msg_4;

    private int[] mStrResIds;
    private TextView mTitleTextView;

    public PlayerPrankDialogView(Context context) {
        this(context, null);
    }

    public PlayerPrankDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mStrResIds = new int[] {STR_PRANK_MSG_DEFAULT, STR_PRANK_MSG_1, STR_PRANK_MSG_2, STR_PRANK_MSG_3, STR_PRANK_MSG_4};

        setYesOrNoId(VIEW_YES_RES_ID, -1);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitleTextView = (TextView) findViewById(VIEW_TEXT_VIEW_TITLE_RES_ID);
    }

    public void changeDialogTitle(int idx) {
        if (idx >= 0 && idx < mStrResIds.length) {
            mTitleTextView.setText(mStrResIds[idx]);
        } else {
            mTitleTextView.setText(mStrResIds[mStrResIds.length - 1]);
        }
    }
}
