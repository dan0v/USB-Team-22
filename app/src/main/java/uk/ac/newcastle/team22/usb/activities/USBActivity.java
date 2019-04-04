package uk.ac.newcastle.team22.usb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class which manages an Urban Sciences Building activity.
 * This configures the general layout to be adopted by descendant activities.
 * Configuration includes inserting a {@link FloatingActionButton}
 * for accessing the {@link SearchActivity}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public abstract class USBActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureView();
    }

    /** Configures the view displayed by the activity. */
    void configureView() {
        configureSearchFab();
    }

    /**
     * Configures the {@link FloatingActionButton} which links the {@link SearchActivity}.
     * This floating button enables the user quick access to the application's unified search.
     */
    private void configureSearchFab() {
        FloatingActionButton fab = findViewById(R.id.search_fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(USBActivity.this, SearchActivity.class);
                    startActivity(i);

//                  Test navigation UI:
//                  Intent i = new Intent(USBActivity.this, NavigationActivity.class);
//                  startActivity(i);
                }
            });
        }
    }
}
