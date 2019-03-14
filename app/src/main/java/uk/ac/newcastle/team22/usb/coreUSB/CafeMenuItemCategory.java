package uk.ac.newcastle.team22.usb.coreUSB;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which defines a café menu item category.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class CafeMenuItemCategory {

    /** The name of the category. */
    private String name;

    public CafeMenuItemCategory(String name) {
        this.name = name;
    }

    /**
     * Gathers the food and drink categories on the café menu.
     *
     * @param menuItems The café menu items.
     * @return The food and drink categories on the café menu.
     */
    public static List<CafeMenuItemCategory> getCategoriesFrom(List<CafeMenuItem> menuItems) {
        List<CafeMenuItemCategory> categories = new ArrayList<>();
        for (CafeMenuItem item : menuItems) {
            if (!categories.contains(item.getCategory())) {
                categories.add(item.getCategory());
            }
        }
        return categories;
    }

    /**
     * Gathers the café menu items in a given category.
     *
     * @param category The café menu item category to filter.
     * @param menuItems The café menu items to filter.
     * @return The filtered café menu items.
     */
    public static List<CafeMenuItem> getItemsInCategory(CafeMenuItemCategory category, List<CafeMenuItem> menuItems) {
        List<CafeMenuItem> items = new ArrayList<>();
        for (CafeMenuItem item : menuItems) {
            if (item.getCategory().equals(category)) {
                items.add(item);
            }
        }
        return items;
    }

    /**
     * @return The name of the café menu item category.
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object rhs) {
        if (this == rhs) return true;
        if (!(rhs instanceof CafeMenuItemCategory)) return false;

        CafeMenuItemCategory c = (CafeMenuItemCategory) rhs;
        return name.equals(c.name);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 37 * hash + (name == null ? 0 : name.hashCode());
        return 37 * hash;
    }
}
