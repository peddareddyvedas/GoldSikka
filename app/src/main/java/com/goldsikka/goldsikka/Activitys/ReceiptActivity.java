package com.goldsikka.goldsikka.Activitys;


import static android.os.Environment.DIRECTORY_DOWNLOADS;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.goldsikka.goldsikka.Fragments.Mygold2020.Get_scheme;
import com.goldsikka.goldsikka.Fragments.Successpopup;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptActivity extends AppCompatActivity {


    DownloadZipFileTask downloadZipFileTask;
    ApiDao apiDao;
    private static final String TAG = "Goldsikka";
    private static final int STORAGE_CODE = 100;
    ResponseBody responseBody;

    TextView unameTv, uidTv, titleTv;
    TextView tv_schemename, tv_date, tv_totalmmis, tv_paidmmis, tv_pending, tv_totalgrams, tv_processingfee, tv_amount, tvmmistatus;
    String st_schemename, st_date, st_totalmmis, st_paidmmis, st_pending, st_totalgrams, st_processingfee, st_amount;
    String title, scheme_type, stid;
    String positionid = "sdv";
    String filenameee = "";
    Button pdfd;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txtProgressPercent)
    TextView txtProgressPercent;

    PDFView pdfview;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    String sttrnsid = "dvdsv";
    String stdescription;
    WebView web_view;
    String url;
    HashMap<String, String> map;

    String downn = "dfbdf";

    RelativeLayout backbtn;

    String transme = "no";
    ImageView pdfimage;
    String pdfurl = "";
    String date;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        backbtn = findViewById(R.id.backbtn);
        web_view = findViewById(R.id.web_view);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            positionid = bundle.getString("positionid");
            sttrnsid = bundle.getString("transctionid");

        }

        if (!sttrnsid.equals("dvdsv")) {
            transme = "yes";
        }

        //String pdfurl=BASE_URL+"api/one-transaction-pdf+"/"+positionid+"/"+ AccountUtils.getAccessToken(this);
        Log.e("positionid", "" + positionid);
//        setTitle("Details");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        pdfview = findViewById(R.id.pdfview);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Receipt Details");

        pdfd = findViewById(R.id.btpdfdownload);

        st_schemename = AccountUtils.getSchemename(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtProgressPercent = (TextView) findViewById(R.id.txtProgressPercent);


        Date currentTime = Calendar.getInstance().getTime();
        Log.e("currentTime", "" + currentTime);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh-mm a");
        date = formatter.format(Date.parse(String.valueOf(currentTime)));
        Log.e("date", "" + date);


        getSupportActionBar().hide();
       /* pdfurl = BASE_URL + "api/one-transaction-pdf/" + sttrnsid + "/" + AccountUtils.getAccessToken(this);
        Log.e("pdfurlpdfurlpdfurl", "" + pdfurl);
*/

        //new RetrivePDFfromUrl().execute(pdfurl);

        pdfd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downn = "download";
                if (!transme.equals("dvdsv")) {
                    filenameee = "Goldsikka" + sttrnsid + date + ".pdf";
                    pdfdownload(sttrnsid);
                } else {
                    filenameee = "Goldsikka" + positionid + date + ".pdf";
                    pdfdownload(positionid);
                }

                // downloadimage("Goldsikka", pdfurl);
            }
        });

        if (!transme.equals("dvdsv")) {
            filenameee = "Goldsikka" + sttrnsid + date + ".pdf";
            pdfdownload(sttrnsid);
        } else {
            filenameee = "Goldsikka" + positionid + date + ".pdf";
            pdfdownload(positionid);

        }
    }

    // create an async task class for loading pdf file from URL.
    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfview.fromStream(inputStream).load();
        }
    }

    public void downloadimage(String filename, String imageURL) {
        try {
            DownloadManager downloadManager = null;
            downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloaduri = Uri.parse(imageURL);
            DownloadManager.Request request = new DownloadManager.Request(downloaduri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("file/pdf")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator + filename + ".pdf");
            downloadManager.enqueue(request);
            Toast.makeText(this, "Image download done", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Image Downloading fail", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadZipFileTask = new DownloadZipFileTask();
                    downloadZipFileTask.execute(responseBody);

                } else {
                    Toast.makeText(this, "Permission was denied! ", Toast.LENGTH_SHORT).show();
                    finish();

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
            saveToDisk(urls[0], filenameee);
            return null;
        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {

            Log.d("API123", progress[0].second + " ");

            if (progress[0].first == 100)
                //Toast.makeText(getApplicationContext(),  Toast.LENGTH_SHORT).show();
                ToastMessage.onToast(ReceiptActivity.this, "File downloaded", ToastMessage.SUCCESS);
            if (progress[0].second > 0) {
                int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);
                progressBar.setProgress(currentProgress);

                txtProgressPercent.setText("Progress " + currentProgress + "%");

            }

            if (progress[0].first == -1) {
                ToastMessage.onToast(ReceiptActivity.this, "Download failed Try again ", ToastMessage.ERROR);

            }

        }

        public void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }

        @Override
        protected void onPostExecute(String result) {

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
            Call<ResponseBody> getpdfdownload;
            if (!transme.equals("dvdsv")) {
                getpdfdownload = apiDao.transactionpdfdownload("Bearer " + AccountUtils.getAccessToken(this), id);
            } else {
                getpdfdownload = apiDao.Schhemetransactionpdfdownload("Bearer " + AccountUtils.getAccessToken(this), id);

            }


            getpdfdownload.enqueue(new Callback<ResponseBody>() {

                @RequiresApi(api = Build.VERSION_CODES.M)
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

                                try {
                                    if (!NetworkUtils.isConnected(ReceiptActivity.this)) {
                                        ToastMessage.onToast(ReceiptActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                                    } else {
                                        if (downn.equals("download")) {
                                            createnotification();
                                            File fileeeee = new File(Environment.getExternalStorageDirectory() + "/" + "download" + "/" + filenameee);
                                            Uri apkURi = FileProvider.getUriForFile(ReceiptActivity.this, getPackageName() + ".provider", fileeeee);
                                            Intent inttt = new Intent(Intent.ACTION_VIEW).setDataAndType(apkURi, "application/pdf").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent resultIntent = PendingIntent.getActivity(ReceiptActivity.this, 1, inttt, PendingIntent.FLAG_UPDATE_CURRENT);

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
                                        } else {
                                            downloadZipFileTask = new DownloadZipFileTask();
                                            downloadZipFileTask.execute(response.body());
                                        }
                                    }

                                } catch (Exception e) {
                                    Log.e("TRY EXCEPT", "" + e);
                                }

                            }
                        } else {


                            try {
                                if (!NetworkUtils.isConnected(ReceiptActivity.this)) {
                                    ToastMessage.onToast(ReceiptActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                                } else {
                                    if (downn.equals("download")) {
                                        createnotification();
                                        File fileeeee = new File(Environment.getExternalStorageDirectory() + "/" + "download" + "/" + filenameee);
                                        Uri apkURi = FileProvider.getUriForFile(ReceiptActivity.this, getPackageName() + ".provider", fileeeee);
                                        Intent inttt = new Intent(Intent.ACTION_VIEW).setDataAndType(apkURi, "application/pdf").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent resultIntent = PendingIntent.getActivity(ReceiptActivity.this, 1, inttt, PendingIntent.FLAG_UPDATE_CURRENT);

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
                                    } else {
                                        downloadZipFileTask = new DownloadZipFileTask();
                                        downloadZipFileTask.execute(response.body());
                                    }
                                }

                            } catch (Exception e) {
                                Log.e("TRY EXCEPT", "" + e);
                            }

                        }


                    } else {
                        dialog.dismiss();
                     //   ToastMessage.onToast(ReceiptActivity.this, "Technical Issue Try after some time  ", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("onfail", t.toString());
                  //  ToastMessage.onToast(ReceiptActivity.this, "We have some issues Try after some time ", ToastMessage.ERROR);

                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createnotification() {
        CharSequence name = "Notification";
        String des = "This is my personal notification";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel notificationChannel = new NotificationChannel("101", name, importance);
        notificationChannel.setDescription(des);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);

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

                if (!downn.equals("download")) {
//                    pdfview.fromStream(inputStream);
                    pdfview.fromFile(destinationFile).load();
                } else {
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
