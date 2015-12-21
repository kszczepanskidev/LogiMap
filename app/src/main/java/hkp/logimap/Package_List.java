package hkp.logimap;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

public class Package_List extends AppCompatActivity {
    Delivery delivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        delivery = (Delivery)i.getSerializableExtra("delivery");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.package__list);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(i.getStringExtra("destination") + ": " + delivery.load.size() + " packages");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Expandable list of packages with details
        ExpandableListView lv = (ExpandableListView) findViewById(R.id.packages_list);
        ExpandablePackageListAdapter adapter = new ExpandablePackageListAdapter(this, delivery.load);
        lv.setAdapter(adapter);
    }
}
