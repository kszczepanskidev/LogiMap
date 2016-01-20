package hkp.logimap;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * Created by krysztal on 05.01.16.
 */
public class MyApplication extends Application {
    Delivery current_delivery;

    @Override
    public void onCreate() {
        super.onCreate();
        current_delivery = null;
    }
}