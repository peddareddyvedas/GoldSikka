package com.goldsikka.goldsikka.Activitys.Coupons;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Activitys.Predict_price.BaseViewHolder;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class CouponslistAdapter extends  RecyclerView.Adapter<BaseViewHolder>{

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    Context context;
    OnItemClickListener clickListener;
    ArrayList<CouponsModel> mPostItems;

    public CouponslistAdapter(Context context,ArrayList<CouponsModel>  postItems,OnItemClickListener clickListener) {
        this.mPostItems = postItems;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.couponitem, parent, false));
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

    public void addItems(ArrayList<CouponsModel>  postItems) {
        mPostItems.addAll(postItems);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        mPostItems.add(new CouponsModel());
        notifyItemInserted(mPostItems.size() - 1);
    }

//    public void addLoading1() {
//        isLoaderVisible = true;
//        mPostItems.add(new CouponsModel());
//        notifyItemInserted(mPostItems.size());
//    }
//
//    public void removeLoading1() {
//        isLoaderVisible = false;
//        int position = mPostItems.size() -1;
//        CouponsModel item = getItem(position);
//        if (item != null) {
//            mPostItems.remove(position);
//            notifyItemRemoved(position);
//        }
//    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mPostItems.size() - 1;
        CouponsModel item = getItem(position);
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

    CouponsModel getItem(int position) {
        return mPostItems.get(position);
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener {
      TextView tvdescription,tvcoponcode,tvmoreinfo,tvminitrans,tvcouponvalue,tvcouponstatus;
//        tvexpiredate
      Button tvapply;
      LinearLayout llcard;

        ViewHolder(View itemView) {
            super(itemView);
            tvcouponvalue = itemView.findViewById(R.id.tvcouponvalue);
            tvminitrans = itemView.findViewById(R.id.tvminitrans);
            tvdescription = itemView.findViewById(R.id.tvdescription);
            tvcoponcode = itemView.findViewById(R.id.tvcoponcode);
            tvapply = itemView.findViewById(R.id.tvapply);
            tvmoreinfo = itemView.findViewById(R.id.tvmoreinfo);
            llcard = itemView.findViewById(R.id.llcard);
            tvapply.setOnClickListener(this);
            tvcouponstatus = itemView.findViewById(R.id.tvcouponstatus);
            itemView.setOnClickListener(this);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            CouponsModel listmodel = mPostItems.get(position);
            tvcoponcode.setText(listmodel.getCoupon());
          //  tvexpiredate.setText("Expired On : "+listmodel.getExpiresAt());
            tvminitrans.setText("Minimum transaction value of Rs. "+listmodel.getMinimum_transaction_amount());
            tvcouponvalue.setText("Get Rs."+listmodel.getAmount()+" worth of Gold");
            tvdescription.setText(listmodel.getDescription());
            tvcouponstatus.setText(listmodel.getStatus());
            tvmoreinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (llcard.getVisibility() == View.VISIBLE) {
                        llcard.setVisibility(View.GONE);
                    } else {
                        llcard.setVisibility(View.VISIBLE);
                    }
                }
            });
            if (!listmodel.isIs_expired()&&!listmodel.isNotApplicable()){
                tvapply.setVisibility(View.VISIBLE);
            }else {
                tvapply.setVisibility(View.GONE);
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
}
