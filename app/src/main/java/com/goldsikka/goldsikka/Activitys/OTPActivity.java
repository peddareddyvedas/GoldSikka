package com.goldsikka.goldsikka.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.goldsikka.goldsikka.Fragments.baseinterface;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.OrganizationWalletModule.Organizationwallet_mainpage;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class OTPActivity extends BaseActivity implements baseinterface {
    String message, verified;
    String rs_mobile, stotp, credential, UUUId;
    String accesstoken, verifytoken, roleid;
    String maskedphone, last2digits;
    //otp
    private int RESOLVE_HINT = 1001;
    private static final int SMS_CONSENT_REQUEST = 1002;  // Set to an unused request code
    private int beginIndex = 0, endIndex = 6;
    //
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_otp)
    PinView etotp;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_sendagain)
    TextView tvsendagain;
    ApiDao apiDao;
    private shared_preference sharedPreference;
    String sendagain, st_from;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.otp_edo1)
    TextView tv_otp;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvmobile)
    TextView tvmobile;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvotpmsg)
    TextView tvotpmsg;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.llsendagain)
    LinearLayout llsendagain;

//    @SuppressLint("NonConstantResourceId")
//    @BindView(value = R.id.iv_back)
//    ImageView iv_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tv_second_title)
    TextView tv_second_title;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.btn_submit)
    Button btn_submit;

    String token;

    public static boolean isFromDeliverye = false;
    String vvjjlyht = "jfj";

    // GifImageView loading_gif;
    View view;
    String readOTP;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_o_t_p;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        ButterKnife.bind(this);

        sharedPreference = new shared_preference(this);
        // loading_gif = findViewById(R.id.loading_gif);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            rs_mobile = bundle.getString("mobile");
            credential = bundle.getString("credential");
            accesstoken = bundle.getString("accesstoken");
            verifytoken = bundle.getString("verifytoken");
            roleid = bundle.getString("roleId");
            UUUId = bundle.getString("uuid");
        }
        if (credential.equals("verifyaccount")) {
            tv_second_title.setVisibility(View.GONE);
            tvsendagain.setVisibility(View.GONE);

        } else {
            vvjjlyht = "login";
            init_timer();
        }
        Log.e("UUUId", "" + UUUId);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.Verify_Details));
        //  toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        apiDao = ApiClient.getClient(accesstoken).create(ApiDao.class);

        etotp.setText("");
        MySMSBroadcastReceiver.initSMSListener(null);    // making SMS Retriever API disable
// Toast.makeText(this, getString(R.string.sms_user_consent_api) + " listening started", Toast.LENGTH_SHORT).show();
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsVerificationReceiver, intentFilter);
        SmsRetriever.getClient(this).startSmsUserConsent(null);


//  Don't forget to remove the this block and AppSignatureHelper class before production release
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        for (String signature : appSignatureHelper.getAppSignatures()) {
            Log.e("hjhjhj", "" + signature);  // See the log and get app's hash string.
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            if (isFromDeliverye) {
                Log.e("isFromHome", "call" + isFromDeliverye);
                Intent intent = new Intent(this, Verifyaccount_Class.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Log.e("isFromHomefalse", "call");
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void init_timer() {
        long duration = 30000;
        new CountDownTimer(duration, 1000) {
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
                llsendagain.setVisibility(View.GONE);
                tvsendagain.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                tv_second_title.setVisibility(View.GONE);
                tvsendagain.setVisibility(View.VISIBLE);
                llsendagain.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @OnClick(R.id.btn_submit)
    public void init_validation(View v) {
        tv_otp.setVisibility(View.GONE);
        // loading_gif.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                OTPActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(OTPActivity.this)) {
                            ToastMessage.onToast(OTPActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            return;
                        } else {
                            validation(v);
                            btn_submit.setVisibility(View.VISIBLE);
                            // loading_gif.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }, 500);
    }

    public void validation(View view) {
        stotp = etotp.getText().toString().trim();
        sendagain = "1";
        tv_otp.setVisibility(View.GONE);
        if (stotp.isEmpty()) {
            tv_otp.setVisibility(View.VISIBLE);
            tv_otp.setText("Please Enter OTP");
//                etotp.setError("Please Enter OTP");
        } else {
            if (credential.equals("signup")) {
                st_from = "0";
                Call<Listmodel> call = apiDao.getotp(stotp, verifytoken);
                responsemethod(call);
            } else if (credential.equals("verifyaccount")) {
                st_from = "1";
                Call<Listmodel> call = apiDao.getotp(stotp, accesstoken);
                responsemethod(call);
            } else if (credential.equals("verifypin")) {
                st_from = "3";
                Call<Listmodel> call = apiDao.getotppin(stotp, accesstoken);
                responsemethod(call);

            } else {
                st_from = "2";
                Call<Listmodel> call = apiDao.getlogin_otp("Bearer " + accesstoken, stotp, UUUId);
                responsemethod(call);
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tv_sendagain)
    public void resend(View view) {
        sendagain = "0";
        if (credential.equals("verifypin")) {
            Call<Listmodel> call1 = apiDao.resendotp("Bearer " + AccountUtils.getAccessToken(this));
            //  ,rs_mobile
            responsemethod(call1);
        } else {
            Call<Listmodel> call1 = apiDao.resendotp("Bearer " + accesstoken);
            //  ,rs_mobile
            responsemethod(call1);
        }
    }

    @Override
    public void responce(@NonNull Response<Listmodel> response, int stauscode) {

        if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
            List<Listmodel> list = Collections.singletonList(response.body());

            for (Listmodel listmodel : list) {
                if (sendagain.equals("1")) {
                    if (st_from.equals("1")) {
                        tv_second_title.setVisibility(View.GONE);
                        tvsendagain.setVisibility(View.GONE);
                        llsendagain.setVisibility(View.GONE);
                        Intent intent = new Intent(OTPActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Log.e("loginds", "okkk login");
                    } else if (st_from.equals("0")) {
                        verified = listmodel.getOtpverofy();
                        message = listmodel.getMessage();
                        facebookEvents();
                        onsucess();

                    } else if (st_from.equals("2")) {
                        verified = listmodel.getOtpverofy();
                        message = listmodel.getMessage();
                        onsucess();
                    } else if (st_from.equals("3")) {
                        token = listmodel.getToken();
                        Intent intent = new Intent(OTPActivity.this, CreatePIN.class);
                        intent.putExtra("from", "forgotpin");
                        intent.putExtra("token", token);
                        intent.putExtra("isPin", false);
                        startActivity(intent);
                    }
                } else if (sendagain.equals("0")) {
                    message = listmodel.getMessage();
                    maskedphone = listmodel.getMaskedPhone();
                    last2digits = listmodel.getLast2digits();
                    tvotpmsg.setText(message);
                    tvmobile.setText(maskedphone);
                    // ToastMessage.onToast(this,message+"\n"+maskedphone,ToastMessage.SUCCESS);
                    init_timer();
                }
            }
        } else {
            try {
                tv_otp.setVisibility(View.VISIBLE);
                assert response.errorBody() != null;
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                String st = jObjError.getString("message");
                tv_otp.setText(st);
                JSONObject er = jObjError.getJSONObject("errors");
                ToastMessage.onToast(OTPActivity.this, st, ToastMessage.ERROR);


            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }


    }


    @Override
    public void listresponce(@NonNull Response<List<Listmodel>> response, int stauscode) {

    }

    public void onsucess() {
        sharedPreference.WriteLoginStatus(true);
        AccountUtils.setAccessToken(this, accesstoken);
        AccountUtils.setRoleid(this, roleid);
        if (roleid.equals("1")) {
            Intent intent = new Intent(this, MainFragmentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else if (roleid.equals("2")) {
            Intent intent = new Intent(this, Organizationwallet_mainpage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }

    public void facebookEvents() {
        logCompleteRegistrationEvent();
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
    }

    public void logCompleteRegistrationEvent() {
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Complete Registration");
    }


    ///extracting the otp number from otp string
    public String extractDigits(String src) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            if (Character.isDigit(c)) {
                builder.append(c);
                Log.e("builder", " " + builder);
            }
        }
        return builder.toString();
    }

    ///////////////////////

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // This block for request hint --> getting user phone number.
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (credential != null) {
                    ////   will need to process phone number string
                    credential.getId();
//                    Toast.makeText(this, credential.getId(), Toast.LENGTH_LONG).show();
                    //  sendPhoneNumberToServer(credential.getId());
                }
            }
        } else if (requestCode == SMS_CONSENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Get SMS message content
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                // Extract one-time code from the message and complete verification
                // `sms` contains the entire text of the SMS message, so you will need
                // to parse the string.
                if (message != null) {
                  /*  Log.e("hjhjhj","" + message);
                    Log.e("hjhjhj","" + (parseOneTimeCode(message)));*/
                    etotp.setText(parseOneTimeCode(message));
                }
                // send one time code to the server
            } else {
                // Consent canceled, handle the error ...
            }
        }
    }

    private String parseOneTimeCode(String otp) {
        init_validation(view);
        return otp.substring(beginIndex, endIndex);
    }


    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent

                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);


//                            String message = consentIntent.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
//
//                            if (message != null) {
//                  /*  Log.e("hjhjhj","" + message);
//                    Log.e("hjhjhj","" + (parseOneTimeCode(message)));*/
//                                etotp.setText(parseOneTimeCode(message));
//                            }

                        } catch (ActivityNotFoundException e) {
                            // Handle the exception ...
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        break;
                }
            }
        }
    };
}