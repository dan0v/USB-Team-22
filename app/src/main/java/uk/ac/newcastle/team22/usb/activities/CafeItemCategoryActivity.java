package uk.ac.newcastle.team22.usb.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItem;

/**
 * A class which displays café menu items in a category.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class CafeItemCategoryActivity extends USBActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_item_category);
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

            TextView title = view.findViewById(R.id.cafeMenuCategoryTitleTextView);
            TextView detail = view.findViewById(R.id.cafeMenuCategoryDetailTextView);

            final CafeMenuItem menuItem = menuItems.get(position);
            title.setText(menuItem.getName());
            detail.setText(menuItem.getFormattedPrice());

            return view;
        }
    }
}
