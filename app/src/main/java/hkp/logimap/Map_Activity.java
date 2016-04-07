package hkp.logimap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;


public class Map_Activity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        AsyncListener{

    private GoogleMap mMap;
    private Map_Controller mapController;
    Delivery delivery;
    MyApplication application;
    static final int MY_FINE_LOCATION_PERMISSION = 1;
    private boolean mPermissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        UiSettings mUiSettings=mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        String route;
        application=(MyApplication)getApplication();

        if(application.history_delivery!=null) {
            route=application.history_delivery.id.toString();
            Toast toast = Toast.makeText(getApplicationContext(), "Showing archive route: " + route, Toast.LENGTH_SHORT);
            toast.show();
            Button GPSButton = (Button)findViewById(R.id.button4);
            GPSButton.setText("History");
            GPSButton.setClickable(false);
            GPSButton.setBackgroundColor(Color.argb(255, 255, 255, 0));

        }
        else{
            route=application.current_delivery.id.toString();
            Toast toast = Toast.makeText(getApplicationContext(), "Showing route: " + route, Toast.LENGTH_SHORT);
            toast.show();
        }

        mapController=new Map_Controller(mMap);


        LatLng origin;
        LatLng destination;
        ArrayList<LatLng>waypoints=new ArrayList<>();
        //if(application.current_delivery.directions==null){
            for(hkp.logimap.Location l:application.current_delivery.locations.values()) {
                LatLng point=new LatLng(l.latitude,l.longtitude);
                waypoints.add(point);
            }
            origin=waypoints.get(0);
            destination=waypoints.get(waypoints.size()-1);
            waypoints.remove(0);
            waypoints.remove(waypoints.size() - 1);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 8));
            Map_Download_Task DT=new Map_Download_Task(this);
            DT.execute(mapController.getDirectionsUrl(origin,destination,waypoints));
            mapController.AddMarkers(origin, destination, waypoints);
           //  }
           /* else{
            for (hkp.logimap.Location l : application.current_delivery.locations.values()) {
                LatLng point = new LatLng(l.latitude, l.longtitude);
                waypoints.add(point);
            }
            origin = waypoints.get(0);
            destination = waypoints.get(waypoints.size() - 1);
            waypoints.remove(0);
            waypoints.remove(waypoints.size() - 1);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 8));
            mapController.ShowRoute(application.current_delivery.directions,Color.BLUE,4);
            mapController.AddMarkers(origin, destination, waypoints);
            mapController.ShowRoute(application.current_delivery.gps, Color.GREEN,4);
        }*/
    }

    public void processFinish(ArrayList<LatLng> output){
        application.current_delivery.directions.addAll(output);
        mapController.ShowRoute(output,Color.BLUE,5);
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, MY_FINE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != MY_FINE_LOCATION_PERMISSION) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    public void OnClickGPS(View v){
        Button GPSButton = (Button)findViewById(R.id.button4);

        if(GPSButton.getText().equals("START GPS")) {

                GPSButton.setText("STOP");
                GPSButton.setBackgroundColor(Color.argb(255, 255, 0, 0));
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "GPS Stoped", Toast.LENGTH_SHORT);
            toast.show();
            GPSButton.setText("COMPLETED");
            GPSButton.setBackgroundColor(Color.argb(255, 255, 255, 0));
            GPSButton.setClickable(false);
        }

    }

    public void OnClickLoad(View v) {
        startActivity(new Intent(getApplicationContext(), Destinations_List_Activity.class));
    }
}
