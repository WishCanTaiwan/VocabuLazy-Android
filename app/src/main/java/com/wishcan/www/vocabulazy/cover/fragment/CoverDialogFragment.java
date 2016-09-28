package com.wishcan.www.vocabulazy.cover.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.cover.view.CoverDialogView;
import com.wishcan.www.vocabulazy.ga.tags.GAScreenName;
import com.wishcan.www.vocabulazy.search.view.SearchAddVocToNoteDialogView;
import com.wishcan.www.vocabulazy.widget.DialogFragmentNew;
import com.wishcan.www.vocabulazy.widget.DialogViewNew;

/**
 * Created by swallow on 2016/4/13.
 */
public class CoverDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener {

    @Override
    protected String getGALabel() {
        return GAScreenName.TTS_ENGINE_INSTALL;
    }

    public interface OnDialogClickListener {
        void onYesClicked();
        void onNoClicked();
    }

    public static final String TAG = "C.DIALOG";

    private static final int LAYOUT_RES_ID = R.layout.view_cover_dialog;

    private CoverDialogView mCoverDialogView;
    private OnDialogClickListener mOnDialogClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mCoverDialogView == null) {
            mCoverDialogView = (CoverDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        }
        mCoverDialogView.setOnYesOrNoClickListener(this);

        return mCoverDialogView;
    }

    @Override
    public void onYesClick() {
        Log.d(TAG, "yes");
        mOnDialogClickListener.onYesClicked();
    }

    @Override
    public void onNoClick() {
        Log.d(TAG, "no");
        mOnDialogClickListener.onNoClicked();
    }

    public void addOnDialogClickListener(OnDialogClickListener listener) {
        mOnDialogClickListener = listener;
    }
}
