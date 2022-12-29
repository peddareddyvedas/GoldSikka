package com.goldsikka.goldsikka.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.goldsikka.goldsikka.Models.MainModuleModel;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;


import java.util.List;

public class  MainModuleAdapterClass extends RecyclerView.Adapter<MainModuleAdapterClass.ViewHolder> {

    Context context;
    List<MainModuleModel> listmodels;
    OnItemClickListener clickListener;
    MenuItem menuItem;

    public MainModuleAdapterClass(List<MainModuleModel> listmodel,Context context,
                                  OnItemClickListener clickListener,MenuItem menuItem){
        this.context = context;

        this.listmodels = listmodel;
        this.clickListener = clickListener;
        this.menuItem = menuItem;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.mainmodule_adapterxml, parent, false);

        return new ViewHolder(view);
    }
    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MainModuleModel list = listmodels.get(position);

        holder.tvname.setText(list.getName());
        holder.tvtitle.setText(list.getTitle());
        String type = list.getModule_type();
        //String id = list.getId();
    //    menuItem.setVisible(false);


        switch (type) {
            case "WM":
                holder.ivmainmodule.setImageDrawable(context.getDrawable(R.drawable.womensday));
                holder.tvname.setText(list.getName());
                holder.tvtitle.setText(list.getTitle());
                menuItem.setVisible(false);
                break;
            case "PR":
                holder.ivmainmodule.setImageDrawable(context.getDrawable(R.drawable.predectimgli));
                holder.tvname.setText(list.getName());
                holder.tvtitle.setText(list.getTitle());
                menuItem.setVisible(true);
                break;
            case "FN":
                holder.ivmainmodule.setImageDrawable(context.getDrawable(R.drawable.financialoffer));
                holder.tvname.setText(list.getName());
                holder.tvtitle.setText(list.getTitle());
                break;
        }

    }

    @Override
    public int getItemCount() {
        return listmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvname,tvtitle;
        LinearLayout iv_module;
        ImageView ivmainmodule;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivmainmodule = itemView.findViewById(R.id.ivmainmodule);
            tvtitle = itemView.findViewById(R.id.tvtitle);
            tvname = itemView.findViewById(R.id.tvname);
            iv_module = itemView.findViewById(R.id.iv_module);
            iv_module.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            clickListener.onItemClick(v,getAdapterPosition());
        }
    }
}
