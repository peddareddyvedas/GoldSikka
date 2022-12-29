package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Ecommerce_ModelClass.Ecommerce_DataClass;
import com.goldsikka.goldsikka.model.Ecommerce_ModelClass.Ecommerce_ModelClass;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;

public class orderslist extends AppCompatActivity {

    RecyclerView rv_orderproductitem;

    ArrayList<Ecommerce_ModelClass> arrayList;
    orderlist_adapter adapter;
    String name;
    ApiDao apiDao;

    LinearLayout linearlayout,ll_cardlist;
    Listmodel listModel;

    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.orderlist);
        linearlayout = findViewById(R.id.linearlayout);
        ll_cardlist = findViewById(R.id.ll_cardlist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Your Orders");
        toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        intilizerecyclerview();
        getdata();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void getdata(){
        arrayList.clear();
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

        Call<Ecommerce_DataClass> call = apiDao.order_list("Bearer "+AccountUtils.getAccessToken(this));
        call.enqueue(new Callback<Ecommerce_DataClass>() {
            @Override
            public void onResponse(@NonNull Call<Ecommerce_DataClass> call, @NonNull retrofit2.Response<Ecommerce_DataClass> response) {
                int statuscode = response.code();
                if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {

                   List<Ecommerce_ModelClass> list  = response.body().getResult();
                    Log.e("response", list.toString());
                            dialog.dismiss();
                    if (list.size() != 0) {
                        ll_cardlist.setVisibility(View.VISIBLE);
                        linearlayout.setVisibility(View.GONE);
                        for (Ecommerce_ModelClass listModel : list) {
                            Log.e("listmodel", listModel.toString());
                            dialog.dismiss();
                            try {
                                dialog.dismiss();
                              //  listModel = new Listmodel();
                                JSONObject object = new JSONObject(new Gson().toJson(listModel));

                                JSONArray jsonArray = object.getJSONArray("order_items");
                                Log.e("array", jsonArray.toString());
                                int length = jsonArray.length();
                                for (int index = 0; index < length; index++) {
                                    dialog.dismiss();
                                    JSONObject object1 = jsonArray.getJSONObject(index);
//
                                    listModel.setFinal_amount(object1.getString("final_amount"));

                                    listModel.setQuantity(object1.getString("quantity"));

                                    listModel.setOrder_id(object1.getString("order_id"));

                                    JsonObject productname = new JsonParser().parse(object1.getJSONObject("product_name").toString()).getAsJsonObject();
                                        Log.e("dvd",productname.toString());
                                    try{
                                        JSONObject jname = new JSONObject(productname.toString());
                                        name  = jname.getString("name");
                                        listModel.setName(name);
                                        Log.e("xvds",name);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("sacas errorr",e.toString());

                                    }

                                    dialog.dismiss();

                                    JSONArray array = object1.getJSONArray("product_image");
                                    Log.e("obj", array.toString());

                                    for (int i= 0; i<array.length();i++){
//                                    ll_cardlist.setVisibility(View.VISIBLE);
//                                    linearlayout.setVisibility(View.GONE);
                                        dialog.dismiss();
                                        JSONObject object12 = array.getJSONObject(i);
                                        listModel.setProduct_uri(object12.getString("product_uri"));
                                        Log.e("imgss",listModel.getProduct_uri());
//                                    arrayList.add(listModel);
//                                    adapter.notifyDataSetChanged();
                                    }

                                }


                                dialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            arrayList.add(listModel);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        ll_cardlist.setVisibility(View.VISIBLE);
                        linearlayout.setVisibility(View.GONE);
                        dialog.dismiss();

                        Toast.makeText(orderslist.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                    }

                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<Ecommerce_DataClass> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("orderlist fail",t.toString());
               // Toast.makeText(orderslist.this, "Technical problem", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void intilizerecyclerview(){
        rv_orderproductitem = findViewById(R.id.rv_orderproductitem);

        rv_orderproductitem.setHasFixedSize(true);
        rv_orderproductitem.setLayoutManager(new LinearLayoutManager(this));
        rv_orderproductitem.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        rv_orderproductitem.addItemDecoration(decoration);
        arrayList = new ArrayList<>();
        adapter = new orderlist_adapter(this,arrayList);
        rv_orderproductitem.setAdapter(adapter);
    }


    class orderlist_adapter extends RecyclerView.Adapter<orderlist_adapter.ViewHolder>{


        private Context context;
        ArrayList<Ecommerce_ModelClass> list;
      //  OnItemClickListener itemClickListener;

        public orderlist_adapter(Context context, ArrayList<Ecommerce_ModelClass> list){
            this.context = context;
            this.list = list;
          //  this.itemClickListener = itemClickListener;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context =  parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.orderitem_list,parent,false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Ecommerce_ModelClass listmodel = list.get(position);

            Picasso.with(context)
                    .load(listmodel.getProduct_uri())
                    .into(holder.iv_orderimage);
            holder.tv_count.setText(listmodel.getQuantity());
            holder.tv_orderamount.setText(listmodel.getFinal_amount());
            holder.tv_ordername.setText(listmodel.getName());
            holder.tv_orderid.setText(listmodel.getOrder_id());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_ordername,tv_orderamount,tv_count,tv_orderid;
            ImageView iv_orderimage;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_orderid = itemView.findViewById(R.id.tv_orderid);
                tv_count = itemView.findViewById(R.id.tv_count);
                tv_ordername = itemView.findViewById(R.id.tv_ordername);
                tv_orderamount = itemView.findViewById(R.id.tv_orderamount);
                iv_orderimage = itemView.findViewById(R.id.iv_orderimage);
            }
        }
    }


}
