package uk.ac.newcastle.team22.usb.navigation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreApp.AbstractCardData;
import uk.ac.newcastle.team22.usb.coreApp.AbstractViewHolder;

/**
 * A class to define navigation directions.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class NavigationAdapter extends RecyclerView.Adapter<AbstractViewHolder> {
    private List<AbstractCardData> cardList;

    public NavigationAdapter(List<AbstractCardData> cardList) {
        this.cardList = cardList;
    }

    public AbstractViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        AbstractViewHolder holder;
        View itemView;
        Context context = viewGroup.getContext();

        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(context).inflate(R.layout.card_view_direction, viewGroup, false);
                holder = new DirectionViewHolder(itemView);
                break;
            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.card_view_tour, viewGroup, false);
                holder = new TourViewHolder(itemView);
                break;
        }

        return holder;
    }

    public void onBindViewHolder(AbstractViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0: {
                DirectionViewHolder updatingHolder = (DirectionViewHolder) viewHolder;
                DirectionCardData item = (DirectionCardData) cardList.get(position);
                updatingHolder.directionText.setText(item.getDirectionText());
                updatingHolder.distanceText.setText(item.getDistanceText());
                updatingHolder.directionImage.setImageResource(item.getDirectionImage());
                break;
            }
            default: {
                TourViewHolder updatingHolder = (TourViewHolder) viewHolder;
                TourCardData item = (TourCardData) cardList.get(position);
                updatingHolder.nameText.setText(item.getNameText());
                updatingHolder.descriptionText.setText(item.getDescriptionText());
                updatingHolder.image.setImageResource(item.getImage());
                break;
            }
        }
    }

    public int getItemViewType(int position) {
        AbstractCardData card = cardList.get(position);
        if (card instanceof DirectionCardData) {
            return 0;
        } else {
            return 1;
        }
    }

    public int getItemCount() {
        return cardList.size();
    }
}
