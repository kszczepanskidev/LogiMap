package hkp.logimap;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by kryształ on 03.12.2015.
 */
public class Package implements Serializable{
    String code;
    Integer destination, id, state;
    Time deadline;

    Package(JSONObject _package) {
        DateFormat format = new SimpleDateFormat("hh:mm");
        try {
            this.id = _package.getInt("id");
            this.code = _package.getString("code");
            this.deadline = new Time(format.parse(_package.getString("deliver_before")).getTime());
            this.state = _package.getInt("status");
            this.destination = _package.getInt("location");
//            Log.i("TEST", deadline.toString());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    public JSONObject getJSON() {
        JSONObject package_json = new JSONObject();
        try {
                package_json.put("id", this.id);
                package_json.put("code", this.code);
                package_json.put("status", this.state);
                package_json.put("location", this.destination);
                package_json.put("deliver_before", this.deadline.toString().substring(0,5));
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        } finally {
            return package_json;
        }
    }
}
