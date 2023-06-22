package com.goldsikka.goldsikka.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Activitys.Events.FileUtilty;
import com.goldsikka.goldsikka.Activitys.OTPActivity;
import com.goldsikka.goldsikka.Activitys.Profile_Details;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.WelcomeActivity;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Edit_coustomer_details extends AppCompatActivity implements View.OnClickListener {

    EditText et_name, et_mobile, et_email;
    String st_name, st_mobile, st_email;
    String rs_name, rs_mobile, rsemail, stprofileimg, stavathar;
    TextView tv_mobile, tv_email, tv_name;
    //tv_changepassword;
    Button bt_edit, submit;
    ApiDao apiDao;

    private static final String TAG = "Goldsikka";
    private static final int CAMERA_REQUEST = 1888;
    File file;
    Uri uri;
    RequestBody requestBody;
    ImageView iv_profile_image;

    //  AlertDialog alertDialogdialog;

    // GifImageView loading_gif,loading_gif1;

//    TextView tv_currentpassword,tv_newpassword,tv_confirmnewpasssword;
//    EditText et_currtntpassword,et_newpasswrod,et_cofirmnewpassword;
//    String st_currtntpassword,st_newpasswrod,st_cofirmnewpassword,st_success;
//    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    shared_preference sharedPreference;
    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;

    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_customer_details);


        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rs_name = AccountUtils.getName(this);
        rsemail = AccountUtils.getEmail(this);
        rs_mobile = AccountUtils.getMobile(this);
        stprofileimg = AccountUtils.getProfileImg(this);
        stavathar = AccountUtils.getAvathar(this);


       /* Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            rs_name = bundle.getString("name");
            rs_mobile = bundle.getString("mobile");
            rsemail = bundle.getString("email");
            stprofileimg = bundle.getString("profileimage");
            stavathar = bundle.getString("stavathar");
        }*/
        sharedPreference = new shared_preference(this);
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Edit Profile");
        intilizview();
        setHint();

    }

    public void setHint() {
        et_name.setHint(Html.fromHtml(getString(R.string.Name)));
        et_email.setHint(Html.fromHtml(getString(R.string.Email)));
        et_mobile.setHint(Html.fromHtml(getString(R.string.Phone_Number)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void intilizview() {
        et_email = findViewById(R.id.et_editemail);
        et_mobile = findViewById(R.id.et_editmobile);
        et_name = findViewById(R.id.et_editname);
        tv_email = findViewById(R.id.tv_email);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_name = findViewById(R.id.tv_name);
        iv_profile_image = findViewById(R.id.ivprofile);
        iv_profile_image.setOnClickListener(this);
//        tv_changepassword = findViewById(R.id.tv_changepassword);
//        tv_changepassword.setOnClickListener(this);
//        loading_gif1 = findViewById(R.id.loading_gif);
        bt_edit = findViewById(R.id.bt_edit);
        bt_edit.setOnClickListener(this);
        et_mobile.setText(rs_mobile);
        et_email.setText(rsemail);
        et_name.setText(rs_name);
        if (stavathar != null) {
            Glide.with(this)
                    .load(stprofileimg)
                    .into(iv_profile_image);
            // Picasso.with(this).load(stprofileimg).into(iv_profile_image);
        } else {
            iv_profile_image.setImageResource(R.drawable.profile);
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_edit:
                if (!NetworkUtils.isConnected(this)) {
                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                } else {
                    init_editvalidation();
                }
                break;
            case R.id.ivprofile:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
                break;
//            case R.id.tv_changepassword:
//                if (!NetworkUtils.isConnected(this)) {
//                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//                    return;
//                }else {
//                    open_changepassword_dialog();
//                }
//                break;
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission Granted ");
                    showChooser();
                } else {

                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
        }
    }

    private void showChooser() {
        Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent1.addCategory(Intent.CATEGORY_OPENABLE);
        intent1.setType("*/*");
        startActivityForResult(intent1, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getData() != null) {
                    try {
                        uri = data.getData();
                        assert uri != null;
                        Log.i(TAG, "Uri = " + uri.toString());
                        file = FileUtilty.getFile(this, uri);
                        Log.e("file", file.toString());
                        List<MultipartBody.Part> parts = new ArrayList<>();
                        parts.add(prepareFilePart("avatar", uri));

                        sendprofileimage(parts);

                    } catch (Exception e) {
                        Log.e(TAG, "File select error", e);
                    }
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        requestBody = RequestBody.create(MediaType.parse(Objects.requireNonNull
                (this.getContentResolver().getType(fileUri))), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
    }

    public void sendprofileimage(List<MultipartBody.Part> part) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getimg = apiDao.sendprofileimage("Bearer " + AccountUtils.getAccessToken(this), part);
            getimg.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("pprofile img code", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        Listmodel listmodel = response.body();
                        stprofileimg = listmodel.getAvatarImageLink();
                        AccountUtils.setAvathar(Edit_coustomer_details.this, listmodel.getAvatar());
                        AccountUtils.setProfileImg(Edit_coustomer_details.this, stprofileimg);
                        //Picasso.with(Edit_coustomer_details.this).load(stprofileimg).into(iv_profile_image);
                        Glide.with(Edit_coustomer_details.this)
                                .load(stprofileimg)
                                .into(iv_profile_image);

                    } else {
                        dialog.dismiss();

//                        ToastMessage.onToast(Edit_coustomer_details.this,"It's take long period. please Try after some time ",
//                                ToastMessage.ERROR);
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Toast.makeText(Edit_coustomer_details.this, message, Toast.LENGTH_SHORT).show();
                            JSONObject Object = jsonObject.getJSONObject("errors");
                            try {
                                JSONArray array_name = Object.getJSONArray("avatar");
                                Log.e("image", array_name.toString());
                                for (int i = 0; i < array_name.length(); i++) {
                                    Toast.makeText(Edit_coustomer_details.this, array_name.getString(i), Toast.LENGTH_SHORT).show();
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
                    Log.e("profile img failp", String.valueOf(t));
                    ToastMessage.onToast(Edit_coustomer_details.this, "We have  issue try after some time", ToastMessage.ERROR);
                }
            });
        }
    }

//    public void open_changepassword_dialog(){
//        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogview = inflater.inflate(R.layout.change_password_popup,null);
//        alertdilog.setCancelable(false);
//        alertdilog.setView(dialogview);
//        alertDialogdialog = alertdilog.create();
//
//        et_currtntpassword = dialogview.findViewById(R.id.et_password);
//        et_newpasswrod = dialogview.findViewById(R.id.et_newpassword);
//        et_cofirmnewpassword = dialogview.findViewById(R.id.et_confirmnewpassword);
//
//        tv_currentpassword = dialogview.findViewById(R.id.tv_password);
//        tv_newpassword = dialogview.findViewById(R.id.tv_newpassword);
//        tv_confirmnewpasssword = dialogview.findViewById(R.id.tv_confirmnewpassword);
//
//         loading_gif = dialogview.findViewById(R.id.loading_gif);
//
//
//        ImageView imageView = dialogview.findViewById(R.id.img_close);
//         submit = dialogview.findViewById(R.id.btn_submit);
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialogdialog.dismiss();
//            }
//        });
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                 if (!NetworkUtils.isConnected(Edit_coustomer_details.this)) {
//                    ToastMessage.onToast(Edit_coustomer_details.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//                    return;
//                }else {
//                     init_changepassword_validation();
//                 }
//            }
//        });
//
//        alertDialogdialog.show();
//    }
//    public void init_changepassword_validation(){
//
//        tv_currentpassword.setVisibility(View.GONE);
//        tv_newpassword.setVisibility(View.GONE);
//        tv_confirmnewpasssword.setVisibility(View.GONE);
//        submit.setVisibility(View.GONE);
//        loading_gif.setVisibility(View.VISIBLE);
//
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                Edit_coustomer_details.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        validation_changepassword();
//                        submit.setVisibility(View.VISIBLE);
//                        loading_gif.setVisibility(View.GONE);
//                    }
//                });
//            }
//        }, 500);
//    }
//
//    @SuppressLint("ResourceAsColor")
//    private void validation_changepassword() {
//        st_currtntpassword = et_currtntpassword.getText().toString().trim();
//        st_newpasswrod = et_newpasswrod.getText().toString().trim();
//        st_cofirmnewpassword = et_cofirmnewpassword.getText().toString().trim();
//
//        tv_currentpassword.setVisibility(View.GONE);
//        tv_newpassword.setVisibility(View.GONE);
//        tv_confirmnewpasssword.setVisibility(View.GONE);
//
//        if (st_currtntpassword.isEmpty()){
//           // et_currtntpassword.setError("Please enter current password");
//            tv_currentpassword.setVisibility(View.VISIBLE);
//            tv_currentpassword.setText("Please enter current password");
//            tv_currentpassword.setTextColor(R.color.colorRed);
//
//        }else if (st_newpasswrod.isEmpty()){
//
//           // et_newpasswrod.setError("Please enter new password");
//            tv_newpassword.setVisibility(View.VISIBLE);
//            tv_newpassword.setText("Please enter new password");
//            tv_newpassword.setTextColor(R.color.colorRed);
//
//        }
//        else if (!st_newpasswrod.matches(PASSWORD_PATTERN)){
//            ToastMessage.onToast( Edit_coustomer_details.this," Password Should Have 1 uppercase, 1 lowercase \n 1 Numerical And special Character",ToastMessage.NOTIFY);
//
//        }
//        else if (st_cofirmnewpassword.isEmpty()){
//          // et_cofirmnewpassword.setError("please enter confirm password");
//            tv_confirmnewpasssword.setVisibility(View.VISIBLE);
//            tv_confirmnewpasssword.setText("please enter confirm password");
//            tv_confirmnewpasssword.setTextColor(R.color.colorRed);
//        }
//        else if (!st_cofirmnewpassword.equals(st_newpasswrod)){
//            // et_cofirmnewpassword.setError("please enter confirm password");
//            tv_confirmnewpasssword.setVisibility(View.VISIBLE);
//            tv_confirmnewpasssword.setText("Password Must Match");
//            tv_confirmnewpasssword.setTextColor(R.color.colorRed);
//        }
//        else {
//            tv_currentpassword.setVisibility(View.GONE);
//            tv_newpassword.setVisibility(View.GONE);
//            tv_confirmnewpasssword.setVisibility(View.GONE);
//
//            change_password();
//        }
//
//    }
//
//    private void change_password() {
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Please Wait....");
//        dialog.setCancelable(false);
//        dialog.show();
//
//        if (!NetworkUtils.isConnected(this)) {
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//            dialog.dismiss();
//            return;
//        }else {
//            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//
//            Call<Listmodel> change_password = apiDao.get_changepassword("Bearer " + AccountUtils.getAccessToken(this),
//                    st_currtntpassword, st_newpasswrod, st_cofirmnewpassword);
//            change_password.enqueue(new Callback<Listmodel>() {
//                @Override
//                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
//                    int statuscode = response.code();
//                    List<Listmodel> list = Collections.singletonList(response.body());
//                    if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {
//                        alertDialogdialog.show();
//                        for (Listmodel listmodel : list) {
//                            st_success = listmodel.getSuccess();
//                            ToastMessage.onToast(Edit_coustomer_details.this, st_success, ToastMessage.SUCCESS);
//                            onlogout();
//
//                            dialog.dismiss();
//                            alertDialogdialog.dismiss();
//                        }
//                    } else {
//                        dialog.dismiss();
//                        alertDialogdialog.show();
//                        try {
//                            tv_currentpassword.setVisibility(View.GONE);
//                            tv_newpassword.setVisibility(View.GONE);
//                            tv_confirmnewpasssword.setVisibility(View.GONE);
//
//
//                            assert response.errorBody() != null;
//                            JSONObject jObjError = new JSONObject(response.errorBody().string());
//                            String st = jObjError.getString("message");
//                            ToastMessage.onToast(Edit_coustomer_details.this, st, ToastMessage.ERROR);
//                            JSONObject er = jObjError.getJSONObject("errors");
//                            try {
//                                JSONArray array_mobile = er.getJSONArray("currentPassword");
//                                for (int i = 0; i < array_mobile.length(); i++) {
//                                    //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
//                                    tv_currentpassword.setVisibility(View.VISIBLE);
//                                    tv_currentpassword.setText(array_mobile.getString(i));
//
//                                    //  Log.e("email",array_mobile.toString());
//                                    //   etmobile.setError(array_mobile.getString(i));
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                            try {
//                                JSONArray array_email = er.getJSONArray("newPassword");
//                                for (int i1 = 0; i1 < array_email.length(); i1++) {
//                                    tv_newpassword.setVisibility(View.VISIBLE);
//                                    //   etemail.setError(array_email.getString(i).toString());
//                                    tv_newpassword.setText(array_email.getString(i1));
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//
//                            try {
//                                JSONArray array_name = er.getJSONArray("confirmNewPassword");
//
//                                for (int i = 0; i < array_name.length(); i++) {
//                                    tv_confirmnewpasssword.setVisibility(View.VISIBLE);
//                                    tv_confirmnewpasssword.setText(array_name.getString(i));
//                                    //  etname.setError(array_name.getString(i).toString());
//                                }
////
//                            } catch (Exception e) {
//                                e.printStackTrace();
//
//                            }
//                        } catch (JSONException | IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Listmodel> call, Throwable t) {
//                    dialog.dismiss();
//                    ToastMessage.onToast(Edit_coustomer_details.this, "We have some issues", ToastMessage.ERROR);
//                }
//            });
//        }
//
//    }
//    public void onlogout(){
//        if (!NetworkUtils.isConnected(this)) {
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//            return;
//        }else {
//            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//            Call<List<Listmodel>> initlogout = apiDao.get_logout("Bearer " + AccountUtils.getAccessToken(this));
//            initlogout.enqueue(new Callback<List<Listmodel>>() {
//                @Override
//                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
//                    int stauscode = response.code();
//                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
//                        sharedPreference.WriteLoginStatus(false);
//                        Intent intent = new Intent(Edit_coustomer_details.this, WelcomeActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                    } else {
//                        ToastMessage.onToast(Edit_coustomer_details.this, "Technical issue", ToastMessage.ERROR);
//                        // Log.e("responce code", String.valueOf(response.code()));
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
//                    Log.e("logout fail", t.toString());
//                    ToastMessage.onToast(Edit_coustomer_details.this, "We have some issues", ToastMessage.ERROR);
//                }
//            });
//        }
//
//    }

    public void init_editvalidation() {

        tv_mobile.setVisibility(View.GONE);
        tv_email.setVisibility(View.GONE);
        tv_name.setVisibility(View.GONE);
        bt_edit.setVisibility(View.GONE);

        //  loading_gif1.setVisibility(View.VISIBLE);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Edit_coustomer_details.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        validation();
                        bt_edit.setVisibility(View.VISIBLE);
                        //  loading_gif1.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);
    }

    private void validation() {
        st_email = et_email.getText().toString().trim();
        st_mobile = et_mobile.getText().toString().trim();
        st_name = et_name.getText().toString().trim();

        tv_mobile.setVisibility(View.GONE);
        tv_email.setVisibility(View.GONE);
        tv_name.setVisibility(View.GONE);

        if (st_name.isEmpty()) {
            tv_name.setVisibility(View.VISIBLE);
            tv_name.setText("please enter name");
        } else if (st_email.isEmpty()) {
            tv_email.setVisibility(View.VISIBLE);
            tv_email.setText("please enter mobile");
        } else if (!st_email.matches(emailPattern)) {
            tv_email.setVisibility(View.VISIBLE);
            tv_email.setText("The Valid email is required. ");
            // Toast.makeText(getApplicationContext(), "The email field is required. ", Toast.LENGTH_SHORT).show();
        } else if (st_mobile.isEmpty()) {
            tv_mobile.setVisibility(View.VISIBLE);
            tv_mobile.setText("please enter mobile");
        } else {
            editprofile();
        }
    }

    private void editprofile() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Listmodel> edit_profile = apiDao.get_editprifile_details("Bearer " + AccountUtils.getAccessToken(this), st_name, st_email, st_mobile);
        edit_profile.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                List<Listmodel> list = Collections.singletonList(response.body());
                if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {
                    for (Listmodel listmodel : list) {

                        ToastMessage.onToast(Edit_coustomer_details.this, "Successfully Updated..", ToastMessage.SUCCESS);

                        onsuccess();
                        dialog.dismiss();
                    }
                } else {
                    dialog.dismiss();
                    try {
                        tv_mobile.setVisibility(View.GONE);
                        tv_email.setVisibility(View.GONE);
                        tv_name.setVisibility(View.GONE);


                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String st = jObjError.getString("message");
                        ToastMessage.onToast(Edit_coustomer_details.this, st, ToastMessage.ERROR);
                        JSONObject er = jObjError.getJSONObject("errors");
                        try {
                            JSONArray array_mobile = er.getJSONArray("mobile");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tv_mobile.setVisibility(View.VISIBLE);
                                tv_mobile.setText(array_mobile.getString(i));
                                //  Log.e("email",array_mobile.toString());
                                //   etmobile.setError(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONArray array_email = er.getJSONArray("email");
                            for (int i1 = 0; i1 < array_email.length(); i1++) {
                                tv_email.setVisibility(View.VISIBLE);
                                //   etemail.setError(array_email.getString(i).toString());
                                tv_email.setText(array_email.getString(i1));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        try {
                            JSONArray array_name = er.getJSONArray("name");

                            for (int i = 0; i < array_name.length(); i++) {
                                tv_name.setVisibility(View.VISIBLE);
                                tv_name.setText(array_name.getString(i));
                                //  etname.setError(array_name.getString(i).toString());
                            }
//
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
                ToastMessage.onToast(Edit_coustomer_details.this, "We have some issues", ToastMessage.ERROR);
            }
        });
    }

    private void onsuccess() {
        onBackPressed();
    }


}
