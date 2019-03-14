package uk.ac.newcastle.team22.usb.coreUSB;

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
    public Cafe(USBUpdateManager.USBUpdate update) {
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
