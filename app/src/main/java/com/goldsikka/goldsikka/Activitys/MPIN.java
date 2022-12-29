package com.goldsikka.goldsikka.Activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;

import java.util.Objects;

public class MPIN extends AppCompatActivity {

    boolean ispin;
    String accesstoken;
    String token,from;

    TextView cpin, upin, dpin;

    View v1, v2 , v3;

    int complete = 0;

    String stpin,strepin,stcpin;
//    String accesstoken;
//    String token,from;
    private shared_preference sharedPreference;
    String value = "0";
    ApiDao apiDao;
//    boolean ispin;
    ActionBar actionBar;

    TextView unameTv, uidTv;

    RelativeLayout backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.Gold));
        }

        Objects.requireNonNull(getSupportActionBar()).hide();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#1A2E23"));

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));

        cpin = findViewById(R.id.createpinid);
        upin = findViewById(R.id.updatepinid);


        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            accesstoken = bundle.getString("accesstoken");
            from = bundle.getString("from");
            token = bundle.getString("token");
            ispin = bundle.getBoolean("isPin");
        }

        if(ispin){
            upin.setVisibility(View.VISIBLE);
            v2.setVisibility(View.VISIBLE);
//            dpin.setVisibility(View.VISIBLE);
//            v3.setVisibility(View.VISIBLE);
        }else{
            cpin.setVisibility(View.VISIBLE);
            v1.setVisibility(View.VISIBLE);
//            v2.setVisibility(View.VISIBLE);
        }


        cpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ispin) {
                    Intent intent = new Intent(MPIN.this, CreatePIN.class);
                    intent.putExtra("from", "Generatepin");
                    intent.putExtra("token", "");
                    intent.putExtra("isPin", AccountUtils.getIsPin(MPIN.this));
                    startActivity(intent);
                }
            }
        });

        upin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ispin){
                    Intent intent = new Intent(MPIN.this, CreatePIN.class);
                    intent.putExtra("from", "Generatepin");
                    intent.putExtra("token", "");
                    intent.putExtra("isPin", AccountUtils.getIsPin(MPIN.this));
                    startActivity(intent);
                }
            }
        });

//        dpin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                AccountUtils.setAccessToken(MPIN.this, "");
////                AccountUtils.setIsPin(MPIN.this, false);
////                ToastMessage.onToast(MPIN.this, from, ToastMessage.SUCCESS);
//
//            }
//        });


    }
}