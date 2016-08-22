package com.wishcan.www.vocabulazy.mainmenu.exam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.mainmenu.exam.view.ExamIndexView;
import com.wishcan.www.vocabulazy.mainmenu.note.adapter.NoteExpandableChildItem;
import com.wishcan.www.vocabulazy.mainmenu.note.adapter.NoteExpandableGroupItem;
import com.wishcan.www.vocabulazy.mainmenu.textbook.adapter.TextbookExpandableChildItem;
import com.wishcan.www.vocabulazy.mainmenu.textbook.adapter.TextbookExpandableGroupItem;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamIndexFragment extends Fragment implements ExamIndexView.OnExamIndexClickListener {

    public interface OnExamIndexClickListener {
        void onExamTextbookClicked(int bookIndex, int lessonIndex);
        void onExamNoteClicked(int noteIndex);
    }

    public static final String TAG = "ExamIndexFragment";

    private ExamIndexView mExamIndexView;
    private OnExamIndexClickListener mOnExamIndexClickListener;

    public static ExamIndexFragment newInstance() {
        ExamIndexFragment fragment = new ExamIndexFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ExamIndexFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exam_index, container, false);
        mExamIndexView = (ExamIndexView) rootView.findViewById(R.id.exam_index_view);
        mExamIndexView.addOnExamIndexClickListener(this);
        return rootView;
    }

    @Override
    public void onExamTextbookClicked(int bookIndex, int lessonIndex) {
        mOnExamIndexClickListener.onExamTextbookClicked(bookIndex, lessonIndex);
    }

    @Override
    public void onExamNoteClicked(int noteIndex) {
        mOnExamIndexClickListener.onExamNoteClicked(noteIndex);
    }

    public void addOnExamIndexClickListener(OnExamIndexClickListener listener) {
        mOnExamIndexClickListener = listener;
    }

    public void updateExamIndexContent(ArrayList<TextbookExpandableGroupItem> textbookGroupItems,
                                       HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> textbookChildItemsMap,
                                       ArrayList<NoteExpandableGroupItem> noteGroupItems,
                                       HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> noteChildItemsMap) {
        mExamIndexView.updateContent(textbookGroupItems, textbookChildItemsMap, noteGroupItems, noteChildItemsMap);
    }
}
