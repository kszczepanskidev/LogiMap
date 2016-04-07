package hkp.logimap;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by Paweł Plichta on 2015-12-19.
 */
public class Map_Controller {
    public MyApplication app;
    public GoogleMap Map;

    public Map_Controller(GoogleMap gMap){
        Map=gMap;
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

    public void ShowRoute(ArrayList<LatLng> directions,int color,int width)
    {
        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.addAll(directions);
        lineOptions.width(width);
        lineOptions.color(color);
        lineOptions.geodesic(true);
        Map.addPolyline(lineOptions);
    }


    public String getDirectionsUrl(LatLng origin,LatLng dest,ArrayList<LatLng> waypointsArr){
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
        String sensor = "sensor=false";
        String waypoints = "";
        for(int i=0;i<waypointsArr.size();i++){
            LatLng point  =waypointsArr.get(i);
            if(i==0)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
            }
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;
    }




}
