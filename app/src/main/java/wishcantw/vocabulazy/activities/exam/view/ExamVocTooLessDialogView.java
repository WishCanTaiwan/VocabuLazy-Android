package wishcantw.vocabulazy.activities.exam.view;

import android.content.Context;
import android.util.AttributeSet;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by SwallowChen on 10/7/16.
 */

public class ExamVocTooLessDialogView extends DialogViewNew {

    private static final int VIEW_YES_RES_ID = R.id.exam_voc_too_less_dialog_yes;

    public ExamVocTooLessDialogView(Context context) {
        this(context, null);
    }

    public ExamVocTooLessDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setYesOrNoId(VIEW_YES_RES_ID, -1);
    }
}
