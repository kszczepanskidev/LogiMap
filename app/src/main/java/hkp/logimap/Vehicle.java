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
}
