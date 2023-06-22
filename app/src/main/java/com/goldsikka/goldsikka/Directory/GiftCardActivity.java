package com.goldsikka.goldsikka.Directory;

import static com.goldsikka.goldsikka.Fragments.Ecommerce.EcommCartActivity.isFromcart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Fragments.Ecommerce.EcommCartActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiftCardActivity extends AppCompatActivity {

    TextView unameTv, uidTv, titleTv, validprice;
    RelativeLayout backbtn;
    shared_preference sharedPreference;
    RecyclerView cardRecyclerview;
    GiftCardAdapter cardAdapter;
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
    EditText etcouponcoode;
    String couponammount = "";
    TextView tvofflineapplyyy;
    String text;
    String fromto = "";
    LinearLayout etcoupon;

    String entercouponcode = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftcard);
//        fromto = bundle.getString("isfrom");

        init();
        getEcommgiftcart();
        onrefresh();
    }

    public void init() {
        sharedPreference = new shared_preference(getApplicationContext());
        swipe_layout = findViewById(R.id.swipe_layout);
        notfound = findViewById(R.id.notfound);
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Gift Card");
        notxt = findViewById(R.id.notxt);
        cardRecyclerview = findViewById(R.id.recyclergiftcard);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        cardRecyclerview.setLayoutManager(linearLayoutManager2); // set LayoutManager to RecyclerView
        cardRecyclerview.setHasFixedSize(true);
        cartList = new ArrayList<>();
        cardAdapter = new GiftCardAdapter(cartList);
        cardRecyclerview.setAdapter(cardAdapter);
        tvofflineapplyyy = findViewById(R.id.tvofflineapplyyy);
        etcoupon = findViewById(R.id.etcoupon);
        etcouponcoode = findViewById(R.id.etcouponcoode);

        if (isFromcart) {
            Log.e("isifcart", "" + isFromcart);

            etcoupon.setVisibility(View.VISIBLE);

        } else {
            //
            Log.e("isifelsecart", "" + isFromcart);
            etcoupon.setVisibility(View.GONE);
        }
        entercouponcode = etcouponcoode.getText().toString().trim();

        tvofflineapplyyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("entercouponcode", "" + entercouponcode);

                postGiftCardCode(entercouponcode);

              /*  if (entercouponcode.length() == 12) {
                } else {
                    ToastMessage.onToast(getApplicationContext(), "Please Enter Valid GiftCard Coupon Code", ToastMessage.ERROR);
                }*/

            }
        });


        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
                        if (!NetworkUtils.isConnected(GiftCardActivity.this)) {
                            ToastMessage.onToast(GiftCardActivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            swipe_layout.setRefreshing(false);
                            return;
                        } else {
                            init();
                            onrefresh();
                            getEcommgiftcart();
                        }
                        swipe_layout.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }

    private void getEcommgiftcart() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        cartList.clear();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<List<Listmodel>> getecomcartlist = apiDao.getEcomgiftcart("Bearer " + AccountUtils.getAccessToken(this));
        getecomcartlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {

                dialog.dismiss();
                int statuscode = response.code();
                Log.e("cartstatuscodess", String.valueOf(statuscode));
                flist = response.body();
                if (statuscode == 200 || statuscode == 202) {
                    Log.e("cartstatuscode", String.valueOf(statuscode));

                    if (flist != null) {
                        cartList.clear();
                        for (Listmodel listmodel : flist) {
                            pidid = listmodel.getId();
                            cartList.add(listmodel);
                            cardAdapter.notifyDataSetChanged();
                            notfound.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Log.e("catname", "No Products");
                    }

                } else if (statuscode == 404) {
                    dialog.dismiss();
                    notfound.setVisibility(View.VISIBLE);
                    checkout.setClickable(false);
                    carttotal.setText(" ");
                    Log.e("400statuscaod", "" + statuscode);
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
            }
        });
    }

    private void getUpDateEcommGiftCart() {
        cartList.clear();
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
                    Log.e("cartstatuscode", String.valueOf(statuscode));

                    if (flist != null) {
                        cartList.clear();
                        for (Listmodel listmodel : flist) {
                            pidid = listmodel.getId();
                            cartList.add(listmodel);
                            cardAdapter.notifyDataSetChanged();
                            notfound.setVisibility(View.GONE);
                        }
                    } else {
                        Log.e("catname", "No Products");
                    }

                } else if (statuscode == 404) {
                    notfound.setVisibility(View.VISIBLE);
                    checkout.setClickable(false);
                    carttotal.setText(" ");
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
    }


    @Override
    public void onBackPressed() {
        // NavUtils.navigateUpFromSameTask(this);
        isFromcart = false;
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onrefresh();
        //  getEcommgiftcart();
    }

    //// ecomcart adapter/////
    private class GiftCardAdapter extends RecyclerView.Adapter<GiftCardAdapter.ViewHolder> {
        private ArrayList<Listmodel> cartList;

        public GiftCardAdapter(ArrayList<Listmodel> moviesList) {
            this.cartList = moviesList;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ecomm_getgiftlist, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
            Log.e("itemName", " " + position);
            Listmodel w = cartList.get(position);
            viewHolder.giftnamename.setText(w.getGift_card());
            viewHolder.giftamount.setText(w.getParchase_amount());
            String coupncod = w.getParchase_amount();

            String pimg = w.getImage_uri();
            if (isFromcart) {
                Log.e("isifcartpos", "" + isFromcart);

                viewHolder.tvofflineapply.setVisibility(View.VISIBLE);

            } else {
                //
                Log.e("isifelsecartpos", "" + isFromcart);
                viewHolder.tvofflineapply.setVisibility(View.GONE);
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

            viewHolder.tvofflineapply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedPosition != position) {
                        selectedPosition = position;
                        notifyDataSetChanged();
                        text = viewHolder.giftnamename.getText().toString();
                        etcouponcoode.setText(text);
                        couponammount = coupncod;
                        postGiftCardCode(text);

                    } else {

                        selectedPosition = -1;
                        notifyDataSetChanged();
                    }
                }
            });

            viewHolder.giftnamename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPosition != position) {
                        selectedPosition = position;
                        notifyDataSetChanged();
                        String text = viewHolder.giftnamename.getText().toString();
                        if (!text.isEmpty()) {
                            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText("key", text);
                            clipboardManager.setPrimaryClip(clipData);
                            Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "No text to be copied", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        selectedPosition = -1;
                        notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            //  return cartList.size();
            Log.e("sizeeeeee", "" + cartList.size());
            if (cartList.size() > 0) {
                notfound.setVisibility(View.GONE);
                return cartList.size();
            } else {
                notfound.setVisibility(View.VISIBLE);
                return 0;
            }

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView giftamount, giftnamename, tvofflineapply;
            ImageView cartpimage;

            @SuppressLint("SetTextI18n")
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                giftamount = itemView.findViewById(R.id.giftamount);
                giftnamename = itemView.findViewById(R.id.giftname);
                cartpimage = itemView.findViewById(R.id.cartpimage);
                tvofflineapply = itemView.findViewById(R.id.tvofflineapply);
            }
        }

    }


    public void postGiftCardCode(String code) {
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
            Call<Listmodel> getdetails = apiDao.postgiftcard("Bearer " + AccountUtils.getAccessToken(this), code);
            getdetails.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    String error = response.message();
                    Log.e("applycouponstatusc", "" + String.valueOf(statuscode));

                    if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                        dialog.dismiss();
                        Listmodel list = response.body();

                        String couponid = list.getId();
                        String couponcode = list.getGift_card();

                        Intent i = new Intent(getApplicationContext(), EcommCartActivity.class);
                        i.putExtra("ecommcouponid", couponid);
                        i.putExtra("ecommcouponcode", couponcode);
                        i.putExtra("ecomcouponprice", couponammount);
                        AccountUtils.setIsGiftCard(getApplicationContext(), couponcode);
                        Log.e("getgiftcard", "" + AccountUtils.getIsGiftCard(getApplicationContext()));

                        startActivity(i);
                        finish();

                        ToastMessage.onToast(GiftCardActivity.this, "GiftCard Code Added Succesfully", ToastMessage.SUCCESS);

                    } else if (statuscode == 422) {
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                        ToastMessage.onToast(getApplicationContext(), "The Gift Card you entered was in progress or applied", ToastMessage.ERROR);

                    } else {
                        dialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    Log.e("on fails", t.toString());
                    dialog.dismiss();
                    // ToastMessage.onToast(getApplicationContext(), "We have some issue", ToastMessage.ERROR);
                }
            });
        }

    }


}
