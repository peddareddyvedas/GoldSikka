package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Activitys.Profile.CustomerAddAddress;
import com.goldsikka.goldsikka.Activitys.Profile.CustomerAddressList;
import com.goldsikka.goldsikka.Activitys.Profile.CustomerEditAddress;
import com.goldsikka.goldsikka.Activitys.Profile_Details;
import com.goldsikka.goldsikka.Adapter.Customer_Address_List_Adapter;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.data;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationAddressList extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll_addressempty,ll_list;
    Button lladd_address;
    RecyclerView recyclerView;
    ArrayList<Listmodel> itemList;
    OrganizationAddressAdapter adapter;
    ApiDao apiDao;
    String from ,fromto;
    String addrssid;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_addresslist);

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.OrgPersonal_Address));
        //toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        intilizeviews();
        initlizerecyclerview();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }
    public void intilizeviews(){
        lladd_address = findViewById(R.id.lladd_address);
        lladd_address.setOnClickListener(this);
        ll_addressempty = findViewById(R.id.ll_addressempty);
        ll_list = findViewById(R.id.ll_list);
    }
    public void initlizerecyclerview(){
        recyclerView = findViewById(R.id.address_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrganizationAddressList.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                OrganizationAddressList.this, 0);
        recyclerView.addItemDecoration(decoration);

        itemList = new ArrayList<Listmodel>();
        adapter = new OrganizationAddressAdapter(OrganizationAddressList.this,itemList);

        recyclerView.setAdapter(adapter);
        openaddresslist();
    }
    public void openaddresslist(){
        itemList.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {

            Call<data> getaddress = apiDao.get_addresslist("Bearer " + AccountUtils.getAccessToken(this));
            getaddress.enqueue(new Callback<data>() {
                @Override
                public void onResponse(Call<data> call, Response<data> response) {
                    int statuscode = response.code();
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {
                        List<Listmodel> list = response.body().getResult();
                        if (list.size() != 0) {
                            for (Listmodel listmodel : list) {

                                itemList.add(listmodel);
                                adapter.notifyDataSetChanged();
                                ll_addressempty.setVisibility(View.GONE);
                                dialog.dismiss();
                            }
                        } else {
                            ll_list.setVisibility(View.GONE);
                            dialog.dismiss();
                            ll_addressempty.setVisibility(View.VISIBLE);
                            Toast.makeText(OrganizationAddressList.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            Toast.makeText(OrganizationAddressList.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                    dialog.dismiss();

                }

                @Override
                public void onFailure(Call<data> call, Throwable t) {
                    ToastMessage.onToast(OrganizationAddressList.this, "We Have Some Issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lladd_address:
                openaddaddress();
                break;

        }

    }

    public void openaddaddress(){
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            Intent intent = new Intent(OrganizationAddressList.this, OrganizationAddAddress.class);
            intent.putExtra("from", "addresslist");
            startActivity(intent);
        }
    }

}
