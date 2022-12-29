package com.goldsikka.goldsikka.Adapter.Ecommerce;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Product_List_Adpter extends RecyclerView.Adapter<Product_List_Adpter.ViewHolder> {


    private Context context;
    ArrayList<Listmodel> list;
    OnItemClickListener itemClickListener;

    public Product_List_Adpter(Context context, ArrayList<Listmodel> list, OnItemClickListener itemClickListener){

        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ec_productlist,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Listmodel listmodel = list.get(position);

//
           JsonObject from = new JsonParser().parse(listmodel.getAmountDetails().toString()).getAsJsonObject();
        Log.e("fromhdsda",from.toString());

        try {
            JSONObject json_from = new JSONObject(from.toString());

            String finalAmount = json_from.getString("finalAmount");
            Log.e("final amvsdiv",finalAmount);
           holder.tv_amount.setText("Price "+finalAmount);

        } catch (JSONException e) {
            e.printStackTrace();
        }

            holder.tv_poroductname.setText(listmodel.getName());
            holder.tv_pay_grams.setText("Grams "+listmodel.getGrams());

        Picasso.with(context)
                .load("https://images.app.goo.gl/JnsBi1qT3FNGeshY7").
                fit().
                centerCrop()
                .into(holder.iv_product);
            holder.tv_count.setText(listmodel.getQuantity());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cd_products;
        ImageView iv_product;
        TextView tv_poroductname,tv_pay_grams,tv_amount;
        TextView tv_count;
        Button bt_add;
        ImageView iv_add,iv_remove;

        LinearLayout ll_additem,ll_addremoves;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_count =  itemView.findViewById(R.id.tv_count);

            bt_add =   itemView.findViewById(R.id.btn_additem);
            bt_add.setOnClickListener(this);
            iv_add =  itemView.findViewById(R.id.iv_add);
            iv_add.setOnClickListener(this);
            iv_remove =   itemView.findViewById(R.id.iv_remove);
            iv_remove.setOnClickListener(this);

        ll_additem =itemView.findViewById(R.id.ll_additem);
        ll_addremoves =  itemView.findViewById(R.id.ll_addremoves);

            cd_products = itemView.findViewById(R.id.cd_products);
            cd_products.setOnClickListener(this);
            tv_poroductname = itemView.findViewById(R.id.tv_poroductname);
            tv_pay_grams = itemView.findViewById(R.id.tv_pay_grams);
            tv_amount = itemView.findViewById(R.id.tv_price);
            iv_product = itemView.findViewById(R.id.iv_product);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v,getAdapterPosition());
            switch (v.getId()){

            }
        }
    }
}
