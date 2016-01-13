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
import com.wishcan.www.vocabulazy.main.voc.fragment.VocLessonFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExamIndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamIndexFragment extends Fragment {

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
        FragmentManager fragmentManager = getFragmentManager();
        ExamBookFragment examBookFragment = ExamBookFragment.newInstance();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.
                setCustomAnimations(MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID,
                        MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID);
        fragmentTransaction.add(MainActivity.VIEW_MAIN_RES_ID, examBookFragment, "ExamBookFragment");
        fragmentTransaction.addToBackStack("ExamIndexFragment");
        fragmentTransaction.commit();
    }

    private void goExamNoteFragment(){

    }

}
