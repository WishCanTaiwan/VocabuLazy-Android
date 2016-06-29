package com.wishcan.www.vocabulazy.main.voc.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.widget.DialogFragment;
import com.wishcan.www.vocabulazy.widget.DialogView;

/**
 * Created by Swallow on 6/29/16.
 */
public class VocBookDialogView extends DialogView<String> {
    private static final int VIEW_RES_ID = R.layout.widget_dialog_new_book;

    public VocBookDialogView(Context context) {
        this(context, null);
    }

    public VocBookDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialog(VIEW_RES_ID);
    }

    @Override
    public void setDialogOutput(String output) {

    }

    @Override
    public String getDialogOutput() {
        return null;
    }

    @Override
    public LayoutTransition getDialogTransition() {
        return null;
    }
}
