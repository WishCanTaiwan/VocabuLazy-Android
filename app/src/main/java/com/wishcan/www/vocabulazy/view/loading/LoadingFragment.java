package com.wishcan.www.vocabulazy.view.loading;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoadingFragment extends Fragment {


    public static LoadingFragment newInstance(Fragment nextFragment) {
        LoadingFragment fragment = new LoadingFragment(nextFragment);
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public LoadingFragment(Fragment nextFragment) {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }


}
