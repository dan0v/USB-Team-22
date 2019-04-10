package uk.ac.newcastle.team22.usb.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItem;
import uk.ac.newcastle.team22.usb.coreUSB.Resource;
import uk.ac.newcastle.team22.usb.coreUSB.Room;
import uk.ac.newcastle.team22.usb.coreUSB.StaffMember;
import uk.ac.newcastle.team22.usb.search.Search;
import uk.ac.newcastle.team22.usb.search.SearchResult;
import uk.ac.newcastle.team22.usb.search.Searchable;

/**
 * A class which manages the unified search activity.
 *
 * @author Alexander MacLeod
 * @author Daniel Vincent
 * @version 1.0
 */
public class SearchActivity extends USBActivity {

    /** The list view which displays the search results. */
    private ListView listView;

    /** The adapter of the list view. */
    private SearchResultAdapter adapter;

    /** The destination node identifier for navigation */
    private int destinationNodeIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_options_menu, menu);
        configureSearchView(menu);
        return true;
    }

    @Override
    void configureView() {
        super.configureView();

        // Configure the search results list view.
        listView = findViewById(R.id.searchResultsListView);

        // Set the adapter of the list view.
        adapter = new SearchResultAdapter(this, R.layout.list_search_result, new ArrayList<SearchResult>());
        listView.setAdapter(adapter);

        // Hide activity's title and replace with custom drawn back button.
        setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configure the navigation origin selector.
        TextView navigationHint = findViewById(R.id.searchNavigationHint);
        destinationNodeIdentifier = getIntent().getIntExtra("destinationNodeIdentifier", -1);
        if (isSelectingNavigationOrigin()) {
            navigationHint.setVisibility(View.VISIBLE);
        } else {
            navigationHint.setVisibility(View.GONE);
        }

        // Set the on click listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                SearchResult selected = (SearchResult) adapter.getItemAtPosition(position);
                if (isSelectingNavigationOrigin()) {
                    Room room = (Room) selected.getResult();
                    Intent intent = new Intent(SearchActivity.this, NavigationActivity.class);
                    intent.putExtra("startNodeIdentifier", room.getNavigationNode().getNodeIdentifier());
                    intent.putExtra("destinationNodeIdentifier", destinationNodeIdentifier);
                    startActivity(intent);
                } else {
                    presentSearchResult(selected.getResult());
                }
            }
        });
    }

    /**
     * Determines which activity to present in order to display more information about a search result.
     *
     * @param result The search result.
     */
    private void presentSearchResult(Searchable result) {
        Intent intent = null;

        // Present the staff member's details.
        if (result instanceof StaffMember) {
            StaffMember staffMember = (StaffMember) result;
            intent = new Intent(this, StaffMemberActivity.class);
            intent.putExtra("identifier", staffMember.getIdentifier());
        }

        // Present the café menu item's category.
        if (result instanceof CafeMenuItem) {
            CafeMenuItem cafeMenuItem = (CafeMenuItem) result;
            intent = new Intent(this, CafeMenuItemActivity.class);
            intent.putExtra("categoryName", cafeMenuItem.getCategory().getName());
        }

        // Display the search result.
        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     * Configures the search view displayed by the activity.
     *
     * @param menu The activity's main menu.
     */
    private void configureSearchView(Menu menu) {
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.onActionViewExpanded();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // Hide the default search view icon.
        ImageView searchViewIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        ViewGroup linearLayoutSearchView =(ViewGroup) searchViewIcon.getParent();
        linearLayoutSearchView.removeView(searchViewIcon);

        // Configure listener for user queries.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Search search;
                if (isSelectingNavigationOrigin()) {
                    search = new Search(query, Room.class);
                } else {
                    search = new Search(query, null);
                }
                adapter.searchResults.clear();
                adapter.addAll(search.search());
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    /**
     * @return Boolean value whether the user is selecting the navigation's origin.
     */
    private boolean isSelectingNavigationOrigin() {
        return destinationNodeIdentifier > -1;
    }

    /**
     * A class which represents the array adapter for search results.
     *
     * @author Alexander MacLeod
     * @version 1.0
     */
    public class SearchResultAdapter extends ArrayAdapter<SearchResult> {

        /** The activity context. */
        Context context;

        /** The cell layout resource identifier. */
        int resource;

        /** The search results to display. */
        List<SearchResult> searchResults;

        public SearchResultAdapter(Context context, int resource, List<SearchResult> searchResults) {
            super(context, resource, searchResults);
            this.context = context;
            this.resource = resource;
            this.searchResults = searchResults;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(resource, null, false);

            TextView title = view.findViewById(R.id.searchResultTitleTextView);
            TextView detail = view.findViewById(R.id.searchResultDetailTextView);

            ImageView navigationIcon = view.findViewById(R.id.searchResultNavigationIcon);
            navigationIcon.setVisibility(View.INVISIBLE);

            Searchable searchResult = searchResults.get(position).getResult();

            // Display each search result with a custom title and description.
            if (searchResult instanceof CafeMenuItem) {
                CafeMenuItem cafeMenuItem = (CafeMenuItem) searchResult;
                String cafeMenuItemCategory = cafeMenuItem.getCategory().getName();
                String cafeMenuItemPrice = cafeMenuItem.getFormattedPrice();
                title.setText(cafeMenuItem.getName());
                detail.setText(cafeMenuItemCategory + ", " + cafeMenuItemPrice);
            } else if (searchResult instanceof StaffMember) {
                StaffMember staffMember = (StaffMember) searchResult;
                title.setText(staffMember.getFullTitle());
                detail.setText(R.string.reasonStaffMember);
            } else if (searchResult instanceof Room) {
                final Room room = (Room) searchResult;
                title.setText(room.getFormattedName(context));
                detail.setText(room.getFloor().getFormattedName(context));
                if (!isSelectingNavigationOrigin()) {
                    navigationIcon.setVisibility(View.VISIBLE);
                }
                // Set the action of the navigation button.
                navigationIcon.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
                        intent.putExtra("destinationNodeIdentifier", room.getNavigationNode().getNodeIdentifier());
                        startActivity(intent);
                    }
                });
            } else if (searchResult instanceof Resource) {
                Resource resource = (Resource) searchResult;
                title.setText(resource.toString());
                detail.setText(getString(R.string.room) + " " + resource.getRoom().getFormattedName(context));
            }

            return view;
        }
    }
}