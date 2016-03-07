package hkp.logimap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

/**
 * Created by krysztal on 04.01.16.
 */
public class Id_Activity extends AppCompatActivity {
    MyApplication application;
    Context context;
    Handler mHandler;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    private final static int INTERVAL = 1000 * 30; //try to get delivery every 30s

    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        edit = preferences.edit();
        application = (MyApplication) getApplication();
        this.context = this;
        mHandler = new Handler();

        //DEBUG MODE
//        edit.putBoolean("firstRun", true);
        edit.putBoolean("deliveryInFile", false);
        edit.commit();
        //DEBUG MODE


        if (!preferences.getBoolean("firstRun", true))
            goToMenu();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_layout);
    }

    public void clickSignIn(View v) {
        Encryptor encryptor = new Encryptor();

        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        try {
            edit.putBoolean("firstRun", false);
            edit.putString("username", encryptor.encrypt(username));
            edit.putString("password", encryptor.encrypt(password));
            edit.commit();

            getDriverID();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    private void goToMenu() {
        try {
            getDeliveryThread.start();

            startActivity(new Intent(getApplicationContext(), Menu_Activity.class));

            finish();
        }catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    private void getDriverID() {
        RestGet api = new RestGet(preferences.getString("username", "#"), preferences.getString("password", "#"),
                new RestGet.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
                        try {
                            Integer temp = new JSONObject(result).getJSONArray("results").getJSONObject(0).getInt("id");
                            SharedPreferences.Editor edit = preferences.edit();
                            Log.i("TEST", temp.toString());
                            edit.putInt("driverID", temp);
                            edit.commit();
                            goToMenu();
                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage(), e);
                            edit.putInt("driverID", -1);

                            //show error about incorrect data or no internet connection
                            TextView error = (TextView) findViewById(R.id.signInError);
                            error.setVisibility(View.VISIBLE);
                            edit.putBoolean("firstRun", true);
                            edit.commit();
                        }
                    }
                });

        api.execute("driverid/");
        try {
            api.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    private Integer getCurrentDeliveryID() {
        RestGet getID = new RestGet(preferences.getString("username", "#"), preferences.getString("password", "#"),
                new RestGet.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {}
                });

        try {
            getID.execute("drivers/" + preferences.getInt("driverID", -1));

            String result = getID.get(5, TimeUnit.SECONDS);
            Integer id = new JSONObject(result).getInt("current_order");
            edit.putInt("deliveryID", id);
            edit.commit();

            return id;
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return -1;
        }
    }

    Thread deliveryFromFile = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                application.current_delivery = new Delivery(new JSONObject(new JSONloader(application).load("delivery")));
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                edit.putBoolean("deliveryInFile", false);
                edit.commit();
            }
        }
    });

    private void getCurrentDelivery(final Runnable thread) {
        Integer orderID;

        if (preferences.getBoolean("deliveryInFile", false)) {
            deliveryFromFile.start();
        } else {
            RestGet getDelivery = new RestGet(preferences.getString("username", "#"), preferences.getString("password", "#"),
                    new RestGet.AsyncResponse() {
                        @Override
                        public void processFinish(String result) {
                            try {
                                if (result != "ERROR") {
                                    application.current_delivery = new Delivery(new JSONObject(result));
                                    application.current_delivery.saveDeliveryToFile(result, application);

                                    //If delivery is not yet accepted then popout
                                    if(application.current_delivery.state == 1) {//TODO notaccepted state number
                                        startActivity(new Intent(getApplicationContext(), New_Delivery_Activity.class));
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("ERROR", e.getMessage(), e);
                            }
                        }
                    });
                try {
                    orderID = getCurrentDeliveryID();

                    if (orderID > 0)
                        getDelivery.execute("orders/" + orderID);
                    else if (orderID <=0 || application.current_delivery.state == 4)//TODO declined delivery
                        throw new Exception();
                } catch (Exception e) {
                    mHandler.postDelayed(thread, INTERVAL);
                }
        }
    }

    Thread getDeliveryThread = new Thread(new Runnable() {
        @Override
        public void run() {
            getCurrentDelivery(this);
        }
    });
}
