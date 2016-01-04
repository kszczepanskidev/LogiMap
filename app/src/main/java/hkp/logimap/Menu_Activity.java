package hkp.logimap;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Menu_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Encryptor encryptor = new Encryptor();

        SharedPreferences preferences = getSharedPreferences("sharedPrefs", 0);
        TextView tv = (TextView) findViewById(R.id.textView);

        String username = "", password = "";
        try {
            username = encryptor.decrypt(preferences.getString("username", "#"));
            password = encryptor.decrypt(preferences.getString("password", "#"));
        }catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }

        tv.setText("Welcome " + username + ":" + password + "!");
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
