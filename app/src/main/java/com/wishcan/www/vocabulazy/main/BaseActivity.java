package com.wishcan.www.vocabulazy.main;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamIndexFragment;
import com.wishcan.www.vocabulazy.main.info.fragment.InfoFragment;
import com.wishcan.www.vocabulazy.main.usr.fragment.UsrNoteFragment;
import com.wishcan.www.vocabulazy.main.voc.fragment.VocBookFragment;

/**
 * Created by allencheng07 on 2016/8/1.
 */
public class BaseActivity extends FragmentActivity implements FragmentManager.OnBackStackChangedListener {

    protected VocBookFragment mVocBookFragment;
    protected UsrNoteFragment mUsrNoteFragment;
    protected ExamIndexFragment mExamIndexFragment;
    protected InfoFragment mInfoFragment;

    protected void initFragments() {
        if (mVocBookFragment == null) {
            mVocBookFragment = VocBookFragment.newInstance();
        }

        if (mUsrNoteFragment == null) {
            mUsrNoteFragment = UsrNoteFragment.newInstance();
        }

        if (mExamIndexFragment == null) {
            mExamIndexFragment = ExamIndexFragment.newInstance();
        }

        if (mInfoFragment == null) {
            mInfoFragment = InfoFragment.newInstance();
        }
    }

    @Override
    public void onBackStackChanged() {

    }
}
