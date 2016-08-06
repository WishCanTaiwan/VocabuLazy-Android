package com.wishcan.www.vocabulazy.main.exam.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.exam.model.ExamModel;
import com.wishcan.www.vocabulazy.main.exam.view.ExamBookView;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Book;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.widget.BookView;
import com.wishcan.www.vocabulazy.widget.ErrorView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExamBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamBookFragment extends ExamBaseFragment implements BookView.OnBookItemClickListener {

    public interface OnExamBookClickListener {
        void onExamBookClicked(int position);
    }

    public static final String TAG = "ExamBookFragment";

    public static final String TAG = "E.HOME";

    private static final int TITLE_RES_ID = R.string.fragment_exam_book_title;

    private ExamBookView mExamBookView;
//    private int mExamBookIndex;
    private OnExamBookClickListener mOnExamBookClickListener;

    public static ExamBookFragment newInstance() {
        ExamBookFragment fragment = new ExamBookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ExamBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Create");
        super.onCreate(savedInstanceState);
//        if (mExamModel == null)
//            mExamModel = new ExamModel((VLApplication) getActivity().getApplication());
//        ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.GO, "單元測驗");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Create View");
        ArrayList<Book> books = mExamModel.getBooks();
        if (books == null) return new ErrorView(getActivity()).setErrorMsg("get book failed");

        LinkedList<String> bookNames = new LinkedList<>();
        for (int i = 0; i < books.size();i++)
            bookNames.add(books.get(i).getTitle());

        if (mExamBookView == null)
            mExamBookView = new ExamBookView(getActivity());
        mExamBookView.setOnBookItemClickListener(this);
        mExamBookView.refreshView(bookNames.size(), bookNames);
        return mExamBookView;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "Resume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "Pause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroy");
        super.onDestroy();
    }

    @Override
    public void onBookItemClick(int position) {
//        goExamLessonFragment(position);
        mOnExamBookClickListener.onExamBookClicked(position);
    }

    @Override
    public void onNewItemClick() {
        //TODO: The function for future use
    }

    public void addOnExamBookClickListener(OnExamBookClickListener listener) {
        mOnExamBookClickListener = listener;
    }

    @Override
    protected String getNameAsGaLabel() {
        return TAG;
    }

    private void goExamLessonFragment(int bookIndex){
//        Bundle args = new Bundle();
//        args.putInt(ExamLessonFragment.BOOK_INDEX_STR, bookIndex);
//        ((MainActivity) getActivity()).goFragment(ExamLessonFragment.class, args, "ExamLessonFragment", "ExamBookFragment");
    }
}
