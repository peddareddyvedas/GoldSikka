package com.goldsikka.goldsikka.Directory.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Directory.DirectoryViewActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.model.Listmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DirectoryCatogoriesAdapter extends RecyclerView.Adapter<DirectoryCatogoriesAdapter.ViewHolder> {


    Context context;
    ArrayList<Listmodel> catogorieslist;


    public DirectoryCatogoriesAdapter(Context context, ArrayList<Listmodel> catogorieslist) {
        this.context = context;
        this.catogorieslist = catogorieslist;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.activity_directory_catogories_adapter, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listmodel listmodel = catogorieslist.get(position);
        String name = listmodel.getCategory();

        String image = listmodel.getImage();
        holder.tv_category.setText(name);

        Log.e("lmmmmm", "" + listmodel.getCategory());
        Log.e("ddddddm", "" + listmodel.getImage());

        if (listmodel.getImage() != null) {

            Picasso.with(context.getApplicationContext()).load(image).into(holder.iv_categoryimg);

            try {
                Picasso.with(context).load(image).into(holder.iv_categoryimg);
            } catch (Exception ignored) {
                Picasso.with(context).load(R.drawable.background_image).into(holder.iv_categoryimg);
            }

        } else {
            Picasso.with(context).load(R.drawable.background_image).into(holder.iv_categoryimg);
        }



        holder.iv_categoryimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DirectoryViewActivity.class);
                intent.putExtra("id", listmodel.getId());
                intent.putExtra("category", listmodel.getCategory());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return catogorieslist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_categoryimg;
        TextView tv_category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_category = itemView.findViewById(R.id.newarrivalname);
            iv_categoryimg = itemView.findViewById(R.id.newarrivalimg);

        }

        @Override
        public void onClick(View v) {
//            itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}