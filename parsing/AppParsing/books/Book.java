package books;

import java.io.Reader;
import java.io.BufferedReader;

public abstract class Book extends BufferedReader {
    Book(Reader in) {
        super(in);
    }
    
    Book(Reader in, int sz) {
        super(in, sz);
    }
    
    /**
     * The implementation should provide the start ID of vocabulary in the book.
     */
    public abstract int getBookVocabularyStartID();
    /**
     * The implementation should return the pattern that can be used as parsing
     * the unit. So that Parsing object can give out the unit string and find
     * corresponding Voc in the unit.
     */
    public abstract String getUnitParsingPattern();
    /**
     * The implementation should return the pattern that can be used as parsing
     * the index. So that Parsing object can give out the voc string and find
     * corresponding index in the unit.
     */
    public abstract String getIdxParsingPattern();
    /**
     * The implementation should return the pattern that can be used as parsing
     * the voc. So that Parsing object can give out the voc string and used it
     * to create Voc project
     */
    public abstract String getVocParsingPattern();
    /**
     * The implementation should return the pattern that can be used as parsing
     * the phrase. So that Parsing object can give out the phrase string and 
     * used it to create Voc project
     */
    public abstract String getPhraseParsingPattern();
    /**
     * The implementation should return the pattern that can be used as parsing
     * the abbreviation. So that Parsing object can give out the abbreivation string 
     * and used it to create Voc project
     */
    public abstract String getAbbreviationParsingPattern();
    
    public String getSpellParsingPattern() {
        return "([a-zA-Z\\-/()=' ]+ *)?";
    }
    
    public String getKKParsingPattern() {
        return "(\\[.*\\])";
    }
    
    public String getPartOfSpeechParsingPattern() {
        return "([a-zA-Z]+\\.)(/[a-zA-Z]+\\.)*";
    }
    
    public String getTranslationParsingPattern() {
        return "";
    }
}