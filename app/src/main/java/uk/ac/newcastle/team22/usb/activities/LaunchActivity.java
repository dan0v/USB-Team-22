
package uk.ac.newcastle.team22.usb.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

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

        // Initialise floors, rooms and other Urban Sciences Building details.
        initialiseUSB();
    }

    /** Called when the Urban Sciences Building was initialised. */
    private void didInitialiseUSB() {
        // Check for location permission.
        if (checkLocationPermission()) {
            presentDashboard();
        }
    }

    /** Presents the dashboard. */
    private void presentDashboard() {
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

    /** Presents an alert stating that an update is required for the application function. */
    private void presentUSBUpdateRequiredAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
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

    /** Presents an alert stating that an update is available for the Urban Sciences Building. */
    private void presentUSBUpdateAvailableAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setCancelable(false);
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

    /** Presents an alert stating that an update for the Urban Sciences Building was unable to be installed. */
    private void presentUSBUpdateErrorAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setCancelable(false);
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

    /** Requests an update to the Urban Sciences Building. */
    @SuppressWarnings("deprecation")
    private void startUSBBuildingUpdate() {
        final ProgressDialog dialog = new ProgressDialog(LaunchActivity.this, R.style.AlertDialogStyle);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.updateInProgress);
        dialog.setMessage(getString(R.string.pleaseWait));
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();

        // Request update of Urban Sciences Building.
        USBManager.shared.updateBuilding(new USBUpdateManager.UpdateCompletionHandler() {
            @Override
            public void requiresUpdate(boolean force) {
                dialog.dismiss();
                presentUSBUpdateErrorAlert();
            }
            @Override
            public void loadedFromCache() {
                dialog.dismiss();
                didInitialiseUSB();
            }
        });
    }

    /** The request code for the user's location. */
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    /** Checks whether the user has granted permission for the app to access the user's location. */
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.locationPermissionTitle)
                    .setMessage(R.string.locationPermissionDetail)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(LaunchActivity.this,
                                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    })
                    .create()
                    .show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                presentDashboard();
            }
            default:
                break;
        }
    }
}
