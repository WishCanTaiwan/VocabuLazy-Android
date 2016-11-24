package wishcantw.vocabulazy.activities.mainmenu.note.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by allencheng07 on 2016/9/15.
 */
public class NoteCreateDialogView extends DialogViewNew {

    private static final int RID_YES = R.id.note_create_confirm;
    private static final int RID_NO = R.id.note_create_cancel;
    private static final int RID_EDIT_TEXT = R.id.note_create_edit_text;

    public EditText mEditText;

    public NoteCreateDialogView(Context context) {
        super(context);
    }

    public NoteCreateDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setYesOrNoId(RID_YES, RID_NO);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEditText = (EditText) findViewById(RID_EDIT_TEXT);
    }

    public String getNewString() {
        return mEditText.getText().toString();
    }
}
