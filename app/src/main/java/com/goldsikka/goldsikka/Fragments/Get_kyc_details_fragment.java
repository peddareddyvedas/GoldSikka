package com.goldsikka.goldsikka.Fragments;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.goldsikka.goldsikka.Activitys.Kyc_Details;
import com.goldsikka.goldsikka.Activitys.Profile_Details;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Get_kyc_details_fragment extends AppCompatActivity {

    TextView tv_gender, tv_spousename, tv_fathername, tv_mobile, tv_aadhaar, tv_pan;
    LinearLayout ll_kyc, ll_bankempty;
    String st_fathername, st_spousename, st_aadhaar, st_pannumber, st_alternate_phone, st_gender;
    ApiDao apiDao;
    Button updatekyc;

    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;


    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_kycdetails);
        ButterKnife.bind(this);

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("KYC");
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            intilizeviews();
            get_kycdetails();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void intilizeviews() {

        ll_bankempty = findViewById(R.id.linearlayout);
        ll_kyc = findViewById(R.id.ll_kyc);

        tv_gender = findViewById(R.id.tv_gender);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_aadhaar = findViewById(R.id.tv_aadhaar);
        tv_pan = findViewById(R.id.tv_pan);
        tv_fathername = findViewById(R.id.tv_father);
        tv_spousename = findViewById(R.id.tv_spouse);

        updatekyc = findViewById(R.id.updatekyc);
        updatekyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Kyc_Details.class);
                //   String st_fathername, st_spousename, st_aadhaar, st_pannumber, st_alternate_phone, st_gender;
                intent.putExtra("st_fathername", st_fathername);
                intent.putExtra("st_spousename", st_spousename);
                intent.putExtra("st_aadhaar", st_aadhaar);
                intent.putExtra("st_pannumber", st_pannumber);
                intent.putExtra("st_alternate_phone", st_alternate_phone);
                intent.putExtra("st_gender", st_gender);
                startActivity(intent);
            }
        });
    }

    private void get_kycdetails() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Listmodel> get_kycdetails = apiDao.get_kyc_details("Bearer " + AccountUtils.getAccessToken(this));
        get_kycdetails.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(@NonNull Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                Listmodel listmodel = response.body();
                if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED ||
                        statuscode == HttpsURLConnection.HTTP_ACCEPTED) {
                    ll_bankempty.setVisibility(View.GONE);
                    ll_kyc.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    assert listmodel != null;
                    st_fathername = listmodel.getFather_name();
                    st_aadhaar = listmodel.getAadhaar_card();
                    st_spousename = listmodel.getSpouse_name();
                    st_pannumber = listmodel.getPan_card();
                    st_alternate_phone = listmodel.getAlternate_phone();
                    st_gender = listmodel.getGender_name();
                    if (st_fathername == null) {
                        ll_bankempty.setVisibility(View.VISIBLE);
                        ll_kyc.setVisibility(View.GONE);
                    }
                    if (st_fathername == null) {
                        ll_bankempty.setVisibility(View.VISIBLE);
                        ll_kyc.setVisibility(View.GONE);
                    }
                    if (st_aadhaar == null) {
                        ll_bankempty.setVisibility(View.VISIBLE);
                        ll_kyc.setVisibility(View.GONE);
                    }
                    if (st_spousename == null) {
                        ll_bankempty.setVisibility(View.VISIBLE);
                        ll_kyc.setVisibility(View.GONE);
                    }
                    if (st_pannumber == null) {
                        ll_bankempty.setVisibility(View.VISIBLE);
                        ll_kyc.setVisibility(View.GONE);
                    }
                    if (st_alternate_phone == null) {
                        ll_bankempty.setVisibility(View.VISIBLE);
                        ll_kyc.setVisibility(View.GONE);
                    }
                    if (st_gender == null) {
                        ll_bankempty.setVisibility(View.VISIBLE);
                        ll_kyc.setVisibility(View.GONE);
                    } else {
                        ll_bankempty.setVisibility(View.GONE);
                        ll_kyc.setVisibility(View.VISIBLE);
                        set_kycdetails();

                    }


                } else {
                    dialog.dismiss();
                   // ToastMessage.onToast(Get_kyc_details_fragment.this, "Technical issue", ToastMessage.ERROR);
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                dialog.dismiss();
                ToastMessage.onToast(Get_kyc_details_fragment.this, "We have some issue", ToastMessage.ERROR);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void set_kycdetails() {
        tv_aadhaar.setText(": " + st_aadhaar);
        tv_fathername.setText(": " + st_fathername);
        tv_spousename.setText(": " + st_spousename);
        tv_gender.setText(": " + st_gender);
        tv_pan.setText(": " + st_pannumber);
        tv_mobile.setText(": " + st_alternate_phone);
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_kycdetails();


    }
}