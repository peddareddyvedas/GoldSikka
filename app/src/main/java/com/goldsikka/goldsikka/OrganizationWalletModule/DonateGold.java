package com.goldsikka.goldsikka.OrganizationWalletModule;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.Activitys.Events.EventModel;
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

public class DonateGold extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

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

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_purity)
    TextView tv_purity;


    EditText etAmount, etgold;
    TextView tv_grms, tv_amt;
    String finalgold, finalamount, taxPercentage;

    String storgtid, amountvalue;
    AlertDialog alertDialogdialog;


    boolean isCoupon = true;


    GifImageView loading_gif;
    TextView iv_liveprice;

    LinearLayout llgstamount;
    TextView tvgstamount;

    Button btnamount, btngrams;

    EditText etamount;
    String status = "0";
    TextView tvresult;
    String paybleamount;

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

    TextView tv_paybaleamount;

  String Walletamount;

    String walletamount = "null";

    boolean ischeck = false;

    TextView unameTv, uidTv, titleTv;

    RelativeLayout backbtn;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rupeessymbol)
    TextView rupeessymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_gold);

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
                ProgressDialog dialog = new ProgressDialog(DonateGold.this);
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
        rupeessymbol.setVisibility(View.VISIBLE);
        rupeessymbol.setText("\u20B9");

        et_wallet_money.setHint(Html.fromHtml(getString(R.string.wallet_hint)));

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Donate Gold");


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            storgtid = bundle.getString("org_id");
        }

        initlizeviews();
        goldcalcluation();
        getdata();

        wallet_amount();


        cb_wallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                 @Override
                                                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                     if (isChecked) {
                                                         ischeck = true;
                                                         et_wallet_money.setVisibility(View.VISIBLE);
                                                         et_wallet_money.setText("");
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
                        walletamount = Walletamount;

                        tvwalletamount.setText("Available amount : " + getString(R.string.Rs) + " " + Walletamount);
                        dediactWalletMoney();
                        dialog.dismiss();

                    } else {

                        dialog.dismiss();

                       // ToastMessage.onToast(DonateGold.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {

                    Log.e("on fails", t.toString());

                    dialog.dismiss();

                    //ToastMessage.onToast(DonateGold.this, "We have some issue", ToastMessage.ERROR);
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

            @RequiresApi(api = Build.VERSION_CODES.M)
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

                                tv_paybaleamount.setText(getString(R.string.Rs)+" "+after_dedcation);
                                btnbuygold.setEnabled(true);

                                Log.e("Walletamountddd", "" + Walletamount);
                                String result = Walletamount.replaceAll("[^\\w\\s]", "");
                                String stopEnd = result.substring(0, result.length() - 2);
                                int amou = Integer.parseInt(walletamount);
                                int wall = Integer.parseInt(stopEnd);
                                if (amou > wall) {
                               /* double entervalue = Double.parseDouble(walletamount);
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
                        }catch (NumberFormatException e){

                        }
                    }
                } else {

                    tv_paybaleamount.setText(getString(R.string.Rs)+" "+paybleamount);
                    tv_wallet_error.setText("");
                    walletamount = "null";

                }
            }
        };
        et_wallet_money.addTextChangedListener(textWatcher);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        // finish();
    }

    public void initlizeviews() {

        loading_gif = findViewById(R.id.loading_gif);
        tv_paybaleamount = findViewById(R.id.tv_paybaleamount);

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
                                String llll = listmodel.getBuy_price_per_gram();
                                Log.e("liveprice", liveprice);
                                tv_sellprice.setText(getString(R.string.Rs) + liveprice);
                                tvliverate.setText(getString(R.string.Rs) + llll);
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
                            Toast.makeText(DonateGold.this, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                  //  ToastMessage.onToast(DonateGold.this, "We have some issues", ToastMessage.ERROR);
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
                    String sp1 = Walletamount.replace(",", "");
//                    String wm = et_wallet_money.getText().toString();

//                    ToastMessage.onToast(DonateGold.this, String.valueOf(ischeck), ToastMessage.SUCCESS);
//                    ToastMessage.onToast(DonateGold.this, paybleamount, ToastMessage.SUCCESS);
//                    ToastMessage.onToast(DonateGold.this, sp1, ToastMessage.SUCCESS);

                    try {
                        Double ss = Double.parseDouble(paybleamount);
                        Double sp = Double.parseDouble(sp1);
//                        Double wm1 = Double.parseDouble(wm);

//                        ToastMessage.onToast(DonateGold.this, String.valueOf(ss), ToastMessage.SUCCESS);
//                        ToastMessage.onToast(DonateGold.this, String.valueOf(sp1), ToastMessage.SUCCESS);
//                        ToastMessage.onToast(DonateGold.this, String.valueOf(wm), ToastMessage.SUCCESS);
                        if (cb_wallet.isChecked()) {
                            if (Double.parseDouble(et_wallet_money.getText().toString()) <= Double.parseDouble(Walletamount.replace(",", ""))) {
                                if (tv_paybaleamount.getText().toString().substring(2).equals("0")) {
//                                    ToastMessage.onToast(DonateGold.this, "only Booking Account", ToastMessage.SUCCESS);
                                    buy_wallet_money();
                                    return;
                                }
                            }
                            if (Integer.parseInt(DonateGold.this.et_wallet_money.getText().toString()) == 0 && !tv_paybaleamount.getText().toString().substring(2).equals("0")) {
                                ToastMessage.onToast(DonateGold.this, "only razorpay", ToastMessage.SUCCESS);
                                onpayment();
                            } else if (Double.parseDouble(et_wallet_money.getText().toString()) <= Double.parseDouble(DonateGold.this.Walletamount.replace(",", "")) && !tv_paybaleamount.getText().toString().substring(2).equals("0")) {
                                ToastMessage.onToast(DonateGold.this, "wallet and razorpay", ToastMessage.SUCCESS);
                                onpayment();
                            } else if (Double.parseDouble(et_wallet_money.getText().toString()) > Double.parseDouble(DonateGold.this.Walletamount.replace(",", ""))) {
                                ToastMessage.onToast(DonateGold.this, "Insufficient Balance", ToastMessage.ERROR);
                            } else {
                                ToastMessage.onToast(DonateGold.this, "Please try again", ToastMessage.ERROR);
                            }
                        } else {
                            onpayment();
                        }
                    }catch (Exception e){
                        Log.e("Exception", ""+e);
                    }
                }
                    // openpayment();
//                    btnbuygold.setEnabled(true);
//                    btnbuygold.setBackgroundResource(R.drawable.buttonborder);

                }

            @Override
            public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                Log.e("lockratesfail", t.toString());
                //ToastMessage.onToast(DonateGold.this, "We have some issues Try After some Time", ToastMessage.ERROR);

            }
        });

    }

    public void buy_wallet_money() {

//                    ToastMessage.onToast(this, "HIIIIIIIIIIIIII", ToastMessage.ERROR);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }
        else {
            //  if (stcouponcode.equals("null")) {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getdetails = apiDao.buywalletAmount_org("Bearer " + AccountUtils.getAccessToken(this), etamount.getText().toString(), et_wallet_money.getText().toString(), storgtid);
            getdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
//                    ToastMessage.onToast(DonateGold.this, String.valueOf(statuscode), ToastMessage.SUCCESS);
                    Log.e("statuscodedd", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        Listmodel list = response.body();
                        if (list.getPurchase_Status()) {
                            stdescription = list.getDescription();
                            sttransactionId = list.getTransactionId();
                            onsuceess();
                        }

                        dialog.dismiss();

                    } else {

                        dialog.dismiss();

                      //  ToastMessage.onToast(DonateGold.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                    onsuceess();

//                    ToastMessage.onToast(DonateGold.this, "We have some issue", ToastMessage.ERROR);
                }
            });
        }
    }


    public void btnAmount() {

        rupeessymbol.setVisibility(View.VISIBLE);
        tvresult.setVisibility(View.VISIBLE);
        rupeessymbol.setText("\u20B9");

        amountvalue = etamount.getText().toString();
        etamount.setHint(Html.fromHtml(getString(R.string.amount)));
        tvchange.setText("Enter Amount");
        btngrams.setText("DONATE IN GRAMS");

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
        etamount.setHint(Html.fromHtml(getString(R.string.grams)));
        btnamount.setText("DONATE IN AMOUNT");
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
                            tvresult.setText("");
                            tvgstamount.setText("");
                            tv_paybaleamount.setText("");

                        }
                        //status = "0";
                        testcal();

                    }

                }
                else {



                    cb_wallet.setEnabled(false);
                    cb_wallet.setChecked(false);

                    et_wallet_money.setVisibility(View.GONE);
                    tv_wallet_error.setVisibility(View.GONE);


                    tvresult.setText("");
                    tvgstamount.setText("");
                    paybleamount = "0";
                    tv_paybaleamount.setText("");
//                    paybleamount = "";


                }

            }

            @Override
            public void afterTextChanged(Editable charSequence) {


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

                    tvresult.setText(finalgold+ " g");

                    finalamount = amountvalue;
                    double itax = Double.parseDouble(taxPercentage);
                    double astamount = Double.parseDouble(amountvalue);
                    double dtotal = itax * astamount;
                    double finalcaluclation = dtotal + astamount;
                    BigDecimal bf = BigDecimal.valueOf(finalcaluclation).setScale(0, RoundingMode.HALF_UP);
                    tv_paybaleamount.setText(getString(R.string.Rs)+" "+String.valueOf(bf));
                    tvgstamount.setText(getString(R.string.Rs)+" "+String.valueOf(bf));
                    paybleamount = String.valueOf(bf);

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
                    //  btnbuygold.setText(getResources().getString(R.string.proceed_to_pay) + " " + finalamount);
                    double itax = Double.parseDouble(taxPercentage);
                    double astamount = Double.parseDouble(finalamount);
                    double dtotal = itax * astamount;
                    double finalcaluclation = dtotal + astamount;
                    BigDecimal bf = BigDecimal.valueOf(finalcaluclation).setScale(0, RoundingMode.HALF_EVEN);
                    tv_paybaleamount.setText(getString(R.string.Rs)+" "+String.valueOf(bf));
                    tvgstamount.setText(getString(R.string.Rs)+" "+String.valueOf(bf));
                    paybleamount = String.valueOf(bf);
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
                            Toast.makeText(DonateGold.this, st, Toast.LENGTH_SHORT).show();
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
                   // Toast.makeText(DonateGold.this, "Technical problem", Toast.LENGTH_SHORT).show();
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

                DonateGold.this.runOnUiThread(new Runnable() {
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
                ToastMessage.onToast(DonateGold.this, "Please Enter Amount", ToastMessage.ERROR);
            } else if (Double.parseDouble(amountvalue) < 100) {
                ToastMessage.onToast(DonateGold.this, "Please Enter Min Rs100", ToastMessage.ERROR);
            } else if (Double.parseDouble(finalgold) > 30) {
                openalertdiloug();
            } else {
                lockprice();
            }

        } else {
            if (amountvalue.isEmpty()) {
                ToastMessage.onToast(DonateGold.this, "Please Enter Gold", ToastMessage.ERROR);
            } else if (Double.parseDouble(finalamount) < 100) {
                ToastMessage.onToast(DonateGold.this, "Amount Must be Rs.100", ToastMessage.ERROR);
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
    //    co.setKeyID("rzp_live_uvxtS5LwJPMIOP");

        paybleamount = tv_paybaleamount.getText().toString().substring(2);

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
        intent.putExtra("from", "ORG");
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
//        if(!et_wallet_money.getText().toString().equals("")){
//            buy_wallet_money();
//        }
        sendgift(s);
        Log.e("payid", s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        walletamount = "null";

        etamount.getText().clear();
        cb_wallet.setChecked(false);
       // tv_paybaleamount.setText("PROCEED TO GIFT");
        ToastMessage.onToast(this,"Payment failed",ToastMessage.ERROR);
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
            Log.e("storgid", ""+storgtid);
            Log.e("storgid", ""+payid);
            Log.e("storgid", ""+finalamount);
            Log.e("storgid", ""+et_wallet_money.getText().toString());
            Call<EventModel> gift = apiDao.Donategold("Bearer " + AccountUtils.getAccessToken(this), storgtid, payid, finalamount, et_wallet_money.getText().toString());
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
                        Log.e("422", "422");
                        dialog.dismiss();
                        onpaymenterror();
                    } else {
                        Log.e("422", ""+statuscode);
                        dialog.dismiss();
                        onpaymenterror();
                    }
                }

                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {
                    dialog.dismiss();
                    onpaymenterror();
                 //   ToastMessage.onToast(DonateGold.this, "We have some issues try after some time", ToastMessage.ERROR);
                }
            });
        }
    }
}