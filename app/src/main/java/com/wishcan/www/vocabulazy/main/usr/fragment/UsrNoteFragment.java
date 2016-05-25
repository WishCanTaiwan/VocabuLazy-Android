package com.wishcan.www.vocabulazy.main.usr.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
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
import com.wishcan.www.vocabulazy.storage.databaseObjects.Lesson;
import com.wishcan.www.vocabulazy.widget.DialogFragment;
import com.wishcan.www.vocabulazy.widget.ErrorView;
import com.wishcan.www.vocabulazy.widget.NoteView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsrNoteFragment extends Fragment implements UsrNoteView.OnListIconClickListener,
                                                         DialogFragment.OnDialogFinishListener<String>{

    public static final String TAG = UsrNoteFragment.class.getSimpleName();
    public static String M_TAG;

    private Tracker wTracker;

    private UsrNoteView mUsrNoteView;
    private Database wDatabase;
    private int mIconId;        // used for identify either Add or Delete action should be executed
    private int mPressedPosition;

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
        LinkedList<String> dataList = new LinkedList<>();
        ArrayList<Lesson> notes = wDatabase.getLessonsByBook(-1);

        if(notes == null)
            return new ErrorView(getActivity()).setErrorMsg("DataBase not found");

        for(int i = 0; i < notes.size(); i++) {
            dataList.add(notes.get(i).getTitle());
        }

        mUsrNoteView.refreshView(notes.size(), dataList);
        mUsrNoteView.setOnListIconClickListener(this);
        return mUsrNoteView;
    }

    @Override
    public void onListIconClick(int iconId, int position, View v) {
        DialogFragment f = null;
        ArrayList<Lesson> notes = wDatabase.getLessonsByBook(-1);

        mIconId = iconId;
        mPressedPosition = position;

        switch (iconId) {
            case NoteView.ICON_PLAY:
                Log.d(TAG, "wDatabase.getNote @" + position);
                int noteSize = wDatabase.getNoteSize(position);
                if (noteSize > 0) {
                    goPlayerFragment(-1, position);
                }
                break;
            case NoteView.ICON_ETC:
                break;
            case NoteView.ICON_ETC_CLOSE:
                break;
            case NoteView.ICON_DEL:
                f = goDialogFragment(UsrNoteDialogView.DIALOG_RES_ID_s.DELETE, notes.get(position).getTitle());
                break;
            case NoteView.ICON_RENAME:
                f = goDialogFragment(UsrNoteDialogView.DIALOG_RES_ID_s.RENAME, null);
                break;
            case NoteView.ICON_COMBINE:
                f = goDialogFragment(UsrNoteDialogView.DIALOG_RES_ID_s.COMBINE, null);
                break;
            case NoteView.ICON_NEW:
                f = goDialogFragment(UsrNoteDialogView.DIALOG_RES_ID_s.NEW, null);
                break;
            default:
                break;
        }
        if (f != null) {
            f.setOnDialogFinishListener(this);
        }
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
            dataList.add(notes.get(i).getTitle());
        mUsrNoteView.refreshView(notes.size(), dataList);
    }

    private void reload() {
        Log.d(TAG, "reload");
        final ArrayList<Lesson> notes = (wDatabase == null) ? null : wDatabase.getLessonsByBook(-1);

        LinkedList<String> dataList = new LinkedList<>();

        if(notes == null) {
            new ErrorView(getActivity()).setErrorMsg("DataBase not found");
            return;
        }

        for(int i = 0; i < notes.size(); i++) {
            dataList.add(notes.get(i).getTitle());
        }

        mUsrNoteView.refreshView(notes.size(), dataList);
    }

    private void goPlayerFragment(int bookIndex, int lessonIndex){
        Bundle args = new Bundle();
        args.putInt(PlayerFragment.BOOK_INDEX_STR, bookIndex);
        args.putInt(PlayerFragment.LESSON_INDEX_STR, lessonIndex);
        ((MainActivity) getActivity()).goFragment(PlayerFragment.class, args, "PlayerFragment", "UsrNoteFragment");
    }

    private UsrNoteDialogFragment goDialogFragment(UsrNoteDialogView.DIALOG_RES_ID_s resId, String inputStr) {
        Bundle args = new Bundle();
        args.putSerializable(UsrNoteDialogFragment.DIALOG_BUNDLE_RES_ID_STR, resId);
        args.putSerializable(UsrNoteDialogFragment.DIALOG_BUNDLE_STR_STR, inputStr);
        return (UsrNoteDialogFragment) ((MainActivity) getActivity())
                .goFragment(UsrNoteDialogFragment.class, args, "UsrNoteDialogFragment", "UsrNoteFragment", MainActivity.FRAGMENT_ANIM.NONE, MainActivity.FRAGMENT_ANIM.NONE);
    }
}
