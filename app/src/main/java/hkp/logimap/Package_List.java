package hkp.logimap;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Package_List extends AppCompatActivity {
    Delivery delivery;
    List<String> package_ids;              //listDataHeader
    HashMap<String, List<String>> package_items;   //listDataChild

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        delivery = (Delivery)i.getSerializableExtra("delivery");
        create_package_list();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.package__list);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(i.getStringExtra("destination") + ": " + delivery.load.size() + " packages");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Expandable list of packages and items
        ExpandableListView lv = (ExpandableListView) findViewById(R.id.packages_list);
        ExpandableListAdapter adapter = new ExpandableListAdapter(this, package_ids, package_items);
        lv.setAdapter(adapter);
    }

    private void create_package_list() {
        package_ids = new ArrayList<>();
        package_items = new HashMap<>();

        List<String> pack_items = new ArrayList<>();

        for(Package p : delivery.load)
            package_ids.add("Package #" + p.id.toString() + " " + p.items.size() + " items");


        package_ids.add("Test packalunga with zwei itemunga");
        List<String> temp = new ArrayList<>();
        temp.add("dupa");
        temp.add("cycki");
        package_items.put(package_ids.get(5), temp);

        for(int i = 0; i<5;++i) {
            for (Item it : delivery.load.get(i).items) {
                pack_items.add(it.id.toString());
            }
            package_items.put(package_ids.get(i), pack_items);

            pack_items.clear();
        }
    }

}
