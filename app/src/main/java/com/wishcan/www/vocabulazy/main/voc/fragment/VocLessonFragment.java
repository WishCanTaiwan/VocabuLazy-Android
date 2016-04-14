package com.wishcan.www.vocabulazy.main.voc.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.player.fragment.PlayerFragment;
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

    public static final String TAG = VocLessonFragment.class.getSimpleName();
    public static final String BOOK_INDEX_STR = "BOOK_INDEX_STR";

    private Tracker wTracker;

    private Database mDatabase;
    private int mBookIndex;
    private int mLessonIndex;

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

        VLApplication application = (VLApplication) getActivity().getApplication();
        wTracker = application.getDefaultTracker();

        mDatabase = ((MainActivity) getActivity()).getDatabase();
        mBookIndex = getArguments() == null ? -1 : getArguments().getInt(BOOK_INDEX_STR);
        mLessonIndex = 0;

        ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.GO, "Book " + mBookIndex);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "Setting screen name: " + TAG + " book " + mBookIndex);
        wTracker.setScreenName(TAG + " book " + mBookIndex);
        wTracker.send(new HitBuilders.ScreenViewBuilder().build());

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
                mLessonIndex = lesson;
                goPlayerFragment(mBookIndex, mLessonIndex);
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

    private void goPlayerFragment(int bookIndex, int lessonIndex){
        Bundle args = new Bundle();
        args.putInt(PlayerFragment.BOOK_INDEX_STR, bookIndex);
        args.putInt(PlayerFragment.LESSON_INDEX_STR, lessonIndex);
        ((MainActivity) getActivity()).goFragment(PlayerFragment.class, args, "PlayerFragment", "VocLessonFragment");
    }
}
