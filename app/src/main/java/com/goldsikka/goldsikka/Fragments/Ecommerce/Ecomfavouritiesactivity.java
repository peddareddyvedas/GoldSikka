package com.goldsikka.goldsikka.Fragments.Ecommerce;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Activitys.AddBankDetailsForReferral;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.CheckoutModel;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.Sizes;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ecomfavouritiesactivity extends AppCompatActivity {
    RecyclerView ecomwishRecyclerview;
    EcommWishAdapter ecomwishAdapter;
    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;
    shared_preference sharedPreference;
    int selectedPosition = -1;
    private List<Listmodel> wislistList = new ArrayList<>();
    private Handler hdlr = new Handler();
    private int i = 0;
    List<Listmodel> flist;
    Listmodel plist;
    ApiDao apiDao;
    SwipeRefreshLayout swipe_layout;
    RelativeLayout notfound;
    String ss = "";
    ArrayList<String> ssss = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecomwislist_activity);
        init();
        onrefresh();
        getWishlistData();
        flist = new ArrayList<>();

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
                        if (!NetworkUtils.isConnected(Ecomfavouritiesactivity.this)) {
                            ToastMessage.onToast(Ecomfavouritiesactivity.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                            swipe_layout.setRefreshing(false);
                            return;
                        } else {
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

        notfound = findViewById(R.id.notfound);

        ecomwishRecyclerview = findViewById(R.id.ecomwishlist_recyclerview);

        ecomwishRecyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ecomwishAdapter = new EcommWishAdapter(wislistList, ssss);
        ecomwishRecyclerview.setHasFixedSize(false);

        ecomwishRecyclerview.setAdapter(ecomwishAdapter);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public void getWishlistData() {
        wislistList.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<List<Listmodel>> getWishlist = apiDao.getEcomFavvouritelist("Bearer " + AccountUtils.getAccessToken(this));
        getWishlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("favoustatuscode", String.valueOf(statuscode));
//                assert response.body() != null;
                flist = response.body();


                dialog.dismiss();
                if (statuscode == 200 || statuscode == 202) {
                    if (!flist.isEmpty()) {
                        for (Listmodel listmodel : flist) {
                            wislistList.clear();
                            notfound.setVisibility(View.GONE);
                            wislistList.addAll(flist);
                            ecomwishRecyclerview.getRecycledViewPool().clear();
                            ecomwishAdapter.notifyDataSetChanged();
                        }

                    } else {
                        notfound.setVisibility(View.VISIBLE);

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

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }


    //// main adapter/////
    private class EcommWishAdapter extends RecyclerView.Adapter<EcommWishAdapter.ViewHolder> {
        private List<Listmodel> wisList;
        List<String> weilist;

        public EcommWishAdapter(List<Listmodel> moviesList, ArrayList<String> wislistweight) {
            this.wisList = moviesList;
            this.weilist = wislistweight;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ecomwislistitems, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
            Log.e("itemName", " " + position);
            Listmodel w = wisList.get(position);
            viewHolder.pnameTv.setText(w.getPname());
//            viewHolder.pweightTv.setText(favweight + " gms");
            viewHolder.totalwhisprice.setText("₹" + w.getPrice());
            String pimg = w.getImage_uri();
            viewHolder.pid = w.getPid();
            Log.e("pidddddd", "" + w.getPid());
            Log.e("pidddddd", "" + w.getId());
            String getproductsize = w.getSize();
            Log.e("getproductsize", "" + getproductsize);

            for (int i = 0; i < w.getSizes().size(); i++) {
                Log.e("favouriteweight", "" + w.getSizes().get(i).getWeight());
                ss = w.getSizes().get(i).getWeight();
                Log.e("favweightss", "" + ss);
                ssss.add(ss);
            }
            viewHolder.pweightTv.setText(ss + " gms");
            try {
                Glide.with(getApplicationContext()).load(pimg).into(viewHolder.pimg);
            } catch (Exception ignored) {
                Glide.with(getApplicationContext()).load(R.drawable.background_image).into(viewHolder.pimg);
            }

            if (selectedPosition == position) {
                Log.e("ifnews", "called");
            } else {
                Log.e("elsenews", "called");

            }
            viewHolder.ewdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog(viewHolder, w.getPname(), w.getPid());
                }
            });
            viewHolder.pimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  Intent intent = new Intent(Ecomfavouritiesactivity.this, Ecommproductdiscreption.class);
                    intent.putExtra("id", viewHolder.pid);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) Ecomfavouritiesactivity.this,
                            viewHolder.pimg,
                            "pimgtransition");

                    ActivityCompat.startActivity(Ecomfavouritiesactivity.this, intent, options.toBundle());
                    Log.e("id", viewHolder.pid);*/
                }
            });
            viewHolder.wishaddtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postmovetocart(w.getId(), getproductsize);
                }
            });
        }


        @Override
        public int getItemCount() {
            if (wisList.size() > 0) {
                notfound.setVisibility(View.GONE);
                return wisList.size();
            } else {
                notfound.setVisibility(View.VISIBLE);
                return 0;
            }

            // return wisList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView pnameTv, pweightTv, totalwhisprice;
            ImageView pimg, ewdelete;
            String pid = "24";
            RelativeLayout wishaddtocart;

            @SuppressLint("SetTextI18n")
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                pnameTv = itemView.findViewById(R.id.pname);
                pweightTv = itemView.findViewById(R.id.pweightTv);
                pimg = itemView.findViewById(R.id.pimg);
                ewdelete = itemView.findViewById(R.id.ewdelete);
                totalwhisprice = itemView.findViewById(R.id.totalwhisprice);
                wishaddtocart = itemView.findViewById(R.id.movetocartrll);
            }
        }
    }

    private void alertDialog(EcommWishAdapter.ViewHolder viewHolder, String pname, String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Ecomfavouritiesactivity.this);
        builder.setMessage("Are you sure want to delete this product " + pname);
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

    @SuppressLint("UseCompatLoadingForDrawables")
    public void deleteproductfromwishlist(DialogInterface dialog, EcommWishAdapter.ViewHolder holder, String pid) {
        Log.e("token", AccountUtils.getAccessToken(Ecomfavouritiesactivity.this));
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(Ecomfavouritiesactivity.this)).create(ApiDao.class);
        Call<Listmodel> deletewishlist = apiDao.deletefavourite("Bearer " + AccountUtils.getAccessToken(Ecomfavouritiesactivity.this), pid);
        deletewishlist.enqueue(new Callback<Listmodel>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<Listmodel> call, @NotNull Response<Listmodel> response) {
                int statuscode = response.code();
                Log.e("poststatuscode", String.valueOf(statuscode));
                Log.e("poststatuscodeerror", String.valueOf(response));
//                    dialog.dismiss();
                if (statuscode == 200 || statuscode == 201 || statuscode == 202) {
                    ecomwishRecyclerview.getRecycledViewPool().clear();
                    ecomwishAdapter.notifyDataSetChanged();
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

    private void postmovetocart(String id, String psize) {
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(Ecomfavouritiesactivity.this)).create(ApiDao.class);
        Call<Listmodel> postmovecart = apiDao.postEcomfavouritiestocart("Bearer " + AccountUtils.getAccessToken(this), id, psize);
        postmovecart.enqueue(new Callback<Listmodel>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<Listmodel> call, @NotNull Response<Listmodel> response) {
                int statuscode = response.code();
                Log.e("ccccccstatuscode", String.valueOf(statuscode));
                plist = response.body();
                if (statuscode == 200 || statuscode == 202) {
                    Log.e("cccccstatuscode", String.valueOf(statuscode));
                    getWishlistData();
                    Intent intent = new Intent(getApplicationContext(), EcommCartActivity.class);
                    startActivity(intent);
                } else if (statuscode == 404) {
                    Toast.makeText(Ecomfavouritiesactivity.this, "Product is Already in Cart", Toast.LENGTH_SHORT).show();
                } else if (statuscode == 422) {
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
}