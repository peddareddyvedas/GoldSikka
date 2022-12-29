package com.goldsikka.goldsikka.Fragments.JewelleryInventory.Adapters;


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

public class AllCatAdapter extends RecyclerView.Adapter<AllCatAdapter.ViewHolder>{


    private Context context;
    ArrayList<Listmodel> list;
    OnItemClickListener itemClickListener;

    public AllCatAdapter(Context context, ArrayList<Listmodel> list, OnItemClickListener itemClickListener){
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =  parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.catitemhome,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listmodel listmodel = list.get(position);
        String name = listmodel.getCatname();
        String id = listmodel.getId();
        String image = listmodel.getImage_uri();

//        Log.e("fcvgbhnj", image);

        holder.cname.setText(name);
        Glide.with(context).load(image).into(holder.cimg);

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
        ImageView cimg;
        TextView cname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cname = itemView.findViewById(R.id.cname);
            cimg = itemView.findViewById(R.id.cimg);
            cimg.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
//            ToastMessage.onToast(context, pname, );
            itemClickListener.onItemClick(v,getAdapterPosition());
        }
    }
}
