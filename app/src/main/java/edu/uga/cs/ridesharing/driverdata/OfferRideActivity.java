package edu.uga.cs.ridesharing.driverdata;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import edu.uga.cs.ridesharing.R;

public class OfferRideActivity extends AppCompatActivity {

    private String address;
    private String date;
    private String time;
    public static int userId; // Changed type to int
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view
        setContentView(R.layout.activity_offer_ride);

        // Dynamically add toolbar
        Toolbar toolbar = new Toolbar(this);
        toolbar.setTitle("Ridesharing");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        addContentView(toolbar, layoutParams);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Changed from getUid() to use hashCode()
            userId = currentUser.getUid().hashCode();
        } else {
            // Handle the case where the user is not authenticated
        }

        // Find fragment container
        fragmentContainer = findViewById(R.id.fragment_container);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void storeOfferData(String address, String date, String time) {
        this.address = address;
        this.date = date;
        this.time = time;

        // Firebase Database operations
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("offers");
        String offerId = databaseReference.push().getKey(); // Generating a unique key
        int offerIdInt = offerId.hashCode(); // Converting the string key to an integer
        OffersModel offer = new OffersModel(offerIdInt, date, address, userId); // Using integer key

        databaseReference.child(String.valueOf(offerIdInt)).setValue(offer)
                .addOnSuccessListener(aVoid -> {
                    // Submitting ride offer successful, show snackbar
                    showSubmissionSuccessSnackbar();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OfferRideActivity.this, "Failed to save offer data", Toast.LENGTH_SHORT).show();
                });
    }

    private void showSubmissionSuccessSnackbar() {
        Snackbar.make(findViewById(android.R.id.content), "Offer submitted successfully", Snackbar.LENGTH_SHORT).show();
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
