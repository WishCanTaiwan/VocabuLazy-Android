package com.wishcan.www.vocabulazy.main.exam.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.exam.view.ExamIndexView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExamIndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamIndexFragment extends ExamBaseFragment {

    public static ExamIndexFragment newInstance() {
        ExamIndexFragment fragment = new ExamIndexFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ExamIndexFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ExamIndexView examIndexView = new ExamIndexView(getActivity());
        examIndexView.setOnExamItemClickListener(new ExamIndexView.OnExamItemClickListener() {
            @Override
            public void onExamUnitBookClick() {
                goExamBookFragment();
            }

            @Override
            public void onExamUnitNoteClick() {
                goExamNoteFragment();
            }
        });
        return examIndexView;
    }

    private void goExamBookFragment(){
        Bundle args = new Bundle();
        ((MainActivity) getActivity()).goFragment(ExamBookFragment.class, args, "ExamBookFragment", "ExamIndexFragment");
    }

    private void goExamNoteFragment(){
        Bundle args = new Bundle();
        ((MainActivity) getActivity()).goFragment(ExamNoteFragment.class, args, "ExamNoteFragment", "ExamIndexFragment");
    }

}
