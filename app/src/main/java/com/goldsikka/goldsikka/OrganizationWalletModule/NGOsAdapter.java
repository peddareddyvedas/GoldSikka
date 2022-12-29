package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Activitys.Predict_price.BaseViewHolder;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class NGOsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    Context context;
    ArrayList<UserOrganizationDetailsModel> list;
    OnItemClickListener clickListener;

    public NGOsAdapter (Context context ,ArrayList<UserOrganizationDetailsModel> arrayListlist,OnItemClickListener clickListener){
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
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.ngos_list_items, parent, false));
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
    public void addItems(ArrayList<UserOrganizationDetailsModel>  postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = list.size() - 1;
        UserOrganizationDetailsModel item = getItem(position);
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

    UserOrganizationDetailsModel getItem(int position) {
        return list.get(position);
    }
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ViewHolder extends BaseViewHolder implements View.OnClickListener {
        TextView tvorgid,tvorgname;
        LinearLayout llngos;

        ViewHolder(View itemView) {
            super(itemView);

            tvorgid = itemView.findViewById(R.id.org_id);
            tvorgname = itemView.findViewById(R.id.org_name);
            llngos = itemView.findViewById(R.id.llngos);
            llngos.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            UserOrganizationDetailsModel model = list.get(position);
            tvorgid.setText("Org.reg.no : "+model.getRegistration_number());
            tvorgname.setText("Org.name : "+model.getName());

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
