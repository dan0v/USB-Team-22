package uk.ac.newcastle.team22.usb.navigation;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreApp.AbstractViewHolder;

/**
 * A class which manages presenting the information in each tour card.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class TourViewHolder extends AbstractViewHolder {
    public TextView nameText;
    public TextView descriptionText;
    public ImageView image;

    public TourViewHolder(View view) {
        super(view);
        nameText = view.findViewById(R.id.tour_location_name_text);
        descriptionText = view.findViewById(R.id.tour_description_text);
        image = view.findViewById(R.id.tour_location_image);
    }
}
