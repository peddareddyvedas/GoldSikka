package com.goldsikka.goldsikka.Activitys.Events;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.goldsikka.goldsikka.Activitys.PaymentError;
import com.goldsikka.goldsikka.Fragments.Successpopup;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventGift extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    String buttonvalidation, stdescription, sttransactionId;
    Button btnbuygold;
    TextWatcher textWatcher;
    String liveprice;
    ApiDao apiDao;
    String st_ingrams, st_incurrency, st_currencyinwords;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_gold)
    TextView tvgold;
    TextView tvliverate, tv_sellprice, tvchange;
    String paybleamount, sttransactionid;

    String after_dedcation = "null";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_wallet_money)
    TextView tvwalletamount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.wallet_check)
    CheckBox cb_wallet;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_purity)
    TextView tv_purity;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_wallet_money)
    EditText et_wallet_money;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_wallet_error)
    TextView tv_wallet_error;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_paybaleamount)
    TextView tv_paybaleamount;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.btn_buygold)
    Button btn_payment;

    String Walletamount;


    EditText etAmount, etgold;
    TextView tv_grms, tv_amt;
    String finalgold, finalamount, taxPercentage;

    String steventid, amountvalue;
    AlertDialog alertDialogdialog;

    String walletamount = "null";

    boolean isCoupon = true;

    boolean ischeck = false;


    GifImageView loading_gif;
    TextView iv_liveprice;

    LinearLayout llgstamount;
    TextView tvgstamount;

    Button btnamount, btngrams;

    EditText etamount;
    String status = "0";
    TextView tvresult;

    boolean valueEmpty = false;

    TextView unameTv, uidTv, titleTv;

    // String paybleamount;
    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_gift);

        getliveprices();
        iv_liveprice = findViewById(R.id.iv_liveprice);
        iv_liveprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = new ProgressDialog(EventGift.this);
                dialog.setCancelable(false);
                dialog.setMessage("Please wait..");
                dialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getliveprices();
                        etamount.getText().clear();
//                            etgold.getText().clear();
                        dialog.dismiss();
                    }
                }, 1500);

            }
        });

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Gift Gold");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Gift Gold");
        //  toolbar.setTitleTextColor(getColor(R.color.white));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        initlizeviews();
        goldcalcluation();
        getdata();

        wallet_amount();

        et_wallet_money.setHint(Html.fromHtml(getString(R.string.wallet_hint)));

        cb_wallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                 @Override
                                                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                     if (isChecked) {
                                                         et_wallet_money.setVisibility(View.VISIBLE);
                                                         et_wallet_money.setText("");
                                                         ischeck = true;
                                                     } else {
                                                         btnbuygold.setEnabled(true);
                                                         ischeck = false;
                                                         et_wallet_money.setVisibility(View.GONE);
                                                         tv_wallet_error.setVisibility(View.GONE);
                                                         //  setdata();
                                                         // tv_payble_amount.setText(paybleamount);
                                                     }
                                                 }
                                             }
        );

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            steventid = bundle.getString("eventid");
        }
    }

    public void wallet_amount() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<Listmodel> getdetails = apiDao.walletAmount("Bearer " + AccountUtils.getAccessToken(this));

            getdetails.enqueue(new Callback<Listmodel>() {

                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {

                    int statuscode = response.code();

                    Log.e("statuscode dd", String.valueOf(statuscode));

                    if (statuscode == HttpsURLConnection.HTTP_OK) {

                        Listmodel list = response.body();

                        Walletamount = list.getAmount_wallet();

                        tvwalletamount.setText("Available amount : " + getString(R.string.Rs) + " " + Walletamount);
                        dediactWalletMoney();
                        dialog.dismiss();

                    } else {

                        dialog.dismiss();

                       // ToastMessage.onToast(EventGift.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {

                    Log.e("on fails", t.toString());

                    dialog.dismiss();

                  //  ToastMessage.onToast(EventGift.this, "We have some issue", ToastMessage.ERROR);
                }
            });
        }
    }

    public void dediactWalletMoney() {

        textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//                finalamount = etAmount.getText().toString();
//                finalgold = etgold.getText().toString();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//                finalamount = etAmount.getText().toString();
//                finalgold = etgold.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable charSequence) {

                if (charSequence != null && !charSequence.toString().equalsIgnoreCase("")) {
                    if (et_wallet_money.getText().hashCode() == charSequence.hashCode()) {

                        walletamount = et_wallet_money.getText().toString();
                        tv_wallet_error.setVisibility(View.VISIBLE);

                        try {


                            if (Integer.parseInt(paybleamount) >= Integer.parseInt(walletamount)) {

                                // tv_wallet_error.setText("Money less then");
                                tv_wallet_error.setVisibility(View.GONE);

                                int minus = Integer.parseInt(paybleamount) - Integer.parseInt(walletamount);

                                after_dedcation = String.valueOf(minus);

                                // paybleamount = after_dedcation;

                                tv_paybaleamount.setText(after_dedcation);
                                btnbuygold.setEnabled(true);

                                Log.e("Walletamountddd", "" + Walletamount);
                                String result = Walletamount.replaceAll("[^\\w\\s]", "");
                                String stopEnd = result.substring(0, result.length() - 2);
                                int amou = Integer.parseInt(walletamount);
                                int wall = Integer.parseInt(stopEnd);
                                if (amou > wall) {

                                /*double entervalue = Double.parseDouble(walletamount);
                                double walletvalue = Double.parseDouble(Walletamount);

                                if (entervalue > walletvalue) {*/

                                    tv_wallet_error.setVisibility(View.VISIBLE);
                                    btnbuygold.setEnabled(false);
                                    btnbuygold.setBackgroundResource(R.drawable.backgroundvisablity);

                                    tv_wallet_error.setText("You can't enter more then " + Walletamount);
                                    tv_wallet_error.setTextColor(getColor(R.color.red));

                                }

                            } else {

                                tv_paybaleamount.setText("0");
                                btnbuygold.setEnabled(false);

                                btnbuygold.setBackgroundResource(R.drawable.backgroundvisablity);

                                tv_wallet_error.setText("You can't enter more than Payable Amount");
                                tv_wallet_error.setTextColor(getColor(R.color.red));

                            }

                            et_wallet_money.removeTextChangedListener(textWatcher);
                            et_wallet_money.addTextChangedListener(textWatcher);
                        } catch (NumberFormatException e) {

                        }
                    }
                } else {

                    tv_paybaleamount.setText(paybleamount);
                    tv_wallet_error.setText("");
                    walletamount = "null";

                }
            }
        };
        et_wallet_money.addTextChangedListener(textWatcher);
    }

    public void initlizeviews() {

        loading_gif = findViewById(R.id.loading_gif);

        tvchange = findViewById(R.id.tvchange);
        tvliverate = findViewById(R.id.tvliverate);
        tvgold = findViewById(R.id.tv_gold);

        tvgstamount = findViewById(R.id.tvgstamount);
        llgstamount = findViewById(R.id.llgstamount);

        tv_purity = findViewById(R.id.tv_purity);
        //tv_purity.setText(AccountUtils.getPurity(this)+" / "+AccountUtils.getCarat(this));

        tv_sellprice = findViewById(R.id.tvrefliverate);


        btnbuygold = findViewById(R.id.btn_buygold);
        btnbuygold.setOnClickListener(this);

        etamount = findViewById(R.id.et_amount);
        etgold = findViewById(R.id.et_grms);
        etamount.setHint(Html.fromHtml(getString(R.string.amount)));


//        tv_grms =findViewById(R.id.tv_grms);
//        tv_amt = findViewById(R.id.tv_amt);

        btnamount = findViewById(R.id.btInAmount);
        btnamount.setOnClickListener(this);

        btngrams = findViewById(R.id.btInGrams);
        btngrams.setOnClickListener(this);

        tvresult = findViewById(R.id.tv_grms);
    }


    public void getliveprices() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
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

                                dialog.dismiss();
                                liveprice = listmodel.getSell_price_per_gram();
                                Log.e("liveprice", liveprice);
                                tv_sellprice.setText(getString(R.string.Rs) + liveprice);
                                tvliverate.setText(getString(R.string.Rs) + liveprice);
                                double tax = Double.parseDouble(listmodel.getTaxPercentage()) / 100;

                                taxPercentage = String.valueOf(tax);

                            }
                        } else {
                            dialog.dismiss();
//
                        }
                    } else {
                        dialog.dismiss();
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            JSONObject er = jObjError.getJSONObject("errors");
                            Toast.makeText(EventGift.this, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                   // ToastMessage.onToast(EventGift.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

    public void buy_wallet_money() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            //  if (stcouponcode.equals("null")) {
            Log.e("Amount", amountvalue);
            Log.e("Wallet AMount", walletamount);
            Log.e("ID", steventid);

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getdetails = apiDao.buywalletAmount_events("Bearer " + AccountUtils.getAccessToken(this), finalamount, walletamount, steventid);
            getdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode dd", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        Listmodel list = response.body();
                        if (list.getPurchase_Status()) {
                            stdescription = list.getDescription();
                            sttransactionId = list.getTransactionId();
                            onsuceess();
                        }
                        // Log.e("Wallet Money Responce",new Gson().toJson(response.body()));


                        dialog.dismiss();

                    } else {

                        dialog.dismiss();

                      //  ToastMessage.onToast(EventGift.this, "Technical issue", ToastMessage.ERROR);

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                //    ToastMessage.onToast(EventGift.this, "We have some issue", ToastMessage.ERROR);
                }
            });
        }
    }


    public void lockprice() {
        Log.e("livepricelrate", String.valueOf(liveprice));
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

        Call<List<Listmodel>> lockrates = apiDao.getlock_rates(liveprice, "Bearer " + AccountUtils.getAccessToken(this));
        lockrates.enqueue(new Callback<List<Listmodel>>() {
            @Override
            public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("statuscode Lock", String.valueOf(statuscode));
                if (statuscode == HttpsURLConnection.HTTP_ACCEPTED) {
                    // onpayment();

                    if (paybleamount.equals(walletamount)) {

                        buy_wallet_money();

                        // ToastMessage.onToast(Buy_Gold_Information.this,"Money Wallet",ToastMessage.SUCCESS);

                    } else {
//                                if (!after_dedcation.isEmpty()){
//
//
//                                }
                        if (ischeck) {
                            if (walletamount.equals("null")) {
                                ToastMessage.onToast(EventGift.this, "Please enter amount", ToastMessage.ERROR);
                            } else {


                                if (after_dedcation.equals("null")) {

                                    Log.e("payble amount if", paybleamount);
                                } else {
                                    Log.e("payble amount else", paybleamount);

                                    paybleamount = after_dedcation;

                                }

                                onpayment();
                            }
                        } else {
                            onpayment();
                        }

                    }
                    // openpayment();
//                    btnbuygold.setEnabled(true);
//                    btnbuygold.setBackgroundResource(R.drawable.buttonborder);

                } else {
                    ToastMessage.onToast(EventGift.this, "Try After some Time", ToastMessage.ERROR);
//                    btnbuygold.setEnabled(false);
//                    btnbuygold.setBackgroundResource(R.drawable.backgroundvisablity);

                }
            }

            @Override
            public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                Log.e("lockratesfail", t.toString());
               // ToastMessage.onToast(EventGift.this, "We have some issues Try After some Time", ToastMessage.ERROR);

            }
        });

    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        // finish();
    }

    public void btnAmount() {
        amountvalue = etamount.getText().toString();
        etamount.setHint(Html.fromHtml(getString(R.string.amount)));
        tvchange.setText("Enter Amount");
        btngrams.setText("GIFT IN GRAMS");

        btnamount.setBackgroundResource(R.drawable.buy_amount_button);
        btngrams.setBackgroundResource(R.drawable.buy_grams_button);

        btngrams.setTextColor(getResources().getColor(R.color.black));
        btnamount.setTextColor(getResources().getColor(R.color.white));

        status = "0";
        testcal();

    }

    public void btGrams() {
        amountvalue = etamount.getText().toString();
        etamount.setHint(Html.fromHtml(getString(R.string.grams)));
        btnamount.setText("GIFT IN AMOUNT");
        tvchange.setText("Enter Grams");

        btnamount.setBackgroundResource(R.drawable.button_left_border);
        btngrams.setBackgroundResource(R.drawable.button_right_background);

        btnamount.setTextColor(getResources().getColor(R.color.black));
        btngrams.setTextColor(getResources().getColor(R.color.white));

        status = "1";
        testcal();

    }


    public void goldcalcluation() {

        textWatcher = new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//                finalamount = etAmount.getText().toString();
//                finalgold = etgold.getText().toString();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                finalamount = etAmount.getText().toString();
//                finalgold = etgold.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable charSequence) {

                if (charSequence != null && !charSequence.toString().equalsIgnoreCase("")) {

                    if (etamount.getText().hashCode() == charSequence.hashCode()) {

                        cb_wallet.setEnabled(true);

                        cb_wallet.setChecked(false);
                        et_wallet_money.setVisibility(View.GONE);
                        tv_wallet_error.setVisibility(View.GONE);


                        amountvalue = etamount.getText().toString();

                        buttonvalidation = amountvalue;

                        etamount.removeTextChangedListener(textWatcher);
                        etamount.addTextChangedListener(textWatcher);
                        // goldvalue = etgold.getText().toString();

                        if (etamount.length() == 0) {
                            // Toast.makeText(EventGift.this, "empty", Toast.LENGTH_SHORT).show();

                            tvresult.setText("");
                            tvgstamount.setText("");
                            tv_paybaleamount.setText("");

                        }
                        //status = "0";
                        testcal();

                    } else {
                        tv_paybaleamount.setText("");

                    }

                } else {
//                    cb_wallet.setChecked(false);
                    cb_wallet.setEnabled(false);
                    cb_wallet.setChecked(false);

                    et_wallet_money.setVisibility(View.GONE);
                    tv_wallet_error.setVisibility(View.GONE);

                    valueEmpty = true;

                    // Toast.makeText(EventGift.this, "empty", Toast.LENGTH_SHORT).show();

                    tvresult.setText("");
                    tvgstamount.setText("");
                    tv_paybaleamount.setText("");
                    //  btnbuygold.setText(getResources().getString(R.string.proceed_to_gift)+" 0");


                }


            }
        };
        etamount.addTextChangedListener(textWatcher);
    }

    public void testcal() {

        if (status.equals("0")) {
            if (!amountvalue.isEmpty()) {

                if (amountvalue.equals(".")) {
                    Toast.makeText(getApplicationContext(), "Enter a valid input", Toast.LENGTH_SHORT).show();
                } else {
                    double goldgrms = Double.parseDouble(amountvalue);
                    double live = Double.parseDouble(liveprice);
                    double grms = (goldgrms / live);
                    BigDecimal b = BigDecimal.valueOf(grms).setScale(2, RoundingMode.HALF_EVEN);
                    finalgold = String.valueOf(b);

                    tvresult.setText(finalgold);

                    finalamount = amountvalue;
                    double itax = Double.parseDouble(taxPercentage);
                    double astamount = Double.parseDouble(amountvalue);
                    double dtotal = itax * astamount;
                    double finalcaluclation = dtotal + astamount;
                    BigDecimal bf = BigDecimal.valueOf(finalcaluclation).setScale(0, RoundingMode.HALF_UP);
                    tv_paybaleamount.setText(bf.toString());
                    tvgstamount.setText(String.valueOf(bf));
                    paybleamount = String.valueOf(bf);

                }
            } else {
                if (valueEmpty) {
                    // Toast.makeText(this, "empty 0", Toast.LENGTH_SHORT).show();
                    tv_paybaleamount.setText("");

                    // btnbuygold.setText(getResources().getString(R.string.proceed_to_gift));
                }
            }

        }

        if (status.equals("1")) {
            if (!amountvalue.isEmpty()) {

                if (amountvalue.equals(".")) {
                    Toast.makeText(getApplicationContext(), "Enter a valid input", Toast.LENGTH_SHORT).show();
                } else {
                    double goldgrams = Double.parseDouble(amountvalue);

                    double live = Double.parseDouble(liveprice);
                    double grams = live * goldgrams;
                    BigDecimal b = BigDecimal.valueOf(grams).setScale(0, RoundingMode.HALF_EVEN);
                    finalamount = String.valueOf(b);

                    tvresult.setText(finalamount);
                    // finalamount = textWatcher.toString().concat(String.valueOf(etAmount));
                    //  btnbuygold.setText(getResources().getString(R.string.proceed_to_pay) + " " + finalamount);
                    double itax = Double.parseDouble(taxPercentage);
                    double astamount = Double.parseDouble(finalamount);
                    double dtotal = itax * astamount;
                    double finalcaluclation = dtotal + astamount;
                    BigDecimal bf = BigDecimal.valueOf(finalcaluclation).setScale(0, RoundingMode.HALF_EVEN);
                    tv_paybaleamount.setText(bf.toString());
                    tvgstamount.setText(String.valueOf(bf));
                    paybleamount = String.valueOf(bf);
                }
            } else {
                if (valueEmpty) {
                    //  Toast.makeText(this, "empty 1", Toast.LENGTH_SHORT).show();
                    tv_paybaleamount.setText("");
//                    btnbuygold.setText(getResources().getString(R.string.proceed_to_gift));
                }
            }
        }

    }

    public void getdata() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
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
                            st_currencyinwords = balance.getString("currencyInWords");
                            st_ingrams = balance.getString("humanReadable");
                            st_incurrency = balance.getString("inCurrency");

                            setview();

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
                            Toast.makeText(EventGift.this, st, Toast.LENGTH_SHORT).show();
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
                   // Toast.makeText(EventGift.this, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    public void setview() {
        //tvliverate.setText("Live Price 1/gram: "+getString(R.string.Rs) +liveprice);
        tvgold.setText(st_ingrams + " grams");
//        tvamount.setText(getString(R.string.Rs) +st_incurrency);
    }


    public void init_validation() {

        btnbuygold.setVisibility(View.GONE);
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

                EventGift.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        validation();
                        btnbuygold.setVisibility(View.VISIBLE);
                        loading_gif.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);
    }

    public void validation() {

        amountvalue = etamount.getText().toString();
//        finalgold = etgold.getText().toString();

        if (status.equals("0")) {
            if (amountvalue.isEmpty()) {
                ToastMessage.onToast(EventGift.this, "Please Enter Amount", ToastMessage.ERROR);
            } else if (Double.parseDouble(amountvalue) < 50) {
                ToastMessage.onToast(EventGift.this, "Please Enter Min Rs50", ToastMessage.ERROR);
            } else if (Double.parseDouble(finalgold) > 30) {
                openalertdiloug();
                // etAmount.getText().clear();

            } else {
                lockprice();
            }

        } else {
            if (amountvalue.isEmpty()) {
                ToastMessage.onToast(EventGift.this, "Please Enter Gold", ToastMessage.ERROR);
            } else if (Double.parseDouble(finalamount) < 50) {
                ToastMessage.onToast(EventGift.this, "Amount Must be Rs.50", ToastMessage.ERROR);
            } else if (Double.parseDouble(amountvalue) > 30) {
                openalertdiloug();
                // etAmount.getText().clear();
            } else {
                lockprice();
            }
        }


    }


    ImageView imageView_close;

    public void openalertdiloug() {
        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.popup_buygold_screen, null);
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

        alertDialogdialog.show();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btInAmount:
                btnAmount();
                return;

            case R.id.btInGrams:
                btGrams();
                return;
            case R.id.btn_buygold:
                openBuygoldValidation();
                break;

        }

    }

    public void openBuygoldValidation() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            init_validation();
        }
    }

    public void onpayment() {
        final Activity activity = this;
        final Checkout co = new Checkout();
       co.setKeyID("rzp_test_0VM20Pg2VIA2aR");
      //  co.setKeyID("rzp_live_uvxtS5LwJPMIOP");

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("payment_capture", 1);

            // double total = Double.parseDouble("1");
            double total = Double.parseDouble(paybleamount);
            Log.e("giftamout", paybleamount);

            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();

            preFill.put("email", AccountUtils.getEmail(this));
            preFill.put("contact", AccountUtils.getMobile(this));

            options.put("prefill", preFill);

            co.open(activity, options);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    public void onsuceess() {

        Intent intent = new Intent(this, Successpopup.class);
        intent.putExtra("from", "Event");
        intent.putExtra("description", stdescription);
        intent.putExtra("transctionid", sttransactionId);
        startActivity(intent);
    }

    public void onpaymenterror() {


        Intent intent = new Intent(this, PaymentError.class);
        startActivity(intent);
    }

    @Override
    public void onPaymentSuccess(String s) {
        sendgift(s);
        Log.e("payid", s);
    }

    @Override
    public void onPaymentError(int i, String s) {

        walletamount = "null";

        etamount.getText().clear();
        // btnbuygold.setText("PROCEED TO GIFT");

        ToastMessage.onToast(this, "Payment failed", ToastMessage.ERROR);
    }

    public void sendgift(String payid) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<EventModel> gift = apiDao.eventgift("Bearer " + AccountUtils.getAccessToken(this), steventid, payid, finalamount, walletamount);
            gift.enqueue(new Callback<EventModel>() {
                @Override
                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                    int statuscode = response.code();
                    Log.e("statuscodee", String.valueOf(statuscode));
                    EventModel eventtestmodel = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        if (eventtestmodel.isProcessed()) {
                            stdescription = eventtestmodel.getDescription();
                            sttransactionId = eventtestmodel.getTransactionId();
                            onsuceess();
                        } else {
                            onpaymenterror();
                        }
                    } else if (statuscode == 422) {
                        dialog.dismiss();
                        onpaymenterror();
                    } else {
                        dialog.dismiss();
                        onpaymenterror();
                    }
                }

                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {
                    dialog.dismiss();
                    onpaymenterror();
                   // ToastMessage.onToast(EventGift.this, "We have some issues try after some time", ToastMessage.ERROR);
                }
            });
        }
    }
}