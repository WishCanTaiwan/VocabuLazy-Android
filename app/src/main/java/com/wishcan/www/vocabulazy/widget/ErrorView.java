package com.wishcan.www.vocabulazy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by swallow on 2016/1/12.
 */
public class ErrorView extends TextView {

    public ErrorView(Context context) {
        this(context, null);

    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setText("Something wrong");
    }

    public ErrorView setErrorMsg(String errorMsg){
        setText(errorMsg);
        return this;
    }
}
