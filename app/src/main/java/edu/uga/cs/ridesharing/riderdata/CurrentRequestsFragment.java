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

import edu.uga.cs.ridesharing.DB.RequestsModel;
import edu.uga.cs.ridesharing.R;

public class CurrentRequestsFragment extends Fragment {

    private LinearLayout requestsLayout;
    private List<RequestsModel> requestsList;

    public CurrentRequestsFragment() {
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
                    if (request.getUserID() == userId) {
                        requestsList.add(request);
                    }
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
            View requestView = LayoutInflater.from(getContext()).inflate(R.layout.offers_layout, requestsLayout, false);

            // Populate request data into the view
            EditText addressEditText = requestView.findViewById(R.id.address_text_view);
            EditText dateEditText = requestView.findViewById(R.id.date_text_view);
            EditText timeEditText = requestView.findViewById(R.id.time_text_view);
            Button editButton = requestView.findViewById(R.id.edit_button);
            Button saveButton = requestView.findViewById(R.id.save_button);
            Button cancelButton = requestView.findViewById(R.id.cancel_button);

            addressEditText.setText(request.getDestination());
            dateEditText.setText(request.getDate());

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
                // Save changes to request
                String newAddress = addressEditText.getText().toString().trim();
                String newDate = dateEditText.getText().toString().trim();
                String newTime = timeEditText.getText().toString().trim();

                if (!newAddress.isEmpty() && !newDate.isEmpty() && !newTime.isEmpty()) {
                    request.setDestination(newAddress);
                    request.setDate(newDate);

                    DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("requests").child(String.valueOf(request.getId()));
                    requestRef.setValue(request); // Update request in Firebase
                }

                addressEditText.setEnabled(false);
                dateEditText.setEnabled(false);
                timeEditText.setEnabled(false);
                editButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
            });

            cancelButton.setOnClickListener(v -> {
                // Remove request from the list and database
                DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("requests").child(String.valueOf(request.getId()));
                requestRef.removeValue(); // Remove request from Firebase database

                // Remove request item from the layout
                ((ViewGroup) requestView.getParent()).removeView(requestView);
                requestsList.remove(request);
            });

            requestsLayout.addView(requestView);
        }
    }
}

