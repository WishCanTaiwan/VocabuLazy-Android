package parsing;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.FileOutputStream;

import books.Book;
import voc.Vocabulary;

public class Parsing {
    /* The book ready to be parsed */
    private Book mBook;
    /* The output stream for parsing result */
    private FileOutputStream mFos;
    
    public Parsing(Book book, FileOutputStream fos) {
        mBook = book;
        mFos  = fos;
    }
    
    /**
     * The flow of parsing Vocabulary
     */
    public void startParsing() throws Exception{
        boolean endOfPage       = false;
        boolean parsingResult   = false;
        String strUnitPattern   = mBook.getUnitParsingPattern();
        String strVocPattern    = mBook.getVocParsingPattern();
        String strPhrasePattern = mBook.getPhraseParsingPattern();
        String strAbbrevPattern = mBook.getAbbreviationParsingPattern();
        String[] strPatterns    = {strVocPattern, strPhrasePattern, strAbbrevPattern};
        String singleLine;
        int vocabularyID        = mBook.getBookVocabularyStartID();
        int vocabularyIDOffset  = 0;
        Pattern patternUnit, patternVoc;
        Matcher matcherUnit, matcherVoc;
        
        Vocabulary outputVocabulary = null;
        
        System.out.println("start Parsing");
        if (mBook == null) {
            System.out.println("Book object null");
            return;
        }
        do {
            // Fetch line from book
            singleLine = mBook.readLine();
            if (singleLine == null) {
                break;
            }
            // Try to match unit pattern first
            patternUnit = Pattern.compile(strUnitPattern);
            matcherUnit = patternUnit.matcher(singleLine);
            if (matcherUnit.find()) {
                // if this is unit line
                System.out.println("========================");
                System.out.println(singleLine);
                System.out.println("========================");
            } else {
                // or this is probably the voc line
                parsingResult = false;
                for (int i = 0; i < strPatterns.length; i++) {
                    patternVoc = Pattern.compile(strPatterns[i]);
                    matcherVoc = patternVoc.matcher(singleLine);
                    if (matcherVoc.matches()) {
                        // erase idx if needed
                        singleLine = idxErasing(singleLine);
                        // find pattern to match voc
                        switch(i) {
                            case 0: // voc pattern match
                                outputVocabulary = vocParsing(singleLine);
                                break;
                            case 1: // phrase pattern match
                                outputVocabulary = phraseParsing(singleLine);
                                break;
                            case 2: // abbreivation pattern match
                                outputVocabulary = abbrevParsing(singleLine);
                                break;
                        }
                        parsingResult = true;
                        break;
                    }
                }
                // did not find any pattern to match voc
                if (parsingResult == false && singleLine.equals("") != true) {
                    System.out.println("Parsing failed " + singleLine);
                }
                if (parsingResult) {
                    
                    writeVocToFile(outputVocabulary, vocabularyID + vocabularyIDOffset++);
                }
            }
        } while(!endOfPage);
    }
    
    private String idxErasing(String vocStr) {
        vocStr = vocStr.replaceAll(mBook.getIdxParsingPattern(), "");
        return vocStr;
    }
    
    private Vocabulary vocParsing(String str) {
        String spell, KK, partOfSpeech, translation;
        Pattern p;
        Matcher m;
        
        spell = KK = partOfSpeech = translation = "";
        
        // 1. Spell part
        p = Pattern.compile(mBook.getSpellParsingPattern());
        m = p.matcher(str);

        if (m.find()) {
            spell = m.group(0);
            str = str.replace(spell, "");
        }
        
        // 2. KK part
        p = Pattern.compile(mBook.getKKParsingPattern());
        m = p.matcher(str);
        
        if (m.find()) {
            KK = m.group(0);
            str = str.replace(KK, "");
        }
        
        // 3. partOfSpeech part
        // replace garbage space first
        str = str.replaceAll(" +", "");
        // for the case that v./n.
        p = Pattern.compile(mBook.getPartOfSpeechParsingPattern());
        m = p.matcher(str);
        
        while(m.find()) {
            partOfSpeech = m.group(0);
            str = str.replace(m.group(0), "");
        }

        // 4. translation part
        translation = str;
        return new Vocabulary(spell, translation, partOfSpeech, 0, KK);
    }
    
    private Vocabulary phraseParsing(String str) {
        String spell, KK, translation;
        Pattern p;
        Matcher m;
        
        spell = KK = translation = "";
        
        // 1. spell part
        p = Pattern.compile(mBook.getSpellParsingPattern());
        m = p.matcher(str);
        if (m.find()) {
            spell = m.group(0);
            str = str.replace(spell, "");
        }
        
        // 2. KK part (phrase should not contais KK, but some database does.) Erase it.
        p = Pattern.compile(mBook.getKKParsingPattern());
        m = p.matcher(str);
        if (m.find()) {
            KK = m.group(0);
            str = str.replace(KK, "");
        }
        
        // 3. translation part
        translation = str;
        return new Vocabulary(spell, translation, "phr.", 0, "");
    }
    
    private Vocabulary abbrevParsing(String str) {
        System.out.println(str);
        return new Vocabulary("", "", "abb.", 0, "");
    }
    
    private void writeVocToFile(Vocabulary voc, int id) {
        String vocOutputStr = "";
        if (mFos == null) {
            System.out.println("FileOutputStream null");
            return;
        }
        if (voc == null) {
            System.out.println("Vocabulary Object null");
        }
        // The output format of .json
        vocOutputStr = vocOutputStr.concat("{\n")
                                   .concat("\t" + "\"spell\" : " + "\"" + voc.getSpell() + "\"" + "," + "\n")
                                   .concat("\t" + "\"cnSentence\" : " + "[\"未定義例句\"]" + "," + "\n")
                                   .concat("\t" + "\"phonetic\" : " + "\"" + voc.getKK() + "\"" + "," + "\n")
                                   .concat("\t" + "\"enSentence\" : " + "[\"default sentence\"]" + "," + "\n")
                                   .concat("\t" + "\"level\" : " + voc.getLevel() + "," + "\n")
                                   .concat("\t" + "\"partOfSpeech\" : " + "\"" + voc.getPartOfSpeech() + "\"" + "," + "\n")
                                   .concat("\t" + "\"translation\" : " + "\"" + voc.getTranslation() + "\"" + "," + "\n")
                                   .concat("\t" + "\"id\" : " + id + "\n")
                                   .concat("},\n");
        try {
            mFos.write(vocOutputStr.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}