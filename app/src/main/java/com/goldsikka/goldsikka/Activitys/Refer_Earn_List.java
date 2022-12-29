package com.goldsikka.goldsikka.Activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goldsikka.goldsikka.Activitys.Events.EventModel;
import com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener;
import com.goldsikka.goldsikka.Adapter.ReferListAdapter;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;

public class Refer_Earn_List extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    LinearLayout linearlayout;
    LinearLayout  llrv;
    RecyclerView rveventlist;

    ArrayList<EventModel> arrayList;
    ReferListAdapter adapter;
    ApiDao apiDao;
    SwipeRefreshLayout swipeRefresh;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage;
    private boolean isLoading = false;
    int itemCount;
    String fromapi,fromtotal;

    int page_no;
    int next_page;

    TextView tvwalletmoney;

    String Walletamount;
TextView unameTv, uidTv, titleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_earn_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Earning List");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Earning List");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initviews();
        intilizerecyclerview();
        loadeventsdetails();
    }

    @Override
    public void onBackPressed() {
        // NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                finish();
                return false;

        }

        return super.onOptionsItemSelected(item);
    }

    public void initviews() {
        swipeRefresh = findViewById(R.id.swipeRefresh);
        linearlayout = findViewById(R.id.linearlayout);
        llrv = findViewById(R.id.llrv);
        rveventlist = findViewById(R.id.rveventlist);
    }

    public void intilizerecyclerview(){
        swipeRefresh.setOnRefreshListener(this);
        rveventlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rveventlist.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        rveventlist.addItemDecoration(decoration);
        arrayList = new ArrayList<>();

        adapter = new ReferListAdapter(this,arrayList,this);
        rveventlist.setAdapter(adapter);

        rveventlist.addOnScrollListener(new PaginationListener(linearLayoutManager) {

            protected void loadMoreItems() {
                isLoading = true;
                Log.e("Pagination wallet money","Sucess");
                // currentPage++;

                if(totalPage != page_no){
                    if (!NetworkUtils.isConnected(Refer_Earn_List.this)){
                        ToastMessage.onToast(Refer_Earn_List.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    }else {
                        Apicall_page();

                    }
                }else{
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
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<EventModel> getdetails = apiDao.EarnedList("Bearer "+AccountUtils.getAccessToken(this));
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

                        }else {
                            linearlayout.setVisibility(View.VISIBLE);
                            llrv.setVisibility(View.GONE);
                        }


                    }else {
                        linearlayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                      //  ToastMessage.onToast(Refer_Earn_List.this,"Technical issue",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {
                    Log.e("on fails",t.toString());
                    dialog.dismiss();
                  //  ToastMessage.onToast(Refer_Earn_List.this,"We have some issue",ToastMessage.ERROR);
                }
            });
        }
    }

    public void Apicall_page(){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<EventModel> getdetails = apiDao.EarnedList_nextpage("Bearer "+AccountUtils.getAccessToken(this), String.valueOf(next_page));
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

                        }else {
                            linearlayout.setVisibility(View.VISIBLE);
                            llrv.setVisibility(View.GONE);
                        }


                    }else {
                        linearlayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                      // ToastMessage.onToast(Refer_Earn_List.this,"Technical issue",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {
                    Log.e("on fails",t.toString());
                    dialog.dismiss();
                   // ToastMessage.onToast(Refer_Earn_List.this,"We have some issue",ToastMessage.ERROR);
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

    }
}