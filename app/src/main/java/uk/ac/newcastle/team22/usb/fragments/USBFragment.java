package uk.ac.newcastle.team22.usb.fragments;

import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.activities.NavigationDrawerActivity;

/**
 * A class which represents a fragment available on the navigation drawer.
 * See {@link NavigationDrawerActivity} for more information.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class USBFragment extends Fragment {

    /**
     * @return The title to display on the title bar when the fragment is visible.
     */
    public @StringRes int getTitle() {
        return R.string.urbanSciencesBuildingShorthand;
    }
}
