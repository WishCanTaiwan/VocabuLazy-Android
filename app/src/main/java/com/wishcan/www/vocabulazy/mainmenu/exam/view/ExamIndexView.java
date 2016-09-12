package com.wishcan.www.vocabulazy.mainmenu.exam.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.mainmenu.exam.adapter.ExamIndexPagerAdapter;
import com.wishcan.www.vocabulazy.mainmenu.note.adapter.NoteExpandableChildItem;
import com.wishcan.www.vocabulazy.mainmenu.note.adapter.NoteExpandableGroupItem;
import com.wishcan.www.vocabulazy.mainmenu.textbook.adapter.TextbookExpandableChildItem;
import com.wishcan.www.vocabulazy.mainmenu.textbook.adapter.TextbookExpandableGroupItem;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamIndexView extends LinearLayout implements TextView.OnClickListener, ExamIndexTextbookView.OnExamTextbookClickListener, ExamIndexNoteView.OnExamIndexNoteClickListener {

    public interface OnExamIndexClickListener {
        void onExamTextbookClicked(int bookIndex, int lessonIndex);
        void onExamNoteClicked(int noteIndex);
    }

    public static final String TAG = "ExamIndexView";

    private static final int VIEWPAGER_INDEX_TEXTBOOK = 0x0;
    private static final int VIEWPAGER_INDEX_NOTE = 0x1;

    private TextView mExamIndexTextBookTextView;
    private TextView mExamIndexNoteTextView;
    private ExamIndexViewPager mExamIndexViewPager;
    private ExamIndexTextbookView mExamIndexTextbookView;
    private ExamIndexNoteView mExamIndexNoteView;
    private OnExamIndexClickListener mOnExamIndexClickListener;

    public ExamIndexView(Context context) {
        super(context);
    }

    public ExamIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        Log.d(TAG, "Finish Inflate");
        super.onFinishInflate();

        // after inflation completed, we can findViewById and set up listeners
        setUpViewsAndListeners();
    }

    @Override
    public void onClick(View view) {
        mExamIndexTextBookTextView.setSelected(false);
        mExamIndexNoteTextView.setSelected(false);
        switch (view.getId()) {
            case R.id.exam_index_textbook_text:
                mExamIndexTextBookTextView.setSelected(true);
                mExamIndexViewPager.setCurrentItem(VIEWPAGER_INDEX_TEXTBOOK);
                break;
            case R.id.exam_index_note_text:
                mExamIndexNoteTextView.setSelected(true);
                mExamIndexViewPager.setCurrentItem(VIEWPAGER_INDEX_NOTE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onExamTextbookClicked(int bookIndex, int lessonIndex) {
        mOnExamIndexClickListener.onExamTextbookClicked(bookIndex, lessonIndex);
    }

    /**
     *
     * @param noteIndex
     */
    @Override
    public void OnExamIndexNoteClicked(int noteIndex) {
        mOnExamIndexClickListener.onExamNoteClicked(noteIndex);
    }

    /**
     *
     * @param textbookGroupItems
     * @param textbookChildItemsMap
     * @param noteGroupItems
     * @param noteChildItemsMap
     */
    public void updateContent(ArrayList<TextbookExpandableGroupItem> textbookGroupItems,
                              HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> textbookChildItemsMap,
                              ArrayList<NoteExpandableGroupItem> noteGroupItems,
                              HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> noteChildItemsMap) {
        ExamIndexPagerAdapter pagerAdapter = new ExamIndexPagerAdapter(getContext(), new ViewGroup[]{mExamIndexTextbookView, mExamIndexNoteView}, textbookGroupItems, textbookChildItemsMap, noteGroupItems, noteChildItemsMap);

        //
        //
        mExamIndexViewPager.setAdapter(pagerAdapter);
        mExamIndexViewPager.setOffscreenPageLimit(pagerAdapter.getCount());
    }

    public void addOnExamIndexClickListener(OnExamIndexClickListener listener) {
        mOnExamIndexClickListener = listener;
    }

    private void setUpViewsAndListeners() {
        if (mExamIndexTextBookTextView == null) {
            mExamIndexTextBookTextView = (TextView) findViewById(R.id.exam_index_textbook_text);
            mExamIndexTextBookTextView.setOnClickListener(this);
        }

        if (mExamIndexNoteTextView == null) {
            mExamIndexNoteTextView = (TextView) findViewById(R.id.exam_index_note_text);
            mExamIndexNoteTextView.setOnClickListener(this);
        }

        if (mExamIndexTextbookView == null) {
            mExamIndexTextbookView = (ExamIndexTextbookView) findViewById(R.id.exam_index_textbook_view);
            mExamIndexTextbookView.addOnExamTextbookClickListener(this);
        }

        if (mExamIndexNoteView == null) {
            mExamIndexNoteView = (ExamIndexNoteView) findViewById(R.id.exam_index_note_view);
            mExamIndexNoteView.addOnExamIndexNoteClickListener(this);
        }

        if (mExamIndexViewPager == null) {
            ExamIndexPagerAdapter pagerAdapter = new ExamIndexPagerAdapter(getContext(), new ViewGroup[]{mExamIndexTextbookView, mExamIndexNoteView});
            mExamIndexViewPager = (ExamIndexViewPager) findViewById(R.id.exam_index_container);
//            mExamIndexViewPager.setPagingEnabled(false);
            mExamIndexViewPager.setAdapter(pagerAdapter);
            mExamIndexViewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        }

        mExamIndexTextBookTextView.setSelected(true);
    }
}
