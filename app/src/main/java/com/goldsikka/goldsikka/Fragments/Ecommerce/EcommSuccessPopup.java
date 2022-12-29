package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.ApiDao;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class EcommSuccessPopup extends AppCompatActivity {

    TextView tv_content;
    String grams, number, amount, from, tennure_type;
    Button bt_continue, btdownload;

    ApiDao apiDao;

    Date currentTime;

    private static final String TAG = "MainActivity";
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txtProgressPercent)
    TextView txtProgressPercent;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    String sttrnsid, stdescription;
    private static final int STORAGE_CODE = 100;
    ResponseBody responseBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecomsuccess_activity);

        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            stdescription = bundle.getString("description");
        }

        tv_content = findViewById(R.id.tv_content);
        tv_content.setText(stdescription);
        bt_continue = findViewById(R.id.bt_continue);
        bt_continue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EcommSuccessPopup.this, EcommMyOrdersActivity.class);
                startActivity(intent);


            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainFragmentActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }


}


