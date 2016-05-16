package com.wishcan.www.vocabulazy.storage.databaseObjects;

import java.util.ArrayList;

public class Book {

    public static final String TAG = Book.class.getSimpleName();

    private int id;
    private String name;
    private ArrayList<Integer> content;

    public Book(int id, String name, ArrayList<Integer> content) {
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
}
