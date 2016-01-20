package hkp.logimap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;

public class Change_State_Activity extends AppCompatActivity {
    MyApplication application;

    ArrayAdapter<String> adapter;
    ArrayList<String> states;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (MyApplication) getApplication();
        Intent i = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_state_layout);
        setTitle(i.getStringExtra("packageCode"));

        states = new ArrayList<>();
        states.add("a");states.add("b");states.add("c");

        //States list
        ListView lv = (ListView) findViewById(R.id.state_list);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                states);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //TODO make POST
            }
        });
    }
}
