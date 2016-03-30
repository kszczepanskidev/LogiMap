package hkp.logimap;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Paweł Plichta on 2015-12-19.
 */
public class Map_Controller {
    public GoogleMap Map;
    private String RouteID;
    private Context context;

    public Map_Controller(GoogleMap Mapa, Context cntext, String id){
        Map=Mapa;
        RouteID=id;
        context=cntext;
    }

    public LatLng GetOrigin() {
        LatLng start= new LatLng(52.17,16.92);
        return start;
    }

    public LatLng GetDestination() {
        LatLng stop = new LatLng(51.21,15.23);
        return stop;
    }

    public ArrayList<LatLng> GetWaypoints(){
        ArrayList<LatLng> waypoints= new ArrayList<>();
        waypoints.add(new LatLng(52.021,19.021));
        waypoints.add(new LatLng(53.22,21));
        waypoints.add(new LatLng(52.12,19.31));
        waypoints.add(new LatLng(52.88, 20.21));
        waypoints.add(new LatLng(51.19, 18.98));
        return waypoints;
    }

    public void AddMarkers(LatLng start,LatLng stop, ArrayList<LatLng> waypoints) {
        Map.addMarker(new MarkerOptions()
                .position(start)
                .title("START"));
        Map.addMarker(new MarkerOptions()
                .position(stop)
                .title("STOP"));
        for (int i = 0; i < waypoints.size(); i++) {

            Map.addMarker(new MarkerOptions()
                    .position(waypoints.get(i))
                    .title("Waypoint: "+i));
        }
    }

    public void ShowGPSHistory(String filename,Context context)
    {
        PolylineOptions lineOptions = new PolylineOptions();
        ArrayList<LatLng>  points = new ArrayList<LatLng>();
        File file = new File(context.getFilesDir(),filename);
        if(file.exists()) {
            String data = "";
            Double lat;
            Double lng;
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
                for (int i = 0; i < arrObj.length(); i++) {
                    JSONObject obj = arrObj.getJSONObject(i);
                    lat = Double.parseDouble(obj.getString("lat"));
                    lng = Double.parseDouble(obj.getString("lng"));
                    LatLng point = new LatLng(lat, lng);
                    points.add(point);
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            lineOptions.addAll(points);
            lineOptions.width(4);
            lineOptions.color(Color.GREEN);
            lineOptions.geodesic(true);
            Map.addPolyline(lineOptions);
        }
    }

    public void ShowRoute(String filename)
    {

        String data="";
        Parser_Task parserTask=new Parser_Task(Map);
        File file = new File(context.getFilesDir(),filename);
        try{
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuffer sb  = new StringBuffer();
        String line = "";
        while( ( line = br.readLine())  != null){
            sb.append(line);
        }
        data = sb.toString();
        br.close();
    }catch(Exception e) {
            e.printStackTrace();
        }
            parserTask.execute(data);
    }

    public void GoogleRouteDownload(LatLng start,LatLng stop, ArrayList<LatLng> waypoints){
        String url=getDirectionsUrl(start,stop,waypoints);
        Map_Download_Task DT=new Map_Download_Task(Map,context,RouteID);
        DT.execute(url);
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest,ArrayList<LatLng> waypointsArr){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for(int i=0;i<waypointsArr.size();i++){
            LatLng point  =waypointsArr.get(i);
            if(i==0)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }
}
