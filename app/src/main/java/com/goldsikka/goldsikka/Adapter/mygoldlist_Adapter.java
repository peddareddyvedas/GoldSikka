package com.goldsikka.goldsikka.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Fragments.Schemes.NextMMiPaymentForSchems;
import com.goldsikka.goldsikka.Models.SchemeModel;
import com.goldsikka.goldsikka.R;

import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;


public class mygoldlist_Adapter extends RecyclerView.Adapter<mygoldlist_Adapter.ViewHolder> {

    Context context;
    List<SchemeModel> arrayList ;
    OnItemClickListener listener;
    int vias;


    public mygoldlist_Adapter(Context context, List<SchemeModel> list,int vias){
        this.arrayList = list;
        this.context = context;
        this.listener = listener;
        this.vias = vias;
    }


  @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.subscribeplanedetails,parent,false);
        return new ViewHolder(view);


    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SchemeModel listmodel = arrayList.get(position);

        String  scheme_type = listmodel.getScheme_calculation_type();

        String stusertransctions = String.valueOf(listmodel.getUser_transactions());

      //  Log.e("statitle", String.valueOf(listmodel.getSchemeStatus()));

        JsonObject from = new JsonParser().parse(listmodel.getUser_transactions().toString()).getAsJsonObject();
        try {

            JSONObject json_from = new JSONObject(from.toString());

            String amount = json_from.getString("amount");
            String gst = json_from.getString("gst");
            String reference_id = json_from.getString("id");

            holder.tv_gst.setText(context.getResources().getString(R.string.Rs) + " " + gst);
            holder.Amount.setText(context.getResources().getString(R.string.Rs) + " " + amount);
            holder.TransactionId.setText(reference_id);

        } catch (JSONException e) {
            e.printStackTrace();

        }

        if (scheme_type.equals("MG")) {
            holder.ll_jewellery.setVisibility(View.GONE);
            holder.ll_mygold.setVisibility(View.VISIBLE);
        }
        else if (scheme_type.equals("JW")){
            holder.ll_jewellery.setVisibility(View.VISIBLE);
            holder.ll_mygold.setVisibility(View.GONE);
        }

        holder.date.setText(listmodel.getEmi_date());
        holder.tv_grams.setText(listmodel.getGrams());
        holder.txn_type.setText(listmodel.getSource());

        Log.e("elememt", String.valueOf(listmodel.getScheme_status()));
        boolean ispay = listmodel.isPay();
        Log.e("stacsdvdsv", String.valueOf(ispay));
        int payvisable = listmodel.getScheme_status();

        if (ispay){
            holder.btnextpay.setVisibility(View.GONE);
            holder.tvstatus.setVisibility(View.VISIBLE);
            holder.tvstatus.setText(listmodel.getStatus());
        }else {
            holder.btnextpay.setVisibility(View.VISIBLE);
        }

//        if (ispay){
//            //  Log.e("stacsdvdsacv", String.valueOf(listmodel.isPay()));
//
//            holder.btnextpay.setVisibility(View.GONE);
//            holder.tvstatus.setVisibility(View.VISIBLE);
//            holder.tvstatus.setText(listmodel.getStatus());
//        }else if (!ispay){
//            holder.btnextpay.setVisibility(View.VISIBLE);
//        }
//
        Log.e("inpos", String.valueOf(vias));
        if (vias == 0||vias == 4){
            holder.btnextpay.setVisibility(View.VISIBLE);
        }else {
            holder.btnextpay.setVisibility(View.GONE);
        }



        holder.btnextpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NextMMiPaymentForSchems.class);
                intent.putExtra("id",listmodel.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView TransactionId,Amount,tv_gst,txn_type,date,tv_grams,tvstatus;

        LinearLayout ll_jewellery,ll_mygold;
        Button btnextpay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TransactionId = itemView.findViewById(R.id.mygold_transctionid);

            ll_mygold = itemView.findViewById(R.id.ll_mygold);
            ll_jewellery = itemView.findViewById(R.id.ll_jewellery);
            tv_grams = itemView.findViewById(R.id.mygold_grams);
            tv_gst = itemView.findViewById(R.id.tv_gst);
            Amount = itemView.findViewById(R.id.mygold_Amount);
            date = itemView.findViewById(R.id.mygold_date);
            txn_type = itemView.findViewById(R.id.mygold_txntype);
            tvstatus = itemView.findViewById(R.id.tvstatus);
            btnextpay = itemView.findViewById(R.id.btnextpay);
        }
    }
}
