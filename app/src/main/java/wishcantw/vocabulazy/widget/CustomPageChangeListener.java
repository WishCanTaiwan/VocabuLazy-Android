package wishcantw.vocabulazy.widget;

import android.support.v4.view.ViewPager;

public class CustomPageChangeListener implements ViewPager.OnPageChangeListener {

    public static final String TAG = CustomPageChangeListener.class.getSimpleName();

    private ViewPager viewPager;
    private int position = 0;

    public CustomPageChangeListener(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_SETTLING) {
            int index = viewPager.getCurrentItem();
            if (index == position) onPageSelected(position);
        }
    }
}
