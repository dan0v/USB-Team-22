package uk.ac.newcastle.team22.usb.navigation;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class which manages presenting the information in each tour card.
 *
 * @author Daniel Vincent
 * @version 1.0
 */

public class DirectionViewHolder extends AbstractViewHolder {
    public TextView directionText;
    public TextView distanceText;
    public ImageView directionImage;

    public DirectionViewHolder(View view) {
        super(view);
        directionText = (TextView) view.findViewById(R.id.navigation_direction_text);
        distanceText = (TextView) view.findViewById(R.id.navigation_distance_text);
        directionImage = (ImageView) view.findViewById(R.id.navigation_direction_image);
    }
}