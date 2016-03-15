package hkp.logimap;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
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

    public JSONObject getJSON(Collection<Location> locations) {
        JSONObject route_json = new JSONObject();
        JSONArray route_locations_json = new JSONArray();
        try {
            route_json.put("length", this.length);
            for(Location l : locations) {
                JSONObject locations_json = new JSONObject();
                locations_json.put("order", l.order);

                JSONObject location_json = new JSONObject();
                location_json.put("id", l.id);
                location_json.put("name", l.name);
                location_json.put("address", l.address);
                location_json.put("latitude", l.latitude);
                location_json.put("longitude", l.longtitude);
                locations_json.put("location", location_json);

                route_locations_json.put(locations_json);
            }
            route_json.put("locations", route_locations_json);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        } finally {
            return route_json;
        }
    }
}
