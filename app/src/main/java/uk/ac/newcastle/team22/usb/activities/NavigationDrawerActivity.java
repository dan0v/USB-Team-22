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
/**
 * A class which manages the dashboard activity
 *
 * @author Alexander Beeching
 * @version 1.0
 */

class DashboardActivity extends USBActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<AbstractCardData> dashboardCardList;
    private Map<String, Integer> displayMap = new TreeMap<String, Integer>();
    private List<String> nameList;
    private List<Integer> computerList;
    private ConstraintLayout tourLayout = (ConstraintLayout) findViewById(R.id.constraintLayoutTour);
    private ConstraintLayout navigationLayout = (ConstraintLayout) findViewById(R.id.constraintLayoutNav);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);
        dashboardCardList = new ArrayList();
        USBManager.shared.updateComputerAvailability();
        initData();

        navigationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        //Setting up RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.available_computers_view);
        adapter = new DashboardAdapter(dashboardCardList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        buildCards();
    }

//    public void tourButton(View view) {
//        Intent intent = new Intent(DashboardActivity.this, TourActivity.class);
//        startActivity(intent);
//    }

    /**
     * Take live data on computer availability from JSON and sort top 5 rooms with most
     * available computers
     */

    private void initData() {
        USB building = USBManager.shared.getBuilding();
        List<Floor> floorsList = new ArrayList<Floor>(building.getFloors().values());
        Map<String, Room> roomsMap = new HashMap<String, Room>();
        Map<Integer, Room> availableMap = new TreeMap<Integer, Room>(Collections.reverseOrder());
        for (Floor floor : floorsList) {
            roomsMap.putAll(floor.getRooms());
        }
        for (Room room : roomsMap.values()) {
            availableMap.put(room.getComputers().getAvailable(), room);
        }
        for (Map.Entry<Integer, Room> map : availableMap.entrySet()) {
            displayMap.put(map.getValue().getFormattedNumber(), map.getKey());
        }
        nameList = new ArrayList<String>(displayMap.keySet());
        computerList = new ArrayList<Integer>(displayMap.values());
    }

    private void buildCards() {
        dashboardCardList.clear();
        for(int i=0;i<5;i++) {
            DashboardCardData computerData = new DashboardCardData("test","test2");
            dashboardCardList.add(computerData);
        }
        adapter.notifyDataSetChanged();
    }

}

/**
 * A class which defines the detail view of the computers available on the dashboard.
 *
 * @author Alexander Beeching
 * @version 1.0
 */

class DashboardAdapter extends RecyclerView.Adapter<AbstractViewHolder> {
    private List<AbstractCardData> cardList;

    public DashboardAdapter(List<AbstractCardData> cardList) {
        this.cardList = cardList;
    }

    public AbstractViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view_available_computers,viewGroup,false);
        return new DashboardViewHolder(itemView);
    }

    public void onBindViewHolder(AbstractViewHolder viewHolder,int position) {
        int viewType = getItemViewType(position);
        DashboardViewHolder updatingHolder = (DashboardViewHolder) viewHolder;
        DashboardCardData item = (DashboardCardData) cardList.get(position);
        updatingHolder.roomNumber.setText(item.getRoomNumber());
        updatingHolder.computersAvailable.setText(item.getComputersAvailable());
    }

    public int getItemCount() {
        return cardList.size();
    }
}

/**
 * A class which defines the data to be displayed by a {@link DashboardViewHolder}.
 *
 * @author Alexander Beeching
 * @version 1.0
 */

class DashboardCardData extends AbstractCardData {
    private String roomNumber;
    private String computersAvailable;

    public DashboardCardData(String roomNumber, String computersAvailable) {
        this.roomNumber = roomNumber;
        this.computersAvailable = computersAvailable;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String computersAvailable) {
        this.roomNumber = roomNumber;
    }

    public String getComputersAvailable() {
        return computersAvailable;
    }

    public void setComputersAvailable(String distanceText) {
        this.computersAvailable = computersAvailable;
    }
}

/**
 * A class to represent the information stored in a computers available card in the UI.
 *
 * @author Alexander Beeching
 * @version 1.0
 */

class DashboardViewHolder extends AbstractViewHolder {
    public TextView roomNumber;
    public TextView computersAvailable;

    public DashboardViewHolder(View view) {
        super(view);
        roomNumber = view.findViewById(R.id.roomNumberText);
        computersAvailable = view.findViewById(R.id.computersAvailableText);
    }
}