package com.goldsikka.goldsikka.Fragments.NewDesignsFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Activitys.Card.CardHome;
import com.goldsikka.goldsikka.Activitys.Events.Eventlist;
import com.goldsikka.goldsikka.Activitys.FeedbackForm;
import com.goldsikka.goldsikka.Activitys.GetContacts.ContactList;
import com.goldsikka.goldsikka.Activitys.GetContacts.ContactsModal;
import com.goldsikka.goldsikka.Activitys.GiftModuleActivity;
import com.goldsikka.goldsikka.Activitys.Kyc_Details;
import com.goldsikka.goldsikka.Activitys.MoneyWallet.AddMonet_to_Wallet;
import com.goldsikka.goldsikka.Activitys.Nominee_Details;
import com.goldsikka.goldsikka.Activitys.NotificationList;
import com.goldsikka.goldsikka.Activitys.Passbook_Activity;
import com.goldsikka.goldsikka.Activitys.Profile.CustomerAddressList;
import com.goldsikka.goldsikka.Activitys.ReferAndEarnActivity;
import com.goldsikka.goldsikka.Adapter.BannersAdapter;
import com.goldsikka.goldsikka.Adapter.PageviewAdapter;
import com.goldsikka.goldsikka.Adapter.Schemes_list_Adapter;
import com.goldsikka.goldsikka.ComingSoon;
import com.goldsikka.goldsikka.Directory.DirectoryHomeActivity;
import com.goldsikka.goldsikka.Fragments.Buy_Gold_Information;
import com.goldsikka.goldsikka.Fragments.Customer_BankDetailslist;
import com.goldsikka.goldsikka.Fragments.Digital_wallet_fragment;
import com.goldsikka.goldsikka.Fragments.Edit_coustomer_details;
import com.goldsikka.goldsikka.Fragments.Get_kyc_details_fragment;
import com.goldsikka.goldsikka.Fragments.JewelleryInventory.JewelleryHome;
import com.goldsikka.goldsikka.Fragments.Sell_Fragment;
import com.goldsikka.goldsikka.Fragments.TransferGold;
import com.goldsikka.goldsikka.Fragments.Reedem_fragment;
import com.goldsikka.goldsikka.Fragments.Schemes.Scheme_Content_Fragment;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.Models.BannersModel;
import com.goldsikka.goldsikka.NewDesignsActivity.GiftContactList;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.OrganizationWalletModule.OrganizationsList;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.WelcomeActivity;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnClickItemListenerForSchemes;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator3;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener, OnClickItemListenerForSchemes, NavigationView.OnNavigationItemSelectedListener {

    ImageView ivmenu;

    TextView notiicon;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.notification_frame)
    FrameLayout notification_frame;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvwalletgold)
    TextView tvwalletgold;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvlocation)
    TextView tvlocation;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvtime)
    TextView tvtime;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvliverate)
    TextView tvliverate;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvdate)
    TextView tvdate;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rvscheme)
    RecyclerView rv_schemeslist;


    //    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.tvtime)
//    TextView tvtime;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.tvlocation)
//    TextView tvlocation;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.tvtime)
//    TextView tvtime;
//
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btbuygold)
    Button btbuygold;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btaddmoney)
    Button btaddmoney;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvwalletmoney)
    TextView tvwalletmoney;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvNotificationCount)
    TextView tvNotificationCount;

    MainFragmentActivity mainFragmentActivity;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.lltransfer)
    LinearLayout lltransfer;
    //    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.llfragmentpassbook)
//    LinearLayout llpassbook;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llevent)
    LinearLayout llevent;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llredeem)
    LinearLayout llredeem;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llfragmentsellgold)
    LinearLayout llfragmentsellgold;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llrpoint)
    LinearLayout llrpoint;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.lldonte)
    LinearLayout lldonte;

    LinearLayout lljewelleryinv;


//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.tvtime)
//    TextView tvtime;

    ArrayList<Listmodel> arrayList;
    Schemes_list_Adapter adapter;

    ApiDao apiDao;
    String id, schemename, stprofileimg, stavathar, st_currencyinwords, st_ingrams, st_incurrency, liveprice;
    String st_name, st_email, st_mobile, get_banners, stcustomerid;
    shared_preference sharedPreference;

    private static final String TAG = "Goldsikka";
    private static final int CAMERA_REQUEST = 1888;
    File file;
    Uri uri;
    RequestBody requestBody;
    ViewPager2 viewPager;
    private Handler sliderHandler = new Handler();
    List<BannersModel> sliderItemList;
    SwipeRefreshLayout swipe_layout;

    String Walletamount;
    //    CircleImageView ivprofileimg;
    TextView tvnoticount;

    LinearLayout llatmcard;
//    public DrawerLayout drawerLayout;
//    public NavigationView navigationView;
//    public ActionBarDrawerToggle actionBarDrawerToggle;

    private Activity activity;
    private ActionBar actionBar;
    LinearLayout llschemee;

    NestedScrollView homens;

    private int currentPage = 0;
    private int NUM_PAGES;
    private ArrayList<String> urls = new ArrayList<>();
    View view;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.indicator)
    CircleIndicator3 indicator;
    RelativeLayout directory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach( context );
        this.activity = (Activity) context;
    }

    @SuppressLint("UseRequireInsteadOfGet")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate( R.layout.homefragment, container, false );
        ButterKnife.bind( this, view );
        sharedPreference = new shared_preference( activity );

        tvliverate = view.findViewById( R.id.tvliverate );
        tvnoticount = view.findViewById( R.id.tvNotificationCount );
        lljewelleryinv = view.findViewById( R.id.lljewelleryinv );
        llschemee = view.findViewById( R.id.llschemee );
        homens = view.findViewById( R.id.homens );
        lljewelleryinv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getContext(), JewelleryHome.class ) );
            }
        } );

        mainFragmentActivity = new MainFragmentActivity();
        ivmenu = view.findViewById( R.id.ivmenu );
        ivmenu.setOnClickListener( this );

        ivmenu.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragmentActivity) getActivity()).getDrawerLayout();
            }
        } );

        intilizerecyclerview( view );
        getbanners();
        CustomerDetails();
        get_walletdetails();
        getliveprices();
        getSchemelist();
        onrefresh();
        wallet_amount();
        notificationCount();

//        String restoredShowcaseValue = AccountUtils.getShowCaseInfo(getContext());
//        if (restoredShowcaseValue == null || restoredShowcaseValue.isEmpty()) {
//            focus();
//        }
        directory = view.findViewById( R.id.directory );
        directory.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( getContext(), DirectoryHomeActivity.class ) );
                // startActivity(new Intent(getContext(), ProfileActivity.class));

            }
        } );
        return view;

    }

    public void wallet_amount() {
        final ProgressDialog dialog = new ProgressDialog( activity );
        dialog.setMessage( "Please Wait...." );
        dialog.setCancelable( false );
        dialog.show();
        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient( AccountUtils.getAccessToken( activity ) ).create( ApiDao.class );
            Call<Listmodel> getdetails = apiDao.walletAmount( "Bearer " + AccountUtils.getAccessToken( activity ) );
            getdetails.enqueue( new Callback<Listmodel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<Listmodel> call, Response<Listmodel> response) {
                    Log.e( "Hometotalurl", "" + response.raw().request().url() );

                    int statuscode = response.code();
                    Log.e( "statuscode dd", String.valueOf( statuscode ) );
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        Listmodel list = response.body();
                        assert list != null;
                        try {
                            Walletamount = list.getAmount_wallet();
                            tvwalletmoney.setText( "₹" + Walletamount );
                            dialog.dismiss();
                        } catch (Exception e) {

                        }


                    } else {
                        dialog.dismiss();
                        // ToastMessage.onToast(activity, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e( "on fails", t.toString() );
                    dialog.dismiss();
                    // ToastMessage.onToast(activity, "We have some issue", ToastMessage.ERROR);
                }
            } );
        }
    }

    public void onrefresh() {
        swipe_layout.setProgressBackgroundColorSchemeColor( Color.WHITE );
        swipe_layout.setColorSchemeColors( Color.RED, Color.YELLOW, Color.GREEN );
        swipe_layout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected( activity )) {
                            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
                            swipe_layout.setRefreshing( false );
                            return;
                        } else {
                            getbanners();
                            CustomerDetails();
                            get_walletdetails();
                            getliveprices();
                            getSchemelist();
                            wallet_amount();
                            notificationCount();
                        }
                        swipe_layout.setRefreshing( false );
                    }
                }, 500 );
            }
        } );
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem( viewPager.getCurrentItem() + 1 );
        }
    };


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mainFragmentActivity.actionBarDrawerToggle.onOptionsItemSelected( item )) {
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    public void onPause() {
        super.onPause();

        sliderHandler.removeCallbacks( sliderRunnable );
    }


    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed( sliderRunnable, 3000 );
        wallet_amount();
    }

    public void intilizerecyclerview(View view) {
        swipe_layout = view.findViewById( R.id.swipe_layout );
        viewPager = view.findViewById( R.id.banners );
        sliderItemList = new ArrayList<>();
        // llgift.setOnClickListener(this);
        notification_frame.setOnClickListener( this );
        llfragmentsellgold.setOnClickListener( this );
        lltransfer.setOnClickListener( this );
        llredeem.setOnClickListener( this );
//        ivprofileimg.setOnClickListener(this);
        llevent.setOnClickListener( this );
        llrpoint.setOnClickListener( this );
        lldonte.setOnClickListener( this );


        rv_schemeslist.setHasFixedSize( true );
        rv_schemeslist.setLayoutManager( new LinearLayoutManager( activity ) );
        rv_schemeslist.setItemAnimator( new DefaultItemAnimator() );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( activity, LinearLayoutManager.VERTICAL, false );
        rv_schemeslist.setLayoutManager( layoutManager );

        arrayList = new ArrayList<>();
        adapter = new Schemes_list_Adapter( arrayList, activity, this );
        rv_schemeslist.setAdapter( adapter );

    }

    private void setscrollfun() {
        new Handler().postDelayed( () -> {
            homens.fullScroll( View.FOCUS_DOWN );
        }, 3000 );
    }

    public void getliveprices() {
        ProgressDialog dialog = new ProgressDialog( activity );
        dialog.setCancelable( false );
        dialog.setMessage( "Please wait..." );
        dialog.show();
        if (!NetworkUtils.isConnected( activity )) {
            dialog.dismiss();
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
        } else {
            apiDao = ApiClient.getClient( AccountUtils.getAccessToken( activity ) ).create( ApiDao.class );
            Call<Listmodel> getliverates = apiDao.getlive_rates( "Bearer " + AccountUtils.getAccessToken( activity ) );
            getliverates.enqueue( new Callback<Listmodel>() {
                @Override
                public void onResponse(@NonNull Call<Listmodel> call, @NonNull Response<Listmodel> response) {
                    int statuscode = response.code();
                    List<Listmodel> list = Collections.singletonList( response.body() );
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {
                        if (list != null) {
                            for (Listmodel listmodel : list) {
                                dialog.dismiss();
                                liveprice = listmodel.getSell_price_per_gram();
                                Log.e( "liveprice", liveprice );
                                //tv_sellprice.setText(getString(R.string.Rs) + liveprice);
                                tvliverate.setText( "₹" + liveprice );
                                tvdate.setText( listmodel.getDate() );
                                tvtime.setText( listmodel.getTime() );
                                tvlocation.setText( listmodel.getLocation() );
                                AccountUtils.setGsttax( activity, listmodel.getTaxPercentage() );
                            }
                        } else {
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject( response.errorBody().string() );
                            String st = jObjError.getString( "message" );
                            JSONObject er = jObjError.getJSONObject( "errors" );
//                            Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    //    ToastMessage.onToast(activity, "We have some issues", ToastMessage.ERROR);
                }
            } );
        }
    }

    public void getBannersData() {
        final ProgressDialog dialog = new ProgressDialog( activity );
        dialog.setMessage( "Please Wait...." );
        dialog.setCancelable( false );
        dialog.show();
        sliderItemList.clear();
        apiDao = ApiClient.getClient( AccountUtils.getAccessToken( activity ) ).create( ApiDao.class );
        Call<List<Listmodel>> getdetails = apiDao.getBannerImages( "Bearer " + AccountUtils.getAccessToken( activity ) );
        getdetails.enqueue( new Callback<List<Listmodel>>() {
            @Override
            public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                int statuscoe = response.code();
                Log.e( "Statuscode", String.valueOf( statuscoe ) );
                if (statuscoe == HttpsURLConnection.HTTP_OK || statuscoe == HttpsURLConnection.HTTP_CREATED) {
                    dialog.dismiss();
                    List<Listmodel> list = response.body();
                    if (list != null) {
                        for (Listmodel listmodel : list) {
                            get_banners = listmodel.getBanner_uri();
                            Log.e( "Banners", get_banners );
                            setupSlider( view );
                        }
                    } else {
                        // Toast.makeText(activity, "No Images", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    dialog.dismiss();
                    // Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                dialog.dismiss();
                //  Toast.makeText(activity, "onFailure", Toast.LENGTH_SHORT).show();
            }
        } );

    }

    public void getbanners() {
        getBannersData();
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer( new MarginPageTransformer( 10 ) );
        compositePageTransformer.addTransformer( new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs( position );
                page.setScaleY( 0.85f + r * 0.15f );

            }
        } );

        viewPager.setPageTransformer( compositePageTransformer );
        viewPager.registerOnPageChangeCallback( new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected( position );
                sliderHandler.removeCallbacks( sliderRunnable );
                sliderHandler.postDelayed( sliderRunnable, 3000 );
            }
        } );
    }


    BannersAdapter bannersAdapter;

    private void setupSlider(View view) {
        viewPager.setClipToPadding( false );
        viewPager.setClipChildren( false );
        viewPager.setOffscreenPageLimit( 3 );
        viewPager.getChildAt( 0 ).setOverScrollMode( RecyclerView.OVER_SCROLL_NEVER );

        sliderItemList.add( new BannersModel( get_banners ) );
        bannersAdapter = new BannersAdapter( sliderItemList, viewPager, activity );
        viewPager.setAdapter( bannersAdapter );


        bannersAdapter.registerAdapterDataObserver( indicator.getAdapterDataObserver() );
        viewPager.setCurrentItem( bannersAdapter.getItemCount() - 2, false );
        indicator.setViewPager( viewPager );


    }

    public void CustomerDetails() {
        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
        } else {
            apiDao = ApiClient.getClient( AccountUtils.getAccessToken( activity ) ).create( ApiDao.class );
            Call<Listmodel> getprofile = apiDao.getprofile_details( "Bearer " + AccountUtils.getAccessToken( activity ) );
            getprofile.enqueue( new Callback<Listmodel>() {

                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {

                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                        List<Listmodel> list = Collections.singletonList( response.body() );
                        if (list != null) {
                            for (Listmodel listmodel : list) {
                                st_name = listmodel.getName();
                                st_email = listmodel.getEmail();
                                st_mobile = listmodel.getMobile();
                                AccountUtils.setIsPin( activity, listmodel.getIsgspin() );
                                stcustomerid = listmodel.getCustomerId();
                                AccountUtils.setCustomerID( activity, stcustomerid );
//                                stavathar = listmodel.getAvatar();
//                                AccountUtils.setAvathar(activity,stavathar);
//                                if (stavathar != null){
//                                    stprofileimg = listmodel.getAvatarImageLink();
//                                    AccountUtils.setProfileImg(activity,stprofileimg);
//                                    Glide.with(activity)
//                                            .load(listmodel.getAvatarImageLink())
//                                            .into(ivprofileimg);
//                                    //Picasso.with(activity).load(listmodel.getAvatarImageLink()).into(ivprofileimg);
//                                }else {
//                                    AccountUtils.setAvathar(activity,null);
//
//                                    ivprofileimg.setImageResource(R.drawable.profile);
//                                }
                                AccountUtils.setName( activity, st_name );
                                AccountUtils.setMobile( activity, st_mobile );
                                AccountUtils.setEmail( activity, st_email );
                                if (listmodel.isTerminated()) {
                                    onlogout();
                                }

                            }
                        }
                    } else if (stauscode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        sharedPreference.WriteLoginStatus( false );
                        Intent intent = new Intent( activity, WelcomeActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity( intent );
                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject( response.errorBody().string() );
                            String st = jObjError.getString( "message" );
                            JSONObject er = jObjError.getJSONObject( "errors" );
//                            Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }


                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    // ToastMessage.onToast(activity, "We Have Some Issues", ToastMessage.ERROR);
                    Log.e( "Error", t.toString() );
                }

            } );

        }

    }

    public void notificationCount() {
        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
            return;
        } else {
            apiDao = ApiClient.getClient( AccountUtils.getAccessToken( activity ) ).create( ApiDao.class );
            Call<Listmodel> initlogout = apiDao.notificationCount( "Bearer " + AccountUtils.getAccessToken( activity ) );
            initlogout.enqueue( new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int stauscode = response.code();
                    Log.e( "stauscodecounttttt", "" + stauscode );

                    Log.e( "", "" + stauscode );
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                        Listmodel list = response.body();
                        Log.e( "counttttt", "" + list.getNotificationsCount() );

                        String count = list.getNotificationsCount();
//                        if(count.equals("0")){
//                            tvnoticount.setVisibility(View.GONE);
//                        }else{
                        tvnoticount.setVisibility( View.VISIBLE );
                        tvNotificationCount.setText( count );
//                        }


                    } else {
                        //  ToastMessage.onToast(activity, "Technical issue", ToastMessage.ERROR);
                        // Log.e("responce code", String.valueOf(response.code()));
                    }

                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e( "logout fail", t.toString() );
                    // ToastMessage.onToast(activity, "We have some issues", ToastMessage.ERROR);
                }
            } );
        }
    }

    public void onlogout() {
        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
            return;
        } else {
            apiDao = ApiClient.getClient( AccountUtils.getAccessToken( activity ) ).create( ApiDao.class );
            Call<List<Listmodel>> initlogout = apiDao.get_logout( "Bearer " + AccountUtils.getAccessToken( activity ) );
            initlogout.enqueue( new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                        sharedPreference.WriteLoginStatus( false );
                        Intent intent = new Intent( activity, WelcomeActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity( intent );
                    } else {
                        //  ToastMessage.onToast(activity, "Technical issue", ToastMessage.ERROR);
                    }

                }

                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                    Log.e( "logout fail", t.toString() );
                    //   ToastMessage.onToast(activity, "We have some issues", ToastMessage.ERROR);
                }
            } );
        }
    }

    private void get_walletdetails() {
        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
        } else {

            apiDao = ApiClient.getClient( AccountUtils.getAccessToken( activity ) ).create( ApiDao.class );

            Call<JsonElement> call = apiDao.get_digitalwallet( "Bearer " + AccountUtils.getAccessToken( activity ) );
            call.enqueue( new Callback<JsonElement>() {

                @Override
                public void onResponse(@NonNull Call<JsonElement> call, @NonNull retrofit2.Response<JsonElement> response) {
                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {

                        JsonElement jsonElement = response.body();
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        JsonObject gson = new JsonParser().parse( String.valueOf( jsonObject ) ).getAsJsonObject();

                        try {
                            JSONObject jo2 = new JSONObject( gson.toString() );
                            JSONObject balance = jo2.getJSONObject( "balance" );
                            st_currencyinwords = balance.getString( "currencyInWords" );
                            st_ingrams = balance.getString( "humanReadable" );
                            st_incurrency = balance.getString( "inCurrency" );

                            tvwalletgold.setText( st_ingrams + " grams" );
                            AccountUtils.setWalletAmount( activity, st_ingrams );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else if (stauscode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        sharedPreference.WriteLoginStatus( false );
                        Intent intent = new Intent( activity, WelcomeActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity( intent );
                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject( response.errorBody().string() );
                            String st = jObjError.getString( "message" );
//                            Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject( "errors" );

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {

                    //  Toast.makeText(activity, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            } );

        }
    }

    public void getSchemelist() {

        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
        } else {
            arrayList.clear();

            apiDao = ApiClient.getClient( AccountUtils.getAccessToken( activity ) ).create( ApiDao.class );
            Call<List<Listmodel>> getschemes = apiDao.getschemes( "Bearer " + AccountUtils.getAccessToken( activity ) );
            getschemes.enqueue( new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int statuscode = response.code();

                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        List<Listmodel> list = response.body();
                        //  dialog.dismiss();
                        for (Listmodel listmodel : list) {
                            id = listmodel.getId();
                            schemename = listmodel.getTitle();
                            arrayList.add( listmodel );
                            adapter.notifyDataSetChanged();

                            Log.e( "ids", id );
                        }
                    } else if (statuscode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        sharedPreference.WriteLoginStatus( false );
                        Intent intent = new Intent( activity, WelcomeActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity( intent );
                    } else {

                        // ToastMessage.onToast(activity, "Error", ToastMessage.ERROR);
                    }


                }

                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {

                    Log.e( "Schemme error", t.toString() );
                    //    ToastMessage.onToast(activity, "We have some issues", ToastMessage.ERROR);

                }
            } );
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lltransfer:
                openTransfer();
                //requestPermissions();
                break;

            case R.id.llredeem:
                openreedem();
                break;
            case R.id.llfragmentsellgold:
                //openpassbook();
                opensell();
                break;
            case R.id.llevent:
                Intent intent = new Intent( activity, Eventlist.class );
                startActivity( intent );
                break;
            case R.id.llrpoint:
                opencomingsoon();
                break;
            case R.id.lldonte:
                //opencomingsoon();
                openorgnation();
                break;

            case R.id.notification_frame:
                opennotification();
                break;
        }

    }

    public void opennotification() {
        Intent intent = new Intent( activity, NotificationList.class );
        startActivity( intent );
    }

    public void openorgnation() {
        Intent intent = new Intent( activity, OrganizationsList.class );
        startActivity( intent );
    }

    public void opencomingsoon() {
        Intent intent = new Intent( activity, ComingSoon.class );
        startActivity( intent );
    }

    @Override
    public void onItemClickListener(View view, int position) {
        Listmodel listmodel = arrayList.get( position );
        switch (view.getId()) {
            case R.id.cd_scheme:
                if (!NetworkUtils.isConnected( activity )) {
                    ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
                    return;
                } else {
                    openschems( listmodel.getScheme_calculation_type(), listmodel.getId(), listmodel.getTitle() );
                    Log.e( "scchemss", listmodel.getScheme_calculation_type() + "  idscm " + listmodel.getId() );
                }
                break;
        }
    }

    public void openschems(String schemetype_name, String schemeid, String schemetitle) {
        AccountUtils.setSchemename( activity, schemetype_name );
        AccountUtils.setSchemeID( activity, schemeid );
        AccountUtils.setSchemeTitle( activity, schemetitle );
        if (schemetype_name.equals( "MG" )) {
            Intent intent = new Intent( activity, Scheme_Content_Fragment.class );
            AccountUtils.setSchemeID( getContext(), "1" );
            AccountUtils.setSchemename( getContext(), "MY GOLD 2021 (Gold Chit)" );
            startActivity( intent );
        } else if (schemetype_name.equals( "JW" )) {
            Intent intent = new Intent( activity, Scheme_Content_Fragment.class );
            AccountUtils.setSchemeID( getContext(), "2" );
            AccountUtils.setSchemename( getContext(), "GOLD PLUS PLAN" );
            startActivity( intent );
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btbuygold)
    public void opendigitalwallet() {
        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
        } else {
            Intent intent = new Intent( activity, Digital_wallet_fragment.class );
            startActivity( intent );
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btaddmoney)
    public void openmoneywallet() {
        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
        } else {
            Intent intent = new Intent( activity, AddMonet_to_Wallet.class );
            startActivity( intent );
        }
    }

    public void get_nomineedetails() {
        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
        } else {
            Intent intent = new Intent( activity, Nominee_Details.class );
            startActivity( intent );
        }
    }

    public void get_kycdetails() {
        final ProgressDialog dialog = new ProgressDialog( activity );
        dialog.setMessage( "Please Wait...." );
        dialog.setCancelable( false );
        dialog.show();

        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
            dialog.dismiss();
        } else {
            apiDao = ApiClient.getClient( AccountUtils.getAccessToken( activity ) ).create( ApiDao.class );
            Call<Listmodel> iskyc = apiDao.checkkyc( "Bearer " + AccountUtils.getAccessToken( activity ) );
            iskyc.enqueue( new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Listmodel listmodel = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        if (listmodel.isKyc()) {
                            Intent intent = new Intent( activity, Get_kyc_details_fragment.class );
                            startActivity( intent );
                        } else {
                            Intent intent = new Intent( activity, Kyc_Details.class );
                            startActivity( intent );
                        }
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                }
            } );


        }
    }


    public void openTransfer() {
        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
        } else {
            Intent intent = new Intent( activity, ContactList.class );
//            intent.putExtra("phonenumber",stphonenumber);
//            intent.putExtra("contactnumber",stcontactname);
            startActivity( intent );
        }
    }

    public void openpassbook() {
        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
        } else {
//            PassBookFragment passBookFragment = new PassBookFragment();
//            getFragmentManager().beginTransaction().replace(R.id.mainframelayout,passBookFragment).commit();
            Intent intent = new Intent( activity, Passbook_Activity.class );
            // intent.putExtra("main_pass", "buy_passbook");
            startActivity( intent );
        }
    }

    public void openreedem() {
        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
        } else {

            Intent intent = new Intent( activity, Reedem_fragment.class );
            intent.putExtra( "wallet", st_ingrams );
            intent.putExtra( "amount", st_incurrency );
            startActivity( intent );
        }


    }

    public void opensell() {

        Intent intent = new Intent( activity, Sell_Fragment.class );
        startActivity( intent );
    }

    public void opengift() {
        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
        } else {
            Intent intent = new Intent( activity, GiftContactList.class );
            startActivity( intent );
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.goldsuvidhamenubtn:
                opendigitalwallet();
            case R.id.sellgoldmenubtn:
                opensell();
            case R.id.redeemmenubtn:
                openreedem();
            case R.id.transfermenubtn:
                openTransfer();
            case R.id.moneywalletmenubtn:
                openmoneywallet();
            case R.id.referandearnmenubtn:
                initreferandearn();
            case R.id.feedbackmenubtn:
                openfeedback();
            case R.id.loyalitypointsmenubtn:
                opencomingsoon();
//            case R.id.mygoldchitmenubtn:
//                Intent i = new Intent(getContext(), Scheme_Content_Fragment.class);
//                AccountUtils.setSchemeID(getContext(), "1");
//                AccountUtils.setSchemename(getContext(), "MY GOLD 2021 (Gold Chit)");
//                startActivity(i);
            case R.id.mygoldplusmenubtn:
                Intent i1 = new Intent( getContext(), Scheme_Content_Fragment.class );
                AccountUtils.setSchemeID( getContext(), "2" );
                AccountUtils.setSchemename( getContext(), "GOLD PLUS PLAN" );
                startActivity( i1 );

        }

        return true;
    }

    public void openfeedback() {
        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
        } else {
            Intent intent = new Intent( activity, FeedbackForm.class );
            startActivity( intent );
        }
    }

    public void initreferandearn() {
        if (!NetworkUtils.isConnected( activity )) {
            ToastMessage.onToast( activity, getString( R.string.error_no_internet_connection ), ToastMessage.ERROR );
        } else {
            Intent intent = new Intent( activity, ReferAndEarnActivity.class );
            startActivity( intent );
        }
    }


}
