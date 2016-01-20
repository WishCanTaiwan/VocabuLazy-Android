package com.wishcan.www.vocabulazy.search.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.widget.DialogView;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by swallow on 2016/1/20.
 */
public class SearchDialogView extends DialogView{

    private static final int YES_ID = R.id.action_note_confirm;
    private static final int NO_ID = R.id.action_note_cancel;

    public enum DIALOG_RES_ID_s implements Serializable {
        NEW(R.layout.dialog_new_note),
        LIST(R.layout.dialog_search_add_to_note);

        private int resId;
        DIALOG_RES_ID_s(int resId) {
            this.resId = resId;
        }

        public int getResId(){
            return resId;
        }
    }

    private SearchDialogNoteShowingView mSearchDialogNoteShowingView;

    public SearchDialogView(Context context) {
        this(context, null);
    }

    public SearchDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchDialogView(Context context, AttributeSet attrs, DIALOG_RES_ID_s dialogRes) {
        super(context, attrs);
        setNoteDialog(dialogRes);
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

    private void setNoteDialog(DIALOG_RES_ID_s dialogRes) {
        int resId;
        switch(dialogRes){
            case NEW:
                resId = DIALOG_RES_ID_s.NEW.getResId();
                setDialog(resId);
                setYesOrNoViewId(YES_ID, NO_ID);
                break;
            case LIST:
                resId = DIALOG_RES_ID_s.LIST.getResId();
                mSearchDialogNoteShowingView = new SearchDialogNoteShowingView(getContext());
                setDialog(mSearchDialogNoteShowingView/*, mSearchDialogNoteShowingView.getLayoutParams()*/);
                break;
        }
    }

    public void refreshNoteList(LinkedList<String> linkedList) {
        if(mSearchDialogNoteShowingView != null)
            mSearchDialogNoteShowingView.refreshDataList(linkedList);
    }
}
