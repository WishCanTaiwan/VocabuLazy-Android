package wishcantw.vocabulazy.activities.player.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by SwallowChen on 9/6/16.
 */
public class PlayerNewNoteDialogView extends DialogViewNew {
    private static final int VIEW_EDIT_TEXT = R.id.player_new_note_dialog_edit_text;
    private static final int VIEW_YES_RES_ID = R.id.player_new_note_dialog_yes;
    private static final int VIEW_NO_RES_ID = R.id.player_new_note_dialog_no;

    private EditText mNoteNameEditText;

    public PlayerNewNoteDialogView(Context context) {
        super(context);
    }

    public PlayerNewNoteDialogView(Context context, AttributeSet attrs) {
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
