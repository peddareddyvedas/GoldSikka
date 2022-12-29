package com.goldsikka.goldsikka.Adapter.Ecommerce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Subcategorylist_adpter  extends RecyclerView.Adapter<Subcategorylist_adpter.ViewHolder> {

    private Context context;
    ArrayList<Listmodel> list;
    OnItemClickListener itemClickListener;

    public Subcategorylist_adpter(Context context, ArrayList<Listmodel> list, OnItemClickListener itemClickListener){
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =  parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.subcategorylist_adapter,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Listmodel listmodel = list.get(position);

            //String test = listmodel.getIsSubCategories();
            holder.tv_subcategory.setText(listmodel.getName());
        Picasso.with(context).load(listmodel.getCategory_uri()).into(holder.iv_subcategoryimg);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView iv_subcategoryimg;
        TextView tv_subcategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_subcategory = itemView.findViewById(R.id.tv_subcategory);
            iv_subcategoryimg = itemView.findViewById(R.id.iv_subcategoryimg);
            iv_subcategoryimg.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v,getAdapterPosition());
        }
    }
}
