package com.goldsikka.goldsikka.OrganizationWalletModule;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Activitys.Events.FileUtilty;
import com.goldsikka.goldsikka.Fragments.Edit_coustomer_details;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ORZProfileEdit extends AppCompatActivity implements View.OnClickListener {

    EditText et_name,et_mobile,et_email;
    String st_name,st_mobile,st_email;
    String rs_name,rs_mobile,rsemail,stprofileimg,stavathar;
    TextView tv_mobile,tv_email,tv_name,tvprofilestatus;
    Button bt_edit;
    ApiDao apiDao;

    private static final String TAG = "Goldsikka";
    private static final int CAMERA_REQUEST = 1888;
    File file;
    Uri uri;
    RequestBody requestBody;
    ImageView iv_profile_image;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    shared_preference sharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_r_z_profile_edit);


        Bundle bundle = getIntent().getExtras();
        if (bundle!= null) {
            rs_name = bundle.getString("name");
            rs_mobile = bundle.getString("mobile");
            rsemail = bundle.getString("email");
            stprofileimg = bundle.getString("profileimage");
            stavathar = bundle.getString("stavathar");
        }
        sharedPreference = new shared_preference(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit Profile");
        // toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        intilizview();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addressmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return false;
        }
        if (item.getItemId()==R.id.maddaddress){
            Openeditlist();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void Openeditlist(){
        Intent intent = new Intent(this,OrgProfileEditList.class);
        startActivity(intent);
    }
    public void intilizview(){
        et_email = findViewById(R.id.et_editemail);
        et_mobile = findViewById(R.id.et_editmobile);
        et_name = findViewById(R.id.et_editname);
        tv_email = findViewById(R.id.tv_email);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_name = findViewById(R.id.tv_name);
        iv_profile_image = findViewById(R.id.ivprofile);
        iv_profile_image.setOnClickListener(this);
        tvprofilestatus = findViewById(R.id.tvprofilestatus);
        bt_edit = findViewById(R.id.bt_edit);
        bt_edit.setOnClickListener(this);
        et_mobile.setText(rs_mobile);
        et_email.setText(rsemail);
        et_name.setText(rs_name);
        if (stavathar!= null){
            Glide.with(this)
                    .load(stprofileimg)
                    .into(iv_profile_image);
            // Picasso.with(this).load(stprofileimg).into(iv_profile_image);
        }else {
            iv_profile_image.setImageResource(R.drawable.profile);
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_edit:
                if (!NetworkUtils.isConnected(this)) {
                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                }else {
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
                if ( data != null && data.getData() != null){
                    try {
                        uri = data.getData();
                        assert uri != null;
                        Log.i(TAG, "Uri = " + uri.toString());
                        file = FileUtilty.getFile(this, uri);
                        Log.e("file",file.toString());
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

        return MultipartBody.Part.createFormData(partName, file.getName(),requestBody);
    }

    public void sendprofileimage(List<MultipartBody.Part> part){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }
        else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getimg = apiDao.sendprofileimage("Bearer "+AccountUtils.getAccessToken(this),part);
            getimg.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("pprofile img code", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK){
                        dialog.dismiss();
                        Listmodel listmodel = response.body();
                        stprofileimg = listmodel.getAvatarImageLink();
                        AccountUtils.setAvathar(ORZProfileEdit.this,listmodel.getAvatar());
                        AccountUtils.setProfileImg(ORZProfileEdit.this,stprofileimg);
                        //Picasso.with(ORZProfileEdit.this).load(stprofileimg).into(iv_profile_image);
                        Glide.with(ORZProfileEdit.this)
                                .load(stprofileimg)
                                .into(iv_profile_image);

                    }else {
                        dialog.dismiss();

//                        ToastMessage.onToast(ORZProfileEdit.this,"It's take long period. please Try after some time ",
//                                ToastMessage.ERROR);
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Toast.makeText(ORZProfileEdit.this, message, Toast.LENGTH_SHORT).show();
                            JSONObject Object = jsonObject.getJSONObject("errors");
                            try {
                                JSONArray array_name = Object.getJSONArray("avatar");
                                Log.e("image",array_name.toString());
                                for (int i = 0; i < array_name.length(); i++) {
                                    Toast.makeText(ORZProfileEdit.this, array_name.getString(i), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("profile img failp", String.valueOf(t));
                    ToastMessage.onToast(ORZProfileEdit.this,"We have  issue try after some time",ToastMessage.ERROR);
                }
            });
        }
    }


    public void init_editvalidation(){

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

                ORZProfileEdit.this.runOnUiThread(new Runnable() {
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

        if (st_name.isEmpty()){
            tv_name.setVisibility(View.VISIBLE);
            tv_name.setText("please enter name");
        }else if (st_email.isEmpty()){
            tv_email.setVisibility(View.VISIBLE);
            tv_email.setText("please enter mobile");
        }  else if (!st_email.matches(emailPattern)) {
            tv_email.setVisibility(View.VISIBLE);
            tv_email.setText("The Valid email is required. ");
            // Toast.makeText(getApplicationContext(), "The email field is required. ", Toast.LENGTH_SHORT).show();
        }
        else if (st_mobile.isEmpty()){
            tv_mobile.setVisibility(View.VISIBLE);
            tv_mobile.setText("please enter mobile");
        }
        else {
            editprofile();
        }
    }

    private void editprofile() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Listmodel> edit_profile = apiDao.org_editprifile_details("Bearer "+AccountUtils.getAccessToken(this),st_name,st_email,st_mobile);
        edit_profile.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                List<Listmodel> list = Collections.singletonList(response.body());
                if (statuscode == HttpsURLConnection.HTTP_CREATED||statuscode==HttpsURLConnection.HTTP_OK){
                    for (Listmodel listmodel : list){

                        ToastMessage.onToast( ORZProfileEdit.this, listmodel.getMessage(),ToastMessage.SUCCESS);

                        tvprofilestatus.setText(listmodel.getMessage());
                        onsuccess();
                        dialog.dismiss();
                    }
                }else {
                    dialog.dismiss();
                    try {
                        tv_mobile.setVisibility(View.GONE);
                        tv_email.setVisibility(View.GONE);
                        tv_name.setVisibility(View.GONE);

//
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String st = jObjError.getString("message");
                        ToastMessage.onToast( ORZProfileEdit.this, st, ToastMessage.ERROR);
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

                        }} catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                dialog.dismiss();
               // ToastMessage.onToast( ORZProfileEdit.this,"We have some issues",ToastMessage.ERROR);
            }
        });
    }

    private void onsuccess() {
        Intent intent = new Intent( ORZProfileEdit.this, OrgProfileEditList.class);
        startActivity(intent);
    }

}