package com.example.q3sensorapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.q3sensorapp.R;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor accelSensor, lightSensor, proximitySensor;

    TextView accelText, lightText, proximityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accelText = findViewById(R.id.accel);
        lightText = findViewById(R.id.light);
        proximityText = findViewById(R.id.proximity);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // 🔹 Check if sensors are available
        if (accelSensor == null) accelText.setText("Accelerometer not available");
        if (lightSensor == null) lightText.setText("Light sensor not available");
        if (proximitySensor == null) proximityText.setText("Proximity sensor not available");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 🔹 Register only if sensor exists
        if (accelSensor != null)
            sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);

        if (lightSensor != null)
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        if (proximitySensor != null)
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelText.setText("Accelerometer:\nX: " + event.values[0] + "\nY: " + event.values[1] + "\nZ: " + event.values[2]);
        }

        else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightText.setText("Light: " + event.values[0]);
        }

        else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proximityText.setText("Proximity: " + event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}