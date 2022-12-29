package com.goldsikka.goldsikka.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.goldsikka.goldsikka.Activitys.MoneyWallet.AddMonet_to_Wallet;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class RazorpayPayment extends AppCompatActivity implements PaymentResultListener {

    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay_payment);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            amount = bundle.getString("amount");
        }

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
            options.put("prefill.contact", AccountUtils.getMobile(this));
            options.put("prefill.email", AccountUtils.getEmail(this));

            // double total = Double.parseDouble("1");
            double total = Double.parseDouble(amount);

            total = total * 100;
            options.put("amount", total);

//                JSONObject preFill = new JSONObject();

            // preFill.put("email", AccountUtils.getEmail(activity));
            //preFill.put("contact", AccountUtils.getMobile(activity));

            // options.put("prefill", preFill);

            co.open(activity, options);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        Intent intent = new Intent();
        intent.putExtra("PaymentID", s);
        setResult(1212, intent);
        finish();

        ToastMessage.onToast(this, "Payment sucess", ToastMessage.SUCCESS);

    }

    @Override
    public void onPaymentError(int i, String s) {
        ToastMessage.onToast(this, "Payment Failed", ToastMessage.ERROR);
        startActivity(new Intent(this, AddMonet_to_Wallet.class));

    }
}