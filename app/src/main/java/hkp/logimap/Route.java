package hkp.logimap;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by krysztal on 10.01.16.
 */
public class Route {
    Double length;

    Route(JSONObject route, HashMap<Integer, Location> locations) {
        try {
            this.length = Double.parseDouble(route.getString("length"));
            JSONArray _locations = route.getJSONArray("locations");

            for(int i = 0; i < _locations.length(); ++i) {
                JSONObject _loc = (_locations.getJSONObject(i));
                Location _location = new Location(_loc.getJSONObject("location"), _loc.getInt("order"));
                locations.put(_location.id, _location);
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }

    }
}
