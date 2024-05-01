package edu.uga.cs.ridesharing.riderdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.uga.cs.ridesharing.DB.OffersModel;
import edu.uga.cs.ridesharing.R;

public class OffersModelAdapter extends ArrayAdapter<OffersModel> {

    private Context mContext;
    private List<OffersModel> mOffersList;

    public OffersModelAdapter(Context context, List<OffersModel> offersList) {
        super(context, 0, offersList);
        mContext = context;
        mOffersList = offersList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.offers_layout, parent, false);
        }

        // Get the current offer object
        OffersModel currentOffer = mOffersList.get(position);

        // Find TextViews in the offer_list_item layout
        TextView addressTextView = listItem.findViewById(R.id.address_text_view);
        TextView dateTextView = listItem.findViewById(R.id.date_text_view);
        TextView timeTextView = listItem.findViewById(R.id.time_text_view);

        // Set the text for each TextView
        addressTextView.setText(currentOffer.getDestination());
        dateTextView.setText(currentOffer.getDate());

        return listItem;
    }
}
