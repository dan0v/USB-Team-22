package uk.ac.newcastle.team22.usb.activities;

import android.os.Bundle;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class which manages the unified search activity.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class SearchActivity extends USBActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
    }
}
