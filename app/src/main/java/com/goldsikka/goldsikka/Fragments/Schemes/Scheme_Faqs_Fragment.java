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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Adapter.Scheme_Faqs_Adapter;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.data;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;


import java.net.HttpURLConnection;
import java.util.ArrayList;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Scheme_Faqs_Fragment extends AppCompatActivity {

    RecyclerView rv_schemefaqs;

    ArrayList<Listmodel> arrayList;
    Scheme_Faqs_Adapter adapter;
    String schemeid;
    ApiDao apiDao;

    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;

    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheme_faqs);

        ButterKnife.bind(this);
        rv_schemefaqs = findViewById(R.id.rv_schemefaqs);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            schemeid = bundle.getString("SchemeId");

        }
        Log.e("faeq", String.valueOf(schemeid));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Faq's");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("FAQs");
        //toolbar.setTitleTextColor(getColor(R.color.colorWhite));

        intilizerecyclerview();
        init_faqs();

    }

    public void intilizerecyclerview() {
        rv_schemefaqs.setHasFixedSize(true);
        rv_schemefaqs.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<>();
        adapter = new Scheme_Faqs_Adapter(this, arrayList);
        rv_schemefaqs.setAdapter(adapter);

    }

    public void init_faqs() {
        //  arrayList.clear();
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
            Call<data> getfaqs = apiDao.getfaqcontent("Bearer " + AccountUtils.getAccessToken(this), schemeid);
            getfaqs.enqueue(new Callback<data>() {
                @Override
                public void onResponse(Call<data> call, Response<data> response) {
                    int statuscode = response.code();
                    Log.e("faeq", String.valueOf(statuscode));
                    List<Listmodel> list = response.body().getScheme_faqs();
                    Log.e("faeq",""+ list.toString());

                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        if (list.size() != 0) {
                            for (Listmodel listmodel : list) {
                                arrayList.add(listmodel);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(Scheme_Faqs_Fragment.this, "No Data Available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<data> call, Throwable t) {
                    Log.e("faeq",""+t);

                    dialog.dismiss();
                    //   ToastMessage.onToast(Scheme_Faqs_Fragment.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }
}
