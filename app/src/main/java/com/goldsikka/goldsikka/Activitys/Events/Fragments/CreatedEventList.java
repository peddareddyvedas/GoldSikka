package com.goldsikka.goldsikka.Activitys.Events.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.goldsikka.goldsikka.Activitys.Events.ContactsforEvents;
import com.goldsikka.goldsikka.Activitys.Events.CreateEvents;
import com.goldsikka.goldsikka.Activitys.Events.EventAdapter;
import com.goldsikka.goldsikka.Activitys.Events.EventDetails;
import com.goldsikka.goldsikka.Activitys.Events.EventModel;
import com.goldsikka.goldsikka.Activitys.Events.Eventlist;
import com.goldsikka.goldsikka.Activitys.Events.SentlistActivity;
import com.goldsikka.goldsikka.Activitys.Events.UpdateEvent;
import com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

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

public class CreatedEventList extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    LinearLayout linearlayout;
    LinearLayout  llrv;
    RecyclerView rveventlist;

    ArrayList<EventModel> arrayList;
    EventAdapter adapter;
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

    private Activity activity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.created_eventlist,container,false);
        ButterKnife.bind(view,activity);

        initviews(view);
        intilizerecyclerview(view);
        fromapi = "getdata";
        loadeventsdetails();

        return view;
    }

    public void initviews(View view) {
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        linearlayout = view.findViewById(R.id.linearlayout);
        llrv = view.findViewById(R.id.llrv);
        rveventlist = view.findViewById(R.id.rveventlist);
    }

    public void intilizerecyclerview(View view){
        swipeRefresh.setOnRefreshListener(this);
        rveventlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        rveventlist.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL);
        rveventlist.addItemDecoration(decoration);
        arrayList = new ArrayList<>();
        adapter = new EventAdapter(activity,arrayList,this);
        rveventlist.setAdapter(adapter);

        rveventlist.addOnScrollListener(new PaginationListener(linearLayoutManager) {

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

    public void loadeventsdetails(){

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
            Call<EventModel> getdetails = apiDao.eventlist("Bearer "+AccountUtils.getAccessToken(activity));
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
            Call<EventModel> getdetails = apiDao.eventlistnextpage("Bearer "+AccountUtils.getAccessToken(activity), String.valueOf(next_page));
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
    public void onItemClick(View view, int position) {

        EventModel model = arrayList.get(position);
        if (view.getId() == R.id.tvmore){
            geteventdetails(model);
        }
        else if (view.getId() == R.id.ivedit){
            Intent intent = new Intent(activity, UpdateEvent.class);
            intent.putExtra("eventid",model.getId());
            intent.putExtra("eventtype",model.getEvent_type());
            startActivity(intent);
        }
        else if (view.getId() == R.id.ivdelete){
            openremove(model.getId());
        }
        else if (view.getId() == R.id.tvsendlist){

            sentlist(model);
        }
        else if (view.getId()== R.id.tvsend){
            Intent intent = new Intent(activity, ContactsforEvents.class);
            intent.putExtra("eventid",model.getId());
            startActivity(intent);
        }


    }

    public void geteventdetails(EventModel model){

        Intent intent = new Intent(activity, EventDetails.class);
        intent.putExtra("eventid",model.getId());
        startActivity(intent);
    }

    public void sentlist(EventModel model){
        Intent intent = new Intent(activity, SentlistActivity.class);
        intent.putExtra("eventid",model.getId());
        startActivity(intent);
    }

    public void openremove(String id){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("Are you sure want to delete")
                .setTitle("Alert")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eventdelete(id);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    public void eventdelete(String id){

        if (!NetworkUtils.isConnected(activity)){
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<EventModel> eventdelte = apiDao.DeleteEvent("Bearer "+AccountUtils.getAccessToken(activity),id);
            eventdelte.enqueue(new Callback<EventModel>() {
                @Override
                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                    int statuscode = response.code();
                    if (statuscode == HttpsURLConnection.HTTP_NO_CONTENT) {
                        ToastMessage.onToast(activity,"Successfully deleted",ToastMessage.SUCCESS);
                        loadeventsdetails();
                    } else {

                        assert response.errorBody() != null;

                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            ToastMessage.onToast(activity, st, ToastMessage.ERROR);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {

                   // ToastMessage.onToast(activity,"We have some issues please try after some time",ToastMessage.ERROR);
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
            if (fromapi.equals("getdata")) {
                loadeventsdetails();
            }
        }
    }

}
