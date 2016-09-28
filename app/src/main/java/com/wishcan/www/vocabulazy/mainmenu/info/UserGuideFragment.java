package com.wishcan.www.vocabulazy.mainmenu.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.ga.GABaseFragment;
import com.wishcan.www.vocabulazy.ga.tags.GAScreenName;

/**
 * Created by allencheng07 on 2016/9/21.
 */

public class UserGuideFragment extends GABaseFragment {

    public static final String TAG = "UserGuideFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_guide, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {

    }

    @Override
    protected String getGALabel() {
        return GAScreenName.USER_GUIDE;
    }
}
