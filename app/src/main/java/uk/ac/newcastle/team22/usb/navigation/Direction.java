package uk.ac.newcastle.team22.usb.navigation;

import android.support.annotation.StringRes;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class to define navigation directions
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public enum Direction {
    FORWARD, LEFT, RIGHT, LIFTUP, LIFTDOWN, STAIRUP, STAIRDOWN;

    public @StringRes int getLocalisedDirection()
    {
        switch (this) {
            case FORWARD:   return R.string.directionForward;
            case LEFT:  return R.string.directionLeft;
            case RIGHT: return R.string.directionRight;
            case LIFTUP:    return R.string.directionLiftUp;
            case LIFTDOWN:  return R.string.directionLiftDown;
            case STAIRUP:    return R.string.directionStairUp;
            case STAIRDOWN:  return R.string.directionStairDown;
            default:    return 0;
        }
    }
}
