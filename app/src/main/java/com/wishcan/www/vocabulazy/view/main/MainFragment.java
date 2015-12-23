package com.wishcan.www.vocabulazy.view.main;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.player.AudioService;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.view.books.BooksGridView;
import com.wishcan.www.vocabulazy.view.notes.AddNoteDialogView;
import com.wishcan.www.vocabulazy.view.notes.CombineNoteDialogView;
import com.wishcan.www.vocabulazy.view.notes.DeleteNoteDialogView;
import com.wishcan.www.vocabulazy.view.notes.NotesListAddButtonView;
import com.wishcan.www.vocabulazy.view.notes.NotesListView;
import com.wishcan.www.vocabulazy.view.notes.RenameNoteDialogView;
import com.wishcan.www.vocabulazy.view.reading.ReadingBooksGridView;
import com.wishcan.www.vocabulazy.view.tab.TabView;
import com.wishcan.www.vocabulazy.view.customview.DialogView;

public class MainFragment extends Fragment {

    public interface OnTabChangeListener {
        void onTabSelected(String str);
    }

    private static final String TAG = MainFragment.class.getSimpleName();

    private static final int MAIN_FRAGMENT_RES_ID = R.layout.fragment_main;

    private static final String ARG_TAB_INDEX = "tab_index";

    private BooksGridView mBooksGridView;

    private NotesListView mNotesListView;

    private ViewGroup mExamView;

    private ReadingBooksGridView mReadingBooksGridView;

    private AudioService mAudioService;

    private DialogView mDialogView;

    private Database mDatabase;

    private TabView mTabView;

    public static MainFragment newInstance(Database database) {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        mDatabase = ((MainActivity) getActivity()).getDatabase();

        if (savedInstanceState != null) {
//            mDatabase = savedInstanceState.getParcelable("database");
//            Log.d(TAG, "onCreate savedInstanceState: " + mDatabase);
        }

        if (getArguments() != null && mDatabase == null) {
//            Log.d(TAG, "onCreate getArguments");
//            mDatabase = getArguments().getParcelable("database");
        } else {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(MAIN_FRAGMENT_RES_ID, container, false);
        mTabView = (TabView) view.findViewById(R.id.mytabhost);

        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt(ARG_TAB_INDEX);
            mTabView.setCurrentTab(index);

        }

        /**
         * Set TabContent(0) (mBooksGridView) on click event
         * */
        mBooksGridView = (BooksGridView) mTabView.getTabContent(0);
        mBooksGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, " " + position);
                if (position < mBooksGridView.getBooksCount())
                    ((MainActivity) getActivity()).goLessonFragment(position, R.integer.MODE_PLAYER);

            }
        });


        /**
         * Set TabContent(1) (mNotesListView) on click event
         * */
        mNotesListView = ((NotesListAddButtonView) mTabView.getTabContent(1)).getNotesListView();
        ((NotesListAddButtonView) mTabView.getTabContent(1)).setAddOnButtonClickListener(new NotesListAddButtonView.AddButtonOnClickListener() {
            @Override
            public void onAddButtonOnClick() {
                mDialogView = new AddNoteDialogView(getActivity());
                mDialogView.setOnYesOrNoClickedListener(new DialogView.OnYesOrNoClickedListener() {
                    @Override
                    public void onYesClicked() {
                        String newNoteName = (String) mDialogView.getDialogOutput();
                        mDatabase.createNewNote(newNoteName);
                        mNotesListView.refresh();
                        closeDialog(mDialogView);
                    }

                    @Override
                    public void onNoClicked() {
                        closeDialog(mDialogView);
                    }
                });
                showDialog();
            }
        });

        mNotesListView.setOnListIconClickedListener(new NotesListView.OnListIconClickedListener() {
            @Override
            public void onListIconClicked(int iconId, int listIndex, View v) {
                final int position = listIndex;
                switch (iconId) {
                    case NotesListView.ICON_PLAY:
                        ((MainActivity) getActivity()).goPlayerFragment(-1, listIndex);
                        break;
                    case NotesListView.ICON_COMBINE:
                        mDialogView = new CombineNoteDialogView(getActivity());
                        mDialogView.setOnYesOrNoClickedListener(new DialogView.OnYesOrNoClickedListener() {
                            @Override
                            public void onYesClicked() {

                            }

                            @Override
                            public void onNoClicked() {
                                closeDialog(mDialogView);
                            }
                        });
                        showDialog();
                        break;
                    case NotesListView.ICON_DEL:
                        mDialogView = new DeleteNoteDialogView(getActivity(), null, v);
                        mDialogView.setOnYesOrNoClickedListener(new DialogView.OnYesOrNoClickedListener() {
                            @Override
                            public void onYesClicked() {
                                mDatabase.deleteNoteAt(position);
                                mNotesListView.refresh();
                                closeDialog(mDialogView);
                            }

                            @Override
                            public void onNoClicked() {
                                closeDialog(mDialogView);
                            }
                        });
                        showDialog();
                        break;
                    case NotesListView.ICON_RENAME:
                        mDialogView = new RenameNoteDialogView(getActivity());
                        mDialogView.setOnYesOrNoClickedListener(new DialogView.OnYesOrNoClickedListener() {
                            @Override
                            public void onYesClicked() {
                                mDatabase.renameNoteAt(position, (String) mDialogView.getDialogOutput());
                                mNotesListView.refresh();
                                closeDialog(mDialogView);
                            }

                            @Override
                            public void onNoClicked() {
                                closeDialog(mDialogView);
                            }
                        });
                        showDialog();
                        break;
                    default:
                        break;
                }
            }
        });


        /**
         * Set TabContent(2) (mExamView) on click event
         * */
        mExamView = mTabView.getTabContent(2);
        mExamView.findViewById(R.id.exam_unit_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).goExamBooksFragment();
            }
        });
        mExamView.findViewById(R.id.exam_unit_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).goExamNoteFragment();
            }
        });

        /***
         * Set TabContent(3) (mReadingBooksGridView) on click event
         */
        mReadingBooksGridView = (ReadingBooksGridView) mTabView.getTabContent(3);
        mReadingBooksGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).goLessonFragment(position, R.integer.MODE_READING);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
//        mDatabase = ((MainActivity) getActivity()).getDatabase();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(ARG_TAB_INDEX, mTabView.getCurrentTabIndex());
//        outState.putParcelable("database", mDatabase);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        MainActivity parentActivity = ((MainActivity) getActivity());

        parentActivity.setActionBarTitleWhenStop(parentActivity.getActionBarTitleTextView());
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
    }

    public void refreshFragment() {
        mDatabase = ((MainActivity) getActivity()).getDatabase();

        mBooksGridView.refreshDatabase();
        mNotesListView.refreshDatabase();

        mBooksGridView.refreshBooksView();
        mNotesListView.refresh();
    }

    public void renameNoteInNoteList(String newNoteName, int index) {
        mDatabase.renameNoteAt(index, newNoteName);
        mNotesListView.refresh();
    }

    private void loadNoteContentFromDBToPlayer(int position) {

        ArrayList<Integer> content = mNotesListView.getNotes().get(position).getContent();

//        mAudioService.setContentToPlayer(content, position);

    }

    public void showDialog() {
        ViewGroup viewGroup = (ViewGroup) getView();
        if (mDialogView != null) {
            viewGroup.addView(mDialogView);
            mDialogView.showDialog();
        }
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        DialogFragment newFragment = NoteDialogFragment.newInstance();
//        newFragment.show(ft, "dialog");

    }

    public void closeDialog(DialogView dialog) {
        ViewGroup viewGroup = (ViewGroup) getView();
        mDialogView = dialog;
        if (mDialogView != null) {
            mDialogView.closeDialog();
            viewGroup.removeView(dialog);
            mDialogView = null;
        }
    }

    public DialogView getDialog() {
        return mDialogView;
    }
}
