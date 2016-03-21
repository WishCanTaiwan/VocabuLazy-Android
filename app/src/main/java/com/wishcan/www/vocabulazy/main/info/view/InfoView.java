package com.wishcan.www.vocabulazy.main.info.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.widget.AutoResizeTextView;

/**
 * Created by swallow on 2016/3/6.
 */
public class InfoView extends LinearLayout {

    private static final int VIEW_RES_ID = R.layout.view_info;
//    private static final int VIEW_TEXT_RES_ID = R.id.info_auto_resize_text_view;
    private static final int VIEW_EDIT_TEXT_RES_ID = R.id.info_edit_text;

    private View mView;
    private AutoResizeTextView mTextView;
    private EditText mEditText;

    public InfoView(Context context) {
        this(context, null);
    }

    public InfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(VIEW_RES_ID, null);
        mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mView);
    }
}
