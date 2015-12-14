package com.wishcan.www.vocabulazy.storage;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by allencheng07 on 2015/9/30.
 */
public class Lesson implements Parcelable {

    private static final String TAG = Lesson.class.getSimpleName();

    private int mID;
    private String mName;
    private ArrayList<Integer> mContent;
    public JSONObject mReading;

    public Lesson(int id, String name, ArrayList<Integer> content, JSONObject reading) {
        mID = id;
        mName = name;
        mContent = content;
        mReading = reading;
    }

    protected Lesson(Parcel in) {
        mID = in.readInt();
        mName = in.readString();
        mContent = in.readArrayList(Integer.class.getClassLoader());
        mReading = null;
    }

    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };

    public int getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public ArrayList<Integer> getContent() {
        return mContent;
    }

    public void rename(String name) {
        mName = name;
    }

    public void setID(int id) {
        mID = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mID);
        dest.writeString(mName);
        dest.writeList(mContent);
    }
}
