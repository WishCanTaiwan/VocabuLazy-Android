package com.wishcan.www.vocabulazy.main.voc.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
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
        vocBookView.setBookItemOnClickListener(new BookView.BookItemOnClickListener() {
            @Override
            public void bookItemOnClick() {
                //TODO: The function for entering VocLessonFragment
                ((MainActivity) getActivity()).goLessonFragment(0);
            }

            @Override
            public void newItemOnClick() {
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


}
