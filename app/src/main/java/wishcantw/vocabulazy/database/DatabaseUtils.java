package wishcantw.vocabulazy.database;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.database.object.Note;
import wishcantw.vocabulazy.database.object.Textbook;
import wishcantw.vocabulazy.database.object.Vocabulary;

public class DatabaseUtils {

    // the maximum size of the search list
    private static final int MAXIMUM_LIST_SIZE = 50;

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

    /**
     * Get the names of the notes
     *
     * @param notes the array list notes
     *
     * @return the array list of note names
     */
    public ArrayList<String> getNoteNames(@NonNull ArrayList<Note> notes) {
        ArrayList<String> noteNames = new ArrayList<>();
        for (Note note : notes) {
            noteNames.add(note.getNoteTitle());
        }
        return noteNames;
    }

    /**
     * Get the array list of the vocabularies based on the given ids
     *
     * @param vocabularies the array list of vocabularies
     * @param ids the array list of the desired vocabulary ids
     *
     * @return the array list of desired vocabularies
     */
    public ArrayList<Vocabulary> getVocabulariesByIDs(@NonNull ArrayList<Vocabulary> vocabularies,
                                                      @NonNull final ArrayList<Integer> ids) {
        ArrayList<Vocabulary> matchedVocabularies = new ArrayList<>();
        for (Integer id : ids) {
            for (Vocabulary vocabulary : vocabularies) {
                if (vocabulary != null && id.equals(vocabulary.getId())) {
                    matchedVocabularies.add(vocabulary);
                }
            }
        }
        return matchedVocabularies;
    }

    /* Search operation */
    /**
     * Get the vocabularies based on the query
     *
     * @param vocabularies the array list of vocabularies
     * @param queryString the string of the query
     *
     * @return the array list of the matched vocabularies
     */
    public ArrayList<Vocabulary> readSuggestVocabularyBySpell(@NonNull ArrayList<Vocabulary> vocabularies,
                                                              @NonNull String queryString) {
        ArrayList<Vocabulary> resultVocabularies = new ArrayList<>();
        for (Vocabulary vocabulary : vocabularies) {
            if (vocabulary != null) {
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

    /**
     * Create a new note with the given name
     *
     * @param notes the array list of notes
     * @param name the name of new note
     */
    public void createNewNote(@NonNull ArrayList<Note> notes,
                              @NonNull String name) {
        int index = notes.size();
        notes.add(index, new Note(index, name, new ArrayList<Integer>()));
    }

    /**
     * Add vocabulary to a note.
     *
     * @param id the id of the vocabulary that will be added.
     * @param noteIndex the index of the note
     */
    public void addVocToNote(@NonNull ArrayList<Note> notes,
                             int id,
                             int noteIndex) {
        // check noteIndex validity
        if (noteIndex == -1 || noteIndex >= notes.size() || notes.get(noteIndex) == null) {
            return;
        }

        // if the selected note doesn't have the vocabulary, then add the vocabulary to note
        ArrayList<Integer> content = notes.get(noteIndex).getNoteContent();
        if (content != null && !content.contains(id)) {
            content.add(id);
        }
    }

    /**
     * Rename the note at the given index with new name
     *
     * @param notes the array list of notes
     * @param noteIndex the index of note which will be renamed
     * @param name the new name of the note
     */
    public void renameNoteAt(@NonNull ArrayList<Note> notes,
                             int noteIndex,
                             @NonNull String name) {
        if (noteIndex == -1 || noteIndex >= notes.size() || notes.get(noteIndex) == null) {
            return;
        }

        int id = notes.get(noteIndex).getNoteId();

        for (Note note : notes) {
            if (note != null && id == note.getNoteId()) {
                note.setNoteTitle(name);
                return;
            }
        }
    }

    /**
     * Delete the note at given index
     *
     * @param notes the array list of notes
     * @param noteIndex the index of the note to be deleted
     */
    public void deleteNoteAt(@NonNull ArrayList<Note> notes,
                             int noteIndex) {
        if (noteIndex == -1 || noteIndex >= notes.size() || notes.get(noteIndex) == null) {
            return;
        }

        int id = notes.get(noteIndex).getNoteId();

        for (Note note : notes) {
            if (note != null && id == note.getNoteId()) {
                notes.remove(note);
            }
        }

        // refresh IDs
        for (int index = 0; index < notes.size(); index++) {
            Note note = notes.get(index);
            note.setNoteId(index);
        }
    }
}
