package uk.ac.newcastle.team22.usb.coreApp;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.util.Map;

/**
 * A class which parses colors stored in a map by Firebase into Android colors.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class ColorUtility {

    /**
     * The default color to return when a color could not parsed.
     * The color matches Newcastle University's main blue color.
     */
    @ColorInt
    public static int DEFAULT_COLOR = Color.rgb(1, 57, 99);

    /**
     * Parses a color from a map of RGB values.
     * If the color could not be determined, {@code DEFAULT_COLOR} is used.
     *
     * @param dictionary The map containing the color's RGB values.
     * @return The parsed color.
     */
    @ColorInt
    public static int valueOf(Map<String, Long> dictionary) {
        if (dictionary == null) {
            return DEFAULT_COLOR;
        }
        // Abstract RGB values.
        Long red = dictionary.get("r");
        Long green = dictionary.get("g");
        Long blue = dictionary.get("b");

        // Check whether all RGB values were set.
        if (red == null || green == null || blue == null) {
            return DEFAULT_COLOR;
        }

        return Color.rgb(red.intValue(), green.intValue(), blue.intValue());
    }
}
