package hkp.logimap;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by krysztal on 07.12.15.
 */
public class JSON_loader {
    Context context;
    String result;

    JSON_loader(Context context) {
        this.context = context;
    }

    public String load(String jsonfile) {
        try {
            InputStream is = context.openFileInput(jsonfile + ".json");

            if ( is != null ) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = br.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                is.close();
                result = stringBuilder.toString();
            }
            return result;
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }
}
