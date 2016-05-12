package com.wishcan.www.vocabulazy.storage.databaseObjects;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.ArrayList;

public class Lesson {

    public static final String TAG = Lesson.class.getSimpleName();

    private int id;
    private String name;
    private ArrayList<Integer> content;

    public Lesson(int id, String name, ArrayList<Integer> content, JSONObject reading) {
        setId(id);
        setName(name);
        setContent(content);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getContent() {
        return content;
    }

    public void setContent(ArrayList<Integer> content) {
        this.content = content;
    }

    public void rename(String newName) {
        name = newName;
    }

//    protected Lesson(Parcel in) {
//        id = in.readInt();
//        name = in.readString();
//        content = in.readArrayList(Integer.class.getClassLoader());
//    }
//
//    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
//        @Override
//        public Lesson createFromParcel(Parcel in) {
//            return new Lesson(in);
//        }
//
//        @Override
//        public Lesson[] newArray(int size) {
//            return new Lesson[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(id);
//        dest.writeString(name);
//        dest.writeList(content);
//    }
}
