package com.goldsikka.goldsikka.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;

import java.util.ArrayList;

public class Customer_bankdetailslist_Adapter extends RecyclerView.Adapter<Customer_bankdetailslist_Adapter.Viewholder> {
    private Context context;
    ArrayList<Listmodel> list;
    OnItemClickListener itemClickListener;

    public Customer_bankdetailslist_Adapter(Context context, ArrayList<Listmodel> list, OnItemClickListener itemclick) {
        this.itemClickListener = itemclick;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from( context ).inflate( R.layout.customer_adapter_banklist, parent, false );
        return new Viewholder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Listmodel listmodel = list.get( position );
        holder.tvnameaccount.setText( listmodel.getName_on_account() );
        holder.tv_accountnumber.setText( listmodel.getAccount_number() );
        holder.tv_bank.setText( listmodel.getBank_name() );
        holder.tvbankname.setText( listmodel.getBank_name() );
        holder.tvbranchname.setText(listmodel.getBank_branch());
        holder.tvifsccode.setText(listmodel.getIfsc_code());

        String primary = listmodel.getIs_primary();
        if (listmodel.isActions()) {
            holder.deletebtn.setVisibility( View.VISIBLE );
            holder.editbtn.setVisibility( View.VISIBLE );
            holder.tvmessage.setVisibility( View.GONE );
        } else {
            holder.deletebtn.setVisibility( View.GONE );
            holder.editbtn.setVisibility( View.GONE );
            holder.tvmessage.setText( listmodel.getMessage() );
            holder.tvmessage.setVisibility( View.VISIBLE );
        }

        if (primary.equals( "true" )) {
            holder.tv_setprimary.setVisibility( View.GONE );
            holder.tvisprimary.setVisibility( View.VISIBLE );
            holder.deletebtn.setVisibility( View.GONE );
            holder.editbtn.setVisibility( View.GONE );
        } else {
            holder.tv_setprimary.setVisibility( View.VISIBLE );
            holder.tvisprimary.setVisibility( View.GONE );
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_accountnumber, tv_bank, tvisprimary, tv_setprimary, tvmessage, tvnameaccount,tvbankname,tvbranchname,tvifsccode;
        ImageView editbtn, deletebtn;

        public Viewholder(@NonNull View itemView) {
            super( itemView );
            tvisprimary = itemView.findViewById( R.id.tvisprimary );
            tv_accountnumber = itemView.findViewById( R.id.tv_accountnumber );
            tv_bank = itemView.findViewById( R.id.tv_bank );
            tvbankname = itemView.findViewById( R.id.tvbankname );
            tvbranchname = itemView.findViewById(R.id.tvbranchname);
            tvifsccode = itemView.findViewById(R.id.tvifsccode);
            tvnameaccount = itemView.findViewById( R.id.tvnameaccount );
            tv_setprimary = itemView.findViewById( R.id.tv_setprimary );

            editbtn = itemView.findViewById( R.id.editbtn );
            deletebtn = itemView.findViewById( R.id.deletebtn );
            tvmessage = itemView.findViewById( R.id.tvmessage );
            itemView.setOnClickListener( this );
            editbtn.setOnClickListener( this );
            deletebtn.setOnClickListener( this );
            tv_setprimary.setOnClickListener( this );

        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick( view, getAdapterPosition() );
        }

    }


}
