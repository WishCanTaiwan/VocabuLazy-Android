package com.wishcan.www.vocabulazy.storage;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by allencheng07 on 2015/9/21.
 */
public class Note {
    private static final String TAG = Note.class.getSimpleName();

    private int mID;
    private String mName;
    private ArrayList<Integer> mContent;

    public Note(int id, String name, ArrayList<Integer> content) {
        mID = id;
        mName = name;
        mContent = content;
    }

    public ArrayList<Integer> getContent() {
        return mContent;
    }

    public int getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

}
