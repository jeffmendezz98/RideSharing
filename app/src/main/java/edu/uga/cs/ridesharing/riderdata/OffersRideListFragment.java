package edu.uga.cs.ridesharing.riderdata;

import static edu.uga.cs.ridesharing.riderdata.RequestRideActivity.userId;

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

import edu.uga.cs.ridesharing.DB.OffersModel;
import edu.uga.cs.ridesharing.R;

public class OffersRideListFragment extends Fragment {

    private LinearLayout offersLayout;
    private List<OffersModel> offersList;

    public OffersRideListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_offers, container, false);

        offersLayout = view.findViewById(R.id.offers_list_layout);
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
                //offersList.clear();
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
            View requestView = LayoutInflater.from(getContext()).inflate(R.layout.offers_list_layout, offersLayout, false);

            // Populate request data into the view
            EditText addressEditText = requestView.findViewById(R.id.address_text_view);
            EditText dateEditText = requestView.findViewById(R.id.date_text_view);
            EditText timeEditText = requestView.findViewById(R.id.time_text_view);
            Button acceptButton = requestView.findViewById(R.id.accept_button);

            addressEditText.setText(offer.getDestination());
            dateEditText.setText(offer.getDate());
            //timeEditText.setText(offer.getTime());

            // Set onClickListeners for accept buttons
            acceptButton.setOnClickListener(v -> {
                // Remove offer from the list and database
                DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("offers").child(String.valueOf(offer.getId()));
                requestRef.removeValue(); // Remove request from Firebase database

                // Remove offer item from the layout
                ((ViewGroup) requestView.getParent()).removeView(requestView);
                offersList.remove(offer);
            });


            offersLayout.addView(requestView);
        }
    }
}