package wishcantw.vocabulazy.mainmenu.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by allencheng07 on 2016/8/16.
 */
public class MainMenuViewPager extends ViewPager implements NestedScrollingParent {

    public static final String TAG = "MainMenuViewPager";

    public MainMenuViewPager(Context context) {
        super(context);
    }

    public MainMenuViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
