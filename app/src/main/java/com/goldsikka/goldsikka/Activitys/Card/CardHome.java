package com.goldsikka.goldsikka.Activitys.Card;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CardHome extends AppCompatActivity {

    TextView cuname, uname, cgrams;
    String st_ingrams;

    RelativeLayout backbtn;

    TextView unameTv, uidTv, titleTv, cwish;

    shared_preference sharedPreference;
    ApiDao apiDao;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_home);

        Objects.requireNonNull(getSupportActionBar()).hide();

        cuname = findViewById(R.id.cuname);
        uname = findViewById(R.id.uname);
        cgrams = findViewById(R.id.cgrams);
        cwish = findViewById(R.id.cwish);

        unameTv = findViewById(R.id.unamee);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        cuname.setText(AccountUtils.getName(this));
        uname.setText(AccountUtils.getName(this));
        cgrams.setText(AccountUtils.getWalletAmount(this)+" g");
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Gold Card");

        String currentTime = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        if(Integer.parseInt(currentTime)<12)
            cwish.setText("Good Morning,");
        else if(Integer.parseInt(currentTime)>=12 && Integer.parseInt(currentTime)<16)
            cwish.setText("Good Afternoon,");
        else if(Integer.parseInt(currentTime)>=16 && Integer.parseInt(currentTime)<=24)
            cwish.setText("Good Evening,");

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sharedPreference = new shared_preference(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}