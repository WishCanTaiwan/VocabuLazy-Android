package wishcantw.vocabulazy.activities.player.view;

import android.content.Context;
import android.util.AttributeSet;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by SwallowChen on 10/7/16.
 */

public class PlayerVocTooLessDialogView extends DialogViewNew {
    private static final int VIEW_YES_RES_ID = R.id.player_voc_too_less_dialog_yes;

    public PlayerVocTooLessDialogView(Context context) {
        this(context, null);
    }

    public PlayerVocTooLessDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // set -1 as there's no NO id
        setYesOrNoId(VIEW_YES_RES_ID, -1);
    }
}
