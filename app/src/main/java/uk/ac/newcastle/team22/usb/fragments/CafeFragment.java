package uk.ac.newcastle.team22.usb.fragments;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItem;
import uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItemCategory;
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
    private CafeMenuItemCategoryAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cafe, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.cafe_list_view);

        // Load and sort café menu items into categories alphabetically.
        List<CafeMenuItem> cafeMenuItems = USBManager.shared.getBuilding().getCafe().getItems();
        List<CafeMenuItemCategory> categories = new ArrayList<>();

        for (CafeMenuItem item : cafeMenuItems) {
            if (!categories.contains(item.getCategory())) {
                categories.add(item.getCategory());
            }
        }

        Collections.sort(cafeMenuItems, new Comparator<CafeMenuItem>() {
            @Override
            public int compare(CafeMenuItem o1, CafeMenuItem o2) {
                return o1.compareAlphabeticallyTo(o2);
            }
        });

        // Initialise the adapter.
        adapter = new CafeMenuItemCategoryAdapter(getContext(), R.layout.cafe_menu_item_list, categories);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public int getTitle() {
        return R.string.cafe;
    }

    /**
     * A class which represents the array adapter for café menu item categories.
     *
     * @author Alexander MacLeod
     * @version 1.0
     */
    public class CafeMenuItemCategoryAdapter extends ArrayAdapter<CafeMenuItemCategory> {

        /** The activity context. */
        Context context;

        /** The cell layout resource identifier. */
        int resource;

        /** The café menu item categories to display. */
        List<CafeMenuItemCategory> categories;

        public CafeMenuItemCategoryAdapter(Context context, int resource, List<CafeMenuItemCategory> categories) {
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

            TextView title = view.findViewById(R.id.cafeMenuCategoryTitleTextView);
            TextView detail = view.findViewById(R.id.cafeMenuCategoryDetailTextView);
            ImageView imageView = view.findViewById(R.id.cafeMenuCategoryImageView);

            final CafeMenuItemCategory category = categories.get(position);
            Icon icon = Icon.createWithResource(getContext(), category.getIcon());
            title.setText(category.getName());
            imageView.setImageIcon(icon);

            return view;
        }
    }
}
