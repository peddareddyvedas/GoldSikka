package com.goldsikka.goldsikka.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.model.price;

import java.util.ArrayList;

public class Globalratesadapter extends RecyclerView.Adapter<Globalratesadapter.SubCategoryViewHolder> implements  Animation.AnimationListener{

    private Context context;
    private ArrayList<price> arrayList;
    Animation animBlink;

    public Globalratesadapter(ArrayList<price> arrayList) {

        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.globalrates_design, parent, false);
        return new SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        price item = arrayList.get(position);

     //   animBlink = AnimationUtils.loadAnimation(context, R.anim.blink);

     //   animBlink.setAnimationListener(this);

        holder.tvcountries.setText(item.getGold());
        holder.tvbuy.setText("\u20B9 "+item.getBuy());
        holder.tvsell.setText("\u20B9 "+item.getSell());

//        holder.tv999.startAnimation(animBlink);
//        holder.tv916.startAnimation(animBlink);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    class SubCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvcountries,tvbuy,tvsell;

        SubCategoryViewHolder(View itemView) {
            super(itemView);

            tvcountries = itemView.findViewById(R.id.tvcountries);
            tvbuy = itemView.findViewById(R.id.tvbuy);
            tvsell = itemView.findViewById(R.id.tvsell);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}


