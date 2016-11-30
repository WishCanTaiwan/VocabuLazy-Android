package wishcantw.vocabulazy.activities.search.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by SwallowChen on 9/4/16.
 */
public class SearchNewNoteDialogView extends DialogViewNew {

    private static final int VIEW_EDIT_TEXT = R.id.search_new_note_dialog_edit_text;
    private static final int VIEW_YES_RES_ID = R.id.search_new_note_dialog_yes;
    private static final int VIEW_NO_RES_ID = R.id.search_new_note_dialog_no;

    private EditText mNoteNameEditText;

    public SearchNewNoteDialogView(Context context) {
        super(context);
    }

    public SearchNewNoteDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setYesOrNoId(VIEW_YES_RES_ID, VIEW_NO_RES_ID);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mNoteNameEditText = (EditText) findViewById(VIEW_EDIT_TEXT);
    }

    public String getNewNoteString() {
        return mNoteNameEditText.getText().toString();
    }
}
