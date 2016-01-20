package com.wishcan.www.vocabulazy.main.usr.fragment;

import android.os.Bundle;

import com.wishcan.www.vocabulazy.main.usr.view.UsrNoteDialogView;
import com.wishcan.www.vocabulazy.widget.DialogFragment;
import com.wishcan.www.vocabulazy.widget.DialogView;

/**
 * Created by swallow on 2016/1/19.
 */
public class UsrNoteDialogFragment extends DialogFragment<String> {

    private static final String DIALOG_BUNDLE_STR = "dialog_bundle_str";
    private UsrNoteDialogView mUsrNoteDialogView;

    public static UsrNoteDialogFragment newInstance(UsrNoteDialogView.DIALOG_RES_ID_s resId) {
        UsrNoteDialogFragment fragment = new UsrNoteDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DIALOG_BUNDLE_STR, resId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public UsrNoteDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            UsrNoteDialogView.DIALOG_RES_ID_s resId = (UsrNoteDialogView.DIALOG_RES_ID_s) getArguments().getSerializable(DIALOG_BUNDLE_STR);
            mUsrNoteDialogView = new UsrNoteDialogView(getContext(), null, resId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mUsrNoteDialogView.setOnYesOrNoClickedListener(new DialogView.OnYesOrNoClickListener() {
            @Override
            public void onYesClicked() {
                OnDialogFinishListener fragment = (OnDialogFinishListener) getFragmentManager().findFragmentByTag(UsrNoteFragment.M_TAG);
                fragment.onDialogFinish(mUsrNoteDialogView.getDialogOutput());
                getActivity().onBackPressed();
            }

            @Override
            public void onNoClicked() {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    protected DialogView getDialogView() {
        return mUsrNoteDialogView;
    }

    @Override
    protected String getCallerTag() {
        return UsrNoteFragment.M_TAG;
    }
}
