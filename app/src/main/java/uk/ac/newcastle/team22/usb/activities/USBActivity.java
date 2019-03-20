package uk.ac.newcastle.team22.usb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.search.Search;
import uk.ac.newcastle.team22.usb.search.SearchResult;

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

        testSearch();
    }

    /** Configures the view displayed by the activity. */
    private void configureView() {
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
                }
            });
        }
    }

    // TODO
    // Remove once finished testing

    private void testSearch() {
        Log.i("", "Testing search");

        Search search = new Search("1");
        List<SearchResult> results = search.search();

        Log.i("", "Finished search (results: " + results.size() + ")");
        Log.i("", "\n***\n");

        for (SearchResult result : results) {
            Log.i("", result.toString());
        }

        Log.i("", "\n***\n");
    }
}
