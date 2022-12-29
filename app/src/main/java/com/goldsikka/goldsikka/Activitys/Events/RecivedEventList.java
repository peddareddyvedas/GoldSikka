package com.goldsikka.goldsikka.Activitys.Events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goldsikka.goldsikka.Activitys.Predict_price.BaseViewHolder;
import com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;

public class RecivedEventList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llrv)
    LinearLayout llrv;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rvrecivedeventlist)
    RecyclerView rvrecivedeventlist;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;


    ArrayList<EventModel> arrayList;
    RecivedEventAdapter adapter;
    ApiDao apiDao;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage;
    private boolean isLoading = false;
    int itemCount;
    String fromapi, fromtotal;

    int page_no;
    int next_page;

    TextView unameTv, uidTv, titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recived_event_list);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Invited List");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Invited List");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        intilizerecyclerview();
        loadsentsdetails();

    }

    @Override
    public void onBackPressed() {
        //  NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    public void intilizerecyclerview() {
        swipeRefresh.setOnRefreshListener(this);
        rvrecivedeventlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvrecivedeventlist.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        rvrecivedeventlist.addItemDecoration(decoration);
        arrayList = new ArrayList<>();
        adapter = new RecivedEventAdapter(this, arrayList, this);
        rvrecivedeventlist.setAdapter(adapter);

        rvrecivedeventlist.addOnScrollListener(new PaginationListener(linearLayoutManager) {

            protected void loadMoreItems() {
                isLoading = true;
                // currentPage++;

                if (totalPage != page_no) {
                    if (!NetworkUtils.isConnected(RecivedEventList.this)) {
                        ToastMessage.onToast(RecivedEventList.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    } else {
                        Apicall_page();

                    }
                } else {
                    adapter.removeLoading();
                    //  Toast.makeText(Passbook_Activity.this, "Nodata", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    public void loadsentsdetails() {

        arrayList.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<EventModel> getdetails = apiDao.recivedeventlist("Bearer " + AccountUtils.getAccessToken(this));
            getdetails.enqueue(new Callback<EventModel>() {
                @Override
                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode dd", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        List<EventModel> list = response.body().getResult();
                        EventModel modelnext = response.body();
                        dialog.dismiss();
                        if (list.size() != 0) {

                            page_no = modelnext.getCurrent_page();

                            next_page = page_no + 1;

                            Log.e("PageNo", String.valueOf(page_no));
                            Log.e("Next page + ", String.valueOf(next_page));

                            totalPage = modelnext.getLast_page();

                            for (EventModel model : list) {
                                final ArrayList<EventModel> items = new ArrayList<>();

                                linearlayout.setVisibility(View.GONE);
                                llrv.setVisibility(View.VISIBLE);
//                                arrayList.add(model);
//                                adapter.notifyDataSetChanged();

                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        items.add(model);
                                        //}
                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();
//

                                        if (currentPage != PAGE_START)
                                            adapter.removeLoading();
                                        adapter.addItems(items);

                                        swipeRefresh.setRefreshing(false);

                                        // check weather is last page or not
                                        if (currentPage < next_page) {

                                        } else {
                                            isLastPage = true;
                                        }
                                        isLoading = false;

                                    }
                                }, 100);
                            }

                        } else {
                            linearlayout.setVisibility(View.VISIBLE);
                            llrv.setVisibility(View.GONE);
                        }


                    } else {
                        linearlayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        //   ToastMessage.onToast(RecivedEventList.this,"Technical issue",ToastMessage.ERROR);
                    }
                }
                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                    //  ToastMessage.onToast(RecivedEventList.this,"We have some issue",ToastMessage.ERROR);
                }
            });
        }
    }

    public void Apicall_page() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<EventModel> getdetails = apiDao.recivedeventlistnextpage("Bearer " + AccountUtils.getAccessToken(this), String.valueOf(next_page));
            getdetails.enqueue(new Callback<EventModel>() {
                @Override
                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode dd", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        List<EventModel> list = response.body().getResult();
                        EventModel modelnext = response.body();
                        dialog.dismiss();
                        if (list.size() != 0) {

                            page_no = modelnext.getCurrent_page();

                            next_page = page_no + 1;

                            Log.e("PageNo", String.valueOf(page_no));
                            Log.e("Next page + ", String.valueOf(next_page));

                            totalPage = modelnext.getLast_page();

                            for (EventModel model : list) {
                                final ArrayList<EventModel> items = new ArrayList<>();

                                linearlayout.setVisibility(View.GONE);
                                llrv.setVisibility(View.VISIBLE);

//                                arrayList.add(model);
//                                adapter.notifyDataSetChanged();

                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        items.add(model);
                                        //}
                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();
//

                                        if (currentPage != PAGE_START)
                                            adapter.removeLoading();
                                        adapter.addItems(items);

                                        swipeRefresh.setRefreshing(false);

                                        // check weather is last page or not
                                        if (currentPage < next_page) {

                                        } else {
                                            isLastPage = true;
                                        }
                                        isLoading = false;

                                    }
                                }, 100);
                            }

                        } else {
                            linearlayout.setVisibility(View.VISIBLE);
                            llrv.setVisibility(View.GONE);
                        }


                    } else {
                        linearlayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        //   ToastMessage.onToast(RecivedEventList.this,"Technical issue",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                    // ToastMessage.onToast(RecivedEventList.this,"We have some issue",ToastMessage.ERROR);
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
            adapter.clear();
            loadsentsdetails();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        EventModel model = arrayList.get(position);
        if (view.getId() == R.id.cditemclick) {
            Intent initent = new Intent(this, RecivedEventInfo.class);
            initent.putExtra("eventid", model.getEvent_id());
            startActivity(initent);
        }
    }


    public class RecivedEventAdapter extends RecyclerView.Adapter<BaseViewHolder> {


        private static final int VIEW_TYPE_LOADING = 0;
        private static final int VIEW_TYPE_NORMAL = 1;
        private boolean isLoaderVisible = false;

        Context context;
        ArrayList<EventModel> list;
        OnItemClickListener clickListener;

        public RecivedEventAdapter(Context context, ArrayList<EventModel> arrayListlist, OnItemClickListener clickListener) {
            this.clickListener = clickListener;
            this.context = context;
            this.list = arrayListlist;
        }

        @NonNull
        @Override
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_NORMAL:
                    return new ViewHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.recivedeventitemlist, parent, false));
                case VIEW_TYPE_LOADING:
                    return new ProgressHolder(
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

        public void addItems(ArrayList<EventModel> postItems) {
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
            TextView tvdiscription;
            LinearLayout cditemclick;

            ViewHolder(View itemView) {
                super(itemView);
                tvdiscription = itemView.findViewById(R.id.tvdescription);
                cditemclick = itemView.findViewById(R.id.cditemclick);
                cditemclick.setOnClickListener(this);

                itemView.setOnClickListener(this);
            }

            protected void clear() {

            }

            public void onBind(int position) {
                super.onBind(position);
                EventModel model = list.get(position);
                tvdiscription.setText(model.getDesc());

            }

            @Override
            public void onClick(View v) {
                clickListener.onItemClick(v, getAdapterPosition());
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
}