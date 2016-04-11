package hkp.logimap;

import android.graphics.Color;
import android.location.*;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Paweł Plichta on 2015-12-19.
 */
public class Map_Controller {
    public MyApplication app;
    public GoogleMap Map;

    public Map_Controller(GoogleMap gMap){
        Map=gMap;
    }

    public void AddMarkers(Collection<Location> locations) {
        for (Location l:locations) {

            Map.addMarker(new MarkerOptions()
                    .position(new LatLng(l.latitude, l.longtitude))
                    .title(l.name.toString()));
        }
    }

    public Polyline ShowRoute(ArrayList<LatLng> directions,int color,int width)
    {
        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.addAll(directions);
        lineOptions.width(width);
        lineOptions.color(color);
        lineOptions.geodesic(true);
        return Map.addPolyline(lineOptions);
    }


    public String getDirectionsUrl(LatLng origin,LatLng dest,ArrayList<LatLng> waypointsArr){
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
        String sensor = "sensor=false";
        String waypoints = "";
        if(waypointsArr!=null) {
            for (int i = 0; i < waypointsArr.size(); i++) {
                LatLng point = waypointsArr.get(i);
                if (i == 0)
                    waypoints = "waypoints=";
                waypoints += point.latitude + "," + point.longitude + "|";
            }
        }
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        if(waypointsArr!=null) {parameters+="&"+waypoints;}
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;
    }




}
