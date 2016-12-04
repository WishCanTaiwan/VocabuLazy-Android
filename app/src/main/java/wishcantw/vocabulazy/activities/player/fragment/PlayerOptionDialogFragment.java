package wishcantw.vocabulazy.activities.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.audio.AudioService;
import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.activities.player.activity.PlayerActivity;
import wishcantw.vocabulazy.activities.player.model.PlayerModel;
import wishcantw.vocabulazy.activities.player.view.PlayerOptionDialogView;
import wishcantw.vocabulazy.activities.player.view.PlayerOptionView;
import wishcantw.vocabulazy.widget.DialogFragmentNew;

/**
 * Created by SwallowChen on 11/21/16.
 */

public class PlayerOptionDialogFragment extends DialogFragmentNew implements PlayerOptionDialogView.PlayerOptionEventListener,
                                                                             PlayerOptionDialogView.PlayerOptionCallbackFunc {

    public interface OnPlayPrankListener {
        void onPlayPrank(int count);
    }
    // layout resources
    private static final int LAYOUT_RES_ID = R.layout.view_player_option_dialog;

    // views
    private PlayerOptionDialogView mPlayerOptionDialogView;

    // Model
    private PlayerModel mPlayerModel;

    private OnPlayPrankListener mOnPlayPrankListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPlayerOptionDialogView = (PlayerOptionDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        mPlayerOptionDialogView.setPlayerOptionEventListener(this);
        mPlayerOptionDialogView.setPlayerOptionCallbackFunc(this);
        return mPlayerOptionDialogView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPlayerModel = ((PlayerActivity) getActivity()).getPlayerModel();

        OptionSettings optionSettings = mPlayerModel.getPlayerOptionSettings();
        mPlayerOptionDialogView.setPlayerOptionModeContent(optionSettings, true);
    }

    @Override
    protected String getGALabel() {
        return null;
    }

    /**------------------------------------- public method --------------------------------------**/
    public void setOnPlayPrankListener(OnPlayPrankListener listener) {
        mOnPlayPrankListener = listener;
    }

    /**--------------------- PlayerOptionDialogView.PlayerOptionEventListener -------------------**/

    @Override
    public void onPlayerOptionChanged(int optionID, int mode, View v, int value) {
        if (optionID == PlayerOptionView.IDX_OPTION_MODE) {
            // The value is the mode option index that is selected
            int newMode = value;
            OptionSettings optionSettings = mPlayerModel.getOptionSettings().get(newMode);
            mPlayerOptionDialogView.setPlayerOptionModeContent(optionSettings, false);
        }
        // Refresh option settings
        mPlayerModel.updateOptionSettings(optionID, mode, v, value);
        // TODO : notify the service that option settings has changed
         optionChanged();
    }

    /**--------------------- PlayerOptionDialogView.PlayerOptionCallbackFunc --------------------**/

    @Override
    public int getBalloonVal(int seekBarIdx, int seekBarVal) {
        switch (seekBarIdx) {
            case PlayerOptionView.IDX_SEEK_BAR_SPEED:
                // TODO : Beibei please fill in the formula of the speed
                return seekBarVal;
            case PlayerOptionView.IDX_SEEK_BAR_REPEAT:
            case PlayerOptionView.IDX_SEEK_BAR_PLAY_TIME:
            default:
                return seekBarVal;
        }
    }

    @Override
    public void playPrank(int count) {
        if (mOnPlayPrankListener != null) {
            mOnPlayPrankListener.onPlayPrank(count);
        }
    }

    /**------------------------------------- private method -------------------------------------**/

    private void optionChanged() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.OPTION_SETTINGS_CHANGED);
        getActivity().startService(intent);
    }
}
