package uk.ac.newcastle.team22.usb.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import uk.ac.newcastle.team22.usb.fragments.CafeFragment;
import uk.ac.newcastle.team22.usb.fragments.DashboardFragment;
import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.fragments.USBFragment;

/**
 * A class which represents the main navigation drawer of the application.
 * The navigation drawer configures the transition between two {@link USBFragment} fragments.
 * See {@link USBFragment} for more information.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class NavigationDrawerActivity extends USBActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_navigation_drawer);
        super.onCreate(savedInstanceState);
        configureView();
    }

    /** Configures the view. */
    private void configureView() {
        // Initialise the toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialise navigation drawer button.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set the navigation listener.
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Display default fragment.
        USBFragment defaultFragment = new DashboardFragment();
        navigateTo(defaultFragment);
        navigationView.setCheckedItem(R.id.nav_dashboard);
    }

    @Override
    /**
     * Navigates to the {@link USBFragment} represented by the selected menu item.
     * @param item The selected menu item.
     */
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        USBFragment fragment = null;

        switch (id) {
            case R.id.nav_dashboard:
                fragment = new DashboardFragment();
                break;
            case R.id.nav_cafe:
                fragment = new CafeFragment();
                break;
            default:
                break;
        }

        // Check whether a new fragment has been selected.
        if (fragment == null) {
            return false;
        }

        // Navigate to selected fragment.
        navigateTo(fragment);

        // Close the drawer.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Navigates to a fragment in the navigation drawer.
     * @param fragment The destination fragment.
     */
    private void navigateTo(USBFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.navigation_content, fragment);
        fragmentTransaction.commit();

        // Set the text of the drawer title bar.
        setTitle(fragment.getTitle());
    }
}
