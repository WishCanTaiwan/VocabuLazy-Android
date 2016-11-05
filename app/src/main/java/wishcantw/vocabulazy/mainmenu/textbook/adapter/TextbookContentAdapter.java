package wishcantw.vocabulazy.mainmenu.textbook.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.database.Database;

import java.util.ArrayList;
import java.util.HashMap;

public class TextbookContentAdapter extends BaseExpandableListAdapter {

    public static final String TAG = "TextbookContentAdapter";

//    private static final float RATIO_GROUP = 170f/647f;
//    private static final float RATIO_CHILD = 7f/34f;

    private ArrayList<TextbookExpandableGroupItem> mGroupItems = new ArrayList<>();
    private HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> mChildItems = new HashMap<>();

    public TextbookContentAdapter(@NonNull ArrayList<TextbookExpandableGroupItem> groupItems,
                                  @NonNull HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> childItems) {
        mGroupItems = groupItems;
        mChildItems = childItems;
    }

    @Override
    public int getGroupCount() {
        return mGroupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildItems.get(mGroupItems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildItems.get(mGroupItems.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        // get group texts
        String textbookType = Database.getInstance().getTextbookType(groupPosition);
        String groupTitle = ((TextbookExpandableGroupItem) getGroup(groupPosition)).getGroupStr();

        // if convert view is null, inflate a new one
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textbook_group, parent, false);
        }

        // set textbook type text
        ((TextView) convertView.findViewById(R.id.textView_bookTitle)).setText(textbookType);

        // set textbook subtitle
        ((TextView) convertView.findViewById(R.id.textView_bookSubTitle)).setText(groupTitle);

        // tag for last group
        boolean isLastGroup = getGroupCount()-1 == groupPosition;

        // set group divider visibility
        convertView.findViewById(R.id.view_divider)
                .setVisibility((isExpanded || isLastGroup)
                        ? View.GONE
                        : View.VISIBLE);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        // get child title
        String childTitle = ((TextbookExpandableChildItem) getChild(groupPosition, childPosition)).getChildStr();

        // if convertView is null, inflate a new layout
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textbook_child, parent, false);
        }

        // get child text view and set text
        ((TextView) convertView.findViewById(R.id.textView_lessonTitle)).setText(childTitle);

        // tag for last group
        boolean isLastGroup = getGroupCount()-1 == groupPosition;

        // set divider visibility
        convertView.findViewById(R.id.view_divider).setVisibility(
                (isLastChild && !isLastGroup)
                        ? View.VISIBLE
                        : View.GONE);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
