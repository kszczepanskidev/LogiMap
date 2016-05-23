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

import java.io.OutputStreamWriter;
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

        deactivateMenu();

        String json = "{\"id\": 2,\"driver\": {\"name\": \"Grzegorz\",\"surname\": \"Mały\"},\"vehicle\": {\"reg_number\": \"PO1652A\",\"brand\": \"Opel\",\"model\": \"Vivaro\",\"category\": \"C\"},\"route\": {\"length\": \"25.0\",\"locations\": [{\"order\": 1,\"location\": {\"id\": 4,\"name\": \"Domek\",\"address\": \"Å\\u0081ubieÅ„skich 4A, 96-317 GuzÃ³w\",\"latitude\": \"52.116354\",\"longitude\": \"20.337085\"}}, {\"order\": 2,\"location\": {\"id\": 1,\"name\": \"NASA\",\"address\": \"Trakt Partyzancki 22A, 05-152 JanÃ³wek\",\"latitude\": \"52.343659\",\"longitude\": \"20.711088\"}}, {\"order\": 3,\"location\": {\"id\": 3,\"name\": \"Petrochemia PÅ‚ock\",\"address\": \"Wyszogrodzka 145, 96-503 Sochaczew\",\"latitude\": \"52.270261\",\"longitude\": \"20.284913\"}}]},\"package\": [{\"id\": 3,\"code\": \"AF5514\",\"status\": 1,\"location\": 4,\"deliver_before\": \"23:01\"}, {\"id\": 1,\"code\": \"AF5516\",\"status\": 3,\"location\": 1,\"deliver_before\": \"09:00\"}, {\"id\": 2,\"code\": \"AF5571\",\"status\": 3,\"location\": 3,\"deliver_before\": \"13:00\"}],\"waypoints\": [{\"lat\": 52.34365,\"lng\": 20.71109}, {\"lat\": 52.34272,\"lng\": 20.7082}, {\"lat\": 52.34144,\"lng\": 20.70453}, {\"lat\": 52.34066,\"lng\": 20.70195}, {\"lat\": 52.34031,\"lng\": 20.70112}, {\"lat\": 52.34024,\"lng\": 20.70097}, {\"lat\": 52.34003,\"lng\": 20.70039}, {\"lat\": 52.33967,\"lng\": 20.69908}, {\"lat\": 52.33963,\"lng\": 20.69897}, {\"lat\": 52.3393,\"lng\": 20.6982}, {\"lat\": 52.3392,\"lng\": 20.69797}, {\"lat\": 52.33906,\"lng\": 20.69755}, {\"lat\": 52.33904,\"lng\": 20.69745}, {\"lat\": 52.33903,\"lng\": 20.69715}, {\"lat\": 52.33908,\"lng\": 20.69659}, {\"lat\": 52.33915,\"lng\": 20.69597}, {\"lat\": 52.33915,\"lng\": 20.69569}, {\"lat\": 52.33911,\"lng\": 20.69548}, {\"lat\": 52.33905,\"lng\": 20.69526}, {\"lat\": 52.33899,\"lng\": 20.69509}, {\"lat\": 52.33885,\"lng\": 20.69469}, {\"lat\": 52.3387,\"lng\": 20.69422}, {\"lat\": 52.33825,\"lng\": 20.69258}, {\"lat\": 52.33815,\"lng\": 20.69223}, {\"lat\": 52.33811,\"lng\": 20.6921}, {\"lat\": 52.33805,\"lng\": 20.69198}, {\"lat\": 52.33801,\"lng\": 20.69191}, {\"lat\": 52.33781,\"lng\": 20.69161}, {\"lat\": 52.33632,\"lng\": 20.68936}, {\"lat\": 52.3317,\"lng\": 20.68254}],\"status\": 2}";
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("delivery#123.json", context.MODE_PRIVATE));
            outputStreamWriter.write(json);
            outputStreamWriter.close();
        } catch (Exception e) {}

        //Try to get delivery
        if(application.current_delivery == null && !application.file_thread_is_running && !application.server_thread_is_running)
            getDelivery();

        if(application.current_delivery != null)
            activateMenu();
        else
            deactivateMenu();
    }

    @Override
    protected void onResume() {
        if(application.current_delivery != null)
            activateMenu();
        else
            deactivateMenu();

        if(application.current_delivery == null && !application.declined_delivery && !application.file_thread_is_running && !application.server_thread_is_running)
            getDelivery();

        if (application.current_delivery != null && application.current_delivery.state == 1) {
            startActivity(new Intent(getApplicationContext(), New_Delivery_Activity.class));
        }

        super.onResume();
    }

    public void onClickMap(View v) {
        startActivity(new Intent(getApplicationContext(), Map_Activity.class));
    }
    public void onClickDestinations(View v) {
        startActivity(new Intent(getApplicationContext(), Destinations_List_Activity.class));
    }
    public void onClickHistory(View v) {
        startActivity(new Intent(getApplicationContext(), History_Activity.class));
    }
    public void onClickDriverStatistics(View v) {
        startActivity(new Intent(getApplicationContext(), Driver_Statistics_Activity.class));
    }


    //Main application threads
    private Thread deliveryFromFile = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                application.current_delivery = new Delivery(new JSONObject(new JSON_loader(application).load("delivery")));
                activateMenu();
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                edit.putBoolean("deliveryInFile", false); edit.commit();

                //If reading from file failed try to download from server
                deliveryFromServer.start();
            }
            application.file_thread_is_running = false;
        }
    });

    private Integer getCurrentDeliveryID() {
        Rest_Get getID = new Rest_Get(preferences.getString("username", "#"), preferences.getString("password", "#"),
                new Rest_Get.AsyncResponse() {
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
            Rest_Get getDelivery = new Rest_Get(preferences.getString("username", "#"), preferences.getString("password", "#"),
                    new Rest_Get.AsyncResponse() {
                        @Override
                        public void processFinish(String result) {
                            try {
                                if (result != "ERROR") {
                                    JSONObject delivery = new JSONObject(result);
                                    application.current_delivery = new Delivery(delivery);
                                    application.current_delivery.saveDeliveryToFile(result,"", application);
                                    activateMenu();

                                    //If delivery is not yet accepted then popout
                                    if (application.current_delivery.state == 1) {
                                        startActivity(new Intent(getApplicationContext(), New_Delivery_Activity.class));
                                        deactivateMenu();
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("ERROR", e.getMessage(), e);
                                mHandler.postDelayed(thread, INTERVAL);
                            }
                        }
                    });
            try {
                Integer orderID = getCurrentDeliveryID();
                if(orderID == preferences.getInt("prevDeliveryID", -1) || orderID <= 0)
                    throw new Exception();
                else if (orderID > 0) {
                    edit.putInt("deliveryID", orderID);
                    edit.commit();

                    getDelivery.execute("orders/" + orderID);
                }

                application.server_thread_is_running = false;
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
                application.file_thread_is_running = true;
            } else {
                deliveryFromServer.start();
                application.server_thread_is_running = true;
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
                Rest_Put api = new Rest_Put(preferences.getString("username", "#"), preferences.getString("password", "#"),
                        new Rest_Put.AsyncResponse() {
                            @Override
                            public void processFinish(String result) {
                            }
                        }, application);

                for (PUT_Request put : application.puts) {
                    Log.i("PUT", put.newjson);
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