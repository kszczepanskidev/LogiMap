package hkp.logimap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class GetDelivery_Activity extends AppCompatActivity {
    MyApplication application;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (MyApplication) getApplication();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getdelivery_layout);
        setTitle("Delivery GET test");

        TextView tv = (TextView) findViewById(R.id.getdeliverytext);
        temp = application.current_delivery.driver.name + " " + application.current_delivery.driver.surname + "\n";

        temp += application.current_delivery.vehicle.reg_number + " " + application.current_delivery.vehicle.brand + " " +
                application.current_delivery.vehicle.model + " " + application.current_delivery.vehicle.category + "\n";

        temp += application.current_delivery.route.length.toString() + "\n";
        for(int i = 0; i < application.current_delivery.route.locations.size(); ++i) {
            temp += i+1 + ") " + application.current_delivery.route.locations.get(i).name + " " + application.current_delivery.route.locations.get(i).address + " " +
                    application.current_delivery.route.locations.get(i).latitude + " " + application.current_delivery.route.locations.get(i).longtitude + "\n";
        }
        temp += "\n";

        for(int i = 0; i < application.current_delivery.packages.size(); ++i) {
            temp += i+1 + ") " + application.current_delivery.packages.get(i).code + " " + application.current_delivery.packages.get(i).state + "\n";
            temp += application.current_delivery.packages.get(i).destination.name + " " + application.current_delivery.packages.get(i).destination.address + " " +
                    application.current_delivery.packages.get(i).destination.latitude + " " + application.current_delivery.packages.get(i).destination.longtitude + "\n";
            temp += "\n";
        }



        tv.setText(temp);
    }

}
