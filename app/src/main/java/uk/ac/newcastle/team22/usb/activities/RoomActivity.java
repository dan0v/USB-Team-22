package uk.ac.newcastle.team22.usb.activities;

import android.os.Bundle;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class which manages the room activity.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class RoomActivity extends USBActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
    }
}
