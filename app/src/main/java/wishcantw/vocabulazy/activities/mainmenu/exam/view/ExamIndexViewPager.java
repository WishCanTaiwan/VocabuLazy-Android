package wishcantw.vocabulazy.activities.mainmenu.exam.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class ExamIndexViewPager extends ViewPager implements NestedScrollingChild {

    public static final String TAG = "ExamIndexViewPager";

    public ExamIndexViewPager(Context context) {
        super(context);
    }

    public ExamIndexViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
