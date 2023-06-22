package com.goldsikka.goldsikka.LOGIN;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_password)
    EditText et_password;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etcurrentpass)
    EditText etcurrentpass;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_confirmpassword)
    EditText et_confirmpassword;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivneweye)
    ImageView ivneweye;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivcurrenteye)
    ImageView ivcurrenteye;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_passwrd)
    TextView tv_passwrd;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvcurrent)
    TextView tvcurrentpass;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivconfirmeye)
    ImageView ivconfirmeye;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_confrmpasswrd)
    TextView tv_confrmpasswrd;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btsubmit)
    Button btsubmit;
    String st_currtntpassword,st_newpasswrod,st_cofirmnewpassword;
    ApiDao apiDao;
    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    shared_preference sharedPreference;

    TextView unameTv, uidTv, titleTv;

    RelativeLayout backbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sharedPreference = new shared_preference(this);
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);

        titleTv = findViewById(R.id.title);
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Change Password");

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        etcurrentpass.setHint(Html.fromHtml(getString(R.string.Current_Password)));
        et_password.setHint(Html.fromHtml(getString(R.string.password)));
        et_confirmpassword.setHint(Html.fromHtml(getString(R.string.confirm_password)));
        ivcurrenteye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etcurrentpass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    etcurrentpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ((ImageView)(v)).setImageResource(R.drawable.eye);
                    //Show Password
                    etcurrentpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    ((ImageView)(v)).setImageResource(R.drawable.eye_off);
                    //Hide Password
                    etcurrentpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });

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

    @OnClick(R.id.btsubmit)
    public void init_changepassword_validation(){

        tvcurrentpass.setVisibility(View.GONE);
        tv_passwrd.setVisibility(View.GONE);
        tv_confrmpasswrd.setVisibility(View.GONE);
        btsubmit.setVisibility(View.GONE);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ChangePassword.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        validation_changepassword();
                        btsubmit.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, 500);
    }

    @SuppressLint("ResourceAsColor")
    private void validation_changepassword() {
        st_currtntpassword = etcurrentpass.getText().toString().trim();
        st_newpasswrod = et_password.getText().toString().trim();
        st_cofirmnewpassword = et_confirmpassword.getText().toString().trim();

        tvcurrentpass.setVisibility(View.GONE);
        tv_passwrd.setVisibility(View.GONE);
        tv_confrmpasswrd.setVisibility(View.GONE);

        if (st_currtntpassword.isEmpty()){
            tvcurrentpass.setVisibility(View.VISIBLE);
            tvcurrentpass.setText("Please enter current password");
            tvcurrentpass.setTextColor(R.color.colorRed);

        }else if (st_newpasswrod.isEmpty()){
            tv_passwrd.setVisibility(View.VISIBLE);
            tv_passwrd.setText("Please enter new password");
            tv_passwrd.setTextColor(R.color.colorRed);

        }
        else if (!st_newpasswrod.matches(PASSWORD_PATTERN)){
            ToastMessage.onToast( ChangePassword.this," Password Should Have 1 uppercase, 1 lowercase \n 1 Numerical And special Character",ToastMessage.NOTIFY);

        }
        else if (st_cofirmnewpassword.isEmpty()){
            tv_confrmpasswrd.setVisibility(View.VISIBLE);
            tv_confrmpasswrd.setText("please enter confirm password");
            tv_confrmpasswrd.setTextColor(R.color.colorRed);
        }
        else if (!st_cofirmnewpassword.equals(st_newpasswrod)){
            tv_confrmpasswrd.setVisibility(View.VISIBLE);
            tv_confrmpasswrd.setText("Password Must Match");
            tv_confrmpasswrd.setTextColor(R.color.colorRed);
        }
        else {
            tvcurrentpass.setVisibility(View.GONE);
            tv_passwrd.setVisibility(View.GONE);
            tv_confrmpasswrd.setVisibility(View.GONE);

            change_password();
        }

    }

    private void change_password() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<Listmodel> change_password = apiDao.get_changepassword("Bearer " + AccountUtils.getAccessToken(this),
                    st_currtntpassword, st_newpasswrod, st_cofirmnewpassword);
            change_password.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    List<Listmodel> list = Collections.singletonList(response.body());
                    if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {

                        for (Listmodel listmodel : list) {
                            String st_success = listmodel.getSuccess();
                            ToastMessage.onToast(ChangePassword.this, st_success, ToastMessage.SUCCESS);

                            onlogout();
                            dialog.dismiss();

                        }
                    } else {
                        dialog.dismiss();
                        try {
                            tvcurrentpass.setVisibility(View.GONE);
                            tv_passwrd.setVisibility(View.GONE);
                            tv_confrmpasswrd.setVisibility(View.GONE);


                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            ToastMessage.onToast(ChangePassword.this, st, ToastMessage.ERROR);
                            JSONObject er = jObjError.getJSONObject("errors");
                            try {
                                JSONArray array_mobile = er.getJSONArray("currentPassword");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    tvcurrentpass.setVisibility(View.VISIBLE);
                                    tvcurrentpass.setText(array_mobile.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray array_email = er.getJSONArray("newPassword");
                                for (int i1 = 0; i1 < array_email.length(); i1++) {
                                    tv_passwrd.setVisibility(View.VISIBLE);
                                    tv_passwrd.setText(array_email.getString(i1));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            try {
                                JSONArray array_name = er.getJSONArray("confirmNewPassword");

                                for (int i = 0; i < array_name.length(); i++) {
                                    tv_confrmpasswrd.setVisibility(View.VISIBLE);
                                    tv_confrmpasswrd.setText(array_name.getString(i));

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
                  //  ToastMessage.onToast(ChangePassword.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }

    }
    public void onlogout() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<List<Listmodel>> initlogout = apiDao.get_logout("Bearer " + AccountUtils.getAccessToken(this));
            initlogout.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                        sharedPreference.WriteLoginStatus(false);
                        Intent intent = new Intent(ChangePassword.this, WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                     //   ToastMessage.onToast(ChangePassword.this, "Technical issue", ToastMessage.ERROR);
                        // Log.e("responce code", String.valueOf(response.code()));
                    }

                }

                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                    Log.e("logout fail", t.toString());
                   // ToastMessage.onToast(ChangePassword.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }
}