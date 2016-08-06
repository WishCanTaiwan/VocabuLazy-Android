package com.wishcan.www.vocabulazy.main.exam.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.exam.view.ExamIndexView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExamIndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamIndexFragment extends ExamBaseFragment implements ExamIndexView.OnExamItemClickListener {

    public interface OnExamIndexClickListener {
        void onExamIndexBookClicked();
        void onExamIndexNoteClicked();
    }

    private ExamIndexView mExamIndexView;
    private OnExamIndexClickListener mOnExamIndexClickListener;

    public static final String TAG = "EXAM";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mExamIndexView == null)
            mExamIndexView = new ExamIndexView(getActivity());
        mExamIndexView.setOnExamItemClickListener(this);
        return mExamIndexView;
    }

    @Override
    public void onExamUnitBookClick() {
        mOnExamIndexClickListener.onExamIndexBookClicked();
    }

    @Override
    public void onExamUnitNoteClick() {
        mOnExamIndexClickListener.onExamIndexNoteClicked();
    }

    public void addOnExamIndexClickListener(OnExamIndexClickListener listener) {
        mOnExamIndexClickListener = listener;
    }

//    private void goExamBookFragment(){
//        Bundle args = new Bundle();
//        ((MainActivity) getActivity()).goFragment(ExamBookFragment.class, args, "ExamBookFragment", "ExamIndexFragment");
//    }

//    private void goExamNoteFragment(){
//        Bundle args = new Bundle();
//        ((MainActivity) getActivity()).goFragment(ExamNoteFragment.class, args, "ExamNoteFragment", "ExamIndexFragment");
//    }
}
