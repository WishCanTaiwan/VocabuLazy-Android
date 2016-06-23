package com.wishcan.www.vocabulazy.storage.databaseObjects;

import java.util.ArrayList;

/**
 * Using this class to represent Lesson and Note objects which are read from the database.\n
 * Each Lesson object is consist of an id, a title and a set of content ids.
 *
 * @author Allen Cheng Yu-Lun
 * @version 1.0
 * @since 1.0
 */
public class Lesson {

    /**
     * String for debugging.
     */
    public static final String TAG = Lesson.class.getSimpleName();

    /**
     * Id of the lesson.
     */
    private int id;

    /**
     * Title of the lesson.
     */
    private String title;

    /**
     * Content of the lesson, containing a list of {@link Vocabulary} ids.
     */
    private ArrayList<Integer> content;

    /**
     * Call constructor to create a {@link Lesson} object.
     *
     * @param id the id of the lesson.
     * @param title the title of the lesson.
     * @param content the content of the lesson.
     */
    public Lesson(int id, String title, ArrayList<Integer> content) {
        setId(id);
        setTitle(title);
        setContent(content);
    }

    /**
     * Call this method to get the id of the lesson.
     *
     * @return id of the lesson.
     */
    public int getId() {
        return id;
    }

    /**
     * Call this method to set id to the lesson.
     *
     * @param id the id of lesson.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Call this method to get the title of the lesson.
     *
     * @return the title of the lesson.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Call this method to set title to the lesson.
     *
     * @param title the title of the lesson.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Call this method to get the {@link Vocabulary} ids contained in the lesson.
     *
     * @return the content of the lesson.
     */
    public ArrayList<Integer> getContent() {
        return content;
    }

    /**
     * Call this method to set content to the lesson.
     *
     * @param content the content of the lesson.
     */
    public void setContent(ArrayList<Integer> content) {
        this.content = content;
    }

    /**
     * Call this method to give the lesson a new title.
     *
     * @param newTitle the new title of the lesson.
     */
    public void rename(String newTitle) {
        title = newTitle;
    }

}
