package com.goldsikka.goldsikka.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Activitys.Events.EventModel;
import com.goldsikka.goldsikka.Activitys.MoneyWallet.Adapter.Wallet_TransctionAdapter;
import com.goldsikka.goldsikka.Activitys.Predict_price.BaseViewHolder;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class HappyCustomer_Adapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    Context context;
    ArrayList<EventModel> list;
    OnItemClickListener clickListener;

    public HappyCustomer_Adapter (Context context ,ArrayList<EventModel> arrayListlist,OnItemClickListener clickListener){
        this.clickListener = clickListener;
        this.context = context;
        this.list = arrayListlist;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new HappyCustomer_Adapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.happy_customer_design, parent, false));
            case VIEW_TYPE_LOADING:
                return new HappyCustomer_Adapter.ProgressHolder(
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
        TextView tvdate,tvname,tvrating,tvdesc;
        ImageView ivprofileimg;
        // TextView ivedit,ivdelete,tvsend,tvsendlist;
        // LinearLayout tvmore;

        ViewHolder(View itemView) {
            super(itemView);

            tvname = itemView.findViewById(R.id.cust_name);
            tvdate = itemView.findViewById(R.id.cust_date);
            tvrating = itemView.findViewById(R.id.cust_rating);
            ivprofileimg = itemView.findViewById(R.id.iv_profile);
            tvdesc = itemView.findViewById(R.id.cust_feedback);
            itemView.setOnClickListener(this);
        }

        protected void clear() {

        }



        public void onBind(int position) {
            super.onBind(position);

            EventModel model = list.get(position);

            tvdate.setText(model.getCreated_date());
            tvdesc.setText(model.getMessage());
            tvname.setText(model.getName());
            tvrating.setText(model.getRating());
          //  String stavathar = model.getAvatar();

            JsonElement liststate = model.getUserDetails();


                try {
                    JsonObject from = new JsonParser().parse(liststate.toString()).getAsJsonObject();
                    JSONObject json_from = new JSONObject(from.toString());
                    Log.e("JSON Element", json_from.toString());
                    String to_name = json_from.getString("avatarImageLink");
                    // holder.tv_state.setText(to_name);
                    Glide.with(context)
                            .load(to_name)
                            .into(ivprofileimg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//            if (stavathar != null){
//               // stprofileimg = listmodel.getAvatarImageLink();
////                AccountUtils.setProfileImg(this,stprofileimg);
//                Glide.with(context)
//                        .load(model.getAvatarImageLink())
//                        .into(ivprofileimg);
//                //Picasso.with(activity).load(listmodel.getAvatarImageLink()).into(ivprofileimg);
//            }else {
//                ivprofileimg.setImageResource(R.drawable.profile);
//            }

            //tvamount.setText(context.getString(R.string.Rs)+" "+model.getAmount());


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
