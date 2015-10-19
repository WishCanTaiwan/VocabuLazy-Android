package com.wishcan.www.vocabulazy.storage;

import java.util.LinkedList;

/**
 * Created by allencheng07 on 2015/9/30.
 */
public class Unit {
    private static final String TAG = Unit.class.getSimpleName();

    private int mID;
    private String mName;
    private LinkedList<Integer> mContent;

    public Unit(int id, String name, LinkedList content) {
        mID = id;
        mName = name;
        mContent = content;
    }

    public int getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public LinkedList<Integer> getContent() {
        return mContent;
    }
}
