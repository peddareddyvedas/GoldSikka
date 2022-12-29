package com.goldsikka.goldsikka.Activitys.Events.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.goldsikka.goldsikka.Activitys.BusinessEnquiryForm;
import com.goldsikka.goldsikka.Activitys.CountactUsForm;
import com.goldsikka.goldsikka.Activitys.PurchaseEnquiryForm;

public class EventsListAdapter extends FragmentPagerAdapter {

    public EventsListAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CreatedEventList();
            case 1:
                return new ReceivedEventList();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Created Events";
            case 1:
                return "Received Events";

        }
        return null;
    }
}
