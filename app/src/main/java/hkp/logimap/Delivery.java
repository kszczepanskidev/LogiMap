package hkp.logimap;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by kryształ on 03.12.2015.
 */
public class Delivery implements Serializable{
    Driver driver;
    Vehicle vehicle;
    Route route;
    ArrayList<Package> packages;
    HashMap<Integer, Location> locations;


    Delivery(JSONObject delivery) {
        packages  = new ArrayList<>();
        locations = new HashMap<>();

        try {
            this.driver = new Driver(delivery.getJSONObject("driver"));
            this.vehicle = new Vehicle(delivery.getJSONObject("vehicle"));
            this.route = new Route(delivery.getJSONObject("route"), locations);

            JSONArray _packages = delivery.getJSONArray("package");

            for(int i=0; i < _packages.length(); ++i) {
                this.packages.add(new Package(_packages.getJSONObject(i)));
            }

            //Minimum deadline for packages in location
            ArrayList<Time> times = new ArrayList<>();
            for(Location l : new ArrayList<>(locations.values())) {
                for (Package p : packages)
                    if (p.destination == l.id)
                        times.add(p.deadline);
                Collections.sort(times);
                l.deadline = times.get(0);
            }

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }
}
