package com.wishcan.www.vocabulazy.mainmenu.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.mainmenu.activity.MainMenuActivity;
import com.wishcan.www.vocabulazy.mainmenu.adapter.MainMenuFragmentPagerAdapter;
import com.wishcan.www.vocabulazy.mainmenu.exam.fragment.ExamIndexFragment;
import com.wishcan.www.vocabulazy.mainmenu.model.MainMenuModel;
import com.wishcan.www.vocabulazy.mainmenu.note.fragment.NoteFragment;
import com.wishcan.www.vocabulazy.mainmenu.textbook.fragment.TextbookFragment;
import com.wishcan.www.vocabulazy.mainmenu.view.MainMenuViewPager;

public class MainMenuFragment extends Fragment implements TextbookFragment.OnTextbookClickListener, NoteFragment.OnNoteClickListener, ExamIndexFragment.OnExamIndexClickListener {

    public interface OnMainMenuEventListener {
        void onTextbookSelected(int bookIndex, int lessonIndex);
        void onNoteSelected(int noteIndex);
        void onExamTextbookSelected(int examBookIndex, int examLessonIndex);
        void onExamNoteSelected(int examNoteIndex);
    }

    public static final String TAG = "MainMenuFragment";

    private MainMenuModel mModel;
    private OnMainMenuEventListener mOnMainMenuEventListener;
    private TextbookFragment mTextbookFragment;
    private NoteFragment mNoteFragment;
    private ExamIndexFragment mExamIndexFragment;

    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MainMenuFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Create");
        super.onCreate(savedInstanceState);
        initFragments();

        // here fakes the process of loading data
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // when data loaded
                mModel.generateBookItems();
                // when items ready
                mTextbookFragment.updateBookContent(mModel.getTextbookGroupItems(), mModel.getTextbookChildItemsMap());

                mModel.generateNoteItems();

                mNoteFragment.updateNoteContent(mModel.getNoteGroupItems(), mModel.getNoteChildItemsMap());

                mModel.generateExamIndexItems();

                mExamIndexFragment.updateExamIndexContent(mModel.getExamIndexTextbookGroupItems(), mModel.getExamIndexTextbookChildItemsMap(), mModel.getExamIndexNoteGroupItems(), mModel.getExamIndexNoteChildItemsMap());
            }
        }, 5000);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "Activity Created");
        super.onActivityCreated(savedInstanceState);
        mModel = ((MainMenuActivity) getActivity()).getModel();
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

        Fragment[] fragments = new Fragment[]{mTextbookFragment, mNoteFragment, mExamIndexFragment};
        String[] titles = new String[]{"Book", "Note", "Exam"};
        MainMenuFragmentPagerAdapter pagerAdapter = new MainMenuFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments, titles);

        MainMenuViewPager viewPager = (MainMenuViewPager) rootView.findViewById(R.id.main_menu_viewpager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(fragments.length);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.main_menu_tab_container);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }

    @Override
    public void onTextbookClicked(int bookIndex, int lessonIndex) {
        Log.d(TAG, "Textbook [" + bookIndex + ", " + lessonIndex + "] clicked");
        if (mOnMainMenuEventListener != null) {
            mOnMainMenuEventListener.onTextbookSelected(bookIndex, lessonIndex);
        }
    }

    @Override
    public void onNotePlay(int noteIndex) {
        Log.d(TAG, "Note [PLAY " + noteIndex + "]");
        if (mOnMainMenuEventListener != null) {
            mOnMainMenuEventListener.onNoteSelected(noteIndex);
        }
    }

    @Override
    public void onNoteRename(String name) {
        Log.d(TAG, "Note [RENAME]");
    }

    @Override
    public void onNoteCopy() {
        Log.d(TAG, "Note [COPY]");
    }

    @Override
    public void onNoteDelete(int noteIndex) {
        Log.d(TAG, "Note [DELETE " + noteIndex + "]");
    }

    @Override
    public void onExamTextbookClicked(int bookIndex, int lessonIndex) {
        Log.d(TAG, "Exam textbook [" + bookIndex + ", " + lessonIndex + "] clicked");
        if (mOnMainMenuEventListener != null) {
            mOnMainMenuEventListener.onExamTextbookSelected(bookIndex, lessonIndex);
        }
    }

    @Override
    public void onExamNoteClicked(int noteIndex) {
        Log.d(TAG, "Exam note [" + noteIndex + "]");
        if (mOnMainMenuEventListener != null) {
            mOnMainMenuEventListener.onExamNoteSelected(noteIndex);
        }
    }

    public void addOnMainMenuEventListener(OnMainMenuEventListener listener) {
        mOnMainMenuEventListener = listener;
    }

    private void initFragments() {
        if (mTextbookFragment == null) {
            mTextbookFragment = TextbookFragment.newInstance();
            mTextbookFragment.addOnTextbookClickListener(this);
        }

        if (mNoteFragment == null) {
            mNoteFragment = NoteFragment.newInstance();
            mNoteFragment.addOnNoteClickListener(this);
        }

        if (mExamIndexFragment == null) {
            mExamIndexFragment = ExamIndexFragment.newInstance();
            mExamIndexFragment.addOnExamIndexClickListener(this);
        }
    }
}
