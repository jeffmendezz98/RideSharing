package edu.uga.cs.ridesharing;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class RequestRideFragment extends Fragment {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private TextInputEditText addressInput;
    private TextInputEditText dateInput;
    private TextInputEditText timeInput;
    private Button submitButton;

    public RequestRideFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_request_ride, container, false);

        // Initialize views
        addressInput = rootView.findViewById(R.id.address_input);
        dateInput = rootView.findViewById(R.id.date_input);
        timeInput = rootView.findViewById(R.id.time_input);
        submitButton = rootView.findViewById(R.id.submit_button);

        // Set click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRideRequest();
            }
        });

        // Set click listener for the address input field
        addressInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPlaceAutocomplete();
            }
        });

        // Set click listener for the date input field
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        // Set click listener for the time input field
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(v);
            }
        });

        return rootView;
    }

    private void submitRideRequest() {
        // Retrieve values from input fields
        String address = addressInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String time = timeInput.getText().toString().trim(); // Ensure that the time value is correctly retrieved

        // Call the method in RequestRideActivity to store the data
        if (getActivity() instanceof RequestRideActivity) {
            ((RequestRideActivity) requireActivity()).storeRequestData(address, date, time);
        }

        // Clear text fields after submission
        clearTextFields();
    }

    private void clearTextFields() {
        addressInput.setText("");
        dateInput.setText("");
        timeInput.setText("");
    }

    public void showDatePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view1, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    dateInput.setText(dateFormat.format(calendar.getTime()));
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void showTimePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                (view1, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    timeInput.setText(timeFormat.format(calendar.getTime()));
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void launchPlaceAutocomplete() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                addressInput.setText(place.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // Handle error
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User canceled the operation
            }
        }
    }
}