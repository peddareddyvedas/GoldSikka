package com.goldsikka.goldsikka.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.goldsikka.goldsikka.Activitys.MoneyWallet.AddMonet_to_Wallet;
import com.goldsikka.goldsikka.Activitys.Passbook_Activity;
import com.goldsikka.goldsikka.Activitys.ReceiptActivity;
import com.goldsikka.goldsikka.Activitys.TransactionInvoice;
import com.goldsikka.goldsikka.Fragments.Schemes.Schemes_usersubscribed_list;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.OrganizationWalletModule.Organizationwallet_mainpage;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Successpopup extends AppCompatActivity {

    TextView tv_content;
    String grams, number, amount, from, tennure_type;
    Button bt_continue, btdownload;

    ApiDao apiDao;

    Date currentTime;

    DownloadZipFileTask downloadZipFileTask;
    private static final String TAG = "MainActivity";
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txtProgressPercent)
    TextView txtProgressPercent;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    String sttrnsid, stdescription;
    private static final int STORAGE_CODE = 100;
    ResponseBody responseBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successpopup_screen);

        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            grams = bundle.getString("grams");
            amount = bundle.getString("amount");
            number = bundle.getString("number");
            from = bundle.getString("from");
            tennure_type = bundle.getString("tennure_type");
            sttrnsid = bundle.getString("transctionid");
            stdescription = bundle.getString("description");
        }

        tv_content = findViewById(R.id.tv_content);
        bt_continue = findViewById(R.id.bt_continue);
        btdownload = findViewById(R.id.btdownload);
        btdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (from) {

                    case "Mygold":
                        // schemepdf();
                        break;
                    case "11+1 Jewellery":
                        //schemepdf();
                        break;
                    case "nextmmi":
                        //  schemepdf();
                        break;
                    default:
//                        receiptdownload();
                        Intent intent = new Intent(Successpopup.this, ReceiptActivity.class);
                        intent.putExtra("transctionid", sttrnsid);
                        startActivity(intent);
                        break;
                }
            }
        });

        bt_continue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (from) {

                    case "Mygold":
                        openschemelist();
                        break;

                    case "11+1Jewellery":
                        openschemelist();
                        break;

                    case "nextmmi":
                        openschemelist();
                        break;

                    case "Digitalgold":

                        Intent intent = new Intent(Successpopup.this, Passbook_Activity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        Passbook_Activity.isFromnotification = true;

                        startActivity(intent);
                        break;

                    case "org_redeem":

                        Intent orgintent = new Intent(Successpopup.this, Organizationwallet_mainpage.class);
                        startActivity(orgintent);
                        break;

                    case "Withdraw":
                        Intent intent1 = new Intent(Successpopup.this, AddMonet_to_Wallet.class);
                        startActivity(intent1);
                        break;

                    default:
                        open_mainactivity();
                        break;

                }

            }
        });

        initopenfrom();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    public void openschemelist() {
        Intent intent = new Intent(this, Schemes_usersubscribed_list.class);
        Schemes_usersubscribed_list.isFromgoldtransaction = true;
        startActivity(intent);
    }

    public void initopenfrom() {

        if (from.equals("redeem")) {
            btdownload.setVisibility(View.VISIBLE);
            tv_content.setText(stdescription);
            // tv_content.setText("Thank You \n You have Redeem.."+grams+" Grams of Gold Successfully.\n Gold Delivery will be done within 7 Working days.If any Delay in Redeem Please Contact Goldsikka .");
        } else if (from.equals("transfer")) {
            btdownload.setVisibility(View.VISIBLE);
            tv_content.setText(stdescription);
            // tv_content.setText("Thank You \n  You Transferred "+grams+"grams of Gold to "+number);
        } else if (from.equals("gift")) {
            btdownload.setVisibility(View.VISIBLE);
            tv_content.setText(stdescription);
            //tv_content.setText("Thank You \n  You Gifted "+grams+" grams of Gold to  "+number);
        } else if (from.equals("sell")) {
            btdownload.setVisibility(View.VISIBLE);
            tv_content.setText(stdescription);
            //tv_content.setText("Thank You \n You have sold " +grams+" gram of Gold Successfully.\n Rs."+amount+ " will be transferred to your bank account within 3 working days. If any delay with transfer amount please contact Goldsikka. Thank you for your continued cooperation. Team Goldsikka.");
        } else if (from.equals("Digitalgold")) {
            btdownload.setVisibility(View.VISIBLE);
            tv_content.setText(stdescription);
            //  tv_content.setText("Thank You \n "+grams+"grams of Gold has been Successfully bought and delivered to your secure wallet ");
        } else if (from.equals("Mygold")) {
            btdownload.setVisibility(View.GONE);
            tv_content.setText(stdescription);
        } else if (from.equals("11+1Jewellery")) {
            btdownload.setVisibility(View.GONE);
            tv_content.setText(stdescription);
        } else if (from.equals("Event")) {
            btdownload.setVisibility(View.GONE);
            tv_content.setText(stdescription);
        } else if (from.equals("nextmmi")) {
            btdownload.setVisibility(View.GONE);
            tv_content.setText(stdescription);
        } else if (from.equals("org_redeem")) {
            btdownload.setVisibility(View.GONE);
            tv_content.setText(stdescription);
        } else if (from.equals("ORG")) {
            btdownload.setVisibility(View.GONE);
            tv_content.setText(stdescription);
        } else if (from.equals("Withdraw")) {
            btdownload.setVisibility(View.GONE);
            tv_content.setText(stdescription);
        }
    }

    public void open_mainactivity() {
        Intent intent = new Intent(this, Passbook_Activity.class);
        Passbook_Activity.isFromnotification = true;
        startActivity(intent);
    }


    public void receiptdownload() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentTime = Calendar.getInstance().getTime();
        }
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {
            //   askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101);
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<ResponseBody> getpdfdownload = apiDao.transactionpdfdownload("Bearer " + AccountUtils.getAccessToken(this), sttrnsid);

            getpdfdownload.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int statuscode = response.code();
                    Log.e("statuscode", String.valueOf(statuscode));
                    ResponseBody model = response.body();

                    if (statuscode == HttpsURLConnection.HTTP_OK) {

                        dialog.dismiss();

                        responseBody = response.body();

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_DENIED) {
                                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permissions, STORAGE_CODE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_LONG).show();
                                downloadZipFileTask = new DownloadZipFileTask();
                                downloadZipFileTask.execute(responseBody);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_LONG).show();
                            downloadZipFileTask = new DownloadZipFileTask();
                            downloadZipFileTask.execute(responseBody);
                        }

                    } else {
                        dialog.dismiss();
                        // ToastMessage.onToast(Successpopup.this, "Technical Issue Try after some time ", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("onfail", t.toString());
                    //  ToastMessage.onToast(Successpopup.this, "We have some issues,Try after some time", ToastMessage.ERROR);
                }
            });
        }
    }

    public void schemepdf() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentTime = Calendar.getInstance().getTime();
        }
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {
            //   askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101);
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<ResponseBody> getpdfdownload = apiDao.Schhemetransactionpdfdownload("Bearer " + AccountUtils.getAccessToken(this), sttrnsid);

            getpdfdownload.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int statuscode = response.code();
                    Log.e("statuscode", String.valueOf(statuscode));
                    ResponseBody model = response.body();

                    if (statuscode == HttpsURLConnection.HTTP_OK) {

                        dialog.dismiss();

                        responseBody = response.body();

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_DENIED) {
                                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permissions, STORAGE_CODE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_LONG).show();
                                downloadZipFileTask = new DownloadZipFileTask();
                                downloadZipFileTask.execute(responseBody);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_LONG).show();
                            downloadZipFileTask = new DownloadZipFileTask();
                            downloadZipFileTask.execute(responseBody);
                        }


                    } else {
                        dialog.dismiss();
                        // ToastMessage.onToast(Successpopup.this, "Technical Issue Try after some time ", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("onfail", t.toString());
                    // ToastMessage.onToast(Successpopup.this, "We have some issues,Try after some time", ToastMessage.ERROR);

                }
            });
        }
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

//    private void askForPermission(String permission, Integer requestCode) {
//        if (ContextCompat.checkSelfPermission(Successpopup.this, permission) != PackageManager.PERMISSION_GRANTED) {
//
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(Successpopup.this, permission)) {
//                ActivityCompat.requestPermissions(Successpopup.this, new String[]{permission}, requestCode);
//
//            } else {
//                ActivityCompat.requestPermissions(Successpopup.this, new String[]{permission}, requestCode);
//            }
//        } else if (ContextCompat.checkSelfPermission(Successpopup.this, permission) == PackageManager.PERMISSION_DENIED) {
//            Toast.makeText(getApplicationContext(), "Permission was denied", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
//
//            if (requestCode == 101)
//                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//        }
//    }

    private class DownloadZipFileTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(ResponseBody... urls) {
            //Copy you logic to calculate progress and call
            saveToDisk(urls[0], "Goldsikka." + sttrnsid + " " + currentTime + ".pdf");
            return null;
        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {

            Log.d("API123", progress[0].second + " ");

            if (progress[0].first == 100)
                //Toast.makeText(getApplicationContext(),  Toast.LENGTH_SHORT).show();
                ToastMessage.onToast(Successpopup.this, "File downloaded in Goldsikka Folder", ToastMessage.SUCCESS);
            if (progress[0].second > 0) {
                int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);
                progressBar.setProgress(currentProgress);

                txtProgressPercent.setText("Progress " + currentProgress + "%");

            }

            if (progress[0].first == -1) {
                ToastMessage.onToast(Successpopup.this, "Download failed Try again ", ToastMessage.ERROR);

            }

        }

        public void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    private void saveToDisk(ResponseBody body, String filename) {
        try {

            File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "Goldsikka");
            if (!file.exists()) {
                file.mkdirs();
            }

            //  File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

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
                Log.d(TAG, "File Size=" + fileSize);
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    progress += count;
                    Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                    downloadZipFileTask.doProgress(pairs);
                    Log.d(TAG, "Progress: " + progress + "/" + fileSize + " >>>> " + (float) progress / fileSize);
                }

                outputStream.flush();

                Log.d(TAG, destinationFile.getParent());
                Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                downloadZipFileTask.doProgress(pairs);

                return;
            } catch (IOException e) {
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                downloadZipFileTask.doProgress(pairs);
                Log.d(TAG, "Failed to save the file!");
                return;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        }
    }
}
