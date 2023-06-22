package com.goldsikka.goldsikka.Fragments.Schemes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.goldsikka.goldsikka.Activitys.Elevenplus_Jewellery;
import com.goldsikka.goldsikka.Activitys.Mygold2020Form_Activity;
import com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener;
import com.goldsikka.goldsikka.Adapter.Schemes_usersubscribed_Adapter;
import com.goldsikka.goldsikka.Fragments.Mygold2020.Get_scheme;
import com.goldsikka.goldsikka.Models.SchemeModel;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;

public class Schemes_usersubscribed_list extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, View.OnClickListener {

    //    TextView tv_title,tv_date,tv_id,tv_tenure,tv_emi_grams;
//    String st_title,st_date,st_tenure,st_emi_grams;
    RecyclerView recyclerView;

    ArrayList<SchemeModel> arrayList;
    Schemes_usersubscribed_Adapter adapter;
    ApiDao apiDao;
    LinearLayout ll_subscribe, linearlayout, ll_rv;
    String schemeid;
    String schemename;


    SwipeRefreshLayout swipeRefresh;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage;
    private boolean isLoading = false;
    int itemCount;
    String fromapi;

    int page_no;
    int next_page;

    Dialog dialog1;
    public static boolean isFromgoldtransaction = false;

    String stForm;
    float stRating = 0;
    Listmodel list;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scheme_usersubscribed_list);

        linearlayout = findViewById(R.id.linearlayout);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        ll_rv = findViewById(R.id.ll_rv);
        schemeid = AccountUtils.getSchemeID(this);
        ll_subscribe = findViewById(R.id.ll_subscribe);

        if(schemeid.equals("1")){
            ll_subscribe.setVisibility(View.GONE);
        }else{
            ll_subscribe.setVisibility(View.VISIBLE);
        }
        ll_subscribe.setOnClickListener(this);
        schemename = AccountUtils.getSchemename(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Your Subscription");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        scheme_recyclerview();

        fromapi = "getdata";
        initi_schemes_list();

    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ticketmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.tickitlist) {
            openticketlist();
        }
        return super.onOptionsItemSelected(item);
    }

    public void openticketlist() {
        Intent intent = new Intent(this, TicketList.class);
        intent.putExtra("schemeid", schemeid);
        startActivity(intent);
    }

    private void scheme_recyclerview() {
        swipeRefresh.setOnRefreshListener(this);
        recyclerView = findViewById(R.id.usersubscribe_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(Schemes_usersubscribed_list.this, LinearLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(decoration);
        arrayList = new ArrayList<>();
        adapter = new Schemes_usersubscribed_Adapter(this, arrayList, this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationListener(linearLayoutManager) {

            protected void loadMoreItems() {
                isLoading = true;
                // currentPage++;

                if (totalPage != page_no) {
                    if (!NetworkUtils.isConnected(Schemes_usersubscribed_list.this)) {
                        ToastMessage.onToast(Schemes_usersubscribed_list.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
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

        if (isFromgoldtransaction) {
            Log.e("isif", "" + isFromgoldtransaction);
            successAlert();

        } else {
            Log.e("iselse", "" + isFromgoldtransaction);
        }
    }


    private void initi_schemes_list() {
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
            Call<SchemeModel> schemesModelCall = apiDao.get_usersubscribed("Bearer " + AccountUtils.getAccessToken(this), schemeid);
            schemesModelCall.enqueue(new Callback<SchemeModel>() {
                @Override
                public void onResponse(@NotNull Call<SchemeModel> call, @NotNull Response<SchemeModel> response) {
                    int statuscode = response.code();
                    assert response.body() != null;
                    List<SchemeModel> list = response.body().getResult();
                    SchemeModel body = response.body();
                    Log.e("Status Code", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {
                        if (list.size() != 0) {
                            page_no = body.getCurrent_page();
                            next_page = page_no + 1;
                            Log.e("PageNo", String.valueOf(page_no));
                            Log.e("Next page + ", String.valueOf(next_page));
                            totalPage = body.getLast_page();
                            for (SchemeModel listmodel : list) {
                                final ArrayList<SchemeModel> items = new ArrayList<>();
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
                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                        if (currentPage != PAGE_START)
                                            adapter.removeLoading();
                                        adapter.addItems(items);
                                        swipeRefresh.setRefreshing(false);
                                        if (currentPage < next_page) {
                                            Log.e("current page", String.valueOf(currentPage));
                                        } else {
                                            isLastPage = true;
                                        }
                                        isLoading = false;
                                    }
                                }, 100);
                            }
                        } else {
                            dialog.dismiss();
                            ll_rv.setVisibility(View.GONE);
                            linearlayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        dialog.dismiss();
                        ll_rv.setVisibility(View.GONE);
                        linearlayout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<SchemeModel> call, @NotNull Throwable t) {
                    Log.e("Failure", t.toString());
                  //  Toast.makeText(Schemes_usersubscribed_list.this, "Sorry! We Have some technical issue", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
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
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<SchemeModel> schemesModelCall = apiDao.get_usersubscribedpage("Bearer " + AccountUtils.getAccessToken(this),
                    schemeid, String.valueOf(next_page));
            schemesModelCall.enqueue(new Callback<SchemeModel>() {
                @Override
                public void onResponse(@NotNull Call<SchemeModel> call, @NotNull Response<SchemeModel> response) {
                    int statuscode = response.code();
                    assert response.body() != null;
                    List<SchemeModel> list = response.body().getResult();
                    SchemeModel body = response.body();
                    Log.e("Status Code", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {
                        if (list.size() != 0) {
                            page_no = body.getCurrent_page();
                            next_page = page_no + 1;
                            Log.e("PageNo", String.valueOf(page_no));
                            Log.e("Next page + ", String.valueOf(next_page));
                            totalPage = body.getLast_page();
                            for (SchemeModel listmodel : list) {
                                final ArrayList<SchemeModel> items = new ArrayList<>();
                                String id = listmodel.getId();
                                Log.e("idsto", id);
                                dialog.dismiss();
                                ll_rv.setVisibility(View.VISIBLE);
                                linearlayout.setVisibility(View.GONE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        items.add(listmodel);
                                        Collections.reverse(items);

                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                        if (currentPage != PAGE_START)
                                            adapter.removeLoading();
                                        adapter.addItems(items);
                                        swipeRefresh.setRefreshing(false);
                                        if (currentPage < next_page) {
                                            Log.e("current page", String.valueOf(currentPage));
                                        } else {
                                            isLastPage = true;
                                        }
                                        isLoading = false;
                                    }
                                }, 100);
                            }
                        } else {
                            dialog.dismiss();
                            ll_rv.setVisibility(View.GONE);
                            linearlayout.setVisibility(View.VISIBLE);
                        }

                    } else {

                        dialog.dismiss();
                        ll_rv.setVisibility(View.GONE);
                        linearlayout.setVisibility(View.VISIBLE);
                    }

                }


                @Override
                public void onFailure(@NotNull Call<SchemeModel> call, @NotNull Throwable t) {
                    Log.e("Failure", t.toString());
                 //   Toast.makeText(Schemes_usersubscribed_list.this, "Sorry! We Have some technical issue", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemClick(View view, int position) {
        SchemeModel model = arrayList.get(position);
        if (view.getId() == R.id.tvmore) {
            openScheme(model.getId());
            Log.e(" idsdsd", model.getId());
        } else if (view.getId() == R.id.tvnickname) {
            Intent intent = new Intent(this, SchemeNicknameAdd.class);
            intent.putExtra("id", model.getId());
            startActivity(intent);
        } else if (view.getId() == R.id.llnickremove) {
            removenickname(model.getId());
        }
    }

    public void removenickname(String stid) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        Log.e("removeisd", stid);
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<SchemeModel> addname = apiDao.removenickname("Bearer " + AccountUtils.getAccessToken(this), stid);
        addname.enqueue(new Callback<SchemeModel>() {
            @Override
            public void onResponse(Call<SchemeModel> call, Response<SchemeModel> response) {
                int statuscode = response.code();
                Log.e("removestatuscode", String.valueOf(statuscode));
                SchemeModel model = response.body();
                if (statuscode == HttpsURLConnection.HTTP_CREATED) {
                    dialog.dismiss();
                    initi_schemes_list();
                } else {
                    dialog.dismiss();
                    ToastMessage.onToast(Schemes_usersubscribed_list.this, "Try after some time", ToastMessage.ERROR);
                }

            }

            @Override
            public void onFailure(Call<SchemeModel> call, Throwable t) {
                dialog.dismiss();
                //  ToastMessage.onToast(Schemes_usersubscribed_list.this, "We have some Issues please try after sometime", ToastMessage.ERROR);

            }
        });
    }

    private void openScheme(String id) {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(this, Get_scheme.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_subscribe) {
            openbuygold();
//            ToastMessage.onToast(Schemes_usersubscribed_list.this, schemeid, ToastMessage.SUCCESS);
//            ToastMessage.onToast(Schemes_usersubscribed_list.this, schemename, ToastMessage.SUCCESS);
        }
    }

    public void openbuygold() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            if (schemeid.equals("1")) {
                Intent intent = new Intent(this, Mygold2020Form_Activity.class);
                intent.putExtra("id", schemeid);
                intent.putExtra("schemename", AccountUtils.getSchemeTitle(this));
                startActivity(intent);
            } else if (schemeid.equals("2")) {
//                Log.e("scheme name", schemename);
//                Log.e("scheme name", schemeid);
                Intent intent = new Intent(this, Elevenplus_Jewellery.class);
                intent.putExtra("id", schemeid);
                intent.putExtra("schemename", schemename);
                AccountUtils.setSchemeIDD(getApplicationContext(),schemeid);
                AccountUtils.setSchemenamee(getApplicationContext(),schemename);

                startActivity(intent);
            }
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
            if (fromapi.equals("getdata")) {
                initi_schemes_list();
            }
        }
    }


    private void successAlert() {
        dialog1 = new Dialog(Schemes_usersubscribed_list.this);
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
                isFromgoldtransaction = true;
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
                       // ToastMessage.onToast(getApplicationContext(), "Technical Error", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    //  ToastMessage.onToast(getApplicationContext(), "We have some issues ", ToastMessage.ERROR);
                }
            });

        }
    }
}
