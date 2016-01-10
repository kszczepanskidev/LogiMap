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

    JSONloader(Context context) {
        this.context = context;
    }

    public String load(String jsonfile) {
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
}
