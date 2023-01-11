package com.goldsikka.goldsikka.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.goldsikka.goldsikka.Fragments.Ecommerce.Cartlist;
import com.goldsikka.goldsikka.Fragments.Ecommerce.orderslist;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class opencart_payment extends AppCompatActivity implements PaymentResultListener, View.OnClickListener {
    String amount, addressid;
    ApiDao apiDao;
    Button bt_continue_fail, bt_continue;

    LinearLayout ll_fail, ll_success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencart_payment);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            amount = bundle.getString("cart_amount");
            addressid = bundle.getString("addressid");
            Log.e("addreiddd", addressid);
        }

        intilizeview();


    }

    public void intilizeview() {
//        ll_success= findViewById(R.id.ll_success);
//        ll_fail= findViewById(R.id.ll_fail);

        bt_continue = findViewById(R.id.bt_continue);
        bt_continue.setOnClickListener(this);

//        bt_continue_fail = findViewById(R.id.bt_continue_fail);
//        bt_continue_fail.setOnClickListener(this);
        openpayment();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        openpayment();
//
//    }

    public void openpayment() {

        final Activity activity = this;
        final Checkout co = new Checkout();
     //   co.setKeyID("rzp_test_0VM20Pg2VIA2aR");
        co.setKeyID("rzp_live_uvxtS5LwJPMIOP");

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("payment_capture", 1);

            double total = Double.parseDouble("1");
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
//                preFill.put("email", AccountUtils.getPrefEmail(this));
//                preFill.put("contact", AccountUtils.getPrefMobile(this));

            options.put("prefill", preFill);

            co.open(activity, options);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e("paymentid", s);
        onsuccess(s);
    }

    @Override
    public void onPaymentError(int i, String s) {

        Log.e("payment_error", s);

        ll_success.setVisibility(View.GONE);
        ll_fail.setVisibility(View.VISIBLE);
    }

    public void onsuccess(String paymentid) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Listmodel> paymentsuccess = apiDao.cart_payment("Bearer " + AccountUtils.getAccessToken(this)
                , addressid, paymentid);
        paymentsuccess.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();

                Log.e("statuscode", String.valueOf(statuscode));
                Listmodel listmodel = response.body();
                Log.e("dataaa", listmodel.toString());
                if (statuscode == HttpsURLConnection.HTTP_CREATED) {

                    Log.e("dataaa", listmodel.toString());

                    dialog.dismiss();

                    ll_success.setVisibility(View.VISIBLE);
                    ll_fail.setVisibility(View.GONE);
//                    orderslist fragment = new orderslist();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layoutorderlist,fragment);
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                dialog.dismiss();
                Log.e("pay failure", t.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_continue:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
//            case R.id.bt_continue_fail:
//                Intent intent1  = new Intent(this,MainActivity.class);
//                startActivity(intent1);
//                break;

        }
    }
}