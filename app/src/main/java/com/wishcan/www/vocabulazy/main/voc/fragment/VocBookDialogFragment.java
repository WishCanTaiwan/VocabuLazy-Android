package com.wishcan.www.vocabulazy.main.voc.fragment;

import com.wishcan.www.vocabulazy.main.voc.view.VocBookDialogView;
import com.wishcan.www.vocabulazy.widget.DialogFragment;
import com.wishcan.www.vocabulazy.widget.DialogView;

/**
 * Created by Swallow on 6/29/16.
 */
public class VocBookDialogFragment extends DialogFragment<String> implements DialogView.OnYesOrNoClickListener {
    @Override
    protected DialogView getDialogView() {
        DialogView dialogView = new VocBookDialogView(getContext());
        dialogView.setOnYesOrNoClickedListener(this);
        return dialogView;
    }

    @Override
    protected String getCallerTag() {
        return null;
    }

    @Override
    public void onYesClicked() {

    }

    @Override
    public void onNoClicked() {
        getActivity().onBackPressed();
    }
}
