package com.goldsikka.goldsikka.OrganizationWalletModule;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goldsikka.goldsikka.Models.Enquiryformmodel;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
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
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationContactUs extends AppCompatActivity implements View.OnClickListener {

    EditText etname, etemail, etmobile, etcity, etmessage;
    TextView tvname, tvemail, tvmobile, tvmessage;

    Button btsubmit;

    String stname, stemail, stmobile, stcity, stmessage;
    ApiDao apiDao;

    TextView unameTv, uidTv, titleTv;

    RelativeLayout backbtn;

    String oid, oname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_contact_us);
//   loading_gif = findViewById(R.id.loading_gif);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            oid = bundle.getString("oid");
            oname = bundle.getString("oname");
            uidTv.setText(oid);
            unameTv.setText(oname);
        }
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Contact us");
        initlizeviwes();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void initlizeviwes() {

        etname = findViewById(R.id.etname);
        etemail = findViewById(R.id.etemail);
        etmobile = findViewById(R.id.etmobile);
        etmessage = findViewById(R.id.etmessage);
        etcity = findViewById(R.id.etcity);


        etname.setHint(Html.fromHtml(getString(R.string.user_name)));
        etemail.setHint(Html.fromHtml(getString(R.string.user_email)));
        etmobile.setHint(Html.fromHtml(getString(R.string.phone_number)));
        etmessage.setHint(Html.fromHtml(getString(R.string.message)));
        etcity.setHint(Html.fromHtml(getString(R.string.City)));


        tvname = findViewById(R.id.tvname);
        tvemail = findViewById(R.id.tvemail);
        tvmobile = findViewById(R.id.tvmobile);
        tvmessage = findViewById(R.id.tvmessage);

        btsubmit = findViewById(R.id.btsubmit);
        btsubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btsubmit) {
            initvalidation();
        }
    }

    public void initvalidation() {
        btsubmit.setVisibility(View.GONE);
        //loading_gif.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                OrganizationContactUs.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(OrganizationContactUs.this)) {
                            ToastMessage.onToast(OrganizationContactUs.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                        } else {
                            validation();
                            //   loading_gif.setVisibility(View.GONE);
                            btsubmit.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }
        }, 500);

    }

    @SuppressLint("SetTextI18n")
    public void validation() {
        tvemail.setVisibility(View.GONE);
        tvname.setVisibility(View.GONE);
        tvmessage.setVisibility(View.GONE);
        tvmobile.setVisibility(View.GONE);

        stname = etname.getText().toString().trim();
        stemail = etemail.getText().toString().trim();
        stmobile = etmobile.getText().toString().trim();
        stcity = etcity.getText().toString();
        stmessage = etmessage.getText().toString();

        if (stname.isEmpty()) {
            tvname.setVisibility(View.VISIBLE);
            tvname.setText("Please Enter The Name");
        } else if (stemail.isEmpty()) {
            tvemail.setVisibility(View.VISIBLE);
            tvemail.setText("Please Enter the Email Id");
        } else if (stmobile.isEmpty()) {
            tvmobile.setVisibility(View.VISIBLE);
            tvmobile.setText("Please Enter the Phone Number");
        } else if (stmessage.isEmpty()) {
            tvmessage.setVisibility(View.VISIBLE);
            tvmessage.setText("Message Field is Required");
        } else {
            if (!NetworkUtils.isConnected(this)) {
                btsubmit.setVisibility(View.VISIBLE);
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            } else {
                btsubmit.setVisibility(View.GONE);
                initEnquiryForm();
                Log.e("cotactus", "contactus");
            }
        }

    }

    public void initEnquiryForm() {


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

        Call<Enquiryformmodel> contactus = apiDao.contactusenquiryform(stname, stemail, stmobile, stcity, stmessage, "1");
        contactus.enqueue(new Callback<Enquiryformmodel>() {
            @Override
            public void onResponse(Call<Enquiryformmodel> call, Response<Enquiryformmodel> response) {
                int statuscode = response.code();
                if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_ACCEPTED
                        || statuscode == HttpsURLConnection.HTTP_CREATED) {
                    Enquiryformmodel enquiryformmodel = response.body();
                    String msg = enquiryformmodel.getMessage();
                    dialog.dismiss();
                    popup(msg);
                    btsubmit.setVisibility(View.VISIBLE);


                } else {
                    dialog.dismiss();
                    tvemail.setVisibility(View.GONE);
                    tvname.setVisibility(View.GONE);
                    tvmessage.setVisibility(View.GONE);
                    tvmobile.setVisibility(View.GONE);
                    try {
                        assert response.errorBody() != null;
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());

                        String msg = jsonObject.getString("message");
                        ToastMessage.onToast(OrganizationContactUs.this, msg, ToastMessage.ERROR);
                        JSONObject er = jsonObject.getJSONObject("errors");
                        try {
                            JSONArray array_mobile = er.getJSONArray("name");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(activity, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvname.setVisibility(View.VISIBLE);
                                tvname.setText(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray array_mobile = er.getJSONArray("email");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(activity, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvemail.setVisibility(View.VISIBLE);
                                tvemail.setText(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray array_mobile = er.getJSONArray("mobile");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(activity, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvmobile.setVisibility(View.VISIBLE);
                                tvmobile.setText(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONArray array_mobile = er.getJSONArray("description");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(activity, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvmessage.setVisibility(View.VISIBLE);
                                tvmessage.setText(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Enquiryformmodel> call, Throwable t) {
                dialog.dismiss();
              //  ToastMessage.onToast(OrganizationContactUs.this, "We have some issues ", ToastMessage.ERROR);
            }
        });
    }

    AlertDialog enquiryalertdialog;
    Button btcountinue;
    TextView tvinfo;

    public void popup(String msg) {
        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.purchasenquerypopup, null);
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
                Intent intent = new Intent(OrganizationContactUs.this, Organizationwallet_mainpage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        enquiryalertdialog.show();

    }
}