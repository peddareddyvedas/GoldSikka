package com.goldsikka.goldsikka.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Activitys.Predict_price.BaseViewHolder;
import com.goldsikka.goldsikka.Models.Enquiryformmodel;
import com.goldsikka.goldsikka.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class TicketAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    Context context;
    ArrayList<Enquiryformmodel> arrayList;

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    public TicketAdapter(Context context, ArrayList<Enquiryformmodel> listmodels) {
        this.context = context;
        this.arrayList = listmodels;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item_list, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));            default:
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
            return position == arrayList.size() ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    public void addItems(ArrayList<Enquiryformmodel> postItems) {
        arrayList.addAll(postItems);
        notifyDataSetChanged();
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = arrayList.size() - 1;
        Enquiryformmodel item = getItem(position);
        if (item != null) {
            arrayList.add(item);
            notifyDataSetChanged();
            arrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        arrayList.clear();
        notifyDataSetChanged();
    }

    Enquiryformmodel getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }


    public class ViewHolder extends BaseViewHolder {
        TextView tvdate, tvdescription, tvstatus,tvticketid,tvschemename;

        ViewHolder(View itemView) {
            super(itemView);
            tvdate = itemView.findViewById(R.id.tvdate);
            tvdescription = itemView.findViewById(R.id.tvdescription);
            tvstatus = itemView.findViewById(R.id.tvstatus);
            tvticketid = itemView.findViewById(R.id.tvticketid);
            tvschemename = itemView.findViewById(R.id.tvschemename);

        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            Enquiryformmodel item = arrayList.get(position);

            tvdescription.setText(item.getDescription());
            tvstatus.setText(item.getStatus());
            tvdate.setText(item.getCharge_date());
            tvschemename.setText(item.getTitle());
            tvticketid.setText(item.getTicket_id());

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
