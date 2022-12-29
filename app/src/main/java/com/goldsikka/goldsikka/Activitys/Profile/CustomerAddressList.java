package com.goldsikka.goldsikka.Activitys.Profile;

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

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.Activitys.Profile_Details;
import com.goldsikka.goldsikka.Adapter.Customer_Address_List_Adapter;
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

public class CustomerAddressList extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {
    LinearLayout ll_addressempty, ll_list;
    Button lladd_address;
    RecyclerView recyclerView;
    ArrayList<Listmodel> itemList;
    Customer_Address_List_Adapter adapter;
    ApiDao apiDao;
    String from, fromto;
    String addrssid;

    TextView unameTv, uidTv, titleTv;


    RelativeLayout backbtn;
    ImageView addcustomeraddress;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_address_list);


        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle(getResources().getString(R.string.Personal_Address));

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Address List");
        addcustomeraddress = findViewById(R.id.addcustomeraddress);

        backbtn = findViewById(R.id.backbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        addcustomeraddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openaddaddress();
            }
        });
//
//        unameTv.setText(AccountUtils.getName(this));
//        uidTv.setText(AccountUtils.getCustomerID(this));
//        titleTv.setVisibility(View.VISIBLE);
//        titleTv.setText("Personal Address");
        //toolbar.setTitleTextColor(getColor(R.color.colorWhite));
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        intilizeviews();
        initlizerecyclerview();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.addressmenu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.maddaddress) {
//            openaddaddress();
//        }
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void intilizeviews() {
        lladd_address = findViewById(R.id.lladd_address);
        lladd_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openaddaddress();

            }
        });
        ll_addressempty = findViewById(R.id.ll_addressempty);
        ll_list = findViewById(R.id.ll_list);


    }

    public void initlizerecyclerview() {
        recyclerView = findViewById(R.id.address_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerAddressList.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                CustomerAddressList.this, 0);
        recyclerView.addItemDecoration(decoration);

        itemList = new ArrayList<Listmodel>();
        adapter = new Customer_Address_List_Adapter(CustomerAddressList.this, itemList, this);

        recyclerView.setAdapter(adapter);
        openaddresslist();
    }

    public void openaddresslist() {
        itemList.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {

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
                            Toast.makeText(CustomerAddressList.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            Toast.makeText(CustomerAddressList.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                    dialog.dismiss();

                }

                @Override
                public void onFailure(Call<data> call, Throwable t) {
                    ToastMessage.onToast(CustomerAddressList.this, "We Have Some Issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
      /*  switch (view.getId()) {
            case R.id.lladd_address:
                openaddaddress();
                break;

        }*/

    }

    public void openaddaddress() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            Intent intent = new Intent(CustomerAddressList.this, CustomerAddAddress.class);
            intent.putExtra("from", "addresslist");
            startActivity(intent);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemClick(View view, int position) {
        Listmodel list = itemList.get(position);
        switch (view.getId()) {
            case R.id.bt_edit:
                openedit(Integer.parseInt(list.getId()));
                break;
            case R.id.bt_remove:
                if (!NetworkUtils.isConnected(this)) {
                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                } else {
                    openremove(list.getId());
                }
                break;
            case R.id.tv_setprimary:
                addrssid = list.getId();
                if (!NetworkUtils.isConnected(this)) {
                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                } else {
                    set_primary(list.getId());
                }
                break;
        }
    }

    private void set_primary(String id) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<List<Listmodel>> get_primmaryaddress = apiDao.get_primaryaddress("Bearer " + AccountUtils.getAccessToken(this),
                    id);
            get_primmaryaddress.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    if (statuscode == HttpsURLConnection.HTTP_NO_CONTENT || statuscode == HttpsURLConnection.HTTP_ACCEPTED) {
                        dialog.dismiss();
                        openaddresslist();
                        //  onsuccess(id);
                        ToastMessage.onToast(CustomerAddressList.this, "successfully added", ToastMessage.SUCCESS);

                    } else {
                        dialog.dismiss();
                        ToastMessage.onToast(CustomerAddressList.this, " Server error", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(CustomerAddressList.this, " We have some issues", ToastMessage.ERROR);

                }
            });
        }
    }

    public void onsuccess(String id) {

        Intent intent = new Intent(CustomerAddressList.this, Profile_Details.class);
        intent.putExtra("id", id);

        startActivity(intent);


    }

    public void openedit(int id) {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            Intent intent = new Intent(CustomerAddressList.this, CustomerEditAddress.class);
            intent.putExtra("id", id);
            intent.putExtra("from", "addresslist");
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    public void openremove(String id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure want to delete")
                .setTitle("Alert")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        get_addressremove(id);
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

    private void get_addressremove(String id) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            Call<Listmodel> addressremove = apiDao.get_address_remove("Bearer " + AccountUtils.getAccessToken(this), id);
            addressremove.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    if (statuscode == HttpsURLConnection.HTTP_NO_CONTENT) {
                        dialog.dismiss();
                        openaddresslist();
                        ToastMessage.onToast(CustomerAddressList.this, "Address Removed", ToastMessage.SUCCESS);
                    } else {
                        assert response.errorBody() != null;
                        JSONObject jObjError = null;
                        try {
                            jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            ToastMessage.onToast(CustomerAddressList.this, st, ToastMessage.ERROR);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    ToastMessage.onToast(CustomerAddressList.this, "We Have Some Issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }
    }
}