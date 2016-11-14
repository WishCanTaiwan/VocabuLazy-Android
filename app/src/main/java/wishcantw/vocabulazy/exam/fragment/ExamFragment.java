package wishcantw.vocabulazy.exam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.audio.AudioService;
import wishcantw.vocabulazy.exam.activity.ExamActivity;
import wishcantw.vocabulazy.exam.model.ExamModel;
import wishcantw.vocabulazy.exam.view.ExamAnswerView;
import wishcantw.vocabulazy.exam.view.ExamView;
import wishcantw.vocabulazy.ga.GABaseFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SwallowChen on 8/25/16.
 */
public class ExamFragment extends GABaseFragment implements ExamView.ExamEventListener {

    @Override
    protected String getGALabel() {
        return Analytics.ScreenName.QUESTIONS;
    }

    public interface OnExamDoneListener {
        void onExamDone(int totalNumber, int correctNumber);
    }

    private static final String ARG_BOOK_INDEX = "bookIndex";
    private static final String ARG_LESSON_INDEX = "lessonIndex";

    private static final int STATE_THINKING = 0x0;
    private static final int STATE_ANSWERED = 0x1;

    private static final int LAYOUT_RES_ID = R.layout.view_exam;

    private ExamView mExamView;
    private ExamModel.PuzzleSetter mPuzzleSetter;

    private int mCurrentBookIndex, mCurrentLessonIndex;
    private int mAnswerState;

    private OnExamDoneListener mOnExamDoneListener;

    public static ExamFragment newInstance(int bookIndex, int lessonIndex) {
        ExamFragment fragment = new ExamFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BOOK_INDEX, bookIndex);
        args.putInt(ARG_LESSON_INDEX, lessonIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentBookIndex = getArguments().getInt(ARG_BOOK_INDEX);
            mCurrentLessonIndex = getArguments().getInt(ARG_LESSON_INDEX);
        } else {
            mCurrentBookIndex = mCurrentLessonIndex = 0;
        }
        mPuzzleSetter = ((ExamActivity) getActivity()).getExamModel().createPuzzleSetter(mCurrentBookIndex, mCurrentLessonIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mExamView == null) {
            mExamView = (ExamView) inflater.inflate(LAYOUT_RES_ID, container, false);
        }

        mExamView.setExamEventListener(this);
        return mExamView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /** NOTE: must call getANewQuestion to update question index */
        HashMap<Integer, ArrayList<String>> questionArrayList = mPuzzleSetter.getANewQuestion();
        mExamView.initContent(mPuzzleSetter.getCurrentQuestionIndex(), mPuzzleSetter.getTotalQuestionNum());
        mExamView.setContent(questionArrayList);
        // hide nextIcon first
        mExamView.hideNextIcon();
    }

    public void setOnExamDoneListener(OnExamDoneListener listener) {
        mOnExamDoneListener = listener;
    }

    @Override
    public void onExamPlayerClick(String questionString) {
        // TODO: Beibei please add the function when player button is clicked

        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.START_SINGLE_ITEM);
        intent.putExtra(AudioService.EXAM_UTTERANCE, questionString);
        getActivity().startService(intent);

    }

    @Override
    public void onExamAnswerClick(int index) {
        if (mAnswerState == STATE_ANSWERED) {
            return;
        }
        /** index start from 1 to 4 */
        int correctIndex = mPuzzleSetter.checkAnswer(index);
        ArrayList<Integer> stateList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            if (i == correctIndex) {
                stateList.add(ExamAnswerView.IDX_EXAM_ANSWER_CORRECT);
            } else if (i == index) {
                stateList.add(ExamAnswerView.IDX_EXAM_ANSWER_WRONG);
            } else {
                stateList.add(ExamAnswerView.IDX_EXAM_ANSWER);
            }
        }
        mExamView.setExamAnswerStates(stateList);
        mExamView.showNextIcon();
        mAnswerState = STATE_ANSWERED;
    }

    @Override
    public void onNextIconClick() {
        // prevent from continuously click during the floating action button hiding animation
        if (mAnswerState == STATE_THINKING) {
            return;
        }
        // change state anyway, preventing from start exam again and state suspend
        mAnswerState = STATE_THINKING;
        // hide next icon anyway
        mExamView.hideNextIcon();
        /** NOTE: must call getANewQuestion to update question index */
        HashMap<Integer, ArrayList<String>> questionArrayList = mPuzzleSetter.getANewQuestion();
        // not enough question to start exam, but should not happen here
        if (questionArrayList == null) {
            return;
        }
        // the exam is over
        if (questionArrayList.size() == 0) {
            if (mOnExamDoneListener != null) {
                mOnExamDoneListener.onExamDone(mPuzzleSetter.getTotalQuestionNum(), mPuzzleSetter.getCorrectCount());
            }
        } else {
            mExamView.updateExamProgressBar(mPuzzleSetter.getCurrentQuestionIndex());
            mExamView.setExamAnswerStates(0, 0, 0, 0);
            mExamView.setContent(questionArrayList);
        }
    }
}
