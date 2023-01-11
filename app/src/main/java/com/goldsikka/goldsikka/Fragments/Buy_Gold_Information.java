package com.goldsikka.goldsikka.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.goldsikka.goldsikka.Activitys.BaseActivity;

import com.goldsikka.goldsikka.Activitys.LoginActivity;
import com.goldsikka.goldsikka.Activitys.PaymentError;
import com.goldsikka.goldsikka.Activitys.RegistationActivity;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collections;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Buy_Gold_Information extends BaseActivity implements baseinterface, PaymentResultListener {

    String stamount, taxPercentage, liveprice, stgold, status;
    String stcouponcode = "null";
    String stCoupongold;
    String streffercode;
    boolean payment_status;
    String paybleamount, sttransactionid, stdescription;
    double finalcaluclation;
    ApiDao apiDao;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_paybleamount)
    TextView tv_payble_amount;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_totalamount)
    TextView tv_total_amount;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_totaldiscount)
    TextView tv_total_discount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etreferralcode)
    EditText etreferalcode;

    //    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.tv_tax)
//    TextView tvtax;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_quantity)
    TextView tvcustomer_gold;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_amount)
    TextView tvcustomer_amount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvgold_grams)
    TextView tvgoldquantiy;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_wallet_money)
    TextView tvwalletamount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvliverate)
    TextView tvliverate;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvoffergold)
    TextView tvoffer;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_total_amount)
    TextView tvfamount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_wallet_money)
    EditText et_wallet_money;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_referal_code_error)
    TextView tv_referal_code_error;


    RelativeLayout backbtn;

    TextWatcher textWatcher;

    String Walletamount;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_roundoffvalue)
    TextView tvroundoffvalue;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.wallet_check)
    CheckBox cb_wallet;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_wallet_error)
    TextView tv_wallet_error;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.lloffer)
    LinearLayout lloffer;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.btn_payment)
    Button btn_payment;

    GifImageView loading_gif;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tv_second_title)
    TextView tv_second_title;

    CountDownTimer countDownTimer;
    private boolean timerRunning;
    String after_dedcation = "null";

    boolean isCoupon;
    String amountvalue = "null";
    boolean isCheck = false;

    TextView unameTv, uidTv, titleTv;


    @Override
    protected int getLayoutId() {
        return R.layout.buygoldinformation_design;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        ButterKnife.bind(this);
        //getliveprices();
        btn_payment = findViewById(R.id.btn_payment);
        loading_gif = findViewById(R.id.loading_gif);
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Gold Suvidha");
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            status = bundle.getString("status");
            stamount = bundle.getString("customer_rate");
            stgold = bundle.getString("customer_gold");
            liveprice = bundle.getString("liveprice");
            stCoupongold = bundle.getString("Coupongold");
            stcouponcode = bundle.getString("couponcode");
            isCoupon = bundle.getBoolean("isCoupon");
            taxPercentage = bundle.getString("taxPercentage");

        }

        tvliverate.setText(getString(R.string.Rs) + liveprice);
        setdata();
        wallet_amount();
        init_timer();

        et_wallet_money.setHint(Html.fromHtml(getString(R.string.wallet_hint)));
        // openoffer();
//        startTimer();

        cb_wallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                 @Override
                                                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                     if (isChecked) {
                                                         et_wallet_money.setVisibility(View.VISIBLE);
                                                         et_wallet_money.setText("");
                                                         isCheck = true;
                                                     } else {
                                                         btn_payment.setEnabled(true);
                                                         isCheck = false;
                                                         et_wallet_money.setVisibility(View.GONE);
                                                         tv_wallet_error.setVisibility(View.GONE);
                                                         setdata();
                                                         // tv_payble_amount.setText(paybleamount);
                                                     }
                                                 }
                                             }
        );

    }

    public void dediactWalletMoney() {

        textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//                finalamount = etAmount.getText().toString();
//                finalgold = etgold.getText().toString();

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


//                finalamount = etAmount.getText().toString();
//                finalgold = etgold.getText().toString();

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void afterTextChanged(Editable charSequence) {

                if (charSequence != null && !charSequence.toString().equalsIgnoreCase("")) {
                    if (et_wallet_money.getText().hashCode() == charSequence.hashCode()) {
                        amountvalue = et_wallet_money.getText().toString();
                        Log.e("amountvalue", "" + amountvalue);
                        tv_wallet_error.setVisibility(View.VISIBLE);


                        if (Integer.parseInt(paybleamount) >= Integer.parseInt(amountvalue)) {

                            // tv_wallet_error.setText("Money less then");
                            tv_wallet_error.setVisibility(View.GONE);

                            int minus = Integer.parseInt(paybleamount) - Integer.parseInt(amountvalue);

                            after_dedcation = String.valueOf(minus);

                            // paybleamount = after_dedcation;

                            tv_payble_amount.setText(getString(R.string.Rs) + after_dedcation);
                            btn_payment.setEnabled(true);


                            Log.e("Walletamountddd", "" + Walletamount);
                            String result = Walletamount.replaceAll("[^\\w\\s]", "");
                            String stopEnd = result.substring(0, result.length() - 2);
                            int amou = Integer.parseInt(amountvalue);
                            int wall = Integer.parseInt(stopEnd);
                            if (amou > wall) {
                              /*  double entervalue = Double.parseDouble(amountvalue);
                                double walletvalue = Double.parseDouble(Walletamount);
                                Log.e("walletvalue", "" + walletvalue);

                                if (entervalue > walletvalue) {
                                    Log.e("amountvalue", "" + entervalue);
                                    Log.e("walletvalue", "" + walletvalue);*/

                                tv_wallet_error.setVisibility(View.VISIBLE);
                                btn_payment.setEnabled(false);
                                btn_payment.setBackgroundResource(R.drawable.backgroundvisablity);

                                tv_wallet_error.setText("You can't enter more then " + Walletamount);
                                tv_wallet_error.setTextColor(getColor(R.color.red));

                            } /*else if (amountvalue.equals("0")) {
                                Log.e("edit", "" + amountvalue);
                                btn_payment.setEnabled(false);
                                btn_payment.setBackgroundResource(R.drawable.backgroundvisablity);
                                tv_wallet_error.setVisibility(View.VISIBLE);
                                tv_wallet_error.setText("Please enter more than 0 Rupees");
                                tv_wallet_error.setTextColor(getColor(R.color.red));
                            }*/

                        } else {
                            tv_payble_amount.setText(getString(R.string.Rs) + "0");
                            btn_payment.setEnabled(false);
                            btn_payment.setBackgroundResource(R.drawable.backgroundvisablity);
                            tv_wallet_error.setText("You can't enter more than Payable Amount");
                            tv_wallet_error.setTextColor(getColor(R.color.red));

                        }

                        et_wallet_money.removeTextChangedListener(textWatcher);
                        et_wallet_money.addTextChangedListener(textWatcher);

                    }
                } else {

                    tv_payble_amount.setText(getString(R.string.Rs) + paybleamount);
                    tv_wallet_error.setText("");
                    amountvalue = "null";

                }
            }
        };
        et_wallet_money.addTextChangedListener(textWatcher);
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

                        // ToastMessage.onToast(Buy_Gold_Information.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {

                    Log.e("on fails", t.toString());

                    dialog.dismiss();

                   // ToastMessage.onToast(Buy_Gold_Information.this, "We have some issue", ToastMessage.ERROR);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        countDownTimer.onFinish();
        timerRunning = false;
        super.onBackPressed();

        finish();
    }

    //    @Override
//    protected void onPause() {
//
//        countDownTimer.cancel();
//        timerRunning = false;
//        super.onPause();
//    }

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
                Intent intent = new Intent(Buy_Gold_Information.this, Buy_Digitalgold.class);
                startActivity(intent);

                // tvsendagain.setVisibility(View.VISIBLE);
            }
        }.start();
        timerRunning = true;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    public void setdata() {

        if (!isCoupon) {

            lloffer.setVisibility(View.VISIBLE);
            tvoffer.setText(stCoupongold);

        }

        if (status.equals("0")) {
            tvcustomer_amount.setText(getString(R.string.Rs) + stamount);
        }

        //  tvtax.setText(taxPercentage);
        tvcustomer_gold.setText(stgold + " grams");
        tvcustomer_amount.setText(getString(R.string.Rs) + stamount);

        double itax = Double.parseDouble(taxPercentage);
        double astamount = Double.parseDouble(stamount);
        double dtotal = itax * astamount;
        finalcaluclation = dtotal + astamount;

//        double inamount = Double.parseDouble(stamount);
//        double dtax = Double.parseDouble(sttax);
//       // int intax = dtax;
//        int aa = Integer.parseInt(String.valueOf(dtax));
//        double total = inamount*dtax;
//
//        int taxcalculation = String.valueOf(total);
//        double fina = total+inamount;

        try {

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);

            BigDecimal b = BigDecimal.valueOf(finalcaluclation).setScale(0, RoundingMode.HALF_UP);
            paybleamount = String.valueOf(b);


            Log.e("paybleamount", "" + paybleamount);

            tv_payble_amount.setText(getString(R.string.Rs) + paybleamount);

            BigDecimal taxamount = BigDecimal.valueOf(dtotal).setScale(0, RoundingMode.HALF_EVEN);
            String finalamount = String.valueOf(taxamount);
            tv_total_amount.setText(getString(R.string.Rs) + df.format(dtotal));

            //  try {
            double value = astamount + dtotal;
            String result = String.valueOf(value);
            tvfamount.setText(getString(R.string.Rs) + df.format(value));

            Double roundoff = Double.parseDouble(paybleamount) - Double.parseDouble(result);
            //  BigDecimal roundamount = BigDecimal.valueOf(roundoff).setScale(0, RoundingMode.HALF_EVEN);
//              DecimalFormat df = new DecimalFormat();
//              df.setMaximumFractionDigits(2);
            //  System.out.println(df.format(roundoff));

            if (Double.parseDouble(paybleamount) >= Double.parseDouble(result)) {

                tvroundoffvalue.setText(getString(R.string.Rs) + "+" + String.valueOf(df.format(roundoff)));
                tvroundoffvalue.setTextColor(getColor(R.color.green));

            } else {

                tvroundoffvalue.setText(getString(R.string.Rs) + String.valueOf(df.format(roundoff)));
                tvroundoffvalue.setTextColor(getColor(R.color.red));


            }

        } catch (NumberFormatException ex) {

        }

        //  Log.e("Final amount", String.valueOf(Integer.parseInt(finalamount)+Integer.parseInt(stamount)));
        // Log.e("Roundoff amount", String.valueOf(Integer.parseInt(String.valueOf(finalcaluclation))-Integer.parseInt(String.valueOf(dtotal))));

        // tvfamount.setText(Integer.parseInt(finalamount)+Integer.parseInt(stamount));

        // tvroundoffvalue.setText(Integer.parseInt(String.valueOf(finalcaluclation))-Integer.parseInt(String.valueOf(dtotal)));

    }

    //    public void openoffer() {
//        if (!stcouponamount.isEmpty()) {
//            lloffer.setVisibility(View.VISIBLE);
//            double goldgrms = Double.parseDouble(stcouponamount);
//            double live = Double.parseDouble(liveprice);
//            double grms = (goldgrms / live);
//            BigDecimal b = BigDecimal.valueOf(grms).setScale(4, RoundingMode.HALF_EVEN);
//           String finalgold = String.valueOf(b);
//           tvoffer.setText(finalgold+"/grams");
//        }else {
//            lloffer.setVisibility(View.GONE);
//        }
//    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_payment)
    public void init_validation(View v) {

        btn_payment.setVisibility(View.GONE);
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

                Buy_Gold_Information.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if (!NetworkUtils.isConnected(Buy_Gold_Information.this)) {

                            btn_payment.setVisibility(View.VISIBLE);
                            loading_gif.setVisibility(View.GONE);

                            ToastMessage.onToast(Buy_Gold_Information.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                        } else {

                            referalCode_validation();

//                            if (paybleamount.equals(amountvalue)) {
//
//                                buy_wallet_money();
//
//                                // ToastMessage.onToast(Buy_Gold_Information.this,"Money Wallet",ToastMessage.SUCCESS);
//
//                            } else {
////                                if (!after_dedcation.isEmpty()){
////
////
////                                }
//                                if (isCheck) {
//                                    if (amountvalue.equals("null")) {
//                                        ToastMessage.onToast(Buy_Gold_Information.this, "Please enter amount", ToastMessage.ERROR);
//                                    } else {
//
//                                        if (after_dedcation.equals("null")) {
//
//                                            Log.e("payble amount if", paybleamount);
//                                        } else {
//                                            Log.e("payble amount else", paybleamount);
//
//                                            paybleamount = after_dedcation;
//
//                                        }
//
//                                        openpayment();
//                                    }
//                                }else {
//                                    openpayment();
//                                }
//                            }

                            btn_payment.setVisibility(View.VISIBLE);
                            loading_gif.setVisibility(View.GONE);
                        }

                    }

                });

            }

        }, 500);
    }

    public void referalCode_validation() {

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

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getdetails = apiDao.referCodeValidation("Bearer " + AccountUtils.getAccessToken(this), etreferalcode.getText().toString().trim());
            getdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode validation", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        Listmodel list = response.body();
                        if (list.isIs_referral()) {
                            openPayment();
                        }
                        // Log.e("Wallet Money Responce",new Gson().toJson(response.body()));


                        dialog.dismiss();

                    } else if (statuscode == 422) {

                        assert response.errorBody() != null;
                        JSONObject jObjError = null;
                        try {
                            jObjError = new JSONObject(response.errorBody().string());

                            String st = jObjError.getString("message");
                            //ToastMessage.onToast(this, st, ToastMessage.ERROR);
                            JSONObject er = jObjError.getJSONObject("errors");
                            try {
                                JSONArray array_mobile = er.getJSONArray("referralCode");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    tv_referal_code_error.setText(array_mobile.getString(i));

                                    //ToastMessage.onToast(Buy_Gold_Information.this,array_mobile.getString(i),ToastMessage.ERROR);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        dialog.dismiss();

                        // ToastMessage.onToast(Buy_Gold_Information.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                    //   ToastMessage.onToast(Buy_Gold_Information.this, "Technical issue", ToastMessage.ERROR);
                }
            });
        }

    }

    public void openPayment() {
        if (paybleamount.equals(amountvalue)) {

            buy_wallet_money();

            // ToastMessage.onToast(Buy_Gold_Information.this,"Money Wallet",ToastMessage.SUCCESS);

        } else {
//                                if (!after_dedcation.isEmpty()){
//
//
//                                }
            if (isCheck) {
                if (amountvalue.equals("null")) {
                    ToastMessage.onToast(Buy_Gold_Information.this, "Please enter amount", ToastMessage.ERROR);
                } else {

                    if (after_dedcation.equals("null")) {

                        Log.e("payble amount if", paybleamount);
                    } else {
                        Log.e("payble amount else", paybleamount);

                        paybleamount = after_dedcation;

                    }

                    openpayment();
                }
            } else {
                openpayment();
            }
        }

//        btn_payment.setVisibility(View.VISIBLE);
//        loading_gif.setVisibility(View.GONE);
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

            Log.e("bookingvalletdata", "" + stamount + "," + amountvalue + "," + stcouponcode);

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getdetails = apiDao.buywalletAmount("Bearer " + AccountUtils.getAccessToken(this), stamount, amountvalue, stcouponcode);
            getdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode dd", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        Listmodel list = response.body();
                        if (list.getPurchase_Status()) {
                            sttransactionid = list.getTransaction_Id();
                            stdescription = list.getDescription();
                            onsucess();
                        }
                        // Log.e("Wallet Money Responce",new Gson().toJson(response.body()));


                        dialog.dismiss();

                    } else {

                        dialog.dismiss();

                        // ToastMessage.onToast(Buy_Gold_Information.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                    onpaymenterror();

                    //   onsucess();
//                    ToastMessage.onToast(Buy_Gold_Information.this, "We have some issue", ToastMessage.ERROR);
                }
            });
//        }else {
//                apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//                Call<Listmodel> getdetails = apiDao.buywalletAmount("Bearer "+AccountUtils.getAccessToken(this),paybleamount,amountvalue,stcouponcode);
//                getdetails.enqueue(new Callback<Listmodel>() {
//                    @Override
//                    public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
//                        int statuscode  = response.code();
//                        Log.e("statuscode dd" , String.valueOf(statuscode));
//                        if (statuscode == HttpsURLConnection.HTTP_OK) {
//                            Listmodel list = response.body();
//                            Log.e("Wallet Money Responce",new Gson().toJson(response.body()));
//
//                            onsucess();
//
//                            dialog.dismiss();
//
//                        }else {
//
//                            dialog.dismiss();
//
//                            ToastMessage.onToast(Buy_Gold_Information.this,"Technical issue",ToastMessage.ERROR);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Listmodel> call, Throwable t) {
//                        Log.e("on fails",t.toString());
//                        dialog.dismiss();
//                        ToastMessage.onToast(Buy_Gold_Information.this,"We have some issue",ToastMessage.ERROR);
//                    }
//                });
//            }
//        }

        }
    }


    public void openpayment() {


        final Activity activity = this;
        final Checkout co = new Checkout();
       // co.setKeyID("rzp_test_0VM20Pg2VIA2aR");
        co.setKeyID("rzp_live_uvxtS5LwJPMIOP");

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


    @Override
    public void onPaymentSuccess(final String s) {
        onpaymentsucess(s);
        Log.e("ssssssssss", "" + s);

    }


    @Override
    public void onPaymentError(int i, String s) {
        Log.e("ssssssssss", "" + s);

        Log.e("calldata", "isscalldayapaymentcancel");
        tv_payble_amount.setText(getString(R.string.Rs) + paybleamount);
        amountvalue = "null";
        ToastMessage.onToast(this, "Payment cancelled", ToastMessage.ERROR);
    }

    public void onpaymenterror() {
        Log.e("calldata", "isscalldayaerror");

        Intent intent = new Intent(this, PaymentError.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        et_wallet_money.setText("");
        et_wallet_money.setVisibility(View.GONE);
        cb_wallet.setChecked(false);
        tv_wallet_error.setVisibility(View.GONE);
        setdata();


    }


    String CouponCode;

    public void onpaymentsucess(String paymentid) {
        Log.e("coupon", "" + paymentid);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {
//            pay_HHmZRtOO3HfaUT
            Log.e("coupon", "" + paymentid);
            dialog.dismiss();

            //if (stcouponcode.equals("null")) {
            Log.e("payamount", stamount);
            Log.e("coupon", stcouponcode);

            Log.e("Enterd Amount", amountvalue);

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> call = apiDao.get_purschasewithoutcoupon("Bearer " + AccountUtils.getAccessToken(this), stamount, paymentid, amountvalue, stcouponcode, etreferalcode.getText().toString().trim());
            call.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int stauscode = response.code();
                    Log.e("stauscode buy", String.valueOf(stauscode));
                    if (stauscode == HttpsURLConnection.HTTP_OK || stauscode == HttpsURLConnection.HTTP_CREATED) {
                        dialog.dismiss();
                        List<Listmodel> list = Collections.singletonList(response.body());
                        for (Listmodel listmodel : list) {
                            payment_status = listmodel.getPurchase_Status();
                            // Toast.makeText(Buy_Gold_Information.this, String.valueOf(payment_status), Toast.LENGTH_SHORT).show();
                            if (payment_status) {
                                sttransactionid = listmodel.getTransaction_Id();
                                stdescription = listmodel.getDescription();
                                onsucess();
                            } else {
                                onpaymenterror();
                            }

                        }

                    } else if (stauscode == 422) {
                        dialog.dismiss();

                        String responvce = new Gson().toJson(response.body());
                        Toast.makeText(Buy_Gold_Information.this, responvce, Toast.LENGTH_SHORT).show();

                        Log.e("ErrorResponce", responvce);
                        onpaymenterror();
                    } else {
                        dialog.dismiss();

                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            Toast.makeText(Buy_Gold_Information.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");
                            JSONArray array_paymentid = er.getJSONArray("payment_id");
                            for (int i = 0; i < array_paymentid.length(); i++) {

                                Toast.makeText(Buy_Gold_Information.this, array_paymentid.getString(i), Toast.LENGTH_SHORT).show();
                                onpaymenterror();
                            }
                            JSONArray array_amount = er.getJSONArray("amount");
                            for (int i = 0; i < array_amount.length(); i++) {
                                Toast.makeText(Buy_Gold_Information.this, array_amount.getString(i), Toast.LENGTH_SHORT).show();
                                onpaymenterror();
                            }

                            JSONArray array_referalcode = er.getJSONArray("amount");


                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    onpaymenterror();
                    Log.e("on buy fail", t.toString());
                    dialog.dismiss();

                }
            });
            //   } else {

//                Log.e("couponwithvalue", stcouponcode);
//                dialog.dismiss();
//
//                apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//                Call<Listmodel> call = apiDao.get_purschase("Bearer " + AccountUtils.getAccessToken(this), stamount, paymentid, stcouponcode,amountvalue);
//                call.enqueue(new Callback<Listmodel>() {
//                    @Override
//                    public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
//                        int stauscode = response.code();
//                        Log.e("stauscode buy", String.valueOf(stauscode));
//                        if (stauscode == HttpsURLConnection.HTTP_OK || stauscode == HttpsURLConnection.HTTP_CREATED) {
//                            dialog.dismiss();
//                            List<Listmodel> list = Collections.singletonList(response.body());
//                            for (Listmodel listmodel : list) {
//                                payment_status = listmodel.getPurchase_Status();
//                                //Toast.makeText(this, String.valueOf(payment_status), Toast.LENGTH_SHORT).show();
//                                if (payment_status) {
//                                    sttransactionid = listmodel.getTransaction_Id();
//                                    stdescription = listmodel.getDescription();
//                                    onsucess();
//                                } else {
//                                    onpaymenterror();
//                                }
//
//                            }
//
//                        } else {
//                            dialog.dismiss();
//                            onpaymenterror();
//                            try {
//                                assert response.errorBody() != null;
//                                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                String st = jObjError.getString("message");
//                                Toast.makeText(Buy_Gold_Information.this, st, Toast.LENGTH_SHORT).show();
//                                JSONObject er = jObjError.getJSONObject("errors");
//                                JSONArray array_paymentid = er.getJSONArray("payment_id");
//                                for (int i = 0; i < array_paymentid.length(); i++) {
//                                    Toast.makeText(Buy_Gold_Information.this, array_paymentid.getString(i), Toast.LENGTH_SHORT).show();
//                                }
//                                JSONArray array_amount = er.getJSONArray("amount");
//                                for (int i = 0; i < array_amount.length(); i++) {
//                                    Toast.makeText(Buy_Gold_Information.this, array_amount.getString(i), Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException | IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Listmodel> call, Throwable t) {
//                        onpaymenterror();
//                        Log.e("on buy fail", t.toString());
//                        dialog.dismiss();
//
//                    }
//                });

            //  }
        }

    }

    @Override
    public void responce(@NonNull Response<Listmodel> response, int stauscode) {
        Log.e("onreponsbuyfail", "" + stauscode);

    }

    @Override
    public void listresponce(@NonNull Response<List<Listmodel>> response, int stauscode) {
        Log.e("onlitrespobuyfail", "" + stauscode);

    }

    public void onsucess() {
        logBuyGoldEvent();

        Intent intent = new Intent(this, Successpopup.class);
        intent.putExtra("from", "Digitalgold");
        intent.putExtra("grams", stgold);
        intent.putExtra("amount", paybleamount);
        intent.putExtra("transctionid", sttransactionid);
        intent.putExtra("description", stdescription);
        startActivity(intent);

    }

    public void logBuyGoldEvent() {
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Buy Gold");
    }
}
