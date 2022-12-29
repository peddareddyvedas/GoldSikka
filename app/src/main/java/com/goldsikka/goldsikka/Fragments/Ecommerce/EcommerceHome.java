package com.goldsikka.goldsikka.Fragments.Ecommerce;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.goldsikka.goldsikka.ComingSoon;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters.EcommGiftCardAdapter;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters.Ecommerce_Home_Adapter;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters.EcomproductfilterAdapter;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters.EcomsubcatfilterAdapter;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters.Newarrival_Ecomm_Adapter;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters.Ourbrands_Ecomm_Adapter;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters.PageviewecomAdapter;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters.Topselling_Ecomm_Adapter;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters.Underbudget_Ecomm_Adapter;
import com.goldsikka.goldsikka.Models.BannersModel;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.CheckoutModel;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.Sizes;
import com.goldsikka.goldsikka.model.Stones;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EcommerceHome extends Fragment implements OnItemClickListener, View.OnClickListener {

    ApiDao apiDao;
    private Activity activity;
    RecyclerView rv_categorylist, homenewarrivalls, hometopsellingproducts, homeourbranslist, homepriceunderlist, homebullionlist;
    Ecommerce_Home_Adapter adapter;

    Newarrival_Ecomm_Adapter ecomm_adapter;
    String bangles;


    Topselling_Ecomm_Adapter topsellecomm;
    Underbudget_Ecomm_Adapter underbudget_ecomm;
    Ourbrands_Ecomm_Adapter brand_ecom;

    ArrayList<Listmodel> arrayList;
    String categoryid, get_banners;
    shared_preference sharedPreference;
    TextView uidTv, unameTv, wihslistcountTv, jwish, viewallproducts;
    ImageView bullionimg;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES;
    private ArrayList<String> urls = new ArrayList<>();
    String banners;

    SwipeRefreshLayout swipe_layout;
    View view;
    List<BannersModel> sliderItemList;

    ArrayList<Listmodel> productsList;
    ArrayList<Listmodel> topsellList;
    ArrayList<Listmodel> ourbrandsList;
    ArrayList<Listmodel> priceunderList;


    List<Listmodel> flist, filteredlist, fflist;
    String cid, catid;
    int wcount = 0;
    int cartcount = 0;
    ImageView wishlistbtn, cartbtn, myordersbtn, giftcards;
    TextView viewallproducts1, viewallproducts2, viewallproducts3, cartcountTv, ordercountTv;
    EditText itemSelectET;

    RecyclerView ecomm_subcategorylistfilter, ecomm_productcategorylistfilter, recyclergiftcards;
    EcomsubcatfilterAdapter ecomsubcatfilteradapter;
    EcomproductfilterAdapter ecomproductfilteradapter;
    EcommGiftCardAdapter giftCardAdapter;
    ArrayList<Listmodel> subfilterarrayList;
    ArrayList<Listmodel> productfilterarrayList;

    ArrayList<Listmodel> subfilterarrayList1;
    ArrayList<Listmodel> productfilterarrayList1;
    ArrayList<Listmodel> subCatfilter;
    ArrayList<Listmodel> ecommgiftcardlist;
    ArrayList<Listmodel> giftcardfilter;


    RelativeLayout rlpriceunder, rlourbrands, rltopselling, rlnewarrivals, rlbacknewarrival;
    LinearLayout llindicater, goldrates;
    TextView textcategory, textsubcategory, textproduct;
    RelativeLayout notfound;
    String value = "21";
    TextView tvlocation, tvdate, tvtime, tvliverate;
    String liveprice;
    JSONObject object, object1, jsonObj;
    RelativeLayout rlgiftcards;

    Listmodel item;
    Listmodel pflist;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_ecom_home, container, false);
        uidTv = view.findViewById(R.id.uid);
        unameTv = view.findViewById(R.id.uname);
        viewallproducts = view.findViewById(R.id.viewallproducts);
        viewallproducts1 = view.findViewById(R.id.viewallproducts1);
        viewallproducts2 = view.findViewById(R.id.viewallproducts3);
        viewallproducts3 = view.findViewById(R.id.viewallproducts4);

        intilizerecyclerview(view);
        getAllEcomCategory();
        getEcomBanners();
        getTopsellingProducts();
        getGIFTCARD();
        getEommNewArrivals();
        getEcomPriceUnderBox();
        getAllEcomBrands();


        cartcountTv = view.findViewById(R.id.cartcountTv);
        uidTv.setText(AccountUtils.getCustomerID(activity));
        unameTv.setText(AccountUtils.getName(activity));
        sharedPreference = new shared_preference(getApplicationContext());
        wihslistcountTv = view.findViewById(R.id.wihslistcountTv);
        wishlistbtn = view.findViewById(R.id.wishlistbtn);
        cartbtn = view.findViewById(R.id.cartbtn);
        myordersbtn = view.findViewById(R.id.myordersbtn);
        ordercountTv = view.findViewById(R.id.ordercountTv);
        rlpriceunder = view.findViewById(R.id.rlpriceunder);
        rlourbrands = view.findViewById(R.id.rlourbrands);
        rltopselling = view.findViewById(R.id.rltopselling);
        llindicater = view.findViewById(R.id.llindicater);
        rlnewarrivals = view.findViewById(R.id.rlnewarrivals);
        rlbacknewarrival = view.findViewById(R.id.rlbacknewarrival);
        textcategory = view.findViewById(R.id.textcategory);
        textsubcategory = view.findViewById(R.id.textsubcategory);
        textproduct = view.findViewById(R.id.textproduct);
        notfound = view.findViewById(R.id.notfound);
        rlgiftcards = view.findViewById(R.id.rlgiftcards);
        goldrates = view.findViewById(R.id.goldrates);
        tvlocation = view.findViewById(R.id.tvlocation);
        tvdate = view.findViewById(R.id.tvdate);
        tvtime = view.findViewById(R.id.tvtime);
        tvliverate = view.findViewById(R.id.tvliverate);
        itemSelectET = view.findViewById(R.id.et_item);
        itemSelectET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable name) {
                if (itemSelectET.getText().toString().isEmpty()) {
                    Log.e("icallllll", "entertextempty");
                    rlpriceunder.setVisibility(View.VISIBLE);
                    rlourbrands.setVisibility(View.VISIBLE);
                    rltopselling.setVisibility(View.VISIBLE);
                    mPager.setVisibility(View.VISIBLE);
                    llindicater.setVisibility(View.VISIBLE);
                    rlnewarrivals.setVisibility(View.VISIBLE);
                    homenewarrivalls.setVisibility(View.VISIBLE);
                    hometopsellingproducts.setVisibility(View.VISIBLE);
                    homeourbranslist.setVisibility(View.VISIBLE);
                    homepriceunderlist.setVisibility(View.VISIBLE);
                    rlbacknewarrival.setVisibility(View.VISIBLE);
                    bullionimg.setVisibility(View.VISIBLE);
                    rv_categorylist.setVisibility(View.VISIBLE);
                    ecomm_subcategorylistfilter.setVisibility(View.GONE);
                    ecomm_productcategorylistfilter.setVisibility(View.GONE);
                    textcategory.setVisibility(View.VISIBLE);
                    textsubcategory.setVisibility(View.GONE);
                    textproduct.setVisibility(View.GONE);
                    goldrates.setVisibility(View.VISIBLE);

                    // editText is empty
                } else {
                    Log.e("icallllll", "entertfull");

                    // editText is not empty
                    rlpriceunder.setVisibility(View.GONE);
                    rlourbrands.setVisibility(View.GONE);
                    rltopselling.setVisibility(View.GONE);
                    mPager.setVisibility(View.GONE);
                    llindicater.setVisibility(View.GONE);
                    rlnewarrivals.setVisibility(View.GONE);
                    homenewarrivalls.setVisibility(View.GONE);
                    hometopsellingproducts.setVisibility(View.GONE);
                    homeourbranslist.setVisibility(View.GONE);
                    homepriceunderlist.setVisibility(View.GONE);
                    rlbacknewarrival.setVisibility(View.GONE);
                    bullionimg.setVisibility(View.GONE);
                    rv_categorylist.setVisibility(View.VISIBLE);
                    ecomm_subcategorylistfilter.setVisibility(View.VISIBLE);
                    ecomm_productcategorylistfilter.setVisibility(View.VISIBLE);
                    textcategory.setVisibility(View.VISIBLE);
                    textsubcategory.setVisibility(View.VISIBLE);
                    textproduct.setVisibility(View.VISIBLE);
                    goldrates.setVisibility(View.GONE);

                }
                subCatfilter(name.toString());
                // subfilterarrayList1(name.toString());
                // productfilterarrayList1(name.toString());
                //   giftcardfilter(name.toString());
            }
        });


        jwish = view.findViewById(R.id.jwish);
        String currentTime = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        if (Integer.parseInt(currentTime) < 12)
            jwish.setText("Good Morning,");
        else if (Integer.parseInt(currentTime) >= 12 && Integer.parseInt(currentTime) < 16)
            jwish.setText("Good Afternoon,");
        else if (Integer.parseInt(currentTime) >= 16 && Integer.parseInt(currentTime) <= 24)
            jwish.setText("Good Evening,");

//        init();


        // onrefresh();
        wishlistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Ecomfavouritiesactivity.class));
            }
        });
        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EcommCartActivity.class));

            }
        });
        myordersbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EcommMyOrdersActivity.class));
            }
        });
        viewallproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), ComingSoon.class));
            }
        });
        viewallproducts1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), ComingSoon.class));
            }
        });
        viewallproducts2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), ComingSoon.class));
            }
        });
        viewallproducts3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), ComingSoon.class));
            }
        });


        giftcards = view.findViewById(R.id.giftcardimg);
        giftcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, EcomSubcategory.class);
                intent.putExtra("cid", "22");
                intent.putExtra("fromto", "gift");
                intent.putExtra("toolname", "Gift Cards");

                startActivity(intent);

            }
        });


        getEcomWishlistCount();
        getEcomCartCount();
        getEcommmyorders();
        getliveprices();
        return view;
    }


    public void getliveprices() {

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
        Call<Listmodel> getliverates = apiDao.getlive_rates("Bearer " + AccountUtils.getAccessToken(activity));
        getliverates.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(@NonNull Call<Listmodel> call, @NonNull Response<Listmodel> response) {
                int statuscode = response.code();
                List<Listmodel> list = Collections.singletonList(response.body());
                if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {

                    if (list != null) {
                        for (Listmodel listmodel : list) {
                            liveprice = listmodel.getSell_price_per_gram();
                            Log.e("liveprice", liveprice);
                            //tv_sellprice.setText(getString(R.string.Rs) + liveprice);
                            tvliverate.setText("₹ " + liveprice);
                            tvdate.setText(listmodel.getDate());
                            tvtime.setText(listmodel.getTime());
                            tvlocation.setText(listmodel.getLocation());
                            AccountUtils.setGsttax(activity, listmodel.getTaxPercentage());
                        }
                    } else {
                    }
                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String st = jObjError.getString("message");
                        JSONObject er = jObjError.getJSONObject("errors");
//                            Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                //    ToastMessage.onToast(activity, "We have some issues", ToastMessage.ERROR);
            }
        });
    }


    private void subfilterarrayList1(String name) {
        subfilterarrayList1 = new ArrayList<>();
        for (Listmodel listmodel : subfilterarrayList) {
            if (listmodel.getSubcatname().toLowerCase().contains(name.toLowerCase())) {
                subfilterarrayList1.add(listmodel);
            } else {
                textsubcategory.setVisibility(View.GONE);
            }
        }
        ecomsubcatfilteradapter.filterList(subfilterarrayList1);
    }

    private void productfilterarrayList1(String name) {
        productfilterarrayList1 = new ArrayList<>();
        for (Listmodel listmodel : productfilterarrayList) {
            if (listmodel.getPname().toLowerCase().contains(name.toLowerCase())) {
                productfilterarrayList1.add(listmodel);
            } else {
                textproduct.setVisibility(View.GONE);
            }
        }
        ecomproductfilteradapter.filterList(productfilterarrayList1);
    }


    private void subCatfilter(String name) {
        subCatfilter = new ArrayList<>();
        for (Listmodel listmodel : arrayList) {
            if (listmodel.getCatname().toLowerCase().contains(name.toLowerCase())) {
                subCatfilter.add(listmodel);
                textcategory.setVisibility(View.VISIBLE);
                notfound.setVisibility(View.GONE);

            } else {
                textcategory.setVisibility(View.GONE);
                notfound.setVisibility(View.VISIBLE);
            }
        }
        adapter.filterList(subCatfilter);
    }

    private void giftcardfilter(String name) {

        giftcardfilter = new ArrayList<>();
        for (Listmodel listmodel : ecommgiftcardlist) {
            if (listmodel.getPname().toLowerCase().contains(name.toLowerCase())) {
                giftcardfilter.add(listmodel);
                rlgiftcards.setVisibility(View.VISIBLE);
            } else {
                rlgiftcards.setVisibility(View.GONE);
                notfound.setVisibility(View.GONE);
            }
        }
        giftCardAdapter.filterList(giftcardfilter);
    }
  /*  public void onrefresh() {
        swipe_layout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipe_layout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(getApplicationContext())) {
                            ToastMessage.onToast(getApplicationContext(), getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            swipe_layout.setRefreshing(false);
                            return;
                        } else {

                            init();
                            onrefresh();
                            getEcomWishlistCount();
                            intilizerecyclerview(view);
                            getAllEcomCategory();
                            getTopsellingProducts();
                            getEommNewArrivals();
                            getEcomPriceUnderBox();
                            getAllEcomBrands();
                        }
                        swipe_layout.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }*/

    @Override
    public void onResume() {
        super.onResume();
        getEcomWishlistCount();
        getEcomCartCount();
        getEcommmyorders();
        getliveprices();

    }


    public void getAllEcomCategory() {
        subCatfilter.clear();
        arrayList.clear();
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<List<Listmodel>> getcatlist = apiDao.get_ecomcotegorylist();

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
                            cid = listmodel.getId();

                            getSubCats(cid);

                            Log.e("catid", "" + cid);
                            if (cid.equals("21") || listmodel.getCatname().equals("Bullion")) {
                                arrayList.remove(listmodel);
                            } else if (cid.equals("22") || listmodel.getCatname().equals("Gift Cards")) {
                                arrayList.remove(listmodel);
                            } else {
                                arrayList.add(listmodel);
                                subCatfilter.add(listmodel);
                                adapter.notifyDataSetChanged();
                            }


                            rv_categorylist.post(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });


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


    public void intilizerecyclerview(View view) {
        // swipe_layout = view.findViewById(R.id.swipe_layout);
        rv_categorylist = view.findViewById(R.id.ecomm_categorylist);
        homenewarrivalls = view.findViewById(R.id.homenewarrivalls);
        hometopsellingproducts = view.findViewById(R.id.hometopsellingproducts);
        homeourbranslist = view.findViewById(R.id.homeourbranslist);
        homepriceunderlist = view.findViewById(R.id.homepriceunderlist);
        ecomm_subcategorylistfilter = view.findViewById(R.id.ecomm_subcategorylistfilter);
        ecomm_productcategorylistfilter = view.findViewById(R.id.ecomm_productcategorylistfilter);

        recyclergiftcards = view.findViewById(R.id.recyclergiftcards);

        bullionimg = view.findViewById(R.id.bullionimg);
        bullionimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, EcomSubcategory.class);
                intent.putExtra("cid", "21");
                intent.putExtra("fromto", "value");
                intent.putExtra("toolname", "Bullion");

                startActivity(intent);

            }
        });


        /////filter recycler views

        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager5.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
        ecomm_subcategorylistfilter.setLayoutManager(linearLayoutManager5); // set LayoutManager to RecyclerView
        ecomm_subcategorylistfilter.setHasFixedSize(true);
        subfilterarrayList = new ArrayList<>();
        subfilterarrayList1 = new ArrayList<>();
        ecomsubcatfilteradapter = new EcomsubcatfilterAdapter(activity, subfilterarrayList, this);
        ecomm_subcategorylistfilter.setAdapter(ecomsubcatfilteradapter);

       /* LinearLayoutManager linearLayoutManager6 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager6.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
        ecomm_productcategorylistfilter.setLayoutManager(linearLayoutManager6); // set LayoutManager to RecyclerView
        ecomm_productcategorylistfilter.setHasFixedSize(true);
        productfilterarrayList = new ArrayList<>();
        productfilterarrayList1 = new ArrayList<>();
        ecomproductfilteradapter = new EcomproductfilterAdapter(activity, productfilterarrayList, this);
        ecomm_productcategorylistfilter.setAdapter(ecomproductfilteradapter);

*/


        LinearLayoutManager linearLayoutManager6 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager6.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
        recyclergiftcards.setLayoutManager(linearLayoutManager6); // set LayoutManager to RecyclerView
        recyclergiftcards.setHasFixedSize(true);
        ecommgiftcardlist = new ArrayList<>();
        giftcardfilter = new ArrayList<>();
        giftCardAdapter = new EcommGiftCardAdapter(ecommgiftcardlist, activity, this);
        recyclergiftcards.setAdapter(giftCardAdapter);

        ecomm_productcategorylistfilter.setHasFixedSize(true);
        ecomm_productcategorylistfilter.setLayoutManager(new GridLayoutManager(activity, 2));
        productfilterarrayList = new ArrayList<>();
        productfilterarrayList1 = new ArrayList<>();
        ecomm_productcategorylistfilter.setItemViewCacheSize(1000);
        ecomproductfilteradapter = new EcomproductfilterAdapter(activity, productfilterarrayList, this);
        ecomm_productcategorylistfilter.setAdapter(ecomproductfilteradapter);


/////Main Recyclerviews
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
        rv_categorylist.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        rv_categorylist.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        subCatfilter = new ArrayList<>();
        adapter = new Ecommerce_Home_Adapter(activity, arrayList, this);
        rv_categorylist.setAdapter(adapter);
        mPager = (ViewPager) view.findViewById(R.id.banners);
        sliderItemList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
        homenewarrivalls.setLayoutManager(linearLayoutManager1); // set LayoutManager to RecyclerView
        homenewarrivalls.setHasFixedSize(true);
        homenewarrivalls.setItemViewCacheSize(1000);
        productsList = new ArrayList<>();
        ecomm_adapter = new Newarrival_Ecomm_Adapter(productsList, activity, this);
        homenewarrivalls.setAdapter(ecomm_adapter);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
        hometopsellingproducts.setLayoutManager(linearLayoutManager2); // set LayoutManager to RecyclerView
        hometopsellingproducts.setHasFixedSize(true);
        topsellList = new ArrayList<>();
        topsellecomm = new Topselling_Ecomm_Adapter(topsellList, activity, this);
        hometopsellingproducts.setAdapter(topsellecomm);

        homepriceunderlist.setHasFixedSize(true);
        homepriceunderlist.setLayoutManager(new GridLayoutManager(activity, 2));
        priceunderList = new ArrayList<>();
        homepriceunderlist.setItemViewCacheSize(1000);
        underbudget_ecomm = new Underbudget_Ecomm_Adapter(priceunderList, activity, this);
        homepriceunderlist.setAdapter(underbudget_ecomm);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
        homeourbranslist.setLayoutManager(linearLayoutManager3); // set LayoutManager to RecyclerView
        homeourbranslist.setHasFixedSize(true);
        ourbrandsList = new ArrayList<>();
        brand_ecom = new Ourbrands_Ecomm_Adapter(ourbrandsList, activity, this);
        homeourbranslist.setAdapter(brand_ecom);


    }

    public void getAllEcomBrands() {
        ourbrandsList.clear();
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<List<Listmodel>> getcatlist = apiDao.getEcomBrands();

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
                            cid = listmodel.getId();
                            ourbrandsList.add(listmodel);
                            brand_ecom.notifyDataSetChanged();
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


    private void getEcommmyorders() {
        ordercountTv.setText("0");
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(getActivity())).create(ApiDao.class);
        Call<List<Listmodel>> getecomcartlist = apiDao.getecommyorders("Bearer " + AccountUtils.getAccessToken(getActivity()));
        getecomcartlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("myorderstatuscodess", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 202) {
                    Log.e("myorderstatuscodess", String.valueOf(statuscode));
                    if (flist != null) {
                        wcount = flist.size();
                        Log.e("count", String.valueOf(wcount));
                        if (wcount == 0) {
                            ordercountTv.setText("0");
                        } else {
                            ordercountTv.setText(String.valueOf(wcount));
                        }

                    } else {
                        Log.e("countb1", String.valueOf(wcount));

                        wcount = 0;
                        ordercountTv.setText(String.valueOf(wcount));
                    }



                   /* if (flist != null) {

                    } else {
                        Log.e("catname", "No Products");
                    }*/
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


    //// Bannersdata///////

    public void getEcomBanners() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        sliderItemList.clear();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(getActivity())).create(ApiDao.class);
        Call<List<Listmodel>> getdetails = apiDao.getEcomBanners();

        getdetails.enqueue(new Callback<List<Listmodel>>() {
            @Override
            public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                int statuscoe = response.code();
                Log.e("bannerStatuscode", String.valueOf(statuscoe));
                if (statuscoe == HttpsURLConnection.HTTP_OK || statuscoe == HttpsURLConnection.HTTP_CREATED) {
                    dialog.dismiss();
                    List<Listmodel> list = response.body();
                    sliderItemList.clear();

                    if (list != null) {
                        for (Listmodel listmodel : list) {
                            get_banners = listmodel.getImage_uri();
                            Log.e("Banners", "" + get_banners);
                            init();
                        }
                    } else {
                        Toast.makeText(activity, "No Images", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                dialog.dismiss();
                // Toast.makeText(activity, "onFailure", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //////Ecomwishcount///

    public void getEcomWishlistCount() {
        wihslistcountTv.setText("0");

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(getActivity())).create(ApiDao.class);
        Call<List<Listmodel>> getWishlist = apiDao.getEcomFavvouritelist("Bearer " + AccountUtils.getAccessToken(getActivity()));
        getWishlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("statuscode", String.valueOf(statuscode));
//                assert response.body() != null;
                adapter.setFlistwish(response.body());
                Log.e("counttop", String.valueOf(wcount));
                if (statuscode == 200 || statuscode == 202) {
                    if (!adapter.getFlistwish().isEmpty()) {
                        wcount = adapter.getFlistwish().size();
                        Log.e("count", String.valueOf(wcount));
                        if (wcount == 0)
                            wihslistcountTv.setText("0");
                        else
                            wihslistcountTv.setText(String.valueOf(wcount));

                    } else {
                        Log.e("countb1", String.valueOf(wcount));

                        wcount = 0;
                        wihslistcountTv.setText(String.valueOf(wcount));
                    }
                } else if (statuscode == 422) {
                    Log.e("countb2", String.valueOf(wcount));

                    Log.e("cv", String.valueOf(statuscode));
                    wihslistcountTv.setText(String.valueOf(wcount));
                } else {
                    Log.e("countb3", String.valueOf(wcount));
                    Log.e("fgd", "sdfsd");
                    wihslistcountTv.setText("0");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
                Log.e("countb4", String.valueOf(wcount));
                wihslistcountTv.setText(String.valueOf(wcount));

//                openpopupscreen("Successfully subscribed to Gold Plus Plan");
            }
        });


    }


    //////EcomCartCount

    private void getEcomCartCount() {
//        cartList.clear();
        cartcountTv.setText("0");

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(getApplicationContext())).create(ApiDao.class);
        Call<List<Listmodel>> getecomcartlist = apiDao.getEcomcartitem("Bearer " + AccountUtils.getAccessToken(getApplicationContext()));
        getecomcartlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("cartstatuscodess", String.valueOf(statuscode));
                flist = response.body();
                Log.e("counttop", String.valueOf(cartcount));
                if (statuscode == 200 || statuscode == 202) {
                    if (!flist.isEmpty()) {
                        cartcount = flist.size();
                        Log.e("count", String.valueOf(cartcount));
                        if (cartcount == 0)
                            cartcountTv.setText("0");
                        else
                            cartcountTv.setText(String.valueOf(cartcount));

                    } else {
                        Log.e("countb1", String.valueOf(cartcount));

                        cartcount = 0;
                        cartcountTv.setText(String.valueOf(cartcount));
                    }
                } else if (statuscode == 422) {
                    Log.e("countb2", String.valueOf(cartcount));

                    Log.e("cv", String.valueOf(statuscode));
                    cartcountTv.setText(String.valueOf(cartcount));
                } else {
                    Log.e("countb3", String.valueOf(cartcount));
                    Log.e("fgd", "sdfsd");
                    cartcountTv.setText("0");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
                Log.e("countb4", String.valueOf(cartcount));
                cartcountTv.setText(String.valueOf(cartcount));

            }
        });
    }


    private void init() {
        float density = 0;
        String[] image = new String[]{get_banners};
        for (int i = 0; i < image.length; i++) {
            Log.e("bannerurl", String.valueOf(urls));
            urls.add(image[i]);
        }


        mPager.setAdapter(new PageviewecomAdapter(getApplicationContext(), urls));

        CirclePageIndicator indicator = view.findViewById(R.id.indicator);

        indicator.setViewPager(mPager);
        try {
            density = getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            density = 0;
        }
        //Set circle indicator radius
        indicator.setRadius(4 * density);
        NUM_PAGES = urls.size();
        Log.e("pages", String.valueOf(NUM_PAGES));

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                Log.e("currentposition", "call" + position);
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {


            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }


    ////////////Priceundr box///////////////

    public void getEcomPriceUnderBox() {
        priceunderList.clear();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        Call<List<Listmodel>> getproductsbycat = null;
        apiDao = ApiClient.getClient("").create(ApiDao.class);

        getproductsbycat = apiDao.getEcompriceunderbox("Bearer " + AccountUtils.getAccessToken(getActivity()));
        getproductsbycat.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("topsellstatuscode", String.valueOf(statuscode));
                flist = response.body();
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("topsellstatuscode1", String.valueOf(statuscode));
                    if (flist != null) {
                        priceunderList.clear();

                        for (Listmodel listmodel : flist) {
                            cid = listmodel.getId();
                            priceunderList.add(listmodel);
                            underbudget_ecomm.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("catname", "No cats");
                    }
                } else if (statuscode == 422) {
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
    ///////New Arrivals////////////


    public void getTopsellingProducts() {
        topsellList.clear();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        Call<List<Listmodel>> getproductsbycat = null;
        apiDao = ApiClient.getClient("").create(ApiDao.class);

        getproductsbycat = apiDao.get_ecomTopSellinglist();
        getproductsbycat.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("topsellstatuscode", String.valueOf(statuscode));
                flist = response.body();

                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("topsellstatuscode1", String.valueOf(statuscode));
                    if (flist != null) {
                        for (Listmodel listmodel : flist) {
                            Log.e("pname", "pname");
                            if (listmodel.getPname().equals("Gift Card")) {
                                topsellList.remove(listmodel);
                            } else {
                                cid = listmodel.getId();
                                Log.e("cidtoplatest", "" + cid);
                                Log.e("cidtoplatestnmae", "" + listmodel.getCatname());
                                Log.e("cidtoplatestnmaedddddd", "" + listmodel.getPname());
                                topsellList.add(listmodel);
                                topsellecomm.notifyDataSetChanged();
                                Log.e("topcatname", "" + listmodel.getId());
                            }


                        }

                    } else {
                    }
                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("toppppughb", String.valueOf(t));
            }
        });
    }


/*
    public void getProductsByCats() {
        ecommgiftcardlist.clear();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<List<Listmodel>> getcatlist = apiDao.get_ecomnewarrivalslist();

        getcatlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("newstatuscode", String.valueOf(statuscode));
                flist = response.body();
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("newstatuscode", String.valueOf(statuscode));
                    if (flist != null) {
                        for (Listmodel listmodel : flist) {
                            if (listmodel.getPname().equals("Gift Card")) {
                                cid = listmodel.getId();
                                ecommgiftcardlist.add(listmodel);
                                giftCardAdapter.notifyDataSetChanged();
                            } else {

                                ecommgiftcardlist.remove(listmodel);

                            }

                        }

                    } else {
                        Log.e("catname", "No cats");
                    }
                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("giftttttt", String.valueOf(t));
            }
        });


    }
*/


    public void getGIFTCARD() {
        ecommgiftcardlist.clear();
        giftcardfilter.clear();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        Call<List<Listmodel>> getproductsbycat = null;
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        getproductsbycat = apiDao.getEcomTotalGiftCards();
        getproductsbycat.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("giftcardstatuscode", String.valueOf(statuscode));
                flist = response.body();
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("giftardstatuscode1", String.valueOf(statuscode));
                    if (flist != null) {
                        for (Listmodel listmodel : flist) {
                            Log.e("cidtoplatest", "" + cid);
                            Log.e("cidtoplatestnmae", "" + listmodel.getCatname());
                            Log.e("cidtoplatestnmaedddddd", "" + listmodel.getPname());
                            cid = listmodel.getId();
                            giftcardfilter.add(listmodel);
                            ecommgiftcardlist.add(listmodel);
                            giftCardAdapter.notifyDataSetChanged();


                        }

                    } else {
                    }
                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("gitscockettime", String.valueOf(t));
            }
        });
    }


    public void getEommNewArrivals() {
        productsList.clear();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<List<Listmodel>> getcatlist = apiDao.get_ecomnewarrivalslist();

        getcatlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("newstatuscode", String.valueOf(statuscode));
                flist = response.body();
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("newstatuscode", String.valueOf(statuscode));
                    if (flist != null) {
                        for (Listmodel listmodel : flist) {
                            if (listmodel.getPname().equals("Gift Card")) {
                                productsList.remove(listmodel);
                            } else {
                                cid = listmodel.getId();
                                productsList.add(listmodel);
                                ecomm_adapter.notifyDataSetChanged();
                            }

                        }

                    } else {
                        Log.e("catname", "No cats");
                    }
                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("newarrivalsughb", String.valueOf(t));
            }
        });

    }


    ///////////////
    public void getSubCats(String ciddd) {
        subfilterarrayList.clear();
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<List<Listmodel>> getcatlist = apiDao.get_ecomsubcategory(ciddd);

        getcatlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("statuscode", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("statuscode", String.valueOf(statuscode));
                    if (flist != null) {
                        for (Listmodel listmodel : flist) {
                            cid = listmodel.getId();

                            getProductsByCats(cid);
                            subfilterarrayList.add(listmodel);
                            subfilterarrayList1.add(listmodel);
                            ecomsubcatfilteradapter.notifyDataSetChanged();
                            Log.e("subfilterarrayList", "" + subfilterarrayList.size());

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

    public void getProductsByCats(String id) {
        productfilterarrayList.clear();
        Call<List<Listmodel>> getproductsbycat = null;
        apiDao = ApiClient.getClient("").create(ApiDao.class);

        getproductsbycat = apiDao.get_ecomsubcategoryproducts(id);
        getproductsbycat.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("subcatstatuscode", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("subcatlistloadstatuscode", String.valueOf(statuscode));
                    if (flist != null) {
                        productfilterarrayList.clear();
                        Collections.shuffle(flist);
                        for (Listmodel listmodel : flist) {
                            cid = listmodel.getId();
                            productfilterarrayList1.add(listmodel);
                            productfilterarrayList.add(listmodel);
                            ecomproductfilteradapter.notifyDataSetChanged();

                            //  spinneralert();
                        }
                    } else {
                        Log.e("catname", "No cats");
                    }
//                    openpopupscreen(listmodel.getDescription());
                } else if (statuscode == 422) {
                    Log.e("cv", String.valueOf(statuscode));
//                        ToastMessage.onToast(Elevenplus_Jewellery.this, String.valueOf(statuscode), ToastMessage.ERROR);
                } else {
//                    ToastMessage.onToast(Elevenplus_Jewellery.this, "Please try again", ToastMessage.ERROR);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
//                openpopupscreen("Successfully subscribed to Gold Plus Plan");
            }
        });
    }


    @Override
    public void onItemClick(View view, int position) {
        if (view.getId() == R.id.llhome) {

            // Listmodel listmodel = arrayList.get(position);
            Listmodel listmodel = subCatfilter.get(position);
            Intent intent = new Intent(activity, EcomSubcategory.class);
            intent.putExtra("cid", listmodel.getId());
            intent.putExtra("toolname", listmodel.getCatname());
            intent.putExtra("fromto", "cfff");


            startActivity(intent);
        } else if (view.getId() == R.id.newarrivalimg) {
            Listmodel listmodel2 = productsList.get(position);
//            Listmodel listmodel2 = NewArrivalsfilter.get(position);
            String idddd = listmodel2.getId();
            Intent intent = new Intent(activity, Ecommproductdiscreption.class);
            intent.putExtra("id", idddd);
            startActivity(intent);
        } else if (view.getId() == R.id.topsellimg) {
            Listmodel listmodel2 = topsellList.get(position);
            String idddd = listmodel2.getId();
            Intent intent = new Intent(activity, Ecommproductdiscreption.class);
            intent.putExtra("id", idddd);

            startActivity(intent);
        } else if (view.getId() == R.id.movetocart) {
            Listmodel listmodel2 = ecommgiftcardlist.get(position);
            String idddd = listmodel2.getId();
            Log.e("selectid", "" + idddd);
            postEcommaddcartproducts(idddd);

          /*  Intent intent = new Intent(getApplicationContext(), EcommCartActivity.class);
            startActivity(intent);*/

        } else if (view.getId() == R.id.selectwish) {
            getEcomWishlistCount();
        } else if (view.getId() == R.id.underprice) {
            Listmodel list = priceunderList.get(position);
            //Listmodel list = sellingProductsfilter.get(position);
            String name = list.getHeading();
            String idddd = list.getPrice();
            Intent intent = new Intent(activity, Ecommunderproductprice_Activity.class);
            intent.putExtra("getprice", name);
            intent.putExtra("id", idddd);
            Log.e("dserfafasdf", "" + name);
            startActivity(intent);
        } else if (view.getId() == R.id.ourbrandimg) {
            Listmodel list = ourbrandsList.get(position);
            String brandid = list.getId();
            String brandname = list.getBrand();
            String brandimage = list.getImage_uri();
            Intent intent = new Intent(activity, EcommBrandListActivity.class);
            intent.putExtra("brandid", brandid);
            intent.putExtra("titleBname", brandname);
            intent.putExtra("brandimage", brandimage);

            startActivity(intent);
        } else if (view.getId() == R.id.subfilterllhome) {
            // Listmodel listmodel = arrayList.get(position);
            Listmodel listmodel = subfilterarrayList1.get(position);
            Intent intent = new Intent(activity, EcomSubcategory.class);
            intent.putExtra("cid", listmodel.getCid());
            intent.putExtra("toolname", listmodel.getCatname());
            intent.putExtra("fromto", "cfff");

            startActivity(intent);
        } else if (view.getId() == R.id.productfilterllhome) {
            // Listmodel listmodel = arrayList.get(position);
            Listmodel listmodel = productfilterarrayList1.get(position);
            Intent intent = new Intent(activity, Ecommproductdiscreption.class);
            intent.putExtra("id", listmodel.getId());
            startActivity(intent);
        }


    }


    @Override
    public void onClick(View v) {

    }

    private void postEcommaddcartproducts(String cartid) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(getActivity())).create(ApiDao.class);

        Call<Listmodel> getecomcartlist = apiDao.postEcomcartitem("Bearer " + AccountUtils.getAccessToken(getActivity()), cartid, "0");

        getecomcartlist.enqueue(new Callback<Listmodel>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<Listmodel> call, @NotNull Response<Listmodel> response) {
                int statuscode = response.code();
                Log.e("ecompostfav", String.valueOf(statuscode));
                Log.e("ecompostfaverror", String.valueOf(response));
                pflist = response.body();
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                    Intent intent = new Intent(getApplicationContext(), EcommCartActivity.class);
                    startActivity(intent);

                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else if (statuscode == 404) {
                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("cartfalse", String.valueOf(t));
            }
        });
    }


}
