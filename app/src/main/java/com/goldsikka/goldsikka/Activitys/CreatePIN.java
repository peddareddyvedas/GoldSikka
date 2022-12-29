package com.goldsikka.goldsikka.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.goldsikka.goldsikka.Fragments.NewDesignsFragments.HomeFragment;
import com.goldsikka.goldsikka.Fragments.baseinterface;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.WelcomeActivity;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class CreatePIN extends BaseActivity implements baseinterface {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_otp)
    PinView etpin;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_re_otp)
    PinView etrepin;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_com_pin)
    PinView et_com_pin;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tv_re_text)
    TextView tv_re_text;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tv_error1)
    TextView tv_error1;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tv_error2)
    TextView tv_error2;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tv_error3)
    TextView tv_error3;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tv_heading)
    TextView tv_heading;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tv_ce_text)
    TextView tv_ce_text;


    int complete = 0;

    String stpin, strepin, stcpin;
    String accesstoken;
    String token, from;
    private shared_preference sharedPreference;
    String value = "0";
    ApiDao apiDao;
    boolean ispin;

    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_pin;
    }

    @Override
    protected void initView() {

        ButterKnife.bind(this);
        setHint();

        sharedPreference = new shared_preference(this);
        // loading_gif = findViewById(R.id.loading_gif);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            accesstoken = bundle.getString("accesstoken");
            from = bundle.getString("from");
            token = bundle.getString("token");
            ispin = bundle.getBoolean("isPin");

        }

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (ispin) {

//            tv_re_text.setText("Enter new 4 digit PIN");
            tv_re_text.setText(Html.fromHtml(getString(R.string.enter_new_pin)));
            tv_re_text.setVisibility(View.VISIBLE);
            et_com_pin.setVisibility(View.VISIBLE);
            tv_ce_text.setVisibility(View.VISIBLE);

//            tv_ce_text.setText("Please reenter PIN");
            tv_ce_text.setText(Html.fromHtml(getString(R.string.reenter_pin)));


//            tv_heading.setText("Enter your old pin");
            tv_heading.setText(Html.fromHtml(getString(R.string.enter_old_pin)));
//            setTitle("Update PIN");
            titleTv.setText("Update PIN");

            // tv_heading.setText("Enter your old pin");

        } else {
//            setTitle("Create PIN");
            titleTv.setText("Create PIN");

        }

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

    }

    public void setHint() {
        tv_heading.setText(Html.fromHtml(getString(R.string.enter_pin)));
        tv_re_text.setText(Html.fromHtml(getString(R.string.reenter_pin)));
        // tv_ce_text.setText(Html.fromHtml(getString(R.string.reenter_pin)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @OnClick(R.id.btn_submit)
    public void init_validation(View v) {

        Log.e("Complete PIN", String.valueOf(complete));

        //tv_pin.setVisibility(View.GONE);
        stpin = etpin.getText().toString().trim();
        strepin = etrepin.getText().toString().trim();
        stcpin = et_com_pin.getText().toString().trim();

        if (!ispin) {

            //  if (complete == 0) {
            if (stpin.isEmpty()) {
                tv_error1.setVisibility(View.VISIBLE);
                tv_error1.setText("Please Enter PIN");
            } else if (strepin.isEmpty()) {
                tv_error1.setVisibility(View.GONE);
                tv_error2.setVisibility(View.VISIBLE);
                tv_error2.setText("Please reenter PIN");
            } else if (!stpin.equals(strepin)) {

                ToastMessage.onToast(this, "Pin must match", ToastMessage.ERROR);

            } else {

                tv_error2.setVisibility(View.GONE);

                if (from.equals("forgotpin")) {
                    value = "1";
                    Call<Listmodel> call = apiDao.forgotpin(token, stpin, strepin);
                    responsemethod(call);
                } else {
                    Call<Listmodel> call = apiDao.savepin("Bearer " + AccountUtils.getAccessToken(this), stpin, strepin);
                    responsemethod(call);
                }
                //  }
            }
        } else {


            if (stpin.isEmpty()) {
                tv_error1.setVisibility(View.VISIBLE);
                tv_error1.setText("Please enter old PIN");
            } else if (strepin.isEmpty()) {
                tv_error1.setVisibility(View.GONE);
                tv_error2.setVisibility(View.VISIBLE);
                tv_error2.setText("Please enter new PIN");
            } else if (stcpin.isEmpty()) {
                tv_error2.setVisibility(View.GONE);
                tv_error3.setVisibility(View.VISIBLE);
                tv_error3.setText("Please reenter PIN");
            } else if (!strepin.equals(stcpin)) {
                tv_error3.setVisibility(View.GONE);
                ToastMessage.onToast(this, "Pin must match", ToastMessage.ERROR);
            } else {
                value = "2";
                Call<Listmodel> call = apiDao.updatepin("Bearer " + AccountUtils.getAccessToken(this), stpin, strepin, stcpin);
                responsemethod(call);
            }
        }
    }

//        else if (complete == 1){
//            etrepin.setVisibility(View.VISIBLE);
//
//            if (ispin) {
//                complete = 2;
//                tv_re_text.setText("Please enter new 4 digit PIN");
//                tv_re_text.setVisibility(View.VISIBLE);
//
//                et_com_pin.setVisibility(View.VISIBLE);
//                tv_ce_text.setVisibility(View.VISIBLE);
//                tv_ce_text.setText("Please reenter PIN");
//                // strepin = etrepin.getText().toString().trim();
//
//                if (stcpin.isEmpty()){
//                    ToastMessage.onToast(this,"Please reenter PIN",ToastMessage.INFO);
//                }else {
//                    value = "2";
//                    Call<Listmodel> call = apiDao.updatepin("Bearer " + AccountUtils.getAccessToken(this), stpin, strepin,stcpin);
//                    responsemethod(call);
//                }
//            }else {
//                tv_re_text.setVisibility(View.VISIBLE);
//
//            }
//
//            if (strepin.isEmpty()) {
//                tv_pin.setVisibility(View.VISIBLE);
//                tv_pin.setText("Please reenter PIN");
//
////                etotp.setError("Please Enter OTP");
//            }else if (!stpin.equals(strepin)){
//                ToastMessage.onToast(this,"Pin must match",ToastMessage.INFO);
//            } else {
//
//                if (from.equals("forgotpin")) {
//                    value = "1";
//                    Call<Listmodel> call = apiDao.forgotpin(token, stpin, strepin);
//                    responsemethod(call);
//                } else {
//                    Call<Listmodel> call = apiDao.savepin("Bearer " + AccountUtils.getAccessToken(this), stpin, strepin);
//                    responsemethod(call);
//                }
//
//            }
//        }
//
//        if (complete == 0) {
//
//            if (stpin.isEmpty()) {
//                tv_pin.setVisibility(View.VISIBLE);
//                tv_pin.setText("Please Enter PIN");
////                etotp.setError("Please Enter OTP");
////            }else if (complete == 1){
////
////                et_com_pin.setVisibility(View.VISIBLE);
////                tv_ce_text.setVisibility(View.VISIBLE);
////                tv_ce_text.setText("Please ReEnter PIN");
//
//            } else {
//                complete = 1;
//
////                et_com_pin.setVisibility(View.VISIBLE);
////                tv_ce_text.setVisibility(View.VISIBLE);
////                tv_ce_text.setText("Please ReEnter PIN");
//
//                tv_pin.setVisibility(View.GONE);
//                etrepin.setVisibility(View.VISIBLE);
//                if (ispin) {
//                    tv_re_text.setText("Please enter new 4 digit PIN");
//                    tv_re_text.setVisibility(View.VISIBLE);
//                }else {
//
//                    tv_re_text.setVisibility(View.VISIBLE);
//
//                }
//
//
//                if (strepin.isEmpty()) {
//                    tv_pin.setVisibility(View.VISIBLE);
//                    tv_pin.setText("Please reenter PIN");
//
////                etotp.setError("Please Enter OTP");
//                }else if (!stpin.equals(strepin)){
//                    ToastMessage.onToast(this,"Pin must match",ToastMessage.INFO);
//                } else {
//
//                    if (from.equals("forgotpin")) {
//                        value = "1";
//                        Call<Listmodel> call = apiDao.forgotpin(token, stpin, strepin);
//                        responsemethod(call);
//                    } else {
//                        Call<Listmodel> call = apiDao.savepin("Bearer " + AccountUtils.getAccessToken(this), stpin, strepin);
//                        responsemethod(call);
//                    }
//
//                }
//
//            }
//
//        }else if (complete == 1){
//            et_com_pin.setVisibility(View.VISIBLE);
//                tv_ce_text.setVisibility(View.VISIBLE);
//                tv_ce_text.setText("Please reenter PIN");
//           // strepin = etrepin.getText().toString().trim();
//
//                if (stcpin.isEmpty()){
//                    ToastMessage.onToast(this,"Please reenter PIN",ToastMessage.INFO);
//                }else {
//                    value = "2";
//                    Call<Listmodel> call = apiDao.updatepin("Bearer " + AccountUtils.getAccessToken(this), stpin, strepin,stcpin);
//                    responsemethod(call);
//                }
//        }


    @Override
    public void responce(@NonNull @NotNull Response<Listmodel> response, int stauscode) {
        Log.e("Status Code PIN", String.valueOf(stauscode));
        if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_ACCEPTED || stauscode == HttpsURLConnection.HTTP_OK) {
            List<Listmodel> list = Collections.singletonList(response.body());
            if (value.equals("0")) {
                ToastMessage.onToast(this, "Pin Created Sucessfully", ToastMessage.SUCCESS);
                sharedPreference.writepinstatus(true);
                // complete = 1;
                onBackPressed();
            } else if (value.equals("2")) {
                ToastMessage.onToast(this, "Pin Updated Sucessfully", ToastMessage.SUCCESS);
                sharedPreference.writepinstatus(true);
                onBackPressed();
            } else {
                ToastMessage.onToast(this, "Pin Created Sucessfully", ToastMessage.SUCCESS);
                sharedPreference.writepinstatus(true);
                Intent intent = new Intent(this, EntryPin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        } else {
            JSONObject jObjError = null;
            try {
                String st = null;

                jObjError = new JSONObject(response.errorBody().string());
                st = jObjError.getString("message");
                ToastMessage.onToast(this, st, ToastMessage.ERROR);
                JSONObject er = jObjError.getJSONObject("errors");
                try {
                    JSONArray password = er.getJSONArray("current_gs_pin");
                    for (int i = 0; i < password.length(); i++) {
                        tv_error1.setVisibility(View.VISIBLE);
                        tv_error1.setText(password.getString(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray password = er.getJSONArray("new_gs_pin");
                    for (int i = 0; i < password.length(); i++) {
                        tv_error2.setVisibility(View.VISIBLE);
                        tv_error2.setText(password.getString(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray password = er.getJSONArray("confirm_gs_pin");
                    for (int i = 0; i < password.length(); i++) {
                        tv_error3.setVisibility(View.VISIBLE);
                        tv_error3.setText(password.getString(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("Responce PIN 422", new Gson().toJson(response.errorBody()));
        }
    }

    @Override
    public void listresponce(@NonNull @NotNull Response<List<Listmodel>> response, int stauscode) {

    }
}