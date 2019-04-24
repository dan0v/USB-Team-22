package uk.ac.newcastle.team22.usb.coreApp;

/**
 * An interface which defines a response from a async operation.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public interface AsyncResponse {

    /** Called when the async operation has completed. */
    void onComplete();

    /** Called when the async operation has failed due to a bad internet connection. */
    void onBadNetwork();
}