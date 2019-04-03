package uk.ac.newcastle.team22.usb.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.navigation.DirectionCardData;
import uk.ac.newcastle.team22.usb.navigation.NavigationAdapter;
import uk.ac.newcastle.team22.usb.navigation.TourCardData;

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
    private List<Object> navigationCardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        recyclerView = (RecyclerView) findViewById(R.id.navigation_recycler_view);

        navigationCardList = new ArrayList<>();
        adapter = new NavigationAdapter(navigationCardList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        buildTestList();
    }


    /**
     * RecyclerView + CardView test
     */
    private void buildTestList() {

        DirectionCardData a = new DirectionCardData("Forwards", 13, R.drawable.navigation_forward);
        navigationCardList.add(a);

        a = new DirectionCardData("Keycard required for door", 2, R.drawable.navigation_keycard);
        navigationCardList.add(a);

        a = new DirectionCardData("Turn Left", 11, R.drawable.navigation_left);
        navigationCardList.add(a);

        TourCardData b = new TourCardData("Urban Sciences Building", "This building was constructed in 1234 and is very cool.", R.drawable.usb_hero);
        navigationCardList.add(b);

        a = new DirectionCardData("Take the Lift Down", 3, R.drawable.navigation_lift_down);
        navigationCardList.add(a);

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