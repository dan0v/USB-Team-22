package uk.ac.newcastle.team22.usb;

import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

/**
 * A class which represents a fragment available on the navigation drawer.
 * See {@link NavigationDrawer} for more information.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class USBFragment extends Fragment {

    /**
     * @return The title to display on the activity's title bar when the fragment is visible.
     */
    public @StringRes int getTitle() {
        return R.string.urbanSciencesBuildingShorthand;
    }
}
