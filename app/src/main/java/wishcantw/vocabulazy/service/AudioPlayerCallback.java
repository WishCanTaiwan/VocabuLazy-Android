package wishcantw.vocabulazy.service;

import java.util.ArrayList;

import wishcantw.vocabulazy.database.object.Vocabulary;

abstract public class AudioPlayerCallback {
    public void succeed(ArrayList<Vocabulary> playerContent) {}

    public void focusChanged(AudioPlayerUtils.PlayerState playerState) {}
}
