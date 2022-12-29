package com.goldsikka.goldsikka.Adapter.DigitalWallet_Content;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.model.Listmodel;

import java.util.ArrayList;

public class Digital_wallet_Feature  extends RecyclerView.Adapter<Digital_wallet_Feature.ViewHolder> {

    private Context context;

    ArrayList<Listmodel> list;

    public Digital_wallet_Feature(Context context,ArrayList<Listmodel> listmodels){
        this.context = context;
        this.list = listmodels;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =  parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.digitalfeature,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                Listmodel listmodel = list.get(position);
                holder.tv_features.setText(listmodel.getContent());
                holder.tv_explanation.setText(listmodel.getExplanation());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_features,tv_explanation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_explanation= itemView.findViewById(R.id.tv_explanation);
            tv_features = itemView.findViewById(R.id.tv_features);
        }
    }
}
