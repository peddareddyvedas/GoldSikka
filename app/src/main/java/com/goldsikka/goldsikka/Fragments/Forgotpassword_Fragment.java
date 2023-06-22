package com.goldsikka.goldsikka.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.goldsikka.goldsikka.LOGIN.ForgotOtp;
import com.goldsikka.goldsikka.LOGIN.LoginActivity;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Forgotpassword_Fragment extends AppCompatActivity implements View.OnClickListener {

    Button btn_emailsubmit;
    EditText et_email;
    TextView tv_email;
    ApiDao apiDao;
    String stemail,rs_email;


    @SuppressLint("NewApi")
    @Nullable
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.forgotpassword_popup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Forgot Password");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        intilizeviews();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent  = new Intent(this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    public void intilizeviews(){
        et_email = findViewById(R.id.et_email);
        tv_email = findViewById(R.id.tv_email);
        btn_emailsubmit = findViewById(R.id.btn_emailsubmit);
        btn_emailsubmit.setOnClickListener(this);

        et_email.setHint(Html.fromHtml(getString(R.string.Email_mobile)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_emailsubmit:
                ititemailvalidation();
                break;
        }
    }

    public void ititemailvalidation(){
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            tv_email.setVisibility(View.GONE);
            btn_emailsubmit.setVisibility(View.GONE);
            emailvalidation();
//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    try {
//
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    Forgotpassword_Fragment.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    });
//                }
//            }, 500);
        }
    }
    public void emailvalidation(){

        tv_email.setVisibility(View.GONE);
        stemail = et_email.getText().toString().trim();
        if (stemail.isEmpty()){
            tv_email.setVisibility(View.VISIBLE);
            tv_email.setText("Please Enter Email");
            btn_emailsubmit.setVisibility(View.VISIBLE);
        }
        else {
            btn_emailsubmit.setVisibility(View.GONE);
            emailapicall();
        }

    }
    public void emailapicall(){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {
            apiDao = ApiClient.getClient("").create(ApiDao.class);
            Call<Listmodel> listmodelCall = apiDao.get_forgot_email(stemail);
            listmodelCall.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        List<Listmodel> list = Collections.singletonList(response.body());
                        for (Listmodel listmodel : list) {
                            String msg = listmodel.getMessage();
//                            ToastMessage.onToast(Forgotpassword_Fragment.this, msg, ToastMessage.SUCCESS);
                            rs_email = listmodel.getEmail();
                            Intent intent = new Intent(Forgotpassword_Fragment.this, ForgotOtp.class);
                            intent.putExtra("rsemail",rs_email);
                            startActivity(intent);
                            btn_emailsubmit.setVisibility(View.VISIBLE);
                        }

                    } else {
                        dialog.dismiss();
                        tv_email.setVisibility(View.GONE);
                        btn_emailsubmit.setVisibility(View.VISIBLE);
                        try {

                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
//                            ToastMessage.onToast(Forgotpassword_Fragment.this, st, ToastMessage.ERROR);
                            //ToastMessage.onToast(this, st, ToastMessage.ERROR);
                            JSONObject er = jObjError.getJSONObject("errors");
                            try {
                                JSONArray array_email = er.getJSONArray("email");
                                for (int i = 0; i < array_email.length(); i++) {
                                    tv_email.setVisibility(View.VISIBLE);
                                    tv_email.setText(array_email.getString(i));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                //    ToastMessage.onToast(Forgotpassword_Fragment.this, "We have some issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }
    }





}
