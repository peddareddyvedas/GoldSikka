package com.goldsikka.goldsikka.Adapter;

import android.annotation.SuppressLint;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.goldsikka.goldsikka.Activitys.Predict_price.BaseViewHolder;
import com.goldsikka.goldsikka.Models.SchemeModel;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class Schemes_usersubscribed_Adapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final ArrayList<SchemeModel> arrayList;
    OnItemClickListener listener;
    Context context;

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    public Schemes_usersubscribed_Adapter(Context context,ArrayList<SchemeModel> arrayList, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
        this.context = context;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.scheme_usersubscribed_item, parent, false));
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
            return position == arrayList.size() ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }
    public void addItems(ArrayList<SchemeModel>  postItems) {
        arrayList.addAll(postItems);
        notifyDataSetChanged();
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = arrayList.size() - 1;
        SchemeModel item = getItem(position);
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

    SchemeModel getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener {
        TextView tv_title,  tv_date;
        LinearLayout tv_more,tvnickname,llnickremove;

        ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tvtitle);
            tv_date = itemView.findViewById(R.id.tvdate);

            tv_more = itemView.findViewById(R.id.tvmore);
            tv_more.setOnClickListener(this);
            llnickremove = itemView.findViewById(R.id.llnickremove);
            llnickremove.setOnClickListener(this);

            tvnickname = itemView.findViewById(R.id.tvnickname);
            tvnickname.setOnClickListener(this);


            itemView.setOnClickListener(this);

        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {
            super.onBind(position);
            SchemeModel item = arrayList.get(position);

//                JsonObject from = new JsonParser().parse(item.getScheme_title().toString()).getAsJsonObject();
//
//                try {
//                    JSONObject json_from = new JSONObject(from.toString());
//                    String from_name = json_from.getString("title");
//                    Log.e("Titlename", from_name);
//                    tv_title.setText(from_name);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//
//
//
//            }


            tv_title.setText(item.getNickName());

            tv_date.setText("Date : "+item.getCreated_date());

            if (item.isNickname()){
                llnickremove.setVisibility(View.VISIBLE);
                tvnickname.setVisibility(View.GONE);
            }else {
                llnickremove.setVisibility(View.GONE);
                tvnickname.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v,getAdapterPosition());
        }
    }

    public static class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
        }
    }


}
