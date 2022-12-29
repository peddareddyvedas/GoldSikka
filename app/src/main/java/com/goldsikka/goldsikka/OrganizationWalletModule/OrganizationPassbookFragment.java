package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener;
import com.goldsikka.goldsikka.Activitys.TransactionInvoice;
import com.goldsikka.goldsikka.Adapter.Passbook_Adapter;
import com.goldsikka.goldsikka.Models.PassBookModel;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;

import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;

public class OrganizationPassbookFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    private Activity activity;
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

    int page_no;
    int next_page;
    LinearLayout ll_transfer, ll_redeem;
    Button btn_sellgold;
    TextView tvWalletGold;
    ImageView ivprofile ;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.organization_passbook, container, false);

        Toolbar toolbar = view.findViewById(R.id.filterToolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("TRANSACTIONS");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        initviews(view);
        intilizerecylerview();

        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            fromapi = "getdata";
            fromtotal = "totaltransction";
            getdata();
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void initviews(View view){

        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        rv_passbook = view.findViewById(R.id.rv_passbook);
        linearLayout = view.findViewById(R.id.linearlayout);
        swipeRefresh.setOnRefreshListener(this);

    }

    public void intilizerecylerview() {

        rv_passbook.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        rv_passbook.setLayoutManager(linearLayoutManager);
        rv_passbook.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL);
        rv_passbook.addItemDecoration(decoration);
        passbook_arrayList = new ArrayList<>();
        adapter = new Passbook_Adapter(activity, passbook_arrayList, this);
        rv_passbook.setAdapter(adapter);

        rv_passbook.addOnScrollListener(new PaginationListener(linearLayoutManager) {

            protected void loadMoreItems() {
                isLoading = true;
                // currentPage++;

                if (totalPage != page_no) {
                    if (!NetworkUtils.isConnected(activity)) {
                        ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    } else {
                        if (fromtotal.equals("totaltransction")) {
                            Apicall_page();
                        } else if (fromtotal.equals("filter")) {
                            FliterApicall_page(fromapi);
                        }
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



    public void Apicall_page() {
        //passbook_arrayList.clear();
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

        Call<PassBookModel> call = apiDao.getp_passbook_page(String.valueOf(next_page), "Bearer " + AccountUtils.getAccessToken(activity));
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
                        Toast.makeText(activity, "No Data Available ", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String st = jObjError.getString("message");
                        Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
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
                //  Toast.makeText(activity, "Technical problem", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void FliterApicall_page(String type) {
        //passbook_arrayList.clear();
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

        Call<PassBookModel> call = apiDao.getFilterpagenation_passbook_page(String.valueOf(next_page), type, "Bearer " + AccountUtils.getAccessToken(activity));
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
                        Toast.makeText(activity, "No Data Available ", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String st = jObjError.getString("message");
                        Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
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
                //  Toast.makeText(activity, "Technical problem", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void getdata() {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            swipeRefresh.setRefreshing(false);
            return;
        } else {
            passbook_arrayList.clear();
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

            Call<PassBookModel> call = apiDao.getp_passbook("Bearer " + AccountUtils.getAccessToken(activity));
            call.enqueue(new Callback<PassBookModel>() {

                @Override
                public void onResponse(@NonNull Call<PassBookModel> call, @NonNull retrofit2.Response<PassBookModel> response) {

                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_ACCEPTED || stauscode == HttpsURLConnection.HTTP_OK) {
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
                            Toast.makeText(activity, "No Data Available ", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
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
                    //   Toast.makeText(activity, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.organization_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {


            case R.id.filterall:
                fromapi = "All";
                fromtotal = "totaltransction";
                getdata();
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("All");
//                   textView.setText("All");
//                   textView.setTextColor();
                break;

            case R.id.donations:
                fromapi = "GT";
                fromtotal = "filter";
                getfilter("GT");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Donations");
//                   textView.setText("Buy");
                break;

            case R.id.withdraw:
                fromapi = "WT";
                fromtotal = "filter";
                getfilter("WT");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Withdraw");
//                   textView.setText("Buy");
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    public void getfilter(String type) {

        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            swipeRefresh.setRefreshing(false);
            return;
        } else {
            passbook_arrayList.clear();
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

            Call<PassBookModel> call = apiDao.getp_filter(type, "Bearer " + AccountUtils.getAccessToken(activity));
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
                            Toast.makeText(activity, "No Data Available ", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
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
                    //  Toast.makeText(activity, "Technical problem", Toast.LENGTH_SHORT).show();
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
        //String
        if (view.getId()== R.id.llclick){
            if (!NetworkUtils.isConnected(activity)){
                ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            }else {
                Intent intent = new Intent(activity, OrganizationTransactionInvoice.class);

                intent.putExtra("date",model.getCreated_at());
                intent.putExtra("grams",model.getGrams());
                intent.putExtra("desc",model.getDesc());
                intent.putExtra("amount",model.getParchase_amount());
                intent.putExtra("transctionid",model.getId());
                intent.putExtra("gst",model.getGst());
                intent.putExtra("totalamount",model.getTotal_amount());
                intent.putExtra("paymode",model.getSource());
                intent.putExtra("name",AccountUtils.getName(activity));
                intent.putExtra("goldrate",model.getGold_price_per_gram());
                intent.putExtra("txntype",model.getTxn_type());

                startActivity(intent);

            }
        }
    }

}
