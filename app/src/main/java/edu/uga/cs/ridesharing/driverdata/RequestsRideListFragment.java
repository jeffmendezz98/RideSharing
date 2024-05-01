package edu.uga.cs.ridesharing.driverdata;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import javax.annotation.Nullable;

import edu.uga.cs.ridesharing.DB.DBHelper;
import edu.uga.cs.ridesharing.DB.RequestsModel;
import edu.uga.cs.ridesharing.R;

public class RequestsRideListFragment extends Fragment {

    private ListView lv_list;
    private ArrayAdapter requestsArrayAdapter;

    public RequestsRideListFragment(){
        // Empty Constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        // Store fragment context on creation
        Context context = getActivity().getApplicationContext();

        // Inflate Layout
        View rootView = inflater.inflate(R.layout.fragment_list_rides_not_used, container, false);

        // Initialize views
        lv_list = rootView.findViewById(R.id.lv_list_requests);


        DBHelper dbHelper = new DBHelper(context);
        requestsArrayAdapter = new ArrayAdapter<RequestsModel>(
                context, android.R.layout.simple_list_item_1, dbHelper.getRequestsList()
        );
        lv_list.setAdapter((requestsArrayAdapter));
        return rootView;
    }
}
