package hkp.logimap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Menu_Activity extends AppCompatActivity {
    MyApplication application;
    Context context;

    Thread waitforjson = new Thread(new Runnable() {
        @Override
        public void run() {
            final Button mapButton = (Button) findViewById(R.id.route_button);
            final Button locationsButton = (Button) findViewById(R.id.destinations_button);

            while(application.current_delivery == null);
            try {
                SystemClock.sleep(1000);
                Log.i("TEST", "InTRY");
                mapButton.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("TEST", "inpost1");
                        mapButton.setEnabled(true);
                    }
                });
                locationsButton.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("TEST", "inpost2");
                        locationsButton.setEnabled(true);
                    }
                });
                Log.i("TEST", "InTRY2");
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        application = (MyApplication) getApplication();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        Encryptor encryptor = new Encryptor();
        try {
            setTitle(encryptor.decrypt(preferences.getString("username", "#")) + ":" + encryptor.decrypt(preferences.getString("password", "#")) + ":" + preferences.getInt("driverID", -1));
        }catch(Exception e){
            Log.e("ERROR", e.getMessage(),e);
        }

        waitforjson.start();
    }

    public void onClickMap(View v) {
        startActivity(new Intent(getApplicationContext(), Maps_Activity.class));
    }
    public void onClickDestinations(View v) {
        startActivity(new Intent(getApplicationContext(), Destinations_List_Activity.class));
    }
    public void onClickHistory(View v) {
    }
    public void onClickDriverStatistics(View v) {
        startActivity(new Intent(getApplicationContext(), DriverStatistics_Activity.class));
    }
}