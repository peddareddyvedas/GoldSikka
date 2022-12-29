package com.goldsikka.goldsikka.Adapter.Ecommerce;


import android.annotation.SuppressLint;
import android.content.Context;
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

public class Ecommerce_Categorylist_Adapter extends RecyclerView.Adapter<Ecommerce_Categorylist_Adapter.ViewHolder>{


    private Context context;
    ArrayList<Listmodel> list;
    OnItemClickListener itemClickListener;

    public Ecommerce_Categorylist_Adapter(Context context, ArrayList<Listmodel> list, OnItemClickListener itemClickListener){
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =  parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ecommerce_category,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listmodel listmodel = list.get(position);
        String name = listmodel.getName();
        String image = listmodel.getCategoryImageLink();

        holder.tv_category.setText(name);
        Glide.with(context).load(image).into(holder.iv_categoryimg);

//        if (name.equals("Metals")){
//            holder.iv_categoryimg.setImageDrawable(context.getDrawable(R.drawable.metalic));
//            holder.tv_category.setText(name);
//        }
//        else if (name.equals("Jewellery")){
//            holder.iv_categoryimg.setImageDrawable(context.getDrawable(R.drawable.jewellery));
//            holder.tv_category.setText(name);
//        }

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

            tv_category = itemView.findViewById(R.id.tv_category);
            iv_categoryimg = itemView.findViewById(R.id.iv_categoryimg);
            iv_categoryimg.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v,getAdapterPosition());
        }
    }
}
