package uk.ac.newcastle.team22.usb.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreUSB.Cafe;
import uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItem;
import uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItemCategory;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * A class which displays café menu items in a category.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class CafeMenuItemActivity extends USBActivity {

    /** The list view which displays the café menu items. */
    private ListView listView;

    /** The adapter of the list view. */
    private CafeMenuItemAdapter adapter;

    /** The selected café menu item category. */
    private CafeMenuItemCategory category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_menu_item);

        listView = findViewById(R.id.cafe_menu_item_list_view);

        // Determine the category to display.
        String categoryName = getIntent().getStringExtra("categoryName");
        int categoryIconIdentifier = getIntent().getIntExtra("categoryIconIdentifier", 1);
        category = new CafeMenuItemCategory(categoryName, categoryIconIdentifier);

        // Set the title of the activity.
        setTitle(categoryName);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Load and sort café menu items alphabetically by default.
        Cafe cafe = USBManager.shared.getBuilding().getCafe();
        List<CafeMenuItem> menuItems = cafe.getItems(category, this);
        Collections.sort(menuItems, new Comparator<CafeMenuItem>() {
            @Override
            public int compare(CafeMenuItem o1, CafeMenuItem o2) {
                return o1.compareAlphabeticallyTo(o2);
            }
        });

        // Initialise the adapter.
        adapter = new CafeMenuItemAdapter(this, R.layout.list_cafe_menu_item, menuItems);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cafe_menu_item_sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.cafe_menu_item_sort_alphabetically:
                sortCafeMenuItems(SortMode.ALPHABETICALLY);
                return true;
            case R.id.cafe_menu_item_sort_least_expensive:
                sortCafeMenuItems(SortMode.LEAST_EXPENSIVE);
                return true;
            case R.id.cafe_menu_item_sort_most_expensive:
                sortCafeMenuItems(SortMode.MOST_EXPENSIVE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Sorts the café menu items either by name or by price.
     *
     * @param sortMode The sort mode of the café menu items.
     */
    private void sortCafeMenuItems(final SortMode sortMode) {
        // Sort café menu items.
        Cafe cafe = USBManager.shared.getBuilding().getCafe();
        List<CafeMenuItem> menuItems = cafe.getItems(category, this);
        Collections.sort(menuItems, new Comparator<CafeMenuItem>() {
            @Override
            public int compare(CafeMenuItem o1, CafeMenuItem o2) {
                switch (sortMode) {
                    case LEAST_EXPENSIVE:
                        return o1.comparePriceTo(o2);
                    case MOST_EXPENSIVE:
                        return -o1.comparePriceTo(o2);
                    default:
                        return o1.compareAlphabeticallyTo(o2);
                }
            }
        });

        // Refresh list view.
        adapter.menuItems.clear();
        adapter.addAll(menuItems);
        adapter.notifyDataSetChanged();
    }

    /**
     * A class which represents the array adapter for café menu items.
     *
     * @author Alexander MacLeod
     * @version 1.0
     */
    public class CafeMenuItemAdapter extends ArrayAdapter<CafeMenuItem> {

        /** The activity context. */
        Context context;

        /** The cell layout resource identifier. */
        int resource;

        /** The café menu items to display. */
        List<CafeMenuItem> menuItems;

        public CafeMenuItemAdapter(Context context, int resource, List<CafeMenuItem> menuItems) {
            super(context, resource, menuItems);
            this.context = context;
            this.resource = resource;
            this.menuItems = menuItems;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(resource, null, false);

            TextView textViewName = view.findViewById(R.id.cafeMenuItemTitleTextView);
            TextView textViewTeam = view.findViewById(R.id.cafeMenuItemDetailTextView);

            final CafeMenuItem menuItem = menuItems.get(position);
            textViewName.setText(menuItem.getName());
            textViewTeam.setText(menuItem.getFormattedPrice());
            return view;
        }
    }

    /** The sort mode of the café menu items. */
    enum SortMode {
        ALPHABETICALLY, LEAST_EXPENSIVE, MOST_EXPENSIVE
    }
}
