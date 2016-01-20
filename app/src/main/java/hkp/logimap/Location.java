package hkp.logimap;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by krysztal on 09.01.16.
 */
public class Location {
    String name, address;
    Double latitude, longtitude;
    Integer id;

    Location(JSONObject location) {
        try {
            this.id = location.getInt("id");
            this.name = location.getString("name");
            this.address = location.getString("address");
            this.latitude = Double.parseDouble(location.getString("latitude"));
            this.longtitude = Double.parseDouble(location.getString("longitude"));
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }

    }
}
