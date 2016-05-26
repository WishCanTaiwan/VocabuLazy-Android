package com.wishcan.www.vocabulazy.cover.fragment;

import android.os.Bundle;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.cover.view.CoverDialogView;
import com.wishcan.www.vocabulazy.log.Logger;
import com.wishcan.www.vocabulazy.widget.DialogFragment;
import com.wishcan.www.vocabulazy.widget.DialogView;

/**
 * Created by swallow on 2016/4/13.
 */
public class CoverDialogFragment extends DialogFragment {

    public static final boolean YES_CLICKED = true;
    public static final boolean NO_CLICKED = false;

    public Logger wLogger;

    private CoverDialogView mCoverDialogView;

    public CoverDialogFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VLApplication application = (VLApplication) getActivity().getApplication();
        wLogger = application.getLogHelper();
        mCoverDialogView = new CoverDialogView(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mCoverDialogView != null) {
            mCoverDialogView.setOnYesOrNoClickedListener(new DialogView.OnYesOrNoClickListener() {
                @Override
                public void onYesClicked() {
                    OnDialogFinishListener fragment = (OnDialogFinishListener) getFragmentManager().findFragmentByTag(CoverFragment.M_TAG);
                    fragment.onDialogFinish(YES_CLICKED);
                    getActivity().onBackPressed();
                }

                @Override
                public void onNoClicked() {
                    OnDialogFinishListener fragment = (OnDialogFinishListener) getFragmentManager().findFragmentByTag(CoverFragment.M_TAG);
                    wLogger.d("CoverDialogFragment", "");
                    fragment.onDialogFinish(NO_CLICKED);
                    getActivity().onBackPressed();
                }
            });
        }
    }

    @Override
    protected DialogView getDialogView() {
        return mCoverDialogView;
    }

    @Override
    protected String getCallerTag() {
        return CoverFragment.M_TAG;
    }
}
