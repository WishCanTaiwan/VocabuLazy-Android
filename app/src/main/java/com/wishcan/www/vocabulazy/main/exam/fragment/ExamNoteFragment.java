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
import com.wishcan.www.vocabulazy.main.exam.view.ExamNoteView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;
import com.wishcan.www.vocabulazy.widget.ErrorView;
import com.wishcan.www.vocabulazy.widget.NoteView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by swallow on 2016/1/14.
 */
public class ExamNoteFragment extends Fragment {

    private static final int TITLE_RES_ID = R.string.fragment_exam_note_title;
    private Database mDatabase;

    public static ExamNoteFragment newInstance() {
        ExamNoteFragment fragment = new ExamNoteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = ((MainActivity) getActivity()).getDatabase();

        ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.GO, "清單測驗");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mDatabase == null)
            mDatabase = ((MainActivity) getActivity()).getDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ExamNoteView examNoteView = new ExamNoteView(getActivity());
        ArrayList<Lesson> notes = (mDatabase == null) ? null : mDatabase.getLessonsByBook(-1);
        LinkedList<String> dataList = new LinkedList<>();

        if(notes == null)
            return new ErrorView(getActivity()).setErrorMsg("DataBase not found");

        for(int i = 0; i < notes.size(); i++){
            dataList.add(notes.get(i).getName());
        }

        examNoteView.refreshView(notes.size(), dataList);
        examNoteView.setOnListIconClickListener(new NoteView.OnListIconClickListener() {
            @Override
            public void onListIconClick(int iconId, int position, View v) {
                switch(iconId) {
                    case NoteView.ICON_PLAY:
                        goExamFragment(position);
                        break;
                    default:
                        break;
                }
            }
        });
        return examNoteView;
    }

    private void goExamFragment(int noteIndex){

        FragmentManager fragmentManager = getFragmentManager();
        ExamFragment examFragment = ExamFragment.newInstance(-1, noteIndex);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.
                setCustomAnimations(MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID,
                        MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID);
        fragmentTransaction.add(MainActivity.VIEW_MAIN_RES_ID, examFragment, "ExamFragment");
        fragmentTransaction.addToBackStack("ExamLessonFragment");
        fragmentTransaction.commit();
    }

}
