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
        final TextView tv = (TextView) findViewById(R.id.statisticsView);
        SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        Rest_Get api = new Rest_Get(preferences.getString("username", "#"), preferences.getString("password", "#"),
                new Rest_Get.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
                        try {
                            DriverStats driverSt = new DriverStats(new JSONObject(result));
                            text = String.format(getResources().getString(R.string.driver_statistics),
                                    driverSt.km_done, driverSt.orders_done, driverSt.pkg_delivered, driverSt.pkg_delayed);

                            tv.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText(text);
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