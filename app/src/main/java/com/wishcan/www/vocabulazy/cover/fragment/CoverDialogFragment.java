package com.wishcan.www.vocabulazy.cover.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.cover.view.CoverDialogView;
import com.wishcan.www.vocabulazy.ga.manager.GAManager;
import com.wishcan.www.vocabulazy.ga.tags.GAScreenName;
import com.wishcan.www.vocabulazy.search.view.SearchAddVocToNoteDialogView;
import com.wishcan.www.vocabulazy.widget.DialogFragmentNew;
import com.wishcan.www.vocabulazy.widget.DialogViewNew;

/**
 * Created by swallow on 2016/4/13.
 */
public class CoverDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener {

    // callback interface
    public interface OnDialogClickListener {
        void onYesClicked();
        void onNoClicked();
    }

    // TAG for debugging
    public static final String TAG = "CoverDialogFragment";

    // layout resource id
    private static final int LAYOUT_RES_ID = R.layout.view_cover_dialog;

    // views
    private CoverDialogView mCoverDialogView;
    private OnDialogClickListener mOnDialogClickListener;

    /** Life cycles **/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate cover dialog view
        if (mCoverDialogView == null) {
            mCoverDialogView = (CoverDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
            mCoverDialogView.setOnYesOrNoClickListener(this);
        }

        return mCoverDialogView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // send GA screen event
        GAManager.getInstance().sendScreenEvent(GAScreenName.TTS_ENGINE_INSTALL);
    }

    /** Abstracts and Interfaces **/

    @Override
    public void onYesClick() {
        mOnDialogClickListener.onYesClicked();
    }

    @Override
    public void onNoClick() {
        mOnDialogClickListener.onNoClicked();
    }

    @Override
    protected String getGALabel() {
        return GAScreenName.TTS_ENGINE_INSTALL;
    }

    /** Public methods **/

    public void addOnDialogClickListener(OnDialogClickListener listener) {
        mOnDialogClickListener = listener;
    }
}
