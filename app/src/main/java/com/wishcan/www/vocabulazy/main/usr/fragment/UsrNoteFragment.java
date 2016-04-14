package com.wishcan.www.vocabulazy.main.usr.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.player.fragment.PlayerFragment;
import com.wishcan.www.vocabulazy.main.usr.view.UsrNoteDialogView;
import com.wishcan.www.vocabulazy.main.usr.view.UsrNoteView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;
import com.wishcan.www.vocabulazy.widget.DialogFragment;
import com.wishcan.www.vocabulazy.widget.ErrorView;
import com.wishcan.www.vocabulazy.widget.NoteView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsrNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsrNoteFragment extends Fragment implements DialogFragment.OnDialogFinishListener<String>{

    public static final String TAG = UsrNoteFragment.class.getSimpleName();
    public static String M_TAG;

    private Tracker wTracker;

    private UsrNoteView mUsrNoteView;
    private Database wDatabase;
    private int mIconId;        // used for identify either Add or Delete action should be executed
    private int mPressedPosition;

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
//        Log.d(TAG, "onCreate");

        VLApplication vlApplication = (VLApplication) getActivity().getApplication();
        wTracker = vlApplication.getDefaultTracker();
        wDatabase = vlApplication.getDatabase();

        M_TAG = getTag();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "Setting screen name: " + TAG);
        wTracker.setScreenName(TAG);
        wTracker.send(new HitBuilders.ScreenViewBuilder().build());

        if (wDatabase == null) {
            VLApplication vlApplication = (VLApplication) getActivity().getApplication();
            wDatabase = vlApplication.getDatabase();
        } else {
            reload();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUsrNoteView = new UsrNoteView(getActivity());
        final ArrayList<Lesson> notes = (wDatabase == null) ? null : wDatabase.getLessonsByBook(-1);
        LinkedList<String> dataList = new LinkedList<>();

        if(notes == null)
            return new ErrorView(getActivity()).setErrorMsg("DataBase not found");

        for(int i = 0; i < notes.size(); i++) {
            Log.d("UsrNote", notes.get(i).getName());
            dataList.add(notes.get(i).getName());
        }

        mUsrNoteView.refreshView(notes.size(), dataList);
        mUsrNoteView.setOnListIconClickListener(new NoteView.OnListIconClickListener() {
            @Override
            public void onListIconClick(int iconId, int position, View v) {
                mIconId = iconId;
                mPressedPosition = position;
                UsrNoteDialogFragment dialogFragment = null;
                switch (iconId) {
                    case NoteView.ICON_PLAY:
                        goPlayerFragment(-1, position);
                        break;
                    case NoteView.ICON_ETC:
                        break;
                    case NoteView.ICON_ETC_CLOSE:
                        break;
                    case NoteView.ICON_DEL:
                        dialogFragment = UsrNoteDialogFragment.newInstance(UsrNoteDialogView.DIALOG_RES_ID_s.DELETE, notes.get(position).getName());
                        break;
                    case NoteView.ICON_RENAME:
                        dialogFragment = UsrNoteDialogFragment.newInstance(UsrNoteDialogView.DIALOG_RES_ID_s.RENAME);
                        break;
                    case NoteView.ICON_COMBINE:
                        dialogFragment = UsrNoteDialogFragment.newInstance(UsrNoteDialogView.DIALOG_RES_ID_s.COMBINE);
                        break;
                    case NoteView.ICON_NEW:
                        dialogFragment = UsrNoteDialogFragment.newInstance(UsrNoteDialogView.DIALOG_RES_ID_s.NEW);
                        break;
                    default:
                        break;
                }
                if(dialogFragment != null) {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.add(MainActivity.VIEW_MAIN_RES_ID, dialogFragment, "UsrNoteDialogFragment");
                    fragmentTransaction.addToBackStack("UsrNoteFragment");
                    fragmentTransaction.commit();
                }
            }
        });
        return mUsrNoteView;
    }

    @Override
    public void onDialogFinish(String str) {
        LinkedList<String> dataList = new LinkedList<>();
        ArrayList<Lesson> notes;
        if(mIconId == NoteView.ICON_DEL)
            wDatabase.deleteNoteAt(mPressedPosition);
        else if(mIconId == NoteView.ICON_RENAME && str != null)
            wDatabase.renameNoteAt(mPressedPosition, str);
        else if(mIconId == NoteView.ICON_NEW && str != null)
            wDatabase.createNewNote(str);
        else
            return;

        notes = wDatabase.getLessonsByBook(-1);
        for(int i = 0; i < notes.size(); i++)
            dataList.add(notes.get(i).getName());
        mUsrNoteView.refreshView(notes.size(), dataList);
    }

    private void goPlayerFragment(int bookIndex, int lessonIndex){
        Bundle args = new Bundle();
        args.putInt(PlayerFragment.BOOK_INDEX_STR, bookIndex);
        args.putInt(PlayerFragment.LESSON_INDEX_STR, lessonIndex);
        ((MainActivity) getActivity()).goFragment(PlayerFragment.class, args, "PlayerFragment", "UsrNoteFragment");
    }

    private void reload() {
        Log.d(TAG, "reload");
//        wDatabase = ((MainActivity) getActivity()).getDatabase();
//        Log.d(TAG, wDatabase.toString());
        final ArrayList<Lesson> notes = (wDatabase == null) ? null : wDatabase.getLessonsByBook(-1);

        LinkedList<String> dataList = new LinkedList<>();

        if(notes == null) {
            new ErrorView(getActivity()).setErrorMsg("DataBase not found");
            return;
        }

        for(int i = 0; i < notes.size(); i++) {
            dataList.add(notes.get(i).getName());
//            Log.d(TAG, notes.get(i).getName() + ", " + notes.get(i).getContent().size());
        }

        mUsrNoteView.refreshView(notes.size(), dataList);
    }

}
