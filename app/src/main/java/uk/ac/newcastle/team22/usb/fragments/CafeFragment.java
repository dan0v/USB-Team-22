package uk.ac.newcastle.team22.usb.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.*;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.activities.CafeMenuCategoryActivity;
import uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItem;
import uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItemCategory;
import uk.ac.newcastle.team22.usb.coreUSB.USB;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * A class which represents the café fragment.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class CafeFragment extends Fragment implements USBFragment {

    /** The list view which displays the café menu items. */
    private ListView listView;

    /** The adapter of the list view. */
    private CafeMenuCategoryAdapter adapter;

    /** The café menu items available at the Urban Sciences Building. */
    private List<CafeMenuItem> menuItems;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cafe, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.listView);
        menuItems = USBManager.shared.getBuilding().getCafe().getItems();

        // Load café menu item categories alphabetically.
        final List<CafeMenuItemCategory> categories = CafeMenuItemCategory.getCategoriesFrom(menuItems);

        Collections.sort(categories, new Comparator<CafeMenuItemCategory>() {
            @Override
            public int compare(CafeMenuItemCategory o1, CafeMenuItemCategory o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        // Initialise the adapter.
        adapter = new CafeMenuCategoryAdapter(getContext(), R.layout.cafe_menu_item_list, categories);
        listView.setAdapter(adapter);

        // Set the on click listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                CafeMenuItemCategory category = (CafeMenuItemCategory) adapter.getItemAtPosition(position);
                presentItemsInCategory(category);
            }
        });
    }

    /**
     * Presents the café menu items in a given category.
     *
     * @param category The category of the café menu items.
     */
    private void presentItemsInCategory(CafeMenuItemCategory category) {
        List<CafeMenuItem> items = CafeMenuItemCategory.getItemsInCategory(category, menuItems);
        Intent intent = new Intent(getActivity(), CafeMenuCategoryActivity.class);
        intent.putExtra("categoryName", category.getName());
        startActivity(intent);
    }

    @Override
    public int getTitle() {
        return R.string.cafe;
    }

    /**
     * A class which represents the array adapter for café menu categories.
     *
     * @author Alexander MacLeod
     * @version 1.0
     */
    public class CafeMenuCategoryAdapter extends ArrayAdapter<CafeMenuItemCategory> {

        /** The activity context. */
        Context context;

        /** The cell layout resource identifier. */
        int resource;

        /** The café menu item categories to display. */
        List<CafeMenuItemCategory> categories;

        public CafeMenuCategoryAdapter(Context context, int resource, List<CafeMenuItemCategory> categories) {
            super(context, resource, categories);
            this.context = context;
            this.resource = resource;
            this.categories = categories;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(resource, null, false);

            TextView textViewName = view.findViewById(R.id.cafeMenuItemNameTextView);

            final CafeMenuItemCategory category = categories.get(position);
            textViewName.setText(category.getName());

            return view;
        }
    }
}
