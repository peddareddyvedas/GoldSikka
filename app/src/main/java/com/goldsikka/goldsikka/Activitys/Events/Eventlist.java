package com.goldsikka.goldsikka.Activitys.Events;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goldsikka.goldsikka.Activitys.Events.Fragments.EventsListAdapter;
import com.goldsikka.goldsikka.Adapter.EnquirySupportAdapter;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Eventlist extends AppCompatActivity {


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llcreateevent)
    LinearLayout  llcreateevent;

    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar toolbar;
    EventsListAdapter adapter;
    RelativeLayout backbtn;
    TextView unameTv, uidTv, titleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.both_eventlist);
        ButterKnife.bind(this);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);

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
        titleTv.setText("Events");

        adapter = new EventsListAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        // finish();
    }

    @OnClick(R.id.llcreateevent)
    public void createevent(){
        Intent intent = new Intent(Eventlist.this, CreateEvents.class);
        startActivity(intent);
    }

}


























//package com.goldsikka.goldsikka.Activitys.Events;
//
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.NavUtils;
//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import com.goldsikka.goldsikka.Activitys.Passbook_Activity;
//import com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener;
//import com.goldsikka.goldsikka.Adapter.Schemes_usersubscribed_Adapter;
//import com.goldsikka.goldsikka.Fragments.Schemes.Schemes_usersubscribed_list;
//import com.goldsikka.goldsikka.Models.PassBookModel;
//import com.goldsikka.goldsikka.R;
//import com.goldsikka.goldsikka.Utils.AccountUtils;
//import com.goldsikka.goldsikka.Utils.NetworkUtils;
//import com.goldsikka.goldsikka.Utils.ToastMessage;
//import com.goldsikka.goldsikka.interfaces.ApiDao;
//import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
//import com.goldsikka.goldsikka.netwokconnection.ApiClient;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.net.ssl.HttpsURLConnection;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;
//
//public class Eventlist extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,OnItemClickListener {
//
//
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.linearlayout)
//    LinearLayout  linearlayout;
//
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.llrv)
//    LinearLayout  llrv;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.rveventlist)
//    RecyclerView rveventlist;
//
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.llcreateevent)
//    LinearLayout  llcreateevent;
//
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.llrecivedlist)
//    LinearLayout  llrecivedlist;
//
//
//
//    ArrayList<EventModel> arrayList;
//    EventAdapter adapter;
//    ApiDao apiDao;
//
//    @BindView(R.id.swipeRefresh)
//    SwipeRefreshLayout swipeRefresh;
//
//    private int currentPage = PAGE_START;
//    private boolean isLastPage = false;
//    private int totalPage;
//    private boolean isLoading = false;
//    int itemCount;
//    String fromapi,fromtotal;
//
//    int page_no;
//    int next_page;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_eventlist);
//        ButterKnife.bind(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle("Events");
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//        intilizerecyclerview();
//        fromapi = "getdata";
//        loadeventsdetails();
//    }
//    @Override
//    public void onBackPressed() {
//        NavUtils.navigateUpFromSameTask(this);
//        super.onBackPressed();
//        // finish();
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                onBackPressed();
//                return false;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//    public void intilizerecyclerview(){
//        swipeRefresh.setOnRefreshListener(this);
//        rveventlist.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rveventlist.setLayoutManager(linearLayoutManager);
//        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
//        rveventlist.addItemDecoration(decoration);
//        arrayList = new ArrayList<>();
//        adapter = new EventAdapter(this,arrayList,this);
//        rveventlist.setAdapter(adapter);
//
//        rveventlist.addOnScrollListener(new PaginationListener(linearLayoutManager) {
//
//            protected void loadMoreItems() {
//                isLoading = true;
//                // currentPage++;
//
//                if(totalPage != page_no){
//                    if (!NetworkUtils.isConnected(Eventlist.this)){
//                        ToastMessage.onToast(Eventlist.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//                    }else {
//                        Apicall_page();
//
//                    }
//                }else{
//                    adapter.removeLoading();
//                    //  Toast.makeText(Passbook_Activity.this, "Nodata", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public boolean isLastPage() {
//                return isLastPage;
//            }
//
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//        });
//    }
//
//
//    public void loadeventsdetails(){
//
//        arrayList.clear();
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Please Wait....");
//        dialog.setCancelable(false);
//        dialog.show();
//        if (!NetworkUtils.isConnected(this)){
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//            dialog.dismiss();
//            return;
//        }else {
//            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//            Call<EventModel> getdetails = apiDao.eventlist("Bearer "+AccountUtils.getAccessToken(this));
//            getdetails.enqueue(new Callback<EventModel>() {
//                @Override
//                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
//                    int statuscode  = response.code();
//                    Log.e("statuscode dd" , String.valueOf(statuscode));
//                    if (statuscode == HttpsURLConnection.HTTP_OK) {
//                        List<EventModel> list = response.body().getResult();
//                        EventModel modelnext = response.body();
//                        dialog.dismiss();
//                        if (list.size() != 0) {
//
//                            page_no = modelnext.getCurrent_page();
//
//                            next_page = page_no + 1;
//
//                            Log.e("PageNo", String.valueOf(page_no));
//                            Log.e("Next page + ", String.valueOf(next_page));
//
//                            totalPage = modelnext.getLast_page();
//
//                            for (EventModel model : list) {
//                                final ArrayList<EventModel> items = new ArrayList<>();
//
//                                linearlayout.setVisibility(View.GONE);
//                                llrv.setVisibility(View.VISIBLE);
////                                arrayList.add(model);
////                                adapter.notifyDataSetChanged();
//
//                                new Handler().postDelayed(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//
//                                        items.add(model);
//                                        //}
//                                        adapter.notifyDataSetChanged();
//                                        dialog.dismiss();
////
//
//                                        if (currentPage != PAGE_START)
//                                            adapter.removeLoading();
//                                        adapter.addItems(items);
//
//                                        swipeRefresh.setRefreshing(false);
//
//                                        // check weather is last page or not
//                                        if (currentPage < next_page) {
//
//                                        } else {
//                                            isLastPage = true;
//                                        }
//                                        isLoading = false;
//
//                                    }
//                                }, 100);
//                            }
//
//                        }else {
//                            linearlayout.setVisibility(View.VISIBLE);
//                            llrv.setVisibility(View.GONE);
//                        }
//
//
//                    }else {
//                        linearlayout.setVisibility(View.VISIBLE);
//                        dialog.dismiss();
//                        ToastMessage.onToast(Eventlist.this,"Technical issue",ToastMessage.ERROR);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<EventModel> call, Throwable t) {
//                    Log.e("on fails",t.toString());
//                    dialog.dismiss();
//                    ToastMessage.onToast(Eventlist.this,"We have some issue",ToastMessage.ERROR);
//                }
//            });
//        }
//    }
//    public void Apicall_page(){
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Please Wait....");
//        dialog.setCancelable(false);
//        dialog.show();
//        if (!NetworkUtils.isConnected(this)){
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//            dialog.dismiss();
//            return;
//        }else {
//            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//            Call<EventModel> getdetails = apiDao.eventlistnextpage("Bearer "+AccountUtils.getAccessToken(this), String.valueOf(next_page));
//            getdetails.enqueue(new Callback<EventModel>() {
//                @Override
//                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
//                    int statuscode  = response.code();
//                    Log.e("statuscode dd" , String.valueOf(statuscode));
//                    if (statuscode == HttpsURLConnection.HTTP_OK) {
//                        List<EventModel> list = response.body().getResult();
//                        EventModel modelnext = response.body();
//                        dialog.dismiss();
//                        if (list.size() != 0) {
//
//                            page_no = modelnext.getCurrent_page();
//
//                            next_page = page_no + 1;
//
//                            Log.e("PageNo", String.valueOf(page_no));
//                            Log.e("Next page + ", String.valueOf(next_page));
//
//                            totalPage = modelnext.getLast_page();
//
//                            for (EventModel model : list) {
//                                final ArrayList<EventModel> items = new ArrayList<>();
//
//                                linearlayout.setVisibility(View.GONE);
//                                llrv.setVisibility(View.VISIBLE);
//
////                                arrayList.add(model);
////                                adapter.notifyDataSetChanged();
//
//                                new Handler().postDelayed(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//
//                                        items.add(model);
//                                        //}
//                                        adapter.notifyDataSetChanged();
//                                        dialog.dismiss();
////
//
//                                        if (currentPage != PAGE_START)
//                                            adapter.removeLoading();
//                                        adapter.addItems(items);
//
//                                        swipeRefresh.setRefreshing(false);
//
//                                        // check weather is last page or not
//                                        if (currentPage < next_page) {
//
//                                        } else {
//                                            isLastPage = true;
//                                        }
//                                        isLoading = false;
//
//                                    }
//                                }, 100);
//                            }
//
//                        }else {
//                            linearlayout.setVisibility(View.VISIBLE);
//                            llrv.setVisibility(View.GONE);
//                        }
//
//
//                    }else {
//                        linearlayout.setVisibility(View.VISIBLE);
//                        dialog.dismiss();
//                        ToastMessage.onToast(Eventlist.this,"Technical issue",ToastMessage.ERROR);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<EventModel> call, Throwable t) {
//                    Log.e("on fails",t.toString());
//                    dialog.dismiss();
//                    ToastMessage.onToast(Eventlist.this,"We have some issue",ToastMessage.ERROR);
//                }
//            });
//        }
//
//    }
//
//    @Override
//    public void onItemClick(View view, int position) {
//
//        EventModel model = arrayList.get(position);
//        if (view.getId() == R.id.tvmore){
//            geteventdetails(model);
//        }
//        else if (view.getId() == R.id.ivedit){
//            Intent intent = new Intent(this,UpdateEvent.class);
//            intent.putExtra("eventid",model.getId());
//            intent.putExtra("eventtype",model.getEvent_type());
//            startActivity(intent);
//        }
//        else if (view.getId() == R.id.ivdelete){
//            openremove(model.getId());
//        }
//        else if (view.getId() == R.id.tvsendlist){
//
//            sentlist(model);
//        }
//        else if (view.getId()== R.id.tvsend){
//            Intent intent = new Intent(this,ContactsforEvents.class);
//            intent.putExtra("steventid",model.getId());
//            startActivity(intent);
//        }
//
//
//    }
//
//    public void geteventdetails(EventModel model){
//
//        Intent intent = new Intent(this,EventDetails.class);
//        intent.putExtra("eventid",model.getId());
//        startActivity(intent);
//    }
//
//    public void sentlist(EventModel model){
//        Intent intent = new Intent(this,SentlistActivity.class);
//        intent.putExtra("eventid",model.getId());
//        startActivity(intent);
//    }
//
////    public  void openeventinfo(EventModel model){
////
////        Intent intent = new Intent(this, Eventinfo.class);
////        intent.putExtra("qrid",model.getId());
////        intent.putExtra("eventdate",model.getEvent_date());
////        intent.putExtra("eventname",model.getEvent_name());
////        intent.putExtra("eventid",model.getEvent_id());
////        startActivity(intent);
////    }
//    public void openremove(String id){
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setMessage("Are you sure want to delete")
//                .setTitle("Alert")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        eventdelete(id);
//                    }
//                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//
//    }
//    public void eventdelete(String id){
//
//        if (!NetworkUtils.isConnected(this)){
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//
//            return;
//        }else {
//            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//            Call<EventModel> eventdelte = apiDao.DeleteEvent("Bearer "+AccountUtils.getAccessToken(this),id);
//            eventdelte.enqueue(new Callback<EventModel>() {
//                @Override
//                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
//                    int statuscode = response.code();
//                    if (statuscode == HttpsURLConnection.HTTP_NO_CONTENT) {
//                        ToastMessage.onToast(Eventlist.this,"Successfully deleted",ToastMessage.INFO);
//                        loadeventsdetails();
//                    } else {
//
//                        assert response.errorBody() != null;
//
//                        try {
//                            JSONObject jObjError = new JSONObject(response.errorBody().string());
//                            String st = jObjError.getString("message");
//                            ToastMessage.onToast(Eventlist.this, st, ToastMessage.ERROR);
//                        } catch (JSONException | IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<EventModel> call, Throwable t) {
//
//                    ToastMessage.onToast(Eventlist.this,"We have some issues please try after some time",ToastMessage.ERROR);
//                }
//            });
//        }
//
//    }
//
//    @Override
//    public void onRefresh() {
//
//        if (!NetworkUtils.isConnected(this)) {
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//            swipeRefresh.setRefreshing(false);
//
//        } else {
//            itemCount = 0;
//            currentPage = PAGE_START;
//            isLastPage = false;
//            adapter.clear();
//            if (fromapi.equals("getdata")) {
//                loadeventsdetails();
//            }
//        }
//    }
//
//    @OnClick(R.id.llcreateevent)
//    public void createevent(){
//        Intent intent = new Intent(this,CreateEvents.class);
//        startActivity(intent);
//    }
//    @OnClick(R.id.llrecivedlist)
//    public void recivedlist(){
//
//        Intent intent= new Intent(this,RecivedEventList.class);
//        startActivity(intent);
//    }
//}