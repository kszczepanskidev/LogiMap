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
    Integer user_id;

    @Override
    public void onCreate() {
        super.onCreate();
        //TODO: Get user id

        try {
            SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
            final MyApplication application= this;

                if (preferences.getBoolean("deliveryFile", false)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                current_delivery = new Delivery(new JSONObject(new JSONloader(application).load("delivery.json")));
                            } catch (Exception e) {
                                Log.e("ERROR", e.getMessage(), e);
                            }
                        }
                    }).start();
                } else {
                    //TODO: Check if delivery is assigned

                    RestGet api = new RestGet(preferences.getString("username", "#"), preferences.getString("password", "#"),
                            new RestGet.AsyncResponse() {
                                @Override
                                public void processFinish(String result) {
                                    try {
                                        current_delivery = new Delivery(new JSONObject(result));
                                    } catch (Exception e) {
                                        Log.e("ERROR", e.getMessage(), e);
                                    }
                                }
                            });

                    api.execute("orders/1");
                    //TODO: Save delivery to file
                }
        }catch (Exception e) {
            Log.e("ERROR" , e.getMessage(), e);
        }

    }
}