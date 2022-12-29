package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Activitys.Coupons.CouponsModel;
import com.goldsikka.goldsikka.Activitys.Coupons.CouponslistAdapter;
import com.goldsikka.goldsikka.Activitys.Predict_price.BaseViewHolder;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class EditProfileAdapter extends  RecyclerView.Adapter<BaseViewHolder>{

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    Context context;
    ArrayList<ORGModel> mPostItems;

    public EditProfileAdapter(Context context,ArrayList<ORGModel>  postItems) {
        this.mPostItems = postItems;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.orgeditprofilelist, parent, false));
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

    public void addItems(ArrayList<ORGModel>  postItems) {
        mPostItems.addAll(postItems);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        mPostItems.add(new ORGModel());
        notifyItemInserted(mPostItems.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mPostItems.size() - 1;
        ORGModel item = getItem(position);
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

    ORGModel getItem(int position) {
        return mPostItems.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        TextView tvname,tvemail,tvnumber,tvstatus,tvdate;


        ViewHolder(View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.tvname);
            tvemail = itemView.findViewById(R.id.tvemail);
            tvnumber = itemView.findViewById(R.id.tvNumber);
            tvstatus = itemView.findViewById(R.id.tvstatus);
            tvdate = itemView.findViewById(R.id.tvdate);


        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            ORGModel listmodel = mPostItems.get(position);
            tvnumber.setText(listmodel.getMobile());
            //  tvexpiredate.setText("Expired On : "+listmodel.getExpiresAt());
            tvdate.setText("Date: "+listmodel.getCreated_date());
            tvstatus.setText("Status: "+listmodel.getProfileStatus());
            tvname.setText(listmodel.getName());
            tvemail.setText(listmodel.getEmail());

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
