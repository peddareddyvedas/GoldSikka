package com.goldsikka.goldsikka.Fragments.JewelleryInventory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.goldsikka.goldsikka.Fragments.JewelleryInventory.Adapters.AllProductsAdapter;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCatagory_Activity extends AppCompatActivity implements OnItemClickListener, View.OnClickListener {
    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;
    shared_preference sharedPreference;
    RecyclerView productsrv;
    AllProductsAdapter productsAdapter;
    ArrayList<Listmodel> productsList;
    ApiDao apiDao, apiDao1;
    private Activity activity;
    String pid;
    String cid;
    List<Listmodel> flist,filteredlist, fflist;
    int selectedPosition = -1;
    RecyclerView recyclerView;

    Spinner pweightspinner;

    Bundle bundle;

    String catid="sdffv";
    String catName="sdffv";

    SearchView searchtext;
    RelativeLayout notfound;
    String from ="sdv";

    SwipeRefreshLayout swipe_layout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcatagory);

        bundle = getIntent().getExtras();

        try{
            from = bundle.getString("from");
            if(from.equals("cats")){
                catid = bundle.getString("catid");
                catName = bundle.getString("catName");
            }
        }catch (Exception e){
            Log.e("sdvd", "sdvd");
        }

        fflist =new ArrayList<>();

        init();
        getWishlistCount();
        intilizerecyclerview();
        getProductsByCats();
        onrefresh();

//        searchtext.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                filterNewText(newText);
//                return false;
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(AccountUtils.getLoadify(SubCatagory_Activity.this).equals("unload")){
            getWishlistCount();
//            intilizerecyclerview();
//            getProductsByCats();
            onrefresh();
            AccountUtils.setLoadify(SubCatagory_Activity.this, "load");
        }else{
            init();
            getWishlistCount();
            intilizerecyclerview();
            getProductsByCats();
            onrefresh();
            AccountUtils.setLoadify(SubCatagory_Activity.this, "load");
        }


    }

    public void getWishlistCount() {

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<List<Listmodel>> getWishlist = apiDao.getWishlist("Bearer "+AccountUtils.getAccessToken(this));
        getWishlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("statuscode", String.valueOf(statuscode));
//                assert response.body() != null;
                fflist = response.body();
                if (statuscode == 200 || statuscode == 201) {
                    productsAdapter.setFlistwish(response.body());
                    productsAdapter.setFlistwish(new ArrayList<>());
                    List<String> list2 = new ArrayList<>();
                    for (Listmodel list : fflist) {
                        list2.add(list.getPids());
                        productsAdapter.setChecklist(list2);
                    }
                    Log.e("checklist", String.valueOf(productsAdapter.getChecklist()));

                } else if (statuscode == 422) {
                    Log.e("cv", String.valueOf(statuscode));
                   ToastMessage.onToast(SubCatagory_Activity.this, "Try again", ToastMessage.ERROR);
                } else {
                    Log.e("fgd", "sdfsd");
                    ToastMessage.onToast(SubCatagory_Activity.this, "Try again", ToastMessage.ERROR);

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
                ToastMessage.onToast(SubCatagory_Activity.this, "Please Try Again", ToastMessage.ERROR);


//                openpopupscreen("Successfully subscribed to Gold Plus Plan");
            }
        });


    }


    public void onrefresh() {
        swipe_layout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipe_layout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(SubCatagory_Activity.this)) {
                            ToastMessage.onToast(SubCatagory_Activity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            swipe_layout.setRefreshing(false);
                            return;
                        } else {
                            init();
                            getWishlistCount();
                            intilizerecyclerview();
                            getProductsByCats();
                            onrefresh();
                        }
                        swipe_layout.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }

    private void innerfilter(String spinnerweight){
            String filter1 = spinnerweight.replace("gms", "");
            String[] parts = filter1.split("-", 2);
            String part1 = "no data";
            String part2 = "yes data";
            for (String a : parts) {
                part1 = part2;
                part2 = a;
            }

//            Log.e("spinnerweight", part1.trim()+part2.trim());
            filterNewText(Integer.parseInt(part1.trim()), Integer.parseInt(part2.trim()));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterNewText(Integer part1, Integer part2) {
        int lp = part1;
        int hp = part2;
        ArrayList<Listmodel> filteredlist = new ArrayList<>();
        for (Listmodel item : flist) {
            if (Integer.parseInt(item.getPweight())>=lp && Integer.parseInt(item.getPweight())<=hp){
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            productsrv.setVisibility(View.GONE);
            notfound.setVisibility(View.VISIBLE);
        } else {
            notfound.setVisibility(View.GONE);
            productsrv.setVisibility(View.VISIBLE);
            productsAdapter.filterList(filteredlist);
        }
    }


    @SuppressLint("SetTextI18n")
    public void init() {
        sharedPreference = new shared_preference(getApplicationContext());
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        if(!from.equals("cats"))
            titleTv.setText("All Jewellery");
        else
            titleTv.setText(catName);
        notfound = findViewById(R.id.notfound);
        backbtn = findViewById(R.id.backbtn);
//        searchtext = findViewById(R.id.searchtext);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pweightspinner = findViewById(R.id.pweightspinner);

        // create list of customer
        ArrayList<String> pweightsList = getPweightsList();

        //Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.pwightpsinner, pweightsList);

        //Set adapter
        pweightspinner.setAdapter(adapter);

        //submit button click event registration
//        pweightspinner.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                Toast.makeText(this, pweightspinner.getText(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private ArrayList<String> getPweightsList()
    {
        ArrayList<String> plist = new ArrayList<>();
        plist.add("Select Grams");
        plist.add("1 - 5 gms");
        plist.add("6 - 10 gms");
        plist.add("11 - 15 gms");
        plist.add("16 - 20 gms");
        plist.add("21 - 25 gms");
        plist.add("Above 25 gms");
        return plist;
    }

    public void intilizerecyclerview() {
        swipe_layout = findViewById(R.id.swipe_layout);
        productsrv = findViewById(R.id.productsrv);
        productsrv.setHasFixedSize(true);
        productsrv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        productsList = new ArrayList<>();
        productsAdapter = new AllProductsAdapter(this, productsList, this, "cats");
        productsrv.setAdapter(productsAdapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // finish();
    }

    public void getProductsByCats() {
        productsList.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        Call<List<Listmodel>> getproductsbycat = null;
                apiDao = ApiClient.getClient("").create(ApiDao.class);
        if(from.equals("cats"))
            getproductsbycat = apiDao.getProductsByCat(catid);
        else
            getproductsbycat = apiDao.getJewProducts();


        getproductsbycat.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("statuscode", String.valueOf(statuscode));
                flist = response.body();
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("statuscode", String.valueOf(statuscode));
                    if (flist != null) {
                        productsList.clear();
                        Collections.shuffle(flist);
                        for (Listmodel listmodel : flist) {
                            cid = listmodel.getId();
                            productsList.add(listmodel);
                            productsAdapter.notifyDataSetChanged();

                            spinneralert();

                        }
                    } else {
                        Log.e("catname", "No cats");
                    }
//                    openpopupscreen(listmodel.getDescription());
                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
//                        ToastMessage.onToast(Elevenplus_Jewellery.this, String.valueOf(statuscode), ToastMessage.ERROR);
                } else {
                    dialog.dismiss();
//                    ToastMessage.onToast(Elevenplus_Jewellery.this, "Please try again", ToastMessage.ERROR);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("ughb", String.valueOf(t));
//                openpopupscreen("Successfully subscribed to Gold Plus Plan");
            }
        });
    }

    private void spinneralert() {
        pweightspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerweight = parent.getItemAtPosition(position).toString();

                if(spinnerweight.equals("Above 25 gms")){
                    filterNewText(26, 1000);
                }else if(spinnerweight.equals("Select Grams")){
                    filterNewText(0, 1000);
                }else{
                    innerfilter(spinnerweight);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
