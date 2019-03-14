package uk.ac.newcastle.team22.usb.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItem;
import uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItemCategory;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;
import uk.ac.newcastle.team22.usb.fragments.CafeFragment;

public class CafeMenuCategoryActivity extends USBActivity {

    /** The list view which displays the café menu items. */
    private ListView listView;

    /** The adapter of the list view. */
    private CafeMenuCategoryActivity.CafeMenuItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_menu_category);

        listView = findViewById(R.id.listView);

        String categoryName = getIntent().getStringExtra("categoryName");
        CafeMenuItemCategory category = new CafeMenuItemCategory(categoryName);

        // Load and sort café menu items alphabetically.
        List<CafeMenuItem> fullMenuItems = USBManager.shared.getBuilding().getCafe().getItems();
        List<CafeMenuItem> menuItems = CafeMenuItemCategory.getItemsInCategory(category, fullMenuItems);

        Collections.sort(menuItems, new Comparator<CafeMenuItem>() {
            @Override
            public int compare(CafeMenuItem o1, CafeMenuItem o2) {
                return o1.compareAlphabeticallyTo(o2);
            }
        });

        // Initialise the adapter.
        adapter = new CafeMenuItemAdapter(this, R.layout.cafe_menu_item_list, menuItems);
        listView.setAdapter(adapter);

        // Set the on click listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                CafeMenuItem value = (CafeMenuItem) adapter.getItemAtPosition(position);
                Log.i("", value.toString());
            }
        });
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

        /** The café menu items to display/ */
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

            TextView textViewName = view.findViewById(R.id.cafeMenuItemNameTextView);
            TextView textViewTeam = view.findViewById(R.id.cafeMenuItemPriceTextView);

            final CafeMenuItem menuItem = menuItems.get(position);
            textViewName.setText(menuItem.getName());
            textViewTeam.setText(menuItem.getFormattedPrice());

            return view;
        }
    }
}
