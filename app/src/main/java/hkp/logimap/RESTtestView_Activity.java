package hkp.logimap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by krysztal on 02.01.16.
 */
public class RESTtestView_Activity extends AppCompatActivity{
    String testResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resttestlay);
    }

    public void runREST(View v) {
        final TextView tv = (TextView) findViewById(R.id.responseView);
        SharedPreferences preferences = getSharedPreferences("sharedPrefs", 0);
        RestGet api = new RestGet(preferences.getString("username", "#"), preferences.getString("password", "#"),
                new RestGet.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
                        tv.setText(result);
                    }
                });


        api.execute("vehicles/1");
    }
}
