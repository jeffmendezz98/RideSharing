package edu.uga.cs.ridesharing.driverdata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.uga.cs.ridesharing.driverdata.RideRequestsFragment;
import edu.uga.cs.ridesharing.driverdata.OfferRideFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_TABS = 2;

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, int behavior) {
        super(fragmentManager, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Return the fragment based on the position
        switch (position) {
            case 0:
                return new OfferRideFragment(); // First tab: Offer a Ride
            case 1:
                return new RideRequestsFragment(); // Second tab: Ride Requests
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Return the total number of fragments
        return NUM_TABS;
    }

    // Optional: Implement this method if you need tab titles
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        // Return the title of the tab based on the position
        switch (position) {
            case 0:
                return "Offer a Ride";
            case 1:
                return "Ride Requests";
            default:
                return null;
        }
    }
}

