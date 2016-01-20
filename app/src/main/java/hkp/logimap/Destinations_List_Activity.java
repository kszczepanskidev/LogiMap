package hkp.logimap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import java.util.ArrayList;

public class Destinations_List_Activity extends AppCompatActivity{
    MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (MyApplication) getApplication();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_list_layout);
        setTitle("Destinations");

        //Destinations list
        ExpandableListView lv = (ExpandableListView) findViewById(R.id.destinations_list);
//        ExpandableDestinationListAdapter adapter = new ExpandableDestinationListAdapter(this, application.current_delivery.route.locations);
        ExpandableDestinationListAdapter adapter = new ExpandableDestinationListAdapter(this, new ArrayList<Destination>());
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
