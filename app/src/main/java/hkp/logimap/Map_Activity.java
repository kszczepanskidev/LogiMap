package hkp.logimap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class Map_Activity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        AsyncListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Map_Controller mapController;
    private Polyline GPSPolyline;
    private boolean gpsState=false;

    LocationRequest mLocationRequest = new LocationRequest()
            .setInterval(10000)
            .setSmallestDisplacement(5)
            .setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    private Marker GPSPositionMarker;
    private Location CurrentLocation;


    MyApplication   application;
    static final int REQUEST_FINE_LOCATION=10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }
    }

    public void saveGPSstate(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("GPSkey", gpsState);
        editor.commit();
    }

    public void loadGpsState() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        gpsState = sharedPref.getBoolean("GPSkey", false);
        Button GPSButton = (Button) findViewById(R.id.button4);
        if (gpsState==false) {
            GPSButton.setText("START GPS");
            GPSButton.setClickable(true);
            GPSButton.setBackgroundColor(Color.argb(255, 0, 255, 0));
        } else if (gpsState==true) {
            if(mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            }
            GPSButton.setText("STOP");
            GPSButton.setClickable(true);
            GPSButton.setBackgroundColor(Color.argb(255, 255, 0, 0));
        } else {
            GPSButton.setText("GPS ERROR");
            GPSButton.setClickable(false);
            GPSButton.setBackgroundColor(Color.argb(255,190, 190, 190));
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        checkAppPermission();
        checkGPSProvider();
        String route;
        mapController = new Map_Controller(mMap);
        application = (MyApplication) getApplication();
        if (application.history_delivery != null) {
            route = application.history_delivery.id.toString();
            Toast toast = Toast.makeText(getApplicationContext(), "Showing archive route: " + route, Toast.LENGTH_SHORT);
            toast.show();
            Button GPSButton = (Button) findViewById(R.id.button4);
            GPSButton.setText("History");
            GPSButton.setClickable(false);
            GPSButton.setBackgroundColor(Color.argb(255, 255, 255, 0));
            hkp.logimap.Location firstLoc = application.history_delivery.locations.values().iterator().next();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(firstLoc.latitude, firstLoc.longtitude), 8));
            mapController.ShowRoute(application.history_delivery.directions, Color.BLUE, 8);
            mapController.AddMarkers(application.history_delivery.locations.values());
            mapController.ShowRoute(application.history_delivery.gps, Color.GREEN, 8);
        } else if(application.current_delivery!=null){
            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
            loadGpsState();
            route = application.current_delivery.id.toString();
            Toast toast = Toast.makeText(getApplicationContext(), "Showing route: " + route, Toast.LENGTH_SHORT);
            toast.show();
            Map_Download_Task DT = new Map_Download_Task(this, true);
            LatLng origin, destination;
            ArrayList<LatLng> waypoints = new ArrayList<>();
            for (hkp.logimap.Location l : application.current_delivery.locations.values()) {
                waypoints.add(new LatLng(l.latitude, l.longtitude));
            }
            origin = waypoints.get(0);
            destination = waypoints.get(waypoints.size() - 1);
            waypoints.remove(0);
            waypoints.remove(waypoints.size() - 1);
            if (application.current_delivery.directions.isEmpty()) {
                DT.execute(mapController.getDirectionsUrl(origin, destination, waypoints));
            } else {
                mapController.ShowRoute(application.current_delivery.directions, Color.BLUE, 8);
                if (!application.current_delivery.gps.isEmpty()) {
                    GPSPolyline = mapController.ShowRoute(application.current_delivery.gps, Color.GREEN, 8);
                }
            }
            mapController.AddMarkers(application.current_delivery.locations.values());
        }
        else {
            finish();
        }

    }

    public void processFinish(ArrayList<LatLng> output, boolean route) {
        if (route) {
            application.current_delivery.directions.clear();
            application.current_delivery.directions.addAll(output);
            mapController.ShowRoute(output, Color.BLUE, 8);
        } else {
            mapController.ShowRoute(output, Color.RED, 8);
        }
    }

    public void checkAppPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_FINE_LOCATION);
            Button GPSButton = (Button) findViewById(R.id.button4);
            GPSButton.setClickable(false);
            Toast.makeText(getApplicationContext(), "Fine location permission required", Toast.LENGTH_SHORT).show();
            }
    }

    public boolean checkGPSProvider()
    {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){return true;}
        else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 10);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
    }

    protected void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Toast.makeText(this,"Connected to location service",Toast.LENGTH_SHORT).show();
        hkp.logimap.Location temp=application.current_delivery.locations.values().iterator().next();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(temp.latitude, temp.longtitude), 9));
        if (getLastKnownLocation() != null) {
            CurrentLocation = getLastKnownLocation();
            Toast.makeText(this, "Your last known position: " + CurrentLocation.getLatitude() +
                    ", " + CurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude()), 9));
            if (GPSPositionMarker == null) {
                GPSPositionMarker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude()))
                                .title("ME")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps))
                );
            }
           Map_Download_Task DT= new Map_Download_Task(this,false);
            hkp.logimap.Location loc=application.current_delivery.locations.values().iterator().next();
            DT.execute(mapController.getDirectionsUrl(new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude()),
                    new LatLng(loc.latitude, loc.longtitude), null));
        }
    }

    protected Location getLastKnownLocation() {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return mLastLocation;
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection to Google Play Service Failed", Toast.LENGTH_SHORT).show();
    }

    public void OnClickGPS(View v) {
        Button GPSButton = (Button) findViewById(R.id.button4);
        if (GPSButton.getText().equals("START GPS") && mGoogleApiClient.isConnected()) {
            gpsState=true;
            saveGPSstate();
            startLocationUpdates();
            Toast.makeText(getApplicationContext(), "GPS Tracking Started", Toast.LENGTH_SHORT).show();
            GPSButton.setText("STOP");
            GPSButton.setBackgroundColor(Color.argb(255, 255, 0, 0));
        } else if (GPSButton.getText().equals("STOP") && mGoogleApiClient.isConnected()) {
            Toast.makeText(getApplicationContext(), "GPS Tracking Stopped", Toast.LENGTH_SHORT).show();
            stopLocationUpdates();
            GPSButton.setText("COMPLETED");
            gpsState=false;
            saveGPSstate();
            GPSButton.setBackgroundColor(Color.argb(255, 255, 255, 0));
            GPSButton.setClickable(false);
            application.current_delivery.finished=true;
        }

    }

    public void OnClickLoad(View v) {
        startActivity(new Intent(getApplicationContext(), Destinations_List_Activity.class));
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            CurrentLocation = location;
            application.current_delivery.gps.add(new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude()));
            if(GPSPolyline!=null){
                GPSPolyline.remove();
                PolylineOptions gpsline = new PolylineOptions()
                        .addAll(application.current_delivery.gps)
                        .color(Color.GREEN)
                        .width(8);
                        mMap.addPolyline(gpsline);
            }
            else
            {
                PolylineOptions gpsline = new PolylineOptions()
                        .addAll(application.current_delivery.gps)
                        .color(Color.GREEN)
                        .width(8);
                mMap.addPolyline(gpsline);
            }
            if (GPSPositionMarker != null) {
                GPSPositionMarker.remove();
                CheckBox box = (CheckBox) findViewById(R.id.checkBox);
                if(box.isChecked()) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude()), 9));
                }
                GPSPositionMarker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude()))
                                .title("ME")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps))
                );
            } else {
                CheckBox box = (CheckBox) findViewById(R.id.checkBox);
                if(box.isChecked()) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude()), 9));
                }
                GPSPositionMarker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude()))
                                    .title("ME")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps))
                );

            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Button GPSButton = (Button) findViewById(R.id.button4);
                    GPSButton.setClickable(true);
                } else {
                    Button GPSButton = (Button) findViewById(R.id.button4);
                    GPSButton.setText("Tracking not available");
                    GPSButton.setClickable(false);
                    GPSButton.setBackgroundColor(Color.argb(255, 190, 190, 190));
                }
                return;
            }
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
            final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
            if(requestCode == 10 && manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ){
                mGoogleApiClient.connect();
            }
            else{
                Toast.makeText(this,"GPS not available",Toast.LENGTH_SHORT).show();
                Button GPSButton = (Button) findViewById(R.id.button4);
                GPSButton.setText("Tracking not available");
                GPSButton.setClickable(false);
                GPSButton.setBackgroundColor(Color.argb(255, 190, 190, 190));
            }
        }

}
