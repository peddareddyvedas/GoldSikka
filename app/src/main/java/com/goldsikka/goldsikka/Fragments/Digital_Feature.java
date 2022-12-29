package com.goldsikka.goldsikka.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goldsikka.goldsikka.Adapter.DigitalWallet_Content.Digital_wallet_Feature;
import com.goldsikka.goldsikka.Adapter.digital_wallet_content_adatper;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.data;
import com.goldsikka.goldsikka.model.dw_contentlist;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Digital_Feature extends AppCompatActivity {

    private ArrayList<Listmodel> itemList;
    private Digital_wallet_Feature adapter;

    JSONArray jsonArray;
    String tearms_condition;
    dw_contentlist item;
    ApiDao apiDao;

    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;

    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digital_wallet_feature);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//            setTitle("Features");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Features");
        // toolbar.setTitleTextColor(getColor(R.color.white));
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        initlizerecyclerview();

    }

    public void initlizerecyclerview() {
        RecyclerView rvRequest = findViewById(R.id.rv_list);
        rvRequest.setHasFixedSize(true);
        rvRequest.setLayoutManager(new LinearLayoutManager(this));
        rvRequest.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                this, LinearLayoutManager.VERTICAL);
        rvRequest.addItemDecoration(decoration);

        itemList = new ArrayList<>();
        adapter = new Digital_wallet_Feature(this, itemList);
        rvRequest.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getdata();

    }

    public void getdata() {
        itemList.clear();
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
            Call<data> getDidigtalwallet_featurecontent = apiDao.getDidigtalwallet_featurecontent("Bearer " + AccountUtils.getAccessToken(this));
            getDidigtalwallet_featurecontent.enqueue(new Callback<data>() {
                @Override
                public void onResponse(Call<data> call, Response<data> response) {
                    int statuscode = response.code();
                    Log.e("statuscode", "" + statuscode);
                    List<Listmodel> list = response.body().getFeatures();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        for (Listmodel listmodel : list) {
                            itemList.add(listmodel);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<data> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(Digital_Feature.this, "We have some issues ", ToastMessage.ERROR);
                }
            });

        }
    }
}
