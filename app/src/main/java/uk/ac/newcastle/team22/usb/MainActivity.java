package uk.ac.newcastle.team22.usb;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;

import uk.ac.newcastle.team22.usb.coreUSB.*;
import uk.ac.newcastle.team22.usb.firebase.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureView();

        // Initialise floors, rooms and other USB Building details.
        initialiseUSB();
    }

    /** Initialises the Urban Sciences Building. */
    private void initialiseUSB() {
        USBManager.shared.prepareBuilding(new USBUpdateManager.UpdateCompletionHandler() {
            @Override
            public void requiresUpdate(boolean force) {
                if (force) {
                    // TODO Explain to user USB needs to be updated (forced).
                    presentUSBUpdateAvailableAlert();
                } else {
                    // TODO Ask user whether they want USB to be updated (not forced).
                    startUSBBuildingUpdate();
                }
            }

            @Override
            public void loadedFromCache() {
                Log.i("", USBManager.shared.getBuilding().toString());
            }
        });
    }

    /** Presents an alert stating the an update is available for USB. */
    private void presentUSBUpdateAvailableAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Update Available");
        builder.setMessage("An update for the Urban Sciences Building is available. Do you want to install this update now?");

        builder.setPositiveButton("Install", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startUSBBuildingUpdate();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /** Requests an update of the Urban Sciences Building. */
    private void startUSBBuildingUpdate() {
        // Present loading dialog.
        final ProgressDialog nDialog;
        nDialog = new ProgressDialog(MainActivity.this);
        nDialog.setTitle("Updating Urban Sciences Building");
        nDialog.setMessage("Please wait");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(false);
        nDialog.show();

        // Request update of USB.
        Thread updateThread = new Thread() {
            @Override
            public void run() {
                USBManager.shared.updateBuilding(new USBUpdateManager.UpdateCompletionHandler() {
                    @Override
                    public void requiresUpdate(boolean force) {
                        nDialog.dismiss();
                        Log.i("","Error");
                        // TODO Explain to user that USB failed to update.
                    }

                    @Override
                    public void loadedFromCache() {
                        nDialog.dismiss();
                        Log.i("", USBManager.shared.getBuilding().toString());
                    }
                });
            }
        };
        updateThread.start();
    }

    /** Configures the view controller. */
    private void configureView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });
    }
}
