package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.goldsikka.goldsikka.Adapter.PageviewAdapter;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class prductinfo_fragment  extends Fragment implements View.OnClickListener {

    TextView tv_itemcount,tv_itemquntity, tv_productname,tv_productgrams,tv_productprice,tv_amntvalue,tv_vavalue,tv_gstvalue,tv_fnalamntvalue,tv_desc;

    Button btn_addcart,btn_cardview;
    ImageView iv_remove,iv_add;

    LinearLayout ll_incrdecr,ll_addcart;
    String productid,subcatagory;
    ApiDao apiDao;
    private Activity activity;
    ImageView iv_cart;
    String banners;
    CirclePageIndicator indicator;
    private static ViewPager mPager;
    private static int currentPage ;
    private static int NUM_PAGES;
    private ArrayList<String> urls = new ArrayList<>();
    TextView  tv_item;


    String product_quntity, st_productname,st_productgrams,st_amntvalue,st_vavalue,st_gstvalue,st_fnalamntvalue,st_desc;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.productinfo,container,false);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        productid = AccountUtils.getProductID(activity);


        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Product Details");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(activity.getColor(R.color.colorWhite));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle1 = new Bundle();
                bundle1.putString("ecoms","productinfo");;
                bundle1.putString("subcategoryId", subcatagory);
                Ecommerce_Productlist frgment = new Ecommerce_Productlist();
                frgment.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, frgment).commit();
            }
        });
        iv_cart = view.findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(this);

        intilizeview(view);
        getdata();
      //  open_additem();
        return view;
    }
    public void intilizeview(View view){

     indicator = view.findViewById(R.id.indicator);
        tv_itemcount = view.findViewById(R.id.tv_itemcount);

        String noofproducts = AccountUtils.getProductQuantity(activity);
        if (noofproducts.equals("0")||noofproducts == null){
            tv_itemcount.setVisibility(View.GONE);
        }else {
            tv_itemcount.setText(noofproducts);
        }



        tv_itemquntity = view.findViewById(R.id.tv_itemquntity);
        tv_productname = view.findViewById(R.id.tv_productname);
        tv_productgrams = view.findViewById(R.id.tv_productgrams);
        tv_productprice = view.findViewById(R.id.tv_productprice);
        tv_amntvalue = view.findViewById(R.id.tv_amntvalue);
        tv_vavalue = view.findViewById(R.id.tv_vavalue);
        tv_gstvalue = view.findViewById(R.id.tv_gstvalue);
        tv_fnalamntvalue = view.findViewById(R.id.tv_fnalamntvalue);
        tv_desc = view.findViewById(R.id.tv_desc);

        btn_addcart = view.findViewById(R.id.btn_addcart);
        btn_addcart.setOnClickListener(this);

        btn_cardview = view.findViewById(R.id.btn_cardview);
        btn_cardview.setOnClickListener(this);

        iv_remove = view.findViewById(R.id.iv_remove);
        iv_remove.setOnClickListener(this);

        iv_add = view.findViewById(R.id.iv_add);
        iv_add.setOnClickListener(this);

        ll_addcart = view.findViewById(R.id.ll_addcart);
        ll_incrdecr = view.findViewById(R.id.ll_incrdecr);
    }
    public void getdata(){
        apiDao = ApiClient.getClient("").create(ApiDao.class);

        Call<Listmodel> call = apiDao.get_prod_info(productid);
        call.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                if(statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK){
                    Listmodel listModel = response.body();


                    st_desc = listModel.getDescription();
                    st_productgrams = listModel.getGrams();

                    JsonObject amount = new JsonParser().parse(listModel.getAmountDetails().toString()).getAsJsonObject();

                    JsonObject productname = new JsonParser().parse(listModel.getCategory_name().toString()).getAsJsonObject();

                    Log.e("jads",amount.toString());
                    try{
                        JSONObject object = new JSONObject(amount.toString());
                        JSONObject object1 = new JSONObject(productname.toString());
                    st_amntvalue = object.getString("amount");
                    st_vavalue = object.getString("vaAmount");
                    st_gstvalue = object.getString("gstAmount");
                    st_fnalamntvalue = object.getString("finalAmount");
                        st_productname = object1.getString("name");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(listModel));
                        JSONArray array = jsonObject.getJSONArray("product_image");
                        for (int i= 0;i<array.length();i++){
                            JSONObject object = array.getJSONObject(i);
                            listModel.setProduct_uri(object.getString("product_uri"));
                            banners = listModel.getProduct_uri();
                            Log.e("proimg",banners);
                            init();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    set_details();
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
              //  Toast.makeText(activity, "Technical problem2", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void init() {

        String[] image = new String[]{banners};
        for(int i = 0; i < image.length;i++){
            Log.e("bannerurl", String.valueOf(urls));
            urls.add(image[i]);
        }

        mPager.setAdapter(new PageviewAdapter(activity, urls));

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(4 * density);
        NUM_PAGES =  urls.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = NUM_PAGES;
                    mPager.setCurrentItem(currentPage++, true);
                }

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000,3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }
    public void set_details(){

        tv_amntvalue .setText(st_amntvalue);
        tv_desc.setText(st_desc);
        tv_vavalue.setText(st_vavalue);
        tv_fnalamntvalue.setText(st_fnalamntvalue);
        tv_gstvalue.setText(st_gstvalue);
        tv_productgrams.setText("Grams  : "+st_productgrams);
        tv_productprice.setText("Amount  : "+getString(R.string.Rs)+st_fnalamntvalue);
        tv_productname.setText(st_productname);


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_cart:
                opencart();
                break;
            case R.id.btn_addcart:
                ll_addcart.setVisibility(View.GONE);
                ll_incrdecr.setVisibility(View.VISIBLE);
                open_additem();
                break;
            case R.id.iv_add:
                open_additem();
                break;
            case  R.id.iv_remove:
                open_itemremove();
                break;
            case R.id.btn_cardview:
                opencart();
                
                break;

        }

    }
    public void open_additem(){
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

        Call<Listmodel> call = apiDao.addproduct("Bearer " + AccountUtils.getAccessToken(activity), productid);
        call.enqueue(new Callback<Listmodel>() {


            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {

                int statuscode = response.code();
                List<Listmodel> list = Collections.singletonList(response.body());
                if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {
                    for (Listmodel listmodel: list) {
                        product_quntity = listmodel.getQuantity();
                        if (product_quntity.equals("0")){
                            ll_addcart.setVisibility(View.VISIBLE);
                        }else {
                            ll_incrdecr.setVisibility(View.VISIBLE);
                            ll_addcart.setVisibility(View.GONE);
                            tv_itemquntity.setText(product_quntity);
                            tv_itemcount.setVisibility(View.VISIBLE);
                            tv_itemcount.setText(listmodel.getProductCount());
                            Log.e("quntity",product_quntity);
                        }

                    }


                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
            //    ToastMessage.onToast(activity, "we have some issues", ToastMessage.ERROR);
            }
        });
    }
    public void open_itemremove(){

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
        Call<Listmodel> call = apiDao.removeproduct("Bearer " + AccountUtils.getAccessToken(activity), productid);
        call.enqueue(new Callback<Listmodel>() {


            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {

                int statuscode = response.code();
                List<Listmodel> list = Collections.singletonList(response.body());
                if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {
                    for (Listmodel listmodel: list) {
                        product_quntity = listmodel.getQuantity();
                        tv_itemquntity.setText(product_quntity);
                        Log.e("quntity",product_quntity);
                        if (product_quntity.equals("0")){
                            ll_addcart.setVisibility(View.VISIBLE);
                            ll_incrdecr.setVisibility(View.GONE);
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
              //  ToastMessage.onToast(activity, "we have some issues", ToastMessage.ERROR);
            }
        });
    }
    
    public  void opencart(){

        Bundle bundle = new Bundle();
        bundle.putString("from","productinfo");
        Cartlist frgment = new Cartlist();
        frgment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.frame_layout, frgment).commit();
    }
}
