package edu.uga.cs.ridesharing.riderdata;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import edu.uga.cs.ridesharing.DB.RequestsModel;
import edu.uga.cs.ridesharing.R;

public class OffersRideListFragment extends Fragment {

    private ViewGroup offersLayout;
    private ViewGroup acceptOfferPopup;
    private List<OffersModel> offersList;

    public OffersRideListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_offers, container, false);

        offersLayout = view.findViewById(R.id.offers_layout);
        //acceptOfferPopup = view.findViewById(R.id.accept_offer_popup);
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
                displayRequests();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void displayRequests() {
        offersLayout.removeAllViews(); // Clear previous views

        for (OffersModel offer : offersList) {
            View offerView = LayoutInflater.from(getContext()).inflate(R.layout.accept_layout, offersLayout, false);

            // Populate request data into the view
            TextView nameTextView = offerView.findViewById(R.id.textViewUserId);
            TextView destinationTextView = offerView.findViewById(R.id.textViewDestination);
            TextView dateTextView = offerView.findViewById(R.id.textViewDate);
            Button acceptButton = offerView.findViewById(R.id.buttonAccept);

            nameTextView.setText(String.valueOf(offer.getUserID()));
            destinationTextView.setText(offer.getDestination());
            dateTextView.setText(offer.getDate());

            // Set onClickListener for accept button
            acceptButton.setOnClickListener(v -> {
                // Display the popup
                displayPopup(String.valueOf(offer.getId())); // Convert offer ID to String
            });

            offersLayout.addView(offerView);
        }
    }

    private void displayPopup(String offerId) {
        // Inflate the popup layout
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.accept_offer_popup, null);

        // Find views in the popup layout
        Button acceptButton = popupView.findViewById(R.id.acceptButton);
        Button cancelButton = popupView.findViewById(R.id.cancelButton);

        // Set onClickListeners for the buttons
        acceptButton.setOnClickListener(v -> {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("offers");
            databaseReference.addValueEventListener(new ValueEventListener() {
                //re-instantiate offersList
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    offersList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        OffersModel offer = snapshot.getValue(OffersModel.class);
                            offersList.add(offer);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });

            for (OffersModel offer : offersList) {
                /*
                if (offer.getId() == ) {
                    offer.setHasNotBeenAccepted(false);
                    DatabaseReference offerRef = FirebaseDatabase.getInstance().getReference("offers").child(String.valueOf(offer.getId()));
                    offerRef.setValue(offer); // Update offer in Firebase
                }
                */
            }

            // Dismiss the popup
            ViewGroup parentView = (ViewGroup) popupView.getParent();
            if (parentView != null) {
                parentView.removeView(popupView);
            }
        });

        //Cancel Button
        cancelButton.setOnClickListener(v -> {
            // Dismiss the popup
            ViewGroup parentView = (ViewGroup) popupView.getParent();
            if (parentView != null) {
                parentView.removeView(popupView);
            }
        });

        // Add the popup to the offersLayout
        offersLayout.addView(popupView);
    }
}