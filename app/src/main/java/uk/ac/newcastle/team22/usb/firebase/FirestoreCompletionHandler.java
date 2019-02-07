package uk.ac.newcastle.team22.usb.firebase;

/**
 * A Firestore operation completion handler.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public abstract class FirestoreCompletionHandler<Response> {

    /** The number of operations to complete. */
    private int operationsToComplete;

    /**
     * Called when the Firestore operation has completed.
     * @param response The response of the operation.
     */
    public void completed(Response response) {
        if (operationsToComplete > 0) {
            operationsToComplete--;
        }
    }

    /**
     * Called when the Firestore operation has failed.
     * @param exception The throwable exception.
     */
    public void failed(Exception exception) {
        if (operationsToComplete > 0) {
            operationsToComplete--;
        }
    }

    /**
     * Constructor for Firestore Completion handler.
     * @param toComplete The number of operators to complete.
     */
    public FirestoreCompletionHandler(int toComplete) {
        this.operationsToComplete = toComplete;
    }

    /** Constructor for Firestore Completion handler. */
    public FirestoreCompletionHandler() {
        this.operationsToComplete = 0;
    }

    /**
     * @return Boolean value whether all operations have been completed.
     */
    public boolean isCompleted() {
        return operationsToComplete == 0;
    }

}
