package com.wishcan.www.vocabulazy.ga;

import android.view.View;

import com.wishcan.www.vocabulazy.log.Logger;
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
abstract public class GAPlayerFragment extends GAFragment implements PlayerModel.PlayerModelDataProcessListener,
                                                                     PlayerMainView.OnPlayerItemPreparedListener,
                                                                     PlayerMainView.OnPlayerScrollListener,
                                                                     PlayerPanelView.OnPanelItemClickListener,
                                                                     PlayerOptionView.OnOptionChangedListener,
                                                                     PlayerView.OnGrayBackClickListener {

    public static final String USER_TOUCH_EVENT = "user-touch-event";

    /**--------------------------- implement GAFragment abstract function -----------------------**/
    @Override
    String getNameAsGaLabel() {
        return GAPlayerFragment.class.getSimpleName();
    }

    /**----------------- Implement PlayerModel.PlayerModelDataProcessListener -------------------**/
    /**
     * Implement from PlayerModel
     * */
    @Override
    public void onPlayerContentCreated(final LinkedList<HashMap> playerDataContent) {
    }

    @Override
    public void onDetailPlayerContentCreated(HashMap<String, Object> playerDetailDataContent) {
    }

    @Override
    public void onVocabulariesGet(ArrayList<Vocabulary> vocabularies) {
    }

    /**------------------ Implement PlayerMainView.OnPlayerItemPreparedListener -----------------**/
    @Override
    public void onInitialItemPrepared() {

    }

    @Override
    public void onFinalItemPrepared() {

    }

    /**----------------- Implement PlayerMainView.OnPlayerScrollListener ------------------------**/
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
    public void onDetailScrollStop(int index, boolean isViewTouchedDown) {
        if (isViewTouchedDown)
            Logger.sendEvent(USER_TOUCH_EVENT, "detail-scroll-stopped", getNameAsGaLabel(), 1);
    }

    @Override
    public void onDetailScrolling() {
        Logger.sendEvent(USER_TOUCH_EVENT, "detail-scrolling", getNameAsGaLabel(), 1);
    }

    /**----------------- Implement PlayerPanelView.OnPanelItemClickListener ---------------------**/
    @Override
    public void onOptionFavoriteClick() {
        Logger.sendEvent(USER_TOUCH_EVENT, "option-favorite-clicked", getNameAsGaLabel(), 1);
    }

    @Override
    public void onOptionPlayClick() {
        Logger.sendEvent(USER_TOUCH_EVENT, "option-play-clicked", getNameAsGaLabel(), 1);
    }

    @Override
    public void onOptionOptionClick() {
        Logger.sendEvent(USER_TOUCH_EVENT, "option-option-clicked", getNameAsGaLabel(), 1);
    }

    /**----------------- Implement PlayerOptionView.OnOptionChangedListener ---------------------**/
    @Override
    public void onOptionChanged(View v, ArrayList<OptionSettings> optionSettingsLL, int currentMode) {
        Logger.sendEvent(USER_TOUCH_EVENT, "option-changed", getNameAsGaLabel(), 1);
    }

    /**-------------------- Implement PlayerView.OnGrayBackClickListener ------------------------**/
    @Override
    public void onGrayBackClick() {
        Logger.sendEvent(USER_TOUCH_EVENT, "gray-back-clicked", getNameAsGaLabel(), 1);
    }
}
