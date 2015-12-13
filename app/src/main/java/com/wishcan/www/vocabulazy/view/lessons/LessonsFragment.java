package com.wishcan.www.vocabulazy.view.lessons;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.storage.Book;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the interface
 * to handle interaction events.
 * Use the {@link LessonsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LessonsFragment extends Fragment {

    private static final String TAG = LessonsFragment.class.getSimpleName();

    public static final String ARG_BOOK_INDEX = "bookIndex";

    public static final String ENTRY_MODE_TAG = "entry_mode_tag";

    private static final int DEFAULT_FRAGMENT_VIEW_RES_ID = R.layout.fragment_lessons;

    private ViewGroup mParentView;

    private LessonsListView mListView;

    private ArrayList<Book> mBookLL;

    private Book mBook;

    private ArrayList<Lesson> mLessonLL;

    private Database mDatabase;

    private String mPreviousTitle;

    private int mBookIndex, mLessonIndex, mBookID, mLessonID, mEntryMode;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bookIndex Parameter 1.
     * @return A new instance of fragment LessonsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LessonsFragment newInstance(String previousTitle, int bookIndex, Database database, int entryMode) {
        LessonsFragment fragment = new LessonsFragment();
        Bundle args = new Bundle();
        //        args.putString(ARG_PARAM1, param1);
        //        args.putString(ARG_PARAM2, param2);
        args.putString(MainActivity.PREVIOUS_TITLE, previousTitle);
        args.putInt(ARG_BOOK_INDEX, bookIndex);
        args.putInt(ENTRY_MODE_TAG, entryMode);
        args.putParcelable("database", database);
        fragment.setArguments(args);
        return fragment;
    }

    public LessonsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        if (getArguments() != null) {
            mPreviousTitle = getArguments().getString(MainActivity.PREVIOUS_TITLE);
            mBookIndex = getArguments().getInt(ARG_BOOK_INDEX);
            mDatabase = getArguments().getParcelable("database");
            mEntryMode = getArguments().getInt(ENTRY_MODE_TAG);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mParentView = (ViewGroup) inflater.inflate(DEFAULT_FRAGMENT_VIEW_RES_ID, container, false);
        mListView = (LessonsListView)((LessonsViewPager) mParentView.getChildAt(0)).getLessonsListView();


        MainActivity activity = (MainActivity) getActivity();
//        mDatabase = activity.getDatabase();

        // I want use the argument I get to find out how many Unit should be show in ListLiew
        mBook = mDatabase.getBooks().get(mBookIndex);
        mLessonLL = mDatabase.getLessonsByBook(mBookIndex);

        // This LinkedList is used to show how many unit this book has
        LinkedList<Integer> ll = new LinkedList<>();
        Log.d(TAG, " " + mLessonLL.size());
        for (int i = 0; i < mLessonLL.size(); i++) {
            ll.add(i);
        }
        mListView.setAdapterByIntLL(ll);

        mListView.setOnLessonClickedListener(new LessonsListView.OnLessonClickedListener() {
            @Override
            public void onLessonClicked(int lesson) {
                mLessonID = lesson;

                switch (mEntryMode){
                    case R.integer.MODE_EXAM:
                        ((MainActivity) getActivity()).goExamMainFragment(mBookIndex, lesson);
                        break;
                    case R.integer.MODE_PLAYER:
                        ((MainActivity) getActivity()).goPlayerFragment(mBookIndex, lesson);
                        break;
                    case R.integer.MODE_READING:
                        ((MainActivity) getActivity()).goReadingMainFragment(mBookIndex, lesson);
                        break;
                    default:
                        break;
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
    public void onStart() {
        super.onStart();
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            Log.d(TAG, "onStart");
            ((MainActivity) getActivity()).switchActionBarTitle(mBook.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
