package hkp.logimap;

import android.util.Log;

import org.json.JSONObject;

import java.sql.Time;

/**
 * Created by krysztal on 09.01.16.
 */
public class Location {
    String name, address;
    Double latitude, longtitude;
    Integer id, order;
    Time deadline;
    Boolean shortDeadline, finished;

    Location(JSONObject location, Integer order) {
        try {
            this.finished = false;
            this.shortDeadline = false;
            this.id = location.getInt("id");
            this.name = location.getString("name");
            this.address = location.getString("address");
            this.latitude = Double.parseDouble(location.getString("latitude"));
            this.longtitude = Double.parseDouble(location.getString("longitude"));
            this.order = order;
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    public void checkPackages(MyApplication application) {
        this.finished = true;

        for(Package p : application.current_delivery.packages)
            if (p.destination == this.id)
                if (p.state != "3") { //TODO finished package state
                    this.finished = false;
                    break;
                }

        application.current_delivery.checkLocations();
    }
}
