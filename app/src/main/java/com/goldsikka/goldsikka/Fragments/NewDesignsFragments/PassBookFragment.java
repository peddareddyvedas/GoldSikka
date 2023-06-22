package com.goldsikka.goldsikka.Fragments.NewDesignsFragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Activitys.GetContacts.ContactList;
import com.goldsikka.goldsikka.Activitys.GiftModuleActivity;
import com.goldsikka.goldsikka.Activitys.Kyc_Details;
import com.goldsikka.goldsikka.Activitys.Nominee_Details;
import com.goldsikka.goldsikka.Activitys.Passbook_Activity;
import com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener;
import com.goldsikka.goldsikka.Activitys.Profile.CustomerAddressList;
import com.goldsikka.goldsikka.Activitys.TransactionInvoice;
import com.goldsikka.goldsikka.Adapter.Passbook_Adapter;
import com.goldsikka.goldsikka.Fragments.Customer_BankDetailslist;
import com.goldsikka.goldsikka.Fragments.Edit_coustomer_details;
import com.goldsikka.goldsikka.Fragments.Get_kyc_details_fragment;
import com.goldsikka.goldsikka.Fragments.TransferGold;
import com.goldsikka.goldsikka.Fragments.Reedem_fragment;
import com.goldsikka.goldsikka.Fragments.Sell_Fragment;
import com.goldsikka.goldsikka.Models.PassBookModel;
import com.goldsikka.goldsikka.NewDesignsActivity.GiftContactList;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.WelcomeActivity;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;

public class PassBookFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, View.OnClickListener {

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
//    ImageView ivprofile ;

    String stavathar, stprofileimg;

    shared_preference sharedPreference;

    TextView uidTv, unameTv;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.passbookfragment, container, false);

        Toolbar toolbar = view.findViewById(R.id.filterToolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("TRANSACTIONS");
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(false);

        initviews(view);
        intilizerecylerview();
        walletgold();
        CustomerDetails();
        sharedPreference = new shared_preference(activity);

        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            fromapi = "getdata";
            fromtotal = "totaltransction";
            getdata();
        }

        uidTv = view.findViewById(R.id.uid);
        unameTv = view.findViewById(R.id.uname);

        uidTv.setText(AccountUtils.getCustomerID(activity));
        unameTv.setText(AccountUtils.getName(activity));


        return view;
    }

    public void CustomerDetails() {


        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<Listmodel> getprofile = apiDao.getprofile_details("Bearer " + AccountUtils.getAccessToken(activity));
            getprofile.enqueue(new Callback<Listmodel>() {

                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {

                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                        List<Listmodel> list = Collections.singletonList(response.body());
                        if (list != null) {
                            for (Listmodel listmodel : list) {

                                AccountUtils.setIsPin(activity, listmodel.getIsgspin());

                                stavathar = listmodel.getAvatar();
                                AccountUtils.setAvathar(activity, stavathar);
                                if (stavathar != null) {
                                    stprofileimg = listmodel.getAvatarImageLink();
                                    AccountUtils.setProfileImg(activity, stprofileimg);
//                                    Glide.with(activity)
//                                            .load(listmodel.getAvatarImageLink())
//                                            .into(ivprofile);
                                    //Picasso.with(activity).load(listmodel.getAvatarImageLink()).into(ivprofileimg);
                                } else {
                                    AccountUtils.setAvathar(activity, null);

//                                    ivprofile.setImageResource(R.drawable.profile);
                                }


                            }
                        }

                    } else if (stauscode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        sharedPreference.WriteLoginStatus(false);
                        Intent intent = new Intent(activity, WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            JSONObject er = jObjError.getJSONObject("errors");
                            Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    ToastMessage.onToast(activity, "We Have Some Issues", ToastMessage.ERROR);
                    Log.e("Error", t.toString());
                }

            });

        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void initviews(View view) {

        ll_transfer = view.findViewById(R.id.ll_transfer);
        ll_transfer.setOnClickListener(this);
//        ll_gift = view.findViewById(R.id.ll_gift);
//        ll_gift.setOnClickListener(this);
        ll_redeem = view.findViewById(R.id.ll_redeem);
        ll_redeem.setOnClickListener(this);

        btn_sellgold = view.findViewById(R.id.btn_sellgold);
        btn_sellgold.setOnClickListener(this);

        tvWalletGold = view.findViewById(R.id.tvWalletGold);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        rv_passbook = view.findViewById(R.id.rv_passbook);
        linearLayout = view.findViewById(R.id.linearlayout);
        swipeRefresh.setOnRefreshListener(this);

//        ivprofile = view.findViewById(R.id.ivprofile);
//        if (AccountUtils.getAvathar(activity)!= null){
//          //  Picasso.with(activity).load(AccountUtils.getProfileImg(activity)).into(ivprofile);
//           // Picasso.with(getContext()).load(AccountUtils.getProfileImg(getContext())).into(ivprofile);
//
//            Glide.with(activity)
//                    .load(AccountUtils.getProfileImg(activity))
//                    .into(ivprofile);
//
//
//        }else {
//            ivprofile.setImageResource(R.drawable.profile);
//        }
//        ivprofile.setOnClickListener(this);

    }

    public void intilizerecylerview() {
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

    public void walletgold() {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<JsonElement> call = apiDao.get_digitalwallet("Bearer " + AccountUtils.getAccessToken(activity));
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
                            Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
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
                   // Toast.makeText(activity, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
             //   Toast.makeText(activity, "Technical problem", Toast.LENGTH_SHORT).show();
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
        inflater.inflate(R.menu.filter_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {

            case R.id.filter_all:
                fromapi = "All";
                fromtotal = "totaltransction";
                getdata();
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("All");
//                   textView.setText("All");
//                   textView.setTextColor();
                break;

            case R.id.filter_buy:
                fromapi = "BU";
                fromtotal = "filter";
                getfilter("BU");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Buy");
//                   textView.setText("Buy");
                break;

            case R.id.filter_sell:
                fromapi = "SL";
                fromtotal = "filter";
                getfilter("SL");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Sell");
                break;

            case R.id.filter_gift:
                fromapi = "GT";
                fromtotal = "filter";
                getfilter("GT");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Gift");
                break;

            case R.id.filter_withdraw:
                fromapi = "WT";
                fromtotal = "filter";
                getfilter("WT");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Withdraw");
                break;

            case R.id.filter_transfer:
                fromapi = "TR";
                fromtotal = "filter";
                getfilter("TR");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Transfer");
                break;

            case R.id.filter_promotional:
                fromapi = "PR";
                fromtotal = "filter";
                getfilter("PR");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Promotional");
                break;

            case R.id.filter_convert:
                fromapi = "CV";
                fromtotal = "filter";
                getfilter("CV");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Convert");
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
//
    }


    @Override
    public void onRefresh() {

        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
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
        //String
        if (view.getId() == R.id.llclick) {
            if (!NetworkUtils.isConnected(activity)) {
                ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            } else {
                Intent intent = new Intent(activity, TransactionInvoice.class);
                intent.putExtra("date", model.getCreated_at());
                intent.putExtra("grams", model.getGrams());
                intent.putExtra("desc", model.getDesc());
                intent.putExtra("amount", model.getParchase_amount());
                intent.putExtra("transctionid", model.getId());
                intent.putExtra("gst", model.getGst());
                intent.putExtra("totalamount", model.getTotal_amount());
                intent.putExtra("paymode", model.getSource());
                intent.putExtra("name", AccountUtils.getName(activity));
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
            case R.id.ivprofile:
                BottomNavigation(v);
                break;
        }
    }

    public void openTransfer() {

//        Intent intent = new Intent(activity,TransferActivity.class);
//        startActivity(intent);
        Intent intent = new Intent(activity, ContactList.class);
        startActivity(intent);
    }

    public void openGift() {

        Intent intent = new Intent(activity, GiftContactList.class);
        startActivity(intent);
    }

    public void openRedeem() {

        Intent intent = new Intent(activity, Reedem_fragment.class);
        startActivity(intent);
    }

    public void openSell() {

        Intent intent = new Intent(activity, Sell_Fragment.class);
        startActivity(intent);
    }

    TextView tveditprofile, tvnominee, tvkyc, tvbank, tvaddress, tvemail, tvname, tvcustomerid;
    ImageView ivprofileb;

    public void BottomNavigation(View view) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setCancelable(true);
        View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.profilebottomview, view.findViewById(R.id.bottomsheet));
        bottomSheetDialog.setContentView(bottomSheet);
        ((View) bottomSheet.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tveditprofile = bottomSheet.findViewById(R.id.tvprofileedit);
        tvnominee = bottomSheet.findViewById(R.id.tvnominee);
        tvkyc = bottomSheet.findViewById(R.id.tvkyc);
        tvbank = bottomSheet.findViewById(R.id.tvbankdetails);
        tvaddress = bottomSheet.findViewById(R.id.tvaddress);
        tvemail = bottomSheet.findViewById(R.id.tvemail);
        tvname = bottomSheet.findViewById(R.id.tvname);
        tvcustomerid = bottomSheet.findViewById(R.id.tvcustomerid);
        tvcustomerid.setText("CustomerId : " + AccountUtils.getCustomerID(activity));
        ivprofileb = bottomSheet.findViewById(R.id.ivprofile);
        if (AccountUtils.getAvathar(activity) != null) {
            Glide.with(activity)
                    .load(AccountUtils.getProfileImg(activity))
                    .into(ivprofileb);
            //Picasso.with(activity).load(AccountUtils.getProfileImg(activity)).into(ivprofileb);
        } else {
            ivprofileb.setImageResource(R.drawable.user);
        }
        tvname.setText(AccountUtils.getName(activity));
        tvemail.setText(AccountUtils.getEmail(activity));
        tvkyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_kycdetails();
            }
        });
        tveditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(activity)) {
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                } else {
                    Intent intent = new Intent(activity, Edit_coustomer_details.class);

                    intent.putExtra("name", AccountUtils.getName(activity));
                    intent.putExtra("email", AccountUtils.getEmail(activity));
                    intent.putExtra("mobile", AccountUtils.getMobile(activity));
                    intent.putExtra("profileimage", AccountUtils.getProfileImg(activity));
                    startActivity(intent);
                }
            }

        });
        tvnominee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_nomineedetails();
            }
        });
        tvaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(activity)) {
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                } else {
                    Intent intent = new Intent(activity, CustomerAddressList.class);
                    startActivity(intent);
                }
            }
        });
        tvbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(activity)) {
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                } else {
                    Intent intent = new Intent(activity, Customer_BankDetailslist.class);
                    startActivity(intent);
                }
            }
        });


        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }

    public void get_nomineedetails() {
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(activity, Nominee_Details.class);
            startActivity(intent);
        }
    }

    public void get_kycdetails() {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<Listmodel> iskyc = apiDao.checkkyc("Bearer " + AccountUtils.getAccessToken(activity));
            iskyc.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Listmodel listmodel = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        if (listmodel.isKyc()) {
                            Intent intent = new Intent(activity, Get_kyc_details_fragment.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(activity, Kyc_Details.class);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                }
            });


        }
    }
}