package com.goldsikka.goldsikka.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goldsikka.goldsikka.Activitys.Events.EventModel;
import com.goldsikka.goldsikka.Activitys.Events.Eventlist;
import com.goldsikka.goldsikka.Activitys.MoneyWallet.AddMonet_to_Wallet;
import com.goldsikka.goldsikka.Activitys.Predict_price.BaseViewHolder;
import com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener;
import com.goldsikka.goldsikka.FirebaseMessageReceiver;
import com.goldsikka.goldsikka.Fragments.Schemes.Schemes_usersubscribed_list;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.WelcomeActivity;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;

public class NotificationList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    LinearLayout linearlayout;
    LinearLayout llrv;
    RecyclerView rveventlist;

    ArrayList<EventModel> arrayList;
    NotificationAdapter adapter;
    ApiDao apiDao;
    SwipeRefreshLayout swipeRefresh;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage;
    private boolean isLoading = false;
    int itemCount;
    String fromapi, fromtotal;

    int page_no;
    int next_page;

    TextView tvwalletmoney;

    String Walletamount;
    RelativeLayout backbtn;

    TextView unameTv, uidTv, titleTv;

    shared_preference sharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);


        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Notifications");
        sharedPreference = new shared_preference(this);

        initviews();
        intilizerecyclerview();
        loadeventsdetails();
    }

    @Override
    public void onBackPressed() {
        // NavUtils.navigateUpFromSameTask(this);
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Please Wait....");
//        dialog.setCancelable(false);
//        dialog.show();
//        if (!NetworkUtils.isConnected(this)){
////            dialog.dismiss();
////            new HomeFragment().notificationCount();
//            super.onBackPressed();
//            finish();
//            return;
//        }
//        else {
//            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//            Call<EventModel> getdetails = apiDao.AllNotificationsSeen("Bearer "+AccountUtils.getAccessToken(this));
//            getdetails.enqueue(new Callback<EventModel>() {
//                @Override
//                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
//                    int statuscode  = response.code();
//                    Log.e("statuscode dd" , String.valueOf(statuscode));
//                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == 200) {
////                        dialog.dismiss();
//                        Log.e("sdhbvds", "hdvc");
//                    }else {
////                        dialog.dismiss();
//                        Log.e("dfvdf", "sdvfsd");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<EventModel> call, Throwable t) {
//                    Log.e("on fails",t.toString());
////                    dialog.dismiss();
//                }
//            });
//        }
        super.onBackPressed();
        finish();
    }

    public void initviews() {
        swipeRefresh = findViewById(R.id.swipeRefresh);
        linearlayout = findViewById(R.id.linearlayout);
        llrv = findViewById(R.id.llrv);
        rveventlist = findViewById(R.id.rveventlist);
    }

    public void intilizerecyclerview() {
        swipeRefresh.setOnRefreshListener(this);
        rveventlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rveventlist.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        rveventlist.addItemDecoration(decoration);
        arrayList = new ArrayList<>();

        adapter = new NotificationAdapter(this, arrayList, this);
        rveventlist.setAdapter(adapter);

        rveventlist.addOnScrollListener(new PaginationListener(linearLayoutManager) {

            protected void loadMoreItems() {
                isLoading = true;
                Log.e("Pagination wallet money", "Sucess");
                // currentPage++;

                if (totalPage != page_no) {
                    if (!NetworkUtils.isConnected(NotificationList.this)) {
                        ToastMessage.onToast(NotificationList.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    } else {
                        Apicall_page();

                    }
                } else {
                    adapter.removeLoading();
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

    public void loadeventsdetails() {

        arrayList.clear();
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
            Call<EventModel> getdetails = apiDao.NotificationList("Bearer " + AccountUtils.getAccessToken(this));
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
                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        items.add(model);
                                        //}
                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();

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
                        // ToastMessage.onToast(NotificationList.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                   // ToastMessage.onToast(NotificationList.this, "We have some issue", ToastMessage.ERROR);
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
            Call<EventModel> getdetails = apiDao.NotificationList_nextpage("Bearer " + AccountUtils.getAccessToken(this), String.valueOf(next_page));
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

                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        items.add(model);
                                        //}
                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();

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
                        // ToastMessage.onToast(NotificationList.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                  //  ToastMessage.onToast(NotificationList.this, "We have some issue", ToastMessage.ERROR);
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
            // if (fromapi.equals("getdata")) {
            loadeventsdetails();
            //  }
        }

    }

    @Override
    public void onItemClick(View view, int position) {

        EventModel model = arrayList.get(position);

        seenNotification(model.getId(), model.getType());
    }

    public void seenNotification(String id, String type) {

        Log.e("type", type);

//        ToastMessage.onToast(NotificationList.this, type, ToastMessage.SUCCESS);
//        ToastMessage.onToast(NotificationList.this, id, ToastMessage.SUCCESS);
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
            Call<EventModel> getdetails = apiDao.NotificationSeen("Bearer " + AccountUtils.getAccessToken(this), id);
            getdetails.enqueue(new Callback<EventModel>() {
                @Override
                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode dd", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == 200) {
                        EventModel list = response.body();
                        if (type.equals("MT")) {
                            Intent intent = new Intent(NotificationList.this, AddMonet_to_Wallet.class);
                            startActivity(intent);

                        } else if (type.equals("EV")) {
                            Intent intent = new Intent(NotificationList.this, Eventlist.class);
                            startActivity(intent);

                        } else if (type.equals("GP")) {
                            Intent intent = new Intent(NotificationList.this, Schemes_usersubscribed_list.class);
                            Schemes_usersubscribed_list.isFromgoldtransaction = false;
                            AccountUtils.setSchemeID(NotificationList.this, "2");
                            AccountUtils.setSchemename(NotificationList.this, "GOLD PLUS PLAN");
                            startActivity(intent);
                        } else if (type.equals("TS")) {
                            Intent intent = new Intent(NotificationList.this, Passbook_Activity.class);
                            Passbook_Activity.isFromnotification = false;
                            startActivity(intent);
                        } else if (type.equals("MW")) {
                            Intent intent = new Intent(NotificationList.this, AddMonet_to_Wallet.class);
                            startActivity(intent);
                        } else if (type.equals("ER")) {
                            Intent intent = new Intent(NotificationList.this, Eventlist.class);
                            startActivity(intent);
                        } else {
//                            ToastMessage.onToast(NotificationList.this, type, ToastMessage.SUCCESS);
                            onBackPressed();
                        }

                        dialog.dismiss();

                    } else {

                        dialog.dismiss();

                        //     ToastMessage.onToast(NotificationList.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                   // ToastMessage.onToast(NotificationList.this, "We have some issue", ToastMessage.ERROR);
                }
            });
        }

//        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

    }


    ////////////////////////////////////////////

    public class NotificationAdapter extends RecyclerView.Adapter<BaseViewHolder> {

        private static final int VIEW_TYPE_LOADING = 0;
        private static final int VIEW_TYPE_NORMAL = 1;
        private boolean isLoaderVisible = false;

        Context context;
        ArrayList<EventModel> list;
        OnItemClickListener clickListener;

        public NotificationAdapter(Context context, ArrayList<EventModel> arrayListlist, OnItemClickListener clickListener) {
            this.clickListener = clickListener;
            this.context = context;
            this.list = arrayListlist;
        }

        @NonNull
        @Override
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_NORMAL:
                    return new NotificationAdapter.ViewHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_design, parent, false));
                case VIEW_TYPE_LOADING:
                    return new NotificationAdapter.ProgressHolder(
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
            TextView tvdate, tvmessage, tvtitle;
            // TextView ivedit,ivdelete,tvsend,tvsendlist;
            LinearLayout llbackground;

            ViewHolder(View itemView) {
                super(itemView);

                tvmessage = itemView.findViewById(R.id.notification_message);
                tvdate = itemView.findViewById(R.id.notification_date);
                tvtitle = itemView.findViewById(R.id.notification_title);
                llbackground = itemView.findViewById(R.id.notification_BG);
                itemView.setOnClickListener(this);
            }

            protected void clear() {

            }


            public void onBind(int position) {
                super.onBind(position);

                EventModel model = list.get(position);

                tvdate.setText(model.getCreated_date());
                tvmessage.setText(model.getMessage());
                tvtitle.setText(model.getTitle());

                if (!model.isSeen()) {
                    llbackground.setBackgroundColor(Color.parseColor("#e1f1ff"));
                }

                if (model.getTitle().equals("Login with new device")) {
                    // onlogout();
                }

//            if (!model.isQR()){
//                tvgetqrcode.setVisibility(View.GONE);
//            }else {
//                tvgetqrcode.setVisibility(View.VISIBLE);
//
//            }
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

    public void onlogout() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<List<Listmodel>> initlogout = apiDao.get_logout("Bearer " + AccountUtils.getAccessToken(this));
            initlogout.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                        sharedPreference.WriteLoginStatus(false);
                        Intent intent = new Intent(NotificationList.this, WelcomeActivity.class);
                        ToastMessage.onToast(getApplicationContext(), "All ready you are login in another device", ToastMessage.SUCCESS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        //   ToastMessage.onToast(FirebaseMessageReceiver.this, "Technical issue", ToastMessage.ERROR);
                        // Log.e("responce code", String.valueOf(response.code()));
                    }

                }

                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                    Log.e("logout fail", t.toString());
                    //  ToastMessage.onToast(FirebaseMessageReceiver.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

}