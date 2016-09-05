package com.wishcan.www.vocabulazy.cover.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.widget.DialogView;
import com.wishcan.www.vocabulazy.widget.DialogViewNew;

/**
 * Created by swallow on 2016/4/13.
 */
public class CoverDialogView extends DialogViewNew {

    private static final int VIEW_YES_RES_ID = R.id.cover_dialog_confirm;
    private static final int VIEW_NO_RES_ID = R.id.cover_dialog_cancel;

    public CoverDialogView(Context context) {
        this(context, null);
    }

    public CoverDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setYesOrNoId(VIEW_YES_RES_ID, VIEW_NO_RES_ID);
    }
}
