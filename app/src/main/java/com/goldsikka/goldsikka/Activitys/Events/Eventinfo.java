package com.goldsikka.goldsikka.Activitys.Events;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
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

public class Eventinfo extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvdate)
    TextView tvdate;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tveventname)
    TextView tveventname;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tveventid)
    TextView tveventid;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivqrcode)
    ImageView ivqrcode;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llscreen)
    LinearLayout llscreen;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btshare)
    Button btshare;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.loading_gif)
//    GifImageView loading_gif;
    ApiDao apiDao;
    String stqrid,steventdate,steventname,steventid;

    TextView unameTv, uidTv, titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventinfo);

        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            stqrid = bundle.getString("qrid");
            steventdate = bundle.getString("eventdate");
            steventid = bundle.getString("eventid");
            steventname = bundle.getString("eventname");

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Events");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Events");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        getqrcode();
    }
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        // finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }
    public void getqrcode(){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<ResponseBody> getqrcode = apiDao.GetqrCode("Bearer "+AccountUtils.getAccessToken(this),stqrid);
            getqrcode.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int statuscode = response.code();
                    Log.e("statuscode", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        assert response.body() != null;
                        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        ivqrcode.setImageBitmap(bitmap);
                        setdetails();
                    }else {
                        dialog.dismiss();
                    //    ToastMessage.onToast(Eventinfo.this,"Technical issue please try after some time",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                  //  ToastMessage.onToast(Eventinfo.this,"We have some issue please try after some time",ToastMessage.ERROR);
                }
            });

        }
    }
    public void setdetails(){
        tvdate.setText("Eventdate : "+steventdate);
        tveventid.setText(steventid);
        tveventname.setText(steventname);

    }

    @OnClick(R.id.btshare)

    public void shareevent(View v){
        //loading_gif.setVisibility(View.VISIBLE);
        btshare.setVisibility(View.GONE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Eventinfo.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      //  loading_gif.setVisibility(View.GONE);
                        try {

                            Bitmap bitmap = share(llscreen);
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(Eventinfo.this, bitmap));
                            shareIntent.setType("image/jpeg");
                            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.app_name)));

                        }catch (Exception e){
                            e.getMessage();
                            Log.e("csd",e.toString());
                        }
                        btshare.setVisibility(View.VISIBLE);
                       // loading_gif.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);


    }

    public Bitmap share(View v){
        int height = v.getHeight();
        int width = v.getWidth();
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas (b);
        v.layout(0, 0 , v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }
    String path;
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            path    = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                    inImage, "IMG_" + Calendar.getInstance().getTime(), "");
            return Uri.parse(path);
        }catch (Exception e){
            e.getMessage();
            Log.e("e.getMessage();",e.getMessage());
        }
        return null;
    }
}