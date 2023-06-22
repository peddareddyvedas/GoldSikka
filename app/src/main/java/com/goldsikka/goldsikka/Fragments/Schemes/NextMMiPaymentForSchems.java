package com.goldsikka.goldsikka.Fragments.Schemes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.Activitys.LoginActivity;
import com.goldsikka.goldsikka.Activitys.PaymentError;
import com.goldsikka.goldsikka.Fragments.Successpopup;
import com.goldsikka.goldsikka.Models.SchemeModel;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NextMMiPaymentForSchems extends AppCompatActivity implements PaymentResultListener, View.OnClickListener {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_paybleamount)
    TextView tv_payble_amount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_totaltaxamount)
    TextView tv_totaltax_amount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etreferralcode)
    EditText etreferalcode;
//
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.tv_tax)
//    TextView tvtax;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_quantity)
    TextView tvcustomer_gold;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_amount)
    TextView tvemiamount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvliverate)
    TextView tvliverate;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tv_second_title)
    TextView tv_second_title;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_payment)
    Button btn_payment;

    CountDownTimer countDownTimer;
    private boolean timerRunning;
    String stid, stliverate, stmsg, stgstamount, stamount, stgrms, stfinalamount, stschemetype;

    ApiDao apiDao;

    String after_dedcation = "null";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_wallet_money)
    TextView tvwalletamount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.wallet_check)
    CheckBox cb_wallet;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_wallet_money)
    EditText et_wallet_money;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_wallet_error)
    TextView tv_wallet_error;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_referal_code_error)
    TextView tv_referal_code_error;

    String Walletamount;

    String amountvalue = "null";

    TextWatcher textWatcher;

    boolean ischeck = false;

    TextView unameTv, uidTv, titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_m_mi_payment_for_schems);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Details");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Details");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            stid = bundle.getString("id");
            stschemetype = bundle.getString("schemetype");
        }
        getdetails();
        init_timer();
        wallet_amount();
        dediactWalletMoney();

        et_wallet_money.setHint(Html.fromHtml(getString(R.string.wallet_hint)));


        cb_wallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                 @Override
                                                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                     if (isChecked) {
                                                         ischeck = true;
                                                         et_wallet_money.setVisibility(View.VISIBLE);
                                                         et_wallet_money.setText("");
                                                     } else {
                                                         btn_payment.setEnabled(true);
                                                         ischeck = false;
                                                         et_wallet_money.setVisibility(View.GONE);
                                                         tv_wallet_error.setVisibility(View.GONE);
                                                         getdetails();
                                                         // tv_payble_amount.setText(paybleamount);
                                                     }
                                                 }
                                             }
        );

        btn_payment.setOnClickListener(this);
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

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void afterTextChanged(Editable charSequence) {

                if (charSequence != null && !charSequence.toString().equalsIgnoreCase("")) {
                    if (et_wallet_money.getText().hashCode() == charSequence.hashCode()) {

                        amountvalue = et_wallet_money.getText().toString();
                        tv_wallet_error.setVisibility(View.VISIBLE);

                        try {

                            if (Integer.parseInt(stfinalamount) >= Integer.parseInt(amountvalue)) {

                                // tv_wallet_error.setText("Money less then");
                                tv_wallet_error.setVisibility(View.GONE);

                                int minus = Integer.parseInt(stfinalamount) - Integer.parseInt(amountvalue);

                                after_dedcation = String.valueOf(minus);

                                // paybleamount = after_dedcation;

                                tv_payble_amount.setText(getString(R.string.Rs) + after_dedcation);
                                btn_payment.setEnabled(true);

                                double entervalue = Double.parseDouble(amountvalue);
                                double walletvalue = Double.parseDouble(Walletamount);

                                if (entervalue > walletvalue) {

                                    tv_wallet_error.setVisibility(View.VISIBLE);
                                    btn_payment.setEnabled(false);
                                    btn_payment.setBackgroundResource(R.drawable.backgroundvisablity);

                                    tv_wallet_error.setText("You can't enter more then " + Walletamount);
                                    tv_wallet_error.setTextColor(getColor(R.color.red));

                                }

                            } else {

                                tv_payble_amount.setText("0");
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

                    tv_payble_amount.setText(getString(R.string.Rs) + stfinalamount);
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

                    Log.e("statuscode wallet", String.valueOf(statuscode));

                    if (statuscode == HttpsURLConnection.HTTP_OK) {

                        Listmodel list = response.body();

                        Walletamount = list.getAmount_wallet();

                        tvwalletamount.setText("Available amount : " + getString(R.string.Rs) + " " + Walletamount);

                        dialog.dismiss();

                    } else {

                        dialog.dismiss();

                      //  ToastMessage.onToast(NextMMiPaymentForSchems.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {

                    Log.e("on fails", t.toString());

                    dialog.dismiss();

                    // ToastMessage.onToast(NextMMiPaymentForSchems.this, "We have some issue", ToastMessage.ERROR);
                }
            });
        }
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
                Intent intent = new Intent(NextMMiPaymentForSchems.this, MainFragmentActivity.class);
                startActivity(intent);

                // tvsendagain.setVisibility(View.VISIBLE);
            }
        }.start();
        timerRunning = true;

    }


    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        timerRunning = false;
        NavUtils.navigateUpFromSameTask(this);

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            countDownTimer.cancel();
            timerRunning = false;
            onBackPressed();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getdetails() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

        Call<SchemeModel> payinfo = apiDao.nextpaymentmmi("Bearer " + AccountUtils.getAccessToken(this), stid);
        payinfo.enqueue(new Callback<SchemeModel>() {
            @Override
            public void onResponse(Call<SchemeModel> call, Response<SchemeModel> response) {
                int statuscode = response.code();
                SchemeModel model = response.body();
                Log.e("statuscode", String.valueOf(statuscode));
                Log.e("statuscode", String.valueOf(stid));
                if (statuscode == HttpsURLConnection.HTTP_OK) {
                    dialog.dismiss();
                    stgrms = model.getGramsEmi();
                    stliverate = model.getLivePrice();
                    stgstamount = model.getGst();
                    stamount = model.getEmiAmount();
                    stfinalamount = model.getFinalAmount();

                    tv_payble_amount.setText(getString(R.string.Rs) + stfinalamount);
                    tv_totaltax_amount.setText(getString(R.string.Rs) + stgstamount);
                    tvliverate.setText(getString(R.string.Rs) + stliverate);
                    tvcustomer_gold.setText(stgrms);
                    tvemiamount.setText(getString(R.string.Rs) + stamount);

                    Log.e("statuscode", stfinalamount);

                } else if (statuscode == HttpsURLConnection.HTTP_BAD_REQUEST) {
                    dialog.dismiss();
                    assert response.errorBody() != null;

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String st = jObjError.getString("message");
                        popup(st);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    dialog.dismiss();
                    ToastMessage.onToast(NextMMiPaymentForSchems.this, "Try After some Time",
                            ToastMessage.ERROR);
                }
            }

            @Override
            public void onFailure(Call<SchemeModel> call, Throwable t) {
                dialog.dismiss();
                //  ToastMessage.onToast(NextMMiPaymentForSchems.this, "We have some issues try after some time",
                //      ToastMessage.ERROR);
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
                Intent intent = new Intent(NextMMiPaymentForSchems.this, Schemes_usersubscribed_list.class);
                Schemes_usersubscribed_list.isFromgoldtransaction = false;

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        enquiryalertdialog.show();

    }

    public void nextpay() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        Log.e("liverate", stliverate);

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<List<Listmodel>> lockrates = apiDao.getlock_rates(stliverate, "Bearer " + AccountUtils.getAccessToken(this));
        lockrates.enqueue(new Callback<List<Listmodel>>() {
            @Override
            public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("statuscode Lock", String.valueOf(statuscode));
                if (statuscode == HttpsURLConnection.HTTP_ACCEPTED) {
                    dialog.dismiss();
                    referalCode_validation();

                } else {
                    dialog.dismiss();

                    ToastMessage.onToast(NextMMiPaymentForSchems.this, "Try After some Time", ToastMessage.ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                Log.e("lockratesfail", t.toString());
                dialog.dismiss();
                // ToastMessage.onToast(NextMMiPaymentForSchems.this, "We have some issues Try After some Time", ToastMessage.ERROR);

            }
        });

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
                    Log.e("statuscode ref", String.valueOf(statuscode));
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
                    //ToastMessage.onToast(NextMMiPaymentForSchems.this, "Technical issue", ToastMessage.ERROR);
                }
            });
        }

    }

    public void openPayment() {

        if (stfinalamount.equals(amountvalue)) {

            buy_wallet_money();

            // ToastMessage.onToast(Buy_Gold_Information.this,"Money Wallet",ToastMessage.SUCCESS);

        } else {

            if (ischeck) {

                if (amountvalue.equals("null")) {
                    ToastMessage.onToast(NextMMiPaymentForSchems.this, "Please enter amount", ToastMessage.ERROR);
                } else {

                    if (after_dedcation.equals("null")) {

                        Log.e("payble amount if", stfinalamount);

                    } else {
                        Log.e("payble amount else", stfinalamount);

                        stfinalamount = after_dedcation;

                    }

                    onpayment();
                }
            } else {
                onpayment();
            }
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

            Log.e("MMI ID", stid);
            Log.e("LiveRate", stliverate);
            Log.e("walletAmount", amountvalue);

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<SchemeModel> getdetails = apiDao.buywalletAmount_mmipayment("Bearer " + AccountUtils.getAccessToken(this), stid, stliverate, amountvalue);
            getdetails.enqueue(new Callback<SchemeModel>() {
                @Override
                public void onResponse(Call<SchemeModel> call, Response<SchemeModel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode buy", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {
                        SchemeModel list = response.body();
                        boolean payment_status = list.isPurchase_Status();
                        if (payment_status) {
                            stmsg = list.getDescription();
                            openpopupscreen();
                        } else {
                            onpaymenterror();
                            ToastMessage.onToast(NextMMiPaymentForSchems.this, "Payment is Failed", ToastMessage.ERROR);
                        }
                        // Log.e("Wallet Money Responce",new Gson().toJson(response.body()));
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        //ToastMessage.onToast(NextMMiPaymentForSchems.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<SchemeModel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                    //  ToastMessage.onToast(NextMMiPaymentForSchems.this, "We have some issue", ToastMessage.ERROR);
                }
            });
        }
    }

    public void onpayment() {

        final Activity activity = this;
        final Checkout co = new Checkout();

       co.setKeyID("rzp_test_0VM20Pg2VIA2aR");
     //   co.setKeyID("rzp_live_uvxtS5LwJPMIOP");

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("payment_capture", 1);
            // double total = Double.parseDouble("1");
            double total = Double.parseDouble(stfinalamount);
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
    public void onPaymentSuccess(String s) {
        onpaymentsucess(s);
        Log.e("paymentid", s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        ToastMessage.onToast(this, "Payment failed", ToastMessage.ERROR);
        amountvalue = "null";
        cb_wallet.setChecked(false);
        // onpaymenterror();
    }

    public void onpaymenterror() {

        Intent intent = new Intent(this, PaymentError.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onpaymentsucess(String paymentid) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {
            Log.e("mmi id", stid);
            Log.e("liveprice", stliverate);
            Log.e("paymentid", paymentid);
            Log.e("amountvalue", amountvalue);
            Log.e("referralcode", etreferalcode.getText().toString().trim());
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<SchemeModel> call = apiDao.nextpaymentmmisucccess("Bearer " + AccountUtils.getAccessToken(this), stid, stliverate, paymentid, amountvalue, etreferalcode.getText().toString().trim());
            call.enqueue(new Callback<SchemeModel>() {
                @Override
                public void onResponse(Call<SchemeModel> call, Response<SchemeModel> response) {
                    int stauscode = response.code();
                    Log.e("staus buy", String.valueOf(stauscode));
                    if (stauscode == HttpsURLConnection.HTTP_OK || stauscode == HttpsURLConnection.HTTP_CREATED) {
                        dialog.dismiss();
                        SchemeModel listmodel = response.body();
                        boolean payment_status = listmodel.isPurchase_Status();
                        if (payment_status) {
                            stmsg = listmodel.getDescription();
                            openpopupscreen();
                        } else {
                            onpaymenterror();
                            ToastMessage.onToast(NextMMiPaymentForSchems.this, "Payment is Failed", ToastMessage.ERROR);
                        }


                    } else {
                        dialog.dismiss();
                        onpaymenterror();
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
//                            Toast.makeText(NextMMiPaymentForSchems.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");
                            JSONArray array_paymentid = er.getJSONArray("payment_id");
                            for (int i = 0; i < array_paymentid.length(); i++) {
//                                Toast.makeText(NextMMiPaymentForSchems.this, array_paymentid.getString(i), Toast.LENGTH_SHORT).show();
                            }
                            JSONArray array_amount = er.getJSONArray("amount");
                            for (int i = 0; i < array_amount.length(); i++) {
//                                Toast.makeText(NextMMiPaymentForSchems.this, array_amount.getString(i), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SchemeModel> call, Throwable t) {
                    onpaymenterror();
                    Log.e("on buy fail", t.toString());
                    dialog.dismiss();

                }
            });

        }
    }

    @SuppressLint("NewApi")
    public void openpopupscreen() {

        Intent intent = new Intent(this, Successpopup.class);
        intent.putExtra("from", "nextmmi");
        intent.putExtra("description", stmsg);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_payment) {
            if (stschemetype.equals("MG")) {
                nextpay();
            } else if (stschemetype.equals("JW")) {
                if (stfinalamount.equals(amountvalue)) {

                    buy_wallet_money();

                    // ToastMessage.onToast(Buy_Gold_Information.this,"Money Wallet",ToastMessage.SUCCESS);

                } else {

                    if (ischeck) {

                        if (amountvalue.equals("null")) {
                            ToastMessage.onToast(NextMMiPaymentForSchems.this, "Please enter amount", ToastMessage.ERROR);
                        } else {

                            if (after_dedcation.equals("null")) {

                                Log.e("payble amount if", stfinalamount);

                            } else {
                                Log.e("payble amount else", stfinalamount);

                                stfinalamount = after_dedcation;

                            }

                            onpayment();
                        }
                    } else {
                        onpayment();
                    }
                }
            }
        }

    }
}