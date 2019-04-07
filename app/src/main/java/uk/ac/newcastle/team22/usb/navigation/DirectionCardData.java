package uk.ac.newcastle.team22.usb.navigation;

import uk.ac.newcastle.team22.usb.coreApp.AbstractCardData;

/**
 * A class to represent the information stored in a navigation card in the UI.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class DirectionCardData extends AbstractCardData {

    private String directionText;
    private String distanceText;
    private int directionImage;

    public DirectionCardData(String directionText, String distanceText, int directionImage) {
        this.directionText = directionText;
        this.distanceText = distanceText;
        this.directionImage = directionImage;
    }

    public String getDirectionText() {
        return directionText;
    }

    public void setDirectionText(String directionText) {
        this.directionText = directionText;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    public int getDirectionImage() {
        return directionImage;
    }

    public void setDirectionImage(int directionImage) {
        this.directionImage = directionImage;
    }
}