package com.wishcan.www.vocabulazy.widget;

import com.wishcan.www.vocabulazy.ga.GABaseFragment;

/**
 * Created by SwallowChen on 9/4/16.
 */
abstract public class DialogFragmentNew<WishCan> extends GABaseFragment {

    @Override
    abstract protected String getGALabel();

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
