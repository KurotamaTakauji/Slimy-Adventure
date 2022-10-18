package com.example.slimyadventure.game;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorHandler implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor magnetometer;
    private Sensor accelerometer;

    // Gravity rotational data
    private float gravity[];
    // Magnetic rotational data
    private float magnetic[]; //for magnetic rotational data
    private float accels[] = new float[3];
    private float mags[] = new float[3];
    private float[] values = new float[3];
    private float[] startValues = null;
    private boolean pauseLoop = false;

    // azimuth, pitch and roll
    private float azimuth = 0.0f;
    private float pitch = 0.0f;
    private float roll = 0.0f;

    public SensorHandler(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (magnetometer != null && accelerometer != null) {
            Log.i("Create", "All sensors work fine.");
        } else if (accelerometer == null) {
            Log.i("error:", "Accelerometer is not accessible!");
        } else if (magnetometer == null) {
            Log.i("error:", "Magnetometer is not accessible!");
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                mags = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accels = event.values.clone();
                break;
        }
        if (mags != null && accels != null) {
            gravity = new float[9];
            magnetic = new float[9];
            SensorManager.getRotationMatrix(gravity, magnetic, accels, mags);
            float[] outGravity = new float[9];
            SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X,SensorManager.AXIS_Z, outGravity);
            SensorManager.getOrientation(outGravity, values);

            if(startValues == null){
                startValues = values;
            }

            if(Math.abs(values[2] * 57.2957795f - startValues[2]) < 30.0){
                azimuth = values[0] * 57.2957795f - startValues[0];
                pitch = values[1] * 57.2957795f - startValues[1];
                roll = values[2] * 57.2957795f - startValues[2];
            }
            mags = null;
            accels = null;
        }
    }



    protected void onResume() {
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        sensorManager.unregisterListener(this);
    }

    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }

    public boolean getPauseLoop() {
        return pauseLoop;
    }
}