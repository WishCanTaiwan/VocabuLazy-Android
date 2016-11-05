package wishcantw.vocabulazy.database.object;

import java.util.ArrayList;

/**
 * Using this class to represent Lesson and Note objects which are read from the database.\n
 * Each Lesson object is consist of an lessonId, a lessonTitle and a set of lessonContent ids.
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
    private int lessonId;

    /**
     * Title of the lesson.
     */
    private String lessonTitle;

    /**
     * Content of the lesson, containing a list of {@link Vocabulary} ids.
     */
    private ArrayList<Integer> lessonContent = new ArrayList<>();

    /**
     * Call constructor to create a {@link Lesson} object.
     *
     * @param lessonId the lessonId of the lesson.
     * @param lessonTitle the lessonTitle of the lesson.
     * @param lessonContent the lessonContent of the lesson.
     */
    public Lesson(int lessonId, String lessonTitle, ArrayList<Integer> lessonContent) {
        setLessonId(lessonId);
        setLessonTitle(lessonTitle);
        setLessonContent(lessonContent);
    }

    /**
     * Call this method to get the lessonId of the lesson.
     *
     * @return lessonId of the lesson.
     */
    public int getLessonId() {
        return lessonId;
    }

    /**
     * Call this method to set lessonId to the lesson.
     *
     * @param lessonId the lessonId of lesson.
     */
    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    /**
     * Call this method to get the lessonTitle of the lesson.
     *
     * @return the lessonTitle of the lesson.
     */
    public String getLessonTitle() {
        return lessonTitle;
    }

    /**
     * Call this method to set lessonTitle to the lesson.
     *
     * @param lessonTitle the lessonTitle of the lesson.
     */
    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    /**
     * Call this method to get the {@link Vocabulary} ids contained in the lesson.
     *
     * @return the lessonContent of the lesson.
     */
    public ArrayList<Integer> getLessonContent() {
        return lessonContent;
    }

    /**
     * Call this method to set lessonContent to the lesson.
     *
     * @param lessonContent the lessonContent of the lesson.
     */
    public void setLessonContent(ArrayList<Integer> lessonContent) {
        this.lessonContent = lessonContent;
    }

    /**
     * Call this method to give the lesson a new lessonTitle.
     *
     * @param newTitle the new lessonTitle of the lesson.
     */
    public void rename(String newTitle) {
        lessonTitle = newTitle;
    }

}
