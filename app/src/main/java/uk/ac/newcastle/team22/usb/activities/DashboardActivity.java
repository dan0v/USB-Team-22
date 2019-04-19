package uk.ac.newcastle.team22.usb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreApp.AbstractCardData;
import uk.ac.newcastle.team22.usb.coreApp.AbstractViewHolder;
import uk.ac.newcastle.team22.usb.coreUSB.Floor;
import uk.ac.newcastle.team22.usb.coreUSB.OpeningHours;
import uk.ac.newcastle.team22.usb.coreUSB.Room;
import uk.ac.newcastle.team22.usb.coreUSB.USB;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * A class which manages the dashboard activity
 *
 * @author Alexander Beeching
 * @version 1.0
 */

public class DashboardActivity extends USBActivity {
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

        //On click of navigation button start the search activity

        navigationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        //Chnage cafe widget circle on dashboard from red to green based on opening hours

        View cafeAvailable = findViewById(R.id.available_cafe_icon);
        OpeningHours openingHours = USBManager.shared.getBuilding().getCafe().getOpeningHours();
        if (openingHours.isOpen()) {
            cafeAvailable.setBackgroundColor(getResources().getColor(R.color.colorOpen, null));
        } else {
            cafeAvailable.setBackgroundColor(getResources().getColor(R.color.colorClosed, null));
        }

        //Setting up RecyclerView

        recyclerView = (RecyclerView) findViewById(R.id.available_computers_view);
        adapter = new DashboardAdapter(dashboardCardList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        buildCards();
    }

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

    public AbstractViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
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
