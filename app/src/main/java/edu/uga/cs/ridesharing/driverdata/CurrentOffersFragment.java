package edu.uga.cs.ridesharing.driverdata;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.ridesharing.DB.OffersModel;
import edu.uga.cs.ridesharing.R;
import edu.uga.cs.ridesharing.driverdata.OfferRideActivity;

public class CurrentOffersFragment extends Fragment {

    private LinearLayout offersLayout;
    private List<OffersModel> offersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_offers, container, false);

        // Retrieve userId directly from OfferRideActivity
        int userId = OfferRideActivity.userId;

        offersLayout = view.findViewById(R.id.offers_layout);
        offersList = new ArrayList<>();

        // Load offers from Firebase for the current user
        loadOffersFromFirebase(userId);

        return view;
    }

    private void loadOffersFromFirebase(int userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("offers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                offersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OffersModel offer = snapshot.getValue(OffersModel.class);
                    if (offer.getUserID() == userId) {
                        offersList.add(offer);
                    }
                }
                displayOffers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void displayOffers() {
        offersLayout.removeAllViews(); // Clear previous views

        for (OffersModel offer : offersList) {
            View offerView = LayoutInflater.from(getContext()).inflate(R.layout.offers_layout, offersLayout, false);

            // Populate offer data into the view
            EditText addressEditText = offerView.findViewById(R.id.address_text_view);
            EditText dateEditText = offerView.findViewById(R.id.date_text_view);
            EditText timeEditText = offerView.findViewById(R.id.time_text_view);
            Button editButton = offerView.findViewById(R.id.edit_button);
            Button saveButton = offerView.findViewById(R.id.save_button);
            Button cancelButton = offerView.findViewById(R.id.cancel_button);
            TextView statusTextView = offerView.findViewById(R.id.status_text_view); // Assuming you have a TextView to display the status

            addressEditText.setText(offer.getDestination());
            dateEditText.setText(offer.getDate());

            Boolean toggle = offer.getHasNotBeenAccepted();
            if (!toggle) {
                // If offer has been accepted, hide buttons and show status
                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                statusTextView.setVisibility(View.VISIBLE);
                statusTextView.setText("Ride has been accepted");
            } else {
                // If offer has not been accepted, enable buttons
                editButton.setEnabled(true);
                saveButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
            }

            // Set onClickListeners for edit, save, and cancel buttons
            editButton.setOnClickListener(v -> {
                // Enable editing
                addressEditText.setEnabled(true);
                dateEditText.setEnabled(true);
                timeEditText.setEnabled(true);
            });

            saveButton.setOnClickListener(v -> {
                // Save changes to offer
                String newAddress = addressEditText.getText().toString().trim();
                String newDate = dateEditText.getText().toString().trim();

                if (!newAddress.isEmpty() && !newDate.isEmpty()) {
                    offer.setDestination(newAddress);
                    offer.setDate(newDate);
                    DatabaseReference offerRef = FirebaseDatabase.getInstance().getReference("offers").child(String.valueOf(offer.getId()));
                    offerRef.setValue(offer); // Update offer in Firebase
                }

                // Disable editing
                addressEditText.setEnabled(false);
                dateEditText.setEnabled(false);
                timeEditText.setEnabled(false);
            });

            cancelButton.setOnClickListener(v -> {
                // Remove offer from the list and database
                DatabaseReference offerRef = FirebaseDatabase.getInstance().getReference("offers").child(String.valueOf(offer.getId()));
                offerRef.removeValue(); // Remove offer from Firebase database

                // Remove offer item from the layout
                ((ViewGroup) offerView.getParent()).removeView(offerView);
                offersList.remove(offer);
            });

            offersLayout.addView(offerView);
        }
    }

    private void acceptOffer(int offerId) {
        DatabaseReference offerRef = FirebaseDatabase.getInstance().getReference("offers").child(String.valueOf(offerId));
        offerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OffersModel offer = dataSnapshot.getValue(OffersModel.class);
                if (offer != null) {
                    // Update the hasNotBeenAccepted field to false
                    offer.setHasNotBeenAccepted(false);
                    offerRef.setValue(offer); // Update offer in Firebase
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}

