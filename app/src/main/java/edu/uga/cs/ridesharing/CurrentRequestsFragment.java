package edu.uga.cs.ridesharing;

import android.os.Bundle;
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

public class CurrentRequestsFragment extends Fragment {

    private TextView addressTextView;
    private TextView dateTextView;
    private TextView timeTextView;
    private Button editButton;
    private Button saveButton;
    private Button cancelButton;

    public CurrentRequestsFragment() {
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
        cancelButton.setOnClickListener(v -> cancelRequest());

        // Fetch and display the request details from the database
        Bundle bundle = getArguments();
        if (bundle != null) {
            int requestId = bundle.getInt("requestId");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("requests").child(String.valueOf(requestId));
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
                    Toast.makeText(getContext(), "Failed to fetch request details", Toast.LENGTH_SHORT).show();
                }
            });
        }

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
        updateRequestData(newAddress, newDate, newTime);

        // Disable editing mode
        addressTextView.setEnabled(false);
        dateTextView.setEnabled(false);
        timeTextView.setEnabled(false);
    }

    private void updateRequestData(String newAddress, String newDate, String newTime) {
        // Get the request ID from arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            int requestId = bundle.getInt("requestId");
            // Perform database update operation using requestId and new data
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("requests").child(String.valueOf(requestId));
            databaseReference.child("destination").setValue(newAddress);
            databaseReference.child("date").setValue(newDate);
            databaseReference.child("time").setValue(newTime);
        }
    }

    private void cancelRequest() {
        // Get the request ID from arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            int requestId = bundle.getInt("requestId");
            // Delete the request from the database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("requests").child(String.valueOf(requestId));
            databaseReference.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Replace the CurrentRequestsFragment with the RequestRideFragment
                    RequestRideFragment requestRideFragment = new RequestRideFragment();
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, requestRideFragment)
                            .commit();
                } else {
                    // Handle deletion failure
                    Toast.makeText(getContext(), "Failed to cancel the request", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
