package com.goldsikka.goldsikka.Activitys.MoneyWallet.Adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.goldsikka.goldsikka.Activitys.Events.Fragments.CreatedEventList;
import com.goldsikka.goldsikka.Activitys.Events.Fragments.ReceivedEventList;
import com.goldsikka.goldsikka.Activitys.MoneyWallet.Fragments.AddMoney;
import com.goldsikka.goldsikka.Activitys.MoneyWallet.Fragments.WalletTransctions;

public class WalletAddAdapter extends FragmentPagerAdapter {

    public WalletAddAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AddMoney();
            case 1:
                return new WalletTransctions();
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
                return "Add Money";
            case 1:
                return "Transactions";

        }
        return null;
    }



}