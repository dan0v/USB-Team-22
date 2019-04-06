package uk.ac.newcastle.team22.usb.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;
import uk.ac.newcastle.team22.usb.navigation.AbstractCardData;
import uk.ac.newcastle.team22.usb.navigation.Direction;
import uk.ac.newcastle.team22.usb.navigation.Edge;
import uk.ac.newcastle.team22.usb.navigation.NavigationAdapter;
import uk.ac.newcastle.team22.usb.navigation.Navigator;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        recyclerView = (RecyclerView) findViewById(R.id.navigation_recycler_view);

        navigationCardList = new ArrayList<>();
        adapter = new NavigationAdapter(navigationCardList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

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
     * @param edges from {@link Navigator} to display.
     */
    public void updateUI(List<Edge> edges) {
        navigationCardList.clear();
        navigationCardList.addAll(Direction.buildCards(edges, this));
        adapter.notifyDataSetChanged();
    }

    /**
     * @return Boolean value whether the user requires a lift when changing floors.
     */
    private boolean navigationRequiresLifts() {
        SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(this);
        return manager.getBoolean("navigationRequiresLiftSettingsKey", false);
    }
}