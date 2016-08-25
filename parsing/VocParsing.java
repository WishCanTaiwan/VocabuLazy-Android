import java.io.Reader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.FileReader;
import java.io.FileOutputStream;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.lang.Exception;

public class VocParsing {
    
    private static final String PAGE_NUM_PATTERN_STR = "^ \\d+";
    private static final String VOC_END_PATTERN_STR = ".*\\d+$";
    private static final String INPUT_FILE_NAME = "Voc.txt";
    private static final String OUTPUT_FILE_NAME = "Voc_aligned.txt";
    
    private int mVocCount = 0;
    
    private static FileOutputStream mFos;
    
    private class Book extends BufferedReader {
        Book(Reader in) {
            super(in);
        }
        
        Book(Reader in, int sz) {
            super(in, sz);
        }
    }
    
    private class Page extends BufferedReader {
        Page(Reader in) {
            super(in);
        }
        
        Page(Reader in, int sz) {
            super(in, sz);
        }
    }
    
    private class Voc {
        private String spell;
        private String partOfSpeech;
        private String level;
        
        Voc (String spell, String partOfSpeech, String level) {
            this.spell = spell;
            this.partOfSpeech = partOfSpeech;
            this.level = level;
        }
        
        public String getSpell() {
            return this.spell;
        }
        
        public String getPartOfSpeech() {
            return this.partOfSpeech;
        }
        
        public String getLevel() {
            return this.level;
        }
    }
    
    private static VocParsing mVocParsing;
    
    private Book getBook(String fileName) {
        Book br = null;
        try {
            br = new Book(new FileReader(fileName));
        }
        catch(Exception e) {
            
        }
        return br;
    }
    
    /**
     * Parsing the book 
     * @param book book is a BufferedReader, looks like below
     *              1 
     *              a/an art. 1
     *              abandon v. 4
     *              abbreviate v. 6
     *              abbreviation n. 6
     *              ...
     *              2
     * @return Page is another BufferedReader, looks like book, but with only one page and without page number
     * */
    private Page parsingBookToPage(Book book) {
        String pageStr = "";
        boolean startOfPage = false;
        boolean endOfPage = false;
        boolean endOfBook = false;
        try {
            do {
                /** Mark before read line */
                book.mark(0);
                String str = book.readLine();
                if (str == null) {
                    endOfBook = true;
                    break;
                }
                Pattern p = Pattern.compile(PAGE_NUM_PATTERN_STR);
                Matcher m = p.matcher(str);
                if (m.matches()) { // find PAGE_NUM_PATTERN
                    if (startOfPage) {
                        /** Reset to the where match PAGE_NUM_PATTERN */
                        /** The book will be parsed from here next time */
                        book.reset();
                        endOfPage = true;
                    }
                    else {
                        startOfPage = true;
                    }
                }
                else {
                    pageStr = pageStr.concat(str + "\n");
                }
            } while (!endOfPage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new Page(new StringReader(pageStr));
    }
    
    /**
     * Parsing the page, making page into one line (some voc in book may not one line, the funcion concat them)
     * @param page
     * @return Voc
     * */
    private void parsingPageToVoc(Page page) {
        boolean endOfPage = false;
        String vocStr = "";
        try {
            do {
                String str = page.readLine();
                if (str == null) {
                    endOfPage = true;
                    break;
                }
                Pattern p = Pattern.compile(VOC_END_PATTERN_STR);
                Matcher m = p.matcher(str);
                /** Concat str anyway */
                vocStr = vocStr.concat(str);
                /** If this line contains VOC_END_PATTERN, then create Voc Object. Else, keep concating*/
                if (m.matches()) { // find VOC_END_PATTERN
                    parsingVoc(vocStr);
                    vocStr = "";
                }
            } while(!endOfPage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Input should be only one line, and line will be splited by space. 
     * Judgement, the last one is level, and before that is partOfSpeech, and others are spell
     * @param str
     * */
    private void parsingVoc(String str) {
        Voc voc;
        String level = "";
        String partOfSpeech = "";
        String spell = "";
        String[] strings = str.split(" ");
        int length = strings.length;
        for (int i = 0; i < length; i++) {
            /** level */
            if (i == length - 1) {
                level = strings[i];
            }
            /** partOfSpeech */
            else if (i == length - 2) {
                partOfSpeech = strings[i];
            } else {
                spell = spell.concat(strings[i]);
            }
        }
        voc = new Voc(spell, partOfSpeech, level);
        /**
         * TODO: Refine the output to our desired format
         */
        //System.out.println(mVocCount++ + "\t" + spell + "\t" + partOfSpeech + "\t" + level);
        writeVocToFile(voc);
    }
    
    private void writeVocToFile(Voc voc) {
        String vocOutputStr = "";
        vocOutputStr = vocOutputStr.concat(voc.getSpell() + "\t" + voc.getPartOfSpeech() + "\t" + voc.getLevel() + "\n");
        try {
            mFos.write(vocOutputStr.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] Args) {
        System.out.println("Start parsing");
        VocParsing vocParsing = new VocParsing();
        Book book = vocParsing.getBook(INPUT_FILE_NAME);
        try {
            mFos = new FileOutputStream(OUTPUT_FILE_NAME);
            /** Total 76 page */
            for (int i = 0; i < 76; i++) {
                vocParsing.parsingPageToVoc(vocParsing.parsingBookToPage(book));
            }
            mFos.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
}