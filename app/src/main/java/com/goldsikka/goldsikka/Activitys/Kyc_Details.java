package com.goldsikka.goldsikka.Activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.Fragments.Get_kyc_details_fragment;
import com.goldsikka.goldsikka.MainActivity;
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
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kyc_Details extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_fathername)
    EditText et_fathername;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_spousename)
    EditText et_spousename;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_aadhaar)
    EditText et_aadhaar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_pannumber)
    EditText et_pannumber;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.alternate_phone)
    EditText et_alternate_phone;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fathername)
    TextView tv_fathername;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_spousename)
    TextView tv_spousename;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_aadhaar)
    TextView tv_aadhaar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_pannumber)
    TextView tv_pannumber;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_alternate_phone)
    TextView tv_alternate_phone;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_gender)
    TextView tv_gender;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rb_male)
    RadioButton rb_male;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rb_female)
    RadioButton rb_female;
    // GifImageView loading_gif;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_kycsubmit)
    Button btn_kycsubmit;


    RadioGroup rg_group;
    ApiDao apiDao;
    RadioButton radioButton;
    String st_fathername, st_spousename, st_aadhaar, st_pannumber, st_alternate_phone;
    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;
    String  st_gender="";
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kys__details);
        Bundle bundle = getIntent().getExtras();
        //   String st_fathername, st_spousename, st_aadhaar, st_pannumber, st_alternate_phone, st_gender;

        if (bundle != null) {
            st_fathername = bundle.getString("st_fathername");
            st_spousename = bundle.getString("st_spousename");
            st_aadhaar = bundle.getString("st_aadhaar");
            st_pannumber = bundle.getString("st_pannumber");
            st_alternate_phone = bundle.getString("st_alternate_phone");
            st_gender = bundle.getString("st_gender");

            //  ToastMessage.onToast(Kyc_Details.this, st_gender, ToastMessage.ERROR);

        }


        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ButterKnife.bind(this);

        //   loading_gif = findViewById(R.id.loading_gif);
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("KYC");
        intillizeview();
        setHint();
    }

    public void setHint() {
        et_fathername.setHint(Html.fromHtml(getString(R.string.Father_Name)));
        et_spousename.setHint(Html.fromHtml(getString(R.string.Spouse_Name)));
        et_aadhaar.setHint(Html.fromHtml(getString(R.string.Aadhar_Number)));
        et_pannumber.setHint(Html.fromHtml(getString(R.string.Pan_Number)));
        et_alternate_phone.setHint(Html.fromHtml(getString(R.string.Althernate_Phone)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    private void intillizeview() {
        rg_group = findViewById(R.id.rg_group);
        et_fathername.setText(st_fathername);
        et_spousename.setText(st_spousename);
        // et_aadhaar.setText(st_aadhaar);
        //     et_pannumber.setText(st_pannumber);
        et_alternate_phone.setText(st_alternate_phone);
        int selectedId = rg_group.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        Log.e("gender", "" + selectedId);
        Log.e("gender", "" + st_gender);
       /* if (st_gender.equals("")) {
            rb_male.setChecked(true);

        } else {

        }*/
        if (st_gender.contains("Male")) {
            rb_male.setChecked(true);
            rb_female.setChecked(false);
        } else if (st_gender.contains("Female")) {
            rb_male.setChecked(false);
            rb_female.setChecked(true);
        }
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @OnClick(R.id.btn_kycsubmit)
    public void init_validation(View v) {
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

                Kyc_Details.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(Kyc_Details.this)) {
                            btn_kycsubmit.setVisibility(View.VISIBLE);
                            // loading_gif.setVisibility(View.GONE);
                            ToastMessage.onToast(Kyc_Details.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            return;
                        } else {
                            validation(v);
                            btn_kycsubmit.setVisibility(View.VISIBLE);
                            //    loading_gif.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }, 500);
    }

    public void validation(View view) {
        tv_aadhaar.setVisibility(View.GONE);
        tv_alternate_phone.setVisibility(View.GONE);
        tv_fathername.setVisibility(View.GONE);
        tv_spousename.setVisibility(View.GONE);
        tv_pannumber.setVisibility(View.GONE);
        int selectedId = rg_group.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        if (rb_male.isChecked()) {
            st_gender = "M";
        } else if (rb_female.isChecked()) {
            st_gender = "F";
        }
        st_fathername = et_fathername.getText().toString().trim();
        st_spousename = et_spousename.getText().toString().trim();
        st_aadhaar = et_aadhaar.getText().toString().trim();
        st_pannumber = et_pannumber.getText().toString().trim();
        st_alternate_phone = et_alternate_phone.getText().toString().trim();


        if (rg_group.getCheckedRadioButtonId() == -1) {
            ToastMessage.onToast(this, "Please select Gender", ToastMessage.ERROR);
        } else if (st_aadhaar.isEmpty()) {
            tv_aadhaar.setVisibility(View.VISIBLE);
            tv_aadhaar.setText("please Enter Aadhaar number");
        } else if (st_pannumber.isEmpty()) {
            tv_pannumber.setVisibility(View.VISIBLE);
            tv_pannumber.setText("Please Enter PAN Number");

        } else if (st_pannumber.length() < 10) {
            tv_pannumber.setVisibility(View.VISIBLE);

            st_pannumber = et_pannumber.getText().toString().trim();

            Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

            Matcher matcher = pattern.matcher(st_pannumber);

            if (matcher.matches()) {
                Log.e("editpanno", "" + st_pannumber);
                tv_pannumber.setText(st_pannumber + " is Matching");
                Toast.makeText(getApplicationContext(), st_pannumber + " is Matching", Toast.LENGTH_LONG).show();
            } else {
                tv_pannumber.setText("plz enter your correct pan num");

                Toast.makeText(getApplicationContext(), st_pannumber + " is Not Matching", Toast.LENGTH_LONG).show();
            }


        } else if (st_alternate_phone.isEmpty()) {
            tv_alternate_phone.setVisibility(View.VISIBLE);
            tv_alternate_phone.setText("Please Enter Alternate Phone Number");
        } else {
            if (!NetworkUtils.isConnected(this)) {
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                return;
            } else {
                update_kycdetails();
            }
        }

    }


    public void update_kycdetails() {

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
//                            ToastMessage.onToast(Kyc_Details.this, st, ToastMessage.ERROR);
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
                    ToastMessage.onToast(Kyc_Details.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

    public void onsuccess() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            ToastMessage.onToast(Kyc_Details.this, "KYC Updated", ToastMessage.SUCCESS);
            finish();
           /* Intent intent = new Intent(this, Get_kyc_details_fragment.class);
            startActivity(intent);*/
        }
    }

}