package com.goldsikka.goldsikka.Activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class CopanyDetails extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
//    double dltitude = 17.400860499617945;
//    double dlongtude = 78.48849053630367;

    double latitude =17.400860499617945;
    double longitude = 78.48849053630367;
    double latitude2 ;
    double longitude2 ;


    LinearLayout llregster,lladmin,lltectlab;

    GoogleMap  map;
    LatLng postion;

    TextView unameTv, uidTv, titleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copany_details);

        llregster = findViewById(R.id.lrgster);
        llregster.setOnClickListener(this);
        lladmin = findViewById(R.id.ladmin);
        lladmin.setOnClickListener(this);
        lltectlab = findViewById(R.id.lltechlab);
        lltectlab.setOnClickListener(this);

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleTv = findViewById(R.id.title);
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Company Details");
//        setTitle("Company Details");
        //toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mapfragment();
    }

    public void mapfragment(){
       // postion = new LatLng(dltitude,dlongtude);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fmap);
      //  map = supportMapFragment.getView();
        supportMapFragment.getMapAsync(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lrgster:
                latitude = 17.394672303767948;
                longitude =78.4888338152866 ;
                onmapchange();
                break;
            case R.id.ladmin:
                latitude = 17.394672303767948;
                longitude = 78.4888338152866;
                onmapchange();
                break;
            case R.id.lltechlab:
                latitude =17.442674342133422 ;
                longitude = 78.39607779739406;
                onmapchange();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    latitude = getIntent().getDoubleExtra("lat", 0);

        // Receiving longitude from MainActivity screen
      longitude = getIntent().getDoubleExtra("long", 0);



        postion = new LatLng(latitude, longitude);

        MarkerOptions options = new MarkerOptions();

        // Setting position for the MarkerOptions
        options.position(postion);

        // Setting title for the MarkerOptions
        options.title("Location");

        // Setting snippet for the MarkerOptions
        // Adding Marker on the Google Map
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);
            options.snippet(cityName + "," + stateName);

        } catch (Exception ex) {
            ex.getMessage();
            options.snippet("Latitude:" + latitude + ",Longitude:" + longitude);

        }
        googleMap.addMarker(options);
        googleMap.addMarker(options.position(new LatLng(latitude2,
                longitude2)));

        // Creating CameraUpdate object for position
        CameraUpdate updatePosition = CameraUpdateFactory.newLatLng(postion);

        // Creating CameraUpdate object for zoom
        CameraUpdate updateZoom = CameraUpdateFactory.zoomTo(6);

        // Updating the camera position to the user input latitude and longitude
        googleMap.moveCamera(updatePosition);

        // Applying zoom to the marker position
        googleMap.animateCamera(updateZoom);


    }
    public void onmapchange(){
        postion = new LatLng(latitude,longitude);
        String url = "http://maps.google.com/maps?daddr=" + postion.latitude + "," + postion.longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);

    }
}