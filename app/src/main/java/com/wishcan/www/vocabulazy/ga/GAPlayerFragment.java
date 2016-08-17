package com.wishcan.www.vocabulazy.ga;

import android.view.View;

import com.wishcan.www.vocabulazy.log.Logger;
import com.wishcan.www.vocabulazy.main.player.fragment.PlayerFragment;
import com.wishcan.www.vocabulazy.main.player.model.PlayerModel;
import com.wishcan.www.vocabulazy.main.player.view.PlayerMainView;
import com.wishcan.www.vocabulazy.main.player.view.PlayerOptionView;
import com.wishcan.www.vocabulazy.main.player.view.PlayerPanelView;
import com.wishcan.www.vocabulazy.main.player.view.PlayerView;
import com.wishcan.www.vocabulazy.storage.databaseObjects.OptionSettings;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by swallow on 2016/5/12.
 */
abstract public class GAPlayerFragment extends GAFragment implements PlayerView.PlayerEventListener,
                                                                     PlayerModel.PlayerModelDataProcessListener {

    public static final String USER_TOUCH_EVENT = "user-touch-event";

    /**--------------------------- implement GAFragment abstract function -----------------------**/
    @Override
    protected String getNameAsGaLabel() {
        return PlayerFragment.class.getSimpleName();
    }

    /**----------------- Implement PlayerModel.PlayerModelDataProcessListener -------------------**/
    /**
     * Implement from PlayerModel
     */
    @Override
    public void onPlayerContentCreated(final LinkedList<HashMap> playerDataContent) {
    }

    @Override
    public void onDetailPlayerContentCreated(HashMap<String, Object> playerDetailDataContent) {
    }

    @Override
    public void onVocabulariesGet(ArrayList<Vocabulary> vocabularies) {
    }

    /**----------------- Implement PlayerView.PlayerEventListener ------------------------**/
    @Override
    public void onPlayerVerticalScrollStop(int currentPosition, boolean isViewTouchedDown) {
        if (isViewTouchedDown)
            Logger.sendEvent(USER_TOUCH_EVENT, "vertical-scroll-stopped", getNameAsGaLabel(), 1);
    }

    @Override
    public void onPlayerVerticalScrolling() {
        Logger.sendEvent(USER_TOUCH_EVENT, "vertical-scrolling", getNameAsGaLabel(), 1);
    }

    @Override
    public void onPlayerHorizontalScrollStop(boolean isOrderChanged, int direction, boolean isViewTouchedDown) {
        if (isViewTouchedDown)
            Logger.sendEvent(USER_TOUCH_EVENT, "horizontal-scroll-stopped", getNameAsGaLabel(), 1);
    }

    @Override
    public void onPlayerHorizontalScrolling() {
        Logger.sendEvent(USER_TOUCH_EVENT, "horizontal-scrolling", getNameAsGaLabel(), 1);
    }

    @Override
    public void onPlayerDetailScrollStop(int index, boolean isViewTouchedDown) {
        if (isViewTouchedDown)
            Logger.sendEvent(USER_TOUCH_EVENT, "detail-scroll-stopped", getNameAsGaLabel(), 1);
    }

    @Override
    public void onPlayerDetailScrolling() {
        Logger.sendEvent(USER_TOUCH_EVENT, "detail-scrolling", getNameAsGaLabel(), 1);
    }
    
    @Override
    public void onPlayerInitialItemPrepared() {

    }

    @Override
    public void onPlayerFinalItemPrepared() {

    }
    
    @Override
    public void onPlayerPanelFavoriteClick() {
        Logger.sendEvent(USER_TOUCH_EVENT, "option-favorite-clicked", getNameAsGaLabel(), 1);
    }

    @Override
    public void onPlayerPanelPlayClick() {
        Logger.sendEvent(USER_TOUCH_EVENT, "option-play-clicked", getNameAsGaLabel(), 1);
    }

    @Override
    public void onPlayerPanelOptionClick() {
        Logger.sendEvent(USER_TOUCH_EVENT, "option-option-clicked", getNameAsGaLabel(), 1);
    }

    @Override
    public void onPlayerOptionChanged(int optionID, int mode, View v) {
        Logger.sendEvent(USER_TOUCH_EVENT, "option-changed", getNameAsGaLabel(), 1);
    }
    
    @Override
    public void onGrayBackClick() {
        Logger.sendEvent(USER_TOUCH_EVENT, "gray-back-clicked", getNameAsGaLabel(), 1);
    }
    
}
