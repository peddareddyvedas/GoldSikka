//package com.goldsikka.goldsikka.NewDesignsActivity;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.view.GravityCompat;
//import androidx.fragment.app.Fragment;
//
//
//import android.annotation.SuppressLint;
//import android.content.res.ColorStateList;
//import android.os.Bundle;
//
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import com.goldsikka.goldsikka.Fragments.Ecommerce.Ecommerce_Category_list;
//import com.goldsikka.goldsikka.Fragments.NewDesign.Settings;
//import com.goldsikka.goldsikka.Fragments.NewDesignsFragments.AboutUsFragment;
//import com.goldsikka.goldsikka.Fragments.NewDesignsFragments.HomeFragment;
//import com.goldsikka.goldsikka.Fragments.NewDesignsFragments.PassBookFragment;
//import com.goldsikka.goldsikka.R;
//import com.goldsikka.goldsikka.Utils.AccountUtils;
//import com.goldsikka.goldsikka.Utils.ErrorSnackBar;
//import com.goldsikka.goldsikka.Utils.NetworkUtils;
//import com.goldsikka.goldsikka.Utils.ToastMessage;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class MainFragmentActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
//
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.llhome)
//    public LinearLayout llhome;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.llpassbook)
//    public LinearLayout llpassbook;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.llabout)
//    public LinearLayout llabout;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.llsettings)
//    public LinearLayout llrefer;
//
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.ivdothome)
//    public ImageView ivdothome;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.ivdotpassbook)
//    public ImageView ivdotpassbook;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.ivdotabout)
//    public ImageView ivdotabout;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.ivdotsettings)
//    public ImageView ivdotsettings;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.ivdotecom)
//    public ImageView ivdotecom;
//
//
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.ivhome)
//    public ImageView ivhome;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.ivpassbook)
//    public ImageView ivpassbook;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.ivabout)
//    public ImageView ivabout;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.ivsettings)
//    public ImageView ivsettings;
//
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.mainframelayout)
//    public FrameLayout mainframelayout;
//
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.framelayoutmain)
//    public FrameLayout framelayoutmain;
//
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.llecom)
//    public LinearLayout llecom;
//
//    BottomNavigationView bottomNavigationView;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_fragment2);
//        ButterKnife.bind(this);
//        Home();
//        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(this);
//        bottomNavigationView.setSelectedItemId(R.id.home);
////        HomeFragment fragment = new HomeFragment();
////        getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, fragment).commit();
////
////        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
////            @SuppressLint("NonConstantResourceId")
////            @Override
////            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
////                switch (item.getItemId()) {
////                    case R.id.home:
////                        HomeFragment homeFragment = new HomeFragment();
////                        getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, homeFragment).commit();
////                        bottomNavigationView.setSelectedItemId(R.id.home);
////                        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gold)));
////
////                    case R.id.passbook:
////                        ToastMessage.onToast(MainFragmentActivity.this, "dshvfd", ToastMessage.SUCCESS);
////                        PassBookFragment passBookFragment = new PassBookFragment();
////                        getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, passBookFragment).commit();
////                        bottomNavigationView.setSelectedItemId(R.id.passbook);
////                        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gold)));
////
////                    case R.id.Ecommerce:
////                        Ecommerce_Category_list efragment = new Ecommerce_Category_list();
////                        getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, efragment).commit();
////                        bottomNavigationView.setSelectedItemId(R.id.Ecommerce);
////                        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gold)));
////
////                    case R.id.Aboutus:
////                        AboutUsFragment fragment = new AboutUsFragment();
////                        getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, fragment).commit();
////                        bottomNavigationView.setSelectedItemId(R.id.Aboutus);
////                        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gold)));
////
////                    case R.id.Account:
////                        Settings sfragment = new Settings();
////                        getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, sfragment).commit();
////                        bottomNavigationView.setSelectedItemId(R.id.Account);
////                        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gold)));
////
////                    default:
////                        return true;
////                }
////            }
////        });
//
//        Log.e("MainAccessToken", AccountUtils.getAccessToken(this));
//    }
//
//
//    @SuppressLint({"NewApi", "NonConstantResourceId"})
//    @OnClick(R.id.llhome)
//    public void Home() {
//        getIvdotpassbook();
//        ivdotpassbook.setVisibility(View.GONE);
//        ivdothome.setVisibility(View.VISIBLE);
//        ivdotabout.setVisibility(View.GONE);
//        ivdotsettings.setVisibility(View.GONE);
//        ivdotecom.setVisibility(View.GONE);
//        HomeFragment homeFragment = new HomeFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, homeFragment).commit();
//    }
//
//    public ImageView getIvdotpassbook() {
//        return ivdotpassbook;
//    }
//
//    @SuppressLint({"NewApi", "NonConstantResourceId"})
//    @OnClick(R.id.llpassbook)
//    public void Passbook() {
//        ivdotpassbook.setVisibility(View.VISIBLE);
//        ivdothome.setVisibility(View.GONE);
//        ivdotabout.setVisibility(View.GONE);
//        ivdotsettings.setVisibility(View.GONE);
//        ivdotecom.setVisibility(View.GONE);
//        PassBookFragment passBookFragment = new PassBookFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, passBookFragment).commit();
//    }
//
//    @SuppressLint({"NewApi", "NonConstantResourceId"})
//    @OnClick(R.id.llabout)
//    public void About() {
//        ivdotpassbook.setVisibility(View.GONE);
//        ivdothome.setVisibility(View.GONE);
//        ivdotabout.setVisibility(View.VISIBLE);
//        ivdotsettings.setVisibility(View.GONE);
//        ivdotecom.setVisibility(View.GONE);
//        AboutUsFragment fragment = new AboutUsFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, fragment).commit();
//    }
//
//    @SuppressLint({"NewApi", "NonConstantResourceId"})
//    @OnClick(R.id.llsettings)
//    public void Settings() {
//
//        ivdotpassbook.setVisibility(View.GONE);
//        ivdothome.setVisibility(View.GONE);
//        ivdotabout.setVisibility(View.GONE);
//        ivdotsettings.setVisibility(View.VISIBLE);
//        ivdotecom.setVisibility(View.GONE);
//
//
//
//        Settings fragment = new Settings();
//        getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, fragment).commit();
//        ivsettings.setImageResource(R.drawable.accountfilled);
//    }
//
//    @SuppressLint("NonConstantResourceId")
//    @OnClick(R.id.llecom)
//    public void openecom() {
//        ivdotpassbook.setVisibility(View.GONE);
//        ivdothome.setVisibility(View.GONE);
//        ivdotabout.setVisibility(View.GONE);
//        ivdotsettings.setVisibility(View.GONE);
//        ivdotecom.setVisibility(View.VISIBLE);
//        if (!NetworkUtils.isConnected(this)) {
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//        } else {
//
//            Ecommerce_Category_list fragment = new Ecommerce_Category_list();
//            getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, fragment).commit();
//        }
//    }
//
////    public void buygold(){
////
////        if (!NetworkUtils.isConnected(this)){
////            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
////        }else {
////            Intent intent = new Intent(this, Digital_wallet_fragment.class);
////            startActivity(intent);
////        }
////    }
//
//    private long backPressed;
//    private static final long TIME_DELAY = 2000;
//
//    @Override
//    public void onBackPressed() {
//
//        Fragment f = getSupportFragmentManager().findFragmentById(R.id.mainframelayout);
////        assert f != null;
////        assert f.getTag() != null;
//        assert f != null;
//        assert f.getTag() != null;
//        if(f.getTag().equals("JewelleryInventory")){
////            assert getFragmentManager() != null;
//            getSupportFragmentManager().popBackStack();
//        }
//        else{
//            super.onBackPressed();
//            finish();
//
////                    if (backPressed + TIME_DELAY > System.currentTimeMillis()) {
////            super.onBackPressed();
////            finish();
////        } else {
////            ErrorSnackBar.onBackExit(this, framelayoutmain);
////        }
////        backPressed = System.currentTimeMillis();
//        }
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.bottom_navigation_menu, menu);
//        return true;
//    }
//
////    @Override
////    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
////
////        return true;
////    }
//
//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.passbook:
//                PassBookFragment passBookFragment = new PassBookFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, passBookFragment).commit();
//                return true;
//            case R.id.home:
//                HomeFragment homeFragment = new HomeFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, homeFragment).commit();
//                return true;
//            case R.id.Ecommerce:
//                Ecommerce_Category_list ecommerce_category_list = new Ecommerce_Category_list();
//                getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout,ecommerce_category_list ).commit();
//                return true;
//            case R.id.Aboutus:
//                AboutUsFragment aboutUsFragment = new AboutUsFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout,aboutUsFragment ).commit();
//                return true;
//            case R.id.Account:
//                Settings settings = new Settings();
//                getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout,settings ).commit();
//
//
//
//                return true;
//        }
//return true;
//}
//}


package com.goldsikka.goldsikka.NewDesignsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Activitys.Events.EventModel;
import com.goldsikka.goldsikka.Activitys.Events.Eventlist;
import com.goldsikka.goldsikka.Activitys.FeedbackForm;
import com.goldsikka.goldsikka.Activitys.GetContacts.ContactList;
import com.goldsikka.goldsikka.Activitys.MoneyWallet.AddMonet_to_Wallet;
import com.goldsikka.goldsikka.Activitys.ReferAndEarnActivity;
import com.goldsikka.goldsikka.ComingSoon;
import com.goldsikka.goldsikka.Fragments.Digital_wallet_fragment;
import com.goldsikka.goldsikka.Fragments.Ecommerce.EcommerceHome;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Ecommerce_Category_list;
import com.goldsikka.goldsikka.Fragments.NewDesign.Settings;
import com.goldsikka.goldsikka.Fragments.NewDesignsFragments.AboutUsFragment;
import com.goldsikka.goldsikka.Fragments.NewDesignsFragments.HomeFragment;
import com.goldsikka.goldsikka.Fragments.NewDesignsFragments.PassBookFragment;
import com.goldsikka.goldsikka.Fragments.Reedem_fragment;
import com.goldsikka.goldsikka.Fragments.Schemes.Scheme_Content_Fragment;
import com.goldsikka.goldsikka.Fragments.Sell_Fragment;
import com.goldsikka.goldsikka.OrganizationWalletModule.OrganizationsList;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ErrorSnackBar;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.mainframelayout)
    public FrameLayout mainframelayout;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.framelayoutmain)
    public FrameLayout framelayoutmain;


    BottomNavigationView bottomNavigationView;

    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    ApiDao apiDao;

    ArrayList<EventModel> arrayList = new ArrayList<>();

    ImageView imageView_close, popimage;
    AlertDialog predictalertdialog;
    TextView tvpredictaccess, tvpoptitle;

    public static boolean isFromnboard = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment2);
        ButterKnife.bind(this);
//        Home();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.bringToFront();

        drawerLayout = findViewById(R.id.my_drawer_layout);
        Log.e("MainAccessToken", AccountUtils.getAccessToken(this));
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        navigationView.bringToFront();
// pass the Open and Close toggle for the drawer layout listener
// to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

//        try {
//            String currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//            versionnum.setText(currentVersion);
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }

        Log.e("MainAccessToken", AccountUtils.getAccessToken(this));

        if (isFromnboard) {
            Log.e("isif", "" + isFromnboard);
            loadeventsdetails();

        } else {
            Log.e("iselse", "" + isFromnboard);
        }
    }

    public void getDrawerLayout() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.goldsuvidhamenubtn:
                //  ToastMessage.onToast(getApplicationContext(), "goldsuvidhamenubtn", Toast.LENGTH_SHORT);
                if (!NetworkUtils.isConnected(getApplicationContext())) {
                    ToastMessage.onToast(getApplicationContext(), getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                } else {
                    startActivity(new Intent(getApplicationContext(), Digital_wallet_fragment.class));
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }

                return true;

            case R.id.sellgoldmenubtn:
                startActivity(new Intent(getApplicationContext(), Sell_Fragment.class));
                drawerLayout.closeDrawer(Gravity.LEFT);

                return true;

            case R.id.redeemmenubtn:
                if (!NetworkUtils.isConnected(getApplicationContext())) {
                    ToastMessage.onToast(getApplicationContext(), getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                } else {
                    Intent intent1 = new Intent(getApplicationContext(), Reedem_fragment.class);
                    intent1.putExtra("wallet", AccountUtils.getWalletAmount(getApplicationContext()));
                    intent1.putExtra("amount", "");
                    startActivity(intent1);
                }
                return true;

            case R.id.transfermenubtn:
                if (!NetworkUtils.isConnected(getApplicationContext())) {
                    ToastMessage.onToast(getApplicationContext(), getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                } else {
                    startActivity(new Intent(getApplicationContext(), ContactList.class));
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                return true;

            case R.id.moneywalletmenubtn:
//                ToastMessage.onToast(getApplicationContext(), "moneywalletmenubtn", Toast.LENGTH_SHORT);
                if (!NetworkUtils.isConnected(getApplicationContext())) {
                    ToastMessage.onToast(getApplicationContext(), getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                } else {
                    startActivity(new Intent(getApplicationContext(), AddMonet_to_Wallet.class));
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                return true;

            case R.id.referandearnmenubtn:
                if (!NetworkUtils.isConnected(getApplicationContext())) {
                    ToastMessage.onToast(getApplicationContext(), getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                } else {
                    startActivity(new Intent(getApplicationContext(), ReferAndEarnActivity.class));
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                return true;

            case R.id.feedbackmenubtn:
                if (!NetworkUtils.isConnected(getApplicationContext())) {
                    ToastMessage.onToast(getApplicationContext(), getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                } else {
                    startActivity(new Intent(getApplicationContext(), FeedbackForm.class));
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                return true;

            case R.id.loyalitypointsmenubtn:
                startActivity(new Intent(getApplicationContext(), ComingSoon.class));
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;

//            case R.id.mygoldchitmenubtn:
//                Intent i = new Intent(getApplicationContext(), Scheme_Content_Fragment.class);
//                AccountUtils.setSchemeID(getApplicationContext(), "1");
//                AccountUtils.setSchemename(getApplicationContext(), "MY GOLD 2021 (Gold Chit)");
//                startActivity(i);
//                return true;

            case R.id.mygoldplusmenubtn:
                Intent i1 = new Intent(getApplicationContext(), Scheme_Content_Fragment.class);
                AccountUtils.setSchemeID(getApplicationContext(), "2");
                AccountUtils.setSchemename(getApplicationContext(), "GOLD PLUS PLAN");
                startActivity(i1);
                return true;
            case R.id.Eventsmenubtn:
                startActivity(new Intent(getApplicationContext(), Eventlist.class));
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            case R.id.donatemenubtn:
                startActivity(new Intent(getApplicationContext(), OrganizationsList.class));
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, new HomeFragment()).commit();
                return true;
            case R.id.passbook:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, new PassBookFragment()).commit();
                return true;
            case R.id.Ecommerce:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, new EcommerceHome()).commit();
                return true;
            case R.id.Aboutus:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, new AboutUsFragment()).commit();
                return true;
            case R.id.Account:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, new Settings()).commit();
                return true;

        }
        // ToastMessage.onToast(getApplicationContext(), "call method", Toast.LENGTH_SHORT);
        return true;
    }

    private long backPressed;
    private static final long TIME_DELAY = 2000;

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.mainframelayout);
        assert f != null;
        if (Objects.equals(f.getTag(), "JewelleryInventory")) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (backPressed + TIME_DELAY > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
            } else {
                ErrorSnackBar.onBackExit(this, framelayoutmain);
            }
            backPressed = System.currentTimeMillis();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.bottom_navigation_menu, menu);

        return true;
    }

    @SuppressLint("RtlHardcoded")
    public void predictaccesdialog(String title, String discreption, String image) {
        final Dialog pdialog = new Dialog(MainFragmentActivity.this);
        pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdialog.setCancelable(false);
        pdialog.setContentView(R.layout.predictaccesspopup);
        pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = pdialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;

        //wmlp.gravity = Gravity.BOTTOM | Gravity.END;
        tvpredictaccess = pdialog.findViewById(R.id.tvpredictaccess);
        tvpoptitle = pdialog.findViewById(R.id.tvpoptitle);
        tvpredictaccess.setText(discreption);
        tvpoptitle.setText(title);
        popimage = pdialog.findViewById(R.id.popimg);
        imageView_close = pdialog.findViewById(R.id.iv_close);
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromnboard = false;
                pdialog.dismiss();
            }
        });

        try {
            Glide.with(getApplicationContext()).load(image).into(popimage);
        } catch (Exception ignored) {
            Glide.with(getApplicationContext()).load(R.drawable.background_image).into(popimage);
        }
        pdialog.show();
        //predictalertdialog.getWindow().setLayout(720, 1020);
        // dialog.getWindow().setLayout(820, WindowManager.LayoutParams.WRAP_CONTENT);

    }


    public void loadeventsdetails() {

        arrayList.clear();
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
            Call<EventModel> getdetails = apiDao.advertaisepopup("Bearer " + AccountUtils.getAccessToken(this));
            getdetails.enqueue(new Callback<EventModel>() {
                @Override
                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                    int statuscode = response.code();
                    Log.e("body", String.valueOf(response.body()));

                    Log.e("statuscodeloaddata", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        Log.e("body", String.valueOf(response.body()));
                        if (response.body() != null) {
                            Log.e("body", String.valueOf(response.body()));
                            List<EventModel> list = response.body().getResult();
                            String modelnext = response.body().getTitle();
                            String dis = response.body().getDescription();
                            String img = response.body().getImage_uri();
                            dialog.dismiss();
                            if (list.size() != 0) {

                            } else {
                                predictaccesdialog(modelnext, dis, img);
                            }
                        } else {
                            Log.e("body", "is empty");
                        }

                    } else {

                        dialog.dismiss();
                        // ToastMessage.onToast(getApplicationContext(), "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
//                    ToastMessage.onToast(getApplicationContext(), "We have some issue", ToastMessage.ERROR);
                }
            });
        }
    }
}