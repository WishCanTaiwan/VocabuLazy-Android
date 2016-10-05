package wishcantw.vocabulazy.mainmenu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by allencheng07 on 2016/8/10.
 */
public class MainMenuFragmentPagerAdapter extends FragmentPagerAdapter {

    public static final String TAG = "MainMenuFragmentPagerAdapter";

    private Fragment[] fragments;
    private String[] titles;

    public MainMenuFragmentPagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    public MainMenuFragmentPagerAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return titles[position];
//    }
}
