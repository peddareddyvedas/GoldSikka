package com.goldsikka.goldsikka.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Activitys.Events.EventModel;
import com.goldsikka.goldsikka.Activitys.Predict_price.BaseViewHolder;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class ReferListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    Context context;
    ArrayList<EventModel> list;
    OnItemClickListener clickListener;

    public ReferListAdapter (Context context ,ArrayList<EventModel> arrayListlist,OnItemClickListener clickListener){
        this.clickListener = clickListener;
        this.context = context;
        this.list = arrayListlist;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ReferListAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.earned_list_design, parent, false));
            case VIEW_TYPE_LOADING:
                return new ReferListAdapter.ProgressHolder(
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
        TextView tvdate,tvmessage,tvtitle;
        // TextView ivedit,ivdelete,tvsend,tvsendlist;
        LinearLayout llbackground;

        ViewHolder(View itemView) {
            super(itemView);

            tvmessage = itemView.findViewById(R.id.notification_message);
            tvdate = itemView.findViewById(R.id.notification_date);
            tvtitle = itemView.findViewById(R.id.notification_title);
            llbackground = itemView.findViewById(R.id.notification_BG);
            itemView.setOnClickListener(this);

        }

        protected void clear() {

        }



        public void onBind(int position) {
            super.onBind(position);

            EventModel model = list.get(position);

            tvdate.setText(model.getCreated_date());
            tvmessage.setText(model.getDescription());
            tvtitle.setText(context.getString(R.string.Rs) + model.getAmount());



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
