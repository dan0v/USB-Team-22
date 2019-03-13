package uk.ac.newcastle.team22.usb.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class which represents the settings fragment of the Urban Sciences Building application.
 * This fragment enables the user to adjust application settings.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class SettingsFragment extends USBFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getTitle() {
        return R.string.settings;
    }
}
