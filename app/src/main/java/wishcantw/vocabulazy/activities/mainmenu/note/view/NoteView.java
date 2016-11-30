package wishcantw.vocabulazy.activities.mainmenu.note.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

public class NoteView extends ExpandableListView implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {

    public interface OnNoteItemClickListener {
        void onNoteChildClicked(int groupPosition, int childPosition);
    }

    private int lastExpandedGroupPosition = -1;
    private OnNoteItemClickListener mOnNoteItemClickListener;

    public NoteView(Context context) {
        super(context);
        setOnChildClickListener(this);
        setOnGroupClickListener(this);
    }

    public NoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnChildClickListener(this);
        setOnGroupClickListener(this);
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
        mOnNoteItemClickListener.onNoteChildClicked(groupPosition, childPosition);
        return true;
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
        if (groupPosition != lastExpandedGroupPosition) {
            expandableListView.collapseGroup(lastExpandedGroupPosition);
            expandableListView.expandGroup(groupPosition, true);
            expandableListView.smoothScrollToPosition(groupPosition);
            lastExpandedGroupPosition = groupPosition;
        } else {
            expandableListView.collapseGroup(groupPosition);
            lastExpandedGroupPosition = -1;
        }
        return true;
    }

    public void addOnNoteItemListener(@NonNull OnNoteItemClickListener listener) {
        mOnNoteItemClickListener = listener;
    }
}
