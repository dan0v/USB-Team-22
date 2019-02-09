package uk.ac.newcastle.team22.usb.fragments;

import android.os.Bundle;
import android.support.annotation.*;
import android.view.*;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class which represents the café fragment.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class CafeFragment extends USBFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cafe, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getTitle() {
        return R.string.cafe;
    }
}
