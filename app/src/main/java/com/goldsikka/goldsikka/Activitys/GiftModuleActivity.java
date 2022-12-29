 package com.goldsikka.goldsikka.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.Fragments.Successpopup;
import com.goldsikka.goldsikka.Fragments.TransferGold;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiftModuleActivity extends AppCompatActivity  implements View.OnClickListener {

    ApiDao apiDao;
    EditText etgrms;
    TextView tvfrndno, tv_second_title, tvwalletgold;
    String Stnumber;
    CountDownTimer countDownTimer;
    private boolean timerRunning;
    String stgrms, stno, amountvalue, finalamount;
    String liveprice, sttransactionid, stdescription;
    TextWatcher textWatcher;
    LinearLayout llmoney;
    double grams;
    Button send;
    GifImageView loading_gif;
    TextView giftamount;
    TextView tv_grams, tv_amount,tv_wallet_gold;

    TextView unameTv, uidTv, titleTv;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_module);


        tv_second_title = findViewById(R.id.tv_second_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Gift");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Gift");
        //toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getliveprices();
        initviews();
        init_timer();
        getdata();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Stnumber = bundle.getString("phone_number");
            tvfrndno.setText(Stnumber);
        }

    }
//    @Override
//    protected void onPause() {
//
//        countDownTimer.cancel();
//        timerRunning = false;
//        super.onPause();
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //finish();
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

public void initviews() {

    etgrms = findViewById(R.id.gift_grms);
    tvfrndno = findViewById(R.id.tv_number);
    tv_second_title = findViewById(R.id.tv_second_title);
    tv_wallet_gold = findViewById(R.id.tv_wallet_gold);
    llmoney = findViewById(R.id.llmoney);
    llmoney.setVisibility(View.GONE);
    loading_gif = findViewById(R.id.loading_gif);
    giftamount = findViewById(R.id.giftamount);
    tv_amount = findViewById(R.id.tv_amount);
    tv_grams = findViewById(R.id.tv_grams);

    textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !s.toString().equalsIgnoreCase("")) {
                if (etgrms.getText().hashCode() == s.hashCode()) {
                    try {
                        amountvalue = etgrms.getText().toString().trim();
                        llmoney.setVisibility(View.VISIBLE);

                        double goldgrams = Double.parseDouble(amountvalue);
                        grams = Double.parseDouble(liveprice) * goldgrams;
                        BigDecimal b21 = BigDecimal.valueOf(grams).setScale(2, RoundingMode.HALF_EVEN);
                        finalamount = new String(String.valueOf(b21));
                        giftamount.setText("Money : " + getApplicationContext().getResources().getString(R.string.Rs) + " " + finalamount);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                giftamount.setText(null);
            }
        }
    };
    etgrms.addTextChangedListener(textWatcher);

//        btn_contacts = findViewById(R.id.btn_contacts);
//        btn_contacts.setOnClickListener(this);
    send = findViewById(R.id.gift_send);
    send.setOnClickListener(this);

}

    public void validation() {
        tv_amount.setVisibility(View.GONE);
        tv_grams.setVisibility(View.GONE);
        llmoney.setVisibility(View.GONE);
//        stno = etfrndno.getText().toString().trim();
        stgrms = etgrms.getText().toString().trim();

        if (stgrms.isEmpty()) {
            tv_grams.setVisibility(View.VISIBLE);
            tv_grams.setText("Please Enter grms");
            ;
        }
//        else if (stno.isEmpty()){
//            giftamount.setText("Money : "+getApplicationContext().getResources().getString(R.string.Rs)+" "+finalamount);
//            tv_contacts.setVisibility(View.VISIBLE);
//            tv_contacts.setText("Please select Contact");
//        }
//        else if (!stno.matches(MobilePattern)||!stno.matches(mobilepattern)){
//            ToastMessage.onToast(activity,"Please enter Valied number",ToastMessage.ERROR);
//        }
        else {
            lockprice();

        }
    }

    private void open_aleartdialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.aleart_dialog, null);
        final EditText etPassword = alertLayout.findViewById(R.id.et_password);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Required Password");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pass = etPassword.getText().toString();
                if (pass.equals(AccountUtils.getPassword(GiftModuleActivity.this))) {
                    open_gift();
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    ToastMessage.onToast(GiftModuleActivity.this, "Password Wrong", ToastMessage.ERROR);
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onShow(DialogInterface arg) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(GiftModuleActivity.this.getResources().getColor(R.color.DarkBrown));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(GiftModuleActivity.this.getResources().getColor(R.color.DarkBrown));

            }
        });
        dialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gift_send:
                if (!NetworkUtils.isConnected(this)) {
                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                } else {
                    init_validation(view);
                }
                return;

//            case R.id.btn_contacts:
//                if (!NetworkUtils.isConnected(this)) {
//                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//
//                }else {
//                    opencontacts();
//                }
//                return;
        }
    }

    public void init_validation(View v) {

        tv_amount.setVisibility(View.GONE);
        tv_grams.setVisibility(View.GONE);
//        tv_contacts.setVisibility(View.GONE);

        send.setVisibility(View.GONE);
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

                GiftModuleActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        validation();
                        send.setVisibility(View.VISIBLE);
                        loading_gif.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);
    }

    public void open_gift() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {
            Log.e("qa",stgrms);
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> get_gift = apiDao.get_gifttranfer("Bearer " + AccountUtils.getAccessToken(this), Stnumber, finalamount, stgrms);
            get_gift.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    List<Listmodel> list = Collections.singletonList(response.body());
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_ACCEPTED
                            || statuscode == HttpsURLConnection.HTTP_CREATED) {
                        for (Listmodel listmodel : list) {
                            dialog.dismiss();
                            sttransactionid = listmodel.getTransaction_Id();
                            stdescription = listmodel.getDescription();
                            openpopupscreen();
                            //  ToastMessage.onToast(activity,"Transferred to Your Loving Ones",ToastMessage.SUCCESS);

                        }
                    } else {
                        try {
                            tv_amount.setVisibility(View.GONE);
                            tv_grams.setVisibility(View.GONE);
//                            tv_contacts.setVisibility(View.GONE);

                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            ToastMessage.onToast(GiftModuleActivity.this, st, ToastMessage.ERROR);
                            JSONObject er = jObjError.getJSONObject("errors");

                            try {
                                JSONArray array_grams = er.getJSONArray("quantity");
                                for (int i = 0; i < array_grams.length(); i++) {
                                    tv_grams.setVisibility(View.VISIBLE);
                                    tv_grams.setText(array_grams.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

//                            try {
//                                JSONArray array_contacts = er.getJSONArray("to");
//                                for (int i = 0; i < array_contacts.length(); i++) {
//                                    tv_contacts.setVisibility(View.VISIBLE);
//                                    tv_contacts.setText(array_contacts.getString(i));
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }

                            try {
                                JSONArray array_amount = er.getJSONArray("amount");
                                for (int i = 0; i < array_amount.length(); i++) {
                                    tv_amount.setVisibility(View.VISIBLE);
                                    tv_amount.setText(array_amount.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                  //  ToastMessage.onToast(GiftModuleActivity.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

    public void getliveprices() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getliverates = apiDao.getlive_rates("Bearer " + AccountUtils.getAccessToken(this));
            getliverates.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("live_status", String.valueOf(statuscode));
                    List<Listmodel> list = Collections.singletonList(response.body());
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {

                        if (list != null) {
                            for (Listmodel listmodel : list) {

                                dialog.dismiss();
//                            st_buyprice =
//                            st_sellprice = listmodel.getSell_price_per_gram();
//                            st_goldtype = listmodel.getGold_type();
//
//                            String purity = listmodel.getGold_purity();


                                liveprice = listmodel.getSell_price_per_gram();
                                Log.e("liveprice", liveprice);
                            }
                        } else {
                            dialog.dismiss();
//                        tv_buyprice.setText(getString(R.string.Rs)+"0.000");
//                        tv_sellprice.setText(getString(R.string.Rs)+"0.000");
                        }
                    } else {
                        dialog.dismiss();
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            JSONObject er = jObjError.getJSONObject("errors");
                            Toast.makeText(GiftModuleActivity.this, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                  //  ToastMessage.onToast(GiftModuleActivity.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

    public void lockprice() {

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

        Call<List<Listmodel>> lockrates = apiDao.getlock_rates(liveprice, "Bearer " + AccountUtils.getAccessToken(this));
        lockrates.enqueue(new Callback<List<Listmodel>>() {
            @Override
            public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                int statuscode = response.code();
                if (statuscode == HttpsURLConnection.HTTP_ACCEPTED) {

                    llmoney.setVisibility(View.VISIBLE);
                    open_aleartdialog();
//                    btnbuygold.setEnabled(true);
//                    btnbuygold.setBackgroundResource(R.drawable.buttonborder);

                } else {
                    ToastMessage.onToast(GiftModuleActivity.this, "Try again", ToastMessage.ERROR);
//                    btnbuygold.setEnabled(false);
//                    btnbuygold.setBackgroundResource(R.drawable.backgroundvisablity);

                }
            }

            @Override
            public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                Log.e("lockratesfail", t.toString());
                ToastMessage.onToast(GiftModuleActivity.this, "We have some issues", ToastMessage.ERROR);

            }
        });

    }

    private JsonObject ApiJsonMap() {

        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("price", liveprice);

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());

            //print parameter
            Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gsonObject;
    }

    public void init_timer() {
        long duration = 300000;
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                String sduration = String.format(Locale.ENGLISH, "%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(l)
                        , TimeUnit.MILLISECONDS.toSeconds(l) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)
                                )
                );
                tv_second_title.setText(sduration);
                tv_second_title.setVisibility(View.VISIBLE);
                //  tvsendagain.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                tv_second_title.setVisibility(View.GONE);
                Intent intent = new Intent(GiftModuleActivity.this, MainFragmentActivity.class);
                startActivity(intent);
                // tvsendagain.setVisibility(View.VISIBLE);
            }
        }.start();
        timerRunning = true;
    }

    @SuppressLint("NewApi")
    public void openpopupscreen() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            Intent intent = new Intent(this, Successpopup.class);
            intent.putExtra("grams", amountvalue);
            intent.putExtra("number", Stnumber);
            intent.putExtra("from", "transfer");
            intent.putExtra("transctionid",sttransactionid);
            intent.putExtra("description",stdescription);
            startActivity(intent);
            Log.e("grams", amountvalue);
            Log.e("number", Stnumber);
            Log.e("from", "transfer");
            Log.e("transactionid", sttransactionid);
            Log.e("description", stdescription);
        }

    }


    public void getdata(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<JsonElement> call = apiDao.get_digitalwallet("Bearer " + AccountUtils.getAccessToken(this));
            call.enqueue(new Callback<JsonElement>() {

                @Override
                public void onResponse(@NonNull Call<JsonElement> call, @NonNull retrofit2.Response<JsonElement> response) {
                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {

                        JsonElement jsonElement = response.body();
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        JsonObject gson = new JsonParser().parse(String.valueOf(jsonObject)).getAsJsonObject();

                        try {
                            JSONObject jo2 = new JSONObject(gson.toString());
                            JSONObject balance = jo2.getJSONObject("balance");
                         String    st_currencyinwords = balance.getString("currencyInWords");
                            String  st_ingrams = balance.getString("humanReadable");
                            String st_incurrency = balance.getString("inCurrency");
                            tv_wallet_gold.setText(st_ingrams+" grams");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
//                else if (stauscode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
//                    getrefresh();
//                }

                    else {
                        try {

                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            Toast.makeText(GiftModuleActivity.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                    dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    dialog.dismiss();
                   // Toast.makeText(GiftModuleActivity.this, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }





}