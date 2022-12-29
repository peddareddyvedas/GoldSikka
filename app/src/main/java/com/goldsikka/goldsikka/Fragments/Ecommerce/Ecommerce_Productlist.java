
package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.data;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ecommerce_Productlist extends Fragment implements OnItemClickListener, View.OnClickListener {


    ApiDao apiDao;
    private Activity activity;
    RecyclerView rv_productlist;
    String product_quntity,productcount;
    TextView  tv_item;

    Product_List_Adpter adapter;
    ArrayList<Listmodel> arrayList;

    String subcategoryid;

    ImageView iv_cart;
    LinearLayout linearlayout;
    RelativeLayout rllayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ecommerce_productlist, container, false);

        linearlayout = view.findViewById(R.id.linearlayout);
        rllayout = view.findViewById(R.id.rllayout);

        subcategoryid = AccountUtils.getSubCategoryId(activity);
        Log.e("subcategoryid",subcategoryid);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Ecommerce");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(activity.getColor(R.color.colorWhite));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Subcategorylist frgment = new Subcategorylist();

                getFragmentManager().beginTransaction().replace(R.id.frame_layout, frgment).commit();
            }
        });
        iv_cart = view.findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(this);
        tv_item = view.findViewById(R.id.tv_itemcount);
        intilizerecyclerview(view);
        getdata();

        return view;
    }

    public void getdata() {
        arrayList.clear();
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

        Call<data> getprodulist = apiDao.getproductlist(subcategoryid);

        getprodulist.enqueue(new Callback<data>() {
            @Override
            public void onResponse(Call<data> call, Response<data> response) {
                int statuscode = response.code();
                List<Listmodel> list = response.body().getResult();

                dialog.dismiss();
                if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {
                    if (list.size()!= 0){
                        rllayout.setVisibility(View.VISIBLE);
                        linearlayout.setVisibility(View.GONE);
                        for (Listmodel listmodel : list) {
                            dialog.dismiss();

                            try {
                                JSONObject object = new JSONObject(new Gson().toJson(listmodel));
                                listmodel.setId(object.getString("id"));
                                JSONArray array = object.getJSONArray("product_image");

                                for (int i =0;i<array.length();i++){
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    listmodel.setProduct_uri(jsonObject.getString("product_uri"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            arrayList.add(listmodel);
                            adapter.notifyDataSetChanged();
                      }
                    }else {
                        linearlayout.setVisibility(View.VISIBLE);
                        rllayout.setVisibility(View.GONE);
                    }

                } else {
                     dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<data> call, Throwable t) {
                  dialog.dismiss();
                Log.e("fails", t.toString());
                ToastMessage.onToast(activity, "we have some issues", ToastMessage.ERROR);
            }
        });

    }


    public void intilizerecyclerview(View view) {

        rv_productlist = view.findViewById(R.id.rv_productlist);
        rv_productlist.setHasFixedSize(true);
        rv_productlist.setLayoutManager(new GridLayoutManager(activity, 2));
        rv_productlist.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(activity, GridLayoutManager.VERTICAL);
        rv_productlist.addItemDecoration(decoration);
        arrayList = new ArrayList<>();
        adapter = new Product_List_Adpter(activity, arrayList, this);
        rv_productlist.setAdapter(adapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemClick(View view, int position) {

        Listmodel listmodel = arrayList.get(position);
//        switch (view.getId()) {
//            case R.id.iv_product:
//                openproductinfo(listmodel.getId());
//                break;
//            case R.id.iv_add:
//                initadditem(listmodel.getId());
//
//                break;
//            case R.id.iv_remove:
//                initremoveitem(listmodel.getId());
//                break;
//            case R.id.btn_additem:
//                initadditem(listmodel.getId());
//                break;
//        }
    }

    public void openproductinfo(String id) {

        AccountUtils.setProductID(activity,id);

        prductinfo_fragment fragment = new prductinfo_fragment();
        getFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();

    }
    public void initremoveitem(String id, Product_List_Adpter.ViewHolder holder){
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

        Call<Listmodel> call = apiDao.removeproduct("Bearer "+AccountUtils.getAccessToken(activity), id);
        call.enqueue(new Callback<Listmodel>() {

            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {

                int statuscode = response.code();
                List<Listmodel> list = Collections.singletonList(response.body());
                if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {
                    for (Listmodel listmodel: list) {
                        productcount = listmodel.getProductCount();

                        if (productcount.equals("0")){
                            tv_item.setVisibility(View.GONE);
                            AccountUtils.setProductQuantity(activity,productcount);
                            Log.e("productcount",productcount);
                        }
                        else {
                            tv_item.setVisibility(View.VISIBLE);
                            tv_item.setText(productcount);
                            AccountUtils.setProductQuantity(activity,productcount);
                            Log.e("productcount",productcount);
                        }
                        product_quntity = listmodel.getQuantity();
                        Log.e("quntity",product_quntity);
                        holder.tv_count.setText(product_quntity);

                    }

                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                Log.e("itemremove",t.toString());
                ToastMessage.onToast(activity, "we have some issues", ToastMessage.ERROR);
            }
        });
    }

    public void initadditem(String id, Product_List_Adpter.ViewHolder holder) {

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

        Call<Listmodel> call = apiDao.addproduct("Bearer "+AccountUtils.getAccessToken(activity), id);
        call.enqueue(new Callback<Listmodel>() {


            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {

                int statuscode = response.code();

                Listmodel listmodel =response.body();
                if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {
                  //  for (Listmodel listmodel: list) {


                        product_quntity = listmodel.getQuantity();

                        Log.e("quntity",product_quntity);
                        holder.tv_count.setText(product_quntity);



                        productcount = listmodel.getProductCount();

                        if (productcount.equals("0")){
                            tv_item.setVisibility(View.GONE);
                            AccountUtils.setProductQuantity(activity,productcount);
                        }
                        else {
                            tv_item.setVisibility(View.VISIBLE);
                            tv_item.setText(productcount);
                            AccountUtils.setProductQuantity(activity,productcount);
                        }


                   // }
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {

                Log.e("additem",t.toString());
                ToastMessage.onToast(activity, "we have some issues", ToastMessage.ERROR);
            }
        });

    }
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.iv_cart) {

            Bundle bundle = new Bundle();
            bundle.putString("from","productlist");
            Cartlist frgment = new Cartlist();
            frgment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frame_layout, frgment).commit();
        }
    }

    class Product_List_Adpter extends RecyclerView.Adapter<Product_List_Adpter.ViewHolder> {


        private Context context;
        ArrayList<Listmodel> list;
        OnItemClickListener itemClickListener;

        public Product_List_Adpter(Context context, ArrayList<Listmodel> list, OnItemClickListener itemClickListener) {

            this.context = context;
            this.list = list;
            this.itemClickListener = itemClickListener;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.ec_productlist, parent, false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Listmodel listmodel = list.get(position);

//
            JsonObject from = new JsonParser().parse(listmodel.getAmountDetails().toString()).getAsJsonObject();
            JsonObject productname = new JsonParser().parse(listmodel.getCategory_name().toString()).getAsJsonObject();
            Log.e("fromhdsda", from.toString());

            try {
                JSONObject json_from = new JSONObject(from.toString());
                String finalAmount = json_from.getString("finalAmount");
                Log.e("final amvsdiv", finalAmount);
                holder.tv_amount.setText(": "+finalAmount);

                JSONObject name = new JSONObject(productname.toString());
                String itemname = name.getString("name");
                holder.tv_poroductname.setText(itemname);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(new Gson().toJson(listmodel));
                JSONArray jsonArray = jsonObject.getJSONArray("product_image");
                for (int i =0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    listmodel.setProduct_uri(object.getString("product_uri"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            holder.tv_pay_grams.setText(" : "+listmodel.getGrams());
         //  holder.tv_count.setText(AccountUtils.getProductQuantity(activity));

            Picasso.with(context)
                    .load(listmodel.getProduct_uri())
                    .into(holder.iv_product);

////
//            holder.bt_add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    initadditem(listmodel.getId());
//                    holder.tv_count.setText(product_quntity);
//                    holder.ll_addremoves.setVisibility(View.VISIBLE);
//                    holder.bt_add.setVisibility(View.GONE);
//                }
//            });

            holder.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    initadditem(listmodel.getId(),holder);
                   //

                }
            });
            holder.iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    initremoveitem(listmodel.getId(),holder);

//                    if (product_quntity.equals("0")){
//                        holder.ll_addremoves.setVisibility(View.GONE);
//                       // holder.bt_add.setVisibility(View.VISIBLE);
//                    }
                   // holder.tv_count.setText(product_quntity);
                }
            });
            holder.iv_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openproductinfo(listmodel.getId());

                }
            });

        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            CardView cd_products;
            ImageView iv_product;
            TextView tv_poroductname, tv_pay_grams, tv_amount;
            TextView tv_count;
            Button bt_add;
            ImageView iv_add, iv_remove;

            LinearLayout ll_additem, ll_addremoves;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_count = itemView.findViewById(R.id.tv_count);

                bt_add = itemView.findViewById(R.id.btn_additem);
                bt_add.setOnClickListener(this);
                iv_add = itemView.findViewById(R.id.iv_add);
                iv_add.setOnClickListener(this);
                iv_remove = itemView.findViewById(R.id.iv_remove);
                iv_remove.setOnClickListener(this);

                ll_additem = itemView.findViewById(R.id.ll_additem);
                ll_addremoves = itemView.findViewById(R.id.ll_addremoves);

                cd_products = itemView.findViewById(R.id.cd_products);

                tv_poroductname = itemView.findViewById(R.id.tv_poroductname);
                tv_pay_grams = itemView.findViewById(R.id.tv_pay_grams);
                tv_amount = itemView.findViewById(R.id.tv_price);
                iv_product = itemView.findViewById(R.id.iv_product);
                iv_product.setOnClickListener(this);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, getAdapterPosition());

            }
        }

    }
}

