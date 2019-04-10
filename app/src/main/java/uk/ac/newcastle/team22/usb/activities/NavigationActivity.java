package uk.ac.newcastle.team22.usb.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreApp.AbstractCardData;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;
import uk.ac.newcastle.team22.usb.navigation.Direction;
import uk.ac.newcastle.team22.usb.navigation.NavigationAdapter;
import uk.ac.newcastle.team22.usb.navigation.Navigator;
import uk.ac.newcastle.team22.usb.navigation.Node;

/**
 * A class which presents navigation between two locations in the Urban Sciences Building.
 *
 * @author Alexander MacLeod
 * @author Daniel Vincent
 * @version 1.0
 */
public class NavigationActivity extends USBActivity {
    private RecyclerView recyclerView;
    private NavigationAdapter adapter;
    private List<AbstractCardData> navigationCardList;
    private Node start;
    private Node destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Add custom drawn back button.
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.navigation_recycler_view);

        navigationCardList = new ArrayList<>();
        adapter = new NavigationAdapter(navigationCardList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        updateUI();

//        Test navigation UI with set nodes:
//        buildTestList();
    }

    /**
     * RecyclerView + CardView test
     */
    private void buildTestList() {
        navigationCardList.clear();
        navigationCardList.addAll(Direction.buildCards(Navigator.shared.getRoute(USBManager.shared.getBuilding().getNavigationNodes().get(0), USBManager.shared.getBuilding().getNavigationNodes().get(400), false), this));

        adapter.notifyDataSetChanged();
    }

    /**
     * Update the UI with newly calculated navigation directions.
     */
    public void updateUI() {
        navigationCardList.clear();
        Intent intent = getIntent();
        Map<Integer, Node> nodes = USBManager.shared.getBuilding().getNavigationNodes();
        start = nodes.get(Integer.parseInt(intent.getExtras().get("startNodeIdentifier") + ""));
        destination = nodes.get(Integer.parseInt(intent.getExtras().get("destinationNodeIdentifier") + ""));
        navigationCardList.addAll(Direction.buildCards(Navigator.shared.getRoute(start, destination, navigationRequiresLifts()), this));
        adapter.notifyDataSetChanged();
    }

    /**
     * @return Boolean value whether the user requires a lift when changing floors.
     */
    private boolean navigationRequiresLifts() {
        SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(this);
        return manager.getBoolean("navigationRequiresLiftSettingsKey", false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}