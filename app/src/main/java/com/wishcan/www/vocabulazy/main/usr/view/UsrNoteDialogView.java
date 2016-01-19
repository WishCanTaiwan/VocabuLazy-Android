package com.wishcan.www.vocabulazy.main.usr.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.widget.DialogView;

import java.io.Serializable;

/**
 * Created by swallow on 2016/1/19.
 */
public class UsrNoteDialogView extends DialogView<String> {

    private static final int YES_ID = R.id.action_note_confirm;
    private static final int NO_ID = R.id.action_note_cancel;

    public enum DIALOG_RES_ID_s implements Serializable{
        NEW(R.layout.dialog_new_note),
        DELETE(R.layout.dialog_delete_note),
        RENAME(R.layout.dialog_rename_note),
        COMBINE(-1);


        private int resId;
        DIALOG_RES_ID_s(int resId) {
            this.resId = resId;
        }

        public int getResId(){
            return resId;
        }
    }

    @Override
    public String getDialogOutput() {
        return "123";
    }

    @Override
    public LayoutTransition getDialogTransition() {
        return null;
    }

    public UsrNoteDialogView(Context context) {
        this(context, null);
    }

    public UsrNoteDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UsrNoteDialogView(Context context, AttributeSet attrs, DIALOG_RES_ID_s dialogRes) {
        super(context, attrs);
        setNoteDialog(dialogRes);
    }

    private void setNoteDialog(DIALOG_RES_ID_s dialogRes) {
        int resId = -1;
        switch(dialogRes){
            case NEW:
                resId = DIALOG_RES_ID_s.NEW.getResId();
                break;
            case DELETE:
                resId = DIALOG_RES_ID_s.DELETE.getResId();
                break;
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
    }

}
