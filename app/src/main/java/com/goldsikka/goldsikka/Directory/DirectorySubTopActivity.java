package com.goldsikka.goldsikka.Directory;

import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Activitys.LocationTracker;
import com.goldsikka.goldsikka.ComingSoon;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectorySubTopActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;
    RecyclerView viewrv;

    DirectorySubTopAdapter viewItemDetailsAdapter;

    List<Listmodel> flist;

    EditText searchview;
    ArrayList<Listmodel> viewlistfilter;
    ArrayList<Listmodel> viewitemlist;
    ArrayList<Listmodel> ratinglist;

    SwipeRefreshLayout swipeRefresh;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage;
    private boolean isLoading = false;
    int itemCount;


    Bundle bundle;
    String id = "";
    ApiDao apiDao;
    String cid;
    String toname;
    String newRating = "5";
    RelativeLayout notfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_sub_top);

        bundle = getIntent().getExtras();
        id = bundle.getString("id");
        Log.e("id", "" + id);
        toname = bundle.getString("store_name");
        Log.e("toname", "" + toname);

        initt();

    }

    private void initt() {
        notfound = findViewById(R.id.notfound);

        viewrv = findViewById(R.id.viewRV);
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Top Listing");
        Log.e("storename", "" + toname);
        backbtn = findViewById(R.id.backbtn);
        searchview = findViewById(R.id.search_view);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable name) {
                viewlistfilter(name.toString());


            }
        });

        LocationTracker.getInstance().fillContext(getApplicationContext());
        LocationTracker.getInstance().startLocation();

        intilizerecyclerview();
        getViewItems(id);


    }


    private void viewlistfilter(String name) {
        viewlistfilter = new ArrayList<>();
        for (Listmodel listmodel : viewitemlist) {
            if (listmodel.getStore_name().toLowerCase().contains(name.toLowerCase())) {
                viewlistfilter.add(listmodel);

            }
            if (viewlistfilter.isEmpty()) {
                viewrv.setVisibility(View.GONE);
                notfound.setVisibility(View.VISIBLE);
            } else {
                notfound.setVisibility(View.GONE);
                viewrv.setVisibility(View.VISIBLE);
                viewItemDetailsAdapter.filterList(viewlistfilter);

            }

        }
    }


    private void intilizerecyclerview() {
        swipeRefresh.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        viewrv.setLayoutManager(linearLayoutManager2);
        viewrv.setHasFixedSize(true);
        viewitemlist = new ArrayList<>();
        viewlistfilter = new ArrayList<>();
        ratinglist = new ArrayList<>();
        viewItemDetailsAdapter = new DirectorySubTopAdapter(this, viewitemlist);
        viewrv.setAdapter(viewItemDetailsAdapter);

    }

    private void getViewItems(String id) {
        viewitemlist.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();

        } else {
            apiDao = ApiClient.getClient("").create(ApiDao.class);
            Call<List<Listmodel>> getcatlist = apiDao.get_toplisting();
            getcatlist.enqueue(new Callback<List<Listmodel>>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                    dialog.dismiss();
                    int statuscode = response.code();
                    Log.e("statuscode", String.valueOf(statuscode));
                    flist = response.body();
                    viewitemlist.clear();
                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("statuscode", String.valueOf(statuscode));
                        if (flist != null) {
                            for (Listmodel listmodel : flist) {

                                viewitemlist.add(listmodel);
                                viewItemDetailsAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.e("catname", "No cats");
                            dialog.dismiss();
                        }
                    } else if (statuscode == 422) {
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    } else {
                        Log.e("catname", "No cats");
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                    dialog.dismiss();

                    Log.e("ughb", String.valueOf(t));
                }
            });
        }

    }

    @Override
    public void onRefresh() {

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            swipeRefresh.setRefreshing(false);

        } else {
            itemCount = 0;
            currentPage = PAGE_START;
            isLastPage = false;
            getViewItems(id);

            swipeRefresh.setRefreshing(false);

        }
    }

    public class DirectorySubTopAdapter extends RecyclerView.Adapter<DirectorySubTopAdapter.ViewHolder> {

        Context context;
        ArrayList<Listmodel> viewitemlist;


        public DirectorySubTopAdapter(Context context, ArrayList<Listmodel> viewitemlist) {
            this.context = context;
            this.viewitemlist = viewitemlist;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.activity_directory_view_details_adapter, parent, false);


            return new ViewHolder(view);
        }


        @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Listmodel listmodel = viewitemlist.get(position);

            String name = listmodel.getStore_name();
            String image = listmodel.getImage();
            String mobile = listmodel.getMobile();
            String tagss = listmodel.getTags();
            holder.tv_tags.setText(tagss);

            if (listmodel.getTags() != null) {
                holder.tv_tags.setVisibility(View.VISIBLE);
            } else {
                holder.tv_tags.setVisibility(View.GONE);
            }
            String ratingtv = listmodel.getRating();
            holder.tv_category.setText(name);
            if (listmodel.getRating() != null) {
                holder.ratinglayout.setVisibility(View.VISIBLE);

            } else {
                holder.ratinglayout.setVisibility(View.GONE);

            }
            holder.ratingtext.setText(ratingtv);
            String open = listmodel.getStore_open_timings();
            String close = listmodel.getStore_close_timings();

            holder.onPostExecute(context, viewitemlist);

            try {
                String _24HourTime = close;
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                Date _24HourDt = _24HourSDF.parse(_24HourTime);
                System.out.println(_12HourSDF.format(_24HourDt));
                holder.tv_closetime.setText(_12HourSDF.format(_24HourDt));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String _24HourTime = open;
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                Date _24HourDt = _24HourSDF.parse(_24HourTime);
                System.out.println(_12HourSDF.format(_24HourDt));
                holder.tv_opentime.setText(_12HourSDF.format(_24HourDt));
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.onPostExecute(context, viewitemlist);

            if (listmodel.getImage() != null) {

                Picasso.with(context.getApplicationContext()).load(image).into(holder.iv_categoryimg);

                try {
                    Picasso.with(context).load(image).into(holder.iv_categoryimg);
                } catch (Exception ignored) {
                    Picasso.with(context).load(R.drawable.background_image).into(holder.iv_categoryimg);
                }

            } else {
                Picasso.with(context).load(R.drawable.background_image).into(holder.iv_categoryimg);
            }
            holder.listcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DirectoryStoreDetails.class);
                    intent.putExtra("id", listmodel.getId());
                    intent.putExtra("store_name", listmodel.getStore_name());
                    intent.putExtra("mobile", listmodel.getMobile());
                    intent.putExtra("latitude", listmodel.getLatitude());
                    intent.putExtra("longitude", listmodel.getLongitude());
                    intent.putExtra("status", listmodel.getStatus());
                    context.startActivity(intent);
                }
            });
            holder.callnowbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mobile));
                    context.startActivity(intent);
                }
            });

            holder.diections.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String latitude, logitude;
                    latitude = String.valueOf(LocationTracker.getInstance().currentLocation.getLatitude());
                    logitude = String.valueOf(LocationTracker.getInstance().currentLocation.getLongitude());

                    String location = latitude + "," + logitude;

                    String serverlatitude, serverlogitude;
                    serverlatitude = listmodel.getLatitude();
                    serverlogitude = listmodel.getLongitude();

//                    String getlocaction = serverlatitude + "," + serverlogitude;

                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + serverlatitude + "," + serverlogitude);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });

        }

        @Override
        public int getItemCount() {

            return viewitemlist.size();
        }

        public void filterList(ArrayList<Listmodel> viewlistfilter) {
            viewitemlist = viewlistfilter;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView iv_categoryimg;
            TextView tv_category, tvdiscription, ratingtext, tv_opentime, tv_closetime, tv_tags;
            LinearLayout callnowbtn, diections;
            RecyclerView listbtnRV;
            DirectoryReviewsAdapter directoryReviewsAdapter;
            CardView listcard;
            AppCompatRatingBar ratingBar;
            LinearLayout ratinglayout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ratingBar = itemView.findViewById(R.id.ratings);

                tv_category = itemView.findViewById(R.id.tv_category);
                iv_categoryimg = itemView.findViewById(R.id.ourbrandimg);
                callnowbtn = itemView.findViewById(R.id.callbtn);
                diections = itemView.findViewById(R.id.directions);
                listbtnRV = itemView.findViewById(R.id.listRV);
                listcard = itemView.findViewById(R.id.listcard);
                ratingtext = itemView.findViewById(R.id.ratingtext);
                tv_opentime = itemView.findViewById(R.id.opentimeing);
                tv_closetime = itemView.findViewById(R.id.closetimeing);
                tv_tags = itemView.findViewById(R.id.tv_tags);

                ratinglayout = itemView.findViewById(R.id.ratinglayout);
            }

            @Override
            public void onClick(View v) {
//            itemClickListener.onItemClick(v, getAdapterPosition());
            }

            public void onPostExecute(Context context, ArrayList<Listmodel> viewitemlist) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                listbtnRV.setLayoutManager(linearLayoutManager);
                listbtnRV.setHasFixedSize(true);
                viewlistfilter = new ArrayList<>();
                directoryReviewsAdapter = new DirectoryReviewsAdapter(context, viewitemlist);
                listbtnRV.setAdapter(directoryReviewsAdapter);

                listbtnRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        super.onScrolled(recyclerView, dx, dy);
                    }
                });
            }
        }
    }

    public class DirectoryReviewsAdapter extends RecyclerView.Adapter<DirectoryReviewsAdapter.ViewHolder> {

        Context context;
        ArrayList<Listmodel> viewitemlist;

        public DirectoryReviewsAdapter(Context context, ArrayList<Listmodel> viewitemlist) {
            this.context = context;
            this.viewitemlist = viewitemlist;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.activity_directory_reviews_adapter, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Listmodel listmodel = viewitemlist.get(position);
            String tags = listmodel.getTags();
            // holder.tv_tags.setText(tags);
            if (listmodel.getTags() != null) {
                holder.viewbtn.setVisibility(View.VISIBLE);
                holder.tv_tags.setText(tags);

            } else {
                holder.viewbtn.setVisibility(View.GONE);
                holder.tv_tags.setText("");

            }
        }

        @Override
        public int getItemCount() {

            return viewitemlist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            LinearLayout viewbtn;
            TextView tv_tags;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                viewbtn = itemView.findViewById(R.id.btnlayout);
                tv_tags = itemView.findViewById(R.id.tv_tags);

                viewbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       /* Intent intent = new Intent( context, DirectoryStoreDetails.class );
                        intent.putExtra( "id", "id" );
                        intent.putExtra( "store_name", "storename" );
                        context.startActivity( intent );*/
                    }
                });


            }

            @Override
            public void onClick(View v) {
//            itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}