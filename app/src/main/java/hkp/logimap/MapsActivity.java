package hkp.logimap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public ArrayList<LatLng> markerPoints;
    public LatLng sydney;
    public LatLng mark;
    public LatLng mark1;
    public LatLng mark2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markerPoints = new ArrayList<LatLng>();
        LatLng sydney = new LatLng(52.1347, 21.0042);
        LatLng mark = new LatLng(53, 19);
        LatLng mark1= new LatLng(53, 19.5);
        LatLng mark2  = new LatLng(54, 19);
        markerPoints.add(sydney);
        markerPoints.add(mark);
        markerPoints.add(mark1);
        markerPoints.add(mark2);
        markerPoints.add(new LatLng(49,13));

    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for(int i=0;i<markerPoints.size();i++){
            LatLng point  =markerPoints.get(i);
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        UiSettings mUiSettings=mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
     //   mMap.setMyLocationEnabled(true);
        LocationManager manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("ME"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 6));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        String url=getDirectionsUrl(new LatLng(52.17,16.92),new LatLng(45.18,5.69));
        DownloadTask DT=new DownloadTask(mMap);
        DT.execute(url);
        mMap.addMarker(new MarkerOptions().position(new LatLng(52.1347, 21.0042)).title("Wawa"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(53, 19)).title("2"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(53, 19.5)).title("3"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(54, 19)).title("4"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.17, 16.92), 6));

          }
}
