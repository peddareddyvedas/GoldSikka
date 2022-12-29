package com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;

import java.util.ArrayList;
import java.util.List;

public class EcomsubcatfilterAdapter extends RecyclerView.Adapter<EcomsubcatfilterAdapter.ViewHolder> {


    private Context context;
    ArrayList<Listmodel> list;
    OnItemClickListener itemClickListener;
    List<Listmodel> flistwish = new ArrayList<>();


    public EcomsubcatfilterAdapter(Context context, ArrayList<Listmodel> list, OnItemClickListener itemClickListener) {
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.activity_ecomm_subfilter_list, parent, false);
        return new ViewHolder(view);
    }

    public List<Listmodel> getFlistwish() {
        return flistwish;
    }

    public void setFlistwish(List<Listmodel> flistwish) {
        this.flistwish = flistwish;
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listmodel listmodel = list.get(position);
        String name = listmodel.getSubcatname();

        String image = listmodel.getImage_uri();
        holder.tv_category.setText(name);

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

    public void filterList(ArrayList<Listmodel> subCatfilter) {
        list = subCatfilter;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_categoryimg;
        TextView tv_category;
        LinearLayout llhome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_category = itemView.findViewById(R.id.tv_category);
            iv_categoryimg = itemView.findViewById(R.id.iv_categoryimg);
            llhome = itemView.findViewById(R.id.subfilterllhome);
            llhome.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}

