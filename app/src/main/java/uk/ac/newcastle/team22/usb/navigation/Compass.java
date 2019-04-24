package uk.ac.newcastle.team22.usb.navigation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * A class to provide compass functionality. Code has been altered for use in this project.
 *
 * @author Viacheslav Iutin - https://github.com/iutinvg/compass - See Compass_License.txt
 * @author Daniel Vincent
 * @version 1.0
 */
public class Compass implements SensorEventListener {

    /** The compass listener. */
    private CompassListener listener;

    /** The instance of the sensor manager. */
    private SensorManager sensorManager;

    /** The g sensor. */
    private Sensor gSensor;

    /** The m sensor. */
    private Sensor mSensor;

    /** The m sensor gravity. */
    private float[] mGravity = new float[3];

    /** The m sensor geomagnetic. */
    private float[] mGeomagnetic = new float[3];

    /** The collection of Rs. */
    private float[] R = new float[9];

    /** The collection of Is. */
    private float[] I = new float[9];

    /** The current azimuth. */
    private float azimuth;

    /** The current azimuth offset. */
    private int azimuthOffset;

    public Compass(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    /** Starts the sensor listener. */
    public void start() {
        sensorManager.registerListener(this, gSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    /** Stops the sensor listener. */
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2];
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * event.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * event.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)  * event.values[2];
            }
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = (float) Math.toDegrees(orientation[0]); // orientation
                azimuth = (azimuth + azimuthOffset + 360) % 360;
                if (listener != null) {
                    listener.onNewAzimuth(azimuth);
                }
            }
        }
    }

    /**
     * Used to alter the heading users should face.
     *
     * @param offset heading the user should face in degrees.
     */
    public void setAzimuthOffset(int offset) {
        int mirror = 360 - offset;
        azimuthOffset = mirror;
    }

    /**
     * Sets the compass listener.
     *
     * @param l The compass listener.
     */
    public void setListener(CompassListener l) {
        listener = l;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    /**
     * An interface which defines a listener for compass changes.
     *
     * @author Daniel Vincent
     * @version 1.0
     */
    public interface CompassListener {
        void onNewAzimuth(float azimuth);
    }
}