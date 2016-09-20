package com.wishcan.www.vocabulazy.mainmenu.exam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.mainmenu.exam.view.ExamIndexTextbookView;
import com.wishcan.www.vocabulazy.mainmenu.textbook.view.TextbookView;

/**
 * Created by allencheng07 on 2016/9/20.
 */

public class ExamIndexTextbookFragment extends Fragment implements TextbookView.OnTextbookClickListener {

    public interface OnExamIndexTextbookClickListener {
        void onExamIndexTextbookClicked(int bookIndex, int lessonIndex);
    }

    private OnExamIndexTextbookClickListener onExamIndexTextbookClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_textbook, container, false);
        TextbookView textbookView = (TextbookView) rootView.findViewById(R.id.textbook_view);
        textbookView.addOnTextBookClickListener(this);
        return rootView;
    }

    @Override
    public void onTextbookChildClicked(int groupPosition, int childPosition) {
        onExamIndexTextbookClickListener.onExamIndexTextbookClicked(groupPosition, childPosition);
    }

    public void addOnExamIndexTextbookClickListener(OnExamIndexTextbookClickListener listener) {
        onExamIndexTextbookClickListener = listener;
    }
}
