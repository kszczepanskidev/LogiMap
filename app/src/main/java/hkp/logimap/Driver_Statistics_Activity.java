package hkp.logimap;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

public class Driver_Statistics_Activity extends AppCompatActivity {
    SharedPreferences preferences;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_statistics_layout);
        preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        runREST();

    }

    public void runREST() {
        final TextView driver_name = (TextView) findViewById(R.id.driver_name);
        final TextView v_km = (TextView) findViewById(R.id.value_kmdone);
        final TextView v_orders = (TextView) findViewById(R.id.value_orders);
        final TextView v_packages = (TextView) findViewById(R.id.value_packages);
        final TextView v_delayed = (TextView) findViewById(R.id.value_delayed);

        SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        Rest_Get api = new Rest_Get(preferences.getString("username", "#"), preferences.getString("password", "#"),
                new Rest_Get.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
                        try {
                            final DriverStats driverSt = new DriverStats(new JSONObject(result));

                            driver_name.post(new Runnable() {
                                @Override
                                public void run() {
                                    driver_name.setText(driverSt.name);
                                }
                            });
                            v_km.post(new Runnable() {
                                @Override
                                public void run() {
                                    v_km.setText(driverSt.km_done.toString());
                                }
                            });
                            v_orders.post(new Runnable() {
                                @Override
                                public void run() {
                                    v_orders.setText(driverSt.orders_done.toString());
                                }
                            });
                            v_packages.post(new Runnable() {
                                @Override
                                public void run() {
                                    v_packages.setText(driverSt.pkg_delivered.toString());
                                }
                            });
                            v_delayed.post(new Runnable() {
                                @Override
                                public void run() {
                                    v_delayed.setText(driverSt.pkg_delayed.toString());
                                }
                            });
                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage(), e);
                        }
                    }
                });

        api.execute("driverstats/" + preferences.getInt("driverID", -1));
    }
}