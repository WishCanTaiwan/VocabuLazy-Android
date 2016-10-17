package wishcantw.vocabulazy.mainmenu.textbook.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

public class TextbookView extends ExpandableListView implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {

    public interface OnTextbookClickListener {
        void onTextbookChildClicked(int groupPosition, int childPosition);
    }

    public static final String TAG = "TextBookView";

    private int lastExpandedGroupPosition = -1;
    private OnTextbookClickListener mOnTextbookClickListener;
    
    public TextbookView(Context context) {
        super(context);
        setOnChildClickListener(this);
        setOnGroupClickListener(this);
    }

    public TextbookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnChildClickListener(this);
        setOnGroupClickListener(this);
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

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
        mOnTextbookClickListener.onTextbookChildClicked(groupPosition, childPosition);
        return true;
    }

    public void addOnTextBookClickListener(@NonNull OnTextbookClickListener listener) {
        mOnTextbookClickListener = listener;
    }
}
