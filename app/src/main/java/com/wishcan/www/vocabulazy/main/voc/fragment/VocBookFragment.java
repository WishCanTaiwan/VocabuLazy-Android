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

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VocBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VocBookFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Database mDatabase;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VocBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VocBookFragment newInstance(String param1, String param2) {
        VocBookFragment fragment = new VocBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
            Log.d("VocBookFragment", "get book failed");

        vocBookView.refreshView(bookNames.size(), bookNames);
        return vocBookView;
    }


}
