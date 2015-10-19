package com.wishcan.www.vocabulazy.view.notes;


import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteDialogFragment extends DialogFragment {


    public static NoteDialogFragment newInstance() {
        NoteDialogFragment fragment = new NoteDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public NoteDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }


}
