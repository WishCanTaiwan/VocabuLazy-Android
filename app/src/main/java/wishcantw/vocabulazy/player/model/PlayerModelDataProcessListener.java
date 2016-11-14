package wishcantw.vocabulazy.player.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import wishcantw.vocabulazy.database.object.Vocabulary;

public interface PlayerModelDataProcessListener {
    void onPlayerContentCreated(LinkedList<HashMap> playerDataContent);

    void onDetailPlayerContentCreated(HashMap<String, Object> playerDetailDataContent);

    void onVocabulariesGet(ArrayList<Vocabulary> vocabularies);
}
