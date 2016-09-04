package com.wishcan.www.vocabulazy.search.view;

import android.content.Context;
import android.util.AttributeSet;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.widget.DialogViewNew;

/**
 * Created by SwallowChen on 9/4/16.
 */
public class SearchAddVocToNoteDialogView extends DialogViewNew {

    private static final int VIEW_YES_RES_ID = R.id.search_add_voc_to_note_dialog_yes;
    private static final int VIEW_NO_RES_ID = R.id.search_add_voc_to_note_dialog_no;

    public SearchAddVocToNoteDialogView(Context context) {
        super(context);
    }

    public SearchAddVocToNoteDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setYesOrNoId(VIEW_YES_RES_ID, VIEW_NO_RES_ID);
    }
}
