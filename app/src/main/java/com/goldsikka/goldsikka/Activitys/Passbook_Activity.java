package com.goldsikka.goldsikka.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.goldsikka.goldsikka.Activitys.GetContacts.ContactList;
import com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener;
import com.goldsikka.goldsikka.Adapter.Passbook_Adapter;

import com.goldsikka.goldsikka.Fragments.TransferGold;
import com.goldsikka.goldsikka.Fragments.Reedem_fragment;
import com.goldsikka.goldsikka.Fragments.Sell_Fragment;

import com.goldsikka.goldsikka.Models.PassBookModel;
import com.goldsikka.goldsikka.NewDesignsActivity.GiftContactList;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;

public class Passbook_Activity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, View.OnClickListener {

    RecyclerView rv_passbook;
    LinearLayout linearLayout;

    ArrayList<PassBookModel> passbook_arrayList;
    Passbook_Adapter adapter;
    ApiDao apiDao;
    SwipeRefreshLayout swipeRefresh;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage;
    private boolean isLoading = false;
    int itemCount;
    String fromapi, fromtotal;

    RelativeLayout backbtn;

    int page_no;
    int next_page;
    LinearLayout ll_transfer, ll_redeem;
    Button btn_sellgold;
    TextView tvWalletGold;
    ImageView ivprofile;

    TextView unameTv, uidTv, titleTv;
    //    ll_gift
    Listmodel list;
    String stForm;
    float stRating = 0;
    Dialog dialog1;
    public static boolean isFromnotification = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_passbook_;

    }

    @SuppressLint("NewApi")
    @Override
    protected void initView() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Passbook");

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("My Transactions");

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initviews();
        intilizerecylerview();
        walletgold();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            fromapi = "getdata";
            fromtotal = "totaltransction";
            getdata();
        }


    }

    public void initviews() {

        ll_transfer = findViewById(R.id.ll_transfer);
        ll_transfer.setOnClickListener(this);
//        ll_gift = findViewById(R.id.ll_gift);
//        ll_gift.setOnClickListener(this);
        ll_redeem = findViewById(R.id.ll_redeem);
        ll_redeem.setOnClickListener(this);

        btn_sellgold = findViewById(R.id.btn_sellgold);
        btn_sellgold.setOnClickListener(this);

        tvWalletGold = findViewById(R.id.tvWalletGold);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        rv_passbook = findViewById(R.id.rv_passbook);
        linearLayout = findViewById(R.id.linearlayout);
        swipeRefresh.setOnRefreshListener(this);


        if (isFromnotification) {
            Log.e("isif", "" + isFromnotification);
            successAlert();

        } else {
            Log.e("iselse", "" + isFromnotification);
        }


    }

    public void intilizerecylerview() {

        rv_passbook.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_passbook.setLayoutManager(linearLayoutManager);
        rv_passbook.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        rv_passbook.addItemDecoration(decoration);
        passbook_arrayList = new ArrayList<>();
        adapter = new Passbook_Adapter(this, passbook_arrayList, this);
        rv_passbook.setAdapter(adapter);

        rv_passbook.addOnScrollListener(new PaginationListener(linearLayoutManager) {

            protected void loadMoreItems() {
//                Log.e("pagination passbbok","Sucess");
                isLoading = true;
                // currentPage++;

                if (totalPage != page_no) {
                    if (!NetworkUtils.isConnected(Passbook_Activity.this)) {
                        ToastMessage.onToast(Passbook_Activity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    } else {
                        if (fromtotal.equals("totaltransction")) {
                            Apicall_page();
                        } else if (fromtotal.equals("filter")) {
                            FliterApicall_page(fromapi);
                        }
                    }
                } else {
                    adapter.removeLoading();
                    //  Toast.makeText(Passbook_Passbook_Activity.this.this, "Nodata", Toast.LENGTH_SHORT).show();
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

    public void walletgold() {
        final ProgressDialog dialog = new ProgressDialog(Passbook_Activity.this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(Passbook_Activity.this)) {
            ToastMessage.onToast(Passbook_Activity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(Passbook_Activity.this)).create(ApiDao.class);

            Call<JsonElement> call = apiDao.get_digitalwallet("Bearer " + AccountUtils.getAccessToken(Passbook_Activity.this));
            call.enqueue(new Callback<JsonElement>() {

                @Override
                public void onResponse(@NonNull Call<JsonElement> call, @NonNull retrofit2.Response<JsonElement> response) {
                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {

                        JsonElement jsonElement = response.body();
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        JsonObject gson = new JsonParser().parse(String.valueOf(jsonObject)).getAsJsonObject();

                        try {
                            JSONObject jo2 = new JSONObject(gson.toString());
                            JSONObject balance = jo2.getJSONObject("balance");
                            String st_currencyinwords = balance.getString("currencyInWords");
                            String st_ingrams = balance.getString("humanReadable");
                            String st_incurrency = balance.getString("inCurrency");

                            tvWalletGold.setText(st_ingrams + " grams");
                            AccountUtils.setWalletAmount(getApplicationContext(), st_ingrams + " grams");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
//                else if (stauscode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
//                    getrefresh();
//                }

                    else {
                        try {

                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
//                            Toast.makeText(Passbook_Activity.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                    dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    dialog.dismiss();
                //    Toast.makeText(Passbook_Activity.this, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void Apicall_page() {
        //passbook_arrayList.clear();
        final ProgressDialog dialog = new ProgressDialog(Passbook_Activity.this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(Passbook_Activity.this)).create(ApiDao.class);

        Call<PassBookModel> call = apiDao.getp_passbook_page(String.valueOf(next_page), "Bearer " + AccountUtils.getAccessToken(Passbook_Activity.this));
        call.enqueue(new Callback<PassBookModel>() {

            @Override
            public void onResponse(@NonNull Call<PassBookModel> call, @NonNull retrofit2.Response<PassBookModel> response) {

                int stauscode = response.code();
                if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                    List<PassBookModel> listmodel = response.body().getResult();
                    PassBookModel list1 = response.body();
                    if (listmodel.size() != 0) {
                        linearLayout.setVisibility(View.GONE);
                        page_no = list1.getCurrent_page();

                        next_page = page_no + 1;

                        Log.e("PageNo", String.valueOf(page_no));
                        Log.e("Next page + ", String.valueOf(next_page));

                        totalPage = list1.getLast_page();
                        Log.e("lastpage", String.valueOf(totalPage));

                        for (PassBookModel list : listmodel) {

                            final ArrayList<PassBookModel> items = new ArrayList<>();

                            try {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(listmodel));


                                JSONArray array = jsonObject.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    //list.setId(object.getString("id"));
//                  listmodel.setGrams(object.getString("grams"));
//                  listmodel.setAmount(object.getString("amount"));
                                    //list.setCreated_at(object.getString("created_at"));
//                  listmodel.setDesc(object.getString("desc"));

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    //for(int i = 0; i <= 10; i++) {
                                    items.add(list);
                                    //}
                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();
//                      adapter.addItems(items);
//                      isLoading = false;
//                      adapter.addLoading1();
                                    //  }

                                    // do this all stuff on Success of APIs response
                                    /**
                                     * manage progress view
                                     */
                                    // isLoading = true;
                                    //  currentPage++;

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
                        linearLayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        Toast.makeText(Passbook_Activity.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String st = jObjError.getString("message");
//                        Toast.makeText(Passbook_Activity.this, st, Toast.LENGTH_SHORT).show();
                        JSONObject er = jObjError.getJSONObject("errors");

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<PassBookModel> call, @NonNull Throwable t) {
                dialog.dismiss();
              //  Toast.makeText(Passbook_Activity.this, "Technical problem", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void FliterApicall_page(String type) {
        //passbook_arrayList.clear();
        final ProgressDialog dialog = new ProgressDialog(Passbook_Activity.this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(Passbook_Activity.this)).create(ApiDao.class);

        Call<PassBookModel> call = apiDao.getFilterpagenation_passbook_page(String.valueOf(next_page), type, "Bearer " + AccountUtils.getAccessToken(Passbook_Activity.this));
        call.enqueue(new Callback<PassBookModel>() {

            @Override
            public void onResponse(@NonNull Call<PassBookModel> call, @NonNull retrofit2.Response<PassBookModel> response) {

                int stauscode = response.code();
                if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                    List<PassBookModel> listmodel = response.body().getResult();
                    PassBookModel list1 = response.body();
                    if (listmodel.size() != 0) {
                        linearLayout.setVisibility(View.GONE);
                        page_no = list1.getCurrent_page();

                        next_page = page_no + 1;

                        Log.e("PageNo", String.valueOf(page_no));
                        Log.e("Next page + ", String.valueOf(next_page));

                        totalPage = list1.getLast_page();
                        Log.e("lastpage", String.valueOf(totalPage));

                        for (PassBookModel list : listmodel) {

                            final ArrayList<PassBookModel> items = new ArrayList<>();

                            try {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(listmodel));


                                JSONArray array = jsonObject.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
//                                    JsonObject objectfrom = new JsonParser().parse(object.getString("user_name")).getAsJsonObject();
//                                    Log.e("user_name test", String.valueOf(objectfrom));
//                                    JSONObject name = new JSONObject(objectfrom.toString());
//                                    username = name.getString("name");
//                                    Log.e("nameuser",username);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    //for(int i = 0; i <= 10; i++) {
                                    items.add(list);
                                    //}
                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();

//                      adapter.addItems(items);
//                      isLoading = false;
//                      adapter.addLoading1();
                                    //  }

                                    // do this all stuff on Success of APIs response
                                    /**
                                     * manage progress view
                                     */
                                    // isLoading = true;
                                    //  currentPage++;

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
                        linearLayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        Toast.makeText(Passbook_Activity.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String st = jObjError.getString("message");
//                        Toast.makeText(Passbook_Activity.this, st, Toast.LENGTH_SHORT).show();
                        JSONObject er = jObjError.getJSONObject("errors");

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<PassBookModel> call, @NonNull Throwable t) {
                dialog.dismiss();
               // Toast.makeText(Passbook_Activity.this, "Technical problem", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void getdata() {
        final ProgressDialog dialog = new ProgressDialog(Passbook_Activity.this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(Passbook_Activity.this)) {
            ToastMessage.onToast(Passbook_Activity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            swipeRefresh.setRefreshing(false);
        } else {
            passbook_arrayList.clear();
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(Passbook_Activity.this)).create(ApiDao.class);

            Call<PassBookModel> call = apiDao.getp_passbook("Bearer " + AccountUtils.getAccessToken(Passbook_Activity.this));
            call.enqueue(new Callback<PassBookModel>() {

                @Override
                public void onResponse(@NonNull Call<PassBookModel> call, @NonNull retrofit2.Response<PassBookModel> response) {

                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                        List<PassBookModel> listmodel = response.body().getResult();
                        PassBookModel list1 = response.body();
                        if (listmodel.size() != 0) {
                            linearLayout.setVisibility(View.GONE);
                            page_no = list1.getCurrent_page();

                            next_page = page_no + 1;

                            Log.e("PageNo", String.valueOf(page_no));
                            Log.e("Next page + ", String.valueOf(next_page));

                            totalPage = list1.getLast_page();
                            Log.e("lastpage", String.valueOf(totalPage));

                            for (PassBookModel list : listmodel) {

                                final ArrayList<PassBookModel> items = new ArrayList<>();

//                                try {
//                                    JSONObject jsonObject = new JSONObject(new Gson().toJson(listmodel));
//
//
//                                    JSONArray array = jsonObject.getJSONArray("data");
//                                    for (int i = 0; i < array.length(); i++) {
//                                        JSONObject object = array.getJSONObject(i);
////                                        JsonObject objectfrom = new JsonParser().parse(object.getString("user_name")).getAsJsonObject();
////                                        Log.e("user_name test", String.valueOf(objectfrom));
////                                        JSONObject name = new JSONObject(objectfrom.toString());
////                                        username = name.getString("name");
////                                        Log.e("nameuser",username);
//
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }

                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        //for(int i = 0; i <= 10; i++) {
                                        items.add(list);
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
                            linearLayout.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                            Toast.makeText(Passbook_Activity.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
//                            Toast.makeText(Passbook_Activity.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<PassBookModel> call, @NonNull Throwable t) {
                    Log.e("passbook error", t.toString());
                    dialog.dismiss();
                  //  Toast.makeText(Passbook_Activity.this, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        finish();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {


            case R.id.filter_all:
                fromapi = "All";
                fromtotal = "totaltransction";
                getdata();
                setTitle("All");
                break;

            case R.id.filter_buy:
                fromapi = "BU";
                fromtotal = "filter";
                getfilter("BU");
                setTitle("Buy");
                break;

            case R.id.filter_sell:
                fromapi = "SL";
                fromtotal = "filter";
                getfilter("SL");
                setTitle("Sell");
                break;

            case R.id.filter_gift:
                fromapi = "GT";
                fromtotal = "filter";
                getfilter("GT");
                setTitle("Gift");
                break;

            case R.id.filter_withdraw:
                fromapi = "WT";
                fromtotal = "filter";
                getfilter("WT");
                setTitle("WithDraw");
                break;

            case R.id.filter_transfer:
                fromapi = "TR";
                fromtotal = "filter";
                getfilter("TR");
                setTitle("Transfer");
                break;

            case R.id.filter_promotional:
                fromapi = "PR";
                fromtotal = "filter";
                getfilter("PR");
                setTitle("Promotional");
                break;

            case R.id.filter_convert:
                fromapi = "CV";
                fromtotal = "filter";
                getfilter("CV");
                setTitle("Convert");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void getfilter(String type) {


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            swipeRefresh.setRefreshing(false);
            return;
        } else {
            passbook_arrayList.clear();
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<PassBookModel> call = apiDao.getp_filter(type, "Bearer " + AccountUtils.getAccessToken(this));
            call.enqueue(new Callback<PassBookModel>() {

                @Override
                public void onResponse(@NonNull Call<PassBookModel> call, @NonNull retrofit2.Response<PassBookModel> response) {
                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                        List<PassBookModel> listmodel = response.body().getResult();
                        PassBookModel list1 = response.body();
                        if (listmodel.size() != 0) {
                            linearLayout.setVisibility(View.GONE);
                            page_no = list1.getCurrent_page();

                            next_page = page_no + 1;

                            Log.e("PageNo", String.valueOf(page_no));
                            Log.e("Next page + ", String.valueOf(next_page));

                            totalPage = list1.getLast_page();
                            Log.e("lastpage", String.valueOf(totalPage));

                            for (PassBookModel list : listmodel) {

                                final ArrayList<PassBookModel> items = new ArrayList<>();

                                try {
                                    JSONObject jsonObject = new JSONObject(new Gson().toJson(listmodel));


                                    JSONArray array = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        //list.setId(object.getString("id"));
//                  listmodel.setGrams(object.getString("grams"));
//                  listmodel.setAmount(object.getString("amount"));
                                        //list.setCreated_at(object.getString("created_at"));
//                  listmodel.setDesc(object.getString("desc"));

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        //for(int i = 0; i <= 10; i++) {
                                        items.add(list);
                                        //}
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
                            linearLayout.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                            Toast.makeText(Passbook_Activity.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
//                            Toast.makeText(Passbook_Activity.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                    dialog.dismiss();


                }

                @Override
                public void onFailure(@NonNull Call<PassBookModel> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e("Failure", t.toString());
                   // Toast.makeText(Passbook_Activity.this, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });
        }
//
    }


    @Override
    public void onRefresh() {

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            swipeRefresh.setRefreshing(false);

        } else {
            walletgold();
            itemCount = 0;
            currentPage = PAGE_START;
            isLastPage = false;
            adapter.clear();
            if (fromapi.equals("getdata")) {
                getdata();
            } else if (fromapi.equals("All")) {
                getdata();
            } else {
                getfilter(fromapi);
                Log.e("sdGSDFWE", fromapi);
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        PassBookModel model = passbook_arrayList.get(position);

        String date = model.getCreated_at();
        if (view.getId() == R.id.llclick) {
            if (!NetworkUtils.isConnected(this)) {
                ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            } else {
                Intent intent = new Intent(this, TransactionInvoice.class);

                intent.putExtra("date", model.getCreated_at());
                intent.putExtra("grams", model.getGrams());
                intent.putExtra("desc", model.getDesc());
                intent.putExtra("amount", model.getParchase_amount());
                intent.putExtra("transctionid", model.getId());
                intent.putExtra("gst", model.getGst());
                intent.putExtra("totalamount", model.getTotal_amount());
                intent.putExtra("paymode", model.getSource());
                intent.putExtra("name", AccountUtils.getName(this));
                intent.putExtra("goldrate", model.getGold_price_per_gram());
                intent.putExtra("txntype", model.getTxn_type());

                startActivity(intent);


            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_transfer:
                openTransfer();
                break;
//            case R.id.ll_gift:
//                openGift();
//                break;
            case R.id.ll_redeem:
                openRedeem();
                break;
            case R.id.btn_sellgold:
                openSell();
                break;
        }
    }

    public void openTransfer() {

        Intent intent = new Intent(this, ContactList.class);
        startActivity(intent);
    }

    public void openGift() {

        Intent intent = new Intent(this, GiftContactList.class);
        startActivity(intent);
    }

    public void openRedeem() {

        Intent intent = new Intent(this, Reedem_fragment.class);
        startActivity(intent);
    }

    public void openSell() {

        Intent intent = new Intent(this, Sell_Fragment.class);
        startActivity(intent);
    }



    private void successAlert() {
        dialog1 = new Dialog(Passbook_Activity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.feedbackdilog);
        dialog1.getWindow().setBackgroundDrawableResource(R.drawable.button_background);


        RatingBar rating = (RatingBar) dialog1.findViewById(R.id.rating);
        EditText etFeedback = (EditText) dialog1.findViewById(R.id.et_feedback_form);
        etFeedback.setHint(Html.fromHtml(getString(R.string.enterfeedbacktext)));

        Button dialogButton = (Button) dialog1.findViewById(R.id.close);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromnotification = true;
                dialog1.dismiss();
            }
        });
        Button btn_submit = (Button) dialog1.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stForm = etFeedback.getText().toString().trim();
                stRating = rating.getRating();
                if (stForm.isEmpty()) {
                    ToastMessage.onToast(getApplicationContext(), "Please enter feedback form", ToastMessage.ERROR);

                } else if (stRating == 0.0) {
                    ToastMessage.onToast(getApplicationContext(), "Please rate ", ToastMessage.ERROR);

                } else {
                    sendFeedback();
                }
            }
        });
        dialog1.show();
    }




    public void sendFeedback() {
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
            Call<Listmodel> getplans = apiDao.feedbackForm("Bearer " + AccountUtils.getAccessToken(this), stForm, String.valueOf(stRating));

            getplans.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("Status COde Form", String.valueOf(statuscode));
                    list = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_CREATED) {
                        ToastMessage.onToast(getApplicationContext(), list.getMessage(), ToastMessage.SUCCESS);
                        dialog1.dismiss();
                        dialog.dismiss();

                    } else {
                        dialog.dismiss();
                     //   ToastMessage.onToast(getApplicationContext(), "Technical Error", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(getApplicationContext(), "We have some issues ", ToastMessage.ERROR);
                }
            });

        }
    }


}