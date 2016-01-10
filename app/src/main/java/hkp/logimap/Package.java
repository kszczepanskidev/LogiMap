package hkp.logimap;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by kryształ on 03.12.2015.
 */
public class Package implements Serializable{
    String code, date, state;
    Location destination;

    Package(JSONObject _package) {
        try {
            this.code = _package.getString("code");
//            this.date = _package.getString("date");
            this.state = _package.getString("status").toString();
            this.destination = new Location(_package.getJSONObject("location"));
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }
}
