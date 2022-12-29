package com.goldsikka.goldsikka.Directory.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.ComingSoon;
import com.goldsikka.goldsikka.Directory.DirectoryStoreDetails;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DirectoryBullionAdapter extends RecyclerView.Adapter<DirectoryBullionAdapter.ViewHolder> {

    Context context;
    ArrayList<Listmodel> bullionlist;

    ArrayList<Listmodel> ratinglist = new ArrayList<>();
    ApiDao apiDao;
    List<Listmodel> flist;
    ArrayList<Listmodel> timeinglist = new ArrayList<>();
    String opentimings = "";
    String closetimings = "";

    public DirectoryBullionAdapter(Context context, ArrayList<Listmodel> bullionlist) {
        this.context = context;
        this.bullionlist = bullionlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.activity_directory_bullion_adapter, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Listmodel listmodel = bullionlist.get(position);
        String name = listmodel.getStore_name();
        String image = listmodel.getImage();
        String discription = listmodel.getStore_description1();
        String ratings = listmodel.getRating();
        holder.tv_category.setText(name);
        holder.tv_discription.setText(discription);
        if (listmodel.getRating() != null) {
            holder.ratinglayout.setVisibility(View.VISIBLE);

        } else {
            holder.ratinglayout.setVisibility(View.GONE);

        }
        holder.tv_ratings.setText(ratings);
        String open = listmodel.getStore_open_timings();
        String close = listmodel.getStore_close_timings();

        try {
            String _24HourTime = close;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            System.out.println(_12HourSDF.format(_24HourDt));
            holder.tv_closetime.setText(_12HourSDF.format(_24HourDt));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String _24HourTime = open;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            System.out.println(_12HourSDF.format(_24HourDt));
            holder.tv_opentime.setText(_12HourSDF.format(_24HourDt));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        holder.listcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DirectoryStoreDetails.class);
                intent.putExtra("id", listmodel.getId());
                intent.putExtra("store_name", listmodel.getStore_name());
                intent.putExtra("mobile", listmodel.getMobile());
                intent.putExtra("latitude", listmodel.getLatitude());
                intent.putExtra("longitude", listmodel.getLongitude());
                intent.putExtra("status", listmodel.getStatus());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        int limit = 3;
        return Math.min(bullionlist.size(), limit);
        // return bullionlist.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<Listmodel> brandsfilter) {
        bullionlist = brandsfilter;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_categoryimg;
        TextView tv_category, tv_ratings, tv_discription, tv_opentime, tv_closetime;
        RecyclerView listbtnRV;

        CardView listcard;
        AppCompatRatingBar ratingBar;
        LinearLayout ratinglayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_category = itemView.findViewById(R.id.bullionText);
            iv_categoryimg = itemView.findViewById(R.id.bullionimg);
            iv_categoryimg.setOnClickListener(this);
            listbtnRV = itemView.findViewById(R.id.listRV);
            listcard = itemView.findViewById(R.id.listcard);
            tv_ratings = itemView.findViewById(R.id.ratingtext);
            ratingBar = itemView.findViewById(R.id.ratings);
            tv_discription = itemView.findViewById(R.id.bulliondiscription);
            ratinglayout = itemView.findViewById(R.id.ratinglayout);
            tv_opentime = itemView.findViewById(R.id.opentime);
            tv_closetime = itemView.findViewById(R.id.closetime);


        }

        @Override
        public void onClick(View v) {
//            itemClickListener.onItemClick(v, getAdapterPosition());
        }

    }
}