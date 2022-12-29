package com.goldsikka.goldsikka.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.goldsikka.goldsikka.Activitys.BusinessEnquiryForm;
import com.goldsikka.goldsikka.Activitys.CountactUsForm;
import com.goldsikka.goldsikka.Activitys.PurchaseEnquiryForm;

public class EnquirySupportAdapter extends FragmentPagerAdapter {

    public EnquirySupportAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BusinessEnquiryForm();
            case 1:
                return new PurchaseEnquiryForm();
            case 2:
                return new CountactUsForm();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Business Enquiry";
            case 1:
                return "Purchase Enquiry";
            case 2:
                return "Contact Us";

        }
        return null;
    }
}