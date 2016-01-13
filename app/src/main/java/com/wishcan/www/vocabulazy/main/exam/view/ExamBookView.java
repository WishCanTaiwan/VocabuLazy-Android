package com.wishcan.www.vocabulazy.main.exam.view;

import android.content.Context;
import android.util.AttributeSet;

import com.wishcan.www.vocabulazy.widget.BookView;

/**
 * Created by swallow on 2016/1/13.
 */
public class ExamBookView extends BookView {

    public ExamBookView(Context context) {
        super(context);
    }

    public ExamBookView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean enableSlideBack() {
        return true;
    }
}
