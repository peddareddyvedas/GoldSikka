package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Adapter.BannersAdapter;
import com.goldsikka.goldsikka.Models.BannersModel;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.WelcomeActivity;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

public class OrganizationHomeFragment extends Fragment implements View.OnClickListener {

//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.ivprofileimg)
//    ImageView ivprofileimg;
//
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnredeemgold)
    Button btnredeemgold;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvwalletgold)
    TextView tvwalletgold;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.indicator)
    CircleIndicator3 indicator;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.tvtime)
//    TextView tvtime;

    TextView org_status,org_id,org_name,org_reg_no,org_adddress,org_description,org_note;
    String st_orgstatus,st_orgid,st_orgname,st_org_reg,st_orgaddress,st_orgdescription,st_orgnote,st_orgphoto;
    String st_state,stimage;
   // CircleImageView org_photo;
    ArrayList<Listmodel> arrayList;

    ApiDao apiDao;
    ImageView ivimage;
    String id,stprofileimg,stavathar;
    String st_name,st_email,st_mobile,get_banners,st_currencyinwords,st_ingrams,st_incurrency,liveprice;
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

    TextView uidTv,unameTv;

    private Activity activity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.organizationwallet_homepage,container,false);
        ButterKnife.bind(this,view);
        sharedPreference = new shared_preference(activity);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.Gold));
        }
        initiviews(view);
        getbanners();
        CustomerDetails();
        onrefresh();

        org_details();
        get_walletdetails();

        uidTv = view.findViewById(R.id.uid);
        unameTv = view.findViewById(R.id.uname);

        return view;

    }

    public String getSt_orgid(){
        return uidTv.getText().toString();
    }

    public String getSt_orgname(){
        return st_orgname;
    }

    public void initiviews(View view){

        org_status = view.findViewById(R.id.organizationStatus);
        org_id = view.findViewById(R.id.organization_id);
        org_name = view.findViewById(R.id.name);
       // org_photo = view.findViewById(R.id.photoImageLink);
        org_reg_no = view.findViewById(R.id.registration_number);
        org_adddress = view.findViewById(R.id.address);
        org_description = view.findViewById(R.id.description);
        ivimage = view.findViewById(R.id.photoImageLink);
        viewPager = view.findViewById(R.id.banners);
        sliderItemList = new ArrayList<>();
        swipe_layout = view.findViewById(R.id.swipe_layout);
        org_note = view.findViewById(R.id.note);
//        ivprofileimg.setOnClickListener(this);
    }

    public void org_details(){
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(activity)) {
            dialog.dismiss();
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<UserOrganizationDetailsModel> org_details = apiDao.get_orgdetails("Bearer " + AccountUtils.getAccessToken(activity));
            org_details.enqueue(new Callback<UserOrganizationDetailsModel>() {
                @Override
                public void onResponse(Call<UserOrganizationDetailsModel> call, Response<UserOrganizationDetailsModel> response) {
                    int statuscode = response.code();
                    List<UserOrganizationDetailsModel> list = Collections.singletonList(response.body());
                    if (statuscode == HttpsURLConnection.HTTP_ACCEPTED || statuscode == HttpsURLConnection.HTTP_OK) {
                        if (list != null) {
                            for (UserOrganizationDetailsModel listmodel : list) {
                                dialog.dismiss();
                                st_orgid = listmodel.getOrganization_id();
                                st_orgstatus = listmodel.getOrganizationStatus();
                                st_org_reg = listmodel.getRegistration_number();
                                st_orgname = listmodel.getName();
                                st_state = listmodel.getState();
                                st_orgaddress = (listmodel.getAddress() + ", " + listmodel.getCity() + ", " + st_state + ", " + listmodel.getZip());
                                st_orgdescription = listmodel.getDescription();
                                st_orgnote = listmodel.getNote();
                                stimage = listmodel.getPhotoImageLink();

                                uidTv.setText(st_orgid);
                                unameTv.setText(st_orgname);
                                org_status.setText(st_orgstatus);
                                org_id.setText(st_orgid);
                                org_reg_no.setText(st_org_reg);
                                org_name.setText(st_orgname);
                                org_adddress.setText(st_orgaddress);
                                org_description.setText(st_orgdescription);
                                org_note.setText(st_orgnote);

                                Glide.with(activity).load(stimage).into(ivimage);

                                AccountUtils.setOrgAvathar(activity,listmodel.getPhoto());
                                AccountUtils.setOrgProfileImg(activity,listmodel.getPhotoImageLink());

                            }
                        } else {
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
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
                public void onFailure(Call<UserOrganizationDetailsModel> call, Throwable t) {
                    dialog.dismiss();
                //    Toast.makeText(activity, "We have some issues", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void get_walletdetails() {
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

            Call<JsonElement> call = apiDao.get_digitalwallet("Bearer " + AccountUtils.getAccessToken(activity));
            call.enqueue(new Callback<JsonElement>() {

                @Override
                public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {

                        JsonElement jsonElement = response.body();
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        JsonObject gson = new JsonParser().parse(String.valueOf(jsonObject)).getAsJsonObject();

                        try {
                            JSONObject jo2 = new JSONObject(gson.toString());
                            JSONObject balance = jo2.getJSONObject("balance");
                            st_currencyinwords = balance.getString("currencyInWords");
                            st_ingrams = balance.getString("humanReadable");
                            st_incurrency = balance.getString("inCurrency");

                            tvwalletgold.setText(st_ingrams+" grams");
                            AccountUtils.setWalletAmount(activity,st_ingrams);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    else if (stauscode == HttpsURLConnection.HTTP_UNAUTHORIZED){
                        sharedPreference.WriteLoginStatus(false);
                        Intent intent = new Intent(activity, WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
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

                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {

                    //  Toast.makeText(activity, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void onrefresh(){
        swipe_layout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipe_layout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(activity)) {
                            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            swipe_layout.setRefreshing(false);
                            return;
                        }
                        else {
                            get_walletdetails();
                            org_details();
                            getbanners();
                            CustomerDetails();
                        }
                        swipe_layout.setRefreshing(false);

                    }
                }, 3000);


            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();

        sliderHandler.removeCallbacks(sliderRunnable);
    }
    public void getbanners(){
        getBannersData();
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);

            }
        });

        viewPager.setPageTransformer(compositePageTransformer);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    public void getBannersData(){

        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        sliderItemList.clear();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
        Call<List<Listmodel>> getdetails = apiDao.getBannerImages("Bearer "+AccountUtils.getAccessToken(activity));
        getdetails.enqueue(new Callback<List<Listmodel>>() {
            @Override
            public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                int statuscoe  = response.code();

                Log.e("Status code", String.valueOf(statuscoe));
                if (statuscoe == HttpsURLConnection.HTTP_OK||statuscoe == HttpsURLConnection.HTTP_CREATED){
                    dialog.dismiss();

                    List<Listmodel> list = response.body();
                    if (list != null) {
                        for (Listmodel listmodel : list) {

                            get_banners = listmodel.getBanner_uri();

                            Log.e("Banners",get_banners);

                            setupSlider();
                        }
                    } else {
                        Toast.makeText(activity, "No Images", Toast.LENGTH_SHORT).show();
                    }


                }else {
                    dialog.dismiss();
                    Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(activity, "onFailure", Toast.LENGTH_SHORT).show();

            }
        });

    }
    BannersAdapter bannersAdapter;
    private void setupSlider() {

        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        sliderItemList.add(new BannersModel(get_banners));
        bannersAdapter = new BannersAdapter(sliderItemList, viewPager,activity);
        viewPager.setAdapter(bannersAdapter);


        bannersAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());
        viewPager.setCurrentItem(bannersAdapter.getItemCount()-2,false);
        indicator.setViewPager(viewPager);


    }

    public void CustomerDetails(){


        if (!NetworkUtils.isConnected(activity)){
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {
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
                                st_name = listmodel.getName();
                                st_email = listmodel.getEmail();
                                st_mobile = listmodel.getMobile();
                                stavathar = listmodel.getAvatar();
                                AccountUtils.setAvathar(activity,stavathar);
                                if (stavathar != null){
                                    stprofileimg = listmodel.getAvatarImageLink();
                                    AccountUtils.setProfileImg(activity,stprofileimg);
//                                    Glide.with(activity)
//                                            .load(listmodel.getAvatarImageLink())
//                                            .into(ivprofileimg);
                                    //Picasso.with(activity).load(listmodel.getAvatarImageLink()).into(ivprofileimg);
                                }else {
//                                    ivprofileimg.setImageResource(R.drawable.profile);
                                }

                                AccountUtils.setName(activity,st_name);
                                AccountUtils.setMobile(activity,st_mobile);
                                AccountUtils.setEmail(activity,st_email);


                                if (listmodel.isTerminated()){
                                    onlogout();
                                }

                            }
                        }

                    }
                    else if (stauscode == HttpsURLConnection.HTTP_UNAUTHORIZED){
                        sharedPreference.WriteLoginStatus(false);
//                        Intent intent = new Intent(activity,WelcomeActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
                    }
                    else {
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
                 //   ToastMessage.onToast(activity, "We Have Some Issues", ToastMessage.ERROR);
                    Log.e("Error", t.toString());
                }

            });

        }

    }
    public void onlogout() {
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<List<Listmodel>> initlogout = apiDao.get_logout("Bearer " + AccountUtils.getAccessToken(activity));
            initlogout.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                        sharedPreference.WriteLoginStatus(false);
//                        Intent intent = new Intent(activity, WelcomeActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
                    } else {
                        //ToastMessage.onToast(activity, "Technical issue", ToastMessage.ERROR);
                        // Log.e("responce code", String.valueOf(response.code()));
                    }

                }

                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                    Log.e("logout fail", t.toString());
                  //  ToastMessage.onToast(activity, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.ivprofileimg:
//                if (!NetworkUtils.isConnected(activity)){
//                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//                }else {
//                    BottomNavigation(v);
//                }
//                break;

        }

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick( R.id.btnredeemgold)
    public void openreedem(){
        if (!NetworkUtils.isConnected(activity)){
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {
            Intent intent = new Intent(activity, OrganizationRedeemActivity.class);
            intent.putExtra("wallet", st_ingrams);
            intent.putExtra("amount", st_incurrency);
            intent.putExtra("oid", st_orgid);
            intent.putExtra("oname", st_orgname);
            startActivity(intent);
        }
    }

    TextView tveditprofile,tvnominee,tvkyc,tvbank,tvaddress,tvemail,tvname;
    ImageView ivprofile;
    public void BottomNavigation(View view){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity,R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setCancelable(true);
        View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.bottomprofilesheet,view.findViewById(R.id.bottomsheet));
        bottomSheetDialog.setContentView(bottomSheet);
        ((View) bottomSheet.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tveditprofile = bottomSheet.findViewById(R.id.tvprofileedit);
        tvkyc = bottomSheet.findViewById(R.id.tvkyc);
        tvbank = bottomSheet.findViewById(R.id.tvbankdetails);
        tvemail = bottomSheet.findViewById(R.id.tvemail);
        tvname = bottomSheet.findViewById(R.id.tvname);
        tvaddress = bottomSheet.findViewById(R.id.tvaddress);
        ivprofile = bottomSheet.findViewById(R.id.ivprofile);
        if (stavathar != null){
            Glide.with(activity)
                    .load(stprofileimg)
                    .into(ivprofile);
            // Picasso.with(activity).load(stprofileimg).into(ivprofile);
        }else {
            ivprofile.setImageResource(R.drawable.user);
        }
        tvname.setText(st_name);
        tvemail.setText(st_email);
        tvkyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_kycdetails();
            }
        });
        tveditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(activity)){
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                }else {
                    Intent intent = new Intent(activity,ORZProfileEdit.class);
                    intent.putExtra("name",st_name);
                    intent.putExtra("email",st_email);
                    intent.putExtra("mobile",st_mobile);
                    intent.putExtra("profileimage",stprofileimg);
                    intent.putExtra("stavathar",stavathar);
                    startActivity(intent);
                }
            }

        });

        tvbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(activity)){
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                }else {
                    Intent intent = new Intent(activity, OrganizationBankDetails.class);
                    startActivity(intent);
                }
            }
        });

        tvaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(activity)){
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                }else {
                    Intent intent = new Intent(activity, OrganizationAddressList.class);
                    startActivity(intent);
                }
            }
        });


        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }


    public void get_kycdetails(){
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(activity)){
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<Listmodel> iskyc = apiDao.checkkyc("Bearer "+AccountUtils.getAccessToken(activity));
            iskyc.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Listmodel listmodel = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK){
                        dialog.dismiss();
                        if (listmodel.isKyc()){
                            Intent intent = new Intent(activity, OrganizationKYCdetails.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(activity, OrganizationAddKycDetails.class);
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
