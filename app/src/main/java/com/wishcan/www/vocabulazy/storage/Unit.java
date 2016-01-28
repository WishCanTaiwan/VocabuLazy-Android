package com.wishcan.www.vocabulazy.storage;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;

/**
 * Created by allencheng07 on 2015/9/30.
 */
public class Unit implements Parcelable {
    private static final String TAG = Unit.class.getSimpleName();

    private int mID;
    private String mName;
    private LinkedList<Integer> mContent;

    public Unit(int id, String name, LinkedList content) {
        mID = id;
        mName = name;
        mContent = content;
    }

    protected Unit(Parcel in) {
        mID = in.readInt();
        mName = in.readString();
    }

    public static final Creator<Unit> CREATOR = new Creator<Unit>() {
        @Override
        public Unit createFromParcel(Parcel in) {
            return new Unit(in);
        }

        @Override
        public Unit[] newArray(int size) {
            return new Unit[size];
        }
    };

    public int getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public LinkedList<Integer> getContent() {
        return mContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mID);
        dest.writeString(mName);
    }
}
