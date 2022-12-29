package com.goldsikka.goldsikka.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnClickItemListenerForSchemes;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;

import java.util.List;

public class Schemes_list_Adapter extends RecyclerView.Adapter<Schemes_list_Adapter.ViewHolder> {
    Context context;
    List<Listmodel> list;
    OnClickItemListenerForSchemes clickListener;

    public Schemes_list_Adapter(List<Listmodel> listmodel,Context context,OnClickItemListenerForSchemes clickListener){
        this.clickListener = clickListener;
        this.context = context;
        this.list = listmodel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.schmeslist,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listmodel listmodel = list.get(position);
        String title = listmodel.getScheme_calculation_type();

//        holder.cd_scheme.setCardBackgroundColor(R.color.white);
        if (title.equals("MG")){
        holder.tv_schemename.setText(listmodel.getTitle());
        holder.iv_scheme.setImageDrawable(context.getDrawable(R.drawable.mygoldnew));
        //holder.cd_scheme.setBackgroundResource(R.drawable.elevenimg);
//        holder.iv_scheme.getResources().getDrawable(R.drawable.mygold2020img);
        }else if (title.equals("JW")){
            holder.tv_schemename.setText(listmodel.getTitle());
         holder.iv_scheme.setImageDrawable(context.getDrawable(R.drawable.elevenscheme));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout cd_scheme;
        TextView tv_schemename;
        ImageView iv_scheme;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cd_scheme = itemView.findViewById(R.id.cd_scheme);
            cd_scheme.setOnClickListener(this);
            tv_schemename = itemView.findViewById(R.id.tv_schemename);
            iv_scheme = itemView.findViewById(R.id.iv_scheme);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            String main = "scheme";
            clickListener.onItemClickListener(view,getAdapterPosition());
        }
    }
}
