package com.wishcan.www.vocabulazy.ga;

import com.wishcan.www.vocabulazy.main.exam.view.ExamView;

/**
 * Created by swallow on 2016/5/13.
 */
public class GAExamFragment extends GAFragment implements ExamView.ExamButtonClickListener{
    @Override
    protected String getNameAsGaLabel() {
        return GAExamFragment.class.getSimpleName();
    }

    @Override
    public void onExamOptionClick(int clickedIndex) {
        // TODO : Add GA
    }

    @Override
    public void onExamNextClick() {
        // TODO : Add GA
    }

    @Override
    public void onPlayerClick(String str) {
        // TODO : Add GA
    }
}
