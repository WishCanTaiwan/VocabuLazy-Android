package com.wishcan.www.vocabulazy.main.voc.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public interface OnLessonClickListener {
        void onLessonClicked(int position);
    }

    public static final String TAG = VocLessonFragment.class.getSimpleName();
    public static final String BOOK_INDEX_STR = "BOOK_INDEX_STR";

    private LessonView mLessonView;
    private LinkedList<Integer> mLessonIntegers;
    private OnLessonClickListener mLessonClickListener;

    private int mBookIndex;
    private int mLessonIndex;

    public static VocLessonFragment newInstance() {
        VocLessonFragment fragment = new VocLessonFragment();
        Bundle args = new Bundle();
//        args.putInt(BOOK_INDEX_STR, bookIndex);
        fragment.setArguments(args);
        return fragment;
    }

    public VocLessonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "voc model [" + mVocModel.toString() + "]");
        if (mVocModel == null)
            mVocModel = new VocModel((VLApplication) getActivity().getApplication());

//        mBookIndex = getArguments() == null ? -1 : getArguments().getInt(BOOK_INDEX_STR);
//        mLessonIndex = 0;

//        ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.GO, mVocModel.getBookTitle(mBookIndex));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // if data model is null, create one
        if (mVocModel == null)
            mVocModel = new VocModel((VLApplication) getActivity().getApplication());

        // retrieve Lessons of given Book from database
        ArrayList<Lesson> lessons = mVocModel.getLessons(mBookIndex);

        // if lessons is null, which means getting lessons has failed, send error message and return.
        if (lessons == null) {
            Log.d(TAG, "get lesson failed");
            return null;
        }

        // if the LinkedList is null, create a new one
        if (mLessonIntegers == null)
            mLessonIntegers = new LinkedList<>();

        // if the LinkedList is not empty, clear the list
        if (mLessonIntegers.size() > 0)
            mLessonIntegers.clear();

        // add content to LinkedList
        for (int i = 0; i < lessons.size(); i++)
            mLessonIntegers.add(i+1);

        if (mLessonView == null) {
            mLessonView = new VocLessonView(getActivity());
        }
        mLessonView.setOnLessonClickListener(this);
        mLessonView.refreshView(mLessonIntegers.size(), mLessonIntegers);
        return mLessonView;
    }

    public void setBook(int bookIndex) {
        mBookIndex = bookIndex;
    }

    public void addOnLessonClickListener(OnLessonClickListener listener) {
        mLessonClickListener = listener;
    }

    private void goPlayerFragment(int bookIndex, int lessonIndex){
        Bundle args = new Bundle();
        args.putInt(PlayerFragment.BOOK_INDEX_STR, bookIndex);
        args.putInt(PlayerFragment.LESSON_INDEX_STR, lessonIndex);
        ((MainActivity) getActivity()).goFragment(PlayerFragment.class, args, "PlayerFragment", "VocLessonFragment");
    }

    @Override
    public void onLessonClick(int lesson) {
        mLessonClickListener.onLessonClicked(lesson);
//        ArrayList<Integer> contentIDs;
//
//        mLessonIndex = lesson;
//        contentIDs = mVocModel.getContent(mBookIndex, mLessonIndex);
//        if (contentIDs.size() >= 4) {
//            goPlayerFragment(mBookIndex, mLessonIndex);
//        }
    }
}
