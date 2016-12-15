package wishcantw.vocabulazy.activities.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.activities.player.view.PlayerPrankDialogView;
import wishcantw.vocabulazy.widget.DialogFragmentNew;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by SwallowChen on 12/4/16.
 */

public class PlayerPrankDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    public static final int PRANK_TYPE_NORMAL = 0x1;
    public static final int PRANK_TYPE_NONE   = 0x0;
    // layout resources
    private static final int LAYOUT_RES_ID = R.layout.view_player_prank_dialog;

    private static final int PRANK_TRIGGER_COUNT = 7;

    private PlayerPrankDialogView mPlayerPrankDialogView;
    private int mType;

    public PlayerPrankDialogFragment() {
        super();
        mType = -1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPlayerPrankDialogView = (PlayerPrankDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        mPlayerPrankDialogView.setOnYesOrNoClickListener(this);
        mPlayerPrankDialogView.setOnBackgroundClickListener(this);
        mPlayerPrankDialogView.changeDialogTitle(mType);
        return mPlayerPrankDialogView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected String getGALabel() {
        return null;
    }

    /**------------------------------------ public method ---------------------------------------**/
    public void setPrankTypeByCount(int count) {
        mType = count - PRANK_TRIGGER_COUNT;
    }

    public int getPrankType() {
        return mType >= 0 ? PRANK_TYPE_NORMAL : PRANK_TYPE_NONE;
    }

    @Override
    public void onYesClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void onNoClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void onBackgroundClick() {
        getActivity().onBackPressed();
    }
}
