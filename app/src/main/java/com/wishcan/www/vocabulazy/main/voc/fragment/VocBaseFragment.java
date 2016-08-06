package com.wishcan.www.vocabulazy.main.voc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.ga.GAVocFragment;
import com.wishcan.www.vocabulazy.main.voc.model.VocModel;

/**
 * Created by allencheng07 on 2016/6/16.
 */
public class VocBaseFragment extends GAVocFragment {
    protected VocModel mVocModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mVocModel == null) mVocModel = new VocModel((VLApplication) getActivity().getApplication());
    }
}
