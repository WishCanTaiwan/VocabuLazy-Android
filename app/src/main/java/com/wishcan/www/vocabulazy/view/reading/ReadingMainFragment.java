package com.wishcan.www.vocabulazy.view.reading;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.view.infinitethreeview.InfiniteThreeView;

public class ReadingMainFragment extends Fragment{

    private static final String ARG_BOOK_INDEX = "bookIndex";

    private static final String ARG_LESSON_INDEX = "lessonIndex";

    private static final String ARG_DATABASE = "database";

    private String mPreviousTitle;

    private int mCurrentBookIndex, mCurrentLessonIndex;

    private Database mDatabase;

    private int mCurrentBookID, mCurrentLessonID;

    private ReadingMainThreeView mReadingThreeView;

    public static ReadingMainFragment newInstance(String previousTitle, int bookIndex,
                                                  int lessonIndex, Database database){
        ReadingMainFragment fragment = new ReadingMainFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.PREVIOUS_TITLE, previousTitle);
        args.putInt(ARG_BOOK_INDEX, bookIndex);
        args.putInt(ARG_LESSON_INDEX, lessonIndex);
        args.putParcelable(ARG_DATABASE, database);
        fragment.setArguments(args);
        return fragment;
    }

    public ReadingMainFragment() {
        mPreviousTitle = "";
        mCurrentBookIndex = -1;
        mCurrentLessonIndex = -1;
        mDatabase = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPreviousTitle = getArguments().getString(MainActivity.PREVIOUS_TITLE);
            mCurrentBookIndex = getArguments().getInt(ARG_BOOK_INDEX);
            mCurrentLessonIndex = getArguments().getInt(ARG_LESSON_INDEX);
            mDatabase = getArguments().getParcelable("database");
        }

        if(mPreviousTitle.equals("") || mCurrentBookIndex < 0 || mCurrentLessonIndex < 0 || mDatabase == null){
            Log.d("" + ReadingMainFragment.class.getSimpleName(),
                    "Must create the fragment with newInstance");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(! (getActivity() instanceof MainActivity))
            return new LinearLayout(getActivity().getApplicationContext());

        mDatabase = ((MainActivity) getActivity()).getDatabase();

        ViewGroup parentVIew = (ViewGroup) ((LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.fragment_reading_main, null);

        if(parentVIew == null)
            return new LinearLayout(getActivity().getApplicationContext());

        mReadingThreeView = (ReadingMainThreeView) parentVIew
                .findViewById(R.id.reading_main_three_view);

        if(mReadingThreeView == null)
            return new LinearLayout(getActivity().getApplicationContext());

        readingThreeViewListenerRegistration();

        if (savedInstanceState != null) {
            mCurrentBookIndex = savedInstanceState.getInt("mCurrentBookIndex");
            mCurrentLessonIndex = savedInstanceState.getInt("mCurrentLessonIndex");
        } else {
            mCurrentBookIndex = -1;
            mCurrentLessonIndex = -1;

            if (getArguments() != null) {
                mPreviousTitle = getArguments().getString(MainActivity.PREVIOUS_TITLE);
                mCurrentBookIndex = getArguments().getInt(ARG_BOOK_INDEX);
                mCurrentLessonIndex = getArguments().getInt(ARG_LESSON_INDEX);
            }
        }

        if(mPreviousTitle.equals("") || mCurrentBookIndex < 0 || mCurrentLessonIndex < 0 || mDatabase == null){
            Log.d("" + ReadingMainFragment.class.getSimpleName(),
                    "Parameters' initialization failed");
            return new LinearLayout(getActivity().getApplicationContext());
        }

        mCurrentBookID = mDatabase.getBookID(mCurrentBookIndex);
        mCurrentLessonID = mDatabase.getLessonID(mCurrentBookIndex, mCurrentLessonIndex);


        mReadingThreeView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mReadingThreeView.addNewReadingPage(mCurrentLessonID);
            }
        }, 600);

        return parentVIew;
    }

    private void readingThreeViewListenerRegistration(){
        mReadingThreeView.setOnPageChangedListener(new InfiniteThreeView.OnPageChangedListener() {
            @Override
            public void onPageScrolled() {

                // TODO: add something you want to do when Page is scrolling
            }

            @Override
            public void onPageChanged(int direction) {
                mReadingThreeView.cleanOldReadingPage(direction == InfiniteThreeView.MOVE_TO_RIGHT ? InfiniteThreeView.RIGHT_VIEW_INDEX : InfiniteThreeView.LEFT_VIEW_INDEX);
                mReadingThreeView.addNewReadingPage(mCurrentLessonID);
            }
        });
    }
}
