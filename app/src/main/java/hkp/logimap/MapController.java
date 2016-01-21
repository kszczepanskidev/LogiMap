package hkp.logimap;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Paweł Plichta on 2015-12-19.
 */
public class MapController {
    public GoogleMap Map;
    private Context context;

    public MapController(GoogleMap Mapa,Context cntext){
        Map=Mapa;
        context=cntext;
    }

    public ArrayList<LatLng> GetWaypoints(){
        ArrayList<LatLng> waypoints= new ArrayList<>();
        waypoints.add(new LatLng(52.021,19.021));
        waypoints.add(new LatLng(53.22,21));
        waypoints.add(new LatLng(52.12,19.31));
        waypoints.add(new LatLng(52.88,20.21));
        waypoints.add(new LatLng(51.19,18.98));
        return waypoints;
    }
    public void ShowRoute(int routeid){
        String url=getDirectionsUrl(new LatLng(52.17,16.92),new LatLng(45.18,5.69),GetWaypoints());
        Map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.17,16.92), 8));
        DownloadTask DT=new DownloadTask(Map,context);
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
