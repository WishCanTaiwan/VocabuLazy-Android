package wishcantw.vocabulazy.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private Fragment[] fragments;
    private int[] rid_drawables;
    private int[] rid_tags;

    public CustomFragmentPagerAdapter(Context context, FragmentManager fm, Fragment[] fragments, int[] rid_drawables, int[] rid_tags) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        this.rid_drawables = rid_drawables;
        this.rid_tags = rid_tags;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    public Drawable getDrawable(int position) {
        return ContextCompat.getDrawable(context, rid_drawables[position]);
    }

    public String getTag(int position) {
        return context.getString(rid_tags[position]);
    }
}
