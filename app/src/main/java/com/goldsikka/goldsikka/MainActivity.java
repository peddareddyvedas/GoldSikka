package com.goldsikka.goldsikka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import com.goldsikka.goldsikka.Activitys.BaseActivity;
import com.goldsikka.goldsikka.Activitys.BusinessEnquiryForm;
import com.goldsikka.goldsikka.Activitys.CopanyDetails;
import com.goldsikka.goldsikka.Activitys.CountactUsForm;
import com.goldsikka.goldsikka.Activitys.Coupons.CouponsList;
import com.goldsikka.goldsikka.Activitys.Events.FileUtilty;
import com.goldsikka.goldsikka.Activitys.Kyc_Details;
import com.goldsikka.goldsikka.Activitys.Nominee_Details;
import com.goldsikka.goldsikka.Activitys.Passbook_Activity;
import com.goldsikka.goldsikka.Activitys.Predict_price.Predict_EditAddress;
import com.goldsikka.goldsikka.Activitys.Predict_price.Predict_price_address;
import com.goldsikka.goldsikka.Activitys.Predict_price.Predicted_List;
import com.goldsikka.goldsikka.Activitys.Profile.CustomerAddressList;
import com.goldsikka.goldsikka.Activitys.Predict_price.predictprice_activity;
import com.goldsikka.goldsikka.Activitys.PurchaseEnquiryForm;
import com.goldsikka.goldsikka.Activitys.Events.EventQRCodeScanner;
import com.goldsikka.goldsikka.Activitys.ReferAndEarnActivity;
import com.goldsikka.goldsikka.Activitys.SpecialModule.SeasonSale;
import com.goldsikka.goldsikka.Activitys.SpecialModule.WomensplActivity;

import com.goldsikka.goldsikka.Adapter.MainModuleAdapterClass;
import com.goldsikka.goldsikka.Adapter.Schemes_list_Adapter;
import com.goldsikka.goldsikka.Fragments.Aboutusfragment;
import com.goldsikka.goldsikka.Fragments.Business_Associates;
import com.goldsikka.goldsikka.Fragments.Customer_BankDetailslist;
import com.goldsikka.goldsikka.Fragments.Digital_wallet_fragment;
import com.goldsikka.goldsikka.Fragments.Edit_coustomer_details;
import com.goldsikka.goldsikka.Fragments.FAQsFragment;
import com.goldsikka.goldsikka.Fragments.TransferGold;
import com.goldsikka.goldsikka.Fragments.MainFragment;
import com.goldsikka.goldsikka.Fragments.Reedem_fragment;
import com.goldsikka.goldsikka.Fragments.Schemes.Scheme_Content_Fragment;
import com.goldsikka.goldsikka.Fragments.Sell_Fragment;
import com.goldsikka.goldsikka.LOGIN.WelcomeActivity;
import com.goldsikka.goldsikka.Models.MainModuleModel;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ErrorSnackBar;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.Fragments.baseinterface;
import com.goldsikka.goldsikka.interfaces.OnClickItemListenerForSchemes;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.List_Model;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

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
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;


public class MainActivity extends BaseActivity implements baseinterface, NavigationView.OnNavigationItemSelectedListener,
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, OnItemClickListener, View.OnClickListener, OnClickItemListenerForSchemes {

    SliderLayout sliderLayout;
    LinearLayout ll_predict_popup, llpassbook;
    HashMap<String, String> sliderImages;
    String st_buyprice, st_sellprice, st_goldtype, st_name, st_email, st_mobile, stprofileimg;
    String st_logout = "1";
    String get_banners;
    ApiDao apiDao;

    LinearLayout ll_betterluck;


    shared_preference sharedPreference;
    String refresh_accesstoken;
    TextView customer_name, customer_email, customer_mobile;
    String st_ingrams, st_incurrency, st_currencyinwords;

    TextView tv_predict_address, tv_state, tv_city, tv_pin, addrs_type, tvinfo;

    TextView tv_price_predicted, tv_createdDate, tv_betterprice_predicted, tv_bettercreatedDate;

    private FirebaseAnalytics mFirebaseAnalytics;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_predicte)
    LinearLayout ll_predicte;
    RecyclerView rv_module;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_walletgold)
    TextView tv_walletgold;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvwinner)
    TextView tvwinner;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvcustomerinfo)
    TextView tvcustomerinfo;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llrefer)
    LinearLayout llrefer;


    RecyclerView rv_schemeslist;
    ArrayList<Listmodel> arrayList;
    Schemes_list_Adapter adapter;

    MainModuleAdapterClass moduleadapter;
    ArrayList<MainModuleModel> mainModuleModelArrayList;
    String id, schemename;

    String st_adreessid;


    String address, state, pin, city, staddresstype;
    Button btn_predict_address, btn_continue;
    ImageView btn_predict_address_change, iv_profile_image;
    LinearLayout lladdress;
    String predictid, price_predicted, createdDate;
    Button bt_nexttime;
    String stdata, stmsg, stprofileimage;

    String stwallet;
    MenuItem nav_predictelist;

    String st_fathername, st_spousename, st_aadhaar, st_pannumber, st_alternate_phone, st_gender;

    LinearLayout llTransfer, llsell, llreedem;
    // LinearLayout llgift;
    DrawerLayout drawer;


    private static final String TAG = "Goldsikka";
    private static final int CAMERA_REQUEST = 1888;
    File file;
    Uri uri;
    RequestBody requestBody;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @SuppressLint("CutPasteId")
    @Override
    protected void initView() {

        ButterKnife.bind(this);
        FirebaseCrashlytics.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        sliderLayout = findViewById(R.id.sliderLayout);
        rv_schemeslist = findViewById(R.id.rv_schemeslist);
        rv_module = findViewById(R.id.rv_module);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setHomeAsUpIndicator(R.drawable.ic_baseline_navmenu);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_navmenu);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View hView = navigationView.getHeaderView(0);
        customer_name = hView.findViewById(R.id.tv_customername);
        customer_email = hView.findViewById(R.id.tv_customeremail);
        customer_mobile = hView.findViewById(R.id.tv_mobile);
        iv_profile_image = hView.findViewById(R.id.iv_profile_image);
        st_name = AccountUtils.getName(this);
        st_email = AccountUtils.getEmail(this);
        st_mobile = AccountUtils.getMobile(this);
        nav_predictelist = navigationView.getMenu().findItem(R.id.nav_predictelist);
        sharedPreference = new shared_preference(this);
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

        intilizeviews();
        intilize_recyclerview();
        intilizeSchemeRecyclerView();
        //init_bottomnavgation();


        if (!NetworkUtils.isConnected(MainActivity.this)) {
            ToastMessage.onToast(MainActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            Log.e("MainAccessToken", AccountUtils.getAccessToken(this));
            Call<List<Listmodel>> call = apiDao.getBannerImages("Bearer " + AccountUtils.getAccessToken(this));
            Listresponce(call);

            Call<Listmodel> live_rates = apiDao.getlive_rates("Bearer " + AccountUtils.getAccessToken(this));
            responsemethod(live_rates);
            customer_details();
            getSchemelist();
            get_walletdetails();
            open_predicte();
        }

        swipe_layout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipe_layout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(MainActivity.this)) {
                            ToastMessage.onToast(MainActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            swipe_layout.setRefreshing(false);
                            return;
                        } else {

//                                    Call<List<Listmodel>> call = apiDao.getBannerImages("Bearer " + AccountUtils.getAccessToken(MainActivity.this));
//                                    Listresponce(call);
                            Call<Listmodel> live_rates = apiDao.getlive_rates("Bearer " + AccountUtils.getAccessToken(MainActivity.this));
                            responsemethod(live_rates);
                            open_predicte();
                            customer_details();
                            getSchemelist();
                            get_walletdetails();
                        }
                        swipe_layout.setRefreshing(false);

                    }
                }, 3000);


            }
        });
    }

    public void initcountactusform() {
        Intent intent = new Intent(this, CountactUsForm.class);
        startActivity(intent);
    }

    public void initpurchaseform() {
        Intent intent = new Intent(this, PurchaseEnquiryForm.class);
        startActivity(intent);

    }

    public void initbusinessform() {
        Intent intent = new Intent(this, BusinessEnquiryForm.class);
        startActivity(intent);
    }

    public void intilizeviews() {


        llTransfer = findViewById(R.id.llTransfer);
        llTransfer.setOnClickListener(this);
        llpassbook = findViewById(R.id.llpassbook);
        llpassbook.setOnClickListener(this);

        llsell = findViewById(R.id.passbook_sell);
        llsell.setOnClickListener(this);

        llreedem = findViewById(R.id.reedem_passbook);
        llreedem.setOnClickListener(this);
//
//        llgift = findViewById(R.id.llgift);
//        llgift.setOnClickListener(this);

        llrefer.setOnClickListener(this);
        iv_profile_image.setOnClickListener(this);

        tv_price_predicted = findViewById(R.id.tv_price_predicted);
        tv_createdDate = findViewById(R.id.tv_createdDate);
        tv_betterprice_predicted = findViewById(R.id.tv_betterprice_predicted);
        tv_bettercreatedDate = findViewById(R.id.tv_bettercreatedDate);

        bt_nexttime = findViewById(R.id.bt_nexttime);

        ll_betterluck = findViewById(R.id.ll_betterluck);
        ll_predict_popup = findViewById(R.id.ll_predict_congpopup);

        tv_predict_address = findViewById(R.id.tv_preaddreess);
        tv_state = findViewById(R.id.tv_prestate);
        tv_city = findViewById(R.id.tv_precity);
        tv_pin = findViewById(R.id.tv_prepin);
        tvinfo = findViewById(R.id.tvinfo);
        addrs_type = findViewById(R.id.addrs_type);

        lladdress = findViewById(R.id.lladdress);

        btn_predict_address = findViewById(R.id.btn_predict_address);

        btn_predict_address_change = findViewById(R.id.btn_predict_address_change);

        btn_continue = findViewById(R.id.btn_continue);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_continue)
    public void btn_continue() {
        init_screensee();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.bt_nexttime)
    public void btnnext() {
        init_screensee();
    }

    public void init_screensee() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> init_seescreen = apiDao.init_seescreen("Bearer " + AccountUtils.getAccessToken(this), predictid);
            init_seescreen.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Listmodel listmodel = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        ll_predict_popup.setVisibility(View.GONE);
                        ll_betterluck.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {

                    Log.e("error see", t.toString());

                    ToastMessage.onToast(MainActivity.this, "we have some issues", ToastMessage.ERROR);

                }
            });
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_predict_address_change)
    public void init_addresschange() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(MainActivity.this, Predict_EditAddress.class);
            intent.putExtra("id", st_adreessid);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_predict_address)
    public void btn_predict_address() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(MainActivity.this, Predict_price_address.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }


    public void getprimaryaddress() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> get_address = apiDao.
                    get_profileaddress("Bearer " + AccountUtils.getAccessToken(this));
            get_address.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    List<Listmodel> list = Collections.singletonList(response.body());


                    if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_ACCEPTED) {
                        for (Listmodel listmodel : list) {

                            lladdress.setVisibility(View.VISIBLE);
                            btn_predict_address_change.setVisibility(View.VISIBLE);
                            btn_predict_address.setVisibility(View.GONE);
                            address = listmodel.getAddress();
                            city = listmodel.getCity();
                            pin = listmodel.getZip();
                            staddresstype = listmodel.getTitle();


                            st_adreessid = listmodel.getId();
                            if (st_adreessid == null) {
                                st_adreessid = listmodel.getId();
                                lladdress.setVisibility(View.VISIBLE);
                                btn_predict_address_change.setVisibility(View.GONE);
                                btn_predict_address.setVisibility(View.VISIBLE);
                                btn_continue.setVisibility(View.GONE);

                            }
                            if (address == null) {
                                lladdress.setVisibility(View.VISIBLE);
                                btn_predict_address_change.setVisibility(View.GONE);
                                btn_predict_address.setVisibility(View.VISIBLE);
                                btn_continue.setVisibility(View.GONE);
                                tv_predict_address.setText("Address");
                            }
                            if (city == null) {
                                lladdress.setVisibility(View.VISIBLE);
                                btn_predict_address_change.setVisibility(View.GONE);
                                btn_predict_address.setVisibility(View.VISIBLE);
                                btn_continue.setVisibility(View.GONE);
                                tv_city.setText("City");
                            }
                            if (pin == null) {
                                lladdress.setVisibility(View.VISIBLE);
                                btn_predict_address_change.setVisibility(View.GONE);
                                btn_predict_address.setVisibility(View.VISIBLE);
                                btn_continue.setVisibility(View.GONE);

                                tv_pin.setText("Pincode");
                            }

                            if (staddresstype == null) {
                                lladdress.setVisibility(View.VISIBLE);
                                btn_predict_address_change.setVisibility(View.GONE);
                                btn_predict_address.setVisibility(View.VISIBLE);
                                btn_continue.setVisibility(View.GONE);

                                addrs_type.setText("AddressType");
                            } else {
                                btn_predict_address_change.setVisibility(View.VISIBLE);
                                btn_continue.setVisibility(View.VISIBLE);
                                btn_predict_address.setVisibility(View.GONE);
                                lladdress.setVisibility(View.VISIBLE);
                                address = listmodel.getAddress();
                                city = listmodel.getCity();
                                pin = listmodel.getZip();
                                staddresstype = listmodel.getTitle();
                                JsonObject from1 = new JsonParser().parse(listmodel.getState().toString()).getAsJsonObject();

                                try {
                                    JSONObject json_from = new JSONObject(from1.toString());
                                    state = json_from.getString("name");


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                setadresstext();

                            }
                        }
                    } else {

                        lladdress.setVisibility(View.GONE);
                        btn_predict_address_change.setVisibility(View.GONE);
                        btn_predict_address.setVisibility(View.VISIBLE);
                        btn_continue.setVisibility(View.GONE);
                        ToastMessage.onToast(MainActivity.this, "Add Address", ToastMessage.ERROR);

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("addresserror", t.toString());
                    ToastMessage.onToast(MainActivity.this, "We Have Some Issues", ToastMessage.ERROR);
                }
            });
        }
    }

    public void setadresstext() {
        tv_city.setText(city);
        tv_state.setText(state);
        tv_predict_address.setText(address);
        tv_pin.setText(pin);
        addrs_type.setText(staddresstype);
    }

    public void open_predicte() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            mainModuleModelArrayList.clear();
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<MainModuleModel> predicte = apiDao.predict("Bearer " + AccountUtils.getAccessToken(this));
            predicte.enqueue(new Callback<MainModuleModel>() {
                @Override
                public void onResponse(Call<MainModuleModel> call, Response<MainModuleModel> response) {
                    int statuascode = response.code();
                    Log.e("main m d error", String.valueOf(statuascode));
                    if (statuascode == 429) {
                        Toast.makeText(MainActivity.this, "To many attmpts \n please try again..", Toast.LENGTH_SHORT).show();
                    }

                    if (statuascode == HttpsURLConnection.HTTP_OK) {
                        List<MainModuleModel> list = response.body().getModules();

                        for (MainModuleModel listmodel : list) {
                            if (list.size() != 0) {

                                ll_predicte.setVisibility(View.VISIBLE);
                                mainModuleModelArrayList.add(listmodel);
                                moduleadapter.notifyDataSetChanged();

                                openscreens();

                            } else {
                                ll_predicte.setVisibility(View.GONE);

                            }
                        }

                    } else if (statuascode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        sharedPreference.WriteLoginStatus(false);
                        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<MainModuleModel> call, Throwable t) {
                    Log.e("predicte failure error", t.toString());
                }
            });
        }
    }

    public void openscreens() {

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<List_Model> getscreens = apiDao.getpopupscreens("Bearer " + AccountUtils.getAccessToken(this));
            getscreens.enqueue(new Callback<List_Model>() {
                @Override
                public void onResponse(Call<List_Model> call, Response<List_Model> response) {
                    int statuscode = response.code();
                    List_Model listmodel = response.body();

                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        String msg = listmodel.getMessage();
                        String stinfo = listmodel.getInfo();
                        tvinfo.setText(stinfo);

                        if (msg.equals("0")) {
                            ll_betterluck.setVisibility(View.VISIBLE);

                            ll_predict_popup.setVisibility(View.GONE);
                            try {
                                JsonObject from = new JsonParser().parse(listmodel.getPrice().toString()).getAsJsonObject();


                                JSONObject jsonObject = new JSONObject(from.toString());


                                predictid = jsonObject.getString("id");
                                Log.e("preid", predictid);
                                createdDate = jsonObject.getString("created_date");
                                price_predicted = jsonObject.getString("price_predicted");

                                tv_bettercreatedDate.setText(createdDate);
                                tv_betterprice_predicted.setText(price_predicted);

                                tv_price_predicted.setText(price_predicted);
                                tv_createdDate.setText(createdDate);

                                JsonObject winner = new JsonParser().parse(listmodel.getWinner().toString()).getAsJsonObject();
                                JSONObject object = new JSONObject(winner.toString());

                                String stwinner = object.getString("name");
                                tvwinner.setText(stwinner);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (msg.equals("1")) {
                            getprimaryaddress();
                            get_primarykycdetails();
                            ll_betterluck.setVisibility(View.GONE);

                            ll_predict_popup.setVisibility(View.VISIBLE);
                            try {
                                JsonObject from = new JsonParser().parse(listmodel.getPrice().toString()).getAsJsonObject();
                                JSONObject jsonObject = new JSONObject(from.toString());

                                JsonObject winner = new JsonParser().parse(listmodel.getWinner().toString()).getAsJsonObject();
                                JSONObject object = new JSONObject(winner.toString());
                                String stsuccessinfo = object.getString("name");
                                String stcongrats = object.getString("date");
                                tvcustomerinfo.setText(stsuccessinfo + stcongrats);

                                predictid = jsonObject.getString("id");
                                Log.e("preid", predictid);
                                createdDate = jsonObject.getString("created_date");
                                price_predicted = jsonObject.getString("price_predicted");

                                tv_bettercreatedDate.setText(createdDate);
                                tv_betterprice_predicted.setText(price_predicted);

                                tv_price_predicted.setText(price_predicted);
                                tv_createdDate.setText(createdDate);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (msg.equals("2")) {
                            ll_betterluck.setVisibility(View.GONE);
                            ll_predict_popup.setVisibility(View.GONE);
                        }
                    } else {
                       // ToastMessage.onToast(MainActivity.this, "Technical Issues", ToastMessage.ERROR);
                        Log.e("responce code error ", String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<List_Model> call, Throwable t) {
                    Log.e("sren error", t.toString());
                    ToastMessage.onToast(MainActivity.this, "we have some issues", ToastMessage.ERROR);
                }
            });
        }
    }


    private void get_walletdetails() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<JsonElement> call = apiDao.get_digitalwallet("Bearer " + AccountUtils.getAccessToken(this));
            call.enqueue(new Callback<JsonElement>() {

                @Override
                public void onResponse(@NonNull Call<JsonElement> call, @NonNull retrofit2.Response<JsonElement> response) {
                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {

                        JsonElement jsonElement = response.body();
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        JsonObject gson = new JsonParser().parse(String.valueOf(jsonObject)).getAsJsonObject();

                        try {
                            JSONObject jo2 = new JSONObject(gson.toString());
                            JSONObject balance = jo2.getJSONObject("balance");
                            st_currencyinwords = balance.getString("currencyInWords");
                            st_ingrams = balance.getString("humanReadable");
                            st_incurrency = balance.getString("inCurrency");
                            stwallet = st_ingrams;
                            set_walletdetails();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else if (stauscode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        sharedPreference.WriteLoginStatus(false);
                        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            Toast.makeText(MainActivity.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {

                    //  Toast.makeText(MainActivity.this, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @SuppressLint("SetTextI18n")
    private void set_walletdetails() {

        stwallet = st_ingrams;
        tv_walletgold.setText("Wallet Gold : " + st_ingrams + " g");
        AccountUtils.setWalletAmount(this, st_ingrams);

    }

    public void customer_details() {


        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Call<Listmodel> getprofile = apiDao.getprofile_details("Bearer " + AccountUtils.getAccessToken(this));
            // responsemethod(getprofile);
            getprofile.enqueue(new Callback<Listmodel>() {

                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {

                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                        List<Listmodel> list = Collections.singletonList(response.body());
                        if (list != null) {
                            for (Listmodel listmodel : list) {
                                st_name = listmodel.getName();
                                st_email = listmodel.getEmail();
                                st_mobile = listmodel.getMobile();
                                stprofileimg = listmodel.getAvatar();
                                if (stprofileimg != null) {
                                    stprofileimage = listmodel.getAvatarImageLink();
                                    Picasso.with(MainActivity.this).load(listmodel.getAvatarImageLink()).into(iv_profile_image);
                                } else {
                                }

                                AccountUtils.setName(MainActivity.this, st_name);
                                AccountUtils.setMobile(MainActivity.this, st_mobile);
                                AccountUtils.setEmail(MainActivity.this, st_email);

                                set_profiledetails();
                                if (listmodel.isTerminated()) {
                                    st_logout = "0";
                                    Call<List<Listmodel>> getlogout = apiDao.get_logout("Bearer " + AccountUtils.getAccessToken(MainActivity.this));
                                    Listresponce(getlogout);
                                }

                            }
                        }

                    } else if (stauscode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        sharedPreference.WriteLoginStatus(false);
                        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            JSONObject er = jObjError.getJSONObject("errors");
                            Toast.makeText(MainActivity.this, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    ToastMessage.onToast(MainActivity.this, "We Have Some Issues", ToastMessage.ERROR);
                    Log.e("Error", t.toString());
                }

            });

        }

    }

    private void set_profiledetails() {
        customer_email.setText(st_email);
        customer_name.setText(st_name);
        customer_mobile.setText("+" + st_mobile);

    }


    public void Clickelistner(int groupPosition, int childPosition) {

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            if (groupPosition == 0 && childPosition == 0) {
                Intent intent = new Intent(this, Aboutusfragment.class);
                startActivity(intent);
            } else if (groupPosition == 5 && childPosition == 2) {
                Intent intent = new Intent(this, MainFragment.class);
                intent.putExtra("parentposition", 5);
                intent.putExtra("childpositiion", 2);
                startActivity(intent);
            } else if (groupPosition == 0 && childPosition == 1) {
                Intent intent = new Intent(this, Business_Associates.class);
                startActivity(intent);
            } else if (groupPosition == 0 && childPosition == 2) {
                Intent intent = new Intent(this, FAQsFragment.class);
                startActivity(intent);
            } else if (groupPosition == 0 && childPosition == 3) {

                Intent intent = new Intent(this, CopanyDetails.class);
                startActivity(intent);
            }

        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_digital_wallet)
    public void opendigitalwallet() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(this, Digital_wallet_fragment.class);
            startActivity(intent);
        }
    }


    @SuppressLint("NewApi")
    private void setupSlider() {
        sliderLayout = (SliderLayout) findViewById(R.id.sliderLayout);
        sliderImages = new HashMap<>();

        sliderImages.put("", get_banners);

        for (String name : sliderImages.keySet()) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(MainActivity.this);
            defaultSliderView.image(sliderImages.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            sliderLayout.addSlider(defaultSliderView);

        }
        sliderLayout.getCurrentSlider();
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setDuration(4000);
        sliderLayout.addOnPageChangeListener(this);

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void responce(@NonNull Response<Listmodel> response, int stauscode) {


        if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
            List<Listmodel> list = Collections.singletonList(response.body());

            if (list != null) {
                for (Listmodel listmodel : list) {
                    st_buyprice = listmodel.getBuy_price_per_gram();
                    st_sellprice = listmodel.getSell_price_per_gram();
                    st_goldtype = listmodel.getGold_type();

                    String purity = listmodel.getGold_purity();

                    AccountUtils.setPurity(this, purity);
                    AccountUtils.setCarat(this, st_goldtype);


                    setlive_price();
                }
            }


        } else if (stauscode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
            sharedPreference.WriteLoginStatus(false);
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            try {
                assert response.errorBody() != null;
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                String st = jObjError.getString("message");
                JSONObject er = jObjError.getJSONObject("errors");
                Toast.makeText(this, st, Toast.LENGTH_SHORT).show();
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void listresponce(@NonNull Response<List<Listmodel>> response, int stauscode) {

        if (st_logout.equals("0")) {

            if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                sharedPreference.WriteLoginStatus(false);
                Intent intent = new Intent(this, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        } else {

            if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                List<Listmodel> list = response.body();
                if (list != null) {
                    for (Listmodel listmodel : list) {
                        refresh_accesstoken = listmodel.getAccessToken();
                        get_banners = listmodel.getBanner_uri();

                        setupSlider();
                    }
                } else {
                    Toast.makeText(this, "No Images", Toast.LENGTH_SHORT).show();
                }
            } else if (stauscode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                sharedPreference.WriteLoginStatus(false);
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                try {
                    assert response.errorBody() != null;
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    String st = jObjError.getString("message");
                    JSONObject er = jObjError.getJSONObject("errors");
                    ToastMessage.onToast(this, st, ToastMessage.ERROR);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void setlive_price() {
        AccountUtils.setLivePrice(this, st_buyprice);
        AccountUtils.setSellPrice(this, st_sellprice);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        switch (id) {
            case R.id.mncotctus:
                initcountactusform();
                break;
            case R.id.mnbusinessenquiry:
                initbusinessform();
                break;
            case R.id.mnpurchaseenuiry:
                initpurchaseform();
                break;
            case R.id.mnqrscaner:
                Intent intent = new Intent(this, EventQRCodeScanner.class);
                startActivity(intent);
                break;
        }
        if (id == R.id.nav_about) {
            Clickelistner(0, 0);
        } else if (id == R.id.nav_business) {
            Clickelistner(0, 1);
        } else if (id == R.id.nav_faqs) {
            Clickelistner(0, 2);
        } else if (id == R.id.nav_companydetails) {
            Clickelistner(0, 3);
        } else if (id == R.id.nav_profile) {
            open_profile();
        }
//        else if (id == R.id.nav_passbook){
//            openpassbook();
//        }
        else if (id == R.id.nav_kyc) {
//            get_kycdetails();
            Clickelistner(5, 2);


        } else if (id == R.id.navoffers) {
            Intent intent = new Intent(MainActivity.this, CouponsList.class);
            startActivity(intent);
        }

//        else if (id == R.id.nav_orderslist){
//          getodrderslist();
//        }
        else if (id == R.id.nav_nominee) {
            get_nomineedetails();
        }
//        else if (id == R.id.nav_share){
//            Intent intent  = new Intent(MainActivity.this, ReferAndEarnActivity.class);
//            startActivity(intent);
//        }
        else if (id == R.id.nav_logout) {
            logoutclick();
        } else if (id == R.id.nav_predictelist) {

            init_predictedlist();
        }
        return true;
    }

//    public  void getodrderslist(){
//        Intent intent = new Intent(this, orderslist.class);
//        startActivity(intent);
//    }


    public void init_predictedlist() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(MainActivity.this, Predicted_List.class);
            startActivity(intent);
        }
    }

    public void logoutclick() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            st_logout = "0";
            Call<List<Listmodel>> getlogout = apiDao.get_logout("Bearer " + AccountUtils.getAccessToken(this));
            Listresponce(getlogout);
        }

    }

    public void open_profile() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
//            Intent intent = new Intent(MainActivity.this, Profile_Details.class);
//            startActivity(intent);
            BottomNavigation();
        }
    }


    public void get_nomineedetails() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(MainActivity.this, Nominee_Details.class);
            startActivity(intent);
        }
    }

    public void get_kycdetails() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(MainActivity.this, Kyc_Details.class);
            startActivity(intent);
        }
    }


    public void intilizeSchemeRecyclerView() {

        rv_schemeslist.setHasFixedSize(true);
        rv_schemeslist.setLayoutManager(new LinearLayoutManager(this));
        rv_schemeslist.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_schemeslist.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();
        adapter = new Schemes_list_Adapter(arrayList, this, this);
        rv_schemeslist.setAdapter(adapter);

        ScrollingPagerIndicator recyclerIndicator = findViewById(R.id.indicator);
        recyclerIndicator.attachToRecyclerView(rv_schemeslist);

        final int timer = 3000;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int counta = 0;

            @Override
            public void run() {
                if (counta < arrayList.size()) {
                    rv_schemeslist.scrollToPosition(counta++);
                    handler.postDelayed(this, timer);
                }
                if (counta == arrayList.size()) {
                    counta = 0;
                }

            }
        };
        handler.postDelayed(runnable, timer);

    }

    public void intilize_recyclerview() {

        rv_module.setHasFixedSize(true);
        rv_module.setLayoutManager(new LinearLayoutManager(this));
        rv_module.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManagers = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_module.setLayoutManager(layoutManagers);
        mainModuleModelArrayList = new ArrayList<>();
        moduleadapter = new MainModuleAdapterClass(mainModuleModelArrayList, this, this, nav_predictelist);
        rv_module.setAdapter(moduleadapter);

        ScrollingPagerIndicator recyclerIndicator = findViewById(R.id.mainindicator);
        recyclerIndicator.attachToRecyclerView(rv_module);

        final int timer = 3500;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int counte = 0;

            @Override
            public void run() {
                if (counte < mainModuleModelArrayList.size()) {
                    rv_module.scrollToPosition(counte++);
                    handler.postDelayed(this, timer);
                }
                if (counte == mainModuleModelArrayList.size()) {
                    counte = 0;
                }

            }
        };
        handler.postDelayed(runnable, timer);

    }

    public void getSchemelist() {

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            arrayList.clear();

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<List<Listmodel>> getschemes = apiDao.getschemes("Bearer " + AccountUtils.getAccessToken(this));
            getschemes.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int statuscode = response.code();

                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        List<Listmodel> list = response.body();
                        //  dialog.dismiss();
                        for (Listmodel listmodel : list) {
                            id = listmodel.getId();
                            schemename = listmodel.getTitle();
                            arrayList.add(listmodel);
                            adapter.notifyDataSetChanged();

                            Log.e("ids", id);
                        }
                    } else if (statuscode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        sharedPreference.WriteLoginStatus(false);
                        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {

                        ToastMessage.onToast(MainActivity.this, "Error", ToastMessage.ERROR);
                    }


                }

                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {

                    Log.e("Schemme error", t.toString());
                    ToastMessage.onToast(MainActivity.this, "We have some issues", ToastMessage.ERROR);

                }
            });
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemClick(View view, int position) {
        MainModuleModel mainModuleModel = mainModuleModelArrayList.get(position);
        String id = mainModuleModel.getId();
        String type = mainModuleModel.getModule_type();

        if (view.getId() == R.id.iv_module) {
            if (!NetworkUtils.isConnected(this)) {
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            } else {
                switch (type) {
                    case "PR":
                        predictaccess(type);
                        break;
                    case "WM":
                        initwomenspl();
                        break;
                    case "FN":
                        initseasonsale();
                        break;
                }
            }
        }

    }

    public void initseasonsale() {
        Intent intent = new Intent(MainActivity.this, SeasonSale.class);
        startActivity(intent);
    }

    public void initwomenspl() {
        Intent intent = new Intent(MainActivity.this, WomensplActivity.class);
        startActivity(intent);
    }

    public void predictaccess(String type) {

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<List_Model> predictacces = apiDao.predictAccess("Bearer " + AccountUtils.getAccessToken(this));
        predictacces.enqueue(new Callback<List_Model>() {
            @Override
            public void onResponse(Call<List_Model> call, Response<List_Model> response) {
                int statucode = response.code();
                if (statucode == HttpsURLConnection.HTTP_OK) {
                    List_Model list_model = response.body();
                    stmsg = list_model.getMessage();
                    stdata = list_model.getData();
                    if (stmsg.equals("1")) {
                        init_predicte(type);
                    } else {
                        predictaccesdialog(stmsg);
                    }
                }
            }

            @Override
            public void onFailure(Call<List_Model> call, Throwable t) {
                ToastMessage.onToast(MainActivity.this, "we have some  issues", ToastMessage.ERROR);
            }
        });
    }

    ImageView imageView_close;
    AlertDialog predictalertdialog;
    TextView tvpredictaccess;
    Button btnpredictaccess;

    public void predictaccesdialog(String msg) {
        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.predictaccesspopup, null);
        alertdilog.setCancelable(false);
        alertdilog.setView(dialogview);
        predictalertdialog = alertdilog.create();
        btnpredictaccess = dialogview.findViewById(R.id.tvpoptitle);
        tvpredictaccess = dialogview.findViewById(R.id.tvpredictaccess);
        if (msg.equals("2")) {
            btnpredictaccess.setVisibility(View.GONE);
            tvpredictaccess.setText(stdata);
        } else if (msg.equals("3")) {
            tvpredictaccess.setText(stdata);
            btnpredictaccess.setVisibility(View.VISIBLE);
            btnpredictaccess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    init_predictedlist();
                    predictalertdialog.dismiss();
                }
            });
        }


        imageView_close = dialogview.findViewById(R.id.iv_close);

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predictalertdialog.dismiss();
            }
        });

        predictalertdialog.show();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemClickListener(View view, int position) {
        Listmodel listmodel = arrayList.get(position);
        switch (view.getId()) {
            case R.id.cd_scheme:
                if (!NetworkUtils.isConnected(this)) {
                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                } else {
                    openschems(listmodel.getScheme_calculation_type(), listmodel.getId(), listmodel.getTitle());
                    Log.e("scchemss", listmodel.getScheme_calculation_type() + "  idscm " + listmodel.getId());
                }

                break;
        }

    }

    public void init_predicte(String type) {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            if (type.equals("PR")) {
                Intent intent = new Intent(MainActivity.this, predictprice_activity.class);
                startActivity(intent);
            }
        }

    }

    public void openschems(String schemetype_name, String schemeid, String schemetitle) {


        AccountUtils.setSchemename(this, schemetype_name);
        AccountUtils.setSchemeID(this, schemeid);
        AccountUtils.setSchemeTitle(this, schemetitle);

        if (schemetype_name.equals("MG")) {
            Intent intent = new Intent(this, Scheme_Content_Fragment.class);
            startActivity(intent);
        } else if (schemetype_name.equals("JW")) {
            Intent intent = new Intent(this, Scheme_Content_Fragment.class);
            startActivity(intent);

        }


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llTransfer:
                openTransfer();
                break;
            case R.id.llrefer:
                Intent intent = new Intent(MainActivity.this, ReferAndEarnActivity.class);
                startActivity(intent);
                break;
            case R.id.passbook_sell:
                opensell();
                break;
            case R.id.reedem_passbook:
                openreedem();
                break;
            case R.id.llpassbook:
                openpassbook();
                break;
            case R.id.iv_profile_image:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
                break;
//            case R.id.llgift:
//                opengift();
//                break;
        }
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission Granted ");
                    showChooser();
                } else {

                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void showChooser() {
        Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent1.addCategory(Intent.CATEGORY_OPENABLE);
        intent1.setType("*/*");
        startActivityForResult(intent1, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getData() != null) {
                    try {
                        uri = data.getData();
                        assert uri != null;
                        Log.i(TAG, "Uri = " + uri.toString());
                        file = FileUtilty.getFile(this, uri);
                        Log.e("file", file.toString());
                        List<MultipartBody.Part> parts = new ArrayList<>();
                        parts.add(prepareFilePart("avatar", uri));

                        sendprofileimage(parts);

                    } catch (Exception e) {
                        Log.e(TAG, "File select error", e);
                    }
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        requestBody = RequestBody.create(MediaType.parse(Objects.requireNonNull
                (this.getContentResolver().getType(fileUri))), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
    }

    public void sendprofileimage(List<MultipartBody.Part> part) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getimg = apiDao.sendprofileimage("Bearer " + AccountUtils.getAccessToken(this), part);
            getimg.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("pprofile img code", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        Listmodel listmodel = response.body();
                        stprofileimg = listmodel.getAvatarImageLink();
                        Picasso.with(MainActivity.this).load(stprofileimg).into(iv_profile_image);

                    } else {
                        dialog.dismiss();
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            JSONObject Object = jsonObject.getJSONObject("errors");
                            try {
                                JSONArray array_name = Object.getJSONArray("avatar");
                                Log.e("image", array_name.toString());
                                for (int i = 0; i < array_name.length(); i++) {
                                    Toast.makeText(MainActivity.this, array_name.getString(i), Toast.LENGTH_SHORT).show();
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
                    dialog.dismiss();
                    Log.e("profile img failp", String.valueOf(t));
                    ToastMessage.onToast(MainActivity.this, "We have  issue try after some time", ToastMessage.ERROR);
                }
            });
        }
    }


//    public void opengift(){
//        if (!NetworkUtils.isConnected(this)){
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//        }else {
//            Intent intent = new Intent(this, GiftModuleActivity.class);
//            startActivity(intent);
//        }
//    }


    public void openreedem() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {

            Intent intent = new Intent(this, Reedem_fragment.class);
            intent.putExtra("wallet", st_ingrams);
            intent.putExtra("amount", st_incurrency);
            startActivity(intent);
        }


    }

    public void openTransfer() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(this, TransferGold.class);

            startActivity(intent);
        }

//
    }

    public void opensell() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {

            Intent intent = new Intent(this, Sell_Fragment.class);
            intent.putExtra("wallet", st_ingrams);
            intent.putExtra("amount", st_incurrency);
            startActivity(intent);
        }


    }

    public void openpassbook() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(this, Passbook_Activity.class);
            intent.putExtra("main_pass", "buy_passbook");
            startActivity(intent);
        }
    }

    private void get_primarykycdetails() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> get_kycdetails = apiDao.get_kyc_details("Bearer " + AccountUtils.getAccessToken(this));
            get_kycdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();

                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED ||
                            statuscode == HttpsURLConnection.HTTP_ACCEPTED) {
                        Listmodel listmodel = response.body();
                        st_fathername = listmodel.getFather_name();
                        st_aadhaar = listmodel.getAadhaar_card();
                        st_spousename = listmodel.getSpouse_name();
                        st_pannumber = listmodel.getPan_card();
                        st_alternate_phone = listmodel.getAlternate_phone();
                        st_gender = listmodel.getGender();

                        if (st_fathername == null) {
                            btn_continue.setVisibility(View.GONE);

                        }
                        if (st_fathername == null) {
                            btn_continue.setVisibility(View.GONE);
                            set_kycdetails();
                        }
                        if (st_aadhaar == null) {
                            btn_continue.setVisibility(View.GONE);
                            set_kycdetails();
                        }
                        if (st_spousename == null) {
                            btn_continue.setVisibility(View.GONE);
                            set_kycdetails();
                        }
                        if (st_pannumber == null) {
                            btn_continue.setVisibility(View.GONE);
                            set_kycdetails();
                        }
                        if (st_alternate_phone == null) {
                            btn_continue.setVisibility(View.GONE);
                            set_kycdetails();
                        }
                        if (st_gender == null) {
                            btn_continue.setVisibility(View.GONE);
                            set_kycdetails();
                        } else {
                            //   btn_continue.setVisibility(View.VISIBLE);
                            ll_predict_popup.setVisibility(View.VISIBLE);
                            // set_kycdetails();

                        }


                    } else {
                        btn_continue.setVisibility(View.GONE);

                       // ToastMessage.onToast(MainActivity.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    ToastMessage.onToast(MainActivity.this, "We have some issue", ToastMessage.ERROR);
                }
            });
        }
    }

    AlertDialog alertDialogdialog;
    GifImageView loading_gif;
    Button submit;

    public void set_kycdetails() {

        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.kycalertdialog, null);
        alertdilog.setCancelable(false);
        alertdilog.setView(dialogview);
        alertDialogdialog = alertdilog.create();
        alertDialogdialog.show();
        loading_gif = dialogview.findViewById(R.id.loading_gif);


        submit = dialogview.findViewById(R.id.btn_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit.setVisibility(View.GONE);
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

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                get_kycdetails();
                                submit.setVisibility(View.VISIBLE);
                                loading_gif.setVisibility(View.GONE);
                            }
                        });
                    }
                }, 500);
            }
        });

    }

    private long backPressed;
    private static final long TIME_DELAY = 2000;

    @Override
    public void onBackPressed() {

        if (backPressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            ErrorSnackBar.onBackExit(this, drawer);
        }
        backPressed = System.currentTimeMillis();
    }

//    @SuppressLint("NonConstantResourceId")
//    @OnClick(R.id.llevent)
//    public void event(){
//                Intent intent = new Intent(MainActivity.this, CreateEvents.class);
//                startActivity(intent);
//        }

    TextView tveditprofile, tvnominee, tvkyc, tvbank, tvaddress, tvemail, tvname;
    ImageView ivprofile;

    public void BottomNavigation() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setCancelable(true);
        View bottomSheet = LayoutInflater.from(this)
                .inflate(R.layout.profilebottomview, findViewById(R.id.bottomsheet));
        bottomSheetDialog.setContentView(bottomSheet);
        ((View) bottomSheet.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tveditprofile = bottomSheet.findViewById(R.id.tvprofileedit);
        tvnominee = bottomSheet.findViewById(R.id.tvnominee);
        tvkyc = bottomSheet.findViewById(R.id.tvkyc);
        tvbank = bottomSheet.findViewById(R.id.tvbankdetails);
        tvaddress = bottomSheet.findViewById(R.id.tvaddress);
        tvemail = bottomSheet.findViewById(R.id.tvemail);
        tvname = bottomSheet.findViewById(R.id.tvname);
        ivprofile = bottomSheet.findViewById(R.id.ivprofile);
        if (stprofileimg != null) {
            Picasso.with(MainActivity.this).load(stprofileimage).into(ivprofile);
        } else {

        }
        tvname.setText(st_name);
        tvemail.setText(st_email);
        tvkyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_kycdetails();
            }
        });
        tveditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(MainActivity.this)) {
                    ToastMessage.onToast(MainActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                } else {
                    Intent intent = new Intent(MainActivity.this, Edit_coustomer_details.class);

                    intent.putExtra("name", st_name);
                    intent.putExtra("email", st_email);
                    intent.putExtra("mobile", st_mobile);
                    intent.putExtra("profileimage", stprofileimage);
                    startActivity(intent);
                }
            }

        });
        tvnominee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_nomineedetails();
            }
        });
        tvaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(MainActivity.this)) {
                    ToastMessage.onToast(MainActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                } else {
                    Intent intent = new Intent(MainActivity.this, CustomerAddressList.class);
                    startActivity(intent);
                }
            }
        });
        tvbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(MainActivity.this)) {
                    ToastMessage.onToast(MainActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                } else {
                    Intent intent = new Intent(MainActivity.this, Customer_BankDetailslist.class);
                    startActivity(intent);
                }
            }
        });


        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }

}