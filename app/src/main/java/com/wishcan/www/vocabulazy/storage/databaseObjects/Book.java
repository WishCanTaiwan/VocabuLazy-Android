package com.wishcan.www.vocabulazy.storage.databaseObjects;

import java.util.ArrayList;

/**
 * Using this class to represent Book objects which are read from the database.\n
 * Each Book object is consist of an id, a title and a set of content ids.
 *
 * @author Allen Cheng Yu-Lun
 * @version 1.0
 * @since 1.0
 */
public class Book {

    /**
     * String for debug logging.
     */
    public static final String TAG = Book.class.getSimpleName();

    /**
     * Id of the book.
     */
    private int id;

    /**
     * Title of the book.
     */
    private String title;

    /**
     * Content of the book. The ArrayList contains one or more Lesson Ids which belongs to the book.
     */
    private ArrayList<Integer> content;

    /**
     * Call this constructor to create a new Book object.
     *
     * @param id the id of the book.
     * @param title the title of the book.
     * @param content the content of the book.
     */
    public Book(int id, String title, ArrayList<Integer> content) {
        setId(id);
        setTitle(title);
        setContent(content);
    }

    /**
     * Call this method to get the id of the book.
     *
     * @return the id of the book.
     */
    public int getId() {
        return id;
    }

    /**
     * Call this method to set id to the book.
     *
     * @param id the id of the book.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Call this method to get the title of the book.
     *
     * @return the title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Call this method to set title to the book.
     *
     * @param title the title of the book.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Call this method to get a {@link ArrayList<Integer>} which contains lesson ids of the book.
     *
     * @return the content of the book.
     */
    public ArrayList<Integer> getContent() {
        return content;
    }

    /**
     * Call this method to set content to the book.
     *
     * @param content the content of the book.
     */
    public void setContent(ArrayList<Integer> content) {
        this.content = content;
    }
}
