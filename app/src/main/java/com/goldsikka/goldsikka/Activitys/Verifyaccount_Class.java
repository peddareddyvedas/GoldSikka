package com.goldsikka.goldsikka.Activitys;

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
import android.widget.TextView;

import com.goldsikka.goldsikka.LOGIN.EntryPin;
import com.goldsikka.goldsikka.LOGIN.LoginActivity;
import com.goldsikka.goldsikka.LOGIN.OTPActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Verifyaccount_Class extends AppCompatActivity {


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_emailmobile)
    EditText etmobile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_submit)
    Button btn_submit;

//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.loading_gif)
//    GifImageView loading_gif;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_error)
    TextView tv_error;


    ApiDao apiDao;
    String stmobile;
    String from = "otp";

TextView unameTv, uidTv;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyaccount__class);
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            from = bundle.getString("from");
        }
        ButterKnife.bind(this);

        etmobile.setHint(Html.fromHtml(getString(R.string.Phone_Number)));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        setTitle("Goldsikka");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
//        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
//        titleTv.setVisibility(View.VISIBLE);
//        titleTv.setText("Add Nickname");
       // toolbar.setTitleTextColor(getColor(R.color.colorWhite));
       // intilizeviews();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (from.equals("Login")) {

                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else {
                Intent intent = new Intent(this, EntryPin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

   @SuppressLint("NonConstantResourceId")
   @OnClick(R.id.btn_submit)
   public void init_validation(View v){
       tv_error.setVisibility(View.GONE);
       btn_submit.setVisibility(View.GONE);
      // loading_gif.setVisibility(View.VISIBLE);

       validation();
//
//       Timer timer = new Timer();
//       timer.schedule(new TimerTask() {
//           @Override
//           public void run() {
//               try {
//
//                   Thread.sleep(500);
//               } catch (InterruptedException e) {
//                   e.printStackTrace();
//               }
//
//               Verifyaccount_Class.this.runOnUiThread(new Runnable() {
//                   @Override
//                   public void run() {
//
//                       btn_submit.setVisibility(View.VISIBLE);
//                      // loading_gif.setVisibility(View.GONE);
//                   }
//               });
//           }
//       }, 500);
   }

   public void validation(){
       tv_error.setVisibility(View.GONE);
       stmobile = etmobile.getText().toString().trim();

       if (stmobile.isEmpty()){
           tv_error.setVisibility(View.VISIBLE);
           tv_error.setText("Please enter the Mobile Number");
       }
       else {
           get_data();
       }
   }


    public void  get_data(){

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }

        apiDao = ApiClient.getClient("").create(ApiDao.class);

        Call<Listmodel> getverify = apiDao.verify_account(stmobile);

        getverify.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                Listmodel listmodel = response.body();
                if (statuscode == HttpsURLConnection.HTTP_OK||statuscode == HttpsURLConnection.HTTP_CREATED){
                    dialog.dismiss();
                    String verfytoken = listmodel.getVerifyToken();
                    onsuccess(verfytoken);
                }
                else {
                    dialog.dismiss();
                    btn_submit.setVisibility(View.VISIBLE);

                    try {
                        tv_error.setVisibility(View.GONE);
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String st = jObjError.getString("message");
                        ToastMessage.onToast(Verifyaccount_Class.this,st,ToastMessage.ERROR);
                } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
            }
            }
            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                Log.e("verify error",t.toString());
            }
        });
    }
    public void onsuccess(String v){

        if (from.equals("Login")) {

            Intent intent = new Intent(this, OTPActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("credential", "verifyaccount");
            intent.putExtra("accesstoken", v);
            Log.e("AccessToken", v);
            OTPActivity.isFromDeliverye = true;
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, OTPActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("credential", "verifypin");
            intent.putExtra("accesstoken", v);
            Log.e("AccessToken", v);
            OTPActivity.isFromDeliverye = true;
            startActivity(intent);
        }

    }
}