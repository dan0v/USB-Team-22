package uk.ac.newcastle.team22.usb.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.activities.NavigationActivity;
import uk.ac.newcastle.team22.usb.activities.SearchActivity;
import uk.ac.newcastle.team22.usb.coreApp.AbstractCardData;
import uk.ac.newcastle.team22.usb.coreApp.AbstractViewHolder;
import uk.ac.newcastle.team22.usb.coreApp.AsyncResponse;
import uk.ac.newcastle.team22.usb.coreApp.JSONDataFetcher;
import uk.ac.newcastle.team22.usb.coreUSB.Floor;
import uk.ac.newcastle.team22.usb.coreUSB.OpeningHours;
import uk.ac.newcastle.team22.usb.coreUSB.Room;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * A class which represents the dashboard fragment of the Urban Sciences Building.
 * This fragment is the default fragment to be displayed when the application is launched.
 *
 * @author Alexander MacLeod
 * @author Daniel Vincent
 * @version 1.0
 */
public class DashboardFragment extends Fragment implements USBFragment {

    /** The recycler view which displays the dashboard's content. */
    private RecyclerView recyclerView;

    /** The list of cards to display in the recycler view. */
    private List<AbstractCardData> cardList = new ArrayList();

    /** The class to update computer availability data. */
    private JSONDataFetcher jsonDataFetcher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Basic UI setup.
        setupStaticElements(view);

        // Configure the recycler view.
        recyclerView = view.findViewById(R.id.dashboard_recycler_view);

        DashboardAdapter adapter = new DashboardAdapter(cardList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        // Download and update local data using the Urban Sciences Building computer availability JSON provided by NUIT.
        jsonDataFetcher = new JSONDataFetcher(getActivity(), new AsyncResponse() {
            @Override
            public void onComplete() {
                // If successful download of new data.
                populateRecyclerView(true);
            }

            @Override
            public void onBadNetwork() {
                 getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        populateRecyclerView(false);
                    }
                });
            }
        });

        jsonDataFetcher.execute();
    }

    @Override
    public int getTitle() {
        return R.string.urbanSciencesBuildingShorthand;
    }

    /**
     * Populate recycler view with card list.
     */
    public void populateRecyclerView(boolean success) {
        cardList.clear();
        if (!success) {
            BadNetworkCardData card = new BadNetworkCardData();
            cardList.add(card);
        }
        cardList.addAll(buildCards());
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    /**
     * Set up static UI elements such as buttons and the colours for building open hours.
     * @param view
     */
    private void setupStaticElements(View view) {
        OpeningHours cafeOpeningHours = USBManager.shared.getBuilding().getCafe().getOpeningHours();
        OpeningHours outOfHoursAccess = USBManager.shared.getBuilding().getOutOfHours();
        OpeningHours buildingHours = USBManager.shared.getBuilding().getOpeningHours();
        View cafe = view.findViewById(R.id.dashboardStatusIndicatorViewCafe);
        View buildingOpen = view.findViewById(R.id.dashboardStatusIndicatorViewBuildingOpen);
        View outOfHours = view.findViewById(R.id.dashboardStatusIndicatorViewOutOfHours);

        // Set the building statuses.
        if (buildingHours.isOpen()) {
            buildingOpen.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.colorOpen));
        } else {
            buildingOpen.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.colorClosed));
        }
        if (outOfHoursAccess.isOpen()) {
            outOfHours.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.colorOpen));
        } else {
            outOfHours.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.colorClosed));
        }
        if (cafeOpeningHours.isOpen()) {
            cafe.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.colorOpen));
        } else {
            cafe.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.colorClosed));
        }

        CardView tourButton = view.findViewById(R.id.tour_card_view);
        CardView navigationButton = view.findViewById(R.id.navigation_card_view);

        tourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NavigationActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        navigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                intent.putExtra("isExplicitNavigation", true);
                v.getContext().startActivity(intent);
            }
        });
    }

    /**
     * @return The cards to display for the dashboard.
     */
    private List<AbstractCardData> buildCards() {
        List<AbstractCardData> cards = new ArrayList();
        DashboardCardData card;

        List<Room> rooms = new ArrayList();
        for (Floor floor : USBManager.shared.getBuilding().getFloors().values()) {
            for (Room room : floor.getRooms().values()) {
                rooms.add(room);
            }
        }
        rooms.sort(new Comparator<Room>() {
            @Override
            public int compare(Room o1, Room o2) {
                return o2.getComputers().getAvailable() - o1.getComputers().getAvailable();
            }
        });
        for (Room room : rooms) {
            if (room.getComputers().getAvailable() > 0) {
                card = new DashboardCardData(room.getFormattedName(getContext()), room.getComputers().getAvailable() + "");
                cards.add(card);
            }
        }
        return cards;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Prevent memory leaks.
        jsonDataFetcher.setReference(null);
    }
}

/**
 * A class to define a room card on the dashboard.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
class DashboardAdapter extends RecyclerView.Adapter<AbstractViewHolder> {
    private List<AbstractCardData> cardList;

    public DashboardAdapter(List<AbstractCardData> cardList) {
        this.cardList = cardList;
    }

    public AbstractViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        AbstractViewHolder holder;
        View itemView;
        Context context = viewGroup.getContext();

        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(context).inflate(R.layout.card_view_available_computers, viewGroup, false);
                holder = new DashboardViewHolder(itemView);
                break;
            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.card_view_bad_network, viewGroup, false);
                holder = new BadNetworkViewHolder(itemView);
                break;
        }

        return holder;
    }

    public void onBindViewHolder(AbstractViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0: {
                DashboardViewHolder updatingHolder = (DashboardViewHolder) viewHolder;
                DashboardCardData item = (DashboardCardData) cardList.get(position);

                updatingHolder.roomNameText.setText(item.getRoomNameText());
                updatingHolder.computersAvailableText.setText(item.getComputersAvailableText());
            }
            default:
                break;
        }
    }

    public int getItemViewType(int position) {
        AbstractCardData card = cardList.get(position);
        if (card instanceof DashboardCardData) {
            return 0;
        } else {
            return 1;
        }
    }

    public int getItemCount() {
        return cardList.size();
    }
}

/**
 * A class to represent the information stored in a room card on the dashboard.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
class DashboardCardData extends AbstractCardData {

    private String roomNameText;
    private String computersAvailableText;

    public DashboardCardData(String roomNameText, String computersAvailableText) {
        this.roomNameText = roomNameText;
        this.computersAvailableText = computersAvailableText;
    }

    public String getRoomNameText() {
        return roomNameText;
    }

    public String getComputersAvailableText() {
        return computersAvailableText;
    }
}

/**
 * A class which manages presenting the information in each room card.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
class DashboardViewHolder extends AbstractViewHolder {
    public TextView roomNameText;
    public TextView computersAvailableText;

    public DashboardViewHolder(View view) {
        super(view);
        roomNameText = view.findViewById(R.id.roomNameText);
        computersAvailableText = view.findViewById(R.id.computersAvailableText);
    }
}

/**
 * A class to represent the a bad network error.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class BadNetworkCardData extends AbstractCardData {
    public BadNetworkCardData() {}
}

/**
 * A class to represent the a bad network error.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class BadNetworkViewHolder extends AbstractViewHolder {

    /** The detail text view. */
    public TextView detailTextView;

    public BadNetworkViewHolder(View view) {
        super(view);
        detailTextView = view.findViewById(R.id.bad_network_detail_text_view);
    }
}