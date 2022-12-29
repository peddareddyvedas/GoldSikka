package com.goldsikka.goldsikka.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Activitys.Edit_CustomerAddress;
import com.goldsikka.goldsikka.Activitys.Passbook_Activity;
import com.goldsikka.goldsikka.Activitys.Profile_Details;
import com.goldsikka.goldsikka.Adapter.Customer_Address_List_Adapter;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Cartlist;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.data;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class customer_addresslist extends Fragment implements View.OnClickListener , OnItemClickListener {

    LinearLayout lladd_address,ll_addressempty,ll_list;
    RecyclerView recyclerView;
    ArrayList<Listmodel> itemList;
   Customer_Address_List_Adapter adapter;
    ApiDao apiDao;
    private Activity activity;
    String from ,fromto;
    String addrssid;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_address_list,container,false);
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
        Bundle bundle= getArguments();
        if (bundle!=null){
            from = bundle.getString("fromcartlist");
            fromto  = bundle.getString("fromto");
        }
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle("AddressList");
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
      //  toolbar.setTitleTextColor(activity.getColor(R.color.colorWhite));
        toolbar.setNavigationOnClickListener(v -> {
            if (from.equals("profile")) {
                Intent intent = new Intent(activity, Profile_Details.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }else if (from.equals("cartlist")){

                Bundle bundle1 = new Bundle();
                bundle1.putString("from",fromto);
                Cartlist frggs = new Cartlist();
                frggs.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout,frggs).commit();
            }
        });
        intilizeviews(view);
        initlizerecyclerview(view);
        return view;
    }
    public void intilizeviews(View view){
        lladd_address = view.findViewById(R.id.lladd_address);
        lladd_address.setOnClickListener(this);
        ll_addressempty = view.findViewById(R.id.ll_addressempty);
        ll_list = view.findViewById(R.id.ll_list);
    }
    public void initlizerecyclerview(View view){
        recyclerView = view.findViewById(R.id.address_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(decoration);

        itemList = new ArrayList<Listmodel>();
        adapter = new Customer_Address_List_Adapter(activity,itemList,this);

        recyclerView.setAdapter(adapter);
        openaddresslist();
    }
    public void openaddresslist(){
        itemList.clear();
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();


        Call<data> getaddress = apiDao.get_addresslist("Bearer "+AccountUtils.getAccessToken(activity));
        getaddress.enqueue(new Callback<data>() {
            @Override
            public void onResponse(Call<data> call, Response<data> response) {
                    int statuscode = response.code();
                    if (statuscode == HttpsURLConnection.HTTP_OK||statuscode== HttpsURLConnection.HTTP_CREATED) {
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
                            Toast.makeText(activity, "No Data Available ", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                dialog.dismiss();

         }

            @Override
            public void onFailure(Call<data> call, Throwable t) {
                ToastMessage.onToast(activity,"We Have Some Issues",ToastMessage.ERROR);
                dialog.dismiss();
            }
        });
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lladd_address:
                openaddaddress();

        }

    }
    public void openaddaddress(){
        Bundle bundel = new  Bundle();
        bundel.putString("from","list");
        bundel.putString("fromlist",from);
        bundel.putString("frommtoo",fromto);
        Customer_AddAddress frs =    new Customer_AddAddress();
        frs.setArguments(bundel);
        getFragmentManager().beginTransaction().replace(R.id.frame_layout,frs).commit();
        //
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemClick(View view, int position) {
        Listmodel list = itemList.get(position);
            switch (view.getId()){
                case R.id.bt_edit:
                    openedit(Integer.parseInt(list.getId()));
                    break;
                case R.id.bt_remove:
                    openremove(list.getId());
                    break;
                case R.id.tv_setprimary:
                    addrssid = list.getId();

                    set_primary(list.getId());
                    break;
            }
    }

    private void set_primary(String id) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
        Call<List<Listmodel>> get_primmaryaddress = apiDao.get_primaryaddress("Bearer "+AccountUtils.getAccessToken(activity),
               id);
        get_primmaryaddress.enqueue(new Callback<List<Listmodel>>() {
            @Override
            public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                int statuscode = response.code();
                if (statuscode == HttpsURLConnection.HTTP_NO_CONTENT||statuscode == HttpsURLConnection.HTTP_ACCEPTED){
                    dialog.dismiss();
                   onsuccess(id);
                    ToastMessage.onToast(activity,"successfully added",ToastMessage.SUCCESS);
                }else {
                    dialog.dismiss();
                    ToastMessage.onToast(activity," Server error",ToastMessage.ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                    dialog.dismiss();
                ToastMessage.onToast(activity," We have some issues",ToastMessage.ERROR);

            }
        });
    }
    public void onsuccess(String  id){
        if (from.equals("profile")){
            Intent intent = new Intent(activity,Profile_Details.class);
            intent.putExtra("id",id);
            Log.e("id address" ,id);
            intent.putExtra("from",from);
            intent.putExtra("fromto",fromto);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if (from.equals("cartlist")){
            Bundle bundle = new Bundle();
            bundle.putString("address_id",id);
            bundle.putString("from",fromto);
            Cartlist frggs = new Cartlist();
            frggs.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frame_layout,frggs).commit();
        }

    }

    public void openedit(int id){
        Intent intent = new Intent(activity, Edit_CustomerAddress.class);
        intent.putExtra("id",id);
        intent.putExtra("from",from);
        intent.putExtra("fromto",fromto);
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
    public void openremove(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

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
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        Call<Listmodel> addressremove = apiDao.get_address_remove("Bearer "+AccountUtils.getAccessToken(activity),id);
        addressremove.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                if (statuscode == HttpsURLConnection.HTTP_NO_CONTENT){
                    dialog.dismiss();
                    openaddresslist();
                    ToastMessage.onToast(activity,"Address Removed",ToastMessage.SUCCESS);
                }else {
                    dialog.dismiss();

                }
         }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                ToastMessage.onToast(activity,"We Have Some Issues",ToastMessage.ERROR);
                dialog.dismiss();
            }
        });
    }

}
