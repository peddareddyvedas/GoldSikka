package com.goldsikka.goldsikka.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.Models.ReferandEarnModel;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Withdraw_popup;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

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
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferAndEarnActivity extends AppCompatActivity{

    //    @SuppressLint("NonConstantResourceId")
//    @BindView(value = R.id.ivback)
//    ImageView ivback;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.ivcopy)
    ImageView ivcopy;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvreferralamount)
    TextView tvreferralamount;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvdownloads)
    TextView tvdownloads;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvreferralcode)
    TextView tvreferralcode;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.btrefer)
    Button btrefer;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvmore)
    Button tvmore;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvwithdrwl)
    Button tvwithdrwl;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.message)
    TextView tvmessage;



    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;

    String streferralcode,stdownlods,stamount,stmore;
    ApiDao apiDao;
//    Button btn_withdraw;
    String stmsg;
TextView unameTv, uidTv, titleTv;

    RelativeLayout backbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ButterKnife.bind(this);
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));

        getreferdetails();

//        btn_withdraw = findViewById(R.id.tvwithdrwl);
//        btn_withdraw.setOnClickListener(this);

        swipe_layout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipe_layout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(ReferAndEarnActivity.this)) {
                            ToastMessage.onToast(ReferAndEarnActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            swipe_layout.setRefreshing(false);
                            return;
                        }
                        else {
                            getreferdetails();
                        }
                        swipe_layout.setRefreshing(false);

                    }
                }, 3000);
            }
        });
        ivcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TextView tv = findViewById(R.id.tvreferralcode);
                //  registerForContextMenu(tvreferralcode);

                if (stmsg.equals("")) {

                    ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("text", tvreferralcode.getText());
                    manager.setPrimaryClip(clipData);
                    Toast.makeText(ReferAndEarnActivity.this, "copied", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
    }
    public void getreferdetails(){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<ReferandEarnModel> getdetails = apiDao.loadreferraldeatils("Bearer "+AccountUtils.getAccessToken(this));
            getdetails.enqueue(new Callback<ReferandEarnModel>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onResponse(Call<ReferandEarnModel> call, Response<ReferandEarnModel> response) {
                    int statuscoe  = response.code();
                    if (statuscoe == HttpsURLConnection.HTTP_OK||statuscoe == HttpsURLConnection.HTTP_CREATED){

                        dialog.dismiss();

                        ReferandEarnModel model = response.body();
                        stamount = model.getEarningsAmount();
                        stdownlods = model.getUserCount();
                        streferralcode = model.getReferralCode();
                        stmore = model.getContent();
                         stmsg = model.getMessage();
                        if (stmsg.equals("")){
                            btrefer.setEnabled(true);
                        }else {
                            btrefer.setEnabled(false);
                            tvmessage.setText(stmsg);
                            tvmessage.setTextColor(getColor(R.color.red));
                        }
                        setdetails();

                    }else {
                        dialog.dismiss();
                      //  ToastMessage.onToast(ReferAndEarnActivity.this,"Technical issues",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<ReferandEarnModel> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(ReferAndEarnActivity.this,"We have some issues", ToastMessage.ERROR);

                }
            });
        }
    }

    public void setdetails(){
        tvdownloads.setText(stdownlods);
        tvreferralamount.setText(stamount);
        tvreferralcode.setText(streferralcode);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btrefer)
    public void shareapp(){
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);

            String msg = "Join with me on Goldsikka, a Trusted app for Buy/ Sell / Loan - Gold. ";
            String refermsg = msg+"\n"+"Enter my code : "+streferralcode+"\nTo avail the Benefits of Goldsikka :\n"+"Google Play Store "+"https://play.google.com/store/apps/details?id=com.goldsikka.goldsikka"+
                    "\nApp Store "+"https://apps.apple.com/in/app/goldsikka/id1549497564";
            //  sendIntent.putExtra(Intent.EXTRA_SUBJECT,refermsg);
            sendIntent.putExtra(Intent.EXTRA_TEXT, refermsg);
            sendIntent.setType("text/plain");
            Intent.createChooser(sendIntent, "Share via");
            startActivity(sendIntent);

        }

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tvmore)
    public void moredetails(){
        Intent intent = new Intent(this,ReferralDetails.class);
        intent.putExtra("moreinfo",stmore);
        intent.putExtra("referralcode",streferralcode);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tvwithdrwl)
    public void withdraw(){
        Intent intent = new Intent(this,Refer_Earn_List.class);
        startActivity(intent);
    }

//    @SuppressLint("NonConstantResourceId")
//    @OnClick(R.id.tra_list)
//    public void list(){
//        Intent intent = new Intent(this,Refer_Earn_List.class);
//        startActivity(intent);
//    }


//    TextView tverror;
//    EditText etamount;
//    Button btwithdarw;
//    ImageView ivclose;
//    AlertDialog alertDialogdialog;
//    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
//    @OnClick(R.id.tvwithdrwl)
//    public void forwithdrawl(){
//        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogview = inflater.inflate(R.layout.popupforreferral,null);
//
//        //alertdilog.setContentView(dialogview);
//       // ((View) dialogview.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
//
//        alertdilog.setCancelable(false);
//        alertdilog.setView(dialogview);
//        alertDialogdialog = alertdilog.create();
//
//        etamount = dialogview.findViewById(R.id.etwithdrawamount);
//        etamount.setText(stamount);
//        btwithdarw = dialogview.findViewById(R.id.btwithdraw);
//        ivclose = dialogview.findViewById(R.id.ivclose);
//        tverror = dialogview.findViewById(R.id.tverror);
//        ivclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialogdialog.dismiss();
//            }
//        });
//        btwithdarw.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tverror.setVisibility(View.GONE);
//                String amount = etamount.getText().toString().trim();
//                initwithdraw(amount);
//                //hh  bottomSheetDialog.dismiss();
//            }
//        });
//
//        alertDialogdialog.show();
//    }
//    public void initwithdraw(String withdrawamount){
//
//        if (withdrawamount.isEmpty()){
//            tverror.setVisibility(View.VISIBLE);
//            tverror.setText("Please enter the amount");
//        }else {
//            initwithdrawl(withdrawamount);
//        }
//    }
//    public void initwithdrawl(String amount){
//
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Please Wait....");
//        dialog.setCancelable(false);
//        dialog.show();
//        if (!NetworkUtils.isConnected(this)){
//            dialog.dismiss();
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//       }
//        else {
//            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//            Call<ReferandEarnModel> getwithdraw = apiDao.loadwithdraw("Bearer "+AccountUtils.getAccessToken(this),amount);
//            getwithdraw.enqueue(new Callback<ReferandEarnModel>() {
//                @Override
//                public void onResponse(@NonNull Call<ReferandEarnModel> call, @NonNull retrofit2.Response<ReferandEarnModel> response) {
//                    int statuscode = response.code();
//                    Log.e("statuscode", String.valueOf(statuscode));
//                    ReferandEarnModel model = response.body();
////                    Log.e("responcebody", new Gson().toJson(response));
//
//                    if (statuscode == HttpsURLConnection.HTTP_OK||statuscode == HttpsURLConnection.HTTP_CREATED){
//                        dialog.dismiss();
//                        String msg = model.getMessage();
//                        opendialog(msg);
//                        alertDialogdialog.dismiss();
//                    }
//                    else if (statuscode == HttpsURLConnection.HTTP_BAD_REQUEST){
//                        dialog.dismiss();
//                        try {
//                            assert  response.errorBody()!= null;
//                            JSONObject jObjError = new JSONObject(response.errorBody().string());
//                            boolean bank = jObjError.getBoolean("is_bank");
//                            String msg = jObjError.getString("message");
//                            if (!bank){
//                                Addbankdetailspopup(msg);
//                                alertDialogdialog.dismiss();
//                            }
//                        } catch (JSONException | IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }else {
//                        dialog.dismiss();
//                        tverror.setVisibility(View.GONE);
//
//                        try {
//                            assert response.errorBody() != null;
//                            JSONObject jObjError = new JSONObject(response.errorBody().string());
//                            String st = jObjError.getString("message");
//                            ToastMessage.onToast(ReferAndEarnActivity.this,st,ToastMessage.ERROR);
//                            JSONObject er = jObjError.getJSONObject("errors");
//
//                            try {
//                                JSONArray array_password= er.getJSONArray("amount");
//                                for (int i = 0; i < array_password.length(); i++) {
//                                    tverror.setVisibility(View.VISIBLE);
//                                    tverror.setText(array_password.getString(i));
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                Log.e("msgerr",e.toString());
//
//                            }
//
//                        } catch (JSONException | IOException e) {
//                            e.printStackTrace();
//                            Log.e("msgerr222exception",e.toString());
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ReferandEarnModel> call, Throwable t) {
//                    dialog.dismiss();
//                    Log.e("onfailure",t.toString());
//                        ToastMessage.onToast(ReferAndEarnActivity.this,"We have some issues",ToastMessage.ERROR);
//                }
//            });
//        }
//
//    }
//    TextView tvmsg;
//    ImageView imageView_close;
//
//    public void opendialog(String  msg){
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
//        bottomSheetDialog.setCancelable(false);
//        View bottomSheet = LayoutInflater.from(this)
//                .inflate(R.layout.referralbottomsheet,findViewById(R.id.bottomsheet));
//        bottomSheetDialog.setContentView(bottomSheet);
//        ((View) bottomSheet.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
//
//        tvmsg = bottomSheet.findViewById(R.id.tvmsg);
//        tvmsg.setText(msg);
//
//        imageView_close = bottomSheet.findViewById(R.id.iv_close);
//
//        imageView_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialog.dismiss();
//            }
//        });
//
//        bottomSheetDialog.setContentView(bottomSheet);
//        bottomSheetDialog.show();
//    }
//
//
//    AlertDialog alertDialogdialogbank;
//    GifImageView loading_gifbank;
//    Button submit;
//    TextView tvtext;
//    ImageView img_close;
//
//    public void Addbankdetailspopup(String msg){
//        AlertDialog.Builder alertdilog = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogview = inflater.inflate(R.layout.bankdetailslinkforsellpopup,null);
//        alertdilog.setCancelable(false);
//        alertdilog.setView(dialogview);
//        alertDialogdialogbank = alertdilog.create();
//
//        tvtext = dialogview.findViewById(R.id.tvaddress);
//        tvtext.setText(msg);
//        img_close = dialogview.findViewById(R.id.img_close);
//        loading_gifbank = dialogview.findViewById(R.id.loading_gif);
//
//        alertDialogdialogbank.show();
//
//        img_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialogdialogbank.dismiss();
//            }
//        });
//        submit = dialogview.findViewById(R.id.btn_submit);
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                submit.setVisibility(View.GONE);
//                loading_gifbank.setVisibility(View.VISIBLE);
//
//                Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        try {
//
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        ReferAndEarnActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                getbankdetails();
//                                submit.setVisibility(View.VISIBLE);
//                                loading_gifbank.setVisibility(View.GONE);
//                            }
//                        });
//                    }
//                }, 500);
//            }
//        });
//    }
//    public void getbankdetails(){
//        if (!NetworkUtils.isConnected(this)) {
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//            return;
//        }else {
//            Intent intent = new Intent(ReferAndEarnActivity.this, AddBankDetailsForReferral.class);
//            startActivity(intent);
//        }
//    }
}
