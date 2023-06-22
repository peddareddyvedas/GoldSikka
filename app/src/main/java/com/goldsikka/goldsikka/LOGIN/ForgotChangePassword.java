package com.goldsikka.goldsikka.LOGIN;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotChangePassword extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_password)
    EditText et_password;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_confirmpassword)
    EditText et_confirmpassword;
    
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivneweye)
    ImageView ivneweye;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_passwrd)
    TextView tv_passwrd;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivconfirmeye)
    ImageView ivconfirmeye;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_confrmpasswrd)
    TextView tv_confrmpasswrd;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btsubmit)
    Button btsubmit;
    String rs_email,stpassword,stconfirmpassword;
    ApiDao apiDao;
    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    TextView unameTv, uidTv, titleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_change_password);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            rs_email = bundle.getString("rsemail");
        }

        et_password.setHint(Html.fromHtml(getString(R.string.New_Password)));
        et_confirmpassword.setHint(Html.fromHtml(getString(R.string.Confirm_Password)));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Forgot Password");
//        unameTv = findViewById(R.id.uname);
//        uidTv = findViewById(R.id.uid);
//        titleTv = findViewById(R.id.title);

//        unameTv.setText(AccountUtils.getName(this));
//        uidTv.setText(AccountUtils.getCustomerID(this));
//        titleTv.setVisibility(View.VISIBLE);
//        titleTv.setText("Forgot Password");
        //  toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ivneweye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ((ImageView)(v)).setImageResource(R.drawable.eye);
                    //Show Password
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    ((ImageView)(v)).setImageResource(R.drawable.eye_off);
                    //Hide Password
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });
        ivconfirmeye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_confirmpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    et_confirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ((ImageView)(v)).setImageResource(R.drawable.eye);
                    //Show Password
                    et_confirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    ((ImageView)(v)).setImageResource(R.drawable.eye_off);
                    //Hide Password
                    et_confirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent  = new Intent(this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return false;
        }

        return super.onOptionsItemSelected(item);
    }
    @OnClick(R.id.btsubmit)
    public void initresetpasswordvalidation(){
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            tv_passwrd.setVisibility(View.GONE);
            tv_confrmpasswrd.setVisibility(View.GONE);
            btsubmit.setVisibility(View.GONE);
            resetpassvalidation();
//
//
//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    try {
//
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    ForgotChangePassword.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                        }
//                    });
//                }
//            }, 500);
        }

    }
    public void resetpassvalidation(){

        tv_confrmpasswrd.setVisibility(View.GONE);
        tv_passwrd.setVisibility(View.GONE);
        stpassword = et_password.getText().toString().trim();
        stconfirmpassword = et_confirmpassword.getText().toString().trim();

        if (stpassword.isEmpty()){

            btsubmit.setVisibility(View.VISIBLE);
            tv_passwrd.setVisibility(View.VISIBLE);
            tv_passwrd.setText("please enter the password");
        }else if (!stpassword.matches(PASSWORD_PATTERN)){
            btsubmit.setVisibility(View.VISIBLE);
            tv_passwrd.setVisibility(View.GONE);
            ToastMessage.onToast(ForgotChangePassword.this," Password Should Have 1 uppercase, 1 lowercase \n 1 Numerical And special Character",ToastMessage.NOTIFY);
        }else if (stconfirmpassword.isEmpty()){
            btsubmit.setVisibility(View.VISIBLE);
            tv_confrmpasswrd.setText("Palese enter the confirm password");
            tv_confrmpasswrd.setVisibility(View.VISIBLE);
        }else if (!stconfirmpassword.equals(stpassword)){
            btsubmit.setVisibility(View.VISIBLE);
            tv_confrmpasswrd.setVisibility(View.VISIBLE);
            tv_confrmpasswrd.setText("confirm password and password must match");
        }else {
            if (!NetworkUtils.isConnected(this)){
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                return;
            }else {
                resetapicall();
                btsubmit.setVisibility(View.GONE);
            }
        }



    }
    public void resetapicall(){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<Listmodel> getotp = apiDao.get_forgot_changepassword(rs_email,stpassword,stconfirmpassword);
        getotp.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                Listmodel listmodel = response.body();
                if (statuscode == HttpsURLConnection.HTTP_OK){
                    dialog.dismiss();
                    String  msg = listmodel.getMessage();
//                    ToastMessage.onToast(ForgotChangePassword.this,msg,ToastMessage.SUCCESS);
                    onsucess();
                    btsubmit.setVisibility(View.VISIBLE);
                }else {
                    dialog.dismiss();
                    btsubmit.setVisibility(View.VISIBLE);

                    assert response.errorBody() != null;

                    tv_passwrd.setVisibility(View.GONE);
                    tv_confrmpasswrd.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String st = jsonObject.getString("message");
//                        ToastMessage.onToast(ForgotChangePassword.this,st,ToastMessage.ERROR);
                        JSONObject er = jsonObject.getJSONObject("errors");

                        try {
                            JSONArray emailmobile = er.getJSONArray("password");
                            for (int i = 0; i < emailmobile.length(); i++) {
                                tv_passwrd.setVisibility(View.VISIBLE);
                                tv_passwrd.setText(emailmobile.getString(i));

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        try {
                            JSONArray emailmobile = er.getJSONArray("confirmPassword");
                            for (int i = 0; i < emailmobile.length(); i++) {
                                tv_confrmpasswrd.setVisibility(View.VISIBLE);
                                tv_confrmpasswrd.setText(emailmobile.getString(i));

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    } catch (JSONException | IOException jsonException) {
                        jsonException.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                dialog.dismiss();
               // ToastMessage.onToast(ForgotChangePassword.this,"we have some issues",ToastMessage.ERROR);

            }
        });
    }
    public void onsucess(){

        Intent intent = new Intent(ForgotChangePassword.this, LoginActivity.class);
        startActivity(intent);
    }
}