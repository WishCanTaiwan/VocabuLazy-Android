package com.wishcan.www.vocabulazy.cover.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.widget.DialogView;

/**
 * Created by swallow on 2016/4/13.
 */
public class CoverDialogView extends DialogView {

    private static final int VIEW_RES_ID = R.layout.view_cover_dialog;
    private static final int YES_ID = R.id.action_cover_confirm;
    private static final int NO_ID = R.id.action_cover_cancel;

    public CoverDialogView(Context context) {
        this(context, null);
    }

    public CoverDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setDialog(VIEW_RES_ID);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setYesOrNoViewId(YES_ID, NO_ID);
    }

    @Override
    public void setDialogOutput(Object output) {
    }

    @Override
    public Object getDialogOutput() {
        return null;
    }

    @Override
    public LayoutTransition getDialogTransition() {
        return null;
    }
}
