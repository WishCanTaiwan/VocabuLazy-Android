package com.wishcan.www.vocabulazy.main.exam.view;

import android.content.Context;
import android.util.AttributeSet;

import com.wishcan.www.vocabulazy.widget.NoteView;

/**
 * Created by swallow on 2016/1/14.
 */
public class ExamNoteView extends NoteView {
    @Override
    public boolean enableSlideBack() {
        return true;
    }

    public ExamNoteView(Context context) {
        this(context, null);
    }

    public ExamNoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
