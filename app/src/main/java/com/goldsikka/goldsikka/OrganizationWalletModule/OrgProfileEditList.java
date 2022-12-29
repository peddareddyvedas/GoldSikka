package com.goldsikka.goldsikka.OrganizationWalletModule;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
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

public class OrgProfileEditList extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rveditlist)
    RecyclerView rveditlist;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llorgetplist)
    LinearLayout llorgetplist;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage;
    private boolean isLoading = false;
    int itemCount;

    ApiDao apiDao;
    int page_no;
    int next_page;

    ArrayList<ORGModel> arrayList ;
    EditProfileAdapter adapter;

    TextView unameTv, uidTv, titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_profile_edit_list);

        ButterKnife.bind(this);


        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Profile");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Profile");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        intilizerecyclerview();
        getcouponslist();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }
    public void intilizerecyclerview(){
        swipeRefresh.setOnRefreshListener(this);
        rveditlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rveditlist.setLayoutManager(linearLayoutManager);
        rveditlist.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        rveditlist.addItemDecoration(decoration);
        arrayList = new ArrayList<>();
        adapter = new EditProfileAdapter(this,arrayList);
        rveditlist.setAdapter(adapter);

        rveditlist.addOnScrollListener(new PaginationListener(linearLayoutManager) {

            protected void loadMoreItems() {
                isLoading = true;
                // currentPage++;

                if(totalPage != page_no){
                    if (!NetworkUtils.isConnected(    OrgProfileEditList.this)){
                        ToastMessage.onToast(    OrgProfileEditList.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    }else {
                        ApiNextPage();
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

    public void getcouponslist(){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            swipeRefresh.setRefreshing(false);
        }else {
            arrayList.clear();
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<ORGModel> getcoupons = apiDao.getEditplist("Bearer "+AccountUtils.getAccessToken(this));
            getcoupons.enqueue(new Callback<ORGModel>() {
                @Override
                public void onResponse(Call<ORGModel> call, Response<ORGModel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode list", String.valueOf(statuscode));

                    if (statuscode == HttpsURLConnection.HTTP_OK){
                        dialog.dismiss();
                        List<ORGModel> list = response.body().getResult();
                        ORGModel ORGModel = response.body();
                        if (list.size()!= 0){

                            page_no = ORGModel.getCurrent_page();

                            next_page = page_no + 1;

                            Log.e("PageNo", String.valueOf(page_no));
                            Log.e("Next page + ", String.valueOf(next_page));

                            totalPage = ORGModel.getLast_page();
                            Log.e("lastpage", String.valueOf(totalPage));
                            dialog.dismiss();
                            linearlayout.setVisibility(View.GONE);
                            llorgetplist.setVisibility(View.VISIBLE);
                            for (ORGModel model : list){
                                final ArrayList<ORGModel> items = new ArrayList<>();
                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        items.add(model);
                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                        if (currentPage != PAGE_START)
                                            adapter.removeLoading();
                                        adapter.addItems(items);

                                        swipeRefresh.setRefreshing(false);

                                        // check weather is last page or not
                                        if (currentPage < next_page) {
                                            // adapter.addLoading();
                                            // isLastPage = true;
                                        } else {
                                            isLastPage = true;
                                        }
                                        isLoading = false;

                                    }
                                }, 100);

                            }
                        }else {
                            linearlayout.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                            Toast.makeText(    OrgProfileEditList.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        dialog.dismiss();
                      //  ToastMessage.onToast(    OrgProfileEditList.this,"Technical issue try after some time",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<ORGModel> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("coupon list fail",t.toString());
                   // ToastMessage.onToast(    OrgProfileEditList.this,"We have some issues try after some time",ToastMessage.ERROR);
                }
            });

        }
    }
    public void ApiNextPage(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<ORGModel> getcoupons = apiDao.getnextEditplist("Bearer "+AccountUtils.getAccessToken(this), String.valueOf(next_page));
        getcoupons.enqueue(new Callback<ORGModel>() {
            @Override
            public void onResponse(Call<ORGModel> call, Response<ORGModel> response) {
                int statuscode = response.code();
                if (statuscode == HttpsURLConnection.HTTP_OK){
                    dialog.dismiss();
                    List<ORGModel> list = response.body().getResult();
                    ORGModel ORGModel = response.body();
                    if (list.size()!= 0){

                        page_no = ORGModel.getCurrent_page();

                        next_page = page_no + 1;

                        Log.e("PageNo", String.valueOf(page_no));
                        Log.e("Next page + ", String.valueOf(next_page));

                        totalPage = ORGModel.getLast_page();
                        Log.e("lastpage", String.valueOf(totalPage));
                        dialog.dismiss();
                        linearlayout.setVisibility(View.GONE);
                        llorgetplist.setVisibility(View.VISIBLE);
                        for (ORGModel model : list){
                            final ArrayList<ORGModel> items = new ArrayList<>();
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    items.add(model);
                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                    if (currentPage != PAGE_START)
                                        adapter.removeLoading();
                                    adapter.addItems(items);

                                    swipeRefresh.setRefreshing(false);

                                    // check weather is last page or not
                                    if (currentPage < next_page) {
                                        // adapter.addLoading();
                                        // isLastPage = true;
                                    } else {
                                        isLastPage = true;
                                    }
                                    isLoading = false;

                                }
                            }, 100);

                        }
                    }else {
                        linearlayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        Toast.makeText( OrgProfileEditList.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    dialog.dismiss();
                  //  ToastMessage.onToast(   OrgProfileEditList.this,"Technical issue try after some time",ToastMessage.ERROR);
                }
            }

            @Override
            public void onFailure(Call<ORGModel> call, Throwable t) {
                dialog.dismiss();
               // ToastMessage.onToast(   OrgProfileEditList.this,"We have some issues try after some time",ToastMessage.ERROR);
            }
        });

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
            getcouponslist();
        }
    }
}