package com.goldsikka.goldsikka.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;

public class Addresslist_fragment extends Fragment {

    private Activity activity;
    Button addAddress;
TextView unameTv, uidTv ,titleTv;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addresslist,container,false);
        Toolbar tool_bar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(tool_bar);
        unameTv = view.findViewById(R.id.uname);
        uidTv = view.findViewById(R.id.uid);
        titleTv = view.findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(getContext()));
        uidTv.setText(AccountUtils.getCustomerID(getContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Addresslist");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Addresslist");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });
        intilizeviews(view);

        return view;
    }

    private void intilizeviews(View view) {
        addAddress = view.findViewById(R.id.bt_addAddress);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frame_layout,new Add_Address_fragments());
                transaction.commit();

            }
        });
    }
}