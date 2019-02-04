package uk.ac.newcastle.team22.usb.coreApp;

import android.app.Application;
import com.google.firebase.FirebaseApp;

/**
 * A class to act as the application's delegate.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class AppDelegate extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
