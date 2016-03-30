package hkp.logimap;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by Paweł Plichta on 2016-03-18.
 */
public class GPS_File {
    private String Filename;
    private Context context;


    public GPS_File(String filename, Context parent){
        Filename=filename;
        context=parent;
    }

    public void AppendValuesToFile(double Latitude,double Longitude) {
        File file = new File(context.getFilesDir(),Filename+"_gps.json");
        if(!file.exists()) {
            try {
                JSONObject jsonObj = new JSONObject();
                JSONArray jsonArr = new JSONArray();
                JSONObject jsonPointObj = new JSONObject();
                jsonPointObj.put("lat", Latitude);
                jsonPointObj.put("lng", Longitude);
                jsonArr.put(jsonPointObj);
                jsonObj.put("points",jsonArr);
                try {
                    FileOutputStream outputStream = new FileOutputStream(file);
                    outputStream.write(jsonObj.toString().getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch(JSONException ex) {
                ex.printStackTrace();
            }
        }
        else
        {
            String data = "";
            ArrayList<Double> Latitudes= new ArrayList<>();
            ArrayList<Double> Longitudes= new ArrayList<>();
            try {
                FileInputStream iStream = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();
                br.close();
                iStream.close();
            } catch (Exception e) {
            e.printStackTrace();
             }
            try {
                JSONObject jObj = new JSONObject(data);
                JSONArray arrObj = jObj.getJSONArray("points");
                for (int i=0;i<arrObj.length();i++){
                    JSONObject obj = arrObj.getJSONObject(i);
                    Latitudes.add(Double.parseDouble(obj.getString("lat")));
                    Longitudes.add(Double.parseDouble(obj.getString("lng")));
                }
                Latitudes.add(Latitude);
                Longitudes.add(Longitude);
            } catch(JSONException ex) {
                ex.printStackTrace();
            }

            try {
                JSONObject jsonObj = new JSONObject();
                JSONArray jsonArr = new JSONArray();
                JSONObject jsonPointObj = new JSONObject();
                for(int i=0;i<Latitudes.size();i++) {
                    jsonPointObj.put("lat", Latitudes.get(i));
                    jsonPointObj.put("lng", Longitudes.get(i));
                    jsonArr.put(jsonPointObj);
                }
                jsonObj.put("points", jsonArr);
                try {
                    FileOutputStream outputStream = new FileOutputStream(file);
                    outputStream.write(jsonObj.toString().getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch(JSONException ex) {
                ex.printStackTrace();
            }


        }
    }
}
