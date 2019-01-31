package uk.ac.newcastle.team22.usb.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which manages interaction and the sharing of data between the application and Firebase.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class FirebaseManager {

    /** The instance of the Firestore Database. */
    private FirebaseFirestore database;

    /** The shared instance of the Firebase manager. */
    public static FirebaseManager shared = new FirebaseManager();

    /** Initialises Firebase */
    private FirebaseManager() {
        database = FirebaseFirestore.getInstance();
        initialiseFirestore();
    }

    /** Configures the Firestore's Database's default settings. */
    private void initialiseFirestore() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        database.setFirestoreSettings(settings);
    }

    /**
     * Retrieves documents contained within a Firestore collection.
     *
     * @param collection The collection of documents to retrieve.
     * @param handler The completion handler called once the Firestore operation has finished.
     */
    @SuppressWarnings("unchecked")
    public <T extends FirestoreConstructable> void getDocuments(final FirestoreDatabaseCollection collection, final FirestoreCompletionHandler handler) {
        database.collection(collection.getCollectionIdentifier()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Class<? extends FirestoreConstructable> documentType = collection.getDocumentType();

                // Check whether the Firestore operation was successful.
                if (!task.isSuccessful()) {
                    handler.failed(task.getException());
                    return;
                }

                // Decode Firestore document dictionaries into Java objects.
                List<T> results = new ArrayList<>();
                try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        T newObject = (T) documentType.newInstance().initFromFirebase(document.getData());
                        results.add(newObject);
                    }
                    handler.completed(results);
                } catch (Exception e) {
                    handler.failed(e);
                }
            }
        });
    }
}
