package com.wishcan.www.vocabulazy.view.notes;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.view.customview.DialogView;

/**
 * Created by swallow on 2015/9/26.
 */
public class AddNoteDialogView extends DialogView {

    private static final int DEFAULT_DIALOG_VIEW_RES_ID = R.layout.dialog_new_note;

    private static final int DEFAULT_DIALOG_CANCEL_VIEW_ID = R.id.action_note_cancel;

    private static final int DEFAULT_DIALOG_CONFIRM_VIEW_ID = R.id.action_note_confirm;

    private static final int DEFAULT_DIALOG_ADDING_NOTE_VIEW_ID = R.id.dialog_input;

    private EditText mAddingNoteNameTextView;

    public AddNoteDialogView(Context context) {
        this(context, null);
    }

    public AddNoteDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialog(DEFAULT_DIALOG_VIEW_RES_ID);
        setYesOrNoViewId(DEFAULT_DIALOG_CONFIRM_VIEW_ID, DEFAULT_DIALOG_CANCEL_VIEW_ID);
        stopYesFunction();

        mAddingNoteNameTextView = (EditText) getDialog().findViewById(DEFAULT_DIALOG_ADDING_NOTE_VIEW_ID);
        mAddingNoteNameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("AddNoteDialogView", "onTextChanged " + count);
                if (count > 0) {
                    getDialog().findViewById(DEFAULT_DIALOG_CONFIRM_VIEW_ID).
                            setBackgroundColor(getResources().getColor(R.color.dialog_new_note_button_background_green));
                    startYesFunction();
                } else {
                    getDialog().findViewById(DEFAULT_DIALOG_CONFIRM_VIEW_ID).
                            setBackgroundColor(getResources().getColor(R.color.dialog_new_note_button_background_gray));
                    stopYesFunction();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public Object getDialogOutput() {
        return mAddingNoteNameTextView.getText().toString();
    }
}
