package hkp.logimap;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by krysztal on 09.01.16.
 */
public class Driver {
    Integer id;
    String name, surname;
//    ArrayList<String> categories;

    Driver(JSONObject driver) {
        try {
            this.name = driver.getString("name");
            this.surname = driver.getString("surname");
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
        //TODO: json categories to arraylist
    }
}
