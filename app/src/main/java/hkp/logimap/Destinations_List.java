package hkp.logimap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Destinations_List extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<String> list = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.destinations__list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), Package_List.class);
                ListView lv = (ListView) findViewById(R.id.listView);
                i.putExtra("destination", lv.getItemAtPosition(position).toString());

                startActivity(i);
            }
        });
        list.add("Sosnowiec");
        list.add("Moskwa");
        list.add("Hamburg");

        lv.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);

        Intent intent = new Intent();
        intent.putExtra("name", position);
        intent.putExtra("id", id);
        startActivity(intent);
    }

}
