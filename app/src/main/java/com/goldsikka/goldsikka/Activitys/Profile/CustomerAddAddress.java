package com.goldsikka.goldsikka.Activitys.Profile;

import static com.facebook.gamingservices.cloudgaming.internal.SDKShareIntentEnum.REQUEST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.Activitys.Profile_Details;
import com.goldsikka.goldsikka.Fragments.Reedem_fragment;
import com.goldsikka.goldsikka.OrganizationWalletModule.OrganizationRedeemActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerAddAddress extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, OnLocationChangeListener {

    TextView tv_address_type, tv_address, tv_city, tv_state, tv_pin;
    EditText et_address, et_city, et_pincode, et_state;
    Button bt_add_adderss;
    String st_address, st_city, st_pincode, st_spinstate, st_spinaddresstype;
    String rs_address, rs_city, rs_pincode, rs_state, rs_spinaddresstype;
    String[] st_address_type = {"Select Address Type", "Home", "Office"};
    Spinner spinner_address_type, spinner_state;
    ArrayList<String> sublist, statelist;
    String subcategory, statesubcatagory, address_type;
    ApiDao apiDao;
    String addressId;
    String state_id = "25";
    List<Listmodel> list;
    //GifImageView loading_gif;
    String from, fromtolist, fromtoo;

    TextView unameTv, uidTv, titleTv;
    Button btn_currentlocation;

    private String TAG = CustomerAddAddress.class.getName();
    private GoogleMap mMap;
    private double currentLat;
    private double currentLang;

    private static final int REQUEST_LOCATION = 1;

    // check if GPS enabled
    private GPSTracker gpsTracker = new GPSTracker(CustomerAddAddress.this);
    private final String[] LOCATION_PERMS = {android.Manifest.permission.ACCESS_FINE_LOCATION};
    private final int INITIAL_REQUEST = 250;   //1337
    private final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    double latitude_current = 0.0;
    double longitude_current = 0.0;
    String country, name, mobilenumber, pincode, houseno, landmark, locality, city, state, customerid;
    TextView toolcartcount;

    public static String addres = "";
    LocationManager locationManager;
    private GoogleApiClient googleApiClient;
    private int SPLASH_TIME_OUT = 1500;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    RelativeLayout backbtn;
    Button manualaddress;

    @SuppressLint({"NewApi", "ServiceCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add_address);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle(getResources().getString(R.string.Add_New_Address));

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);
        btn_currentlocation = (Button) findViewById(R.id.btn_currentlocation);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Address List");

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            from = bundle.getString("from");
        }
        intilizeviews();
        setHint();

        manualaddress = findViewById( R.id.manualaddress );
        manualaddress.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), CustomerEditAddress.class);
                intent.putExtra("from", "addresslist");
                startActivity(intent);
            }


        } );


        btn_currentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker();
                // getLocation();

            }
        });

    }


    public void setHint() {
        et_address.setHint(Html.fromHtml(getString(R.string.Address)));
        et_city.setHint(Html.fromHtml(getString(R.string.City)));
        et_pincode.setHint(Html.fromHtml(getString(R.string.Zip_Code)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void intilizeviews() {
        // loading_gif = findViewById(R.id.loading_gif);
        et_state = findViewById(R.id.et_state);
        et_address = findViewById(R.id.et_customeraddress);
        et_city = findViewById(R.id.et_customercity);
        et_pincode = findViewById(R.id.et_pincode);

        tv_address = findViewById(R.id.tv_erroraddress);
        tv_address_type = findViewById(R.id.address_type);
        tv_city = findViewById(R.id.tv_city);
        tv_state = findViewById(R.id.tv_state);
        tv_pin = findViewById(R.id.tv_pin);
        spinner_state = findViewById(R.id.spin_state);

        bt_add_adderss = findViewById(R.id.btn_addaddress);
        bt_add_adderss.setOnClickListener(this);

        spinner_address_type = findViewById(R.id.spin_title);
        spinner_state.requestFocus();
        spinner_state.setFocusable(true);
        ArrayAdapter spinner_adapter = new ArrayAdapter(CustomerAddAddress.this, android.R.layout.simple_spinner_item, st_address_type);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_address_type.setAdapter(spinner_adapter);
        spinnerclick();


        spinner_stateclick();
    }

    public void spinner_stateclick() {
        statelist = new ArrayList<>();
        loaddata();
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statesubcatagory = spinner_state.getItemAtPosition(spinner_state.getSelectedItemPosition()).toString();
                //    spinner_state.getSelectedView().setTextColor(getResources().getColor(R.color.Blue));
                ((TextView) view).setTextColor(ContextCompat.getColor(CustomerAddAddress.this, R.color.DarkBrown));
                // state_id     = spinner_state.getSelectedItemPosition();
                if (!statesubcatagory.equals("Select State")) {
                    Listmodel listmodel = list.get(i);
                    state_id = listmodel.getId();
                    rs_state = listmodel.getName();
                    Log.e("stateid", state_id);

                    //Toast.makeText(activity,String.valueOf(id), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }

    public void loaddata() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<List<Listmodel>> get_address = apiDao.get_states("Bearer " + AccountUtils.getAccessToken(this));
            get_address.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    list = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        for (Listmodel listmodel : list) {
                            dialog.dismiss();
                            statelist.add(listmodel.getName());
                        }
                        spinner_state.setAdapter(new ArrayAdapter<String>(CustomerAddAddress.this, android.R.layout.simple_spinner_dropdown_item,
                                statelist));

                    }
                }


                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                    ToastMessage.onToast(CustomerAddAddress.this, "We Have Some Issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }

    }

    public void spinnerclick() {

        sublist = new ArrayList<>();
        // spinner_signuptype = findViewById(R.id.sub_category);

        spinner_address_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subcategory = String.valueOf(spinner_address_type.getSelectedItem());
                // subcategory = spinner_address_type.getItemAtPosition(spinner_address_type.getSelectedItemPosition() - 1).toString();
                // ((TextView) view).setTextColor(ContextCompat.getColor(RegistationActivity.this, R.color.colorWhite));
                ((TextView) view).setTextColor(ContextCompat.getColor(CustomerAddAddress.this, R.color.DarkBrown));

                if (subcategory.equals("Home")) {
                    address_type = "Home";
                } else if (subcategory.equals("Office")) {
                    address_type = "Office";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addaddress:
                if (!NetworkUtils.isConnected(this)) {
                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                } else {
                    initvalidation();
                }
                break;
        }
    }

    public void initvalidation() {
        tv_address_type.setVisibility(View.GONE);
        tv_address.setVisibility(View.GONE);
        tv_city.setVisibility(View.GONE);
        tv_state.setVisibility(View.GONE);
        tv_pin.setVisibility(View.GONE);

        bt_add_adderss.setVisibility(View.GONE);
        //loading_gif.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                CustomerAddAddress.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        validation();
                        bt_add_adderss.setVisibility(View.VISIBLE);
                        //loading_gif.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);
    }

    public void validation() {

        st_address = et_address.getText().toString().trim();
        st_city = et_city.getText().toString().trim();
        st_pincode = et_pincode.getText().toString().trim();

        tv_address_type.setVisibility(View.GONE);
        tv_address.setVisibility(View.GONE);
        tv_city.setVisibility(View.GONE);
        tv_state.setVisibility(View.GONE);
        tv_pin.setVisibility(View.GONE);

        if (subcategory.equals("Select Address Type")) {
            ToastMessage.onToast(CustomerAddAddress.this, "Please Select Address Type", ToastMessage.ERROR);
        } else if (st_address.isEmpty()) {
            tv_address.setVisibility(View.VISIBLE);
            tv_address.setText("Please Enter Address");
        } else if (st_city.isEmpty()) {
            tv_city.setVisibility(View.VISIBLE);
            tv_city.setText("Please Enter City");
        }
//        else if (statesubcatagory.equals("Select State")){
//            ToastMessage.onToast(CustomerAddAddress.this, "Please Select State", ToastMessage.ERROR);
//        }
        else if (st_pincode.isEmpty()) {
            tv_pin.setVisibility(View.VISIBLE);
            tv_pin.setText("Please Enter Zip Code");
        } else {
            openAdd_Address();
        }

    }

    public void openAdd_Address() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> get_address = apiDao.getcustomeraddress("Bearer " + AccountUtils.getAccessToken(this),
                    address_type, st_address, st_city, state_id, st_pincode);
            get_address.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    List<Listmodel> list = Collections.singletonList(response.body());
                    if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {
                        for (Listmodel listmodel : list) {
                            addressId = listmodel.getId();
                            //  AccountUtils.setAddressid(activity,addressId);
                            dialog.dismiss();
                            ToastMessage.onToast(CustomerAddAddress.this, "Successfully added", ToastMessage.SUCCESS);
                            onsuccess();
                        }
                    } else {
                        dialog.dismiss();
                        try {
                            dialog.dismiss();
                            tv_address_type.setVisibility(View.GONE);
                            tv_address.setVisibility(View.GONE);
                            tv_city.setVisibility(View.GONE);
                            tv_state.setVisibility(View.GONE);
                            tv_pin.setVisibility(View.GONE);

                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            ToastMessage.onToast(CustomerAddAddress.this, st, ToastMessage.ERROR);
                            JSONObject er = jObjError.getJSONObject("errors");

                            try {
                                JSONArray array_title = er.getJSONArray("title");
                                for (int i = 0; i < array_title.length(); i++) {
                                    tv_address_type.setVisibility(View.VISIBLE);
                                    tv_address_type.setText(array_title.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray array_address = er.getJSONArray("address");
                                for (int i = 0; i < array_address.length(); i++) {
                                    tv_address.setVisibility(View.VISIBLE);
                                    tv_address.setText(array_address.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray arry_city = er.getJSONArray("city");
                                for (int i = 0; i < arry_city.length(); i++) {
                                    tv_city.setVisibility(View.VISIBLE);
                                    tv_city.setText(arry_city.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_state = er.getJSONArray("state_id");
                                for (int i = 0; i < array_state.length(); i++) {
                                    tv_state.setVisibility(View.VISIBLE);
                                    tv_state.setText(array_state.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_pin = er.getJSONArray("zip");
                                for (int i = 0; i < array_pin.length(); i++) {
                                    tv_pin.setVisibility(View.VISIBLE);
                                    tv_pin.setText(array_pin.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    ToastMessage.onToast(CustomerAddAddress.this, "We Have Some Issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }
    }

    public void onsuccess() {
        if (from.equals("profiles")) {
            Intent intent = new Intent(CustomerAddAddress.this, Profile_Details.class);
            intent.putExtra("id", addressId);
            startActivity(intent);
        } else if (from.equals("addresslist")) {
            Intent intent = new Intent(CustomerAddAddress.this, CustomerAddressList.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("id", addressId);
            startActivity(intent);
        } else if (from.equals("redeem")) {
            Intent intent = new Intent(CustomerAddAddress.this, Reedem_fragment.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (from.equals("orgredeem")) {
            Intent intent = new Intent(CustomerAddAddress.this, OrganizationRedeemActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.e(TAG, "LAT-" + location.getLatitude());
            Log.e(TAG, "LNG-" + location.getLongitude());

            currentLat = location.getLatitude();
            currentLang = location.getLongitude();

//            mMap.setMyLocationEnabled(true);
            LatLng currentLocation = new LatLng(currentLat, currentLang);

            createMarker(currentLat,
                    currentLang);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentLocation)      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            locationManager.setNullListener();

        }
    }

    protected Marker createMarker(double latitude, double longitude) {
        mMap.clear();

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Current Location")
                .anchor(0.5f, 0.5f)
                .draggable(true));
        marker.showInfoWindow();
        return marker;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }


    @Override
    protected void onResume() {
        super.onResume();
        // getLocation();


    }

    public void checker() {
        Log.e("check", "call");
        android.location.LocationManager locationManager = (android.location.LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            //redirect();
            loadcurrenntmap();

        } else {
            enableLoc();
        }
    }

    public void loadcurrenntmap() {

        getLocation();
    }

    //// Below 6.0.1 Laction can be on /////
    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            // StartAnimations();
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true); //this is the key ingredient
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(CustomerAddAddress.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        //  finish();
                        break;
                    }
                    case Activity.RESULT_OK: {
                        // The user was asked to change settings, but chose not to
                        //  redirect();

                        getLocation();
                        break;
                    }

                }
                break;
        }

    }


    //For Getting Current Location
    public void getLocation() {
        gpsTracker = new GPSTracker(CustomerAddAddress.this);
        // Log.e("ryu",""+gpsTracker.canGetLocation());
        if (gpsTracker.canGetLocation()) {
            latitude_current = gpsTracker.getLatitude();
            longitude_current = gpsTracker.getLongitude();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker = new GPSTracker(CustomerAddAddress.this);
            gpsTracker.showSettingsAlert();
        }
        try {
            locationManager = LocationManager.getInstance(CustomerAddAddress.this, this, 60000);
            ((LocationManager) locationManager).checkPermissions();
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(CustomerAddAddress.this, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude_current, longitude_current, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            Log.e("dataload", "Enter into Data");
//            Log.e("dataload", String.valueOf(addresses));
//            Log.e("dataload", String.valueOf(geocoder));
//            Log.e("jjb",""+latitude_current);
//            Log.e("jjb",""+longitude_current);
            if (latitude_current == 0.0) {
                getLocation();
            }

            if (addresses != null && addresses.size() > 0) {
                String address1 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String address = addresses.get(0).getSubAdminArea();
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                String exactlocation = addresses.get(0).getCountryCode();



//////state_id
                state_id = "25";

              //  String add = addresses.get(0).getFeatureName() + "," + addresses.get(0).getSubThoroughfare() + "," + addresses.get(0).getThoroughfare();
                  String add = addresses.get(0).getFeatureName();
                addres = add;
                et_address.setText(add);
                et_city.setText(city);
                et_pincode.setText(postalCode);
                et_state.setText(state);

                Log.e("addres ", "" + addres);

                Log.e("add ", "" + add);

                Log.e("orginallocation ", "" + address1);


                // location.setText("" + address);
            } else {
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int grantedCount = 0;
        if (requestCode == REQUEST_LOCATION && grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    grantedCount++;
                }
            }
            if (grantedCount == 1) {
                getLocation();
            } else {
                Toast.makeText(this, "Permissions are mandatory", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


}