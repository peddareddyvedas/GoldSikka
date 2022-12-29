package com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;

import java.util.ArrayList;

public class EcommGiftCardAdapter extends RecyclerView.Adapter<EcommGiftCardAdapter.ViewHolder> {


    private Context context;
    ArrayList<Listmodel> list;
    OnItemClickListener itemClickListener;

    public EcommGiftCardAdapter(ArrayList<Listmodel> list, Context context, OnItemClickListener itemClickListener) {
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public EcommGiftCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.activity_ecommgiftlist, parent, false);
        return new EcommGiftCardAdapter.ViewHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull EcommGiftCardAdapter.ViewHolder holder, int position) {

        Listmodel listmodel = list.get(position);
        String name = listmodel.getPname();
        String purchase = listmodel.getPrice();
        String image = listmodel.getImage_uri();
        holder.tv_category.setText(name);

       /* double asdf2 = Double.parseDouble(purchase);
        int i = (int) asdf2;*/
        holder.purchaseprice.setText("₹" + purchase);


        Log.e("lmmmmm", "" + listmodel.getPname());
        Log.e("ddddddm", "" + listmodel.getPimg());

        // Glide.with(context).load(image).into(holder.iv_categoryimg);


        try {
            Glide.with(context).load(image).into(holder.iv_categoryimg);
        } catch (Exception ignored) {
            Glide.with(context).load(R.drawable.background_image).into(holder.iv_categoryimg);
        }


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public void filterList(ArrayList<Listmodel> giftcardfilter) {
        list = giftcardfilter;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_categoryimg;
        TextView tv_category, purchaseprice;
        Button movetocart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_category = itemView.findViewById(R.id.newarrivalname);
            iv_categoryimg = itemView.findViewById(R.id.newarrivalimg);
            purchaseprice = itemView.findViewById(R.id.purchaseprice);
            movetocart = itemView.findViewById(R.id.movetocart);
            movetocart.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}