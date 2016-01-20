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
    String code, state;
    Integer destination, id;
    Time deadline;

    Package(JSONObject _package) {
        DateFormat format = new SimpleDateFormat("hh:mm");
        try {
            this.id = _package.getInt("id");
            this.code = _package.getString("code");
            this.deadline = new Time(format.parse(_package.getString("deliver_before")).getTime());
            this.state = _package.getString("status").toString();
            this.destination = _package.getInt("location");
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }
}
