package com.android.example.easygo.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.example.easygo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String routeNumber;

    LocationManager locationManager;
    LocationListener locationListener;

    DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        routeNumber = getIntent().getStringExtra("rn");
        Log.i("rn", routeNumber);

        mRef = FirebaseDatabase.getInstance().getReference();
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

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

            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);

            final float[] results = new float[1];

            final Location currentLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            final LatLng currentLocation = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());

            final BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bus_black_24dp);

            mMap.addMarker(new MarkerOptions().position(currentLocation).title("User Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));

            DatabaseReference devRef = mRef.child("devices");
            devRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("ds", dataSnapshot.toString());
                    for(final DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.i("ds", ds.toString());
                        if(ds.child("route no").getValue(String.class).equals(routeNumber)) {
                            Log.i("rne", routeNumber);
                            if(ds.child("status").getValue(String.class).equals("active")) {
                                final double[] lat = new double[1];
                                final double[] lon = new double[1];
                                DatabaseReference devRefX = mRef.child(ds.child("id").getValue(Integer.class).toString()+"x");
                                devRefX.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        lat[0] = Double.parseDouble(dataSnapshot.getValue(String.class));
                                        Log.i("lat", lat[0] + "");
                                        DatabaseReference devRefY = mRef.child(ds.child("id").getValue(Integer.class).toString()+"y");
                                        devRefY.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                lon[0] = Double.parseDouble(dataSnapshot.getValue(String.class));
                                                Log.i("lon", lon[0] + "");

                                                LatLng busLoc = new LatLng(lat[0], lon[0]);
                                                Log.i("latlng", busLoc + "");
                                                Location.distanceBetween(currentLoc.getLatitude(), currentLoc.getLongitude(), lat[0], lon[0], results);
                                                Log.i("distance", "" + results[0]);
                                                if(results[0] < 1500) {
                                                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("User Location"));
                                                    Marker m = mMap.addMarker(new MarkerOptions().position(busLoc).title(routeNumber).icon(icon));
                                                    Log.i("rn", routeNumber);
                                                    m.showInfoWindow();
                                                    m.setPosition(busLoc);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);

                }

            } else {

                onBackPressed();

            }
        }
    }

}
