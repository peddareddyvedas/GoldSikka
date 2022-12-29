package com.goldsikka.goldsikka.Directory;

import static com.goldsikka.goldsikka.Activitys.Predict_price.PaginationListener.PAGE_START;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Directory.Adapters.DirectoryBannersAdapter;
import com.goldsikka.goldsikka.Directory.Adapters.DirectoryBullionAdapter;
import com.goldsikka.goldsikka.Directory.Adapters.DirectoryCatogoriesAdapter;
import com.goldsikka.goldsikka.Directory.Adapters.DirectoryGoldSmithAdapter;
import com.goldsikka.goldsikka.Directory.Adapters.DirectoryPawnsAdapter;
import com.goldsikka.goldsikka.Directory.Adapters.DirectoryRandomAdapter;
import com.goldsikka.goldsikka.Directory.Adapters.DirectoryStonesAdapter;
import com.goldsikka.goldsikka.Directory.Adapters.DirectoryTagslistingAdapter;
import com.goldsikka.goldsikka.Directory.Adapters.DirectoryTopListingAdapter;
import com.goldsikka.goldsikka.Models.BannersModel;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectoryHomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView toplistrv, bullionlistRV, randomlistRV, pawnbrokersRV,
            goldsmithrv, gemsStonesrv, tagslistrv, bannerlistRV, catogoriesRV;


    ImageView viewall, viewall1, viewall2, viewall3, viewall4, viewall5, viewall6;
    TextView toplisttext, randomview, bullionview, pawnview, gemsview, goldview, tagsview;


    private Activity activity;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage;
    private boolean isLoading = false;
    int itemCount;

    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;
    EditText searchview;
    ApiDao apiDao;
    List<Listmodel> flist;
    String id;
    String cid;
    String pid = "1";

    SwipeRefreshLayout swipeRefresh;

    DirectoryTopListingAdapter directoryTopListingAdapter;
    DirectoryBullionAdapter directoryBullionAdapter;
    DirectoryRandomAdapter directoryRandomAdapter;
    DirectoryGoldSmithAdapter goldSmithAdapter;
    DirectoryPawnsAdapter directoryPawnsAdapter;
    DirectoryStonesAdapter gemsnStoneAdapter;
    DirectoryTagslistingAdapter tagslistingAdapter;
    DirectoryBannersAdapter bannersAdapter;
    DirectoryCatogoriesAdapter catogoriesAdapter;


    ArrayList<Listmodel> toplistlist;
    ArrayList<Listmodel> bullionlist;
    ArrayList<Listmodel> randomlist;
    ArrayList<Listmodel> pawnslist;
    ArrayList<Listmodel> goldsmithlist;
    ArrayList<Listmodel> gemsStoneslist;
    ArrayList<Listmodel> tagslist;
    ArrayList<Listmodel> bannerlistrv;
    ArrayList<Listmodel> catogorieslist;
    ArrayList<Listmodel> ratinglist;

    ArrayList<Listmodel> toplistfilter;
    ArrayList<Listmodel> bullionfilter;
    ArrayList<Listmodel> randomfilter;
    ArrayList<Listmodel> pawnfilter;
    ArrayList<Listmodel> goldsmithfilter;
    ArrayList<Listmodel> goldstonefilter;
    ArrayList<Listmodel> tagsfilter;

    List<BannersModel> sliderItemList;
    ImageView bannerimg, bannerimg1;

    LinearLayout toplistrvv, randomlistrv, bullionlistrv, pwnlistrv, smithlistrv, tagslistrvv, stonelistrv;
    RelativeLayout notfound;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directoryhome);
        init();
    }

    public void init() {
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Directory");
        backbtn = findViewById(R.id.backbtn);
        stonelistrv = findViewById(R.id.stonelistrv);
        toplistrvv = findViewById(R.id.toplistrv);
        randomlistrv = findViewById(R.id.randomlistrv);
        bullionlistrv = findViewById(R.id.bullionlistrv);
        pwnlistrv = findViewById(R.id.pwnlistrv);
        smithlistrv = findViewById(R.id.smithlistrv);
        tagslistrvv = findViewById(R.id.tagslistrv);
        notfound = findViewById(R.id.notfound);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toplistrv = findViewById(R.id.toplistingRV);
        bullionlistRV = findViewById(R.id.bullionlistRV);
        randomlistRV = findViewById(R.id.RandomRV1);
        pawnbrokersRV = findViewById(R.id.PawnBrokerRV);
        goldsmithrv = findViewById(R.id.goldsmithRV);
        gemsStonesrv = findViewById(R.id.gemstoreRV);
        tagslistrv = findViewById(R.id.tagslistingRV);
        bannerlistRV = findViewById(R.id.bannerlistingRV);
        catogoriesRV = findViewById(R.id.catogotiesRV);

        viewall = findViewById(R.id.randomlist);
        viewall1 = findViewById(R.id.bullionlist);
        viewall2 = findViewById(R.id.pawnlist);
        viewall3 = findViewById(R.id.gemslist);
        viewall4 = findViewById(R.id.goldsmithlist);
        viewall6 = findViewById(R.id.tagslist);
        viewall5 = findViewById(R.id.toplist);

        toplisttext = findViewById(R.id.toplisttext);
        randomview = findViewById(R.id.randomview);
        bullionview = findViewById(R.id.bullionview);
        pawnview = findViewById(R.id.pawnview);
        gemsview = findViewById(R.id.gemsview);
        goldview = findViewById(R.id.goldview);
        tagsview = findViewById(R.id.tagsview);

        searchview = findViewById(R.id.search_view);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        bannerimg = findViewById(R.id.bannerimg);
        bannerimg1 = findViewById(R.id.bannerimg1);


        randomview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectorySubRandomActivity.class);
                intent.putExtra("id", "random");
                intent.putExtra("store_name", "storename");
                startActivity(intent);
            }
        });

        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectorySubRandomActivity.class);
                intent.putExtra("id", "random");
                intent.putExtra("store_name", "storename");
                startActivity(intent);
            }
        });

        bullionview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectorySubbullionActivity.class);
                intent.putExtra("id", "id");
                intent.putExtra("store_name", "storename");
                startActivity(intent);
            }
        });

        viewall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectorySubbullionActivity.class);
                intent.putExtra("id", "id");
                intent.putExtra("store_name", "storename");
                startActivity(intent);
            }
        });

        pawnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectorySubPawnActivity.class);
                intent.putExtra("id", "id");
                intent.putExtra("store_name", "storename");
                startActivity(intent);
            }
        });

        viewall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectorySubPawnActivity.class);
                intent.putExtra("id", "id");
                intent.putExtra("store_name", "storename");
                startActivity(intent);
            }
        });

        gemsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectorySubStoneActivity.class);
                intent.putExtra("id", "id");
                intent.putExtra("store_name", "storename");
                startActivity(intent);
            }
        });

        viewall3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectorySubStoneActivity.class);
                intent.putExtra("id", "id");
                intent.putExtra("store_name", "storename");
                startActivity(intent);
            }
        });

        goldview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectorySubGoldsmithActivity.class);
                intent.putExtra("id", "id");
                intent.putExtra("store_name", "storename");
                startActivity(intent);
            }
        });

        viewall4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectorySubGoldsmithActivity.class);
                intent.putExtra("id", "id");
                intent.putExtra("store_name", "storename");
                startActivity(intent);
            }
        });

        toplisttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectorySubTopActivity.class);
                intent.putExtra("id", "random");
                intent.putExtra("store_name", "storename");
                startActivity(intent);
            }
        });

        viewall5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectorySubTopActivity.class);
                intent.putExtra("id", "random");
                intent.putExtra("store_name", "storename");
                startActivity(intent);
            }
        });

        tagsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectorySubTagsActivity.class);
                intent.putExtra("id", "id");
                intent.putExtra("store_name", "storename");
                startActivity(intent);
            }
        });

        viewall6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DirectorySubTagsActivity.class);
                intent.putExtra("id", "id");
                intent.putExtra("store_name", "storename");
                startActivity(intent);
            }
        });


        searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable name) {
                if (searchview.getText().toString().isEmpty()) {
                    toplistrvv.setVisibility(View.VISIBLE);
                    randomlistrv.setVisibility(View.VISIBLE);
                    bullionlistrv.setVisibility(View.VISIBLE);
                    pwnlistrv.setVisibility(View.VISIBLE);
                    smithlistrv.setVisibility(View.VISIBLE);
                    tagslistrvv.setVisibility(View.VISIBLE);
                    stonelistrv.setVisibility(View.VISIBLE);
                    bannerimg.setVisibility(View.VISIBLE);
                    bannerimg1.setVisibility(View.VISIBLE);
                    catogoriesRV.setVisibility(View.VISIBLE);
                } else {
                    toplistrvv.setVisibility(View.GONE);
                    randomlistrv.setVisibility(View.GONE);
                    bullionlistrv.setVisibility(View.GONE);
                    pwnlistrv.setVisibility(View.GONE);
                    smithlistrv.setVisibility(View.GONE);
                    tagslistrvv.setVisibility(View.GONE);
                    stonelistrv.setVisibility(View.GONE);
                    bannerimg.setVisibility(View.GONE);
                    bannerimg1.setVisibility(View.GONE);
                    catogoriesRV.setVisibility(View.GONE);
                    notfound.setVisibility(View.GONE);
                }
                topfilter(name.toString());
                bullionlistfilter(name.toString());
                randomftr(name.toString());
                pawnftr(name.toString());
                goldsmithftr(name.toString());
                goldstoneftr(name.toString());
                tagslistfilter(name.toString());

            }
        });


        intilizerecyclerview();
        getToplist();
        getBullionlist();
        getRandomlist();
        getGoldsmith();
        getPawnslist();
        getGemsStone();
        gettagslist();
        getBannerslist();
        getCatogories();
        getbanner();

    }

    private void getbanner() {

        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<List<Listmodel>> getcatlist = apiDao.get_bannerslist();

        getcatlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("ecomcatstatuscode", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("ecomcatstatuscode1", String.valueOf(statuscode));
                    if (flist != null) {
                        for (Listmodel listmodel : flist) {


                            String image = listmodel.getImage_uri();

                            Log.e("images ", "" + image);
                            for (int i = 0; i < image.length(); i++) {
                                Log.e("imagessss ", "" + image.length());
                                Glide.with(getApplicationContext())
                                        .load(image)
                                        .into(bannerimg);
                            }

                            for (int j = 1; j < image.length(); j++) {
                                Log.e("imagess1 ", "" + image.length());
                                Glide.with(getApplicationContext())
                                        .load(image)
                                        .into(bannerimg1);
                            }
                        }

                    } else {
                        Log.e("catname", "No cats");
                    }
                } else if (statuscode == 422) {
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {

                Log.e("ughb", String.valueOf(t));
            }
        });
    }

    private void goldstoneftr(String name) {
        goldstonefilter = new ArrayList<>();
        for (Listmodel listmodel : gemsStoneslist) {
            if (listmodel.getStore_name().toLowerCase().contains(name.toLowerCase())) {
                goldstonefilter.add(listmodel);
                notfound.setVisibility(View.GONE);


            } else {
                notfound.setVisibility(View.VISIBLE);

            }
            gemsnStoneAdapter.filterList(goldstonefilter);

        }
    }

    private void goldsmithftr(String name) {
        goldsmithfilter = new ArrayList<>();
        for (Listmodel listmodel : goldsmithlist) {
            if (listmodel.getStore_name().toLowerCase().contains(name.toLowerCase())) {
                goldsmithfilter.add(listmodel);
                notfound.setVisibility(View.GONE);


            } else {
                notfound.setVisibility(View.VISIBLE);

            }
            goldSmithAdapter.filterList(goldsmithfilter);

        }

    }

    private void pawnftr(String name) {
        pawnfilter = new ArrayList<>();
        for (Listmodel listmodel : pawnslist) {
            if (listmodel.getStore_name().toLowerCase().contains(name.toLowerCase())) {
                pawnfilter.add(listmodel);
                notfound.setVisibility(View.GONE);

            } else {
                notfound.setVisibility(View.VISIBLE);

            }

            directoryPawnsAdapter.filterList(pawnfilter);

        }
    }

    private void randomftr(String name) {
        randomfilter = new ArrayList<>();
        for (Listmodel listmodel : randomlist) {
            if (listmodel.getStore_name().toLowerCase().contains(name.toLowerCase())) {
                randomfilter.add(listmodel);
                notfound.setVisibility(View.GONE);

            } else {
                notfound.setVisibility(View.VISIBLE);

            }
            directoryRandomAdapter.filterList(randomfilter);

        }
    }

    private void tagslistfilter(String name) {
        tagsfilter = new ArrayList<>();
        for (Listmodel listmodel : tagslist) {
            if (listmodel.getStore_name().toLowerCase().contains(name.toLowerCase())) {
                tagsfilter.add(listmodel);
                notfound.setVisibility(View.GONE);

            } else {
                notfound.setVisibility(View.VISIBLE);

            }
            tagslistingAdapter.filterList(tagsfilter);


        }
    }

    private void bullionlistfilter(String name) {
        bullionfilter = new ArrayList<>();
        for (Listmodel listmodel : bullionlist) {
            if (listmodel.getStore_name().toLowerCase().contains(name.toLowerCase())) {
                bullionfilter.add(listmodel);
                notfound.setVisibility(View.GONE);

            } else {
                notfound.setVisibility(View.VISIBLE);

            }
            directoryBullionAdapter.filterList(bullionfilter);

        }

    }

    private void topfilter(String name) {
        toplistfilter = new ArrayList<>();
        for (Listmodel listmodel : toplistlist) {
            if (listmodel.getStore_name().toLowerCase().contains(name.toLowerCase())) {
                toplistfilter.add(listmodel);
                notfound.setVisibility(View.GONE);
            } else {
                notfound.setVisibility(View.VISIBLE);

            }
            directoryTopListingAdapter.filterList(toplistfilter);

        }
    }


    private void intilizerecyclerview() {
        swipeRefresh.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        toplistrv.setLayoutManager(linearLayoutManager);
        toplistrv.setHasFixedSize(true);
        sliderItemList = new ArrayList<>();
        toplistlist = new ArrayList<>();
        toplistfilter = new ArrayList<>();
        directoryTopListingAdapter = new DirectoryTopListingAdapter(this, toplistlist);
        toplistrv.setAdapter(directoryTopListingAdapter);


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        bullionlistRV.setLayoutManager(linearLayoutManager1);
        bullionlistRV.setHasFixedSize(true);
        bullionfilter = new ArrayList<>();
        bullionlist = new ArrayList<>();
        ratinglist = new ArrayList<>();
        directoryBullionAdapter = new DirectoryBullionAdapter(this, bullionlist);
        bullionlistRV.setAdapter(directoryBullionAdapter);


        randomlistRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        randomlistRV.setHasFixedSize(true);
        randomlist = new ArrayList<>();
        randomlistRV.setItemViewCacheSize(1000);
        randomfilter = new ArrayList<>();
        directoryRandomAdapter = new DirectoryRandomAdapter(this, randomlist);
        randomlistRV.setAdapter(directoryRandomAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        goldsmithrv.setLayoutManager(staggeredGridLayoutManager);
        goldsmithrv.setHasFixedSize(true);
        goldsmithlist = new ArrayList<>();
        goldsmithfilter = new ArrayList<>();
        goldSmithAdapter = new DirectoryGoldSmithAdapter(this, goldsmithlist);
        goldsmithrv.setAdapter(goldSmithAdapter);


        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        pawnbrokersRV.setLayoutManager(linearLayoutManager3);
        pawnbrokersRV.setHasFixedSize(true);
        pawnslist = new ArrayList<>();
        pawnfilter = new ArrayList<>();
        directoryPawnsAdapter = new DirectoryPawnsAdapter(this, pawnslist);
        pawnbrokersRV.setAdapter(directoryPawnsAdapter);

        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        gemsStonesrv.setLayoutManager(linearLayoutManager4);
        gemsStonesrv.setHasFixedSize(true);
        gemsStoneslist = new ArrayList<>();
        goldstonefilter = new ArrayList<>();
        gemsnStoneAdapter = new DirectoryStonesAdapter(this, gemsStoneslist);
        gemsStonesrv.setAdapter(gemsnStoneAdapter);

        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager5.setOrientation(LinearLayoutManager.HORIZONTAL);
        tagslistrv.setLayoutManager(linearLayoutManager5);
        tagslistrv.setHasFixedSize(true);
        tagslist = new ArrayList<>();
        tagsfilter = new ArrayList<>();
        tagslistingAdapter = new DirectoryTagslistingAdapter(this, tagslist);
        tagslistrv.setAdapter(tagslistingAdapter);


        LinearLayoutManager linearLayoutManager7 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager7.setOrientation(LinearLayoutManager.HORIZONTAL);
        bannerlistRV.setLayoutManager(linearLayoutManager7);
        bannerlistRV.setHasFixedSize(true);
        bannerlistrv = new ArrayList<>();
        bannersAdapter = new DirectoryBannersAdapter(this, bannerlistrv);
        bannerlistRV.setAdapter(bannersAdapter);

        LinearLayoutManager layoutManager1 = new GridLayoutManager(getApplicationContext(), 4,
                GridLayoutManager.VERTICAL, false);
        catogoriesRV.setLayoutManager(layoutManager1);
        catogoriesRV.setHasFixedSize(true);
        catogorieslist = new ArrayList<>();
        catogoriesAdapter = new DirectoryCatogoriesAdapter(this, catogorieslist);
        catogoriesRV.setAdapter(catogoriesAdapter);

    }

    private void getCatogories() {
        catogorieslist.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();

        } else {
            apiDao = ApiClient.getClient("").create(ApiDao.class);
            Call<List<Listmodel>> getcatlist = apiDao.get_catogorieslist();

            getcatlist.enqueue(new Callback<List<Listmodel>>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    dialog.dismiss();

                    Log.e("statuscode", String.valueOf(statuscode));
                    flist = response.body();
                    catogorieslist.clear();
                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("statuscode", String.valueOf(statuscode));
                        if (flist != null) {
                            for (Listmodel listmodel : flist) {
                                catogorieslist.add(listmodel);
                                catogoriesAdapter.notifyDataSetChanged();

                            }

                        } else {
                            Log.e("catname", "No cats");
                            swipeRefresh.setRefreshing(false);
                            dialog.dismiss();
                        }
                    } else if (statuscode == 422) {
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    }

                }

                @Override
                public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e("ughb", String.valueOf(t));
                }
            });
        }
    }

    private void getBannerslist() {
        bannerlistrv.clear();
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<List<Listmodel>> getcatlist = apiDao.get_bannerslist();

        getcatlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("ecomcatstatuscode", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("ecomcatstatuscode1", String.valueOf(statuscode));
                    if (flist != null) {
                        for (Listmodel listmodel : flist) {
                            pid = listmodel.getId();
                            bannerlistrv.add(listmodel);
                            bannersAdapter.notifyDataSetChanged();

                        }

                    } else {
                        Log.e("catname", "No cats");
                    }
                } else if (statuscode == 422) {
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
            }
        });


    }


    private void getToplist() {
        toplistlist.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();

        } else {
            apiDao = ApiClient.getClient("").create(ApiDao.class);
            Call<List<Listmodel>> getcatlist = apiDao.get_toplisting();
            getcatlist.enqueue(new Callback<List<Listmodel>>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    Log.e("statuscode", String.valueOf(statuscode));
                    flist = response.body();
                    dialog.dismiss();
                    Log.e("tamountstatusssss", "" + flist);
                    toplistlist.clear();
                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("statuscode", String.valueOf(statuscode));
                        if (flist != null) {
                            for (Listmodel listmodel : flist) {
                                id = listmodel.getId();
                                toplistlist.add(listmodel);
                                directoryTopListingAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e("catname", "No cats");
                            dialog.dismiss();
                        }
                    } else if (statuscode == 404) {
                        dialog.dismiss();

                        Log.e("400statuscaod", "" + statuscode);
                    } else if (statuscode == 422) {
                        dialog.dismiss();
                        toplistrvv.setVisibility(View.GONE);

                        Log.e("cv", String.valueOf(statuscode));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e("ughb", String.valueOf(t));
                }
            });
        }

    }

    private void getBullionlist() {
        bullionlist.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();

        } else {
            apiDao = ApiClient.getClient("").create(ApiDao.class);
            Call<List<Listmodel>> getproductslist = apiDao.getcatogorieslistid("Bearer " + AccountUtils.getAccessToken(this), "5");

            getproductslist.enqueue(new Callback<List<Listmodel>>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    dialog.dismiss();
                    Log.e("ccccccstatuscode", String.valueOf(statuscode));
                    flist = response.body();
                    bullionlist.clear();
                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("topsellstatuscode1", String.valueOf(statuscode));
                        if (flist != null) {
                            for (Listmodel listmodel : flist) {
                                bullionlist.add(listmodel);
                                bullionfilter.add(listmodel);
                                directoryBullionAdapter.notifyDataSetChanged();
                                Log.e("catname", "No cats");

                            }
                        }
                    } else if (statuscode == 404) {
                        dialog.dismiss();

                        Log.e("400statuscaod", "" + statuscode);
                    } else if (statuscode == 422) {
                        dialog.dismiss();
                        bullionlistrv.setVisibility(View.GONE);

                        Log.e("cv", String.valueOf(statuscode));
                    } else {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e("ughb", String.valueOf(t));
                }
            });
        }
    }

    private void getRandomlist() {
        randomlist.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();

        } else {
            apiDao = ApiClient.getClient("").create(ApiDao.class);
            Call<List<Listmodel>> getproductslist = apiDao.gettagslistid("Bearer " + AccountUtils.getAccessToken(this), "random");
            getproductslist.enqueue(new Callback<List<Listmodel>>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    Log.e("statusrandom", String.valueOf(statuscode));
                    dialog.dismiss();
                    flist = response.body();
                    randomlist.clear();
                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("status", String.valueOf(statuscode));
                        if (flist != null) {
                            for (Listmodel listmodel : flist) {
                                randomlist.add(listmodel);
                                randomfilter.add(listmodel);
                                directoryRandomAdapter.notifyDataSetChanged();

                            }

                        }
                    } else if (statuscode == 422) {
                        randomlistrv.setVisibility(View.GONE);
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    } else if (statuscode == 404) {
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    } else {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e("ughb", String.valueOf(t));
                }
            });
        }
    }

    private void getGoldsmith() {
        goldsmithlist.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();

        } else {
            apiDao = ApiClient.getClient("").create(ApiDao.class);
            Call<List<Listmodel>> getproductslist = apiDao.getcatogorieslistid("Bearer " + AccountUtils.getAccessToken(this), "3");
            getproductslist.enqueue(new Callback<List<Listmodel>>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    Log.e("ccccccstatuscode", String.valueOf(statuscode));
                    dialog.dismiss();
                    flist = response.body();
                    goldsmithlist.clear();

                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("topsellstatuscode1", String.valueOf(statuscode));
                        if (flist != null) {


                            for (Listmodel listmodel : flist) {
                                cid = listmodel.getId();
                                goldsmithlist.add(listmodel);
                                goldsmithfilter.add(listmodel);
                                goldSmithAdapter.notifyDataSetChanged();
                                Log.e("catname", "No cats");

                            }
                        }
                    } else if (statuscode == 422) {
                        smithlistrv.setVisibility(View.GONE);
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    } else if (statuscode == 404) {
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    } else {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e("ughb", String.valueOf(t));
                }
            });
        }

    }

    private void getPawnslist() {
        pawnslist.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();

        } else {
            apiDao = ApiClient.getClient("").create(ApiDao.class);
            Call<List<Listmodel>> getproductslist = apiDao.getcatogorieslistid("Bearer " + AccountUtils.getAccessToken(this), "2");

            getproductslist.enqueue(new Callback<List<Listmodel>>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    Log.e("topsellstatuscode", String.valueOf(statuscode));
                    dialog.dismiss();
                    flist = response.body();
                    pawnslist.clear();
                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("topsellstatuscode1", String.valueOf(statuscode));
                        if (flist != null) {

                            for (Listmodel listmodel : flist) {
                                pawnslist.add(listmodel);
                                pawnfilter.add(listmodel);
                                directoryPawnsAdapter.notifyDataSetChanged();
                                Log.e("catname", "No cats");

                            }
                        }
                    } else if (statuscode == 422) {
                        pwnlistrv.setVisibility(View.GONE);
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    } else if (statuscode == 404) {
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    } else {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e("ughb", String.valueOf(t));
                }
            });
        }
    }

    private void getGemsStone() {
        gemsStoneslist.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();

        } else {
            apiDao = ApiClient.getClient("").create(ApiDao.class);
            Call<List<Listmodel>> getproductslist = apiDao.getcatogorieslistid("Bearer " + AccountUtils.getAccessToken(this), "4");
            getproductslist.enqueue(new Callback<List<Listmodel>>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    dialog.dismiss();
                    gemsStoneslist.clear();
                    flist = response.body();
                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("statuscode", String.valueOf(statuscode));
                        if (flist != null) {
                            for (Listmodel listmodel : flist) {


                                if (gemsStoneslist.equals(null)) {
                                    stonelistrv.setVisibility(View.GONE);
                                } else {
                                    stonelistrv.setVisibility(View.VISIBLE);
                                    gemsStoneslist.add(listmodel);
                                    goldstonefilter.add(listmodel);
                                    gemsnStoneAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    } else if (statuscode == 422) {
                        stonelistrv.setVisibility(View.GONE);
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    } else if (statuscode == 404) {
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    } else {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e("ughb", String.valueOf(t));
                }
            });
        }
    }

    private void gettagslist() {
        tagslist.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();

        } else {
            apiDao = ApiClient.getClient("").create(ApiDao.class);
            Call<List<Listmodel>> getcatlist = apiDao.gettagslisting();

            getcatlist.enqueue(new Callback<List<Listmodel>>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    Log.e("statuscode", String.valueOf(statuscode));
                    dialog.dismiss();
                    flist = response.body();
                    tagslist.clear();
                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("statuscode", String.valueOf(statuscode));
                        if (flist != null) {
                            for (Listmodel listmodel : flist) {

                                tagslist.add(listmodel);
                                tagsfilter.add(listmodel);
                                tagslistingAdapter.notifyDataSetChanged();
                            }

                        }
                    } else if (statuscode == 422) {
                        tagslistrvv.setVisibility(View.GONE);
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    } else if (statuscode == 404) {
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    } else {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e("ughb", String.valueOf(t));
                }
            });
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
            init();
            intilizerecyclerview();

            getToplist();
            getBullionlist();
            getRandomlist();
            getGoldsmith();
            getPawnslist();
            getGemsStone();
            gettagslist();
            getCatogories();

            swipeRefresh.setRefreshing(false);

        }


    }
}