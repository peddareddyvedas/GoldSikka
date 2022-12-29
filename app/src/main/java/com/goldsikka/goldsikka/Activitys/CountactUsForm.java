package com.goldsikka.goldsikka.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.Models.Enquiryformmodel;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class CountactUsForm extends Fragment  implements View.OnClickListener {


    EditText etname,etemail,etmobile,etcity,etmessage;
    TextView tvname,tvemail,tvmobile,tvmessage;

    Button btsubmit;

    String stname,stemail,stmobile,stcity,stmessage;
    ApiDao apiDao;
    private Activity activity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_countact_us_form,container,false);
        initlizeviwes(view);

        return view;
    }
    public void initlizeviwes(View view){

        etname = view.findViewById(R.id.etname);
        etemail = view.findViewById(R.id.etemail);
        etmobile = view.findViewById(R.id.etmobile);
        etmessage = view.findViewById(R.id.etmessage);
        etcity = view.findViewById(R.id.etcity);

        etname.setHint(Html.fromHtml(getString(R.string.user_name)));
        etemail.setHint(Html.fromHtml(getString(R.string.user_email)));
        etmobile.setHint(Html.fromHtml(getString(R.string.phone_number)));
        etmessage.setHint(Html.fromHtml(getString(R.string.message)));
        etcity.setHint(Html.fromHtml(getString(R.string.City)));


        tvname = view.findViewById(R.id.tvname);
        tvemail = view.findViewById(R.id.tvemail);
        tvmobile = view.findViewById(R.id.tvmobile);
        tvmessage = view.findViewById(R.id.tvmessage);

        btsubmit = view.findViewById(R.id.btsubmit);
        btsubmit.setOnClickListener(this);

    }


    public void initvalidation(){
        btsubmit .setVisibility(View.GONE);
        //loading_gif.setVisibility(View.VISIBLE);
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
                        }else {
                            validation();
                         //   loading_gif.setVisibility(View.GONE);
                            btsubmit.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }
        }, 500);

    }

    @SuppressLint("SetTextI18n")
    public void validation(){
        tvemail.setVisibility(View.GONE);
        tvname.setVisibility(View.GONE);
        tvmessage.setVisibility(View.GONE);
        tvmobile.setVisibility(View.GONE);

        stname = etname.getText().toString().trim();
        stemail = etemail.getText().toString().trim();
        stmobile = etmobile.getText().toString().trim();
        stcity = etcity.getText().toString();
        stmessage = etmessage.getText().toString();

        if (stname.isEmpty()){
            tvname.setVisibility(View.VISIBLE);
            tvname.setText("Please Enter The Name");
        }else if (stemail.isEmpty()){
            tvemail.setVisibility(View.VISIBLE);
            tvemail.setText("Please Enter The Email Id");
        }else if (stmobile.isEmpty()){
            tvmobile.setVisibility(View.VISIBLE);
            tvmobile.setText("Please Enter The Phone Number");
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
                Log.e("cotactus","contactus");
            }
        }

    }

    public void initEnquiryForm(){


        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

        Call<Enquiryformmodel> contactus = apiDao.contactusenquiryform(stname,stemail,stmobile,stcity,stmessage,"1");
        contactus.enqueue(new Callback<Enquiryformmodel>() {
            @Override
            public void onResponse(Call<Enquiryformmodel> call, Response<Enquiryformmodel> response) {
                int statuscode  = response.code();
                if (statuscode == HttpsURLConnection.HTTP_OK||statuscode == HttpsURLConnection.HTTP_ACCEPTED
                ||statuscode == HttpsURLConnection.HTTP_CREATED){
                    Enquiryformmodel enquiryformmodel = response.body();
                        String msg = enquiryformmodel.getMessage();
                    dialog.dismiss();
                    popup(msg);
                    btsubmit.setVisibility(View.VISIBLE);


                }
                else {
                    dialog.dismiss();
                    tvemail.setVisibility(View.GONE);
                    tvname.setVisibility(View.GONE);
                    tvmessage.setVisibility(View.GONE);
                    tvmobile.setVisibility(View.GONE);
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
                        JSONArray array_mobile = er.getJSONArray("description");
                        for (int i = 0; i < array_mobile.length(); i++) {
                            //Toast.makeText(activity, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                            tvmessage.setVisibility(View.VISIBLE);
                            tvmessage.setText(array_mobile.getString(i));
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
            public void onFailure(Call<Enquiryformmodel> call, Throwable t) {
                    dialog.dismiss();
                  //  ToastMessage.onToast(activity, "We have some issues ",ToastMessage.ERROR);
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
        if (v.getId() == R.id.btsubmit) {
            initvalidation();
        }
    }
}