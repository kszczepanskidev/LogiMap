package hkp.logimap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class History_Activity extends AppCompatActivity {
    Context context;
    MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);

        context = this;
        application = (MyApplication) getApplication();

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.history_list_layout, getRoutes());

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Object child = parent.getItemAtPosition(position);
                String order_id = child.toString();

                Toast toast = Toast.makeText(context, order_id, Toast.LENGTH_SHORT);
                toast.show();

                try {
                    application.history_delivery = new Delivery(new JSONObject(new JSON_loader(application).load("delivery" + order_id)));
                    application.history_delivery.history = true;
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                }

                startActivity(new Intent(getApplicationContext(), Map_Activity.class));
            }
        });
    }

    @Override
    protected void onStop() {
        application.history_delivery = null;
        super.onStop();
    }

    public ArrayList<String> getRoutes() {
        ArrayList<String> routeArray = new ArrayList<>();

        for (String strFile : this.getFilesDir().list()) {
            if(strFile.contains("delivery") && strFile.contains(".json")) {
                strFile=strFile.substring(8 ,strFile.length() - 5);
                routeArray.add(strFile);
            }
        }

        return routeArray;
    }

}
