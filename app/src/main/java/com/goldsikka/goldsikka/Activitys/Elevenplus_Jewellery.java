package com.goldsikka.goldsikka.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.Activitys.Coupons.CouponsList;
import com.goldsikka.goldsikka.Activitys.Coupons.CouponsModel;
import com.goldsikka.goldsikka.Activitys.Profile.CustomerAddressList;
import com.goldsikka.goldsikka.Fragments.Buy_Digitalgold;
import com.goldsikka.goldsikka.Fragments.Buy_Gold_Information;
import com.goldsikka.goldsikka.Fragments.Successpopup;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

@SuppressLint({"NewApi", "SetTextI18n"})
public class Elevenplus_Jewellery extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_lvprice)
    TextView tv_lvprice;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_mmiamount)
    TextView tv_mmiamount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_jew_gst)
    TextView tv_jew_gst;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etreferralcode)
    EditText etreferalcode;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_jew_grams)
    TextView tv_jew_grams;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_jew_prsfee)
    TextView tv_jew_prsfee;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_final_amount)
    TextView tv_final_amount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spin_amount)
    Spinner spin_amount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvrefresh)
    TextView tvrefresh;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_jew_buy)
    Button bt_jew_buy;
    GifImageView loading_gif;

//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.tvschemename)
//    TextView tvschemename;


    String finial_amount;
    String refcode = "";

    ApiDao apiDao;
    String schemeId, schemename;

    String after_dedcation = "null";
    String st_liveprice, st_processingfee, st_grams, st_finalamount, st_mmiamount, st_gst, sublistamount, SUB_LISTAMOUNT = "";
    String[] spn_amount = {"Select Monthly Amount", "1000", "1500", "2000", "2500",
            "3000", "3500", "4000", "4500", "5000"};


    ArrayList<String> spn_list;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_wallet_money)
    TextView tvwalletamount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.wallet_check)
    CheckBox cb_wallet;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_referal_code_error)
    TextView tv_referal_code_error;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_wallet_money)
    EditText et_wallet_money;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_wallet_error)
    TextView tv_wallet_error;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_head_final_amount)
    TextView tv_head_final_amount;

    String Walletamount;

    String walletamount = "null";

    TextWatcher textWatcher;

    boolean ischeck = false;

    String pp = "yes";

    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;

    LinearLayout opencoupon, openoffers;
    TextView tvofferremove, tv_discountamount;
    String stcouponcode = "null";
    String stcouponamount = "null";
    boolean isCoupon = true;

    String minamount = "yes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elevenplus__jewellery);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            schemeId = bundle.getString("id");
            schemename = bundle.getString("schemename");
        }
        Log.e("dfvdf", "" + schemeId);
        Log.e("dfvdf", "" + schemename);
        et_wallet_money.setHint(Html.fromHtml(getString(R.string.wallet_hint)));
        loading_gif = findViewById(R.id.loading_gif);
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);
        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Gold Plus Plan");
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initializviews();
        getliveprices();
        wallet_amount();
        checkboxClick();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tvofferremove)
    public void offerremove() {
        opencoupon.setVisibility(View.VISIBLE);
        openoffers.setVisibility(View.GONE);
        isCoupon = true;
        stcouponcode = "null";
        stcouponamount = "null";
    }

    public void checkboxClick() {
        cb_wallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                 @Override
                                                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                     if (isChecked) {
                                                         ischeck = true;
                                                         et_wallet_money.setVisibility(View.VISIBLE);
                                                         et_wallet_money.setText("");
                                                         tv_head_final_amount.setText("Payable Amount");
                                                         setdetails();
                                                     } else {
                                                         tv_head_final_amount.setText("Final Amount");
                                                         bt_jew_buy.setEnabled(true);
                                                         ischeck = false;
                                                         et_wallet_money.setVisibility(View.GONE);
                                                         tv_wallet_error.setVisibility(View.GONE);
                                                         setdetails();
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
                        //    ToastMessage.onToast(Elevenplus_Jewellery.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                   // ToastMessage.onToast(Elevenplus_Jewellery.this, "We have some issue", ToastMessage.ERROR);
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

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && !charSequence.toString().equalsIgnoreCase("")) {
                    if (et_wallet_money.getText().hashCode() == charSequence.hashCode()) {
                        walletamount = et_wallet_money.getText().toString();
                        tv_wallet_error.setVisibility(View.VISIBLE);
                        try {
                            if (Double.parseDouble(finial_amount) >= Double.parseDouble(walletamount)) {
                                tv_wallet_error.setVisibility(View.GONE);
                                Double minus = Double.parseDouble(finial_amount) - Double.parseDouble(walletamount);
                                after_dedcation = String.valueOf(minus);
//                                ToastMessage.onToast(Elevenplus_Jewellery.this, after_dedcation, ToastMessage.SUCCESS);
                                tv_final_amount.setText(": " + getResources().getString(R.string.Rs) + after_dedcation);
                                bt_jew_buy.setEnabled(true);
                                /*double entervalue = Double.parseDouble(walletamount);
                                double walletvalue = Double.parseDouble(Walletamount);
                                if (entervalue > walletvalue) {*/

                                Log.e("Walletamountddd", "" + Walletamount);
                                String result = Walletamount.replaceAll("[^\\w\\s]", "");
                                String stopEnd = result.substring(0, result.length() - 2);
                                int amou = Integer.parseInt(walletamount);
                                int wall = Integer.parseInt(stopEnd);
                                if (amou > wall) {
                                    tv_wallet_error.setVisibility(View.VISIBLE);
                                    bt_jew_buy.setEnabled(false);
                                    bt_jew_buy.setBackgroundResource(R.drawable.backgroundvisablity);
                                    tv_wallet_error.setText("You can't enter more then " + Walletamount);
                                    tv_wallet_error.setTextColor(getColor(R.color.red));
                                }
                            } else {
                                tv_final_amount.setText(": " + getResources().getString(R.string.Rs) + finial_amount);
                                bt_jew_buy.setEnabled(false);
                                bt_jew_buy.setBackgroundResource(R.drawable.backgroundvisablity);
                                tv_wallet_error.setText("You can't enter more than Payable Amount");
                                tv_wallet_error.setTextColor(getColor(R.color.red));
                            }
                            et_wallet_money.removeTextChangedListener(textWatcher);
                            et_wallet_money.addTextChangedListener(textWatcher);
                        } catch (NumberFormatException e) {

                        }
                    }
                } else {
                    tv_final_amount.setText(": " + getResources().getString(R.string.Rs) + finial_amount);
                    tv_wallet_error.setText("");
                    walletamount = "null";
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void afterTextChanged(Editable charSequence) {
            }
        };
        et_wallet_money.addTextChangedListener(textWatcher);
    }

    @OnClick(R.id.tvrefresh)
    public void liveraterefresh() {
        getliveprices();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initializviews() {
        spin_amount = findViewById(R.id.spin_amount);
        ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spn_amount);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_amount.setAdapter(spinner_adapter);
        spinnerclick();
        etreferalcode = findViewById(R.id.etreferralcode);
        opencoupon = findViewById(R.id.llopencoupon);
        openoffers = findViewById(R.id.llofferremove);
        tvofferremove = findViewById(R.id.tvofferremove);
        tv_discountamount = findViewById(R.id.tv_discountamount);
        opencoupon.setOnClickListener(this);
    }

    public String couponvalidation() {
        String[] sts = {"success"};
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();

        } else {
            Log.e("codestatuscode", finial_amount);
            Log.e("stcouponcode", stcouponcode);
            Log.e("stcouponamount", stcouponamount);
            String ffamount = String.valueOf(Double.parseDouble(finial_amount) + Double.parseDouble(stcouponamount));
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<CouponsModel> invalidation = apiDao.couponvalidation("Bearer " + AccountUtils.getAccessToken(this), stcouponcode, ffamount);
            invalidation.enqueue(new Callback<CouponsModel>() {
                @Override
                public void onResponse(Call<CouponsModel> call, Response<CouponsModel> response) {
                    int statuscode = response.code();
                    CouponsModel model = response.body();
                    Log.e("codestatuscode", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_ACCEPTED) {
                        dialog.dismiss();
//                        ToastMessage.onToast(Elevenplus_Jewellery.this, "Coupon Validation Successful", ToastMessage.SUCCESS);
                        sts[0] = "success";
                    } else if (statuscode == 404) {

                        try {
                            if (null != response.errorBody()) {
                                String errorResponse = response.errorBody().string();
                                Log.e("bstatus", " " + errorResponse);
                                JSONObject jObjError = new JSONObject(errorResponse);
                                String st = jObjError.getString("message");
                                Log.e("bstatus", " " + st);
                                ToastMessage.onToast(getApplicationContext(), st, ToastMessage.ERROR);


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //  ToastMessage.onToast(getApplicationContext(), "Please try again after 24 hours", ToastMessage.ERROR);
                    } else {
                        dialog.dismiss();
                        assert response.errorBody() != null;
                        try {

                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
//                            ToastMessage.onToast(Elevenplus_Jewellery.this,st,ToastMessage.ERROR);
                            JSONObject er = jObjError.getJSONObject("errors");
                            try {
                                JSONArray password = er.getJSONArray("coupon");
                                for (int i = 0; i < password.length(); i++) {
                                    ToastMessage.onToast(Elevenplus_Jewellery.this, password.getString(i), ToastMessage.ERROR);
                                    sts[0] = "failed";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray password = er.getJSONArray("amount");
                                for (int i = 0; i < password.length(); i++) {
//                                    ToastMessage.onToast(Elevenplus_Jewellery.this,password.getString(i),ToastMessage.ERROR);
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
                 //   ToastMessage.onToast(Elevenplus_Jewellery.this, "We have some issues please try after some time"
                            //, ToastMessage.ERROR);
                }
            });
        }
        return sts[0];
    }


    public void spinnerclick() {
        spn_list = new ArrayList<>();
        spin_amount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sublistamount = spin_amount.getItemAtPosition(spin_amount.getSelectedItemPosition()).toString();

                ((TextView) view).setTextColor(ContextCompat.getColor(Elevenplus_Jewellery.this, R.color.textcolorprimary));

                if (sublistamount.equals("Select Monthly Amount")) {
                    cb_wallet.setEnabled(false);
                    cb_wallet.setChecked(false);
                    et_wallet_money.setVisibility(View.GONE);
                    tv_wallet_error.setVisibility(View.GONE);

                    tv_lvprice.setText(getString(R.string.Rs) + "0" + "/gram");
                    tv_final_amount.setText(": " + getResources().getString(R.string.Rs) + "0");
                    tv_jew_gst.setText(": " + getResources().getString(R.string.Rs) + "0");
                    tv_jew_grams.setText(": " + "0" + " g");
                    tv_jew_prsfee.setText(": " + getResources().getString(R.string.Rs) + "0");
                    tv_mmiamount.setText(": " + getResources().getString(R.string.Rs) + "0");
                    tv_discountamount.setText(": " + getResources().getString(R.string.Rs) + "0");
                } else {
                    cb_wallet.setEnabled(true);
                }

                if (sublistamount.equals("1000")) {
                    SUB_LISTAMOUNT = "1000";
                    calculation(SUB_LISTAMOUNT);
                    cb_wallet.setChecked(false);
                } else if (sublistamount.equals("1500")) {
                    SUB_LISTAMOUNT = "1500";
                    calculation(SUB_LISTAMOUNT);
                    cb_wallet.setChecked(false);
                } else if (sublistamount.equals("2000")) {
                    SUB_LISTAMOUNT = "2000";
                    calculation(SUB_LISTAMOUNT);
                    cb_wallet.setChecked(false);
                } else if (sublistamount.equals("2500")) {
                    SUB_LISTAMOUNT = "2500";
                    calculation(SUB_LISTAMOUNT);
                    cb_wallet.setChecked(false);
                } else if (sublistamount.equals("3000")) {
                    SUB_LISTAMOUNT = "3000";
                    calculation(SUB_LISTAMOUNT);
                    cb_wallet.setChecked(false);
                } else if (sublistamount.equals("3500")) {
                    SUB_LISTAMOUNT = "3500";
                    calculation(SUB_LISTAMOUNT);
                    cb_wallet.setChecked(false);
                } else if (sublistamount.equals("4000")) {
                    SUB_LISTAMOUNT = "4000";
                    calculation(SUB_LISTAMOUNT);
                    cb_wallet.setChecked(false);
                } else if (sublistamount.equals("4500")) {
                    SUB_LISTAMOUNT = "4500";
                    calculation(SUB_LISTAMOUNT);
                    cb_wallet.setChecked(false);
                } else if (sublistamount.equals("5000")) {
                    SUB_LISTAMOUNT = "5000";
                    calculation(SUB_LISTAMOUNT);
                    cb_wallet.setChecked(false);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
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
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getliverates = apiDao.getlive_rates22carats("Bearer " + AccountUtils.getAccessToken(this));
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
                                st_liveprice = listmodel.getSell_price_per_gram();
//                                String dsfd = listmodel.getBuy_price_per_gram();
                                tv_lvprice.setText(getResources().getString(R.string.Rs) + " " + st_liveprice + "/gram");
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
//                            Toast.makeText(Elevenplus_Jewellery.this, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                   // ToastMessage.onToast(Elevenplus_Jewellery.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

    public void calculation(String amount) {
        if (sublistamount.equals("Select Monthly Amount")) {

        } else {
            opencalculation(amount);
        }
    }

    public void opencalculation(String selected_amount) {
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
            Call<Listmodel> getjewelleery = apiDao.getelevenplus_calculation("Bearer " + AccountUtils.getAccessToken(this),
                    schemeId, selected_amount);
            getjewelleery.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Listmodel listmodel = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {
                        dialog.dismiss();

                        st_processingfee = listmodel.getProcessingFee();
                        st_grams = listmodel.getGramsEmi();
                        finial_amount = listmodel.getFinalAmount();
                        st_gst = listmodel.getGst();
                        st_mmiamount = listmodel.getEmiAmount();
                        st_liveprice = listmodel.getLivePrice();
                        setdetails();
                    } else {
                        dialog.dismiss();
                        //  ToastMessage.onToast(Elevenplus_Jewellery.this, "Technical issues", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                   // ToastMessage.onToast(Elevenplus_Jewellery.this, "We have some issues", ToastMessage.ERROR);

                }
            });
        }

    }

    public void setdetails() {
        // llliveprice.setVisibility(View.VISIBLE);
        tv_lvprice.setText(getString(R.string.Rs) + st_liveprice + "/gram");
        tv_final_amount.setText(": " + this.getResources().getString(R.string.Rs) + finial_amount);
        tv_jew_gst.setText(": " + this.getResources().getString(R.string.Rs) + st_gst);
        tv_jew_grams.setText(": " + st_grams + " g");
        tv_jew_prsfee.setText(": " + this.getResources().getString(R.string.Rs) + st_processingfee);
        tv_mmiamount.setText(": " + this.getResources().getString(R.string.Rs) + st_mmiamount);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.bt_jew_buy)
    public void init_validation(View v) {
        bt_jew_buy.setVisibility(View.GONE);
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

                Elevenplus_Jewellery.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        validation();
                        bt_jew_buy.setVisibility(View.VISIBLE);
                        loading_gif.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);
    }

    public void validation() {

        if (sublistamount.equals("Select Monthly Amount")) {
            ToastMessage.onToast(Elevenplus_Jewellery.this, "Please select the Amount", ToastMessage.ERROR);

        } else {
            if (!NetworkUtils.isConnected(this)) {
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                return;
            } else {
//               referalCode_validation();
                if (!NetworkUtils.isConnected(this)) {
                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                } else {
                    refcode = etreferalcode.getText().toString().trim();
                    if (isCoupon && !cb_wallet.isChecked() && refcode.equals("")) { //NNN
                        openpayment();
                    } else if (!isCoupon && !cb_wallet.isChecked() && refcode.equals("")) { //YNN
                        String sts = couponvalidation();
//                    ToastMessage.onToast(Mygold2020Form_Activity.this, sts, ToastMessage.ERROR);
                        if (sts.equals("success"))
                            openpayment();
                        else
                            ToastMessage.onToast(Elevenplus_Jewellery.this, "Invalid Coupon", ToastMessage.ERROR);
                    } else if (isCoupon && cb_wallet.isChecked() && refcode.equals("")) { //NYN
                        String sts = walletvalidation();
                        if (sts.equals("success"))
                            buy_wallet_money("", "");
                        else if (sts.equals("half")) {
//                            ToastMessage.onToast(Elevenplus_Jewellery.this, sts, ToastMessage.SUCCESS);
                            openpayment();
//                            buy_wallet_money(stcouponcode, "");

//                            ToastMessage.onToast(Elevenplus_Jewellery.this, "Invalid Code", ToastMessage.ERROR);
                        } else {
                            Log.e("failed", "check sts");
                        }
                    } else if (isCoupon && !cb_wallet.isChecked() && !refcode.equals("")) { //NNY
                        String sts = referalCode_validation();
                        if (sts.equals("success"))
                            openpayment();
                        else
                            ToastMessage.onToast(Elevenplus_Jewellery.this, "Invalid Referral Code", ToastMessage.ERROR);
                    } else if (!isCoupon && cb_wallet.isChecked() && refcode.equals("")) { //YYN
                        String sts = walletvalidation();
                        String sts1 = couponvalidation();
                        if (sts.equals("success"))
                            if (sts1.equals("success"))
                                buy_wallet_money(stcouponcode, "");
                            else
                                ToastMessage.onToast(Elevenplus_Jewellery.this, "Try Again", ToastMessage.ERROR);
                        else if (sts.equals("half")) {
                            openpayment();
//                            buy_wallet_money(stcouponcode, "");
//                            ToastMessage.onToast(Elevenplus_Jewellery.this, "Invalid Code", ToastMessage.ERROR);
                        } else {
                            Log.e("failed", "check sts");
                        }
                    } else if (!isCoupon && !cb_wallet.isChecked() && !refcode.equals("")) { //YNY
                        String sts = couponvalidation();
                        String sts1 = referalCode_validation();

//                    ToastMessage.onToast(Mygold2020Form_Activity.this, sts, ToastMessage.SUCCESS);
//                    ToastMessage.onToast(Mygold2020Form_Activity.this, "sts1", ToastMessage.SUCCESS);
                        if (sts.equals("success"))
                            if (sts1.equals("success"))
                                openpayment();
                            else
                                ToastMessage.onToast(Elevenplus_Jewellery.this, "Referral Code Validation Error", ToastMessage.ERROR);
                        else if (sts.equals("half")) {
                            openpayment();
                            buy_wallet_money(stcouponcode, "");
//                            ToastMessage.onToast(Elevenplus_Jewellery.this, "Invalid Code", ToastMessage.ERROR);
                        } else {
                            Log.e("failed", "check sts");
                        }
                    } else if (isCoupon && cb_wallet.isChecked() && !refcode.equals("")) { //NYY
                        String sts = walletvalidation();
                        String sts1 = referalCode_validation();
                        if (sts.equals("success"))
                            if (sts1.equals("success"))
                                buy_wallet_money("", etreferalcode.getText().toString());
                            else
                                ToastMessage.onToast(Elevenplus_Jewellery.this, "Referral Code Validation Error", ToastMessage.ERROR);
                        else if (sts.equals("half")) {
                            openpayment();
                            buy_wallet_money(stcouponcode, "");
//                            ToastMessage.onToast(Elevenplus_Jewellery.this, "Invalid Code", ToastMessage.ERROR);
                        } else {
                            Log.e("failed", "check sts");
                        }
                    } else if (!isCoupon && cb_wallet.isChecked() && !refcode.equals("")) { //YYY
                        String sts = walletvalidation();
                        String sts1 = referalCode_validation();
                        String sts2 = couponvalidation();
                        if (sts.equals("success"))
                            if (sts1.equals("success"))
                                if (sts2.equals("success"))
                                    buy_wallet_money(stcouponcode, etreferalcode.getText().toString());
                                else
                                    ToastMessage.onToast(Elevenplus_Jewellery.this, "Coupon code validation Error", ToastMessage.ERROR);
                            else
                                ToastMessage.onToast(Elevenplus_Jewellery.this, "Referral Code Validation Error", ToastMessage.ERROR);
                        else if (sts.equals("half")) {
                            openpayment();
                            buy_wallet_money(stcouponcode, "");
//                            ToastMessage.onToast(Elevenplus_Jewellery.this, "Invalid Code", ToastMessage.ERROR);
                        } else {
                            Log.e("failed", "check sts");
                        }
                    }

//                if(!isCoupon){
//                    couponvalidation();
//                    amt_ed.setVisibility(View.GONE);
//                    referalCode_validation();
//                }else {
//                    amt_ed.setVisibility(View.GONE);
//                    referalCode_validation();
//                }
                }
            }
            // onsuccess();
        }
    }

    private String walletvalidation() {
        String etmoney = et_wallet_money.getText().toString().trim();

        if (etmoney.equals("")) {
            ToastMessage.onToast(Elevenplus_Jewellery.this, "Please Enter Booking Account Amount", ToastMessage.SUCCESS);
            return "failed";
        } else if (Double.parseDouble(etmoney) <= Double.parseDouble(walletamount)) {
//            ToastMessage.onToast(Elevenplus_Jewellery.this, "Money Wallet Validation Successful", ToastMessage.SUCCESS);
            if (Double.parseDouble(etmoney) == Double.parseDouble(finial_amount)) {
//                ToastMessage.onToast(Elevenplus_Jewellery.this, "Money Wallet Validation Successful", ToastMessage.SUCCESS);
                return "success";
            } else {
//                ToastMessage.onToast(Elevenplus_Jewellery.this, "Money Wallet Validation Successful", ToastMessage.SUCCESS);
                return "half";
            }
        } else {
            ToastMessage.onToast(Elevenplus_Jewellery.this, "Insufficient Balance", ToastMessage.SUCCESS);
            return "failed";
        }
    }


    public String referalCode_validation() {
        final String[] vv = {""};
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return "null";
        } else {
            //  if (stcouponcode.equals("null")) {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getdetails = apiDao.referCodeValidation("Bearer " + AccountUtils.getAccessToken(this), etreferalcode.getText().toString().trim());
            getdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode dd", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        Listmodel list = response.body();
                        assert list != null;
                        if (list.isIs_referral()) {
                            openPayment();
                        }
                        // Log.e("Wallet Money Responce",new Gson().toJson(response.body()));
                        dialog.dismiss();

                        vv[0] = "success";

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
                        vv[0] = "failed";
                        //  ToastMessage.onToast(Elevenplus_Jewellery.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                   // ToastMessage.onToast(Elevenplus_Jewellery.this, "We have some issue", ToastMessage.ERROR);
                    vv[0] = "failed";
                }
            });
        }
        return vv[0];
    }

    public void openPayment() {
        if (ischeck) {

            if (walletamount.equals("null")) {
                ToastMessage.onToast(Elevenplus_Jewellery.this, "Please enter amount", ToastMessage.ERROR);
            } else {


                if (after_dedcation.equals("null")) {

                    Log.e("payble amount if", st_finalamount);
                } else {
                    Log.e("payble amount else", st_finalamount);

                    st_finalamount = after_dedcation;
//                        finial_amount = st_finalamount;

                }

                openpayment();
            }
        } else {
            openpayment();
        }

    }


    public void buy_wallet_money(String coupon, String referral) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
//            ToastMessage.onToast(Elevenplus_Jewellery.this, et_wallet_money.getText().toString(), ToastMessage.SUCCESS);
//            ToastMessage.onToast(Elevenplus_Jewellery.this, SUB_LISTAMOUNT, ToastMessage.SUCCESS);
//            ToastMessage.onToast(Elevenplus_Jewellery.this, st_liveprice, ToastMessage.SUCCESS);
//            ToastMessage.onToast(Elevenplus_Jewellery.this, SUB_LISTAMOUNT, ToastMessage.SUCCESS);
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<Listmodel> getdetails;
            if (coupon.equals("null")) {

                getdetails = apiDao.buywalletAmount_elevenscheme("Bearer " + AccountUtils.getAccessToken(this)
                        , schemeId, "12", SUB_LISTAMOUNT, st_liveprice, et_wallet_money.getText().toString(), "");
            } else {
                getdetails = apiDao.buywalletAmount_elevenscheme("Bearer " + AccountUtils.getAccessToken(this)
                        , schemeId, "12", SUB_LISTAMOUNT, st_liveprice, et_wallet_money.getText().toString(), coupon);
            }

            getdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode dd", String.valueOf(statuscode));
//                    ToastMessage.onToast(Elevenplus_Jewellery.this, String.valueOf(statuscode), ToastMessage.ERROR);
//                    ToastMessage.onToast(Elevenplus_Jewellery.this, schemeId, ToastMessage.ERROR);
//                    ToastMessage.onToast(Elevenplus_Jewellery.this, SUB_LISTAMOUNT, ToastMessage.ERROR);
//                    ToastMessage.onToast(Elevenplus_Jewellery.this, st_liveprice, ToastMessage.ERROR);
//                    ToastMessage.onToast(Elevenplus_Jewellery.this, Walletamount, ToastMessage.ERROR);
//                    ToastMessage.onToast(Elevenplus_Jewellery.this, coupon, ToastMessage.ERROR);
                    if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == 201) {
                        Listmodel list = response.body();
                        boolean payment_status = list.getPurchase_Status();
                        if (payment_status) {
                            openpopupscreen(list.getDescription());
                        } else {
                            onpaymenterror();
                            ToastMessage.onToast(Elevenplus_Jewellery.this, "Payment is Failed", ToastMessage.ERROR);
                        }
                        // Log.e("Wallet Money Responce",new Gson().toJson(response.body()));
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        // ToastMessage.onToast(Elevenplus_Jewellery.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                    openpopupscreen("Successfully Subscribed to Gold Plus Plan");
//                    ToastMessage.onToast(Elevenplus_Jewellery.this, "We have some issue", ToastMessage.ERROR);
                }
            });
        }
    }

    private void subscribe(String paymentid, String coupon, String wallet, String referral) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {
//            ToastMessage.onToast(Elevenplus_Jewellery.this, coupon, ToastMessage.SUCCESS);
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getsubscribedetails = null;
            if (coupon.equals("")) {
                if (wallet.equals("")) {
                    if (referral.equals("")) {
                        getsubscribedetails = apiDao.getelevensubscribedetails("Bearer " + AccountUtils.getAccessToken(this),
                                schemeId,
                                paymentid,
                                "12",
                                SUB_LISTAMOUNT,
                                st_liveprice,
                                et_wallet_money.getText().toString(),
                                "",
                                "");
                    } else {
                        getsubscribedetails = apiDao.getelevensubscribedetails("Bearer " + AccountUtils.getAccessToken(this),
                                schemeId,
                                paymentid,
                                "12",
                                SUB_LISTAMOUNT,
                                st_liveprice,
                                et_wallet_money.getText().toString(),
                                referral,
                                "");
                    }
                } else {
                    ToastMessage.onToast(Elevenplus_Jewellery.this, "Exceptional", ToastMessage.ERROR);
                }
            } else {
                getsubscribedetails = apiDao.getelevensubscribedetails("Bearer " + AccountUtils.getAccessToken(this),
                        schemeId,
                        paymentid,
                        "12",
                        SUB_LISTAMOUNT,
                        st_liveprice,
                        et_wallet_money.getText().toString(),
                        "",
                        coupon);
            }

            assert getsubscribedetails != null;
            getsubscribedetails.enqueue(new Callback<Listmodel>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(@NotNull Call<Listmodel> call, @NotNull Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode gold plus plan", String.valueOf(statuscode));
                    Listmodel listmodel = response.body();
                    dialog.dismiss();
                    if (statuscode == 200 || statuscode == 201) {
                        assert listmodel != null;
                        openpopupscreen(listmodel.getDescription());
                    } else if (statuscode == 422) {
                        dialog.dismiss();
                        Log.e("iuhi", paymentid);
//                        ToastMessage.onToast(Elevenplus_Jewellery.this, String.valueOf(statuscode), ToastMessage.ERROR);
                    } else {
                        dialog.dismiss();
                        ToastMessage.onToast(Elevenplus_Jewellery.this, "Please try again", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    openpopupscreen("Successfully subscribed to Gold Plus Plan");
                }
            });
        }
    }

    public void onsuccess(String paymentid) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        try {
            if (!NetworkUtils.isConnected(this)) {
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                dialog.dismiss();
            } else {
//                ToastMessage.onToast(Elevenplus_Jewellery.this, et_wallet_money.getText().toString(), ToastMessage.SUCCESS);
//                ToastMessage.onToast(Elevenplus_Jewellery.this, finial_amount, ToastMessage.SUCCESS);
//                ToastMessage.onToast(Elevenplus_Jewellery.this, st_finalamount, ToastMessage.SUCCESS);
                refcode = etreferalcode.getText().toString().trim();
                if (isCoupon && refcode.equals("")) {
                    subscribe(paymentid, "", "", "");
//                ToastMessage.onToast(Elevenplus_Jewellery.this, refcode, ToastMessage.SUCCESS);
                } else if (!isCoupon && refcode.equals("")) {
                    subscribe(paymentid, stcouponcode, "", "");
                } else if (isCoupon && !refcode.equals("")) {
                    subscribe(paymentid, "", "", etreferalcode.getText().toString());
                } else if (!isCoupon && !refcode.equals("")) {
                    subscribe(paymentid, stcouponcode, "", etreferalcode.getText().toString());
                } else {
                    ToastMessage.onToast(Elevenplus_Jewellery.this, "Try Again", ToastMessage.ERROR);
                }


//            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//            Call<Listmodel> getsubscribedetails = apiDao.getelevensubscribedetails("Bearer " + AccountUtils.getAccessToken(this)
//                    ,schemeId, paymentid, "12", SUB_LISTAMOUNT,st_liveprice,walletamount,etreferalcode.getText().toString().trim());
////                ToastMessage.onToast(Elevenplus_Jewellery.this, "Scheme id : "+schemeId, ToastMessage.SUCCESS);
////                ToastMessage.onToast(Elevenplus_Jewellery.this, paymentid, ToastMessage.SUCCESS);
////                ToastMessage.onToast(Elevenplus_Jewellery.this, SUB_LISTAMOUNT, ToastMessage.SUCCESS);
//            getsubscribedetails.enqueue(new Callback<Listmodel>() {
//                @Override
//                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
//                    int statuscode = response.code();
//                    Log.e("11+1 status code", String.valueOf(statuscode));
//                    Listmodel listmodel = response.body();
//                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {
//                        dialog.dismiss();
//
//                        boolean  payment_status = listmodel.getPurchase_Status();
//                        //Toast.makeText(this, String.valueOf(payment_status), Toast.LENGTH_SHORT).show();
//                        if (payment_status){
//                            openpopupscreen(listmodel.getDescription());
//                        } else {
//                            onpaymenterror();
//                            Toast.makeText(Elevenplus_Jewellery.this, "Payment is Failed", Toast.LENGTH_SHORT).show();
//                        }
//
//                    } else {
//
//                        dialog.dismiss();
//                        onpaymenterror();
//                        ToastMessage.onToast(Elevenplus_Jewellery.this, "Technical issues ", ToastMessage.ERROR);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Listmodel> call, Throwable t) {
//                    Log.e("on 11+1 fail",t.toString());
//
//                    dialog.dismiss();
//                    openpopupscreen("Successfully subscribed to Gold Plus Plan");
////                    onpaymenterror();
////                    ToastMessage.onToast(Elevenplus_Jewellery.this, "we have some issues", ToastMessage.ERROR);
//                }
//            });
            }
        } catch (Exception e) {
            ToastMessage.onToast(Elevenplus_Jewellery.this, "Exception here", ToastMessage.ERROR);
        }

    }

    public void onpaymenterror() {
        walletamount = "null";

        Intent intent = new Intent(this, PaymentError.class);
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    public void openpopupscreen(String msg) {

        Intent intent = new Intent(this, Successpopup.class);
        intent.putExtra("from", "11+1Jewellery");
        intent.putExtra("description", msg);
        startActivity(intent);
    }

    public void openpayment() {

        final Activity activity = this;
        final Checkout co = new Checkout();

        co.setKeyID("rzp_test_0VM20Pg2VIA2aR");

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("payment_capture", 1);

            double total = Double.parseDouble(tv_final_amount.getText().toString().substring(3));

            //  double total = Double.parseDouble("1");

            Log.e("finialdouble", String.valueOf(total));
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
        try {
            onsuccess(s);
            Log.e("payment id", s);
//            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            onsuccess(s);
            Log.e("payment id", s);
//            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        calculation(SUB_LISTAMOUNT);
        cb_wallet.setChecked(false);
//        ToastMessage.onToast(Elevenplus_Jewellery.this, s, ToastMessage.ERROR);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llopencoupon) {
            Intent intent = new Intent(Elevenplus_Jewellery.this, CouponsList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, 2);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {

            stcouponcode = data.getStringExtra("couponcode");
            Log.e("stcoupen", "" + stcouponcode);


            if (stcouponcode.equals("gghytt")) {
                Log.e("retuytguhrn", "");
            } else {
                Log.e("fcghh", "");


                stcouponamount = data.getStringExtra("offeramount");
                minamount = data.getStringExtra("minamount");

//            ToastMessage.onToast(this, SUB_LISTAMOUNT, ToastMessage.SUCCESS);
//            ToastMessage.onToast(this, String.valueOf(isCoupon), ToastMessage.SUCCESS);


                try {
                    if (SUB_LISTAMOUNT.equals("")) {
                        ToastMessage.onToast(Elevenplus_Jewellery.this, "Please Select the Amount", ToastMessage.ERROR);
                    } else {
                        if (minamount.equals("yes")) {
                            Log.e("dvd", "sdv");
                        } else if (Double.parseDouble(SUB_LISTAMOUNT) < Double.parseDouble(minamount)) {
                            ToastMessage.onToast(Elevenplus_Jewellery.this, "Coupon is not applicable", ToastMessage.ERROR);
                        } else if (Double.parseDouble(SUB_LISTAMOUNT) < Double.parseDouble(stcouponamount)) {
                            ToastMessage.onToast(Elevenplus_Jewellery.this, "Coupon is not applicable", ToastMessage.ERROR);
                        } else {
//                ToastMessage.onToast(Elevenplus_Jewellery.this, minamount, ToastMessage.SUCCESS);
                            tv_discountamount.setText(": " + this.getResources().getString(R.string.Rs) + stcouponamount);
                            isCoupon = data.getBooleanExtra("isCoupon", false);

                            if (et_wallet_money.getText().toString().equals("")) {
                                finial_amount = String.valueOf(Double.parseDouble(tv_final_amount.getText().toString().substring(3)) - Double.parseDouble(stcouponamount));
                            } else {
                                finial_amount = String.valueOf(Double.parseDouble(tv_final_amount.getText().toString().substring(3)) + Double.parseDouble(et_wallet_money.getText().toString()) - Double.parseDouble(stcouponamount));
                                et_wallet_money.setText("");
                            }

                            tv_final_amount.setText(": ₹" + finial_amount);
                            opencoupon.setVisibility(View.GONE);
                            openoffers.setVisibility(View.VISIBLE);
                            tvofferremove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    isCoupon = true;
//                    ToastMessage.onToast(Mygold2020Form_Activity.this, finial_amount, ToastMessage.SUCCESS);
                                    if (et_wallet_money.getText().toString().equals("")) {
                                        finial_amount = String.valueOf(Double.parseDouble(tv_final_amount.getText().toString().substring(3)) + Double.parseDouble(stcouponamount));
                                    } else {
                                        finial_amount = String.valueOf(Double.parseDouble(tv_final_amount.getText().toString().substring(3)) + Double.parseDouble(et_wallet_money.getText().toString()) + Double.parseDouble(stcouponamount));
                                        et_wallet_money.setText("");
                                    }
//                        dediactWalletMoney();
                                    tv_final_amount.setText(": ₹" + finial_amount);
                                    stcouponcode = "null";
                                    stcouponamount = "null";
                                    tv_discountamount.setText(": ₹0");
                                    opencoupon.setVisibility(View.VISIBLE);
                                    openoffers.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
//                ToastMessage.onToast(Elevenplus_Jewellery.this, String.valueOf(e), ToastMessage.ERROR);
                    Log.e("Exception", String.valueOf(e));


                }

            }


        }
    }
}