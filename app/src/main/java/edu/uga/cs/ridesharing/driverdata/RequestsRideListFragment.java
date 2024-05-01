package edu.uga.cs.ridesharing.driverdata;

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

import edu.uga.cs.ridesharing.DB.RequestsModel;
import edu.uga.cs.ridesharing.R;

public class RequestsRideListFragment extends Fragment {

    private ViewGroup requestsLayout;
    private List<RequestsModel> requestsList;

    public RequestsRideListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_offers, container, false);

        requestsLayout = view.findViewById(R.id.offers_layout);
        requestsList = new ArrayList<>();

        // Load requests from Firebase for the current user
        loadRequestsFromFirebase();

        return view;
    }

    private void loadRequestsFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("requests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RequestsModel request = snapshot.getValue(RequestsModel.class);
                    requestsList.add(request);
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
        requestsLayout.removeAllViews(); // Clear previous views

        for (RequestsModel request : requestsList) {
            View requestView = LayoutInflater.from(getContext()).inflate(R.layout.accept_layout, requestsLayout, false);

            // Populate request data into the view
            TextView nameTextView = requestView.findViewById(R.id.textViewUserId);
            TextView destinationTextView = requestView.findViewById(R.id.textViewDestination);
            TextView dateTextView = requestView.findViewById(R.id.textViewDate);
            Button acceptButton = requestView.findViewById(R.id.buttonAccept);

            nameTextView.setText(String.valueOf(request.getUserID()));
            destinationTextView.setText(request.getDestination());
            dateTextView.setText(request.getDate());

            // Set onClickListener for accept button
            acceptButton.setOnClickListener(v -> {
                // Handle accept button click event
            });

            requestsLayout.addView(requestView);
        }
    }
}
