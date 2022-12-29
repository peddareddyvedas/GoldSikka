package com.goldsikka.goldsikka.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.model.dw_contentlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MyGold2020_ContentFragment extends Fragment implements View.OnClickListener {

    private Activity activity;

    private ArrayList<dw_contentlist> itemList;
    private digital_wallet_content_adatper adapter;
    TextView tvsubhead,tvcontent1,tvcontent2,tvcontent3,tvcontent4;

    Button btn_buygold;
    TextView tvfeature,tvterms_condition,tvfaqs;
    String terms_conditions;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.mygold2020_design,container,false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Gold 2020");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().onBackPressed();
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });
      //  initlizerecyclerview(view);
        initlizeviews(view);


        return view;
    }
    public void initlizeviews(View view){

        tvsubhead = view.findViewById(R.id.sub_head);
        tvcontent1 = view.findViewById(R.id.tv_content1);
        tvcontent2 = view.findViewById(R.id.tv_content2);
        tvcontent3 = view.findViewById(R.id.tv_content3);
        tvcontent4 = view.findViewById(R.id.tv_content4);

        tvterms_condition = view.findViewById(R.id.dw_terms_condition);
        tvterms_condition.setOnClickListener(this);

        tvfeature = view.findViewById(R.id.tvfeatures);
        tvfeature.setOnClickListener(this);

        tvfaqs = view.findViewById(R.id.mygold_faqs);
        tvfaqs.setOnClickListener(this);

        btn_buygold = view.findViewById(R.id.btn_buygold);
        btn_buygold.setOnClickListener(this);

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
        getcontent();
    }

    public void getcontent(){

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
                            terms_conditions = jsonObject.getString("terms_conditions");

                            tvcontent1.setText(content1);
                            tvcontent2.setText(content2);
                            tvcontent3.setText(content3);
                            tvcontent4.setText(content4);
                         //   tvsubhead.setText(mainheading);

                            dialog.dismiss();

                            JSONArray jsonArray = jsonObject.getJSONArray("features");
                            int length = jsonArray.length();
                            for (int index = 0; index < length; index++) {
                                JSONObject object = jsonArray.getJSONObject(index);
                                dw_contentlist item = new dw_contentlist();
                                item.setHeading(object.getString("heading"));
                                item.setSubheading(object.getString("subheading"));
                                item.setId(object.getString("gf_id"));
                                item.setType(object.getString("type"));
//                                itemList.add(item);
//                                adapter.notifyDataSetChanged();
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
//                                itemList.add(item);
//                                adapter.notifyDataSetChanged();
                                dialog.cancel();
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
                        Toast.makeText(activity, "We Have some issues", Toast.LENGTH_SHORT).show();

                    }

                });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        //adding the string request to request queue
        requestQueue.add(jsonRequest);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_buygold:
                openbuygold();
                return;

            case R.id.tvfeatures:
                openfeature();
                return;

            case R.id.dw_terms_condition:
                openterms_condition();
                return;

            case R.id.mygold_faqs:
                openfaqs();
                return;
        }
    }

    public void openfaqs(){

        mygold_faqs digitalFeature = new mygold_faqs();
        //  digitalFeature.setArguments(bundle1);
        getFragmentManager().beginTransaction().replace(R.id.frame_layout,digitalFeature).commit();
    }
    public void openfeature() {

        //  Bundle bundle1 = new Bundle();
        //  bundle1.putString("features", String.valueOf(item));
        mygold_features digitalFeature = new mygold_features();
        //  digitalFeature.setArguments(bundle1);
        getFragmentManager().beginTransaction().replace(R.id.frame_layout,digitalFeature).commit();

    }

    public void openterms_condition(){
        Bundle bundle1 = new Bundle();
        bundle1.putString("terms_condition", terms_conditions);
        mygold_terms_condition digitaltermsconditiom = new mygold_terms_condition();
        digitaltermsconditiom.setArguments(bundle1);
        getFragmentManager().beginTransaction().replace(R.id.frame_layout,digitaltermsconditiom).commit();

    }

    public void openbuygold(){

//        String uri = String.format(Locale.ENGLISH, "https://goldsikka.com/main/mygold2020");
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//        startActivity(intent);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new Mygold_personalDetails_Fragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

}

