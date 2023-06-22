package com.goldsikka.goldsikka.Fragments;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.goldsikka.goldsikka.Activitys.PaymentError;
import com.goldsikka.goldsikka.Activitys.Profile.CustomerAddAddress;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
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

public class Reedem_fragment extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {
    Button next;
    EditText et_grams;
    // private this this;
    TextWatcher textWatcher;
    ApiDao apiDao;
    TextView tv_grmserror, tv_amount;
    double redeem, fredeem;
    String st_grams;
    String liveprice, finalamount, sttransactionid, stdescription;
    TextView wallet_gold;
    String st_wallet_gold;
    GifImageView loading_gif;
    double grams;
    BigDecimal b21;
    CountDownTimer countDownTimer;
    private boolean timerRunning;

    TextView tv_second_title;

    TextView ftax1, famount1, pamount1;
    TextView ftaxTv, famountTv, tvtwograms2;
    TextView walletamountTv;
    String walletamount;
    RelativeLayout mw;

    CheckBox moneycheck;
    boolean payment_status;
    RelativeLayout backbtn;


    TextView unameTv, uidTv, titleTv;

    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeem_design);

        ftax1 = findViewById(R.id.ftax);
        famount1 = findViewById(R.id.famount);
        pamount1 = findViewById(R.id.pamount);

        ftaxTv = findViewById(R.id.ftaxTv);
        famountTv = findViewById(R.id.famountTv);
        tvtwograms2 = findViewById(R.id.tvtwograms2);

        walletamountTv = findViewById(R.id.walletamountTv);
        mw = findViewById(R.id.moneywalletRL);

        moneycheck = findViewById(R.id.moneycheck);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            st_wallet_gold = bundle.getString("wallet");
        }

        st_wallet_gold = AccountUtils.getWalletAmount(Reedem_fragment.this);

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Redeem Gold");
        tv_second_title = findViewById(R.id.tv_second_title);
        initlizeviews();
        init_timer();

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
                Intent intent = new Intent(Reedem_fragment.this, MainFragmentActivity.class);
                startActivity(intent);
                // tvsendagain.setVisibility(View.VISIBLE);
            }
        }.start();
        timerRunning = true;
    }
//    @Override
//    protected void onPause() {
//
//        countDownTimer.cancel();
//        timerRunning = false;
//        super.onPause();
//    }

    @Override
    protected void onStart() {
        super.onStart();
        getliveprices();
    }

    public void getliveprices() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            dialog.dismiss();
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
                            Toast.makeText(Reedem_fragment.this, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(Reedem_fragment.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        countDownTimer.cancel();
        timerRunning = false;
        super.onBackPressed();
        finish();
    }

    public void initlizeviews() {

        et_grams = findViewById(R.id.et_grams);

        et_grams.setHint(Html.fromHtml(getString(R.string.grams)));

        st_grams = et_grams.getText().toString().trim();

        tv_grmserror = findViewById(R.id.tv_grmserror);
        tv_amount = findViewById(R.id.tv_amount);
        tv_amount.setVisibility(View.GONE);

        loading_gif = findViewById(R.id.loading_gif);
//        wallet_amount = view.findViewById(R.id.wallet_amount);
        wallet_gold = findViewById(R.id.tv_wallet_gold);
//        wallet_amount.setText(st_wallet_amount);
        wallet_gold.setText(st_wallet_gold + " grams");

        next = findViewById(R.id.btn_redeemnext);
        next.setOnClickListener(this);


        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("ResourceType")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                final String tax1 = "25";
                final String tax2 = "20";
                final String tax3 = "18";

                if (charSequence != null && !charSequence.toString().equalsIgnoreCase("")) {

                    if (et_grams.getText().hashCode() == charSequence.hashCode()) {
                        try {
                            st_grams = et_grams.getText().toString().trim();

                            double goldgrams = Double.parseDouble(st_grams);
                            grams = Double.parseDouble(liveprice) * goldgrams;
                            Log.e("liveprice", String.valueOf(grams));
                            BigDecimal b21 = BigDecimal.valueOf(grams).setScale(2, RoundingMode.HALF_EVEN);
                            finalamount = new String(String.valueOf(b21));


                            if (Double.parseDouble(st_grams) < 2) {
                                mw.setVisibility(View.VISIBLE);
                                tv_amount.setVisibility(View.GONE);
                                tv_grmserror.setVisibility(View.VISIBLE);
                                if (Double.parseDouble(st_grams) == 0.5) {
                                    mw.setVisibility(View.VISIBLE);
                                    tv_grmserror.setVisibility(View.GONE);
                                    String ftax = redeemtax(liveprice, tax1);
                                    redeemuiviewactive(grams, ftax);
                                } else if (Double.parseDouble(st_grams) == 1) {
                                    mw.setVisibility(View.VISIBLE);
                                    tv_grmserror.setVisibility(View.GONE);
                                    String ftax = redeemtax(liveprice, tax2);
                                    redeemuiviewactive(grams, ftax);
                                } else if (Double.parseDouble(st_grams) == 1.5) {
                                    mw.setVisibility(View.VISIBLE);
                                    tv_grmserror.setVisibility(View.GONE);
                                    String ftax = redeemtax(liveprice, tax3);
                                    redeemuiviewactive(grams, ftax);
                                } else if (Double.parseDouble(st_grams) >= 0 && Double.parseDouble(st_grams) < 0.5) {
                                    tv_grmserror.setVisibility(View.VISIBLE);
                                    mw.setVisibility(View.VISIBLE);
                                    tv_grmserror.setText("Grams must be atleast 0.5");

                                } else if (Double.parseDouble(st_grams) > 0.5 && Double.parseDouble(st_grams) < 1) {
                                    tv_grmserror.setVisibility(View.VISIBLE);
                                    mw.setVisibility(View.VISIBLE);
                                    tv_grmserror.setText("Grams must be atleast 0.5 or 1 Gm");
                                } else if (Double.parseDouble(st_grams) > 1 && Double.parseDouble(st_grams) < 1.5) {
                                    tv_grmserror.setVisibility(View.VISIBLE);
                                    mw.setVisibility(View.VISIBLE);
                                    tv_grmserror.setText("Grams must be atleast 1 or 1.5 Gms");
                                } else if (Double.parseDouble(st_grams) > 1.5 && Double.parseDouble(st_grams) < 2) {
                                    tv_grmserror.setVisibility(View.VISIBLE);
                                    mw.setVisibility(View.VISIBLE);
                                    tv_grmserror.setText("Grams must be atleast 1.5 or 2 Gms");
                                }
                            } else if (Double.parseDouble(st_grams) >= 2 && Double.parseDouble(st_grams) <= 30) {
                                mw.setVisibility(View.GONE);
                                moneycheck.setChecked(false);
                                if (Double.parseDouble(st_grams) % 1 != 0 && Double.parseDouble(st_grams) > 2) {
                                    tv_grmserror.setVisibility(View.VISIBLE);
                                    tv_grmserror.setText("Decimal Values are not allowed");
                                } else {
                                    redeemuiviewactive(grams, "0");
                                }
                            } else {
                                mw.setVisibility(View.GONE);
                                moneycheck.setChecked(false);
                                tv_grmserror.setVisibility(View.VISIBLE);
                                tv_grmserror.setText("Please Enter max 30 gms only");

                            }
//

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    tv_amount.setText(null);
                    tv_grmserror.setVisibility(View.GONE);
                    ftaxTv.setVisibility(View.GONE);
                    ftax1.setVisibility(View.GONE);
                    famountTv.setVisibility(View.GONE);
                    famount1.setVisibility(View.GONE);
                    tvtwograms2.setVisibility(View.GONE);
                    pamount1.setVisibility(View.GONE);
                    mw.setVisibility(View.GONE);
                    moneycheck.setChecked(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        et_grams.addTextChangedListener(textWatcher);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_redeemnext:
                if (!NetworkUtils.isConnected(this)) {
                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                } else {
                    init_validation(view);
                }
                return;
        }
    }

    public String redeemtax(String liveprice, String tax) {
        Double lv = null;
        if (tax.equals("25"))
            lv = Double.parseDouble(liveprice) / 2;
        else if (tax.equals("20"))
            lv = Double.parseDouble(liveprice);
        else if (tax.equals("18"))
            lv = Double.parseDouble(liveprice) + (Double.parseDouble(liveprice) / 2);

        redeem = lv * (Double.parseDouble(tax) / 100);
        Log.e("redeem", String.valueOf(redeem));

        fredeem = ((redeem * 0) / 100) + redeem - 0.005;
        Log.e("fredeem", String.valueOf(fredeem));
        BigDecimal bellow4grms = BigDecimal.valueOf(fredeem).setScale(2, RoundingMode.HALF_EVEN);
        return bellow4grms.toPlainString();
    }

    public void redeemuiviewactive(Double grams, String ftax) {
        ftaxTv.setVisibility(View.VISIBLE);
        ftax1.setVisibility(View.VISIBLE);
        famount1.setVisibility(View.VISIBLE);
        famountTv.setVisibility(View.VISIBLE);
        ftax1.setText(String.format("₹ %s", ftax));
        Double famount = grams + Double.parseDouble(ftax);
        famount1.setText(String.format("₹ %s", Math.round(famount) + ".00"));
        tvtwograms2.setVisibility(View.VISIBLE);
        pamount1.setVisibility(View.VISIBLE);
        pamount1.setText(ftax);
        walletamount();
    }

    public void walletamount() {
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Listmodel> getdetails = apiDao.walletAmount("Bearer " + AccountUtils.getAccessToken(this));
        getdetails.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                Log.e("statuscodedd", String.valueOf(statuscode));
                if (statuscode == HttpsURLConnection.HTTP_OK) {
                    Listmodel list = response.body();
                    String Walletamount = list.getAmount_wallet();
                    walletamount = Walletamount;
                    walletamountTv.setText(String.format("%s %s", getString(R.string.Rs), Walletamount));

                } else {
                    // ToastMessage.onToast(getApplicationContext(),"Technical issue",ToastMessage.ERROR);
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                Log.e("on fails", t.toString());
                ToastMessage.onToast(getApplicationContext(), "We have some issue", ToastMessage.ERROR);
            }
        });
    }

    public void init_validation(View v) {

        tv_grmserror.setVisibility(View.GONE);
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

                Reedem_fragment.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        validation();
                        loading_gif.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);
    }

    private void validation() {
        tv_grmserror.setVisibility(View.GONE);
        st_grams = et_grams.getText().toString().trim();
        if (st_grams.isEmpty()) {
            tv_grmserror.setVisibility(View.VISIBLE);
            tv_grmserror.setText("Please enter grams");
        }

//        else if (Double.parseDouble(st_grams)<5){
//            tv_grmserror.setVisibility(View.VISIBLE);
//            tv_grmserror.setText("Please enter the min 5 grams");
//        }
        else {
            double goldgrams = Double.parseDouble(st_grams);
            grams = Double.parseDouble(liveprice) * goldgrams;
            Log.e("liveprice", String.valueOf(grams));
            b21 = BigDecimal.valueOf(grams).setScale(2, RoundingMode.HALF_EVEN);
            finalamount = new String(String.valueOf(b21));
            validationapi();
            //open_aleartdialog();
        }
    }

    public void validationapi() {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getvalidation = apiDao.get_validationreedem("Bearer " + AccountUtils.getAccessToken(this), st_grams);
            getvalidation.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        lockprice();

                    } else if (statuscode == 422) {

                        try {
                            dialog.dismiss();
                            tv_grmserror.setVisibility(View.GONE);

                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            JSONObject er = jObjError.getJSONObject("errors");
                            try {
                                JSONArray array_grmserror = er.getJSONArray("grams");
                                for (int i = 0; i < array_grmserror.length(); i++) {
                                    tv_grmserror.setVisibility(View.VISIBLE);
                                    tv_grmserror.setText(array_grmserror.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray array_error = er.getJSONArray("amount");
                                for (int i = 0; i < array_error.length(); i++) {
                                    tv_grmserror.setVisibility(View.VISIBLE);
                                    tv_grmserror.setText(array_error.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            Log.e("dsv", e.toString());
                        }

                        dialog.dismiss();


                    } else {
                        dialog.dismiss();
                        try {
                            assert response.errorBody() != null;
                            try {
                                JSONObject addresserror = new JSONObject(response.errorBody().string());

                                String stmsg = addresserror.getString("message");
                                tv_grmserror.setVisibility(View.VISIBLE);
                                tv_grmserror.setText(stmsg);
                                boolean staddress = addresserror.getBoolean("is_address");

                                if (!staddress) {
                                    AddressPopUpScreen(stmsg);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                }
            });
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
                if (pass.equals(AccountUtils.getPassword(Reedem_fragment.this))) {
                    openwithdraw();
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    ToastMessage.onToast(Reedem_fragment.this, "Password Wrong", ToastMessage.ERROR);
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
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Reedem_fragment.this.getResources().getColor(R.color.textcolorprimary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Reedem_fragment.this.getResources().getColor(R.color.textcolorprimary));

            }
        });
        dialog.show();
    }


    private void openwithdraw() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

        } else {

            if (Double.parseDouble(st_grams) < 2) {
                mw.setVisibility(View.VISIBLE);
                String fftax1 = ftax1.getText().toString().substring(2, ftax1.length());
                if (moneycheck.isChecked()) {
//                    if(Double.parseDouble(walletamount)>=Double.parseDouble(fftax1))
//                        ToastMessage.onToast(this, "money available", ToastMessage.SUCCESS);
                    buyfromwallet(liveprice, st_grams);
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    openpayment();
                }
            } else {
                apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
                Call<Listmodel> get_withdraw = apiDao.get_redeem("Bearer " + AccountUtils.getAccessToken(this), st_grams, finalamount);
                get_withdraw.enqueue(new Callback<Listmodel>() {
                    @Override
                    public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                        int statuscode = response.code();
                        List<Listmodel> list = Collections.singletonList(response.body());
                        if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_ACCEPTED || statuscode == HttpsURLConnection.HTTP_CREATED
                                || statuscode == HttpsURLConnection.HTTP_NO_CONTENT) {
                            for (Listmodel listmodel : list) {
                                dialog.dismiss();
                                // ToastMessage.onToast(this,"Redeemed",ToastMessage.SUCCESS);
                                //onsuccess();
                                sttransactionid = listmodel.getTransaction_Id();
                                stdescription = listmodel.getDescription();
                                openpopupscreen();
                                //  openpayment(String.valueOf(redeem));
                            }
                        } else if (statuscode == 422) {
                            try {
                                dialog.dismiss();
                                tv_grmserror.setVisibility(View.GONE);

                                assert response.errorBody() != null;
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                String st = jObjError.getString("message");


                                //   boolean staddress = jObjError.getBoolean("is_address");

                                //  if (!staddress){
                                //   AddressPopUpScreen(st);
                                //  }else {

                                JSONObject er = jObjError.getJSONObject("errors");
                                try {
                                    JSONArray array_grmserror = er.getJSONArray("grams");
                                    for (int i = 0; i < array_grmserror.length(); i++) {
                                        tv_grmserror.setVisibility(View.VISIBLE);
                                        tv_grmserror.setText(array_grmserror.getString(i));
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    JSONArray array_error = er.getJSONArray("amount");
                                    for (int i = 0; i < array_error.length(); i++) {
                                        tv_grmserror.setVisibility(View.VISIBLE);
                                        tv_grmserror.setText(array_error.getString(i));
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                Log.e("dsv", e.toString());
                            }

                            dialog.dismiss();


                        } else {
                            dialog.dismiss();
                            try {
                                assert response.errorBody() != null;

                                try {
                                    JSONObject addresserror = new JSONObject(response.errorBody().string());

                                    String stmsg = addresserror.getString("message");

                                    boolean staddress = addresserror.getBoolean("is_address");

                                    if (!staddress) {
                                        AddressPopUpScreen(stmsg);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();


                        }

                    }

                    @Override
                    public void onFailure(Call<Listmodel> call, Throwable t) {
                        dialog.dismiss();
                        ToastMessage.onToast(Reedem_fragment.this, "We have some issues", ToastMessage.ERROR);
                        openpopupscreen();
                    }
                });
            }
        }
    }

    public void openpayment() {


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
            double total = Double.parseDouble(ftax1.getText().toString().substring(2, ftax1.length()));

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
        Log.e("paymentID", s);

    }

    String CouponCode;

    public void onpaymentsucess(String paymentid) {

        Log.e("payment id;", paymentid);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {
//            pay_HHmZRtOO3HfaUT
            //  Log.e("coupon", stcouponcode);
            dialog.dismiss();

            //if (stcouponcode.equals("null")) {
//            Log.e("payamount", stamount);
//            Log.e("coupon", stcouponcode);

//            Log.e("Enterd Amount", amountvalue);

            Log.e("finalamount: ", String.valueOf(finalamount));
            Log.e("St_grams :  ", st_grams);
            Log.e("Fraction :  ", ftax1.getText().toString());

            String fftax1 = ftax1.getText().toString().substring(2, ftax1.length());
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> call = apiDao.getrazorpayforredeem("Bearer " + AccountUtils.getAccessToken(this), paymentid, String.valueOf(finalamount), st_grams, "24", fftax1);
            call.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int stauscode = response.code();
                    Log.e("stauscode buy", String.valueOf(stauscode));
                    if (stauscode == 200 || stauscode == HttpsURLConnection.HTTP_OK || stauscode == HttpsURLConnection.HTTP_CREATED) {
                        dialog.dismiss();
                        List<Listmodel> list = Collections.singletonList(response.body());
                        for (Listmodel listmodel : list) {
                            payment_status = listmodel.getPurchase_Status();
                            // Toast.makeText(Buy_Gold_Information.this, String.valueOf(payment_status), Toast.LENGTH_SHORT).show();
                            if (payment_status) {
                                sttransactionid = listmodel.getTransaction_Id();
                                stdescription = listmodel.getDescription();
                                openpopupscreen();
                            } else {
                                openpopupscreen();
                            }

                        }

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
                            Toast.makeText(Reedem_fragment.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");
                            JSONArray array_paymentid = er.getJSONArray("payment_id");
                            for (int i = 0; i < array_paymentid.length(); i++) {

                                Toast.makeText(Reedem_fragment.this, array_paymentid.getString(i), Toast.LENGTH_SHORT).show();
                                openpopupscreen();
                            }
                            JSONArray array_amount = er.getJSONArray("amount");
                            for (int i = 0; i < array_amount.length(); i++) {
                                Toast.makeText(Reedem_fragment.this, array_amount.getString(i), Toast.LENGTH_SHORT).show();
                                openpopupscreen();
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


    public void onpaymenterror() {

        Intent intent = new Intent(this, PaymentError.class);
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    public void openpopupscreen() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(this, Successpopup.class);
            intent.putExtra("grams", st_grams);
            intent.putExtra("from", "redeem");
            intent.putExtra("transctionid", sttransactionid);
            intent.putExtra("description", stdescription);
            startActivity(intent);
        }


    }


    public void buyfromwallet(String fa, String fg) {


        switch (fg) {
            case "0.5":
                fa = String.valueOf(Integer.parseInt(fa) / 2);
                break;
            case "1":
                fa = fa;
                break;
            case "1.5":
                fa = String.valueOf(Integer.parseInt(fa) + (Integer.parseInt(fa) / 2));
                break;
        }
        Log.e("sendfyfftv", "" + fa);


        String s = fa;
        int i = Integer.parseInt(s.substring(0, s.length() - 2));
        Log.e("sendfyffintttv", "" + i);
        String goldliveprive = String.valueOf(i);
        Log.e("goldliveprive", "" + goldliveprive);


        String fftax1 = ftax1.getText().toString().substring(2, ftax1.length());
        Log.e("fa: ", fa);
        Log.e("fftax1: ", fftax1);
        Log.e("fg: ", fg);
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Listmodel> walletdebit = apiDao.get_redeem_gold_from_money_wallet("Bearer " + AccountUtils.getAccessToken(this), fa, fg, fftax1);
        walletdebit.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int stauscode = response.code();
                Log.e("stauscode buy", String.valueOf(stauscode));
                if (stauscode == 200 || stauscode == HttpsURLConnection.HTTP_OK || stauscode == HttpsURLConnection.HTTP_CREATED) {
                    List<Listmodel> list = Collections.singletonList(response.body());
                    for (Listmodel listmodel : list) {
                        payment_status = listmodel.getPurchase_Status();
                        // Toast.makeText(Buy_Gold_Information.this, String.valueOf(payment_status), Toast.LENGTH_SHORT).show();
                        if (payment_status) {
                            sttransactionid = listmodel.getTransaction_Id();
                            stdescription = listmodel.getDescription();
                            openpopupscreen();
                        } else {
                            openpopupscreen();
                        }

                    }
                } else {
                    Log.e("", AccountUtils.getAccessToken(Reedem_fragment.this));
                    return;
//                    btnbuygold.setEnabled(false);
//                    btnbuygold.setBackgroundResource(R.drawable.backgroundvisablity);

                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                Log.e("lockratesfail", t.toString());
                openpopupscreen();

//                ToastMessage.onToast(Reedem_fragment.this,"We have some issues",ToastMessage.ERROR);
                return;

            }
        });
    }


    @Override
    public void onPaymentError(int i, String s) {

        // Toast.makeText(this, "Payment Error"+s, Toast.LENGTH_SHORT).show();
    }

    AlertDialog alertDialogdialog;
    GifImageView loading_gifaddress;
    Button submit;
    TextView tvtext;
    ImageView img_close;


    public void AddressPopUpScreen(String msg) {

        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.addresslinkforreedempopup, null);
        alertdilog.setCancelable(false);
        alertdilog.setView(dialogview);
        alertDialogdialog = alertdilog.create();

        tvtext = dialogview.findViewById(R.id.tvaddress);
        tvtext.setText(msg);
        img_close = dialogview.findViewById(R.id.img_close);
        loading_gifaddress = dialogview.findViewById(R.id.loading_gif);

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

                        Reedem_fragment.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getaddress();
                                submit.setVisibility(View.VISIBLE);
                                loading_gif.setVisibility(View.GONE);
                            }
                        });
                    }
                }, 500);
            }
        });
    }

    public void getaddress() {
        if (!NetworkUtils.isConnected(this)) {

            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(Reedem_fragment.this, CustomerAddAddress.class);
            intent.putExtra("from", "redeem");
            startActivity(intent);
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
//                    open_aleartdialog();
                    openwithdraw();
                    // validationapi();

//                    btnbuygold.setEnabled(true);
//                    btnbuygold.setBackgroundResource(R.drawable.buttonborder);

                } else {
                    ToastMessage.onToast(Reedem_fragment.this, "Try again", ToastMessage.ERROR);
//                    btnbuygold.setEnabled(false);
//                    btnbuygold.setBackgroundResource(R.drawable.backgroundvisablity);

                }
            }

            @Override
            public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                Log.e("lockratesfail", t.toString());
                ToastMessage.onToast(Reedem_fragment.this, "We have some issues", ToastMessage.ERROR);

            }
        });

    }

}
