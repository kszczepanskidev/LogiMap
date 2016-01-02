package hkp.logimap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Id extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);
    }
    public void onClickMap(View v) {
        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(i);
    }
    public void onClickDestinations(View v) {
        startActivity(new Intent(getApplicationContext(), Destinations_List.class));
    }
    public void onClickRestTest(View v) {
        startActivity(new Intent(getApplicationContext(), RESTtestView.class));
    }

}
