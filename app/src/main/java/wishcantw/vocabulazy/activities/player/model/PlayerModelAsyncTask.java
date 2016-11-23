package wishcantw.vocabulazy.activities.player.model;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import wishcantw.vocabulazy.database.Database;
import wishcantw.vocabulazy.database.DatabaseUtils;
import wishcantw.vocabulazy.database.object.Vocabulary;
import wishcantw.vocabulazy.activities.player.view.PlayerMainView;

/* package */ class PlayerModelAsyncTask extends AsyncTask<Object, Void, Object> {

    // data process listener
    private PlayerModelDataProcessListener dataProcessListener;

    // constructor
    @SuppressWarnings("synthetic-access")
    /* package */ PlayerModelAsyncTask(PlayerModelDataProcessListener listener) {
        dataProcessListener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object doInBackground(Object... params) {

        if (params[0] instanceof ArrayList) {
            ArrayList<Vocabulary> vocArrayList = (ArrayList<Vocabulary>) params[0];
            LinkedList<HashMap> playerDataContent = new LinkedList<>();
            for(Vocabulary voc : vocArrayList) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_CONTENT_FROM[0], voc.getSpell());
                String partOfSpeech = (voc.getPartOfSpeech().equals("")
                        ? ""
                        : "[" + voc.getPartOfSpeech() + "] ");
                hm.put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_CONTENT_FROM[1], partOfSpeech + voc.getTranslation());
                hm.put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_CONTENT_FROM[2], voc.getPhonetic());
                playerDataContent.add(hm);
            }
            return playerDataContent;
        }

        if (params[0] instanceof Vocabulary) {
            Vocabulary voc = (Vocabulary) params[0];
            HashMap<String, Object> playerDetailDataContent = new HashMap<>();

            playerDetailDataContent
                    .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[0],
                            voc.getSpell());
            playerDetailDataContent
                    .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[1],
                            voc.getTranslation());
            playerDetailDataContent
                    .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[2],
                            voc.getPhonetic());
            playerDetailDataContent
                    .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[3],
                            voc.getEnSentence());
            playerDetailDataContent
                    .put(PlayerMainView.PlayerScrollView.PLAYER_ITEM_DETAIL_CONTENT_FROM[4],
                            voc.getCnSentence());

            return playerDetailDataContent;
        }

        if (params[0] instanceof Integer) {
            int bookIndex = (Integer) params[0];
            int lessonIndex = (Integer) params[1];

            Database database = Database.getInstance();
            DatabaseUtils databaseUtils = DatabaseUtils.getInstance();

            ArrayList<Integer> contentIDs = (bookIndex == -1)
                    ? databaseUtils.getNoteContent(database.getNotes(), lessonIndex)
                    : databaseUtils.getLessonContent(database.getTextbooks(), bookIndex, lessonIndex);

            return databaseUtils.getVocabulariesByIDs(database.getVocabularies(), contentIDs);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);

        if (result instanceof LinkedList) {
            LinkedList<HashMap> playerDataContent = (LinkedList<HashMap>) result;
            dataProcessListener.onPlayerContentCreated(playerDataContent);
            return;
        }

        if (result instanceof HashMap) {
            HashMap<String, Object> playerDetailDataContent = (HashMap<String, Object>) result;
            dataProcessListener.onDetailPlayerContentCreated(playerDetailDataContent);
            return;
        }

        if (result instanceof ArrayList) {
            ArrayList<Vocabulary> vocabularies = (ArrayList<Vocabulary>) result;
            dataProcessListener.onVocabulariesGet(vocabularies);
        }
    }
}
