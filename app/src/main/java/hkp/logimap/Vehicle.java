package hkp.logimap;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by krysztal on 09.01.16.
 */
public class Vehicle {
    String reg_number, brand, model, category;

    Vehicle(JSONObject vehicle) {
        try {
            this.reg_number = vehicle.getString("reg_number");
            this.brand = vehicle.getString("brand");
            this.model = vehicle.getString("model");
            this.category = vehicle.getString("category");
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    public JSONObject getJSON() {
        JSONObject vehicle_json = new JSONObject();
        try {
            vehicle_json.put("reg_number", this.reg_number);
            vehicle_json.put("brand", this.brand);
            vehicle_json.put("model", this.model);
            vehicle_json.put("category", this.category);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        } finally {
            return vehicle_json;
        }
    }
}
