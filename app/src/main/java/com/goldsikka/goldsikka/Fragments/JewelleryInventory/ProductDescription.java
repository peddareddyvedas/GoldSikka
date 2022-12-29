package com.goldsikka.goldsikka.Fragments.JewelleryInventory;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Fragments.JewelleryInventory.Adapters.AllProductsAdapter;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDescription extends AppCompatActivity {
    TextView unameTv, uidTv, titleTv, pname, pva, pweight, pcatid, pidTv;
    RelativeLayout backbtn;
    ImageView pimg;
    shared_preference sharedPreference;
    LinearLayout llproductdetails, llproductrating;
    RelativeLayout rldetails, rlreviews;
    Button wishlist;
    Bundle bundle;
    String productimage, productname;
    ImageView proimage;
    CheckBox selectwhislist;
    TextView protext;

    ApiDao apiDao;
    String pid = "74";
    List<Listmodel> flist;
    Listmodel pflist;
    AllProductsAdapter allProductsAdapter;

    List<Listmodel> flistwish = new ArrayList<>();
    List<String> checklist = new ArrayList<String>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityproductdiscreption_new);

        // setContentView(R.layout.activity_jewellery_productdescription);
        bundle = getIntent().getExtras();
        pid = bundle.getString("pid");

        Log.e("pid", "" + pid);
        init();
        getWishlistCount();
        getProduct();

    }


    public void getWishlistCount() {
        checklist.clear();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<List<Listmodel>> getWishlist = apiDao.getWishlist("Bearer "+AccountUtils.getAccessToken(this));
        getWishlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("statuscode", String.valueOf(statuscode));
//                assert response.body() != null;
//                allProductsAdapter.setFlistwish(response.body());
                flistwish = response.body();
                if(flistwish!=null) {
                    checklist.clear();
                    for (Listmodel list : flistwish) {
                        checklist.add(list.getPids());
                    }
                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("sdfsd", "dvd");
                    } else if (statuscode == 422) {
                        Log.e("cv", String.valueOf(statuscode));
                    } else {
                        Log.e("fgd", "sdfsd");
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
            }
        });
}

    public void init() {
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Product Description");

        wishlist = (Button) findViewById(R.id.wishlist);
        backbtn = findViewById(R.id.backbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AccountUtils.setLoadify(ProductDescription.this, "mload");
                if(wishlist.getText().toString().trim().equals("Add to wishlist")){
                    postwishlist();

                }else{
                    startActivity(new Intent(ProductDescription.this, Wishlist_Activity.class));
                }
               // startActivity(new Intent(getApplicationContext(), Wishlist_Activity.class));
            }
        });

        pname = findViewById(R.id.pnametv);
        pva = findViewById(R.id.pvaTv);
        pweight = findViewById(R.id.pweightTv);
        pimg = findViewById(R.id.pimg);
        pidTv = findViewById(R.id.pidTv);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
        getWishlistCount();
        getProduct();
    }

    public void postwishlist() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Listmodel> get_jewwproductlist = apiDao.postwishlist("Bearer "+AccountUtils.getAccessToken(getApplicationContext()), pid);
        get_jewwproductlist.enqueue(new Callback<Listmodel>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<Listmodel> call, @NotNull Response<Listmodel> response) {
                int statuscode = response.code();
                Log.e("poststatuscode", String.valueOf(statuscode));
                Log.e("poststatuscodeerror", String.valueOf(response));

                 pflist = response.body();
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                    startActivity(new Intent(getApplicationContext(), Wishlist_Activity.class));
                     Log.e("sdffg", pflist.getPids());
                     Log.e("sdffg", pid);

                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    dialog.dismiss();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("ughb", String.valueOf(t));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        getWishlistCount();
        getProduct();
    }

    public void getProduct() {
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<List<Listmodel>> getProduct = apiDao.getProductById(pid);
        getProduct.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("statuscode", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("statuscode", String.valueOf(statuscode));
                    if (flist != null) {
                        for (Listmodel listmodel : flist) {
                            pname.setText(listmodel.getPname());
                            pva.setText("VA         :  " + listmodel.getPva() + " %");
                            pweight.setText("Weight  :  " + listmodel.getPweight() + " grams");
                            pidTv.setText("Id           :  " + listmodel.getId());
                            String fpimg = listmodel.getPimg();

                            try {
                                Glide.with(getApplicationContext()).load(fpimg).into(pimg);
                            } catch (Exception ignored) {
                                Glide.with(getApplicationContext()).load(R.drawable.background_image).into(pimg);
                            }

                            boolean checkwish = checklist.contains(pid);

                            if (checkwish) {
            Log.e("checklist", String.valueOf(checklist));
                               wishlist.setText("Added in wishlist");
                            } else {
            Log.e("checklist", String.valueOf(checklist));
                                wishlist.setText("Add to wishlist");

                            }

                        }
                    } else {
                        Log.e("catname", "No cats");
                    }
//                    openpopupscreen(listmodel.getDescription());
                } else if (statuscode == 422) {
                    Log.e("cv", String.valueOf(statuscode));
//                        ToastMessage.onToast(Elevenplus_Jewellery.this, String.valueOf(statuscode), ToastMessage.ERROR);
                } else {
//                    ToastMessage.onToast(Elevenplus_Jewellery.this, "Please try again", ToastMessage.ERROR);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
//                openpopupscreen("Successfully subscribed to Gold Plus Plan");
            }
        });

    }

    @Override
    public void onBackPressed() {
//        NavUtils.navigateUpFromSameTask(this);
        if(AccountUtils.getLoadify(ProductDescription.this).equals("mload")){
            ActivityCompat.finishAfterTransition(this);
        }else{
            AccountUtils.setLoadify(ProductDescription.this, "unload");
            ActivityCompat.finishAfterTransition(this);
        }
        super.onBackPressed();

    }
}
