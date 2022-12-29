package com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;

import java.util.ArrayList;

public class Underbudget_Ecomm_Adapter extends RecyclerView.Adapter<Underbudget_Ecomm_Adapter.ViewHolder> {

    private Context context;
    ArrayList<Listmodel> list;
    OnItemClickListener itemClickListener;


    public Underbudget_Ecomm_Adapter(ArrayList<Listmodel> list, Context context, OnItemClickListener itemClickListener) {
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.activity_ecomm_shoponbudget, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listmodel listmodel = list.get(position);
        String name = listmodel.getHeading();
        String image = listmodel.getImage_uri();
        String style = listmodel.getStylecount();
        Log.e("style", "" + style);
        holder.underpricetext.setText(name);
        holder.styles.setText(style + " Styles");

        try {
            Glide.with(context).load(image).into(holder.iv_categoryimg);
        } catch (Exception ignored) {
            Glide.with(context).load(R.drawable.background_image).into(holder.iv_categoryimg);
        }


       /* Glide.with(context)
                .load(image)
                .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.shoopcart.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_categoryimg,topsellimg;
        TextView underpricetext, styles;
        CardView underprice;
        LinearLayout shoopcart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            underpricetext = itemView.findViewById(R.id.underpricetext);
            styles = itemView.findViewById(R.id.styles);
            iv_categoryimg = itemView.findViewById(R.id.topsellimg);
            shoopcart = itemView.findViewById(R.id.shoopcart);
            underprice = itemView.findViewById(R.id.underprice);

            //    iv_categoryimg.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
