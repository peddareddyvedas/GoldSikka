package com.goldsikka.goldsikka.Fragments.Schemes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

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

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchemeTicket extends AppCompatActivity implements View.OnClickListener {

    EditText etname,etemail,etmobile,etmessage;
    TextView tvname,tvemail,tvmobile,tvmessage;

    Button btsubmit;

    String stname,stemail,stmobile,stid,stmessage;
    ApiDao apiDao;

    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_ticket);
        intilizeviews();
        setHint();

        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            stid = bundle.getString("id");
        }

        backbtn = findViewById(R.id.backbtn);
//        setTitle("Ticket");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Ticket");

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void setHint(){
        etname.setHint(Html.fromHtml(getString(R.string.Name)));
        etemail.setHint(Html.fromHtml(getString(R.string.Email)));
        etmessage.setHint(Html.fromHtml(getString(R.string.message)));
        etmobile.setHint(Html.fromHtml(getString(R.string.Phone_Number)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }
    public void intilizeviews(){

        etname =findViewById(R.id.etname);
        etemail = findViewById(R.id.etemail);
        etmobile = findViewById(R.id.etmobile);
        etmessage = findViewById(R.id.etmessage);

        tvname = findViewById(R.id.tvname);
        tvemail = findViewById(R.id.tvemail);
        tvmobile = findViewById(R.id.tvmobile);
        tvmessage = findViewById(R.id.tvmessage);

        btsubmit = findViewById(R.id.btsubmit);
        btsubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btsubmit:
                validation();
                break;
        }
    }
    public void validation(){
        tvemail.setVisibility(View.GONE);
        tvname.setVisibility(View.GONE);
        tvmessage.setVisibility(View.GONE);
        tvmobile.setVisibility(View.GONE);

        stname = etname.getText().toString().trim();
        stemail = etemail.getText().toString().trim();
        stmobile = etmobile.getText().toString().trim();
        stmessage = etmessage.getText().toString();

        if (stname.isEmpty()){
            tvname.setVisibility(View.VISIBLE);
            tvname.setText("Please enter the name");
        }else if (stemail.isEmpty()){
            tvemail.setVisibility(View.VISIBLE);
            tvemail.setText("Please Enter the Email Id");
        }else if (stmobile.isEmpty()){
            tvmobile.setVisibility(View.VISIBLE);
            tvmobile.setText("Please Enter the Phone Number");
        }else if (stmessage.isEmpty()){
            tvmessage.setVisibility(View.VISIBLE);
            tvmessage.setText("Message Field is Required");
        }else {
            if (!NetworkUtils.isConnected(this)){
                btsubmit.setVisibility(View.VISIBLE);
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            }else {
                btsubmit.setVisibility(View.GONE);
                initticketsubmit();

            }
        }
    }
    public void initticketsubmit(){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

        Call<Enquiryformmodel> contactus = apiDao.RiseTicket("Bearer "+AccountUtils.getAccessToken(this),stname,stemail,stmobile,stmessage,stid);
        contactus.enqueue(new Callback<Enquiryformmodel>() {
            @Override
            public void onResponse(Call<Enquiryformmodel> call, Response<Enquiryformmodel> response) {
                int statuscode  = response.code();
                if (statuscode == HttpsURLConnection.HTTP_OK||statuscode == HttpsURLConnection.HTTP_ACCEPTED
                        ||statuscode == HttpsURLConnection.HTTP_CREATED){
                    Enquiryformmodel enquiryformmodel = response.body();
                    String msg = enquiryformmodel.getMessage();
                    dialog.dismiss();
                    popup(msg);
                    btsubmit.setVisibility(View.VISIBLE);


                }
                else {
                    dialog.dismiss();
                    tvemail.setVisibility(View.GONE);
                    tvname.setVisibility(View.GONE);
                    tvmessage.setVisibility(View.GONE);
                    tvmobile.setVisibility(View.GONE);
                    try {
                        assert response.errorBody() != null;
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());

                        String msg = jsonObject.getString("message");
//                        ToastMessage.onToast(SchemeTicket.this,msg,ToastMessage.ERROR);
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
                            JSONArray array_mobile = er.getJSONArray("message");
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
                //  ToastMessage.onToast(SchemeTicket.this, "We have some issues ",ToastMessage.ERROR);
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
                Intent intent = new Intent(SchemeTicket.this, MainFragmentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        enquiryalertdialog.show();

    }
}