package com.goldsikka.goldsikka.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Activitys.Predict_price.BaseViewHolder;
import com.goldsikka.goldsikka.Models.PassBookModel;
import com.goldsikka.goldsikka.R;

import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Passbook_Adapter extends  RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    Context context;
    OnItemClickListener clickListener;
    ArrayList<PassBookModel> mPostItems;

    public Passbook_Adapter(Context context,ArrayList<PassBookModel>  postItems,OnItemClickListener clickListener) {
        this.mPostItems = postItems;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.passbooklist, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mPostItems.size() ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mPostItems == null ? 0 : mPostItems.size();
    }

    public void addItems(ArrayList<PassBookModel>  postItems) {
        mPostItems.addAll(postItems);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        mPostItems.add(new PassBookModel());
        notifyItemInserted(mPostItems.size() - 1);
    }

    public void addLoading1() {
        isLoaderVisible = true;
        mPostItems.add(new PassBookModel());
        notifyItemInserted(mPostItems.size());
    }

    public void removeLoading1() {
        isLoaderVisible = false;
        int position = mPostItems.size() -1;
        PassBookModel item = getItem(position);
        if (item != null) {
            mPostItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mPostItems.size() - 1;
        PassBookModel item = getItem(position);
        if (item != null) {
            mPostItems.add(item);
            notifyDataSetChanged();
            mPostItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        mPostItems.clear();
        notifyDataSetChanged();
    }

    PassBookModel getItem(int position) {
        return mPostItems.get(position);
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener {
        TextView tvpassstatus;
        TextView TransactionId;
        TextView grams,pass_amount;
        TextView txn_type;
        TextView date;
        TextView desc;
        LinearLayout llclick;

        ViewHolder(View itemView) {
            super(itemView);
            TransactionId = itemView.findViewById(R.id.pass_transctionid);
            grams = itemView.findViewById(R.id.pass_grams);
            pass_amount = itemView.findViewById(R.id.pass_amount);
            desc = itemView.findViewById(R.id.pass_desc);
            date = itemView.findViewById(R.id.pass_date);
            tvpassstatus = itemView.findViewById(R.id.tvpassstatus);
            llclick = itemView.findViewById(R.id.llclick);
            llclick.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            PassBookModel listmodel = mPostItems.get(position);
            TransactionId.setText("TransactionId :"+listmodel.getId());
            desc.setText(listmodel.getDesc());
            Log.e("decs",listmodel.getDesc());
            date.setText("Date :"+listmodel.getCreated_at());
            tvpassstatus.setText("Status :"+listmodel.getTransactionStatus());

            if (listmodel.getTransction_type().equals("CR")){
                grams.setText("+" +listmodel.getGrams()+" g");
                grams.setTextColor(context.getResources().getColor(R.color.colorGreen));
                pass_amount.setText(context.getString(R.string.Rs)+" "+listmodel.getTotal_amount());
                pass_amount.setTextColor(context.getResources().getColor(R.color.colorGreen));
            }else {
                grams.setText("-" +listmodel.getGrams()+" g");
                grams.setTextColor(context.getResources().getColor(R.color.colorRed));
                pass_amount.setText(context.getString(R.string.Rs) +" "+listmodel.getTotal_amount());
                pass_amount.setTextColor(context.getResources().getColor(R.color.colorRed));
            }

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v,getAdapterPosition());
        }
    }

    public class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
        }
    }
//
//
//
//
// RecyclerView.Adapter<Passbook_Adapter.ViewHolder> {
//
//    Context context;
//    List<Listmodel> listmodels ;
//
//    public Passbook_Adapter(Context context, List<Listmodel> list){
//        this.listmodels = list;
//        this.context = context;
//    }
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        context = parent.getContext();
//        View view = LayoutInflater.from(context).inflate(R.layout.passbooklist,parent,false);
//        return new  ViewHolder(view);
//    }
//
//    @SuppressLint({"SetTextI18n", "NewApi"})
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // Listmodel listmodel = listmodels.get(position);
//
//  JsonObject from = new JsonParser().parse(listmodel.getFrom().toString()).getAsJsonObject();
//       // JsonObject to = new JsonParser().parse(listmodel.getTo().toString()).getAsJsonObject();
//
////        try {
////            JSONObject json_from = new JSONObject(from.toString());
////            JSONObject json_to = new JSONObject(to.toString());
////            String from_name = json_from.getString("name");
////            String to_name = json_to.getString("name");
////            holder.type_transcation.setText(frdswc z      om_name);
////            Toast.makeText(context, to_name, Toast.LENGTH_SHORT).show();
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
//
////            if (listmodel.getTxn_type().equals("CR")){
////                holder.totalgold.setTextColor(Color.GREEN);
////            }else {
////                holder.totalgold.setTextColor(Color.RED);
////            }
//          // holder.paymentmode.setText("Credit/Debit :"+ listmodel.getTransction_type());
//            holder.TransactionId.setText("TransactionId :"+listmodel.getId());
//
//            if (listmodel.getTransction_type().equals("CR")){
//                holder.grams.setText("+" +listmodel.getGrams()+" g");
//                holder.grams.setTextColor(context.getResources().getColor(R.color.colorGreen));
//                holder.pass_amount.setText(context.getString(R.string.Rs)+" "+listmodel.getParchase_amount());
//                holder.pass_amount.setTextColor(context.getResources().getColor(R.color.colorGreen));
//            }else {
//                holder.grams.setText("-" +listmodel.getGrams()+" g");
//                holder.grams.setTextColor(context.getResources().getColor(R.color.colorRed));
//                holder.pass_amount.setText(context.getString(R.string.Rs) +" "+listmodel.getParchase_amount());
//                holder.pass_amount.setTextColor(context.getResources().getColor(R.color.colorRed));
//            }
//            holder.desc.setText(listmodel.getDesc());
//            holder.date.setText("Date :"+listmodel.getPurchasedate());
//          //  holder.txn_type.setText("Transaction Type :"+listmodel.getTxn_type());
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return listmodels.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//           TextView get_grams;
//            TextView TransactionId;
//            TextView grams,pass_amount;
//            TextView txn_type;
//            TextView date;
//            TextView desc;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//           TransactionId = itemView.findViewById(R.id.pass_transctionid);
//            grams = itemView.findViewById(R.id.pass_grams);
//            pass_amount = itemView.findViewById(R.id.pass_amount);
//           desc = itemView.findViewById(R.id.pass_desc);
//            date = itemView.findViewById(R.id.pass_date);
//          //  txn_type = itemView.findViewById(R.id.pass_txntype);
//        }
//    }
}
