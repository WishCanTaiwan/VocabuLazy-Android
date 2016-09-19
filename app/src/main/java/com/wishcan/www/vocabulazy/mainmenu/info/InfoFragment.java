package com.wishcan.www.vocabulazy.mainmenu.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by allencheng07 on 2016/9/15.
 */
public class InfoFragment extends Fragment {

    private InfoView mInfoView;

    public static InfoFragment getInstance() {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInfoView = (InfoView) inflater.inflate(R.layout.view_info, container, false);
        return mInfoView;
    }
}
