package com.goldsikka.goldsikka.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.goldsikka.goldsikka.Fragments.Nominee_details_fragment;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.Models.Enquiryformmodel;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  PurchaseEnquiryForm extends Fragment implements View.OnClickListener {


    EditText etname,etemail,etmobile,etaddress,etmessage;

    Spinner spinplan,spinquantity;

    TextView tvname,tvemail,tvmobile,tvplan,tvquantity,tvmessage,tvaddress;

    Button btsubmit;


    String stname,stemail,stmobile,staddress,stmessage,stquantity,resquantity,stplan,resplan;
    String [] stquantityarray = {"Select Grams","5","10","20","30"};
    String [] stplanyarray = {"Select Plan"};

    ArrayList<String> quanityarraylist,planarraylist;

    ApiDao apiDao;
    List<Enquiryformmodel> list;

    private Activity activity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_purchase_enquiry_form,container,false);


        intilizeviews(view);


        return view;
    }
    

    public void intilizeviews(View view){


        etname = view.findViewById(R.id.etname);
        etemail = view.findViewById(R.id.etemail);
        etmobile = view.findViewById(R.id.etmobile);
        etaddress = view.findViewById(R.id.etaddress);
        etmessage = view.findViewById(R.id.etmessage);

        etname.setHint(Html.fromHtml(getString(R.string.full_name)));
        etemail.setHint(Html.fromHtml(getString(R.string.user_email)));
        etmobile.setHint(Html.fromHtml(getString(R.string.phone_number)));
        etaddress.setHint(Html.fromHtml(getString(R.string.Address)));
        etmessage.setHint(Html.fromHtml(getString(R.string.message)));

        tvname = view.findViewById(R.id.tvname);
        tvemail = view.findViewById(R.id.tvemail);
        tvmobile = view.findViewById(R.id.tvmobile);
        tvplan = view.findViewById(R.id.tvplan);
        tvquantity = view.findViewById(R.id.tvquantity);
        tvmessage = view.findViewById(R.id.tvmessage);
        tvaddress = view.findViewById(R.id.tvaddress);


        spinplan = view.findViewById(R.id.spinplan);
        spinquantity = view.findViewById(R.id.spinquantity);

        ArrayAdapter spinner_adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item,stquantityarray);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinquantity.setAdapter(spinner_adapter);
        spinnerclick();
        ArrayAdapter planspinner_adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item,stplanyarray);
        planspinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinplan.setAdapter(planspinner_adapter);
        getplan();

        btsubmit = view.findViewById(R.id.btsubmit);
        btsubmit.setOnClickListener(this);

    }
    public void spinnerclick(){
        quanityarraylist = new ArrayList<>();

        spinquantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stquantity = spinquantity.getItemAtPosition(spinquantity.getSelectedItemPosition() ).toString();
                ((TextView) view).setTextColor(ContextCompat.getColor(activity, R.color.DarkBrown));
                if (stquantity.equals("5")){
                    resquantity = "5";
                }else if (stquantity.equals("10")){
                    resquantity = "10";
                }else if (stquantity.equals("15")){
                    resquantity = "15";
                }else if (stquantity.equals("20")){
                    resquantity = "20";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void getplan(){
        planarraylist = new ArrayList<>();
      loadplans();
        spinplan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stplan = spinplan.getItemAtPosition(spinplan.getSelectedItemPosition()).toString();
               // ((TextView) view).setTextColor(ContextCompat.getColor(activity, R.color.DarkBrown));
                if (!stplan.equals("Select Plan")){
                    Enquiryformmodel enquiryformmodel = list.get(position);
                    resplan = enquiryformmodel.getId();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void loadplans(){
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(activity)){
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
           Call<List<Enquiryformmodel>> getplans = apiDao.loadplansforenquiry("PU");

           getplans.enqueue(new Callback<List<Enquiryformmodel>>() {
               @Override
               public void onResponse(Call<List<Enquiryformmodel>> call, Response<List<Enquiryformmodel>> response) {
                   int statuscode = response.code();
                   if (statuscode == HttpsURLConnection.HTTP_OK){
                       dialog.dismiss();
                      list = response.body();
                      for (Enquiryformmodel enquiryformmodel:list){
                        planarraylist.add(enquiryformmodel.getName());
                      }
                       spinplan.setAdapter(new ArrayAdapter<String>(activity,
                               android.R.layout.simple_spinner_dropdown_item, planarraylist));
                   }else {
                       dialog.dismiss();
                     //  ToastMessage.onToast(activity,"Technical Error",ToastMessage.ERROR);
                   }
               }

               @Override
               public void onFailure(Call<List<Enquiryformmodel>> call, Throwable t) {
                        dialog.dismiss();
                      //  ToastMessage.onToast(activity,"We ahve some issues ",ToastMessage.ERROR);
               }
           });

        }
    }

    public void initvalidation(){
        btsubmit .setVisibility(View.GONE);
//        loading_gif.setVisibility(View.VISIBLE);
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
                        if (!NetworkUtils.isConnected(activity)){
                            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            return;
                        }else {
                            validation();
                      //      loading_gif.setVisibility(View.GONE);
                            btsubmit.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }
        }, 500);

    }
    @SuppressLint("SetTextI18n")
    public void validation(){

        tvaddress.setVisibility(View.GONE);
        tvemail.setVisibility(View.GONE);
        tvname.setVisibility(View.GONE);
        tvmessage.setVisibility(View.GONE);
        tvmobile.setVisibility(View.GONE);
        tvplan.setVisibility(View.GONE);
        tvquantity.setVisibility(View.GONE);

        stname = etname.getText().toString().trim();
        stemail = etemail.getText().toString().trim();
        stmobile = etmobile.getText().toString().trim();
        staddress = etaddress.getText().toString();
        stmessage = etmessage.getText().toString();

        if (stname.isEmpty()){
            tvname.setVisibility(View.VISIBLE);
            tvname.setText("Please enter the name");
        }else if (stemail.isEmpty()){
            tvemail.setVisibility(View.VISIBLE);
            tvemail.setText("Please enter the Email Id");
        }else if (stmobile.isEmpty()){
            tvmobile.setVisibility(View.VISIBLE);
            tvmobile.setText("Please enter the phone number");
        }
        else if (stplan.equals("Select Plan")) {
            tvplan.setText("Please select the plan");
            tvplan.setVisibility(View.VISIBLE);
        }
       else if (stquantity.equals("Select Grams")){
            tvquantity.setVisibility(View.VISIBLE);
            tvquantity.setText("Please Select the Grams");
        }
       else if (staddress.isEmpty()){
            tvaddress.setVisibility(View.VISIBLE);
            tvaddress.setText("Please Enter the Address");
        }else if (stmessage.isEmpty()){
            tvmessage.setVisibility(View.VISIBLE);
            tvmessage.setText("Message Field is Required");
        }else {
            if (!NetworkUtils.isConnected(activity)){
                btsubmit.setVisibility(View.VISIBLE);
                ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

            }else {
                btsubmit.setVisibility(View.GONE);
                initEnquiryForm();
                Log.e("purchas","purchas");
            }
        }


    }
    public  void initEnquiryForm(){

        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
        Call<Enquiryformmodel> initenquiryform = apiDao.purchaseenquiryform(stname,stemail,stmobile,resplan,resquantity,staddress,stmessage);
        initenquiryform.enqueue(new Callback<Enquiryformmodel>() {
            @Override
            public void onResponse(Call<Enquiryformmodel> call, Response<Enquiryformmodel> response) {
                int statuscode = response.code();
                if (statuscode == HttpsURLConnection.HTTP_ACCEPTED||statuscode == HttpsURLConnection.HTTP_OK){
                        dialog.dismiss();
                        Enquiryformmodel enquiryformmodel = response.body();
                        String msg = enquiryformmodel.getMessage();
                        popup(msg);
                    btsubmit.setVisibility(View.VISIBLE);
                }else {
                            dialog.dismiss();
                    tvaddress.setVisibility(View.GONE);
                    tvemail.setVisibility(View.GONE);
                    tvname.setVisibility(View.GONE);
                    tvmessage.setVisibility(View.GONE);
                    tvmobile.setVisibility(View.GONE);
                    tvplan.setVisibility(View.GONE);
                    tvquantity.setVisibility(View.GONE);
                    try {
                        assert response.errorBody() != null;
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());

                        String msg = jsonObject.getString("message");
                        ToastMessage.onToast(activity,msg,ToastMessage.ERROR);
                        JSONObject er = jsonObject.getJSONObject("errors");
                        try {
                            JSONArray array_mobile = er.getJSONArray("name");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(activity, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvname.setVisibility(View.VISIBLE);
                                tvname.setText(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray array_mobile = er.getJSONArray("email");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(activity, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvemail.setVisibility(View.VISIBLE);
                                tvemail.setText(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray array_mobile = er.getJSONArray("mobile");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(activity, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvmobile.setVisibility(View.VISIBLE);
                                tvmobile.setText(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray array_mobile = er.getJSONArray("purchase_type");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(activity, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvplan.setVisibility(View.VISIBLE);
                                tvplan.setText(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray array_mobile = er.getJSONArray("address");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(activity, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvaddress.setVisibility(View.VISIBLE);
                                tvaddress.setText(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray array_mobile = er.getJSONArray("description");
                            for (int i = 0; i < array_mobile.length(); i++) {
                                //Toast.makeText(activity, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                tvmessage.setVisibility(View.VISIBLE);
                                tvmessage.setText(array_mobile.getString(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Enquiryformmodel> call, Throwable t) {
                        dialog.dismiss();
                btsubmit.setVisibility(View.VISIBLE);
                        ToastMessage.onToast(activity,"We have some issurs",ToastMessage.ERROR);
            }
        });

    }
    AlertDialog enquiryalertdialog;
    Button btcountinue;
    TextView tvinfo;
    public void popup(String msg){
        AlertDialog.Builder alertdilog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.purchasenquerypopup,null);
        alertdilog.setCancelable(false);
        alertdilog.setView(dialogview);
        enquiryalertdialog = alertdilog.create();

        tvinfo = dialogview.findViewById(R.id.tvinfo);
        tvinfo.setText(msg);

        btcountinue = dialogview.findViewById(R.id.btcountinue);
        btcountinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enquiryalertdialog.dismiss();
                Intent intent = new Intent(activity, MainFragmentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        enquiryalertdialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btsubmit:
                initvalidation();
                break;
        }
    }
}