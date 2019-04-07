package uk.ac.newcastle.team22.usb.coreUSB;

import android.content.Context;

import uk.ac.newcastle.team22.usb.R;

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

    public CafeMenuItemCategory(String name, int iconIdentifier) {
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
    public int getIcon() {
        switch (iconIdentifier) {
            case 1:
                return R.drawable.food_category_1;
            case 2:
                return R.drawable.food_category_2;
            case 3:
                return R.drawable.food_category_3;
            case 4:
                return R.drawable.food_category_4;
            case 5:
                return R.drawable.food_category_5;
            case 6:
                return R.drawable.food_category_6;
            case 7:
                return R.drawable.food_category_7;
            case 8:
                return R.drawable.food_category_8;
            case 9:
                return R.drawable.food_category_9;
            case 10:
                return R.drawable.food_category_10;
            case 11:
                return R.drawable.food_category_11;
            default:
                return 0;
        }
    }

    /**
     * Retrieves the café menu item category.
     *
     * @param context The activity context.
     * @return The category of café menu item.
     */
    public static CafeMenuItemCategory getMealDealCategory(Context context) {
        return new CafeMenuItemCategory(context.getString(R.string.mealDeal), 11);
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
        int result = 17;
        result = 31 * result + name.hashCode();
        return result;
    }
}
