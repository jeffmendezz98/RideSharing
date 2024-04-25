package edu.uga.cs.ridesharing;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RideRequestPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 2;

    public RideRequestPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RequestRideFragment();
            case 1:
                return new RideOffersFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Request a Ride";
            case 1:
                return "Ride Offers";
            default:
                return null;
        }
    }
}

