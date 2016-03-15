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

    Driver(JSONObject driver) {
        try {
            this.name = driver.getString("name");
            this.surname = driver.getString("surname");
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    public JSONObject getJSON() {
        JSONObject driver_json = new JSONObject();
        try {
            driver_json.put("name", this.name);
            driver_json.put("surname", this.surname);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        } finally {
            return driver_json;
        }
    }
}
