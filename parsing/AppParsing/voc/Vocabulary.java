package voc;

public class Vocabulary {
    private String spell;
    private String translation;
    private String partOfSpeech;
    private int level;
    private String KK;
    
    public Vocabulary (String spell, String translation, String partOfSpeech, int level, String KK) {
        this.spell = spell;
        this.translation = translation;
        this.partOfSpeech = partOfSpeech;
        this.level = level;
        this.KK = KK;
    }
    
    public String getSpell() {
        return this.spell;
    }
    
    public String getTranslation() {
        return this.translation;
    }
    
    public String getPartOfSpeech() {
        return this.partOfSpeech;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public String getKK() {
        return this.KK;
    }
}