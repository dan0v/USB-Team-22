
package uk.ac.newcastle.team22.usb.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.support.v7.app.*;
import android.os.Bundle;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;
import uk.ac.newcastle.team22.usb.coreUSB.USBUpdateManager;

/**
 * A class which manages the application's launch activity.
 * The launch activity handles the initial setup of the Urban Sciences Building.
 * This includes initialising the Urban Sciences Building from cache and checking for
 * available updates.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // Hide the title bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Initialise floors, rooms and other USB Building details.
        initialiseUSB();
    }

    /** Called when the Urban Sciences Building was initialised. */
    private void didInitialiseUSB() {
        Intent i = new Intent(LaunchActivity.this, NavigationDrawerActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /** Initialises the Urban Sciences Building. */
    private void initialiseUSB() {
        USBManager.shared.prepareBuilding(new USBUpdateManager.UpdateCompletionHandler() {
            @Override
            public void requiresUpdate(boolean force) {
                if (force) {
                    presentUSBUpdateRequiredAlert();
                } else {
                    presentUSBUpdateAvailableAlert();
                }
            }

            @Override
            public void loadedFromCache() {
                didInitialiseUSB();
            }
        });
    }

    /** Presents an alert stating that an update is required for the applcation function. */
    private void presentUSBUpdateRequiredAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.updateRequired);
        builder.setMessage(R.string.usbUpdateRequiredInstallMessage);

        builder.setPositiveButton(R.string.install, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startUSBBuildingUpdate();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /** Presents an alert stating that an update is available for USB. */
    private void presentUSBUpdateAvailableAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.updateAvailable);
        builder.setMessage(R.string.usbUpdateAvailableInstallMessage);

        builder.setPositiveButton(R.string.install, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startUSBBuildingUpdate();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                didInitialiseUSB();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /** Presents an alert stating that an update for USB was unable to be installed. */
    private void presentUSBUpdateErrorAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.updateUnableToInstall);
        builder.setMessage(R.string.usbUpdateUnableToInstallMessage);

        builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startUSBBuildingUpdate();
            }
        });

        // Check whether an update is required to use the app.
        // Hide cancel button if no Urban Sciences Building has been cached.
        if (USBManager.shared.getBuilding() != null) {
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    didInitialiseUSB();
                }
            });
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /** Requests an update of the Urban Sciences Building. */
    private void startUSBBuildingUpdate() {
        // Present loading dialog.
        final ProgressDialog nDialog;
        nDialog = new ProgressDialog(LaunchActivity.this);
        nDialog.setTitle("Updating Urban Sciences Building");
        nDialog.setMessage("Please wait");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(false);
        nDialog.show();

        // Request update of USB.
        USBManager.shared.updateBuilding(new USBUpdateManager.UpdateCompletionHandler() {
            @Override
            public void requiresUpdate(boolean force) {
                nDialog.dismiss();
                presentUSBUpdateErrorAlert();
            }
            @Override
            public void loadedFromCache() {
                nDialog.dismiss();
                didInitialiseUSB();
            }
        });
    }
}
