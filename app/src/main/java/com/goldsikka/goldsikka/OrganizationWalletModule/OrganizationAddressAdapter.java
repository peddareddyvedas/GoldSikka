package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Adapter.Customer_Address_List_Adapter;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrganizationAddressAdapter extends RecyclerView.Adapter<OrganizationAddressAdapter.ViewHolder> {

    private Context context;
    ArrayList<Listmodel> list;
    String to_name;

    public OrganizationAddressAdapter(Context context,ArrayList<Listmodel> list){
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =  parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.organization_address_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listmodel listmodel = list.get(position);


//            holder.tv_pin.setText(listmodel.getZip());
//            holder.tv_city.setText(listmodel.getCity());
//            holder.tv_address.setText(listmodel.getAddress());

        JsonElement liststate = listmodel.getState();

        if (liststate.equals(null)){
            Log.e("jsonelement","jsonelement error");
        }else {

            try {
                JsonObject from = new JsonParser().parse(listmodel.getState().toString()).getAsJsonObject();
                JSONObject json_from = new JSONObject(from.toString());
                to_name = json_from.getString("name");
                // holder.tv_state.setText(to_name);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        holder.tvaddresstitle.setText(listmodel.getTitle());
        holder.tvaddress.setText(listmodel.getAddress()+","+listmodel.getCity()+","+to_name+","+listmodel.getZip());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvaddresstitle,tvaddress,tvmessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvaddresstitle = itemView.findViewById(R.id.tvaddresstitle);
            tvaddress = itemView.findViewById(R.id.tvaddress);
//            tv_city = itemView.findViewById(R.id.tv_city);
//            tv_pin = itemView.findViewById(R.id.tv_state);
//            tv_state = itemView.findViewById(R.id.tv_pin);
            tvmessage = itemView.findViewById(R.id.tvmessage);

        }

    }
}
