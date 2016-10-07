package wishcantw.vocabulazy.mainmenu.exam.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.ga.GABaseFragment;
import wishcantw.vocabulazy.ga.tags.GAScreenName;
import wishcantw.vocabulazy.mainmenu.exam.view.ExamIndexView;
import wishcantw.vocabulazy.mainmenu.note.adapter.NoteExpandableChildItem;
import wishcantw.vocabulazy.mainmenu.note.adapter.NoteExpandableGroupItem;
import wishcantw.vocabulazy.mainmenu.textbook.adapter.TextbookExpandableChildItem;
import wishcantw.vocabulazy.mainmenu.textbook.adapter.TextbookExpandableGroupItem;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamIndexFragment extends GABaseFragment implements ExamIndexView.OnExamIndexClickListener {

    @Override
    protected String getGALabel() {
        return GAScreenName.EXAM_TEXTBOOK;
    }

    public interface OnExamIndexClickListener {
        void onExamTextbookClicked(int bookIndex, int lessonIndex);
        void onExamNoteClicked(int noteIndex);
    }

    public static final String TAG = "ExamIndexFragment";

    private ExamIndexView mExamIndexView;
    private ArrayList<TextbookExpandableGroupItem> mTextbookGroupItems;
    private HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> mTextbookChildItemsMap;
    private ArrayList<NoteExpandableGroupItem> mNoteGroupItems;
    private HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> mNoteChildItemsMap;
    private OnExamIndexClickListener mOnExamIndexClickListener;

    public static ExamIndexFragment newInstance() {
        ExamIndexFragment fragment = new ExamIndexFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ExamIndexFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exam_index, container, false);
        mExamIndexView = (ExamIndexView) rootView.findViewById(R.id.exam_index_view);
        mExamIndexView.updateContent(mTextbookGroupItems, mTextbookChildItemsMap, mNoteGroupItems, mNoteChildItemsMap);
        mExamIndexView.refresh();
        mExamIndexView.addOnExamIndexClickListener(this);
        return rootView;
    }

    @Override
    public void onExamTextbookClicked(int bookIndex, int lessonIndex) {
        // TODO: prevent from entering exam if there's too less vocabulary (5)
        mOnExamIndexClickListener.onExamTextbookClicked(bookIndex, lessonIndex);
    }

    @Override
    public void onExamNoteClicked(int noteIndex) {
        mOnExamIndexClickListener.onExamNoteClicked(noteIndex);
    }

    public void addOnExamIndexClickListener(OnExamIndexClickListener listener) {
        mOnExamIndexClickListener = listener;
    }

    public void updateExamIndexContent(ArrayList<TextbookExpandableGroupItem> textbookGroupItems,
                                       HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> textbookChildItemsMap,
                                       ArrayList<NoteExpandableGroupItem> noteGroupItems,
                                       HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> noteChildItemsMap) {
        mTextbookGroupItems = textbookGroupItems;
        mTextbookChildItemsMap = textbookChildItemsMap;

        mNoteGroupItems = noteGroupItems;
        mNoteChildItemsMap = noteChildItemsMap;
    }

    public void refresh() {
        mExamIndexView.updateContent(mTextbookGroupItems, mTextbookChildItemsMap, mNoteGroupItems, mNoteChildItemsMap);
        mExamIndexView.refresh();
    }
}
