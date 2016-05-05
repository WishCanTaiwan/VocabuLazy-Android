package com.wishcan.www.vocabulazy.main.exam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.exam.model.ExamModel;
import com.wishcan.www.vocabulazy.main.exam.view.ExamView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamFragment extends Fragment implements ExamView.ExamButtonClickListener {

    private static final String ARG_BOOK_INDEX = "bookIndex";
    private static final String ARG_LESSON_INDEX = "lessonIndex";

    private ExamView mExamView;
    private ExamModel mPuzzleSetter;
    private Database wDatabase;
    private ArrayList<Vocabulary> mVocabularies;
    private int mCurrentBookIndex, mCurrentLessonIndex;
    private Runnable mRefreshAnimTask;

    public static ExamFragment newInstance(int bookIndex, int lessonIndex) {
        ExamFragment fragment = new ExamFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BOOK_INDEX, bookIndex);
        args.putInt(ARG_LESSON_INDEX, lessonIndex);
        fragment.setArguments(args);
        return fragment;
    }

    public ExamFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentBookIndex = getArguments().getInt(ARG_BOOK_INDEX);
            mCurrentLessonIndex = getArguments().getInt(ARG_LESSON_INDEX);
        }

        ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.GO, "Book " + mCurrentBookIndex + " Lesson " + mCurrentLessonIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mExamView = new ExamView(getActivity());
        VLApplication vlApplication = (VLApplication) getActivity().getApplication();
        wDatabase = vlApplication.getDatabase();
        mVocabularies =
                wDatabase.getVocabulariesByIDs(
                        wDatabase.getContentIDs(mCurrentBookIndex, mCurrentLessonIndex));
        mPuzzleSetter = new ExamModel(mVocabularies);

        // set ExamView listener
        mExamView.setExamButtonClickListener(this);
        // set the click event, 0 is question, should not be pressed
//        registerOnOptionClickEvent();
        // set the NEXT click event
//        registerOnNextClickEvent();

        // fill in the question and option, which are from PuzzleSetter
        HashMap<Integer, ArrayList<String>> map = mPuzzleSetter.getANewQuestion();      // should call first to refresh question index
        if(map != null)
            mExamView.refreshContent(mPuzzleSetter.getCurrentQuestionIndex(), mPuzzleSetter.getTotalQuestionNum(), map);

        // Inflate the layout for this fragment
        return mExamView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mExamView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mExamView.startPopOutSequentially();
            }
        }, 600);

    }

    public void restartExam(){
        mPuzzleSetter = new ExamModel(mVocabularies);
        HashMap<Integer, ArrayList<String>> map = mPuzzleSetter.getANewQuestion();      // should call first to refresh question index
        mExamView.refreshContent(mPuzzleSetter.getCurrentQuestionIndex(), mPuzzleSetter.getTotalQuestionNum(), map);
        mExamView.startVanish();
        mExamView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mExamView.startPopOutSequentially();
            }
        }, 600);
        registerOnOptionClickEvent();
        registerOnNextClickEvent();
    }

    private void registerOnOptionClickEvent(){
        mExamView.setButtonsEnable(ExamView.BUTTON_OPTIONS);
        for(int i = 2; i < ExamView.EXAM_PARENT_VIEW_RES_IDs.length -1; i++){   // NEXT icon should not be handle here
            final int checkIndex = i - 1;
            mExamView.findViewById(ExamView.EXAM_PARENT_VIEW_RES_IDs[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int correctIndex = mPuzzleSetter.checkAnswer(checkIndex);
                    mExamView.showAnswer(correctIndex, checkIndex);
                    mExamView.popOutNextIcon();
                    unregisterOnOptionClickEvent();
                }
            });
        }
    }

    private void unregisterOnOptionClickEvent(){
        mExamView.setButtonsDisable(ExamView.BUTTON_OPTIONS);
//        for(int i = 2; i < ExamView.EXAM_PARENT_VIEW_RES_IDs.length -1; i++) {   // NEXT icon should not be handle here
//            mExamView.findViewById(ExamView.EXAM_PARENT_VIEW_RES_IDs[i]).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }
    }

    private void registerOnNextClickEvent(){
            mExamView.setButtonsEnable(ExamView.BUTTON_NEXT);
//        mRefreshAnimTask = new Runnable() {
//            @Override
//            public void run() {
//                if(mExamView.getAnimLocker())
//                    mExamView.postDelayed(mRefreshAnimTask, 100);
//                else {
//                    HashMap<Integer, ArrayList<String>> map = mPuzzleSetter.getANewQuestion();
//                    mExamView.refreshContent(mPuzzleSetter.getCurrentQuestionIndex(), mPuzzleSetter.getTotalQuestionNum(), map);
//                    mExamView.startPopOut();
//                }
//            }
//        };
//
//        mExamView.findViewById(ExamView.VIEW_NEXT_RES_ID).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mPuzzleSetter.getCurrentQuestionIndex() >= mPuzzleSetter.getTotalQuestionNum()) {
//                    FragmentManager fragmentManager = getFragmentManager();
//                    ExamResultFragment examResultFragment = ExamResultFragment.newInstance(
//                            (float) mPuzzleSetter.getCorrectCount() / mPuzzleSetter.getTotalQuestionNum(),
//                            mPuzzleSetter.getCorrectCount());
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.
//                            setCustomAnimations(MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID,
//                                    MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID);
//                    fragmentTransaction.add(MainActivity.VIEW_MAIN_RES_ID, examResultFragment, "ExamResultFragment");
//                    fragmentTransaction.addToBackStack("ExamFragment");
//                    fragmentTransaction.commit();
//                    return;
//                }
//                registerOnOptionClickEvent();
//                mExamView.startVanish();
//                mRefreshAnimTask.run();
//            }
//        });
    }

    private void unregisterOnNextClickEvent() {
        mExamView.setButtonsDisable(ExamView.BUTTON_NEXT);
    }

    /**----------------------- ExamView.ExamButtonClickListener ---------------------------------**/
    @Override
    public void onExamOptionClick(int clickedIndex) {
        int correctIndex = mPuzzleSetter.checkAnswer(clickedIndex);
        mExamView.showAnswer(correctIndex, clickedIndex);
        mExamView.popOutNextIcon();
        unregisterOnOptionClickEvent();
    }

    @Override
    public void onExamNextClick() {
        unregisterOnNextClickEvent();
        mRefreshAnimTask = new Runnable() {
            @Override
            public void run() {
                if(mExamView.getAnimLocker())
                    mExamView.postDelayed(mRefreshAnimTask, 100);
                else {
                    HashMap<Integer, ArrayList<String>> map = mPuzzleSetter.getANewQuestion();
                    mExamView.refreshContent(mPuzzleSetter.getCurrentQuestionIndex(), mPuzzleSetter.getTotalQuestionNum(), map);
                    mExamView.startPopOut();
                    registerOnNextClickEvent();
                }
            }
        };

        if(mPuzzleSetter.getCurrentQuestionIndex() >= mPuzzleSetter.getTotalQuestionNum()) {
            FragmentManager fragmentManager = getFragmentManager();
            ExamResultFragment examResultFragment = ExamResultFragment.newInstance(
                    (float) mPuzzleSetter.getCorrectCount() / mPuzzleSetter.getTotalQuestionNum(),
                    mPuzzleSetter.getCorrectCount());
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.
                    setCustomAnimations(MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID,
                            MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID);
            fragmentTransaction.add(MainActivity.VIEW_MAIN_RES_ID, examResultFragment, "ExamResultFragment");
            fragmentTransaction.addToBackStack("ExamFragment");
            fragmentTransaction.commit();
            return;
        }
        registerOnOptionClickEvent();
        mExamView.startVanish();
        mRefreshAnimTask.run();
    }

    @Override
    public void onPlayerClick(String str) {
        // TODO: Beibei please add your function here. Params str is the string for TTS
    }
}
