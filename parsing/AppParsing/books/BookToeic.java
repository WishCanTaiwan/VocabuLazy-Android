package books;

import java.io.Reader;

public class BookToeic extends Book {
    private static final int BOOK_VOCABULARY_START_ID = 20000;
    
    public BookToeic(Reader in) {
        super(in);
    }
    
    public int getBookVocabularyStartID() {
        return BOOK_VOCABULARY_START_ID;
    }
    
    public String getUnitParsingPattern() {
        return "^[\\p{IsHan}]+、\\p{IsHan}*";
    }
    
    public String getIdxParsingPattern() {
        return "^[0-9]+\\. *";
    }
    
    public String getVocParsingPattern() {
        return "^[0-9]+\\. *([a-zA-Z\\-/()=']+){1} *(\\[.+\\]){0,1} *[\\p{IsHan}；（）()\\.0-9、 ]*";
    }
    
    public String getAbbreviationParsingPattern() {
        return "^[0-9]+\\. *[a-zA-Z]+={1}[a-zA-Z= ]+[\\p{IsHan}；（）()\\.0-9、 ]*";
    }
    
    public String getPhraseParsingPattern() {
        return "^[0-9]+\\. *[a-zA-Z]+[ \\-]+[a-zA-Z\\-= ]* *(\\[.+\\]){0,1} *[\\p{IsHan}；（）()\\.0-9、 ]*";
    }
}