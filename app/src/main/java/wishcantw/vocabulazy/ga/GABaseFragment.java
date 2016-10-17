package wishcantw.vocabulazy.ga;

import android.support.v4.app.Fragment;

/**
 * GABaseFragment is an abstract class used for sending Lifecycle event to Google Analysis.
 * All fragments extends the GABaseFragment will automatically sending their Lifecycle event as long as
 * they implement {@link GABaseFragment#getGALabel()}
 */
public abstract class GABaseFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * The abstract function should be implement to specified which Fragment is being running their
     * life cycle.
     * @return The String return will be used as Label for Google Analysis used
     */
    protected abstract String getGALabel();
}
