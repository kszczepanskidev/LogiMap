package hkp.logimap;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Menu_Activity extends AppCompatActivity {
    MyApplication application;

    Thread waitforjson = new Thread(new Runnable() {
        @Override
        public void run() {
            final Button b = (Button) findViewById(R.id.getdelivery_button);
            while(application.current_delivery == null);
            try {
                b.post(new Runnable() {
                    @Override
                    public void run() {
                        b.setEnabled(true);
                    }
                });
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (MyApplication) getApplication();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        waitforjson.start();
    }

    public void onClickMap(View v) {
        startActivity(new Intent(getApplicationContext(), Maps_Activity.class));
    }
    public void onClickDestinations(View v) {
        startActivity(new Intent(getApplicationContext(), Destinations_List_Activity.class));
    }
    public void onClickHistory(View v) {
        startActivity(new Intent(getApplicationContext(), RESTtestView_Activity.class));
    }
    public void onClickGetDelivery(View v) {
        startActivity(new Intent(getApplicationContext(), GetDelivery_Activity.class));
    }

}
