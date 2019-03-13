package uk.ac.newcastle.team22.usb.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class which represents the tour fragment of the Urban Sciences Building application.
 * This fragment enables the user to be guided on a tour of the building.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class TourFragment extends Fragment implements USBFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tour, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getTitle() {
        return R.string.tour;
    }
}
