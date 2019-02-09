package uk.ac.newcastle.team22.usb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * A class which represents an Urban Sciences Building activity.
 * This configures a general layout to be adopted by descendant activities.
 * Layout attributes includes a {@link FloatingActionButton} for accessing the {@link SearchActivity}.
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
    private void configureView() {
        configureSearchFab();
    }

    /** Configures the search fab. */
    private void configureSearchFab() {
        FloatingActionButton fab = findViewById(R.id.search_fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(USBActivity.this, SearchActivity.class);
                    startActivity(i);
                }
            });
        }
    }
}
