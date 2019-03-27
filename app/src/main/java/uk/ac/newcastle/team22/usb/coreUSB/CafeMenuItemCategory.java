package uk.ac.newcastle.team22.usb.coreUSB;

/**
 * A class which represents a category of items at the café.
 * See {@link Cafe} for more information.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class CafeMenuItemCategory {

    /** The name of the category. */
    private String name;

    /** The identifier of the icon. */
    private int iconIdentifier;

    CafeMenuItemCategory(String name, int iconIdentifier) {
        this.name = name;
        this.iconIdentifier = iconIdentifier;
    }

    /**
     * @return The name of the category.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The icon of the category.
     */
    public void getIcon() {

    }
}
