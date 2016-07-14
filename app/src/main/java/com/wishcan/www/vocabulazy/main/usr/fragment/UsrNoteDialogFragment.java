package com.wishcan.www.vocabulazy.main.usr.fragment;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.main.usr.view.UsrNoteDialogView;
import com.wishcan.www.vocabulazy.widget.DialogFragment;
import com.wishcan.www.vocabulazy.widget.DialogView;

/**
 * Created by swallow on 2016/1/19.
 */
public class UsrNoteDialogFragment extends DialogFragment<String> implements UsrNoteDialogView.OnYesOrNoClickListener {

    public static final String TAG = "L.EDIT";

    public static final String DIALOG_BUNDLE_RES_ID_STR = "dialog_bundle_res_id_str";
    public static final String DIALOG_BUNDLE_STR_STR = "dialog_bundle_str_str";
    private UsrNoteDialogView mUsrNoteDialogView;

    private Tracker wTracker;

    public static UsrNoteDialogFragment newInstance(UsrNoteDialogView.DIALOG_RES_ID_s resId) {
        return newInstance(resId, null);
    }

    public static UsrNoteDialogFragment newInstance(UsrNoteDialogView.DIALOG_RES_ID_s resId, String str) {
        UsrNoteDialogFragment fragment = new UsrNoteDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DIALOG_BUNDLE_RES_ID_STR, resId);
        if(str != null && !str.equals(""))
            bundle.putString(DIALOG_BUNDLE_STR_STR, str);
        fragment.setArguments(bundle);
        return fragment;
    }

    public UsrNoteDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VLApplication application = (VLApplication) getActivity().getApplication();
        wTracker = application.getDefaultTracker();

        if(getArguments() != null) {
            UsrNoteDialogView.DIALOG_RES_ID_s resId = (UsrNoteDialogView.DIALOG_RES_ID_s) getArguments().getSerializable(DIALOG_BUNDLE_RES_ID_STR);
            String inputStr = getArguments().getString(DIALOG_BUNDLE_STR_STR);
            mUsrNoteDialogView = new UsrNoteDialogView(getContext(), null, resId, inputStr);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        wTracker.setScreenName(TAG);
        wTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mUsrNoteDialogView.setOnYesOrNoClickedListener(this);
    }

    @Override
    protected String getNameAsGaLabel() {
        return TAG;
    }

    @Override
    protected DialogView getDialogView() {
        return mUsrNoteDialogView;
    }

    @Override
    protected String getCallerTag() {
        return UsrNoteFragment.M_TAG;
    }

    @Override
    public void onYesClicked() {
        OnDialogFinishListener<String> listener = getOnDialogFinishListener();
        if (listener != null) {
            listener.onDialogFinish(mUsrNoteDialogView.getDialogOutput());
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onNoClicked() {
        getActivity().onBackPressed();
    }
}
