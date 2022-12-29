package com.goldsikka.goldsikka.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.goldsikka.goldsikka.Adapter.Address_swippage_adapter;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.google.android.material.tabs.TabLayout;

public class Add_Address_fragments extends Fragment implements TabLayout.OnTabSelectedListener {

    private Activity activity;
    private TabLayout tabLayout;

    private ViewPager viewPager;
TextView unameTv, uidTv, titleTv;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.add_address,container,false);

        Toolbar tool_bar = view.findViewById(R.id.toolbar);
        unameTv = view.findViewById(R.id.uname);
        uidTv = view.findViewById(R.id.uid);
        titleTv = view.findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(getContext()));
        uidTv.setText(AccountUtils.getCustomerID(getContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Address");
        ((AppCompatActivity)getActivity()).setSupportActionBar(tool_bar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Address");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frame_layout,new Addresslist_fragment());
                transaction.commit();
            }
        });

        tabLayout =  view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Manual Entry"));
        tabLayout.addTab(tabLayout.newTab().setText("Google Map"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = view.findViewById(R.id.pager);
        Address_swippage_adapter adapter = new Address_swippage_adapter(getFragmentManager(),
                tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(this);


        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}