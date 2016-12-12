package wishcantw.vocabulazy.activities.mainmenu.info.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.activities.mainmenu.info.view.InfoView;
import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.analytics.ga.GABaseFragment;
import wishcantw.vocabulazy.activities.mainmenu.activity.MainMenuActivity;

public class InfoFragment extends GABaseFragment {

    public static final String TAG = "InfoFragment";

    private InfoView mInfoView;
    private AlertDialog alertDialog;

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
        findViews(mInfoView);
        return mInfoView;
    }

    public void findViews(View view) {

        TextView rateUsTextView = (TextView) view.findViewById(R.id.rate_us_text_view);
        rateUsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = getResources().getString(R.string.info_goto_market_message);
                showAlert(message);
            }
        });

        TextView reportTextView = (TextView) view.findViewById(R.id.report_text_view);
        reportTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to report page
                ((MainMenuActivity) getActivity()).displayReportPage();
            }
        });

//        TextView userGuideTextView = (TextView) view.findViewById(R.id.guide_text_view);
//        userGuideTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // go to user guide
//                ((MainMenuActivity) getActivity()).displayUserGuide();
//            }
//        });

//        TextView tncTextView = (TextView) view.findViewById(R.id.terms_conditions_text_view);
//        tncTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // go to TNC (terms and conditions) page
//                ((MainMenuActivity) getActivity()).displayTNCPage();
//            }
//        });

        TextView introTextView = (TextView) view.findViewById(R.id.wishcan_introduction_text_view);
        introTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to introduction page
                ((MainMenuActivity) getActivity()).displayIntroPage();
            }
        });

    }

    private void showAlert(String message) {
        new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.InfoAlertDialog))
        .setTitle(message)
        .setPositiveButton(R.string.info_alert_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((MainMenuActivity) getActivity()).navigateToGooglePlay();
                dialogInterface.dismiss();
            }
        })
        .setNegativeButton(R.string.info_alert_dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })
        .create()
        .show();
    }

    public void close() {
        alertDialog.cancel();
    }

    @Override
    protected String getGALabel() {
        return Analytics.ScreenName.INFO;
    }
}
