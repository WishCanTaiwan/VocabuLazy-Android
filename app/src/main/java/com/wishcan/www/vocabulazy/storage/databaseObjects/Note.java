package com.wishcan.www.vocabulazy.storage.databaseObjects;

import android.util.Log;

import java.util.ArrayList;

public class Note {

    public static final String TAG = "Note";

    private int noteId;
    private String noteTitle;
    private ArrayList<Integer> noteContent;

    public Note(int noteId, String noteTitle, ArrayList<Integer> noteContent) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        Log.d(TAG, "create note " + toString());
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public ArrayList<Integer> getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(ArrayList<Integer> noteContent) {
        this.noteContent = noteContent;
    }

    public String toString() {
        return "Note[id="+noteId+",title="+noteTitle+",content="+noteContent.toString()+"]";
    }
}
