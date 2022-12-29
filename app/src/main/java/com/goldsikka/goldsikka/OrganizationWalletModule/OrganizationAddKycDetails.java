package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.goldsikka.goldsikka.Activitys.Kyc_Details;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationAddKycDetails extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.org_etfathername)
    EditText et_fathername;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.org_etspousename)
    EditText et_spousename;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.org_etaadhaar)
    EditText et_aadhaar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.org_etpannumber)
    EditText et_pannumber;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.org_alternate_phone)
    EditText et_alternate_phone;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.org_tvfathername)
    TextView tv_fathername;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.org_tvspousename)
    TextView tv_spousename;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.org_tvaadhaar)
    TextView tv_aadhaar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.org_tvpannumber)
    TextView tv_pannumber;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.org_tvalternate_phone)
    TextView tv_alternate_phone;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.org_tvgender)
    TextView tv_gender;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.org_rbmale)
    RadioButton rb_male;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.org_rbfemale)
    RadioButton rb_female;
    // GifImageView loading_gif;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.org_btnkycsubmit)
    Button btn_kycsubmit;


    RadioGroup rg_group;
    ApiDao apiDao;
    RadioButton radioButton;
    String st_fathername,st_spousename,st_aadhaar,st_pannumber,st_alternate_phone,st_gender;
TextView unameTv, uidTv, titleTv;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_addkyc_details);

        ButterKnife.bind(this);
        //   loading_gif = findViewById(R.id.loading_gif);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("KYC");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("KYC");
        //  toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        intillizeview();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //finish();
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }
    private void intillizeview() {
        rg_group = findViewById(R.id.rg_group);



    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @OnClick(R.id.org_btnkycsubmit)
    public void init_validation(View v){

        tv_aadhaar.setVisibility(View.GONE);
        tv_alternate_phone.setVisibility(View.GONE);
        tv_fathername.setVisibility(View.GONE);
        tv_spousename.setVisibility(View.GONE);
        tv_pannumber.setVisibility(View.GONE);

        btn_kycsubmit.setVisibility(View.GONE);
        // loading_gif.setVisibility(View.VISIBLE);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                OrganizationAddKycDetails.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(OrganizationAddKycDetails.this)){
                            btn_kycsubmit.setVisibility(View.VISIBLE);
                            // loading_gif.setVisibility(View.GONE);
                            ToastMessage.onToast(OrganizationAddKycDetails.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            return;
                        }else {
                            validation(v);
                            btn_kycsubmit.setVisibility(View.VISIBLE);
                            //    loading_gif.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }, 500);
    }
    public void validation (View view){
        tv_aadhaar.setVisibility(View.GONE);
        tv_alternate_phone.setVisibility(View.GONE);
        tv_fathername.setVisibility(View.GONE);
        tv_spousename.setVisibility(View.GONE);
        tv_pannumber.setVisibility(View.GONE);

        int selectedId = rg_group.getCheckedRadioButtonId();
        radioButton  = findViewById(selectedId);
        if (rb_male.isChecked()){
            st_gender = "M";
        }else if(rb_female.isChecked()){
            st_gender ="F";
        }
        st_fathername = et_fathername.getText().toString().trim();
        st_spousename = et_spousename.getText().toString().trim();
        st_aadhaar = et_aadhaar.getText().toString().trim();
        st_pannumber = et_pannumber.getText().toString().trim();
        st_alternate_phone = et_alternate_phone.getText().toString().trim();


        if(rg_group.getCheckedRadioButtonId()==-1)
        {
            ToastMessage.onToast(this,"Please select Gender",ToastMessage.ERROR);

        }
//        else if (st_fathername.isEmpty()){
//            tv_fathername.setVisibility(View.VISIBLE);
//            tv_fathername.setText("please Enter Father's Name");
//        }  else if (st_spousename.isEmpty()){
//            tv_spousename.setVisibility(View.VISIBLE);
//            tv_spousename.setText("please Enter Spouse Name");
//        }
        else   if (st_aadhaar.isEmpty()){
            tv_aadhaar.setVisibility(View.VISIBLE);
            tv_aadhaar.setText("please Enter Aadhaar number");
        }
        else if (st_pannumber.isEmpty()){
            tv_pannumber.setVisibility(View.VISIBLE);
            tv_pannumber.setText("Plase Enter Pan Number");
        }
        else if (st_alternate_phone.isEmpty()){
            tv_alternate_phone.setVisibility(View.VISIBLE);
            tv_alternate_phone.setText("Plase Enter Alternate Phone Number");
        }
        else {
            if (!NetworkUtils.isConnected(this)){
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                return;
            }else {
                update_kycdetails();
            }
        }

    }
    public void update_kycdetails(){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> update_kyc = apiDao.get_kyc("Bearer " + AccountUtils.getAccessToken(this),
                    st_gender, st_fathername, st_spousename, st_aadhaar, st_pannumber, st_alternate_phone);
            update_kyc.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    List<Listmodel> list = Collections.singletonList(response.body());
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED ||
                            statuscode == HttpsURLConnection.HTTP_ACCEPTED) {
                        for (Listmodel listmodel : list) {
                            dialog.dismiss();
                            onsuccess();

                        }
                    } else {
                        dialog.dismiss();
                        try {
                            tv_gender.setVisibility(View.GONE);
                            tv_aadhaar.setVisibility(View.GONE);
                            tv_alternate_phone.setVisibility(View.GONE);
                            tv_fathername.setVisibility(View.GONE);
                            tv_spousename.setVisibility(View.GONE);
                            tv_pannumber.setVisibility(View.GONE);

                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            ToastMessage.onToast(OrganizationAddKycDetails.this, st, ToastMessage.ERROR);
                            JSONObject er = jObjError.getJSONObject("errors");
                            try {
                                JSONArray array_gender = er.getJSONArray("gender");
                                for (int i = 0; i < array_gender.length(); i++) {
                                    tv_gender.setVisibility(View.VISIBLE);
                                    tv_gender.setText(array_gender.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray array_phone = er.getJSONArray("alternate_phone");
                                for (int i1 = 0; i1 < array_phone.length(); i1++) {
                                    tv_alternate_phone.setVisibility(View.VISIBLE);
                                    //   etemail.setError(array_email.getString(i).toString());
                                    tv_alternate_phone.setText(array_phone.getString(i1));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            try {
                                JSONArray array_pannumber = er.getJSONArray("pan_card");

                                for (int i = 0; i < array_pannumber.length(); i++) {
                                    tv_pannumber.setVisibility(View.VISIBLE);
                                    tv_pannumber.setText(array_pannumber.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                            try {
                                JSONArray array_aadhaarnumber = er.getJSONArray("aadhaar_card");

                                for (int i = 0; i < array_aadhaarnumber.length(); i++) {
                                    tv_aadhaar.setVisibility(View.VISIBLE);
                                    tv_aadhaar.setText(array_aadhaarnumber.getString(i));

                                }
//
                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                  //  ToastMessage.onToast(OrganizationAddKycDetails.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }

    }
    public void onsuccess(){
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            ToastMessage.onToast(OrganizationAddKycDetails.this, "KYC Updated", ToastMessage.SUCCESS);
            Intent intent = new Intent(this, Organizationwallet_mainpage.class);
            startActivity(intent);
        }
    }

}
