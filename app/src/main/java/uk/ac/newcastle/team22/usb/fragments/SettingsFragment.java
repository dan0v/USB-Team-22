package uk.ac.newcastle.team22.usb.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class which represents the settings fragment of the Urban Sciences Building application.
 * This fragment enables the user to adjust application settings.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class SettingsFragment extends PreferenceFragmentCompat implements USBFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);
    }

    @Override
    public int getTitle() {
        return R.string.settings;
    }
}
