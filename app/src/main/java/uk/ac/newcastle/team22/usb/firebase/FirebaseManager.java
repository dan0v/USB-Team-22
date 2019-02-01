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

    /** Initialises Firebase. */
    private FirebaseManager() {
        database = FirebaseFirestore.getInstance();
        initialiseFirestore();
    }

    /** Configures the Firestore's Database's default settings. */
    private void initialiseFirestore() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().build();
        database.setFirestoreSettings(settings);
    }

    /**
     * Retrieves documents contained within a Firestore collection.
     *
     * @param collection The collection of documents to retrieve.
     * @param handler The completion handler called once the Firestore operation has finished.
     */
    @SuppressWarnings("unchecked")
    public <T extends FirestoreConstructable> void getDocuments(final FirestoreDatabaseCollection collection, final String path, final FirestoreCompletionHandler handler) {
        CollectionReference collectionRef;
        String collectionIdentifier = collection.getCollectionIdentifier();

        // Determine the location of the collection in the Firestore database.
        if (path == null) {
            collectionRef = database.collection(collectionIdentifier);
        } else {
            collectionRef = database.document(path).collection(collectionIdentifier);
        }

        // Request all documents in the Firestore collection.
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Class<? extends FirestoreConstructable> documentType = collection.getDocumentType();
                QuerySnapshot result = task.getResult();

                // Check whether the Firestore operation was successful.
                if (!task.isSuccessful() || result == null) {
                    handler.failed(task.getException());
                    return;
                }

                // Decode Firestore document dictionaries into Java objects.
                List<T> results = new ArrayList<>();
                try {
                    for (QueryDocumentSnapshot document : result) {
                        T newObject = (T) documentType.newInstance();
                        newObject.initFromFirebase(document.getData());
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
