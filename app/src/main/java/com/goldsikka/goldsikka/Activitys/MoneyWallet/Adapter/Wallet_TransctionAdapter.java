package com.goldsikka.goldsikka.Activitys.MoneyWallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Activitys.Events.EventAdapter;
import com.goldsikka.goldsikka.Activitys.Events.EventModel;
import com.goldsikka.goldsikka.Activitys.Predict_price.BaseViewHolder;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class Wallet_TransctionAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    Context context;
    ArrayList<EventModel> list;
    OnItemClickListener clickListener;

    public Wallet_TransctionAdapter (Context context ,ArrayList<EventModel> arrayListlist,OnItemClickListener clickListener){
        this.clickListener = clickListener;
        this.context = context;
        this.list = arrayListlist;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new Wallet_TransctionAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.wallettranction_list_design, parent, false));
            case VIEW_TYPE_LOADING:
                return new Wallet_TransctionAdapter.ProgressHolder(
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
            return position == list.size() ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }
    public void addItems(ArrayList<EventModel>  postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = list.size() - 1;
        EventModel item = getItem(position);
        if (item != null) {
            list.add(item);
            notifyDataSetChanged();
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    EventModel getItem(int position) {
        return list.get(position);
    }
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ViewHolder extends BaseViewHolder implements View.OnClickListener {
        TextView tvdate,tvtransctionId,tvamount,tvdesc;
        // TextView ivedit,ivdelete,tvsend,tvsendlist;
        // LinearLayout tvmore;

        ViewHolder(View itemView) {
            super(itemView);

            tvtransctionId = itemView.findViewById(R.id.pass_transctionid);
            tvdate = itemView.findViewById(R.id.pass_date);
            tvamount = itemView.findViewById(R.id.pass_amount);
            tvdesc = itemView.findViewById(R.id.pass_desc);
            itemView.setOnClickListener(this);
        }

        protected void clear() {

        }



        public void onBind(int position) {
            super.onBind(position);

            EventModel model = list.get(position);

            if (model.getType().equals("CR")){
                tvamount.setTextColor(context.getResources().getColor(R.color.colorGreen));
                tvamount.setText(context.getString(R.string.Rs)+" "+model.getAmount());

            }else {
                tvamount.setTextColor(context.getResources().getColor(R.color.colorRed));
                tvamount.setText(context.getString(R.string.Rs)+" "+model.getAmount());

            }

            //tvamount.setText(context.getString(R.string.Rs)+" "+model.getAmount());

            tvdate.setText("Date : "+model.getCreated_date());
            tvtransctionId.setText("Transaction ID : "+model.getTransaction_id());
            tvdesc.setText(model.getDescription());
            // tvadminsttus.setText(model.getAdminStatus());

//            if (!model.isQR()){
//                tvgetqrcode.setVisibility(View.GONE);
//            }else {
//                tvgetqrcode.setVisibility(View.VISIBLE);
//
//            }
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
}