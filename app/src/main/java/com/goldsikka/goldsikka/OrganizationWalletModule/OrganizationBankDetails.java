package com.goldsikka.goldsikka.OrganizationWalletModule;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Activitys.Edit_CustomerBankdetails;
import com.goldsikka.goldsikka.Adapter.Customer_bankdetailslist_Adapter;
import com.goldsikka.goldsikka.Fragments.Customer_AddBankDetails;
import com.goldsikka.goldsikka.Fragments.Customer_BankDetailslist;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
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

public class OrganizationBankDetails extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    LinearLayout ll_bankempty,ll_list;
    Button ll_bankdetails;
    RecyclerView recyclerView;
    ArrayList<Listmodel> arrayList;
    OrganizationBankDetailsListAdapter adapter;
    ApiDao apiDao;

    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_bankdetails);

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.Bank_Details));
        //  toolbar.setTitleTextColor(getColor(R.color.colorWhite));
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
        NavUtils.navigateUpFromSameTask(this);
    }

    public void intilizeviews(){
        ll_bankdetails = findViewById(R.id.lladd_bankdetails);
        ll_bankdetails.setOnClickListener(this);
        ll_bankempty = findViewById(R.id.ll_bankempty);
        ll_list = findViewById(R.id.ll_list);


    }
    public void initlizerecyclerview(){
        recyclerView = findViewById(R.id.bank_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrganizationBankDetails.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                OrganizationBankDetails.this,0);
        recyclerView.addItemDecoration(decoration);

        arrayList = new ArrayList<Listmodel>();
        adapter = new OrganizationBankDetailsListAdapter(OrganizationBankDetails.this,arrayList,this);

        recyclerView.setAdapter(adapter);
        openbank_list();
    }

    private void openbank_list() {
        arrayList.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();

        }else {
            Call<data> getbank_list = apiDao.get_banklist("Bearer " + AccountUtils.getAccessToken(this));
            getbank_list.enqueue(new Callback<data>() {
                @Override
                public void onResponse(Call<data> call, Response<data> response) {
                    int statuscode = response.code();
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {
                        List<Listmodel> list = response.body().getResult();
                        if (list.size() != 0) {
                            for (Listmodel listmodel : list) {
                                ll_bankempty.setVisibility(View.GONE);
                                arrayList.add(listmodel);
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        } else {
                            ll_bankempty.setVisibility(View.VISIBLE);
                            ll_list.setVisibility(View.GONE);
                            dialog.dismiss();
                            Toast.makeText(OrganizationBankDetails.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dialog.dismiss();
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            Toast.makeText(OrganizationBankDetails.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<data> call, Throwable t) {
                  //  ToastMessage.onToast(OrganizationBankDetails.this, "We Have Some Issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addressmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.maddaddress){
            openbankdetails();
        }
        if (item.getItemId() == android.R.id.home) {//finish();
            onBackPressed();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.lladd_bankdetails) {
            openbankdetails();
        }
    }

    private void openbankdetails() {

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

        }else {
            Intent intent = new Intent(this, OrganizationAddBankDetails.class);
            intent.putExtra("from", "banklist");
            startActivity(intent);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemClick(View view, int position) {
        Listmodel list = arrayList.get(position);
        switch (view.getId()){
            case R.id.bt_edit:

                openedit(list.getId());
                break;
            case R.id.bt_remove:

                if (!NetworkUtils.isConnected(this)) {
                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                }else {
                    openremove(list.getId());
                }
                break;
            case R.id.set_bank_primary:
                set_bankdetails_asprimary(list.getId());
                break;
        }
    }

    private void set_bankdetails_asprimary(String id) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        }else {
            Call<List<Listmodel>> get_primarybankdetails = apiDao.get_primarybankdetails("Bearer " + AccountUtils.getAccessToken(this), id);
            get_primarybankdetails.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    if (statuscode == HttpsURLConnection.HTTP_ACCEPTED) {
                        List<Listmodel> list = response.body();
                        //for (Listmodel listmodel:list){
                        dialog.dismiss();
                        openbank_list();
//
                        ToastMessage.onToast(OrganizationBankDetails.this, "successfully added", ToastMessage.SUCCESS);

                    } else {
                        dialog.dismiss();
                        ToastMessage.onToast(OrganizationBankDetails.this, "server error", ToastMessage.ERROR);
                    }

                }

                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                    dialog.dismiss();
                //    ToastMessage.onToast(OrganizationBankDetails.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

    private void openremove(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure want to delete")
                .setTitle("Alert")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        get_bankdetailsremove(id);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void get_bankdetailsremove(String id) {

        Log.e("iddddddd bank",id);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            Call<Listmodel> bankdetaisremove = apiDao.get_bankdetails_remove("Bearer " + AccountUtils.getAccessToken(this), id);
            bankdetaisremove.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    if (statuscode == HttpsURLConnection.HTTP_NO_CONTENT) {
                        dialog.dismiss();
                        openbank_list();
                        ToastMessage.onToast(OrganizationBankDetails.this, "Bank details Removed", ToastMessage.SUCCESS);
                    } else {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                  //  ToastMessage.onToast(OrganizationBankDetails.this, "We Have Some Issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }
    }

    private void openedit(String id) {

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {
            Intent intent = new Intent(this, OrganizationUpdateBankDetails.class);
            intent.putExtra("id", id);
            intent.putExtra("from", "banklist");
            startActivity(intent);
        }
    }
}
