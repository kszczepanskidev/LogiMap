package hkp.logimap;

import android.app.Application;
import android.util.Log;

/**
 * Created by krysztal on 05.01.16.
 */
public class MyApplication extends Application {
    Delivery current_delivery;

    @Override
    public void onCreate() {
        super.onCreate();
//        current_delivery = new Delivery();

        //TODO: Check if delivery is assigned and downloaded
    }
}
