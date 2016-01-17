package hkp.logimap;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

public class DriverStatistics_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_statistics_layout);
        runREST();

    }

    public void runREST() {
        final TextView tv = (TextView) findViewById(R.id.statisticsView);
        SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        RestGet api = new RestGet(preferences.getString("username", "#"), preferences.getString("password", "#"),
                new RestGet.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
                        try {
                            DriverStats driverSt = new DriverStats(new JSONObject(result));
                            String text = String.format(getResources().getString(R.string.driver_statistics),
                                    driverSt.km_done, driverSt.orders_done, driverSt.pkg_delivered, driverSt.pkg_delayed);
                            tv.setText(text);
                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage(), e);
                        }
                    }
                });

        api.execute("driverstats/1/");  //TODO: replace "1" with driverId
    }
}