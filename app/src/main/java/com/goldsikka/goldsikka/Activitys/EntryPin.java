package com.goldsikka.goldsikka.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.goldsikka.goldsikka.Fragments.baseinterface;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ErrorSnackBar;
import com.goldsikka.goldsikka.Utils.ToastMessage;
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

public class EntryPin extends BaseActivity implements baseinterface, View.OnClickListener {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_otp)
    PinView etpin;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.otp_edo1)
    TextView tv_pin;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.forgot_pin)
    TextView tv_forgotpin;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.lllayout)
    LinearLayout lllayout;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tv_enterpin)
    TextView tv_enterpin;


    String stpin,strepin;
    String test;
    String test2;

    ApiDao apiDao;
    String value = "0";
    String accesstoken;

    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_entry_pin;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_forgotpin.setOnClickListener(this);
        tv_enterpin.setText(Html.fromHtml(getString(R.string.enter_pin)));
        accesstoken = AccountUtils.getAccessToken(this);
        Log.e("Pin access token",accesstoken);
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);
        titleTv.setText("Enter Pin");
        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        apiDao = ApiClient.getClient(accesstoken).create(ApiDao.class);
//        Call<Listmodel> call = apiDao.isPin("Bearer "+accesstoken);
//        responsemethod(call);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @OnClick(R.id.btn_submit)
    public void init_validation(View v) {

        stpin = etpin.getText().toString().trim();
        if (stpin.isEmpty()) {
            tv_pin.setVisibility(View.VISIBLE);
            tv_pin.setText("Please Enter PIN");

        } else {
            value = "1";
            Call<Listmodel> call = apiDao.entrypin("Bearer "+AccountUtils.getAccessToken(this), stpin);
            responsemethod(call);

        }
    }

    @Override
    public void responce(@NonNull @NotNull Response<Listmodel> response, int stauscode) {
        Log.e("Status Code Entry PIN", String.valueOf(stauscode));
        if (stauscode == HttpsURLConnection.HTTP_OK||stauscode==HttpsURLConnection.HTTP_ACCEPTED) {
            Listmodel list = response.body();

                Intent intent = new Intent(this, WelcomeActivity.class);
               // intent.putExtra("complete","1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

        } else {
            if (value.equals("1")) {
                assert response.errorBody() != null;
                try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    String st = jObjError.getString("message");
                    ToastMessage.onToast(this, st, ToastMessage.ERROR);
                    JSONObject er = jObjError.getJSONObject("errors");
                    try {
                        JSONArray password = er.getJSONArray("gsPin");
                        for (int i = 0; i < password.length(); i++) {
                            tv_pin.setVisibility(View.VISIBLE);
                            tv_pin.setText(password.getString(i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void listresponce(@NonNull @NotNull Response<List<Listmodel>> response, int stauscode) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgot_pin:
                openpin();
            break;
        }
    }
    public void openpin(){

        Intent intent = new Intent(EntryPin.this,Verifyaccount_Class.class);
        intent.putExtra("from","PIN");
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (backPressed + TIME_DELAY > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
            } else {
                ErrorSnackBar.onBackExit(this, lllayout);
            }
            backPressed = System.currentTimeMillis();
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onBackPressed() {
//        NavUtils.navigateUpFromSameTask(this);
//        super.onBackPressed();
//         finish();
//    }

    private  long backPressed;
    private static final long TIME_DELAY = 2000;
    @Override
    public void onBackPressed() {

        if (backPressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            ErrorSnackBar.onBackExit(this, lllayout);
        }
        backPressed = System.currentTimeMillis();
    }
}