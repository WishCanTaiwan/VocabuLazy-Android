package wishcantw.vocabulazy.activities.player.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.activities.player.view.PlayerVocTooLessDialogView;
import wishcantw.vocabulazy.widget.DialogFragmentNew;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by SwallowChen on 10/7/16.
 */

public class PlayerVocTooLessDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener {

    public interface OnPlayerAlertDoneListener {
        void onPlayerAlertDone();
    }

    // layout resources
    private static final int LAYOUT_RES_ID = R.layout.view_player_voc_too_less_dialog;

    private PlayerVocTooLessDialogView mPlayerVocTooLessDialogView;
    private OnPlayerAlertDoneListener mOnPlayerAlertDoneListener;

    @Override
    protected String getGALabel() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPlayerVocTooLessDialogView = (PlayerVocTooLessDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        mPlayerVocTooLessDialogView.setOnYesOrNoClickListener(this);
        return mPlayerVocTooLessDialogView;
    }

    public void setOnExamAlertDoneListener(OnPlayerAlertDoneListener listener) {
        mOnPlayerAlertDoneListener = listener;
    }

    @Override
    public void onYesClick() {
        mOnPlayerAlertDoneListener.onPlayerAlertDone();
    }

    @Override
    public void onNoClick() {
        return;
    }
}
