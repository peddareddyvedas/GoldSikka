package com.goldsikka.goldsikka.Fragments;

import android.annotation.SuppressLint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.goldsikka.goldsikka.Activitys.Coupons.CouponsList;
import com.goldsikka.goldsikka.Activitys.Coupons.CouponsModel;
import com.goldsikka.goldsikka.Activitys.Elevenplus_Jewellery;
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
import java.util.Timer;
import java.util.TimerTask;


import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Buy_Digitalgold extends AppCompatActivity implements View.OnClickListener {

    MenuItem menuItem;
    String buttonvalidation, customer_gold, live_gold_buy;
    Button btnbuygold;
    TextWatcher textWatcher;
    String liveprice, taxPercentage;
    ApiDao apiDao;
    String st_ingrams, st_incurrency, st_currencyinwords;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_gold)
    TextView tvgold;
    TextView tvliverate, tv_sellprice, tvchange;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_purity)
    TextView tv_purity;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llopencoupon)
    LinearLayout llopencoupon;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llofferremove)
    LinearLayout llofferremove;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvofferremove)
    TextView tvofferremove;

    EditText etAmount, etgold;
    TextView tv_grms, tv_amt;
    String finalgold, finalamount;
    String stcouponcode = "null";
    String stcouponamount = "null";
    String stcouponfrom = "null";
    String goldvalue, amountvalue;
    AlertDialog alertDialogdialog;

    boolean isCoupon = true;

    RelativeLayout backbtn;
    TextView coupencode;

    GifImageView loading_gif;
    TextView iv_liveprice;

    LinearLayout llInAmount, llInGrams, llAmount;
    TextView tvInAmount, tvInGrams;

    Button btnamount, btngrams;

    EditText etamount;
    String status = "0";
    TextView tvresult;
    String minamount = "yes";
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rupeessymbol)
    TextView rupeessymbol;

    TextView unameTv, uidTv, titleTv;

    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_digitalgold_design);


        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getliveprices();
        iv_liveprice = findViewById(R.id.iv_liveprice);
        iv_liveprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = new ProgressDialog(Buy_Digitalgold.this);
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

        //liveprice  =AccountUtils.getSellPrice(this);
        ButterKnife.bind(this);

        rupeessymbol.setVisibility(View.VISIBLE);
        rupeessymbol.setText("\u20B9");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle("Buy Gold");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Buy Gold");
        //  toolbar.setTitleTextColor(getColor(R.color.white));
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        initlizeviews();

        llAmount = findViewById(R.id.llAmount);
        llAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etamount.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                        INPUT_METHOD_SERVICE);
                imm.toggleSoftInputFromWindow(etamount.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
            }
        });

        goldcalcluation();
        getdata();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
//            stcouponamount = bundle.getString("offeramount");
//            stcouponcode = bundle.getString("couponcode");
//            stcouponfrom = bundle.getString("couponfrom");

            stcouponcode = bundle.getString("couponcode");
            stcouponamount = bundle.getString("offeramount");
            isCoupon = bundle.getBoolean("isCoupon", true);

        }
//
//        if (stcouponfrom.equals("offlinecoupon")||stcouponfrom.equals("onlinecoupon")){
//            llopencoupon.setVisibility(View.GONE);
//            llofferremove.setVisibility(View.VISIBLE);
//        }
//        else {
//            llopencoupon.setVisibility(View.VISIBLE);
//            llofferremove.setVisibility(View.GONE);
//        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tvofferremove)
    public void offerremove() {
        llopencoupon.setVisibility(View.VISIBLE);
        llofferremove.setVisibility(View.GONE);
        isCoupon = true;
        stcouponcode = "null";
        stcouponamount = "null";

    }

    public void initlizeviews() {

        loading_gif = findViewById(R.id.loading_gif);

        tvchange = findViewById(R.id.tvchange);
        tvliverate = findViewById(R.id.tvliverate);
        tvgold = findViewById(R.id.tv_gold);

        tv_purity = findViewById(R.id.tv_purity);
        //tv_purity.setText(AccountUtils.getPurity(this)+" / "+AccountUtils.getCarat(this));

        tv_sellprice = findViewById(R.id.tv_sellprice);


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
        coupencode = findViewById(R.id.coupencode);
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
                                String sp = listmodel.getBuy_price_per_gram();
                                Log.e("liveprice", liveprice);
                                tv_sellprice.setText(getString(R.string.Rs) + sp);
                                tvliverate.setText(getString(R.string.Rs) + liveprice);
                                double tax = Double.parseDouble(listmodel.getTaxPercentage()) / 100;
                                taxPercentage = String.valueOf(tax);
                                AccountUtils.setGsttax(Buy_Digitalgold.this, listmodel.getTaxPercentage());
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
                            Toast.makeText(Buy_Digitalgold.this, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                //    ToastMessage.onToast(Buy_Digitalgold.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.llopencoupon)
    public void openoffers() {
        Intent intent = new Intent(Buy_Digitalgold.this, CouponsList.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {
            minamount = data.getStringExtra("minamount");
            stcouponamount = data.getStringExtra("offeramount");

           /* stcouponcode = data.getStringExtra("couponcode");
            coupencode.setText("Coupen Code:" + data.getStringExtra("couponcode"));
*/
            try {
                if (minamount.equals("yes")) {
                    Log.e("dvd", "sdv");
                } else if (Double.parseDouble(amountvalue) < Double.parseDouble(minamount)) {
                    ToastMessage.onToast(Buy_Digitalgold.this, "Coupon is not applicable", ToastMessage.ERROR);
                } else if (Double.parseDouble(amountvalue) < Double.parseDouble(stcouponamount)) {
                    ToastMessage.onToast(Buy_Digitalgold.this, "Coupon is not applicable", ToastMessage.ERROR);
                } else {
//               ToastMessage.onToast(Buy_Digitalgold.this, "success", ToastMessage.SUCCESS);
                    stcouponcode = data.getStringExtra("couponcode");
                    stcouponamount = data.getStringExtra("offeramount");
                    isCoupon = data.getBooleanExtra("isCoupon", false);
                    llopencoupon.setVisibility(View.GONE);
                    llofferremove.setVisibility(View.VISIBLE);
                    Log.e("stcouponcode ", "" + stcouponcode);
                    Log.e("stcouponamount ", "" + stcouponamount);

                    coupencode.setText("Coupon Code:" + data.getStringExtra("couponcode"));

                    tvofferremove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isCoupon = true;
                            llopencoupon.setVisibility(View.VISIBLE);
                            llofferremove.setVisibility(View.GONE);
                        }
                    });
                }
            } catch (Exception e) {
                Log.e("xhcbv ", "sfsgvd" + e);
            }
        }
    }


    public void lockprice() {

        Log.e("livepricelrate", String.valueOf(this.liveprice));
        ApiDao apiDao2 = (ApiDao) ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        this.apiDao = apiDao2;
        String str = this.liveprice;
        apiDao2.getlock_rates(str, "Bearer " + AccountUtils.getAccessToken(this)).enqueue(new Callback<List<Listmodel>>() {
            public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                int code = response.code();
                Log.e("statuscode Lock", String.valueOf(code));
//                ToastMessage.onToast(Buy_Digitalgold.this, String.valueOf(code), ToastMessage.ERROR);

                if (code == 202) {
                    Buy_Digitalgold.this.buygold();
                } else {
                    ToastMessage.onToast(Buy_Digitalgold.this, "Try After some Time", ToastMessage.ERROR);
                }
            }

            public void onFailure(Call<List<Listmodel>> call, Throwable th) {
                Log.e("lockratesfail", th.toString());
               // ToastMessage.onToast(Buy_Digitalgold.this, "We have some issues Try After some Time", ToastMessage.ERROR);
            }
        });
//        Log.e("livepricelrate",String.valueOf(liveprice));
//        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//
//        Call<List<Listmodel>> lockrates = apiDao.getlock_rates(liveprice,"Bearer "+AccountUtils.getAccessToken(this));
//        lockrates.enqueue(new Callback<List<Listmodel>>() {
//            @Override
//            public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
//                int statuscode = response.code();
//                Log.e("statuscode Lock",String.valueOf(statuscode));
//                if (statuscode == HttpsURLConnection.HTTP_ACCEPTED){
//
//                    buygold();
////                    btnbuygold.setEnabled(true);
////                    btnbuygold.setBackgroundResource(R.drawable.buttonborder);
//
//                }else {
//                    ToastMessage.onToast(Buy_Digitalgold.this,"Try After some Time",ToastMessage.ERROR);
////                    btnbuygold.setEnabled(false);
////                    btnbuygold.setBackgroundResource(R.drawable.backgroundvisablity);
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Listmodel>> call, Throwable t) {
//                Log.e("lockratesfail",t.toString());
//                ToastMessage.onToast(Buy_Digitalgold.this,"We have some issues Try After some Time",ToastMessage.ERROR);
//
//            }
//        });

    }

    @Override
    public void onBackPressed() {
//        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        // finish();
    }

    public void btnAmount() {

        rupeessymbol.setVisibility(View.VISIBLE);
        tvresult.setVisibility(View.VISIBLE);
        rupeessymbol.setText("\u20B9");

        amountvalue = etamount.getText().toString();
//        etamount.setHint("Amount");
        etamount.setHint(Html.fromHtml(getString(R.string.amount)));
        tvchange.setText("Enter Amount");
        btngrams.setText("BUY IN GRAMS");

        btnamount.setBackgroundResource(R.drawable.buy_amount_button);
        btngrams.setBackgroundResource(R.drawable.buy_grams_button);

        btngrams.setTextColor(getResources().getColor(R.color.black));
        btnamount.setTextColor(getResources().getColor(R.color.white));

        status = "0";
        testcal();

    }

    public void btGrams() {

        rupeessymbol.setVisibility(View.VISIBLE);
        tvresult.setVisibility(View.VISIBLE);
        rupeessymbol.setText("Gms");

        amountvalue = etamount.getText().toString();
//        etamount.setHint("Grams");
        etamount.setHint(Html.fromHtml(getString(R.string.grams)));
        btnamount.setText("BUY IN AMOUNT");
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

                        amountvalue = etamount.getText().toString();

                        buttonvalidation = amountvalue;

                        etamount.removeTextChangedListener(textWatcher);
                        etamount.addTextChangedListener(textWatcher);
                        // goldvalue = etgold.getText().toString();

                        if (etamount.length() == 0) {
                            tvresult.setText("");
                            btnbuygold.setText(getResources().getString(R.string.proceed_to_pay));

                        }
                        //status = "0";
                        testcal();

                    }

                } else {

                    tvresult.setText("");
                    btnbuygold.setText(getResources().getString(R.string.proceed_to_pay));


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

                    tvresult.setText(finalgold + " g");

//                    tvresult.setText(finalgold);
                    btnbuygold.setText(getResources().getString(R.string.proceed_to_pay));


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

                    tvresult.setText("\u20B9 " + finalamount);
                    // finalamount = textWatcher.toString().concat(String.valueOf(etAmount));
                    btnbuygold.setText(getResources().getString(R.string.proceed_to_pay));

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
                            Toast.makeText(Buy_Digitalgold.this, st, Toast.LENGTH_SHORT).show();
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
                  //  Toast.makeText(Buy_Digitalgold.this, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

//    private void getrefresh() {
//
//        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
//        Call<Listmodel> getrefresh = apiDao.getrefresh("Bearer "+AccountUtils.getAccessToken(activity));
//        getrefresh.enqueue(new Callback<Listmodel>() {
//            @Override
//            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
//                refresh = "refresh";
//                int stauscode = response.code();
//                if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
//                    List<Listmodel> list = Collections.singletonList(response.body());
//                    for (Listmodel listmodel : list) {
//                        String accestoken = listmodel.getAccessToken();
//                        AccountUtils.setAccessToken(activity, accestoken);
//                        getdata();
//                    }
//
//                }else if (stauscode == HttpsURLConnection.HTTP_UNAUTHORIZED){
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Listmodel> call, Throwable t) {
//                Toast.makeText(activity, "onFailure", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }

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

                Buy_Digitalgold.this.runOnUiThread(new Runnable() {
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

        if (isCoupon) {
            stcouponcode = "null";
            stcouponamount = "null";
            if (status.equals("0")) {
                if (amountvalue.isEmpty()) {
                    ToastMessage.onToast(Buy_Digitalgold.this, "Please Enter Amount", ToastMessage.ERROR);
                } else if (Double.parseDouble(amountvalue) < 100) {
                    ToastMessage.onToast(Buy_Digitalgold.this, "Please Enter Min Rs100", ToastMessage.ERROR);
                } else if (Double.parseDouble(finalgold) > 30) {
                    openalertdiloug();
                    // etAmount.getText().clear();

                } else {
                    lockprice();
                }

            } else {
                if (amountvalue.isEmpty()) {
                    ToastMessage.onToast(Buy_Digitalgold.this, "Please Enter Gold", ToastMessage.ERROR);
                } else if (Double.parseDouble(finalamount) < 100) {
                    ToastMessage.onToast(Buy_Digitalgold.this, "Amount Must be Rs.100", ToastMessage.ERROR);
                } else if (Double.parseDouble(amountvalue) > 30) {
                    openalertdiloug();
                    // etAmount.getText().clear();
                } else {
                    lockprice();
                }
            }
        } else {
            if (status.equals("0")) {
                if (amountvalue.isEmpty()) {
                    ToastMessage.onToast(Buy_Digitalgold.this, "Please Enter Amount", ToastMessage.ERROR);
                } else if (Double.parseDouble(finalgold) > 30) {
                    openalertdiloug();
                    // etAmount.getText().clear();

                } else {
                    Log.e("Else", "Else checking");
                    couponvalidation();
                }
            } else {
                if (amountvalue.isEmpty()) {
                    ToastMessage.onToast(Buy_Digitalgold.this, "Please Enter Gold", ToastMessage.ERROR);
                } else if (Double.parseDouble(amountvalue) > 30) {
                    openalertdiloug();
                    // etAmount.getText().clear();
                } else {
                    couponvalidation();
                }
            }
            if (!amountvalue.isEmpty()) {
                if ((Double.parseDouble(stcouponamount) > (Double.parseDouble(amountvalue)))) {
                    ToastMessage.onToast(Buy_Digitalgold.this, "Please enter amount greater then the coupon Discount amount", ToastMessage.ERROR);

                } else if ((Double.parseDouble(minamount) > (Double.parseDouble(amountvalue)))) {
                    ToastMessage.onToast(Buy_Digitalgold.this, "Please enter amount greater then the coupon Transaction amount", ToastMessage.ERROR);

                }

            }

        }

//        if (status.equals("0")){
//            if (amountvalue.isEmpty()){
//                ToastMessage.onToast(Buy_Digitalgold.this,"Please Enter Amount",ToastMessage.ERROR);
//            }else if (Double.parseDouble(amountvalue)<100){
//                ToastMessage.onToast(Buy_Digitalgold.this,"Please Enter Min Rs100",ToastMessage.ERROR);
//            }else if (Double.parseDouble(finalgold)>30){
//                openalertdiloug();
//               // etAmount.getText().clear();
//
//            }else {
//                lockprice();
//            }
//        }else {
//            if (amountvalue.isEmpty()){
//                ToastMessage.onToast(Buy_Digitalgold.this,"Please Enter Gold",ToastMessage.ERROR);
//            }
//            else if (Double.parseDouble(amountvalue)>30){
//                openalertdiloug();
//               // etAmount.getText().clear();
//            }
//            else {
//                lockprice();
//            }
        //     }

//        if (amountvalue.isEmpty()){
//            ToastMessage.onToast(Buy_Digitalgold.this,"Please Enter Amount or Gold",ToastMessage.ERROR);
//        }else if (Double.parseDouble(finalamount)<100){
//            ToastMessage.onToast(Buy_Digitalgold.this,"Please Enter Min Rs100",ToastMessage.ERROR);
//        }else if (Double.parseDouble(finalgold)>30){
//            openalertdiloug();
//            etgold.getText().clear();
//
//        }
//        else {
//
//            lockprice();
//
//        }
    }

    String passamount;

    public void couponvalidation() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();

        } else {

            if (status.equals("0")) {
                passamount = amountvalue;
            } else {
                passamount = finalamount;
            }

            Log.e("codestatuscode", String.valueOf(passamount));
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<CouponsModel> invalidation = apiDao.couponvalidation("Bearer " + AccountUtils.getAccessToken(this), stcouponcode, passamount);
            invalidation.enqueue(new Callback<CouponsModel>() {
                @Override
                public void onResponse(Call<CouponsModel> call, Response<CouponsModel> response) {
                    int statuscode = response.code();
                    CouponsModel model = response.body();
                    Log.e("codestatuscode", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_ACCEPTED) {
                        dialog.dismiss();
                        lockprice();
                    } else {
                        dialog.dismiss();
                        assert response.errorBody() != null;

                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
//                            ToastMessage.onToast(Buy_Digitalgold.this,st,ToastMessage.ERROR);
                            JSONObject er = jObjError.getJSONObject("errors");
                            try {
                                JSONArray password = er.getJSONArray("coupon");
                                for (int i = 0; i < password.length(); i++) {
//                                    ToastMessage.onToast(Buy_Digitalgold.this,password.getString(i),ToastMessage.ERROR);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray password = er.getJSONArray("amount");
                                for (int i = 0; i < password.length(); i++) {
//                                    ToastMessage.onToast(Buy_Digitalgold.this,password.getString(i),ToastMessage.ERROR);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CouponsModel> call, Throwable t) {
                    dialog.dismiss();
                    //ToastMessage.onToast(Buy_Digitalgold.this, "We have some issues please try after some time"
                      //      , ToastMessage.ERROR);

                }
            });
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

    String Coupongold;

    public void buygold() {

        if (!isCoupon) {

            double itax = Double.parseDouble(taxPercentage);
            double astamount = Double.parseDouble(stcouponamount);
            double dtotal = itax * astamount;
            double finalcaluclation = astamount - dtotal;
            Log.e("witfinalcaluclation", String.valueOf(finalcaluclation));
            double live = Double.parseDouble(liveprice);
            double grms = (finalcaluclation / live);
            Coupongold = String.valueOf(grms);

            Log.e("withgst@cou", Coupongold);

        } else {
            stcouponcode = "null";
            stcouponamount = "null";
        }
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            if (status.equals("0")) {

                Intent intent = new Intent(Buy_Digitalgold.this, Buy_Gold_Information.class);
                intent.putExtra("customer_rate", amountvalue);
                intent.putExtra("customer_gold", finalgold);
                intent.putExtra("liveprice", liveprice);
                intent.putExtra("Coupongold", Coupongold);
                intent.putExtra("couponcode", stcouponcode);
                intent.putExtra("isCoupon", isCoupon);
                intent.putExtra("status", status);
                intent.putExtra("taxPercentage", taxPercentage);
                startActivity(intent);
                Log.e("customer_rate", amountvalue);
                Log.e("customer_gold", finalgold);
//            Log.e("liveprice","4800");
//            Log.e("offeramount","");
//            Log.e("couponcode","");
            } else if (status.equals("1")) {

                Intent intent = new Intent(Buy_Digitalgold.this, Buy_Gold_Information.class);
                intent.putExtra("customer_rate", finalamount);
                intent.putExtra("customer_gold", amountvalue);
                intent.putExtra("liveprice", liveprice);
                intent.putExtra("Coupongold", Coupongold);
                intent.putExtra("couponcode", stcouponcode);
                intent.putExtra("isCoupon", isCoupon);
                intent.putExtra("taxPercentage", taxPercentage);
                intent.putExtra("status", status);

                startActivity(intent);
                Log.e("customer_rate", finalamount);
                Log.e("customer_gold", amountvalue);
//            Log.e("liveprice","4800");
//            Log.e("offeramount","");
//            Log.e("couponcode","");
            }
        }

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
                return;
        }

    }

    public void openBuygoldValidation() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            init_validation();
        }
    }

}

//
//package com.goldsikka.goldsikka.Fragments;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.Editable;
//import android.text.Html;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.NavUtils;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import com.facebook.appevents.AppEventsConstants;
//import com.goldsikka.goldsikka.Activitys.Coupons.CouponsList;
//import com.goldsikka.goldsikka.Activitys.Coupons.CouponsModel;
////import com.goldsikka.goldsikka.C2002R;
//import com.goldsikka.goldsikka.C2002R;
//import com.goldsikka.goldsikka.Utils.AccountUtils;
//import com.goldsikka.goldsikka.Utils.NetworkUtils;
//import com.goldsikka.goldsikka.Utils.ToastMessage;
//import com.goldsikka.goldsikka.interfaces.ApiDao;
//import com.goldsikka.goldsikka.model.Listmodel;
//import com.goldsikka.goldsikka.netwokconnection.ApiClient;
//import com.google.firebase.analytics.FirebaseAnalytics;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.Collections;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
////import p010pl.droidsonroids.gif.GifImageView;
//import pl.droidsonroids.gif.GifImageView;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class Buy_Digitalgold extends AppCompatActivity implements View.OnClickListener {
//    String Coupongold;
//    AlertDialog alertDialogdialog;
//    String amountvalue;
//    ApiDao apiDao;
//    Button btnamount;
//    Button btnbuygold;
//    Button btngrams;
//    String buttonvalidation;
//    String customer_gold;
//    EditText etAmount;
//    EditText etamount;
//    EditText etgold;
//    String finalamount;
//    String finalgold;
//    String goldvalue;
//    ImageView imageView_close;
//    boolean isCoupon = true;
//    TextView iv_liveprice;
//    String live_gold_buy;
//    String liveprice;
//    LinearLayout llInAmount;
//    LinearLayout llInGrams;
//    @SuppressLint("ResourceType")
//    @BindView(2131362483)
//    LinearLayout llofferremove;
//    @SuppressLint("ResourceType")
//    @BindView(2131362484)
//    LinearLayout llopencoupon;
//    GifImageView loading_gif;
//    MenuItem menuItem;
//    String passamount;
//    String st_currencyinwords;
//    String st_incurrency;
//    String st_ingrams;
//    String status = AppEventsConstants.EVENT_PARAM_VALUE_NO;
//    String stcouponamount = "null";
//    String stcouponcode = "null";
//    String stcouponfrom = "null";
//    String taxPercentage;
//    TextWatcher textWatcher;
//    TextView tvInAmount;
//    TextView tvInGrams;
//    TextView tv_amt;
//    TextView tv_grms;
//    @SuppressLint("ResourceType")
//    @BindView(2131363085)
//    TextView tv_purity;
//    TextView tv_sellprice;
//    TextView tvchange;
//    @SuppressLint("ResourceType")
//    @BindView(2131363019)
//    TextView tvgold;
//    TextView tvliverate;
//    @SuppressLint("ResourceType")
//    @BindView(2131363206)
//    TextView tvofferremove;
//    TextView tvresult;
//    String value;
//
//    /* access modifiers changed from: protected */
//    public void onCreate(Bundle bundle) {
//        super.onCreate(bundle);
//        setContentView((int) C2002R.layout.buy_digitalgold_design);
//        getliveprices();
//        TextView textView = (TextView) findViewById(C2002R.C2005id.iv_liveprice);
//        this.iv_liveprice = textView;
//        textView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                final ProgressDialog progressDialog = new ProgressDialog(Buy_Digitalgold.this);
//                progressDialog.setCancelable(false);
//                progressDialog.setMessage("Please wait..");
//                progressDialog.show();
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        Buy_Digitalgold.this.getliveprices();
//                        progressDialog.dismiss();
//                    }
//                }, 1500);
//            }
//        });
//        ButterKnife.bind((Activity) this);
//        setSupportActionBar((Toolbar) findViewById(C2002R.C2005id.toolbar));
//        setTitle("Buy Gold");
//        ActionBar supportActionBar = getSupportActionBar();
//        if (supportActionBar != null) {
//            supportActionBar.setDisplayHomeAsUpEnabled(true);
//        }
//        initlizeviews();
//        goldcalcluation();
//        getdata();
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            this.stcouponcode = extras.getString("couponcode");
//            this.stcouponamount = extras.getString("offeramount");
//            this.isCoupon = extras.getBoolean("isCoupon", true);
//            this.value = extras.getString("value");
//        }
//    }
//
//    @OnClick({2131363206})
//    public void offerremove() {
//        this.llopencoupon.setVisibility(View.VISIBLE);
//        this.llofferremove.setVisibility(View.GONE);
//        this.isCoupon = true;
//        this.stcouponcode = "null";
//        this.stcouponamount = "null";
//    }
//
//    public void initlizeviews() {
//        this.loading_gif = (GifImageView) findViewById(C2002R.C2005id.loading_gif);
//        this.tvchange = (TextView) findViewById(C2002R.C2005id.tvchange);
//        this.tvliverate = (TextView) findViewById(C2002R.C2005id.tvliverate);
//        this.tvgold = (TextView) findViewById(C2002R.C2005id.tv_gold);
//        this.tv_purity = (TextView) findViewById(C2002R.C2005id.tv_purity);
//        this.tv_sellprice = (TextView) findViewById(C2002R.C2005id.tv_sellprice);
//        Button button = (Button) findViewById(C2002R.C2005id.btn_buygold);
//        this.btnbuygold = button;
//        button.setOnClickListener(this);
//        this.etamount = (EditText) findViewById(C2002R.C2005id.et_amount);
//        this.etgold = (EditText) findViewById(C2002R.C2005id.et_grms);
//        this.etamount.setHint(Html.fromHtml(getString(C2002R.string.amount)));
//        Button button2 = (Button) findViewById(C2002R.C2005id.btInAmount);
//        this.btnamount = button2;
//        button2.setOnClickListener(this);
//        Button button3 = (Button) findViewById(C2002R.C2005id.btInGrams);
//        this.btngrams = button3;
//        button3.setOnClickListener(this);
//        this.tvresult = (TextView) findViewById(C2002R.C2005id.tv_grms);
//    }
//
//    public void getliveprices() {
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Please wait...");
//        progressDialog.show();
//        if (!NetworkUtils.isConnected(this)) {
//            progressDialog.dismiss();
//            ToastMessage.onToast(this, getString(C2002R.string.error_no_internet_connection), ToastMessage.ERROR);
//            return;
//        }
//        ApiDao apiDao2 = (ApiDao) ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//        this.apiDao = apiDao2;
//        apiDao2.getlive_rates("Bearer " + AccountUtils.getAccessToken(this)).enqueue(new Callback<Listmodel>() {
//            static final /* synthetic */ boolean $assertionsDisabled = false;
//
//            {
//                Class<Buy_Digitalgold> cls = Buy_Digitalgold.class;
//            }
//
//            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
//                int code = response.code();
//                List<Listmodel> singletonList = Collections.singletonList(response.body());
//                if (code != 200 && code != 201) {
//                    progressDialog.dismiss();
//                    try {
//                        JSONObject jSONObject = new JSONObject(response.errorBody().string());
//                        String string = jSONObject.getString("message");
//                        jSONObject.getJSONObject("errors");
//                        Toast.makeText(Buy_Digitalgold.this, string, Toast.LENGTH_SHORT).show();
//                    } catch (IOException | JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else if (singletonList != null) {
//                    for (Listmodel listmodel : singletonList) {
//                        progressDialog.dismiss();
//                        Buy_Digitalgold.this.liveprice = listmodel.getSell_price_per_gram();
//                        Log.e("liveprice", Buy_Digitalgold.this.liveprice);
//                        TextView textView = Buy_Digitalgold.this.tv_sellprice;
//                        textView.setText(Buy_Digitalgold.this.getString(C2002R.string.f168Rs) + Buy_Digitalgold.this.liveprice);
//                        TextView textView2 = Buy_Digitalgold.this.tvliverate;
//                        textView2.setText(Buy_Digitalgold.this.getString(C2002R.string.f168Rs) + Buy_Digitalgold.this.liveprice);
//                        Buy_Digitalgold.this.taxPercentage = String.valueOf(Double.parseDouble(listmodel.getTaxPercentage()) / 100.0d);
//                        AccountUtils.setGsttax(Buy_Digitalgold.this, listmodel.getTaxPercentage());
//                    }
//                } else {
//                    progressDialog.dismiss();
//                }
//            }
//
//            public void onFailure(Call<Listmodel> call, Throwable th) {
//                progressDialog.dismiss();
//                ToastMessage.onToast(Buy_Digitalgold.this, "We have some issues", ToastMessage.ERROR);
//            }
//        });
//    }
//
//    @OnClick({2131362484})
//    public void openoffers() {
//        Intent intent = new Intent(this, CouponsList.class);
//        intent.putExtra("value", this.etamount.getText().toString().trim());
//        startActivityForResult(intent, 2);
//    }
//
//    /* access modifiers changed from: protected */
//    public void onActivityResult(int i, int i2, Intent intent) {
//        super.onActivityResult(i, i2, intent);
//        if (i == 2) {
//            this.stcouponcode = intent.getStringExtra("couponcode");
//            this.stcouponamount = intent.getStringExtra("offeramount");
//            this.isCoupon = intent.getBooleanExtra("isCoupon", false);
//            this.etamount.setText(intent.getStringExtra("value"));
//            this.amountvalue = intent.getStringExtra("value");
//            this.llopencoupon.setVisibility(View.GONE);
//            this.llofferremove.setVisibility(View.VISIBLE);
//            this.tvofferremove.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    Buy_Digitalgold.this.isCoupon = true;
//                    Buy_Digitalgold.this.llopencoupon.setVisibility(View.VISIBLE);
//                    Buy_Digitalgold.this.llofferremove.setVisibility(View.GONE);
//                }
//            });
//        }
//    }
//
//    public void lockprice() {
//        Log.e("livepricelrate", String.valueOf(this.liveprice));
//        ApiDao apiDao2 = (ApiDao) ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//        this.apiDao = apiDao2;
//        String str = this.liveprice;
//        apiDao2.getlock_rates(str, "Bearer " + AccountUtils.getAccessToken(this)).enqueue(new Callback<List<Listmodel>>() {
//            public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
//                int code = response.code();
//                Log.e("statuscode Lock", String.valueOf(code));
//                if (code == 202) {
//                    Buy_Digitalgold.this.buygold();
//                } else {
//                    ToastMessage.onToast(Buy_Digitalgold.this, "Try After some Time", ToastMessage.ERROR);
//                }
//            }
//
//            public void onFailure(Call<List<Listmodel>> call, Throwable th) {
//                Log.e("lockratesfail", th.toString());
//                ToastMessage.onToast(Buy_Digitalgold.this, "We have some issues Try After some Time", ToastMessage.ERROR);
//            }
//        });
//    }
//
//    public void onBackPressed() {
//        NavUtils.navigateUpFromSameTask(this);
//        super.onBackPressed();
//    }
//
//    public void btnAmount() {
//        this.amountvalue = this.etamount.getText().toString();
//        this.etamount.setHint(Html.fromHtml(getString(C2002R.string.amount)));
//        this.tvchange.setText("Enter Amount");
//        this.btngrams.setText("BUY IN GRAMS");
//        this.btnamount.setBackgroundResource(C2002R.C2004drawable.buy_amount_button);
//        this.btngrams.setBackgroundResource(C2002R.C2004drawable.buy_grams_button);
//        this.btngrams.setTextColor(getResources().getColor(C2002R.C2003color.black));
//        this.btnamount.setTextColor(getResources().getColor(C2002R.C2003color.white));
//        this.status = AppEventsConstants.EVENT_PARAM_VALUE_NO;
//        testcal();
//    }
//
//    public void btGrams() {
//        this.amountvalue = this.etamount.getText().toString();
//        this.etamount.setHint(Html.fromHtml(getString(C2002R.string.grams)));
//        this.btnamount.setText("BUY IN AMOUNT");
//        this.tvchange.setText("Enter Grams");
//        this.btnamount.setBackgroundResource(C2002R.C2004drawable.button_left_border);
//        this.btngrams.setBackgroundResource(C2002R.C2004drawable.button_right_background);
//        this.btnamount.setTextColor(getResources().getColor(C2002R.C2003color.black));
//        this.btngrams.setTextColor(getResources().getColor(C2002R.C2003color.white));
//        this.status = "1";
//        testcal();
//    }
//
//    public void goldcalcluation() {
//        TextWatcher r0 = new TextWatcher() {
//            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//            }
//
//            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//            }
//
//            public void afterTextChanged(Editable editable) {
//                if (editable == null || editable.toString().equalsIgnoreCase("")) {
//                    Buy_Digitalgold.this.tvresult.setText("");
//                    Buy_Digitalgold.this.btnbuygold.setText(Buy_Digitalgold.this.getResources().getString(C2002R.string.proceed_to_pay));
//                } else if (Buy_Digitalgold.this.etamount.getText().hashCode() == editable.hashCode()) {
//                    Buy_Digitalgold buy_Digitalgold = Buy_Digitalgold.this;
//                    buy_Digitalgold.amountvalue = buy_Digitalgold.etamount.getText().toString();
//                    Buy_Digitalgold buy_Digitalgold2 = Buy_Digitalgold.this;
//                    buy_Digitalgold2.buttonvalidation = buy_Digitalgold2.amountvalue;
//                    Buy_Digitalgold.this.etamount.removeTextChangedListener(Buy_Digitalgold.this.textWatcher);
//                    Buy_Digitalgold.this.etamount.addTextChangedListener(Buy_Digitalgold.this.textWatcher);
//                    if (Buy_Digitalgold.this.etamount.length() == 0) {
//                        Buy_Digitalgold.this.tvresult.setText("");
//                        Buy_Digitalgold.this.btnbuygold.setText(Buy_Digitalgold.this.getResources().getString(C2002R.string.proceed_to_pay));
//                    }
//                    Buy_Digitalgold.this.testcal();
//                }
//            }
//        };
//        this.textWatcher = r0;
//        this.etamount.addTextChangedListener(r0);
//    }
//
//    public void testcal() {
//        if (this.status.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO) && !this.amountvalue.isEmpty()) {
//            if (this.amountvalue.equals(".")) {
//                Toast.makeText(getApplicationContext(), "Enter a valid input", Toast.LENGTH_SHORT).show();
//            } else {
//                String valueOf = String.valueOf(BigDecimal.valueOf(Double.parseDouble(this.amountvalue) / Double.parseDouble(this.liveprice)).setScale(2, RoundingMode.HALF_EVEN));
//                this.finalgold = valueOf;
//                this.tvresult.setText(valueOf);
//                Button button = this.btnbuygold;
//                button.setText(getResources().getString(C2002R.string.proceed_to_pay) + " " + this.amountvalue);
//            }
//        }
//        if (this.status.equals("1") && !this.amountvalue.isEmpty()) {
//            if (this.amountvalue.equals(".")) {
//                Toast.makeText(getApplicationContext(), "Enter a valid input", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            String valueOf2 = String.valueOf(BigDecimal.valueOf(Double.parseDouble(this.liveprice) * Double.parseDouble(this.amountvalue)).setScale(0, RoundingMode.HALF_EVEN));
//            this.finalamount = valueOf2;
//            this.tvresult.setText(valueOf2);
//            Button button2 = this.btnbuygold;
//            button2.setText(getResources().getString(C2002R.string.proceed_to_pay) + " " + this.finalamount);
//        }
//    }
//
//    public void getdata() {
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please Wait....");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        if (!NetworkUtils.isConnected(this)) {
//            ToastMessage.onToast(this, getString(C2002R.string.error_no_internet_connection), ToastMessage.ERROR);
//            progressDialog.dismiss();
//            return;
//        }
//        ApiDao apiDao2 = (ApiDao) ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//        this.apiDao = apiDao2;
//        apiDao2.get_digitalwallet("Bearer " + AccountUtils.getAccessToken(this)).enqueue(new Callback<JsonElement>() {
//            static final /* synthetic */ boolean $assertionsDisabled = false;
//
//            {
//                Class<Buy_Digitalgold> cls = Buy_Digitalgold.class;
//            }
//
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                int code = response.code();
//                if (code == 201 || code == 200) {
//                    try {
//                        JSONObject jSONObject = new JSONObject(new JsonParser().parse(String.valueOf(response.body().getAsJsonObject())).getAsJsonObject().toString()).getJSONObject("balance");
//                        Buy_Digitalgold.this.st_currencyinwords = jSONObject.getString("currencyInWords");
//                        Buy_Digitalgold.this.st_ingrams = jSONObject.getString("humanReadable");
//                        Buy_Digitalgold.this.st_incurrency = jSONObject.getString("inCurrency");
//                        Buy_Digitalgold.this.setview();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    try {
//                        JSONObject jSONObject2 = new JSONObject(response.errorBody().string());
//                        Toast.makeText(Buy_Digitalgold.this, jSONObject2.getString("message"), Toast.LENGTH_SHORT).show();
//                        jSONObject2.getJSONObject("errors");
//                    } catch (IOException | JSONException e2) {
//                        e2.printStackTrace();
//                    }
//                }
//                progressDialog.dismiss();
//            }
//
//            public void onFailure(Call<JsonElement> call, Throwable th) {
//                progressDialog.dismiss();
//                Toast.makeText(Buy_Digitalgold.this, "Technical problem", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void setview() {
//        TextView textView = this.tvgold;
//        textView.setText(this.st_ingrams + " grams");
//    }
//
//    public void init_validation() {
//        this.btnbuygold.setVisibility(View.GONE);
//        this.loading_gif.setVisibility(View.VISIBLE);
//        new Timer().schedule(new TimerTask() {
//            public void run() {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Buy_Digitalgold.this.runOnUiThread(new Runnable() {
//                    public void run() {
//                        Buy_Digitalgold.this.validation();
//                        Buy_Digitalgold.this.btnbuygold.setVisibility(View.VISIBLE);
//                        Buy_Digitalgold.this.loading_gif.setVisibility(View.GONE);
//                    }
//                });
//            }
//        }, 500);
//    }
//
//    public void validation() {
//        this.amountvalue = this.etamount.getText().toString();
//        if (this.isCoupon) {
//            this.stcouponcode = "null";
//            this.stcouponamount = "null";
//            if (this.status.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
//                if (this.amountvalue.isEmpty()) {
//                    ToastMessage.onToast(this, "Please Enter Amount", ToastMessage.ERROR);
//                } else if (Double.parseDouble(this.amountvalue) < 50.0d) {
//                    ToastMessage.onToast(this, "Please Enter Min Rs50", ToastMessage.ERROR);
//                } else if (Double.parseDouble(this.finalgold) > 30.0d) {
//                    openalertdiloug();
//                } else {
//                    lockprice();
//                }
//            } else if (this.amountvalue.isEmpty()) {
//                ToastMessage.onToast(this, "Please Enter Gold", ToastMessage.ERROR);
//            } else if (Double.parseDouble(this.finalamount) < 50.0d) {
//                ToastMessage.onToast(this, "Amount Must be Rs.50", ToastMessage.ERROR);
//            } else if (Double.parseDouble(this.amountvalue) > 30.0d) {
//                openalertdiloug();
//            } else {
//                lockprice();
//            }
//        } else if (this.status.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
//            if (this.amountvalue.isEmpty()) {
//                ToastMessage.onToast(this, "Please Enter Amount", ToastMessage.ERROR);
//            } else if (Double.parseDouble(this.finalgold) > 30.0d) {
//                openalertdiloug();
//            } else {
//                Log.e("Else", "Else checking");
//                couponvalidation();
//            }
//        } else if (this.amountvalue.isEmpty()) {
//            ToastMessage.onToast(this, "Please Enter Gold", ToastMessage.ERROR);
//        } else if (Double.parseDouble(this.amountvalue) > 30.0d) {
//            openalertdiloug();
//        } else {
//            couponvalidation();
//        }
//    }
//
//    public void couponvalidation() {
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please Wait....");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        if (!NetworkUtils.isConnected(this)) {
//            ToastMessage.onToast(this, getString(C2002R.string.error_no_internet_connection), ToastMessage.ERROR);
//            progressDialog.dismiss();
//            return;
//        }
//        if (this.status.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
//            this.passamount = this.amountvalue;
//        } else {
//            this.passamount = this.finalamount;
//        }
//        Log.e("codestatuscode", String.valueOf(this.passamount));
//        ApiDao apiDao2 = (ApiDao) ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//        this.apiDao = apiDao2;
//        apiDao2.couponvalidation("Bearer " + AccountUtils.getAccessToken(this), this.stcouponcode, this.passamount).enqueue(new Callback<CouponsModel>() {
//            static final /* synthetic */ boolean $assertionsDisabled = false;
//
//            {
//                Class<Buy_Digitalgold> cls = Buy_Digitalgold.class;
//            }
//
//            public void onResponse(Call<CouponsModel> call, Response<CouponsModel> response) {
//                int code = response.code();
//                CouponsModel body = response.body();
//                Log.e("codestatuscode", String.valueOf(code));
//                if (code == 200 || code == 202) {
//                    progressDialog.dismiss();
//                    Buy_Digitalgold.this.lockprice();
//                    return;
//                }
//                progressDialog.dismiss();
//                try {
//                    JSONObject jSONObject = new JSONObject(response.errorBody().string());
//                    ToastMessage.onToast(Buy_Digitalgold.this, jSONObject.getString("message"), ToastMessage.ERROR);
//                    JSONObject jSONObject2 = jSONObject.getJSONObject("errors");
//                    try {
//                        JSONArray jSONArray = jSONObject2.getJSONArray(FirebaseAnalytics.Param.COUPON);
//                        for (int i = 0; i < jSONArray.length(); i++) {
//                            ToastMessage.onToast(Buy_Digitalgold.this, jSONArray.getString(i), ToastMessage.ERROR);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        JSONArray jSONArray2 = jSONObject2.getJSONArray("amount");
//                        for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
//                            ToastMessage.onToast(Buy_Digitalgold.this, jSONArray2.getString(i2), ToastMessage.ERROR);
//                        }
//                    } catch (Exception e2) {
//                        e2.printStackTrace();
//                    }
//                } catch (IOException | JSONException e3) {
//                    e3.printStackTrace();
//                }
//            }
//
//            public void onFailure(Call<CouponsModel> call, Throwable th) {
//                progressDialog.dismiss();
//                ToastMessage.onToast(Buy_Digitalgold.this, "We have some issues please try after some time", ToastMessage.ERROR);
//            }
//        });
//    }
//
//    public void openalertdiloug() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View inflate = getLayoutInflater().inflate(C2002R.layout.popup_buygold_screen, (ViewGroup) null);
//        builder.setCancelable(false);
//        builder.setView(inflate);
//        this.alertDialogdialog = builder.create();
//        ImageView imageView = (ImageView) inflate.findViewById(C2002R.C2005id.iv_close);
//        this.imageView_close = imageView;
//        imageView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Buy_Digitalgold.this.alertDialogdialog.dismiss();
//            }
//        });
//        this.alertDialogdialog.show();
//    }
//
//    public void buygold() {
//        if (!this.isCoupon) {
//            double parseDouble = Double.parseDouble(this.taxPercentage);
//            double parseDouble2 = Double.parseDouble(this.stcouponamount);
//            double d = parseDouble2 - (parseDouble * parseDouble2);
//            Log.e("witfinalcaluclation", String.valueOf(d));
//            String valueOf = String.valueOf(d / Double.parseDouble(this.liveprice));
//            this.Coupongold = valueOf;
//            Log.e("withgst@cou", valueOf);
//        } else {
//            this.stcouponcode = "null";
//            this.stcouponamount = "null";
//        }
//        if (!NetworkUtils.isConnected(this)) {
//            ToastMessage.onToast(this, getString(C2002R.string.error_no_internet_connection), ToastMessage.ERROR);
//        } else if (this.status.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
//            Intent intent = new Intent(this, Buy_Gold_Information.class);
//            intent.putExtra("customer_rate", this.amountvalue);
//            intent.putExtra("customer_gold", this.finalgold);
//            intent.putExtra("liveprice", this.liveprice);
//            intent.putExtra("Coupongold", this.Coupongold);
//            intent.putExtra("couponcode", this.stcouponcode);
//            intent.putExtra("isCoupon", this.isCoupon);
//            intent.putExtra("status", this.status);
//            intent.putExtra("taxPercentage", this.taxPercentage);
//            startActivity(intent);
//            Log.e("customer_rate", this.amountvalue);
//            Log.e("customer_gold", this.finalgold);
//        } else if (this.status.equals("1")) {
//            Intent intent2 = new Intent(this, Buy_Gold_Information.class);
//            intent2.putExtra("customer_rate", this.finalamount);
//            intent2.putExtra("customer_gold", this.amountvalue);
//            intent2.putExtra("liveprice", this.liveprice);
//            intent2.putExtra("Coupongold", this.Coupongold);
//            intent2.putExtra("couponcode", this.stcouponcode);
//            intent2.putExtra("isCoupon", this.isCoupon);
//            intent2.putExtra("taxPercentage", this.taxPercentage);
//            intent2.putExtra("status", this.status);
//            startActivity(intent2);
//            Log.e("customer_rate", this.finalamount);
//            Log.e("customer_gold", this.amountvalue);
//        }
//    }
//
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case C2002R.C2005id.btInAmount /*2131361939*/:
//                btnAmount();
//                return;
//            case C2002R.C2005id.btInGrams /*2131361940*/:
//                btGrams();
//                return;
//            case C2002R.C2005id.btn_buygold /*2131361968*/:
//                openBuygoldValidation();
//                return;
//            default:
//                return;
//        }
//    }
//
//    public void openBuygoldValidation() {
//        if (!NetworkUtils.isConnected(this)) {
//            ToastMessage.onToast(this, getString(C2002R.string.error_no_internet_connection), ToastMessage.ERROR);
//        } else {
//            init_validation();
//        }
//    }
//}
