package com.wishcan.www.vocabulazy.widget;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.ga.GADialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Fragment that calls this fragment must implement the
 * {@link DialogFragment.OnDialogFinishListener} interface
 * to handle dialog result events.
 */
abstract public class DialogFragment<WishCan> extends GADialogFragment {

    protected abstract DialogView getDialogView();
    protected abstract String getCallerTag();

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDialogFinishListener<WishCan> {
        // TODO: Update argument type and name
        void onDialogFinish(WishCan obj);
    }


    private String mCallerTag;
    private DialogView<WishCan> mDialogView;
    private OnDialogFinishListener<WishCan> mOnDialogFinishListener;

    public DialogFragment() {
        // Required empty public constructor
        mCallerTag = getCallerTag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDialogView = getDialogView();
        return mDialogView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDialogView.showDialog();

    }

    public void setOnDialogFinishListener(OnDialogFinishListener listener) {
        mOnDialogFinishListener = listener;
    }

    public OnDialogFinishListener<WishCan> getOnDialogFinishListener() {
        return mOnDialogFinishListener;
    }
}
