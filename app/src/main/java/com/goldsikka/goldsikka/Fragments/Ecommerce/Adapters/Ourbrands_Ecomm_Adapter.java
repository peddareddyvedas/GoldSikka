package com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;

import java.util.ArrayList;

public class Ourbrands_Ecomm_Adapter extends RecyclerView.Adapter<Ourbrands_Ecomm_Adapter.ViewHolder> {

    private Context context;
    ArrayList<Listmodel> list;
    OnItemClickListener itemClickListener;


    public Ourbrands_Ecomm_Adapter(ArrayList<Listmodel> list, Context context, OnItemClickListener itemClickListener) {
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.activity_ecomm_ourbrands, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listmodel listmodel = list.get(position);
        String name = listmodel.getBrand();
        String image = listmodel.getImage_uri();
        holder.tv_category.setText(name);
        Log.e("namessssss", "" + name);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_categoryimg;
        TextView tv_category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_category = itemView.findViewById(R.id.brandlname);
            iv_categoryimg = itemView.findViewById(R.id.ourbrandimg);
            iv_categoryimg.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
