package hkp.logimap;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class Menu_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void onClickMap(View v) {
        Intent i = new Intent(getApplicationContext(), Maps_Activity.class);
        startActivity(i);
    }
    public void onClickDestinations(View v) {
        startActivity(new Intent(getApplicationContext(), Destinations_List_Activity.class));
    }
    public void onClickRestTest(View v) {
        startActivity(new Intent(getApplicationContext(), RESTtestView_Activity.class));
    }

}
