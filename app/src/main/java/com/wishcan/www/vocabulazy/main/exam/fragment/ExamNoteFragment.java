package com.wishcan.www.vocabulazy.main.exam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.exam.model.ExamModel;
import com.wishcan.www.vocabulazy.main.exam.view.ExamNoteView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Lesson;
import com.wishcan.www.vocabulazy.widget.ErrorView;
import com.wishcan.www.vocabulazy.widget.NoteView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by swallow on 2016/1/14.
 */
public class ExamNoteFragment extends ExamBaseFragment implements ExamNoteView.OnListIconClickListener {

    public interface OnExamNoteClickListener {
        void onExamNoteClicked(int position);
    }

    private int mNoteIndex;
    private ExamNoteView mExamNoteView;
    private OnExamNoteClickListener mOnExamNoteClickListener;

    public static ExamNoteFragment newInstance() {
        ExamNoteFragment fragment = new ExamNoteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mNoteIndex = -1;
//
//        ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.GO, "清單測驗");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mExamModel == null) mExamModel = new ExamModel((VLApplication) getActivity().getApplication());
        ArrayList<Lesson> notes = mExamModel.getLessons(-1);
        if (notes == null)
            return new ErrorView(getActivity()).setErrorMsg("DataBase not found");

        LinkedList<String> dataList = new LinkedList<>();
        for (int i = 0; i < notes.size(); i++)
            dataList.add(notes.get(i).getTitle());

        if (mExamNoteView == null)
            mExamNoteView = new ExamNoteView(getActivity());
        mExamNoteView.refreshView(notes.size(), dataList);
        mExamNoteView.setOnListIconClickListener(this);
        return mExamNoteView;
    }

    public void addOnExamNoteClickListener(OnExamNoteClickListener listener) {
        mOnExamNoteClickListener = listener;
    }

//    private void goExamFragment(int noteIndex){
//        Bundle args = new Bundle();
//        args.putInt(ExamFragment.ARG_BOOK_INDEX, -1);
//        args.putInt(ExamFragment.ARG_LESSON_INDEX, noteIndex);
//        ((MainActivity) getActivity()).goFragment(ExamFragment.class, args, "ExamFragment", "ExamLessonFragment");
//    }

    @Override
    public void onListIconClick(int iconId, int position, View v) {
        if (iconId == NoteView.ICON_PLAY) {
            mOnExamNoteClickListener.onExamNoteClicked(position);
        }

//        ArrayList<Integer> contentIDs;
//        switch(iconId) {
//            case NoteView.ICON_PLAY:
//                mNoteIndex = position;
                /** -1 will get note contents*/
//                contentIDs = mExamModel.getContent(-1, mNoteIndex);
//                if (contentIDs.size() >= 4) {
//                    goExamFragment(mNoteIndex);
//                }
//                break;
//            default:
//                break;
//        }
    }
}
