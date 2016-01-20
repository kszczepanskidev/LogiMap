package hkp.logimap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

/**
 * Created by krysztal on 04.01.16.
 */
public class Id_Activity extends AppCompatActivity {
    MyApplication application;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        edit = preferences.edit();
        application = (MyApplication) getApplication();

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
        goToMenu();
    }

    private void goToMenu() {
        getCurrentDelivery();

        startActivity(new Intent(getApplicationContext(), Menu_Activity.class));
        try {
            deliveryFromFile.join();
            finish();
        } catch (Exception e) {
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
                            edit.putInt("driverID", temp);
                            edit.commit();
                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage(), e);
                        }
                    }
                });

        api.execute("driverid/");
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
            return (new JSONObject(result).getInt("current_order"));
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
                Log.i("TEST", "After load from file: " +  application.current_delivery.toString());
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                edit.putBoolean("deliveryInFile", false);
                edit.commit();
            }
        }
    });

    private void getCurrentDelivery() {
        Integer orderID;

        if (preferences.getBoolean("deliveryInFile", false)) {
            Log.i("TEST", "Load from file");
            deliveryFromFile.start();
        } else {
            RestGet getDelivery = new RestGet(preferences.getString("username", "#"), preferences.getString("password", "#"),
                    new RestGet.AsyncResponse() {
                        @Override
                        public void processFinish(String result) {
                            try {
                                if (result != "ERROR") {
                                    application.current_delivery = new Delivery(new JSONObject(result));
                                    saveDeliveryToFile(result);
                                } else
                                    Log.i("GetOrderERROR", result);
                            } catch (Exception e) {
                                Log.e("ERROR", e.getMessage(), e);
                            }
                        }
                    });
            orderID = getCurrentDeliveryID();

            if (orderID != -1)
                getDelivery.execute("orders/" + orderID);
        }
    }

    private void saveDeliveryToFile(String json) {
        Log.i("TEST", "Saving to file");
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("delivery.json", MODE_PRIVATE));
            outputStreamWriter.write(json);
            outputStreamWriter.close();
        }
    catch (Exception e) {
        Log.e("ERROR", e.getMessage(), e);
    }
        edit.putBoolean("deliveryInFile", true);
        edit.commit();
    }
}
