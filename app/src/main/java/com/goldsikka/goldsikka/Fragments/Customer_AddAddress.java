package com.goldsikka.goldsikka.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.goldsikka.goldsikka.Activitys.Profile_Details;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
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
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Customer_AddAddress extends Fragment implements View.OnClickListener {
    TextView tv_address_type,tv_address,tv_city,tv_state,tv_pin;
    EditText et_address,et_city,et_pincode;
    Button bt_add_adderss;
    String st_address,st_city,st_pincode,st_spinstate,st_spinaddresstype;
    String rs_address,rs_city,rs_pincode,rs_state,rs_spinaddresstype;
    private Activity activity;
    String[] st_address_type = {"HOME","Office"};
    Spinner spinner_address_type,spinner_state;
    ArrayList<String> sublist,statelist;
    String subcategory,statesubcatagory,address_type;
    ApiDao apiDao;
    String addressId;
    String state_id;
    List<Listmodel> list;
    GifImageView loading_gif;
    String from,fromtolist,fromtoo;

    TextView unameTv, uidTv, titleTv;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }
    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_addaddress,container,false);
            Bundle bundle = getArguments();
            if (bundle!= null){
                from = bundle.getString("from");

                fromtolist = bundle.getString("fromlist");
                fromtoo = bundle.getString("frommtoo");
            }
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
//        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle("Address");
        unameTv = view.findViewById(R.id.uname);
        uidTv = view.findViewById(R.id.uid);
        titleTv = view.findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(getContext()));
        uidTv.setText(AccountUtils.getCustomerID(getContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Address");
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
      //  toolbar.setTitleTextColor(activity.getColor(R.color.colorWhite));
        toolbar.setNavigationOnClickListener(v -> {
            if (from.equals("profiles")){
                Intent intent = new Intent(activity, Profile_Details.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else if (from.equals("list")){
                    Bundle bundle1  = new Bundle();
                    bundle1.putString("fromcartlist",fromtolist);
                    bundle1.putString("fromto",fromtoo);
                customer_addresslist frh = new customer_addresslist();
               frh.setArguments(bundle1);
                 getFragmentManager().beginTransaction().replace(R.id.frame_layout,frh).commit();
            }


        });
        intilizeviews(view);
        return view;
    }
    public void intilizeviews(View view){
        loading_gif = view.findViewById(R.id.loading_gif);

        et_address = view.findViewById(R.id.et_customeraddress);
        et_city = view.findViewById(R.id.et_customercity);
        et_pincode = view.findViewById(R.id.et_pincode);
//        et_pincode.setFocusable(true);
//        et_pincode.setFocusableInTouchMode(true);

        tv_address = view.findViewById(R.id.tv_erroraddress);
        tv_address_type = view.findViewById(R.id.address_type);
        tv_city = view.findViewById(R.id.tv_city);
        tv_state = view.findViewById(R.id.tv_state);
        tv_pin = view.findViewById(R.id.tv_pin);
        spinner_state = view.findViewById(R.id.spin_state);
//        spinner_state.setFocusable(true);
//        spinner_state.setFocusableInTouchMode(true);
//        spinner_state.setVisibility(View.VISIBLE);

        bt_add_adderss = view.findViewById(R.id.btn_addaddress);
        bt_add_adderss.setOnClickListener(this);

        spinner_address_type = view.findViewById(R.id.spin_title);
        ArrayAdapter spinner_adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item,st_address_type);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_address_type.setAdapter(spinner_adapter);
        spinnerclick();


        spinner_stateclick();
    }
    public void spinner_stateclick(){
        statelist = new ArrayList<>();
        loaddata();
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statesubcatagory = spinner_state.getItemAtPosition(spinner_state.getSelectedItemPosition() -1).toString();
            //    spinner_state.getSelectedView().setTextColor(getResources().getColor(R.color.Blue));
                ((TextView) view).setTextColor(ContextCompat.getColor(activity, R.color.textcolorprimary));
               // state_id     = spinner_state.getSelectedItemPosition();
                if (!statesubcatagory.equals("Select State")) {
                    Listmodel listmodel = list.get(i);
                    state_id = listmodel.getId();
                    rs_state = listmodel.getName();
                    Log.e("stateid",state_id);
//                    tv_pin.setVisibility(View.VISIBLE);
//                    tv_pin.requestFocus();




                    //Toast.makeText(activity,String.valueOf(id), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }

    public void loaddata(){
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
        Call<List<Listmodel>> get_address = apiDao.get_states("Bearer "+AccountUtils.getAccessToken(activity));
        get_address.enqueue(new Callback<List<Listmodel>>() {
            @Override
            public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                int statuscode = response.code();
               list = response.body();
                if (statuscode == HttpsURLConnection.HTTP_OK) {
                    for (Listmodel listmodel : list) {
                        dialog.dismiss();
                        statelist.add(listmodel.getName());
                    }
                    spinner_state.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item,
                            statelist));

                }
            }


            @Override
            public void onFailure(Call<List<Listmodel>> call, Throwable t) {
               // ToastMessage.onToast(activity,"We Have Some Issues",ToastMessage.ERROR);
                dialog.dismiss();
            }
        });

    }
    public void spinnerclick(){

        sublist = new ArrayList<>();
        // spinner_signuptype = findViewById(R.id.sub_category);

        spinner_address_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subcategory = spinner_address_type.getItemAtPosition(spinner_address_type.getSelectedItemPosition() - 1).toString();
               // ((TextView) view).setTextColor(ContextCompat.getColor(RegistationActivity.this, R.color.colorWhite));
                ((TextView) view).setTextColor(ContextCompat.getColor(activity, R.color.textcolorprimary));

                if (subcategory.equals("HOME")) {
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_addaddress:
                initvalidation();
                break;
        }
    }
    public void initvalidation(){
        tv_address_type.setVisibility(View.GONE);
        tv_address.setVisibility(View.GONE);
        tv_city.setVisibility(View.GONE);
        tv_state.setVisibility(View.GONE);
        tv_pin.setVisibility(View.GONE);

        bt_add_adderss.setVisibility(View.GONE);
        loading_gif.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

               activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        validation();
                        bt_add_adderss.setVisibility(View.VISIBLE);
                        loading_gif.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);
    }
    public void validation(){

        st_address = et_address.getText().toString().trim();
        st_city = et_city.getText().toString().trim();
        st_pincode = et_pincode.getText().toString().trim();


        tv_address_type.setVisibility(View.GONE);
        tv_address.setVisibility(View.GONE);
        tv_city.setVisibility(View.GONE);
        tv_state.setVisibility(View.GONE);
        tv_pin.setVisibility(View.GONE);

        if(subcategory.equals("Select Address Type")){
            ToastMessage.onToast(activity, "Please Select Address Type", ToastMessage.ERROR);
        }

       else   if (st_address.isEmpty()){
            tv_address.setVisibility(View.VISIBLE);
            tv_address.setText("Please Enter Address");
        }
        else if (st_city.isEmpty()){
            tv_city.setVisibility(View.VISIBLE);
            tv_city.setText("Please Enter City");
        }
        else if (statesubcatagory.equals("Select State")){
            ToastMessage.onToast(activity, "Please Select State", ToastMessage.ERROR);
        }
        else if (st_pincode.isEmpty()){
            tv_pin.setVisibility(View.VISIBLE);
            tv_pin.setText("Please Enter Zip Code");
        }
        else {
            openAdd_Address();
        }

    }
    public void openAdd_Address(){
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
        Call<Listmodel> get_address = apiDao.getcustomeraddress("Bearer "+AccountUtils.getAccessToken(activity),
                address_type,st_address,st_city, state_id,st_pincode);
        get_address.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                List<Listmodel> list = Collections.singletonList(response.body());
                if (statuscode == HttpsURLConnection.HTTP_CREATED||statuscode == HttpsURLConnection.HTTP_OK){
                    for (Listmodel listmodel : list){
                        addressId = listmodel.getId();
//                        et_city.setCursorVisible(true);
                      //  AccountUtils.setAddressid(activity,addressId);
                        dialog.dismiss();
                        ToastMessage.onToast(activity,"Successfully added",ToastMessage.SUCCESS);
                    onsuccess();
                    }
                }
                else {

                    try {
                        tv_address_type.setVisibility(View.GONE);
                        tv_address.setVisibility(View.GONE);
                        tv_city.setVisibility(View.GONE);
                        tv_state.setVisibility(View.GONE);
                        tv_pin.setVisibility(View.GONE);

                    assert response.errorBody() != null;
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    String st = jObjError.getString("message");
                    ToastMessage.onToast(activity, st, ToastMessage.ERROR);
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
                        dialog.dismiss();
                } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
            //    ToastMessage.onToast(activity,"We Have Some Issues",ToastMessage.ERROR);
               //     dialog.dismiss();
            }
        });
    }
    public void onsuccess(){
        if (from.equals("profiles")) {
            Intent intent = new Intent(activity, Profile_Details.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("id", addressId);
            startActivity(intent);
        }else if (from.equals("list")) {
            Bundle bundle1  = new Bundle();
            bundle1.putString("fromcartlist",fromtolist);
            bundle1.putString("fromto",fromtoo);
            customer_addresslist frh = new customer_addresslist();
            frh.setArguments(bundle1);
            getFragmentManager().beginTransaction().replace(R.id.frame_layout,frh).commit();
        }
//        Bundle bundle = new Bundle();
//        bundle.putString("id",addressId);
//        Profile_Fragment fragment = new Profile_Fragment();
//        fragment.setArguments(bundle);
//        getFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commit();

    }
}
