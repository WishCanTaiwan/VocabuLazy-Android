package wishcantw.vocabulazy.database.object;

import java.util.ArrayList;

/**
 * Using this class to represent Vocabulary objects which are read from the database.\n
 *
 * @author Allen Cheng Yu-Lun
 * @version 1.0
 * @since 1.0
 */
public class Vocabulary {

    public static final String TAG = Vocabulary.class.getSimpleName();

    private int id;
    private int level;
    private String spell;
    private String phonetic;
    private String partOfSpeech;
    private String translation;

    private ArrayList<String> enSentence = new ArrayList<>();
    private ArrayList<String> cnSentence = new ArrayList<>();

    public Vocabulary(int id, int level, String spell, String phonetic,
                      String partOfSpeech,
                      String translation,
                      ArrayList<String> enSentence,
                      ArrayList<String> cnSentence) {
        setId(id);
        setLevel(level);
        setSpell(spell);
        setPhonetic(phonetic);
        setPartOfSpeech(partOfSpeech);
        setTranslation(translation);
        setEnSentence(enSentence);
        setCnSentence(cnSentence);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public ArrayList<String> getEnSentence() {
        return enSentence;
    }

    public void setEnSentence(ArrayList<String> enSentence) {
        this.enSentence = enSentence;
    }

    public ArrayList<String> getCnSentence() {
        return cnSentence;
    }

    public void setCnSentence(ArrayList<String> cnSentence) {
        this.cnSentence = cnSentence;
    }

    public int getSentenceAmount() {
        return enSentence.size();
    }
}
