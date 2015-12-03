package com.wishcan.www.vocabulazy.view.unit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.wishcan.www.vocabulazy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by swallow on 2015/11/22.
 */
public class UnitExpandableListView extends ExpandableListView {

    // TODO: Mapping to the outer list
    String[] groupFrom  = {"ROOT_NAME"};
    //int[] groupTo       = new int[] {android.R.id.text1};
    int[] groupTo       = new int[] {R.id.unit_expandable_group_list_text_1};

    // TODO: Mapping to the inner list
    String[] childFrom  = {"CHILD_NAME"};
    //int[] childTo       = new int[] {android.R.id.text1, android.R.id.text2};
    int[] childTo       = new int[] {R.id.unit_expandable_child_list_text_1};

    private HashMap<String, String> groupData;
    private LinkedList<HashMap<String, String>> listOfGroups;

    private LinkedList<HashMap<String, String>> listOfChildGroups;

    ArrayList<LinkedList<HashMap<String, String>>> listOfListChildGroups = new ArrayList<>();

    private SimpleExpandableListAdapter mListAdapter;

    public UnitExpandableListView(Context context) {
        this(context, null);
    }

    public UnitExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDividerHeight(0);
        setSmoothScrollbarEnabled(true);
        

        listOfGroups = new LinkedList<HashMap<String, String>>() {{
            add(new HashMap<String, String>() {{
                put("ROOT_NAME", "Group 1");
            }});
            add(new HashMap<String, String>() {{
                put("ROOT_NAME", "Group 2");
            }});
        }};


        listOfListChildGroups.add(new LinkedList<HashMap<String, String>>() {{
            add(new HashMap<String, String>() {{
                put("CHILD_NAME", "child in group 1");
            }});
            add(new HashMap<String, String>() {{
                put("CHILD_NAME", "child in group 1");
            }});
        }});

        listOfListChildGroups.add(new LinkedList<HashMap<String,String>>(){{
            add(new HashMap<String, String>() {{
                put("CHILD_NAME", "child in group 2");
            }});
            add(new HashMap<String, String>() {{
                put("CHILD_NAME", "child in group 2");
            }});
        }});

        mListAdapter = new SimpleExpandableListAdapter
                (context,listOfGroups,R.layout.unit_expandable_group_layout,groupFrom,groupTo, listOfListChildGroups,
                        R.layout.unit_expandable_child_layout,childFrom, childTo);
        setAdapter(mListAdapter);

    }
}
