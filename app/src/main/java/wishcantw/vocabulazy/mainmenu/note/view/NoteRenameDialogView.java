package wishcantw.vocabulazy.mainmenu.note.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by allencheng07 on 2016/9/12.
 */
public class NoteRenameDialogView extends DialogViewNew {

    public static final String TAG = "NoteRenameDialogView";

    private static final int RID_EDIT_TEXT = R.id.note_rename_edit_text;
    private static final int RID_YES = R.id.note_rename_confirm;
    private static final int RID_NO = R.id.note_rename_cancel;

    private EditText mEditText;

    public NoteRenameDialogView(Context context) {
        super(context);
    }

    public NoteRenameDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setYesOrNoId(RID_YES, RID_NO);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEditText = (EditText) findViewById(RID_EDIT_TEXT);
    }

    public void setOriginalString(String string) {
        mEditText.setText(string);
    }

    public String getNewString() {
        return mEditText.getText().toString();
    }
}
