package com.goldsikka.goldsikka.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.goldsikka.goldsikka.Activitys.Passbook_Activity;
import com.goldsikka.goldsikka.Activitys.Predict_price.Predicted_List;
import com.goldsikka.goldsikka.Activitys.Profile.CustomerAddAddress;
import com.goldsikka.goldsikka.Activitys.RegistationActivity;
import com.goldsikka.goldsikka.AddBankDetailsForSellModule;
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
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class Sell_Fragment extends AppCompatActivity implements View.OnClickListener {

    TextView tv_amount,tv_gramserror,tv_ammounterror,tvfractionalamount,tv_liveprice;
    EditText etgrms;
    String amountvalue,sttransactionid,stdescription,taxPercentage;
    LinearLayout ll_bankdetsila;
    Button btn_AddBank;

    ApiDao apiDao;
    TextWatcher textWatcher;
    String liveprice,finalamount,fprice;
    double sellprice;
    BigDecimal sellpricepoint;
    Button btnnext;
    double redeem;
    TextView ivliveprice;

    LinearLayout llmoney,llfractionalmoney;


    TextView tv_wallet_gold;

    GifImageView loading_gif;

    TextView tv_second_title;
    CountDownTimer countDownTimer;
    private boolean timerRunning;
    RelativeLayout backbtn;


    Spinner spin_wallet_bank;

    String[] spinknowarray = {"Select ","Bank Amount","Booking Account"};
    ArrayList<String> knowlist;

    Spinner spin_bank_details;

    String[] spinbank = {"Select ","Bank Amount","Booking Account"};
    ArrayList<String> banklist;
    String SelectBank;

    String stknow,bank_id;
    String select_option = "Select";
    List<Listmodel> list;
    TextView select_wallet_option;
    LinearLayout ll_sell;


TextView unameTv, uidTv, titleTv;
    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_gold_design);

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

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Sell Gold");

        tv_second_title = findViewById(R.id.tv_second_title);
        ivliveprice = findViewById(R.id.iv_liveprice);
        ivliveprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog  dialog = new ProgressDialog(Sell_Fragment.this);
                dialog.setCancelable(false);
                dialog.setMessage("Please wait..");
                dialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getliveprices();
                        dialog.dismiss();
                    }
                },1500);

            }
        });
        getliveprices();
        initlizeviews();
        init_timer();
        walletgold();

        ArrayAdapter spinnerknow_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,spinknowarray);
        spinnerknow_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_wallet_bank.setAdapter(spinnerknow_adapter);
        spinknowclick();
        spinner_bank();
    }

    public void spinner_bank(){
        banklist = new ArrayList<>();
        loaddata();
        spin_bank_details.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectBank = spin_bank_details.getItemAtPosition(spin_bank_details.getSelectedItemPosition()).toString();
                //    spinner_state.getSelectedView().setTextColor(getResources().getColor(R.color.Blue));
                ((TextView) view).setTextColor(ContextCompat.getColor(Sell_Fragment.this, R.color.DarkBrown));
                // state_id     = spinner_state.getSelectedItemPosition();
              //  if (!SelectBank.equals("Select Bank")) {
                    Listmodel listmodel = list.get(i);
                    bank_id = listmodel.getId();
//                    rs_state = listmodel.getName();

                    //Toast.makeText(activity,String.valueOf(id), Toast.LENGTH_SHORT).show();
               // }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }

    public void loaddata(){
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
            Call<List<Listmodel>> get_address = apiDao.get_bank("Bearer " + AccountUtils.getAccessToken(this));
            get_address.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    list = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        if (list.size() != 0) {
                            ll_bankdetsila.setVisibility(View.VISIBLE);
                            btn_AddBank.setVisibility(View.GONE);
                            for (Listmodel listmodel : list) {
                                dialog.dismiss();
                                banklist.add(listmodel.getName());
                            }
                            spin_bank_details.setAdapter(new ArrayAdapter<String>(Sell_Fragment.this, android.R.layout.simple_spinner_dropdown_item,
                                    banklist));

                        }else {
                            dialog.dismiss();
                            btn_AddBank.setVisibility(View.VISIBLE);
                            ll_bankdetsila.setVisibility(View.GONE);
                        }
                    }
                }


                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                    ToastMessage.onToast(Sell_Fragment.this, "We Have Some Issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }

    }

    public void spinknowclick(){
        knowlist = new ArrayList<>();
        spin_wallet_bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stknow = spin_wallet_bank.getItemAtPosition(spin_wallet_bank.getSelectedItemPosition()).toString();
                ((TextView) view).setTextColor(ContextCompat.getColor(Sell_Fragment.this, R.color.DarkBrown));
                if (stknow.equals("Bank Amount")) {
                    select_option = "bankAccount";
                    ll_sell.setVisibility(View.VISIBLE);

                }else if (stknow.equals("Booking Account")){
                    select_option = "moneyWallet";
                    ll_sell.setVisibility(View.GONE);


                }else {
                    select_option = "Select";
                    ll_sell.setVisibility(View.GONE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void init_timer(){
        long duration = 300000;

        countDownTimer =    new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                String sduration = String.format(Locale.ENGLISH,"%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(l)
                        ,TimeUnit.MILLISECONDS.toSeconds(l)-
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
                Intent intent = new Intent(Sell_Fragment.this, MainFragmentActivity.class);
                startActivity(intent);
                // tvsendagain.setVisibility(View.VISIBLE);
            }
        }.start();
        timerRunning = true;
    }

    public void walletgold(){
        final ProgressDialog dialog = new ProgressDialog(Sell_Fragment.this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(Sell_Fragment.this)){
            ToastMessage.onToast(Sell_Fragment.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(Sell_Fragment.this)).create(ApiDao.class);

            Call<JsonElement> call = apiDao.get_digitalwallet("Bearer " + AccountUtils.getAccessToken(Sell_Fragment.this));
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
                            String st_currencyinwords = balance.getString("currencyInWords");
                            String st_ingrams = balance.getString("humanReadable");
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
//                            Toast.makeText(Sell_Fragment.this, st, Toast.LENGTH_SHORT).show();
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
                  //  Toast.makeText(Sell_Fragment.this, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void getliveprices(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
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

                                        dialog.dismiss();
                                liveprice = listmodel.getBuy_price_per_gram();
                                tv_liveprice.setText(getResources().getString(R.string.Rs) + " " + liveprice);

                                Log.e("Tax",listmodel.getTaxPercentage());

                                double tax = Double.parseDouble(listmodel.getTaxPercentage()) / 100;
                                taxPercentage = String.valueOf(tax);
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
//                            Toast.makeText(Sell_Fragment.this, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(Sell_Fragment.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }
    public void lockprice(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<List<Listmodel>> lockrates = apiDao.getlock_rates(liveprice, "Bearer " + AccountUtils.getAccessToken(this));
            lockrates.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    if (statuscode == HttpsURLConnection.HTTP_ACCEPTED) {

                        tv_ammounterror.setVisibility(View.GONE);

                        tv_ammounterror.setText(null);
//                        open_aleartdialog();
                        opensell();
                        // validationapi();

//                    btnbuygold.setEnabled(true);
//                    btnbuygold.setBackgroundResource(R.drawable.buttonborder);

                    } else {
                        ToastMessage.onToast(Sell_Fragment.this, "Try again", ToastMessage.ERROR);
//                    btnbuygold.setEnabled(false);
//                    btnbuygold.setBackgroundResource(R.drawable.backgroundvisablity);

                    }
                }

                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                    Log.e("lockratesfail", t.toString());
                    ToastMessage.onToast(Sell_Fragment.this, "We have some issues", ToastMessage.ERROR);

                }
            });
        }
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
    public void validationapi(){

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();
 if (!NetworkUtils.isConnected(this)){
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
     apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
     Call<Listmodel> getvalidation = apiDao.get_validationsell("Bearer " + AccountUtils.getAccessToken(this), amountvalue,select_option);
     getvalidation.enqueue(new Callback<Listmodel>() {
         @Override
         public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
             int statuscode = response.code();
             if (statuscode == HttpsURLConnection.HTTP_OK) {
                 dialog.dismiss();
                 lockprice();
             } else if (statuscode == 422) {

                 dialog.dismiss();
                 try {

                     tv_ammounterror.setVisibility(View.GONE);
                     tv_gramserror.setVisibility(View.GONE);

                     assert response.errorBody() != null;
                     JSONObject jObjError = new JSONObject(response.errorBody().string());
                     String st = jObjError.getString("message");
                     ToastMessage.onToast(Sell_Fragment.this, st, ToastMessage.ERROR);

                     JSONObject er = jObjError.getJSONObject("errors");
                     try {
                         JSONArray array_title = er.getJSONArray("grams");
                         for (int i = 0; i < array_title.length(); i++) {
                             tv_gramserror.setVisibility(View.VISIBLE);
                             tv_gramserror.setText(array_title.getString(i));
                         }

                     } catch (Exception e) {
                         e.printStackTrace();
                     }

                     try {
                         JSONArray array_address = er.getJSONArray("amount");
                         for (int i = 0; i < array_address.length(); i++) {
                             tv_ammounterror.setVisibility(View.VISIBLE);
                             tv_ammounterror.setText(array_address.getString(i));
                         }

                     } catch (Exception e) {
                         e.printStackTrace();
                     }


                 } catch (JSONException | IOException e) {
                     e.printStackTrace();
                 }
                 dialog.dismiss();

             } else {
                 dialog.dismiss();
                 assert response.errorBody() != null;

                 try {
                     JSONObject jObjError = new JSONObject(response.errorBody().string());
                     try {
                         String st = jObjError.getString("message");
                         tv_ammounterror.setVisibility(View.VISIBLE);
                         tv_ammounterror.setText(st);
                         boolean bankdetails = jObjError.getBoolean("is_bank");
                         if (!bankdetails) {
                             Addbankdetailspopup(st);
                         }
                     } catch (JSONException e) {
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
             lockprice();
//             ToastMessage.onToast(Sell_Fragment.this, "We have some issues", ToastMessage.ERROR);
         }
     });
         }
    }

    @Override
    public void onBackPressed() {
       // NavUtils.navigateUpFromSameTask(this);
        countDownTimer.cancel();
        timerRunning = false;


        super.onBackPressed();
         finish();
    }

    public void initlizeviews(){
        llmoney = findViewById(R.id.llmoney);
       // llmoney.setVisibility(View.GONE);

        ll_sell = findViewById(R.id.ll_bank);

        llfractionalmoney = findViewById(R.id.llfractionalmoney);
        spin_wallet_bank = findViewById(R.id.spin_wallet_bank);
        spin_bank_details = findViewById(R.id.spin_bank_details);
       // llfractionalmoney.setVisibility(View.GONE);
        ll_bankdetsila = findViewById(R.id.ll_bank_details);
        btn_AddBank = findViewById(R.id.add_bank);
        btn_AddBank.setOnClickListener(this);

        select_wallet_option = findViewById(R.id.select_wallet_option);
        select_wallet_option.setText(Html.fromHtml(getString(R.string.select_bank_option)));

        tv_liveprice = findViewById(R.id.tv_liveprice);
        tvfractionalamount = findViewById(R.id.tvfractionalamount);
//        liveprice = AccountUtils.getLivePrice(this);
//        tv_wallet_amount = view.findViewById(R.id.tv_wallet_amount);
        tv_wallet_gold = findViewById(R.id.tv_wallet_gold);
       // tv_wallet_amount.setText(st_wallet_amount);

        etgrms = findViewById(R.id.gift_grms);
        tv_ammounterror = findViewById(R.id.tv_amounterror);
        tv_gramserror = findViewById(R.id.tv_grmserror);
        tv_amount = findViewById(R.id.tv_amount);

        etgrms.setHint(Html.fromHtml(getString(R.string.grams)));

        tv_amount.setVisibility(View.GONE);
        tvfractionalamount.setVisibility(View.GONE);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().equalsIgnoreCase("")) {

                    if (etgrms.getText().hashCode() == s.hashCode()) {


                        try {
                            tv_ammounterror.setVisibility(View.GONE);
                            tv_gramserror.setVisibility(View.GONE);
                            amountvalue = etgrms.getText().toString();
                            double goldgrams = Double.parseDouble(amountvalue);


//                            double grams = Double.parseDouble(liveprice) * goldgrams;
//                            BigDecimal b21 = BigDecimal.valueOf(grams).setScale(2, RoundingMode.HALF_EVEN);
//                            finalamount = new String(String.valueOf(b21));

                            if (!amountvalue.isEmpty()) {

                                if (amountvalue.equals(".")) {
                                    Toast.makeText(getApplicationContext(), "Enter a valid input", Toast.LENGTH_SHORT).show();
                                }else if (goldgrams<2){
                                    tv_ammounterror.setVisibility(View.VISIBLE);
                                    tv_ammounterror.setText("Min 2 grams");
                                    tv_amount.setVisibility(View.GONE);
                                    btnnext.setEnabled(false);
                                }else if (goldgrams>30){
                                    tv_amount.setVisibility(View.GONE);
                                    btnnext.setEnabled(false);
                                    tv_ammounterror.setVisibility(View.VISIBLE);
                                    tv_ammounterror.setText("Max 30 grams");
                                }
                                else {
                                    btnnext.setEnabled(true);
                                  //  double goldgrams = Double.parseDouble(amountvalue);
                                    tv_ammounterror.setVisibility(View.GONE);
                                    Log.e("Tax", String.valueOf(taxPercentage));


                                    double live = Double.parseDouble(liveprice);
                                    Log.e("live", ""+live);
                                    Log.e("goldgrams", ""+goldgrams);

                                    double grams = live * goldgrams;
                                    BigDecimal b = BigDecimal.valueOf(grams).setScale(0, RoundingMode.HALF_EVEN);
                                    finalamount = String.valueOf(b);
                                    tv_amount.setVisibility(View.VISIBLE);

//                                    tvresult.setText(finalamount);
                                    // finalamount = textWatcher.toString().concat(String.valueOf(etAmount));
                                    //  btnbuygold.setText(getResources().getString(R.string.proceed_to_pay) + " " + finalamount);
                                    double itax = Double.parseDouble(taxPercentage);
                                    double astamount = Double.parseDouble(finalamount);
                                    double dtotal = itax * astamount;
                                    double finalcaluclation = astamount - dtotal;
                                    BigDecimal bf = BigDecimal.valueOf(finalcaluclation).setScale(0, RoundingMode.HALF_EVEN);
                                    tv_amount.setText("Money : "+getApplicationContext().getResources().getString(R.string.Rs)+" "+bf.toString());
                                    sellpricepoint  = BigDecimal.valueOf(finalcaluclation).setScale(2, RoundingMode.HALF_EVEN);
                                    Log.e("sellpricepoint",sellpricepoint.toString());
                                }
                            }


//                            if (Double.parseDouble(amountvalue) < 2) {
//                                tv_amount.setVisibility(View.GONE);
//                                tvfractionalamount.setVisibility(View.GONE);
////                                llmoney.setVisibility(View.GONE);
////                                llfractionalmoney.setVisibility(View.GONE);
////                                tv_amount.setVisibility(View.GONE);
////                                tvfractionalamount.setVisibility(View.GONE);
////                                tv_gramserror.setVisibility(View.VISIBLE);
////                                tv_ammounterror.setVisibility(View.GONE);
//                                //tv_gramserror.setText("Please Enter min 2 grams");
//                               // btnnext.setEnabled(false);
//                                // Toast.makeText(activity, "Please Enter min 2 grams", Toast.LENGTH_SHORT).show();
//                            } else
//                                if (Double.parseDouble(amountvalue) >= 2 && Double.parseDouble(amountvalue) < 3) {
////                                llmoney.setVisibility(View.VISIBLE);
////                                llfractionalmoney.setVisibility(View.VISIBLE);
//                                tv_amount.setVisibility(View.VISIBLE);
//                                tvfractionalamount.setVisibility(View.VISIBLE);
//
//                                tv_gramserror.setVisibility(View.GONE);
//                                fprice = "18";
//                                redeem = Double.parseDouble(finalamount) * (Double.parseDouble(fprice) / 100);
//                                sellprice = Double.parseDouble(finalamount) - redeem;
//
//                                BigDecimal bellow3grms = BigDecimal.valueOf(sellprice).setScale(2, RoundingMode.HALF_EVEN);
//                                tv_amount.setText("Money : " +getApplicationContext().getResources().getString(R.string.Rs)+" "+ String.valueOf(bellow3grms));
//
//                                BigDecimal frpbellow3grms = BigDecimal.valueOf(redeem).setScale(2, RoundingMode.HALF_EVEN);
//                                tvfractionalamount.setText("Fractional Price : " +getApplicationContext().
//                                        getResources().getString(R.string.Rs)+" "+String.valueOf(frpbellow3grms));
//                                    sellpricepoint  = BigDecimal.valueOf(sellprice).setScale(2, RoundingMode.HALF_EVEN);
//
//                                    Log.e("sellprice",String.valueOf(sellpricepoint));
//                            } else if (Double.parseDouble(amountvalue) >= 3 && Double.parseDouble(amountvalue) < 4) {
////                                llmoney.setVisibility(View.VISIBLE);
////                                llfractionalmoney.setVisibility(View.VISIBLE);
//                                tv_amount.setVisibility(View.VISIBLE);
//                                tvfractionalamount.setVisibility(View.VISIBLE);
//                                tv_gramserror.setVisibility(View.GONE);
//                                fprice = "16";
//                                redeem = Double.parseDouble(finalamount) * (Double.parseDouble(fprice) / 100);
//                                sellprice = Double.parseDouble(finalamount) - redeem;
//
//                                BigDecimal frpbellow4grms = BigDecimal.valueOf(redeem).setScale(2, RoundingMode.HALF_EVEN);
//                                tvfractionalamount.setText("Fractional Price : " +getApplicationContext().
//                                        getResources().getString(R.string.Rs)+" "+String.valueOf(frpbellow4grms));
//
//                                BigDecimal bellow4grms = BigDecimal.valueOf(sellprice).setScale(2, RoundingMode.HALF_EVEN);
//                                tv_amount.setText("Money : " +getApplicationContext().getResources().getString(R.string.Rs)+" "+ String.valueOf(bellow4grms));
//                                    sellpricepoint  = BigDecimal.valueOf(sellprice).setScale(2, RoundingMode.HALF_EVEN);
//
//                                    Log.e("sellprice",String.valueOf(sellpricepoint));
//                            } else if (Double.parseDouble(amountvalue) >= 4 && Double.parseDouble(amountvalue) < 5) {
//                                tv_gramserror.setVisibility(View.GONE);
////                                llmoney.setVisibility(View.VISIBLE);
////                                llfractionalmoney.setVisibility(View.VISIBLE);
//                                tv_amount.setVisibility(View.VISIBLE);
//                                tvfractionalamount.setVisibility(View.VISIBLE);
//                                fprice = "14";
//                                redeem = Double.parseDouble(finalamount) * (Double.parseDouble(fprice) / 100);
//
//                                BigDecimal frpbellow5grms = BigDecimal.valueOf(redeem).setScale(2, RoundingMode.HALF_EVEN);
//                                tvfractionalamount.setText("Fractional Price : " +getApplicationContext().
//                                        getResources().getString(R.string.Rs)+" "+String.valueOf(frpbellow5grms));
//
//                                sellprice = Double.parseDouble(finalamount) - redeem;
//                                BigDecimal bellow5grms = BigDecimal.valueOf(sellprice).setScale(2, RoundingMode.HALF_EVEN);
//                                tv_amount.setText("Money : " +getApplicationContext().getResources().getString(R.string.Rs)+" "+ String.valueOf(bellow5grms));
//                                    sellpricepoint  = BigDecimal.valueOf(sellprice).setScale(2, RoundingMode.HALF_EVEN);
//
//                                    Log.e("sellprice",String.valueOf(sellpricepoint));
//                            } else {
//                                tv_amount.setVisibility(View.VISIBLE);
//                                tvfractionalamount.setVisibility(View.GONE);
//                                btnnext.setEnabled(true);
//
////                                llfractionalmoney.setVisibility(View.GONE);
//                                tv_gramserror.setVisibility(View.GONE);
//                                tv_amount.setText("Money : " +getApplicationContext().getResources().getString(R.string.Rs)+" "+ finalamount);
//                                        sellprice = Double.parseDouble(finalamount);
//                                    sellpricepoint  = BigDecimal.valueOf(sellprice).setScale(2, RoundingMode.HALF_EVEN);
//
//                                    Log.e("sellprice",String.valueOf(sellpricepoint));
//                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                }
                else {
                    tv_ammounterror.setVisibility(View.GONE);
                    btnnext.setEnabled(true);
                    tv_gramserror.setText(null);
                    tv_amount.setText(null);
                    tvfractionalamount.setText(null);
                }

            }
        };
        etgrms.addTextChangedListener(textWatcher);

        loading_gif = findViewById(R.id.loading_gif);
        btnnext = findViewById(R.id.btn_next);
        btnnext.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next:
                if (!NetworkUtils.isConnected(this)) {
                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                }else {
                    init_validation(view);
                }
                break;

            case R.id.add_bank:
                Intent intent = new Intent(this,Customer_AddBankDetails.class);
                intent.putExtra("from","sell");
                startActivity(intent);
                break;

        }

    }
    public void init_validation(View v){

        btnnext.setVisibility(View.GONE);
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

                Sell_Fragment.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        validation();
                        btnnext.setVisibility(View.VISIBLE);
                        loading_gif.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);
    }

    private void validation() {
        amountvalue = etgrms.getText().toString().trim();
        tv_ammounterror.setVisibility(View.GONE);
        if(amountvalue.isEmpty()){
            tv_ammounterror.setVisibility(View.VISIBLE);
            tv_ammounterror.setText("Please enter the Grams");
           // ToastMessage.onToast(activity,,ToastMessage.WARNING);
        }else if (select_option.equals("Select")){
            ToastMessage.onToast(this,"Select Transfer option",ToastMessage.ERROR);

        }
        else {
            validationapi();

          //  opensell();
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
                if (pass.equals(AccountUtils.getPassword(Sell_Fragment.this))){


                    opensell();
                    dialog.dismiss();
                }
                else {
                    dialog.dismiss();
                    ToastMessage.onToast(Sell_Fragment.this,"Password Wrong",ToastMessage.ERROR);
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
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Sell_Fragment.this.getResources().getColor(R.color.textcolorprimary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Sell_Fragment.this.getResources().getColor(R.color.textcolorprimary));

            }
        });
        dialog.show();
    }

    private void opensell() {


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        }else {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getsell_details = apiDao.get_sell("Bearer " + AccountUtils.getAccessToken(this), amountvalue, String.valueOf(sellpricepoint),select_option,bank_id);
            getsell_details.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {

                    int statuscode = response.code();
                    List<Listmodel> list = Collections.singletonList(response.body());
                    if (statuscode == HttpsURLConnection.HTTP_NO_CONTENT || statuscode == HttpsURLConnection.HTTP_CREATED
                            || statuscode == HttpsURLConnection.HTTP_ACCEPTED || statuscode == HttpsURLConnection.HTTP_OK) {
                        for (Listmodel listmodel : list) {
                            dialog.dismiss();

                            sttransactionid = listmodel.getTransaction_Id();
                            stdescription = listmodel.getDescription();
//                        ToastMessage.onToast(this,"Successfuly sold ", ToastMessage.SUCCESS);
                            openpopupscreen();
                            walletgold();
                            //  onsucces();

                        }
                    } else if (statuscode == 422) {

                        dialog.dismiss();
                        try {

                            tv_ammounterror.setVisibility(View.GONE);
                            tv_gramserror.setVisibility(View.GONE);

                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            ToastMessage.onToast(Sell_Fragment.this, st, ToastMessage.ERROR);

                            JSONObject er = jObjError.getJSONObject("errors");
                            try {
                                JSONArray array_title = er.getJSONArray("grams");
                                for (int i = 0; i < array_title.length(); i++) {
                                    tv_gramserror.setVisibility(View.VISIBLE);
                                    tv_gramserror.setText(array_title.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray array_address = er.getJSONArray("amount");
                                for (int i = 0; i < array_address.length(); i++) {
                                    tv_ammounterror.setVisibility(View.VISIBLE);
                                    tv_ammounterror.setText(array_address.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();

                    } else {
                        assert response.errorBody() != null;
                        JSONObject jObjError = null;
                        try {
                            jObjError = new JSONObject(response.errorBody().string());
                            try {
                                String st = null;
                                st = jObjError.getString("message");
                                ToastMessage.onToast(Sell_Fragment.this, st, ToastMessage.ERROR);
                                boolean bankdetails = jObjError.getBoolean("is_bank");
                                if (!bankdetails) {
                                    Addbankdetailspopup(st);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();

                    }

                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    openpopupscreen();
                    walletgold();
//                    ToastMessage.onToast(Sell_Fragment.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }
    @SuppressLint("NewApi")
    public void openpopupscreen(){

        Intent intent = new Intent(this,Successpopup.class);
        intent.putExtra("grams",amountvalue);
        intent.putExtra("amount", String.valueOf(sellpricepoint));
        intent.putExtra("from","sell");
        intent.putExtra("transctionid",sttransactionid);
        intent.putExtra("description",stdescription);
        startActivity(intent);

    }

    AlertDialog alertDialogdialog;
    GifImageView loading_gifbank;
    Button submit;
    TextView tvtext;
    ImageView img_close;

    public void Addbankdetailspopup(String msg){
        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.bankdetailslinkforsellpopup,null);
        alertdilog.setCancelable(false);
        alertdilog.setView(dialogview);
        alertDialogdialog = alertdilog.create();

        tvtext = dialogview.findViewById(R.id.tvaddress);
        tvtext.setText(msg);
        img_close = dialogview.findViewById(R.id.img_close);
        loading_gifbank = dialogview.findViewById(R.id.loading_gif);

        alertDialogdialog.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogdialog.dismiss();
            }
        });
        submit = dialogview.findViewById(R.id.btn_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit.setVisibility(View.GONE);
                loading_gifbank.setVisibility(View.VISIBLE);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {

                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Sell_Fragment.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getbankdetails();
                                submit.setVisibility(View.VISIBLE);
                                loading_gifbank.setVisibility(View.GONE);
                            }
                        });
                    }
                }, 500);
            }
        });
    }
    public void getbankdetails(){
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            Intent intent = new Intent(Sell_Fragment.this, Customer_AddBankDetails.class);
            intent.putExtra("from","sell");
            startActivity(intent);
        }
    }
}
