package com.wishcan.www.vocabulazy.main.voc.view;

import android.content.Context;
import android.util.AttributeSet;

import com.wishcan.www.vocabulazy.widget.BookView;

import java.util.LinkedList;

/**
 * Created by swallow on 2016/1/11.
 */
public class VocBookView extends BookView {

    public VocBookView(Context context) {
        super(context);
    }

    public VocBookView(Context context, AttributeSet attrs, LinkedList<String> booksNameLL) {
        super(context, attrs);
        if(booksNameLL != null)
            refreshView(booksNameLL.size(), booksNameLL);
    }

    @Override
    public boolean enableSlideBack() {
        return false;
    }
}
