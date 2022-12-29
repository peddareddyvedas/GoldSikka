package com.goldsikka.goldsikka.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.goldsikka.goldsikka.Adapter.gift_adapter;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.model.giftlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Gift_passbook extends Fragment {
    private Activity activity;
    private ArrayList<giftlist> itemList;
    private gift_adapter adapter;
    JSONArray jsonArray;
    giftlist item;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gift_passbook,container,false);

//        Toolbar toolbar = view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Goldsikka");
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frame_layout, new Buy_Digitalgold());
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });

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
        adapter = new gift_adapter(itemList);
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

        String url = "https://goldsikka.com/api/digitalwallet/user_giftpassbook";

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equals("success")){

                               // itemList.clear();

                                jsonArray = jsonObject.getJSONArray("data");
                                int length = jsonArray.length();
                                for (int index = 0; index < length; index++) {
                                    JSONObject object = jsonArray.getJSONObject(index);
                                    item = new giftlist();
                                    item.setPayment_slipno(object.getString("payment_slipno"));
                                    item.setGold(object.getString("gold"));
                                    item.setTotal_amount(object.getString("total_amount"));
                                    item.setReference(object.getString("reference"));
                                    item.setDate(object.getString("date"));
                                    item.setAmount(object.getString("amount"));

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
                   //     Toast.makeText(activity, "We Have some issues", Toast.LENGTH_SHORT).show();

                    }

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

               // params.put("user_id", AccountUtils.getPrefId(activity));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        //adding the string request to request queue
        requestQueue.add(jsonRequest);

    }

}

