package com.goldsikka.goldsikka.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.LayoutInflater;
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
import com.goldsikka.goldsikka.Fragments.Successpopup;
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

public class Mygold2020Form_Activity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {


    TextView tv_lvprice, tv_gramount, tv_processingfee, tv_cashback, tv_gst, amt_ed, tv_sp_tenuer, tv_final_amount, tv_cashbakgst;
    EditText et_grms;
    Spinner sp_mmi_tennure;
    Button btn_subscribe;
    GifImageView loading_gif;
    String st_grams, subcategory, tennure_type, schemeid, schemename, st_cashbackgst;
    String[] signup_type = {"Select Tenure", "6 months", "12 months", "18 months", "24 months"};
    ArrayList<String> sublist;
    TextWatcher textWatcher;
    // LinearLayout lllive;

    ApiDao apiDao;
    String emiamount, gst, cashback, processingfee;
    String finial_amount;
    String liveprice;
    String passamount;
    String refcode;
    String minamount = "yes";


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llinformation)
    LinearLayout llinformation;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvrefresh)
    TextView tvrefresh;

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
    @BindView(R.id.tv_referal_code_error)
    TextView tv_referal_code_error;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_wallet_error)
    TextView tv_wallet_error;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_head_final_amount)
    TextView tv_head_final_amount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etreferralcode)
    EditText etreferalcode;


    String Walletamount;

    String walletamount = "null";

    boolean checkValidation = true;

    boolean ischeck = false;

    LinearLayout opencoupon, openoffers;
    TextView tvofferremove, tv_discountamount;
    String stcouponcode = "null";
    String stcouponamount = "null";
    boolean isCoupon = true;

    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mygold2020_form_);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            schemeid = bundle.getString("id");
            schemename = bundle.getString("schemename");
        }

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
        titleTv.setText("Gold Chit");
        init_formdisplay();
        textwatcher();
        getliveprices();
        wallet_amount();
        checkboxClick();
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
                                                     } else {
                                                         tv_head_final_amount.setText("Final Amount");
                                                         btn_subscribe.setEnabled(true);
                                                         ischeck = false;
                                                         et_wallet_money.setVisibility(View.GONE);
                                                         tv_wallet_error.setVisibility(View.GONE);
                                                         setdetails();
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

                     //   ToastMessage.onToast(Mygold2020Form_Activity.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {

                    Log.e("on fails", t.toString());

                    dialog.dismiss();

                    ToastMessage.onToast(Mygold2020Form_Activity.this, "We have some issue", ToastMessage.ERROR);
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
//                                ToastMessage.onToast(Mygold2020Form_Activity.this, after_dedcation, ToastMessage.SUCCESS);
                                tv_final_amount.setText(": " + getResources().getString(R.string.Rs) + after_dedcation);
                                btn_subscribe.setEnabled(true);

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
                                    btn_subscribe.setEnabled(false);
                                    btn_subscribe.setBackgroundResource(R.drawable.backgroundvisablity);
                                    tv_wallet_error.setText("You can't enter more then " + Walletamount);
                                    tv_wallet_error.setTextColor(getColor(R.color.red));
                                }
                            } else {
                                tv_final_amount.setText(": " + getResources().getString(R.string.Rs) + finial_amount);
                                btn_subscribe.setEnabled(false);
                                btn_subscribe.setBackgroundResource(R.drawable.backgroundvisablity);
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

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tvofferremove)
    public void offerremove() {
        opencoupon.setVisibility(View.VISIBLE);
        openoffers.setVisibility(View.GONE);
        isCoupon = true;
        stcouponcode = "null";
        stcouponamount = "null";
    }

    @SuppressLint("SetTextI18n")
    public void init_formdisplay() {
        //  lllive = findViewById(R.id.lllive);
        opencoupon = findViewById(R.id.llopencoupon);
        openoffers = findViewById(R.id.llofferremove);
        tvofferremove = findViewById(R.id.tvofferremove);
        tv_discountamount = findViewById(R.id.tv_discountamount);
        opencoupon.setOnClickListener(this);
        loading_gif = findViewById(R.id.loading_gif);
        tv_lvprice = findViewById(R.id.tv_lvprice);
        tv_gramount = findViewById(R.id.tv_gramount);
        tv_processingfee = findViewById(R.id.tv_processingfee);
        tv_cashback = findViewById(R.id.tv_cashback);
        tv_gst = findViewById(R.id.tv_gst);
        amt_ed = findViewById(R.id.amt_ed);
        tv_sp_tenuer = findViewById(R.id.tv_sp_tenuer);
        tv_final_amount = findViewById(R.id.tv_final_amount);
        et_grms = findViewById(R.id.etgrms);
        et_grms.setHint(Html.fromHtml(getString(R.string.grams)));
        st_grams = et_grms.getText().toString();
        btn_subscribe = findViewById(R.id.btn_subscribe);
        btn_subscribe.setOnClickListener(this);

        et_wallet_money.setHint(Html.fromHtml(getString(R.string.wallet_hint)));

        tv_cashbakgst = findViewById(R.id.tv_cashbackgst);
//        tvschemename = findViewById(R.id.tvschemename);
//        tvschemename.setText(schemename);

        tvrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getliveprices();
            }
        });


        sp_mmi_tennure = findViewById(R.id.sp_mmi_tennure);
        ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, signup_type);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_mmi_tennure.setAdapter(spinner_adapter);
        spinnerclick();
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
//                            String purity = listmodel.getGold_purity();

                                dialog.dismiss();
                                liveprice = listmodel.getSell_price_per_gram();
                                tv_lvprice.setText(getResources().getString(R.string.Rs) + " " + liveprice + "/gram");
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
//                            Toast.makeText(Mygold2020Form_Activity.this, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(Mygold2020Form_Activity.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

    public void spinnerclick() {
        sublist = new ArrayList<>();
        // spinner_signuptype = findViewById(R.id.sub_category);

        sp_mmi_tennure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subcategory = sp_mmi_tennure.getItemAtPosition(sp_mmi_tennure.getSelectedItemPosition()).toString();

                ((TextView) view).setTextColor(ContextCompat.getColor(Mygold2020Form_Activity.this, R.color.DarkBrown));

                if (!st_grams.isEmpty()) {
                    if (Double.parseDouble(st_grams) < 10) {
                        cb_wallet.setChecked(false);
                        et_wallet_money.setVisibility(View.GONE);
                        tv_wallet_error.setVisibility(View.GONE);
                        setdetails();

                    } else if (Double.parseDouble(st_grams) > 30) {
                        cb_wallet.setChecked(false);
                        et_wallet_money.setVisibility(View.GONE);
                        tv_wallet_error.setVisibility(View.GONE);
                        setdetails();

                    } else {
                        cb_wallet.setEnabled(true);
                    }
                }

                if (subcategory.equals("6 months")) {
                    tennure_type = "6";
                    calculation(tennure_type, st_grams);
                    cb_wallet.setChecked(false);
//                    cb_wallet.setEnabled();
                } else if (subcategory.equals("12 months")) {
                    tennure_type = "12";
                    calculation(tennure_type, st_grams);
                    cb_wallet.setChecked(false);
                } else if (subcategory.equals("18 months")) {
                    tennure_type = "18";
                    calculation(tennure_type, st_grams);
                    cb_wallet.setChecked(false);
                } else if (subcategory.equals("24 months")) {
                    tennure_type = "24";
                    calculation(tennure_type, st_grams);
                    cb_wallet.setChecked(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }

    public void textwatcher() {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && !charSequence.toString().equalsIgnoreCase("")) {

                    if (et_grms.getText().hashCode() == charSequence.hashCode()) {

                        st_grams = et_grms.getText().toString();


                        if (st_grams.equals(".")) {
                            Toast.makeText(getApplicationContext(), "Enter a valid input", Toast.LENGTH_SHORT).show();
                        } else if (Double.parseDouble(st_grams) < 10) {
                            cb_wallet.setEnabled(false);
                            cb_wallet.setChecked(false);
                            et_wallet_money.setVisibility(View.GONE);
                            tv_wallet_error.setVisibility(View.GONE);
                            amt_ed.setVisibility(View.VISIBLE);
                            setdetails();
                            amt_ed.setText("Please enter the Minimum 10 grams");
                        } else if (Double.parseDouble(st_grams) > 30) {
                            cb_wallet.setEnabled(false);
                            cb_wallet.setChecked(false);
                            et_wallet_money.setVisibility(View.GONE);
                            tv_wallet_error.setVisibility(View.GONE);
                            openalertdiloug();
                            et_grms.getText().clear();

                        } else {
//                            cb_wallet.setEnabled(true);
                            if (!subcategory.equals("Select Tenure")) {
                                cb_wallet.setEnabled(true);

                            }
                            amt_ed.setVisibility(View.GONE);
                            calculation(tennure_type, st_grams);
                        }
                    }

                } else {
                    setdetails();
                    et_grms.getText().clear();
                    amt_ed.setVisibility(View.GONE);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        et_grms.addTextChangedListener(textWatcher);
    }

    AlertDialog alertDialogdialog;
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

    public void calculation(String tenure, String grms) {

        if (subcategory.equals("Select Tenure")) {

        } else if (st_grams.isEmpty()) {

        } else if (st_grams.equals(".")) {
            Toast.makeText(getApplicationContext(), "Enter a valid input", Toast.LENGTH_SHORT).show();
        } else {
            opencalculation(tenure, grms);
        }
    }

    public void opencalculation(String tennuretype, String grams) {

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
            Call<Listmodel> getcalculation = apiDao.getcalculation("Bearer " + AccountUtils.getAccessToken(this), schemeid, tennuretype, grams);
            getcalculation.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("code", String.valueOf(statuscode));
                    Listmodel listmodel = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        emiamount = listmodel.getEmiAmount();
                        cashback = listmodel.getCashbackAmount();
                        gst = listmodel.getGst();
                        finial_amount = listmodel.getFinalAmount();
                        processingfee = listmodel.getProcessingFee();
                        st_cashbackgst = listmodel.getCashBackGstAmount();
                        liveprice = listmodel.getLivePrice();
                        setdetails();
                    } else {
                        amt_ed.setVisibility(View.GONE);
                        tv_sp_tenuer.setVisibility(View.GONE);
                        dialog.dismiss();
                        assert response.errorBody() != null;
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
//                            ToastMessage.onToast(Mygold2020Form_Activity.this, st, ToastMessage.ERROR);
                            JSONObject er = jObjError.getJSONObject("errors");
                            try {
                                JSONArray array_grams = er.getJSONArray("tenure");
                                for (int i = 0; i < array_grams.length(); i++) {
                                    tv_sp_tenuer.setVisibility(View.VISIBLE);
                                    tv_sp_tenuer.setText(array_grams.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_tenure = er.getJSONArray("grams");
                                for (int i = 0; i < array_tenure.length(); i++) {
                                    amt_ed.setVisibility(View.VISIBLE);
                                    amt_ed.setText(array_tenure.getString(i));
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
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(Mygold2020Form_Activity.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setdetails();
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
                        ToastMessage.onToast(Mygold2020Form_Activity.this, "Coupon Validation Successful", ToastMessage.SUCCESS);
//                        lockprice();
                        sts[0] = "success";
                    } else {
                        dialog.dismiss();
                        assert response.errorBody() != null;

                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
//                            ToastMessage.onToast(Mygold2020Form_Activity.this, st, ToastMessage.ERROR);
                            JSONObject er = jObjError.getJSONObject("errors");
                            try {
                                JSONArray password = er.getJSONArray("coupon");
                                for (int i = 0; i < password.length(); i++) {
                                    ToastMessage.onToast(Mygold2020Form_Activity.this, password.getString(i), ToastMessage.ERROR);
                                    sts[0] = "failed";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray password = er.getJSONArray("amount");
                                for (int i = 0; i < password.length(); i++) {
//                                    ToastMessage.onToast(Mygold2020Form_Activity.this, password.getString(i), ToastMessage.ERROR);
                                Log.e("xdfcgvbhn", "hb");
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
                    ToastMessage.onToast(Mygold2020Form_Activity.this, "We have some issues please try after some time"
                            , ToastMessage.ERROR);

                }
            });
        }
        return sts[0];
    }


    @SuppressLint("SetTextI18n")
    public void setdetails() {

        //  lllive.setVisibility(View.VISIBLE);
        tv_lvprice.setText(getString(R.string.Rs) + liveprice + "/gram");
        if (processingfee == null || processingfee.equals(""))
            tv_processingfee.setText(": " + this.getResources().getString(R.string.Rs) + "0");
        else
            tv_processingfee.setText(": " + this.getResources().getString(R.string.Rs) + processingfee);

        String amount = String.valueOf(finial_amount);
        if (amount.equals("") || amount.equals("null") || amount == null)
            tv_final_amount.setText(": " + this.getResources().getString(R.string.Rs) + "0");
        else
            tv_final_amount.setText(": " + this.getResources().getString(R.string.Rs) + amount);

        if (gst == null || gst.equals(""))
            tv_gst.setText(": " + this.getResources().getString(R.string.Rs) + "0");
        else
            tv_gst.setText(": " + this.getResources().getString(R.string.Rs) + gst);
        tv_cashback.setText(": " + this.getResources().getString(R.string.Rs) + cashback);
        if (emiamount == null || emiamount.equals(""))
            tv_gramount.setText(": " + this.getResources().getString(R.string.Rs) + "0");
        else
            tv_gramount.setText(": " + this.getResources().getString(R.string.Rs) + emiamount);

        if (stcouponamount == "null")
            tv_discountamount.setText(": " + this.getResources().getString(R.string.Rs) + "0");
        else
            tv_discountamount.setText(": " + this.getResources().getString(R.string.Rs) + stcouponamount);

        tv_cashbakgst.setText(": " + this.getResources().getString(R.string.Rs) + st_cashbackgst);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_subscribe) {


            init_validation(view);
            //init_pay();
        } else if (view.getId() == R.id.llopencoupon) {
            Intent intent = new Intent(Mygold2020Form_Activity.this, CouponsList.class);
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
                    if (st_grams.equals("")) {
                        ToastMessage.onToast(Mygold2020Form_Activity.this, "Please Type the Grams", ToastMessage.ERROR);
                    } else if (sp_mmi_tennure.getSelectedItemPosition() == 0) {
                        ToastMessage.onToast(Mygold2020Form_Activity.this, "Please Select the Tenure", ToastMessage.ERROR);
                    } else {
                        if (minamount.equals("yes")) {
                            Log.e("dvd", "sdv");
                        } else if (Double.parseDouble(finial_amount) < Double.parseDouble(minamount)) {
                            ToastMessage.onToast(Mygold2020Form_Activity.this, "Coupon is not applicable", ToastMessage.ERROR);
//                            ToastMessage.onToast(Mygold2020Form_Activity.this, finial_amount, ToastMessage.ERROR);
//                            ToastMessage.onToast(Mygold2020Form_Activity.this, minamount, ToastMessage.ERROR);
                        } else {
//                ToastMessage.onToast(Elevenplus_Jewellery.this, minamount, ToastMessage.SUCCESS);
                            isCoupon = data.getBooleanExtra("isCoupon", false);
                            tv_discountamount.setText(": " + this.getResources().getString(R.string.Rs) + stcouponamount);
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

    public void init_validation(View v) {

        amt_ed.setVisibility(View.GONE);
        btn_subscribe.setVisibility(View.GONE);
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

                Mygold2020Form_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        validation();
                        btn_subscribe.setVisibility(View.VISIBLE);
                        loading_gif.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);
    }


    public String referalCode_validation() {
        String[] sts = {"success"};
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
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
                            ToastMessage.onToast(Mygold2020Form_Activity.this, "Referralcode validation Successful", ToastMessage.SUCCESS);
                            sts[0] = "success";
                        }
                        // Log.e("Wallet Money Responce",new Gson().toJson(response.body()));
                        dialog.dismiss();

                    } else if (statuscode == 422) {
//                        L3SXU9
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
                                    sts[0] = "failed";
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

                        // ToastMessage.onToast(Mygold2020Form_Activity.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                    ToastMessage.onToast(Mygold2020Form_Activity.this, "We have some issue", ToastMessage.ERROR);
                }
            });
        }

        return sts[0];

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
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getdetails;
            if (!coupon.equals("")) {
                if (!referral.equals("")) {
                    getdetails = apiDao.buywalletAmount_scheme("Bearer " + AccountUtils.getAccessToken(this),
                            schemeid,
                            tennure_type,
                            st_grams,
                            tv_final_amount.getText().toString(),
                            liveprice,
                            walletamount, referral, stcouponcode);
                } else {
                    getdetails = apiDao.buywalletAmount_scheme("Bearer " + AccountUtils.getAccessToken(this),
                            schemeid,
                            tennure_type,
                            st_grams,
                            tv_final_amount.getText().toString(),
                            liveprice,
                            walletamount, "", stcouponcode);
                }
            } else if (!referral.equals("")) {
                getdetails = apiDao.buywalletAmount_scheme("Bearer " + AccountUtils.getAccessToken(this),
                        schemeid,
                        tennure_type,
                        st_grams,
                        tv_final_amount.getText().toString(),
                        liveprice,
                        walletamount, referral, "");
            } else {
                getdetails = apiDao.buywalletAmount_scheme("Bearer " + AccountUtils.getAccessToken(this),
                        schemeid,
                        tennure_type,
                        st_grams,
                        tv_final_amount.getText().toString(),
                        liveprice,
                        walletamount, "", "");
            }

            getdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(@NonNull Call<Listmodel> call, @NonNull Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode dd", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_CREATED) {
                        Listmodel list = response.body();
                        assert list != null;
                        openpopupscreen(list.getDescription());
                        dialog.dismiss();

                    } else {

                        dialog.dismiss();

                      //  ToastMessage.onToast(Mygold2020Form_Activity.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                    openpopupscreen("Successfully Subscribed to Plan");
//                    ToastMessage.onToast(Mygold2020Form_Activity.this, "We have some issue", ToastMessage.ERROR);
                }
            });
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

            double total = Double.parseDouble(tv_final_amount.getText().toString().substring(3));
            //double total = Double.parseDouble("1");

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
                Log.e("Payment id : ", paymentid);
                if (isCoupon && refcode.equals("")) {
                    subscribe(paymentid, "", "", "");
                } else if (!isCoupon && refcode.equals("")) {
                    subscribe(paymentid, stcouponcode, "", "");
                } else if (isCoupon && !refcode.equals("")) {
                    subscribe(paymentid, "", "", etreferalcode.getText().toString());
                } else if (!isCoupon && !refcode.equals("")) {
                    subscribe(paymentid, stcouponcode, "", etreferalcode.getText().toString());

                }
            }
        } catch (Exception e) {
            ToastMessage.onToast(Mygold2020Form_Activity.this, "Exception here", ToastMessage.ERROR);
        }
    }

    public void validation() {
        amt_ed.setVisibility(View.GONE);
        st_grams = et_grms.getText().toString();
        if (st_grams.isEmpty()) {
            amt_ed.setVisibility(View.VISIBLE);
            amt_ed.setText("please enter the grams");
        } else if (subcategory.equals("Select Tenure")) {
            ToastMessage.onToast(Mygold2020Form_Activity.this, "please select the tenure", ToastMessage.ERROR);
        } else if (Double.parseDouble(st_grams) < 10) {
            ToastMessage.onToast(Mygold2020Form_Activity.this, "Please enter the Minimum 10 grams", ToastMessage.ERROR);

        } else if (Double.parseDouble(st_grams) > 30) {
            openalertdiloug();
        } else {

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
                        ToastMessage.onToast(Mygold2020Form_Activity.this, "Invalid Coupon", ToastMessage.ERROR);
                } else if (isCoupon && cb_wallet.isChecked() && refcode.equals("")) { //NYN
                    String sts = walletvalidation();
                    if (sts.equals("success"))
                        buy_wallet_money("", "");
                    else if (sts.equals("half")) {
                        ToastMessage.onToast(Mygold2020Form_Activity.this, sts, ToastMessage.SUCCESS);
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
                        ToastMessage.onToast(Mygold2020Form_Activity.this, "Invalid Referral Code", ToastMessage.ERROR);
                } else if (!isCoupon && cb_wallet.isChecked() && refcode.equals("")) { //YYN
                    String sts = walletvalidation();
                    String sts1 = couponvalidation();
                    if (sts.equals("success"))
                        if (sts1.equals("success"))
                            buy_wallet_money(stcouponcode, "");
                        else
                            ToastMessage.onToast(Mygold2020Form_Activity.this, "Try Again", ToastMessage.ERROR);
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
                            ToastMessage.onToast(Mygold2020Form_Activity.this, "Referral Code Validation Error", ToastMessage.ERROR);
                    else if (sts.equals("half")) {
                        openpayment();
//                        buy_wallet_money(stcouponcode, "");
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
                            ToastMessage.onToast(Mygold2020Form_Activity.this, "Referral Code Validation Error", ToastMessage.ERROR);
                    else if (sts.equals("half")) {
                        openpayment();
//                        buy_wallet_money(stcouponcode, "");
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
                                ToastMessage.onToast(Mygold2020Form_Activity.this, "Coupon code validation Error", ToastMessage.ERROR);
                        else
                            ToastMessage.onToast(Mygold2020Form_Activity.this, "Referral Code Validation Error", ToastMessage.ERROR);
                    else if (sts.equals("half")) {
                        openpayment();
//                        buy_wallet_money(stcouponcode, "");
//                            ToastMessage.onToast(Elevenplus_Jewellery.this, "Invalid Code", ToastMessage.ERROR);
                    } else {
                        Log.e("failed", "check sts");
                    }
                }
            }

        }
    }

    private String walletvalidation() {
        String etmoney = et_wallet_money.getText().toString().trim();

        if (etmoney.equals("")) {
            ToastMessage.onToast(Mygold2020Form_Activity.this, "Please Enter Booking Account Amount", ToastMessage.SUCCESS);
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
            ToastMessage.onToast(Mygold2020Form_Activity.this, "Insufficient Balance", ToastMessage.SUCCESS);
            return "failed";
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
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getsubscribedetails = null;
            if (coupon.equals("")) {
                if (wallet.equals("")) {
                    if (referral.equals("")) {
                        Log.e("path1 : ", "path1");
                        Log.e("schemeid : ", schemeid);
                        Log.e("payment id : ", paymentid);
                        Log.e("tennure type : ", tennure_type);
                        Log.e("st grams : ", st_grams);
                        Log.e("total amount : ", tv_final_amount.getText().toString());
                        Log.e("live price : ", liveprice);
                        Log.e("Booking Account : ", et_wallet_money.getText().toString());
                        Log.e("coupon code : ", coupon);
                        getsubscribedetails = apiDao.getsubscribedetails("Bearer " + AccountUtils.getAccessToken(this),
                                schemeid,
                                paymentid,
                                tennure_type,
                                st_grams,
                                tv_final_amount.getText().toString().substring(1),
                                liveprice,
                                et_wallet_money.getText().toString(),
                                "",
                                "");
                    } else {
                        Log.e("path2 : ", "path2");
                        Log.e("schemeid : ", schemeid);
                        Log.e("payment id : ", paymentid);
                        Log.e("tennure type : ", tennure_type);
                        Log.e("st grams : ", st_grams);
                        Log.e("total amount : ", tv_final_amount.getText().toString());
                        Log.e("live price : ", liveprice);
                        Log.e("Booking Account : ", et_wallet_money.getText().toString());
                        Log.e("coupon code : ", coupon);
                        getsubscribedetails = apiDao.getsubscribedetails("Bearer " + AccountUtils.getAccessToken(this),
                                schemeid,
                                paymentid,
                                tennure_type,
                                st_grams,
                                tv_final_amount.getText().toString().substring(1),
                                liveprice,
                                et_wallet_money.getText().toString(),
                                etreferalcode.getText().toString().trim(),
                                "");
                    }
                } else {
                    ToastMessage.onToast(Mygold2020Form_Activity.this, "Exceptional", ToastMessage.ERROR);
                }
            } else {
                Log.e("path3 : ", "path3");
                Log.e("schemeid : ", schemeid);
                Log.e("payment id : ", paymentid);
                Log.e("tennure type : ", tennure_type);
                Log.e("st grams : ", st_grams);
                Log.e("total amount : ", tv_final_amount.getText().toString());
                Log.e("live price : ", liveprice);
                Log.e("Booking Account : ", et_wallet_money.getText().toString());
                Log.e("coupon code : ", coupon);
                getsubscribedetails = apiDao.getsubscribedetails("Bearer " + AccountUtils.getAccessToken(this),
                        schemeid,
                        paymentid,
                        tennure_type,
                        st_grams,
                        tv_final_amount.getText().toString().substring(1),
                        liveprice,
                        et_wallet_money.getText().toString(),
                        "",
                        stcouponcode);
                Log.e("schemeid : ", schemeid);
                Log.e("payment id : ", paymentid);
                Log.e("tennure type : ", tennure_type);
                Log.e("st grams : ", st_grams);
                Log.e("total amount : ", tv_final_amount.getText().toString());
                Log.e("live price : ", liveprice);
                Log.e("Booking Account : ", et_wallet_money.getText().toString());
                Log.e("coupon code : ", coupon);
            }

            assert getsubscribedetails != null;
            getsubscribedetails.enqueue(new Callback<Listmodel>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(@NotNull Call<Listmodel> call, @NotNull Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode mygold 20", String.valueOf(statuscode));
//                    Log.e("statuscode mygold 20", String.valueOf(statuscode));
                    Listmodel listmodel = response.body();
                    dialog.dismiss();
                    assert listmodel != null;
                    openpopupscreen(listmodel.getDescription());
                }

                @Override
                public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    openpopupscreen("Successfully subscribed to My Gold 2021 (Gold Chit)");
                }
            });
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
        intent.putExtra("from", "Mygold");
        intent.putExtra("tennure_type", tennure_type);
        intent.putExtra("description", msg);
        startActivity(intent);

    }

    @Override
    public void onPaymentSuccess(String s) {
//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        onsuccess(s);
        Log.e("payid", s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("paymenterror", s);
//
//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
//        setdetails();
        calculation(tennure_type, st_grams);
        cb_wallet.setChecked(false);
        ToastMessage.onToast(this, s, ToastMessage.ERROR);

    }

}