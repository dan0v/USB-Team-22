package uk.ac.newcastle.team22.usb.fragments;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.ac.newcastle.team22.usb.R;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.MyViewHolder> {

    private Context mContext;
    private List<Object> navigationList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView directionText, distanceText;
        public ImageView directionImage;

        public MyViewHolder(View view) {
            super(view);
            directionText = (TextView) view.findViewById(R.id.navigation_direction_text);
            distanceText = (TextView) view.findViewById(R.id.navigation_distance_text);
            directionImage = (ImageView) view.findViewById(R.id.navigation_direction_image);
        }
    }


    public NavigationAdapter(Context mContext, List<Object> navigationList) {
        this.mContext = mContext;
        this.navigationList = navigationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_card_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Object listLocation = navigationList.get(position);

        if (listLocation.getClass().equals(NavigationDirection.class)) {
            NavigationDirection direction = (NavigationDirection) listLocation;
            holder.directionText.setText(direction.getDirectionText());
            holder.distanceText.setText(direction.getDistanceText());
            holder.directionImage.setImageResource(direction.getDirectionImage());
        }
        else {
            String msg = String.format("The object type in recycler is unknown: %s", listLocation.getClass().toString());
            Log.e("Navigation UI", msg);
        }
    }

    @Override
    public int getItemCount() {
        return navigationList.size();
    }
}
