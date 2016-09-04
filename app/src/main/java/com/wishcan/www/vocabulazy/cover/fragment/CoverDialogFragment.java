package com.wishcan.www.vocabulazy.cover.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.cover.view.CoverDialogView;
import com.wishcan.www.vocabulazy.widget.DialogFragment;
import com.wishcan.www.vocabulazy.widget.DialogView;

/**
 * Created by swallow on 2016/4/13.
 */
public class CoverDialogFragment extends DialogFragment implements DialogView.OnYesOrNoClickListener {

    public interface OnDialogClickListener {
        void onYesClicked();
        void onNoClicked();
    }

    public static final String TAG = "C.DIALOG";

    public static final boolean YES_CLICKED = true;
    public static final boolean NO_CLICKED = false;

    private CoverDialogView mCoverDialogView;
    private OnDialogClickListener mOnDialogClickListener;

    public CoverDialogFragment() {
//        super();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_cover_dialog, container, false);
//        mCoverDialogView = (CoverDialogView) rootView.findViewById(R.id.cover_dialog_view);
//        mCoverDialogView.setOnYesOrNoClickedListener(this);
//        return mCoverDialogView;
//    }

    @Override
    protected DialogView getDialogView() {
        Log.d(TAG, "get dialog view");
        mCoverDialogView = (CoverDialogView) LayoutInflater.from(getActivity()).inflate(R.layout.view_cover_dialog, null, false);
        mCoverDialogView.setOnYesOrNoClickedListener(this);
        return mCoverDialogView;
    }

    @Override
    protected String getCallerTag() {
        return CoverFragment.M_TAG;
    }

    @Override
    public void onYesClicked() {
        Log.d(TAG, "yes");
        mOnDialogClickListener.onYesClicked();
//        OnDialogFinishListener fragment = (OnDialogFinishListener) getFragmentManager().findFragmentByTag(CoverFragment.M_TAG);
//        fragment.onDialogFinish(YES_CLICKED);
//        getActivity().onBackPressed();
    }

    @Override
    public void onNoClicked() {
        Log.d(TAG, "no");
        mOnDialogClickListener.onNoClicked();
//        OnDialogFinishListener fragment = (OnDialogFinishListener) getFragmentManager().findFragmentByTag(CoverFragment.M_TAG);
//        fragment.onDialogFinish(NO_CLICKED);
//        getActivity().onBackPressed();
    }

    public void addOnDialogClickListener(OnDialogClickListener listener) {
        mOnDialogClickListener = listener;
    }
}
