package com.wishcan.www.vocabulazy.main.exam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.exam.model.ExamModel;
import com.wishcan.www.vocabulazy.main.exam.view.ExamView;
import com.wishcan.www.vocabulazy.service.AudioService;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamFragment extends Fragment implements ExamView.ExamButtonClickListener {

    public static final String ARG_BOOK_INDEX = "bookIndex";
    public static final String ARG_LESSON_INDEX = "lessonIndex";

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
        if (wDatabase == null) {
            VLApplication vlApplication = (VLApplication) getActivity().getApplication();
            wDatabase = vlApplication.getDatabase();    
        }
        mVocabularies =
                wDatabase.getVocabulariesByIDs(
                        wDatabase.getContentIDs(mCurrentBookIndex, mCurrentLessonIndex));
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
    }

    private void unregisterOnOptionClickEvent(){
        mExamView.setButtonsDisable(ExamView.BUTTON_OPTIONS);
    }

    private void registerOnNextClickEvent(){
            mExamView.setButtonsEnable(ExamView.BUTTON_NEXT);
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
            Bundle args = new Bundle();
            args.putFloat(ExamResultFragment.BUNDLE_RATIO_STRING, (float) mPuzzleSetter.getCorrectCount() / mPuzzleSetter.getTotalQuestionNum());
            args.putInt(ExamResultFragment.BUNDLE_COUNT_STRING, mPuzzleSetter.getCorrectCount());
            ((MainActivity) getActivity()).goFragment(ExamResultFragment.class, args, "ExamResultFragment", "ExamFragment");
            return;
        }

        registerOnOptionClickEvent();
        mExamView.startVanish();
        mRefreshAnimTask.run();
    }

    @Override
    public void onPlayerClick(String str) {
        // TODO: Beibei please add your function here. Params str is the string for TTS
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.START_SINGLE_ITEM);
        intent.putExtra(AudioService.EXAM_UTTERANCE, str);
        getActivity().startService(intent);
    }
}
