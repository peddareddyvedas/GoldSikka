package com.goldsikka.goldsikka.Activitys;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.Activitys.Events.FileUtilty;
import com.goldsikka.goldsikka.Activitys.Profile.CustomerAddAddress;
import com.goldsikka.goldsikka.Activitys.Profile.CustomerAddressList;
import com.goldsikka.goldsikka.Activitys.Profile.CustomerEditAddress;
import com.goldsikka.goldsikka.FileUtiltyforprofile;
import com.goldsikka.goldsikka.Fragments.Customer_AddBankDetails;
import com.goldsikka.goldsikka.Fragments.Customer_BankDetailslist;
import com.goldsikka.goldsikka.Fragments.Edit_coustomer_details;
import com.goldsikka.goldsikka.Fragments.Get_kyc_details_fragment;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile_Details extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.customer_address)
    TextView tv_customer_address;



    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_addresstitle)
    TextView tv_addresstitle;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvcity)
    TextView tv_city;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvpin)
    TextView tv_pin;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvstate)
    TextView tv_state;
    String address,state,pin,city,StAddressId,StAddressTitle;
    ApiDao apiDao;
    Bundle bundle;
    String addressId;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_accountname)
    TextView tv_accountname;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_accountnumber)
    TextView tv_accountnumber;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_bank)
    TextView tv_bank;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_branch)
    TextView tv_branch;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_ifsccode)
    TextView tv_ifsccode;
    String st_accont_name,st_accountnumber,st_bankname,st_bankbranch,st_ifsc,StBankId,stprofileimg;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_email)
    TextView tv_email;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    String st_name, st_mobile,st_email;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_kyc)
    TextView tv_kyc;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.address_edit)
    ImageView address_edit;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bankdetails_edit)
    ImageView bankdetails_edit;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_profile_image)
    ImageView iv_profile_image;
TextView unameTv, uidTv, titleTv;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__details);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Profile");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Profile");
        // toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        bundle = getIntent().getExtras();
        if (bundle != null){
            addressId = bundle.getString("id");
        }

        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {
            setprofile_details();
            setbankdetails();
            setaddress();
        }
    }

//    @Override
//    protected void onResume() {
//        setprofile_details();
//        setbankdetails();
//        setaddress();
//        super.onResume();
//    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
       // finish();
    }

    private void setprofile_details() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> profile_details = apiDao.getprofile_details("Bearer " + AccountUtils.getAccessToken(this));
            profile_details.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    List<Listmodel> list = Collections.singletonList(response.body());
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {
                        for (Listmodel listmodel : list) {
                            st_email = listmodel.getEmail();
                            st_name = listmodel.getName();
                            st_mobile = listmodel.getMobile();
                            AccountUtils.setName(Profile_Details.this,st_name);
                            AccountUtils.setMobile(Profile_Details.this,st_mobile);
                            AccountUtils.setEmail(Profile_Details.this,st_email);
                            if (listmodel.getAvatar()!=null) {
                                Picasso.with(Profile_Details.this).load(listmodel.getAvatarImageLink()).into(iv_profile_image);
                            }else {

                            }
                            set_profiledetails();
                            dialog.dismiss();

                        }
                    } else {
                        dialog.dismiss();
                       // ToastMessage.onToast(Profile_Details.this, "Technical problem", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                 //   ToastMessage.onToast(Profile_Details.this, "We Have Some Issues", ToastMessage.ERROR);
                }
            });
        }
    }

    private void set_profiledetails() {

        tv_profile_name.setText(st_name);
        tv_mobile.setText("+"+st_mobile);
        tv_email.setText(st_email);
//
//        AccountUtils.setMobile(this,st_mobile);
//        AccountUtils.setEmail(this,st_email);
//        AccountUtils.setName(this,st_name);
    }



        @SuppressLint("NonConstantResourceId")
        @OnClick(R.id.bt_add_adderss)
          public void open_add_address() {
            if (!NetworkUtils.isConnected(this)){
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                return;
            }else {
                Intent intent = new Intent(this, CustomerAddAddress.class);
                intent.putExtra("from", "profiles");
                startActivity(intent);
            }
//
        }
        @SuppressLint("NonConstantResourceId")
        @OnClick(R.id.bt_list_adderss)
        public void openaddresslist(){
            if (!NetworkUtils.isConnected(this)){
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                return;
            }else {
                Intent intent = new Intent(this, CustomerAddressList.class);
                startActivity(intent);
            }

        }
        @SuppressLint("NonConstantResourceId")
        @OnClick(R.id.add_bankdetails)
        public void openadd_bankdetails(){
            if (!NetworkUtils.isConnected(this)){
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                return;
            }else {
                Intent intent = new Intent(this, Customer_AddBankDetails.class);
                intent.putExtra("from", "profile");
                startActivity(intent);
            }
        }
        @SuppressLint("NonConstantResourceId")
        @OnClick(R.id.bt_list_bank)
        public void openbanklist(){
            if (!NetworkUtils.isConnected(this)){
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                return;
            }else {
                Intent intent = new Intent(this, Customer_BankDetailslist.class);
                startActivity(intent);
            }
        }
        @SuppressLint("NonConstantResourceId")
        @OnClick(R.id.profile_edit)
        public void openeditprofile(){
            if (!NetworkUtils.isConnected(this)){
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                return;
            }else {
        Intent intent = new Intent(this,Edit_coustomer_details.class);

            intent.putExtra("name",st_name);
            intent.putExtra("email",st_email);
            intent.putExtra("mobile",st_mobile);
           startActivity(intent);}
        }
        @SuppressLint("NonConstantResourceId")
        @OnClick(R.id.tv_kyc)
        public void get_kycdetails(){
            if (!NetworkUtils.isConnected(this)){
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                return;
            }else {
                Intent intent = new Intent(this, Get_kyc_details_fragment.class);
                startActivity(intent);
            }
        }


        public void setaddress(){
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please Wait....");
            dialog.setCancelable(false);
            dialog.show();
            if (!NetworkUtils.isConnected(this)){
                dialog.dismiss();
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                return;
            }else {

                apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
                Call<Listmodel> get_address = apiDao.
                        get_profileaddress("Bearer " + AccountUtils.getAccessToken(this));
                get_address.enqueue(new Callback<Listmodel>() {
                    @Override
                    public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                        int statuscode = response.code();
                        List<Listmodel> list = Collections.singletonList(response.body());

                        if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK
                                || statuscode == HttpsURLConnection.HTTP_ACCEPTED) {
                            for (Listmodel listmodel : list) {
                                address = listmodel.getAddress();
                                city = listmodel.getCity();
                                pin = listmodel.getZip();
                                StAddressTitle = listmodel.getTitle();
                                StAddressId = listmodel.getId();

                                if (StAddressId == null) {
                                    address_edit.setVisibility(View.GONE);
                                    //ToastMessage.onToast(Profile_Details.this, "Please add your Address ", ToastMessage.INFO);
                                }

                                if (address == null) {
                                    dialog.dismiss();
                                    tv_customer_address.setText("xxxxxx");
                                }

                                if (StAddressTitle == null) {
                                    dialog.dismiss();
                                    tv_addresstitle.setText("xxxxxx");
                                }
                                if (city == null) {
                                    dialog.dismiss();
                                    tv_city.setText("xxxxxx");
                                }
                                if (pin == null) {
                                    dialog.dismiss();
                                    tv_pin.setText("xxxxxx");
                                    tv_state.setText("xxxxxx");
                                }
//                            if (state == null){
//                                dialog.dismiss();
//                                tv_state.setText("xxxxxx");
//                            }
                                else {
                                    JsonObject from1 = new JsonParser().parse(listmodel.getState().toString()).getAsJsonObject();
                                    try {
                                        JSONObject json_from = new JSONObject(from1.toString());
                                        state = json_from.getString("name");

                                        Log.e("state name", state);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("state name", e.toString());
                                    }
                                    StAddressTitle = listmodel.getTitle();
                                    address = listmodel.getAddress();
                                    city = listmodel.getCity();
                                    pin = listmodel.getZip();
                                    address_edit.setVisibility(View.VISIBLE);

                                    setadresstext();
                                    dialog.dismiss();
                                }

                            }
                        } else {
                            dialog.dismiss();
                            ToastMessage.onToast(Profile_Details.this, "Add Address", ToastMessage.ERROR);

                        }
                    }

                    @Override
                    public void onFailure(Call<Listmodel> call, Throwable t) {
                        Log.e("addresserror", t.toString());
                      //  ToastMessage.onToast(Profile_Details.this, "We Have Some Issues", ToastMessage.ERROR);
                    }
                });
            }

        }

    private void setadresstext() {
        tv_city.setText(city);
        tv_state.setText(state);
        tv_customer_address.setText(address);
        tv_pin.setText(pin);
        tv_addresstitle.setText(StAddressTitle);
    }

    public void setbankdetails(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getbankdetails = apiDao.get_profile_primarybankdetais("Bearer " + AccountUtils.getAccessToken(this));
            getbankdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    List<Listmodel> list = Collections.singletonList(response.body());

                    if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_ACCEPTED) {


                        for (Listmodel listmodel : list) {
                            st_accont_name = listmodel.getName_on_account();
                            st_accountnumber = listmodel.getAccount_number();
                            st_bankname = listmodel.getBank_name();
                            st_bankbranch = listmodel.getBank_branch();
                            st_ifsc = listmodel.getIfsc_code();
                            StBankId = listmodel.getId();

                            if (StBankId == null || StBankId.isEmpty()) {
                                //ToastMessage.onToast(Profile_Details.this, "Please add your Bank Details", ToastMessage.INFO);
                                bankdetails_edit.setVisibility(View.GONE);
                            }

                            if (st_accountnumber == null || st_accountnumber.isEmpty()) {
                                dialog.dismiss();

                                tv_accountnumber.setText("xxxxxx");
                                //   Toast.makeText(Profile_Details.this, "add details", Toast.LENGTH_SHORT).show();
                            }
                            if (st_accont_name == null) {
                                dialog.dismiss();
                                tv_accountname.setText("xxxxxx");
                            }
                            if (st_bankname == null) {
                                dialog.dismiss();
                                tv_bank.setText("xxxxxx");
                            }
                            if (st_bankbranch == null) {
                                dialog.dismiss();
                                tv_branch.setText("xxxxxx");
                            }
                            if (st_ifsc == null) {
                                dialog.dismiss();
                                tv_ifsccode.setText("xxxxxx");
                            } else {
                                bankdetails_edit.setVisibility(View.VISIBLE);
                                settextbankdetails();
                                dialog.dismiss();
                            }
                        }
                    } else {
                      //  ToastMessage.onToast(Profile_Details.this, "Technical issue", ToastMessage.WARNING);
                    }


                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                   // ToastMessage.onToast(Profile_Details.this, "We Have Some Issues", ToastMessage.ERROR);
                }
            });
        }

    }

    private void settextbankdetails() {
        tv_accountname.setText(st_accont_name);
        tv_accountnumber.setText(st_accountnumber);
        tv_bank.setText(st_bankname);
        tv_branch.setText(st_bankbranch);
        tv_ifsccode.setText(st_ifsc);
    }

@OnClick(R.id.bankdetails_edit)
    public void editaddress(){
    if (!NetworkUtils.isConnected(this)){
        ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        return;
    }else {
        Intent intent = new Intent(this, Edit_CustomerBankdetails.class);
        intent.putExtra("id", StBankId);
        intent.putExtra("from", "profileedit");
        //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
@OnClick(R.id.address_edit)
public void openedit(){
    if (!NetworkUtils.isConnected(this)){
        ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        return;
    }else {
        Intent intent = new Intent(this, CustomerEditAddress.class);
        intent.putExtra("id", Integer.parseInt(StAddressId));
        intent.putExtra("from", "profileedit");
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.iv_profile_image)
    public void chageprofileimg(){
        requestPermissions(
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
    }

    private static final int CAMERA_REQUEST = 1887;
    File profilefile;
    Uri profileuri;
    RequestBody profilerequest;
    List<MultipartBody.Part> profilepart;

    @SuppressLint("LongLogTag")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("TAG", "Permission Granted ");
            switch (requestCode) {
                case 1:

                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, CAMERA_REQUEST);
                    break;
            }
        }
        else {
            Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show(); }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            Log.e("from couple","couple");
            //If the file selection was successful
            if (resultCode == RESULT_OK) {
                if ( data != null && data.getData() != null){

                    try {
                        profileuri = data.getData();
                        assert profileuri != null;
                        // Log.e("Uri",uri.toString());
                        Log.i("TAG", "Uri = " + profileuri.toString());
                       // iv_profile_image.setImageURI(profileuri);

                        profilefile = FileUtiltyforprofile.getFile(this, profileuri);

                        Log.e("file",profilefile.toString());
                        profilepart = new ArrayList<>();
                        profilepart.add(prepareFilePart("avatar", profileuri));


                       sendprofileimage(profilepart);

                    } catch (Exception e) {
                        Log.e("TAG", "File select error", e);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        profilerequest = RequestBody.create(MediaType.parse(Objects.requireNonNull
                (this.getContentResolver().getType(fileUri))), profilefile);

        return MultipartBody.Part.createFormData(partName, profilefile.getName(),profilerequest);
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
                        stprofileimg = listmodel.getAvatar();
                        if (listmodel.getAvatar() != null){
                            Picasso.with(Profile_Details.this).load(stprofileimg).into(iv_profile_image);
                        }else {

                        }

                    }else {
                        dialog.dismiss();
                        try {
                            dialog.dismiss();
                            JSONObject  jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Toast.makeText(Profile_Details.this, message, Toast.LENGTH_SHORT).show();
                            JSONObject Object = jsonObject.getJSONObject("errors");
                            try {
                                JSONArray array_name = Object.getJSONArray("avatar");
                                Log.e("image",array_name.toString());
                                for (int i = 0; i < array_name.length(); i++) {
                                    Toast.makeText(Profile_Details.this, array_name.getString(i), Toast.LENGTH_SHORT).show();
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
                 //   ToastMessage.onToast(Profile_Details.this,"We have  issue try after some time",ToastMessage.ERROR);
                }
            });
        }
    }
}