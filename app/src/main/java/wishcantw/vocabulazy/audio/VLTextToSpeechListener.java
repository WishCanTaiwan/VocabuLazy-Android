package wishcantw.vocabulazy.audio;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

@SuppressWarnings("deprecation")
abstract class VLTextToSpeechListener extends UtteranceProgressListener implements TextToSpeech.OnUtteranceCompletedListener {

    abstract void onUtteranceFinished(String utteranceId);

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        onUtteranceFinished(utteranceId);
    }

    @Override
    public void onStart(String utteranceId) {}

    @Override
    public void onDone(String utteranceId) {
        onUtteranceFinished(utteranceId);
    }

    @Override
    public void onError(String utteranceId) {}
}