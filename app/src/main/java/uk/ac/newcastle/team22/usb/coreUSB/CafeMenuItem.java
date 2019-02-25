package uk.ac.newcastle.team22.usb.coreUSB;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

/**
 * A class which represents an item on the menu at the café in the Urban Sciences Building.
 * See {@link Cafe} for more information.
 *
 * @author Patrick Lindley
 * @author Alexander MacLeod
 * @version 1.0
 */
public class CafeMenuItem implements FirestoreConstructable<CafeMenuItem> {

    /** The unique identifier of the café menu item. */
    private String identifier;

    /** The name of the café menu item. */
    private String name;

    /** The price of the café menu item in pence. */
    private int price;

    @Override
    public CafeMenuItem initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) {
        String name = (String) firestoreDictionary.get("name");
        int price = ((Number) firestoreDictionary.get("price")).intValue();

        this.identifier = documentIdentifier;
        this.name = name;
        this.price = price;
        return this;
    }

    /** Empty constructor. */
    public CafeMenuItem() {}

    public CafeMenuItem(int price, String name) {
        this.price = price;
        this.name = name;
    }

    /**
     * @return The name of the café menu item.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The price of the café menu item in pence.
     */
    public int getPrice() {
        return price;
    }

    /**
     * @return The formatted price of the café menu item.
     */
    public String getFormattedPrice() {
        Locale currentLocale = Locale.getDefault();
        NumberFormat format = NumberFormat.getCurrencyInstance(currentLocale);
        format.setCurrency(Currency.getInstance("GBP"));

        double pounds = (double) price / 100;
        return format.format(pounds);
    }

    @Override
    public String toString() {
        return "Menu Item (name: " + getName() + ", price: " + getFormattedPrice() + ")";
    }
}
