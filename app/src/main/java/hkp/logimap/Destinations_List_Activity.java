package hkp.logimap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Destinations_List_Activity extends AppCompatActivity{
//    Delivery delivery;
    MyApplication application;
    JSONloader json_loader;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (MyApplication) getApplication();
        application.current_delivery = new Delivery();
        json_loader = new JSONloader(this);

        // JSON load TEST
        try {
            json = new JSONObject(json_loader.loadFromFile("destinations_test"));

            JSONArray destins = json.getJSONArray("destination");

            for (int i=0; i< destins.length();++i) {
                Destination d = new Destination();
                d.name = destins.getJSONObject(i).getString("name");
                application.current_delivery.destinations.add(d);
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
        // JSON load TEST

        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_list_layout);
        setTitle("Delivery #" + application.current_delivery.id);

        //Destinations list
        ExpandableListView lv = (ExpandableListView) findViewById(R.id.destinations_list);
        ExpandableDestinationListAdapter adapter = new ExpandableDestinationListAdapter(this, application.current_delivery.destinations);
        lv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
