package com.wishcan.www.vocabulazy.view.exam;


import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamFragment extends Fragment {

    private static final String ARG_BOOK_INDEX = "bookIndex";

    private static final String ARG_LESSON_INDEX = "lessonIndex";

    private static final int NEXT_VIEW_ID = R.id.exam_next_parent;

    private static final int[] EXAM_PARENT_VIEW_RES_IDs = {
            R.id.exam_question_parent, R.id.exam_question_title_parent,
            R.id.exam_option1_parent, R.id.exam_option2_parent,
            R.id.exam_option3_parent, R.id.exam_option4_parent,
            R.id.exam_next_parent};

    private static final int[] QUESTION_OPTION_TEXT_VIEW_IDs = {
            R.id.exam_question_text, R.id.exam_question_number, R.id.exam_question_count };

    private View mFragmentView;

    private PuzzleSetter mPuzzleSetter;

    private Database mDatabase;

    private int mCurrentBookIndex, mCurrentLessonIndex;

    private String mPreviousTitle;

    private LayoutController mLayoutController;

    private Runnable mRefreshAnimTask;

    public static ExamFragment newInstance(String previousTitle, int bookIndex, int lessonIndex) {
        ExamFragment fragment = new ExamFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.PREVIOUS_TITLE, previousTitle);
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
            mPreviousTitle = getArguments().getString(MainActivity.PREVIOUS_TITLE);
            mCurrentBookIndex = getArguments().getInt(ARG_BOOK_INDEX);
            mCurrentLessonIndex = getArguments().getInt(ARG_LESSON_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_exam, container, false);
        mLayoutController = new LayoutController(mFragmentView, EXAM_PARENT_VIEW_RES_IDs, QUESTION_OPTION_TEXT_VIEW_IDs);
        mDatabase = ((MainActivity) getActivity()).getDatabase();
        mPuzzleSetter = new PuzzleSetter(mDatabase, mCurrentBookIndex, mCurrentLessonIndex);


        // set the click event, 0 is question, should not be pressed
        registerOnOptionClickEvent();

        // set the NEXT click event
        registerOnNextClickEvent();

        // fill in the question and option, which are from PuzzleSetter
        HashMap<Integer, ArrayList<String>> map = mPuzzleSetter.getANewQuestion();      // should call first to refresh question index
        if(map != null)
            mLayoutController.refreshContent(mPuzzleSetter.getCurrentQuestionIndex(), mPuzzleSetter.getTotalQuestionNum(), map);

        // Inflate the layout for this fragment
        return mFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFragmentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLayoutController.startPopOutSequentially();
            }
        }, 600);

    }

    public void restartExam(){
        mPuzzleSetter = new PuzzleSetter(mDatabase, mCurrentBookIndex, mCurrentLessonIndex);
        HashMap<Integer, ArrayList<String>> map = mPuzzleSetter.getANewQuestion();      // should call first to refresh question index
        mLayoutController.refreshContent(mPuzzleSetter.getCurrentQuestionIndex(), mPuzzleSetter.getTotalQuestionNum(), map);
        mLayoutController.startVanish();
        mFragmentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLayoutController.startPopOutSequentially();
            }
        }, 600);
        registerOnOptionClickEvent();
        registerOnNextClickEvent();
    }

    private void registerOnOptionClickEvent(){
        for(int i = 2; i < EXAM_PARENT_VIEW_RES_IDs.length -1; i++){   // NEXT icon should not be handle here
            final int checkIndex = i - 1;
            mFragmentView.findViewById(EXAM_PARENT_VIEW_RES_IDs[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ((ExamOptionItem) v).performOnClickAnimation();
                    int correctIndex = mPuzzleSetter.checkAnswer(checkIndex);
                    mLayoutController.showAnswer(correctIndex, checkIndex);
                    mLayoutController.popOutNextIcon();
                    unregisterOnOptionClickEvent();
                }
            });
        }
    }

    private void unregisterOnOptionClickEvent(){
        for(int i = 2; i < EXAM_PARENT_VIEW_RES_IDs.length -1; i++) {   // NEXT icon should not be handle here
            mFragmentView.findViewById(EXAM_PARENT_VIEW_RES_IDs[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    private void registerOnNextClickEvent(){

        mRefreshAnimTask = new Runnable() {
            @Override
            public void run() {
                if(mLayoutController.mAnimLocker)
                    mFragmentView.postDelayed(mRefreshAnimTask, 100);
                else {
                    HashMap<Integer, ArrayList<String>> map = mPuzzleSetter.getANewQuestion();
                    mLayoutController.refreshContent(mPuzzleSetter.getCurrentQuestionIndex(), mPuzzleSetter.getTotalQuestionNum(), map);
                    mLayoutController.startPopOut();
                }
            }
        };

        mFragmentView.findViewById(NEXT_VIEW_ID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPuzzleSetter.getCurrentQuestionIndex() >= mPuzzleSetter.getTotalQuestionNum()) {
                    ((MainActivity) getActivity()).goExamResultFragment(
                            (float) mPuzzleSetter.getCorrectCount() / mPuzzleSetter.getTotalQuestionNum(),
                            mPuzzleSetter.getCorrectCount());
                    return;
                }
                registerOnOptionClickEvent();
                mLayoutController.startVanish();
                mRefreshAnimTask.run();
            }
        });
    }

    /**
     * PuzzleSetter will randomly give a new question and 4 options.
     * All algorithm how question is given should be designed here.
     * */
    private class PuzzleSetter{

        private int mCurrentQuestionIndex, mTotalQuestionNum, mAnswerOptionIndex, mCorrectCount;

        private ArrayList<Vocabulary> mVocabularies;

        PuzzleSetter(Database database, int bookIndex, int lessonIndex){
            ArrayList<Vocabulary> newArrayList = database.getVocabulariesByIDs(database.getContentIDs(bookIndex, lessonIndex));
            mVocabularies = shuffleArrayList(newArrayList);
            mCurrentQuestionIndex = -1;
            mTotalQuestionNum = mVocabularies.size();
            mCorrectCount = 0;

        }

        public HashMap<Integer, ArrayList<String>> getANewQuestion(){
            /***
             * HashMap<Integer, ArrayList<String>>
             *     KEY is QuestionAndOption, 0 means Question, 1-4 means option
             *     VALUE is english and translation
             */

            if(mTotalQuestionNum < 5)
                return null;

            mCurrentQuestionIndex++;

            HashMap<Integer, ArrayList<String>> questionMap = new HashMap<>();

            mAnswerOptionIndex = 0;     // reset the answer_option index
            do{     // Determine which option will be the answer
                mAnswerOptionIndex = new Random().nextInt(5);
            } while(mAnswerOptionIndex == 0);

            for(int i = 0; i < 5; i++){

                int pickIndex;      // It's used to pick up option from vocabularies
                ArrayList<String> strArr = new ArrayList<>();   //save option and answer string
                if(i == 0) {          // 0 is for question, only spell is needed
                    pickIndex = mCurrentQuestionIndex;
                    strArr.add(mVocabularies.get(pickIndex).getSpell());
                }
                else if(i == mAnswerOptionIndex){
                    pickIndex = mCurrentQuestionIndex;
                    strArr.add("");
                    strArr.add(mVocabularies.get(pickIndex).getTranslationInOneString());
                }
                else {
                    do {
                        pickIndex = new Random().nextInt(mTotalQuestionNum);
                    } while(pickIndex == mCurrentQuestionIndex);
                    strArr.add(mVocabularies.get(pickIndex).getSpell());
                    strArr.add(mVocabularies.get(pickIndex).getTranslationInOneString());
                }
                questionMap.put(i, strArr);
            }

            return questionMap;
        }

        public int checkAnswer(int checkIndex){
            if(checkIndex == mAnswerOptionIndex)
                mCorrectCount++;
            return mAnswerOptionIndex;
        }

        private ArrayList<Vocabulary> shuffleArrayList(ArrayList<Vocabulary> oldArrayList){
            ArrayList<Vocabulary> newArrayList = new ArrayList<>();
            Random rnd = new Random();
            for(int i = oldArrayList.size() - 1; i >= 0; i--) {
                int index = rnd.nextInt(i + 1);
                newArrayList.add(oldArrayList.get(index));
                oldArrayList.remove(index);
            }
            return newArrayList;
        }

        public int getCurrentQuestionIndex(){
            return mCurrentQuestionIndex + 1;       // because we can't show 00 to students
        }

        public int getTotalQuestionNum(){
            return mTotalQuestionNum;
        }

        public int getCorrectCount() { return mCorrectCount; }
    }

    /**
     * Use function startPopOutSequentially to pop out initial
     * Use function startVanish to recycle the option and refresh option by startPopOut
     * */
    private class LayoutController{

        private View mView;

        private int[] mParentIds, mTextViewIds;

        private int mSequentialCounter;

        private Runnable popOutTask;

        private boolean mAnimLocker;

        LayoutController(View v, int[] pIds, int[] tIds){
            mView = v;
            mParentIds = pIds;
            mTextViewIds = tIds;
            mSequentialCounter = 0;

            popOutTask = new Runnable() {
                @Override
                public void run() {
                    final View view = mView.findViewById(mParentIds[mSequentialCounter++]);
                    view.setVisibility(View.VISIBLE);
                    if(mSequentialCounter < mParentIds.length - 1)      // should not pop out next icon
                        view.postDelayed(popOutTask, 200);
                    else
                        mSequentialCounter = 0;
                }
            };

            initPopOutAnimation();
        }

        private void initPopOutAnimation(){
            PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
            PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
            PropertyValuesHolder pvhScaleX2 = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
            PropertyValuesHolder pvhScaleY2 = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
            List<Animator> animatorList = new ArrayList<>();

            View v = mView.findViewById(mParentIds[0]);

            ValueAnimator popOutAnimator = ObjectAnimator.ofPropertyValuesHolder(v, pvhScaleX, pvhScaleY);
            popOutAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            popOutAnimator.setDuration(300);

            ValueAnimator vanishAnimator = ObjectAnimator.ofPropertyValuesHolder(v, pvhScaleX2, pvhScaleY2);
            vanishAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            vanishAnimator.setDuration(300);

            LayoutTransition layoutTransition = new LayoutTransition();
            layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, vanishAnimator);
            layoutTransition.setAnimator(LayoutTransition.APPEARING, popOutAnimator);
            layoutTransition.addTransitionListener(new LayoutTransition.TransitionListener() {
                @Override
                public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) { mAnimLocker = true; }

                @Override
                public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) { mAnimLocker = false; }
            });

            ((ViewGroup) v).setLayoutTransition(layoutTransition);

            for(int i = 0; i < mParentIds.length; i++)
                mView.findViewById(mParentIds[i]).setVisibility(View.GONE);

        }

        public void startPopOut(){
            for(int i = 0; i < mParentIds.length -1; i++)   // should not pop out next icon
                mView.findViewById(mParentIds[i]).setVisibility(View.VISIBLE);
        }

        public void popOutNextIcon(){
            mView.findViewById(mParentIds[mParentIds.length - 1]).setVisibility(View.VISIBLE);
        }

        public void startVanish(){
            for(int i = 1; i < mParentIds.length; i++)      // should vanish all icon
                mView.findViewById(mParentIds[i]).setVisibility(View.GONE);
        }

        public boolean getAnimLocker(){
            return mAnimLocker;
        }

        public void showAnswer(int correctIndex, int chosenIndex){
            for(int i = 2 ; i < mParentIds.length - 1; i++){
                ((ExamOptionItem) mView.findViewById(mParentIds[i])).performShowAnswerAction((i - 1) == correctIndex);

                if((i - 1) == correctIndex)
                    ((ExamOptionItem) mView.findViewById(mParentIds[i])).showCorrectImage(true);
                else if((i-1) == chosenIndex)
                    ((ExamOptionItem) mView.findViewById(mParentIds[i])).showCorrectImage(false);
            }
        }

        public void refreshContent(int qIndex, int qCount, HashMap<Integer, ArrayList<String>> map){

            if(map == null)
                return;

            for(int i = 2; i < mParentIds.length - 1; i++)  // but question and next should not be handled
                ((ExamOptionItem) mView.findViewById(mParentIds[i])).initState();

            // 0 is question
            ArrayList<String> questionArr = map.get(0);
            ((TextView) mView.findViewById(mTextViewIds[0])).setText(questionArr.get(0));
            // 1, 2 is current_question_index and question_count individually
            ((TextView) mView.findViewById(mTextViewIds[1])).setText(String.valueOf(qIndex));
            ((TextView) mView.findViewById(mTextViewIds[2])).setText(String.valueOf(qCount));


            for(int i = 2, j = 1 ; i < mParentIds.length - 1 && j < map.size(); j++, i++ ){
                // next option(0) should not be handled
                // 1 and 2 are parent view
                ArrayList<String> strArr = map.get(j);  // j should be 1~4, i.e. 4 options, 0 is question
                ((ExamOptionItem) mView.findViewById(mParentIds[i])).setEnglishText(strArr.get(0));
                ((ExamOptionItem) mView.findViewById(mParentIds[i])).setTransText(strArr.get(1));
            }

        }

        public void startPopOutSequentially(){ popOutTask.run(); }
    }


}
