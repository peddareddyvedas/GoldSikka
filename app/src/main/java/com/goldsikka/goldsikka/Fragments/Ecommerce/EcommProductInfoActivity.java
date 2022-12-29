package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EcommProductInfoActivity extends AppCompatActivity {

    RecyclerView productlistRecyclerview;
    ProductinfoAdapter pinfoAdapter;
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
    ArrayList<Listmodel> getpinfoList;
    String pidid;
    int weight, cost, orginalpriceresult;
    String totalprice;
    ArrayList<Listmodel> pricefilteredlist;
    Bundle bundle;
    String titlename;

    String porderid = "99";

    ImageView brandimage;
    RelativeLayout notfound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
        bundle = getIntent().getExtras();
        porderid = bundle.getString("orderid");

        Log.e("ccc", "" + porderid);


        init();
        getEcomProductinfo();

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
        titleTv.setText("Products Info");
        notfound = findViewById(R.id.notfound);

        notxt = findViewById(R.id.notxt);
        lottieAnimationView = findViewById(R.id.wempty);
        productlistRecyclerview = findViewById(R.id.recyclerproductinfo);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        productlistRecyclerview.setHasFixedSize(true);
        getpinfoList = new ArrayList<>();
        productlistRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        pinfoAdapter = new ProductinfoAdapter(getpinfoList);
        productlistRecyclerview.setAdapter(pinfoAdapter);

        brandimage = findViewById(R.id.brandimage);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getEcomProductinfo() {
        getpinfoList.clear();
        apiDao = ApiClient.getClient("Bearer " + AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<List<Listmodel>> getProduct = apiDao.getecommproductinfo("Bearer " + AccountUtils.getAccessToken(this), porderid);
        getProduct.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("proinfostatuscode", String.valueOf(statuscode));
                flist = response.body();
                Log.e("flist", "" + flist);


                if (statuscode == 200 || statuscode == 202) {
                    Log.e("infostatuscode", String.valueOf(statuscode));
                    for (Listmodel listmodel : flist) {
                        Log.e("orderlist", "" + flist.size());

                        Log.e("catname", "enter2");
                        Log.e("jcjnnnnn", "" + listmodel.getPname());
                        getpinfoList.add(listmodel);
                        pinfoAdapter.notifyDataSetChanged();

                       /* if (listmodel.getGold_type().equals("0")) {
                            getpinfoList.remove(listmodel);

                        }else{
                            getpinfoList.add(listmodel);
                            pinfoAdapter.notifyDataSetChanged();

                        }*/


                    }

                } else if (statuscode == 400) {
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

    //// ecombrands main adapter/////
    private class ProductinfoAdapter extends RecyclerView.Adapter<ProductinfoAdapter.ViewHolder> {
        private ArrayList<Listmodel> wisList;

        public ProductinfoAdapter(ArrayList<Listmodel> moviesList) {
            this.wisList = moviesList;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ecomm_item_productinfo, viewGroup, false);
            return new ViewHolder(itemView);
        }


        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
            Log.e("itemName", " " + position);
            Listmodel w = wisList.get(position);
            viewHolder.text_subname.setText(w.getSubcatname());
            viewHolder.text_pname.setText(w.getPname());
            String pimg = w.getImage_uri();

            try {
                Glide.with(getApplicationContext()).load(pimg).into(viewHolder.img);
            } catch (Exception ignored) {
                Glide.with(getApplicationContext()).load(R.drawable.background_image).into(viewHolder.img);
            }

            if (selectedPosition == position) {
                Log.e("ifnews", "called");
            } else {
                Log.e("elsenews", "called");

            }
        }

        @Override
        public int getItemCount() {
            Log.e("sizeeeeee", "" + wisList.size());
            if (wisList.size() > 0) {
                notfound.setVisibility(View.GONE);
                return wisList.size();
            } else {
                notfound.setVisibility(View.VISIBLE);
                return 0;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView text_subname, text_pname;
            ImageView img;
            String pid = "24";

            CardView cardunderproduct;

            @SuppressLint("SetTextI18n")
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                text_subname = itemView.findViewById(R.id.text_subname);
                text_pname = itemView.findViewById(R.id.text_pname);
                img = itemView.findViewById(R.id.img);
            }
        }

    }

}
