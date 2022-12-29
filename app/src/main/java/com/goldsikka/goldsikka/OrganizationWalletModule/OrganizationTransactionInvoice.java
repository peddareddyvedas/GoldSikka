package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.goldsikka.goldsikka.Activitys.TransactionInvoice;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationTransactionInvoice extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvdate)
    TextView tvdate;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvtransid)
    TextView tvtransid;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvname)
    TextView tvname;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvpaymode)
    TextView tvpaymode;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvgoldrate)
    TextView tvgoldrate;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvgrams)
    TextView tvgrams;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvamount)
    TextView tvamount;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvgst)
    TextView tvgst;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvtotalamount)
    TextView tvtotalamount;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvdesc)
    TextView tvdesc;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btdownload)
    Button btdownload;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.lltxntype)
    LinearLayout lltxntype;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txtProgressPercent)
    TextView txtProgressPercent;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llinvoice)
    LinearLayout llinvoice;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llbar)
    LinearLayout llbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llgst)
    LinearLayout llgst;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.lltotalamount)
    LinearLayout lltotalamount;



    DownloadZipFileTask downloadZipFileTask;
    private static final String TAG = "MainActivity";
    private static final int STORAGE_CODE = 100;
    ResponseBody responseBody;

    String stdate,sttrnsid,stname,stpaiedmode,stgoldrate,stgrams,stamount,stgst,sttotalamount,stdesc,sttxntype;
    ApiDao apiDao;

    Date currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_transaction_invoice);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){

            stdesc = bundle.getString("desc");
            stamount = bundle.getString("amount");
            stdate = bundle.getString("date");
            stgoldrate = bundle.getString("goldrate");
            stgrams = bundle.getString("grams");
            stgst = bundle.getString("gst");
            stname = bundle.getString("name");
            stpaiedmode = bundle.getString("paymode");
            sttotalamount = bundle.getString("totalamount");
            sttrnsid = bundle.getString("transctionid");
            sttxntype = bundle.getString("txntype");

        }
        if (sttxntype.equals("BU")){
            lltxntype.setVisibility(View.VISIBLE);
            llgst.setVisibility(View.VISIBLE);
            lltotalamount.setVisibility(View.VISIBLE);
        }else {
            lltotalamount.setVisibility(View.GONE);
            lltxntype.setVisibility(View.GONE);
            llgst.setVisibility(View.GONE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Transaction Receipt");
        //  toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setdetails();



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                //   onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("SetTextI18n")
    public void setdetails(){

        tvtotalamount.setText(": "+getResources().getString(R.string.Rs)+sttotalamount);
        tvdate.setText(": "+stdate);
        tvdesc.setText(stdesc);
        tvgrams.setText(": "+stgrams);
        tvtransid.setText(": "+sttrnsid);
        tvname.setText(": "+stname);
        tvpaymode.setText(": "+stpaiedmode);
        tvgoldrate.setText(": "+getResources().getString(R.string.Rs)+stgoldrate);
        tvamount.setText(": "+getResources().getString(R.string.Rs)+stamount);
        tvgst.setText(": "+getResources().getString(R.string.Rs)+stgst);

    }
    @OnClick(R.id.btdownload)
    public void receiptdownload(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentTime = Calendar.getInstance().getTime();
        }
        pdfdownload();
        //  askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101);


    }


    public  void  pdfdownload(){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        }else {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<ResponseBody> getpdfdownload = apiDao.transactionpdfdownload("Bearer "+AccountUtils.getAccessToken(this),sttrnsid);

            getpdfdownload.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int statuscode = response.code();
                    Log.e("statuscode", String.valueOf(statuscode));


                    if (statuscode == HttpsURLConnection.HTTP_OK){
                        dialog.dismiss();
                        responseBody = response.body();
                        Log.e("responseBody", String.valueOf(responseBody));
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_DENIED) {
                                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permissions, STORAGE_CODE);
                            }else {
                                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_LONG).show();
                                downloadZipFileTask = new DownloadZipFileTask();
                                downloadZipFileTask.execute(response.body());
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), " Downloading...", Toast.LENGTH_LONG).show();
//                            downloadZipFileTask = new DownloadZipFileTask();
//                            downloadZipFileTask.execute(response.body());
                        }



                    }else {
                        dialog.dismiss();
                     //   ToastMessage.onToast(OrganizationTransactionInvoice.this,"Technical Issue Try after some time  ",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("onfail",t.toString());
                    ToastMessage.onToast(OrganizationTransactionInvoice.this,"We have some issues Try after some time ",ToastMessage.ERROR);

                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(ResponseBody... urls) {
            //Copy you logic to calculate progress and call
            saveToDisk(urls[0], "Goldsikka."+sttrnsid+currentTime+".pdf");

            return null;
        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {

            Log.e("API123", progress[0].second + " ");

            if (progress[0].first == 100)
                //Toast.makeText(getApplicationContext(),  Toast.LENGTH_SHORT).show();
                ToastMessage.onToast(OrganizationTransactionInvoice.this,"File downloaded in Goldsikka Folder",ToastMessage.SUCCESS);

            if (progress[0].second > 0) {
                int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);
                progressBar.setProgress(currentProgress);

                txtProgressPercent.setText("Progress " + currentProgress + "%");

            }

            if (progress[0].first == -1) {

                ToastMessage.onToast(OrganizationTransactionInvoice.this,"Download failed Try Again",ToastMessage.ERROR);

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
            file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "Goldsikka");
            if (!file.exists()){
                file.mkdirs();
            }

//          file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

            File destinationFile = new File(file, filename);
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
            } catch (IOException e) {
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                downloadZipFileTask.doProgress(pairs);
                Log.e(TAG, " 1 Failed to save the file!"+e);

            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "2 Failed to save the file!"+e);
        }
    }

}
