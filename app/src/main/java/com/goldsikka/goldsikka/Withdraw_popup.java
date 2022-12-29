package com.goldsikka.goldsikka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.goldsikka.goldsikka.Activitys.AddBankDetailsForReferral;
import com.goldsikka.goldsikka.Activitys.MoneyWallet.AddMonet_to_Wallet;
import com.goldsikka.goldsikka.Activitys.ReferAndEarnActivity;
import com.goldsikka.goldsikka.Fragments.Customer_AddBankDetails;
import com.goldsikka.goldsikka.Fragments.Sell_Fragment;
import com.goldsikka.goldsikka.Fragments.Successpopup;
import com.goldsikka.goldsikka.Models.ReferandEarnModel;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Withdraw_popup extends AppCompatActivity implements View.OnClickListener {

    TextView tverror;
    EditText etamount;
    Button btwithdarw;
    String stamount;
    ApiDao apiDao;
    String Walletamount;
    TextView tvwalletmoney;

    Spinner spin_bank_details;

    List<Listmodel> list;
    ArrayList<String> banklist;
    String SelectBank;
    String stknow,bank_id;
    LinearLayout ll_bankdetsila;
    Button btn_AddBank;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupforreferral);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Walletamount = bundle.getString("Walletamount");
        }
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Withdraw Amount");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initview();
        spinner_bank();
        wallet_amount();


    }

    public void spinner_bank(){
        banklist = new ArrayList<>();
        loaddata();
        spin_bank_details.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectBank = spin_bank_details.getItemAtPosition(spin_bank_details.getSelectedItemPosition()).toString();
                //    spinner_state.getSelectedView().setTextColor(getResources().getColor(R.color.Blue));
                ((TextView) view).setTextColor(ContextCompat.getColor(Withdraw_popup.this, R.color.DarkBrown));
                // state_id     = spinner_state.getSelectedItemPosition();
                //  if (!SelectBank.equals("Select Bank")) {
                Listmodel listmodel = list.get(i);
                bank_id = listmodel.getId();
//                    rs_state = listmodel.getName();

                //Toast.makeText(activity,String.valueOf(id), Toast.LENGTH_SHORT).show();
                // }

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
            Call<List<Listmodel>> get_address = apiDao.get_bank("Bearer " + AccountUtils.getAccessToken(this));
            get_address.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    list = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        if (list.size() != 0) {
                            ll_bankdetsila.setVisibility(View.VISIBLE);
                            btn_AddBank.setVisibility(View.GONE);
                            for (Listmodel listmodel : list) {
                                dialog.dismiss();
                                banklist.add(listmodel.getName());
                            }
                            spin_bank_details.setAdapter(new ArrayAdapter<String>(Withdraw_popup.this, android.R.layout.simple_spinner_dropdown_item,
                                    banklist));

                        }else {
                            dialog.dismiss();
                            btn_AddBank.setVisibility(View.VISIBLE);
                            ll_bankdetsila.setVisibility(View.GONE);
                        }
                    }
                }


                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                    //  ToastMessage.onToast(Withdraw_popup.this, "We Have Some Issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
             onBackPressed();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }
    public void initview(){

        etamount = findViewById(R.id.etwithdrawamount);

        tvwalletmoney = findViewById(R.id.tvwalletmoney);

        ll_bankdetsila = findViewById(R.id.ll_bank_details);
        btn_AddBank = findViewById(R.id.add_bank);
        btn_AddBank.setOnClickListener(this);
        spin_bank_details = findViewById(R.id.spin_bank_details);

        etamount.setHint(Html.fromHtml(getString(R.string.amount)));


        btwithdarw = findViewById(R.id.btwithdraw);
        btwithdarw.setOnClickListener(this);

        tverror = findViewById(R.id.tverror);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btwithdraw:
                validation();
                break;

            case R.id.add_bank:
                Intent intent = new Intent(this,Customer_AddBankDetails.class);
                intent.putExtra("from","withdraw");
                startActivity(intent);
                break;
        }
    }

    public void validation(){

        stamount = etamount.getText().toString().trim();

        if (stamount.isEmpty()){
            tverror.setVisibility(View.VISIBLE);
            tverror.setText("Please enter the amount");
        }else {
            initwithdrawl();
        }
    }

    public void wallet_amount(){

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
            Call<Listmodel> getdetails = apiDao.walletAmount("Bearer "+AccountUtils.getAccessToken(this));
            getdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode  = response.code();
                    Log.e("statuscode dd" , String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        Listmodel list = response.body();
                        Walletamount = list.getAmount_wallet();
                        tvwalletmoney.setText("Account Balance : "+getString(R.string.Rs)+" "+Walletamount);

                        dialog.dismiss();

                    }else {

                        dialog.dismiss();

                       // ToastMessage.onToast(Withdraw_popup.this,"Technical issue",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails",t.toString());
                    dialog.dismiss();
                    // ToastMessage.onToast(Withdraw_popup.this,"We have some issue",ToastMessage.ERROR);
                }
            });
        }

    }

    public void initwithdrawl(){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }
        else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<ReferandEarnModel> getwithdraw = apiDao.loadwithdraw("Bearer "+AccountUtils.getAccessToken(this),stamount,bank_id);
            getwithdraw.enqueue(new Callback<ReferandEarnModel>() {
                @Override
                public void onResponse(@NonNull Call<ReferandEarnModel> call, @NonNull retrofit2.Response<ReferandEarnModel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode", String.valueOf(statuscode));
                    ReferandEarnModel model = response.body();
//                    Log.e("responcebody", new Gson().toJson(response));

                    if (statuscode == HttpsURLConnection.HTTP_OK||statuscode == HttpsURLConnection.HTTP_CREATED){
                        dialog.dismiss();
                        String msg = model.getMessage();
                        Log.e("responcebodymsg",""+msg);

                        onsucess(msg);
                      //  opendialog(msg);


                    }
                    else if (statuscode == HttpsURLConnection.HTTP_BAD_REQUEST){
                        dialog.dismiss();
                        try {
                            assert  response.errorBody()!= null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            boolean bank = jObjError.getBoolean("is_bank");
                            String msg = jObjError.getString("message");
                            ToastMessage.onToast(Withdraw_popup.this,msg,ToastMessage.ERROR);

                            if (!bank){
                                Addbankdetailspopup(msg);

                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }else {
                        dialog.dismiss();
                        tverror.setVisibility(View.GONE);

                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            ToastMessage.onToast(Withdraw_popup.this,st,ToastMessage.ERROR);
                            JSONObject er = jObjError.getJSONObject("errors");

                            try {
                                JSONArray array_password= er.getJSONArray("amount");
                                for (int i = 0; i < array_password.length(); i++) {
                                    tverror.setVisibility(View.VISIBLE);
                                    tverror.setText(array_password.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("msgerr",e.toString());

                            }

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            Log.e("msgerr222exception",e.toString());
                        }

                    }
                }

                @Override
                public void onFailure(Call<ReferandEarnModel> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("onfailure",t.toString());
                    // ToastMessage.onToast(Withdraw_popup.this,"We have some issues",ToastMessage.ERROR);
                }
            });
        }
    }

    public void onsucess(String msg) {

        Intent intent = new Intent(this, Successpopup.class);
        intent.putExtra("from", "Withdraw");
        intent.putExtra("description", msg);
        startActivity(intent);

    }


    TextView tvmsg;
    ImageView imageView_close;

    public void opendialog(String  msg){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setCancelable(false);
        View bottomSheet = LayoutInflater.from(this)
                .inflate(R.layout.referralbottomsheet,findViewById(R.id.bottomsheet));
        bottomSheetDialog.setContentView(bottomSheet);
        ((View) bottomSheet.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tvmsg = bottomSheet.findViewById(R.id.tvmsg);
        tvmsg.setText(msg);

        imageView_close = bottomSheet.findViewById(R.id.iv_close);

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(Withdraw_popup.this, AddMonet_to_Wallet.class);
                startActivity(intent);
            }
        });

        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }


    AlertDialog alertDialogdialogbank;
    GifImageView loading_gifbank;
    Button submit;
    TextView tvtext;
    ImageView img_close;

    public void Addbankdetailspopup(String msg){
        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.bankdetailslinkforsellpopup,null);
        alertdilog.setCancelable(false);
        alertdilog.setView(dialogview);
        alertDialogdialogbank = alertdilog.create();

        tvtext = dialogview.findViewById(R.id.tvaddress);
        tvtext.setText(msg);
        img_close = dialogview.findViewById(R.id.img_close);
        loading_gifbank = dialogview.findViewById(R.id.loading_gif);

        alertDialogdialogbank.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogdialogbank.dismiss();
            }
        });
        submit = dialogview.findViewById(R.id.btn_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit.setVisibility(View.GONE);
                loading_gifbank.setVisibility(View.VISIBLE);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {

                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Withdraw_popup.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getbankdetails();
                                submit.setVisibility(View.VISIBLE);
                                loading_gifbank.setVisibility(View.GONE);
                            }
                        });
                    }
                }, 500);
            }
        });
    }
    public void getbankdetails(){
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            Intent intent = new Intent(Withdraw_popup.this, Customer_AddBankDetails.class);
            intent.putExtra("from","Refer");
            startActivity(intent);
        }
    }
}
