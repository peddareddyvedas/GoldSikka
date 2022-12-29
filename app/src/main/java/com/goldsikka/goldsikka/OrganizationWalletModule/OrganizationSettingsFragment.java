package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.WelcomeActivity;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationSettingsFragment extends Fragment implements View.OnClickListener {

    TextView tvchangepass,tvlogout,tvterms,tvsupport,tvrefer;
    ImageView ivprofile;
    ApiDao apiDao;
    shared_preference sharedPreference;
    private Activity activity;
String oid, oname;
    TextView unameTv, uidTv, titleTv;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.organization_settings,container,false);

        intilizeview(view);
        sharedPreference = new shared_preference(activity);

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
                   // Toast.makeText(activity, "We have some issues", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void intilizeview(View view){
        tvlogout = view.findViewById(R.id.tvlogout);
        tvlogout.setOnClickListener(this);
        tvchangepass = view.findViewById(R.id.tvchangepass);
        tvchangepass.setOnClickListener(this);
        tvterms = view.findViewById(R.id.tvterms);
        tvterms.setOnClickListener(this);
        tvsupport = view.findViewById(R.id.tvsupport);
        tvsupport.setOnClickListener(this);

        unameTv = view.findViewById(R.id.uname);
        uidTv = view.findViewById(R.id.uid);
        titleTv = view.findViewById(R.id.title);

        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Settings");
//        if (AccountUtils.getAvathar(activity) != null){
//            Glide.with(activity)
//                    .load(AccountUtils.getProfileImg(activity))
//                    .into(ivprofile);
//
////            Picasso.with(activity).load(AccountUtils.getProfileImg(activity)).into(ivprofile);
//        }else {
//            ivprofile.setImageResource(R.drawable.profile);
//        }
//        ivprofile.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvlogout:
                onlogout();
                break;
            case R.id.tvchangepass:
                if (!NetworkUtils.isConnected(activity)) {
                    ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                }else {
                    Intent intent = new Intent(activity, OrganizationChangePassword.class);
                    intent.putExtra("oid", oid);
                    intent.putExtra("oname", oname);
                    startActivity(intent);
                }
                break;
            case R.id.tvterms:
                initterms();
                break;
            case R.id.tvsupport:
                initsupport();
                break;

        }
    }

    public void initsupport(){
        if (!NetworkUtils.isConnected(activity)){
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {
            Intent intent  = new Intent(activity, OrganizationContactUs.class);
            intent.putExtra("oid", oid);
            intent.putExtra("oname", oname);
            startActivity(intent);
        }
    }

    public void initterms(){
        if (!NetworkUtils.isConnected(activity)){
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            String uri = String.format(Locale.ENGLISH, "https://goldsikka.com/privacy-policy");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
    }


    public void onlogout(){
        if (!NetworkUtils.isConnected(activity)) {
            ToastMessage.onToast(activity, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
            Call<List<Listmodel>> initlogout = apiDao.get_logout("Bearer " + AccountUtils.getAccessToken(activity));
            initlogout.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {
                        sharedPreference.WriteLoginStatus(false);
                        Intent intent = new Intent(activity, WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                    //    ToastMessage.onToast(activity, "Technical issue", ToastMessage.ERROR);
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


}
