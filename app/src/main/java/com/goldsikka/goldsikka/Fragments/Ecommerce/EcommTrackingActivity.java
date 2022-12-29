package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Activitys.GetContacts.ContactList;
import com.goldsikka.goldsikka.Fragments.TransferGold;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EcommTrackingActivity extends AppCompatActivity {

    TextView unameTv, uidTv, titleTv, orderidd;

    RelativeLayout backbtn;
    EcomtrackorderAdapter ecommtrackorder;
    RecyclerView recyclerView;
    Button pdfdownload;
    ArrayList<Listmodel> trackoderslist;
    ApiDao apiDao;
    List<Listmodel> flist;
    String orderid = "";
    ImageView view_order_placed, view_order_placed1, view_order_placed2, view_order_placed3, view_order_placed4, view_order_placed5;
    TextView placed_divider, placed_divider1, placed_divider2, placed_divider3, placed_divider4;
    TextView text1, text2, text3, text4, text5, text6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecomm_tracking_activity);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            orderid = bundle.getString("orderid");
        }

        init();
        getEcommmtrackorders();
    }


    public void init() {
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Tracking");
        orderidd = findViewById(R.id.orderid);
        orderidd.setText(orderid);
        recyclerView = findViewById(R.id.trackrecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        trackoderslist = new ArrayList<>();
        ecommtrackorder = new EcomtrackorderAdapter(trackoderslist);
        recyclerView.setAdapter(ecommtrackorder);


        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        view_order_placed = findViewById(R.id.view_order_placed);
        view_order_placed1 = findViewById(R.id.view_order_placed1);
        view_order_placed2 = findViewById(R.id.view_order_placed2);
        view_order_placed3 = findViewById(R.id.view_order_placed3);
        view_order_placed4 = findViewById(R.id.view_order_placed4);

        placed_divider = findViewById(R.id.placed_divider);
        placed_divider1 = findViewById(R.id.placed_divider1);
        placed_divider2 = findViewById(R.id.placed_divider2);
        placed_divider3 = findViewById(R.id.placed_divider3);
        placed_divider4 = findViewById(R.id.placed_divider4);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);

        pdfdownload = findViewById(R.id.pdfdownload);
        pdfdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EcommTrackingActivity.this, EcommRecipt_Activity.class);
                intent.putExtra("orderid", orderid);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onBackPressed() {
        // NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        // finish();
    }

    private void getEcommmtrackorders() {
        trackoderslist.clear();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<List<Listmodel>> getecomcartlist = apiDao.getecommorderstracking("Bearer " + AccountUtils.getAccessToken(this), orderid);
        getecomcartlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("myorderstatuscodess", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 202) {
                    Log.e("mytrackrstatuscodess", String.valueOf(statuscode));
                    if (flist != null) {
                        trackoderslist.clear();
                        for (Listmodel listmodel : flist) {
                            trackoderslist.add(listmodel);
                            ecommtrackorder.notifyDataSetChanged();
                            Log.e("message", "" + listmodel.getMessage());
                            Log.e("status", "" + listmodel.getStatus());


                           /* if (listmodel.getStatus().equals("true") && listmodel.getMessage().equals("Transaction Successful")) {
                                text1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Gold));
                                view_order_placed.setBackgroundResource(R.drawable.tracking);
                            } else if (listmodel.getStatus().equals("true") && listmodel.getMessage().equals("Order Placed at Goldsikka")) {
                                text2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Gold));
                                view_order_placed1.setBackgroundResource(R.drawable.tracking);
                                placed_divider.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.DarkBrown));

                            } else if (listmodel.getStatus().equals("true") && listmodel.getMessage().equals("Order Accepted at Goldsikka")) {
                                text3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Gold));
                                view_order_placed2.setBackgroundResource(R.drawable.tracking);
                                placed_divider1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.Gold));

                            } else if (listmodel.getStatus().equals("true") && listmodel.getMessage().equals("Your Order is in Making")) {
                                text4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Gold));
                                view_order_placed3.setBackgroundResource(R.drawable.tracking);
                                placed_divider2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.Gold));

                            } else if (listmodel.getStatus().equals("true") && listmodel.getMessage().equals("Order is ready to pickup")) {
                                text5.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Gold));
                                view_order_placed4.setBackgroundResource(R.drawable.tracking);
                                placed_divider3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.Gold));

                            } else if (listmodel.getStatus().equals("true") && listmodel.getMessage().equals("Your Order is Delivered")) {
                                text6.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Gold));
                                view_order_placed5.setBackgroundResource(R.drawable.tracking);
                                placed_divider4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.Gold));

                            } else {

                            }*/
                        }
                    } else {
                        Log.e("catname", "No Products");
                    }
                } else if (statuscode == 422) {
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
            }
        });
    }

    private class EcomtrackorderAdapter extends RecyclerView.Adapter<EcomtrackorderAdapter.ViewHolder> {
        Context context;
        ArrayList<Listmodel> myoders;


        public EcomtrackorderAdapter(ArrayList<Listmodel> myoderslist) {
            this.context = context;
            this.myoders = myoderslist;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.ecomordertrackinglist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Listmodel w = myoders.get(position);
            String name = w.getMessage();
            holder.trackstatusdesc.setText(name);
            Log.e("name", "" + name);

            if (w.getStatus().equals("true")) {
                holder.trackstatusdesc.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Gold));
                holder.cimg.setBackgroundResource(R.drawable.tracking);
                holder.line.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.trackinggreen));


            }
            if (name.equals("Your Order is Delivered")) {
                holder.line.setVisibility(View.GONE);

            } else if (w.getStatus().equals("true") && w.getMessage().equals("Order Cancelled at Goldsikka")) {
                holder.line.setVisibility(View.GONE);
                holder.trackstatusdesc.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                holder.cimg.setBackgroundResource(R.drawable.cancelorder);


            }

        }

        @Override
        public int getItemCount() {
            return myoders.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView cimg;
            TextView line, trackstatusdesc;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                cimg = itemView.findViewById(R.id.view_order_placed);
                line = itemView.findViewById(R.id.placed_divider);
                trackstatusdesc = itemView.findViewById(R.id.trackstatusdesc);

            }
        }
    }


}