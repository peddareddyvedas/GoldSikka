package com.goldsikka.goldsikka.Fragments.Schemes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener;
import com.goldsikka.goldsikka.Adapter.TicketAdapter;
import com.goldsikka.goldsikka.Adapter.mygoldlist_Adapter;
import com.goldsikka.goldsikka.Fragments.Mygold2020.Get_scheme;
import com.goldsikka.goldsikka.Models.Enquiryformmodel;
import com.goldsikka.goldsikka.Models.Enquiryformmodel;
import com.goldsikka.goldsikka.Models.Enquiryformmodel;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;

public class TicketList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rvtickitlist)
    RecyclerView rvtickitlist;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    LinearLayout ll_rv;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    
    ArrayList<Enquiryformmodel> arrayList;
    
    TicketAdapter adapter;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage;
    private boolean isLoading = false;
    int itemCount;
    ApiDao apiDao;
    
    int page_no;
    int next_page;
    String schemeid;
TextView unameTv, uidTv, titleTv;
RelativeLayout backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);
        ButterKnife.bind(this);
        swipeRefresh.setOnRefreshListener(this);
        ll_rv = findViewById(R.id.ll_rv);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle("Ticket Details");
        backbtn = findViewById(R.id.backbtn);
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Ticket Details");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // toolbar.setTitleTextColor(getColor(R.color.colorWhite));
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            schemeid = bundle.getString("schemeid");
        }

        initlizeviews();
        getdetails();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }
    public void initlizeviews(){
        rvtickitlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvtickitlist.setLayoutManager(linearLayoutManager);
        rvtickitlist.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(TicketList.this, LinearLayoutManager.VERTICAL);
        rvtickitlist.addItemDecoration(decoration);
        arrayList = new ArrayList<>();
        adapter = new TicketAdapter (TicketList.this,arrayList);
        rvtickitlist.setAdapter(adapter);
        rvtickitlist.addOnScrollListener(new PaginationListener(linearLayoutManager) {

            protected void loadMoreItems() {
                isLoading = true;
                // currentPage++;

                if(totalPage != page_no){
                    if (!NetworkUtils.isConnected(TicketList.this)){
                        ToastMessage.onToast(TicketList.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
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

    public void getdetails(){
        arrayList.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        }else {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Enquiryformmodel> schemesModelCall = apiDao.getticketlist("Bearer " + AccountUtils.getAccessToken(this),schemeid);
            schemesModelCall.enqueue(new Callback<Enquiryformmodel>() {
                @Override
                public void onResponse(@NotNull Call<Enquiryformmodel> call, @NotNull Response<Enquiryformmodel> response) {
                    int statuscode = response.code();
                    assert response.body() != null;
                    List<Enquiryformmodel> list = response.body().getResult();
                    Enquiryformmodel body = response.body();
                    Log.e("Status Code", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {
                               // dialog.dismiss();

                        if (list.size() != 0) {
                            page_no = body.getCurrent_page();

                            next_page = page_no + 1;

                            Log.e("PageNo", String.valueOf(page_no));
                            Log.e("Next page + ", String.valueOf(next_page));

                            totalPage = body.getLast_page();
                            for (Enquiryformmodel listmodel : list) {

                                final ArrayList<Enquiryformmodel> items = new ArrayList<>();

                                String id = listmodel.getId();
                                Log.e("idsto", id);
                                dialog.dismiss();

                                ll_rv.setVisibility(View.VISIBLE);
                                linearlayout.setVisibility(View.GONE);
//                                arrayList.add(listmodel);
//                                adapter.notifyDataSetChanged();
                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        items.add(listmodel);
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
                                            Log.e("current page", String.valueOf(currentPage));
                                        } else {
                                            isLastPage = true;
                                        }
                                        isLoading = false;

                                    }
                                }, 100);
                            }

                        }else {
                            swipeRefresh.setRefreshing(false);
                            dialog.dismiss();
                            ll_rv.setVisibility(View.GONE);
                            linearlayout.setVisibility(View.VISIBLE);
                        }

                    } else {

                        dialog.dismiss();
                        ll_rv.setVisibility(View.GONE);
                        linearlayout.setVisibility(View.VISIBLE);
                        swipeRefresh.setRefreshing(false);
                    }


                }


                @Override
                public void onFailure(@NotNull Call<Enquiryformmodel> call, @NotNull Throwable t) {
                    Log.e("Failure", t.toString());
                    swipeRefresh.setRefreshing(false);
                  //  Toast.makeText(TicketList.this, "Sorry! We Have some technical issue", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
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
        }else {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Enquiryformmodel> schemesModelCall = apiDao.getticketnextpage("Bearer " + AccountUtils.getAccessToken(this),
                    schemeid, String.valueOf(next_page));
            schemesModelCall.enqueue(new Callback<Enquiryformmodel>() {
                @Override
                public void onResponse(@NotNull Call<Enquiryformmodel> call, @NotNull Response<Enquiryformmodel> response) {
                    int statuscode = response.code();
                    assert response.body() != null;
                    List<Enquiryformmodel> list = response.body().getResult();
                    Enquiryformmodel body = response.body();
                    Log.e("Status Code", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {


                        if (list.size() != 0) {
                            page_no = body.getCurrent_page();

                            next_page = page_no + 1;

                            Log.e("PageNo", String.valueOf(page_no));
                            Log.e("Next page + ", String.valueOf(next_page));

                            totalPage = body.getLast_page();
                            for (Enquiryformmodel listmodel : list) {

                                final ArrayList<Enquiryformmodel> items = new ArrayList<>();

                                String id = listmodel.getId();
                                Log.e("idsto", id);
                                dialog.dismiss();

                                ll_rv.setVisibility(View.VISIBLE);
                                linearlayout.setVisibility(View.GONE);
                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        items.add(listmodel);

                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                        if (currentPage != PAGE_START)
                                            adapter.removeLoading();
                                        adapter.addItems(items);

                                        swipeRefresh.setRefreshing(false);

                                        // check weather is last page or not
                                        if (currentPage < next_page) {
                                            Log.e("current page", String.valueOf(currentPage));
                                        } else {
                                            isLastPage = true;
                                        }
                                        isLoading = false;

                                    }
                                }, 100);
                            }

                        }
                        else {

                            dialog.dismiss();
                            ll_rv.setVisibility(View.GONE);
                            linearlayout.setVisibility(View.VISIBLE);
                            swipeRefresh.setRefreshing(false);
                        }
                    } else {

                        dialog.dismiss();
                        ll_rv.setVisibility(View.GONE);
                        linearlayout.setVisibility(View.VISIBLE);
                        swipeRefresh.setRefreshing(false);
                    }


                }


                @Override
                public void onFailure(@NotNull Call<Enquiryformmodel> call, @NotNull Throwable t) {
                    Log.e("Failure", t.toString());
                 //   Toast.makeText(TicketList.this, "Sorry! We Have some technical issue", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
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
          getdetails();
        }
    }
}