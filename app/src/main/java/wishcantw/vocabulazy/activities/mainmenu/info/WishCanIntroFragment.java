package wishcantw.vocabulazy.activities.mainmenu.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.analytics.ga.GABaseFragment;

/**
 * Created by allencheng07 on 2016/9/21.
 */

public class WishCanIntroFragment extends GABaseFragment {

    public static final String TAG = "WishCanIntroFragment";

    private RecyclerView recyclerView;

    private int[] profilePictures = {
            R.drawable.profile_sojier,
            R.drawable.profile_carlos,
            R.drawable.profile_allen,
            R.drawable.profile_swallow,
            R.drawable.profile_goston,
            R.drawable.profile_jianhow,
            R.drawable.profile_tom,
            R.drawable.profile_daisy
    };

    private String[] introductions = {
            "Head - Sojier",
            "Co-founder - Carlos",
            "Android Developer - Allen",
            "Android Developer - Swallow",
            "iOS Developer - Goston",
            "iOS Developer - Jian How",
            "Designer - Tom",
            "Designer - Daisy"
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishcan_intro, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MemberInfoAdapter(profilePictures, introductions));
    }

    @Override
    protected String getGALabel() {
        return Analytics.ScreenName.WISHCAN_INTRO;
    }
}
