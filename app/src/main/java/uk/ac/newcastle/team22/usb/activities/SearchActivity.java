package uk.ac.newcastle.team22.usb.activities;

import android.os.Bundle;
import android.util.Log;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;
import uk.ac.newcastle.team22.usb.navigation.Direction;
import uk.ac.newcastle.team22.usb.navigation.Navigator;

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

        Navigator n = new Navigator();
        try {
            // Should show a route, but doesn't
            Log.d("Navigation", "_\n" + n.getRoute(USBManager.shared.getBuilding().getNavigationNodes().get(0), USBManager.shared.getBuilding().getNavigationNodes().get(611), false).toString() + "\n\n" + Direction.parseDirections(n.getRoute(USBManager.shared.getBuilding().getNavigationNodes().get(0), USBManager.shared.getBuilding().getNavigationNodes().get(611), false)).toString());
            Log.d("Navigation", "_\n" + n.getTourRoute().toString());

            Direction.parseDirections(n.getTourRoute());
        }
        catch (Exception e){
            Log.e("Navigation", "could not access nodes", e);
        }
    }
}
