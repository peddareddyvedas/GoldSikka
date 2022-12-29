package com.goldsikka.goldsikka.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.razorpay.PaymentResultListener;

public class Mygold_PurchasePaymentDetails_fragment extends Fragment implements View.OnClickListener, PaymentResultListener {

    private Activity activity;
    Button btn_buygold;
    TextView  tv_amount,tv_gst1,tv_purchasegold,tv_processing_fee,tv_company,tv_gst2,tv_goldvalue,tv_finalamount,tv_finalgold,tv_mmi_amount;
    Spinner spin_tennure,spin_gbpcycle;
    String[] gbp_cycledate = {"--Select Date--","10th of each month"};
    double liveprice = 5296.00;
TextView unameTv, uidTv, titleTv;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mygold_purchasepayment, container, false);

        double three = liveprice/3,six = liveprice/6,twelve = liveprice/12,eighteen = liveprice/18,twentyfour = liveprice/24;
        String[] tennure_months = {"3 Months(Minimum amount Rs."+Math.round(three)+")","6 Months(Minimum amount Rs."+Math.round(six)+")","12 Months(Minimum amount Rs."+Math.round(twelve)+")","18 Months(Minimum amount Rs."+Math.round(eighteen)+")","24 Months(Minimum amount Rs."+Math.round(twentyfour)+")"};


        spin_tennure = view.findViewById(R.id.sp_mmi_tennure);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, tennure_months);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spin_tennure.setAdapter(adapter);

        spin_gbpcycle = view.findViewById(R.id.sp_gpb_cycle);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, gbp_cycledate);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spin_gbpcycle.setAdapter(adapter2);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Gold 2020");
        unameTv = view.findViewById(R.id.uname);
        uidTv = view.findViewById(R.id.uid);
        titleTv = view.findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(getContext()));
        uidTv.setText(AccountUtils.getCustomerID(getContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("My Gold 2020");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new Mygold_NomineeDetails_fragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        initlizeviews(view);
        return view;

    }

    private void initlizeviews(View view) {
        tv_amount = view.findViewById(R.id.amount);
        tv_gst1 = view.findViewById(R.id.gst1);
        tv_purchasegold = view.findViewById(R.id.p_gold);
        tv_processing_fee = view.findViewById(R.id.prs_fee);
        tv_company = view.findViewById(R.id.cmp_contibution);
        tv_gst2 = view.findViewById(R.id.gst2);
        tv_goldvalue = view.findViewById(R.id.gold_value);
        tv_finalamount = view.findViewById(R.id.final_amount);
        tv_finalgold = view.findViewById(R.id.final_gold);
        tv_mmi_amount = view.findViewById(R.id.mmi_amount);

        btn_buygold = view.findViewById(R.id.btn_buy);
        btn_buygold.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPaymentSuccess(String s) {

    }

    @Override
    public void onPaymentError(int i, String s) {

    }
}
