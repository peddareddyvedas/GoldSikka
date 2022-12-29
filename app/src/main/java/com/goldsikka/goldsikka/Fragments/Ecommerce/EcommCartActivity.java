package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Activitys.LoginActivity;
import com.goldsikka.goldsikka.Activitys.OTPActivity;
import com.goldsikka.goldsikka.Activitys.Verifyaccount_Class;
import com.goldsikka.goldsikka.Directory.GiftCardActivity;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.jetbrains.annotations.NotNull;
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

public class EcommCartActivity extends AppCompatActivity {
    RecyclerView cartRecyclerview;
    EcommCartAdapter cartAdapter;
    TextView unameTv, uidTv, titleTv, validprice;
    RelativeLayout backbtn;
    shared_preference sharedPreference;
    int selectedPosition = -1;
    List<Listmodel> flist;
    ApiDao apiDao;
    TextView notxt;

    ArrayList<Listmodel> cartList;
    String pidid;
    Bundle bundle;
    String titleprice;
    String cartid = "99";
    Button checkout;
    TextView walletbalace, carttotal;
    RelativeLayout notfound;
    SwipeRefreshLayout swipe_layout;
    TextView tvofflineapply;
    EditText etcouponcoode;
    String couponcode = "";
    String couponid = "";

    String giftAmount = "";
    LinearLayout etcoupon;
    int wcount = 0;
    TextView giftcount, remove, addgift;
    LinearLayout llopencoupon;
    public static boolean isFromcart = false;

    String totalamountdect = "";
    String couponAmount = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecomm_cartpage);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            couponid = bundle.getString("ecommcouponid");
            couponcode = bundle.getString("ecommcouponcode");
            giftAmount = bundle.getString("ecomcouponprice");

        }
        //   couponcode = AccountUtils.getIsGiftCard(getApplicationContext());
        init();
        getEcommcartproducts();
        wallet_amount();
        onrefresh();
    }


    public void init() {
        sharedPreference = new shared_preference(getApplicationContext());
        swipe_layout = findViewById(R.id.swipe_layout);
        notfound = findViewById(R.id.notfound);
        giftcount = findViewById(R.id.giftcount);
        llopencoupon = findViewById(R.id.llopencoupon);
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Cart");
        notxt = findViewById(R.id.notxt);
        cartRecyclerview = findViewById(R.id.recyclercart);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        cartRecyclerview.setLayoutManager(linearLayoutManager2); // set LayoutManager to RecyclerView
        cartRecyclerview.setHasFixedSize(true);
        cartList = new ArrayList<>();
        cartAdapter = new EcommCartAdapter(cartList);
        cartRecyclerview.setAdapter(cartAdapter);
        checkout = findViewById(R.id.checkout);
        walletbalace = findViewById(R.id.walletbalace);
        carttotal = findViewById(R.id.carttotal);
        remove = findViewById(R.id.remove);
        addgift = findViewById(R.id.addgift);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EcommCheckout_Activity.class);
                i.putExtra("couponcode", couponid);
                i.putExtra("getcouponamout", giftAmount);
                startActivity(i);
                //startActivity( new Intent( EcommCartActivity.this, EcommCheckout_Activity.class ) );
                finish();
            }
        });
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        etcouponcoode = findViewById(R.id.etcouponcoode);

        llopencoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        addgift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFromcart) {
                    Log.e("isFromcartactivity", "call" + isFromcart);
                    Intent intent = new Intent(getApplicationContext(), GiftCardActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), GiftCardActivity.class);
                    isFromcart = true;
                    startActivity(intent);
                }


            }
        });
        Log.e("getgiftcardcart", "" + AccountUtils.getIsGiftCard(getApplicationContext()));

        if (couponcode.equals("")) {
            giftcount.setText("Apply Gift Card");
        } else {
            giftcount.setText("Gift Card Code: " + AccountUtils.getIsGiftCard(getApplicationContext()));
            remove.setVisibility(View.VISIBLE);
            addgift.setVisibility(View.GONE);
        }
        etcoupon = findViewById(R.id.etcoupon);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeGiftCardCode(couponcode);

            }
        });
        getGiftCardAmountDecect(couponcode);

    }

    public void removeGiftCardCode(String code) {
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
            Call<Listmodel> getdetails = apiDao.getecomupdategiftcart("Bearer " + AccountUtils.getAccessToken(this), code);
            getdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    String error = response.message();
                    Log.e("statuscode", String.valueOf(statuscode));

                    if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                        //    selectfavourite.setImageResource(R.drawable.ic_like_offfgt);
                        Listmodel list = response.body();

                        //  couponid = list.getId();
                        dialog.dismiss();
                        ToastMessage.onToast(EcommCartActivity.this, "GiftCode Remove Succesfully", ToastMessage.SUCCESS);
                        remove.setVisibility(View.GONE);
                        addgift.setVisibility(View.VISIBLE);
                        giftcount.setText("Apply Gift Card");
                        remove.setVisibility(View.GONE);
                        addgift.setVisibility(View.VISIBLE);
                        carttotal.setText("₹ " + totalamountdect);

                        couponid = "";
                        giftAmount = "";
                        finish();
                    } else if (statuscode == 422) {

                        try {
                            if (null != response.errorBody()) {
                                String errorResponse = response.errorBody().string();
                                Log.e("bstatus", " " + errorResponse);
                                JSONObject jObjError = new JSONObject(errorResponse);
                                String st = jObjError.getString("message");
                                Log.e("bstatus", " " + st);
                                ToastMessage.onToast(getApplicationContext(), st, ToastMessage.ERROR);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        //   Log.e("cv", String.valueOf(statuscode));
                        //    ToastMessage.onToast(getApplicationContext(), "All ready user this gift code", ToastMessage.ERROR);

                    } else {
                        dialog.dismiss();
                    }

                    /*if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {
                        Listmodel list = response.body();

                        couponid = list.getId();
                        dialog.dismiss();
                        ToastMessage.onToast(EcommCartActivity.this, error, ToastMessage.ERROR);

                    } else {
                        dialog.dismiss();
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            JSONObject er = jObjError.getJSONObject("errors");
                            ToastMessage.onToast(EcommCartActivity.this, st, ToastMessage.ERROR);


                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }*/

                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                    //   ToastMessage.onToast(getApplicationContext(), "We have some issue", ToastMessage.ERROR);
                }
            });
        }
    }

    public void removeGiftCardCodeback(String code) {

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getdetails = apiDao.getecomupdategiftcart("Bearer " + AccountUtils.getAccessToken(this), code);
            getdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    String error = response.message();
                    Log.e("statuscode", String.valueOf(statuscode));

                    if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                        Listmodel list = response.body();

                        couponid = list.getId();
                        //   ToastMessage.onToast(EcommCartActivity.this, "Gift Code Added Succesfully", ToastMessage.SUCCESS);
                        remove.setVisibility(View.GONE);
                        addgift.setVisibility(View.VISIBLE);
                        giftcount.setText("Apply Gift Card");
                        remove.setVisibility(View.GONE);
                        addgift.setVisibility(View.VISIBLE);
                        // carttotal.setText("₹ " + totalamountdect);

                    } else if (statuscode == 422) {
                       /*try {
                            if (null != response.errorBody()) {
                                String errorResponse = response.errorBody().string();
                                Log.e("bstatus", " " + errorResponse);
                                JSONObject jObjError = new JSONObject(errorResponse);
                                String st = jObjError.getString("message");
                                Log.e("bstatus", " " + st);
                                ToastMessage.onToast(getApplicationContext(), st, ToastMessage.ERROR);


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/

                    } else {
                    }

                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    //   ToastMessage.onToast(getApplicationContext(), "We have some issue", ToastMessage.ERROR);
                }
            });
        }
    }

    public void wallet_amount() {
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
            Call<Listmodel> getdetails = apiDao.walletAmount("Bearer " + AccountUtils.getAccessToken(this));
            getdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("statuscode dd", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        Listmodel list = response.body();
                        String Walletamount = list.getAmount_wallet();
                        walletbalace.setText("Account Balance : " + getString(R.string.Rs) + " " + Walletamount);
                        dialog.dismiss();

                    } else {

                        dialog.dismiss();

                        //    ToastMessage.onToast(getApplicationContext(), "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                    //   ToastMessage.onToast(getApplicationContext(), "We have some issue", ToastMessage.ERROR);
                }
            });
        }

    }

    public void onrefresh() {
        swipe_layout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipe_layout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(EcommCartActivity.this)) {
                            ToastMessage.onToast(EcommCartActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            swipe_layout.setRefreshing(false);
                            return;
                        } else {
                            init();
                            onrefresh();
                            wallet_amount();
                            getEcommcartproducts();
                        }
                        swipe_layout.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }

    private void getEcommcartproducts() {
        cartList.clear();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<List<Listmodel>> getecomcartlist = apiDao.getEcomcartitem("Bearer " + AccountUtils.getAccessToken(this));
        getecomcartlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("getcartstatuscodess", String.valueOf(statuscode));
                flist = response.body();
                Log.e("result", "" + response.message());
                if (statuscode == 200 || statuscode == 202) {
                    Log.e("gettcartstatuscode111", String.valueOf(statuscode));
                    //   String responseRecieved = response.body().string();
                    if (flist != null) {
                        cartList.clear();
                        for (Listmodel listmodel : flist) {
                            pidid = listmodel.getId();
                            cartList.add(listmodel);
                            cartAdapter.notifyDataSetChanged();
                            notfound.setVisibility(View.GONE);
                            checkout.setClickable(true);
                        }
                    } else {
                        checkout.setClickable(false);
                        // carttotal.setText(" ");
                        carttotal.setText("₹ " + "0");

                        notfound.setVisibility(View.VISIBLE);
                    }

                } else if (statuscode == 404) {
                    notfound.setVisibility(View.VISIBLE);
                    checkout.setClickable(false);
                    //  carttotal.setText(" ");
                    carttotal.setText("₹ " + "0");

                    Log.e("400statuscaod", "" + statuscode);

                } else if (statuscode == 422) {
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                Log.e("cartfailcall", String.valueOf(t));
                /*notfound.setVisibility(View.VISIBLE);
                checkout.setClickable(false);
                remove.setVisibility(View.GONE);
                addgift.setVisibility(View.VISIBLE);
                giftcount.setText("Apply Gift Card");
                remove.setVisibility(View.GONE);
                addgift.setVisibility(View.VISIBLE);
                AccountUtils.setIsGiftCard(getApplicationContext(), "");*/
                llopencoupon.setVisibility(View.GONE);
                //removeGiftCardCodeback(couponcode);
                carttotal.setText("₹ " + "0");
                notfound.setVisibility(View.VISIBLE);
                checkout.setClickable(false);


            }
        });
    }

    @Override
    public void onBackPressed() {
        // NavUtils.navigateUpFromSameTask(this);
        removeGiftCardCodeback(couponcode);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onrefresh();
        wallet_amount();
        carttotal.setText("₹ " + totalamountdect);
        getEcommcartproducts();
    }

    //// ecomcart adapter/////
    private class EcommCartAdapter extends RecyclerView.Adapter<EcommCartAdapter.ViewHolder> {
        private ArrayList<Listmodel> cartList;

        public EcommCartAdapter(ArrayList<Listmodel> moviesList) {
            this.cartList = moviesList;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ecomcartlist, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
            Log.e("itemName", " " + position);
            Listmodel w = cartList.get(position);
            viewHolder.subcatname.setText(w.getSubcatname());
            viewHolder.cartpname.setText(w.getPname());
            /*double asdf2 = Double.parseDouble(w.getPrice());
            int ii = (int) asdf2;*/
            viewHolder.price.setText("₹ " + w.getTotalprice());
            viewHolder.weight.setText(w.getWeight() + " gms");
            String pimg = w.getImage_uri();
            viewHolder.pid = w.getPids();
            Log.e("totalprice", "" + w.getTotalprice());

            if (w.getSubcatname().equals("Gift Card")) {
                llopencoupon.setVisibility(View.GONE);
                viewHolder.weight.setVisibility(View.GONE);
                viewHolder.cartpname.setVisibility(View.GONE);
                viewHolder.cartaddtofav.setVisibility(View.GONE);
                viewHolder.lineee.setVisibility(View.GONE);
            } else {
                llopencoupon.setVisibility(View.VISIBLE);
                viewHolder.weight.setVisibility(View.VISIBLE);
                viewHolder.cartpname.setVisibility(View.VISIBLE);
                viewHolder.cartaddtofav.setVisibility(View.VISIBLE);
                viewHolder.lineee.setVisibility(View.VISIBLE);
            }


            int total_sum = 0;
            try {
                for (int i = 0; i < flist.size(); i++) {
                    double asdf = Double.parseDouble(flist.get(i).getTotalprice());
                    int valuedded = (int) asdf;
                    Log.e("flstprice", "" + valuedded);
                    int price = Integer.parseInt(String.valueOf(valuedded));
                    Log.e("countprice", "" + price);
                    total_sum += price;
                }
                Log.e("total_sum", "" + total_sum);
                totalamountdect = String.valueOf(total_sum);
                Log.e("totalamountdect", "" + totalamountdect);
                if (couponAmount.equals("")) {
                    carttotal.setText("₹ " + total_sum);

                } else {
                    Log.e("couponAmount", "" + couponAmount);
                    int final_ = Integer.parseInt(totalamountdect) - Integer.parseInt(couponAmount);
                    Log.e("totalfinal", "" + final_);
                    carttotal.setText("₹ " + final_);
                }


            } catch (Exception e) {
                carttotal.setText("₹ " + w.getTotalprice());
                totalamountdect = w.getTotalprice();
                Log.e("totalamountdect", "" + totalamountdect);
            }
            try {
                Glide.with(getApplicationContext()).load(pimg).into(viewHolder.cartpimage);
            } catch (Exception ignored) {
                Glide.with(getApplicationContext()).load(R.drawable.background_image).into(viewHolder.cartpimage);
            }

            if (selectedPosition == position) {
                Log.e("ifnews", "called");
            } else {
                Log.e("elsenews", "called");
            }
            viewHolder.cartremove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteproductfromcart(w.getPid());
                }
            });
            viewHolder.cartaddtofav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postproductfromcarttofavourit(w.getId(), w.getSize());
                }
            });
        }

        @Override
        public int getItemCount() {
            return cartList.size();
            /*if (cartList.size() > 0) {
                checkout.setClickable(true);
                return cartList.size();
            } else {
                removeGiftCardCode(couponcode);
                checkout.setClickable(false);
                return 0;
            }*/
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView cartpname, cartremove, cartaddtofav, subcatname, price, weight;
            ImageView cartpimage;
            String pid = "24";
            TextView lineee;
            CardView cardunderproduct;

            @SuppressLint("SetTextI18n")
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                cartpname = itemView.findViewById(R.id.cartpname);
                cartaddtofav = itemView.findViewById(R.id.cartaddtofav);
                cartpimage = itemView.findViewById(R.id.cartpimage);
                cartremove = itemView.findViewById(R.id.cartremove);
                subcatname = itemView.findViewById(R.id.subcatname);
                price = itemView.findViewById(R.id.price);
                weight = itemView.findViewById(R.id.weight);
                lineee = itemView.findViewById(R.id.lineee);
            }
        }

    }

    public void deleteproductfromcart(String pid) {
        Log.e("token", AccountUtils.getAccessToken(EcommCartActivity.this));
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(EcommCartActivity.this)).create(ApiDao.class);

        Call<Listmodel> deletewishlist = apiDao.deletecommcartproduct("Bearer " + AccountUtils.getAccessToken(EcommCartActivity.this), pid);
        deletewishlist.enqueue(new Callback<Listmodel>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<Listmodel> call, @NotNull Response<Listmodel> response) {
                int statuscode = response.code();
                Log.e("delstatuscode", String.valueOf(statuscode));
                Log.e("delstatuscodeerror", String.valueOf(response));
                if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                    cartRecyclerview.getRecycledViewPool().clear();
                    cartAdapter.notifyDataSetChanged();
                    getEcommcartproducts();
                } else if (statuscode == 422) {
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    Log.e("cv", String.valueOf(statuscode));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
            }
        });
    }

    public void postproductfromcarttofavourit(String id, String size) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        Log.e("token", AccountUtils.getAccessToken(EcommCartActivity.this));
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(EcommCartActivity.this)).create(ApiDao.class);
        Call<Listmodel> deletewishlist = apiDao.postEcomcartitemtofavourite("Bearer " + AccountUtils.getAccessToken(EcommCartActivity.this), id, size);
        deletewishlist.enqueue(new Callback<Listmodel>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<Listmodel> call, @NotNull Response<Listmodel> response) {
                int statuscode = response.code();
                Log.e("postfavcode", String.valueOf(statuscode));
                Log.e("postfavcodeerror", String.valueOf(response));
                cartRecyclerview.setVisibility(View.VISIBLE);
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                    cartRecyclerview.getRecycledViewPool().clear();
                    cartAdapter.notifyDataSetChanged();
                    getEcommcartproducts();
                    Intent i = new Intent(getApplicationContext(), Ecomfavouritiesactivity.class);
                    startActivity(i);


                } else if (statuscode == 404) {
                    dialog.dismiss();

                    /*Toast.makeText(getApplicationContext(), "Product is Already in favourites list", Toast.LENGTH_SHORT).show();
                    Log.e("cv", String.valueOf(statuscode));*/

                    try {
                        if (null != response.errorBody()) {
                            String errorResponse = response.errorBody().string();
                            Log.e("bstatus", " " + errorResponse);
                            JSONObject jObjError = new JSONObject(errorResponse);
                            String st = jObjError.getString("message");
                            Log.e("bstatus", " " + st);
                            ToastMessage.onToast(getApplicationContext(), st, ToastMessage.ERROR);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    dialog.dismiss();

                    Log.e("cv", String.valueOf(statuscode));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
                dialog.dismiss();

            }
        });
    }


    private void getGiftCardAmountDecect(String editcouponid) {
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Listmodel> getProduct = apiDao.getEcomTotalGiftCardsAmountdecut("Bearer " + AccountUtils.getAccessToken(this), editcouponid);
        getProduct.enqueue(new Callback<Listmodel>() {
            String abc = "sddv";

            @SuppressLint({"LongLogTag", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                int statuscode = response.code();
                String error = response.message();
                Log.e("applycouponstatusc", "" + String.valueOf(statuscode));
                if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                    Listmodel list = response.body();

                    couponAmount = list.getParchase_amount();
                    Log.e("couponAmount", "" + couponAmount);


                } else if (statuscode == 400) {
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                }
            }

            @Override
            public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
                Log.e("ughb", String.valueOf(t));
            }
        });


    }


    /*private void getEcommgiftcart() {
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<List<Listmodel>> getecomcartlist = apiDao.getEcomgiftcart("Bearer " + AccountUtils.getAccessToken(this));
        getecomcartlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("cartstatuscodess", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 202) {
                    if (!flist.isEmpty()) {
                        wcount = flist.size();
                        Log.e("count", String.valueOf(wcount));
                        if (wcount == 0)
                            giftcount.setText("You have 0 Gift Card");
                        else
                            giftcount.setText("You have " + String.valueOf(wcount) + " Gift Card");

                    } else {
                        Log.e("countb1", String.valueOf(wcount));

                        wcount = 0;
                    }
                } else if (statuscode == 404) {
                    Log.e("400statuscaod", "" + statuscode);
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
    }*/

}
