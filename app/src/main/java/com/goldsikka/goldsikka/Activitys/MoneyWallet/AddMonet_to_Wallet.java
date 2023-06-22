package com.goldsikka.goldsikka.Activitys.MoneyWallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goldsikka.goldsikka.Activitys.BaseActivity;
import com.goldsikka.goldsikka.Activitys.Events.Fragments.EventsListAdapter;
import com.goldsikka.goldsikka.Activitys.MoneyWallet.Adapter.WalletAddAdapter;
import com.goldsikka.goldsikka.Activitys.Passbook_Activity;
import com.goldsikka.goldsikka.Fragments.baseinterface;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMonet_to_Wallet extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.lladdmoney)
    LinearLayout llcreateevent;

    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar toolbar;
    WalletAddAdapter adapter;

    TextView uidTv, unameTv, titleTv;

    RelativeLayout backbtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monet_to_wallet);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));


        titleTv = findViewById(R.id.title);
        titleTv.setVisibility(View.VISIBLE);

     /*   if (toolbar != null) {
            boolean condition = false;
            if (condition) {
//                toolbar.setTitle( "Booking Account" );
                titleTv.setText("Booking Account");

            } else {
//                toolbar.setTitle( "Transactions" );
                titleTv.setText("Transactions");

            }
            setSupportActionBar( toolbar );
        }*/
        titleTv.setText("Booking Account");
        adapter = new WalletAddAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);

        tabLayout.setBackgroundColor(getResources().getColor(R.color.white)); // Set the background color for the TabLayout if needed
        tabLayout.setTabTextColors( ContextCompat.getColor(this, R.color.Gold),
                ContextCompat.getColor(this, R.color.white));
        tabLayout.setTabTextColors( Color.BLACK, Color.WHITE);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View tabView = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tabView.setBackground(getDrawable(R.drawable.tab_background)); // Apply the custom background drawable
            }
        }


/*
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    viewPager.setCurrentItem(tab.getPosition());
                    int TabPosition = tab.getPosition();
                    int TabCount = tabLayout.getTabCount();

                    try {
                        if (TabPosition == 0) {
                            GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.policy_tab_blue);
                            drawable.setCornerRadii(new float[]{10,10,0,0,0,0,10,10}); // this will create the corner radious to left side

                        } else {
                            GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.policy_tab_white);
                            drawable.setCornerRadii(new float[]{0,0,10,10,10,10,0,0}); // this will create the corner radious to right side

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.i("TabPosition:--->", TabPosition + "");
                    Log.i("TabCount:--->", TabCount + "");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
*/
    }

//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_add_monet_to_wallet;
//    }
//
//    @Override
//    protected void initView() {
//
//        ButterKnife.bind(this);
//        viewPager = findViewById(R.id.viewpager);
//        tabLayout = findViewById(R.id.tablayout);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle("Money Wallet");
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//
//        adapter = new WalletAddAdapter(getSupportFragmentManager());
//        tabLayout.setupWithViewPager(viewPager);
//        viewPager.setAdapter(adapter);
//
//    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        finish();
    }



}