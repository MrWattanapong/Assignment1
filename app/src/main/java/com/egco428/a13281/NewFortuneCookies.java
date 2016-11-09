package com.egco428.a13281;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;


public class NewFortuneCookies extends AppCompatActivity implements SensorEventListener {

    private CommentsDataSource dataSource;
    private SensorManager sensorManager;
    private long lastUpdate;
    private Button shakeBtn;
    private Boolean checkshake = false;
    private Boolean save = false;
    private String result;
    private String date;
    private String img;
    int n = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fortune_cookies);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.cookiesbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        shakeBtn = (Button)findViewById(R.id.shakeBtn);
        dataSource = new CommentsDataSource(this);
        dataSource.open();

        shakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (save){
                    Comment comment = null;
                    comment = dataSource.createComment(result,img,date);
                    finish();

                }else {
                    checkshake = true;
                    shakeBtn.setText("Shaking...");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();


        if (accelationSquareRoot >= 5 && checkshake) {
            TextView resultText = (TextView)findViewById(R.id.resultText);
            TextView dateText = (TextView)findViewById(R.id.dateText);
            ImageView cookies = (ImageView)findViewById(R.id.cookiesImg);

            if (actualTime - lastUpdate < 700) {
                return;
            }
            n++;
            if(n==6){
                shakeBtn.setText("Save");
                String[] resultlist = {"Something surprise you today","You will get A","You're Lucky","Don't Panic","Work Harder"};
                int nextInt = new Random().nextInt(5);
                resultText.setText("Result :  "+ resultlist[nextInt]);
                result = resultlist[nextInt];

                int res = getResources().getIdentifier("opened" + nextInt, "drawable", getPackageName());
                cookies.setImageResource(res);
                img = "opened"+nextInt;

                DateFormat df = new SimpleDateFormat("d-MMM-yyyy  HH:mm");
                date = df.format(Calendar.getInstance().getTime());
                dateText.setText("Date :   " + date);

                checkshake = false;
                save = true;
            }

            lastUpdate = actualTime;
            Toast.makeText(this, "Device was shuffled", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}

