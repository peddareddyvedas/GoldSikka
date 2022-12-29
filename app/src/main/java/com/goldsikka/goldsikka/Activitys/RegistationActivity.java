package com.goldsikka.goldsikka.Activitys;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import com.goldsikka.goldsikka.Activitys.Events.FileUtilty;
import com.goldsikka.goldsikka.Activitys.Profile.GPSTracker;
import com.goldsikka.goldsikka.Fragments.baseinterface;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistationActivity extends BaseActivity implements baseinterface {

    String stname, stemail, stmobile, stpassword, stconfirmpassword, streferralcode;
    String MobilePattern = "[0-9]{10}";
    String password_pattern = "[0-9a-zA-Z]{8}";
    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ApiDao apiDao;
    String rs_name, rs_accesstoken, rs_roleid, rspknow, rs_maksedphoone, rs_verifytoken, roleId;
    String[] signup_type = {"Select Wallet", "Personal Wallet", "Organization Wallet"};
    String[] spinknowarray = {"How did you hear about us?", "1. Google Search", "2. Goldsikka Representative", "3. Social Media Add",
            "4. Radio Add", "5. Word of Mouth", "6.others"};

    String stknow;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spin_signuptype)
    Spinner spinner_signuptype;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.reg_eds1)
    TextView tv_signuptype;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.reg_eds2)
    TextView selcrsignuptype2;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.reg_edn1)
    TextView tv_name;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.reg_edn2)
    TextView tv_name2;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.reg_ede1)
    TextView tv_email;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.reg_ede2)
    TextView tv_email2;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.edreferralcode)
    TextView tv_refferl_error;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.reg_edm1)
    TextView tv_mobilel;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.reg_edm2)
    TextView tv_mobile2;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.reg_edp1)
    TextView tv_password;
    //    @SuppressLint("NonConstantResourceId")
//    @BindView(value = R.id.et_passwords)
//    TextView tv_passwordMsg;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.reg_edcp1)
    TextView tv_conforimpassword1;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvpolicy)
    TextView tvpolicy;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvorgpolicy)
    TextView tvorgpolicy;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tv_terms)
    TextView tv_terms;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.et_name)
    EditText etname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_email)
    EditText etemail;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_mobile)
    EditText etmobile;

   /* @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_password)
    EditText etpassword;*/

 /*   @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_cpassword)
    EditText etconfirmpassword;*/

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etreferralcode)
    EditText etreferralcode;
//
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.iv_back)
//    ImageView iv_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_signup)
    Button btn_signup;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llname)
    LinearLayout llname;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llemail)
    LinearLayout llemail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llmobile)
    LinearLayout llmobile;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llpassword)
    LinearLayout llpassword;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llconfirmpass)
    LinearLayout llconfirmpass;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cblength)
    CheckBox cblength;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cblowcase)
    CheckBox cblowcase;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cbupper)
    CheckBox cbupper;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cbnumber)
    CheckBox cbnumber;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cbsplchar)
    CheckBox cbsplchar;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spinknow)
    Spinner spinknow;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvspinknow)
    TextView tvspinknow;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etknow)
    EditText etknow;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvknowother)
    TextView tvknowother;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivpasseye)
    ImageView ivpasseye;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivconfirmeye)
    ImageView ivconfirmeye;


    String[] OrgTypearry = {"Select Type", "Temple", "NGO"};
    ArrayList<String> statelist, orgtypelist;
    List<Listmodel> list;
    View view;
    ArrayList<String> sublist, knowlist, orgknowlist;
    String subcategory, stknowothers;
    GifImageView loading_gif;

    TextWatcher ettextwatcher;

    int score = 0;
    // boolean indicating if password has an upper case
    boolean upper = false;
    // boolean indicating if password has a lower case
    boolean lower = false;
    // boolean indicating if password has at least one digit
    boolean digit = false;
    // boolean indicating if password has a leat one special char
    boolean specialChar = false;
    int minimumtotal = 8;

    String appVersion, androidVersion, uuid, device_token;

    public static String latLong = "";
    public static String addressLat = "", addressLong = "";

    // organzation Wallet ID's


    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.etorgname)
    EditText etorgname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etorgaddress)
    EditText etorgaddress;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etorgcity)
    EditText etorgcity;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etorgzip)
    EditText etorgzip;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etorgregno)
    EditText etorgregno;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etorgdesc)
    EditText etorgdesc;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.etothname)
    EditText etothname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etothemail)
    EditText etothemail;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etothmobile)
    EditText etothmobile;

 /*   @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etothpassword)
    EditText etothpassword;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etothconfirmpass)
    EditText etothconfirmpass;*/

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etothknow)
    EditText etothknow;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvorgterms)
    TextView tvorgterms;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.orgcblength)
    CheckBox orgcblength;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.orgcblowercase)
    CheckBox orgcblowercase;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.orgcbupper)
    CheckBox orgcbupper;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.orgcbnumber)
    CheckBox orgcbnumber;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.orgcbsplchar)
    CheckBox orgcbsplchar;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvorgname)
    TextView tvorgname;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvorgaddress)
    TextView tvorgaddress;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvorgcity)
    TextView tvorgcity;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvorgstate)
    TextView tvorgstate;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvorgzip)
    TextView tvorgzip;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvorgregno)
    TextView tvorgregno;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvorgdec)
    TextView tvorgdec;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvorgpic)
    TextView tvorgpic;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvorgdoc)
    TextView tvorgdoc;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvothname)
    TextView tvothname;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvothemail)
    TextView tvothemail;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvothmobile)
    TextView tvothmobile;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvothpass)
    TextView tvothpass;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvothconfirmpass)
    TextView tvothconfirmpass;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvspinhowknow)
    TextView tvspinhowknow;


    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvothknow)
    TextView tvothknow;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivorgmg)
    ImageView ivorgmg;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivorgre)
    ImageView ivorgre;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivorgpass)
    ImageView ivorgpass;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivorgconfirmpass)
    ImageView ivorgconfirmpass;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etorgstate)
    Spinner etorgstate;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spinorgtype)
    Spinner spinorgtype;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spinothherd)
    Spinner spinothherd;
    TextView upload1, upload2;
    String state_id, st_statelist, state;
    LinearLayout ll_personal_wallet, ll_oranization_wallet;
    String storgname, storgtype, storgsendtype, storgaddress, storgcity, storgzip, storgdic, storgtypenumber,
            storgregno, stopname, stopemail, stopmobile, stoppassword, stopconfirmpassword, storgknowother;

    RequestBody rbstorgname, rbknow, rbotherknow, rbstorgaddress, rbstorgcity, rbstate_id, rqroltype,
            rbstorgzip, rbstorgdic, rbstorgregno, rbstorgtypenumber, rbstopname, rbstopemail, rbstopmobile,
            rbstoppassword, rbstopconfirmpassword, rbandroidversion, rbvia, rbdtoken, rbuuid, rbdtype, rbappversiod,
            opdevicetoken, opdeviceip, opdeviceid, oplatitude, oplongitude, opzipcode, opaddress;

    File orgfile, orgdocfile;
    Uri orguri, orgdocuri;
    RequestBody orgrequestBody, orgdocgrequestBody;
    List<MultipartBody.Part> orgpart, orgdocparts;
    private static final int CAMERA_REQUEST = 1887;
    //    private static final int ORGIMG = 1888;
    private static final int ORGDOCCAMERA_REQUEST = 1889;
    private static final int STORAGE_CODE = 100;
    private static final int STORAGE_CODE1 = 101;
    ScrollView scrollview;
    Bundle bundle;
    String name = "", email = "", mobile = "";
    String deviceId, wifiIp, mobileIp;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 3;
    // check if GPS enabled
    private GPSTracker gpsTracker = new GPSTracker(RegistationActivity.this);
    private final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private final int INITIAL_REQUEST = 250;   //1337
    private final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    double latitude_current = 0.0;
    double longitude_current = 0.0;


    String latitude, longitude;

    String deviceip, deviceid, zipcode, address;

    @Override
    protected int getLayoutId() {
        return R.layout.registation;
    }

    @SuppressLint({"ResourceAsColor", "NewApi"})
    @Override
    protected void initView() {
        ButterKnife.bind(this);

        ll_oranization_wallet = findViewById(R.id.organization_wallet);
        ll_personal_wallet = findViewById(R.id.personal_wallet);
        loading_gif = findViewById(R.id.loading_gif);
        upload1 = findViewById(R.id.upload1);
        upload2 = findViewById(R.id.upload2);

        bundle = getIntent().getExtras();


        if (bundle != null) {
            name = bundle.getString("Gname");
            email = bundle.getString("Gemail");
            mobile = bundle.getString("mobile");
        }


        stname = name;
        stemail = email;
        stmobile = mobile;

        etname.setText(stname);
        etemail.setText(stemail);
        etmobile.setText(stmobile);

        LocationTracker.getInstance().fillContext(getApplicationContext());
        LocationTracker.getInstance().startLocation();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setHint();
        ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, signup_type);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_signuptype.setAdapter(spinner_adapter);
        spinnerclick();
        ArrayAdapter spinnerknow_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinknowarray);
        spinnerknow_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinknow.setAdapter(spinnerknow_adapter);
        spinknowclick();

        submitbuttonvisible(view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // setTitle("Forgot Password");
        //  toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (task.isSuccessful()) {
                            device_token = task.getResult().getToken(); // device token
                            Log.e("Device Tocken", "device token: Token: " + device_token);
                        } else {
                            //  Toast.makeText(RegistationActivity.this, "Token generation Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        uuid = UUID.randomUUID().toString();
        Log.e("uuid", uuid);//device UUID

        appVersion = String.valueOf(Build.VERSION.SDK_INT);
        Log.e("sdkVersion", appVersion);//app version
        // String BrandName = MAN;      // Manufacturer will come I think, Correct me if I am wrong :)  Brand name like Samsung or Mircomax

        androidVersion = Build.VERSION.RELEASE;
        Log.e("osVersion", androidVersion);
        //android version
//        iv_back = findViewById(R.id.iv_back);
//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RegistationActivity.this, WelcomeActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//            }
//        });

        etname.addTextChangedListener(textWatcher);
        etemail.addTextChangedListener(textWatcher);
        etmobile.addTextChangedListener(textWatcher);
        // etpassword.addTextChangedListener(textWatcher);
        //  etconfirmpassword.addTextChangedListener(textWatcher);

        apiDao = ApiClient.getClient(rs_accesstoken).create(ApiDao.class);

       /* ettextwatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && !s.toString().equalsIgnoreCase("")) {

                    if (etpassword.getText().hashCode() == s.hashCode()) {

                        etpassword.removeTextChangedListener(ettextwatcher);
                        etpassword.addTextChangedListener(ettextwatcher);


                        if (etpassword.length() == 0) {
                            cbupper.setChecked(false);
                            cblowcase.setChecked(false);
                            cblength.setChecked(false);
                            cbnumber.setChecked(false);
                            cbsplchar.setChecked(false);
                        } else {


                            calculatepasswrd(s.toString());
                        }


                    }

                } else {
                    cbupper.setChecked(false);
                    cblowcase.setChecked(false);
                    cblength.setChecked(false);
                    cbnumber.setChecked(false);
                    cbsplchar.setChecked(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                upper = false;
                lower = false;
                digit = false;
                specialChar = false;
                minimumtotal = 8;
            }
        };
        etpassword.addTextChangedListener(ettextwatcher);

        ivpasseye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ((ImageView) (v)).setImageResource(R.drawable.eye);
                    //Show Password
                    etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    ((ImageView) (v)).setImageResource(R.drawable.eye_off);
                    //Hide Password
                    etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });
        ivconfirmeye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etconfirmpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    etconfirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ((ImageView) (v)).setImageResource(R.drawable.eye);
                    //Show Password
                    etconfirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    ((ImageView) (v)).setImageResource(R.drawable.eye_off);
                    //Hide Password
                    etconfirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });*/

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        wifiIp = getWifiIPAddress();
        mobileIp = getMobileIPAddress();

        Log.e("deviceId", "" + deviceId);
        Log.e("wifiIp", "" + wifiIp);
        Log.e("mobileIp", "" + mobileIp);
        deviceid = deviceId;
        deviceip = wifiIp + "," + mobileIp;

        if (ActivityCompat.checkSelfPermission(
                RegistationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                RegistationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            getLocation();
        }
    }

    public static String getMobileIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    public String getWifiIPAddress() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return Formatter.formatIpAddress(ip);
    }

    public void setHint() {
        //Money User
        etname.setHint(Html.fromHtml(getString(R.string.user_name)));
        etemail.setHint(Html.fromHtml(getString(R.string.user_email)));
        etmobile.setHint(Html.fromHtml(getString(R.string.phone_number)));
        //  etpassword.setHint(Html.fromHtml(getString(R.string.password)));
        //    etconfirmpassword.setHint(Html.fromHtml(getString(R.string.confirm_password)));

        //org wallet
        etothname.setHint(Html.fromHtml(getString(R.string.user_name)));
        etothemail.setHint(Html.fromHtml(getString(R.string.user_email)));
        etothmobile.setHint(Html.fromHtml(getString(R.string.phone_number)));
        // etothpassword.setHint(Html.fromHtml(getString(R.string.password)));
        //  etothconfirmpass.setHint(Html.fromHtml(getString(R.string.confirm_password)));

        etorgname.setHint(Html.fromHtml(getString(R.string.organization_name)));
        etorgaddress.setHint(Html.fromHtml(getString(R.string.organization_address)));
        etorgcity.setHint(Html.fromHtml(getString(R.string.organization_city)));
        etorgzip.setHint(Html.fromHtml(getString(R.string.organization_zipcode)));
        etorgregno.setHint(Html.fromHtml(getString(R.string.organization_registration_number)));
        etorgdesc.setHint(Html.fromHtml(getString(R.string.organization_description)));
        upload1.setText(Html.fromHtml(getString(R.string.upload_org_photo)));
        upload2.setText(Html.fromHtml(getString(R.string.upload_organization_document)));
    }


    @SuppressLint("NewApi")
    public void submitbuttonvisible(View view) {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            stname = etname.getText().toString().trim();
            stemail = etemail.getText().toString().trim();
            stmobile = etmobile.getText().toString().trim();
            // stpassword = etpassword.getText().toString().trim();
            //  stconfirmpassword = etconfirmpassword.getText().toString().trim();
            if (stname.equals("") || stemail.equals("") || stmobile.equals("") /*|| stpassword.equals("") || stconfirmpassword.equals("")*/) {
                btn_signup.setEnabled(false);
                btn_signup.setBackgroundResource(R.drawable.backgroundvisablity);
            } else {
                btn_signup.setEnabled(true);
                btn_signup.setBackgroundResource(R.drawable.button_background);
                btn_signup.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gold)));
                //  validations(view);
            }
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //  calculatepasswrd(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            submitbuttonvisible(view);
        }

    };

    public void calculatepasswrd(String str) {

        String verify = str;

        for (int k = 0; k < verify.length(); k++) {
            char c = verify.charAt(k);

            if (!specialChar && !Character.isLetterOrDigit(c)) {
                score++;
                specialChar = true;
                if (specialChar) {
                    cbsplchar.setChecked(true);
                } else {
                    cbsplchar.setChecked(false);
                }

            } else {
                if (!digit && Character.isDigit(c)) {
                    score++;
                    digit = true;
                    cbnumber.setChecked(true);
                } else {
                    if (!upper || !lower) {
                        if (Character.isUpperCase(c)) {
                            upper = true;
                            cbupper.setChecked(true);

                        } else if (Character.isLowerCase(c)) {
                            lower = true;
                            cblowcase.setChecked(true);
                        }

                        if (upper && lower) {
                            score++;
                        }

                    }
                }
                if (verify.length() >= minimumtotal) {
                    cblength.setChecked(true);

                } else {
                    cblength.setChecked(false);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    /*    ApiClient.changeApiBaseUrl("https://staging-api.dev.goldsikka.in/");
        Log.e("backurl", "" + BASE_URL);*/
        super.onBackPressed();

        // finish();
    }

    public void spinknowclick() {
        knowlist = new ArrayList<>();
        spinknow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stknow = spinknow.getItemAtPosition(spinknow.getSelectedItemPosition()).toString();
                ((TextView) view).setTextColor(ContextCompat.getColor(RegistationActivity.this, R.color.DarkBrown));
                if (stknow.equals("1. Google Search")) {
                    rspknow = "1";
                    etknow.setVisibility(View.GONE);
                    tvknowother.setVisibility(View.GONE);
                } else if (stknow.equals("2. Goldsikka Representative")) {
                    rspknow = "2";
                    etknow.setVisibility(View.GONE);
                    tvknowother.setVisibility(View.GONE);

                } else if (stknow.equals("3. Social Media Add")) {
                    rspknow = "3";
                    etknow.setVisibility(View.GONE);
                    tvknowother.setVisibility(View.GONE);
                } else if (stknow.equals("4. Radio Add")) {
                    rspknow = "4";
                    etknow.setVisibility(View.GONE);
                    tvknowother.setVisibility(View.GONE);
                } else if (stknow.equals("5. Word of Mouth")) {
                    rspknow = "5";
                    etknow.setVisibility(View.GONE);
                    tvknowother.setVisibility(View.GONE);
                } else if (stknow.equals("6.others")) {
                    rspknow = "6";
                    etknow.setVisibility(View.VISIBLE);

                } else if (stknow.equals("How did you hear about us?")) {
                    etknow.setVisibility(View.GONE);
                    tvknowother.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void spinnerclick() {
        sublist = new ArrayList<>();
        // spinner_signuptype = findViewById(R.id.sub_category);

        spinner_signuptype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subcategory = spinner_signuptype.getItemAtPosition(spinner_signuptype.getSelectedItemPosition()).toString();
                //spinner_signuptype.getSelectedView().setTextColor(getResources().getColor(R.color.Blue));
                ((TextView) view).setTextColor(ContextCompat.getColor(RegistationActivity.this, R.color.DarkBrown));

                if (subcategory.equals("Personal Wallet")) {
                    rs_roleid = "1";
                    ll_oranization_wallet.setVisibility(View.GONE);
                    ll_personal_wallet.setVisibility(View.VISIBLE);
                    rqroltype = RequestBody.create(MediaType.parse("text/plain"), rs_roleid);

                } else if (subcategory.equals("Organization Wallet")) {
                    rs_roleid = "2";
                    rqroltype = RequestBody.create(MediaType.parse("text/plain"), rs_roleid);

                    ArrayAdapter genderadapter = new ArrayAdapter(RegistationActivity.this, android.R.layout.simple_spinner_item, OrgTypearry);
                    genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinorgtype.setAdapter(genderadapter);
                    orgtypespinclick();
                    get_statelist();
                    ArrayAdapter spinnerknow_adapter = new ArrayAdapter(RegistationActivity.this, android.R.layout.simple_spinner_item, spinknowarray);
                    spinnerknow_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinothherd.setAdapter(spinnerknow_adapter);
                    orgspinknowclick();
                    //  orgtextchangepass();
                    ll_oranization_wallet.setVisibility(View.VISIBLE);
                    ll_personal_wallet.setVisibility(View.GONE);

                 /*   ivorgpass.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etothpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                                etothpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                ((ImageView) (v)).setImageResource(R.drawable.eye);
                                //Show Password
                                etothpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            } else {
                                ((ImageView) (v)).setImageResource(R.drawable.eye_off);
                                //Hide Password
                                etothpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            }

                        }
                    });
                    ivorgconfirmpass.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etothconfirmpass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                                etothconfirmpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                ((ImageView) (v)).setImageResource(R.drawable.eye);
                                //Show Password
                                etothconfirmpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            } else {
                                ((ImageView) (v)).setImageResource(R.drawable.eye_off);
                                //Hide Password
                                etothconfirmpass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                            }
                        }
                    });
*/
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tv_terms)
    public void open_termscondition() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            open_terms_condition("https://goldsikka.com/privacy-policy");
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tvorgterms)
    public void tvorgterms() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            open_terms_condition("https://goldsikka.com/privacy-policy");
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tvpolicy)
    public void policy() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            open_terms_condition("https://goldsikka.com/privacy-policy");
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tvorgpolicy)
    public void tvorgpolicy() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            open_terms_condition("https://goldsikka.com/privacy-policy");
        }
    }


    public void open_terms_condition(String link) {

        String uri = String.format(Locale.ENGLISH, link);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_signup)
    public void validation(View view) {
        registermovetoOtp(view);
    }

    public void registermovetoOtp(View view) {
        tv_mobilel.setVisibility(View.GONE);
        tv_email.setVisibility(View.GONE);
        tv_name.setVisibility(View.GONE);
        tv_password.setVisibility(View.GONE);
        tv_conforimpassword1.setVisibility(View.GONE);

        btn_signup.setVisibility(View.GONE);
        loading_gif.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                RegistationActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(RegistationActivity.this)) {
                            ToastMessage.onToast(RegistationActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            return;
                        } else {
                            validations(view);
                            btn_signup.setVisibility(View.VISIBLE);
                            loading_gif.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }, 500);
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Log.d("TAG", "Permission Granted ");

            if (requestCode == 1) {
                showotherimges();
            } else if (requestCode == 2) {
                showorgdocChooser();
            } else if (requestCode == 101) {
                registermovetoOtp(view);
            } else if (requestCode == REQUEST_LOCATION) {
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
        } else if (requestCode == 101) {
            registermovetoOtp(view);
        } else {
            Toast.makeText(this, "Permissions are mandatory", Toast.LENGTH_SHORT).show();

        }

       /* if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission Granted ");
            switch (requestCode) {
                case 1:
                    showChooser();
                    break;
                case 2:
                    showweddingChooser();
                    break;
                case 3:
                    showotherimges();
                    break;
            }
        } else {
            Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
        }*/


    }

    public void validations(View view) {

        stname = etname.getText().toString().trim();

        stemail = etemail.getText().toString().trim();
        stmobile = etmobile.getText().toString().trim();
        // stpassword = etpassword.getText().toString().trim();
        //stconfirmpassword = etconfirmpassword.getText().toString().trim();
        streferralcode = etreferralcode.getText().toString().trim();
        stknowothers = etknow.getText().toString().trim();

        tv_mobilel.setVisibility(View.GONE);
        tv_email.setVisibility(View.GONE);
        tv_name.setVisibility(View.GONE);
        tv_password.setVisibility(View.GONE);
        tv_conforimpassword1.setVisibility(View.GONE);
        tvspinknow.setVisibility(View.GONE);
        tvknowother.setVisibility(View.GONE);

        llname.setBackgroundResource(R.drawable.edittext_border);
        llemail.setBackgroundResource(R.drawable.edittext_border);
        llmobile.setBackgroundResource(R.drawable.edittext_border);
        llpassword.setBackgroundResource(R.drawable.edittext_border);
        llconfirmpass.setBackgroundResource(R.drawable.edittext_border);

        if (subcategory.equals("Select Wallet")) {

            ToastMessage.onToast(this, "Please Select Wallet", ToastMessage.ERROR);
            // Toast.makeText(this, "Please Select Wallet", Toast.LENGTH_SHORT).show();
        } else if (stname.isEmpty()) {
            llname.setBackgroundResource(R.drawable.edittext_errorborder);
            tv_name.setVisibility(View.VISIBLE);
            tv_name.setText("The name field is required.");
        } else if (stemail.isEmpty()) {
            llemail.setBackgroundResource(R.drawable.edittext_errorborder);
            tv_email.setVisibility(View.VISIBLE);
            tv_email.setText("The email field is required");
        } else if (!stemail.matches(emailPattern)) {
            llemail.setBackgroundResource(R.drawable.edittext_errorborder);
            tv_email.setVisibility(View.VISIBLE);
            tv_email.setText("The Valid email is required. ");
            // Toast.makeText(getApplicationContext(), "The email field is required. ", Toast.LENGTH_SHORT).show();
        } else if (stmobile.isEmpty()) {
            llmobile.setBackgroundResource(R.drawable.edittext_errorborder);
            tv_mobilel.setVisibility(View.VISIBLE);
            tv_mobilel.setText("The mobile field is required.");
        } else if (!etmobile.getText().toString().matches(MobilePattern)) {
            llmobile.setBackgroundResource(R.drawable.edittext_errorborder);
            tv_mobilel.setVisibility(View.VISIBLE);
            tv_mobilel.setText("Valid mobile Number is required.");
            // Toast.makeText(getApplicationContext(), "The mobile field is required.", Toast.LENGTH_SHORT).show();
        }/* else if (stpassword.isEmpty()) {
            llpassword.setBackgroundResource(R.drawable.edittext_errorborder);
            tv_password.setVisibility(View.VISIBLE);
            tv_password.setText("The password field is required.");
            //Toast.makeText(this, "The password field is required.", Toast.LENGTH_SHORT).show();
        } else if (!stpassword.matches(PASSWORD_PATTERN)) {
            llpassword.setBackgroundResource(R.drawable.edittext_errorborder);
            //  etpassword.setError("Password Must Have 8 Characters");
            ToastMessage.onToast(this, " Password Should Have 1 uppercase, 1 lowercase \n 1 Numerical And special Character", ToastMessage.NOTIFY);
        } else if (stconfirmpassword.isEmpty()) {
            llconfirmpass.setBackgroundResource(R.drawable.edittext_errorborder);
            tv_conforimpassword1.setVisibility(View.VISIBLE);
            tv_conforimpassword1.setText("The confirm password field is required.");
            //Toast.makeText(this, "The confirm password field is required.", Toast.LENGTH_SHORT).show();
        } else if (!stconfirmpassword.equals(stpassword)) {
            llconfirmpass.setBackgroundResource(R.drawable.edittext_errorborder);
            tv_conforimpassword1.setVisibility(View.VISIBLE);
            tv_conforimpassword1.setText("Password Must Match");
            //Toast.makeText(this, "The confirm password field is required.", Toast.LENGTH_SHORT).show();
        }*/ else if (stknow.equals("How did you hear about us?")) {
            tvspinknow.setText("Filed is required ");
            tvspinknow.setVisibility(View.VISIBLE);

        } else {
            btn_signup.setEnabled(true);

            Call<Listmodel> call = apiDao.getsignup(stname, stemail, stmobile, "Sample@123", "Sample@123",
                    rs_roleid, streferralcode, "1", rspknow, stknowothers,
                    "Android", appVersion, androidVersion, deviceid,
                    device_token, deviceip, deviceid, latitude, longitude, zipcode, address);
            responsemethod(call);
        }
    }

    @Override
    public void responce(@NonNull Response<Listmodel> response, int stauscode) {


        if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
            List<Listmodel> list = Collections.singletonList(response.body());
            for (Listmodel listmodel : list) {
                rs_roleid = listmodel.getRole_id();
                rs_accesstoken = listmodel.getAccessToken();
                rs_name = listmodel.getName();
                rs_maksedphoone = listmodel.getMaskedPhone();
                rs_verifytoken = listmodel.getVerifyToken();
                roleId = listmodel.getRoleId();
                onsucess();
            }

        } else {
            try {
                tv_mobilel.setVisibility(View.GONE);
                tv_email.setVisibility(View.GONE);
                tv_name.setVisibility(View.GONE);
                tv_password.setVisibility(View.GONE);
                tv_conforimpassword1.setVisibility(View.GONE);
                tvknowother.setVisibility(View.GONE);

                assert response.errorBody() != null;
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                String st = jObjError.getString("message");
                //ToastMessage.onToast(this, st, ToastMessage.ERROR);
                JSONObject er = jObjError.getJSONObject("errors");
                try {
                    JSONArray array_mobile = er.getJSONArray("mobile");
                    for (int i = 0; i < array_mobile.length(); i++) {
                        //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                        tv_mobilel.setVisibility(View.VISIBLE);
                        tv_mobilel.setText(array_mobile.getString(i));
                        //  Log.e("email",array_mobile.toString());
                        //   etmobile.setError(array_mobile.getString(i));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray array_email = er.getJSONArray("email");
                    for (int i1 = 0; i1 < array_email.length(); i1++) {
                        tv_email.setVisibility(View.VISIBLE);
                        //   etemail.setError(array_email.getString(i).toString());
                        tv_email.setText(array_email.getString(i1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    JSONArray array_name = er.getJSONArray("name");

                    for (int i = 0; i < array_name.length(); i++) {
                        tv_name.setVisibility(View.VISIBLE);
                        tv_name.setText(array_name.getString(i));
                        //  etname.setError(array_name.getString(i).toString());
                    }
//
                } catch (Exception e) {
                    e.printStackTrace();

                }

               /* try {
                    JSONArray array_password = er.getJSONArray("password");


                    for (int i = 0; i < array_password.length(); i++) {
                        tv_password.setVisibility(View.VISIBLE);
                        //   etpassword.setError(array_password.getString(i).toString());
                        tv_password.setText(array_password.getString(i));
                    }
//
                } catch (Exception e) {
                    e.printStackTrace();

                }
                try {
                    JSONArray confirmpasswods = er.getJSONArray("confirmPassword");
                    for (int i = 0; i < confirmpasswods.length(); i++) {
                        tv_conforimpassword1.setVisibility(View.VISIBLE);
                        tv_conforimpassword1.setText(confirmpasswods.getString(i));
                        // etconfirmpassword.setError(confirmpasswods.getString(i).toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                try {
                    JSONArray array_roltype = er.getJSONArray("roleType");

                    for (int i = 0; i < array_roltype.length(); i++) {
                        tv_signuptype.setVisibility(View.VISIBLE);
                        tv_signuptype.setText(array_roltype.getString(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray array_roltype = er.getJSONArray("heard_others_text");

                    for (int i = 0; i < array_roltype.length(); i++) {
                        tvknowother.setVisibility(View.VISIBLE);
                        tvknowother.setText(array_roltype.getString(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray array_roltype = er.getJSONArray("referral_code");

                    for (int i = 0; i < array_roltype.length(); i++) {
                        tv_refferl_error.setVisibility(View.VISIBLE);
                        tv_refferl_error.setText(array_roltype.getString(i));
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
    public void listresponce(@NonNull Response<List<Listmodel>> response, int stauscode) {

    }

    public void onsucess() {
        AccountUtils.setAccessToken(this, rs_accesstoken);
        // AccountUtils.setPassword(this, stpassword);
        AccountUtils.setRoleid(this, roleId);
        Intent intent = new Intent(RegistationActivity.this, OTPActivity.class);
        intent.putExtra("mobile", stmobile);
        intent.putExtra("credential", "signup");
        intent.putExtra("accesstoken", rs_accesstoken);
        intent.putExtra("verifytoken", rs_verifytoken);
        intent.putExtra("roleId", roleId);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btorgsubmit)
    public void orgvalidation() {
        scrollview = findViewById(R.id.scrollview);
        tvothknow.setVisibility(View.GONE);
        tvspinhowknow.setVisibility(View.GONE);
        tvothconfirmpass.setVisibility(View.GONE);
        tvothpass.setVisibility(View.GONE);
        tvothmobile.setVisibility(View.GONE);
        tvothemail.setVisibility(View.GONE);
        tvothname.setVisibility(View.GONE);
        tvorgdoc.setVisibility(View.GONE);
        tvorgpic.setVisibility(View.GONE);
        tvorgdec.setVisibility(View.GONE);
        tvorgregno.setVisibility(View.GONE);
        tvorgzip.setVisibility(View.GONE);
        tvorgstate.setVisibility(View.GONE);
        tvorgcity.setVisibility(View.GONE);
        tvorgaddress.setVisibility(View.GONE);
        tvorgname.setVisibility(View.GONE);

        storgname = etorgname.getText().toString().trim();


        storgaddress = etorgaddress.getText().toString().trim();

        storgcity = etorgcity.getText().toString().trim();


        storgzip = etorgzip.getText().toString().trim();

        storgdic = etorgdesc.getText().toString().trim();

        storgregno = etorgregno.getText().toString().trim();

        stopname = etothname.getText().toString().trim();


        stopemail = etothemail.getText().toString().trim();


        stopmobile = etothmobile.getText().toString().trim();

        //  stoppassword = etothpassword.getText().toString().trim();

        // stopconfirmpassword = etothconfirmpass.getText().toString().trim();

        storgknowother = etothknow.getText().toString().trim();


        if (subcategory.equals("Select Wallet")) {

            ToastMessage.onToast(this, "Please Select Wallet", ToastMessage.ERROR);
            // Toast.makeText(this, "Please Select Wallet", Toast.LENGTH_SHORT).show();
        } else if (storgtype.equals("Select Type")) {
            ToastMessage.onToast(this, "Please select the organization Type", ToastMessage.ERROR);
        } else if (storgname.isEmpty()) {
            tvorgname.setVisibility(View.VISIBLE);
            tvorgname.setText("The organization Name field is required");
            scrollview.smoothScrollTo(0, scrollview.getTop());

        } else if (storgaddress.isEmpty()) {

            tvorgaddress.setVisibility(View.VISIBLE);
            tvorgaddress.setText("The organization Address is required. ");
            scrollview.smoothScrollTo(0, scrollview.getTop());

            // Toast.makeText(getApplicationContext(), "The email field is required. ", Toast.LENGTH_SHORT).show();
        } else if (storgcity.isEmpty()) {
            tvorgcity.setVisibility(View.VISIBLE);
            tvorgcity.setText("The organization city field is required.");
            scrollview.smoothScrollTo(0, scrollview.getTop());

        } else if (st_statelist.equals("Select State")) {
            tvorgstate.setVisibility(View.VISIBLE);
            tvorgstate.setText("Please Select the State.");
            scrollview.smoothScrollTo(0, scrollview.getTop());

        } else if (storgzip.isEmpty()) {
            tvorgzip.setVisibility(View.VISIBLE);
            tvorgzip.setText("The ZipCode field is required");
            scrollview.smoothScrollTo(0, scrollview.getTop());

        } else if (storgregno.isEmpty()) {
            tvorgregno.setVisibility(View.VISIBLE);
            tvorgregno.setText("The Reg.No feild id required.");
            scrollview.smoothScrollTo(0, scrollview.getTop());

        } else if (storgdic.isEmpty()) {
            tvorgdec.setVisibility(View.VISIBLE);
            tvorgdec.setText("The Description feild is required. ");
            scrollview.smoothScrollTo(0, scrollview.getTop());

            // Toast.makeText(getApplicationContext(), "The email field is required. ", Toast.LENGTH_SHORT).show();
        } else if (stopname.isEmpty()) {
            tvothname.setVisibility(View.VISIBLE);
            tvothname.setText("The Name feild is required. ");
            scrollview.smoothScrollTo(0, scrollview.getBottom());

            // Toast.makeText(getApplicationContext(), "The email field is required. ", Toast.LENGTH_SHORT).show();
        } else if (stopemail.isEmpty()) {
            tvothemail.setVisibility(View.VISIBLE);
            tvothemail.setText("The Email feild is required. ");
            scrollview.smoothScrollTo(0, scrollview.getBottom());

            // Toast.makeText(getApplicationContext(), "The email field is required. ", Toast.LENGTH_SHORT).show();
        } else if (!stopemail.matches(emailPattern)) {
            tvothemail.setVisibility(View.VISIBLE);
            tvothemail.setText("The Valid email is required. ");
            scrollview.smoothScrollTo(0, scrollview.getBottom());

            // Toast.makeText(getApplicationContext(), "The email field is required. ", Toast.LENGTH_SHORT).show();
        } else if (stopmobile.isEmpty()) {
            tvothmobile.setVisibility(View.VISIBLE);
            tvothmobile.setText("The mobile field is required.");
            scrollview.smoothScrollTo(0, scrollview.getBottom());

        } else if (!etothmobile.getText().toString().matches(MobilePattern)) {
            tvothmobile.setVisibility(View.VISIBLE);
            tvothmobile.setText("Valid mobile Number is required.");
            scrollview.smoothScrollTo(0, scrollview.getBottom());

            // Toast.makeText(getApplicationContext(), "The mobile field is required.", Toast.LENGTH_SHORT).show();
        } /*else if (stoppassword.isEmpty()) {
            tvothpass.setVisibility(View.VISIBLE);
            tvothpass.setText("The password field is required.");
            scrollview.smoothScrollTo(0, scrollview.getBottom());

            //Toast.makeText(this, "The password field is required.", Toast.LENGTH_SHORT).show();
        } else if (!stoppassword.matches(PASSWORD_PATTERN)) {
            //  etpassword.setError("Password Must Have 8 Characters");
            ToastMessage.onToast(this, " Password Should Have 1 uppercase, 1 lowercase \n 1 Numerical And special Character", ToastMessage.NOTIFY);
            scrollview.smoothScrollTo(0, scrollview.getBottom());

        } else if (stopconfirmpassword.isEmpty()) {
            tvothconfirmpass.setVisibility(View.VISIBLE);
            tvothconfirmpass.setText("The confirm password field is required.");
            scrollview.smoothScrollTo(0, scrollview.getBottom());

            //Toast.makeText(this, "The confirm password field is required.", Toast.LENGTH_SHORT).show();
        }*/ else if (stknow.equals("How did you hear about us?")) {
            tvspinhowknow.setText("Filed is required ");
            tvspinhowknow.setVisibility(View.VISIBLE);

        } else {

            rbstorgname = RequestBody.create(MediaType.parse("text/plain"), storgname);
            rbstorgaddress = RequestBody.create(MediaType.parse("text/plain"), storgaddress);
            rbstorgcity = RequestBody.create(MediaType.parse("text/plain"), storgcity);
            rbstorgzip = RequestBody.create(MediaType.parse("text/plain"), storgzip);
            rbstorgdic = RequestBody.create(MediaType.parse("text/plain"), storgdic);
            rbstorgregno = RequestBody.create(MediaType.parse("text/plain"), storgregno);
            rbstopemail = RequestBody.create(MediaType.parse("text/plain"), stopemail);
            rbstopname = RequestBody.create(MediaType.parse("text/plain"), stopname);
            rbstopmobile = RequestBody.create(MediaType.parse("text/plain"), stopmobile);
            rbstoppassword = RequestBody.create(MediaType.parse("text/plain"), "Sample@123");
            rbstopconfirmpassword = RequestBody.create(MediaType.parse("text/plain"), "Sample@123");
            rbotherknow = RequestBody.create(MediaType.parse("text/plain"), storgknowother);

            rbandroidversion = RequestBody.create(MediaType.parse("text/plain"), androidVersion);
            rbvia = RequestBody.create(MediaType.parse("text/plain"), "1");
            rbdtoken = RequestBody.create(MediaType.parse("text/plain"), device_token);
            rbuuid = RequestBody.create(MediaType.parse("text/plain"), deviceid);
            rbdtype = RequestBody.create(MediaType.parse("text/plain"), "Android");
            rbappversiod = RequestBody.create(MediaType.parse("text/plain"), appVersion);
            opdevicetoken = RequestBody.create(MediaType.parse("text/plain"), device_token);
            opdeviceip = RequestBody.create(MediaType.parse("text/plain"), deviceip);
            opdeviceid = RequestBody.create(MediaType.parse("text/plain"), deviceid);
            oplatitude = RequestBody.create(MediaType.parse("text/plain"), latitude);
            oplongitude = RequestBody.create(MediaType.parse("text/plain"), longitude);
            opzipcode = RequestBody.create(MediaType.parse("text/plain"), zipcode);
            opaddress = RequestBody.create(MediaType.parse("text/plain"), address);

            sendregdata();
        }

    }


    public void orgtypespinclick() {
        orgtypelist = new ArrayList<>();
        // spinner_signuptype = findViewById(R.id.sub_category);

        spinorgtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storgtype = spinorgtype.getItemAtPosition(spinorgtype.getSelectedItemPosition()).toString();
                //spinner_signuptype.getSelectedView().setTextColor(getResources().getColor(R.color.Blue));
                ((TextView) view).setTextColor(ContextCompat.getColor(RegistationActivity.this, R.color.DarkBrown));

                if (storgtype.equals("Temple")) {
                    storgtypenumber = "1";
                    rbstorgtypenumber = RequestBody.create(MediaType.parse("text/plain"), storgtypenumber);

                } else if (storgtype.equals("NGO")) {
                    storgtypenumber = "2";
                    rbstorgtypenumber = RequestBody.create(MediaType.parse("text/plain"), storgtypenumber);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }

    public void get_statelist() {
        statelist = new ArrayList<>();

        load_state_data();

        etorgstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(ContextCompat.getColor(RegistationActivity.this, R.color.DarkBrown));

                st_statelist = etorgstate.getItemAtPosition(etorgstate.getSelectedItemPosition()).toString();

                if (!st_statelist.equals("Select State")) {
                    Listmodel listmodel = list.get(i);
                    state_id = listmodel.getId();
                    rbstate_id = RequestBody.create(MediaType.parse("text/plain"), state_id);
//                    for (Listmodel listmodel:list){
                    Log.e("stateid..", state_id);
                    state = listmodel.getName();
                    Log.e("state..", st_statelist);

                } else {

                    ToastMessage.onToast(RegistationActivity.this, "Please Select State", ToastMessage.ERROR);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

    }

    public void load_state_data() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<List<Listmodel>> get_address = apiDao.get_states("Bearer " + AccountUtils.getAccessToken(this));
            get_address.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    list = (response.body());
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        for (Listmodel listmodel : list) {
                            dialog.dismiss();
                            statelist.add(listmodel.getName());
                        }
                        etorgstate.setAdapter(new ArrayAdapter<String>(RegistationActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, statelist));

                    }
                }


                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                    // ToastMessage.onToast(RegistationActivity.this, "We Have Some Issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }

    }

    public void orgspinknowclick() {
        orgknowlist = new ArrayList<>();
        spinothherd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stknow = spinothherd.getItemAtPosition(spinothherd.getSelectedItemPosition()).toString();
                ((TextView) view).setTextColor(ContextCompat.getColor(RegistationActivity.this, R.color.DarkBrown));
                if (stknow.equals("1. Google Search")) {
                    rspknow = "1";
                    rbknow = RequestBody.create(MediaType.parse("text/plain"), rspknow);

                    etothknow.setVisibility(View.GONE);
                    tvothknow.setVisibility(View.GONE);
                } else if (stknow.equals("2. Goldsikka Representative")) {
                    rspknow = "2";
                    rbknow = RequestBody.create(MediaType.parse("text/plain"), rspknow);
                    etothknow.setVisibility(View.GONE);
                    tvothknow.setVisibility(View.GONE);

                } else if (stknow.equals("3. Social Media Add")) {
                    rspknow = "3";
                    rbknow = RequestBody.create(MediaType.parse("text/plain"), rspknow);
                    etothknow.setVisibility(View.GONE);
                    tvothknow.setVisibility(View.GONE);
                } else if (stknow.equals("4. Radio Add")) {
                    rspknow = "4";
                    rbknow = RequestBody.create(MediaType.parse("text/plain"), rspknow);
                    etothknow.setVisibility(View.GONE);
                    tvknowother.setVisibility(View.GONE);
                } else if (stknow.equals("5. Word of Mouth")) {
                    rspknow = "5";
                    rbknow = RequestBody.create(MediaType.parse("text/plain"), rspknow);
                    etothknow.setVisibility(View.GONE);
                    tvothknow.setVisibility(View.GONE);
                } else if (stknow.equals("6.others")) {
                    rspknow = "6";
                    rbknow = RequestBody.create(MediaType.parse("text/plain"), rspknow);
                    etothknow.setVisibility(View.VISIBLE);

                } else if (stknow.equals("How did you hear about us?")) {
                    etothknow.setVisibility(View.GONE);
                    tvothknow.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

/*
    public void orgtextchangepass() {

        ettextwatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && !s.toString().equalsIgnoreCase("")) {

                    if (etothpassword.getText().hashCode() == s.hashCode()) {

                        etothpassword.removeTextChangedListener(ettextwatcher);
                        etothpassword.addTextChangedListener(ettextwatcher);


                        if (etothpassword.length() == 0) {
                            orgcblength.setChecked(false);
                            orgcbupper.setChecked(false);
                            orgcblowercase.setChecked(false);
                            orgcbnumber.setChecked(false);
                            orgcbsplchar.setChecked(false);
                        } else {


                            orgcalculatepasswrd(s.toString());
                        }


                    }

                } else {
                    orgcblength.setChecked(false);
                    orgcbupper.setChecked(false);
                    orgcblowercase.setChecked(false);
                    orgcbnumber.setChecked(false);
                    orgcbsplchar.setChecked(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                upper = false;
                lower = false;
                digit = false;
                specialChar = false;
                minimumtotal = 8;
            }
        };
        etothpassword.addTextChangedListener(ettextwatcher);
    }
*/

    public void orgcalculatepasswrd(String str) {

        String verify = str;

        for (int k = 0; k < verify.length(); k++) {
            char c = verify.charAt(k);

            if (!specialChar && !Character.isLetterOrDigit(c)) {
                score++;
                specialChar = true;
                if (specialChar) {
                    orgcbsplchar.setChecked(true);
                } else {
                    orgcbsplchar.setChecked(false);
                }

            } else {
                if (!digit && Character.isDigit(c)) {
                    score++;
                    digit = true;
                    orgcbnumber.setChecked(true);
                } else {
                    if (!upper || !lower) {
                        if (Character.isUpperCase(c)) {
                            upper = true;
                            orgcbupper.setChecked(true);

                        } else if (Character.isLowerCase(c)) {
                            lower = true;
                            orgcblowercase.setChecked(true);
                        }

                        if (upper && lower) {
                            score++;
                        }

                    }
                }
                if (verify.length() >= minimumtotal) {
                    orgcblength.setChecked(true);

                } else {
                    orgcblength.setChecked(false);
                }
            }

        }

    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.ivorgmg)
    public void orgimgload() {
        requestPermissions(
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.ivorgre)
    public void orgdocload() {
        requestPermissions(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
    }


    public void showotherimges() {
        Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent1.addCategory(Intent.CATEGORY_OPENABLE);
        intent1.setType("*/*");
        startActivityForResult(intent1, CAMERA_REQUEST);
    }

    public void showorgdocChooser() {
        Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent1.addCategory(Intent.CATEGORY_OPENABLE);
        intent1.setType("*/*");
        startActivityForResult(intent1, ORGDOCCAMERA_REQUEST);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            Log.e("from couple", "couple");
            //If the file selection was successful
            if (resultCode == RESULT_OK) {
                if (data != null && data.getData() != null) {

                    try {
                        orguri = data.getData();
                        assert orguri != null;
                        // Log.e("Uri",uri.toString());
                        Log.i("TAG", "Uri = " + orguri.toString());
                        ivorgmg.setImageURI(orguri);

                        orgfile = FileUtilty.getFile(this, orguri);

                        Log.e("file", orgfile.toString());
                        orgpart = new ArrayList<>();
                        orgpart.add(prepareFilePart("org_photo", orguri));


                    } catch (Exception e) {
                        Log.e("TAG", "File select error", e);
                    }
                }
            }
        } else if (requestCode == ORGDOCCAMERA_REQUEST) {
            Log.e("from wedding", "wedding");
            if (resultCode == RESULT_OK) {
                if (data != null && data.getData() != null) {

                    try {
                        orgdocuri = data.getData();
                        assert orgdocuri != null;
                        // Log.e("Uri",uri.toString());
                        Log.i("TAG", "Uri = " + orgdocuri.toString());
                        ivorgre.setImageURI(orgdocuri);
                        orgdocfile = FileUtilty.getFile(this, orgdocuri);

                        Log.e("file", orgdocfile.toString());
                        orgdocparts = new ArrayList<>();
                        orgdocparts.add(prepareweddingFilePart("org_registraion_photo", orgdocuri));


                    } catch (Exception e) {
                        Log.e("TAG", "File select error", e);
                    }
                }
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        orgrequestBody = RequestBody.create(MediaType.parse(Objects.requireNonNull
                (this.getContentResolver().getType(fileUri))), orgfile);

        return MultipartBody.Part.createFormData(partName, orgfile.getName(), orgrequestBody);
    }

    private MultipartBody.Part prepareweddingFilePart(String partName, Uri fileUri) {

        orgdocgrequestBody = RequestBody.create(MediaType.parse(Objects.requireNonNull
                (this.getContentResolver().getType(fileUri))), orgdocfile);

        return MultipartBody.Part.createFormData(partName, orgdocfile.getName(), orgdocgrequestBody);
    }


    public void sendregdata() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient("").create(ApiDao.class);

        Call<Listmodel> sendregdata = apiDao.orggetsignup(rqroltype, rbstorgtypenumber, rbstorgname, rbstorgaddress, rbstorgcity, rbstate_id
                , rbstorgzip, rbstorgregno, rbstorgdic, orgpart, orgdocparts, rbstopname, rbstopemail, rbstopmobile, rbstoppassword, rbstopconfirmpassword, rbvia,
                rbknow, rbotherknow, rbdtype, rbappversiod, rbandroidversion, opdeviceid, rbdtoken, opdeviceip, opdeviceid, oplatitude, oplongitude, opzipcode, opaddress);

        sendregdata.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                Listmodel listmodel = response.body();
                if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {
                    dialog.dismiss();
                    rs_roleid = listmodel.getRole_id();
                    rs_accesstoken = listmodel.getAccessToken();
                    rs_name = listmodel.getName();
                    rs_maksedphoone = listmodel.getMaskedPhone();
                    rs_verifytoken = listmodel.getVerifyToken();
                    roleId = listmodel.getRoleId();
                    onorggsuccess();
                } else {
                    dialog.dismiss();
                    try {
                        tv_signuptype.setVisibility(View.GONE);
                        tvothknow.setVisibility(View.GONE);
                        tvspinhowknow.setVisibility(View.GONE);
                        tvothconfirmpass.setVisibility(View.GONE);
                        tvothpass.setVisibility(View.GONE);
                        tvothmobile.setVisibility(View.GONE);
                        tvothemail.setVisibility(View.GONE);
                        tvothname.setVisibility(View.GONE);
                        tvorgdoc.setVisibility(View.GONE);
                        tvorgpic.setVisibility(View.GONE);
                        tvorgdec.setVisibility(View.GONE);
                        tvorgregno.setVisibility(View.GONE);
                        tvorgzip.setVisibility(View.GONE);
                        tvorgstate.setVisibility(View.GONE);
                        tvorgcity.setVisibility(View.GONE);
                        tvorgaddress.setVisibility(View.GONE);
                        tvorgname.setVisibility(View.GONE);
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String st = jObjError.getString("message");
                        //ToastMessage.onToast(this, st, ToastMessage.ERROR);
                        JSONObject er = jObjError.getJSONObject("errors");
                        try {
                            JSONArray array_mobile = er.getJSONArray("organization_type");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                ToastMessage.onToast(RegistationActivity.this, array_mobile.getString(i), ToastMessage.ERROR);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONArray array_email = er.getJSONArray("org_name");
                            for (int i1 = 0; i1 < array_email.length(); i1++) {
                                tvorgname.setVisibility(View.VISIBLE);
                                //   etemail.setError(array_email.getString(i).toString());
                                tvorgname.setText(array_email.getString(i1));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        try {
                            JSONArray array_name = er.getJSONArray("org_registration_number");

                            for (int i = 0; i < array_name.length(); i++) {
                                tvorgregno.setVisibility(View.VISIBLE);
                                tvorgregno.setText(array_name.getString(i));
                                //  etname.setError(array_name.getString(i).toString());
                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        try {
                            JSONArray array_name = er.getJSONArray("org_registration_number");

                            for (int i = 0; i < array_name.length(); i++) {
                                tvorgregno.setVisibility(View.VISIBLE);
                                tvorgregno.setText(array_name.getString(i));
                                //  etname.setError(array_name.getString(i).toString());
                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        try {
                            JSONArray array_name = er.getJSONArray("org_description");

                            for (int i = 0; i < array_name.length(); i++) {
                                tvorgdec.setVisibility(View.VISIBLE);
                                tvorgdec.setText(array_name.getString(i));
                                //  etname.setError(array_name.getString(i).toString());
                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        try {
                            JSONArray array_name = er.getJSONArray("org_address");

                            for (int i = 0; i < array_name.length(); i++) {
                                tvorgaddress.setVisibility(View.VISIBLE);
                                tvorgaddress.setText(array_name.getString(i));
                                //  etname.setError(array_name.getString(i).toString());
                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        try {
                            JSONArray array_name = er.getJSONArray("org_city");

                            for (int i = 0; i < array_name.length(); i++) {
                                tvorgcity.setVisibility(View.VISIBLE);
                                tvorgcity.setText(array_name.getString(i));
                                //  etname.setError(array_name.getString(i).toString());
                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        try {
                            JSONArray array_name = er.getJSONArray("org_state");

                            for (int i = 0; i < array_name.length(); i++) {
                                tvorgstate.setVisibility(View.VISIBLE);
                                tvorgstate.setText(array_name.getString(i));
                                //  etname.setError(array_name.getString(i).toString());
                                scrollview.smoothScrollTo(0, scrollview.getTop());

                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        try {
                            JSONArray array_name = er.getJSONArray("org_zip_code");

                            for (int i = 0; i < array_name.length(); i++) {
                                tvorgzip.setVisibility(View.VISIBLE);
                                tvorgzip.setText(array_name.getString(i));
                                //  etname.setError(array_name.getString(i).toString());
                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        try {
                            JSONArray array_name = er.getJSONArray("org_photo");

                            for (int i = 0; i < array_name.length(); i++) {
                                tvorgpic.setVisibility(View.VISIBLE);
                                tvorgpic.setText(array_name.getString(i));
                                //  etname.setError(array_name.getString(i).toString());
                                scrollview.smoothScrollTo(0, scrollview.getTop());

                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        try {
                            JSONArray array_name = er.getJSONArray("org_registraion_photo");

                            for (int i = 0; i < array_name.length(); i++) {
                                tvorgdoc.setVisibility(View.VISIBLE);
                                tvorgdoc.setText(array_name.getString(i));
                                //  etname.setError(array_name.getString(i).toString());
                                scrollview.smoothScrollTo(0, scrollview.getTop());

                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        try {
                            JSONArray array_name = er.getJSONArray("name");

                            for (int i = 0; i < array_name.length(); i++) {
                                tvothname.setVisibility(View.VISIBLE);
                                tvothname.setText(array_name.getString(i));
                                //  etname.setError(array_name.getString(i).toString());
                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        try {
                            JSONArray array_name = er.getJSONArray("mobile");

                            for (int i = 0; i < array_name.length(); i++) {
                                tvothmobile.setVisibility(View.VISIBLE);
                                tvothmobile.setText(array_name.getString(i));
                                //  etname.setError(array_name.getString(i).toString());
                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        try {
                            JSONArray array_name = er.getJSONArray("email");

                            for (int i = 0; i < array_name.length(); i++) {
                                tvothemail.setVisibility(View.VISIBLE);
                                tvothemail.setText(array_name.getString(i));
                                //  etname.setError(array_name.getString(i).toString());
                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        try {
                            JSONArray array_password = er.getJSONArray("password");


                            for (int i = 0; i < array_password.length(); i++) {
                                tvothpass.setVisibility(View.VISIBLE);
                                //   etpassword.setError(array_password.getString(i).toString());
                                tvothpass.setText(array_password.getString(i));
                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        try {
                            JSONArray confirmpasswods = er.getJSONArray("confirmPassword");
                            for (int i = 0; i < confirmpasswods.length(); i++) {
                                tvothconfirmpass.setVisibility(View.VISIBLE);
                                tvothconfirmpass.setText(confirmpasswods.getString(i));
                                // etconfirmpassword.setError(confirmpasswods.getString(i).toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray array_roltype = er.getJSONArray("roleType");

                            for (int i = 0; i < array_roltype.length(); i++) {
                                tv_signuptype.setVisibility(View.VISIBLE);
                                tv_signuptype.setText(array_roltype.getString(i));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray array_roltype = er.getJSONArray("heard_others_text");

                            for (int i = 0; i < array_roltype.length(); i++) {
                                tvothknow.setVisibility(View.VISIBLE);
                                tvothknow.setText(array_roltype.getString(i));
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
                Log.e("orgregstfail", t.toString());
                dialog.dismiss();
                //  ToastMessage.onToast(RegistationActivity.this,
                //        "We have some issues please try after some time", ToastMessage.ERROR);
            }
        });
    }

    public void onorggsuccess() {
        AccountUtils.setAccessToken(this, rs_accesstoken);
        AccountUtils.setPassword(this, stoppassword);
        AccountUtils.setRoleid(this, roleId);
        Intent intent = new Intent(RegistationActivity.this, OTPActivity.class);
        intent.putExtra("mobile", stmobile);
        intent.putExtra("credential", "signup");
        intent.putExtra("accesstoken", rs_accesstoken);
        intent.putExtra("verifytoken", rs_verifytoken);
        intent.putExtra("roleId", roleId);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // getLocation();


    }


    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(RegistationActivity.this, perm));
    }


    //For Getting Current Location
    public void getLocation() {

        gpsTracker = new GPSTracker(RegistationActivity.this);

        if (gpsTracker.canGetLocation()) {
            latitude_current = gpsTracker.getLatitude();
            longitude_current = gpsTracker.getLongitude();

            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker = new GPSTracker(RegistationActivity.this);
            gpsTracker.showSettingsAlert();
        }

        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(RegistationActivity.this, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude_current, longitude_current, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            if (addresses != null && addresses.size() > 0) {
                String address1 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                //   String address1 = addresses.get(0).getSubAdminArea();
                String city = addresses.get(0).getLocality();
              /*  String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL*/
                Log.e("address", "" + address1);
                address = address1;
                zipcode = addresses.get(0).getPostalCode();


            } else {
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }


}
