package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EcommBrandListActivity extends AppCompatActivity {

    RecyclerView brandlistRecyclerview;
    EcommbrandlistAdapter ebrandsAdapter;
    TextView unameTv, uidTv, titleTv, validprice;
    RelativeLayout backbtn;
    shared_preference sharedPreference;
    int selectedPosition = -1;
    //  private List<Listmodel> wislistList = new ArrayList<>();
    private Handler hdlr = new Handler();
    private int i = 0;

    List<Listmodel> flist;

    ApiDao apiDao;

    LottieAnimationView lottieAnimationView;
    TextView notxt;

    SwipeRefreshLayout swipe_layout;
    ArrayList<Listmodel> getbradList;
    String pidid;
    int weight, cost, orginalpriceresult;
    String totalprice;
    ArrayList<Listmodel> pricefilteredlist;
    Bundle bundle;
    String titlename;
    String brandid = "99";
    String brandimagel;

    ImageView brandimage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecomm_brandactivity);
        bundle = getIntent().getExtras();
        brandid = bundle.getString("brandid");
        titlename = bundle.getString("titleBname");
        brandimagel = bundle.getString("brandimage");

        Log.e("brandimagel", "" + brandimagel);

        init();
        onrefresh();
        // getbrandsImage();
        postbrandsImage(brandid);
    }


    public void onrefresh() {
        swipe_layout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipe_layout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(EcommBrandListActivity.this)) {
                            ToastMessage.onToast(EcommBrandListActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            swipe_layout.setRefreshing(false);
                            return;
                        } else {
                            init();
                            onrefresh();
                           // getbrandsImage();
                            postbrandsImage(brandid);
                        }
                        swipe_layout.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }


    public void init() {
        sharedPreference = new shared_preference(getApplicationContext());
        swipe_layout = findViewById(R.id.swipe_layout);
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText(titlename);
        notxt = findViewById(R.id.notxt);
        lottieAnimationView = findViewById(R.id.wempty);
        brandlistRecyclerview = findViewById(R.id.recyclerbrand);
        brandlistRecyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        getbradList = new ArrayList<>();
        ebrandsAdapter = new EcommbrandlistAdapter(getbradList);
        brandlistRecyclerview.setHasFixedSize(false);
        brandlistRecyclerview.setAdapter(ebrandsAdapter);
        brandimage = findViewById(R.id.brandimage);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        try {
            Glide.with(getApplicationContext()).load(brandimagel).into(brandimage);
        } catch (Exception ignored) {
            Glide.with(getApplicationContext()).load(R.drawable.background_image).into(brandimage);
        }
    }


    private void getbrandsImage() {

        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<List<Listmodel>> getproductslist = apiDao.getEcomBrands();
        //  Call<List<Listmodel>> getproductslist = apiDao.getEcomBrands("Bearer " + AccountUtils.getAccessToken(this), prceeee);

        getproductslist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("ccccccstatuscode", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("cccccstatuscode", String.valueOf(statuscode));
                    if (flist != null) {
                        for (Listmodel listmodel : flist) {
                            String fpimg = listmodel.getImage_uri();
                            Log.e("fpimg", "" + listmodel.getImage_uri());


                            Log.e("iddddffd", "" + listmodel.getId());

                        }
                    } else {
                        Log.e("catname", "No Products");
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

    private void postbrandsImage(String brandid) {
        getbradList.clear();
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        // Call<List<Listmodel>> getproductslist = apiDao.getEcomBrands();
        Call<List<Listmodel>> getproductslist = apiDao.postEcomBrands("Bearer " + AccountUtils.getAccessToken(this), brandid);

        getproductslist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("ccccccstatuscode", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 202) {
                    Log.e("cccccstatuscode", String.valueOf(statuscode));
                    if (flist != null) {
                        getbradList.clear();
                        Collections.shuffle(flist);
                        for (Listmodel listmodel : flist) {
                            if (listmodel.getPname().equals("Gift Card")) {
                                getbradList.remove(listmodel);
                            }else{
                                getbradList.add(listmodel);
                                ebrandsAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Log.e("catname", "No Products");
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
        // NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        // finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        init();
        onrefresh();
        getbrandsImage();
        postbrandsImage(brandid);
    }

    //// ecombrands main adapter/////
    private class EcommbrandlistAdapter extends RecyclerView.Adapter<EcommbrandlistAdapter.ViewHolder> {
        private ArrayList<Listmodel> wisList;

        public EcommbrandlistAdapter(ArrayList<Listmodel> moviesList) {
            this.wisList = moviesList;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ecommbrandslist, viewGroup, false);
            return new ViewHolder(itemView);
        }


        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
            Log.e("itemName", " " + position);
            Listmodel w = wisList.get(position);
            viewHolder.pnameTv.setText(w.getPname());
            viewHolder.pweightTv.setText(w.getWeight() + " gms");
            String pimg = w.getImage_uri();
            viewHolder.pid = w.getId();
            Log.e("pidddddd", "" + w.getPids());
            /*double asdf2 = Double.parseDouble(w.getPrice());
            int i = (int) asdf2;*/
            viewHolder.productprice.setText("₹" + w.getPrice());


            try {
                Glide.with(getApplicationContext()).load(pimg).into(viewHolder.pimg);
            } catch (Exception ignored) {
                Glide.with(getApplicationContext()).load(R.drawable.background_image).into(viewHolder.pimg);
            }

            if (selectedPosition == position) {
                Log.e("ifnews", "called");
            } else {
                Log.e("elsenews", "called");

            }


            viewHolder.cardunderproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Ecommproductdiscreption.class);
                    intent.putExtra("id", viewHolder.pid);
                    Log.e("id", "" + viewHolder.pid);
                    startActivity(intent);
                }
            });
           /* viewHolder.cardunderproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   *//* Intent intent = new Intent(getApplicationContext(), Ecommproductdiscreption.class);
                    intent.putExtra("id", viewHolder.pid);
                    Log.e("id",""+ viewHolder.pid);
                    startActivity(intent);*//*
                }
            });*/

        }


        @Override
        public int getItemCount() {

            return wisList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView pnameTv, pweightTv, productprice;
            ImageView pimg;
            String pid = "24";

            CardView cardunderproduct;

            @SuppressLint("SetTextI18n")
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                pnameTv = itemView.findViewById(R.id.newarrivalname);
                pweightTv = itemView.findViewById(R.id.price);
                pimg = itemView.findViewById(R.id.newarrivalimg);
                productprice = itemView.findViewById(R.id.productprice);
                cardunderproduct = itemView.findViewById(R.id.cardunderproduct);
            }
        }

    }


}
