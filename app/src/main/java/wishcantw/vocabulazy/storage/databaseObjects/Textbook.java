package wishcantw.vocabulazy.storage.databaseObjects;

import java.util.ArrayList;

/**
 * Created by allencheng07 on 2016/8/22.
 */
public class Textbook {

    public static final String TAG = "Textbook";

    private String textbookType;
    private int textbookId;
    private String textbookTitle;
    private ArrayList<Lesson> textbookContent;

    public Textbook(int textbookId, String textbookType, String textbookTitle, ArrayList<Lesson> textbookContent) {
        setTextbookId(textbookId);
        setTextbookType(textbookType);
        setTextbookTitle(textbookTitle);
        setTextbookContent(textbookContent);
    }

    public int getTextbookId() {
        return textbookId;
    }

    public void setTextbookId(int textbookId) {
        this.textbookId = textbookId;
    }

    public String getTextbookTitle() {
        return textbookTitle;
    }

    public void setTextbookTitle(String textbookTitle) {
        this.textbookTitle = textbookTitle;
    }

    public String getTextbookType() {
        return textbookType;
    }

    public void setTextbookType(String textbookType) {
        this.textbookType = textbookType;
    }

    public ArrayList<Lesson> getTextbookContent() {
        return textbookContent;
    }

    public void setTextbookContent(ArrayList<Lesson> textbookContent) {
        this.textbookContent = textbookContent;
    }
}
