package com.goldsikka.goldsikka.LOGIN;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.goldsikka.goldsikka.LOGIN.LoginActivity;
import com.goldsikka.goldsikka.LOGIN.RegistationActivity;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.OrganizationWalletModule.Organizationwallet_mainpage;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;

public class WelcomeActivity extends AppCompatActivity {

    public shared_preference sharedPreference;
    Button proceedemoaccount;
    //   public static String fromurl = "https://staging-api.dev.goldsikka.in/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sharedPreference = new shared_preference(this);
        if (sharedPreference.readloginstatus()) {
            if (AccountUtils.getRoleid(this).equals("1")) {
                startActivity(new Intent(this, MainFragmentActivity.class));
                finish();
            } else if (AccountUtils.getRoleid(this).equals("2")) {
                startActivity(new Intent(this, Organizationwallet_mainpage.class));
                finish();
            }
        }
    }

   /* public void proceedemoaccount(View view) {
        LoginActivity.isFromDemo = true;

       // ApiClient.changeApiBaseUrl("https://staging-api.dev.goldsikka.in/");

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }*/

    public void login(View view) {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            //   ApiClient.changeApiBaseUrl("https://staging-api.dev.goldsikka.in/");
            //   ApiClient.changeApiBaseUrl(BASE_URL);
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    public void signup(View view) {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            startActivity(new Intent(this, RegistationActivity.class));
        }
    }
}