package books;

import java.io.Reader;

public class BookToefl extends Book {
    private static final int BOOK_VOCABULARY_START_ID = 10000;
    
    public BookToefl(Reader in) {
        super(in);
    }
    
    public int getBookVocabularyStartID() {
        return BOOK_VOCABULARY_START_ID;
    }
    
    public String getUnitParsingPattern() {
        return "^ \\d+";
    }
    
    public String getIdxParsingPattern() {
        return "";
    }
    
    public String getVocParsingPattern() {
        return "";
    }
    
    public String getAbbreviationParsingPattern() {
        return "";
    }
    
    public String getPhraseParsingPattern() {
        return "";
    }
}