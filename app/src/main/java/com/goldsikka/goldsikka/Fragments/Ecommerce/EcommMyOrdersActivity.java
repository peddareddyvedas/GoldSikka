package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EcommMyOrdersActivity extends AppCompatActivity {

    EcommMyOrdersAdapter ecommMyOrdersAdapter;
    RecyclerView recyclerView;
    TextView unameTv, uidTv, titleTv, totalorders;
    ArrayList<Listmodel> myoderslist, myorderimages;
    RelativeLayout backbtn;
    ApiDao apiDao;
    List<Listmodel> flist, flist1;
    int selectedPosition = -1;
    int wcount = 0;
    RelativeLayout notfound;
    SwipeRefreshLayout swipe_layout;
    String orderimage = "sdf";
    String ordernameee = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecomm_my_orders_activity);
        init();
        getEcommmyorders();
        onrefresh();
    }

    public void init() {
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        totalorders = findViewById(R.id.totalorders);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("MyOrders");
        notfound = findViewById(R.id.notfound);
        swipe_layout = findViewById(R.id.swipe_layout);

        recyclerView = findViewById(R.id.recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        myoderslist = new ArrayList<>();
        myorderimages = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ecommMyOrdersAdapter = new EcommMyOrdersAdapter(this, myoderslist);
        recyclerView.setAdapter(ecommMyOrdersAdapter);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void onrefresh() {
        swipe_layout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipe_layout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(EcommMyOrdersActivity.this)) {
                            ToastMessage.onToast(EcommMyOrdersActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            swipe_layout.setRefreshing(false);
                            return;
                        } else {
                            init();
                            onrefresh();
                            getEcommmyorders();
                        }
                        swipe_layout.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }

    private void getMyOrderImages11(String porderid, Listmodel mylistmodel) {
        myorderimages.clear();
        Call<List<Listmodel>> getProduct = apiDao.getecommproductinfo("Bearer " + AccountUtils.getAccessToken(this), porderid);
        getProduct.enqueue(new Callback<List<Listmodel>>() {
            String abc = "sddv";

            @SuppressLint({"LongLogTag", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("proinfostatuscode", String.valueOf(statuscode));
                flist1 = response.body();
                Log.e("flist", "" + flist1);
                myorderimages.clear();
                if (statuscode == 200 || statuscode == 202) {
                    Log.e("infostatuscode", String.valueOf(statuscode));
                    for (Listmodel listmodel : flist1) {
                        Log.e("catname", "enter2");
                        Log.e("jcjnnnnn", "" + listmodel.getImage_uri());

                        Log.e("jcjnnnnn", "" + listmodel.getPname());

                        ordernameee = listmodel.getGold_type();
                        Log.e("caratgift", "" + ordernameee);

                        orderimage = String.valueOf(listmodel.getImage_uri());
                        Log.e("orderimage", orderimage);


                        break;

                    }
                    mylistmodel.setImage_uri(orderimage);
                    myoderslist.add(mylistmodel);
                    Collections.reverse(myoderslist);
                    ecommMyOrdersAdapter.notifyDataSetChanged();
                   /* if (ordernameee.equals("0")) {
                        myoderslist.remove(mylistmodel);

                    } else {
                        mylistmodel.setImage_uri(orderimage);
                        myoderslist.add(mylistmodel);
                        Collections.reverse(myoderslist);
                        ecommMyOrdersAdapter.notifyDataSetChanged();

                    }*/

                } else if (statuscode == 400) {
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

    private void getEcommmyorders() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        myoderslist.clear();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<List<Listmodel>> getecomcartlist = apiDao.getecommyorders("Bearer " + AccountUtils.getAccessToken(this));
        getecomcartlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {

                dialog.dismiss();
                int statuscode = response.code();
                Log.e("myorderstatuscodess", String.valueOf(statuscode));
                flist = response.body();
                myoderslist.clear();

                if (statuscode == 200 || statuscode == 202) {
                    Log.e("myorderstatuscodess", String.valueOf(statuscode));
                    if (flist != null) {
                        wcount = flist.size();
                        Log.e("count", String.valueOf(wcount));
                        if (wcount == 0) {
                            totalorders.setText("0");
                            notfound.setVisibility(View.VISIBLE);
                        } else {
                            totalorders.setText(String.valueOf(wcount));
                            notfound.setVisibility(View.GONE);
                        }
                        myoderslist.clear();
                        for (Listmodel listmodel : flist) {
                            Log.e("orderid", listmodel.getOrder_id());
                            getMyOrderImages11(listmodel.getOrder_id(), listmodel);
                            Log.e("imageurl", "" + orderimage);
                        }

                    } else {
                        Log.e("countb1", String.valueOf(wcount));
                        wcount = 0;
                        totalorders.setText(String.valueOf(wcount));
                    }

                   /* if (flist != null) {

                    } else {
                        Log.e("catname", "No Products");
                    }*/
                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
//          getEcommmyorders();
        super.onResume();
    }

    private class EcommMyOrdersAdapter extends RecyclerView.Adapter<EcommMyOrdersAdapter.ViewHolder> {
        Context context;
        ArrayList<Listmodel> myoderslist;


        public EcommMyOrdersAdapter(EcommMyOrdersActivity ecommMyOrdersActivity, ArrayList<Listmodel> myoderslist) {
            this.context = context;
            this.myoderslist = myoderslist;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.ecomm_myordreslist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            Listmodel w = myoderslist.get(position);
            holder.sub_id.setText("Order Id :" + w.getOrder_id());
            Log.e("myimage", w.getImage_uri());
            Picasso.with(getApplicationContext()).load(w.getImage_uri()).into(holder.cimg);
            //  holder.order_status_tv.setText(w.getStatustext());
            Log.e("ordername", "" + w.getPname());


            if (selectedPosition == position) {
                Log.e("if", "" + position);
                //    holder.textselectline.setVisibility(View.VISIBLE);

            } else {
                //  holder.textselectline.setVisibility(View.GONE);
            }


            holder.orderInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (selectedPosition != position) {
                        selectedPosition = position;
                        notifyDataSetChanged();
                        Intent intent = new Intent(getApplicationContext(), EcommProductInfoActivity.class);
                        intent.putExtra("orderid", w.getOrder_id());
                        startActivity(intent);
                        Log.e("orderidddddd", "" + w.getOrder_id());

                    } else {
                        selectedPosition = -1;
                        notifyDataSetChanged();
                    }
                }
            });
            holder.order_status_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedPosition != position) {
                        selectedPosition = position;
                        notifyDataSetChanged();
                        Intent intent = new Intent(getApplicationContext(), EcommTrackingActivity.class);
                        intent.putExtra("orderid", w.getOrder_id());
                        startActivity(intent);
                        Log.e("orderidddddd", "" + w.getOrder_id());

                    } else {
                        selectedPosition = -1;
                        notifyDataSetChanged();
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return myoderslist.size();
            /*if (myoderslist.size() > 0) {
                notfound.setVisibility(View.GONE);
                return myoderslist.size();
            } else {
                notfound.setVisibility(View.VISIBLE);
                return 0;
            }*/

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView cimg;
            TextView orderInfo, order_status_tv, sub_id;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                cimg = itemView.findViewById(R.id.oimg);
                sub_id = itemView.findViewById(R.id.sub_id);
                orderInfo = itemView.findViewById(R.id.order_info_TV);
                order_status_tv = itemView.findViewById(R.id.order_status_tv);
            }
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        //startActivity(new Intent(getApplicationContext(), EcomSubcategory.class));

        Intent intent = new Intent(this, MainFragmentActivity.class);

        startActivity(intent);


    }
}