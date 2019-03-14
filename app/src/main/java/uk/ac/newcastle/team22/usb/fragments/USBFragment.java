package uk.ac.newcastle.team22.usb.fragments;

import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.activities.NavigationDrawerActivity;

/**
 * An interface which declares a fragment available in Urban Sciences Building navigation drawer.
 * See {@link NavigationDrawerActivity} for more information.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public interface USBFragment {

    /**
     * @return The title to display on the title bar when the fragment is visible.
     */
    @StringRes int getTitle();
}
