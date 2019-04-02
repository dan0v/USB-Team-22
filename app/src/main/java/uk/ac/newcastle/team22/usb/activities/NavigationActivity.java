package uk.ac.newcastle.team22.usb.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class which prepares navigation between two locations in the Urban Sciences Building.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class NavigationActivity extends USBActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
    }

    /**
     * @return Boolean value whether the user requires a lift when changing floors.
     */
    private boolean navigationRequiresLifts() {
        SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(this);
        return manager.getBoolean("navigationRequiresLiftSettingsKey", false);
    }
}
