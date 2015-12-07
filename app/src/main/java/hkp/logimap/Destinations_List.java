package hkp.logimap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Destinations_List extends AppCompatActivity{
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
                delivery.destinations.add(destins.getJSONObject(i).getString("name"));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();        }

        // JSON load TEST


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, delivery.destinations);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.destinations__list);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Delivery #" + delivery.id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Destinations list
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), Package_List.class);
                ListView lv = (ListView) findViewById(R.id.listView);
                i.putExtra("destination", lv.getItemAtPosition(position).toString());
                i.putExtra("delivery", delivery);

                startActivity(i);
            }
        });
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
