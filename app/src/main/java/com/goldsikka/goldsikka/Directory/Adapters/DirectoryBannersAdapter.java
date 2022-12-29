package com.goldsikka.goldsikka.Directory.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.model.Listmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DirectoryBannersAdapter extends RecyclerView.Adapter<DirectoryBannersAdapter.ViewHolder> {

    Context context;
    ArrayList<Listmodel> bannerslist;

    public DirectoryBannersAdapter(Context context, ArrayList<Listmodel> bannerslist) {
        this.context = context;
        this.bannerslist = bannerslist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate( R.layout.activity_directory_banners_adapter, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Listmodel listmodel = bannerslist.get(position);
        String image = listmodel.getImage_uri();




        if (listmodel.getImage_uri() != null) {

            Picasso.with(context.getApplicationContext()).load(image).into(holder.iv_categoryimg);

            try {
                Picasso.with(context).load(image).into(holder.iv_categoryimg);
            } catch (Exception ignored) {
                Picasso.with(context).load(R.drawable.background_image).into(holder.iv_categoryimg);
            }

        } else {
            Picasso.with(context).load(R.drawable.background_image).into(holder.iv_categoryimg);
        }
    }

    @Override
    public int getItemCount() {
        return bannerslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_categoryimg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_categoryimg = itemView.findViewById(R.id.ourbrandimg);


        }

        @Override
        public void onClick(View v) {
//            itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}