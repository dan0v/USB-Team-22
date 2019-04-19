package uk.ac.newcastle.team22.usb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.navigation.Compass;

/**
 * A class to provide compass functionality. Code has been adapted for use in this project.
 *
 * @author Viacheslav Iutin - https://github.com/iutinvg/compass - See Compass_License.txt
 * @author Daniel Vincent
 * @version 1.0
 */
public class CompassActivity extends USBActivity {

    private static final String TAG = "CompassActivity";

    // Acceptable error in degrees for checking user heading.
    private static final int compassAccuracyRange = 3;

    private Compass compass;
    private ImageView compassBorder;

    private float currentAzimuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        // Hide activity's title and hide action bar shadow.
        setTitle("");
        getSupportActionBar().setElevation(0);

        compassBorder = findViewById(R.id.navigation_compass_border);
        setupCompass();

        Intent intent = getIntent();
        // Show or hide tour hint.
        boolean isTour = intent.getBooleanExtra("isTour", false);
        TextView compassTourHint = findViewById(R.id.navigation_tour_compass_hint);
        if (!isTour) {
            compassTourHint.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compass_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.compassActionDone) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "start compass");
        compass.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop compass");
        compass.stop();
    }

    private void setupCompass() {
        Intent intent = getIntent();
        // Offset the zero location in compass logic to determine the heading users should face.
        int azimuthOffset = intent.getIntExtra("azimuthOffset", 0);
        compass = new Compass(this);
        Compass.CompassListener cl = getCompassListener();
        compass.setListener(cl);
        compass.setAzimuthOffset(azimuthOffset);
    }

    private void adjustArrow(float azimuth) {
        Log.d(TAG, "will set rotation from " + currentAzimuth + " to "
                + azimuth);

        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = azimuth;

        an.setDuration(300);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        compassBorder.startAnimation(an);
        // Stop activity if user is facing correct direction (within range).
        if (currentAzimuth > (compassAccuracyRange * -1) && currentAzimuth < compassAccuracyRange) {
            compass.stop();
            this.finish();
        }
    }

    private Compass.CompassListener getCompassListener() {
        return new Compass.CompassListener() {
            @Override
            public void onNewAzimuth(final float azimuth) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adjustArrow(azimuth);
                    }
                });
            }
        };
    }
}