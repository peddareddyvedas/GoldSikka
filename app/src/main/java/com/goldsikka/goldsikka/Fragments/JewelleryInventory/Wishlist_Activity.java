package com.goldsikka.goldsikka.Fragments.JewelleryInventory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Fragments.Buy_Digitalgold;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.LOGIN.WelcomeActivity;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

public class Wishlist_Activity extends AppCompatActivity {
    RecyclerView wishRecyclerview;
    WishAdapter wishAdapter;
    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;
    shared_preference sharedPreference;
    private List<Wislistmodel> wislistmodelList = new ArrayList<>();
    int selectedPosition = -1;
    RecyclerView wishlistitemdata;
//    DownloadAdapter downloadAdapter;
    private List<Listmodel> wislistList = new ArrayList<>();
    private List<Listmodel> wislistList2 = new ArrayList<>();
    private Handler hdlr = new Handler();
    private int i = 0;

    List<Listmodel> flist;
    List<Listmodel> flist1, flist2;
    ApiDao apiDao;

    LottieAnimationView lottieAnimationView;
    TextView notxt;

    String goldwallet= "sdv";

    SwipeRefreshLayout swipe_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        getWalletGold();
        init();
        onrefresh();
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
                        if (!NetworkUtils.isConnected(Wishlist_Activity.this)) {
                            ToastMessage.onToast(Wishlist_Activity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            swipe_layout.setRefreshing(false);
                            return;
                        } else {
                            getWalletGold();
                            init();
                            onrefresh();
                        }
                        swipe_layout.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }


    public void init() {
        sharedPreference = new shared_preference(getApplicationContext());
        swipe_layout = findViewById(R.id.swipe_layout);
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Wishlist");

        notxt = findViewById(R.id.notxt);
        lottieAnimationView = findViewById(R.id.wempty);

        wishRecyclerview = findViewById(R.id.wishlist_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
        wishRecyclerview.setLayoutManager(linearLayoutManager);
        wishAdapter = new WishAdapter(wislistList2);
        wishRecyclerview.setAdapter(wishAdapter);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        wishRecyclerview.setHasFixedSize(false);

        getWishlistData();
    }

    public void getWishlistData() {
        wislistList.clear();
        wislistList2.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<List<Listmodel>> getWishlist = apiDao.getWishlist("Bearer "+AccountUtils.getAccessToken(this));
        getWishlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("statuscode", String.valueOf(statuscode));
//                assert response.body() != null;
                flist =  response.body();
                flist1 = new ArrayList<>();
                flist2 = new ArrayList<>();
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201) {
                    if (!flist.isEmpty()) {
                        wishRecyclerview.setVisibility(View.VISIBLE);
                        notxt.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        wislistList2.clear();

                        for(Listmodel flistt : flist){
                            if(flistt.getSum()>0){
                                flist1.add(flistt);
                            }
                        }

                        for(Listmodel flistt1 : flist){
                            if(flistt1.getSum()==0){
                                flist2.add(flistt1);
                            }
                        }

                        wislistList2.addAll(flist2);
                        wislistList2.addAll(flist1);
                        Collections.reverse(wislistList2);
//                        wishAdapter = new WishAdapter(wislistList2);
//                        mBusinessAdapter.notifyDataSetChanged();
                        wishRecyclerview.getRecycledViewPool().clear();
                        wishAdapter.notifyDataSetChanged();


                    } else {
                        Log.e("catname", "No cats");
                        wishRecyclerview.setVisibility(View.GONE);
                        notxt.setVisibility(View.VISIBLE);
                        lottieAnimationView.setVisibility(View.VISIBLE);
                    }
                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("ughb", String.valueOf(t));
//                openpopupscreen("Successfully subscribed to Gold Plus Plan");
            }
        });

    }

    @Override
    public void onBackPressed() {
        // NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        // finish();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void deleteproductfromwishlist(DialogInterface dialog, WishAdapter.ViewHolder holder, String pid) {
        Log.e("token", AccountUtils.getAccessToken(Wishlist_Activity.this));
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(Wishlist_Activity.this)).create(ApiDao.class);

        Call<Listmodel> deletewishlist = apiDao.deletewishlist("Bearer " + AccountUtils.getAccessToken(Wishlist_Activity.this), pid);
        deletewishlist.enqueue(new Callback<Listmodel>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<Listmodel> call, @NotNull Response<Listmodel> response) {
                int statuscode = response.code();
                Log.e("poststatuscode", String.valueOf(statuscode));
                Log.e("poststatuscodeerror", String.valueOf(response));
//                    dialog.dismiss();

                if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                    wishRecyclerview.getRecycledViewPool().clear();
                    wishAdapter.notifyDataSetChanged();
                    getWishlistData();
                    dialog.dismiss();
                } else if (statuscode == 422) {
//                        dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    Log.e("cv", String.valueOf(statuscode));
//                        dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
//                    dialog.dismiss();
                Log.e("ughb", String.valueOf(t));
            }
        });




    }



    @Override
    protected void onResume() {
        super.onResume();
        getWalletGold();
        init();
    }

    private void getWalletGold() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

            Call<JsonElement> call = apiDao.get_digitalwallet("Bearer " + AccountUtils.getAccessToken(this));
            call.enqueue(new Callback<JsonElement>() {

                @Override
                public void onResponse(@NonNull Call<JsonElement> call, @NonNull retrofit2.Response<JsonElement> response) {
                    int stauscode = response.code();
                    if (stauscode == HttpsURLConnection.HTTP_CREATED || stauscode == HttpsURLConnection.HTTP_OK) {

                        JsonElement jsonElement = response.body();
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        JsonObject gson = new JsonParser().parse(String.valueOf(jsonObject)).getAsJsonObject();

                        try {
                            JSONObject jo2 = new JSONObject(gson.toString());
                            JSONObject balance = jo2.getJSONObject("balance");
                            String st_ingrams = balance.getString("humanReadable");
                            goldwallet = st_ingrams;
                            AccountUtils.setWalletAmount(Wishlist_Activity.this, st_ingrams);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else if (stauscode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        sharedPreference.WriteLoginStatus(false);
                        Intent intent = new Intent(Wishlist_Activity.this, WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
//                            Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                            JSONObject er = jObjError.getJSONObject("errors");

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {

                 //   Toast.makeText(Wishlist_Activity.this, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    //// main adapter/////
    private class WishAdapter extends RecyclerView.Adapter<WishAdapter.ViewHolder> {
        private List<Listmodel> wisList;

        public WishAdapter(List<Listmodel> moviesList) {
            this.wisList = moviesList;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_whislistcart, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
            Log.e("itemName", " " + position);
            Listmodel w = wisList.get(position);
            viewHolder.pnameTv.setText(w.getPname());
            viewHolder.pvaTv.setText(w.getPva() + " %");
            viewHolder.pweightTv.setText(w.getPweight() + " gms");
            viewHolder.status.setText(w.getPids());
            String pimg = w.getPimg();
            String status = w.getStatus();
            int wishlist_status = w.getWishlist_status();

            viewHolder.em1.setVisibility(View.GONE);
            viewHolder.em2.setVisibility(View.GONE);

            Integer fsum = Integer.parseInt(String.valueOf(w.getSum()));
            Log.e("sum", String.valueOf(fsum));

            viewHolder.progressBar.setMax(Integer.parseInt(w.getPweight()));
            viewHolder.progressBar.setProgress(w.getSum());

            if (Double.parseDouble(String.valueOf(w.getSum())) >= (Double.parseDouble(w.getPweight())) / 2) {
                viewHolder.progresstext.setText(w.getSum() + "/" + w.getPweight() + " gms");
                viewHolder.progresstext.setTextColor(Color.WHITE);
            } else {
                viewHolder.progresstext.setText(w.getSum() + "/" + w.getPweight() + " gms");
                viewHolder.progresstext.setTextColor(Color.BLACK);
            }

            if(wishlist_status==1 || wishlist_status==2) {

                if (Double.parseDouble(String.valueOf(w.getSum())) == (Double.parseDouble(w.getPweight()))) {
                    viewHolder.statustxt.setVisibility(View.VISIBLE);
                    viewHolder.statustxt.setText("Redeem Now");
                    viewHolder.wdelete.setVisibility(View.GONE);
                    viewHolder.accumulate.setVisibility(View.GONE);
                    viewHolder.transactions.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        viewHolder.statustxt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1A2E23")));
                    }

                    viewHolder.statustxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertRedeemBox();
                            postredeemwish(w.getPids());
                        }
                    });

                } else {
                    viewHolder.statustxt.setVisibility(View.VISIBLE);
                    viewHolder.statustxt.setText("pending");
                    viewHolder.accumulate.setVisibility(View.VISIBLE);
                    viewHolder.transactions.setVisibility(View.GONE);

                }

                if (fsum == 0) {
                    viewHolder.wdelete.setVisibility(View.VISIBLE);
                    viewHolder.statustxt.setVisibility(View.GONE);
                    viewHolder.wtransactions.setVisibility(View.GONE);
                } else if (fsum < Double.parseDouble(w.getPweight())) {
                    viewHolder.wdelete.setVisibility(View.GONE);
                    viewHolder.statustxt.setVisibility(View.VISIBLE);
                    viewHolder.statustxt.setText("pending");
                    viewHolder.wtransactions.setVisibility(View.VISIBLE);

                } else {
                    Log.e("sum", "exceeded");
                }
            }else{
                viewHolder.statustxt.setVisibility(View.VISIBLE);
                viewHolder.statustxt.setText("Delivered");
                viewHolder.wdelete.setVisibility(View.GONE);
                viewHolder.accumulate.setVisibility(View.GONE);
                viewHolder.transactions.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    viewHolder.statustxt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1A2E23")));
                }
            }

            try {
                Glide.with(getApplicationContext()).load(pimg).into(viewHolder.pimg);
            } catch (Exception ignored) {
                Glide.with(getApplicationContext()).load(R.drawable.background_image).into(viewHolder.pimg);
            }

            viewHolder.wdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog(viewHolder, w.getPname(), w.getPids());
                }
            });
            if (selectedPosition == position) {
                Log.e("ifnews", "called");
            } else {
                Log.e("elsenews", "called");

            }




            viewHolder.accumulate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPosition != position) {
                        selectedPosition = position;
                        viewHolder.linerargoldEnter.setVisibility(View.VISIBLE);

                        if ( position == getItemCount() - 1) {
                            LinearLayoutManager layoutManager = (LinearLayoutManager) wishRecyclerview.getLayoutManager();
                            layoutManager.scrollToPositionWithOffset(position, 0);
                            Log.e("layout", "call"+position);
                        }

                    } else {
                        selectedPosition = -1;
                        viewHolder.linerargoldEnter.setVisibility(View.GONE);

                    }

                /*if (viewHolder.linerargoldEnter.getVisibility() == View.GONE)

                    viewHolder.linerargoldEnter.setVisibility(View.VISIBLE);

                else
                    viewHolder.linerargoldEnter.setVisibility(View.GONE);*/

                }
            });

            viewHolder.buyjewellery.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    if (viewHolder.et_grams.getText().toString().isEmpty()) {
                        ToastMessage.onToast(Wishlist_Activity.this, "Please Enter the Grams", ToastMessage.ERROR);
                    } else {
                        BuyJewelleryWithGoldWallet(viewHolder,w.getSum(), w.getPweight(), w.getPids(), viewHolder.et_grams.getText().toString());
                    }
                }
            });

            viewHolder.wtransactions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Wishlist_Activity.this, JewelleryPassbook.class);
                    intent.putExtra("pid", String.valueOf(w.getPids()));
                    startActivity(intent);
                }
            });
            viewHolder.transactions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Wishlist_Activity.this, JewelleryPassbook.class);
                    intent.putExtra("pid", String.valueOf(w.getPids()));
                    startActivity(intent);
                }
            });
        }

        private void postredeemwish(String pid) {
            Log.e("token", AccountUtils.getAccessToken(Wishlist_Activity.this));
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(Wishlist_Activity.this)).create(ApiDao.class);

            Call<Integer> postredeemwish = apiDao.postredeemwish("Bearer " + AccountUtils.getAccessToken(Wishlist_Activity.this), pid);
            postredeemwish.enqueue(new Callback<Integer>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<Integer> call, @NotNull Response<Integer> response) {
                    int statuscode = response.code();
                    if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                        Log.e("statuscode", String.valueOf(statuscode));
                    } else if (statuscode == 422) {
//                        dialog.dismiss();
                        Log.e("statuscode", String.valueOf(statuscode));
                    } else {
                        Log.e("statuscode", String.valueOf(statuscode));
//                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
//                    dialog.dismiss();
                    Log.e("ughb", String.valueOf(t));
                }
            });

        }

        private void AlertRedeemBox() {
            final Dialog dialog = new Dialog(Wishlist_Activity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.redeem_dialog);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.button_background);



            Button dialogButton = (Button) dialog.findViewById(R.id.close);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        private void alertDialog(ViewHolder viewHolder, String pname, String id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Wishlist_Activity.this);
            builder.setMessage("Are you sure want to delete Jewellery " + pname);
            builder.setTitle("Alert !");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteproductfromwishlist(dialog, viewHolder, id);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


        @Override
        public int getItemCount() {
            return wisList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView pnameTv, pvaTv, pweightTv, gamount, wtransactions, statustxt, progresstext, transactions, status, em1, em2;
            ImageView pimg, wdelete;
            Button accumulate, buyjewellery;
            LinearLayout linerargoldEnter;
            EditText et_grams;

            CardView pwishcard;

            ProgressBar progressBar;


            @SuppressLint("SetTextI18n")
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                pnameTv = itemView.findViewById(R.id.pnameTv);
                pweightTv = itemView.findViewById(R.id.pweightTv);
                pvaTv = itemView.findViewById(R.id.pvaTv);
                pimg = itemView.findViewById(R.id.pimg);
                wdelete = itemView.findViewById(R.id.wdelete);
                accumulate = itemView.findViewById(R.id.accumulate);
                buyjewellery = itemView.findViewById(R.id.buyjewellery);
                linerargoldEnter = itemView.findViewById(R.id.linerargoldEnter);
                et_grams = itemView.findViewById(R.id.et_grams);
                et_grams.setHint(Html.fromHtml(getString(R.string.grams)));
                gamount = itemView.findViewById(R.id.gamount);
                wtransactions = itemView.findViewById(R.id.wtransactions);
                statustxt = itemView.findViewById(R.id.statustxt);
                transactions = itemView.findViewById(R.id.transactions);
                status = itemView.findViewById(R.id.pid);
                progressBar = itemView.findViewById(R.id.progressBar);
                progresstext = itemView.findViewById(R.id.progresstext);
                em1 = itemView.findViewById(R.id.em1);
                em2 = itemView.findViewById(R.id.em2);
                pwishcard = itemView.findViewById(R.id.pwishcard);


                gamount.setText(AccountUtils.getWalletAmount(getApplicationContext()) + " grams");

            }
        }

    }

//    //// main adapter/////
//    private class WishAdapter extends RecyclerView.Adapter<WishAdapter.ViewHolder> {
//        private List<Listmodel> wisList;
//
//        public WishAdapter(List<Listmodel> moviesList) {
//            this.wisList = moviesList;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//            View itemView = LayoutInflater.from(viewGroup.getContext())
//                    .inflate(R.layout.activity_whislistcart, viewGroup, false);
//            return new ViewHolder(itemView);
//        }
//
//        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
//        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//        @Override
//        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
//            Log.e("itemName", " " + position);
//            Listmodel w = wisList.get(position);
//            viewHolder.pnameTv.setText(w.getPname());
//            viewHolder.pvaTv.setText(w.getPva() + " %");
//            viewHolder.pweightTv.setText(w.getPweight() + " gms");
//            viewHolder.pid.setText(w.getPids());
//            String pimg = w.getPimg();
//
//            Integer fsum = Integer.parseInt(String.valueOf(w.getSum()));
//            Log.e("sum", String.valueOf(fsum));
//
//            viewHolder.progressBar.setMax(Integer.parseInt(w.getPweight()));
//            viewHolder.progressBar.setProgress(w.getSum());
//
//            if(Double.parseDouble(String.valueOf(w.getSum()))>=(Double.parseDouble(w.getPweight()))/2) {
//                viewHolder.progresstext.setText(w.getSum() + "/" + w.getPweight()+" gms");
//                viewHolder.progresstext.setTextColor(Color.WHITE);
//            }
//            else {
//                viewHolder.progresstext.setText(w.getSum() + "/" + w.getPweight()+" gms");
//                viewHolder.progresstext.setTextColor(Color.BLACK);
//            }
//
//            if (Double.parseDouble(String.valueOf(w.getSum())) == (Double.parseDouble(w.getPweight()))) {
//                viewHolder.statustxt.setVisibility(View.VISIBLE);
//                viewHolder.statustxt.setText("Accumulated");
//                viewHolder.wdelete.setVisibility(View.GONE);
//                viewHolder.accumulate.setVisibility(View.GONE);
//                viewHolder.transactions.setVisibility(View.VISIBLE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    viewHolder.statustxt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1A2E23")));
//                }
//
//            } else {
//                viewHolder.statustxt.setVisibility(View.VISIBLE);
//                viewHolder.statustxt.setText("pending");
//                viewHolder.accumulate.setVisibility(View.VISIBLE);
//                viewHolder.transactions.setVisibility(View.GONE);
//                viewHolder.wdelete.setVisibility(View.GONE);
//
//            }
//
//            if(fsum==0){
//                viewHolder.wdelete.setVisibility(View.VISIBLE);
//                viewHolder.statustxt.setVisibility(View.GONE);
//                viewHolder.wtransactions.setVisibility(View.GONE);
//            }else if(fsum<Double.parseDouble(w.getPweight())){
//                viewHolder.wdelete.setVisibility(View.GONE);
//                viewHolder.statustxt.setVisibility(View.VISIBLE);
//                viewHolder.statustxt.setText("pending");
//                viewHolder.wtransactions.setVisibility(View.VISIBLE);
//
//            }else {
//                Log.e("sum","exceeded");
//            }
//
//            try {
//                Glide.with(getApplicationContext()).load(pimg).into(viewHolder.pimg);
//            } catch (Exception ignored) {
//                Glide.with(getApplicationContext()).load(R.drawable.background_image).into(viewHolder.pimg);
//            }
//
//            viewHolder.wdelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    alertDialog(viewHolder, w.getPname(), w.getPids());
//                }
//            });
//
//
//
//            viewHolder.accumulate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (selectedPosition == position) {
//                        Log.e("ifnews", "called");
//                    } else {
//                        Log.e("elsenews", "called");
//
//                    }
//
//                    if (selectedPosition != position) {
//                        selectedPosition = position;
//                        viewHolder.linerargoldEnter.setVisibility(View.VISIBLE);
//
//                        if ( position == getItemCount() - 1) {
//                            LinearLayoutManager layoutManager = (LinearLayoutManager) wishRecyclerview.getLayoutManager();
//                            layoutManager.scrollToPositionWithOffset(position, 0);
//                            Log.e("layout", "call"+position);
//                        }
//
//                    } else {
//                        selectedPosition = -1;
//                        viewHolder.linerargoldEnter.setVisibility(View.GONE);
//
//                    }
//                }
//            });
//
//            viewHolder.buyjewellery.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (viewHolder.et_grams.getText().toString().isEmpty()) {
//                        ToastMessage.onToast(Wishlist_Activity.this, "Please Enter the Grams", ToastMessage.ERROR);
//                    } else {
//                        BuyJewelleryWithGoldWallet(w.getSum(), w.getPweight(), w.getPids(), viewHolder.et_grams.getText().toString());
//                    }
//                }
//            });
//
//            viewHolder.wtransactions.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Wishlist_Activity.this, JewelleryPassbook.class);
//                    intent.putExtra("pid", String.valueOf(w.getPids()));
//                    startActivity(intent);
//                }
//            });
//
//            viewHolder.transactions.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Wishlist_Activity.this, JewelleryPassbook.class);
//                    intent.putExtra("pid", String.valueOf(w.getPids()));
//                    startActivity(intent);
//                }
//            });
//
//        }
//
//        private void alertDialog(ViewHolder viewHolder, String pname, String id) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(Wishlist_Activity.this);
//            builder.setMessage("Are you sure want to delete Jewellery " + pname);
//            builder.setTitle("Alert !");
//            builder.setCancelable(false);
//            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    deleteproductfromwishlist(dialog,viewHolder, id);
//                }
//            });
//            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//        }
//
//
//        @Override
//        public int getItemCount() {
//            return wisList.size();
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            TextView pnameTv, pvaTv, pweightTv, gamount, wtransactions, statustxt,progresstext, transactions, pid;
//            ImageView pimg, wdelete;
//            Button accumulate, buyjewellery;
//            LinearLayout linerargoldEnter;
//            EditText et_grams;
//
//            ProgressBar progressBar;
//
//            @SuppressLint("SetTextI18n")
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//                pnameTv = itemView.findViewById(R.id.pnameTv);
//                pweightTv = itemView.findViewById(R.id.pweightTv);
//                pvaTv = itemView.findViewById(R.id.pvaTv);
//                pimg = itemView.findViewById(R.id.pimg);
//                wdelete = itemView.findViewById(R.id.wdelete);
//                accumulate = itemView.findViewById(R.id.accumulate);
//                buyjewellery = itemView.findViewById(R.id.buyjewellery);
//                linerargoldEnter = itemView.findViewById(R.id.linerargoldEnter);
//                et_grams = itemView.findViewById(R.id.et_grams);
//                et_grams.setHint(Html.fromHtml(getString(R.string.grams)));
//                gamount = itemView.findViewById(R.id.gamount);
//                wtransactions = itemView.findViewById(R.id.wtransactions);
//                statustxt = itemView.findViewById(R.id.statustxt);
//                transactions = itemView.findViewById(R.id.transactions);
//                pid = itemView.findViewById(R.id.pid);
//
//                progressBar = itemView.findViewById(R.id.progressBar);
//                progresstext = itemView.findViewById(R.id.progresstext);
//
//                gamount.setText(AccountUtils.getWalletAmount(getApplicationContext()) + " grams");
//
//            }
//        }
//
//    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void BuyJewelleryWithGoldWallet(WishAdapter.ViewHolder viewHolder, int sum, String pweight, String pid, String goldtransfer) {
        Log.e("token", AccountUtils.getAccessToken(Wishlist_Activity.this));
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(Wishlist_Activity.this)).create(ApiDao.class);

        Log.e("sdvdsfv", pid);
        Log.e("sdvdsfv", goldtransfer);

        int rem = Integer.parseInt(pweight)-sum;

        if(!(sum >=Integer.parseInt(pweight))) {
            if (Integer.parseInt(goldtransfer) > rem) {
                ToastMessage.onToast(Wishlist_Activity.this, "You can't enter more than " + rem + " grams for this Jewellery", ToastMessage.ERROR);
                return;
            }
        }

        if(!(Double.parseDouble(AccountUtils.getWalletAmount(Wishlist_Activity.this)) >Double.parseDouble(goldtransfer))){
//            ToastMessage.onToast(Wishlist_Activity.this, "Insufficient Balance", ToastMessage.ERROR);
            viewHolder.em1.setVisibility(View.VISIBLE);
            viewHolder.em2.setVisibility(View.VISIBLE);
            viewHolder.em1.setText("Insufficient Balance");
            viewHolder.em1.setTextColor(Color.RED);
//            viewHolder.em2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1A2E23")));
            viewHolder.em2.setTextColor(ColorStateList.valueOf(Color.parseColor("#1A2E23")));
//            viewHolder.em2.setText("Click here to Buy Gold");
            viewHolder.em2.setText(Html.fromHtml("<u>Click here to Buy Gold</u>"));
            viewHolder.em2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Wishlist_Activity.this, Buy_Digitalgold.class));
                    finish();
                }
            });
            return;
        }

        if(sum>=Integer.parseInt(pweight)){
            ToastMessage.onToast(Wishlist_Activity.this, "Accumulation Completed, Redeem is in Process...", ToastMessage.ERROR);
        }else {
            Call<JsonObject> buyjewellery = apiDao.buyJewelleryWithGold("Bearer " + AccountUtils.getAccessToken(Wishlist_Activity.this), pid, goldtransfer);
            buyjewellery.enqueue(new Callback<JsonObject>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                    int statuscode = response.code();
                    Log.e("poststatuscode", String.valueOf(statuscode));
                    Log.e("poststatuscodeerror", String.valueOf(response));
//                    dialog.dismiss();
                    if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                        Intent intent = new Intent(Wishlist_Activity.this, JewelleryPassbook.class);
                        intent.putExtra("pid", pid);
                        startActivity(intent);
                    } else if (statuscode == 422) {
                        String responvce = new Gson().toJson(response.body());
                        Log.e("Error Responce", responvce);
                        Log.e("status code", String.valueOf(statuscode));
                    } else {
                        Log.e("cv", String.valueOf(statuscode));
//                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
//                    dialog.dismiss();
                    Log.e("ughb", String.valueOf(t));
                }
            });
        }
    }
}
