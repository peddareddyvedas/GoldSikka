package com.goldsikka.goldsikka.Adapter.DigitalWallet_Content;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Adapter.Ecommerce.Ecommerce_Categorylist_Adapter;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.model.Listmodel;

import java.util.ArrayList;

public class Digitalwallet_SubContent extends RecyclerView.Adapter<Digitalwallet_SubContent.ViewHolder> {
    private Context context;

    ArrayList<Listmodel> list;

    public Digitalwallet_SubContent(Context context,ArrayList<Listmodel> listmodels){
        this.context = context;
        this.list = listmodels;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =  parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.digitalsubcontent,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listmodel listmodel = list.get(position);
        String newmaincontent = listmodel.getContent();
            try{
                newmaincontent = newmaincontent.replace("Digital Wallet", "Gold Suvidha");
            }catch (Exception e){
                Log.e("changed from server","No changes");
            }
        holder.tv_content1.setText(newmaincontent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_content1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_content1= itemView.findViewById(R.id.tv_content1);
        }
    }
}
