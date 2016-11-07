package wishcantw.vocabulazy.mainmenu.exam.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.mainmenu.exam.view.ExamIndexNoteView;
import wishcantw.vocabulazy.mainmenu.exam.view.ExamIndexTextbookView;
import wishcantw.vocabulazy.mainmenu.note.adapter.NoteContentAdapter;
import wishcantw.vocabulazy.mainmenu.note.adapter.NoteExpandableChildItem;
import wishcantw.vocabulazy.mainmenu.note.adapter.NoteExpandableGroupItem;
import wishcantw.vocabulazy.mainmenu.textbook.adapter.TextbookContentAdapter;
import wishcantw.vocabulazy.mainmenu.textbook.adapter.TextbookExpandableChildItem;
import wishcantw.vocabulazy.mainmenu.textbook.adapter.TextbookExpandableGroupItem;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamIndexPagerAdapter extends PagerAdapter {

    public static final String TAG = "ExamIndexPagerAdapter";

    private static final int VIEW_INDEX_TEXTBOOK = 0x0;
    private static final int VIEW_INDEX_NOTE = 0x1;

    private Context context;
    private View[] views;
    private ArrayList<TextbookExpandableGroupItem> textbookGroupItems;
    private HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> textbookChildItemsMap;
    private ArrayList<NoteExpandableGroupItem> noteGroupItems;
    private HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> noteChildItemsMap;

    public ExamIndexPagerAdapter(Context context, View[] views) {
        this(context, views, null, null, null, null);
    }

    public ExamIndexPagerAdapter(Context context,
                                 View[] views,
                                 ArrayList<TextbookExpandableGroupItem> textbookGroupItems,
                                 HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> textbookChildItemsMap,
                                 ArrayList<NoteExpandableGroupItem> noteGroupItems,
                                 HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> noteChildItemsMap) {
        this.context = context;
        this.views = views;
        this.textbookGroupItems = textbookGroupItems;
        this.textbookChildItemsMap = textbookChildItemsMap;
        this.noteGroupItems = noteGroupItems;
        this.noteChildItemsMap = noteChildItemsMap;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // get current view
        View view = views[position];

        // if items are null, return raw view
        if (textbookGroupItems == null || textbookChildItemsMap == null || noteGroupItems == null || noteChildItemsMap == null) {
            container.addView(views[position]);
            return view;
        }

        // update view content with items
        switch (position) {
            case VIEW_INDEX_TEXTBOOK:
                ((ExamIndexTextbookView) view).setAdapter(new TextbookContentAdapter(textbookGroupItems, textbookChildItemsMap));
                break;
            case VIEW_INDEX_NOTE:
                ((ExamIndexNoteView) view).setAdapter(new NoteContentAdapter(noteGroupItems, noteChildItemsMap));
                break;
            default:
                break;
        }

        // add updated view to container/viewgroup
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return views.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
