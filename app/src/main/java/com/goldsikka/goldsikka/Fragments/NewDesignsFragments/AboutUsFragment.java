package com.goldsikka.goldsikka.Fragments.NewDesignsFragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Activitys.HappyCustomers;
import com.goldsikka.goldsikka.Activitys.Kyc_Details;
import com.goldsikka.goldsikka.Activitys.Nominee_Details;
import com.goldsikka.goldsikka.Activitys.Profile.CustomerAddressList;
import com.goldsikka.goldsikka.Activitys.ReadMoreActivity;
import com.goldsikka.goldsikka.Fragments.Aboutusfragment;
import com.goldsikka.goldsikka.Fragments.Business_Associates;
import com.goldsikka.goldsikka.Fragments.Company_Details;
import com.goldsikka.goldsikka.Fragments.Customer_BankDetailslist;
import com.goldsikka.goldsikka.Fragments.Edit_coustomer_details;
import com.goldsikka.goldsikka.Fragments.FAQsFragment;
import com.goldsikka.goldsikka.Fragments.Get_kyc_details_fragment;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.LOGIN.WelcomeActivity;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUsFragment extends Fragment implements View.OnClickListener {

    private Activity activity;
    LinearLayout ll_about,ll_business,ll_company,ll_faqs,ll_happyCustomers;
    ApiDao apiDao;
    TextView tvcontent,readmore;
    LinearLayout llinfo;
    String stContent;
//    ImageView ivprofile;
    String stavathar,stprofileimg;
    shared_preference sharedPreference;

    TextView uidTv, unameTv;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
        sharedPreference = new shared_preference(activity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aboutusfragment,container,false);
        initview(view);
        getdata();
        CustomerDetails();

        uidTv = view.findViewById(R.id.uid);
        unameTv = view.findViewById(R.id.uname);

        uidTv.setText(AccountUtils.getCustomerID(activity));
        unameTv.setText(AccountUtils.getName(activity));
        return view;
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

                                AccountUtils.setIsPin(activity,listmodel.getIsgspin());

                                stavathar = listmodel.getAvatar();
                                AccountUtils.setAvathar(activity,stavathar);
                                if (stavathar != null){
                                    stprofileimg = listmodel.getAvatarImageLink();
                                    AccountUtils.setProfileImg(activity,stprofileimg);
//                                    Glide.with(activity)
//                                            .load(listmodel.getAvatarImageLink())
//                                            .into(ivprofile); (((((((((((CHANGED)))))))))))))))))))
                                    //Picasso.with(activity).load(listmodel.getAvatarImageLink()).into(ivprofileimg);
                                }else {
                                    AccountUtils.setAvathar(activity,null);

//                                    ivprofile.setImageResource(R.drawable.profile); (((((((((((CHANGED)))))))))))))))))))
                                }




                            }
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

    public void initview(View view){

        ll_about = view.findViewById(R.id.ll_about);
        ll_about.setOnClickListener(this);
        ll_business = view.findViewById(R.id.ll_business);
        ll_business.setOnClickListener(this);
        ll_company = view.findViewById(R.id.ll_company);
        ll_company.setOnClickListener(this);
        ll_faqs = view.findViewById(R.id.ll_faqs);
        ll_faqs.setOnClickListener(this);
        ll_happyCustomers = view.findViewById(R.id.ll_happy_customers);
        ll_happyCustomers.setOnClickListener(this);

        llinfo = view.findViewById(R.id.llinfo);
        readmore=view.findViewById(R.id.readmore);
        readmore.setOnClickListener(this);
        tvcontent=view.findViewById(R.id.tvcontent);
//        ivprofile = view.findViewById(R.id.ivprofile); (((((((((((CHANGED)))))))))))))))))))
//        if (AccountUtils.getAvathar(activity)!=null){
//           // Picasso.with(activity).load(AccountUtils.getProfileImg(activity)).into(ivprofile);
//            Glide.with(activity)
//                    .load(AccountUtils.getProfileImg(activity))
//                    .into(ivprofile);
//        }else {
//            ivprofile.setImageResource(R.drawable.profile);
//        }
//        ivprofile.setOnClickListener(this); (((((((((((CHANGED)))))))))))))))))))

    }

    public void getdata() {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait.....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(activity)){
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
        Call<Listmodel> aboutus = apiDao.getabouts("Bearer "+AccountUtils.getAccessToken(activity));
        aboutus.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode  = response.code();
                Listmodel listmodel = response.body();
                if (statuscode == HttpsURLConnection.HTTP_OK){
                    dialog.dismiss();
                    llinfo.setVisibility(View.VISIBLE);
                    readmore.setVisibility(View.VISIBLE);
                    listmodel.getParagraph();
                    tvcontent.setText(listmodel.getParagraph());
                    stContent = listmodel.getParagraph();
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                Log.e("about us on failure",t.toString());
                dialog.dismiss();
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_about:
                openAbout();
                break;
            case R.id.ll_business:
                openBusiness();
                break;
            case R.id.ll_company:
                openCompany();
                break;
            case R.id.ll_faqs:
                openFaqs();
                break;

            case R.id.ll_happy_customers:
                openhappyCustomers();
                break;
            case R.id.readmore:
                Intent intent = new Intent(activity, ReadMoreActivity.class);
                intent.putExtra("content",stContent);
                startActivity(intent);
                break;
            case R.id.ivprofile:
                BottomNavigation(v);
                break;
        }
    }

    public void openhappyCustomers(){
        Intent intent = new Intent(activity, HappyCustomers.class);
        startActivity(intent);
    }

    public void openAbout(){

        Intent intent = new Intent(activity, Aboutusfragment.class);
        startActivity(intent);
    }

    public void openBusiness(){

        Intent intent = new Intent(activity, Business_Associates.class);
        startActivity(intent);
    }

    public void openCompany(){

        Intent intent = new Intent(activity, Company_Details.class);
        startActivity(intent);
    }

    public void openFaqs(){

        Intent intent = new Intent(activity, FAQsFragment.class);
        startActivity(intent);
    }


    TextView tveditprofile,tvnominee,tvkyc,tvbank,tvaddress,tvemail,tvname,tvcustomerid;
    ImageView ivprofileb;
    public void BottomNavigation(View view){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity,R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setCancelable(true);
        View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.profilebottomview,view.findViewById(R.id.bottomsheet));
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
        tvcustomerid.setText("CustomerId : "+AccountUtils.getCustomerID(activity));
        ivprofileb = bottomSheet.findViewById(R.id.ivprofile);
        if (AccountUtils.getAvathar(activity) != null){
        //    Picasso.with(activity).load(AccountUtils.getProfileImg(activity)).into(ivprofileb);
            Glide.with(activity)
                    .load(AccountUtils.getProfileImg(activity))
                    .into(ivprofileb);
        }else {
                ivprofileb.setImageResource(R.drawable.profile);
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
                if (!NetworkUtils.isConnected(activity)){
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                }else {
                    Intent intent = new Intent(activity, Edit_coustomer_details.class);

                    intent.putExtra("name",AccountUtils.getName(activity));
                    intent.putExtra("email",AccountUtils.getEmail(activity));
                    intent.putExtra("mobile",AccountUtils.getMobile(activity));
                    intent.putExtra("profileimage",AccountUtils.getProfileImg(activity));
                    startActivity(intent);}
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
                if (!NetworkUtils.isConnected(activity)){
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                }else {
                    Intent intent = new Intent(activity, CustomerAddressList.class);
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
                    Intent intent = new Intent(activity, Customer_BankDetailslist.class);
                    startActivity(intent);
                }
            }
        });


        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }
    public void get_nomineedetails(){
        if (!NetworkUtils.isConnected(activity)){
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {
            Intent intent = new Intent(activity, Nominee_Details.class);
            startActivity(intent);
        }
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
                            Intent intent = new Intent(activity, Get_kyc_details_fragment.class);
                            startActivity(intent);
                        }else {
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
