package com.wishcan.www.vocabulazy.main.usr.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.widget.DialogView;

import java.io.Serializable;

/**
 * Created by swallow on 2016/1/19.
 */
public class UsrNoteDialogView extends DialogView<String> {

    private static final int YES_ID = R.id.action_note_confirm;
    private static final int NO_ID = R.id.action_note_cancel;
    private static final int INPUT_ID = R.id.dialog_input;

    private String mInputStr;

    public enum DIALOG_RES_ID_s implements Serializable{
        NEW(R.layout.widget_note_dialog_new_note),
        DELETE(R.layout.widget_note_dialog_delete_note),
        RENAME(R.layout.widget_note_dialog_rename_note),
        COMBINE(-1);

        private int resId;
        DIALOG_RES_ID_s(int resId) {
            this.resId = resId;
        }

        public int getResId(){
            return resId;
        }
    }

    public String mResultString;

    public UsrNoteDialogView(Context context) {
        this(context, null);
    }

    public UsrNoteDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UsrNoteDialogView(Context context, AttributeSet attrs, DIALOG_RES_ID_s dialogRes) {
        this(context, attrs, dialogRes, null);
    }

    public UsrNoteDialogView(Context context, AttributeSet attrs, DIALOG_RES_ID_s dialogRes, String inputStr) {
        super(context, attrs);
        mInputStr = inputStr;
        setNoteDialog(dialogRes);
    }

    private void setNoteDialog(DIALOG_RES_ID_s dialogRes) {
        View inputView;
        int resId = -1;
        switch(dialogRes){
            case NEW:
                resId = DIALOG_RES_ID_s.NEW.getResId();
                break;
            case DELETE:
                resId = DIALOG_RES_ID_s.DELETE.getResId();
                View v = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resId, null);
                if(mInputStr != null)
                    ((TextView) v.findViewById(INPUT_ID)).setText(mInputStr);
                setDialog(v);
                setYesOrNoViewId(YES_ID, NO_ID);
                return;
            case RENAME:
                resId = DIALOG_RES_ID_s.RENAME.getResId();
                break;
            case COMBINE:
                resId = DIALOG_RES_ID_s.COMBINE.getResId();
                break;
        }
        if(resId != -1) {
            setDialog(resId);
            setYesOrNoViewId(YES_ID, NO_ID);
        }
        if ((inputView = findViewById(INPUT_ID)) != null) {
            if (inputView instanceof EditText) {
                ((EditText) inputView).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (count > 0) {
                            startYesFunction();
                        } else {
                            stopYesFunction();
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable s) {}
                });
            }
        }
    }

    @Override
    public void setDialogOutput(String outputStr) {
        mResultString = outputStr;
    }

    @Override
    public String getDialogOutput() {
        View v = findViewById(INPUT_ID);
        if(v != null && v instanceof EditText) {
            mResultString = ((EditText) v).getText().toString();
            return mResultString;
        }
        return null;
    }

    @Override
    public LayoutTransition getDialogTransition() {
        return null;
    }

}
