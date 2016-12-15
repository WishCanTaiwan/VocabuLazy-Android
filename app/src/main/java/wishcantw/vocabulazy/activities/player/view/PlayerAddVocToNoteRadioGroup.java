package wishcantw.vocabulazy.activities.player.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.activities.player.activity.PlayerActivity;

import java.util.LinkedList;

/**
 * Created by SwallowChen on 9/6/16.
 */
public class PlayerAddVocToNoteRadioGroup extends RadioGroup {

    private static final int PLAYER_ADD_VOC_TO_NOTE_LIST_ITEM_RES_ID = R.layout.view_player_add_voc_to_note_radio_button;

    private Context mContext;
    private LinkedList<String> mDataList;
    private CustomizedSimpleAdapter mAdapter;

    public PlayerAddVocToNoteRadioGroup(Context context) {
        this(context, null);
    }

    public PlayerAddVocToNoteRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mDataList = new LinkedList<>();
        mAdapter = new CustomizedSimpleAdapter(context, mDataList, PLAYER_ADD_VOC_TO_NOTE_LIST_ITEM_RES_ID);
        setAdapter(mAdapter);
    }

    /**
     * TODO: refine performance
     * @param adapter
     */
    public void setAdapter(CustomizedSimpleAdapter adapter) {
        removeAllViews();
        /** The index showing newNote radio button */
        int newNoteIndex = mDataList.size();
        for (int i = 0; i < mDataList.size(); i++) {
            addView(adapter.getView(i, null, this));
        }
        addView(adapter.getView(newNoteIndex, null, this));
    }

    /**
     * TODO: for future use, need refined
     */
    private void refresh() {
        setAdapter(new CustomizedSimpleAdapter(mContext, mDataList, PLAYER_ADD_VOC_TO_NOTE_LIST_ITEM_RES_ID));
    }

    /**
     * TODO: for future use, need refined
     */
    public void refreshView(LinkedList<String> linkedList) {
        if(linkedList == null) {
            return;
        }

        mDataList.clear();
        mDataList = linkedList;
        refresh();
    }

    class CustomizedSimpleAdapter {

        private Context mContext;
        private LinkedList<String> mDataList;
        private int mResource;

        private LayoutInflater mInflater;
        /**
         * Constructor
         *
         * @param context  The context where the View associated with this SimpleAdapter is running
         * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
         *                 Maps contain the data for each row, and should include all the entries specified in
         *                 "from"
         * @param resource Resource identifier of a view layout that defines the views for this list
         *                 item. The layout file should include at least those named views defined in "to"
         */
        public CustomizedSimpleAdapter(Context context, LinkedList<String> data, int resource) {
            mContext = context;
            mDataList = data;
            mResource = resource;

            mInflater = ((PlayerActivity) mContext).getLayoutInflater();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return createViewFromResource(position, convertView, parent , mResource);
        }

        private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource){
            View v;
            if(convertView == null) {
                v = mInflater.inflate(resource, parent, false);
            }
            else {
                v = convertView;
            }
            bindView(position, v);
            return v;
        }

        private void bindView(int position, View v) {
            if (mDataList == null) {
                return;
            }

            if (v instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) v;
                if (position == mDataList.size()) {
                    radioButton.setText("新增筆記清單");
                } else {
                    radioButton.setText(mDataList.get(position));
                }
                radioButton.setId(position);
            }
        }
    }
}
