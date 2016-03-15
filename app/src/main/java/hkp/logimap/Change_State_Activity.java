package hkp.logimap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

public class Change_State_Activity extends AppCompatActivity {
    MyApplication application;
    SharedPreferences preferences;

    ArrayAdapter<String> adapter;

    Integer packageID, locationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (MyApplication) getApplication();
        preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        Intent i = getIntent();
        packageID = i.getIntExtra("packageID", -1);
        locationID = i.getIntExtra("locationID", -1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_state_layout);
        setTitle(i.getStringExtra("packageCode"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //States list
        ListView lv = (ListView) findViewById(R.id.state_list);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                application.package_states);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                updateStatus(position + 1);
                application.current_delivery.locations.get(locationID).checkPackages(application);
            }
        });
    }

    public void updateStatus(Integer status) {
        Package p = application.current_delivery.getPackage(packageID);
        JSONObject package_json = new JSONObject();

        try {
            package_json.put("id", p.id);
            package_json.put("code", p.code);
            package_json.put("status", status);
            package_json.put("location", p.destination);
            package_json.put("deliver_before", p.deadline.toString().substring(0,5));
        }catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
        application.puts.add(0, new PUTRequest("packages/" + packageID, package_json.toString()));

        p.state = status;

        application.current_delivery.saveDeliveryToFile("", "delivery" + application.current_delivery.id, this);

        this.finish();
    }
}
