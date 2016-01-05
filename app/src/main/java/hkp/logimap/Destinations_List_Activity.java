package hkp.logimap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Destinations_List_Activity extends AppCompatActivity{
    Delivery delivery;
    JSONloader json_loader = new JSONloader();
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        delivery = new Delivery();

        // JSON load TEST
        try {
            json = new JSONObject(json_loader.loadFromFile(getAssets().open("destinations_test.json")));

            JSONArray destins = json.getJSONArray("destination");

            for (int i=0; i< destins.length();++i) {
                Destination d = new Destination();
                d.name = destins.getJSONObject(i).getString("name");
                delivery.destinations.add(d);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();        }
        // JSON load TEST

        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_list_layout);
        setTitle("Delivery #" + delivery.id);

        //Destinations list
        ExpandableListView lv = (ExpandableListView) findViewById(R.id.destinations_list);
        ExpandableDestinationListAdapter adapter = new ExpandableDestinationListAdapter(this, delivery.destinations);
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
