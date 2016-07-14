package com.wishcan.www.vocabulazy.main.voc.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.log.Logger;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.player.fragment.PlayerFragment;
import com.wishcan.www.vocabulazy.main.voc.model.VocModel;
import com.wishcan.www.vocabulazy.main.voc.view.VocLessonView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Lesson;
import com.wishcan.www.vocabulazy.widget.ErrorView;
import com.wishcan.www.vocabulazy.widget.LessonView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VocLessonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VocLessonFragment extends VocBaseFragment implements VocLessonView.OnLessonClickListener {

    public static final String TAG = "UNIT";
    public static final String BOOK_INDEX_STR = "BOOK_INDEX_STR";

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

        if (mVocModel == null)
            mVocModel = new VocModel((VLApplication) getActivity().getApplication());

        mBookIndex = getArguments() == null ? -1 : getArguments().getInt(BOOK_INDEX_STR);
        mLessonIndex = 0;

        ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.GO, mVocModel.getBookTitle(mBookIndex));
    }

    @Override
    public void onResume() {
        super.onResume();
//        Logger.sendScreenViewEvent(TAG + " book " + mBookIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        VocLessonView vocLessonView = new VocLessonView(getActivity());
        ArrayList<Lesson> lessons = mVocModel.getLessons(mBookIndex);
        LinkedList<Integer> lessonIntegers = new LinkedList<>();
        vocLessonView.setOnLessonClickListener(this);
        if(lessons != null)
            for(int i = 0; i < lessons.size(); i++)
                lessonIntegers.add(i + 1);
        else
            return new ErrorView(getActivity()).setErrorMsg("get lesson failed");

        vocLessonView.refreshView(lessonIntegers.size(), lessonIntegers);
        return vocLessonView;
    }

    @Override
    protected String getNameAsGaLabel() {
        return TAG;
    }

    private void goPlayerFragment(int bookIndex, int lessonIndex){
        Bundle args = new Bundle();
        args.putInt(PlayerFragment.BOOK_INDEX_STR, bookIndex);
        args.putInt(PlayerFragment.LESSON_INDEX_STR, lessonIndex);
        ((MainActivity) getActivity()).goFragment(PlayerFragment.class, args, "PlayerFragment", "VocLessonFragment");
    }

    @Override
    public void onLessonClick(int lesson) {
        ArrayList<Integer> contentIDs;

        mLessonIndex = lesson;
        contentIDs = mVocModel.getContent(mBookIndex, mLessonIndex);
        if (contentIDs.size() >= 4) {
            goPlayerFragment(mBookIndex, mLessonIndex);
        }
    }
}
