package wishcantw.vocabulazy.mainmenu.note.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.ga.GABaseFragment;
import wishcantw.vocabulazy.mainmenu.activity.MainMenuActivity;
import wishcantw.vocabulazy.mainmenu.model.MainMenuModel;
import wishcantw.vocabulazy.mainmenu.note.adapter.NoteContentAdapter;
import wishcantw.vocabulazy.mainmenu.note.adapter.NoteExpandableChildItem;
import wishcantw.vocabulazy.mainmenu.note.adapter.NoteExpandableGroupItem;
import wishcantw.vocabulazy.mainmenu.note.view.NoteView;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteFragment extends GABaseFragment implements NoteView.OnNoteItemClickListener, FloatingActionButton.OnClickListener {

    public interface OnNoteClickListener {
        void onNotePlay(int noteIndex);
        void onNoteRename(int noteIndex, String name);
        void onNoteCopy();
        void onNoteDelete(int noteIndex, String name);
        void onNoteCreate();
    }

    public static final String TAG = "NoteFragment";

    private static final int PLAY = 0x0;
    private static final int RENAME = 0x1;
    private static final int COPY = 0x2;
    private static final int DELETE = 0x3;

    private MainMenuModel mMainMenuModel;
    private NoteView mNoteView;
    private ImageView imageView;
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
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        mNoteView = (NoteView) rootView.findViewById(R.id.note_view);
        mNoteView.addOnNoteItemListener(this);

        imageView = (ImageView) rootView.findViewById(R.id.imageView);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.new_note_fab);
        fab.setOnClickListener(this);
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
        super.onResume();
        refresh();
    }

    @Override
    protected String getGALabel() {
        return Analytics.ScreenName.NOTE;
    }

    @Override
    public void onNoteChildClicked(int groupPosition, int childPosition) {
        String name = mMainMenuModel.getNoteTitle(groupPosition);
        switch (childPosition) {
            case PLAY:
                // TODO: prevent from entering if there's no vocabulary
                mOnNoteClickListener.onNotePlay(groupPosition);
                break;
            case RENAME:
                mOnNoteClickListener.onNoteRename(groupPosition, name);
                break;
            case COPY:
                mOnNoteClickListener.onNoteCopy();
                break;
            case DELETE:
                mOnNoteClickListener.onNoteDelete(groupPosition, name);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        mOnNoteClickListener.onNoteCreate();
    }

    public void addOnNoteClickListener(OnNoteClickListener listener) {
        mOnNoteClickListener = listener;
    }

    public void updateNoteContent(ArrayList<NoteExpandableGroupItem> groupItems, HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> childItemsMap) {
        mGroupItems = groupItems;
        mChildItemsMap = childItemsMap;
    }

    public void refresh() {
        mNoteView.setAdapter(new NoteContentAdapter(mGroupItems, mChildItemsMap));
        if (mGroupItems.size() > 0) {
            imageView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }
    }
}
