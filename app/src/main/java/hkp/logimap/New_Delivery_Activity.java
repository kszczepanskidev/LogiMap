package hkp.logimap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class New_Delivery_Activity extends AppCompatActivity {
    MyApplication application;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (MyApplication) getApplication();
        preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        edit = preferences.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_delivery_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle("New delivery assigned");

        setDeliveryData();
    }

    private void setDeliveryData() {
        TextView head = (TextView) findViewById(R.id.header);
        TextView tv = (TextView) findViewById(R.id.delivery);

        head.append(application.current_delivery.id.toString());

        tv.setText("Number of packages: " + application.current_delivery.packages.size() + "\nLocations:\n");
        for (Location l : application.current_delivery.locations.values())
            tv.append("\t\t" + l.name + " - " + l.deadline + "\n");
        tv.append("Route Length: " + application.current_delivery.route.length);
    }

    public void clickAccept(View v){
        application.current_delivery.state = 2;
        updateDelivery();
        this.finish();
    }
    public void clickDecline(View v){
        application.current_delivery.state = 4;
        updateDelivery();

        edit.putInt("prevDeliveryID", application.current_delivery.id);
        edit.commit();
        application.current_delivery = null;

        this.finish();
    }

    private void updateDelivery() {
        String json = application.current_delivery.getJSON();
        application.puts.add(0, new PUTRequest("orders/" + application.current_delivery.id, json));
    }

}
