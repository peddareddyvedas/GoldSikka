package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Activitys.Elevenplus_Jewellery;
import com.goldsikka.goldsikka.Activitys.LoginActivity;
import com.goldsikka.goldsikka.Activitys.PaymentError;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.CheckoutModel;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Header;

public class EcommCheckout_Activity extends AppCompatActivity implements PaymentResultListener {


    RecyclerView checkoutRecyclerview;
    CheckoutAdapter checkAdapter;
    TextView unameTv, uidTv, titleTv, validprice;
    RelativeLayout backbtn;
    shared_preference sharedPreference;
    int selectedPosition = -1;
    List<Listmodel> flist;
    ApiDao apiDao;
    TextView notxt;
    ArrayList<CheckoutModel> checkoutList;
    String pidid;
    Bundle bundle;
    String titleprice;
    String cartid = "99";
    Button proceedPay;
    TextView ecomsubtotal, ecomgst, ecomva, ecomgrandtotal;


    CheckoutModel item;

    // ArrayList<checkout> subcatList;

    JSONObject object, object1, jsonObj;

    String totalpayableamount = "0";
    String liveprice = "";
    String noofproducts = "";
    String pid = "";
    String pidvalue;
    String productscount;
    String size = "";

    String stonegetid = "";


    //////
    String stamount = " ", taxPercentage, stgold, status;
    String stcouponcode = "null";
    String stCoupongold;
    String streffercode;
    boolean payment_status;
    String paybleamount, sttransactionid, stdescription;
    double finalcaluclation;
    String getcouponamout = "";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_paybleamount)
    TextView tv_payble_amount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_wallet_money)
    TextView tvwalletamount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_wallet_money)
    EditText et_wallet_money;

    TextWatcher textWatcher;
    String Walletamount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.wallet_check)
    CheckBox cb_wallet;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_wallet_error)
    TextView tv_wallet_error;


    GifImageView loading_gif;

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tv_second_title)
    TextView tv_second_title;

    CountDownTimer countDownTimer;
    private boolean timerRunning;
    String after_dedcation = "null";
    boolean isCoupon;
    String amountvalue = "0";
    boolean isCheck = false;

    Button emi;
    Button subscribebtn;
    ImageView imageView_close;
    TextView ecomsubtotaldiv, ecomitemtotalamt, ecommonthlyamt, ecomsubtotalpay, recomendtotalamt, ecomgiftamount;

    String liveprice22, liveprice24, silverliveprice;
    RelativeLayout relativesubtotal, relativegst, relativecardamount;
    String pigdsize = "";
    String CouponCodeid;
    String giftcouponcode = "";
    String sstoneid = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecomm_checkout_activity);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();

        if (bundle != null) {
            getcouponamout = bundle.getString("getcouponamout");
            CouponCodeid = bundle.getString("couponcode");
            Log.e("CouponCodeid", "" + CouponCodeid);
            Log.e("getcouponamout", "" + getcouponamout);
        }

        //  titleprice = bundle.getString("getprice");
        // cartid = bundle.getString("id");

        //  Log.e("checkoutcode", "" + AccountUtils.getIsGiftCard(getApplicationContext()));
        //Log.e("cartid", "" + cartid);
        init();
        //getEcommCheckoutproducts();
        Log.e("checkoutcode", "" + AccountUtils.getIsGiftCard(getApplicationContext()));

        if (CouponCodeid.equals("")) {
            getEcommCheckoutproducts();
        } else {
            getEcommCheckoutproductswithid(CouponCodeid);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init() {
        sharedPreference = new shared_preference(getApplicationContext());
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Checkout");
        notxt = findViewById(R.id.notxt);

        ecomsubtotal = findViewById(R.id.ecomsubtotal);
        ecomgst = findViewById(R.id.ecomgst);
        ecomva = findViewById(R.id.ecomva);
        ecomgrandtotal = findViewById(R.id.ecomgrandtotal);
        ecomgiftamount = findViewById(R.id.ecomgiftamount);

        relativesubtotal = findViewById(R.id.relativesubtotal);
        relativegst = findViewById(R.id.relativegst);
        relativecardamount = findViewById(R.id.relativecardamount);
        checkoutRecyclerview = findViewById(R.id.recyclercart);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        checkoutRecyclerview.setLayoutManager(linearLayoutManager2); // set LayoutManager to RecyclerView
        checkoutRecyclerview.setHasFixedSize(true);
        checkoutList = new ArrayList<CheckoutModel>();
        checkAdapter = new CheckoutAdapter(checkoutList);
        checkoutRecyclerview.setAdapter(checkAdapter);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loading_gif = findViewById(R.id.loading_gif);
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        emi = findViewById(R.id.emi);
        emi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialog();
            }
        });

        proceedPay = findViewById(R.id.proceedcheckout);
        proceedPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //  onpaymentsucess();
            /*    Intent i = new Intent(getApplicationContext(), EcommPaymentActivity.class);
                i.putExtra("ecommtotalprice", totalpayableamount);
                i.putExtra("liveprice", liveprice);
                i.putExtra("no_of_products", noofproducts);
                i.putExtra("pid", pidvalue);
                i.putExtra("productscount", productscount);
                startActivity(i);*/
                //  liveprice=

                Timer timer = new Timer();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {

                            Thread.sleep(500);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                if (!NetworkUtils.isConnected(EcommCheckout_Activity.this)) {
                                    proceedPay.setVisibility(View.VISIBLE);
                                    loading_gif.setVisibility(View.GONE);
                                    ToastMessage.onToast(EcommCheckout_Activity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                                } else {
                                    openPayment();
                                    proceedPay.setVisibility(View.VISIBLE);
                                    loading_gif.setVisibility(View.GONE);
                                }

                            }

                        });

                    }

                }, 500);
            }

        });

       /* String ssss = et_wallet_money.getText().toString().trim();
        if (ssss.isEmpty()) {
            ToastMessage.onToast(getApplicationContext(), "Please enter amount", ToastMessage.ERROR);
        } else { }*/

        wallet_amount();
        init_timer();


        et_wallet_money.setHint(Html.fromHtml(getString(R.string.wallet_hint)));

        cb_wallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                 @RequiresApi(api = Build.VERSION_CODES.M)
                                                 @Override
                                                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                     if (isChecked) {
                                                         et_wallet_money.setVisibility(View.VISIBLE);
                                                         // et_wallet_money.setText("");
                                                         et_wallet_money.setText(paybleamount);

                                                         isCheck = true;
                                                     } else {
                                                         proceedPay.setEnabled(true);
                                                         isCheck = false;
                                                         et_wallet_money.setText("");
                                                         et_wallet_money.setVisibility(View.GONE);
                                                         tv_wallet_error.setVisibility(View.GONE);
                                                         setdata();
                                                     }
                                                 }
                                             }
        );

    }

    @SuppressLint("SetTextI18n")
    public void opendialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setCancelable(false);
        View bottomSheet = LayoutInflater.from(this)
                .inflate(R.layout.addtoschemepopup, findViewById(R.id.bottomsheet));
        bottomSheetDialog.setContentView(bottomSheet);
        ((View) bottomSheet.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        subscribebtn = bottomSheet.findViewById(R.id.btn_subscribe);
        imageView_close = bottomSheet.findViewById(R.id.img_close);
        ecomsubtotal = bottomSheet.findViewById(R.id.ecomsubtotal);
        ecomsubtotaldiv = bottomSheet.findViewById(R.id.ecomsubtotaldiv);
        ecomitemtotalamt = bottomSheet.findViewById(R.id.ecomitemtotalamt);
        ecommonthlyamt = bottomSheet.findViewById(R.id.ecommonthlyamt);
        ecomsubtotalpay = bottomSheet.findViewById(R.id.ecomsubtotalpay);
        ecomgst = bottomSheet.findViewById(R.id.ecomgst);
        ecomgrandtotal = bottomSheet.findViewById(R.id.ecomgrandtotal);
        recomendtotalamt = bottomSheet.findViewById(R.id.recomendtotalamt);

        try {
            ecomitemtotalamt.setText("₹" + object.getString("payable_amount"));
            String s = totalpayableamount;
            Log.e("divisionamount", "" + s);

            Float i = Float.valueOf(s);
            Float ii = i / 12;
            Float iii = ii * 11;
            Float iiii = ii + iii;
            Log.e("divisionamount", "" + ii);

            recomendtotalamt.setText("₹" + new DecimalFormat("####.##").format(ii));
            ecommonthlyamt.setText("₹" + new DecimalFormat("####.##").format(ii));
            ecomsubtotalpay.setText("₹" + new DecimalFormat("####.##").format(iii));
            ecomgst.setText("₹" + new DecimalFormat("####.##").format(ii));
            ecomsubtotaldiv.setText("₹" + new DecimalFormat("####.##").format(ii) + getString(R.string.addprice));
            ecomgrandtotal.setText("₹" + new DecimalFormat("####.##").format(iiii));

//            ecomsubtotaldiv.setText( "₹" + ii );
//            ecomgst.setText( "₹" + ii );

        } catch (JSONException e) {
            e.printStackTrace();
        }


        subscribebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Elevenplus_Jewellery.class);
                intent.putExtra("schemename", "GOLD PLUS PLAN");
                intent.putExtra("id", "2");
                AccountUtils.setSchemeIDD(getApplicationContext(), "2");
                AccountUtils.setSchemenamee(getApplicationContext(),"GOLD PLUS PLAN");

                startActivity(intent);

            }
        });


        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }

    private void getEcommCheckoutproducts() {

        checkoutList.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Object> getecomcartlist = apiDao.getEcommcheckout("Bearer " + AccountUtils.getAccessToken(this));

        getecomcartlist.enqueue(new Callback<Object>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {
                int statuscode = response.code();
                Log.e("checkoutstatuscodess", String.valueOf(statuscode));
                Object flist = response.body();
                Object flist1 = new Object();
                Log.e("flistdatatotalcount", "" + flist);
                Log.e("flistdatatotalcount", "" + flist);

                // Log.e("object", "" + object);
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 202) {
                    Log.e("checkstatuscode", String.valueOf(statuscode));
                    checkoutList.clear();
                    try {

                        object = new JSONObject(new Gson().toJson(flist));
                        object1 = object.getJSONObject("0");
                        Log.e("object1", "" + object1.getString("pname"));
                        Log.e("objlength", "" + object.length());


                        totalpayableamount = object.getString("payable_amount");
                        ecomsubtotal.setText("₹" + object.getString("cartprice"));
                        ecomgst.setText("₹" + object.getString("totalgstprice"));
                        String giftamount = object.getString("GiftAmount");
                        Log.e("giftamount", "" + giftamount);
                        ecomgrandtotal.setText("₹" + totalpayableamount);
                        productscount = object.getString("productscount");
                        int length = object.length();
                        Log.e("objlength", "" + object.length());
                        for (int index = 0; index < length; index++) {
                            jsonObj = object.getJSONObject(String.valueOf(index));
                            Log.e("ggggg", "" + object.getString(String.valueOf(index)));
                            item = new CheckoutModel();
                            Log.e("checkme", jsonObj.getString("pname"));
                            item.setUser_id(Float.parseFloat(jsonObj.getString("user_id")));
                            item.setPname(jsonObj.getString("pname"));
                            item.setDescription(jsonObj.getString("description"));
                            item.setWeight(jsonObj.getString("weight"));
                            item.setImage_uri(jsonObj.getString("image_uri"));
                            item.setVa_price(Float.parseFloat(jsonObj.getString("vaprice")));
                            item.setVa(jsonObj.getString("va"));
                            item.setTotal_price(Float.parseFloat(jsonObj.getString("totalprice")));
                            item.setPrice(Float.parseFloat(jsonObj.getString("price")));
                            item.setPid(String.valueOf(jsonObj.getInt("pid")));
                            item.setSize(String.valueOf(jsonObj.getInt("size")));


                            item.setStoneid(String.valueOf(jsonObj.getInt("stoneid")));
                            stonegetid = (String.valueOf(jsonObj.getInt("stoneid")));
                            Log.e("stonegetid", "" + stonegetid);


                            liveprice = jsonObj.getString("liveprice");
                            Log.e("getliveprice", "" + liveprice);
                            noofproducts = "1";
                            pid = String.valueOf(jsonObj.getInt("pid"));
                            Log.e("getpid", "" + pid);


                            size = String.valueOf(jsonObj.getInt("size"));
                            liveprice22 = String.valueOf(jsonObj.getInt("liveprice22"));
                            liveprice24 = String.valueOf(jsonObj.getInt("liveprice24"));
                            silverliveprice = String.valueOf(jsonObj.getInt("silverprice"));

                            Log.e("silverliveprice", "" + silverliveprice);
                            Log.e("liveprice22", "" + liveprice22);
                            Log.e("liveprice24", "" + liveprice24);

                            checkoutList.add(item);
                            Log.e("checkoutlistdata", "" + checkoutList.size());
                            checkAdapter.notifyDataSetChanged();
                            setdata();
                        }

                    } catch (JSONException e) {
                        Log.e("throwsme", "" + e);
                        e.printStackTrace();
                    }

                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("jhguygh", String.valueOf(t));
            }
        });
    }

    private void getEcommCheckoutproductswithid(String giftcardid) {

        checkoutList.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<Object> getecomcartlist = apiDao.getEcommcheckoutwitcardis("Bearer " + AccountUtils.getAccessToken(this), giftcardid);

        getecomcartlist.enqueue(new Callback<Object>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {
                int statuscode = response.code();
                Log.e("giftcardcheckout", String.valueOf(statuscode));
                Object flist = response.body();
                Object flist1 = new Object();
                Log.e("giftcardcheckoutfflist", "" + flist);

                // Log.e("object", "" + object);
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 202) {
                    Log.e("giftcheckstatuscode", String.valueOf(statuscode));
                    checkoutList.clear();
                    try {

                        object = new JSONObject(new Gson().toJson(flist));
                        object1 = object.getJSONObject("0");
                        Log.e("giftobject1", "" + object1.getString("pname"));
                        Log.e("giftobjlength", "" + object.length());

                        relativecardamount.setVisibility(View.VISIBLE);

                        /*if (getcouponamout.equals("")) {
                            ecomgiftamount.setVisibility(View.GONE);
                        } else {
                            ecomgiftamount.setVisibility(View.VISIBLE);
                            ecomgiftamount.setText("₹" + getcouponamout);
                        }*/
                        //ecomgiftamount=object.getString("payable_amount")
                        totalpayableamount = object.getString("payable_amount");
                        ecomsubtotal.setText("₹" + object.getString("cartprice"));
                        ecomgst.setText("₹" + object.getString("totalgstprice"));
                        String giftamount = object.getString("GiftAmount");
                        Log.e("giftamount", "" + giftamount);
                        ecomgiftamount.setText("₹" + giftamount);
                        ecomgrandtotal.setText("₹" + totalpayableamount);
                        productscount = object.getString("productscount");
                        int length = object.length();
                        Log.e("objlength", "" + object.length());
                        for (int index = 0; index < length; index++) {
                            jsonObj = object.getJSONObject(String.valueOf(index));
                            Log.e("ggggg", "" + object.getString(String.valueOf(index)));
                            item = new CheckoutModel();
                            Log.e("checkme", jsonObj.getString("pname"));
                            item.setUser_id(Float.parseFloat(jsonObj.getString("user_id")));
                            item.setPname(jsonObj.getString("pname"));
                            item.setDescription(jsonObj.getString("description"));
                            item.setWeight(jsonObj.getString("weight"));
                            item.setImage_uri(jsonObj.getString("image_uri"));
                            item.setVa_price(Float.parseFloat(jsonObj.getString("vaprice")));
                            item.setVa(jsonObj.getString("va"));
                            item.setTotal_price(Float.parseFloat(jsonObj.getString("totalprice")));
                            item.setPrice(Float.parseFloat(jsonObj.getString("price")));
                            item.setPid(String.valueOf(jsonObj.getInt("pid")));
                            item.setSize(String.valueOf(jsonObj.getInt("size")));

                            item.setStoneid(String.valueOf(jsonObj.getInt("stoneid")));
                            stonegetid = (String.valueOf(jsonObj.getInt("stoneid")));
                            Log.e("stonegetid", "" + stonegetid);


                            liveprice = jsonObj.getString("liveprice");
                            Log.e("getliveprice", "" + liveprice);
                            noofproducts = "1";
                            pid = String.valueOf(jsonObj.getInt("pid"));
                            Log.e("getpid", "" + pid);


                            size = String.valueOf(jsonObj.getInt("size"));
                            liveprice22 = String.valueOf(jsonObj.getInt("liveprice22"));
                            liveprice24 = String.valueOf(jsonObj.getInt("liveprice24"));
                            silverliveprice = String.valueOf(jsonObj.getInt("silverprice"));

                            Log.e("silverliveprice", "" + silverliveprice);

                            checkoutList.add(item);
                            Log.e("checkoutlistdata", "" + checkoutList.size());
                            checkAdapter.notifyDataSetChanged();
                            setdata();
                        }

                    } catch (JSONException e) {
                        Log.e("throwsme", "" + e);
                        e.printStackTrace();
                    }

                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("jhguygh", String.valueOf(t));
            }
        });
    }


    @Override
    public void onBackPressed() {
        removeGiftCardCodeback(AccountUtils.getIsGiftCard(getApplicationContext()));
        countDownTimer.cancel();
        countDownTimer.onFinish();
        timerRunning = false;
        startActivity(new Intent(getApplicationContext(), EcommCartActivity.class));
        super.onBackPressed();

    }


    //// ecomcart adapter/////
    private class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {
        private ArrayList<CheckoutModel> cartList;

        public CheckoutAdapter(ArrayList<CheckoutModel> moviesList) {
            this.cartList = moviesList;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ecommchecloutlist, viewGroup, false);
            return new ViewHolder(itemView);
        }


        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
            CheckoutModel ch = cartList.get(position);
            viewHolder.cartvaprice.setText(String.valueOf("₹" + ch.getVa_price()));
            viewHolder.cartprice.setText(String.valueOf("₹" + ch.getPrice()));
            viewHolder.carttotalprice.setText(String.valueOf("Total: ₹" + ch.getTotal_price()));
            viewHolder.cartva.setText(String.valueOf(ch.getVa() + " %"));
            viewHolder.cpname.setText(ch.getPname());

            if (ch.getPname().equals("Gift Card")) {
                viewHolder.linearva.setVisibility(View.GONE);
                relativesubtotal.setVisibility(View.GONE);
                relativegst.setVisibility(View.GONE);
                emi.setVisibility(View.GONE);
            } else {
                viewHolder.linearva.setVisibility(View.VISIBLE);
                relativesubtotal.setVisibility(View.VISIBLE);
                relativegst.setVisibility(View.VISIBLE);
                emi.setVisibility(View.VISIBLE);
            }

            List<String> list = new ArrayList<>();
            for (CheckoutModel item : cartList) {
//                list.add(String.valueOf(item.getPid()));
                list.add(item.getSize());

//                pidvalue = TextUtils.join(",", list);
                size = TextUtils.join(",", list);
//                Log.e("LOGTAG", "" + pidvalue);
                Log.e("LOGTAG", "" + size);

            }

            List<String> list1 = new ArrayList<>();
            for (CheckoutModel item : cartList) {
                list1.add(item.getPid());
                pigdsize = TextUtils.join(",", list1);
                Log.e("pigdsize", "" + pigdsize);

            }


            List<String> stoneidlist = new ArrayList<>();
            for (CheckoutModel item : cartList) {
                stoneidlist.add(item.getStoneid());
                sstoneid = TextUtils.join(",", stoneidlist);
                Log.e("sstoneid", "" + sstoneid);

            }

            String pimg = ch.getImage_uri();
            // Log.e("nammmmee", "" + ch.getImage_uri());
            try {
                Log.e("image", "" + pimg);

                Glide.with(getApplicationContext()).load(pimg).into(viewHolder.cartpimage);

            } catch (Exception ignored) {
                Glide.with(getApplicationContext()).load(R.drawable.background_image).into(viewHolder.cartpimage);
            }

        }

        @Override
        public int getItemCount() {
            return cartList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView cpname, cartprice, cartva, cartvaprice, carttotalprice;
            ImageView cartpimage;
            String pid = "24";
            LinearLayout linearva;
            CardView cardunderproduct;

            @SuppressLint("SetTextI18n")
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                cpname = itemView.findViewById(R.id.pname);
                cartprice = itemView.findViewById(R.id.text_quantity_price);
                cartva = itemView.findViewById(R.id.textVAper);
                cartvaprice = itemView.findViewById(R.id.text_VA_price);
                carttotalprice = itemView.findViewById(R.id.text_total_price);
                cartpimage = itemView.findViewById(R.id.img);
                linearva = itemView.findViewById(R.id.linearva);
            }
        }

    }

    public void dediactWalletMoney() {

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void afterTextChanged(Editable charSequence) {
                if (charSequence.toString().length() == 1 && charSequence.toString().startsWith("0")) {
                    charSequence.clear();
                }


                if (charSequence != null && !charSequence.toString().equalsIgnoreCase("")) {
                    if (et_wallet_money.getText().hashCode() == charSequence.hashCode()) {
                        amountvalue = et_wallet_money.getText().toString();
                        tv_wallet_error.setVisibility(View.VISIBLE);

                        try {
                            if (Integer.parseInt(paybleamount) >= Integer.parseInt(amountvalue)) {

                                // tv_wallet_error.setText("Money less then");
                                tv_wallet_error.setVisibility(View.GONE);

                                int minus = Integer.parseInt(paybleamount) - Integer.parseInt(amountvalue);

                                after_dedcation = String.valueOf(minus);


                                tv_payble_amount.setText(getString(R.string.Rs) + after_dedcation);
                                proceedPay.setEnabled(true);

                                Log.e("Walletamountddd", "" + Walletamount);
                                String result = Walletamount.replaceAll("[^\\w\\s]", "");
                                String stopEnd = result.substring(0, result.length() - 2);
                                int amou = Integer.parseInt(amountvalue);
                                int wall = Integer.parseInt(stopEnd);
                                if (amou > wall) {
                              /*  double entervalue = Double.parseDouble(amountvalue);
                                double walletvalue = Double.parseDouble(Walletamount);

                                if (entervalue > walletvalue) {*/

                                    tv_wallet_error.setVisibility(View.VISIBLE);
                                    proceedPay.setEnabled(false);
                                    proceedPay.setBackgroundResource(R.drawable.backgroundvisablity);

                                    tv_wallet_error.setText("You can't enter more then " + Walletamount);
                                    tv_wallet_error.setTextColor(getColor(R.color.red));

                                }

                            } else {

                                tv_payble_amount.setText(getString(R.string.Rs) + "0");
                                proceedPay.setEnabled(false);

                                proceedPay.setBackgroundResource(R.drawable.backgroundvisablity);

                                tv_wallet_error.setText("You can't enter more than Payable Amount");
                                tv_wallet_error.setTextColor(getColor(R.color.red));

                            }

                            et_wallet_money.removeTextChangedListener(textWatcher);
                            et_wallet_money.addTextChangedListener(textWatcher);
                        } catch (NumberFormatException e) {

                        }
                    }
                } else {

                    tv_payble_amount.setText(getString(R.string.Rs) + paybleamount);
                    tv_wallet_error.setText("");
                    amountvalue = " ";

                }
            }
        };
        et_wallet_money.addTextChangedListener(textWatcher);
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

                        Walletamount = list.getAmount_wallet();

                        tvwalletamount.setText("Available amount : " + getString(R.string.Rs) + " " + Walletamount);
                        dediactWalletMoney();
                        dialog.dismiss();

                    } else {

                        dialog.dismiss();

                        //    ToastMessage.onToast(EcommCheckout_Activity.this, "Technical issue", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {

                    Log.e("on fails", t.toString());

                    dialog.dismiss();

                    // ToastMessage.onToast(EcommCheckout_Activity.this, "We have some issue", ToastMessage.ERROR);
                }
            });
        }
    }


    public void init_timer() {
        long duration = 300000;
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                String sduration = String.format(Locale.ENGLISH, "%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(l)
                        , TimeUnit.MILLISECONDS.toSeconds(l) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)
                                )
                );
                tv_second_title.setText(sduration);
                tv_second_title.setVisibility(View.VISIBLE);
                //  tvsendagain.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                tv_second_title.setVisibility(View.GONE);
                //  Intent intent = new Intent(EcommCheckout_Activity.this, EcommCartActivity.class);
                //  startActivity(intent);
                finish();
                // tvsendagain.setVisibility(View.VISIBLE);
            }
        }.start();
        timerRunning = true;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    public void setdata() {

        double astamount = Double.parseDouble(totalpayableamount);
        Log.e("paybleamount", "" + totalpayableamount);


        finalcaluclation = astamount;
        try {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            BigDecimal b = BigDecimal.valueOf(finalcaluclation).setScale(0, RoundingMode.HALF_UP);
            paybleamount = String.valueOf(b);
            tv_payble_amount.setText(getString(R.string.Rs) + paybleamount);
            Log.e("paybleamount", "" + paybleamount);

        } catch (NumberFormatException ex) {
            Log.e("myexception", "" + ex);
        }


    }

    public void openPayment() {
        if (paybleamount.equals(amountvalue)) {
            buy_wallet_money();
        } else {
            if (isCheck) {
                if (amountvalue.equals("null")) {
                    ToastMessage.onToast(EcommCheckout_Activity.this, "Please enter amount", ToastMessage.ERROR);
                } else {

                    if (after_dedcation.equals("null")) {

                        Log.e("paybleamountif", paybleamount);
                    } else {
                        Log.e("paybleamountelse", paybleamount);
                        paybleamount = after_dedcation;
                    }
                    openpayment();
                }
            } else {
                openpayment();
            }
        }
        if (isCheck) {
            if (amountvalue.equals("null")) {
                ToastMessage.onToast(EcommCheckout_Activity.this, "Please enter amount", ToastMessage.ERROR);
            } else {

                if (after_dedcation.equals("null")) {

                    Log.e("payble amount if", paybleamount);
                } else {
                    Log.e("payble amount else", paybleamount);
                    paybleamount = after_dedcation;
                }
                //  openpayment();
            }
        } else {
            //openpayment();
        }
    }

    public void buy_wallet_money() {
        String s = productscount;
        int i = Integer.parseInt(s.substring(0, s.length() - 2));
        String jjkgj = String.valueOf(i);


      /*  String ss = liveprice;
        Log.e("postliveprice", "" + ss);
        int ii = Integer.parseInt(ss.substring(0, ss.length() - 2));
        String livepriceconversion = String.valueOf(ii);
        Log.e("postiiii", "" + livepriceconversion);


        String ssilver = silverliveprice;
        Log.e("postsilverliveprice", "" + ssilver);
        int iisilver = Integer.parseInt(ssilver.substring(0, ssilver.length() - 2));
        String silverpriceconversion = String.valueOf(iisilver);
        Log.e("postiiii", "" + silverpriceconversion);*/


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

            Log.e("seeme", AccountUtils.getAccessToken(this) + " , " + pigdsize + "," + liveprice22 + "," + jjkgj + "," + amountvalue + "," + silverliveprice + "," + size + "," + liveprice24 + "," + sstoneid);


            Call<CheckoutModel> getdetails = apiDao.postcheckoutmoneywallet("Bearer " + AccountUtils.getAccessToken(this), pigdsize, liveprice22, jjkgj, amountvalue, silverliveprice, size, liveprice24, sstoneid);
            getdetails.enqueue(new Callback<CheckoutModel>() {
                @Override
                public void onResponse(Call<CheckoutModel> call, Response<CheckoutModel> response) {
                    int statuscode = response.code();
                    Log.e("walletstatuscodedd", String.valueOf(statuscode));

                    if (statuscode == 200 || statuscode == 202) {
                        dialog.dismiss();
                        AccountUtils.setIsGiftCard(getApplicationContext(), "");

                        Log.e("walletstatuscodeddss", String.valueOf(statuscode));
                        CheckoutModel list = response.body();

                        Log.e("isprocess", "" + list.isProcessed());

                        if (list.isProcessed()) {
                            Log.e("walletstatumessage", "" + list.getMessage());
                            Log.e("walletstatuisprocess", "" + list.isProcessed());
                            stdescription = list.getMessage();
                            onsucess();
                        }


                    } else if (statuscode == 422) {
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    } else {
                        dialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<CheckoutModel> call, Throwable t) {
                    Log.e("onfails", t.toString());
                    dialog.dismiss();
                    // ToastMessage.onToast(getApplicationContext(), "We have some issue", ToastMessage.ERROR);
                }
            });


        }
    }

    public void openpayment() {

        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setKeyID("rzp_test_0VM20Pg2VIA2aR");
        //  co.setKeyID("rzp_live_uvxtS5LwJPMIOP");

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("payment_capture", 1);

            // double total = Double.parseDouble("1");
            double total = Double.parseDouble(paybleamount);

            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();

            preFill.put("email", AccountUtils.getEmail(this));
            preFill.put("contact", AccountUtils.getMobile(this));

            options.put("prefill", preFill);

            co.open(activity, options);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(final String s) {
        try {
            Toast.makeText(getApplicationContext(), "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
            onpaymentsucess(s);
            Log.e("paymentID", s);
        } catch (Exception e) {
            Log.e("com.merchant", e.getMessage(), e);
        }
    }

    public void onpaymenterror() {
        Intent intent = new Intent(this, PaymentError.class);
        removeGiftCardCodeback(AccountUtils.getIsGiftCard(getApplicationContext()));
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        et_wallet_money.setText("");
        et_wallet_money.setVisibility(View.GONE);
        cb_wallet.setChecked(false);
        tv_wallet_error.setVisibility(View.GONE);
        // init();

        if (CouponCodeid.equals("")) {
            getEcommCheckoutproducts();
        } else {
            getEcommCheckoutproductswithid(CouponCodeid);
        }
        setdata();
    }

    @Override
    public void onPaymentError(int i, String s) {
        tv_payble_amount.setText(getString(R.string.Rs) + paybleamount);
//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        amountvalue = "";
        removeGiftCardCodeback(AccountUtils.getIsGiftCard(getApplicationContext()));
        ToastMessage.onToast(this, "Payment cancelled", ToastMessage.ERROR);
    }

    public void onpaymentsucess(String paymentid) {
        Log.e("sendfyfftv", "" + productscount.substring(0, productscount.length() - 2));
        String s = productscount;
        int i = Integer.parseInt(s.substring(0, s.length() - 2));
        Log.e("sendfyffintttv", "" + i);
        String jjkgj = String.valueOf(i);


        /*String ss = liveprice;
        Log.e("postliveprice", "" + liveprice);
        int ii = Integer.parseInt(ss.substring(0, ss.length() - 2));
        String rrrlivepriceconversion = String.valueOf(ii);
        Log.e("postiiii", "" + rrrlivepriceconversion);*/

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {

            //  dialog.dismiss();

            Log.e("payamount", "" + pigdsize + "," + liveprice22 + "," + jjkgj + "," + paymentid + "," + paybleamount + "," + amountvalue + "," + size + "," + sstoneid);

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            //   Call<Listmodel> call = apiDao.postcheckout("Bearer " + AccountUtils.getAccessToken(this), "9", "4100", "1", "pay_JtAZjARQGTZG9K", "11781", "");

            Call<CheckoutModel> call = apiDao.postcheckout("Bearer " + AccountUtils.getAccessToken(this), pigdsize, liveprice22, jjkgj, paymentid, paybleamount, amountvalue, silverliveprice, size, liveprice24, sstoneid);
            call.enqueue(new Callback<CheckoutModel>() {
                @Override
                public void onResponse(Call<CheckoutModel> call, Response<CheckoutModel> response) {
                    dialog.dismiss();
                    int stauscode = response.code();
                    Log.e("stauscodebuy", String.valueOf(stauscode));

                    if (stauscode == 200 || stauscode == 202) {
                        AccountUtils.setIsGiftCard(getApplicationContext(), "");

                        dialog.dismiss();
                        Log.e("walletstatuscodeddss", String.valueOf(stauscode));
                        CheckoutModel list = response.body();
                        if (list.isProcessed()) {
                            Log.e("walletstatuscodeddss", "" + list.getMessage());
                            Log.e("walletstatuscodeddss", "" + list.isProcessed());
                            stdescription = list.getMessage();
                            onsucess();
                        }

                       /* List<CheckoutModel> list = Collections.singletonList(response.body());
                        for (CheckoutModel listmodel : list) {
                            payment_status = listmodel.isProcessed();
                            // Toast.makeText(Buy_Gold_Information.this, String.valueOf(payment_status), Toast.LENGTH_SHORT).show();
                            if (payment_status) {
                                // sttransactionid = listmodel.getTransaction_Id();
                                stdescription = listmodel.getMessage();
                                onsucess();
                            } else {
                                onpaymenterror();
                            }

                        }*/

                    } else if (stauscode == 422) {
                        dialog.dismiss();

                        String responvce = new Gson().toJson(response.body());
                        Log.e("Error Responce", responvce);
                        onpaymenterror();
                    } else {
                        dialog.dismiss();

                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            Toast.makeText(EcommCheckout_Activity.this, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");
                            JSONArray array_paymentid = er.getJSONArray("payment_id");
                            for (int i = 0; i < array_paymentid.length(); i++) {

                                Toast.makeText(EcommCheckout_Activity.this, array_paymentid.getString(i), Toast.LENGTH_SHORT).show();
                                onpaymenterror();
                            }
                            JSONArray array_amount = er.getJSONArray("amount");
                            for (int i = 0; i < array_amount.length(); i++) {
                                Toast.makeText(EcommCheckout_Activity.this, array_amount.getString(i), Toast.LENGTH_SHORT).show();
                                onpaymenterror();
                            }

                            JSONArray array_referalcode = er.getJSONArray("amount");


                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CheckoutModel> call, Throwable t) {
                    onpaymenterror();
                    Log.e("on buy fail", t.toString());
                    dialog.dismiss();

                }
            });

        }

    }


    public void onsucess() {

        Intent intent = new Intent(this, EcommSuccessPopup.class);
        intent.putExtra("description", stdescription);
        startActivity(intent);

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

                        //      ToastMessage.onToast(EcommCheckout_Activity.this, "Gift Code Added Succesfully", ToastMessage.SUCCESS);


                    } else if (statuscode == 422) {
                      /*  try {
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


}
