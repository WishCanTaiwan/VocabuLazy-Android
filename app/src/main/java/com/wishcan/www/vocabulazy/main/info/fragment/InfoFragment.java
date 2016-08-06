package com.wishcan.www.vocabulazy.main.info.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.ga.GAInfoFragment;
import com.wishcan.www.vocabulazy.log.Logger;
import com.wishcan.www.vocabulazy.main.info.view.InfoView;
import com.wishcan.www.vocabulazy.main.info.view.TypeFormView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends GAInfoFragment {

    public static final String TAG = "INFO";

    private InfoView mInfoView;
    private TypeFormView mTypeFormView;

    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTypeFormView = new TypeFormView(getActivity());
        return mTypeFormView;
//        mInfoView = new InfoView(getActivity());
//        /**
//         * The code is just used for test, should be moved to MVC after test.
//         * */
//        EditText editText = (EditText) mInfoView.findViewById(R.id.info_edit_text);
//        final TextView textView = (TextView) mInfoView.findViewById(R.id.info_auto_resize_text_view);
//        Button buttonSizeUp = (Button) mInfoView.findViewById(R.id.button_size_up);
//        Button buttonSizeDown = (Button) mInfoView.findViewById(R.id.button_size_down);
//
//        if(editText != null) {
//            editText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
////                    Logger.d("InfoFragment", "" + s.toString());
//                    if(textView != null) {
//                        textView.setText(s);
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//        }
//
//        if(buttonSizeUp != null) {
//            buttonSizeUp.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    float textSize = textView.getTextSize();
////                    Logger.d("InfoFragment", "textSize = " +textSize);
//                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize + 1.0f);
//                }
//            });
//        }
//
//        if(buttonSizeDown != null) {
//            buttonSizeDown.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    float textSize = textView.getTextSize();
////                    Logger.d("InfoFragment", "textSize = " +textSize);
//                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize - 1.0f);
//                }
//            });
//        }
//        return mInfoView;
    }

    @Override
    protected String getNameAsGaLabel() {
        return TAG;
    }
}
