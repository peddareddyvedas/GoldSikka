package com.goldsikka.goldsikka.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;

public class FAQsFragment extends AppCompatActivity implements View.OnClickListener {

    TextView btnaboutgoldsikka,btnregstration,btnoperations,btntaxesapplicable,btngoldbuildplan;

    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.faqfragment);



            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle("FAQs");

        //toolbar.setTitleTextColor(getColor(R.color.colorWhite));
                        ActionBar actionBar = getSupportActionBar();
                        if (actionBar != null) {
                            actionBar.setDisplayHomeAsUpEnabled(true);
                        }


        initlizeviews();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initlizeviews(){

        btnaboutgoldsikka = findViewById(R.id.btn_aboutgoldsikka);
        btnregstration = findViewById(R.id.btn_registration);
        btnoperations = findViewById(R.id.btn_operations);
        btntaxesapplicable = findViewById(R.id.btn_taxesapplicable);
   //     btngoldbuildplan = findViewById(R.id.btn_goldbuildplan);

        btnaboutgoldsikka.setOnClickListener(this);
        btnoperations.setOnClickListener(this);
//        btngoldbuildplan.setOnClickListener(this);
        btnregstration.setOnClickListener(this);
        btntaxesapplicable.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_aboutgoldsikka:
                openquestions("AB");
                return;

            case R.id.btn_registration:
                openquestions("RE");
                return;

            case R.id.btn_taxesapplicable:
                openquestions("TX");
                return;
            case R.id.btn_operations:
                openquestions("OP");
                return;

            case R.id.btn_goldbuildplan:
                openquestions("GB");
                return;

        }
    }

    public void openquestions(String ids){
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            Intent intent = new Intent(this, FAQ_Questions_Answers.class);
            intent.putExtra("faq_ids", ids);
            startActivity(intent);
        }


    }
}
