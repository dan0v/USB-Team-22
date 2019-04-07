package uk.ac.newcastle.team22.usb.navigation;


import uk.ac.newcastle.team22.usb.coreApp.AbstractCardData;

/**
 * A class to represent the information stored in a tour card in the UI.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class TourCardData extends AbstractCardData {

    private String nameText;
    private String descriptionText;
    private int image;

    public TourCardData(String nameText, String descriptionText, int image) {
        this.nameText = nameText;
        this.descriptionText = descriptionText;
        this.image = image;
    }

    public String getNameText() {
        return nameText;
    }

    public void setNameText(String nameText) {
        this.nameText = nameText;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}