package com.goldsikka.goldsikka.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.model.Listmodel;

import java.util.List;

public class Scheme_Feature_Adapter extends RecyclerView.Adapter<Scheme_Feature_Adapter.ViewHolder> {

    Context context;
    List<Listmodel> list;

    public Scheme_Feature_Adapter(Context context,List<Listmodel> listmodels){
        this.context = context;
        this.list = listmodels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.scheme_features_adapter,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listmodel listmodel = list.get(position);
        holder.tv_features.setText(listmodel.getFeatures()+":");
        holder.tv_explanation.setText(listmodel.getExplanation());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView tv_features,tv_explanation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_explanation = itemView.findViewById(R.id.tv_explanation);
            tv_features = itemView.findViewById(R.id.tv_features);
        }
    }
}
