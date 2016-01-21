package hkp.logimap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

public class History extends AppCompatActivity {


    String[] mobileArray = {
            "Poznan - Wroclaw 18.02.2015",
            "Grenoble - Wroclaw 14.02.2015",
            "Montreal - Detroid 12.12.2014",
         };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Object child = parent.getItemAtPosition(position);
                Context context = getApplicationContext();
                String text = child.toString();

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                i.putExtra("Route_id",text);
                startActivity(i);


            }
        });
    }



}
