package com.goldsikka.goldsikka.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Mygold_personalDetails_Fragment extends Fragment implements View.OnClickListener{

    private Activity activity;
    EditText p_name,p_fathername,p_email,p_phno,p_altno,p_address,p_city,p_country,p_state,p_pincode;
    Button btn_p;
    String stp_name,stp_fathername,stp_gender,stp_email,stp_phno,stp_altno,stp_address,stp_city,stp_country,stp_state,stp_pincode;
    String MobilePattern = "[0-9]{10}";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    RadioGroup radioGroup;
    RadioButton p_gender;
TextView unameTv, uidTv, titleTv;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mygold_personaldetails, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Gold 2020");
        unameTv = view.findViewById(R.id.uname);
        uidTv = view.findViewById(R.id.uid);
        titleTv = view.findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(getContext()));
        uidTv.setText(AccountUtils.getCustomerID(getContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("My Gold 2020");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //getActivity().onBackPressed();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new MyGold2020_ContentFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        initlizeviews(view);
        return view;
    }

    private void initlizeviews(View view) {
        p_name = view.findViewById(R.id.pd_fullname);
        p_fathername = view.findViewById(R.id.pd_fhname);
        p_email = view.findViewById(R.id.pd_Email);
        p_phno = view.findViewById(R.id.pd_phnumber);
        p_altno = view.findViewById(R.id.pd_alternatenum);
        p_address = view.findViewById(R.id.pd_address);
        p_city = view.findViewById(R.id.pd_city);
        p_country = view.findViewById(R.id.pd_country);
        p_state = view.findViewById(R.id.pd_state);
        p_pincode = view.findViewById(R.id.pd_pincode);

        btn_p = view.findViewById(R.id.btn_pd);
        btn_p.setOnClickListener(this);

        radioGroup = view.findViewById(R.id.pd_gender);
        int selectedID = radioGroup.getCheckedRadioButtonId();
        p_gender = view.findViewById(selectedID);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_pd:
                validations();
                return;
        }

        }

    private void validations() {
        stp_name = p_name.getText().toString();
        stp_fathername = p_fathername.getText().toString();
        stp_gender = String.valueOf(p_gender.getText());
        stp_email = p_email.getText().toString();
        stp_phno = p_phno.getText().toString();
        stp_altno = p_altno.getText().toString();
        stp_address = p_address.getText().toString();
        stp_city = p_city.getText().toString();
        stp_country = p_country.getText().toString();
        stp_state = p_state.getText().toString();
        stp_pincode = p_pincode.getText().toString();

        if (stp_name.isEmpty()){
            Toast.makeText(activity, "Please enter name", Toast.LENGTH_SHORT).show();
        }else if(stp_fathername.isEmpty()) {
            Toast.makeText(activity, "Please enter father name", Toast.LENGTH_SHORT).show();
        }else if (stp_gender.isEmpty()){
            Toast.makeText(activity, "Please select gender", Toast.LENGTH_SHORT).show();
        }else if(p_email.getText().toString().matches(emailPattern)){
            Toast.makeText(activity, "Please enter email", Toast.LENGTH_SHORT).show();
        }else if(!p_phno.getText().toString().matches(MobilePattern)){
            Toast.makeText(activity, "Please enter phnone number", Toast.LENGTH_SHORT).show();
        }else if(stp_address.isEmpty()){
            Toast.makeText(activity, "Please enter address", Toast.LENGTH_SHORT).show();
        }else if(stp_city.isEmpty()){
            Toast.makeText(activity, "Please enter city", Toast.LENGTH_SHORT).show();
        }else if(stp_state.isEmpty()){
            Toast.makeText(activity, "Please enter state", Toast.LENGTH_SHORT).show();
        }else if(stp_country.isEmpty()){
            Toast.makeText(activity, "Please enter country", Toast.LENGTH_SHORT).show();
        }else if(stp_pincode.isEmpty()){
            Toast.makeText(activity, "Please Enter Zip Code", Toast.LENGTH_SHORT).show();
        }else {

            getDetails();
        }

    }

    private void getDetails() {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait.....");
        dialog.setCancelable(false);
        dialog.show();

        String url = "";
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean result = jsonObject.getBoolean("");

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(activity, "Please try again", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
               // Toast.makeText(activity, "We have some issues", Toast.LENGTH_SHORT).show();

            }

        }){

        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(jsonRequest);
    }
}

