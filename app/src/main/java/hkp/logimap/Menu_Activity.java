package hkp.logimap;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Menu_Activity extends AppCompatActivity {
    MyApplication application;
    Context context;
    Handler mHandler;
    String hour, minute;

    private final static int INTERVAL = 1000 * 60 * 5; //check deadlines every 5min
    private final static int delivery_INTERVAL = 1000 * 10; //check if delivery is ready every 10s

    Thread waitforjson = new Thread(new Runnable() {
        @Override
        public void run() {
            final Button mapButton = (Button) findViewById(R.id.route_button);
            final Button locationsButton = (Button) findViewById(R.id.destinations_button);

            if(application.current_delivery != null) {
                try {
                    SystemClock.sleep(1000);
                    mapButton.post(new Runnable() {
                        @Override
                        public void run() {
                            mapButton.setEnabled(true);
                        }
                    });
                    locationsButton.post(new Runnable() {
                        @Override
                        public void run() {
                            locationsButton.setEnabled(true);

                        }
                    });

                    //Start other threads working on delivery
                    checking_deadlines.start();
                    makePUTs.start();
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
            } else
                mHandler.postDelayed(this, delivery_INTERVAL);
        }
    });

    Thread checking_deadlines = new Thread(new Runnable() {
        @Override
        public void run() {
            ArrayList<Location> temp = new ArrayList<>(application.current_delivery.locations.values());
            //TODO GET FIRST UNCOMPLETED LOCATION
            for( Location l: temp)
                if (!l.finished)
                    check_deadline(l);
            mHandler.postDelayed(this, INTERVAL);
        }
    });

    private void check_deadline(Location location) {
        DateFormat format = new SimpleDateFormat("hh:mm");
        Calendar c = Calendar.getInstance();
        hour = ((Integer)c.get(Calendar.HOUR)).toString();
        minute = ((Integer)c.get(Calendar.MINUTE)).toString();
        try {
            long current_time = new Time(format.parse(hour + ":" + minute).getTime()).getTime();
            long diff = location.deadline.getTime() - current_time;
            long diffmin =  diff / (60 * 1000) % 60;
            long diffh =  diff / (60 * 60 * 1000) % 24;

            if(diffh <= 0 && diffmin <= 30) {
                location.shortDeadline = true;
                notify(location);
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    private void notify(Location location) {
        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                    .setContentTitle("DEADLINE")
                    .setContentText(location.name + ":" + location.deadline)
                    .setAutoCancel(true);

        Intent resultIntent = new Intent(this, Destinations_List_Activity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(Destinations_List_Activity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());
    }

    Thread makePUTs = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                RestPut api = new RestPut(preferences.getString("username", "#"), preferences.getString("password", "#"),
                        new RestPut.AsyncResponse() {
                            @Override
                            public void processFinish(String result) {
                            }
                        }, application);

                for (PUTRequest put : application.puts) {
                    api.execute(put.url, put.newjson);

                    application.puts.remove(put);
                }
                mHandler.postDelayed(this, INTERVAL);
            }catch (Exception e) {
                Log.i("PUT", "PUT request did not completed");
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        application = (MyApplication) getApplication();
        mHandler = new Handler();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle("LogiMap");

        waitforjson.start();
    }

    public void onClickMap(View v) {
        startActivity(new Intent(getApplicationContext(), Maps_Activity.class));
    }
    public void onClickDestinations(View v) {
        startActivity(new Intent(getApplicationContext(), Destinations_List_Activity.class));
    }
    public void onClickHistory(View v) {
        try {
        SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String newjson = "{\"km_done\":1337,\"orders_done\":15,\"pkg_delivered\":1,\"pkg_delayed\":666}";

        RestPut api = new RestPut(preferences.getString("username", "#"), preferences.getString("password", "#"),
                new RestPut.AsyncResponse() {
                    @Override
                    public void processFinish(String result) { }
                }, application);

        api.execute("driverstats/1", newjson);

        String test = "";
            test = api.get(10, TimeUnit.SECONDS);
        Log.i("TEST_PUT", test);
        }catch (Exception e) {
            Log.i("TEST", "notsuceeded");
//            Log.e("ERROR", e.getMessage(), e);
        }
    }
    public void onClickDriverStatistics(View v) {
        startActivity(new Intent(getApplicationContext(), DriverStatistics_Activity.class));
    }
}