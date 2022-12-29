package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters.Reviewproductadapter;
import com.goldsikka.goldsikka.Fragments.JewelleryInventory.Adapters.AllProductsAdapter;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.ReviewsModel;
import com.goldsikka.goldsikka.model.Sizes;
import com.goldsikka.goldsikka.model.Stones;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ecommproductdiscreption extends AppCompatActivity {
    TextView unameTv, uidTv, titleTv, pname, ratetext, pdiscreption, pva, pweight, pcatid, pidTv, ecomsubtotal, productprice, ecomgst, ecomva, ecomgrandtotal;
    RelativeLayout backbtn;
    ImageView pimg;
    shared_preference sharedPreference;
    LinearLayout llproductdetails, llproductrating;
    RelativeLayout rldetails, rlreviews;
    Button addtocart;
    Bundle bundle;
    String productimage, productname;
    ImageView proimage;
    CheckBox selectwhislist;
    TextView protext, similarproducts;
    ApiDao apiDao;
    String id = "0";
    Listmodel list;
    List<Listmodel> flist;
    List<ReviewsModel> rflist;

    Listmodel pflist;
    AllProductsAdapter allProductsAdapter;

    List<Listmodel> flistwish = new ArrayList<>();
    List<String> checklist = new ArrayList<String>();

    String Productweight;

    String from = "sdf";
    RecyclerView rvsimilarproducts;
    SimilarproductsAdapter similaradapter;
    ArrayList<Listmodel> similarList;
    TextView viewallproducts, reviewproducts;
    ArrayList<Listmodel> cartList;
    Listmodel ratelist;
    String stForm, pid;
    int stRating = 0;
    EditText etFeedback;
    RatingBar rating;
    LinearLayout selectlist, ratingll, banglessize_ll;

    RecyclerView rvreviewproducts;
    Reviewproductadapter reviewadapter;
    ArrayList<ReviewsModel> reviewList;

    Spinner spinnerviewBangles;
    TextView customdesign, ecomliveprice;

    ImageView imageView_close;
    TextView subcat_nameTV, subcat_priceTV, goldcolortv, goldyellowtv, goldkarat14, goldkarat18, diamondsiij, diamondsigh, diamondvsgh, diamondvvsef, cancelTv;
    String selectname = "";
    //  String[] rings = {"Select Size ", "6", "7", "8", "9", "10", "11", "12"};

    /*String[] bangle = {"Select Size ", "1'' ", "2''", "3''", "4''"};
    String[] rings ;*/

    TextView stonename, stoneprice, stonecolore, txtstoneprice;
    String sizeitem = "";
    String toname;

    ArrayList<String> arrayList = new ArrayList<>();
    String selectsize, selectweight, selectstoneprice, selectliveprice;
    List<String> spinnerlist = new ArrayList<>();
    String bulltype = "";
    ToggleButton selectfavourite;
    RelativeLayout relativesubtota, relativeva, relativegrandtotal, relativestone;
    int selectedPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecom_productdiscreption_activity);
        bundle = getIntent().getExtras();
        id = bundle.getString("id");
        Log.e("id", "" + id);
        toname = bundle.getString("catrgoryname");
        Log.e("toname", "" + toname);


        init();
        getEcomWishlistCount();
        getEcomProductdisc(id);
        getEcomProductRating();
    }

    public void getEcomWishlistCount() {
        checklist.clear();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<List<Listmodel>> getWishlist = apiDao.getEcomFavvouritelist("Bearer " + AccountUtils.getAccessToken(this));
        getWishlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("favcountstatuscode", String.valueOf(statuscode));
                flistwish = response.body();
                if (flistwish != null) {
                    checklist.clear();
                    for (Listmodel list : flistwish) {
                        checklist.add(list.getId());
                    }
                    if (statuscode == 200 || statuscode == 202) {
                        Log.e("sdfsd", "dvd");
                    } else if (statuscode == 404) {
                        Log.e("cv", String.valueOf(statuscode));
                    } else {
                        Log.e("fgd", "sdfsd");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
            }
        });
    }

    public void init() {
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Product Description");
        similarproducts = findViewById(R.id.similarproducts);
        addtocart = (Button) findViewById(R.id.addtocart);
        backbtn = findViewById(R.id.backbtn);
        ratetext = findViewById(R.id.ratetext);
        pdiscreption = findViewById(R.id.pdiscreption);
        reviewproducts = findViewById(R.id.reviewproducts);
        spinnerviewBangles = findViewById(R.id.spinner_spnring);
        customdesign = findViewById(R.id.bottom_textview);
        ecomliveprice = findViewById(R.id.ecomliveprice);
        stonename = findViewById(R.id.stonename);
        stoneprice = findViewById(R.id.stoneprice);
        stonecolore = findViewById(R.id.stonecolore);
        selectlist = findViewById(R.id.selectlist);
        ratingll = findViewById(R.id.ratingll);
        banglessize_ll = findViewById(R.id.banglessize_ll);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postEcommaddcartproducts(id);
                /*if (sizeitem.equals("")) {
                    ToastMessage.onToast(getApplicationContext(), "Please Select Size", ToastMessage.ERROR);
                    addtocart.setClickable(false);
                } else {
                    addtocart.setClickable(true);
                    postEcommaddcartproducts(id);
                }*/
            }
        });
        customdesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customizedesign();
            }
        });
        pname = findViewById(R.id.pnametv);
        pva = findViewById(R.id.pvaTv);
        pweight = findViewById(R.id.pweightTv);
        pimg = findViewById(R.id.ecommpimg);
        pidTv = findViewById(R.id.pidTv);
        productprice = findViewById(R.id.productprice);
        ecomsubtotal = findViewById(R.id.ecomsubtotal);
        ecomgst = findViewById(R.id.ecomgst);
        ecomva = findViewById(R.id.ecomva);
        ecomgrandtotal = findViewById(R.id.ecomgrandtotal);
        selectfavourite = findViewById(R.id.selectfavourite);

        /*selectfavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   postwishlist();
                postecomfavouritedd(id);
                // postwishlisttt(pid, listmodel.getSum());

            }
        });*/

        selectfavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {

                    //ToDo something
                    postecomfavouritedd(id);

                } else {
                    deletecomfavouritedd(id);
                }


            }
        });
        pimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EcommProductzoom.class);
                intent.putExtra("pid", id);
                startActivity(intent);

            }
        });


        rvsimilarproducts = findViewById(R.id.rvsimilarproducts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
        rvsimilarproducts.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        rvsimilarproducts.setHasFixedSize(true);
        similarList = new ArrayList<>();
        similaradapter = new SimilarproductsAdapter(similarList, getApplicationContext());
        rvsimilarproducts.setAdapter(similaradapter);


        rvreviewproducts = findViewById(R.id.rvreviewproducts);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        rvreviewproducts.setLayoutManager(linearLayoutManager1); // set LayoutManager to RecyclerView
        rvreviewproducts.setHasFixedSize(true);
        reviewList = new ArrayList<>();
        reviewadapter = new Reviewproductadapter(reviewList, getApplicationContext());
        rvreviewproducts.setAdapter(reviewadapter);

        rating = (RatingBar) findViewById(R.id.rating);
        etFeedback = (EditText) findViewById(R.id.et_feedback_form);

        Button btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stForm = etFeedback.getText().toString().trim();
                stRating = (int) rating.getRating();
                if (stForm.isEmpty()) {
                    ToastMessage.onToast(getApplicationContext(), "Please enter feedback form", ToastMessage.ERROR);

                } else if (stRating == 0) {
                    ToastMessage.onToast(getApplicationContext(), "Please rate ", ToastMessage.ERROR);

                } else {
                    sendFeedback();
                }
            }
        });

        relativesubtota = findViewById(R.id.relativesubtota);
        relativeva = findViewById(R.id.relativeva);
        relativegrandtotal = findViewById(R.id.relativegrandtotal);

        relativestone = findViewById(R.id.relativestone);
        txtstoneprice = findViewById(R.id.txtstoneprice);
    }

    public void Customizedesign() {
        BottomSheetDialog dialog = new BottomSheetDialog(Ecommproductdiscreption.this);
        dialog.setContentView(R.layout.productdesc_bottom_dialog);
        imageView_close = dialog.findViewById(R.id.iv_close);
        subcat_nameTV = dialog.findViewById(R.id.subcat_name);
        subcat_priceTV = dialog.findViewById(R.id.subcat_price);
        goldcolortv = dialog.findViewById(R.id.gold_color_tv);
        goldyellowtv = dialog.findViewById(R.id.gold_yellow_tv);
        goldkarat14 = dialog.findViewById(R.id.gold24_karat_tv);
        goldkarat18 = dialog.findViewById(R.id.gold_karat18);
        diamondsiij = dialog.findViewById(R.id.diamond_tv_siij);
        diamondsigh = dialog.findViewById(R.id.diamond_tv_SIGH);
        diamondvsgh = dialog.findViewById(R.id.diamond_tv_VSGH);
        diamondvvsef = dialog.findViewById(R.id.diamond_tv_VVSEF);
        cancelTv = dialog.findViewById(R.id.cancel_tv);
        subcat_priceTV.setText("");
        subcat_nameTV.setText("");

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void sendFeedback() {
        Log.e("pid", "" + id);
        Log.e("stRating", "" + stRating);

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Listmodel> getplans = apiDao.postratingform("Bearer " + AccountUtils.getAccessToken(this), stForm, String.valueOf(stRating), id);

        getplans.enqueue(new Callback<Listmodel>() {
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                Log.e("StatusCodeForm", String.valueOf(statuscode));
                list = response.body();

                if (statuscode == 200 || statuscode == 202) {
                    Log.e("statuscode", String.valueOf(statuscode));
                    rating.setRating(0.0F);
                    etFeedback.setText("");
                    ToastMessage.onToast(getApplicationContext(), "Thanks for your feedback", ToastMessage.SUCCESS);
                } else if (statuscode == 404) {
                    ToastMessage.onToast(getApplicationContext(), "Already the rating has given", ToastMessage.ERROR);

                } else {
                    //ToastMessage.onToast(getApplicationContext(), "Technical Error", ToastMessage.ERROR);

                }

            }

            @Override
            public void onFailure(Call<Listmodel> call, Throwable t) {

                //    ToastMessage.onToast(getApplicationContext(), "We have some issues ", ToastMessage.ERROR);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
        getEcomWishlistCount();
        getEcomProductdisc(id);

    }
    /////addproducttocart/

    private void postEcommaddcartproducts(String cartid) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

        Call<Listmodel> getecomcartlist = apiDao.postEcomcartitem("Bearer " + AccountUtils.getAccessToken(this), cartid, sizeitem);

        getecomcartlist.enqueue(new Callback<Listmodel>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<Listmodel> call, @NotNull Response<Listmodel> response) {
                int statuscode = response.code();
                Log.e("ecompostfav", String.valueOf(statuscode));
                Log.e("ecompostfaverror", String.valueOf(response));
                pflist = response.body();
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                    Intent intent = new Intent(getApplicationContext(), EcommCartActivity.class);
                    startActivity(intent);

                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else if (statuscode == 404) {
                    ToastMessage.onToast(getApplicationContext(), "Already the the product is in your cart", ToastMessage.ERROR);
                } else {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("cartfalse", String.valueOf(t));
            }
        });
    }
    ////////


    @SuppressLint("UseCompatLoadingForDrawables")
    public void postecomfavouritedd(String pid) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        Log.e("token", AccountUtils.getAccessToken(this));
        Log.e("Customerid", "" + AccountUtils.getCustomerID(this));
        Call<Listmodel> postwishlist = apiDao.postfavourites("Bearer " + AccountUtils.getAccessToken(this), pid, sizeitem);
        postwishlist.enqueue(new Callback<Listmodel>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<Listmodel> call, @NotNull Response<Listmodel> response) {
                int statuscode = response.code();
                Log.e("ecompostfav", String.valueOf(statuscode));
                Log.e("ecompostfaverror", String.valueOf(response));
                pflist = response.body();
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                    //   selectfavourite.setImageResource(R.drawable.ic_like_on);
                    if (from.equals("home"))
                        getEcomWishlistCount();
                    Log.e("context", from);
                    Toast.makeText(getApplicationContext(), "Product Added in Favouritelist", Toast.LENGTH_SHORT).show();
                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else if (statuscode == 404) {

                } else {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("ughb", String.valueOf(t));
            }
        });

    }

    public void deletecomfavouritedd(String pid) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        Log.e("token", AccountUtils.getAccessToken(this));
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Listmodel> deletewishlist = apiDao.deletefavourite("Bearer " + AccountUtils.getAccessToken(this), pid);
        deletewishlist.enqueue(new Callback<Listmodel>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<Listmodel> call, @NotNull Response<Listmodel> response) {
                int statuscode = response.code();
                Log.e("poststatuscode", String.valueOf(statuscode));
                Log.e("poststatuscodeerror", String.valueOf(response));
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                    //    selectfavourite.setImageResource(R.drawable.ic_like_offfgt);
                    if (from.equals("home"))
                        getEcomWishlistCount();
                    Log.e("context", from);
                    Toast.makeText(getApplicationContext(), "Product Delete in Favouritelist", Toast.LENGTH_SHORT).show();

                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                    ToastMessage.onToast(getApplicationContext(), "try again", ToastMessage.ERROR);

                } else if (statuscode == 404) {
                    Toast.makeText(getApplicationContext(), "Product is Already in favourites", Toast.LENGTH_SHORT).show();
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    Log.e("cv", String.valueOf(statuscode));
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("ughb", String.valueOf(t));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        getEcomWishlistCount();
        getEcomProductdisc(id);
        getEcomProductRating();

    }

    public void getEcomProductdisc(String getid) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<List<Listmodel>> getProduct = apiDao.getproductid(getid);
        getProduct.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {

                dialog.dismiss();
                int statuscode = response.code();
                Log.e("produenterstatuscode", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("statuscode", String.valueOf(statuscode));
                    if (flist != null) {
                        for (Listmodel listmodel : flist) {
                            spinnerlist.clear();
                            Log.e("pname", "pname");
                            pname.setText(listmodel.getPname());
                            selectname = listmodel.getPname();
                            Log.e("selectname", "selectname" + selectname);


                            if (listmodel.getPname().equals("Gift Card")) {
                                relativesubtota.setVisibility(View.GONE);
                                relativeva.setVisibility(View.GONE);
                                relativegrandtotal.setVisibility(View.GONE);
                            } else {
                                relativesubtota.setVisibility(View.VISIBLE);
                                relativeva.setVisibility(View.VISIBLE);
                                relativegrandtotal.setVisibility(View.VISIBLE);
                            }


                            if (selectname.equals("Gift Card")) {
                                pweight.setVisibility(View.GONE);
                                pva.setVisibility(View.GONE);
                                pdiscreption.setVisibility(View.GONE);
                                selectlist.setVisibility(View.GONE);
                                banglessize_ll.setVisibility(View.GONE);
                                rvsimilarproducts.setVisibility(View.GONE);
                                similarproducts.setVisibility(View.GONE);

                            } else {
                                pweight.setVisibility(View.VISIBLE);
                                pva.setVisibility(View.VISIBLE);
                                pdiscreption.setVisibility(View.VISIBLE);
                                selectlist.setVisibility(View.VISIBLE);
                                banglessize_ll.setVisibility(View.VISIBLE);
                                rvsimilarproducts.setVisibility(View.VISIBLE);
                                similarproducts.setVisibility(View.VISIBLE);


                            }

                            for (int i = 0; i < listmodel.getSizes().size(); i++) {
//                                spinnerlist.clear();
                                Log.e("sizesize", "" + listmodel.getSizes().get(i).getSizes());
                                spinnerlist.add(listmodel.getSizes().get(i).getSizes());
                                Log.e("psizes", "" + listmodel.getSizes().get(i).getSizes());
                                Log.e("sizesize", "" + listmodel.getSizes().get(i).getWeight());
                                selectsize = listmodel.getSizes().get(i).getSizes();
                                Log.e("psizessss", "" + selectsize);
                                selectweight = listmodel.getSizes().get(i).getWeight();
                            }

                            for (int i = 0; i < listmodel.getStones().size(); i++) {
                                Log.e("stonessizesize", "" + listmodel.getStones().get(i).getStoneprice());
                                selectstoneprice = listmodel.getStones().get(i).getStoneprice();
                                stonename.setText("Stone Name    :  " + listmodel.getStones().get(i).getStonename());
                                Log.e("stonename", "" + stonename);
                                stoneprice.setText("Stone Price     : ₹ " + listmodel.getStones().get(i).getStoneprice());
                                Log.e("stonename1", "" + stoneprice);
                                stonecolore.setText("Stone Colour  :  " + listmodel.getStones().get(i).getColor());
                                Log.e("stonename2", "" + stonecolore);

                            }
                            pva.setText("VA         :  " + listmodel.getVa() + " %");
                            pweight.setText("Weight  :  " + listmodel.getWeight() + " grams");
                            pidTv.setText("Id           :  " + listmodel.getId());
                            productprice.setText("₹ " + listmodel.getPrice());
                            ecomsubtotal.setText("₹ " + listmodel.getPrice());
                            ecomva.setText(listmodel.getVa() + " %");
                            pid = listmodel.getPid();
                            pdiscreption.setText(listmodel.getDescription());
                            ecomliveprice.setText("₹ " + listmodel.getProductliveprice() + "/ gram");
                            selectliveprice = listmodel.getProductliveprice();

                            bulltype = listmodel.getTransction_type();
                            Log.e("bulltype", "" + bulltype);
                            //  bulltype.equals("gold") ||
                            if (bulltype.equals("silver")) {
                                banglessize_ll.setVisibility(View.GONE);

                            } else {
                                banglessize_ll.setVisibility(View.VISIBLE);
                            }

                            if (listmodel.getStonename().equals("0")) {
                                selectlist.setVisibility(View.GONE);

                            } else {
                                selectlist.setVisibility(View.VISIBLE);

                            }

                            if (listmodel.getStoneprice().equals("0")) {
                                relativestone.setVisibility(View.GONE);

                            } else {
                                relativestone.setVisibility(View.VISIBLE);
                                txtstoneprice.setText("₹ " + listmodel.getStoneprice());
                            }


                            relativestone = findViewById(R.id.relativestone);
                            txtstoneprice = findViewById(R.id.txtstoneprice);

                            stonename.setText("Stone Name    :  " + listmodel.getStonename());
                            Log.e("stonename", "" + stonename);
                            stoneprice.setText("Stone Price     : ₹ " + listmodel.getStoneprice());
                            Log.e("stonename1", "" + stoneprice);
                            stonecolore.setText("Stone Colour  :  " + listmodel.getStonecolor());
                            Log.e("stonename2", "" + stonecolore);


                            //////////////////////////////////////Sizes in spinner/////////////////////////
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Ecommproductdiscreption.this, android.R.layout.simple_spinner_item, spinnerlist);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerviewBangles.setAdapter(arrayAdapter);
                            spinnerviewBangles.setSelection(arrayAdapter.NO_SELECTION, true); //Add this line before setting listener

                            spinnerviewBangles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                String weight1 = "0";

                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Log.e("sizeitem", "" + sizeitem);
                                    sizeitem = parent.getItemAtPosition(position).toString();

                                    for (int j = 0; j < listmodel.getSizes().size(); j++) {
                                        if (listmodel.getSizes().get(j).getSizes().equals(sizeitem)) {
                                            weight1 = listmodel.getSizes().get(j).getWeight();

                                        }
                                    }

                                    pweight.setText("Weight  :  " + weight1 + " grams");
  /*if (bulltype.equals("gold")) {

                                        Float tempgrandtotal = Float.parseFloat(weight1) * Float.parseFloat(listmodel.getProductliveprice());
                                        Log.e("tempsgoldtotal", "" + tempgrandtotal);

                                        Float tempgrandva = tempgrandtotal * Float.parseFloat(listmodel.getVa()) / 100;
                                        Log.e("tempgoldtotal", "" + tempgrandva);


                                        Float tempgrandgst = ((tempgrandtotal + tempgrandva) * Float.parseFloat(listmodel.getGst()) / 100);
                                        Log.e("tempgoldtotal", "" + tempgrandgst);
                                        Log.e("tempgoldtotal", "" + listmodel.getStoneprice());

                                        Integer finalprice = Math.round(tempgrandtotal + tempgrandva + tempgrandgst + Float.parseFloat(listmodel.getStoneprice()));
                                        Log.e("tempgoldtotal", "" + finalprice);
                                        ecomgrandtotal.setText("₹" + finalprice);

                                    } else if (bulltype.equals("silver")) {
                                        Log.e("tempsgoldtotalweight", "" + weight1);

                                        ecomliveprice.setText("₹" + listmodel.getProductliveprice() + "/kg");

                                        Float tempsilvertotal = Float.parseFloat(weight1) * Float.parseFloat(listmodel.getProductliveprice()) / 1000;

                                        Log.e("tempsilvertotal", "" + tempsilvertotal);

                                        Float tempsilverva = tempsilvertotal * Float.parseFloat(listmodel.getVa()) / 100;
                                        Log.e("tempsilvertotal", "" + tempsilverva);

                                        Float temsilvergst = (tempsilvertotal + tempsilverva) * Float.parseFloat(listmodel.getGst()) / 100;

                                        Log.e("tempsilvertotal", "" + temsilvergst);

                                        Integer finalprice1 = Math.round(tempsilvertotal + tempsilverva + temsilvergst + Float.parseFloat(listmodel.getStoneprice()));
                                        Log.e("tempsilvertotal", "" + finalprice1);

                                        ecomgrandtotal.setText("₹" + finalprice1);
                                        //   productprice.setText("₹ " + finalprice1);
                                    }*/
                                    if (bulltype.equals("gold")) {
                                        Float tempgrandtotal = Float.parseFloat(weight1) * Float.parseFloat(listmodel.getProductliveprice());
                                        Log.e("tempsgoldtotal", "" + tempgrandtotal);
                                        Float tempgrandva = tempgrandtotal * Float.parseFloat(listmodel.getVa()) / 100;
                                        Log.e("tempgoldtotal", "" + tempgrandva);
                                        Float tempgrandgst = ((tempgrandtotal + tempgrandva) * Float.parseFloat(listmodel.getGst()) / 100);
                                        Log.e("tempgoldtotal", "" + tempgrandgst);
                                        Log.e("stoneprice", "" + listmodel.getStoneprice());

                                        // Integer finalprice = Math.round(Float.parseFloat(tempgrandtotal + tempgrandva + tempgrandgst + listmodel.getStoneprice()));
                                        String stonpriceecalculation = listmodel.getStoneprice();
                                        Float a = tempgrandtotal;
                                        Float b = tempgrandva;
                                        Float c = tempgrandgst;
                                        Float d = Float.valueOf(stonpriceecalculation);
                                        Integer finalprice = Math.round(Float.parseFloat(String.valueOf(a + b + c + d)));
                                        Log.e("tempgoldtotal", "" + finalprice);
                                        ecomgrandtotal.setText("₹ " + finalprice);
                                        productprice.setText("₹ " + tempgrandtotal);
                                        ecomsubtotal.setText("₹ " + tempgrandtotal);
                                    } else if (bulltype.equals("silver")) {
                                        Log.e("tempsgoldtotalweight", "" + weight1);
                                        ecomliveprice.setText("₹ " + listmodel.getProductliveprice() + "/kg");
                                        Float tempsilvertotal = Float.parseFloat(weight1) * Float.parseFloat(listmodel.getProductliveprice()) / 1000;
                                        Log.e("tempsilvertotal", "" + tempsilvertotal);
                                        Float tempsilverva = tempsilvertotal * Float.parseFloat(listmodel.getVa()) / 100;
                                        Log.e("tempsilvertotal", "" + tempsilverva);
                                        Float temsilvergst = (tempsilvertotal + tempsilverva) * Float.parseFloat(listmodel.getGst()) / 100;
                                        Log.e("tempsilvertotal", "" + temsilvergst);
                                        //   Integer finalsilverprice1 = Math.round(Float.parseFloat(tempsilvertotal + tempsilverva + temsilvergst + listmodel.getStoneprice()));

                                        String stonpriceecalculation = listmodel.getStoneprice();
                                        Float a = tempsilvertotal;
                                        Float b = tempsilverva;
                                        Float c = temsilvergst;
                                        Float d = Float.valueOf(stonpriceecalculation);

                                        Integer finalsilverprice1 = Math.round(Float.parseFloat(String.valueOf(a + b + c + d)));
                                        Log.e("finalsilverprice1", "" + finalsilverprice1);
                                        ecomgrandtotal.setText("₹ " + finalsilverprice1);
                                        productprice.setText("₹ " + tempsilvertotal);
                                        ecomsubtotal.setText("₹ " + tempsilvertotal);
                                    } else {
                                        Float normalgrandtotal = Float.parseFloat(weight1) * Float.parseFloat(listmodel.getProductliveprice());
                                        Log.e("normalgrandtotal", "" + normalgrandtotal);
                                        Float nornalgrandva = normalgrandtotal * Float.parseFloat(listmodel.getVa()) / 100;
                                        Log.e("nornalgrandva", "" + nornalgrandva);
                                        Float normalgrandgst = ((normalgrandtotal + nornalgrandva) * Float.parseFloat(listmodel.getGst()) / 100);
                                        Log.e("tempgoldtotal", "" + normalgrandgst);

                                        String stonpriceecalculation = listmodel.getStoneprice();
                                        Float a = normalgrandtotal;
                                        Float b = nornalgrandva;
                                        Float c = normalgrandgst;
                                        Float d = Float.valueOf(stonpriceecalculation);

                                        Integer finalprice1 = Math.round(Float.parseFloat(String.valueOf(a + b + c + d)));
                                        Log.e("tempgoldtotalallvalues", "" + finalprice1);

                                        // Integer finalprice1 = Math.round(Float.parseFloat(normalgrandtotal + nornalgrandva + normalgrandgst + stonpriceecalculation));
                                        Log.e("tempsilvertotal", "" + finalprice1);
                                        ecomgrandtotal.setText("₹ " + finalprice1);
                                        Log.e("", "" + normalgrandtotal + "," + nornalgrandva + "," + normalgrandgst + "," + listmodel.getStoneprice());


                                        double asdf2 = Double.parseDouble(String.valueOf(normalgrandtotal));
                                        int ii = (int) asdf2;

                                        productprice.setText("₹ " + ii);
                                        ecomsubtotal.setText("₹ " + ii);


                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            ///////////////////////////////////////////////////////

                            Log.e("silvertotalpricd", "" + listmodel.getTotalprice());
                            //  ecomgrandtotal.setText("₹ " + listmodel.getTotalprice());
                            Log.e("grandtotal", "" + listmodel.getTotalprice());

                            String Cid = listmodel.getSid();
                            Log.e("Cid", "" + Cid);
                            getEcommSimilarProducts(Cid);
                            ratetext.setText("Ratings  :  " + listmodel.getRatings());
                            String fpimg = listmodel.getImage_uri();
                            try {
                                Glide.with(getApplicationContext()).load(fpimg).into(pimg);
                            } catch (Exception ignored) {
                                Glide.with(getApplicationContext()).load(R.drawable.background_image).into(pimg);
                            }
                            Log.e("Cddddid", "" + listmodel.getId());

                            boolean checkwish = checklist.contains(listmodel.getId());
                            if (checkwish) {
                                Log.e("checklist", String.valueOf(checklist));
                                //  selectfavourite.setImageResource(R.drawable.ic_like_on);
                                selectfavourite.setChecked(true);

                            } else {
                                Log.e("checklist", String.valueOf(checklist));
                                selectfavourite.setChecked(false);

                                // selectfavourite.setImageResource(R.drawable.ic_like_offfgt);
                            }
                        }
                    } else {
                        dialog.dismiss();
                        Log.e("catname", "No cats");
                    }
                } else if (statuscode == 404) {
                    dialog.dismiss();

                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
                dialog.dismiss();
            }
        });

    }

    public void getEcommSimilarProducts(String eeeee) {
        similarList.clear();
        Call<List<Listmodel>> getproductsbycat = null;
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        getproductsbycat = apiDao.get_ecomsubcategoryproducts(eeeee);
        getproductsbycat.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("statuscode", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("statuscode", String.valueOf(statuscode));
                    if (flist != null) {
                        similarList.clear();
                        Collections.shuffle(flist);
                        for (Listmodel listmodel : flist) {
                            //cid = listmodel.getId();
                            similarList.add(listmodel);
                            similaradapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("catname", "No cats");
                    }
                } else if (statuscode == 422) {
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
            }
        });
    }

    public void getEcomProductRating() {
        apiDao = ApiClient.getClient("").create(ApiDao.class);
        Call<List<ReviewsModel>> getProduct = apiDao.getratings(AccountUtils.getAccessToken(this), id);
        getProduct.enqueue(new Callback<List<ReviewsModel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NotNull Call<List<ReviewsModel>> call, @NotNull Response<List<ReviewsModel>> response) {
                int statuscode = response.code();
                Log.e("produenterstatuscode", String.valueOf(statuscode));
                rflist = response.body();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("statuscode", String.valueOf(statuscode));
                    try {
                        if (!flist.isEmpty()) {
                            reviewList.clear();
                            for (ReviewsModel listmodel : rflist) {
                                reviewproducts.setVisibility(View.VISIBLE);
                                reviewList.add(listmodel);
                                reviewadapter.notifyDataSetChanged();
                            }
                        } else {
                            reviewproducts.setVisibility(View.GONE);

                            Log.e("catname", "No cats");
                        }
                    } catch (Exception e) {
                        // Toast.makeText(getApplicationContext(), "Try again later", Toast.LENGTH_SHORT).show();
                    }

                } else if (statuscode == 404) {

                    Log.e("cv", String.valueOf(statuscode));
                } else {
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ReviewsModel>> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public class SimilarproductsAdapter extends RecyclerView.Adapter<SimilarproductsAdapter.ViewHolder> {


        private Context context;
        ArrayList<Listmodel> list;
        OnItemClickListener itemClickListener;

        public SimilarproductsAdapter(ArrayList<Listmodel> list, Context context) {
            this.context = context;
            this.list = list;
            this.itemClickListener = itemClickListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.ecomsimilarproductlist, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            Listmodel listmodel = list.get(position);
            String name = listmodel.getPname();
            String image = listmodel.getImage_uri();
            String rupee = listmodel.getPrice();
            String idddd = listmodel.getId();

            double x = Double.parseDouble(rupee);
            double y = Math.floor(x * 100) / 100;

       /* double asdf2 = Double.parseDouble(rupee);
        int i = (int) asdf2;*/
            holder.simiprice.setText("₹ " + rupee);


            holder.tv_category.setText(name);
            Log.e("iddddddddd", "" + idddd);
            Log.e("ddddddm", "" + listmodel.getPimg());

            try {
                Glide.with(context).load(image).into(holder.iv_categoryimg);
            } catch (Exception ignored) {
                Glide.with(context).load(R.drawable.background_image).into(holder.iv_categoryimg);
            }
            if (selectedPosition == position) {
                Log.e("ifnews", "called");
            } else {
                Log.e("elsenews", "called");
            }
            holder.cardsimilar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedPosition != position) {
                        selectedPosition = position;
                        notifyDataSetChanged();
                    //    getEcomProductdisc(listmodel.getId());

                      /*  text = viewHolder.giftnamename.getText().toString();
                        etcouponcoode.setText(text);
                        couponammount = coupncod;
                        postGiftCardCode(text);*/

                    } else {

                        selectedPosition = -1;
                        notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView iv_categoryimg;
            TextView tv_category, simiprice;
            CardView cardsimilar;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_category = itemView.findViewById(R.id.newarrivalname);
                simiprice = itemView.findViewById(R.id.simiprice);
                cardsimilar = itemView.findViewById(R.id.cardsimilar);
                iv_categoryimg = itemView.findViewById(R.id.newarrivalimg);
                //   iv_categoryimg.setOnClickListener(this);
                //  itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                //   itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}