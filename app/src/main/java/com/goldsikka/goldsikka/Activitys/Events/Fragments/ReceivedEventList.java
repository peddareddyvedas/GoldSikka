package com.goldsikka.goldsikka.Activitys.Events.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.goldsikka.goldsikka.Activitys.Events.EventModel;
import com.goldsikka.goldsikka.Activitys.Events.RecivedEventAdapter;
import com.goldsikka.goldsikka.Activitys.Events.RecivedEventInfo;
import com.goldsikka.goldsikka.Activitys.Events.RecivedEventList;
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

public class ReceivedEventList extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.llrv)
    LinearLayout  llrv;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.rvrecivedeventlist)
    RecyclerView rvrecivedeventlist;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;


    ArrayList<EventModel> arrayList;
    RecivedEventAdapter adapter;
    ApiDao apiDao;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage;
    private boolean isLoading = false;
    int itemCount;
    String fromapi,fromtotal;

    int page_no;
    int next_page;

    private Activity activity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_recived_event_list,container,false);
        ButterKnife.bind(view,activity);

        initviews(view);
        intilizerecyclerview(view);
        loadsentsdetails();

        return view;
    }

    public void initviews(View view) {
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        rvrecivedeventlist = view.findViewById(R.id.rvrecivedeventlist);
        linearlayout = view.findViewById(R.id.linearlayout);
        llrv = view.findViewById(R.id.llrv);

    }


    public void intilizerecyclerview(View view){
        swipeRefresh.setOnRefreshListener(this);
        rvrecivedeventlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        rvrecivedeventlist.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL);
        rvrecivedeventlist.addItemDecoration(decoration);
        arrayList = new ArrayList<>();
        adapter = new RecivedEventAdapter(activity,arrayList,this);
        rvrecivedeventlist.setAdapter(adapter);

        rvrecivedeventlist.addOnScrollListener(new PaginationListener(linearLayoutManager) {

            protected void loadMoreItems() {
                isLoading = true;
                // currentPage++;

                if(totalPage != page_no){
                    if (!NetworkUtils.isConnected(activity)){
                        ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    }else {
                        Apicall_page();

                    }
                }else{
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

    public void loadsentsdetails(){

        arrayList.clear();
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(activity)){
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<EventModel> getdetails = apiDao.recivedeventlist("Bearer "+AccountUtils.getAccessToken(activity));
            getdetails.enqueue(new Callback<EventModel>() {
                @Override
                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                    int statuscode  = response.code();
                    Log.e("statuscode dd" , String.valueOf(statuscode));
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

                        }else {
                            linearlayout.setVisibility(View.VISIBLE);
                            llrv.setVisibility(View.GONE);
                        }


                    }else {
                        linearlayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                      //  ToastMessage.onToast(activity,"Technical issue",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {
                    Log.e("on fails",t.toString());
                    dialog.dismiss();
                 //   ToastMessage.onToast(activity,"We have some issue",ToastMessage.ERROR);
                }
            });
        }
    }
    public void Apicall_page(){
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(activity)){
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<EventModel> getdetails = apiDao.recivedeventlistnextpage("Bearer "+AccountUtils.getAccessToken(activity),String.valueOf(next_page));
            getdetails.enqueue(new Callback<EventModel>() {
                @Override
                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                    int statuscode  = response.code();
                    Log.e("statuscode dd" , String.valueOf(statuscode));
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

                        }else {
                            linearlayout.setVisibility(View.VISIBLE);
                            llrv.setVisibility(View.GONE);
                        }


                    }else {
                        linearlayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                       // ToastMessage.onToast(activity,"Technical issue",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {
                    Log.e("on fails",t.toString());
                    dialog.dismiss();
                 //   ToastMessage.onToast(activity,"We have some issue",ToastMessage.ERROR);
                }
            });
        }

    }

    @Override
    public void onRefresh() {

        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
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
        switch (view.getId()){
            case R.id.cditemclick:
                Intent initent = new Intent(activity, RecivedEventInfo.class);
                initent.putExtra("eventid",model.getEvent_id());
                startActivity(initent);
                break;
        }
    }
}
