package com.wishcan.www.vocabulazy.widget;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

/**
 * Created by swallow on 2015/9/20.
 */
public class LinkedListPagerAdapter extends PagerAdapter {

    private LinkedList<ViewGroup> mLinkedList;

    private int mItemCount;

    public LinkedListPagerAdapter(LinkedList<ViewGroup> ll) {

        mLinkedList = ll;
        mItemCount = ll.size();

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = mLinkedList.get(position);
        if (v.getParent() != null) {
            ((ViewGroup) v.getParent()).removeView(v);
        }
        container.addView(v);
        return v;
    }

    @Override
    public int getCount() {
        return mItemCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

