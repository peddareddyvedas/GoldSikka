package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.goldsikka.goldsikka.Activitys.Events.Eventlist;
import com.goldsikka.goldsikka.Activitys.FeedbackForm;
import com.goldsikka.goldsikka.Activitys.GetContacts.ContactList;
import com.goldsikka.goldsikka.Activitys.MoneyWallet.AddMonet_to_Wallet;
import com.goldsikka.goldsikka.Activitys.ReferAndEarnActivity;
import com.goldsikka.goldsikka.ComingSoon;
import com.goldsikka.goldsikka.Fragments.Digital_wallet_fragment;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Ecommerce_Category_list;
import com.goldsikka.goldsikka.Fragments.NewDesign.Settings;
import com.goldsikka.goldsikka.Fragments.NewDesignsFragments.AboutUsFragment;
import com.goldsikka.goldsikka.Fragments.NewDesignsFragments.HomeFragment;
import com.goldsikka.goldsikka.Fragments.NewDesignsFragments.PassBookFragment;
import com.goldsikka.goldsikka.Fragments.Reedem_fragment;
import com.goldsikka.goldsikka.Fragments.Schemes.Scheme_Content_Fragment;
import com.goldsikka.goldsikka.Fragments.Sell_Fragment;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ErrorSnackBar;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Organizationwallet_mainpage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    String contactId = "";
    String displayName = "";
    String phoneNumber;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.mainframelayout)
    FrameLayout mainframelayout;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.framelayoutmain)
    FrameLayout framelayoutmain;

    BottomNavigationView bottomNavigationView;

    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public ActionBarDrawerToggle actionBarDrawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizationwallet_mainpage);
        ButterKnife.bind(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.bringToFront();

        // requestPermissions();

        Log.e("MainAccessToken", AccountUtils.getAccessToken(this));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, new OrganizationHomeFragment()).commit();
                return true;
            case R.id.passbook:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, new OrganizationPassbookFragment()).commit();
                return true;
            case R.id.Aboutus:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, new OrganizationAboutUsFragment()).commit();
                return true;
            case R.id.Account:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainframelayout, new OrganizationSettingsFragment()).commit();
                return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.org_bottom_navigation_menu, menu);

        return true;
    }

    private  long backPressed;
    private static final long TIME_DELAY = 2000;
    @Override
    public void onBackPressed() {

        if (backPressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            ErrorSnackBar.onBackExit(this, framelayoutmain);
        }
        backPressed = System.currentTimeMillis();
    }

}
