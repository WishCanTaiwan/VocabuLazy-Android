package wishcantw.vocabulazy.activities.exam.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.activities.exam.view.ExamVocTooLessDialogView;
import wishcantw.vocabulazy.widget.DialogFragmentNew;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by SwallowChen on 10/7/16.
 */

public class ExamVocTooLessDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener {

    public interface OnExamAlertDoneListener {
        void onExamAlertDone();
    }

    private static final int LAYOUT_RES_ID = R.layout.view_exam_voc_too_less_dialog;

    private ExamVocTooLessDialogView mExamVocTooLessDialogView;
    private OnExamAlertDoneListener mOnExamAlertDoneListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mExamVocTooLessDialogView = (ExamVocTooLessDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        mExamVocTooLessDialogView.setOnYesOrNoClickListener(this);
        return mExamVocTooLessDialogView;
    }

    public void setOnExamAlertDoneListener(OnExamAlertDoneListener listener) {
        mOnExamAlertDoneListener = listener;
    }

    @Override
    public void onYesClick() {
        mOnExamAlertDoneListener.onExamAlertDone();
    }

    @Override
    public void onNoClick() {
        return;
    }

    @Override
    protected String getGALabel() {
        return null;
    }
}
