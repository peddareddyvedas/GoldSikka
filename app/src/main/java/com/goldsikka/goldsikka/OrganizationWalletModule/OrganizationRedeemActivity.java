package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.goldsikka.goldsikka.Activitys.Profile.CustomerAddAddress;
import com.goldsikka.goldsikka.Fragments.Reedem_fragment;
import com.goldsikka.goldsikka.Fragments.Successpopup;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
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

public class OrganizationRedeemActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    Button next;
    EditText et_grams;
    // private this this;
    TextWatcher textWatcher;
    ApiDao apiDao;
    TextView tv_grmserror,tv_amount;
    double redeem;
    String st_grams;
    String  liveprice,finalamount,sttransactionid,stdescription;
    TextView wallet_gold;
    String st_wallet_gold;
    GifImageView loading_gif;
    double grams;
    BigDecimal b21;
    CountDownTimer countDownTimer;
    private boolean timerRunning;
    RelativeLayout backbtn;

    String oid, oname;
    TextView tv_second_title;
TextView unameTv, uidTv, titleTv;
    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_redeem);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Bundle bundle = getIntent().getExtras();

        if (bundle!= null){
            st_wallet_gold = bundle.getString("wallet");
            oid = bundle.getString("oid");
            oname = bundle.getString("oname");

        }

        st_wallet_gold = AccountUtils.getWalletAmount(OrganizationRedeemActivity.this);
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

//        String bb = new OrganizationHomeFragment().getSt_orgname();

        unameTv.setText(oname);
        uidTv.setText(oid);
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Redeem");
        tv_second_title = findViewById(R.id.tv_second_title);
        initlizeviews();
        init_timer();

    }
    public void init_timer(){
        long duration = 300000;
        countDownTimer=     new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                String sduration = String.format(Locale.ENGLISH,"%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(l)
                        ,TimeUnit.MILLISECONDS.toSeconds(l)-
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
                Intent intent = new Intent(OrganizationRedeemActivity.this, Organizationwallet_mainpage.class);
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

    public void getliveprices(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
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
                            Toast.makeText(OrganizationRedeemActivity.this, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(OrganizationRedeemActivity.this, "We have some issues", ToastMessage.ERROR);
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


    public void initlizeviews(){

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
        wallet_gold.setText(st_wallet_gold+" grams");

        next = findViewById(R.id.btn_redeemnext);
        next.setOnClickListener(this);



//    textWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            if (s != null && !s.toString().equalsIgnoreCase("")) {
//
//                if (et_grams.getText().hashCode() == s.hashCode()) {
//                    try {
//                    st_grams = et_grams.getText().toString().trim();
//
//                    double goldgrams = Double.parseDouble(st_grams);
//                    grams = Double.parseDouble(liveprice) * goldgrams;
//                    Log.e("liveprice", String.valueOf(grams));
//                    BigDecimal b21 = BigDecimal.valueOf(grams).setScale(2, RoundingMode.HALF_EVEN);
//                    finalamount = new String(String.valueOf(b21));
//
////                    if (Double.parseDouble(st_grams) < 2) {
////                        tv_amount.setVisibility(View.GONE);
////                        tv_grmserror.setVisibility(View.VISIBLE);
////                        tv_grmserror.setText("Please Enter min 2 grams");
//////                        Toast.makeText(this, "Please Enter min 2 grams", Toast.LENGTH_SHORT).show();
////                    }
//////                    else if (Double.parseDouble(st_grams) >= 2 && Double.parseDouble(st_grams) < 3) {
//////                        tv_amount.setVisibility(View.VISIBLE);
//////                        tv_grmserror.setVisibility(View.GONE);
//////                        fprice = "18";
//////                        redeem = Double.parseDouble(finalamount) * (Double.parseDouble(fprice) / 100);
//////                        BigDecimal bellow3grms = BigDecimal.valueOf(redeem).setScale(2, RoundingMode.HALF_EVEN);
//////                        tv_amount.setText("Fractional Price : " +getApplicationContext().
//////                                getResources().getString(R.string.Rs)+" "+String.valueOf(bellow3grms));
//////
//////                    } else if (Double.parseDouble(st_grams) >= 3 && Double.parseDouble(st_grams) < 4) {
//////                        tv_amount.setVisibility(View.VISIBLE);
//////                        tv_grmserror.setVisibility(View.GONE);
//////                        fprice = "16";
//////                        redeem = Double.parseDouble(finalamount) * (Double.parseDouble(fprice) / 100);
//////
//////                        BigDecimal bellow4grms = BigDecimal.valueOf(redeem).setScale(2, RoundingMode.HALF_EVEN);
//////                        tv_amount.setText("Fractional Price : " + getApplicationContext().getResources().getString(R.string.Rs)+" "+String.valueOf(bellow4grms));
//////
//////                    } else if (Double.parseDouble(st_grams) >= 4 && Double.parseDouble(st_grams) < 5) {
//////                        tv_amount.setVisibility(View.VISIBLE);
//////                        tv_grmserror.setVisibility(View.GONE);
//////                        fprice = "14";
//////                        redeem = Double.parseDouble(finalamount) * (Double.parseDouble(fprice) / 100);
//////                        BigDecimal bellow5grms = BigDecimal.valueOf(redeem).setScale(2, RoundingMode.HALF_EVEN);
//////                        tv_amount.setText("Fractional Price :  " + getApplicationContext().getResources().getString(R.string.Rs)+" "+String.valueOf(bellow5grms));
//////
//////                    }
//
////                    else {
////                        tv_amount.setVisibility(View.GONE);
////                        tv_grmserror.setVisibility(View.GONE);
////                        tv_amount.setText("Money : "+getApplicationContext().getResources().getString(R.string.Rs)+" "+finalamount);
////                    }
//
//                  }catch (NumberFormatException e){
//                    e.printStackTrace();
//                }
//            }
//
//         }else {
//                tv_amount.setText(null);
//            }
//        }
//    };
//    et_grams.addTextChangedListener(textWatcher);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_redeemnext:
                if (!NetworkUtils.isConnected(this)){
                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                }else {
                    init_validation(view);
                }
                return;
        }
    }

    public void init_validation(View v){

        tv_grmserror.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
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

                OrganizationRedeemActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        validation();
                        next.setVisibility(View.VISIBLE);
                        loading_gif.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);
    }

    private void validation() {
        tv_grmserror.setVisibility(View.GONE);
        st_grams = et_grams.getText().toString().trim();
        if (st_grams.isEmpty()){
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

    public void validationapi(){

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {
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
                                ToastMessage.onToast(OrganizationRedeemActivity.this, stmsg, ToastMessage.ERROR);
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

    private void openwithdraw() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

        }else {

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

                            ToastMessage.onToast(OrganizationRedeemActivity.this, st, ToastMessage.ERROR);

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
                                ToastMessage.onToast(OrganizationRedeemActivity.this, stmsg, ToastMessage.ERROR);

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
                    ToastMessage.onToast(OrganizationRedeemActivity.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }
    @SuppressLint("NewApi")
    public void openpopupscreen(){
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {
            Intent intent = new Intent(this, Successpopup.class);
            intent.putExtra("grams", st_grams);
            intent.putExtra("from", "org_redeem");
            intent.putExtra("transctionid",sttransactionid);
            intent.putExtra("description",stdescription);
            startActivity(intent);
        }


    }



    @Override
    public void onPaymentSuccess(String s) {
        //   Toast.makeText(this,s+"Payment success",Toast.LENGTH_LONG).show();

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


    public void AddressPopUpScreen(String msg){

        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.addresslinkforreedempopup,null);
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

                        OrganizationRedeemActivity.this.runOnUiThread(new Runnable() {
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

    public void getaddress(){
        if (!NetworkUtils.isConnected(this)){

            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {
            Intent intent = new Intent(OrganizationRedeemActivity.this, CustomerAddAddress.class);
            intent.putExtra("from","orgredeem");
            startActivity(intent);
        }
    }




    public void lockprice(){

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

        Call<List<Listmodel>> lockrates = apiDao.getlock_rates(liveprice,"Bearer "+AccountUtils.getAccessToken(this));
        lockrates.enqueue(new Callback<List<Listmodel>>() {
            @Override
            public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                int statuscode = response.code();
                if (statuscode == HttpsURLConnection.HTTP_ACCEPTED){
                    openwithdraw();
                    // validationapi();

//                    btnbuygold.setEnabled(true);
//                    btnbuygold.setBackgroundResource(R.drawable.buttonborder);

                }else {
                    ToastMessage.onToast(OrganizationRedeemActivity.this,"Try again",ToastMessage.ERROR);
//                    btnbuygold.setEnabled(false);
//                    btnbuygold.setBackgroundResource(R.drawable.backgroundvisablity);

                }
            }

            @Override
            public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                Log.e("lockratesfail",t.toString());
                ToastMessage.onToast(OrganizationRedeemActivity.this,"We have some issues",ToastMessage.ERROR);

            }
        });

    }

}
