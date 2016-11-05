import java.io.FileOutputStream;
import java.io.FileReader;

import java.lang.Exception;

import books.*;
import parsing.Parsing;

public class MainEntry {
    
    private static final String BOOK_DIR = "assests//books//";
    private static final String DB_DIR   = "assests//databases//";
    private static final String TOEIC    = "TOEIC";
    private static final String TOEFL    = "TOEFL_600";
    
    public static void main(String[] Args) {
        String inputFileStr  = BOOK_DIR + TOEIC + ".txt";
        String outputFilestr = DB_DIR   + TOEIC + "_output.json";
        
        try {
            /* Get Book Object first */
            Book book = new BookToeic(new FileReader(inputFileStr));
            /* Prepare the output object for putting parsing result */
            FileOutputStream fos = new FileOutputStream(outputFilestr);
            /* Create the Parsing object with prepared Book Obj. and FileOutputStream */
            Parsing mParsingEntry = new Parsing(book, fos);
            /* Start parsing */
            mParsingEntry.startParsing();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}