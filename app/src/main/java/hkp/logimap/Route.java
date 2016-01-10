package hkp.logimap;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by krysztal on 10.01.16.
 */
public class Route {
    Double length;
    ArrayList<Location> locations;

    Route(JSONObject route) {
        locations = new ArrayList<>();
        try {
            this.length = Double.parseDouble(route.getString("length"));
            JSONArray _locations = route.getJSONArray("locations");

            for(int i = 0; i < _locations.length(); ++i) {
                JSONObject _loc = (_locations.getJSONObject(i));
//                locations.add(_loc.getInt("order"), new Location(_loc.getJSONObject("location")));
                locations.add(new Location(_loc.getJSONObject("location")));
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }

    }
}
