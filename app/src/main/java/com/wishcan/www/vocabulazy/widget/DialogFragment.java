package com.wishcan.www.vocabulazy.widget;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Fragment that calls this fragment must implement the
 * {@link DialogFragment.OnDialogFinishListener} interface
 * to handle dialog result events.
 */
abstract public class DialogFragment<WishCan> extends Fragment {

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
        mDialogView.setOnYesOrNoClickedListener(new DialogView.OnYesOrNoClickListener() {
            @Override
            public void onYesClicked() {
                OnDialogFinishListener fragment = (OnDialogFinishListener) getFragmentManager().findFragmentByTag(mCallerTag);
                fragment.onDialogFinish(mDialogView.getDialogOutput());
                getActivity().onBackPressed();
            }

            @Override
            public void onNoClicked() {
                getActivity().onBackPressed();
            }
        });
        return mDialogView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDialogView.showDialog();

    }
}
