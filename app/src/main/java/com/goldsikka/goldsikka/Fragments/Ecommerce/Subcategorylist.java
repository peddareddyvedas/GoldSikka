package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Adapter.Ecommerce.Subcategorylist_adpter;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Subcategorylist extends Fragment implements OnItemClickListener, View.OnClickListener {


    ApiDao apiDao;
    private Activity activity;
    RecyclerView rv_subcategory;
    ImageView iv_cart;
    Subcategorylist_adpter adapter;
    ArrayList<Listmodel> arrayList;

    String categoryid;
    String isSubCategories;
    String subcategoryid, name;
    Listmodel list, listsubcategory;
    String str_categoryid, st_from;
    TextView tv_item;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subcategorylist, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Ecommerce");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(activity.getColor(R.color.colorWhite));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //getActivity().onBackPressed();
                EcommerceHome frgment = new EcommerceHome();
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, frgment).commit();
            }
        });
        categoryid = AccountUtils.getMainCategoryId(activity);

        iv_cart = view.findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(this);
        tv_item = view.findViewById(R.id.tv_itemcount);
        String noofproducts = AccountUtils.getProductQuantity(activity);
        if (noofproducts.equals("0")) {
            tv_item.setVisibility(View.GONE);
        } else {
            tv_item.setText(noofproducts);
        }

        intilizerecyclerview(view);
        getdata(view);
        return view;
    }

    public void getdata(View view) {

        arrayList.clear();

        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<Listmodel> get_subcotegorylist = apiDao.get_subcotegorylist(categoryid);
        get_subcotegorylist.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                Listmodel listmodel = response.body();

                if (statuscode == HttpsURLConnection.HTTP_OK) {
                    dialog.dismiss();
                    isSubCategories = listmodel.getIsSubCategories();
                    if (!isSubCategories.equals("false")) {
                        JsonObject from = new JsonParser().parse(listmodel.getSubCategories().toString()).getAsJsonObject();
                        Log.e("form", from.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(from.toString());
                            JSONArray jsonArrayd = jsonObject.getJSONArray("data");
                            Log.e("print", jsonArrayd.toString());
//
                            for (int i = 0; i < jsonArrayd.length(); i++) {
                                Log.e("iew", String.valueOf(i));
                                subcategoryid = "1";
                                JSONObject object = jsonArrayd.getJSONObject(i);
                                list = new Listmodel();
                                list.setId(object.getString("id"));
                                list.setName(object.getString("name"));
                                list.setCategory_uri("category_uri");
                                arrayList.add(list);
                                adapter.notifyDataSetChanged();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                dialog.dismiss();
           //     ToastMessage.onToast(activity, "we have some issues", ToastMessage.ERROR);

            }
        });
    }


    public void intilizerecyclerview(View view) {

        rv_subcategory = view.findViewById(R.id.rv_subcategory);

        rv_subcategory.setHasFixedSize(true);
        rv_subcategory.setLayoutManager(new GridLayoutManager(activity, 2));
        rv_subcategory.setItemAnimator(new DefaultItemAnimator());

//        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(activity,LinearLayoutManager.VERTICAL);
//        rv_subcategory.addItemDecoration(decoration);
        arrayList = new ArrayList<>();
        adapter = new Subcategorylist_adpter(activity, arrayList, this);
        rv_subcategory.setAdapter(adapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemClick(View view, int position) {
        list = arrayList.get(position);
        listsubcategory = arrayList.get(position);

        switch (view.getId()) {
            case R.id.iv_subcategoryimg:
                if (subcategoryid.equals("1")) {

                    str_categoryid = list.getId();
                    AccountUtils.setSubCategoryId(activity, str_categoryid);
                    init_subcategory(str_categoryid, isSubCategories);

                } else {
                    str_categoryid = listsubcategory.getId();
                    AccountUtils.setSubCategoryId(activity, str_categoryid);
                    init_subcategory(str_categoryid, isSubCategories);
                }

                break;
        }
    }

    public void init_subcategory(String id, String isSubCategoriee) {

        if (isSubCategoriee.equals("true")) {
            getsubcategorylist(id);
        } else {
            Ecommerce_Productlist fragment = new Ecommerce_Productlist();
            getFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
        }
    }

    public void getsubcategorylist(String id) {
        arrayList.clear();

        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<Listmodel> get_subcotegorylist = apiDao.get_subcotegorylist(id);
        get_subcotegorylist.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                listsubcategory = response.body();
                if (statuscode == HttpsURLConnection.HTTP_OK) {
                    isSubCategories = listsubcategory.getIsSubCategories();
                    Log.e("iscategory", isSubCategories);
                    if (!isSubCategories.equals("false")) {
                        dialog.dismiss();
                        JsonObject from = new JsonParser().parse(listsubcategory.getSubCategories().toString()).getAsJsonObject();
                        Log.e("form", from.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(from.toString());
                            JSONArray jsonArrayd = jsonObject.getJSONArray("data");
                            Log.e("print", jsonArrayd.toString());
//
                            for (int i = 0; i < jsonArrayd.length(); i++) {
                                dialog.dismiss();
                                Log.e("iew", String.valueOf(i));
                                subcategoryid = "2";
                                JSONObject object = jsonArrayd.getJSONObject(i);
                                listsubcategory = new Listmodel();
                                listsubcategory.setId(object.getString("id"));
                                listsubcategory.setName(object.getString("name"));
                                arrayList.add(listsubcategory);
                                adapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        dialog.dismiss();
//
                        Ecommerce_Productlist fragment = new Ecommerce_Productlist();

                        getFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
                    }

                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
             //   ToastMessage.onToast(activity, "we have some issues", ToastMessage.ERROR);

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_cart) {
            Bundle bundle = new Bundle();
            bundle.putString("from", "subcategory");
            Cartlist frgment = new Cartlist();
            frgment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frame_layout, frgment).commit();
        }
    }
}
