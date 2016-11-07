This is the parsing-app for parsing the books and output a database.

a. How to compile and run the app?
    0. Install java jdk.
    1. Open terminal.
    2. $ cd parsing/AppParsing
    3. $ javac @sources.txt
    4. $ java MainEntry

b. How to add another book for parsing? (Using voc7000.txt as example)
    1. Put the voc7000.txt into AppParsing/assests/books/
    2. Create a BookVoc7000.java and make it extends Book.
    3. Implement the abstract function.
        i.   getBookVocabularyStartID() is used to indicate the start id of vocabulary in this book.
        ii.  getUnitParsingPattern() is used to indicate whether the line is Unit string (must matches the hole line)
        iii. getIdxParsingPattern() is used to indicate whether there’s index before the vocabulary string. If it does, entering the regular express. Otherwise, return “”.
        iv.  getVocParsingPattern() is used to indicate whether the line is a “word”(not phrase or abbreviation) (must matches the hole line)
        v.   getPhraseParsingPattern() is used to indicate whether the line is a “phrase”( not other case)(must matches the hole line)
        vi.  getAbbreviationParsingPattern() is used to indicate whether the line is a “abbreviation” (not other case)(must matches the hole line)
    4. Add the file into sources.txt
    5. $ javac @sources.txt
    6. $ java MainEntry