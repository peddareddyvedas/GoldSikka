package com.goldsikka.goldsikka.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goldsikka.goldsikka.Activitys.InvoicePage;
import com.goldsikka.goldsikka.Adapter.buy_adapter;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.interfaces.OnItemClickListener;
import com.goldsikka.goldsikka.model.buy_list;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Buy_fragment extends Fragment implements OnItemClickListener {

    private Activity activity;
    private ArrayList<buy_list> itemList;
    private buy_adapter adapter;
    JSONArray jsonArray;
    buy_list item;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.passbook_design,container,false);

        initlizerecycleview(view);
        return view;
    }

    public void initlizerecycleview(View view){



        RecyclerView rvRequest = view.findViewById(R.id.rv_list);
        rvRequest.setHasFixedSize(true);
        rvRequest.setLayoutManager(new LinearLayoutManager(activity));
        rvRequest.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                activity, LinearLayoutManager.VERTICAL);
        rvRequest.addItemDecoration(decoration);

        itemList = new ArrayList<>();
        adapter = new buy_adapter(itemList,this::onItemClick);
        rvRequest.setAdapter(adapter);

    }
    @Override
    public void onStart() {
        super.onStart();
        getcontent();
    }

    public void getcontent(){

        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait.....");
        dialog.setCancelable(false);
        dialog.show();

        String url = "https://goldsikka.com/api/dwcustomer_passbook";

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equals("success")){

                                itemList.clear();

                            jsonArray = jsonObject.getJSONArray("data");
                            int length = jsonArray.length();
                            for (int index = 0; index < length; index++) {
                                JSONObject object = jsonArray.getJSONObject(index);

                                item = new buy_list();
                                item.setTotal_amount(object.getString("total_amount"));
                                item.setGold(object.getString("gold"));
                                item.setDate(object.getString("date"));
                                item.setReference(object.getString("reference"));
                                item.setTransaction_id(object.getString("payment_slipno"));
                                item.setAmount(object.getString("amount"));
                                item.setGram_value(object.getString("gram_value"));
                                item.setGst(object.getString("gst"));
                                item.setTotalamount(object.getString("totalamount"));

                                itemList.add(item);
                                adapter.notifyDataSetChanged();
                                dialog.cancel();
                            }

                            } else {
                                dialog.dismiss();
                                Toast.makeText(activity, "No Data Available", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                            dialog.dismiss();
                            Toast.makeText(activity, "Please Try again", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(MainActivity.this, "Exception", Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                    //    Toast.makeText(activity, "We Have some issues", Toast.LENGTH_SHORT).show();

                    }

                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();



                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        //adding the string request to request queue
        requestQueue.add(jsonRequest);

    }

    @Override
    public void onItemClick(View view, int position) {
       // Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show();
        item = itemList.get(position);
        Intent in = new Intent(activity, InvoicePage.class);
        in.putExtra("gst",item.getGst());
        in.putExtra("totalamount",item.getTotalamount());
        in.putExtra("amount",item.getAmount());
        in.putExtra("grams",item.getGram_value());
        in.putExtra("date",item.getDate());
        in.putExtra("id",item.getTransaction_id());
        startActivity(in);
    }
}

