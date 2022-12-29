package com.goldsikka.goldsikka.Activitys.Predict_price;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.goldsikka.goldsikka.Fragments.Predictions.prediction_TermsConditions;
import com.goldsikka.goldsikka.Fragments.Sell_Fragment;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.List_Model;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class predictprice_activity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_liveprice,tv_showerror,tv_terms_condition,tv_price_timeing,tvsubmission,tvpredictprice;
    EditText et_pridectprice;
    Button bt_continue;
    CheckBox check_terms_condition;
        LinearLayout ll_success,ll_addprice;
    String st_predictprice,rs_predictprice,st_data,liveprice;
        ImageView iv_close,iv_liveprice;
    ApiDao apiDao;
    AlertDialog alertDialogdialog;

    ImageView imageView_close;
    TextView tv_msg;

    TextView unameTv, uidTv, titleTv;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictprice_activity);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Predict");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Predict");
      //  toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        intilizeview();
        getliveprices();
        init_pricetimeings();


    }

    @Override
    protected void onStart() {
        super.onStart();
        getliveprices();
    }

    public void getliveprices(){
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getliverates = apiDao.getlive_rates("Bearer " + AccountUtils.getAccessToken(this));
            getliverates.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    List<Listmodel> list = Collections.singletonList(response.body());
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {

                        if (list != null) {
                            for (Listmodel listmodel : list) {


//                            st_buyprice =
//                            st_sellprice = listmodel.getSell_price_per_gram();
//                            st_goldtype = listmodel.getGold_type();
//
//                            String purity = listmodel.getGold_purity();


                                liveprice = listmodel.getSell_price_per_gram();
                                tv_liveprice.setText("Live Rate : " + getString(R.string.Rs) + liveprice);
                            }
                        } else {
//                        tv_buyprice.setText(getString(R.string.Rs)+"0.000");
//                        tv_sellprice.setText(getString(R.string.Rs)+"0.000");
                        }
                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            JSONObject er = jObjError.getJSONObject("errors");
                            Toast.makeText(predictprice_activity.this, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                   // ToastMessage.onToast(predictprice_activity.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


        public void intilizeview(){

            tv_price_timeing = findViewById(R.id.tv_price_timeing);
            tvsubmission = findViewById(R.id.tvsubmission);
            tvpredictprice = findViewById(R.id.tvpredictprice);

        ll_success = findViewById(R.id.ll_success);
            ll_addprice = findViewById(R.id.ll_addprice);
            iv_close = findViewById(R.id.iv_close);
            iv_close.setOnClickListener(this);

        tv_terms_condition = findViewById(R.id.tv_terms_condition);
        tv_terms_condition.setOnClickListener(this);

        tv_liveprice = findViewById(R.id.tv_liveprice);
        tv_showerror = findViewById(R.id.tv_showerror);
        et_pridectprice = findViewById(R.id.et_pridectprice);

            iv_liveprice = findViewById(R.id.iv_liveprice);
            iv_liveprice.setOnClickListener(this);

        check_terms_condition = findViewById(R.id.check_terms_condition);
        bt_continue = findViewById(R.id.btn_submit);
        bt_continue.setOnClickListener(this);

    }

    public void init_pricetimeings(){

        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<Listmodel> predict_timeings = apiDao.getpredict_timeings("Bearer " + AccountUtils.getAccessToken(this));
            predict_timeings.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Listmodel listmodel = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        String data = listmodel.getData();
                        tv_price_timeing.setText(data);
                        //InItPriceTimeings(data);

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
              //      ToastMessage.onToast(predictprice_activity.this, "we have some issues", ToastMessage.ERROR);
                }
            });
        }


    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_submit:
              validation();
                break;
            case R.id.tv_terms_condition:
                init_tv_terms_condition();
                break;
            case R.id.iv_close:
                open_sucess();
                break;
            case R.id.iv_liveprice:
                Log.e("iv_click","ivclick");
               initrefresh_click();
                break;
        }
    }
    public void initrefresh_click(){
        tv_liveprice.setVisibility(View.GONE);
        ProgressDialog  dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait..");
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!NetworkUtils.isConnected(predictprice_activity.this)){
                    dialog.dismiss();
                    ToastMessage.onToast(predictprice_activity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                }else {
                    getliveprices();

                    tv_liveprice.setVisibility(View.VISIBLE);
                    tv_liveprice.setText("Live Rate : " + getString(R.string.Rs) + liveprice);
                    dialog.dismiss();
                }
            }
        },1500);

    }
    public void init_tv_terms_condition(){

        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            Intent intent = new Intent(this, PredictionTermsCondtion.class);
            startActivity(intent);
        }
//        prediction_TermsConditions fragment = new prediction_TermsConditions();
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commit();
    }
    public void validation(){
        tv_showerror.setVisibility(View.GONE);
        st_predictprice = et_pridectprice.getText().toString().trim();
        if (!check_terms_condition.isChecked()){
            tv_showerror.setVisibility(View.VISIBLE);
            tv_showerror.setText("Please accept the Terms & Conditions.");
        }
      else  if (st_predictprice.isEmpty()){
            tv_showerror.setVisibility(View.VISIBLE);
            tv_showerror.setText("Please enter the Price.");
        }
        else {
            init_pricesubmit();
        }
    }
    public void init_pricesubmit(){

        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setMessage("Please Wait..");
            dialog.show();
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<List_Model> pricesubmit = apiDao.predict_pricesubmit("Bearer " + AccountUtils.getAccessToken(this), st_predictprice);
            pricesubmit.enqueue(new Callback<List_Model>() {
                @Override
                public void onResponse(Call<List_Model> call, Response<List_Model> response) {
                    int statuscode = response.code();
                    List_Model listmodel = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();

                        ll_success.setVisibility(View.VISIBLE);
                        ll_addprice.setVisibility(View.GONE);
                        String msg = listmodel.getData();
                        tvsubmission.setText(msg);
                        st_data = listmodel.getData();
                        tv_price_timeing.setText(st_data);

                        JsonObject object = new JsonParser().parse(listmodel.getPrice().toString()).getAsJsonObject();
                        try {
                            JSONObject json_from = new JSONObject(object.toString());
                            rs_predictprice = json_from.getString("price_predicted");
                            tvpredictprice.setText("Your Predicted Price : " + getString(R.string.Rs) + rs_predictprice);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        onsuccess();
                        //    ToastMessage.onToast(predictprice_activity.this,"Submited.. ",ToastMessage.SUCCESS);

                    } else {
                        dialog.dismiss();
                        ll_success.setVisibility(View.GONE);
                        ll_addprice.setVisibility(View.VISIBLE);
                        assert response.errorBody() != null;

                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("data");
                            //ToastMessage.onToast(predictprice_activity.this,st,ToastMessage.INFO);
                            initpopup(st);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<List_Model> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("failure pricepredict", t.toString());
                }
            });
        }
    }
    public void onsuccess(){

        logGoldDhamakaEvent();
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
    }

    public void logGoldDhamakaEvent () {
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Gold Dhamaka");
    }
    public  void  initpopup(String msg){

        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.popup_message_screen,null);
        alertdilog.setCancelable(false);
        alertdilog.setView(dialogview);
        alertDialogdialog = alertdilog.create();


        imageView_close = dialogview.findViewById(R.id.iv_close);

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogdialog.dismiss();
            }
        });

        tv_msg = dialogview.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        alertDialogdialog.show();



    }
    public void open_sucess(){
        ll_success.setVisibility(View.GONE);
        ll_addprice.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }
}