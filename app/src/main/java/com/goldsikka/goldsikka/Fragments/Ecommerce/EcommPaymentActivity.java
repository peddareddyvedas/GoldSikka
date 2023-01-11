package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.goldsikka.goldsikka.Activitys.BaseActivity;
import com.goldsikka.goldsikka.Activitys.LoginActivity;
import com.goldsikka.goldsikka.Activitys.PaymentError;
import com.goldsikka.goldsikka.Fragments.baseinterface;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.CheckoutModel;
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

public class EcommPaymentActivity extends BaseActivity implements baseinterface, PaymentResultListener {

    String stamount = " ", taxPercentage, liveprice, stgold, status, noofproducts, pid;
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
    @BindView(R.id.tv_wallet_money)
    TextView tvwalletamount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_wallet_money)
    EditText et_wallet_money;


    RelativeLayout backbtn;

    TextWatcher textWatcher;

    String Walletamount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.wallet_check)
    CheckBox cb_wallet;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_wallet_error)
    TextView tv_wallet_error;

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
    String amountvalue = "0";
    boolean isCheck = false;
    TextView unameTv, uidTv, titleTv;
    String productscount;


    @Override
    protected int getLayoutId() {
        return R.layout.ecomm_payment_activity;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        ButterKnife.bind(this);
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
        titleTv.setText("Payment");
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            stamount = bundle.getString("ecommtotalprice");
            liveprice = bundle.getString("liveprice");
            noofproducts = bundle.getString("no_of_products");
            pid = bundle.getString("pid");

            status = bundle.getString("status");
            stgold = bundle.getString("customer_gold");
            stCoupongold = bundle.getString("Coupongold");
            stcouponcode = bundle.getString("couponcode");
            isCoupon = bundle.getBoolean("isCoupon");
            taxPercentage = bundle.getString("taxPercentage");
            productscount = bundle.getString("productscount");

        }


        setdata();
        wallet_amount();
        init_timer();



        et_wallet_money.setHint(Html.fromHtml(getString(R.string.wallet_hint)));

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
                                                     }
                                                 }
                                             }
        );
    }

    public void dediactWalletMoney() {

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void afterTextChanged(Editable charSequence) {

                if (charSequence != null && !charSequence.toString().equalsIgnoreCase("")) {
                    if (et_wallet_money.getText().hashCode() == charSequence.hashCode()) {
                        amountvalue = et_wallet_money.getText().toString();
                        tv_wallet_error.setVisibility(View.VISIBLE);

                        try {
                            if (Integer.parseInt(paybleamount) >= Integer.parseInt(amountvalue)) {

                                // tv_wallet_error.setText("Money less then");
                                tv_wallet_error.setVisibility(View.GONE);

                                int minus = Integer.parseInt(paybleamount) - Integer.parseInt(amountvalue);

                                after_dedcation = String.valueOf(minus);


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

                                if (entervalue > walletvalue) {*/

                                    tv_wallet_error.setVisibility(View.VISIBLE);
                                    btn_payment.setEnabled(false);
                                    btn_payment.setBackgroundResource(R.drawable.backgroundvisablity);

                                    tv_wallet_error.setText("You can't enter more then " + Walletamount);
                                    tv_wallet_error.setTextColor(getColor(R.color.red));

                                }

                            } else {

                                tv_payble_amount.setText(getString(R.string.Rs) + "0");
                                btn_payment.setEnabled(false);

                                btn_payment.setBackgroundResource(R.drawable.backgroundvisablity);

                                tv_wallet_error.setText("You can't enter more than Payable Amount");
                                tv_wallet_error.setTextColor(getColor(R.color.red));

                            }

                            et_wallet_money.removeTextChangedListener(textWatcher);
                            et_wallet_money.addTextChangedListener(textWatcher);
                        } catch (NumberFormatException e) {

                        }
                    }
                } else {

                    tv_payble_amount.setText(getString(R.string.Rs) + paybleamount);
                    tv_wallet_error.setText("");
                    amountvalue = " ";

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

                        //ToastMessage.onToast(EcommPaymentActivity.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {

                    Log.e("on fails", t.toString());

                    dialog.dismiss();

                    ToastMessage.onToast(EcommPaymentActivity.this, "We have some issue", ToastMessage.ERROR);
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
        //  finish();
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
                Intent intent = new Intent(EcommPaymentActivity.this, EcommCheckout_Activity.class);
                startActivity(intent);

                // tvsendagain.setVisibility(View.VISIBLE);
            }
        }.start();
        timerRunning = true;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    public void setdata() {

        double astamount = Double.parseDouble(stamount);
        finalcaluclation = astamount;
        double dtotal = astamount;


        try {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            BigDecimal b = BigDecimal.valueOf(finalcaluclation).setScale(0, RoundingMode.HALF_UP);
            paybleamount = String.valueOf(b);
            tv_payble_amount.setText(getString(R.string.Rs) + paybleamount);
            Log.e("paybleamount", "" + paybleamount);
        } catch (NumberFormatException ex) {

        }


    }


    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_payment)
    public void init_validation(View v) {

        btn_payment.setVisibility(View.GONE);
        loading_gif.setVisibility(View.VISIBLE);
       /* Intent intent = new Intent(this, EcommMyOrdersActivity.class);
        startActivity(intent);*/

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if (!NetworkUtils.isConnected(EcommPaymentActivity.this)) {
                            btn_payment.setVisibility(View.VISIBLE);
                            loading_gif.setVisibility(View.GONE);
                            ToastMessage.onToast(EcommPaymentActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                        } else {
                            openPayment();
                            btn_payment.setVisibility(View.VISIBLE);
                            loading_gif.setVisibility(View.GONE);
                        }

                    }

                });

            }

        }, 500);
    }


    public void openPayment() {
        if (paybleamount.equals(amountvalue)) {
            buy_wallet_money();
        } else {
            if (isCheck) {
                if (amountvalue.equals("null")) {
                    ToastMessage.onToast(EcommPaymentActivity.this, "Please enter amount", ToastMessage.ERROR);
                } else {

                    if (after_dedcation.equals("null")) {

                        Log.e("paybleamountif", paybleamount);
                    } else {
                        Log.e("paybleamountelse", paybleamount);
                        paybleamount = after_dedcation;
                    }
                    openpayment();
                }
            } else {
                openpayment();
            }
        }
        if (isCheck) {
            if (amountvalue.equals("null")) {
                ToastMessage.onToast(EcommPaymentActivity.this, "Please enter amount", ToastMessage.ERROR);
            } else {

                if (after_dedcation.equals("null")) {

                    Log.e("payble amount if", paybleamount);
                } else {
                    Log.e("payble amount else", paybleamount);
                    paybleamount = after_dedcation;
                }
                //  openpayment();
            }
        } else {
            //openpayment();
        }
    }

    public void buy_wallet_money() {
        String s = productscount;
        int i = Integer.parseInt(s.substring(0, s.length() - 2));
        String jjkgj = String.valueOf(i);
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
            Call<CheckoutModel> getdetails = apiDao.postcheckoutmoneywallet("Bearer " + AccountUtils.getAccessToken(this), pid, liveprice, jjkgj, amountvalue,"","","","");
            getdetails.enqueue(new Callback<CheckoutModel>() {
                @Override
                public void onResponse(Call<CheckoutModel> call, Response<CheckoutModel> response) {
                    int statuscode = response.code();
                    Log.e("walletstatuscodedd", String.valueOf(statuscode));

                    if (statuscode == 200 || statuscode == 202) {
                        dialog.dismiss();

                        Log.e("walletstatuscodeddss", String.valueOf(statuscode));
                        CheckoutModel list = response.body();

                        if (list.isProcessed()) {
                            Log.e("walletstatuscodeddss", "" + list.getMessage());
                            Log.e("walletstatuscodeddss", "" + list.isProcessed());
                            stdescription = list.getMessage();
                            onsucess();
                        }


                    } else if (statuscode == 422) {
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    } else {
                        dialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<CheckoutModel> call, Throwable t) {
                    Log.e("onfails", t.toString());
                    dialog.dismiss();
                    ToastMessage.onToast(getApplicationContext(), "We have some issue", ToastMessage.ERROR);
                }
            });


        }
    }


    public void openpayment() {

        final Activity activity = this;
        final Checkout co = new Checkout();
      //  co.setKeyID("rzp_test_0VM20Pg2VIA2aR");
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
        try {
            Toast.makeText(getApplicationContext(), "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
            onpaymentsucess(s);

            Log.e("paymentID", s);
        } catch (Exception e) {
            Log.e("com.merchant", e.getMessage(), e);
        }

    }

    public void onpaymenterror() {

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

    @Override
    public void onPaymentError(int i, String s) {
        tv_payble_amount.setText(getString(R.string.Rs) + paybleamount);
//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        amountvalue = "";
        ToastMessage.onToast(this, "Payment cancelled", ToastMessage.ERROR);
    }

    String CouponCode;

    public void onpaymentsucess(String paymentid) {
        Log.e("sendfyfftv", "" + productscount.substring(0, productscount.length() - 2));


        String s = productscount;
        int i = Integer.parseInt(s.substring(0, s.length() - 2));
        Log.e("sendfyffintttv", "" + i);
        String jjkgj = String.valueOf(i);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {

            dialog.dismiss();

            Log.e("payamount", "" + pid + "," + liveprice + "," + jjkgj + "," + paymentid + "," + paybleamount + "," + amountvalue);

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            //   Call<Listmodel> call = apiDao.postcheckout("Bearer " + AccountUtils.getAccessToken(this), "9", "4100", "1", "pay_JtAZjARQGTZG9K", "11781", "");

            Call<CheckoutModel> call = apiDao.postcheckout("Bearer " + AccountUtils.getAccessToken(this), pid, liveprice, jjkgj, paymentid, paybleamount, amountvalue,"","","","");
            call.enqueue(new Callback<CheckoutModel>() {
                @Override
                public void onResponse(Call<CheckoutModel> call, Response<CheckoutModel> response) {
                    dialog.dismiss();
                    int stauscode = response.code();
                    Log.e("stauscodebuy", String.valueOf(stauscode));

                    if (stauscode == 200 || stauscode == 202) {

                        dialog.dismiss();
                        Log.e("walletstatuscodeddss", String.valueOf(stauscode));
                        CheckoutModel list = response.body();
                        if (list.isProcessed()) {
                            Log.e("walletstatuscodeddss", "" + list.getMessage());
                            Log.e("walletstatuscodeddss", "" + list.isProcessed());
                            stdescription = list.getMessage();
                            onsucess();
                        }




                       /* List<CheckoutModel> list = Collections.singletonList(response.body());
                        for (CheckoutModel listmodel : list) {
                            payment_status = listmodel.isProcessed();
                            // Toast.makeText(Buy_Gold_Information.this, String.valueOf(payment_status), Toast.LENGTH_SHORT).show();
                            if (payment_status) {
                                // sttransactionid = listmodel.getTransaction_Id();
                                stdescription = listmodel.getMessage();
                                onsucess();
                            } else {
                                onpaymenterror();
                            }

                        }*/

                    } else if (stauscode == 422) {
                        dialog.dismiss();

                        String responvce = new Gson().toJson(response.body());
                        Log.e("Error Responce", responvce);
                        onpaymenterror();
                    } else {
                        dialog.dismiss();

                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            Toast.makeText(EcommPaymentActivity.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");
                            JSONArray array_paymentid = er.getJSONArray("payment_id");
                            for (int i = 0; i < array_paymentid.length(); i++) {

                                Toast.makeText(EcommPaymentActivity.this, array_paymentid.getString(i), Toast.LENGTH_SHORT).show();
                                onpaymenterror();
                            }
                            JSONArray array_amount = er.getJSONArray("amount");
                            for (int i = 0; i < array_amount.length(); i++) {
                                Toast.makeText(EcommPaymentActivity.this, array_amount.getString(i), Toast.LENGTH_SHORT).show();
                                onpaymenterror();
                            }

                            JSONArray array_referalcode = er.getJSONArray("amount");


                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CheckoutModel> call, Throwable t) {
                    onpaymenterror();
                    Log.e("on buy fail", t.toString());
                    dialog.dismiss();

                }
            });

        }

    }

    @Override
    public void responce(@NonNull Response<Listmodel> response, int stauscode) {

    }

    @Override
    public void listresponce(@NonNull Response<List<Listmodel>> response, int stauscode) {

    }


    public void onsucess() {

        Intent intent = new Intent(this, EcommSuccessPopup.class);
        intent.putExtra("description", stdescription);
        startActivity(intent);

    }


}

