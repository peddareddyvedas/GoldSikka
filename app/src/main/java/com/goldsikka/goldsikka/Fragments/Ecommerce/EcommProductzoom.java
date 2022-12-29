package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EcommProductzoom extends AppCompatActivity {
    ScaleGestureDetector scaleGestureDetector;
    float scalefactor = 1.0f;
    ImageView pimg;
    ApiDao apiDao;
    ArrayList<Listmodel> productszoomList;
    List<Listmodel> flist;
    Bundle bundle;
    String pid = "74";
    TextView unameTv, uidTv, titleTv, pname, pva, pweight, pcatid;
    RelativeLayout backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecommproduct_zoom);
        bundle = getIntent().getExtras();
        pid = bundle.getString("pid");
        Log.e("pid", "" + pid);
        init();
        getProduct();

    }

    public void init() {
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        pimg = findViewById(R.id.pinch_image_view);
        titleTv.setText("Product");
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void getProduct() {
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<List<Listmodel>> getProduct = apiDao.getproductid(pid);
        getProduct.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("zoomstatuscode", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("statuscode", String.valueOf(statuscode));
                    if (flist != null) {
                        for (Listmodel listmodel : flist) {
                            String fpimg = listmodel.getImage_uri();

                            try {
                                Glide.with(getApplicationContext()).asBitmap().load(fpimg).into(pimg);

                            } catch (Exception ignored) {
                                Glide.with(getApplicationContext()).load(R.drawable.background_image).into(pimg);
                            }
                        }
                    } else {
                        Log.e("catname", "No cats");
                    }
                } else if (statuscode == 422) {
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //ActivityCompat.finishAfterTransition(this);
        finish();
    }


}



