package com.goldsikka.goldsikka.Activitys.Events;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Activitys.Predict_price.BaseViewHolder;
import com.goldsikka.goldsikka.Adapter.Customer_Address_List_Adapter;
import com.goldsikka.goldsikka.Adapter.Passbook_Adapter;
import com.goldsikka.goldsikka.Models.PassBookModel;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class EventAdapter extends RecyclerView.Adapter<BaseViewHolder> {


    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    Context context;
    ArrayList<EventModel> list;
    OnItemClickListener clickListener;

    public EventAdapter (Context context ,ArrayList<EventModel> arrayListlist,OnItemClickListener clickListener){
        this.clickListener = clickListener;
        this.context = context;
        this.list = arrayListlist;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.eventlist, parent, false));
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
        TextView tvtitle,tvdate,tvstatus,tvadminsttus;
        TextView ivedit,ivdelete,tvsend,tvsendlist;
        LinearLayout tvmore;

        ViewHolder(View itemView) {
            super(itemView);

            tvtitle = itemView.findViewById(R.id.tvtitle);
            tvdate = itemView.findViewById(R.id.tvdate);
            tvstatus = itemView.findViewById(R.id.tvstatus);
            tvmore = itemView.findViewById(R.id.tvmore);
            ivedit = itemView.findViewById(R.id.ivedit);
            ivdelete = itemView.findViewById(R.id.ivdelete);
            tvadminsttus = itemView.findViewById(R.id.tvadminsttus);
            tvsendlist = itemView.findViewById(R.id.tvsendlist);
            tvsendlist.setOnClickListener(this);
            tvsend = itemView.findViewById(R.id.tvsend);
            tvsend.setOnClickListener(this);
//            tvgetqrcode = itemView.findViewById(R.id.tvgetqrcode);
//            tvgetqrcode.setOnClickListener(this);
            ivedit.setOnClickListener(this);
            ivdelete.setOnClickListener(this);
            tvmore.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            EventModel model = list.get(position);
            tvdate.setText("Event Date : "+model.getEvent_date());
            tvtitle.setText(model.getEvent_name());
            tvstatus.setText("Status : "+model.getEventStatus());
            tvadminsttus.setText(model.getAdminStatus());
            if (model.getStatus().equals("2")){
                tvadminsttus.setTextColor(context.getResources().getColor(R.color.colorRed));
            }else {
                tvmore.setVisibility(View.VISIBLE);
                tvadminsttus.setTextColor(context.getResources().getColor(R.color.DarkBrown));
            }
            if (!model.isUpdate()){
                ivdelete.setVisibility(View.GONE);
                ivedit.setVisibility(View.GONE);
            }else {
                ivdelete.setVisibility(View.VISIBLE);
                ivedit.setVisibility(View.VISIBLE);
            }
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
//    @NonNull
//    @Override
//    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        context =  parent.getContext();
//        View view = LayoutInflater.from(context).inflate(R.layout.eventlist,parent,false);
//        return  new Viewholder(view);
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
//
//        EventModel model = list.get(position);
//       tvdate.setText("Event Date : "+model.getEvent_date());
//       tvtitle.setText(model.getEvent_name());
//       tvstatus.setText("Status : "+model.getEventStatus());
//       tvadminsttus.setText(model.getAdminStatus());
//        if (model.getStatus().equals("2")){
//           tvadminsttus.setTextColor(context.getResources().getColor(R.color.colorRed));
//        }else {
//           tvmore.setVisibility(View.VISIBLE);
//           tvadminsttus.setTextColor(context.getResources().getColor(R.color.textcolorprimary));
//        }
//        if (!model.isUpdate()){
//           ivdelete.setVisibility(View.GONE);
//           ivedit.setVisibility(View.GONE);
//        }else {
//           ivdelete.setVisibility(View.VISIBLE);
//           ivedit.setVisibility(View.VISIBLE);
//        }
//        if (!model.isQR()){
//           tvgetqrcode.setVisibility(View.GONE);
//        }else {
//           tvgetqrcode.setVisibility(View.VISIBLE);
//
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        TextView tvtitle,tvdate,tvstatus,tvmore,tvadminsttus,tvgetqrcode;
//        ImageView ivedit,ivdelete;

//        public Viewholder(@NonNull View itemView) {
//            super(itemView);
//
//            tvtitle = itemView.findViewById(R.id.tvtitle);
//            tvdate = itemView.findViewById(R.id.tvdate);
//            tvstatus = itemView.findViewById(R.id.tvstatus);
//            tvmore = itemView.findViewById(R.id.tvmore);
//            ivedit = itemView.findViewById(R.id.ivedit);
//            ivdelete = itemView.findViewById(R.id.ivdelete);
//            tvadminsttus = itemView.findViewById(R.id.tvadminsttus);
//            tvgetqrcode = itemView.findViewById(R.id.tvgetqrcode);
//            tvgetqrcode.setOnClickListener(this);
//            ivedit.setOnClickListener(this);
//            ivdelete.setOnClickListener(this);
//            tvmore.setOnClickListener(this);
//            itemView.setOnClickListener(this);
//
//        }
//
//        @Override
//        public void onClick(View v) {
//            clickListener.onItemClick(v,getAdapterPosition());
//        }
//    }
}
