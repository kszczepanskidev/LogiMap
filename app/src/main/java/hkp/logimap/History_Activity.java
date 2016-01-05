package hkp.logimap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class History_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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

}
