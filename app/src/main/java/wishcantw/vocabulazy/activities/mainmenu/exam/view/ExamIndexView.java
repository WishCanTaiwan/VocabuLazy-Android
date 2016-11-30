package wishcantw.vocabulazy.activities.mainmenu.exam.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import wishcantw.vocabulazy.R;

import wishcantw.vocabulazy.activities.mainmenu.note.adapter.NoteContentAdapter;
import wishcantw.vocabulazy.activities.mainmenu.note.adapter.NoteExpandableChildItem;
import wishcantw.vocabulazy.activities.mainmenu.note.adapter.NoteExpandableGroupItem;
import wishcantw.vocabulazy.activities.mainmenu.textbook.adapter.TextbookContentAdapter;
import wishcantw.vocabulazy.activities.mainmenu.textbook.adapter.TextbookExpandableChildItem;
import wishcantw.vocabulazy.activities.mainmenu.textbook.adapter.TextbookExpandableGroupItem;

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
    private LinearLayout examIndexContainer;
    private ExamIndexTextbookView mExamIndexTextbookView;
    private ExamIndexNoteView mExamIndexNoteView;
    private OnExamIndexClickListener mOnExamIndexClickListener;

    private ArrayList<TextbookExpandableGroupItem> textbookGroupItems;
    private HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> textbookChildItemsMap;
    private ArrayList<NoteExpandableGroupItem> noteGroupItems;
    private HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> noteChildItemsMap;

    public ExamIndexView(Context context) {
        super(context);
    }

    public ExamIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onFinishInflate() {
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

                examIndexContainer.removeAllViewsInLayout();
                examIndexContainer.addView(mExamIndexTextbookView);

//                mExamIndexViewPager.setCurrentItem(VIEWPAGER_INDEX_TEXTBOOK);
                break;
            case R.id.exam_index_note_text:
                mExamIndexNoteTextView.setSelected(true);

                examIndexContainer.removeAllViewsInLayout();
                examIndexContainer.addView(mExamIndexNoteView);

//                mExamIndexViewPager.setCurrentItem(VIEWPAGER_INDEX_NOTE);
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

        this.textbookGroupItems = textbookGroupItems;
        this.textbookChildItemsMap = textbookChildItemsMap;
        this.noteGroupItems = noteGroupItems;
        this.noteChildItemsMap = noteChildItemsMap;

//        ExamIndexPagerAdapter pagerAdapter = new ExamIndexPagerAdapter(getContext(), new ViewGroup[]{mExamIndexTextbookView, mExamIndexNoteView}, textbookGroupItems, textbookChildItemsMap, noteGroupItems, noteChildItemsMap);

//        mExamIndexViewPager.setAdapter(pagerAdapter);
//        mExamIndexViewPager.setOffscreenPageLimit(pagerAdapter.getCount());
    }

    public void refresh() {
        mExamIndexTextbookView.setAdapter(new TextbookContentAdapter(textbookGroupItems, textbookChildItemsMap));
        mExamIndexNoteView.setAdapter(new NoteContentAdapter(noteGroupItems, noteChildItemsMap));
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
//            mExamIndexTextbookView = (ExamIndexTextbookView) findViewById(R.id.exam_index_textbook_view);
            mExamIndexTextbookView = new ExamIndexTextbookView(getContext());
            mExamIndexTextbookView.addOnExamTextbookClickListener(this);
            mExamIndexTextbookView.setVerticalScrollBarEnabled(false);
            mExamIndexTextbookView.setGroupIndicator(null);
        }

        if (mExamIndexNoteView == null) {
//            mExamIndexNoteView = (ExamIndexNoteView) findViewById(R.id.exam_index_note_view);
            mExamIndexNoteView = new ExamIndexNoteView(getContext());
            mExamIndexNoteView.addOnExamIndexNoteClickListener(this);
            mExamIndexNoteView.setVerticalScrollBarEnabled(false);
            mExamIndexNoteView.setGroupIndicator(null);
        }

        if (examIndexContainer == null) {
            examIndexContainer = (LinearLayout) findViewById(R.id.exam_index_container);
            examIndexContainer.addView(mExamIndexTextbookView);
        }

//        if (mExamIndexViewPager == null) {
//            ExamIndexPagerAdapter pagerAdapter = new ExamIndexPagerAdapter(getContext(), new ViewGroup[]{mExamIndexTextbookView, mExamIndexNoteView});
//            mExamIndexViewPager = (ExamIndexViewPager) findViewById(R.id.exam_index_container);
////            mExamIndexViewPager.setPagingEnabled(false);
//            mExamIndexViewPager.setAdapter(pagerAdapter);
//            mExamIndexViewPager.setOffscreenPageLimit(pagerAdapter.getCount());
//        }

        mExamIndexTextBookTextView.setSelected(true);
    }
}
