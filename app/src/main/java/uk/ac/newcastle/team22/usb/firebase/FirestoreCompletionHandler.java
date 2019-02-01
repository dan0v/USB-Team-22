package uk.ac.newcastle.team22.usb.firebase;

/**
 * A Firestore operation completion handler.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public interface FirestoreCompletionHandler<Response> {

    /**
     * Called when the Firestore operation has completed.
     * @param response The response of the operation.
     */
     void completed(Response response);

    /**
     * Called when the Firestore operation has failed.
     * @param exception The throwable exception.
     */
    void failed(Exception exception);
}
