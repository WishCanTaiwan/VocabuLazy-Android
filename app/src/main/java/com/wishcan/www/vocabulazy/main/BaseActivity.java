package com.wishcan.www.vocabulazy.main;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.wishcan.www.vocabulazy.ga.GAFragment;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamBookFragment;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamFragment;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamIndexFragment;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamLessonFragment;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamNoteFragment;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamResultFragment;
import com.wishcan.www.vocabulazy.main.fragment.MainFragment;
import com.wishcan.www.vocabulazy.main.info.fragment.InfoFragment;
import com.wishcan.www.vocabulazy.main.player.fragment.PlayerFragment;
import com.wishcan.www.vocabulazy.main.usr.fragment.UsrNoteFragment;
import com.wishcan.www.vocabulazy.main.voc.fragment.VocBookFragment;
import com.wishcan.www.vocabulazy.main.voc.fragment.VocLessonFragment;

public class BaseActivity extends FragmentActivity
        implements  FragmentManager.OnBackStackChangedListener,
                    MainFragment.OnTabSelectListener,
                    VocBookFragment.OnBookClickListener,
                    UsrNoteFragment.OnNoteClickListener,
                    ExamIndexFragment.OnExamIndexClickListener,
                    VocLessonFragment.OnLessonClickListener,
                    ExamBookFragment.OnExamBookClickListener,
                    ExamLessonFragment.OnExamLessonClickListener,
                    ExamNoteFragment.OnExamNoteClickListener,
                    ExamFragment.OnExamCompleteListener,
                    ExamResultFragment.OnExamTryAgainOrAnotherListener,
                    PlayerFragment.OnPlayerLessonChangeListener {

    public static final String TAG = "BaseActivity";

    protected int mBookIndex;
    protected int mLessonIndex;

    protected int mExamBookIndex;
    protected int mExamLessonIndex;

    protected GAFragment mCurrentFragment;

    protected VocBookFragment mVocBookFragment;
    protected UsrNoteFragment mUsrNoteFragment;
    protected ExamIndexFragment mExamIndexFragment;
    protected InfoFragment mInfoFragment;

    protected VocLessonFragment mVocLessonFragment;
    protected ExamBookFragment mExamBookFragment;
    protected ExamLessonFragment mExamLessonFragment;
    protected ExamNoteFragment mExamNoteFragment;
    protected ExamFragment mExamFragment;
    protected ExamResultFragment mExamResultFragment;

    protected PlayerFragment mPlayerFragment;

    protected void initFragments() {
        if (mVocBookFragment == null) {
            mVocBookFragment = VocBookFragment.newInstance();
            mVocBookFragment.addOnBookClickListener(this);
        }

        if (mUsrNoteFragment == null) {
            mUsrNoteFragment = UsrNoteFragment.newInstance();
            mUsrNoteFragment.addOnNoteClickListener(this);
        }

        if (mExamIndexFragment == null) {
            mExamIndexFragment = ExamIndexFragment.newInstance();
            mExamIndexFragment.addOnExamIndexClickListener(this);
        }

        if (mInfoFragment == null) {
            mInfoFragment = InfoFragment.newInstance();
        }

        if (mVocLessonFragment == null) {
            mVocLessonFragment = VocLessonFragment.newInstance();
            mVocLessonFragment.addOnLessonClickListener(this);
        }

        if (mExamBookFragment == null) {
            mExamBookFragment = ExamBookFragment.newInstance();
            mExamBookFragment.addOnExamBookClickListener(this);
        }

        if (mExamLessonFragment == null) {
            mExamLessonFragment = ExamLessonFragment.newInstance();
            mExamLessonFragment.addOnExamLessonClickListener(this);
        }

        if (mExamNoteFragment == null) {
            mExamNoteFragment = ExamNoteFragment.newInstance();
            mExamNoteFragment.addOnExamNoteClickListener(this);
        }

        if (mExamFragment == null) {
            mExamFragment = ExamFragment.newInstance();
            mExamFragment.addOnExamCompleteListener(this);
        }

        if (mExamResultFragment == null) {
            mExamResultFragment = ExamResultFragment.newInstance();
            mExamResultFragment.addOnExamTryAgainOrAnotherListener(this);
        }

        if (mPlayerFragment == null) {
            mPlayerFragment = PlayerFragment.newInstance();
            mPlayerFragment.addOnPlayerLessonChangeListener(this);
        }
    }

    protected void addFragmentsToBackStack(int rid_container) {
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(rid_container, mVocLessonFragment, "VocLessonFragment");
//        fragmentTransaction.hide(mVocLessonFragment);
////        fragmentTransaction.addToBackStack("VocLessonFragment");
//        fragmentTransaction.add(rid_container, mExamBookFragment, "ExamBookFragment");
//        fragmentTransaction.hide(mExamBookFragment);
////        fragmentTransaction.addToBackStack("ExamBookFragment");
//        fragmentTransaction.add(rid_container, mExamLessonFragment, "ExamLessonFragment");
//        fragmentTransaction.hide(mExamLessonFragment);
////        fragmentTransaction.addToBackStack("ExamLessonFragment");
//        fragmentTransaction.add(rid_container, mExamNoteFragment, "ExamNoteFragment");
//        fragmentTransaction.hide(mExamNoteFragment);
////        fragmentTransaction.addToBackStack("ExamNoteFragment");
//        fragmentTransaction.add(rid_container, mExamFragment, "ExamFragment");
//        fragmentTransaction.hide(mExamFragment);
////        fragmentTransaction.addToBackStack("ExamFragment");
//        fragmentTransaction.add(rid_container, mExamResultFragment, "ExamResultFragment");
//        fragmentTransaction.hide(mExamResultFragment);
////        fragmentTransaction.addToBackStack("ExamResultFragment");
//        fragmentTransaction.commit();
    }

    @Override
    public void onBackStackChanged() {

    }

    @Override
    public void onTabSelected(int position) {
        Log.d(TAG, "tab " + position + " selected");
    }

    @Override
    public void onBookClicked(int position) {
        Log.d(TAG, "book " + position + " clicked");
    }

    @Override
    public void onLessonClicked(int position) {
        Log.d(TAG, "lesson " + position + " clicked");
    }

    @Override
    public void onNoteClicked(int position) {
        Log.d(TAG, "note " + position + " clicked");
    }

    @Override
    public void onExamIndexBookClicked() {
        Log.d(TAG, "exam voc clicked");
    }

    @Override
    public void onExamIndexNoteClicked() {
        Log.d(TAG, "exam note clicked");
    }

    @Override
    public void onExamBookClicked(int position) {
        Log.d(TAG, "exam book " + position + " clicked");
    }

    @Override
    public void onExamLessonClicked(int position) {
        Log.d(TAG, "exam book " + position + " clicked");
    }

    @Override
    public void onExamNoteClicked(int position) {
        Log.d(TAG, "exam note " + position + " clicked");
    }

    @Override
    public void onExamCompleted(float correctRatio, int correctCount) {
        Log.d(TAG, "exam completed");
    }

    @Override
    public void onExamTryAgain() {
        Log.d(TAG, "exam try again");
    }

    @Override
    public void onExamTryAnother() {
        Log.d(TAG, "exam try another");
    }

    @Override
    public void onLessonChange(int lesson) {
        Log.d(TAG, "change to lesson " + lesson);
    }
}
