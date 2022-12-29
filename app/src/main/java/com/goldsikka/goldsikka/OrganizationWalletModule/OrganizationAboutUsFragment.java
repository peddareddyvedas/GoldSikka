package com.goldsikka.goldsikka.OrganizationWalletModule;

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
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationAboutUsFragment extends Fragment implements View.OnClickListener {

    private Activity activity;
    LinearLayout ll_about,ll_business,ll_company;
    ApiDao apiDao;
    TextView tvcontent,readmore;
    LinearLayout llinfo;
    String stContent;
    ImageView ivprofile;

    TextView unameTv, uidTv;
String oid, oname;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.organization_aboutus,container,false);
        initview(view);
        getdata();
        org_details();
        return view;
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
                                oid = listmodel.getOrganization_id();
                                oname = listmodel.getName();

                                uidTv.setText(oid);
                                unameTv.setText(oname);

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
                 //   Toast.makeText(activity, "We have some issues", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void initview(View view){

        unameTv = view.findViewById(R.id.uname);
        uidTv = view.findViewById(R.id.uid);

//        unameTv.setText(AccountUtils.getName(getContext()));
//        uidTv.setText(AccountUtils.getCustomerID(getContext()));

        ll_about = view.findViewById(R.id.ll_about);
        ll_about.setOnClickListener(this);
        ll_business = view.findViewById(R.id.ll_business);
        ll_business.setOnClickListener(this);
        ll_company = view.findViewById(R.id.ll_company);
        ll_company.setOnClickListener(this);

        llinfo = view.findViewById(R.id.llinfo);
        readmore=view.findViewById(R.id.readmore);
        readmore.setOnClickListener(this);
        tvcontent=view.findViewById(R.id.tvcontent);
//        ivprofile = view.findViewById(R.id.ivprofile);
//        if (AccountUtils.getAvathar(activity)!=null){
//            // Picasso.with(activity).load(AccountUtils.getProfileImg(activity)).into(ivprofile);
//            Glide.with(activity)
//                    .load(AccountUtils.getProfileImg(activity))
//                    .into(ivprofile);
//        }else {
//            ivprofile.setImageResource(R.drawable.profile);
//        }
//        ivprofile.setOnClickListener(this);

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
            case R.id.readmore:
                Intent intent = new Intent(activity, OrganizationReadmore.class);
                intent.putExtra("content",stContent);
                startActivity(intent);
                break;
        }
    }

    public void openAbout(){

        Intent intent = new Intent(activity, OrganizationVisionMissionActivity.class);
        intent.putExtra("oid", oid);
        intent.putExtra("oname", oname);
        startActivity(intent);
    }

    public void openBusiness(){

        Intent intent = new Intent(activity, OrganizationBusinessAssociates.class);
        intent.putExtra("oid", oid);
        intent.putExtra("oname", oname);
        startActivity(intent);
    }

    public void openCompany(){

        Intent intent = new Intent(activity, OrganizationCompanyDetails.class);
        intent.putExtra("oid", oid);
        intent.putExtra("oname", oname);
        startActivity(intent);
    }

}
