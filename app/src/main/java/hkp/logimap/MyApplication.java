package hkp.logimap;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by krysztal on 05.01.16.
 */
public class MyApplication extends Application {
    Delivery current_delivery;
    ArrayList<PUTRequest> puts;

    @Override
    public void onCreate() {
        puts = new ArrayList<>();
        current_delivery = null;

        super.onCreate();
    }
}