package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.goldsikka.goldsikka.Activitys.Kyc_Details;
import com.goldsikka.goldsikka.Fragments.Get_kyc_details_fragment;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import javax.net.ssl.HttpsURLConnection;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationKYCdetails extends AppCompatActivity {

    TextView tv_gender,tv_spousename,tv_fathername,tv_mobile,tv_aadhaar,tv_pan;
    LinearLayout ll_kyc,ll_bankempty;
    String st_fathername,st_spousename,st_aadhaar,st_pannumber,st_alternate_phone,st_gender;
    ApiDao apiDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_kyc_details);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("KYC");
        //  toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        intilizeviews();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            get_kycdetails();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void intilizeviews(){

        ll_bankempty = findViewById(R.id.linearlayout);
        ll_kyc = findViewById(R.id.ll_kyc);

        tv_gender =findViewById(R.id.org_tvgender);
        tv_mobile =findViewById(R.id.org_tvmobile);
        tv_aadhaar =findViewById(R.id.org_tvaadhaar);
        tv_pan =findViewById(R.id.org_tvpan);
        tv_fathername =findViewById(R.id.tv_fathername);
        tv_spousename =findViewById(R.id.tv_spousename);

    }

    private void get_kycdetails() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Listmodel> get_kycdetails = apiDao.get_kyc_details("Bearer "+AccountUtils.getAccessToken(this));
        get_kycdetails.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                Listmodel listmodel = response.body();
                if (statuscode == HttpsURLConnection.HTTP_OK||statuscode == HttpsURLConnection.HTTP_CREATED||
                        statuscode == HttpsURLConnection.HTTP_ACCEPTED){
                    ll_bankempty.setVisibility(View.GONE);
                    ll_kyc.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    st_fathername = listmodel.getFather_name();
                    st_aadhaar = listmodel.getAadhaar_card();
                    st_spousename = listmodel.getSpouse_name();
                    st_pannumber = listmodel.getPan_card();
                    st_alternate_phone = listmodel.getAlternate_phone();
                    st_gender = listmodel.getGender_name();

                    if (st_fathername == null){
                        ll_bankempty.setVisibility(View.VISIBLE);
                        ll_kyc.setVisibility(View.GONE);
                    }
                    if (st_fathername == null){
                        ll_bankempty.setVisibility(View.VISIBLE);
                        ll_kyc.setVisibility(View.GONE);
                    }
                    if (st_aadhaar == null){
                        ll_bankempty.setVisibility(View.VISIBLE);
                        ll_kyc.setVisibility(View.GONE);
                    }
                    if (st_spousename == null){
                        ll_bankempty.setVisibility(View.VISIBLE);
                        ll_kyc.setVisibility(View.GONE);
                    }
                    if (st_pannumber == null){
                        ll_bankempty.setVisibility(View.VISIBLE);
                        ll_kyc.setVisibility(View.GONE);
                    }
                    if (st_alternate_phone == null){
                        ll_bankempty.setVisibility(View.VISIBLE);
                        ll_kyc.setVisibility(View.GONE);
                    }
                    if (st_gender == null){
                        ll_bankempty.setVisibility(View.VISIBLE);
                        ll_kyc.setVisibility(View.GONE);
                    }
                    else {
                        ll_bankempty.setVisibility(View.GONE);
                        ll_kyc.setVisibility(View.VISIBLE);
                        set_kycdetails();

                    }



                }
                else {
                    dialog.dismiss();
                   // ToastMessage.onToast(OrganizationKYCdetails.this,"Technical issue",ToastMessage.ERROR);
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                dialog.dismiss();
              //  ToastMessage.onToast(OrganizationKYCdetails.this,"We have some issue",ToastMessage.ERROR);
            }
        });
    }
    @SuppressLint("SetTextI18n")
    public void set_kycdetails(){
        tv_aadhaar.setText(": "+st_aadhaar);
//        tv_fathername.setText("Father's Name : "+st_fathername);
//        tv_spousename.setText("Spouse Name : "+st_spousename);
        tv_gender.setText(": " +st_gender);
        tv_pan.setText(": " +st_pannumber);
        tv_mobile.setText(": "+st_alternate_phone);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.org_btnkyc)
    public void add_kyc(){
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {
            Intent intent = new Intent(this, OrganizationAddKycDetails.class);
            startActivity(intent);
        }

    }
}
