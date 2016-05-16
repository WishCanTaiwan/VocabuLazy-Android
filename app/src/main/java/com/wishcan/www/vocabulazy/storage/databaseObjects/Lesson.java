package com.wishcan.www.vocabulazy.storage.databaseObjects;

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

}
