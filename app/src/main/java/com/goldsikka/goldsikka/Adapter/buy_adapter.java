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
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.buy_list;

import java.util.ArrayList;

public class buy_adapter extends RecyclerView.Adapter<buy_adapter.SubCategoryViewHolder> {

    private Context context;
    private ArrayList<buy_list> arrayList;
    Animation animBlink;
    OnItemClickListener listener;

    public buy_adapter(ArrayList<buy_list> arrayList,OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.passbook_adapter_design, parent, false);
        return new SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {

        buy_list item = arrayList.get(position);

        holder.tvamount.setText(item.getTotal_amount()+" \u20B9");
        holder.tvdate.setText(item.getDate());
        holder.tvtrastionid.setText(item.getTransaction_id());
        holder.tvreference.setText(item.getReference());
        holder.tvgold.setText(item.getGold());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class SubCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvamount,tvgold,tvtrastionid,tvdate,tvreference;


        SubCategoryViewHolder(View itemView) {
            super(itemView);

            tvamount = itemView.findViewById(R.id.tv_amount);
            tvgold = itemView.findViewById(R.id.tv_gold);
            tvreference = itemView.findViewById(R.id.tv_reference);
            tvtrastionid = itemView.findViewById(R.id.tv_transtionid);
            tvdate = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            listener.onItemClick(v,getAdapterPosition());

        }
    }
}