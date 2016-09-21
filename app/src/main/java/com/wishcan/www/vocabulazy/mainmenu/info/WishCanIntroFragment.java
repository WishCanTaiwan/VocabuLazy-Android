package com.wishcan.www.vocabulazy.mainmenu.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by allencheng07 on 2016/9/21.
 */

public class WishCanIntroFragment extends Fragment {

    public static final String TAG = "WishCanIntroFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishcan_intro, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {

    }
}
