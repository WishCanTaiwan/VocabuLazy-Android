package com.wishcan.www.vocabulazy.widget;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.LinkedList;

/**
 * Created by swallow on 2016/1/11.
 */
public class LinkedListFragmentPagerAdapter extends FragmentPagerAdapter {

    private LinkedList<Class<?>> ll;

    private Context mContext;

    public LinkedListFragmentPagerAdapter(FragmentActivity activity, LinkedList<Class<?>> ll){
        super(activity.getSupportFragmentManager());
        mContext = activity;
        this.ll = ll;
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment.instantiate(mContext, ll.get(position).getName(), null);

    }

    @Override
    public int getCount() {
        return ll.size();
    }


}
