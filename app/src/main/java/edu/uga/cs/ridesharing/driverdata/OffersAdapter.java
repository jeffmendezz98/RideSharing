package edu.uga.cs.ridesharing.driverdata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uga.cs.ridesharing.DB.OffersModel;
import edu.uga.cs.ridesharing.R;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OfferViewHolder> {

    private List<OffersModel> offersList;

    public OffersAdapter(List<OffersModel> offersList) {
        this.offersList = offersList;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_layout, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        OffersModel offer = offersList.get(position);
        holder.addressTextView.setText(offer.getDestination());
        holder.dateTextView.setText(offer.getDate());

        // You can set OnClickListener for buttons here if needed
    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder {

        public TextView addressTextView;
        public TextView dateTextView;
        public TextView timeTextView;
        public Button editButton;
        public Button saveButton;
        public Button cancelButton;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            addressTextView = itemView.findViewById(R.id.address_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
            editButton = itemView.findViewById(R.id.edit_button);
            saveButton = itemView.findViewById(R.id.save_button);
            cancelButton = itemView.findViewById(R.id.cancel_button);
        }
    }
}
