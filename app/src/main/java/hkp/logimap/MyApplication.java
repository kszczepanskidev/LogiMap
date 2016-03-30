package hkp.logimap;

import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by krysztal on 05.01.16.
 */
public class MyApplication extends Application {
    Delivery current_delivery, history_delivery;
    ArrayList<PUT_Request> puts;
    ArrayList<String> package_states;
    Boolean file_thread_is_running, server_thread_is_running;

    @Override
    public void onCreate() {
        puts = new ArrayList<>();
        current_delivery = null;
        history_delivery = null;
        file_thread_is_running = false;
        server_thread_is_running = false;
        load_PUTs();

        package_states = new ArrayList<>();
        package_states.add("Loaded");
        package_states.add("Delivered");
        package_states.add("Not delivered");
        package_states.add("Delayed");
        package_states.add("Damaged");

        super.onCreate();
    }

    public void addPUT(PUT_Request put) {
        puts.add(put);
        save_PUTs();
    }

    private void save_PUTs() {
        JSONArray puts_json = new JSONArray();

        for(PUT_Request p : puts)
            puts_json.put(p.get_JSON());

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("PUT_requests.json", MODE_PRIVATE));
            outputStreamWriter.write(puts_json.toString());
            outputStreamWriter.close();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    private void load_PUTs() {
        try {
            InputStream is = openFileInput("PUT_requests.json");

            if ( is != null ) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = br.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                is.close();
                JSONArray puts_json = new JSONArray(stringBuilder.toString());

                for (int i = 0; i < puts_json.length(); ++i)
                    puts.add(new PUT_Request(puts_json.getJSONObject(i)));
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }
}