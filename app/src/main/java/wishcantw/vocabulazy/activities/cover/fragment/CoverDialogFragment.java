package wishcantw.vocabulazy.activities.cover.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.analytics.firebase.FirebaseManager;
import wishcantw.vocabulazy.activities.cover.view.CoverDialogView;
import wishcantw.vocabulazy.widget.DialogFragmentNew;
import wishcantw.vocabulazy.widget.DialogViewNew;

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
        FirebaseManager.getInstance().sendScreenEvent(Analytics.ScreenName.TTS_ENGINE_INSTALL);
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
        return Analytics.ScreenName.TTS_ENGINE_INSTALL;
    }

    /** Public methods **/

    public void addOnDialogClickListener(OnDialogClickListener listener) {
        mOnDialogClickListener = listener;
    }
}
