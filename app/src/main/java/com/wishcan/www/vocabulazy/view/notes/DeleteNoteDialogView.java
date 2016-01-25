package com.wishcan.www.vocabulazy.view.notes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.view.customview.DialogView;

/**
 * Created by swallow on 2015/9/26.
 */
public class DeleteNoteDialogView extends DialogView {

    private static final int DEFAULT_DIALOG_VIEW_RES_ID = R.layout.widget_note_dialog_delete_note;

    private static final int DEFAULT_DIALOG_DELETING_NOTE_TEXT_VIEW_RES_ID = R.id.dialog_input;

    private static final int DEFAULT_DIALOG_CANCEL_VIEW_ID = R.id.action_note_cancel;

    private static final int DEFAULT_DIALOG_CONFIRM_VIEW_ID = R.id.action_note_confirm;

    public DeleteNoteDialogView(Context context) {
        this(context, null);
    }

    public DeleteNoteDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public DeleteNoteDialogView(Context context, AttributeSet attrs, View v) {
        this(context, null);
        setDialog(DEFAULT_DIALOG_VIEW_RES_ID);
        ((TextView) getDialog().findViewById(DEFAULT_DIALOG_DELETING_NOTE_TEXT_VIEW_RES_ID)).setText(((TextView) v).getText());
        setYesOrNoViewId(DEFAULT_DIALOG_CONFIRM_VIEW_ID, DEFAULT_DIALOG_CANCEL_VIEW_ID);
    }

    @Override
    public Object getDialogOutput() {
        return null;
    }
}
