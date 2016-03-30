package hkp.logimap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
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


public class Map_Activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker GPSLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap){

        mMap = googleMap;
        UiSettings mUiSettings=mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        Bundle b=getIntent().getExtras();
        String route=b.getString("Route_id");

        Toast toast = Toast.makeText(getApplicationContext(), "Showing route: "+route, Toast.LENGTH_SHORT);
        toast.show();

        Map_Controller mapController=new Map_Controller(mMap,getApplicationContext(),route);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapController.GetOrigin(), 8));

        File file = new File(this.getFilesDir(),route+"_groute.json");
        if(!file.exists()) {
            mapController.GoogleRouteDownload(mapController.GetOrigin(),mapController.GetDestination(),mapController.GetWaypoints());
            mapController.ShowRoute(route +"_groute.json");
            mapController.AddMarkers(mapController.GetOrigin(), mapController.GetDestination(), mapController.GetWaypoints());

        }
        else {
            mapController.ShowRoute(route+"_groute.json");
            mapController.AddMarkers(mapController.GetOrigin(), mapController.GetDestination(),mapController.GetWaypoints());
            mapController.ShowGPSHistory(route+"_gps.json",getApplicationContext());
        }
          }
    public void OnClickGPS(View v){
        Button GPSButton = (Button)findViewById(R.id.button4);
        LocationManager manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(GPSLocation!=null){GPSLocation.remove();}
                GPSLocation=mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                .title("ME")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 6));
                Bundle b=getIntent().getExtras();
                String route=b.getString("Route_id");
                GPS_File GPSfile = new GPS_File(route,getApplicationContext());
                GPSfile.AppendValuesToFile(location.getLatitude(),location.getLongitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }
        if(GPSButton.getText().equals("START GPS")) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, listener);
            Toast toast = Toast.makeText(getApplicationContext(), "GPS Started", Toast.LENGTH_SHORT);
            toast.show();
            GPSButton.setText("STOP");
            GPSButton.setBackgroundColor(Color.argb(255, 255, 0, 0));
        }
        else
        {
            manager.removeUpdates(listener);
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
