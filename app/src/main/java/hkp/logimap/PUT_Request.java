package hkp.logimap;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by krysztal on 23.01.16.
 */
public class PUT_Request {
    String url, newjson;

    PUT_Request(String url, String json) {
        this.url = url;
        this.newjson = json;
    }

    PUT_Request(JSONObject put) {
        try {
            this.url = put.getString("url");
            this.newjson = put.getString("newjson");
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    public JSONObject get_JSON() {
        JSONObject json = new JSONObject();

        try {
            json.put("url", url);
            json.put("newjson", newjson);
        } catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
        return json;
    }
}
