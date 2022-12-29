package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.OrganizationWalletModule.DonateGold;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TempleAndNGOsDetails extends AppCompatActivity implements View.OnClickListener {

    TextView tvorgid, tvorgname, tvorgreg, tvorgaddress, tvorgdesc;
    String storgid, storgname, storgreg, storgaddress, storgdesc, ststate;
    ImageView ivtemple;
    ApiDao apiDao;
    Button btntemple;
    String st_org_id, stid, stimage;
    RelativeLayout backbtn;
    TextView unameTv, uidTv, titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.templeandngos_details);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Organization Info");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            st_org_id = bundle.getString("org_id");
        }

        initviews();
        getdetails();
    }

    public void initviews() {

        tvorgid = findViewById(R.id.tvorgid);
        tvorgname = findViewById(R.id.tvorgname);
        tvorgreg = findViewById(R.id.tvorgreg);
        tvorgaddress = findViewById(R.id.tvorgaddress);
        tvorgdesc = findViewById(R.id.tvorgdesc);
        ivtemple = findViewById(R.id.ivtemple);
        btntemple = findViewById(R.id.btnDonate);
        btntemple.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        // finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getdetails() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<UserOrganizationDetailsModel> getdetails = apiDao.get_orginfo("Bearer " + AccountUtils.getAccessToken(this), st_org_id);
            getdetails.enqueue(new Callback<UserOrganizationDetailsModel>() {
                @Override
                public void onResponse(Call<UserOrganizationDetailsModel> call, Response<UserOrganizationDetailsModel> response) {
                    int statuscode = response.code();
                    Log.e("Stattuscode", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        dialog.dismiss();
                        UserOrganizationDetailsModel model = response.body();
                        storgid = model.getOrganization_id();
                        storgname = model.getName();
                        storgreg = model.getRegistration_number();
                        ststate = model.getState();
                        storgaddress = (model.getAddress() + ", " + model.getCity() + ", " + ststate + ", " + model.getZip());
                        storgdesc = model.getDescription();
                        stid = model.getId();
                        stimage = model.getPhotoImageLink();

                        setdetails();
                    } else {
                        dialog.dismiss();
                    //    ToastMessage.onToast(TempleAndNGOsDetails.this, "Technical Issue try after some time", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<UserOrganizationDetailsModel> call, Throwable t) {

                    dialog.dismiss();
                  //  ToastMessage.onToast(TempleAndNGOsDetails.this, "we have some issue try after some time", ToastMessage.ERROR);

                }
            });
        }
    }

    public void setdetails() {

        tvorgid.setText(storgid);
        tvorgname.setText(storgname);
        tvorgreg.setText(storgreg);
        tvorgaddress.setText(storgaddress);
        tvorgdesc.setText(storgdesc);

        Glide.with(this).load(stimage).into(ivtemple);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDonate:
                Intent intent = new Intent(TempleAndNGOsDetails.this, DonateGold.class);
                intent.putExtra("org_id", stid);
                startActivity(intent);
                break;
        }
    }
}
