package com.goldsikka.goldsikka.Fragments.Schemes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Adapter.Scheme_Content_Adapter;
import com.goldsikka.goldsikka.Adapter.Scheme_Feature_Adapter;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.data;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Scheme_features extends AppCompatActivity {

    ApiDao apiDao;
    ArrayList<Listmodel> arrayList;
    Scheme_Feature_Adapter adapter;
    String schemeid;


    RecyclerView rv_schemefeature;

    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;

    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheme_features);

        ButterKnife.bind(this);
        rv_schemefeature = findViewById(R.id.rv_schemefeature);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            schemeid = bundle.getString("SchemeId");

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Features");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Features");
        //toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        init_features();
        intilizerecyclerview();

    }

    public void intilizerecyclerview() {
        rv_schemefeature.setHasFixedSize(true);
        rv_schemefeature.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<>();
        adapter = new Scheme_Feature_Adapter(this, arrayList);
        rv_schemefeature.setAdapter(adapter);

    }

    public void init_features() {
//        arrayList.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait.....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<data> getfeatures = apiDao.getfeaturecontent("Bearer " + AccountUtils.getAccessToken(this)
                    , schemeid);
            getfeatures.enqueue(new Callback<data>() {
                @Override
                public void onResponse(Call<data> call, Response<data> response) {
                    int statuscode = response.code();
                    Log.e("featurs", String.valueOf(statuscode));
                    List<Listmodel> list = response.body().getScheme_features();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        if (list.size() != 0) {
                            for (Listmodel listmodel : list) {
                                arrayList.add(listmodel);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e("id", String.valueOf(list));
                            Toast.makeText(Scheme_features.this, "No Data Available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dialog.dismiss();
                        // ToastMessage.onToast(activity,"Techincal issues",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<data> call, Throwable t) {
                    dialog.dismiss();
                   // ToastMessage.onToast(Scheme_features.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

}
