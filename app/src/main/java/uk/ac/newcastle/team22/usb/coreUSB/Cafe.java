package uk.ac.newcastle.team22.usb.coreUSB;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which represents the café in Urban Sciences Building.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class Cafe {

    /**
     * The items, food or drink, which are served at the café.
     * See {@link CafeMenuItem} for more information.
     */
    private List<CafeMenuItem> items;

    /** The opening hours of the café. */
    private OpeningHours openingHours;

    /** Constructor using an Urban Sciences Building update. */
    public Cafe(USBUpdate update) {
        this.items =  update.getCafeMenuItems();
        this.openingHours = update.getOpeningHours().get(OpeningHours.Service.CAFE);
    }

    /**
     * @return The items, food or drink, which are served at the café.
     */
    public List<CafeMenuItem> getItems() {
        return items;
    }

    /**
     * @param category The category of café menu items.
     * @param context The context.
     * @return The items, food or drink, which are served at the café in a given category.
     */
    public List<CafeMenuItem> getItems(CafeMenuItemCategory category, Context context) {
        List<CafeMenuItem> items = new ArrayList<>();
        for (CafeMenuItem item : this.items) {
            if (item.getCategory().equals(category)) {
                items.add(item);
            }
            if (category.equals(CafeMenuItemCategory.getMealDealCategory(context))) {
                if (item.isMealDeal()) {
                    items.add(item);
                }
            }
        }
        return items;
    }

    /**
     * @return The opening hours of the café.
     */
    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    @Override
    public String toString() {
        return "USB Café (items: " + items.size() + ")";
    }
}
