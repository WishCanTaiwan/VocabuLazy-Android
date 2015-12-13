package com.wishcan.www.vocabulazy.view.exam;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.view.books.BooksGridView;

public class ExamBooksFragment extends Fragment {

    private static final int DEFAULT_FRAGMENT_VIEW_RES_ID = R.layout.fragment_exam_books;

    private ViewGroup mParentView;

    private BooksGridView mExamBooksGridView;

    private String mPreviousTitle;

    public static ExamBooksFragment newInstance(String previousTitle) {
        ExamBooksFragment fragment = new ExamBooksFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.PREVIOUS_TITLE, previousTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public ExamBooksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPreviousTitle = getArguments().getString(MainActivity.PREVIOUS_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mParentView = (ViewGroup) inflater.inflate(DEFAULT_FRAGMENT_VIEW_RES_ID, container, false);

        mExamBooksGridView = ((ExamBooksViewPager) mParentView.getChildAt(0)).getExamBooksGridView();
        mExamBooksGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).goLessonFragment(position, R.integer.MODE_EXAM);
            }
        });

        return mParentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity parentActivity = ((MainActivity) getActivity());
        parentActivity.setActionBarTitleWhenStop(parentActivity.getActionBarTitleTextView());
        parentActivity.switchActionBarTitle(mPreviousTitle);
    }

    @Override
    public void onDetach() {
        super.onDetach();



    }

}
