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
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Mygold_NomineeDetails_fragment extends Fragment implements View.OnClickListener {

    EditText n_name,n_relation,n_phno,n_address,n_city,n_state,n_country,n_pincode;
    String stn_name,stn_relation,stn_phno,stn_address,stn_city,stn_state,stn_country,stn_pincode;
    Button btn_n;
    String MobilePattern = "[0-9]{10}";
    private Activity activity;
TextView unameTv, uidTv, titleTv;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mygold_nomineedetails, container, false);

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
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new Mygold_personalDetails_Fragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        initlizeviews(view);
        return view;
    }

    private void initlizeviews(View view) {
        n_name = view.findViewById(R.id.nm_fullname);
        n_relation = view.findViewById(R.id.nm_relation);
        n_phno = view.findViewById(R.id.nm_phno);
        n_address = view.findViewById(R.id.nm_adress);
        n_city = view.findViewById(R.id.nm_city);
        n_state = view.findViewById(R.id.nm_state);
        n_country = view.findViewById(R.id.nm_country);
        n_pincode = view.findViewById(R.id.nm_pincode);

        btn_n = view.findViewById(R.id.btn_nm);
        btn_n.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_nm:
                validations();
                return;
        }

    }

    private void validations() {

        stn_name = n_name.getText().toString();
        stn_relation = n_relation.getText().toString();
        stn_phno = n_phno.getText().toString();
        stn_address = n_address.getText().toString();
        stn_city = n_city.getText().toString();
        stn_country = n_country.getText().toString();
        stn_state = n_state.getText().toString();
        stn_pincode = n_pincode.getText().toString();

        if(stn_name.isEmpty()){
            Toast.makeText(activity, "Please enter nominee name", Toast.LENGTH_SHORT).show();
        }else if (stn_relation.isEmpty()){
            Toast.makeText(activity, "Please enter your relation", Toast.LENGTH_SHORT).show();
        }else if(!n_phno.getText().toString().matches(MobilePattern)){
            Toast.makeText(activity, "Please enter ph.no", Toast.LENGTH_SHORT).show();
        }else if (stn_address.isEmpty()){
            Toast.makeText(activity, "Please enter address", Toast.LENGTH_SHORT).show();
        }else if(stn_city.isEmpty()){
            Toast.makeText(activity, "Please enter city", Toast.LENGTH_SHORT).show();
        }else if (stn_state.isEmpty()) {
            Toast.makeText(activity, "Please enter state", Toast.LENGTH_SHORT).show();
        }else if (stn_country.isEmpty()){
            Toast.makeText(activity, "Please enter country", Toast.LENGTH_SHORT).show();
        }else if(stn_pincode.isEmpty()){
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
             //   Toast.makeText(activity, "We have some issues", Toast.LENGTH_SHORT).show();

            }
        }){

        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(jsonRequest);
    }
}
