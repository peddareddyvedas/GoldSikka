 package com.goldsikka.goldsikka.OrganizationWalletModule;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;

public class TemplesList extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    LinearLayout lltempleslist,linearLayout;
    RecyclerView rvtempleslist;
    SwipeRefreshLayout swipeRefresh;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage;
    private boolean isLoading = false;
    int itemCount;
    String fromapi,fromtotal;


    int page_no;
    int next_page;

    ArrayList<UserOrganizationDetailsModel> arrayList;
    TemplesAdapter adapter;
    ApiDao apiDao;
    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.temples_list,container,false);

        intilizeviews(view);
        intilizerecyclerview();
        fromapi= "getdata";
        loadtempledetails();

        return view;
    }

    public void intilizeviews(View view){

        lltempleslist = view.findViewById(R.id.lltempleslist);
        linearLayout = view.findViewById(R.id.linearlayout);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        rvtempleslist = view.findViewById(R.id.rvtempleslist);
    }

    public void intilizerecyclerview(){
        swipeRefresh.setOnRefreshListener(this);
        rvtempleslist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        rvtempleslist.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL);
        rvtempleslist.addItemDecoration(decoration);
        arrayList = new ArrayList<>();
        adapter = new TemplesAdapter(activity,arrayList,this);
        rvtempleslist.setAdapter(adapter);

        rvtempleslist.addOnScrollListener(new PaginationListener(linearLayoutManager) {

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

    public void loadtempledetails(){

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
            Call<UserOrganizationDetailsModel> getdetails = apiDao.org_list("Bearer "+AccountUtils.getAccessToken(activity),"1");
            getdetails.enqueue(new Callback<UserOrganizationDetailsModel>() {
                @Override
                public void onResponse(Call<UserOrganizationDetailsModel> call, Response<UserOrganizationDetailsModel> response) {
                    int statuscode  = response.code();
                    Log.e("statuscode dd" , String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        List<UserOrganizationDetailsModel> list = response.body().getResult();
                        UserOrganizationDetailsModel modelnext = response.body();
                        dialog.dismiss();
                        if (list.size() != 0) {

                            page_no = modelnext.getCurrent_page();

                            next_page = page_no + 1;

                            Log.e("PageNo", String.valueOf(page_no));
                            Log.e("Next page + ", String.valueOf(next_page));

                            totalPage = modelnext.getLast_page();

                            for (UserOrganizationDetailsModel model : list) {
                                final ArrayList<UserOrganizationDetailsModel> items = new ArrayList<>();

                                linearLayout.setVisibility(View.GONE);
                                lltempleslist.setVisibility(View.VISIBLE);
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
                            linearLayout.setVisibility(View.VISIBLE);
                            lltempleslist.setVisibility(View.GONE);
                        }


                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                      //  ToastMessage.onToast(activity,"Technical issue",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<UserOrganizationDetailsModel> call, Throwable t) {
                    Log.e("on fails",t.toString());
                    dialog.dismiss();
                    ToastMessage.onToast(activity,"We have some issue",ToastMessage.ERROR);
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
            Call<UserOrganizationDetailsModel> getdetails = apiDao.org_nextlist("Bearer "+AccountUtils.getAccessToken(activity), String.valueOf(next_page),"1");
            getdetails.enqueue(new Callback<UserOrganizationDetailsModel>() {
                @Override
                public void onResponse(Call<UserOrganizationDetailsModel> call, Response<UserOrganizationDetailsModel> response) {
                    int statuscode  = response.code();
                    Log.e("statuscode dd" , String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        List<UserOrganizationDetailsModel> list = response.body().getResult();
                        UserOrganizationDetailsModel modelnext = response.body();
                        dialog.dismiss();
                        if (list.size() != 0) {

                            page_no = modelnext.getCurrent_page();

                            next_page = page_no + 1;

                            Log.e("PageNo", String.valueOf(page_no));
                            Log.e("Next page + ", String.valueOf(next_page));

                            totalPage = modelnext.getLast_page();

                            for (UserOrganizationDetailsModel model : list) {
                                final ArrayList<UserOrganizationDetailsModel> items = new ArrayList<>();

                                linearLayout.setVisibility(View.GONE);
                                lltempleslist.setVisibility(View.VISIBLE);

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
                            linearLayout.setVisibility(View.VISIBLE);
                            lltempleslist.setVisibility(View.GONE);
                        }


                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                     //   ToastMessage.onToast(activity,"Technical issue",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<UserOrganizationDetailsModel> call, Throwable t) {
                    Log.e("on fails",t.toString());
                    dialog.dismiss();
                    ToastMessage.onToast(activity,"We have some issue",ToastMessage.ERROR);
                }
            });
        }

    }

    @Override
    public void onItemClick(View view, int position) {

        UserOrganizationDetailsModel model = arrayList.get(position);
        if (view.getId() == R.id.lltemple) {
            gettempledetails(model);
        }
    }

    public void gettempledetails(UserOrganizationDetailsModel model){
        Intent intent = new Intent(activity, TempleAndNGOsDetails.class);
        intent.putExtra("org_id",model.getId());
        startActivity(intent);
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
                loadtempledetails();
            }
        }
    }


}
