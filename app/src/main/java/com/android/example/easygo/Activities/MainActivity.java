package com.android.example.easygo.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.example.easygo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    TextView tvBusNumber;
    TextView tvBusStop;

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;

    Marker busMarker;
    LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvBusNumber = (TextView) findViewById(R.id.textView_busNumber);
        tvBusStop = (TextView) findViewById(R.id.textView_busStop);

        tvBusNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BusNumber.class));
            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                busMarker.showInfoWindow();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                marker.showInfoWindow();
                return true;
            }
        });

        locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                // Add a marker in Sydney and move the camera
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bus_black_24dp);
                busMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("User Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                busMarker.showInfoWindow();

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);

            Location currentLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            currentLocation = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
            mMap.clear();
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bus_black_24dp);
            busMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("User Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            busMarker.showInfoWindow();

        }



    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                }

            } else {

                onBackPressed();

            }
        }
    }
}
