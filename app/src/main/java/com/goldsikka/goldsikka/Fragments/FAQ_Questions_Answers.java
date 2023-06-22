package com.goldsikka.goldsikka.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Adapter.Faqlistadapter;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.goldsikka.goldsikka.netwokconnection.NetworkConnection;
import com.goldsikka.goldsikka.model.faqlist;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;

public class FAQ_Questions_Answers extends AppCompatActivity {

    private ArrayList<Listmodel> itemList;
    private Faqlistadapter adapter;
   // LinearLayout linearlayout;
    String ids;


    @SuppressLint("NewApi")
    @Nullable
    @Override
          protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.faq_questions_answers);
     //   linearlayout = findViewById(R.id.linearlayout);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("FAQs");
       // toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();


        ids = bundle.getString("faq_ids");

        initlizerecyclerview();


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


    public void initlizerecyclerview(){
        RecyclerView rvRequest = findViewById(R.id.rv_list);
        rvRequest.setHasFixedSize(true);
        rvRequest.setLayoutManager(new LinearLayoutManager(this));
        rvRequest.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                this, LinearLayoutManager.VERTICAL);
        rvRequest.addItemDecoration(decoration);

        itemList = new ArrayList<>();
        adapter = new Faqlistadapter(itemList);
        rvRequest.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getquestions_answers();
    }

    public void getquestions_answers() {

        if (!NetworkConnection.isConnected(this)) {
            Toast.makeText(FAQ_Questions_Answers.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();

            return;
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait....");
            progressDialog.setCancelable(false);
            progressDialog.show();


            ApiDao apiService = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<List<Listmodel>> call = apiService.getfaqs("Bearer " + AccountUtils.getAccessToken(this), ids);
            call.enqueue(new Callback<List<Listmodel>>() {

                @Override
                public void onResponse(@NonNull Call<List<Listmodel>> call, @NonNull retrofit2.Response<List<Listmodel>> response) {
                    int stauscode = response.code();
                    List<Listmodel> subcategoryList = response.body();

                    if (stauscode == HttpsURLConnection.HTTP_OK || stauscode == HttpsURLConnection.HTTP_CREATED) {
                        if (subcategoryList != null) {
                            for (Listmodel listmodel : subcategoryList) {
//                                linearlayout.setVisibility(View.GONE);
                                itemList.add(listmodel);
                                adapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                        } else {
                            progressDialog.dismiss();
                            //linearlayout.setVisibility(View.VISIBLE);
                        }

                    }

                }

                @Override
                public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                    // Log error here since request failed
                //    Toast.makeText(FAQ_Questions_Answers.this, "Sorry! We Have some technical issue", Toast.LENGTH_SHORT).show();
                    //  gifloading.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
            });
        }
    }

}
