package com.wishcan.www.vocabulazy.mainmenu.note.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.mainmenu.activity.MainMenuActivity;
import com.wishcan.www.vocabulazy.mainmenu.model.MainMenuModel;
import com.wishcan.www.vocabulazy.mainmenu.note.adapter.NoteContentAdapter;
import com.wishcan.www.vocabulazy.mainmenu.note.adapter.NoteExpandableChildItem;
import com.wishcan.www.vocabulazy.mainmenu.note.adapter.NoteExpandableGroupItem;
import com.wishcan.www.vocabulazy.mainmenu.note.view.NoteView;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteFragment extends Fragment implements NoteView.OnNoteItemClickListener {

    public interface OnNoteClickListener {
        void onNotePlay(int noteIndex);
        void onNoteRename(int noteIndex, String name);
        void onNoteCopy();
        void onNoteDelete(int noteIndex);
    }

    public static final String TAG = "NoteFragment";

    private static final int PLAY = 0x0;
    private static final int RENAME = 0x1;
    private static final int COPY = 0x2;
    private static final int DELETE = 0x3;

    private MainMenuModel mMainMenuModel;
    private NoteView mNoteView;
    private ArrayList<NoteExpandableGroupItem> mGroupItems;
    private HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> mChildItemsMap;
    private OnNoteClickListener mOnNoteClickListener;

    public static NoteFragment newInstance() {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public NoteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        mNoteView = (NoteView) rootView.findViewById(R.id.note_view);

        mNoteView.addOnNoteItemListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainMenuActivity activity = (MainMenuActivity) getActivity();
        mMainMenuModel = activity.getModel();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "Resume");
        super.onResume();
        mNoteView.setAdapter(new NoteContentAdapter(getContext(), mGroupItems, mChildItemsMap));
    }

    @Override
    public void onNoteChildClicked(int groupPosition, int childPosition) {
        switch (childPosition) {
            case PLAY:
                mOnNoteClickListener.onNotePlay(groupPosition);
                break;
            case RENAME:
                String name = mMainMenuModel.getNoteTitle(groupPosition);
                mOnNoteClickListener.onNoteRename(groupPosition, name);
                break;
            case COPY:
                mOnNoteClickListener.onNoteCopy();
                break;
            case DELETE:
                mOnNoteClickListener.onNoteDelete(groupPosition);
                break;
            default:
                break;
        }
    }

    public void addOnNoteClickListener(OnNoteClickListener listener) {
        mOnNoteClickListener = listener;
    }

    public void updateNoteContent(ArrayList<NoteExpandableGroupItem> groupItems, HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> childItemsMap) {
        mGroupItems = groupItems;
        mChildItemsMap = childItemsMap;
    }

    public void refresh() {
        mNoteView.setAdapter(new NoteContentAdapter(getContext(), mGroupItems, mChildItemsMap));
    }
}
