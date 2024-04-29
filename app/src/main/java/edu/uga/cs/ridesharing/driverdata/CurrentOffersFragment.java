package edu.uga.cs.ridesharing.driverdata;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import edu.uga.cs.ridesharing.R;

public class CurrentOffersFragment extends Fragment {

    private LinearLayout offersLayout;
    private List<OffersModel> offersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_offers, container, false);

        offersLayout = view.findViewById(R.id.offers_layout);
        offersList = new ArrayList<>();

        // Load offers from Firebase
        loadOffersFromFirebase();

        return view;
    }

    private void loadOffersFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("offers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                offersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OffersModel offer = snapshot.getValue(OffersModel.class);
                    offersList.add(offer);
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

            addressEditText.setText(offer.getDestination());
            dateEditText.setText(offer.getDate());


            // Set onClickListeners for edit, save, and cancel buttons
            editButton.setOnClickListener(v -> {
                addressEditText.setEnabled(true);
                dateEditText.setEnabled(true);
                timeEditText.setEnabled(true);
                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
            });

            saveButton.setOnClickListener(v -> {
                // Save changes to offer
                String newAddress = addressEditText.getText().toString().trim();
                String newDate = dateEditText.getText().toString().trim();
                String newTime = timeEditText.getText().toString().trim();

                if (!newAddress.isEmpty() && !newDate.isEmpty() && !newTime.isEmpty()) {
                    offer.setDestination(newAddress);
                    offer.setDate(newDate);


                    DatabaseReference offerRef = FirebaseDatabase.getInstance().getReference("offers").child(String.valueOf(offer.getId()));
                    offerRef.setValue(offer); // Update offer in Firebase
                }

                addressEditText.setEnabled(false);
                dateEditText.setEnabled(false);
                timeEditText.setEnabled(false);
                editButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
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
}


