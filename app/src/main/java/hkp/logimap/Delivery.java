package hkp.logimap;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by kryształ on 03.12.2015.
 */
public class Delivery implements Serializable{
    Driver driver;
    Vehicle vehicle;
    Route route;
    Integer state;
    ArrayList<Package> packages;
    HashMap<Integer, Location> locations;
    Boolean finished;
    Integer id;


    Delivery(JSONObject delivery) {
        packages  = new ArrayList<>();
        locations = new HashMap<>();

        try {
            this.id = delivery.getInt("id");
            this.driver = new Driver(delivery.getJSONObject("driver"));
            this.vehicle = new Vehicle(delivery.getJSONObject("vehicle"));
            this.route = new Route(delivery.getJSONObject("route"), locations);
            this.state = delivery.getInt("status");
            this.finished = false;

            JSONArray _packages = delivery.getJSONArray("package");

            for(int i=0; i < _packages.length(); ++i) {
                this.packages.add(new Package(_packages.getJSONObject(i)));
            }

            //Minimum deadline for packages in location
            ArrayList<Time> times = new ArrayList<>();
            for(Location l : locations.values()) {
                for (Package p : packages)
                    if (p.destination == l.id)
                        times.add(p.deadline);
                Collections.sort(times);
                l.deadline = times.get(0);
                times.clear();
            }

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    public Package getPackage(Integer id) {
        for(Package p: this.packages)
            if(p.id == id)
                return p;

        return  null;
    }

    public void checkLocations(MyApplication application) {
        this.finished = true;

        for(Location l : this.locations.values())
            if(!l.finished) {
                this.finished = false;
                break;
            }

        if(this.finished) {
            this.state = 30; //TODO finished delivery state

            String newjson = getJSON();
            application.puts.add(0, new PUTRequest("orders/" + this.id, newjson));

            saveDeliveryToFile("", application);
        }
    }

    public String getJSON() {
        try {
            JSONObject delivery_json = new JSONObject();
            delivery_json.put("id", this.id);

                JSONObject driver_json = new JSONObject();
                driver_json.put("name", this.driver.name);
                driver_json.put("surname", this.driver.surname);
            delivery_json.put("driver", driver_json);

                JSONObject vehicle_json = new JSONObject();
                vehicle_json.put("reg_number", this.vehicle.reg_number);
                vehicle_json.put("brand", this.vehicle.brand);
                vehicle_json.put("model", this.vehicle.model);
                vehicle_json.put("category", this.vehicle.category);
            delivery_json.put("vehicle", vehicle_json);

                JSONObject route_json = new JSONObject();
                route_json.put("length", this.route.length);
                    JSONArray route_locations_json = new JSONArray();
                    for(Location l : this.locations.values()) {
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
            delivery_json.put("route", route_json);

                JSONArray packages_json = new JSONArray();
                for(Package p : this.packages) {
                    JSONObject package_json = new JSONObject();
                    package_json.put("id", p.id);
                    package_json.put("code", p.code);
                    package_json.put("status", p.state);
                    package_json.put("location", p.destination);
                    package_json.put("deliver_before", p.deadline.toString().substring(0,5));

                    packages_json.put(package_json);
                }
            delivery_json.put("package", packages_json);

            return delivery_json.toString();
        }catch (Exception e) {
            Log.e("ERROR", e.getMessage(),e);
            return null;
        }
    }


    public void saveDeliveryToFile(String json, Context context) {
        if(json == "")
            json = getJSON();
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("delivery.json", context.MODE_PRIVATE));
            outputStreamWriter.write(json);
            outputStreamWriter.close();
        }
        catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }
}
