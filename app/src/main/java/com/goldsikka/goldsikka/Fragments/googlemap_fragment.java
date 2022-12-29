package com.goldsikka.goldsikka.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.goldsikka.goldsikka.R;

import java.util.List;
import java.util.Locale;

public class googlemap_fragment   extends Fragment implements LocationListener {



    LocationManager locationManager;
    TextView tvCity, tvState, tvCountry, tvPin, tvLocality;
    String st_city,st_state,st_country,st_pin,st_locality;
    private Activity activity;
    ProgressDialog dialog;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.google_map,container,false);

        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        intilizeviews(view);
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationEnabled();
        dialog = new ProgressDialog(activity);
        getLocation();
        return view;
    }

    public void intilizeviews(View view){


        tvCity = view.findViewById(R.id.tvCity);
        tvState = view.findViewById(R.id.tvState);
        tvCountry = view.findViewById(R.id.tvCountry);
        tvPin = view.findViewById(R.id.tvPin);
        tvLocality = view.findViewById(R.id.tvLocality);

        st_city = tvCity.getText().toString();
        st_state = tvState.getText().toString();
        st_country = tvCountry.getText().toString();
        st_pin =  tvPin.getText().toString();
        st_locality = tvLocality.getText().toString();
    }
    private void locationEnabled() {

        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {


            new AlertDialog.Builder(activity)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    void getLocation() {
        try {
            dialog.setMessage("Please Wait.....");
            dialog.setCancelable(false);
            dialog.show();
            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500,
                    5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            tvCity.setText(addresses.get(0).getLocality());
            tvState.setText(addresses.get(0).getAdminArea());
            tvCountry.setText(addresses.get(0).getCountryName());
            tvPin.setText(addresses.get(0).getPostalCode());
            tvLocality.setText(addresses.get(0).getAddressLine(0).trim());

            dialog.dismiss();


        } catch (Exception e) {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}