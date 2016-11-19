package wishcantw.vocabulazy.mainmenu.model;

import android.content.Context;
import android.support.annotation.NonNull;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.database.DatabaseUtils;
import wishcantw.vocabulazy.mainmenu.note.adapter.NoteExpandableChildItem;
import wishcantw.vocabulazy.mainmenu.note.adapter.NoteExpandableGroupItem;
import wishcantw.vocabulazy.mainmenu.textbook.adapter.TextbookExpandableChildItem;
import wishcantw.vocabulazy.mainmenu.textbook.adapter.TextbookExpandableGroupItem;
import wishcantw.vocabulazy.database.Database;
import wishcantw.vocabulazy.database.object.Lesson;
import wishcantw.vocabulazy.database.object.Note;
import wishcantw.vocabulazy.database.object.Textbook;

import java.util.ArrayList;
import java.util.HashMap;

public class MainMenuModel {

    // tag for debugging
    public static final String TAG = "MainMenuModel";

    // singleton
    private static MainMenuModel mainMenuModel = new MainMenuModel();

    // private constructor
    private MainMenuModel() {}

    // singleton getter
    public static MainMenuModel getInstance() {
        return mainMenuModel;
    }

    // database instances
    private Database mDatabase;
    private DatabaseUtils mDatabaseUtils;

    // textbook items
    private ArrayList<TextbookExpandableGroupItem> textbookGroupItems = new ArrayList<>();
    private HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> textbookChildItemsMap = new HashMap<>();

    // note items
    private ArrayList<NoteExpandableGroupItem> noteGroupItems = new ArrayList<>();
    private HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> noteChildItemsMap = new HashMap<>();

    // exam items
    private ArrayList<TextbookExpandableGroupItem> examIndexTextbookGroupItems = new ArrayList<>();
    private HashMap<TextbookExpandableGroupItem, ArrayList<TextbookExpandableChildItem>> examIndexTextbookChildItemsMap = new HashMap<>();
    private ArrayList<NoteExpandableGroupItem> examIndexNoteGroupItems = new ArrayList<>();
    private HashMap<NoteExpandableGroupItem, ArrayList<NoteExpandableChildItem>> examIndexNoteChildItemsMap = new HashMap<>();

    public void init() {
        if (mDatabase == null) {
            mDatabase = Database.getInstance();
        }

        if (mDatabaseUtils == null) {
            mDatabaseUtils = DatabaseUtils.getInstance();
        }
    }

    public void generateBookItems() {
        ArrayList<Textbook> textbooks = mDatabase.getTextbooks();

        textbookGroupItems = new ArrayList<>();
        textbookChildItemsMap = new HashMap<>();
        for (int bookIndex = 0; bookIndex < textbooks.size(); bookIndex++) {
            TextbookExpandableGroupItem groupItem = new TextbookExpandableGroupItem(textbooks.get(bookIndex).getTextbookTitle());

            ArrayList<Lesson> lessons = textbooks.get(bookIndex).getTextbookContent();
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

    public void generateNoteItems(Context context) {
        ArrayList<Note> notes = mDatabase.getNotes();

        String[] childStrings = context.getResources().getStringArray(R.array.note_child);
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
        ArrayList<Textbook> textbooks = mDatabase.getTextbooks();
        ArrayList<Note> notes = mDatabase.getNotes();

        // generate textbook items for exam index
        examIndexTextbookGroupItems = new ArrayList<>();
        examIndexTextbookChildItemsMap = new HashMap<>();
        for (int bookIndex = 0; bookIndex < textbooks.size(); bookIndex++) {
            TextbookExpandableGroupItem groupItem = new TextbookExpandableGroupItem(textbooks.get(bookIndex).getTextbookTitle());

            ArrayList<Lesson> lessons = textbooks.get(bookIndex).getTextbookContent();
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
            for (String child : childs) {
                NoteExpandableChildItem childItem = new NoteExpandableChildItem(child);
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

    public String getNoteTitle(@NonNull Context context,
                               int noteIndex) {
        return mDatabaseUtils.getNoteTitle(context, mDatabase.getNotes(), noteIndex);
    }

    public void renameNote(int noteIndex,
                           @NonNull String name) {
        mDatabaseUtils.renameNoteAt(mDatabase.getNotes(), noteIndex, name);
    }

    public void deleteNote(int noteIndex) {
        mDatabaseUtils.deleteNoteAt(mDatabase.getNotes(), noteIndex);
    }

    public void createNote(String noteTitle) {
        mDatabaseUtils.createNewNote(mDatabase.getNotes(), noteTitle);
    }
}
