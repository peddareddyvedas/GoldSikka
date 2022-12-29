package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldsikka.goldsikka.Activitys.opencart_payment;
import com.goldsikka.goldsikka.Fragments.customer_addresslist;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.List_Model;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.data;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class Cartlist extends Fragment implements View.OnClickListener,OnItemClickListener {


   String  address,city,pin,state;
   TextView tv_customer_address,tv_city,tv_pin,tv_state;

    TextView tv_cartamount;
    TextView  tv_item;
    String productcount,st_totoalcartamount;
    Button btn_check;
     LinearLayout ll_cartaddress,linearlayout,ll_cardlist;
    NestedScrollView ll_savelist;

    RecyclerView rv_product_item,rv_saveitem;
    cart_productlist_adapter productlist_adapter;
    cart_savedlist_adapter savedlist_adapter;
    ArrayList<Listmodel> product_arrayList,saved_arraylist;
    String st_itemquantity;
    String profileaddressid;
    String from ;
    ApiDao apiDao;
    private Activity activity;
    RadioButton radioButton;
    Bundle bundle;
  LinearLayout  ll_cartlayout;

    List<Listmodel> lists;

    TextView unameTv, uidTv, titleTv;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.cartlist_frgment,container,false);

              intilizeviews(view);
              intilizerecylerview(view);
        bundle  = getArguments();
        if (bundle!= null) {
            from = bundle.getString("from");

        }

        ll_cartlayout = view.findViewById(R.id.ll_cartlayout);
        ll_cardlist = view.findViewById(R.id.ll_cardlist);

        unameTv = view.findViewById(R.id.uname);
        uidTv = view.findViewById(R.id.uid);
        titleTv = view.findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(getContext()));
        uidTv.setText(AccountUtils.getCustomerID(getContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Cart");

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Cart");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(activity.getColor(R.color.colorWhite));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equals("maincategory")){
                    EcommerceHome frgment = new EcommerceHome();
                    getFragmentManager().beginTransaction().replace(R.id.frame_layout,frgment).commit();
                }else if (from.equals("subcategory")){
                    Subcategorylist fragment = new Subcategorylist();
                    getFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commit();
                }
                else if (from.equals("productlist")){
                    Ecommerce_Productlist fragment = new Ecommerce_Productlist();
                    getFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
                }else if (from.equals("productinfo")){
                    prductinfo_fragment fragment = new prductinfo_fragment();
                    getFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
                }
              //  getActivity().onBackPressed();
            }
        });
        tv_item = view.findViewById(R.id.tv_itemcount);
        String noofproducts = AccountUtils.getProductQuantity(activity);
        if (noofproducts.equals("0")){
            tv_item.setVisibility(View.GONE);
        }else {
            tv_item.setText(noofproducts);
        }

        gettotalamount();
        getdata();
        open_savedlist();
      return view;
    }


    public void getdata(){

            product_arrayList.clear();

        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

        Call<data> call = apiDao.cart_list("Bearer "+AccountUtils.getAccessToken(activity));
        call.enqueue(new Callback<data>() {

            @Override
            public void onResponse( Call<data> call,Response<data> response) {

                int stauscode = response.code();
          lists = response.body().getResult();
                Log.e("Status code", String.valueOf(stauscode));
                if (stauscode == HttpsURLConnection.HTTP_OK){


                    Log.e("list response ",response.body().toString());
                    if (lists.size() != 0) {
           for (Listmodel listmodel : lists) {

                       Log.e("list model ", listmodel.toString());

                       linearlayout.setVisibility(View.GONE);
                       ll_cartlayout.setVisibility(View.VISIBLE);
                       ll_cardlist.setVisibility(View.VISIBLE);
                       try {

                           JSONObject object = new JSONObject(new Gson().toJson(listmodel));
                           Log.e("obj", object.toString());
//                           JSONArray jsonArray = object.getJSONArray("product_details");

                           JSONArray array = object.getJSONArray("product_image");



//                           int length = jsonArray.length();
//                           for (int index = 0; index < length; index++) {
//                               JSONObject object1 = jsonArray.getJSONObject(index);
//
//                               listmodel.setName(object1.getString("name"));
//                               listmodel.setGrams(object1.getString("grams"));
//                               listmodel.setId(object1.getString("id"));
//
//
//                           }
                           for (int i = 0; i < array.length(); i++) {
                               JSONObject object1 = array.getJSONObject(i);
                               listmodel.setProduct_uri(object1.getString("product_uri"));
                           }
                           product_arrayList.add(listmodel);

                           productlist_adapter.notifyDataSetChanged();
                           progressDialog.dismiss();
                       } catch (JSONException e) {
                           Log.e("catch", e.toString());
                           e.printStackTrace();
                       }
                   }


                  }
                    else {

                        progressDialog.dismiss();
                        open_savedlist();
                      //  linearlayout.setVisibility(View.VISIBLE);
                     //   ll_cartlayout.setVisibility(View.GONE);
                        ll_cardlist.setVisibility(View.GONE);
                    }

                }

                progressDialog.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<data> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e("failss",t.toString());
               // Toast.makeText(activity, "Technical problem", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void gettotalamount(){
      apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
      Call<List_Model> gettotal = apiDao.cart_listfortotalamount("Bearer "+AccountUtils.getAccessToken(activity));
      gettotal.enqueue(new Callback<List_Model>() {

          @Override
          public void onResponse(@NotNull Call<List_Model> call, @NotNull Response<List_Model> response) {
              int statuscode = response.code();
              List_Model listmodel = response.body();
              if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK){
                  st_totoalcartamount = listmodel.getTotalAmount();
                  tv_cartamount.setText(" "+getString(R.string.Rs)+st_totoalcartamount);
                  Log.e("totalamount",st_totoalcartamount);

              }
          }

          @Override
          public void onFailure(Call<List_Model> call, Throwable t) {
                Log.e("fail totla ",t.toString());
          }
      });
    }
    public void intilizeviews(View view){

        tv_cartamount = view.findViewById(R.id.tv_cartamount);


        linearlayout = view.findViewById(R.id.linearlayout);
        ll_cartaddress  = view.findViewById(R.id.ll_cartaddress);
        ll_cartaddress.setOnClickListener(this);

        btn_check = view.findViewById(R.id.btn_check);
        btn_check.setOnClickListener(this);

        ll_savelist = view.findViewById(R.id.ll_savelist);


    }
    public void intilizerecylerview(View view){

        //***Cart list RecyclerView***//

        rv_product_item = view.findViewById(R.id.rv_product_item);

        rv_product_item.setHasFixedSize(true);
        rv_product_item.setLayoutManager(new LinearLayoutManager(activity));
        rv_product_item.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(activity,LinearLayoutManager.VERTICAL);
        rv_product_item.addItemDecoration(decoration);
        product_arrayList = new ArrayList<>();
        productlist_adapter = new cart_productlist_adapter(activity,product_arrayList,this);
        rv_product_item.setAdapter(productlist_adapter);
        product_arrayList.clear();

        //***saved list RecyclerView***//

        rv_saveitem  = view.findViewById(R.id.rv_saveitem);

        rv_saveitem.setHasFixedSize(true);
        rv_saveitem.setLayoutManager(new LinearLayoutManager(activity));
        rv_saveitem.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration1 = new DividerItemDecoration(activity,LinearLayoutManager.VERTICAL);
        rv_saveitem.addItemDecoration(decoration1);
        saved_arraylist = new ArrayList<>();
        savedlist_adapter = new cart_savedlist_adapter(activity,saved_arraylist,this);
        rv_saveitem.setAdapter(savedlist_adapter);
        saved_arraylist.clear();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.ll_cartaddress:
                openbottomsheet(v);
                break;
            case R.id.btn_check:
                openbottomsheet(v);
                break;

        }
    }

    @Override
    public void onItemClick(View view, int position) {
            Listmodel listmodel = product_arrayList.get(position);
          //  Listmodel listmodel1 = saved_arraylist.get(position);

//            switch (view.getId()){
//                case R.id.btn_delete:
//                    open_itemdelete(listmodel.getId());
//                    break;
//
//            }
    }

    public void initremoveitem(String id){
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

        Call<Listmodel> call = apiDao.removeproduct("Bearer " + AccountUtils.getAccessToken(activity), id);
        call.enqueue(new Callback<Listmodel>() {


            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {

                int statuscode = response.code();
                List<Listmodel> list = Collections.singletonList(response.body());
                if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {
                    for (Listmodel listmodel: list) {
                        productcount = listmodel.getProductCount();
                        if (productcount.equals("0")){
                            tv_item.setVisibility(View.GONE);
                        }
                        else {
                            tv_item.setVisibility(View.VISIBLE);
                            tv_item.setText(productcount);
                        }
                        st_itemquantity = listmodel.getQuantity();
                        Log.e("quntity",st_itemquantity);

                    }


                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                Log.e("item remove fail",t.toString());
              //  ToastMessage.onToast(activity, "we have some issues", ToastMessage.ERROR);
            }
        });
    }

    public void initadditem(String id) {
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

        Call<Listmodel> call = apiDao.addproduct("Bearer " + AccountUtils.getAccessToken(activity), id);
        call.enqueue(new Callback<Listmodel>() {


            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {

                int statuscode = response.code();
                List<Listmodel> list = Collections.singletonList(response.body());
                if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {
                    for (Listmodel listmodel: list) {
                        productcount = listmodel.getProductCount();
                        if (productcount.equals("0")){
                            tv_item.setVisibility(View.GONE);
                        }
                        else {
                            tv_item.setVisibility(View.VISIBLE);
                            tv_item.setText(productcount);
                        }

                        st_itemquantity = listmodel.getQuantity();

                        Log.e("quntity",st_itemquantity);
                    }


                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {

                Log.e("add item fail",t.toString());
              //  ToastMessage.onToast(activity, "we have some issues", ToastMessage.ERROR);
            }
        });

    }
    public void open_itemdelete(String id){

    apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

    Call<Listmodel> cart_itemdelete = apiDao.cart_item_deete("Bearer "+AccountUtils.getAccessToken(activity),id);

    cart_itemdelete.enqueue(new Callback<Listmodel>() {
        @Override
        public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
            int statuscode = response.code();
            if (statuscode == HttpsURLConnection.HTTP_NO_CONTENT){
                Listmodel listmodel = response.body();
                product_arrayList.clear();
                getdata();
               gettotalamount();
              open_savedlist();

                ToastMessage.onToast(activity,"Removed",ToastMessage.SUCCESS);

            }
        }

        @Override
        public void onFailure(Call<Listmodel> call, Throwable t) {
            Log.e("cart delete fail",t.toString());

        }
    });

    }
    public  void open_savedlist(){
        saved_arraylist.clear();
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
        Call<data> getsavedlist = apiDao.cart_savedlist("Bearer "+AccountUtils.getAccessToken(activity));
        getsavedlist.enqueue(new Callback<data>() {
            @Override
            public void onResponse(Call<data> call, Response<data> response) {
                int statuscode = response.code();
                List<Listmodel> list = response.body().getResult();
                if (statuscode == HttpsURLConnection.HTTP_OK){
                    if (list.size() != 0) {
                        for (Listmodel listmodel : list) {
                            ll_savelist.setVisibility(View.VISIBLE);
                            linearlayout.setVisibility(View.GONE);
                           // getdata();
                            Log.e("list model ",listmodel.toString());

                            try {
                                JSONObject object = new JSONObject(new Gson().toJson(listmodel));
                                Log.e("obj", object.toString());
                               // JSONArray jsonArray = object.getJSONArray("product_details");
                                JSONArray array = object.getJSONArray("product_image");
//                                Log.e("Array",jsonArray.toString());
//                                int length = jsonArray.length();
//                                for (int index = 0; index < length; index++) {
//                                    JSONObject object1 = jsonArray.getJSONObject(index);
//
//                                    listmodel.setName(object1.getString("name"));
//                                    listmodel.setGrams(object1.getString("grams"));
//                                    listmodel.setId(object1.getString("id"));
//
//
//                                }
                                for (int j = 0; j<array.length();j++){
                                    JSONObject ob = array.getJSONObject(j);
                                    listmodel.setProduct_uri(ob.getString("product_uri"));
                                }
                                saved_arraylist.add(listmodel);
                                savedlist_adapter.notifyDataSetChanged();
                                //  card_arraylist.add(item);

                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                Log.e("catchsavrd",e.toString());
                                e.printStackTrace();
                            }
                        }

                    }
                    else {
                        list.size();
                        progressDialog.dismiss();
                        ll_savelist.setVisibility(View.GONE);
                        linearlayout.setVisibility(View.VISIBLE);
//                        if (lists.size() !=0){
//                            ll_savelist.setVisibility(View.GONE);
//                            getdata();
//                        }
//                        else {
//                            ll_savelist.setVisibility(View.GONE);
//                            linearlayout.setVisibility(View.VISIBLE);
//                        }

                       // getdata();

                        //  Toast.makeText(Cardlist.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                    }

                }else {
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

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<data> call, Throwable t) {
                        Log.e("saved fail",t.toString());
                        ToastMessage.onToast(activity,"We have some issues",ToastMessage.ERROR);
            }
        });


    }

    public void open_movetocart(String id){
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
        Call<Listmodel> movetocart = apiDao.movetocart("Bearer "+AccountUtils.getAccessToken(activity),id);
        movetocart.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                Listmodel listmodel = response.body();
                if (statuscode == HttpsURLConnection.HTTP_OK){
                    open_savedlist();
                    getdata();
                   gettotalamount();
                    ToastMessage.onToast(activity,"Moved to cart..",ToastMessage.SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                Log.e("move fail",t.toString());
            }
        });
    }
    public void initsavedlist(String id){

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
        Call<Listmodel> getsavedlist = apiDao.cart_saveditem("Bearer "+AccountUtils.getAccessToken(activity),id);
        getsavedlist.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                Listmodel listmodel = response.body();
                if (statuscode == HttpsURLConnection.HTTP_OK){
                    ll_savelist.setVisibility(View.VISIBLE);
                    open_savedlist();
                    getdata();
                    gettotalamount();
                    ToastMessage.onToast(activity,"Item saved into SavedList..",ToastMessage.SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                Log.e("saved fail",t.toString());
            }
        });
    }
    public void open_saveditemdelete(String id){
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);

        Call<Listmodel> itemdelete = apiDao.cart_saveditem_delete("Bearer "+AccountUtils.getAccessToken(activity),id);
        itemdelete.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                Listmodel listmodel = response.body();
                if (statuscode == HttpsURLConnection.HTTP_NO_CONTENT){
                    open_savedlist();
                    getdata();
                   gettotalamount();
                    ToastMessage.onToast(activity,"Deleted ",ToastMessage.SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("svade dele fail",t.toString());
            }
        });
    }

// ******* Adapter Class ****//
    class cart_productlist_adapter extends RecyclerView.Adapter<cart_productlist_adapter.Viewholder>{

        Context context;
        ArrayList<Listmodel> list;
        OnItemClickListener onItemClickListener;

        public cart_productlist_adapter(Context context,ArrayList<Listmodel> list,
                                        OnItemClickListener onItemClickListener){
            this.context = context;
            this.list = list;
            this.onItemClickListener = onItemClickListener;
        }

        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.cart_productlist,parent,false);
            return new Viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder holder, int position) {
            Listmodel listmodel = list.get(position);


            Picasso.with(context).
                    load(listmodel.getProduct_uri())
                    .into(holder.iv_prodimages);

            JsonObject Product_details = new JsonParser().parse(listmodel.getProduct_details().toString()).getAsJsonObject();

            try {
                JSONObject json_from = new JSONObject(Product_details.toString());
                String name = json_from.getString("name");
                String grams = json_from.getString("grams");
                String id = json_from.getString("id");
                listmodel.setId(id);
                holder.tv_prodname.setText(name);
                holder.tv_gramsvalue.setText(grams);

            }catch (JSONException e){
                e.printStackTrace();
            }


            holder.tv_quntity.setText(listmodel.getQuantity());
            holder.tv_amountvalue.setText(listmodel.getFinalAmount());


//            holder.btn_additem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    holder.ll_cartincdecr.setVisibility(View.VISIBLE);
//                    holder.ll_cartadditem.setVisibility(View.GONE);
//                    initadditem(listmodel.getId());
//                    holder.tv_carttext.setText(st_itemquantity);
//                }
//            });
//            holder.iv_cartadd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    initadditem(listmodel.getId());
//                    holder.tv_carttext.setText(st_itemquantity);
//                }
//            });
//            holder.iv_cartremove.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    initremoveitem(listmodel.getId());
//                    holder.tv_carttext.setText(st_itemquantity);
//                    if (st_itemquantity.equals("0")){
//                        holder.ll_cartincdecr.setVisibility(View.GONE);
//
//                        holder.ll_cartadditem.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("products_____id**",listmodel.getId());
                    open_itemdelete(listmodel.getId());
                }
            });
            holder.btn_savecart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initsavedlist(listmodel.getId());

                    Log.e("products_____id**",listmodel.getId());

                    ll_savelist.setVisibility(View.VISIBLE);
                    if (list.size()== 0){
                        ll_savelist.setVisibility(View.GONE);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tv_prodname,tv_gramsvalue,tv_quntity,tv_amountvalue,tv_carttext;
            Button btn_delete,btn_savecart,btn_additem;
            ImageView iv_cartremove,iv_cartadd,iv_prodimages;
            LinearLayout ll_cartadditem,ll_cartincdecr;

            public Viewholder(@NonNull View itemView) {
                super(itemView);

//                ll_cartadditem = itemView.findViewById(R.id.ll_cartadditem);
//                ll_cartincdecr = itemView.findViewById(R.id.ll_cartincdecr);

                tv_amountvalue = itemView.findViewById(R.id.tv_amountvalue);
                tv_prodname = itemView.findViewById(R.id.tv_prodname);
                tv_gramsvalue = itemView.findViewById(R.id.tv_gramsvalue);
                tv_quntity = itemView.findViewById(R.id.tv_quntity);
            //    tv_carttext = itemView.findViewById(R.id.tv_carttext);

                btn_delete = itemView.findViewById(R.id.btn_delete);
                btn_delete.setOnClickListener(this);

                btn_savecart = itemView.findViewById(R.id.btn_savecart);
                btn_savecart.setOnClickListener(this);

//                btn_additem = itemView.findViewById(R.id.btn_additem);
//                btn_additem.setOnClickListener(this);

                iv_prodimages = itemView.findViewById(R.id.iv_prodimages);
//
//                iv_cartremove = itemView.findViewById(R.id.iv_cartremove);
//                iv_cartremove.setOnClickListener(this);
//
//                iv_cartadd = itemView.findViewById(R.id.iv_cartadd);
//                iv_cartadd.setOnClickListener(this);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,getAdapterPosition());

            }
        }
    }

    public class cart_savedlist_adapter extends RecyclerView.Adapter<cart_savedlist_adapter.ViewHolder>{

        Context context;
        List<Listmodel> listdata;
        OnItemClickListener onItemClickListener;

        public cart_savedlist_adapter(Context context,List<Listmodel> list,
                                      OnItemClickListener onItemClickListener){
            this.context = context;
            this.listdata = list;
            this.onItemClickListener = onItemClickListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.cart_itemsavedlist,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Listmodel listmodel = listdata.get(position);

            Picasso.with(context).
                    load(listmodel.getProduct_uri())
                    .into(holder.iv_prodsaveimages);

            JsonObject Product_details = new JsonParser().parse(listmodel.getProduct_details().toString()).getAsJsonObject();

            try {
                JSONObject json_from = new JSONObject(Product_details.toString());
                String name = json_from.getString("name");
                String grams = json_from.getString("grams");
                String id = json_from.getString("id");
                listmodel.setId(id);
                holder.tv_prodsavename.setText(name);
                holder.tv_programsavevalue.setText(grams);

            }catch (JSONException e){
                e.printStackTrace();
            }

            holder.tv_prodamountsavevalue.setText(listmodel.getFinalAmount());

//            holder.tv_programsavevalue.setText(listmodel.getGrams());
//            holder.tv_prodsavename.setText(listmodel.getName());

            holder.btn_savedelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open_saveditemdelete(listmodel.getId());
                    Log.e("saved id",listmodel.getId());
                    //open_savedlist();
                }
            });
            holder.btn_movecart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open_movetocart(listmodel.getId());
                    Log.e("saved id",listmodel.getId());
                    if (listdata.size()==0){
                        ll_savelist.setVisibility(View.GONE);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

            TextView tv_prodsavename,tv_programsavevalue,tv_prodamountsavevalue;
            Button btn_savedelete,btn_movecart;
            ImageView iv_prodsaveimages;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_prodsavename = itemView.findViewById(R.id.tv_prodsavename);
                tv_programsavevalue = itemView.findViewById(R.id.tv_programsavevalue);
                tv_prodamountsavevalue = itemView.findViewById(R.id.tv_prodamountsavevalue);

                iv_prodsaveimages = itemView.findViewById(R.id.iv_prodsaveimages);

                btn_savedelete = itemView.findViewById(R.id.btn_savedelete);
                btn_savedelete.setOnClickListener(this);

                btn_movecart = itemView.findViewById(R.id.btn_movecart);
                btn_movecart.setOnClickListener(this);
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,getAdapterPosition());

            }
        }
    }

    public void openbottomsheet(View view){

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);
                View bottomSheet = LayoutInflater.from(activity)
                        .inflate(R.layout.layout_bottom_sheet,view.findViewById(R.id.bottomsheet));
        tv_customer_address = bottomSheet.findViewById(R.id.customer_address);
        tv_city = bottomSheet.findViewById(R.id.tvcity);
        tv_pin = bottomSheet.findViewById(R.id.tvpin);
        tv_state = bottomSheet.findViewById(R.id.tvstate);

                setprymary_address();

                radioButton =bottomSheet.findViewById(R.id.rb_address);
                radioButton.setChecked(true);
//                if (radioButton.isChecked()){
//
//                }

                bottomSheet.findViewById(R.id.id_checkout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        Intent intent = new Intent(activity, opencart_payment.class);
                        intent.putExtra("cart_amount",st_totoalcartamount);
                        intent.putExtra("addressid",profileaddressid);
                        Log.e("addresshjg id",profileaddressid);
                       // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });
                bottomSheet.findViewById(R.id.tv_more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("fromcartlist","cartlist");
                        bundle.putString("fromto",from);
                        customer_addresslist frg = new customer_addresslist();
                        frg.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.frame_layout,frg).commit();

                    }
                });
                bottomSheetDialog.setContentView(bottomSheet);
                bottomSheetDialog.show();
            }
            public void setprymary_address(){
                final ProgressDialog dialog = new ProgressDialog(activity);
                dialog.setMessage("Please Wait....");
                dialog.setCancelable(false);
                dialog.show();
                apiDao = ApiClient.getClient(AccountUtils.getAccessToken(activity)).create(ApiDao.class);
                Call<Listmodel> get_address = apiDao.
                        get_profileaddress("Bearer "+AccountUtils.getAccessToken(activity));
                get_address.enqueue(new Callback<Listmodel>() {
                    @Override
                    public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                        int statuscode = response.code();
                        List<Listmodel> list = Collections.singletonList(response.body());

                        if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_ACCEPTED) {
                            for (Listmodel listmodel : list) {
                                address = listmodel.getAddress();
                                city = listmodel.getCity();
                                pin = listmodel.getZip();
                              //  state = listmodel.getTitle();
                                profileaddressid = listmodel.getId();
                                JsonObject from = new JsonParser().parse(listmodel.getState().toString()).getAsJsonObject();


                                try {
                                    JSONObject json_from = new JSONObject(from.toString());
                                    state = json_from.getString("name");


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (address == null) {
                                    dialog.dismiss();
                                    tv_customer_address.setText("xxxxxx");
                                }
                                if (city == null){
                                    dialog.dismiss();
                                    tv_city.setText("xxxxxx");
                                }
                                if (pin == null){
                                    dialog.dismiss();
                                    tv_pin.setText("xxxxxx");
                                }
                                if (state == null){
                                    dialog.dismiss();
                                    tv_state.setText("xxxxxx");
                                }
                                else {
                                    address = listmodel.getAddress();
                                    city = listmodel.getCity();
                                    pin = listmodel.getZip();
                                    JsonObject from1 = new JsonParser().parse(listmodel.getState().toString()).getAsJsonObject();
                                    try {
                                        JSONObject json_from = new JSONObject(from1.toString());
                                        state = json_from.getString("name");


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    setadresstext();
                                    dialog.dismiss();
                                }
                            }
                        } else {
                            dialog.dismiss();
                            ToastMessage.onToast(activity, "Add Address", ToastMessage.ERROR);
                        }
                    }

                    @Override
                    public void onFailure(Call<Listmodel> call, Throwable t) {
                        dialog.dismiss();
                        ToastMessage.onToast(activity,"We Have Some Issues",ToastMessage.ERROR);
                    }
                });

            }
            public void setadresstext(){
                tv_city.setText(city);
                tv_state.setText(state);
                tv_customer_address.setText(address);
                tv_pin.setText(pin);
            }


}
