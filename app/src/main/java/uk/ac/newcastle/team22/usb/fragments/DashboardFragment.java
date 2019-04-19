package uk.ac.newcastle.team22.usb.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.activities.NavigationActivity;
import uk.ac.newcastle.team22.usb.activities.SearchActivity;
import uk.ac.newcastle.team22.usb.coreApp.AbstractCardData;
import uk.ac.newcastle.team22.usb.coreApp.AbstractViewHolder;

/**
 * A class which represents the dashboard fragment of the Urban Sciences Building.
 * This fragment is the default fragment to be displayed when the application is launched.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class DashboardFragment extends Fragment implements USBFragment {

    /** The recycler view which displays the dashboard's content. */
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configure the recycler view.
        recyclerView = view.findViewById(R.id.dashboard_recycler_view);

        List<AbstractCardData> details = buildCards();
        DashboardAdapter adapter = new DashboardAdapter(details);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getTitle() {
        return R.string.urbanSciencesBuildingShorthand;
    }

    /**
     * @return The cards to display for the dashboard.
     */
    private List<AbstractCardData> buildCards() {
        List<AbstractCardData> cards = new ArrayList();

        // Add tour and navigation shortcuts.
        DashboardShortcutCardData tourShortcut = new DashboardShortcutCardData(DashboardShortcutCardData.Shortcut.TOUR);
        DashboardShortcutCardData navigationShortcut = new DashboardShortcutCardData(DashboardShortcutCardData.Shortcut.NAVIGATION);
        cards.add(tourShortcut);
        cards.add(navigationShortcut);

        // Add building statuses.
        DashboardStatusCardData buildingOpen = new DashboardStatusCardData(DashboardStatusCardData.Status.BUILDING, DashboardStatusCardData.IndicatorColor.GREEN);
        DashboardStatusCardData outOfHours = new DashboardStatusCardData(DashboardStatusCardData.Status.OUT_OF_HOURS, DashboardStatusCardData.IndicatorColor.GREEN);
        DashboardStatusCardData cafe = new DashboardStatusCardData(DashboardStatusCardData.Status.CAFE, DashboardStatusCardData.IndicatorColor.GREEN);
        cards.add(buildingOpen);
        cards.add(outOfHours);
        cards.add(cafe);

        return cards;
    }
}

/**
 * A class which defines the detail views of the dashboard.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class DashboardAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    /** The list of cards. */
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
                itemView = LayoutInflater.from(context).inflate(R.layout.card_view_dashboard_shortcut, viewGroup, false);
                holder = new DashboardShortcutViewHolder(itemView);
                break;
            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.card_view_dashboard_status, viewGroup, false);
                holder = new DashboardStatusViewHolder(itemView);
                break;
        }

        return holder;
    }

    public void onBindViewHolder(AbstractViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0: {
                final DashboardShortcutCardData item = (DashboardShortcutCardData) cardList.get(position);
                DashboardShortcutViewHolder updatingHolder = (DashboardShortcutViewHolder) viewHolder;

                updatingHolder.titleTextView.setText(item.getShortcut().getLocalisedTitle());
                updatingHolder.detailTextView.setText(item.getShortcut().getLocalisedDetail());
                updatingHolder.imageView.setImageResource(item.getShortcut().getIcon());

                updatingHolder.selectionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (item.getShortcut()) {
                            case TOUR:
                                Intent navIntent = new Intent(v.getContext(), NavigationActivity.class);
                                v.getContext().startActivity(navIntent);
                                break;
                            case NAVIGATION:
                                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                                intent.putExtra("isExplicitNavigation", true);
                                v.getContext().startActivity(intent);
                                break;
                            default:
                                break;
                        }
                    }
                });
                break;
            }
            case 1: {
                final DashboardStatusCardData item = (DashboardStatusCardData) cardList.get(position);
                DashboardStatusViewHolder updatingHolder = (DashboardStatusViewHolder) viewHolder;
                updatingHolder.titleTextView.setText(item.getStatus().getLocalisedTitle());

                // Set the indicator color.
                GradientDrawable indicator = new GradientDrawable();
                int indicatorColor = ContextCompat.getColor(updatingHolder.indicatorView.getContext(), item.getIndicatorColor().getColor());
                indicator.setColor(indicatorColor);
                indicator.setShape(GradientDrawable.OVAL);
                updatingHolder.indicatorView.setBackground(indicator);
            }
            default:
                break;
        }
    }

    public int getItemViewType(int position) {
        AbstractCardData card = cardList.get(position);
        if (card instanceof DashboardShortcutCardData) {
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
 * A class which defines the data to be displayed by a {@link DashboardShortcutViewHolder}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class DashboardShortcutCardData extends AbstractCardData {

    /** The shortcut being displayed. */
    private Shortcut shortcut;

    /** A dashboard shortcut. */
    enum Shortcut {
        TOUR, NAVIGATION;

        /**
         * @return The localised title of the shortcut.
         */
        @StringRes
        int getLocalisedTitle() {
            switch (this) {
                case TOUR:
                    return R.string.titleTour;
                case NAVIGATION:
                    return R.string.titleNav;
                default:
                    return 0;
            }
        }

        /**
         * @return The localised detail of the shortcut.
         */
        @StringRes
        int getLocalisedDetail() {
            switch (this) {
                case TOUR:
                    return R.string.descTour;
                case NAVIGATION:
                    return R.string.descNav;
                default:
                    return 0;
            }
        }

        /**
         * @return The localised detail.
         */
        int getIcon() {
            switch (this) {
                case TOUR:
                    return R.drawable.tour;
                case NAVIGATION:
                    return R.drawable.navigation;
                default:
                    return 0;
            }
        }
    }

    DashboardShortcutCardData(Shortcut shortcut) {
        this.shortcut = shortcut;
    }

    /**
     * @return The shortcut being displayed.
     */
    public Shortcut getShortcut() {
        return shortcut;
    }
}

/**
 * A class which defines the view to be display data from a {@link DashboardShortcutCardData}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class DashboardShortcutViewHolder extends AbstractViewHolder {

    /** The text view which displays the shortcut title. */
    TextView titleTextView;

    /** The text view which displays the shortcut detail. */
    TextView detailTextView;

    /** The image view which displays an icon for the shortcut. */
    ImageView imageView;

    /** The selection view which displays the shortcut's content. */
    View selectionView;

    DashboardShortcutViewHolder(View view) {
        super(view);
        titleTextView = view.findViewById(R.id.dashboardShortcutTitleTextView);
        detailTextView = view.findViewById(R.id.dashboardShortcutDetailTextView);
        imageView = view.findViewById(R.id.dashboardShortcutImageView);
        selectionView = view.findViewById(R.id.dashboardShortcutSelectionView);
    }
}

/**
 * A class which defines the data to be displayed by a {@link DashboardStatusViewHolder}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class DashboardStatusCardData extends AbstractCardData {

    /** The status being displayed. */
    private Status status;

    /** The indicator color of the building feature. */
    private IndicatorColor indicatorColor;

    /** A Urban Sciences Building status. */
    enum Status {
        BUILDING, OUT_OF_HOURS, CAFE;

        /**
         * @return The localised title of the shortcut.
         */
        @StringRes
        int getLocalisedTitle() {
            switch (this) {
                case BUILDING:
                    return R.string.building;
                case OUT_OF_HOURS:
                    return R.string.outOfHours;
                case CAFE:
                    return R.string.cafe;
                default:
                    return 0;
            }
        }
    }

    /** The indicator color of the building feature. */
    enum IndicatorColor {
        GREEN, RED;

        /**
         * @return The color of the indicator.
         */
        @ColorRes
        int getColor() {
            switch (this) {
                case GREEN:
                    return R.color.colorOpen;
                case RED:
                    return R.color.colorClosed;
                default:
                    return 0;
            }
        }
    }

    DashboardStatusCardData(Status status, IndicatorColor color) {
        this.status = status;
        this.indicatorColor = color;
    }

    /**
     * @return The status being displayed.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return The indicator color of the building feature.
     */
    public IndicatorColor getIndicatorColor() {
        return indicatorColor;
    }
}

/**
 * A class which defines the view to be display data from a {@link DashboardStatusCardData}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class DashboardStatusViewHolder extends AbstractViewHolder {

    /** The text view which displays the status title. */
    TextView titleTextView;

    /** The indicator view representing the status of a feature. */
    View indicatorView;

    DashboardStatusViewHolder(View view) {
        super(view);
        titleTextView = view.findViewById(R.id.dashboardStatusTitleTextView);
        indicatorView = view.findViewById(R.id.dashboardStatusIndicatorView);
    }
}

/* OLD ALEX BEECHING CODE FOR REFERENCE

//Chnage cafe widget circle on dashboard from red to green based on opening hours

        View cafeAvailable = findViewById(R.id.available_cafe_icon);
        OpeningHours openingHours = USBManager.shared.getBuilding().getCafe().getOpeningHours();
        if (openingHours.isOpen()) {
            cafeAvailable.setBackgroundColor(getResources().getColor(R.color.colorOpen, null));
        } else {
            cafeAvailable.setBackgroundColor(getResources().getColor(R.color.colorClosed, null));
        }

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
 */