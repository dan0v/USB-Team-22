package uk.ac.newcastle.team22.usb.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreApp.AbstractCardData;
import uk.ac.newcastle.team22.usb.coreApp.AbstractViewHolder;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;
import uk.ac.newcastle.team22.usb.navigation.CardBuilder;
import uk.ac.newcastle.team22.usb.navigation.Direction;
import uk.ac.newcastle.team22.usb.navigation.DirectionCardData;
import uk.ac.newcastle.team22.usb.navigation.Edge;
import uk.ac.newcastle.team22.usb.navigation.Navigator;
import uk.ac.newcastle.team22.usb.navigation.Node;
import uk.ac.newcastle.team22.usb.navigation.TourCardData;

/**
 * A class which presents navigation between two locations in the Urban Sciences Building.
 *
 * @author Alexander MacLeod
 * @author Daniel Vincent
 * @version 1.0
 */
public class NavigationActivity extends USBActivity {

    /** The recycler view containing the directions of the navigation. */
    private RecyclerView recyclerView;

    /** The list of navigation directions. */
    private List<AbstractCardData> navigationCardList;

    /** The start node. */
    private Node start;

    /** The destination node. */
    private Node destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Set the title of the activity.
        setTitle(R.string.directions);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configure recycler view.
        recyclerView = findViewById(R.id.navigation_recycler_view);

        navigationCardList = new ArrayList<>();
        NavigationAdapter adapter = new NavigationAdapter(navigationCardList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        updateUI();
    }

    /** Update the UI with the newly calculated navigation directions. */
    private void updateUI() {
        Intent intent = getIntent();

        // Get static UI elements.
        TextView startLocation = findViewById(R.id.navigation_start_text);
        TextView destinationLocation = findViewById(R.id.navigation_destination_text);

        // Access navigation node identifiers from intent.
        int startNodeIdentifier = intent.getIntExtra("startNodeIdentifier", -1);
        int destinationNodeIdentifier = intent.getIntExtra("destinationNodeIdentifier", -1);

        // Tour mode or navigation mode. Default to tour mode if something goes wrong with navigation.
        List<Edge> route;
        boolean isTour;
        if (startNodeIdentifier == -1 || destinationNodeIdentifier == -1) {
            // Tour mode.
            startLocation.setText("Urban Sciences Building Tour");
            startLocation.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            destinationLocation.setVisibility(View.GONE);

            // Clear navigation tags text - temporary visual fix.
            setTitle(R.string.tourTitle);
            TextView startTag = findViewById(R.id.navigation_start_tag);
            TextView destinationTag = findViewById(R.id.navigation_destination_tag);
            startTag.setVisibility(View.GONE);
            destinationTag.setVisibility(View.GONE);

            route = Navigator.shared.getTourRoute();
            isTour = true;
        } else {
            // Navigation mode.
            Map<Integer, Node> nodes = USBManager.shared.getBuilding().getNavigationNodes();

            // Access static info bar UI elements from intent.
            String startLocationName = intent.getStringExtra("startLocationName");
            String destinationLocationName = intent.getStringExtra("destinationLocationName");

            // Set static info bar UI elements from intent.
            startLocation.setText(startLocationName);
            destinationLocation.setText(destinationLocationName);

            start = nodes.get(startNodeIdentifier);
            destination = nodes.get(destinationNodeIdentifier);

            route = Navigator.shared.getRoute(start, destination, navigationRequiresLifts());
            isTour = false;
        }

        populateRecyclerView(route);

        // Display compass to align user to directions.
        int azimuthOffset = Direction.getFirstAngle(route);
        if (azimuthOffset != -1) {
            Intent compassIntent = new Intent(NavigationActivity.this, CompassActivity.class);
            compassIntent.putExtra("azimuthOffset", azimuthOffset);
            compassIntent.putExtra("isTour", isTour);
            startActivity(compassIntent);
        }
    }

    /**
     * Populate recycler view with card list.
     *
     * @param route the list of edges whose route to populate recycler view with.
     */
    private void populateRecyclerView(List<Edge> route) {
        navigationCardList.clear();
        navigationCardList.addAll(CardBuilder.buildCards(route, this));
        recyclerView.getAdapter().notifyDataSetChanged();
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

/**
 * A class which acts as the adapter for the navigation recycler view.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
class NavigationAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    /** The list of navigation cards. */
    private List<AbstractCardData> cardList;

    public NavigationAdapter(List<AbstractCardData> cardList) {
        this.cardList = cardList;
    }

    public AbstractViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        AbstractViewHolder holder;
        View itemView;
        Context context = viewGroup.getContext();

        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(context).inflate(R.layout.card_view_direction, viewGroup, false);
                holder = new DirectionViewHolder(itemView);
                break;
            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.card_view_tour, viewGroup, false);
                holder = new TourViewHolder(itemView);
                break;
        }
        return holder;
    }

    public void onBindViewHolder(AbstractViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0: {
                DirectionViewHolder updatingHolder = (DirectionViewHolder) viewHolder;
                DirectionCardData item = (DirectionCardData) cardList.get(position);
                updatingHolder.directionTextView.setText(item.getDirectionText());
                updatingHolder.distanceTextView.setText(item.getDistanceText());
                updatingHolder.directionImageView.setImageResource(item.getDirectionImage());
                break;
            }
            default: {
                TourViewHolder updatingHolder = (TourViewHolder) viewHolder;
                TourCardData item = (TourCardData) cardList.get(position);
                updatingHolder.nameTextView.setText(item.getNameText());
                updatingHolder.descriptionTextView.setText(item.getDescriptionText());
                updatingHolder.imageView.setImageResource(item.getImage());
                break;
            }
        }
    }

    public int getItemViewType(int position) {
        AbstractCardData card = cardList.get(position);
        if (card instanceof DirectionCardData) {
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
 * A class which manages presenting content for each tour card.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
class DirectionViewHolder extends AbstractViewHolder {

    /** The text view for the direction text. */
    TextView directionTextView;

    /** The text view for the distance text. */
    TextView distanceTextView;

    /** The image view for the direction icon. */
    ImageView directionImageView;

    DirectionViewHolder(View view) {
        super(view);
        directionTextView = view.findViewById(R.id.navigationDirectionText);
        distanceTextView = view.findViewById(R.id.navigationDistanceText);
        directionImageView = view.findViewById(R.id.navigationDirectionImage);
    }
}

/**
 * A class which manages presenting the information in each tour card.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
class TourViewHolder extends AbstractViewHolder {

    /** The text view for the tour location's name. */
    TextView nameTextView;

    /** The text view for the tour location's description. */
    TextView descriptionTextView;

    /** The image view for the tour location's image. */
    ImageView imageView;

    TourViewHolder(View view) {
        super(view);
        nameTextView = view.findViewById(R.id.tourLocationNameText);
        descriptionTextView = view.findViewById(R.id.tourDescriptionText);
        imageView = view.findViewById(R.id.tourLocationImage);
    }
}