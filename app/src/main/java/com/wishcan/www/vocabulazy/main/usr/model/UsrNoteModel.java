package com.wishcan.www.vocabulazy.main.usr.model;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Lesson;

import java.util.ArrayList;

/**
 * Created by allencheng07 on 2016/6/16.
 */
public class UsrNoteModel {
    private Database mDatabase;
    public UsrNoteModel(VLApplication vlApplication) {
        mDatabase = vlApplication.getDatabase();
    }

    public ArrayList<Lesson> getNotes() {
        return mDatabase.getLessonsByBook(-1);
    }

    public int getNoteSize(int index) {
        return mDatabase.getNoteSize(index);
    }

    public void deleteNoteAt(int index) {
        mDatabase.deleteNoteAt(index);
    }

    public void renameNoteAt(int index, String name) {
        mDatabase.renameNoteAt(index, name);
    }

    public void createNewNote(String name) {
        mDatabase.createNewNote(name);
    }
}
