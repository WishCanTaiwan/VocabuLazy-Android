package com.wishcan.www.vocabulazy.main.voc.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.voc.view.VocLessonView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;
import com.wishcan.www.vocabulazy.widget.ErrorView;
import com.wishcan.www.vocabulazy.widget.LessonView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VocLessonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VocLessonFragment extends Fragment {

    private static final String BOOK_INDEX_STR = "BOOK_INDEX_STR";
    private Database mDatabase;
    private int mBookIndex;

    public static VocLessonFragment newInstance(int bookIndex) {
        VocLessonFragment fragment = new VocLessonFragment();
        Bundle args = new Bundle();
        args.putInt(BOOK_INDEX_STR, bookIndex);
        fragment.setArguments(args);
        return fragment;
    }

    public VocLessonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = ((MainActivity) getActivity()).getDatabase();
        mBookIndex = getArguments() == null ? 0 : getArguments().getInt(BOOK_INDEX_STR);

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
        VocLessonView vocLessonView = new VocLessonView(getActivity());
        ArrayList<Lesson> lessons = (mDatabase == null) ? null : mDatabase.getLessonsByBook(mBookIndex);
        LinkedList<Integer> lessonIntegers = new LinkedList<>();
        vocLessonView.setOnLessonClickListener(new LessonView.OnLessonClickListener() {
            @Override
            public void onLessonClick(int lesson) {

            }
        });
        if(lessons != null)
            for(int i = 0; i < lessons.size(); i++)
                lessonIntegers.add(i + 1);
        else
            return new ErrorView(getActivity()).setErrorMsg("get lesson failed");

        vocLessonView.refreshView(lessonIntegers.size(), lessonIntegers);
        return vocLessonView;
    }


}
