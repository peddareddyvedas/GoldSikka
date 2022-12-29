package com.goldsikka.goldsikka.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Company_Details extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {


//    TextView tvreg_address,tvadmin_address;
//
//    ImageView ivfacebook,ivtwiter,ivinstagrem,ivpinterest,ivyoutube,ivlinkdlin;

    double dltitude, dlongtude;

    LinearLayout llregster, lladmin, lltectlab;

    GoogleMap googleMap;

    RelativeLayout backbtn;

    TextView unameTv, uidTv, titleTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_details_design);
        llregster = findViewById(R.id.lrgster);
        llregster.setOnClickListener(this);
        lladmin = findViewById(R.id.ladmin);
        lladmin.setOnClickListener(this);
        lltectlab = findViewById(R.id.lltechlab);
        lltectlab.setOnClickListener(this);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fmap);
        supportMapFragment.getMapAsync(this);
        Requestpermissions();
//        dlongtude = ;
//        dltitude = ;
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Company Details");
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.company_details_design);
//
//        llregster = findViewById(R.id.lrgster);
//        llregster.setOnClickListener(this);
//        lladmin = findViewById(R.id.ladmin);
//        lladmin.setOnClickListener(this);
//        lltectlab = findViewById(R.id.lltechlab);
//        lltectlab.setOnClickListener(this);
//
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle("Company Details");
//        //toolbar.setTitleTextColor(getColor(R.color.colorWhite));
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//
//        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.fmap);
//        supportMapFragment.getMapAsync(this);
//        //  initlizeviews();
//
//    }

    public void Requestpermissions() {
        if (ContextCompat.checkSelfPermission(Company_Details.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Company_Details.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(Company_Details.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(Company_Details.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(Company_Details.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.fmap);
                    supportMapFragment.getMapAsync(this);
                }
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng goldsikka = new LatLng(17.4448917, 78.4658366);
        //  17.49377761,78.40181658
        googleMap.addMarker(new MarkerOptions().position(goldsikka).title("Goldsikka"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(goldsikka));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.lrgster:
//                dlongtude = 17.394672303767948;
//                dltitude =78.4888338152866 ;
//                onMapReady(googleMap);
//                break;
//            case R.id.ladmin:
//                dlongtude = 17.394672303767948;
//                dltitude = 78.4888338152866;
//                onMapReady(googleMap);
//                break;
//            case R.id.lltechlab:
//                dlongtude =17.442674342133422 ;
//                dltitude = 78.39607779739406;
//                onMapReady(googleMap);
//                break;
//        }

    }
//
//    public void initlizeviews(){
//        tvreg_address = findViewById(R.id.tv_reg_address);
//        tvreg_address.setOnClickListener(this);
//
//        tvadmin_address = findViewById(R.id.tv_admin_address);
//        tvadmin_address.setOnClickListener(this);
//
//        ivfacebook = findViewById(R.id.iv_facebook);
//        ivinstagrem = findViewById(R.id.iv_instagram);
//        ivpinterest = findViewById(R.id.iv_pinterest);
//        ivtwiter = findViewById(R.id.iv_twitter);
//        ivlinkdlin = findViewById(R.id.iv_linkdlin);
//        ivyoutube = findViewById(R.id.iv_youtube);
//
//        ivfacebook.setOnClickListener(this);
//        ivinstagrem.setOnClickListener(this);
//        ivpinterest.setOnClickListener(this);
//        ivtwiter.setOnClickListener(this);
//        ivlinkdlin.setOnClickListener(this);
//        ivyoutube.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.tv_reg_address:
//                openmaps(17.3970629,78.4881669);
//                return;
//
//            case R.id.tv_admin_address:
//                openmaps(17.3970629,78.4881669);
//                return;
//
//            case R.id.iv_facebook:
//                opensocialmedia("https://www.facebook.com/GoldSikka-118144203314327");
//                return;
//
//                case R.id.iv_instagram:
//                    opensocialmedia("https://www.instagram.com/goldsikka");
//                    return;
//
//            case R.id.iv_pinterest:
//                opensocialmedia("https://in.pinterest.com/goldsikka/");
//                return;
//            case R.id.iv_twitter:
//                opensocialmedia("https://twitter.com/goldsikka");
//                return;
//
//            case R.id.iv_linkdlin:
//                opensocialmedia("https://www.linkedin.com/company/goldsikkalimited/");
//                return;
//
//            case R.id.iv_youtube:
//                opensocialmedia("https://www.youtube.com/channel/UCm65O1cTNaO0NnRE5e1qrzA/featured?disable_polymer=1");
//                return;
//        }
//    }
//
//    public void opensocialmedia(String link){
//        if (!NetworkUtils.isConnected(this)) {
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//            return;
//        }else {
//            String uri = String.format(Locale.ENGLISH, link);
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//            startActivity(intent);
//        }
//    }
//
//    public void openmaps(double lat,double lon){
//        if (!NetworkUtils.isConnected(this)) {
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//            return;
//        }else {
//            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", lat, lon);
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//            startActivity(intent);
//        }
//    }
}
