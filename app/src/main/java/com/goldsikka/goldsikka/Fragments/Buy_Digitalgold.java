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
    @BindView(R.id.tvlocation)
    TextView tvlocation;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvtime)
    TextView tvtime;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvdate)
    TextView tvdate;
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
                                tvdate.setText(listmodel.getDate());
                                tvtime.setText(listmodel.getTime());
                                tvlocation.setText(listmodel.getLocation());
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
    }
    @Override
    public void onBackPressed() {
//        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainFragmentActivity.class));
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
        rupeessymbol.setText("g");
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


