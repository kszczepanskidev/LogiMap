package hkp.logimap;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kryształ on 03.12.2015.
 */
public class Delivery implements Serializable{
    Driver driver;
    Vehicle vehicle;
    Route route;
    ArrayList<Package> packages;


    Delivery(JSONObject delivery) {
        packages = new ArrayList<>();

        try {
            this.driver = new Driver(delivery.getJSONObject("driver"));
            this.vehicle = new Vehicle(delivery.getJSONObject("vehicle"));
            this.route = new Route(delivery.getJSONObject("route"));

            JSONArray _packages = delivery.getJSONArray("package");

            for(int i=0; i < _packages.length(); ++i) {
                this.packages.add(new Package(_packages.getJSONObject(i)));
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }
}
