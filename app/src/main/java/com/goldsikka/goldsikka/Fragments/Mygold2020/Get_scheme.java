package com.goldsikka.goldsikka.Fragments.Mygold2020;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.goldsikka.goldsikka.Activitys.ReceiptActivity;
import com.goldsikka.goldsikka.Adapter.mygoldlist_Adapter;
import com.goldsikka.goldsikka.Fragments.Schemes.NextMMiPaymentForSchems;
import com.goldsikka.goldsikka.Fragments.Schemes.SchemeTicket;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.Models.SchemeModel;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.OrganizationWalletModule.DonateGold;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.WelcomeActivity;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Get_scheme extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final Object CHANNEL_ID = "Notification";
    private static final int NOTIFICATION_ID = 101;
    ArrayList<SchemeModel> mygold_arrayList;
    mygoldlist_Adapter adapter;
    RecyclerView rv_mygolduser;
    String rs_id;
    LinearLayout ll_amount, lltotalgrams;
    //    lu_list,ll_empty,
    ApiDao apiDao;
    int ticketvisable;
    DownloadZipFileTask downloadZipFileTask;
    private static final String TAG = "Goldsikka";
    private static final int STORAGE_CODE = 100;
    ResponseBody responseBody;
    Date currentTime;
    TextWatcher textWatcher;


    TextView tv_schemename, tv_date, tv_totalmmis, tv_paidmmis, tv_pending, tv_totalgrams, tv_processingfee, tv_amount, tvmmistatus;
    String st_schemename, st_date, st_totalmmis, st_paidmmis, st_pending, st_totalgrams, st_processingfee, st_amount;
    String title, scheme_type, stid;

    SwipeRefreshLayout swipeRefresh;
    Button btticket;

    String after_dedcation = "null";


    String Walletamount;

    String walletamount = "null";
    TextView unameTv, uidTv, titleTv;

    String filenameee = "";
RelativeLayout backbtn;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_get_scheme);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Details");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Details");

        st_schemename = AccountUtils.getSchemename(this);
        backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
//        ToastMessage.onToast(Get_scheme.this, st_schemename, ToastMessage.SUCCESS);
//
//        ll_empty = findViewById(R.id.ll_empty);
//        lu_list = findViewById(R.id.lu_list);

        Bundle bundle = getIntent().getExtras();
        rs_id = bundle.getString("id");

        intilizeview();
        intilizerecylerview();

        if (st_schemename.equals("MY GOLD 2021 (Gold Chit)")) {
            lltotalgrams.setVisibility(View.VISIBLE);
        } else if (st_schemename.equals("JW")) {
            lltotalgrams.setVisibility(View.GONE);
        }
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            getdatascheme();
        }

//        wallet_amount();
//        dediactWalletMoney();


    }


    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
    }



    public void intilizeview() {
        lltotalgrams = findViewById(R.id.lltotalgrams);
        tv_schemename = findViewById(R.id.tv_schemename);
        tv_date = findViewById(R.id.tv_date);
        tv_totalmmis = findViewById(R.id.tv_totalmmis);
        tv_paidmmis = findViewById(R.id.tv_paidmmis);
        tv_pending = findViewById(R.id.tv_pending);
        tv_totalgrams = findViewById(R.id.tv_totalgrams);
        tv_processingfee = findViewById(R.id.tv_processingfee);
        tv_amount = findViewById(R.id.tv_amount);
        ll_amount = findViewById(R.id.ll_amount);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        btticket = findViewById(R.id.btticket);
        btticket.setOnClickListener(this);
        tvmmistatus = findViewById(R.id.tvmmistatus);

    }

    private void intilizerecylerview() {
        swipeRefresh.setOnRefreshListener(this);
        //   linearLayout = view.findViewById(R.id.linear);
        rv_mygolduser = findViewById(R.id.rv_mygolduser);
        rv_mygolduser.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_mygolduser.setLayoutManager(linearLayoutManager);
        rv_mygolduser.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(Get_scheme.this, LinearLayoutManager.VERTICAL);
        rv_mygolduser.addItemDecoration(decoration);
        mygold_arrayList = new ArrayList<>();
        adapter = new mygoldlist_Adapter(Get_scheme.this, mygold_arrayList);
        rv_mygolduser.setAdapter(adapter);
    }

    private void getdatascheme() {
        mygold_arrayList.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<SchemeModel> call = apiDao.getsubscribeplanedetails("Bearer " + AccountUtils.getAccessToken(this), rs_id);
            call.enqueue(new Callback<SchemeModel>() {
                @Override
                public void onResponse(@NotNull Call<SchemeModel> call, @NotNull Response<SchemeModel> response) {
                    int stauscode = response.code();
                    Log.e("Status Codee", String.valueOf(stauscode));
                    if (stauscode == HttpsURLConnection.HTTP_OK) {
                        List<SchemeModel> list = Collections.singletonList(response.body());
                        for (SchemeModel listmodel : list) {
                            stid = listmodel.getId();
                            st_processingfee = listmodel.getProcessing_fee();
                            st_totalgrams = listmodel.getEmi_grams();
                            st_date = listmodel.getCreated_date();
                            st_paidmmis = listmodel.getPaid_installments();
                            st_pending = listmodel.getPending_installments();
                            st_totalmmis = listmodel.getTotal_installments();
                            Log.e("statitle", listmodel.getSchemeStatus());
                            ticketvisable = listmodel.getScheme_status();
                            if (ticketvisable == 2) {
                                btticket.setVisibility(View.VISIBLE);
                            } else {
                                btticket.setVisibility(View.GONE);
                            }
                            tvmmistatus.setVisibility(View.VISIBLE);
                            tvmmistatus.setText(listmodel.getSchemeStatus());
                            JsonObject from = new JsonParser().parse(listmodel.getScheme_title().toString()).getAsJsonObject();
                            Log.e("fromhdsda", from.toString());
                            try {
                                JSONObject json_from = new JSONObject(from.toString());
                                title = json_from.getString("title");
                                scheme_type = json_from.getString("scheme_calculation_type");
                                Log.e("final amvsdiv", scheme_type);
                                tv_schemename.setText(title);
                                if (scheme_type.equals("MG")) {
                                    ll_amount.setVisibility(View.GONE);
                                } else if (scheme_type.equals("JW")) {
                                    ll_amount.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            openlist();
                            dialog.dismiss();
//                            ll_empty.setVisibility(View.GONE);
//                            lu_list.setVisibility(View.VISIBLE);
                            setdetails();
                        }
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(@NotNull Call<SchemeModel> call, @NotNull Throwable t) {
                    dialog.dismiss();
                   // ToastMessage.onToast(Get_scheme.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

    public void openlist() {
        mygold_arrayList.clear();
        Call<SchemeModel> list = apiDao.getsubscribeplane_details("Bearer " + AccountUtils.getAccessToken(this), rs_id);
        list.enqueue(new Callback<SchemeModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<SchemeModel> call, @NotNull Response<SchemeModel> response) {
                int statuscode = response.code();
                assert response.body() != null;
                List<SchemeModel> list = response.body().getUser_scheme_installments();
                if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {
                    if (list.size() != 0) {
                        for (SchemeModel listmodel : list) {
                            st_amount = listmodel.getAmount();
                            tv_amount.setText(getResources().getString(R.string.Rs) + " " + st_amount);
                            Log.e("stamounts", st_amount);
                            listmodel.setScheme_calculation_type(scheme_type);
                            mygold_arrayList.add(listmodel);
                            adapter.notifyDataSetChanged();
                            swipeRefresh.setRefreshing(false);
                        }
                    } else {
                        Toast.makeText(Get_scheme.this, "No Data Available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<SchemeModel> call, @NotNull Throwable t) {
                Log.e("scheme details", t.toString());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void setdetails() {
        tv_date.setText(st_date);
        tv_paidmmis.setText(st_paidmmis + " Months");
        tv_pending.setText(st_pending + " Months");
        tv_processingfee.setText(getResources().getString(R.string.Rs) + " " + st_processingfee);
        tv_totalgrams.setText(st_totalgrams + " Grams");
        tv_totalmmis.setText(st_totalmmis + " Months");
    }

    @Override
    public void onRefresh() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            swipeRefresh.setRefreshing(false);

        } else {
            getdatascheme();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btticket:
                Intent intent = new Intent(this, SchemeTicket.class);
                intent.putExtra("id", stid);
                startActivity(intent);
        }
    }

    //    @Override
//    public void onItemClick(View view, int position) {
//        SchemeModel model = mygold_arrayList.get(position);
//        Button button = view.findViewById(R.id.btnextpay);
//
//        if (ticketvisable == 0 || ticketvisable == 4){
//            button.setVisibility(View.VISIBLE);
//        }else {
//            button.setVisibility(View.GONE);
//        }
//    }
    public class mygoldlist_Adapter extends RecyclerView.Adapter<mygoldlist_Adapter.ViewHolder> {

        Context context;
        List<SchemeModel> arrayList;
        OnItemClickListener listener;

        public mygoldlist_Adapter(Context context, List<SchemeModel> list) {
            this.arrayList = list;
            this.context = context;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.subscribeplanedetails, parent, false);
            return new ViewHolder(view);


        }

        String amount;

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SchemeModel listmodel = arrayList.get(position);

            String scheme_type = listmodel.getScheme_calculation_type();

            String stusertransctions = String.valueOf(listmodel.getUser_transactions());

            //  Log.e("statitle", String.valueOf(listmodel.getSchemeStatus()));

            JsonObject from = new JsonParser().parse(listmodel.getUser_transactions().toString()).getAsJsonObject();
            try {

                JSONObject json_from = new JSONObject(from.toString());

                amount = json_from.getString("amount");
                String gst = json_from.getString("gst");
                String reference_id = json_from.getString("id");

                Double num1 = Double.valueOf(json_from.getString("gst"));
                Double num2 = Double.valueOf(json_from.getString("amount"));
                Double num3 = Double.valueOf(st_processingfee);


                Double sum = num1 + num2 + num3;
                Log.e("jhbbfb", "   " + sum);


                holder.tv_gst.setText(context.getResources().getString(R.string.Rs) + " " + gst);
                holder.Amount.setText(context.getResources().getString(R.string.Rs) + " " + amount);
                holder.mygold_totalamount.setText(context.getResources().getString(R.string.Rs) + " " + sum);

//            holder.myfinalamount.setText((context.getResources().getString(R.string.Rs) + " " + Integer.parseInt(amount) + Integer.parseInt(gst)));
                holder.TransactionId.setText(reference_id);


            } catch (JSONException e) {
                e.printStackTrace();

            }

            if (scheme_type.equals("MG")) {
                holder.ll_jewellery.setVisibility(View.GONE);
                holder.ll_mygold.setVisibility(View.VISIBLE);
//            holder.llmyfinalamount.setVisibility((View.GONE));
                holder.llfinalamount.setVisibility(View.VISIBLE);
            } else if (scheme_type.equals("JW")) {
                holder.ll_jewellery.setVisibility(View.VISIBLE);
                holder.ll_mygold.setVisibility(View.GONE);
//            holder.llmyfinalamount.setVisibility(View.VISIBLE);
                holder.llfinalamount.setVisibility(View.GONE);
            }

            holder.date.setText(listmodel.getEmi_date());
            holder.tv_grams.setText(listmodel.getGrams());
            holder.txn_type.setText(listmodel.getSource());
//        holder.llmyfinalamount.setVisibility(View.VISIBLE);
//        holder.myfinalamount.setText((context.getResources().getString(R.string.Rs) + " " + Integer.parseInt(amount) + Integer.parseInt(gst)));

            boolean ispay = listmodel.isPay();

            Log.e("inpos", String.valueOf(ticketvisable));
            if ((ticketvisable == 0 || ticketvisable == 4) && !ispay) {
                Log.e("ispay", String.valueOf(ispay));
                Log.e("ticjet", String.valueOf(ticketvisable));
                holder.btnextpay.setVisibility(View.VISIBLE);


            } else {
                Log.e("elseispay", String.valueOf(ispay));
                Log.e("elseticjet", String.valueOf(ticketvisable));
                holder.btnextpay.setVisibility(View.GONE);
                holder.tvstatus.setVisibility(View.VISIBLE);
                holder.tvstatus.setText(listmodel.getStatus());
            }
            if (listmodel.getPayment_status().equals("1")) {
                holder.btpdfdownload.setVisibility(View.VISIBLE);
            } else {
                holder.btpdfdownload.setVisibility(View.GONE);
            }

            holder.btnextpay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NextMMiPaymentForSchems.class);
                    intent.putExtra("id", listmodel.getId());
                    intent.putExtra("schemetype", scheme_type);
                    context.startActivity(intent);
                }
            });
            holder.btpdfdownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        currentTime = Calendar.getInstance().getTime();
                        filenameee = "Goldsikka." + currentTime + ".pdf";
                    }
                    filenameee = "Goldsikka." + currentTime + ".pdf";
                    Intent ii = new Intent(Get_scheme.this, ReceiptActivity.class);
                    ii.putExtra("positionid", listmodel.getId());
                    ii.putExtra("transctionid", listmodel.getTransaction_id());


                    startActivity(ii);
                }
            });
        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            TextView TransactionId, Amount, tv_gst, txn_type, date, tv_grams, tvstatus, myfinalamount, mygold_totalamount;

            LinearLayout ll_jewellery, ll_mygold, llmyfinalamount, llfinalamount;
            Button btnextpay, btpdfdownload;
            LinearLayout walletLayout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                TransactionId = itemView.findViewById(R.id.mygold_transctionid);

                ll_mygold = itemView.findViewById(R.id.ll_mygold);
                llmyfinalamount = itemView.findViewById(R.id.ll_myfinalamount);
                ll_jewellery = itemView.findViewById(R.id.ll_jewellery);
                tv_grams = itemView.findViewById(R.id.mygold_grams);
                tv_gst = itemView.findViewById(R.id.tv_gst);
                Amount = itemView.findViewById(R.id.mygold_Amount);
                myfinalamount = itemView.findViewById(R.id.myfinal_amount);
                date = itemView.findViewById(R.id.mygold_date);
                txn_type = itemView.findViewById(R.id.mygold_txntype);
                tvstatus = itemView.findViewById(R.id.tvstatus);
                btnextpay = itemView.findViewById(R.id.btnextpay);
                btpdfdownload = itemView.findViewById(R.id.btpdfdownload);
                walletLayout = itemView.findViewById(R.id.wallet_layout);

                llfinalamount = itemView.findViewById(R.id.llfinalamount);
                mygold_totalamount = itemView.findViewById(R.id.mygold_totalamount);


            }
        }
    }


    public void pdfdownload(String id) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<ResponseBody> getpdfdownload = apiDao.Schhemetransactionpdfdownload("Bearer " + AccountUtils.getAccessToken(this), id);

            getpdfdownload.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int statuscode = response.code();
                    Log.e("statuscode", String.valueOf(statuscode));


                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        responseBody = response.body();
                        Log.e("responseBody", String.valueOf(responseBody));
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_DENIED) {
                                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permissions, STORAGE_CODE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_LONG).show();


                                try {
                                    if (!NetworkUtils.isConnected(Get_scheme.this)) {
                                        ToastMessage.onToast(Get_scheme.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                                    } else {
                                        createnotification();
                                        File fileeeee = new File(Environment.getExternalStorageDirectory() + "/" + "download" + "/" + filenameee);
                                        Uri apkURi = FileProvider.getUriForFile(Get_scheme.this, getPackageName() + ".provider", fileeeee);
//                                startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(apkURi, "application/pdf").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION));
                                        Intent inttt = new Intent(Intent.ACTION_VIEW).setDataAndType(apkURi, "application/pdf").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

//                            Intent inttt = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
                                        PendingIntent resultIntent = PendingIntent.getActivity(Get_scheme.this, 1, inttt, PendingIntent.FLAG_UPDATE_CURRENT);

                                        final NotificationCompat.Builder builder1 = new NotificationCompat.Builder(getApplicationContext(), "101");

                                        builder1.setSmallIcon(R.drawable.gs_small_logo);
                                        builder1.setContentTitle(filenameee);
                                        builder1.setContentText("Downloading....");
                                        builder1.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                        builder1.setAutoCancel(true);
                                        builder1.setContentIntent(resultIntent);

                                        final int progressMaxValue = 100;
                                        int progressCurrentValue = 0;
                                        builder1.setProgress(progressMaxValue, progressCurrentValue, false);

                                        final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                                        notificationManagerCompat.notify(1, builder1.build());

                                        Thread thread = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                for (int progressCurrent = 0; progressCurrent < progressMaxValue; progressCurrent += 30) {
                                                    SystemClock.sleep(500);
                                                    builder1.setProgress(progressMaxValue, progressCurrent, false);
                                                    notificationManagerCompat.notify(1, builder1.build());
                                                }
                                                builder1.setProgress(0, 0, false);
                                                builder1.setContentText("Download Finished");
                                                notificationManagerCompat.notify(1, builder1.build());
                                                downloadZipFileTask = new DownloadZipFileTask();
                                                downloadZipFileTask.execute(response.body());
                                            }
                                        });
                                        thread.start();
                                    }
                                } catch (Exception e) {
                                    Log.e("TRY EXCEPT", "" + e);
                                }


//                            receiptdownload(id);
//
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.africau.edu/images/default/sample.pdf")));
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), " Downloading...", Toast.LENGTH_LONG).show();
//                        receiptdownload(id);

                            try {
                                if (!NetworkUtils.isConnected(Get_scheme.this)) {
                                    ToastMessage.onToast(Get_scheme.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                                } else {
                                    createnotification();
                                    File fileeeee = new File(Environment.getExternalStorageDirectory() + "/" + "download" + "/" + filenameee);
                                    Uri apkURi = FileProvider.getUriForFile(Get_scheme.this, getPackageName() + ".provider", fileeeee);
//                                startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(apkURi, "application/pdf").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION));
                                    Intent inttt = new Intent(Intent.ACTION_VIEW).setDataAndType(apkURi, "application/pdf").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

//                            Intent inttt = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
                                    PendingIntent resultIntent = PendingIntent.getActivity(Get_scheme.this, 1, inttt, PendingIntent.FLAG_UPDATE_CURRENT);

                                    final NotificationCompat.Builder builder1 = new NotificationCompat.Builder(getApplicationContext(), "101");

                                    builder1.setSmallIcon(R.drawable.gs_small_logo);
                                    builder1.setContentTitle(filenameee);
                                    builder1.setContentText("Downloading....");
                                    builder1.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                    builder1.setAutoCancel(true);
                                    builder1.setContentIntent(resultIntent);

                                    final int progressMaxValue = 100;
                                    int progressCurrentValue = 0;
                                    builder1.setProgress(progressMaxValue, progressCurrentValue, false);

                                    final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                                    notificationManagerCompat.notify(1, builder1.build());

                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (int progressCurrent = 0; progressCurrent < progressMaxValue; progressCurrent += 30) {
                                                SystemClock.sleep(500);
                                                builder1.setProgress(progressMaxValue, progressCurrent, false);
                                                notificationManagerCompat.notify(1, builder1.build());
                                            }
                                            builder1.setProgress(0, 0, false);
                                            builder1.setContentText("Download Finished");
                                            notificationManagerCompat.notify(1, builder1.build());
                                            downloadZipFileTask = new DownloadZipFileTask();
                                            downloadZipFileTask.execute(response.body());
                                        }
                                    });
                                    thread.start();
                                }
                            } catch (Exception e) {
                                Log.e("TRY EXCEPT", "" + e);
                            }

//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.africau.edu/images/default/sample.pdf")));

                        }


                    } else {
                        dialog.dismiss();
                       // ToastMessage.onToast(Get_scheme.this, "Technical Issue Try after some time  ", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("onfail", t.toString());
                  //  ToastMessage.onToast(Get_scheme.this, "We have some issues Try after some time ", ToastMessage.ERROR);

                }
            });
        }
    }

    public void createnotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification";
            String des = "This is my personal notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel("101", name, importance);
            notificationChannel.setDescription(des);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void receiptdownload(String id) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://staging-api.dev.goldsikka.in/api/user/schemes/" + id + "/scheme-pdf"));
        request.addRequestHeader("Authorization", "Bearer " + AccountUtils.getAccessToken(this));
        request.setTitle("Goldsikka." + currentTime + ".pdf");
        request.setMimeType("application/pdf");
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(true);
//    request.setShowRunningNotification(true);
//    request.setDestinationInExternalFilesDir(this, DIRECTORY_DOWNLOADS, "");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Download");
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_LONG).show();
                    downloadZipFileTask = new DownloadZipFileTask();
                    downloadZipFileTask.execute(responseBody);

                } else {
                    Toast.makeText(this, "Permission was denied! ", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private class DownloadZipFileTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, String> {

        String bb;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(ResponseBody... urls) {
            //Copy you logic to calculate progress and call
            saveToDisk(urls[0], filenameee);

            return null;
        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {

            Log.e("API123", progress[0].second + " ");

            if (progress[0].first == 100)
                //Toast.makeText(getApplicationContext(),  Toast.LENGTH_SHORT).show();
                ToastMessage.onToast(Get_scheme.this, "File downloaded", ToastMessage.SUCCESS);
//            Log.e("Status", "file downloaded");


            if (progress[0].second > 0) {
                int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);
                //  progressBar.setProgress(currentProgress);

                //    txtProgressPercent.setText("Progress " + currentProgress + "%");

            }

            if (progress[0].first == -1) {

                ToastMessage.onToast(Get_scheme.this, "Download failed Try Again", ToastMessage.ERROR);

            }

        }

        public void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    File file;

    private void saveToDisk(ResponseBody body, String filename) {
//
        try {
            file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), "");
            if (!file.exists()) {
                file.mkdirs();
            }

//          file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), filename);
//            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"Download");

            File destinationFile = new File(file, filename);
//            File newfile = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), filename);
            InputStream inputStream = null;
            OutputStream outputStream = null;


            try {

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);
                byte data[] = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                Log.e(TAG, "File Size=" + fileSize);
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    progress += count;
                    Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                    downloadZipFileTask.doProgress(pairs);
                    Log.e(TAG, "Progress: " + progress + "/" + fileSize + " >>>> " + (float) progress / fileSize);
                }

                outputStream.flush();

                Log.d(TAG, destinationFile.getParent());
                Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                downloadZipFileTask.doProgress(pairs);

                String pathhh = "file:/" + Environment.getExternalStorageDirectory() + "/" + "download" + "/" + filename;
                File fileeeee = new File(Environment.getExternalStorageDirectory() + "/" + "download" + "/" + filename);
                Log.e("dfbdfb", pathhh);
                Uri uriii = Uri.parse(pathhh);
                Log.e("URIIII", String.valueOf(uriii));
                try {
                    Uri apkURi = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", fileeeee);
                    startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(apkURi, "application/pdf").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION));
                } catch (Exception e) {
                    Log.e("Exception", "" + e);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                downloadZipFileTask.doProgress(pairs);
                Log.e(TAG, " 1 Failed to save the file!" + e);

            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "2 Failed to save the file!" + e);
        }
    }
}
