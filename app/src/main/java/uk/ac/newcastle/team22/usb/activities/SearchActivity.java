package uk.ac.newcastle.team22.usb.activities;

import android.content.Context;
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
 * @version 1.0
 */
public class SearchActivity extends USBActivity {

    /** The list view which displays the search results. */
    private ListView listView;

    /** The adapter of the list view. */
    private SearchResultAdapter adapter;

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

        // Hide activity's title and replace with custom drawn back button.
        setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configure the search results list view.
        listView = findViewById(R.id.search_results_list_view);

        // Set the adapter of the list view.
        adapter = new SearchResultAdapter(this, R.layout.search_result_list, new ArrayList<SearchResult>());
        listView.setAdapter(adapter);

        // Set the on click listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                Searchable selected = (Searchable) adapter.getItemAtPosition(position);
            }
        });
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
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.searchResults.clear();
                adapter.addAll(new Search(s).search());
                adapter.notifyDataSetChanged();
                return false;
            }
        });
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

            Searchable searchResult = searchResults.get(position).getResult();

            // Display each search result with a custom title and description.
            if (searchResult instanceof CafeMenuItem) {
                CafeMenuItem cafeMenuItem = (CafeMenuItem) searchResult;
                title.setText(cafeMenuItem.getName());
                detail.setText(R.string.reasonCafeMenuItem);
            } else if (searchResult instanceof StaffMember) {
                StaffMember staffMember = (StaffMember) searchResult;
                title.setText(staffMember.getFullTitle());
                detail.setText(R.string.reasonStaffMember);
            } else if (searchResult instanceof Room) {
                Room room = (Room) searchResult;
                title.setText(getString(R.string.room) + " " + room.getNumber());
                detail.setText(getString(R.string.floor) + " " + room.getFloor().getNumber());
            } else if (searchResult instanceof Resource) {
                Resource resource = (Resource) searchResult;
                title.setText(resource.toString());
                detail.setText(getString(R.string.room) + " " + resource.getRoom().getFormattedNumber());
            }

            return view;
        }
    }
}