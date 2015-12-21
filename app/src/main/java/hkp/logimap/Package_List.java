package hkp.logimap;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

public class Package_List extends AppCompatActivity {
    Destination destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        destination = (Destination)i.getSerializableExtra("destination");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_list);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(destination.name + ": " + destination.packages.size() + " packages");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Expandable list of packages with details
        ExpandableListView lv = (ExpandableListView) findViewById(R.id.packages_list);
        ExpandablePackageListAdapter adapter = new ExpandablePackageListAdapter(this, destination.packages);
        lv.setAdapter(adapter);
    }
}
