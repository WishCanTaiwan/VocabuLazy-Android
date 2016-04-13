package com.wishcan.www.vocabulazy.cover.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.cover.CoverActivity;
import com.wishcan.www.vocabulazy.cover.view.CoverDialogView;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.widget.DialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoverFragment extends Fragment implements DialogFragment.OnDialogFinishListener<Boolean>{

    public static String M_TAG;

    private static final int VIEW_RES_ID = R.layout.view_cover;

    private View mView;
    private boolean mShowDialog;

    public CoverFragment() {
        // Required empty public constructor
    }

    public static CoverFragment newInstance() {
        CoverFragment fragment = new CoverFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        M_TAG = getTag();
        //TODO: BeiBei please add your condition here to change mShowDialog, TRUE will show the dialog
        mShowDialog = true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView =  inflater.inflate(VIEW_RES_ID, container, false);
        final CoverDialogFragment dialogFragment = new CoverDialogFragment();
        if (mShowDialog) {
            mView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(dialogFragment != null) {
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.add(CoverActivity.VIEW_MAIN_RES_ID, dialogFragment, "CoverDialogFragment");
                        fragmentTransaction.addToBackStack("CoverFragment");
                        fragmentTransaction.commit();
                    }
                }
            }, 1000);
        }
        return mView;
    }

    @Override
    public void onDialogFinish(Boolean obj) {
        //TODO: BeiBei please put the code here to execute what you want to do after click YES or NO
        if(CoverDialogFragment.YES_CLICKED){

        } else {

        }

        final Intent intent = new Intent(getActivity(), MainActivity.class);
        mView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                getActivity().finish();
            }
        }, 2000);

    }
}
