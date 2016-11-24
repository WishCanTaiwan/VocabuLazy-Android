package wishcantw.vocabulazy.activities.mainmenu.note.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by allencheng07 on 2016/9/15.
 */
public class NoteDeleteDialogView extends DialogViewNew {

    private static final int RID_YES = R.id.note_delete_confirm;
    private static final int RID_NO = R.id.note_delete_cancel;

    private TextView subtitleTextView;
    private String noteTitle;

    public NoteDeleteDialogView(Context context) {
        super(context);
    }

    public NoteDeleteDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setYesOrNoId(RID_YES, RID_NO);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        subtitleTextView = (TextView) findViewById(R.id.note_delete_subtitle);
        subtitleTextView.setText(noteTitle);
    }

    public void setNoteTitle(String title) {
        noteTitle = title;
    }
}
