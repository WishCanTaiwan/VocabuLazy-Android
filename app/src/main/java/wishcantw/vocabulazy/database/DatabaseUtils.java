package wishcantw.vocabulazy.database;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.database.object.Note;
import wishcantw.vocabulazy.database.object.Textbook;
import wishcantw.vocabulazy.database.object.Vocabulary;

public class DatabaseUtils {

    // the singleton
    private DatabaseUtils databaseUtils = new DatabaseUtils();

    // make constructor private to prevent others from instantiate
    private DatabaseUtils() {}

    // getting the singleton
    public synchronized DatabaseUtils getInstance() {
        return databaseUtils;
    }

    /**
     * Get the type of the textbook
     *
     * @param context the context
     * @param textbooks the array list of the textbook
     * @param bookIndex the index of the textbook
     *
     * @return the type of the textbook
     */
    public String getTextbookType(@NonNull Context context,
                                  @NonNull ArrayList<Textbook> textbooks,
                                  int bookIndex) {
        return (textbooks.isEmpty()
                || bookIndex < 0
                || bookIndex >= textbooks.size())
                ? context.getString(R.string.textbook_group_title)
                : textbooks.get(bookIndex).getTextbookType();
    }

    /**
     * Get the title of the textbook.
     *
     * @param context the context
     * @param textbooks the textbooks
     * @param bookIndex the index of the textbook
     *
     * @return the title of the given textbook
     */
    public String getTextbookTitle(@NonNull Context context,
                                   @NonNull ArrayList<Textbook> textbooks,
                                   int bookIndex) {
        return (textbooks.isEmpty()
                || bookIndex < 0
                || bookIndex >= textbooks.size())
                    ? context.getString(R.string.textbook_group_subtitle)
                    : textbooks.get(bookIndex).getTextbookTitle();
    }



    /**
     * Get the title of the lesson
     *
     * @param context the context
     * @param textbooks the array list of textbooks
     * @param bookIndex the index of the textbook
     * @param lessonIndex the index of the lesson
     *
     * @return the title of the given lesson
     */
    public String getLessonTitle(@NonNull Context context,
                                 @NonNull ArrayList<Textbook> textbooks,
                                 int bookIndex,
                                 int lessonIndex) {
        return (textbooks.isEmpty()
                || bookIndex < 0
                || bookIndex >= textbooks.size()
                || textbooks.get(bookIndex).getTextbookContent().isEmpty()
                || lessonIndex < 0
                || lessonIndex >= textbooks.get(bookIndex).getTextbookContent().size())
                    ? context.getString(R.string.textbook_child_title)
                    : textbooks.get(bookIndex).getTextbookContent().get(lessonIndex).getLessonTitle();
    }

    /**
     * Get the title of the note
     *
     * @param context the context
     * @param notes the array list of notes
     * @param noteIndex the index of the note
     *
     * @return the title of the note
     */
    public String getNoteTitle(@NonNull Context context,
                               @NonNull ArrayList<Note> notes,
                               int noteIndex) {
        return (notes.isEmpty()
                || noteIndex < 0
                || noteIndex >= notes.size())
                    ? context.getString(R.string.note_group_default_text)
                    : notes.get(noteIndex).getNoteTitle();
    }

    /**
     * Get the number of lessons in a textbook
     *
     * @param textbooks the array list of textbooks
     * @param bookIndex the index of the textbook
     *
     * @return the number of the lessons of the textbook
     */
    public int getLessonAmount(@NonNull ArrayList<Textbook> textbooks,
                              int bookIndex) {
        return (textbooks.isEmpty()
                || bookIndex < 0
                || bookIndex >= textbooks.size())
                    ? 0
                    : textbooks.get(bookIndex).getTextbookContent().size();
    }

    /**
     * Get the number of notes
     *
     * @param notes the array list of the notes
     * @return the number of notes
     */
    public int getNoteAmount(@NonNull ArrayList<Note> notes) {
        return notes.size();
    }

    /**
     * Get the content of the lesson
     *
     * @param textbooks the array list of the textbooks
     * @param bookIndex the index of the textbook
     * @param lessonIndex the index of the lesson
     *
     * @return the content of the lesson
     */
    public ArrayList<Integer> getLessonContent(@NonNull ArrayList<Textbook> textbooks,
                                               int bookIndex,
                                               int lessonIndex) {
        return (textbooks.isEmpty()
                || bookIndex < 0
                || bookIndex >= textbooks.size()
                || textbooks.get(bookIndex).getTextbookContent().isEmpty()
                || lessonIndex < 0
                || lessonIndex >= textbooks.get(bookIndex).getTextbookContent().size())
                    ? new ArrayList<Integer>()
                    : textbooks.get(bookIndex).getTextbookContent().get(lessonIndex).getLessonContent();
    }

    /**
     * Get the content of the note
     *
     * @param notes the array list of notes
     * @param noteIndex the index of the note
     *
     * @return the content of the note
     */
    public ArrayList<Integer> getNoteContent(@NonNull ArrayList<Note> notes,
                                             int noteIndex) {
        return (notes.isEmpty()
                || noteIndex < 0
                || noteIndex >= notes.size())
                    ? new ArrayList<Integer>()
                    : notes.get(noteIndex).getNoteContent();
    }
    
    public ArrayList<String> getNoteNames() {
        ArrayList<String> noteNames = new ArrayList<>();
        if (mNotes != null) {
            for (Note note : mNotes) {
                noteNames.add(note.getNoteTitle());
            }
        }
        return noteNames;
    }

    public ArrayList<Vocabulary> getVocabulariesByIDs(@NonNull final ArrayList<Integer> vocIDs) {
        ArrayList<Vocabulary> vocabularies = new ArrayList<>();
        if (mVocabularies != null) {
            for (int index = 0; index < vocIDs.size(); index++) {
                for (int index2 = 0; index2 < mVocabularies.size(); index2++) {
                    Vocabulary vocabulary = mVocabularies.get(index2);
                    if (vocIDs.get(index).equals(vocabulary.getId())) {
                        vocabularies.add(vocabulary);
                    }
                }
            }
        }
        return vocabularies;
    }

    /* Search operation */
    public ArrayList<Vocabulary> readSuggestVocabularyBySpell(String queryString) {
        ArrayList<Vocabulary> resultVocabularies = new ArrayList<>();
        if (mVocabularies != null) {
            for (int index = 0; index < mVocabularies.size(); index++) {
                Vocabulary vocabulary = mVocabularies.get(index);
                String spell = vocabulary.getSpell();
                int queryStringLength = queryString.length();
                if (spell.length() < queryStringLength) {
                    continue;
                }
                if (spell.substring(0, queryStringLength).equals(queryString)) {
                    resultVocabularies.add(vocabulary);
                    if (resultVocabularies.size() > MAXIMUM_LIST_SIZE) {
                        break;
                    }
                }
            }
        }
        return resultVocabularies;
    }

    /* Note operations */
    public void createNewNote(String name) {
        if (mNotes == null) mNotes = new ArrayList<>();
        int index = mNotes.size();
        mNotes.add(index, new Note(index, name, new ArrayList<Integer>()));
    }

    /**
     * Add vocabulary to a note.
     *
     * @param vocId the id of the vocabulary that will be added.
     * @param noteIndex the index of the note
     */
    public void addVocToNote(int vocId, int noteIndex) {
        // check noteIndex validity
        if (noteIndex >= mNotes.size() || noteIndex == -1) {
            return;
        }

        // if the selected note doesn't have the vocabulary, then add the vocabulary to note
        ArrayList<Integer> content = mNotes.get(noteIndex).getNoteContent();
        if (!content.contains(vocId)) {
            content.add(vocId);
        }
    }

    public void renameNoteAt(int noteIndex, String name) {
        if (mNotes == null || noteIndex == -1
                || noteIndex >= mNotes.size() || mNotes.get(noteIndex) == null) {
            return;
        }
        int id = mNotes.get(noteIndex).getNoteId();
        renameNote(mNotes, id, name);
    }

    private void renameNote(@NonNull ArrayList<Note> notes, int noteId, String newName) {
        for (int index = 0; index < notes.size(); index++) {
            Note note = notes.get(index);
            if (noteId == note.getNoteId()) {
                note.setNoteTitle(newName);
                return;
            }
        }
    }

    public void deleteNoteAt(int noteIndex) {
        if (mNotes == null|| noteIndex == -1
                || noteIndex >= mNotes.size() || mNotes.get(noteIndex) == null) {
            return;
        }
        int id = mNotes.get(noteIndex).getNoteId();
        deleteNote(mNotes, id);
    }

    private void deleteNote(@NonNull ArrayList<Note> notes, int noteId) {
        Note noteToBeDelete = null;
        for (int index = 0; index < notes.size(); index++) {
            Note note = notes.get(index);
            if (noteId == note.getNoteId()) {
                noteToBeDelete = note;
            }
        }

        if (noteToBeDelete == null)
            return;

        notes.remove(noteToBeDelete);

        // refresh IDs
        for (int index = 0; index < notes.size(); index++) {
            Note note = notes.get(index);
            note.setNoteId(index);
        }
    }
}
