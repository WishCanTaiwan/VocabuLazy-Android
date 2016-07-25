package com.wishcan.www.vocabulazy.main;

import android.support.v4.app.FragmentActivity;

import com.wishcan.www.vocabulazy.main.exam.fragment.ExamBookFragment;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamIndexFragment;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamLessonFragment;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamNoteFragment;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamResultFragment;
import com.wishcan.www.vocabulazy.main.info.fragment.InfoFragment;
import com.wishcan.www.vocabulazy.main.player.fragment.PlayerFragment;
import com.wishcan.www.vocabulazy.main.usr.fragment.UsrNoteDialogFragment;
import com.wishcan.www.vocabulazy.main.usr.fragment.UsrNoteFragment;
import com.wishcan.www.vocabulazy.main.voc.fragment.VocBookDialogFragment;
import com.wishcan.www.vocabulazy.main.voc.fragment.VocBookFragment;
import com.wishcan.www.vocabulazy.main.voc.fragment.VocLessonFragment;
import com.wishcan.www.vocabulazy.search.fragment.SearchDialogFragment;
import com.wishcan.www.vocabulazy.search.fragment.SearchFragment;

/**
 * Created by allencheng07 on 2016/7/25.
 */
public class BaseActivity extends FragmentActivity {

    protected VocBookFragment mVocBookFragment;
    protected VocLessonFragment mVocLessonFragment;
    protected VocBookDialogFragment mVocBookDialogFragment;
    protected UsrNoteFragment mUsrNoteFragment;
    protected UsrNoteDialogFragment mUsrNoteDialogFragment;
    protected ExamIndexFragment mExamIndexFragment;
    protected ExamBookFragment mExamBookFragment;
    protected ExamLessonFragment mExamLessonFragment;
    protected ExamNoteFragment mExamNoteFragment;
    protected ExamResultFragment mExamResultFragment;
    protected InfoFragment mInfoFragment;
    protected PlayerFragment mPlayerFragment;
    protected SearchFragment mSearchFragment;
    protected SearchDialogFragment mSearchDialogFragment;

    protected void instantiateFragments() {

    }

}
