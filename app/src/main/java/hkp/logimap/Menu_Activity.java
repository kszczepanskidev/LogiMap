package hkp.logimap;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Menu_Activity extends AppCompatActivity {
    MyApplication application;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    Context context;
    Handler mHandler;
    String hour, minute;

    private final static int INTERVAL = 1000 * 60 * 5;      //check deadlines every 5min
    private final static int put_INTERVAL = 1000 * 60;      //make PUT requests every 1min
    private final static int delivery_INTERVAL = 1000 * 30; //try to get delivery every 30s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        application = (MyApplication) getApplication();
        preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        edit = preferences.edit();
        mHandler = new Handler();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle("LogiMap");

        //Try to get delivery
        if(application.current_delivery == null && !deliveryFromServer.isAlive())
            getDelivery();
    }

    @Override
    protected void onResume() {
        if(application.current_delivery == null && !deliveryFromServer.isAlive())
            getDelivery();

        super.onResume();
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


    //Main application threads
    private Thread deliveryFromFile = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                application.current_delivery = new Delivery(new JSONObject(new JSONloader(application).load("delivery")));
                activateMenu();
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                edit.putBoolean("deliveryInFile", false); edit.commit();

                //If reading from file failed try to download from server
                deliveryFromServer.start();
            }
        }
    });

    private Integer getCurrentDeliveryID() {
        RestGet getID = new RestGet(preferences.getString("username", "#"), preferences.getString("password", "#"),
                new RestGet.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {}
                });

        try {
            getID.execute("drivers/" + preferences.getInt("driverID", -1));
            String result = getID.get(1, TimeUnit.MINUTES);

            return new JSONObject(result).getInt("current_order");
        }catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return -1;
        }
    }

    private Thread deliveryFromServer = new Thread(new Runnable() {
        final Runnable thread = this;
        @Override
        public void run() {
            RestGet getDelivery = new RestGet(preferences.getString("username", "#"), preferences.getString("password", "#"),
                    new RestGet.AsyncResponse() {
                        @Override
                        public void processFinish(String result) {
                            try {
                                if (result != "ERROR") {
                                    JSONObject delivery = new JSONObject(result);
                                    if(delivery.getInt("id") != preferences.getInt("prevDeliveryID", -1)) {
                                        application.current_delivery = new Delivery(delivery);
                                        application.current_delivery.saveDeliveryToFile(result,"", application);

                                        //If delivery is not yet accepted then popout
                                        if (application.current_delivery.state == 1) {
                                            startActivity(new Intent(getApplicationContext(), New_Delivery_Activity.class));
                                        }
                                        activateMenu();
                                    } else
                                        throw new Exception();
                                }
                            } catch (Exception e) {
                                Log.e("ERROR", e.getMessage(), e);
                                mHandler.postDelayed(thread, INTERVAL);
                            }
                        }
                    });
            try {
                Integer orderID = getCurrentDeliveryID();

                if (orderID > 0) {
                    edit.putInt("deliveryID", orderID);
                    edit.commit();

                    getDelivery.execute("orders/" + orderID);
                }
                else
                    throw new Exception();
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                mHandler.postDelayed(this, delivery_INTERVAL);
            }
        }
    });

    private void getDelivery() {
        deactivateMenu();
            if (preferences.getBoolean("deliveryInFile", false)) {
                deliveryFromFile.start();
            } else {
                deliveryFromServer.start();
            }
    }

    private void activateMenu() {
            final Button mapButton = (Button) findViewById(R.id.route_button);
            final Button locationsButton = (Button) findViewById(R.id.destinations_button);

                try {
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
        }

    private void deactivateMenu() {
        final Button mapButton = (Button) findViewById(R.id.route_button);
        final Button locationsButton = (Button) findViewById(R.id.destinations_button);

        mapButton.post(new Runnable() {
                @Override
                public void run() {
                    mapButton.setEnabled(false);
                }
            });
        locationsButton.post(new Runnable() {
            @Override
            public void run() {
                locationsButton.setEnabled(false);
            }
            });
        }

    Thread checking_deadlines = new Thread(new Runnable() {
        @Override
        public void run() {
            ArrayList<Location> temp = new ArrayList<>(application.current_delivery.locations.values());
            //TODO: CHECK IF IT CHECKS ONLY FIRST UNFINISHED
            for( Location l: temp)
                if (!l.finished) {
                    check_deadline(l);
                    break;
                }
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
            }catch (Exception e) {
                Log.e("PUT", "PUT request failed");
            } finally {
                mHandler.postDelayed(this, put_INTERVAL);
            }
        }
    });
}