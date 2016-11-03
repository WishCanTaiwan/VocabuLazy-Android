package wishcantw.vocabulazy.mainmenu.note.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import wishcantw.vocabulazy.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteContentAdapter extends BaseExpandableListAdapter {

    private ArrayList<NoteExpandableGroupItem> mGroupItems = new ArrayList<>();
    private HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> mChildItemsMap = new HashMap<>();

    public NoteContentAdapter(@NonNull ArrayList<NoteExpandableGroupItem> groupItems,
                              @NonNull HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> childItemsMap) {
        mGroupItems = groupItems;
        mChildItemsMap = childItemsMap;
    }

    @Override
    public int getGroupCount() {
        return mGroupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildItemsMap.get(mGroupItems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildItemsMap.get(mGroupItems.get(groupPosition)).get(childPosition);
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

        // get note strings
        String groupIndex = String.valueOf(groupPosition+1);
        String groupTitle = ((NoteExpandableGroupItem) getGroup(groupPosition)).getGroupStr();

        // if convert view is null, inflate a new one
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_group, parent, false);
        }

        // set note index
        ((TextView) convertView.findViewById(R.id.textView_index)).setText(groupIndex);

        // set note title
        ((TextView) convertView.findViewById(R.id.textView_title)).setText(groupTitle);

        // tag the last group
        boolean isLastGroup = getGroupCount()-1 == groupPosition;

        // set divider visibility
        convertView.findViewById(R.id.view_divider)
                .setVisibility((isExpanded || isLastGroup)
                        ? View.GONE
                        : View.VISIBLE);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        // get title strings
        String childTitle = ((NoteExpandableChildItem) getChild(groupPosition, childPosition)).getChildStr();

        // if the convert view is null, inflate one
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_child, parent, false);
        }

        // set note child text
        ((TextView) convertView.findViewById(R.id.textView_option)).setText(childTitle);

        // tag for last group
        boolean isLastGroup = getGroupCount()-1 == groupPosition;

        // set divider visibility
        convertView.findViewById(R.id.view_divider)
                .setVisibility((isLastChild && !isLastGroup)
                        ? View.VISIBLE
                        : View.GONE);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
