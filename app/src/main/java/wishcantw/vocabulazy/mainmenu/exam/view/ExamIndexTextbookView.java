package wishcantw.vocabulazy.mainmenu.exam.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

public class ExamIndexTextbookView extends ExpandableListView implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {

    public interface OnExamTextbookClickListener {
        void onExamTextbookClicked(int bookIndex, int lessonIndex);
    }

    public static final String TAG = "ExamIndexTextbookView";

    private int lastExpandedGroupPosition = -1;
    private OnExamTextbookClickListener mOnExamTextbookClickListener;

    public ExamIndexTextbookView(Context context) {
        super(context);
        setOnChildClickListener(this);
        setOnGroupClickListener(this);
    }

    public ExamIndexTextbookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnChildClickListener(this);
        setOnGroupClickListener(this);
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
        mOnExamTextbookClickListener.onExamTextbookClicked(groupPosition, childPosition);
        return true;
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
        if (groupPosition != lastExpandedGroupPosition) {
            expandableListView.collapseGroup(lastExpandedGroupPosition);
            expandableListView.expandGroup(groupPosition, true);
            expandableListView.setSelection(groupPosition);
            lastExpandedGroupPosition = groupPosition;
        } else {
            expandableListView.collapseGroup(groupPosition);
            lastExpandedGroupPosition = -1;
        }
        return true;
    }

    public void addOnExamTextbookClickListener(OnExamTextbookClickListener listener) {
        mOnExamTextbookClickListener = listener;
    }
}
