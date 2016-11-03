package wishcantw.vocabulazy.mainmenu.textbook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.ga.GABaseFragment;
import wishcantw.vocabulazy.mainmenu.textbook.adapter.TextbookContentAdapter;
import wishcantw.vocabulazy.mainmenu.textbook.adapter.TextbookExpandableChildItem;
import wishcantw.vocabulazy.mainmenu.textbook.adapter.TextbookExpandableGroupItem;
import wishcantw.vocabulazy.mainmenu.textbook.view.TextbookView;

import java.util.ArrayList;
import java.util.HashMap;

public class TextbookFragment extends GABaseFragment implements TextbookView.OnTextbookClickListener {

    @Override
    protected String getGALabel() {
        return Analytics.ScreenName.TEXTBOOK;
    }

    public interface OnTextbookClickListener {
        void onTextbookClicked(int bookIndex, int lessonIndex);
    }

    public static final String TAG = "TextBookFragment";

    private TextbookView mTextbookView;
    private ArrayList<TextbookExpandableGroupItem> mGroupItems;
    private HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> mChildItemsMap;
    private OnTextbookClickListener mOnTextbookClickListener;

    public static TextbookFragment newInstance() {
        TextbookFragment fragment = new TextbookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public TextbookFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_textbook, container, false);
        mTextbookView = (TextbookView) rootView.findViewById(R.id.textbook_view);
        mTextbookView.setAdapter(new TextbookContentAdapter(mGroupItems, mChildItemsMap));
        mTextbookView.addOnTextBookClickListener(this);
        return rootView;
    }

    @Override
    public void onTextbookChildClicked(int groupPosition, int childPosition) {
        mOnTextbookClickListener.onTextbookClicked(groupPosition, childPosition);
    }

    public void addOnTextbookClickListener(OnTextbookClickListener listener) {
        mOnTextbookClickListener = listener;
    }

    public void updateBookContent(ArrayList<TextbookExpandableGroupItem> groupItems, HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> childItemsMap) {
        mGroupItems = groupItems;
        mChildItemsMap = childItemsMap;
    }

    public void refresh() {
        mTextbookView.setAdapter(new TextbookContentAdapter(mGroupItems, mChildItemsMap));
    }
}
