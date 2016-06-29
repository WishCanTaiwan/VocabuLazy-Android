package com.wishcan.www.vocabulazy.main.voc.fragment;

import com.wishcan.www.vocabulazy.main.voc.view.VocBookDialogView;
import com.wishcan.www.vocabulazy.widget.DialogFragment;
import com.wishcan.www.vocabulazy.widget.DialogView;

/**
 * Created by Swallow on 6/29/16.
 */
public class VocBookDialogFragment extends DialogFragment<String> {
    @Override
    protected DialogView getDialogView() {
        return new VocBookDialogView(getContext());
    }

    @Override
    protected String getCallerTag() {
        return null;
    }
}
