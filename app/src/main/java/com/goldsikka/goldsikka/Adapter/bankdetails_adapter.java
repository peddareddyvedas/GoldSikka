package com.goldsikka.goldsikka.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.bankdetails_list;

import java.util.ArrayList;

public class bankdetails_adapter extends RecyclerView.Adapter<bankdetails_adapter.SubCategoryViewHolder> {

    private Context context;
    private ArrayList<bankdetails_list> arrayList;
    private OnItemClickListener listener;

    public bankdetails_adapter(ArrayList<bankdetails_list> arrayList,OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.bank_details_design, parent, false);
        return new SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {

        bankdetails_list item = arrayList.get(position);

        holder.tvname.setText(item.getBeneficiary_name());
        holder.tvbank_name.setText(item.getBank_name());
        holder.tvbranch.setText(item.getBranch_bank());
        holder.tvifsc.setText(item.getIfsc_code());
        holder.tvaccount_no.setText(item.getAccount_no());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class SubCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvname,tvifsc,tvaccount_no,tvbank_name,tvbranch;
        LinearLayout linearLayout;
        ImageView iv_delete;

        SubCategoryViewHolder(View itemView) {
            super(itemView);

            tvname = itemView.findViewById(R.id.tv_beneficary_name);
            tvaccount_no = itemView.findViewById(R.id.tv_account_no);
            tvbranch = itemView.findViewById(R.id.tv_branch);
            tvifsc = itemView.findViewById(R.id.tv_ifsc);
            tvbank_name = itemView.findViewById(R.id.tv_bank_name);
            //iv_delete = itemView.findViewById(R.id.iv_delete);
            linearLayout = itemView.findViewById(R.id.ll_layout);

            linearLayout.setOnClickListener(this);

            itemView.setOnClickListener(this);
            iv_delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
         listener.onItemClick(v,getAdapterPosition());
        }

    }
}