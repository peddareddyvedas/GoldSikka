package com.goldsikka.goldsikka.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class loanenquire_fragment extends Fragment implements View.OnClickListener {

    private Activity activity;
    EditText etname,etemail,etmobile,etaddress,etmessage;
    String stname,stemail,stmobile,staddress,stmessage,stplan,stloanamount,stjewellery,stpurity;
    Button btnsubmit;

    Spinner spin_plan,spin_loanamount,spin_jewellery,spin_purity;
    ArrayList<String> plan_list,loanamount_list,jewellery_list,purity_list;

    String plan_url = "";
    String loanamount_url = "";
    String purity_url = "";
    String jewellery_url = "";

TextView unameTv, uidTv;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.loanenquire_design,container,false);


        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Goldsikka");
        unameTv = view.findViewById(R.id.uname);
        uidTv = view.findViewById(R.id.uid);

        unameTv.setText(AccountUtils.getName(getContext()));
        uidTv.setText(AccountUtils.getCustomerID(getContext()));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //getActivity().onBackPressed();
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });

        initlizeviews(view);
        initlizespin_plan(view);
        initlizespin_loanamount(view);
        initlizespin_jewellery(view);
        initlizespin_purity(view);
        return view;
    }

    public void initlizespin_loanamount(View view){

        loanamount_list = new ArrayList<>();
        spin_loanamount = view.findViewById(R.id.spin_selectloan_amount);

        load_loanamount(loanamount_url);

        spin_loanamount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stloanamount = spin_loanamount.getItemAtPosition(spin_loanamount.getSelectedItemPosition() - 1).toString();

                if (!stloanamount.equals("Please Select Loan Amount")) {
//                    sendtprequest(shopname);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

    }
    public void initlizespin_jewellery(View view){

        jewellery_list = new ArrayList<>();
        spin_jewellery = view.findViewById(R.id.spin_selectjewellery_type);

        load_loanamount(jewellery_url);

        spin_jewellery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stjewellery = spin_jewellery.getItemAtPosition(spin_jewellery.getSelectedItemPosition() - 1).toString();

                if (!stjewellery.equals("Please Select Jewellery Type")) {
//                    sendtprequest(shopname);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

    }
    public void initlizespin_purity(View view){

         purity_list = new ArrayList<>();
        spin_purity = view.findViewById(R.id.spin_selectgold_purity);

        load_loanamount(purity_url);

        spin_purity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stpurity = spin_purity.getItemAtPosition(spin_purity.getSelectedItemPosition() - 1).toString();

                if (!stpurity.equals("Please Select Gold Purity")) {
//                    sendtprequest(shopname);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

    }

    public void initlizespin_plan(View view){

        plan_list = new ArrayList<>();
        spin_plan = view.findViewById(R.id.spin_selectplan);

        load_loanamount(plan_url);

        spin_purity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stpurity = spin_purity.getItemAtPosition(spin_purity.getSelectedItemPosition() - 1).toString();

                if (!stpurity.equals("Please Select plan")) {
//                    sendtprequest(shopname);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


    }

    public void initlizeviews(View view){
        etname = view.findViewById(R.id.et_name);
        etemail = view.findViewById(R.id.et_email);
        etmessage = view.findViewById(R.id.et_message);
        etmobile = view.findViewById(R.id.et_mobile);
        etaddress = view.findViewById(R.id.et_address);
        btnsubmit = view.findViewById(R.id.btn_submit);
        btnsubmit.setOnClickListener(this);
    }

    public void load_loanamount(String url){

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_submit:
                validation();
                return;
        }

    }

    public void validation(){
        stname = etname.getText().toString().trim();
        stemail = etemail.getText().toString().trim();
        stmobile = etmobile.getText().toString().trim();
        staddress = etaddress.getText().toString().trim();
        stmessage = etmessage.getText().toString().trim();


        if (stname.isEmpty()){

            Toast.makeText(activity, "Please Enter Name", Toast.LENGTH_LONG).show();

        }else if (stemail.isEmpty()){

            Toast.makeText(activity, "Please Enter Email", Toast.LENGTH_LONG).show();

        }else if (stmobile.isEmpty()){

            Toast.makeText(activity, "Please Enter Mobile", Toast.LENGTH_LONG).show();

        }else if (staddress.isEmpty()){

            Toast.makeText(activity, "Please Enter Address", Toast.LENGTH_LONG).show();

        }else if (stmessage.isEmpty()){

            Toast.makeText(activity, "Please Enter Message", Toast.LENGTH_LONG).show();

        }else {
            submitclick();
        }
    }

    public void submitclick(){

        String url = "" ;

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            boolean result = jsonObject.getBoolean("error");

                            if (!result) {

                                onsucess();

                            } else {
                                Toast.makeText(activity, "PLease check your fields", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     //   Toast.makeText(activity, "We have some techincal issue", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the reques
                params.put("request" ,"login");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        //adding the string request to request queue
        requestQueue.add(jsonRequest);

    }

    public void onsucess(){

    }
}
