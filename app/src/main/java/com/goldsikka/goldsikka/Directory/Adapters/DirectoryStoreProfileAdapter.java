package com.goldsikka.goldsikka.Directory.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Activitys.Profile_Details;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.model.Listmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DirectoryStoreProfileAdapter extends RecyclerView.Adapter<DirectoryStoreProfileAdapter.ViewHolder> {

    Context context;
    ArrayList<Listmodel> viewprofilelist;
    private final int limit = 2;


    public DirectoryStoreProfileAdapter(Context context, ArrayList<Listmodel> viewprofilelist) {
        this.context = context;
        this.viewprofilelist = viewprofilelist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.activity_directory_store_profile_adapter, parent, false);

        return new ViewHolder(view);
    }


    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Listmodel listmodel = viewprofilelist.get(position);

        String name = listmodel.getName();
        String image = listmodel.getAvatar();
        Log.e("imageee ", "" + image);
        String review = listmodel.getReview();
        String ratingtv = listmodel.getRating();
        //   holder.ratingtext.setText(ratingtv + ".0");

        if (listmodel.getRating() != null) {
            holder.ratinglayout.setVisibility(View.VISIBLE);

        } else {
            holder.ratinglayout.setVisibility(View.GONE);

        }
        holder.ratingtext.setText(ratingtv);

        holder.profilename.setText(name);
        holder.reviewtext.setText(review);

        if (listmodel.getAvatar() != null) {

            Picasso.with(context.getApplicationContext()).load(image).into(holder.iv_categoryimg);

            try {
                Picasso.with(context).load(image).into(holder.iv_categoryimg);
            } catch (Exception ignored) {
                Picasso.with(context).load(R.drawable.profile).into(holder.iv_categoryimg);
            }

        } else {
            Picasso.with(context).load(R.drawable.profile).into(holder.iv_categoryimg);
        }

    }

    @Override
    public int getItemCount() {
        int limit = 2;
        return Math.min(viewprofilelist.size(), limit);
        // return viewprofilelist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_categoryimg;
        TextView profilename, ratingtext, reviewtext;
        LinearLayout ratinglayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_categoryimg = itemView.findViewById(R.id.profileimg);
            profilename = itemView.findViewById(R.id.profilename);
            ratingtext = itemView.findViewById(R.id.ratingtext);
            reviewtext = itemView.findViewById(R.id.reviewtext);
            ratinglayout = itemView.findViewById(R.id.ratinglayout);

        }

        @Override
        public void onClick(View v) {
//            itemClickListener.onItemClick(v, getAdapterPosition());
        }

    }
}