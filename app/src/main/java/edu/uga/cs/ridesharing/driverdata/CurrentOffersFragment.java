package edu.uga.cs.ridesharing.driverdata;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import edu.uga.cs.ridesharing.R;

public class CurrentOffersFragment extends Fragment {

    private TextView addressTextView;
    private TextView dateTextView;
    private TextView timeTextView;
    private Button editButton;
    private Button saveButton;
    private Button cancelButton;

    private int offerId;
    private SharedPreferences preferences;
    private static final String OFFER_STATE_PREF = "OfferState";

    public CurrentOffersFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_current_requests, container, false);

        // Initialize views
        addressTextView = rootView.findViewById(R.id.address_text_view);
        dateTextView = rootView.findViewById(R.id.date_text_view);
        timeTextView = rootView.findViewById(R.id.time_text_view);
        editButton = rootView.findViewById(R.id.edit_button);
        saveButton = rootView.findViewById(R.id.save_button);
        cancelButton = rootView.findViewById(R.id.cancel_button);

        // Set click listeners for edit, save, and cancel buttons
        editButton.setOnClickListener(v -> enableEditMode());
        saveButton.setOnClickListener(v -> saveChanges());
        cancelButton.setOnClickListener(v -> cancelOffer());

        // Initialize SharedPreferences
        preferences = requireContext().getSharedPreferences(OFFER_STATE_PREF, Context.MODE_PRIVATE);

        // Fetch and display the offer details from the database
        Bundle bundle = getArguments();
        if (bundle != null) {
            offerId = bundle.getInt("offerId");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("offers").child(String.valueOf(offerId));
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String address = dataSnapshot.child("destination").getValue(String.class);
                        String date = dataSnapshot.child("date").getValue(String.class);
                        String time = dataSnapshot.child("time").getValue(String.class); // Correctly retrieve time data

                        addressTextView.setText(address);
                        dateTextView.setText(date);
                        timeTextView.setText(time);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    Toast.makeText(getContext(), "Failed to fetch offer details", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Restore offer state if available
        restoreOfferState();

        return rootView;
    }

    private void enableEditMode() {
        // Enable editing of text fields
        addressTextView.setEnabled(true);
        dateTextView.setEnabled(true);
        timeTextView.setEnabled(true);
    }

    private void saveChanges() {
        // Save the changes made to the text fields
        String newAddress = addressTextView.getText().toString();
        String newDate = dateTextView.getText().toString();
        String newTime = timeTextView.getText().toString();

        // Update the information in the database
        updateOfferData(newAddress, newDate, newTime);

        // Disable editing mode
        addressTextView.setEnabled(false);
        dateTextView.setEnabled(false);
        timeTextView.setEnabled(false);
    }

    private void updateOfferData(String newAddress, String newDate, String newTime) {
        // Perform database update operation using offerId and new data
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("offers").child(String.valueOf(offerId));
        databaseReference.child("destination").setValue(newAddress);
        databaseReference.child("date").setValue(newDate);
        databaseReference.child("time").setValue(newTime);
    }

    private void cancelOffer() {
        // Delete the offer from the database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("offers").child(String.valueOf(offerId));
        databaseReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Replace the CurrentOffersFragment with the OfferRideFragment
                OfferRideFragment offerRideFragment = new OfferRideFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, offerRideFragment)
                        .commit();
            } else {
                // Handle deletion failure
                Toast.makeText(getContext(), "Failed to cancel the offer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void restoreOfferState() {
        String address = preferences.getString("address", "");
        String date = preferences.getString("date", "");
        String time = preferences.getString("time", "");

        Log.d("Fragment", "Restored Address: " + address);
        Log.d("Fragment", "Restored Date: " + date);
        Log.d("Fragment", "Restored Time: " + time);

        addressTextView.setText(address);
        dateTextView.setText(date);
        timeTextView.setText(time);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore the offerId from the saved instance state
            offerId = savedInstanceState.getInt("offerId");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the offerId to the saved instance state
        outState.putInt("offerId", offerId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveOfferState();
        Log.d("Fragment", "onDestroyView called");
    }

    private void saveOfferState() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("address", addressTextView.getText().toString());
        editor.putString("date", dateTextView.getText().toString());
        editor.putString("time", timeTextView.getText().toString());
        editor.apply();

        Log.d("Fragment", "Offer state saved");
    }
}

