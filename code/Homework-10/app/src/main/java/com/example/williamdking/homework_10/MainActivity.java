package com.example.williamdking.homework_10;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

    private RelativeLayout compass;
    private Button location;
    private EditText longitude;
    private EditText latitude;
    private TextView address;

    private String provider;

    private SensorEventListener listener = new SensorEventListener() {
        float[] accelerometerValues = new float [3];
        float[] magneticValues = new float[3];
        private float lastRotateDegree;
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                accelerometerValues = sensorEvent.values.clone();
            } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticValues = sensorEvent.values.clone();
            }

            float[] R = new float[9];
            float[] values = new float[3];

            SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticValues);
            SensorManager.getOrientation(R, values);

            float rotateDegree = -(float)Math.toDegrees(values[0]);

            if (Math.abs(rotateDegree - lastRotateDegree) > 1) {
                RotateAnimation animation = new RotateAnimation(lastRotateDegree, rotateDegree, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setFillAfter(true);
                compass.startAnimation(animation);
                lastRotateDegree = rotateDegree;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compass = (RelativeLayout)findViewById(R.id.compass);
        location = (Button)findViewById(R.id.location);
        longitude = (EditText)findViewById(R.id.longitude);
        latitude = (EditText)findViewById(R.id.latitude);
        address = (TextView)findViewById(R.id.address);

        List<String> providerList = locationManager.getProviders(true);

        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "No location provider to use", Toast.LENGTH_SHORT).show();
            return;
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location loc = locationManager.getLastKnownLocation(provider);

                if (loc != null) {
                    longitude.setText("" + loc.getLongitude());
                    latitude.setText("" + loc.getLatitude());
                }
            }
        });

    }
}
