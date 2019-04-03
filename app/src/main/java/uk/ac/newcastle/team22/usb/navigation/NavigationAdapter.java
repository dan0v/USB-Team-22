package uk.ac.newcastle.team22.usb.navigation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class to define navigation directions.
 *
 * @author Daniel Vincent
 * @version 1.0
 */

public class NavigationAdapter extends RecyclerView.Adapter<GeneralViewHolder> {
    private List<Object> cardList;

    public NavigationAdapter (List<Object> cardList) {
        this.cardList = cardList;
    }

    public GeneralViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        GeneralViewHolder holder;
        View itemView;
        Context context = viewGroup.getContext();

        if (viewType == 0) {
            itemView = LayoutInflater.from(context).inflate(R.layout.direction_card_view, viewGroup, false);

            holder = new DirectionViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(context).inflate(R.layout.tour_card_view, viewGroup, false);
            holder = new TourViewHolder(itemView);
        }

        return holder;
    }

    public void onBindViewHolder(GeneralViewHolder viewHolder, int position) {
        if(getItemViewType(position) == 0) {
            DirectionViewHolder updatingHolder = (DirectionViewHolder) viewHolder;
            DirectionCardData item = (DirectionCardData) cardList.get(position);
            updatingHolder.directionText.setText(item.getDirectionText());
            updatingHolder.distanceText.setText(item.getDistanceText());
            updatingHolder.directionImage.setImageResource(item.getDirectionImage());
        }
        else {
            TourViewHolder updatingHolder = (TourViewHolder) viewHolder;
            TourCardData item = (TourCardData) cardList.get(position);
            updatingHolder.nameText.setText(item.getNameText());
            updatingHolder.descriptionText.setText(item.getDescriptionText());
            updatingHolder.image.setImageResource(item.getImage());
        }
    }

    public int getItemViewType (int position) {
        if(cardList.get(position).getClass().equals(DirectionCardData.class))
            return 0;
        return 1;
    }

    public int getItemCount() {
        return cardList.size();
    }
}
