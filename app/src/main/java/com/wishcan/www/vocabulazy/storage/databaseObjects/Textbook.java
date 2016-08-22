package com.wishcan.www.vocabulazy.storage.databaseObjects;

import java.util.ArrayList;

/**
 * Created by allencheng07 on 2016/8/22.
 */
public class Textbook {
    private int bookId;
    private String bookTitle;
    private ArrayList<Lesson> bookContent;

    public Textbook(int bookId, String bookTitle, ArrayList<Lesson> bookContent) {
        setBookId(bookId);
        setBookTitle(bookTitle);
        setBookContent(bookContent);
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public ArrayList<Lesson> getBookContent() {
        return bookContent;
    }

    public void setBookContent(ArrayList<Lesson> bookContent) {
        this.bookContent = bookContent;
    }
}
