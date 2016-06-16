package com.wishcan.www.vocabulazy.main.voc.model;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Book;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Lesson;

import java.util.ArrayList;

/**
 * Created by allencheng07 on 2016/6/15.
 */
public class VocModel {
    public static final String TAG = VocModel.class.getSimpleName();

    private Database mDatabase;

    public VocModel(VLApplication vlApplication) {
        mDatabase = vlApplication.getDatabase();
    }

    public String getBookTitle(int bookIndex) {
        return mDatabase.getBookTitle(bookIndex);
    }

    public ArrayList<Book> getBooks() {
        return mDatabase.getBooks();
    }

    public ArrayList<Lesson> getLessons(int bookIndex) {
        if (mDatabase == null)
            return null;
        return mDatabase.getLessonsByBook(bookIndex);
    }

    public ArrayList<Integer> getContent(int bookIndex, int lessonIndex) {
        if (mDatabase == null)
            return null;
        return mDatabase.getContentIDs(bookIndex, lessonIndex);
    }
}
