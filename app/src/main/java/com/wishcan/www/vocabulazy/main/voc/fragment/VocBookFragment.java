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
import com.wishcan.www.vocabulazy.main.voc.model.VocModel;
import com.wishcan.www.vocabulazy.main.voc.view.VocBookView;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Book;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.widget.BookView;
import com.wishcan.www.vocabulazy.widget.ErrorView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VocBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VocBookFragment extends VocBaseFragment implements BookView.OnBookItemClickListener {

    public interface OnBookClickListener {
        void onBookClicked(int position);
    }

    public static final String TAG = VocBookFragment.class.getSimpleName();

    private VocBookView mVocBookView;
    private OnBookClickListener mOnBookClickListener;

    public static VocBookFragment newInstance() {
        VocBookFragment fragment = new VocBookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public VocBookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Book> books = mVocModel.getBooks();
        if (books == null) {
            return new ErrorView(getActivity()).setErrorMsg("get book failed");
        }

        LinkedList<String> bookNames = new LinkedList<>();
        for (Book book : books)
            bookNames.add(book.getTitle());

        if (mVocBookView == null)
            mVocBookView = new VocBookView(getActivity());
        mVocBookView.setOnBookItemClickListener(this);
        mVocBookView.refreshView(bookNames.size(), bookNames);
        return mVocBookView;
    }

    @Override
    public void onBookItemClick(int position) {
//        goVocLessonFragment(position);
        mOnBookClickListener.onBookClicked(position);
    }

    @Override
    public void onNewItemClick() {
        //TODO: The function for future use
//        goDialogFragment("");
    }

    public void addOnBookClickListener(OnBookClickListener listener) {
        mOnBookClickListener = listener;
    }

//    private void goVocLessonFragment(int bookIndex){
//        Bundle args = new Bundle();
//        Log.d(TAG, " bookIndex " +bookIndex);
//        args.putInt(VocLessonFragment.BOOK_INDEX_STR, bookIndex);
//        ((MainActivity) getActivity()).goFragment(VocLessonFragment.class, args, "VocLessonFragment", "MainFragment");
//    }

//    private VocBookDialogFragment goDialogFragment(String inputStr) {
//        Bundle args = new Bundle();
//        return (VocBookDialogFragment) ((MainActivity) getActivity())
//                .goFragment(VocBookDialogFragment.class, args, "VocBookDialogFragment", "VocBookFragment", MainActivity.FRAGMENT_ANIM.NONE, MainActivity.FRAGMENT_ANIM.NONE);
//    }


}
