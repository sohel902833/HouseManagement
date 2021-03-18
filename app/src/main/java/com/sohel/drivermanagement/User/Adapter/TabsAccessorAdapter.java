package com.sohel.drivermanagement.User.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sohel.drivermanagement.TransectionDetailsActivity;
import com.sohel.drivermanagement.User.AllTransectionListFragment;
import com.sohel.drivermanagement.User.PersonDueBalanceFragment;

public class TabsAccessorAdapter extends FragmentPagerAdapter {
    public TabsAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                PersonDueBalanceFragment personDueBalance =new PersonDueBalanceFragment();
                return  personDueBalance;
            case  1:
                AllTransectionListFragment transectionFragment=new AllTransectionListFragment();
                return  transectionFragment;
            default:
                    return  null;
        }


    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return  "Due";
            case  1:
                return "Transaction List";
            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
