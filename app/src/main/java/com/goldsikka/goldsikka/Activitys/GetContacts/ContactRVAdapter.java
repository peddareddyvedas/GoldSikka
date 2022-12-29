package com.goldsikka.goldsikka.Activitys.GetContacts;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.goldsikka.goldsikka.Activitys.GiftModuleActivity;
import com.goldsikka.goldsikka.Fragments.TransferGold;
import com.goldsikka.goldsikka.NewDesignsActivity.GiftContactList;
import com.goldsikka.goldsikka.R;

import java.util.ArrayList;

public class ContactRVAdapter extends RecyclerView.Adapter<ContactRVAdapter.ViewHolder> implements Filterable {


    Context context;
    ArrayList<ContactsModal> list;
    ArrayList<ContactsModal> listfilter;

        public ContactRVAdapter(Context context , ArrayList<ContactsModal> arrayList){
            this.context = context;
            this.list = arrayList;
            this.listfilter = arrayList;

        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.transfercontactlistitems,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactsModal modal = listfilter.get(position);
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getRandomColor();

        // below text drawable is a circular.
        TextDrawable drawable2 = TextDrawable.builder().beginConfig()
                .width(100)  // width in px
                .height(100) // height in px
                .endConfig()
                .buildRound(modal.getContacytName().substring(0, 1), color);
        // setting image to our image view on below line.
        holder.ivcontact.setImageDrawable(drawable2);
        String number = modal.getPhoneNumber();
        holder.tvname.setText(modal.getContacytName());
        holder.tvnumber.setText(number);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransferGold.class);
                intent.putExtra("phone_number", number);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listfilter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        TextView tvname,tvnumber;
        ImageView ivcontact;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.rllayout);
            tvnumber = itemView.findViewById(R.id.tvnumber);
            tvname = itemView.findViewById(R.id.tvcontactname);
            ivcontact = itemView.findViewById(R.id.ivcontactimg);
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listfilter = list;
                } else {
                    ArrayList<ContactsModal> filteredList = new ArrayList<>();
                    for (ContactsModal row : list) {


                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getContacytName().toLowerCase().contains(charString.toLowerCase()) || row.getPhoneNumber().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    listfilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listfilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listfilter = (ArrayList<ContactsModal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

