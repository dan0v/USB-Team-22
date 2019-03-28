package uk.ac.newcastle.team22.usb.navigation;

/**
 * All locations to be navigated to must implement this interface.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public interface Navigable {
    Node getNavigationNode();
}
