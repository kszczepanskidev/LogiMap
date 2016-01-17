package hkp.logimap;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by hfscofield on 17.01.16.
 */
public class DriverStats {
    Integer orders_done, pkg_delivered, pkg_delayed;
    Float km_done;

    DriverStats(JSONObject driverStats) {
        try {
            this.km_done = Float.parseFloat(driverStats.getString("km_done"));
            this.orders_done = driverStats.getInt("orders_done");
            this.pkg_delivered = driverStats.getInt("pkg_delivered");
            this.pkg_delayed = driverStats.getInt("pkg_delayed");
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }
}
