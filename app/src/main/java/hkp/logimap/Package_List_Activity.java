package hkp.logimap;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class Package_List_Activity extends AppCompatActivity {
    Integer destination;
    MyApplication application;
    ArrayList<Package> packages;
    ExpandablePackageListAdapter adapter;
    Delivery delivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        packages = new ArrayList<>();

        application = (MyApplication) getApplication();
        Intent i = getIntent();
        destination = i.getIntExtra("destinationID", -1);

        if(application.history_delivery != null)
            delivery = application.history_delivery;
        else
            delivery = application.current_delivery;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_list_layout);
        setTitle(delivery.locations.get(destination).name);

        //Prepare packages for this destination
        for(Package p : delivery.packages)
                if(p.destination == destination)
                    packages.add(p);

        //Expandable list of packages with details
        ExpandableListView lv = (ExpandableListView) findViewById(R.id.packages_list);
        adapter = new ExpandablePackageListAdapter(this, application, packages, delivery.history);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
