package com.goldsikka.goldsikka.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Customer_Address_List_Adapter extends RecyclerView.Adapter<Customer_Address_List_Adapter.ViewHolder> {
    private Context context;
    ArrayList<Listmodel> list;
    OnItemClickListener itemClickListener;
    String to_name = " ";

    public Customer_Address_List_Adapter(Context context, ArrayList<Listmodel> list, OnItemClickListener itemclick) {
        this.itemClickListener = itemclick;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.customer_adapter_addresslist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listmodel listmodel = list.get(position);

//            holder.tv_pin.setText(listmodel.getZip());
//            holder.tv_city.setText(listmodel.getCity());
//            holder.tv_address.setText(listmodel.getAddress());
        String primary = listmodel.getIs_primary();
        if (listmodel.isActions()) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
            holder.tvmessage.setVisibility(View.GONE);
            holder.tverrormessage.setVisibility(View.VISIBLE);

        } else {
            holder.delete.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
            holder.tvmessage.setText(listmodel.getMessage());
            holder.tverrormessage.setVisibility(View.VISIBLE);
        }

        if (primary.equals("true")) {
            holder.tvsetprimary.setVisibility(View.GONE);
            holder.tvisprimary.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
            holder.tverrormessage.setVisibility(View.GONE);

        } else {
            holder.tvsetprimary.setVisibility(View.VISIBLE);
            holder.tvisprimary.setVisibility(View.GONE);

        }

        JsonElement liststate = listmodel.getState();

       /*    if (liststate.equals(null)) {
            Log.e("jsonelement", "jsonelement error");
        } else {

            try {
                JsonObject from = new JsonParser().parse(listmodel.getState().toString()).getAsJsonObject();
                JSONObject json_from = new JSONObject(from.toString());
                to_name = json_from.getString("name");
                // holder.tv_state.setText(to_name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
        try {
            JSONObject   object = new JSONObject(new Gson().toJson(listmodel.getState()));

            to_name = object.getString("name");
            Log.e("to_name", ""+to_name);

            // holder.tv_state.setText(to_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.tverrormessage.setText(listmodel.getMessage().trim());
        holder.tvaddresstitle.setText(listmodel.getTitle());
        holder.tvaddress.setText(listmodel.getAddress() + "," + listmodel.getCity() + "," + to_name + "," + listmodel.getZip());
        Log.e("totaladdresslist", "" + listmodel.getAddress() + "," + listmodel.getCity() + "," + to_name + "," + listmodel.getZip());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvaddresstitle, tvaddress, tvisprimary, tvmessage;
        TextView tvsetprimary, edit, delete, tverrormessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvisprimary = itemView.findViewById(R.id.tvisprimary);
            tvaddresstitle = itemView.findViewById(R.id.tvaddresstitle);
            tvaddress = itemView.findViewById(R.id.tvaddress);
//            tv_city = itemView.findViewById(R.id.tv_city);
//            tv_pin = itemView.findViewById(R.id.tv_state);
//            tv_state = itemView.findViewById(R.id.tv_pin);
            tvsetprimary = itemView.findViewById(R.id.tv_setprimary);
            edit = itemView.findViewById(R.id.bt_edit);
            delete = itemView.findViewById(R.id.bt_remove);
            tvmessage = itemView.findViewById(R.id.tvmessage);
            itemView.setOnClickListener(this);
            edit.setOnClickListener(this);
            delete.setOnClickListener(this);
            tvsetprimary.setOnClickListener(this);
            tverrormessage = itemView.findViewById(R.id.tverrormessage);

        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
