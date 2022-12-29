package com.goldsikka.goldsikka.Fragments.Schemes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.goldsikka.goldsikka.Adapter.Scheme_Content_Adapter;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.data;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Scheme_Content_Fragment extends AppCompatActivity  implements View.OnClickListener {

    TextView tv_content,tv_content_title,tv_features,tv_faqs,tv_terms_condition;
    CheckBox check_terms_condition;
    Button bt_continue;
    RecyclerView rv_cotent;
    ArrayList<Listmodel> arrayList;
    Scheme_Content_Adapter adapter;
    ApiDao apiDao;
    String schemeid;
    String schemename;
    RelativeLayout llscheme_content;


    RelativeLayout backbtn;
TextView unameTv, uidTv, titleTv;
    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygold2020_content);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));

        intilizeviews();
        schemeid = AccountUtils.getSchemeID(this);
       schemename = AccountUtils.getSchemename(this);

//       ToastMessage.onToast(this, schemename+schemeid, ToastMessage.SUCCESS);
        if (schemename.equals("JW")){
            tv_faqs.setVisibility(View.GONE);
            tv_features.setVisibility(View.GONE);
        }

        intilizerecyclerview();

        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            init_content();
            init_subcontent();
        }

    }

    public void intilizeviews(){

        tv_content = findViewById(R.id.tv_content);
        tv_content_title = findViewById(R.id.tv_content_title);
        tv_features = findViewById(R.id.tv_features);
        tv_features.setOnClickListener(this);
        tv_faqs = findViewById(R.id.tv_faqs);
        tv_faqs.setOnClickListener(this);
        tv_terms_condition = findViewById(R.id.tv_terms_condition);
        tv_terms_condition.setOnClickListener(this);
        //check_terms_condition = findViewById(R.id.check_terms_condition);
        bt_continue = findViewById(R.id.bt_continue);
        llscheme_content = findViewById(R.id.llscheme_content);
        llscheme_content.setVisibility(View.GONE);
        bt_continue.setOnClickListener(this);
    }

    public void intilizerecyclerview(){
        rv_cotent = findViewById(R.id.rv_content);

        rv_cotent.setHasFixedSize(true);
        rv_cotent.setLayoutManager(new LinearLayoutManager(this));
        rv_cotent.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        rv_cotent.addItemDecoration(decoration);
        arrayList = new ArrayList<>();
        adapter = new Scheme_Content_Adapter(this,arrayList);
        rv_cotent.setAdapter(adapter);

    }
    public void init_content(){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getcontent = apiDao.getcontent("Bearer " + AccountUtils.getAccessToken(this),
                    schemeid);
            getcontent.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Listmodel listmodel = response.body();
                    List<Listmodel> list = Collections.singletonList(response.body());
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        llscheme_content.setVisibility(View.VISIBLE);
                        String titile = listmodel.getTitle();
                        tv_content_title.setText(titile);
                        tv_content.setText(listmodel.getScheme_content());
                        dialog.dismiss();
                    } else {
                        llscheme_content.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        //   ToastMessage.onToast(Scheme_Content_Fragment.this, "we have some issues", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    //   ToastMessage.onToast(Scheme_Content_Fragment.this, "we have some issues", ToastMessage.ERROR);
                }
            });
        }
    }
    public void init_subcontent(){
          arrayList.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<data> getsubcontent = apiDao.getsubcontent("Bearer " + AccountUtils.getAccessToken(this), schemeid);
            getsubcontent.enqueue(new Callback<data>() {
                @Override
                public void onResponse(Call<data> call, Response<data> response) {
                    int statuscode = response.code();
                    List<Listmodel> list = response.body().getScheme_sub_content();
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        llscheme_content.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        if (list != null) {
                            for (Listmodel listmodel : list) {
                                arrayList.add(listmodel);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            dialog.dismiss();
                            Toast.makeText(Scheme_Content_Fragment.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<data> call, Throwable t) {
                    dialog.dismiss();
                    //  ToastMessage.onToast(Scheme_Content_Fragment.this, "we have some issues", ToastMessage.ERROR);

                }
            });
        }

    }


    public void init_mygold2020_form(){
        openbuygold();
//        if (!check_terms_condition.isChecked()){
//            ToastMessage.onToast(Scheme_Content_Fragment.this,"Please Accept Terms & Condition",ToastMessage.ERROR);
//        }else {
//            openbuygold();
//        }
    }

    public void openbuygold(){

        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            Intent intent = new Intent(this, Schemes_usersubscribed_list.class);
            Schemes_usersubscribed_list.isFromgoldtransaction = false;
            startActivity(intent);
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_continue:
                init_mygold2020_form();
                break;
            case R.id.tv_features:
                openfeatures();
                break;
            case R.id.tv_faqs:
                openfaqs();
                break;
            case R.id.tv_terms_condition:
                openterms_condition();
                break;

        }
    }
    public void openterms_condition(){

        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            Intent intent = new Intent(this, Scheme_TermsandCondition.class);
            intent.putExtra("SchemeId", schemeid);
            startActivity(intent);
        }

    }
    public void openfaqs(){

        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            Intent intent = new Intent(this, Scheme_Faqs_Fragment.class);
            intent.putExtra("SchemeId", schemeid);
            startActivity(intent);
        }
        }
    public void openfeatures(){

        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            Intent intent = new Intent(this, Scheme_features.class);
            intent.putExtra("SchemeId", schemeid);
            startActivity(intent);
        }
    }



}
