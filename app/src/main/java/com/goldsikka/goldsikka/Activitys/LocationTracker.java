package com.goldsikka.goldsikka.Activitys;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;


public class LocationTracker {
    Context context;
    LocationManager locationManager;
    public static LocationTracker myObj;
    public Location currentLocation;


    public static LocationTracker getInstance() {
        if (myObj == null) {
            myObj = new LocationTracker();

        }
        return myObj; //9666609904
    }

    public void fillContext(Context context1) {
        context = context1;
        loadDefaultLocation();
    }

    public void startLocation() {
        // Set Default Location.


        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (currentLocation == null) {
            loadDefaultLocation();
        }
        refreshLocations();
    }

    public void refreshLocations() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    private final LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            currentLocation = location;
            Log.e("locationlatitude", "" + location.getLatitude());
            Log.e("locationlongitude", "" + location.getLongitude());

            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void loadDefaultLocation() {
        currentLocation = new Location("Default");
        currentLocation.setLatitude(0);
        currentLocation.setLongitude(0);
    }


}
