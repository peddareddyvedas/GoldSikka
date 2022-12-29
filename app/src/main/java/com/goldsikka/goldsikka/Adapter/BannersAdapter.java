package com.goldsikka.goldsikka.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.goldsikka.goldsikka.Models.BannersModel;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.model.Listmodel;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannersAdapter extends RecyclerView.Adapter<BannersAdapter.MYVIEWHODLER>{

    List<BannersModel> list;
    ViewPager2 viewPager2;
    Context context;

    public BannersAdapter(List<BannersModel> list, ViewPager2 viewPager2, Context context) {
        this.list = list;
        this.viewPager2 = viewPager2;
        this.context = context;
    }

    @NonNull
    @Override
    public MYVIEWHODLER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container,parent , false);

        return new MYVIEWHODLER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MYVIEWHODLER holder, int position) {
        holder.setImage(list.get(position));

        if (position == list.size() - 2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    class MYVIEWHODLER extends RecyclerView.ViewHolder {
        RoundedImageView roundedImageView;
        // Context context;
        public MYVIEWHODLER(@NonNull View itemView) {
            super(itemView);

            roundedImageView = itemView.findViewById(R.id.roundimage);
        }

        void setImage(BannersModel sliderItem){

            try {
                Picasso.with(context)
                        .load(sliderItem.getBanner_uri())
                        .into(roundedImageView);
            }catch (Exception e){
                Picasso.with(context)
                        .load(R.drawable.background_image)
                        .into(roundedImageView);
            }
            // roundedImageView.setImageURI(Uri.parse(sliderItem.getBanner_uri()));


            // roundedImageView.setImageResource(Integer.parseInt(sliderItem.getBanner_uri()));
        }


    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            list.addAll(list);
            notifyDataSetChanged();
        }
    };
}
