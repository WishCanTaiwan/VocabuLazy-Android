package com.wishcan.www.vocabulazy.main.usr.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.player.fragment.PlayerFragment;
import com.wishcan.www.vocabulazy.main.usr.view.UsrNoteView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;
import com.wishcan.www.vocabulazy.storage.Note;
import com.wishcan.www.vocabulazy.widget.ErrorView;
import com.wishcan.www.vocabulazy.widget.NoteView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsrNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsrNoteFragment extends Fragment {

    private Database mDatabase;

    public static UsrNoteFragment newInstance() {
        UsrNoteFragment fragment = new UsrNoteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public UsrNoteFragment() {
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
        UsrNoteView usrNoteView = new UsrNoteView(getActivity());
        ArrayList<Lesson> notes = (mDatabase == null) ? null : mDatabase.getLessonsByBook(-1);
        LinkedList<String> dataList = new LinkedList<>();

        if(notes == null)
            return new ErrorView(getActivity()).setErrorMsg("DataBase not found");

        for(int i = 0; i < notes.size(); i++){
            dataList.add(notes.get(i).getName());
        }

        usrNoteView.refreshView(notes.size(), dataList);
        usrNoteView.setOnListIconClickListener(new NoteView.OnListIconClickListener() {
            @Override
            public void onListIconClick(int iconId, int position, View v){
                switch(iconId){
                    case NoteView.ICON_PLAY:
                        goPlayerFragment(-1, position);
                    break;
                    case NoteView.ICON_ETC:
                    break;
                    case NoteView.ICON_ETC_CLOSE:
                    break;
                    case NoteView.ICON_DEL:
                    break;
                    case NoteView.ICON_RENAME:
                    break;
                    case NoteView.ICON_COMBINE:
                    break;
                    default:
                    break;
                }
            }
        });
        return usrNoteView;
    }

    private void goPlayerFragment(int bookIndex, int lessonIndex){
        FragmentManager fragmentManager = getFragmentManager();
        PlayerFragment playerFragment = PlayerFragment.newInstance(bookIndex, lessonIndex);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.
                setCustomAnimations(MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID,
                        MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID);
        fragmentTransaction.add(MainActivity.VIEW_MAIN_RES_ID, playerFragment, "PlayerFragment");
        fragmentTransaction.addToBackStack("UsrNoteFragment");
        fragmentTransaction.commit();
    }


}
