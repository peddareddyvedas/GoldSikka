package com.goldsikka.goldsikka.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.goldsikka.goldsikka.Adapter.digital_wallet_content_adatper;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.model.dw_contentlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class mygold_features extends Fragment {

    private ArrayList<dw_contentlist> itemList;
    private digital_wallet_content_adatper adapter;
    private Activity activity;
    JSONArray jsonArray;
    String tearms_condition;
    dw_contentlist item;
TextView unameTv, uidTv;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.digital_wallet_feature,container,false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Goldsikka");
        unameTv = view.findViewById(R.id.uname);
        uidTv = view.findViewById(R.id.uid);

        unameTv.setText(AccountUtils.getName(getContext()));
        uidTv.setText(AccountUtils.getCustomerID(getContext()));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new MyGold2020_ContentFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        initlizerecyclerview(view);
        return view;
    }

    public void initlizerecyclerview(View view){
        RecyclerView rvRequest = view.findViewById(R.id.rv_list);
        rvRequest.setHasFixedSize(true);
        rvRequest.setLayoutManager(new LinearLayoutManager(activity));
        rvRequest.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                activity, LinearLayoutManager.VERTICAL);
        rvRequest.addItemDecoration(decoration);

        itemList = new ArrayList<>();
        adapter = new digital_wallet_content_adatper(itemList);
        rvRequest.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getdata();

    }

    public void getdata() {

        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait.....");
        dialog.setCancelable(false);
        dialog.show();

        String url = "https://goldsikka.com/mygold2020content";

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String mainheading = jsonObject.getString("mainheading");
                            String content1 = jsonObject.getString("content1");
                            String content2 = jsonObject.getString("content2");
                            String content3 = jsonObject.getString("content3");
                            String content4 = jsonObject.getString("content4");

                            dialog.dismiss();
                            itemList.clear();

                            JSONArray jsonArray = jsonObject.getJSONArray("features");
                            int length = jsonArray.length();
                            for (int index = 0; index < length; index++) {
                                JSONObject object = jsonArray.getJSONObject(index);
                                dw_contentlist item = new dw_contentlist();
                                item.setHeading(object.getString("heading"));
                                item.setSubheading(object.getString("subheading"));
                                item.setId(object.getString("gf_id"));
                                item.setType(object.getString("type"));
                                itemList.add(item);
                                adapter.notifyDataSetChanged();
                                dialog.cancel();
                            }

                            JSONArray jsonArray1 = jsonObject.getJSONArray("faqs");
                            int length1 = jsonArray1.length();
                            for (int index = 0; index < length1; index++) {
                                JSONObject object = jsonArray1.getJSONObject(index);
                                dw_contentlist item = new dw_contentlist();
                                item.setHeading(object.getString("heading"));
                                item.setSubheading(object.getString("subheading"));
                                item.setId(object.getString("gf_id"));
                                item.setType(object.getString("type"));
                                //    itemList.add(item);
                                //   adapter.notifyDataSetChanged();
                              //  dialog.cancel();
                            }

//                            } else {
//                                dialog.dismiss();
//                                Toast.makeText(MainActivity.this, "Please Check Your fields", Toast.LENGTH_SHORT).show();
//                            }

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
                      //  Toast.makeText(activity, "We Have some issues", Toast.LENGTH_SHORT).show();

                    }

                });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        //adding the string request to request queue
        requestQueue.add(jsonRequest);
    }

}
