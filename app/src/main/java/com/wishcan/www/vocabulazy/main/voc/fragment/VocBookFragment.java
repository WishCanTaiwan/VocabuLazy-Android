package com.wishcan.www.vocabulazy.main.voc.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.voc.view.VocBookView;
import com.wishcan.www.vocabulazy.storage.Book;
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
public class VocBookFragment extends Fragment {


    private Database mDatabase;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = ((MainActivity) getActivity()).getDatabase();
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
        VocBookView vocBookView = new VocBookView(getActivity());
        ArrayList<Book> books = (mDatabase == null) ? null : mDatabase.getBooks();
        LinkedList<String> bookNames = new LinkedList<>();
        vocBookView.setOnBookItemClickListener(new BookView.OnBookItemClickListener() {
            @Override
            public void onBookItemClick(int position) {
                goVocLessonFragment(position);
            }

            @Override
            public void onNewItemClick() {
                //TODO: The function for future use
            }
        });

        if(books != null)
            for(int i = 0; i < books.size();i++)
                bookNames.add(books.get(i).getName());
        else
            return new ErrorView(getActivity()).setErrorMsg("get book failed");


        vocBookView.refreshView(bookNames.size(), bookNames);
        return vocBookView;
    }

    private void goVocLessonFragment(int bookIndex){
        Bundle args = new Bundle();
        args.putInt(VocLessonFragment.BOOK_INDEX_STR, bookIndex);
        ((MainActivity) getActivity()).goFragment(VocLessonFragment.class, args, "VocLessonFragment", "MainFragment");
        /*
        FragmentManager fragmentManager = getFragmentManager();
        VocLessonFragment lessonsFragment = VocLessonFragment.newInstance(bookIndex);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.
                setCustomAnimations(MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID,
                        MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID);
        fragmentTransaction.add(MainActivity.VIEW_MAIN_RES_ID, lessonsFragment, "VocLessonFragment");
        fragmentTransaction.addToBackStack("MainFragment");
        fragmentTransaction.commit();
        */
    }


}
