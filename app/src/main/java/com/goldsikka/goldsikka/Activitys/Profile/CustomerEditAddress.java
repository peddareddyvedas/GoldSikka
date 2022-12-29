package com.goldsikka.goldsikka.Activitys.Profile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.goldsikka.goldsikka.Activitys.Profile_Details;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerEditAddress extends AppCompatActivity implements View.OnClickListener {
    Button bt_Add;
    EditText et_address,et_city,et_pincode;
    //Spinner spin_title,spin_state;
    TextView tv_address_type,tv_address,tv_city,tv_state,tv_pin;
    Bundle bundle;
    int addressid;
    Spinner spinner_address_type,spinner_state;
    ArrayList<String> sublist,statelist;
    String subcategory,statesubcatagory,address_type;
    String[] st_address_type = {"Select Address Type","Home","Office"};
    ApiDao apiDao;
    String stateee;
    int state_id;
    String st_address,st_pincode,st_city;
    String rs_address,rs_pincode,rs_city,rs_state,rs_title;
    ArrayAdapter<String> adapter;
    String idcode,fromto,fromcartlist;
    List<Listmodel> list;

    TextView unameTv, uidTv, titleTv;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_edit_address);

            Bundle bundle = getIntent().getExtras();
        addressid = bundle.getInt("id");
        fromto = bundle.getString("from");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Edit Your Address");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Edit Your Addess");
      //  toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        spinner_stateclick();
        intilizeviews();
        set_previousdetails();
        setHint();
    }

    public void setHint(){
        et_address.setHint(Html.fromHtml(getString(R.string.Address)));
        et_city.setHint(Html.fromHtml(getString(R.string.City)));
        et_pincode.setHint(Html.fromHtml(getString(R.string.Zip_Code)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }


       private void set_previousdetails() {
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
               Call<Listmodel> setaddress_details = apiDao.get_address("Bearer " + AccountUtils.getAccessToken(this), String.valueOf(addressid));
               setaddress_details.enqueue(new Callback<Listmodel>() {
                   @SuppressLint("NewApi")
                   @Override
                   public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                       int statuscode = response.code();
                       List<Listmodel> list = Collections.singletonList(response.body());
                       if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_ACCEPTED || statuscode == HttpsURLConnection.HTTP_OK) {
                           for (Listmodel listmodel : list) {
                               rs_address = listmodel.getAddress();
                               rs_city = listmodel.getCity();
                               rs_pincode = listmodel.getZip();
                               rs_state = listmodel.getState_id();
                               rs_title = listmodel.getTitle();
                               et_address.setText(rs_address);
                               et_city.setText(rs_city);
                               et_pincode.setText(rs_pincode);
                               Log.e("city", rs_city);


//                        String myString = rs_city; //the value you want the position for
//
//                        ArrayAdapter<String> spinnerAdap = (ArrayAdapter<String>) spinner_state.getAdapter();
//
//                        int spinnerPosition = adapter.getPosition(myString);
//
//                        spinner_state.setSelection(spinnerPosition);

                               dialog.dismiss();
                           }
                       } else {
                           dialog.dismiss();
                           ToastMessage.onToast(CustomerEditAddress.this, "Server Error", ToastMessage.ERROR);
                       }
                   }

                   @Override
                   public void onFailure(Call<Listmodel> call, Throwable t) {
                       dialog.dismiss();
                       ToastMessage.onToast(CustomerEditAddress.this, "We have some issues", ToastMessage.ERROR);
                   }
               });
           }
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    public void intilizeviews(){
        et_address = findViewById(R.id.et_customeraddress);
        et_city = findViewById(R.id.et_customercity);
        et_pincode = findViewById(R.id.et_pincode);
        //loading_gif = findViewById(R.id.loading_gif);
        tv_address = findViewById(R.id.tv_erroraddress);
        tv_city = findViewById(R.id.tv_city);
        tv_pin = findViewById(R.id.tv_pin);

        bt_Add = findViewById(R.id.btn_addaddress);
        bt_Add.setOnClickListener(this);

        spinner_address_type = findViewById(R.id.spin_title);
        ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,st_address_type);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_address_type.setAdapter(spinner_adapter);
        spinnerclick();

    }
    public void spinnerclick(){

        sublist = new ArrayList<>();

        spinner_address_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              //  subcategory = spinner_address_type.getItemAtPosition(spinner_address_type.getSelectedItemPosition() - 1).toString();

                subcategory =   String.valueOf(spinner_address_type.getSelectedItem());
                ((TextView) view).setTextColor(ContextCompat.getColor(CustomerEditAddress.this, R.color.DarkBrown));

                if (subcategory.equals("Home")) {
                    address_type = "Home";
                }else if (subcategory.equals("Office")){
                    address_type = "Office";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }
    public void spinner_stateclick(){
        statelist = new ArrayList<>();

        spinner_state = findViewById(R.id.spin_state);
        loaddata();
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(ContextCompat.getColor(CustomerEditAddress.this, R.color.DarkBrown));

                statesubcatagory = spinner_state.getItemAtPosition(spinner_state.getSelectedItemPosition()).toString();

              // String listmodel = statelist.get();
                if (!statesubcatagory.equals("Select State")) {
                    Listmodel listmodel = list.get(i);
                    idcode = listmodel.getId();
//                    for (Listmodel listmodel:list){
                   Log.e("stateid..", idcode);
//                    }

                  //
                    Log.e("state..", statesubcatagory);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }
    public void loaddata(){
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
            Call<List<Listmodel>> get_address = apiDao.get_states("Bearer " + AccountUtils.getAccessToken(this));
            get_address.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    list = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        for (Listmodel listmodel : list) {
                            dialog.dismiss();
                            statelist.addAll(Collections.singleton(listmodel.getName()));


                            // codee = listmodel.getCode();

                            adapter = new ArrayAdapter<String>(CustomerEditAddress.this, android.R.layout.simple_spinner_dropdown_item, statelist);
                            spinner_state.setAdapter(adapter);
                            //Log.e("responce iid", idcode + codee);
                        }


                    }
                }

                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                    Log.e("exception", t.toString());
                  //  ToastMessage.onToast(CustomerEditAddress.this, "Technical Issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_addaddress:
                if (!NetworkUtils.isConnected(this)){
                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                }else {
                    init_validation();
                }
                break;
        }

    }

    public void init_validation(){

        tv_address.setVisibility(View.GONE);
        tv_city.setVisibility(View.GONE);
        tv_pin.setVisibility(View.GONE);

        bt_Add.setVisibility(View.GONE);
      //  loading_gif.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                CustomerEditAddress.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        validations();
                        bt_Add.setVisibility(View.VISIBLE);
                      //  loading_gif.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);
    }

    private void validations() {
        st_address = et_address.getText().toString();
        st_city = et_city.getText().toString();
        st_pincode = et_pincode.getText().toString();

        tv_address.setVisibility(View.GONE);
        tv_city.setVisibility(View.GONE);
        //  tv_state.setVisibility(View.GONE);
        tv_pin.setVisibility(View.GONE);

        if(subcategory.equals("Select Address Type")){
            ToastMessage.onToast(this, "Please Select Address Type", ToastMessage.ERROR);
        }
       else if (st_address.isEmpty()){
            tv_address.setVisibility(View.VISIBLE);
            tv_address.setText("Please enter address");
        }else if (st_city.isEmpty()){
            tv_city.setVisibility(View.VISIBLE);
            tv_city.setText("please enter city");
        }
//        else if (statesubcatagory.equals("Select State")){
//            ToastMessage.onToast(this, "Please Select State", ToastMessage.ERROR);
//        }
        else if (st_pincode.isEmpty()){
            tv_pin.setVisibility(View.VISIBLE);
            tv_pin.setText("Please Enter Zip Code");
        }
        else {
            setaddress();
        }
    }

    public void setaddress(){

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
            Call<Listmodel> edit_address = apiDao.get_address_edit("Bearer " + AccountUtils.getAccessToken(this),
                    String.valueOf(addressid), address_type, st_address, st_city, idcode, st_pincode);
            edit_address.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    List<Listmodel> list = Collections.singletonList(response.body());
                    if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK ||
                            statuscode == HttpsURLConnection.HTTP_ACCEPTED) {
                        for (Listmodel listmodel : list) {
                            dialog.dismiss();
                            ToastMessage.onToast(CustomerEditAddress.this, "Successfully Updated..", ToastMessage.SUCCESS);
                            onsuccess();

                        }
                    } else {
                        dialog.dismiss();
                        try {
                            //  tv_address_type.setVisibility(View.GONE);
                            tv_address.setVisibility(View.GONE);
                            tv_city.setVisibility(View.GONE);
                            //  tv_state.setVisibility(View.GONE);
                            tv_pin.setVisibility(View.GONE);

                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            ToastMessage.onToast(CustomerEditAddress.this, st, ToastMessage.ERROR);
                            JSONObject er = jObjError.getJSONObject("errors");

                            try {
                                JSONArray array_title = er.getJSONArray("title");
                                for (int i = 0; i < array_title.length(); i++) {
                                    tv_address_type.setVisibility(View.VISIBLE);
                                    tv_address_type.setText(array_title.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray array_address = er.getJSONArray("address");
                                for (int i = 0; i < array_address.length(); i++) {
                                    tv_address.setVisibility(View.VISIBLE);
                                    tv_address.setText(array_address.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray arry_city = er.getJSONArray("city");
                                for (int i = 0; i < arry_city.length(); i++) {
                                    tv_city.setVisibility(View.VISIBLE);
                                    tv_city.setText(arry_city.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_state = er.getJSONArray("state_id");
                                for (int i = 0; i < array_state.length(); i++) {
                                    tv_state.setVisibility(View.VISIBLE);
                                    tv_state.setText(array_state.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_pin = er.getJSONArray("zip");
                                for (int i = 0; i < array_pin.length(); i++) {
                                    tv_pin.setVisibility(View.VISIBLE);
                                    tv_pin.setText(array_pin.getString(i));
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
                    ToastMessage.onToast(CustomerEditAddress.this, "We Have Some Issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }

    }

    private void onsuccess() {


        if (fromto.equals("profileedit")){
            Intent intent = new Intent(CustomerEditAddress.this, Profile_Details.class);
            startActivity(intent);
        }else if (fromto.equals("addresslist")){

            Intent intent = new Intent(CustomerEditAddress.this, CustomerAddressList.class);
            startActivity(intent);
        }
    }

}