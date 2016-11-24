package wishcantw.vocabulazy.activities.mainmenu.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.analytics.ga.GABaseFragment;

/**
 * Created by allencheng07 on 2016/9/21.
 */

public class TNCFragment extends GABaseFragment {

    public static final String TAG = "TNCFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tnc, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {

    }

    @Override
    protected String getGALabel() {
        return Analytics.ScreenName.TNC;
    }
}
