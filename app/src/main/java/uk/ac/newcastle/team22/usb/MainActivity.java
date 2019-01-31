package uk.ac.newcastle.team22.usb;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;

import uk.ac.newcastle.team22.usb.coreUSB.Floor;
import uk.ac.newcastle.team22.usb.firebase.FirebaseManager;
import uk.ac.newcastle.team22.usb.firebase.FirestoreCompletionHandler;
import uk.ac.newcastle.team22.usb.firebase.FirestoreDatabaseCollection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadBuilding();
            }
        });
    }

    /** Loads the USB's floors. */
    private void loadBuilding() {
        FirebaseManager.shared.getDocuments(FirestoreDatabaseCollection.FLOORS, new FirestoreCompletionHandler<List<Floor>>() {
            @Override
            public void completed(List<Floor> floors) {
                for (Floor floor : floors) {
                    Log.i("", floor.toString());
                }
            }

            @Override
            public void failed(Throwable exception) {
                Log.e("", "Unable to retrieve USB floors", exception);
            }
        });
    }

}
