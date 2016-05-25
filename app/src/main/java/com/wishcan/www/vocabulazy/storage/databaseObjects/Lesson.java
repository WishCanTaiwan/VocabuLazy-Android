package com.wishcan.www.vocabulazy.storage.databaseObjects;

import org.json.JSONObject;

import java.util.ArrayList;

public class Lesson {

    public static final String TAG = Lesson.class.getSimpleName();

    private int id;
    private String title;
    private ArrayList<Integer> content;

    public Lesson(int id, String title, ArrayList<Integer> content) {
        setId(id);
        setTitle(title);
        setContent(content);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Integer> getContent() {
        return content;
    }

    public void setContent(ArrayList<Integer> content) {
        this.content = content;
    }

    public void rename(String newTitle) {
        title = newTitle;
    }

}
