package com.goldsikka.goldsikka.Fragments;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;

import com.goldsikka.goldsikka.Activitys.BaseActivity;
import com.goldsikka.goldsikka.Activitys.Passbook_Activity;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Ecommerce_Category_list;
import com.goldsikka.goldsikka.Fragments.Ecommerce.orderslist;
import com.goldsikka.goldsikka.Fragments.NewDesign.Settings;
import com.goldsikka.goldsikka.Fragments.Schemes.Scheme_Content_Fragment;
import com.goldsikka.goldsikka.Fragments.Schemes.Schemes_usersubscribed_list;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;


public class MainFragment extends BaseActivity {

    int parentposition,childposition;
    String scheme_name,id,st_incurrency,st_ingrams;


    @Override
    protected int getLayoutId() {
        return R.layout.frame_toolbar;
    }

    @Override
    protected void initView() {
//        scheme_name = AccountUtils.getSchemename(this);
//        id = AccountUtils.getId(this);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            parentposition = bundle.getInt("parentposition");
            childposition = bundle.getInt("childpositiion");
            scheme_name = bundle.getString("schemename");
            st_ingrams = bundle.getString("st_ingrams");
            st_incurrency = bundle.getString("amount");
            id = bundle.getString("schemeid");
        }




//        else if (parentposition == 5 && childposition == 1) {
//
//            try {
//
//
//
//            }catch (IndexOutOfBoundsException e){
//                Log.e("BoundsException",e.toString());
//            }
//        }
         if (parentposition == 5 && childposition == 2) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.frame_layout, new Settings());
            transaction.commit();
        }










    }

}