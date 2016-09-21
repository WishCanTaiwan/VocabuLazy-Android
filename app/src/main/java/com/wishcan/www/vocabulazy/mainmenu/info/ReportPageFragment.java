package com.wishcan.www.vocabulazy.mainmenu.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.mainmenu.activity.MainMenuActivity;

/**
 * Created by allencheng07 on 2016/9/21.
 */

public class ReportPageFragment extends Fragment {

    public static final String TAG = "ReportPageFragment";

    private ReportPageView reportPageView;
    private EditText editText;
    private Button sendButton;
    private Button cancelButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        reportPageView = (ReportPageView) inflater.inflate(R.layout.view_report_page, container, false);
        findViews(reportPageView);
        return reportPageView;
    }

    private void findViews(View view) {

        // edit text
        editText = (EditText) view.findViewById(R.id.report_edit_text);

        // send button
        sendButton = (Button) view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "send");
                // send report
                ((MainMenuActivity) getActivity()).sendReport(editText.getText().toString());

                // leave fragment
                getActivity().onBackPressed();
            }
        });

        // cancel button
        cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "cancel");
                getActivity().onBackPressed();
            }
        });
    }

}
