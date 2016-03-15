package hkp.logimap;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by krysztal on 05.01.16.
 */
public class MyApplication extends Application {
    Delivery current_delivery;
    ArrayList<PUTRequest> puts;
    ArrayList<String> package_states;

    @Override
    public void onCreate() {
        puts = new ArrayList<>();
        current_delivery = null;

        package_states = new ArrayList<>();
        package_states.add("Loaded");
        package_states.add("Delivered");
        package_states.add("Not delivered");
        package_states.add("Delayed");
        package_states.add("Damaged");

        super.onCreate();
    }
}