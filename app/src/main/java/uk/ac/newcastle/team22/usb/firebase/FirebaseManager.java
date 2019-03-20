package uk.ac.newcastle.team22.usb.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
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

    /** The instance of the Firebase authentication. */
    private FirebaseAuth authentication;

    /** Initialises Firebase. */
    private FirebaseManager() {
        database = FirebaseFirestore.getInstance();
        initialiseFirestore();
        authentication = FirebaseAuth.getInstance();
    }

    /** Configures the Firestore's Database's default settings. */
    private void initialiseFirestore() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
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
    public <T extends FirestoreConstructable> void getDocuments(final FirestoreDatabaseCollection collection, final String path, final FirestoreCompletionHandler<List<T>> handler) {
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
                if (!task.isSuccessful() || result == null || result.isEmpty()) {
                    handler.failed(task.getException());
                    return;
                }

                // Decode Firestore document dictionaries into Java objects.
                List<T> results = new ArrayList<>();
                try {
                    for (QueryDocumentSnapshot document : result) {
                        T newObject = (T) documentType.newInstance();
                        newObject.initFromFirebase(document.getData(), document.getId());
                        results.add(newObject);
                    }
                    handler.completed(results);
                } catch (Exception e) {
                    handler.failed(e);
                }
            }
        });
    }

    /**
     * Enables the Firestore cache.
     * By enabling the cache, network access is disabled.
     * While the cache is enabled, all snapshot listeners and document requests retrieve results
     * from the cache. Write operations are queued until network access is re-enabled.
     *
     * See <a href="https://firebase.google.com/docs/firestore/manage-data/enable-offline">Firestore Data Model</a>
     * for more information on Firestore caching.
     *
     * @param handler The completion handler called once the cache has been enabled.
     */
    public void enableCache(final FirestoreCompletionHandler<Void> handler) {
        database.disableNetwork().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                handler.completed(null);
            }
        });
    }

    /**
     * Disables the Firestore cache.
     * By disabling the cache, network access is enabled.
     * It is not guaranteed that new data will be returned from Firestore operations.
     *
     * See <a href="https://firebase.google.com/docs/firestore/manage-data/enable-offline">Firestore Data Model</a>
     * for more information on Firestore caching.
     *
     * @param handler The completion handler called once the cache has been disabled.
     */
    public void disableCache(final FirestoreCompletionHandler<Void> handler) {
        database.enableNetwork().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                handler.completed(null);
            }
        });
    }

    /**
     * Authenticates the user so that they are able to interact with Firebase.
     *
     * @param handler The completion handler called once the user has been authenticated.
     */
    public void authenticate(final FirestoreCompletionHandler<Void> handler) {
        FirebaseUser currentUser = authentication.getCurrentUser();
        /*
        if (currentUser != null) {
            handler.completed(null);
            return;
        }
        */

        // Sign into Firebase anonymously.
        authentication.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("", "Successful");
                    handler.completed(null);
                } else {
                    Log.i("", "Failed");
                    Log.i("", task.getException().getLocalizedMessage());
                    handler.failed(task.getException());
                }
            }
        });
    }
}
