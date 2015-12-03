package com.wishcan.www.vocabulazy.view.reading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.view.lessons.LessonsViewPager;

/**
 * Created by swallow on 2015/12/3.
 */
public class ReadingLessonViewPager extends LessonsViewPager {

    private Context mContext;

    public ReadingLessonViewPager(Context context) {
        super(context);
    }

    public ReadingLessonViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public ViewGroup setMainPage() {
        return new ReadingLessonListView(getContext());
    }

}
