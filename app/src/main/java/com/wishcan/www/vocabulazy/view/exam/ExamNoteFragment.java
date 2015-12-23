package com.wishcan.www.vocabulazy.view.exam;

import android.app.ActionBar;
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
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.view.notes.NotesListView;

public class ExamNoteFragment extends Fragment {

    private static final int DEFAULT_FRAGMENT_VIEW_RES_ID = R.layout.fragment_exam_note;

    private ViewGroup mParentView;

    private NotesListView mNotesListView;

    private Database mDatabase;

    private String mPreviousTitle;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param previousTitle Parameter 1.
     * @return A new instance of fragment ExamNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExamNoteFragment newInstance(String previousTitle) {
        ExamNoteFragment fragment = new ExamNoteFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.PREVIOUS_TITLE, previousTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public ExamNoteFragment() {
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
        mParentView = (ViewGroup) inflater.inflate(DEFAULT_FRAGMENT_VIEW_RES_ID, container, false);
        mNotesListView = ((ExamNoteViewPager) mParentView.getChildAt(0)).getExamNotesListView();
        mNotesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: I want to make a if condition, if the vocabulary is too less (less than 5),
                // TODO: then not to go ExamMainFragment
                if(true) {
                    ((MainActivity) getActivity()).goExamMainFragment(-1, position);
                }
            }
        });

        return mParentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStart() {
        super.onStart();
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity parentActivity = ((MainActivity) getActivity());
        parentActivity.setActionBarTitleWhenStop(parentActivity.getActionBarTitleTextView());
        parentActivity.switchActionBarTitle(mPreviousTitle);
    }
}
