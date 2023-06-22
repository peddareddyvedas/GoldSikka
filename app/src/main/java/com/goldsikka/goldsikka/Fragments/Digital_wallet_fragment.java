package com.goldsikka.goldsikka.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Adapter.DigitalWallet_Content.Digitalwallet_SubContent;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.List_Model;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.data;
import com.goldsikka.goldsikka.model.dw_contentlist;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Digital_wallet_fragment extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<dw_contentlist> itemList;
    ArrayList<Listmodel> arrayList;
    //private digital_wallet_content_adatper adapter;
   // private Activity activity;
    TextView tvsubhead,tvcontent1,tvcontent2,tvcontent3,tvcontent4;
    LinearLayout linearLayout;
    TextView tvfeature,tvterms_condition;
    //CheckBox check_terms_condition;

    Digitalwallet_SubContent adapter;

   RecyclerView rv_digitalcontent ;

    Button btn_buygold;
    dw_contentlist item;
    JSONArray jsonArray;
    String tearms_condition;
    ApiDao apiDao;


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        this.activity = (Activity) context;
//    }

TextView unameTv, uidTv;
    RelativeLayout backbtn;
    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digital_wallet_design);


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
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

        initlizeviews();
        intilizerecyclerview();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            getcontent();
            init_subcontent();
        }

    }

    public void intilizerecyclerview(){
        rv_digitalcontent =findViewById(R.id.rv_digitalcontent);

        rv_digitalcontent.setHasFixedSize(true);
        rv_digitalcontent.setLayoutManager(new LinearLayoutManager(this));
        rv_digitalcontent.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        rv_digitalcontent.addItemDecoration(decoration);
        arrayList = new ArrayList<>();
        adapter = new Digitalwallet_SubContent (this,arrayList);
        rv_digitalcontent.setAdapter(adapter);

    }

    public void initlizeviews(){

        tvsubhead = findViewById(R.id.sub_head);
        tvcontent1 = findViewById(R.id.tv_content1);
//        tvcontent2 = view.findViewById(R.id.tv_content2);
//        tvcontent3 = view.findViewById(R.id.tv_content3);
//        tvcontent4 = view.findViewById(R.id.tv_content4);

        tvterms_condition = findViewById(R.id.dw_terms_condition);
        tvterms_condition.setOnClickListener(this);

        tvfeature = findViewById(R.id.tvfeatures);
        tvfeature.setOnClickListener(this);

        itemList = new ArrayList<>();

       // check_terms_condition = findViewById(R.id.check_terms_condition);

        linearLayout = findViewById(R.id.linearlayout);

        btn_buygold = findViewById(R.id.btn_buygold);
        btn_buygold.setOnClickListener(this);

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    public void getcontent(){

            arrayList.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait.....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {

            Call<List_Model> getcontent = apiDao.getDidigtalwallet_content("Bearer " + AccountUtils.getAccessToken(this));
            getcontent.enqueue(new Callback<List_Model>() {
                @Override
                public void onResponse(Call<List_Model> call, Response<List_Model> response) {
                    int statuscode = response.code();
                    Log.e("respo", String.valueOf(statuscode));
                    List_Model listmodel = response.body();

                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        String maincontent = listmodel.getMainContent();
                        Log.e(" main content", maincontent);
                        String newmaincontent = maincontent;
                        try{
                            newmaincontent = maincontent.replace("Digital Wallet", "Gold Suvidha");
                        }catch (Exception e){
                            Log.e("changed from server","No changes");
                        }

                        tvsubhead.setText(newmaincontent);
                        tearms_condition = listmodel.getTerms_and_conditions();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<List_Model> call, Throwable t) {
                    Log.e("fail content", t.toString());
                    dialog.dismiss();
                }
            });
        }

    }
    public void init_subcontent(){
        arrayList.clear();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<data> getsubcomtent = apiDao.getDidigtalwallet_subcontent("Bearer "+AccountUtils.getAccessToken(this));
                getsubcomtent.enqueue(new Callback<data>() {
                    @Override
                    public void onResponse(Call<data> call, Response<data> response) {
                        int statuscode = response.code();
                        List<Listmodel> list = response.body().getSubContent();

                        if (statuscode == HttpsURLConnection.HTTP_OK){
                            for (Listmodel listmodel :list){
                                arrayList.add(listmodel);
                                adapter.notifyDataSetChanged();

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<data> call, Throwable t) {
                            Log.e("subcontent",t.toString());
                    }
                });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_buygold:
               /// openbuygold();
                validation();
                return;

            case R.id.tvfeatures:
                openfeature();
                return;

            case R.id.dw_terms_condition:
                openterms_condition();
                return;
        }
    }

    public void validation(){
        openbuygold();
//        if (!check_terms_condition.isChecked()){
//            ToastMessage.onToast(this,"Please Accept Terms and Condition",ToastMessage.ERROR);
//        }else {
//            openbuygold();
//        }

    }
    public void openfeature() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            Intent intent = new Intent(this, Digital_Feature.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


    }

    public void openterms_condition() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {

            Intent intent = new Intent(this, Digital_Terms_Condition.class);
            intent.putExtra("terms_condition", tearms_condition);
            intent.putExtra("value", "0");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void openbuygold(){

//        Buy_Digitalgold faqfragment = new Buy_Digitalgold();
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,faqfragment).addToBackStack("null").commit();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            Intent intent = new Intent(this, Buy_Digitalgold.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
