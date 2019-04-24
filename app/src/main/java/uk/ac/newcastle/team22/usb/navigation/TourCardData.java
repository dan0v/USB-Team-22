package uk.ac.newcastle.team22.usb.navigation;

import android.support.annotation.DrawableRes;

import uk.ac.newcastle.team22.usb.coreApp.AbstractCardData;

/**
 * A class to represent the information stored in a tour card in the UI.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class TourCardData extends AbstractCardData {

    /** The name of the tour location. */
    private String nameText;

    /** The description of the tour location. */
    private String descriptionText;

    /** The image of the tour location. */
    @DrawableRes private int image;

    public TourCardData(String nameText, String descriptionText, int image) {
        this.nameText = nameText;
        this.descriptionText = descriptionText;
        this.image = image;
    }

    public String getNameText() {
        return nameText;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public int getImage() {
        return image;
    }
}