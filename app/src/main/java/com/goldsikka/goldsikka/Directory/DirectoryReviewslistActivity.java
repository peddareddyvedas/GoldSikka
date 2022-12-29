package com.goldsikka.goldsikka.Directory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.goldsikka.goldsikka.Fragments.Ecommerce.Ecomfavouritiesactivity;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectoryReviewslistActivity extends AppCompatActivity {

    RecyclerView reviewsRV;
    DirectoryStorereviewsAdapter directoryStorereviewsAdapter;
    ArrayList<Listmodel> storereviewslist;
    ApiDao apiDao;
    String id;
    TextView unameTv, uidTv, titleTv;
    List<Listmodel> flist;
    RelativeLayout backbtn;
    Bundle bundle;
    String toname;
    SwipeRefreshLayout swipeRefresh;
    RelativeLayout notfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_reviewslist);

        bundle = getIntent().getExtras();
        id = bundle.getString("id");
        Log.e("id", "" + id);
        toname = bundle.getString("store_name");

        init();
        getRatingsdata();
        onrefresh();

    }

    public void init() {
        notfound = findViewById(R.id.notfound);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        reviewsRV = findViewById(R.id.reviewsRV);
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("All Reviews");
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        intilizerecyclerview();

    }

    private void getRatingsdata() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();

        } else {
            apiDao = ApiClient.getClient("").create(ApiDao.class);
            Call<List<Listmodel>> getproductslist = apiDao.getretaingsdata("Bearer " + AccountUtils.getAccessToken(this), id);
            getproductslist.enqueue(new Callback<List<Listmodel>>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    dialog.dismiss();

                    Log.e("statusrandom", String.valueOf(statuscode));
                    flist = response.body();
                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("reviewstatus", String.valueOf(statuscode));
                        if (flist != null) {
                            for (Listmodel listmodel : flist) {
                                String ree = listmodel.getReview();
                                storereviewslist.add(listmodel);
                                directoryStorereviewsAdapter.notifyDataSetChanged();
                                notfound.setVisibility(View.GONE);


                             /*   if (listmodel.getReview().equals(ree)) {

                                    storereviewslist.remove(position);
                                    notifyItemRemoved(position);
                                }*/
                            }


                        } else {
                            dialog.dismiss();
                            Log.e("catname", "No cats");
                        }
                    } else if (statuscode == 404) {
                        dialog.dismiss();
                        notfound.setVisibility(View.VISIBLE);

                        Log.e("400statuscaod", "" + statuscode);
                    } else if (statuscode == 422) {
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e("reviewfailughb", String.valueOf(t));
                }
            });
        }
    }


    private void intilizerecyclerview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reviewsRV.setLayoutManager(linearLayoutManager);
        reviewsRV.setHasFixedSize(true);
        storereviewslist = new ArrayList<>();
        directoryStorereviewsAdapter = new DirectoryStorereviewsAdapter(this, storereviewslist);
        reviewsRV.setAdapter(directoryStorereviewsAdapter);
    }

    public void onrefresh() {
        swipeRefresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipeRefresh.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(DirectoryReviewslistActivity.this)) {
                            ToastMessage.onToast(DirectoryReviewslistActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            swipeRefresh.setRefreshing(false);
                            return;
                        } else {
                            init();
                            getRatingsdata();

                            onrefresh();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }


    public class DirectoryStorereviewsAdapter extends RecyclerView.Adapter<DirectoryStorereviewsAdapter.ViewHolder> {

        Context context;
        ArrayList<Listmodel> viewprofilelist;
        private final int limit = 2;


        public DirectoryStorereviewsAdapter(Context context, ArrayList<Listmodel> viewprofilelist) {
            this.context = context;
            this.viewprofilelist = viewprofilelist;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.activity_directory_store_profile_adapter, parent, false);

            return new ViewHolder(view);
        }


        @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Listmodel listmodel = viewprofilelist.get(position);

            String name = listmodel.getName();
            String image = listmodel.getAvatar();
            Log.e("imageee ", "" + image);
            String review = listmodel.getReview();
            String ratingtv = listmodel.getRating();
            holder.ratingtext.setText(ratingtv + ".0");
            holder.profilename.setText(name);
            holder.reviewtext.setText(review);


            if (listmodel.getAvatar() != null) {

                Picasso.with(context.getApplicationContext()).load(image).into(holder.iv_categoryimg);

                try {
                    Picasso.with(context).load(image).into(holder.iv_categoryimg);
                } catch (Exception ignored) {
                    Picasso.with(context).load(R.drawable.profile).into(holder.iv_categoryimg);
                }

            } else {
                Picasso.with(context).load(R.drawable.profile).into(holder.iv_categoryimg);
            }
        }

        @Override
        public int getItemCount() {
            return viewprofilelist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView iv_categoryimg;
            TextView profilename, ratingtext, reviewtext;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                iv_categoryimg = itemView.findViewById(R.id.profileimg);
                profilename = itemView.findViewById(R.id.profilename);
                ratingtext = itemView.findViewById(R.id.ratingtext);
                reviewtext = itemView.findViewById(R.id.reviewtext);

            }

            @Override
            public void onClick(View v) {
//            itemClickListener.onItemClick(v, getAdapterPosition());
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  getRatingsdata();
    }
}