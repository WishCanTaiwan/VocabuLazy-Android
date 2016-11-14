package wishcantw.vocabulazy.audio;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;

public class VLTextToSpeech extends VLTextToSpeechListener {

    // callback interface
    interface OnUtteranceFinishListener {
        void onUtteranceFinished(String utterance);
    }

    public static final String TAG = VLTextToSpeech.class.getSimpleName();

    private static final String UTTERANCE_SILENCE = "-silence";

    private TextToSpeech mTextToSpeech;
    private OnUtteranceFinishListener mOnUtteranceFinishListener;

    private boolean isEngineInit = false;
    private String currentUtterance;

    // singleton
    private static VLTextToSpeech vlTextToSpeech = new VLTextToSpeech();

    // private constructor
    private VLTextToSpeech () {}

    // singleton getter
    public static VLTextToSpeech getInstance() {
        return vlTextToSpeech;
    }

    public void init(Context context) {
        if (mTextToSpeech == null) {
            mTextToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    switch (status) {
                        case TextToSpeech.SUCCESS:
                            isEngineInit = true ;
                            break;

                        default:
                            break;
                    }
                }
            }, "com.google.android.tts");
        }
    }

    @SuppressWarnings("deprecation")
    void setOnUtteranceFinishListener(OnUtteranceFinishListener listener) {
        mOnUtteranceFinishListener = listener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech.setOnUtteranceProgressListener(this);
        } else {
            mTextToSpeech.setOnUtteranceCompletedListener(this);
        }
    }

    void speak(String utterance, int rate) {
        currentUtterance = utterance;

        if (!isEngineInit) {
            currentUtterance = utterance;
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech.setSpeechRate(0.7f * rate);
            mTextToSpeech.speak(utterance, TextToSpeech.QUEUE_ADD, null, utterance);
        } else {
            mTextToSpeech.setSpeechRate(0.7f * rate);
            HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utterance);
            mTextToSpeech.speak(utterance, TextToSpeech.QUEUE_ADD, params);
        }
    }

    void speakSilence(long time) {
        if (!isEngineInit) {
            return;
        }

        String silenceUtterance = currentUtterance + UTTERANCE_SILENCE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech.playSilentUtterance(time, TextToSpeech.QUEUE_ADD, silenceUtterance);
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, silenceUtterance);
            mTextToSpeech.playSilence(time, TextToSpeech.QUEUE_ADD, params);
        }
    }

    void stop() {
        if (mTextToSpeech == null)
            return;

        mTextToSpeech.stop();
    }

    @SuppressWarnings("unused")
    void release() { // releasing TTS resources
        if (mTextToSpeech == null)
            return;

        if (mTextToSpeech.isSpeaking())
            return;

        mTextToSpeech.shutdown();
    }

    void setLanguage(Locale locale) {
        if (!isEngineInit)
            return;

        mTextToSpeech.setLanguage(locale);
    }

    @SuppressWarnings("unused")
    boolean isLanguageAvailable() {
        return (mTextToSpeech.isLanguageAvailable(Locale.ENGLISH) >= 0 && mTextToSpeech.isLanguageAvailable(Locale.TAIWAN) >= 0);
    }

    @Override
    protected void onUtteranceFinished(String utteranceId) {
        String utteranceAfterSilence = currentUtterance + UTTERANCE_SILENCE;
        if (!utteranceId.equals(utteranceAfterSilence))
            return;
        mOnUtteranceFinishListener.onUtteranceFinished(currentUtterance);
    }
}
