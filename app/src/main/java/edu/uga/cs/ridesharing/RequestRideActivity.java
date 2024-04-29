package edu.uga.cs.ridesharing;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestRideActivity extends AppCompatActivity {

    private String address;
    private String date;
    private String time;
    private int userId; // Changed type to int
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);

        // Dynamically add toolbar
        Toolbar toolbar = new Toolbar(this);
        toolbar.setTitle("Request Ride");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        addContentView(toolbar, layoutParams);

        // Retrieve current user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid().hashCode();
        } else {
            // Handle the case where the user is not authenticated
        }

        // Find fragment container
        fragmentContainer = findViewById(R.id.fragment_container);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        RideRequestPagerAdapter adapter = new RideRequestPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        fragmentContainer = findViewById(R.id.fragment_container);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {
                    fragmentContainer.setVisibility(View.VISIBLE);
                } else {
                    fragmentContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    public void storeRequestData(String address, String date, String time) {
        this.address = address;
        this.date = date;
        this.time = time;

        // Firebase Database operations
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("requests");
        String requestId = databaseReference.push().getKey(); // Generating a unique key
        int requestIdInt = requestId.hashCode(); // Converting the string key to an integer
        RequestsModel request = new RequestsModel(requestIdInt, date, address, userId); // Using integer key

        databaseReference.child(String.valueOf(requestIdInt)).setValue(request)
                .addOnSuccessListener(aVoid -> {
                    // Submitting ride request successful, replace fragment
                    submitRideRequest(requestIdInt);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RequestRideActivity.this, "Failed to save request data", Toast.LENGTH_SHORT).show();
                });
    }

    private void submitRideRequest(int requestId) {
        // Replace RequestRideFragment with CurrentRequestsFragment
        Bundle bundle = new Bundle();
        bundle.putInt("requestId", requestId);
        CurrentRequestsFragment currentRequestsFragment = new CurrentRequestsFragment();
        currentRequestsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, currentRequestsFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Finish the activity when the back button in the toolbar is pressed
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

