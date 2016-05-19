package com.wishcan.www.vocabulazy.ga;

import android.view.View;

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
    }

    @Override
    public void onPlayerVerticalScrolling() {
    }

    @Override
    public void onPlayerHorizontalScrollStop(boolean isOrderChanged, int direction, boolean isViewTouchedDown) {
    }

    @Override
    public void onPlayerHorizontalScrolling() {
    }

    @Override
    public void onDetailScrollStop(int index, boolean isViewTouchedDown) {
    }

    @Override
    public void onDetailScrolling() {
    }

    @Override
    public void onViewTouchDown() {

    }

    /**----------------- Implement PlayerPanelView.OnPanelItemClickListener ---------------------**/
    @Override
    public void onOptionFavoriteClick() {
    }

    @Override
    public void onOptionPlayClick() {
    }

    @Override
    public void onOptionOptionClick() {
    }

    /**----------------- Implement PlayerOptionView.OnOptionChangedListener ---------------------**/
    @Override
    public void onOptionChanged(View v, ArrayList<OptionSettings> optionSettingsLL, int currentMode) {
    }

    /**-------------------- Implement PlayerView.OnGrayBackClickListener ------------------------**/
    @Override
    public void onGrayBackClick() {

    }
}
