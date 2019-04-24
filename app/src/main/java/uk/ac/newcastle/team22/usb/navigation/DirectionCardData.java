package uk.ac.newcastle.team22.usb.navigation;

import android.support.annotation.DrawableRes;

import uk.ac.newcastle.team22.usb.coreApp.AbstractCardData;

/**
 * A class to represent the information stored in a navigation card in the UI.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class DirectionCardData extends AbstractCardData {

    /** The title of the direction. */
    private String directionText;

    /** A description of the distance. */
    private String distanceText;

    /** The image of the direction. */
    @DrawableRes private int directionImage;

    public DirectionCardData(String directionText, String distanceText, int directionImage) {
        this.directionText = directionText;
        this.distanceText = distanceText;
        this.directionImage = directionImage;
    }

    public String getDirectionText() {
        return directionText;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public int getDirectionImage() {
        return directionImage;
    }
}