package uk.ac.newcastle.team22.usb.fragments;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItem;
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
    private CafeMenuItemAdapter adapter;

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

        listView = view.findViewById(R.id.listView);

        // Load and sort café menu items alphabetically.
        List<CafeMenuItem> cafeMenuItems = USBManager.shared.getBuilding().getCafe().getItems();
        Collections.sort(cafeMenuItems, new Comparator<CafeMenuItem>() {
            @Override
            public int compare(CafeMenuItem o1, CafeMenuItem o2) {
                return o1.compareAlphabeticallyTo(o2);
            }
        });

        // Initialise the adapter.
        adapter = new CafeMenuItemAdapter(getContext(), R.layout.cafe_menu_item_list, cafeMenuItems);
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
