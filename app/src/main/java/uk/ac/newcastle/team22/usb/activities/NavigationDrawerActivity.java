package uk.ac.newcastle.team22.usb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.*;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreUSB.*;
import uk.ac.newcastle.team22.usb.fragments.CafeFragment;
import uk.ac.newcastle.team22.usb.fragments.DashboardFragment;
import uk.ac.newcastle.team22.usb.fragments.SettingsFragment;
import uk.ac.newcastle.team22.usb.fragments.TourFragment;
import uk.ac.newcastle.team22.usb.fragments.USBFragment;
import uk.ac.newcastle.team22.usb.coreApp.AbstractCardData;
import uk.ac.newcastle.team22.usb.coreApp.AbstractViewHolder;
import android.support.v7.widget.LinearLayoutManager;

import java.util.List;

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
        Log.i("", "VERSION " + USBManager.shared.getBuilding().getConfiguration().getVersion());
    }

    /**
     * Navigates to the {@link USBFragment} represented by the selected menu item.
     * @param item The selected menu item.
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        USBFragment fragment = new DashboardFragment();
        switch (id) {
            case R.id.nav_cafe:
                fragment = new CafeFragment();
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            case R.id.nav_tour:
                fragment = new TourFragment();
                break;
            default:
                break;
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
        fragmentTransaction.replace(R.id.navigation_content, (Fragment) fragment);
        fragmentTransaction.commit();

        // Set the text of the drawer title bar.
        setTitle(fragment.getTitle());
    }

    @Override
    void configureView() {
        super.configureView();

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
}
