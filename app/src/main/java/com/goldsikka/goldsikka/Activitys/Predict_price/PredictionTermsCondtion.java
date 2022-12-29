package com.goldsikka.goldsikka.Activitys.Predict_price;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.w3c.dom.Text;

import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PredictionTermsCondtion extends AppCompatActivity {

    TextView tv_terms_condition;
    String schemeid;
    ApiDao apiDao;

    TextView unameTv, uidTv, titleTv;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_terms_condtion);

        tv_terms_condition = findViewById(R.id.tv_terms_condition);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Terms&Conditions");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Terms&Conditions");
       // toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        InItTermsAndCondiction();
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
    public void InItTermsAndCondiction(){


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait.....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Listmodel> getfaqs = apiDao.getpredict_termscontent("Bearer "+AccountUtils.getAccessToken(this));
        getfaqs.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();

                Listmodel list = response.body();
                Log.e("codeterms", list.toString());
                dialog.dismiss();
                if (statuscode == HttpsURLConnection.HTTP_OK){
                    dialog.dismiss();
                    tv_terms_condition.setText(list.getTc());



                }
                else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                dialog.dismiss();
                Log.e("predict term fail",t.toString());
              //  ToastMessage.onToast(PredictionTermsCondtion.this,"We have some issues",ToastMessage.ERROR);
            }
        });
    }
}