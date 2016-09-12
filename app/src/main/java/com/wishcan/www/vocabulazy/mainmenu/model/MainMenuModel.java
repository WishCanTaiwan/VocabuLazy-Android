package com.wishcan.www.vocabulazy.mainmenu.model;

import android.content.Context;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.mainmenu.note.adapter.NoteExpandableChildItem;
import com.wishcan.www.vocabulazy.mainmenu.note.adapter.NoteExpandableGroupItem;
import com.wishcan.www.vocabulazy.mainmenu.textbook.adapter.TextbookExpandableChildItem;
import com.wishcan.www.vocabulazy.mainmenu.textbook.adapter.TextbookExpandableGroupItem;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Lesson;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Note;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Textbook;

import java.util.ArrayList;
import java.util.HashMap;

public class MainMenuModel {

    public static final String TAG = "MainMenuModel";

    private Context mContext;

    private ArrayList<TextbookExpandableGroupItem> textbookGroupItems;
    private HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> textbookChildItemsMap;

    private ArrayList<NoteExpandableGroupItem> noteGroupItems;
    private HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> noteChildItemsMap;

    private ArrayList<TextbookExpandableGroupItem> examIndexTextbookGroupItems;
    private HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> examIndexTextbookChildItemsMap;
    private ArrayList<NoteExpandableGroupItem> examIndexNoteGroupItems;
    private HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> examIndexNoteChildItemsMap;

    private ArrayList<Textbook> texbooks;
    private ArrayList<Note> notes;

    public MainMenuModel(Context context) {
        mContext = context;
        getDataFromDatabase();
    }

    public void generateBookItems() {
        textbookGroupItems = new ArrayList<>();
        textbookChildItemsMap = new HashMap<>();
        for (int bookIndex = 0; bookIndex < texbooks.size(); bookIndex++) {
            TextbookExpandableGroupItem groupItem = new TextbookExpandableGroupItem(texbooks.get(bookIndex).getTextbookTitle());

            ArrayList<Lesson> lessons = texbooks.get(bookIndex).getTextbookContent();
            ArrayList<TextbookExpandableChildItem> childItems = new ArrayList<>();
            for (int lessonIndex = 0; lessonIndex < lessons.size(); lessonIndex++) {
                TextbookExpandableChildItem childItem = new TextbookExpandableChildItem(lessons.get(lessonIndex).getLessonTitle());
                childItems.add(childItem);
            }

            textbookGroupItems.add(groupItem);
            textbookChildItemsMap.put(groupItem, childItems);
        }
    }

    public ArrayList<TextbookExpandableGroupItem> getTextbookGroupItems() {
        return textbookGroupItems;
    }

    public HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> getTextbookChildItemsMap() {
        return textbookChildItemsMap;
    }

    public void generateNoteItems() {
        String[] childStrings = mContext.getResources().getStringArray(R.array.note_child);
        noteGroupItems = new ArrayList<>();
        noteChildItemsMap = new HashMap<>();
        for (int noteIndex = 0; noteIndex < notes.size(); noteIndex++) {
            NoteExpandableGroupItem groupItem = new NoteExpandableGroupItem(notes.get(noteIndex).getNoteTitle());

            ArrayList<NoteExpandableChildItem> childItems = new ArrayList<>();
            for (int index = 0; index < 4; index++) {
                NoteExpandableChildItem childItem = new NoteExpandableChildItem(childStrings[index]);
                childItems.add(childItem);
            }

            noteGroupItems.add(groupItem);
            noteChildItemsMap.put(groupItem, childItems);
        }
    }

    public ArrayList<NoteExpandableGroupItem> getNoteGroupItems() {
        return noteGroupItems;
    }

    public HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> getNoteChildItemsMap() {
        return noteChildItemsMap;
    }

    public void generateExamIndexItems() {

        // generate textbook items for exam index
        examIndexTextbookGroupItems = new ArrayList<>();
        examIndexTextbookChildItemsMap = new HashMap<>();
        for (int bookIndex = 0; bookIndex < texbooks.size(); bookIndex++) {
            TextbookExpandableGroupItem groupItem = new TextbookExpandableGroupItem(texbooks.get(bookIndex).getTextbookTitle());

            ArrayList<Lesson> lessons = texbooks.get(bookIndex).getTextbookContent();
            ArrayList<TextbookExpandableChildItem> childItems = new ArrayList<>();
            for (int lessonIndex = 0; lessonIndex < lessons.size(); lessonIndex++) {
                TextbookExpandableChildItem childItem = new TextbookExpandableChildItem(lessons.get(lessonIndex).getLessonTitle());
                childItems.add(childItem);
            }

            examIndexTextbookGroupItems.add(groupItem);
            examIndexTextbookChildItemsMap.put(groupItem, childItems);
        }

        // generate note items for exam index
        String[] childs = new String[]{"Play", "Rename", "Copy", "Delete"};
        examIndexNoteGroupItems = new ArrayList<>();
        examIndexNoteChildItemsMap = new HashMap<>();
        for (int noteIndex = 0; noteIndex < notes.size(); noteIndex++) {
            NoteExpandableGroupItem groupItem = new NoteExpandableGroupItem(notes.get(noteIndex).getNoteTitle());

            ArrayList<NoteExpandableChildItem> childItems = new ArrayList<>();
            for (int index = 0; index < childs.length; index++) {
                NoteExpandableChildItem childItem = new NoteExpandableChildItem(childs[index]);
                childItems.add(childItem);
            }

            examIndexNoteGroupItems.add(groupItem);
            examIndexNoteChildItemsMap.put(groupItem, childItems);
        }
    }

    public ArrayList<TextbookExpandableGroupItem> getExamIndexTextbookGroupItems() {
        return examIndexTextbookGroupItems;
    }

    public HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> getExamIndexTextbookChildItemsMap() {
        return examIndexTextbookChildItemsMap;
    }

    public ArrayList<NoteExpandableGroupItem> getExamIndexNoteGroupItems() {
        return examIndexNoteGroupItems;
    }

    public HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> getExamIndexNoteChildItemsMap() {
        return examIndexNoteChildItemsMap;
    }

    public void getDataFromDatabase() {
        Database database = Database.getInstance();
        texbooks = database.getTextbooks();
        notes = database.getNotes();
    }
}
