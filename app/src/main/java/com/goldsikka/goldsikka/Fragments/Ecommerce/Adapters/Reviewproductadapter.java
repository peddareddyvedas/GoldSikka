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

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.ReviewsModel;

import java.util.ArrayList;

public class Reviewproductadapter extends RecyclerView.Adapter<Reviewproductadapter.ViewHolder> {


    private Context context;
    ArrayList<ReviewsModel> list;
    OnItemClickListener itemClickListener;

    public Reviewproductadapter(ArrayList<ReviewsModel> list, Context context) {
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ecommreviwproductlist, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ReviewsModel listmodel = list.get(position);

        holder.reviewname.setText("User ID :" + listmodel.getRuser_id());
        holder.review.setText("Review :" + listmodel.getRreview());
        holder.rating.setText("Rating :" + listmodel.getRrating());

        String string = listmodel.getRcreated_at();
        string = string.substring(0, string.length() - 8);
        holder.time.setText(" Date :" + string);

        Log.e("lmmmmm", "" + listmodel.getRreview());
        Log.e("ddddddm", "" + listmodel.getRrating());
        //  String image = listmodel.getRcreated_at();

     /*  try {
            Glide.with(context).load(image).into(holder.iv_categoryimg);
        } catch (Exception ignored) {
            Glide.with(context).load(R.drawable.background_image).into(holder.iv_categoryimg);
        }*/
/*
        try {

                Glide.with(context).load(AccountUtils.getProfileImg(context)).into(holder.rateimg);

        } catch (Exception e) {
            Glide.with(context).load(R.drawable.profile).into(holder.rateimg);
        }*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView rateimg;
        TextView reviewname, review, rating, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reviewname = itemView.findViewById(R.id.reviewname);
            review = itemView.findViewById(R.id.review);
            rating = itemView.findViewById(R.id.rating);
            time = itemView.findViewById(R.id.time);

            rateimg = itemView.findViewById(R.id.newarrivalimg);
            //   iv_categoryimg.setOnClickListener(this);
            //  itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}

