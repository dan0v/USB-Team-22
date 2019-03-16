package uk.ac.newcastle.team22.usb.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

public class TourNode extends Node {
    private String imageIdentifier;
    private String name;
    private String description;

    /** Constructor. */
    public TourNode(int nodeIdentifier, int floorNumber, String description, String imageIdentifier, String name) {
        super(nodeIdentifier, floorNumber);
        this.description = description;
        this.imageIdentifier = imageIdentifier;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageIdentifier() {
        return imageIdentifier;
    }

    public String getName() {
        return name;
    }
}
