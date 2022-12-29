package com.goldsikka.goldsikka.Activitys.SpecialModule;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.Models.Enquiryformmodel;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeasonSale extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etname)
    EditText etname;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etemail)
    EditText etemail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etmobile)
    EditText etmobile;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etaddress)
    EditText etaddress;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etmessage)
    EditText etmessage;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvname)
    TextView tvname;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvemail)
    TextView tvemail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvmobile)
    TextView tvmobile;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvmessage)
    TextView tvmessage;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvaddress)
    TextView tvaddress;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvfeatures)
    TextView tvfeatures;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_terms_condition)
    TextView tv_terms_condition;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.loading_gif)
    GifImageView loading_gif;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btsubmit)
    Button btsubmit;

    String stname,stemail,stmobile,stcity,stmessage;
    ApiDao apiDao;

    TextView unameTv, uidTv, titleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season_sale);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Financial Year End Offer ");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Financial Year End Offer");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.tv_terms_condition)
    public void initterms(){
        openwebpage("https://goldsikka.com/season-sale");
    }
    @OnClick(R.id.tvfeatures)
    public void initfeatures(){
        openwebpage("https://goldsikka.com/season-sale");
    }

    public void openwebpage(String link){
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            String uri = String.format(Locale.ENGLISH, link);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btsubmit)
    public void initvalidation(){
        btsubmit .setVisibility(View.GONE);
        loading_gif.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SeasonSale.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(SeasonSale.this)){
                            ToastMessage.onToast(SeasonSale.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            return;
                        }else {
                            validation();
                            loading_gif.setVisibility(View.GONE);
                            btsubmit.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }
        }, 500);

    }

    public void validation(){
        tvemail.setVisibility(View.GONE);
        tvname.setVisibility(View.GONE);
        tvmessage.setVisibility(View.GONE);
        tvmobile.setVisibility(View.GONE);
        tvaddress.setVisibility(View.GONE);

        stname = etname.getText().toString().trim();
        stemail = etemail.getText().toString().trim();
        stmobile = etmobile.getText().toString().trim();
        stcity = etaddress.getText().toString();
        stmessage = etmessage.getText().toString();

        if (stname.isEmpty()){
            tvname.setVisibility(View.VISIBLE);
            tvname.setText("Please Enter The Name");
        }else if (stemail.isEmpty()){
            tvemail.setVisibility(View.VISIBLE);
            tvemail.setText("Please Enter The Email Id");
        }
        else if (stmobile.isEmpty()){
            tvmobile.setVisibility(View.VISIBLE);
            tvmobile.setText("Please Enter the Phone Number");
        }
        else if (stcity.isEmpty()){
            tvaddress.setVisibility(View.VISIBLE);
            tvaddress.setText("Please Enter the Address");
        }
        else if (stmessage.isEmpty()){
            tvmessage.setVisibility(View.VISIBLE);
            tvmessage.setText("Message Field is Required");
        }
        else {
            if (!NetworkUtils.isConnected(SeasonSale.this)){
                btsubmit.setVisibility(View.VISIBLE);
                ToastMessage.onToast(SeasonSale.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                return;
            }else {
                btsubmit.setVisibility(View.GONE);
                initEnquiryForm();
            }
        }

    }

    public void initEnquiryForm(){


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

        Call<Enquiryformmodel> contactus = apiDao.womensplapi(stname,stemail,stmobile,stcity,stmessage,"4");
        contactus.enqueue(new Callback<Enquiryformmodel>() {
            @Override
            public void onResponse(Call<Enquiryformmodel> call, Response<Enquiryformmodel> response) {
                int statuscode  = response.code();
                if (statuscode == HttpsURLConnection.HTTP_OK||statuscode == HttpsURLConnection.HTTP_ACCEPTED
                        ||statuscode == HttpsURLConnection.HTTP_CREATED){
                    Enquiryformmodel enquiryformmodel = response.body();
                    String msg =  enquiryformmodel.getMessage();
                    dialog.dismiss();
                    popup(msg);



                }
                else {
                    dialog.dismiss();
                    tvemail.setVisibility(View.GONE);
                    tvname.setVisibility(View.GONE);
                    tvmessage.setVisibility(View.GONE);
                    tvmobile.setVisibility(View.GONE);
                    tvaddress.setVisibility(View.GONE);
                    try {
                        assert response.errorBody() != null;
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());

                        String msg = jsonObject.getString("message");
                        ToastMessage.onToast(SeasonSale.this,msg,ToastMessage.ERROR);
                        JSONObject er = jsonObject.getJSONObject("errors");
                        try {
                            JSONArray array_mobile = er.getJSONArray("name");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvname.setVisibility(View.VISIBLE);
                                tvname.setText(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray array_mobile = er.getJSONArray("email");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvemail.setVisibility(View.VISIBLE);
                                tvemail.setText(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray array_mobile = er.getJSONArray("mobile");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvmobile.setVisibility(View.VISIBLE);
                                tvmobile.setText(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray array_mobile = er.getJSONArray("address");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvaddress.setVisibility(View.VISIBLE);
                                tvaddress.setText(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONArray array_mobile = er.getJSONArray("description");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvmessage.setVisibility(View.VISIBLE);
                                tvmessage.setText(array_mobile.getString(i));
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

            @Override
            public void onFailure(Call<Enquiryformmodel> call, Throwable t) {
                dialog.dismiss();
               // ToastMessage.onToast(SeasonSale.this, "We have some issues ",ToastMessage.ERROR);
            }
        });
    }
    AlertDialog enquiryalertdialog;
    Button btcountinue;
    TextView tvinfo;
    public void popup(String msg){
        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.purchasenquerypopup,null);
        alertdilog.setCancelable(false);
        alertdilog.setView(dialogview);
        enquiryalertdialog = alertdilog.create();

        tvinfo = dialogview.findViewById(R.id.tvinfo);
        tvinfo.setText(msg);

        btcountinue = dialogview.findViewById(R.id.btcountinue);
        btcountinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enquiryalertdialog.dismiss();
                Intent intent = new Intent(SeasonSale.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        enquiryalertdialog.show();

    }
}