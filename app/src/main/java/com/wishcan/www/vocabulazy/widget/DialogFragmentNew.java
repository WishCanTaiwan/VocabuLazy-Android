package com.wishcan.www.vocabulazy.widget;

import com.wishcan.www.vocabulazy.ga.GADialogFragment;

/**
 * Created by SwallowChen on 9/4/16.
 */
public class DialogFragmentNew<WishCan> extends GADialogFragment {

    public interface OnDialogFinishListener<WishCan> {
        // TODO: Update argument type and name
        void onDialogFinish(WishCan obj);
    }

    private OnDialogFinishListener<WishCan> mOnDialogFinishListener;

    public void setOnDialogFinishListener(OnDialogFinishListener listener) {
        mOnDialogFinishListener = listener;
    }

    public OnDialogFinishListener<WishCan> getOnDialogFinishListener() {
        return mOnDialogFinishListener;
    }
}
