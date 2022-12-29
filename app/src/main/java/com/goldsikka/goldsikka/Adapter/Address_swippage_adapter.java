package com.goldsikka.goldsikka.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.goldsikka.goldsikka.Fragments.Manual_Address;
import com.goldsikka.goldsikka.Fragments.googlemap_fragment;

public class Address_swippage_adapter extends FragmentStatePagerAdapter {


    int tabCount;


    public Address_swippage_adapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }


    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Manual_Address tab1 = new Manual_Address();
                return tab1;
            case 1:
                googlemap_fragment tab2 = new googlemap_fragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}