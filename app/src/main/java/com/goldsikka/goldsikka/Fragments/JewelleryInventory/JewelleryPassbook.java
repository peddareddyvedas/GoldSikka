package com.goldsikka.goldsikka.Fragments.JewelleryInventory;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Fragments.JewelleryInventory.Adapters.AllProductsAdapter;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JewelleryPassbook extends AppCompatActivity implements OnItemClickListener, View.OnClickListener {

    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;
    shared_preference sharedPreference;
    RecyclerView transactionsrv;
    AllJewelleryPassbook allJewelleryPassbook;
    ArrayList<Listmodel> passbooklist;
    ApiDao apiDao, apiDao1;
    Bundle bundle;

    List<Listmodel> flist;
    String pid="sdfsd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jewellery_passbook);

        getSupportActionBar().hide();
        bundle = getIntent().getExtras();

        try{
            pid = bundle.getString("pid");
        }catch (Exception e){
            Log.e("sdvd", "sdvd");
        }

        init();
        intilizerecyclerview();
        getTransactionsByPid(pid);



    }

    @SuppressLint("SetTextI18n")
    public void init() {
        sharedPreference = new shared_preference(getApplicationContext());
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Passbook");
//        notfound = findViewById(R.id.notfound);
        backbtn = findViewById(R.id.backbtn);
//        searchtext = findViewById(R.id.searchtext);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void intilizerecyclerview() {
//        swipe_layout = findViewById(R.id.swipe_layout);
        transactionsrv = findViewById(R.id.transactionsrv);
        transactionsrv.setHasFixedSize(true);
        transactionsrv.setLayoutManager(new LinearLayoutManager(this));
        passbooklist = new ArrayList<>();
        allJewelleryPassbook = new AllJewelleryPassbook(passbooklist);
        transactionsrv.setAdapter(allJewelleryPassbook);
    }

    public void getTransactionsByPid(String pid) {
        passbooklist.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<List<Listmodel>> getWishlist = apiDao.getJewelleryPassbook("Bearer "+AccountUtils.getAccessToken(this), pid);
        getWishlist.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("statuscode", String.valueOf(statuscode));
//                assert response.body() != null;
                flist =  response.body();
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("statuscode", String.valueOf(statuscode));

                    if (!flist.isEmpty()) {
//                        wishRecyclerview.setVisibility(View.VISIBLE);
//                        notxt.setVisibility(View.GONE);
//                        lottieAnimationView.setVisibility(View.GONE);
//                        wislistList2.clear();
                        passbooklist.addAll(flist);
//                        Collections.reverse(wislistList2);
//                        wishAdapter = new WishAdapter(wislistList2);
//                        mBusinessAdapter.notifyDataSetChanged();
                        allJewelleryPassbook.notifyDataSetChanged();

                    } else {
                        Log.e("catname", "No cats");
//                        wishRecyclerview.setVisibility(View.GONE);
//                        notxt.setVisibility(View.VISIBLE);
//                        lottieAnimationView.setVisibility(View.VISIBLE);
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
        super.onBackPressed();
        // finish();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    //// main adapter/////
    private class AllJewelleryPassbook extends RecyclerView.Adapter<JewelleryPassbook.AllJewelleryPassbook.ViewHolder> {
        private List<Listmodel> wisList;

        public AllJewelleryPassbook(List<Listmodel> moviesList) {
            this.wisList = moviesList;
        }

        @Override
        public JewelleryPassbook.AllJewelleryPassbook.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.accimulateitemlist, viewGroup, false);
            return new JewelleryPassbook.AllJewelleryPassbook.ViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onBindViewHolder(@NonNull final JewelleryPassbook.AllJewelleryPassbook.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
            Log.e("itemName", " " + position);

            Listmodel w = wisList.get(position);
            viewHolder.tid.setText("Transaction Id: "+w.getId());
            viewHolder.pass_date.setText("Date: "+w.getCreated_at());
            viewHolder.pass_grams.setText("- "+w.getGoldtransfer()+" g");
            viewHolder.pass_amount.setText("₹"+w.getTotal_amount());
            viewHolder.pass_desc.setText(w.getDescription());
//            viewHolder.pvaTv.setText(w.getPva() + " %");
//            viewHolder.pweightTv.setText(w.getPweight() + " gms");
//            String pimg = w.getPimg();
//            try {
//                Glide.with(getApplicationContext()).load(pimg).into(viewHolder.pimg);
//            } catch (Exception ignored) {
//                Glide.with(getApplicationContext()).load(R.drawable.background_image).into(viewHolder.pimg);
//            }



        }

        @Override
        public int getItemCount() {
            return wisList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tid,pass_date, pass_amount, pass_grams, pass_desc;

            @SuppressLint("SetTextI18n")
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tid = itemView.findViewById(R.id.tid);
                pass_date = itemView.findViewById(R.id.pass_date);
                pass_amount = itemView.findViewById(R.id.pass_amount);
                pass_grams = itemView.findViewById(R.id.pass_grams);
                pass_desc = itemView.findViewById(R.id.pass_desc);

            }
        }

    }

}