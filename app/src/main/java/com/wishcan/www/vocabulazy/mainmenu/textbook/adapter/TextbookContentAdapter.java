package com.wishcan.www.vocabulazy.mainmenu.textbook.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TextbookContentAdapter extends BaseExpandableListAdapter {

    public static final String TAG = "TextbookContentAdapter";

    private static final float RATIO_GROUP = 170f/647f;
    private static final float RATIO_CHILD = 7f/34f;

    private int groupHeight;
    private int childHeight;

    private Context mContext;
    private ArrayList<TextbookExpandableGroupItem> mGroupItems;
    private HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> mChildItems;

    public TextbookContentAdapter(Context context, ArrayList<TextbookExpandableGroupItem> groupItems, HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> childItems) {
        if (context == null) Log.d(TAG, "context is null");
        mContext = context;
        mGroupItems = groupItems;
        mChildItems = childItems;

        calculateLayoutParams();
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
        String groupTitle = ((TextbookExpandableGroupItem) getGroup(groupPosition)).getGroupStr();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_textbook_group, parent, false);
        }

        TextView groupTextView = (TextView) convertView.findViewById(R.id.book_subtitle);
        groupTextView.setText(groupTitle);
//        groupTextView.setTypeface(null, Typeface.BOLD);

        ExpandableListView expandableListView = (ExpandableListView) parent;
        boolean isLastGroup = (expandableListView.getExpandableListAdapter().getGroupCount()-1 == groupPosition);

        View groupDivider = convertView.findViewById(R.id.group_divider);
        groupDivider.setVisibility((isExpanded || isLastGroup) ? View.GONE : View.VISIBLE);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childTitle = ((TextbookExpandableChildItem) getChild(groupPosition, childPosition)).getChildStr();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_textbook_child, parent, false);
        }

        TextView childTextView = (TextView) convertView.findViewById(R.id.lesson_title);
        childTextView.setText(childTitle);

        ExpandableListView expandableListView = (ExpandableListView) parent;
        boolean isLastGroup = (expandableListView.getExpandableListAdapter().getGroupCount()-1 == groupPosition);

        View childDivider = convertView.findViewById(R.id.child_divider);
        childDivider.setVisibility((isLastChild && !isLastGroup) ? View.VISIBLE : View.GONE);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void calculateLayoutParams() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float screenHeightDp = displayMetrics.heightPixels / displayMetrics.density;
        int groupHeightDp = (int) (screenHeightDp * RATIO_GROUP + 0.5f);
        int childHeightDp = (int) (groupHeightDp * RATIO_CHILD + 0.5f);
        groupHeight = toPixel(groupHeightDp, mContext.getResources().getDisplayMetrics().density);
        childHeight = toPixel(childHeightDp, mContext.getResources().getDisplayMetrics().density);
    }

    private int toPixel(int dp, float density) {
        return (int) (dp*density + 0.5f);
    }
}
