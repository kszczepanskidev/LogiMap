package hkp.logimap;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by krysztal on 05.01.16.
 */
public class MyApplication extends Application {
    Delivery current_delivery, history_delivery;
    ArrayList<PUT_Request> puts;
    ArrayList<String> package_states;
    Boolean file_thread_is_running, server_thread_is_running;

    @Override
    public void onCreate() {
        puts = new ArrayList<>();
        current_delivery = null;
        history_delivery = null;
        file_thread_is_running = false;
        server_thread_is_running = false;

        package_states = new ArrayList<>();
        package_states.add("Loaded");
        package_states.add("Delivered");
        package_states.add("Not delivered");
        package_states.add("Delayed");
        package_states.add("Damaged");

        super.onCreate();
    }
}