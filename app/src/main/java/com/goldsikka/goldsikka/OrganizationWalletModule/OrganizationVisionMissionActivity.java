package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationVisionMissionActivity extends AppCompatActivity {

    TextView tvparagraph1;


    LinearLayout linearLayout,linear_layout;

    ApiDao apiDao;

    RelativeLayout backbtn;
String oid, oname;
TextView unameTv, uidTv;
    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_vissionmission);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initlizeviews();

        Bundle bundle = getIntent().getExtras();

        if (bundle!= null){
            oid = bundle.getString("oid");
            oname = bundle.getString("oname");
            uidTv.setText(oid);
            unameTv.setText(oname);

        }

//        if (!NetworkUtils.isConnected(this)){
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//            return;
//        }else {
//            getdata();
//        }
        //  setLocale("hi");

    }
//
//    public void setLocale(String lang) {
//        Locale myLocale = new Locale(lang);
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;
//        res.updateConfiguration(conf, dm);
//        Intent refresh = new Intent(activity, Aboutusfragment.class);
//        // finish();
//        startActivity(refresh);
//    }


    public void initlizeviews(){

        tvparagraph1 = findViewById(R.id.paragraph1);
        linearLayout = findViewById(R.id.linear_layout);
        linearLayout.setVisibility(View.VISIBLE);

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);

    }



    public void getdata() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait.....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Listmodel> aboutus = apiDao.getabouts("Bearer "+AccountUtils.getAccessToken(this));
        aboutus.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode  = response.code();
                Listmodel listmodel = response.body();
                if (statuscode == HttpsURLConnection.HTTP_OK){
                    dialog.dismiss();
                    linearLayout.setVisibility(View.VISIBLE);
                    listmodel.getParagraph();
                    tvparagraph1.setText(listmodel.getParagraph());
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                Log.e("about us on failure",t.toString());
                dialog.dismiss();
            }
        });


    }

}
