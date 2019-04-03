package uk.ac.newcastle.team22.usb.fragments;

public class NavigationDirection {

    private String directionText;
    private int distanceText;
    private int directionImage;

    public NavigationDirection() {
    }

    public NavigationDirection(String directionText, int distanceText, int directionImage) {
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

    public int getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(int distanceText) {
        this.distanceText = distanceText;
    }

    public int getDirectionImage() {
        return directionImage;
    }

    public void setDirectionImage(int directionImage) {
        this.directionImage = directionImage;
    }
}