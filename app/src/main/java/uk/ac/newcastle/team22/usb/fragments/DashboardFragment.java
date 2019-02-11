package uk.ac.newcastle.team22.usb.fragments;

import android.os.Bundle;
import android.support.annotation.*;
import android.view.*;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class which represents the dashboard fragment of the Urban Sciences Building.
 * This fragment is the default fragment to be displayed when the application is launched.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class DashboardFragment extends USBFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}