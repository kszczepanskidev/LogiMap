package hkp.logimap;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by krysztal on 07.12.15.
 */
public class JSONloader {
    Context context;
    SharedPreferences preferences;

    JSONloader(Context context) {
        this.context = context;
        this.preferences = this.context.getSharedPreferences("sharedPrefs", this.context.MODE_PRIVATE);
    }

    public String loadFromFile(String jsonfile) {
        try {
            InputStream is = context.getAssets().open(jsonfile + ".json");

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            return new String(buffer, "UTF-8");
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    public String loadFromHttp(String url) {
        RestGet api = new RestGet(preferences.getString("username", "#"), preferences.getString("password", "#"),
                new RestGet.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
//                        return result;
                    }
                });
        return null;
    }
}
