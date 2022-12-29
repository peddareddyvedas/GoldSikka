package com.goldsikka.goldsikka.Activitys.Coupons;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;

public class CouponsList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rvcouponlist)
    RecyclerView rvcouponlist;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etcouponcoode)
    EditText etcouponcoode;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llcouponlist)
    LinearLayout llcouponlist;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvofflineapply)
    TextView tvapply;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvofflineerror)
    TextView tvofflineerror;


    ArrayList<CouponsModel> arrayList;
    CouponslistAdapter adapter;

    ApiDao apiDao;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage;
    private boolean isLoading = false;
    int itemCount;
    String stcouponcode;

    int page_no;
    int next_page;
    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.couponlist);

        ButterKnife.bind(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Offers");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Offers");
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        intilizerecyclerview();
        getcouponslist();
    }


    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Toast.makeText(this,"back",Toast.LENGTH_LONG).show();
//        Intent intent=new Intent();
//        intent.putExtra("offeramount","null");
//        intent.putExtra("couponcode","null");
//        intent.putExtra("isCoupon",true);
//        setResult(2,intent);
//
//        finish();
//    }
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent intent = new Intent();
        intent.putExtra("offeramount", "");
        intent.putExtra("couponcode", "gghytt");
        intent.putExtra("minamount", "");
        intent.putExtra("isCoupon", false);
        setResult(2, intent);
        finish();

    }


    @OnClick(R.id.tvofflineapply)
    public void tvcodeapply() {
        tvofflineerror.setVisibility(View.GONE);
        stcouponcode = etcouponcoode.getText().toString().trim();
        if (stcouponcode.isEmpty()) {
            tvofflineerror.setVisibility(View.VISIBLE);
            tvofflineerror.setText("Enter the Coupon Code");
        } else {
            ApplyCouponCode(stcouponcode);
        }
    }

    public void intilizerecyclerview() {
        swipeRefresh.setOnRefreshListener(this);
        rvcouponlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvcouponlist.setLayoutManager(linearLayoutManager);
        rvcouponlist.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        rvcouponlist.addItemDecoration(decoration);
        arrayList = new ArrayList<>();
        adapter = new CouponslistAdapter(this, arrayList, this);
        rvcouponlist.setAdapter(adapter);

        rvcouponlist.addOnScrollListener(new PaginationListener(linearLayoutManager) {

            protected void loadMoreItems() {
                isLoading = true;
                // currentPage++;

                if (totalPage != page_no) {
                    if (!NetworkUtils.isConnected(CouponsList.this)) {
                        ToastMessage.onToast(CouponsList.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    } else {
                        ApiNextPage();
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

    public void getcouponslist() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            swipeRefresh.setRefreshing(false);
        } else {
            arrayList.clear();
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<CouponsModel> getcoupons = apiDao.getcouponlist("Bearer " + AccountUtils.getAccessToken(this));
            getcoupons.enqueue(new Callback<CouponsModel>() {
                @Override
                public void onResponse(Call<CouponsModel> call, Response<CouponsModel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode list", String.valueOf(statuscode));

                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        List<CouponsModel> list = response.body().getResult();
                        CouponsModel couponsModel = response.body();
                        if (list.size() != 0) {

                            page_no = couponsModel.getCurrent_page();

                            next_page = page_no + 1;

                            Log.e("PageNo", String.valueOf(page_no));
                            Log.e("Next page + ", String.valueOf(next_page));

                            totalPage = couponsModel.getLast_page();
                            Log.e("lastpage", String.valueOf(totalPage));
                            dialog.dismiss();
                            linearlayout.setVisibility(View.GONE);
                            llcouponlist.setVisibility(View.VISIBLE);
                            for (CouponsModel model : list) {
                                final ArrayList<CouponsModel> items = new ArrayList<>();
                                new Handler().postDelayed(new Runnable()
                                {

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
                        } else {
                            linearlayout.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                            Toast.makeText(CouponsList.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dialog.dismiss();
                      //  ToastMessage.onToast(CouponsList.this, "Technical issue try after some time", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<CouponsModel> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("coupon list fail", t.toString());
                    ToastMessage.onToast(CouponsList.this, "We have some issues try after some time", ToastMessage.ERROR);
                }
            });

        }
    }

    public void ApiNextPage() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<CouponsModel> getcoupons = apiDao.getnextcouponlist("Bearer " + AccountUtils.getAccessToken(this), String.valueOf(next_page));
        getcoupons.enqueue(new Callback<CouponsModel>() {
            @Override
            public void onResponse(Call<CouponsModel> call, Response<CouponsModel> response) {
                int statuscode = response.code();
                Log.e("Statuse code coupons", String.valueOf(statuscode));
                if (statuscode == HttpsURLConnection.HTTP_OK) {
                    dialog.dismiss();
                    List<CouponsModel> list = response.body().getResult();
                    CouponsModel couponsModel = response.body();
                    if (list.size() != 0) {

                        page_no = couponsModel.getCurrent_page();

                        next_page = page_no + 1;

                        Log.e("PageNo", String.valueOf(page_no));
                        Log.e("Next page + ", String.valueOf(next_page));

                        totalPage = couponsModel.getLast_page();
                        Log.e("lastpage", String.valueOf(totalPage));
                        dialog.dismiss();
                        linearlayout.setVisibility(View.GONE);
                        llcouponlist.setVisibility(View.VISIBLE);
                        for (CouponsModel model : list) {
                            final ArrayList<CouponsModel> items = new ArrayList<>();
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
                    } else {
                        linearlayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        Toast.makeText(CouponsList.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                   // ToastMessage.onToast(CouponsList.this, "Technical issue try after some time", ToastMessage.ERROR);
                }
            }

            @Override
            public void onFailure(Call<CouponsModel> call, Throwable t) {
                dialog.dismiss();
                ToastMessage.onToast(CouponsList.this, "We have some issues try after some time", ToastMessage.ERROR);
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

    @Override
    public void onItemClick(View view, int position) {

        CouponsModel model = arrayList.get(position);
        if (view.getId() == R.id.tvapply) {
            ApplyOnlineCouponCode(model.getCoupon(), model.getAmount(), model.getMinimum_transaction_amount());
        }
    }

    public void ApplyOnlineCouponCode(String couponcode, String amount, String minamount) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            swipeRefresh.setRefreshing(false);
        } else {
            dialog.dismiss();
//            Intent intent = new Intent(this,Buy_Digitalgold.class);
//            intent.putExtra("offeramount",amount);
//            intent.putExtra("couponcode",couponcode);
//            intent.putExtra("couponfrom","onlinecoupon");
//            startActivity(intent);

            Intent intent = new Intent();
            intent.putExtra("offeramount", amount);
            intent.putExtra("couponcode", couponcode);
            intent.putExtra("isCoupon", false);
            intent.putExtra("minamount", minamount);
            setResult(2, intent);
            finish();


        }
    }

    public void ApplyCouponCode(String couponcode) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            swipeRefresh.setRefreshing(false);
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<CouponsModel> getcouponvalue = apiDao.offlinecoupon("Bearer " + AccountUtils.getAccessToken(this), couponcode);
            getcouponvalue.enqueue(new Callback<CouponsModel>() {
                @Override
                public void onResponse(Call<CouponsModel> call, Response<CouponsModel> response) {
                    int statuscode = response.code();
                    CouponsModel model = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("offeramount", model.getAmount());
                        intent.putExtra("couponcode", model.getCoupon());
                        intent.putExtra("minamount", model.getMinimum_transaction_amount());
                        intent.putExtra("isCoupon", false);
                        setResult(2, intent);
                        finish();
                    } else {
                        dialog.dismiss();
                        tvofflineerror.setVisibility(View.GONE);

                        assert response.errorBody() != null;

                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            ToastMessage.onToast(CouponsList.this, st, ToastMessage.ERROR);
                            JSONObject er = jObjError.getJSONObject("errors");
                            try {
                                JSONArray password = er.getJSONArray("coupon");
                                for (int i = 0; i < password.length(); i++) {
                                    tvofflineerror.setVisibility(View.VISIBLE);
                                    tvofflineerror.setText(password.getString(i));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(Call<CouponsModel> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(CouponsList.this, "we have some issues please try after some time", ToastMessage.ERROR);
                }
            });

        }
    }

}
