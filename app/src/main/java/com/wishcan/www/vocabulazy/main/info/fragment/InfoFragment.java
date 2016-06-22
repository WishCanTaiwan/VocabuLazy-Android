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

    public static final String TAG = InfoFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private InfoView mInfoView;
    private TypeFormView mTypeFormView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateView");
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
}
