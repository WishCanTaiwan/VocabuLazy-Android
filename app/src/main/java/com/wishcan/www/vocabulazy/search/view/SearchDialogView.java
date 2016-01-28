package com.wishcan.www.vocabulazy.search.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.widget.DialogView;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by swallow on 2016/1/20.
 */
public class SearchDialogView extends DialogView{

    public interface OnDialogItemClickListener {
        void onListItemClick(int position);
        void onAddItemClick();
    }

    private static final int YES_ID = R.id.action_note_confirm;
    private static final int NO_ID = R.id.action_note_cancel;
    private static final int INPUT_ID = R.id.dialog_input;

    public enum DIALOG_RES_ID_s implements Serializable {
        NEW(R.layout.widget_note_dialog_new_note),
        LIST(R.layout.widget_dialog_search_add_to_note);

        private int resId;
        DIALOG_RES_ID_s(int resId) {
            this.resId = resId;
        }

        public int getResId(){
            return resId;
        }
    }

    private OnDialogItemClickListener mOnDialogItemClickListener;
    private SearchDialogNoteShowingView mSearchDialogNoteShowingView;
    public String mResultString;

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

    private void setNoteDialog(DIALOG_RES_ID_s dialogRes) {
        int resId;
        switch(dialogRes){
            case NEW:
                resId = DIALOG_RES_ID_s.NEW.getResId();
                setDialog(resId);
                setYesOrNoViewId(YES_ID, NO_ID);
                break;
            case LIST:
                mSearchDialogNoteShowingView = new SearchDialogNoteShowingView(getContext());
                mSearchDialogNoteShowingView.setOnListItemClickListener(new SearchDialogNoteShowingView.OnListItemClickListener() {
                    @Override
                    public void onListItemClick(int position) {
                        if(mOnDialogItemClickListener != null)
                            mOnDialogItemClickListener.onListItemClick(position);
                    }

                    @Override
                    public void onAddItemClick() {
                        if(mOnDialogItemClickListener != null)
                            mOnDialogItemClickListener.onAddItemClick();
                    }
                });
                setDialog(mSearchDialogNoteShowingView);
                break;
        }
    }

    public void refreshNoteList(LinkedList<String> linkedList) {
        if(mSearchDialogNoteShowingView != null)
            mSearchDialogNoteShowingView.refreshDataList(linkedList);
    }

    public void setOnDialogItemClickListener(OnDialogItemClickListener listener) {
        mOnDialogItemClickListener = listener;
    }

    @Override
    public void setDialogOutput(Object output) {
        if(output instanceof String)
            mResultString = (String) output;
    }

    @Override
    public Object getDialogOutput() {
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
